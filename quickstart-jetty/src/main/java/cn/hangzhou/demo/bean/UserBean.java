package cn.hangzhou.demo.bean;

public class UserBean {
	private String companyName; 	// 法人名称
	private String orgNumber; 		// 机构代码号
	private String regNumber; 		// 工商注册号
	private String userId; 			// 法人在办事系统内部的登录名
	private String password; 		// 法人在办事系统内部的登录口令
	private String apprInfo;		//办事系统内部信息

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOrgNumber() {
		return orgNumber;
	}

	public void setOrgNumber(String orgNumber) {
		this.orgNumber = orgNumber;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApprInfo() {
		return apprInfo;
	}

	public void setApprInfo(String apprInfo) {
		this.apprInfo = apprInfo;
	}
}
