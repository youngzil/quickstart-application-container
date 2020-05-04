package cn.hangzhou.demo.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hangzhou.demo.bean.UserBean;
import cn.hangzhou.demo.service.UserService;
import cn.hangzhou.demo.util.DigestHelper;
import cn.hangzhou.demo.util.SSLUtils;
import net.sf.json.JSONObject;

public class LegalServlet extends HttpServlet {

	private static final long serialVersionUID = -6187964647473231392L;
	
	
	// 测试环境---验证令牌并获取用户的登录信息接口调用地址
	public final String QUERY_URL = "http://essotest.zjzwfw.gov.cn:8080/rest/user/query";
	// 正式环境---验证令牌并获取用户的登录信息接口调用地址
	// public final String QUERY_URL = "https://ssoapi.zjzwfw.gov.cn/rest/user/query";
	// 测试项目ID(公共应用ID),正式环境下贵司将拥有独立的应用ID 
	public final String projectId = "1111564305";
	// 测试项目Secret(公共应用Secret),正式环境下贵司将拥有独立的应用Secret
	public final String projectSecret = "55ea54dac3f497c43344a9904f4aa1ae";

	//获取 ssotoken使用post方法   
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		// 跳转Url
		String redirectUrl = "";
		// ssoticket是跨session用的，在原先链接里面加上&ssoticket=就可以实现跨session登录
		// System.out.println("ssoticket==" +
		// request.getParameter("ssoticket"));

		// 从Request请求的参数中获取ssotoken
		String ssotoken = request.getParameter("ssotoken");
		System.out.println("ssotoken = " + ssotoken);
		
		// 取具体办事事项地址（若此项有值，成功登录后请跳转此地址到具体事项，否则跳转系统首页）
		String gotoUrl = request.getQueryString();
		if (null != gotoUrl && !gotoUrl.trim().equals("")) {
			// 清理事项地址前的“goto=”标识
			gotoUrl = gotoUrl.substring(5);
			System.out.println("具体业务办理地址： " + gotoUrl);
		}

		// 验证令牌并获取用户的登录信息
		JSONObject jsonObj = doQuery(ssotoken, projectId, projectSecret);

		int errCode = jsonObj.getInt("errCode");

		// errCode = 0 表示认证成功
		if (0 == errCode) {
			// 验证成功
			String info = jsonObj.getString("info");
			System.out.println("验证令牌并获取用户的登录信息接口返回数据：" + info);
			
			JSONObject legalInfo = JSONObject.fromObject(info);
			
			// 第三步：
			
			// 企业名称
			String companyName = legalInfo.get("CompanyName").toString();
			if (null != companyName) {
				System.out.println("企业名称 = " + companyName);
			}

			// 工商注册号
			String regNumber = null;
			Object regNumberObj = legalInfo.get("CompanyRegNumber");
			if (null != regNumberObj) {
				regNumber = regNumberObj.toString();
				System.out.println("工商注册号 = " + regNumber);
			}

			// 机构代码号
			String orgNumber = null;
			Object orgNumberObj = legalInfo.get("OrganizationNumber");
			if (null != orgNumberObj) {
				orgNumber = orgNumberObj.toString();
				System.out.println("机构代码号 = " + orgNumber);
			}

			// 统一社会信用代码
			String uniscid = null;
			Object uniscidObj = legalInfo.get("uniscid");
			if (null != uniscidObj) {
				uniscid = uniscidObj.toString();
				System.out.println("统一社会信用代码  = " + uniscid);
			}

			// 用户id，政务服务网账号唯一标识
			String userid = null;
			Object useridObj = legalInfo.get("userId");
			if (null != useridObj) {
				userid = useridObj.toString();
				System.out.println("政务服务网账号唯一标识userId=" + userid);
			}

			// 用户在政务服务网认证等级
			String realLevel = null;
			Object RealLevelObj = legalInfo.get("realLevel");
			if (null != RealLevelObj) {
				realLevel = RealLevelObj.toString();
				System.out.println("用户在政务服务网认证等级RealLevel=" + realLevel);
			}

			// 根据政务服务网账号唯一标识userId关联项在自建系统中查找是否存在此法人的账户信息
			// 本Demo中以9152作为政务服务网账号唯一标识userId，使其关联成功
			// 如果需要演示关联失败跳转到自定义的绑定页面可以传入非9152值
			UserBean userBean = UserService.findUser("9152");

			// 找不到关联用户，表示该法人第一次登陆自建系统，
			// 显示页面由用户选择以此信息绑定自建系统中原有用户或在自建系统中新注册账号，账户与政务服务网账号唯一标识userid绑定
			if (null == userBean) {
				redirectUrl = "bind.html?companyName=" + URLEncoder.encode(companyName, "UTF8") + "&userId="
						+ URLEncoder.encode(userid, "UTF8");
			}

			// 找到了关联用户，显示法人信息
			else {
				// 成功登录时，若gotoUrl有值，先将政务服务网账号唯一标识userid与现有的账号绑定，然后跳转到具体的办事事项，否则跳转系统首页
				redirectUrl = "success.html?companyName=" + URLEncoder.encode(companyName, "UTF8") + "&userId="
						+ URLEncoder.encode(userid, "UTF8");
				// 成功登录后，如果若gotoUrl有值，且将政务服务网账号唯一标识userid与现有的账号绑定，会跳转到具体的办事事项
				// redirectUrl = gotoUrl;
			}
		} else {
			// 验证失败，跳转登录失败的页面
			redirectUrl = "fail.html";
		}
		
