package cn.hangzhou.demo.service;

import java.util.HashMap;
import java.util.Map;

import cn.hangzhou.demo.bean.UserBean;

/***
 * 模拟自建系统数据库中的数据
 *
 */
public class UserService {
	private static Map<String,UserBean> userMap;
	static {
		userMap = new HashMap<String,UserBean>();
		// 假设企业的组织机构代码号为 75490956-0
		String orgNumber = "75490956-0";
		// 假设政务服务网账号唯一标识为 9152
		String userId = "9152";
		UserBean ub = new UserBean();
		ub.setCompanyName("统计直报应用测试");
		ub.setOrgNumber(orgNumber);
		ub.setRegNumber("0001");
		ub.setApprInfo("test0001");
		ub.setUserId("328872");
		ub.setPassword("123456");
		userMap.put(orgNumber, ub);
		userMap.put(userId, ub);
	}
	
	public static UserBean findUser(String orgNumber){
		return userMap.get(orgNumber);
	}
}