		response.setHeader("refresh", "1;URL=" + redirectUrl);
	}

	/***
	 * 验证令牌并获取用户的登录信息
	 * 
	 * @param ssotoken
	 */
	public JSONObject doQuery(String ssotoken, String projectId, String projectSecret) {
		JSONObject param = new JSONObject();
		param.put("token", ssotoken);

		// 获取请求签名值
		String signature = DigestHelper.getSignature(param.toString(), projectSecret);

		// 设置请求的Headers头信息
		LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("x-esso-project-id", projectId);
		headers.put("x-esso-signature", signature);
		headers.put("Content-Type", "application/json");
		headers.put("Charset", "UTF-8");
	
		JSONObject jsonObj = sendPOST(QUERY_URL, param.toString(), headers);
		if (null != jsonObj) {
			int errCode = jsonObj.getInt("errCode");
			String msg = jsonObj.getString("msg");
			if (0 == errCode) {
				/* 用户信息 */
				String info = jsonObj.getString("info");
				System.out.println("用户信息  = " + info);
			} else {
				System.out.println("验证令牌并获取用户的登录信息失败:" + msg);
			}
		}
		return jsonObj;
	}

	/***
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param apiUrl
	 * @param data
	 * @param headers
	 * @param encoding
	 * @return
	 */
	public JSONObject sendPOST(String apiUrl, String data, LinkedHashMap<String, String> headers) {
		StringBuffer strBuffer = null;
		String result = null;
		JSONObject jsonObj = null;
		try {
			// 建立连接
			URL url = new URL(apiUrl);
			/* 获取客户端向服务器端传送数据所依据的协议名称 */
			String protocol = url.getProtocol();
			if ("https".equalsIgnoreCase(protocol)) {
				/* 获取HTTPS请求的SSL证书 */
				try {
					SSLUtils.ignoreSsl();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			// 需要输出
			httpURLConnection.setDoOutput(true);
			// 需要输入
			httpURLConnection.setDoInput(true);
			// 不允许缓存
			httpURLConnection.setUseCaches(false);

			httpURLConnection.setRequestMethod("POST");
			// 设置Headers
			if (null != headers) {
				for (String key : headers.keySet()) {
					httpURLConnection.setRequestProperty(key, headers.get(key));
				}
			}
			// 连接会话
			httpURLConnection.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
			// 设置请求参数
			dos.write(data.getBytes("UTF-8"));
			dos.flush();
			dos.close();
			// 获得响应状态
			int http_StatusCode = httpURLConnection.getResponseCode();
			String http_ResponseMessage = httpURLConnection.getResponseMessage();
			if (HttpURLConnection.HTTP_OK == http_StatusCode) {
				strBuffer = new StringBuffer();
				String readLine = new String();
				BufferedReader responseReader = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null) {
					strBuffer.append(readLine);
				}
				responseReader.close();
				result = strBuffer.toString();
				if (null == result || result.length() == 0) {
					throw new Exception("获取企业（法人）信息失败");
				} else {
					jsonObj = JSONObject.fromObject(result);
				}
			} else {
				throw new Exception(
						MessageFormat.format("请求失败,失败原因: Http状态码 = {0} , {1}", http_StatusCode, http_ResponseMessage));
			}
			// 断开连接
			httpURLConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

}
