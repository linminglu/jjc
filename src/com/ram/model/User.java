package com.ram.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The persistent class for the USERS database table.
 * 
 * @author BEA Workshop Studio
 */
public class User implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private Integer userId;

	private String gender;// 性别 1男 2女
	private java.util.Date lastLoginDate;
	private String lastLoginIp;
	private String loginName;
	private Integer loginTimes;
	private String password;

	private Date registDateTime;// 注册时间
	private String regIp;//注册ip
	private String status;
	private String cellPhone;// 用户是绑定手机，一般同登录的手机相同，如果更换绑定，发短信则以这个为准，商家是电话
	private String userName;// 用户昵称
	private String userType;// 用户类型 1 APP用户 2.数据 管理员（添加和删除） 3. 商家
							// 4超级管理员（all）5.订餐商家
	// 6.电商商家7.团购商家 8.商圈管理员 9.小区管理员 10.物业管理员
	private Date birthday;// 生日
	private String userEmail;// 邮箱
	private String logo;// 头像
	private String logoMini;// 头像。小
	private BigDecimal money;// 账户余额
	private BigDecimal addUpRechargeMoney;// 累计充值金额
	
	// ---临时字段
	private String sname;// 所属商家名
	// ----

	private String realName;// 真实姓名

	// 以下字段暂未用到
	private Integer occId;// 职业,BaseData里的id
	private Integer cityId;// 城市id
	private Integer areaId;// 辖区id
	private String longAlt;// 经纬度
	private String machineType;// 设备类型 1是安卓 2是IOS
	private String token;// 设备码
	private String address;// 地址

	private String motto;// 个性签名
	private String actCode;// 用作记录身份证号
	private String city;// 定位城市

	private String activeCode;
	private Integer birthdayDay;
	private Integer birthdayMonth;
	private Integer birthdayYear;
	private Integer browseCount;
	private Integer lookTimes = 0;
	private String pic;
	private String picture;
	private String province;// 省份
	private Integer prCode;
	private Integer unCode;
	private String userNameZh;
	private Integer inviteUserId;
	private String userFolder;
	private Integer waveId;
	private Integer organizationId;

	private Integer orgUserId;
	private Date activeTime;// 结束时间
	private Date deadline;// 结束期限
	private Integer learnDays;// 学习周期
	private Integer categoryId;// 科目类型

	private BigDecimal userpoints;// 总积分
	private Integer userwebpoints;// 网络积分
	private Integer useruppoints;// 上传积分

	private Integer defDict;

	private String userFrom;// 用户来源
	private String postcode;
	private String company;

	private BigDecimal userBalance;// 今日收益
	private BigDecimal drawMoney;// 提款金额
	private BigDecimal userScore;// 红包(已废弃，原来设计优先使用红包购买)
	private String invitationCode;// 邀请码
	private String statusCode;// 邀请码状态（0 未使用，1 已使用）每一个邀请码只能邀请一名用户
	private String alipy;// 支付包账号
	private String banben;// 用户的版本
	// 备用字段
	private String extend1;
	private String extend2;
	private String extend3;

	private Integer ccid;

	private String companyName;// 公司名

	private String companyAddress;// 公司地址

	private String companyContent;// 公司介绍
	private Integer agentId;// 直系上级代理用户ID
	private Integer agentId1;// 一级代理
	private String agentName;// 代理名称

	private BigDecimal dayRecharge;// 今日充值
//	private BigDecimal dayBet;// 今日投注（打码）
	private Integer turnTableNum;// 转盘抽奖次数
	private Integer redPacketsNum;// 红包抽奖次数

	private BigDecimal recRevenue;// 推荐收益
	private Date lastSignDate;// 今日签到状态
	private String cityName;//

	
	private BigDecimal dayWinMoney;// 日中奖金额
	private BigDecimal weekWinMoney;// 周中奖金额
	private BigDecimal winMoney;// 累计中奖金额
	private BigDecimal aggregateBetMoney;// 累计投注金额
	private BigDecimal dayBetMoney;// 日投注金额
	private String isAuthentication;// 是否身份验证
	private String isVerifyPhone;// 是否验证手机号码
	private String isVerifyEmail;// 是否验证邮箱
	private Integer agentNum;// 代理数量
	private Integer memberNum;// 会员数量
	private BigDecimal totalRechage;// 累计充值
	private BigDecimal rechagePresent;// 累计充值赠送
	private BigDecimal exchangeMoney;// 累计积分兑换
	private String qq;
	private Date firstRechargeTime;// 每日首次充值时间
	private Integer rechargeWay;// 充值通道
	private String isBetBack;// 每个用户打码返水开关
	
	/**
	 * 如果此字段是在后台给用户设置的，一但设置以后， 用户不能提现，除非
	 * 1.用户从设置时间limitDate开始打码够limitBetBack金额
	 * 2.用户一次性充值limitRecharge金额
	 * 才能提现
	 **/
	private BigDecimal limitBetBack;// 限制打码金额 
	private BigDecimal limitRecharge;// 取消限制的充值金额
	private Date limitDate;// 限制开始时间
	
	private String session;// 

	public String getSession() {
		return session;
	}
	
	public BigDecimal getAddUpRechargeMoney() {
		return addUpRechargeMoney;
	}

	public void setAddUpRechargeMoney(BigDecimal addUpRechargeMoney) {
		this.addUpRechargeMoney = addUpRechargeMoney;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	public User(Integer userId, String logo, BigDecimal userpoints){
		this.userId = userId;
		this.logo = logo;
		this.userpoints = userpoints;
	}
	
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getAgentId1() {
		return agentId1;
	}

	public void setAgentId1(Integer agentId1) {
		this.agentId1 = agentId1;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	private String cashPassword;
	private String QRCode;
	private String accountNo; // 用户提现绑定的银行卡号
	private String bankName;// 银行名字
	private String bindName;// 银行卡持卡人姓名

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBindName() {
		return bindName;
	}

	public void setBindName(String bindName) {
		this.bindName = bindName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public BigDecimal getLimitRecharge() {
		return limitRecharge;
	}


	public void setLimitRecharge(BigDecimal limitRecharge) {
		this.limitRecharge = limitRecharge;
	}


	public Date getLimitDate() {
		return limitDate;
	}


	public void setLimitDate(Date limitDate) {
		this.limitDate = limitDate;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void setCity(String city) {
		this.city = city;
	}

	public Integer getOccId() {
		return occId;
	}

	public BigDecimal getLimitBetBack() {
		return limitBetBack;
	}


	public void setLimitBetBack(BigDecimal limitBetBack) {
		this.limitBetBack = limitBetBack;
	}


	public String getIsBetBack() {
		return isBetBack;
	}


	public void setIsBetBack(String isBetBack) {
		this.isBetBack = isBetBack;
	}


	public void setOccId(Integer occId) {
		this.occId = occId;
	}

	public String getLongAlt() {
		return longAlt;
	}

	public void setLongAlt(String longAlt) {
		this.longAlt = longAlt;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExtend3() {
		return extend3;
	}

	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	public User(Integer userId, String userName, String picture) {
		this.userId = userId;
		this.userName = userName;
		this.picture = picture;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getDeadline() {
		return deadline;
	}

	public String getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	private java.util.Set userInfos;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogoMini() {
		return logoMini;
	}

	public void setLogoMini(String logoMini) {
		this.logoMini = logoMini;
	}

	public User() {
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getActiveCode() {
		return this.activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getActCode() {
		return this.actCode;
	}

	public void setActCode(String actCode) {
		this.actCode = actCode;
	}

	public Integer getBirthdayDay() {
		return this.birthdayDay;
	}

	public void setBirthdayDay(Integer birthdayDay) {
		this.birthdayDay = birthdayDay;
	}

	public Integer getBirthdayMonth() {
		return this.birthdayMonth;
	}

	public void setBirthdayMonth(Integer birthdayMonth) {
		this.birthdayMonth = birthdayMonth;
	}

	public Integer getBirthdayYear() {
		return this.birthdayYear;
	}

	public void setBirthdayYear(Integer birthdayYear) {
		this.birthdayYear = birthdayYear;
	}

	public Integer getBrowseCount() {
		return this.browseCount;
	}

	public void setBrowseCount(Integer browseCount) {
		this.browseCount = browseCount;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getDefDict() {
		return defDict;
	}

	public void setDefDict(Integer defDict) {
		this.defDict = defDict;
	}

	public java.util.Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(java.util.Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Integer getLookTimes() {
		return this.lookTimes;
	}

	public void setLookTimes(Integer lookTimes) {
		this.lookTimes = lookTimes;
	}

	public String getMotto() {
		return this.motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
		this.pic = picture;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getPrCode() {
		return this.prCode;
	}

	public void setPrCode(Integer prCode) {
		this.prCode = prCode;
	}

	public java.util.Date getRegistDateTime() {
		return this.registDateTime;
	}

	public void setRegistDateTime(java.util.Date registDateTime) {
		this.registDateTime = registDateTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUnCode() {
		return this.unCode;
	}

	public void setUnCode(Integer unCode) {
		this.unCode = unCode;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userNameZh = userName;
		this.userName = userName;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	// uni-directional many-to-one association to UserInfo
	public java.util.Set getUserInfos() {
		return this.userInfos;
	}

	public void setUserInfos(java.util.Set userInfos) {
		this.userInfos = userInfos;
	}

	public String toString() {
		return new ToStringBuilder(this).append("userId", getUserId())
				.toString();
	}

	public Integer getInviteUserId() {
		return inviteUserId;
	}

	public void setInviteUserId(Integer inviteUserId) {
		this.inviteUserId = inviteUserId;
	}

	public String getUserFolder() {
		return userFolder;
	}

	public void setUserFolder(String userFolder) {
		this.userFolder = userFolder;
	}

	public String getUserNameZh() {
		return userNameZh;
	}

	public void setUserNameZh(String userNameZh) {
		this.userName = userNameZh;
		this.userNameZh = userNameZh;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
		this.picture = pic;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getWaveId() {
		return waveId;
	}

	public void setWaveId(Integer waveId) {
		this.waveId = waveId;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Integer getLearnDays() {
		return learnDays;
	}

	public void setLearnDays(Integer learnDays) {
		this.learnDays = learnDays;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Integer getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(Integer orgUserId) {
		this.orgUserId = orgUserId;
	}

	public BigDecimal getUserpoints() {
		return userpoints;
	}

	public void setUserpoints(BigDecimal userpoints) {
		this.userpoints = userpoints;
	}

	public Integer getUseruppoints() {
		return useruppoints;
	}

	public void setUseruppoints(Integer useruppoints) {
		this.useruppoints = useruppoints;
	}

	public Integer getUserwebpoints() {
		return userwebpoints;
	}

	public void setUserwebpoints(Integer userwebpoints) {
		this.userwebpoints = userwebpoints;
	}

	public BigDecimal getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(BigDecimal userBalance) {
		this.userBalance = userBalance;
	}

	public BigDecimal getUserScore() {
		return userScore;
	}

	public void setUserScore(BigDecimal userScore) {
		this.userScore = userScore;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getAlipy() {
		return alipy;
	}

	public void setAlipy(String alipy) {
		this.alipy = alipy;
	}

	public String getBanben() {
		return banben;
	}

	public void setBanben(String banben) {
		this.banben = banben;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyContent() {
		return companyContent;
	}

	public void setCompanyContent(String companyContent) {
		this.companyContent = companyContent;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public BigDecimal getDrawMoney() {
		return drawMoney;
	}

	public void setDrawMoney(BigDecimal drawMoney) {
		this.drawMoney = drawMoney;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		this.QRCode = qRCode;
	}

	public String getCashPassword() {
		return cashPassword;
	}

	public void setCashPassword(String cashPassword) {
		this.cashPassword = cashPassword;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo
	 *            the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getDayRecharge() {
		return dayRecharge;
	}

	public void setDayRecharge(BigDecimal dayRecharge) {
		this.dayRecharge = dayRecharge;
	}

	public Integer getTurnTableNum() {
		return turnTableNum;
	}

	public void setTurnTableNum(Integer turnTableNum) {
		this.turnTableNum = turnTableNum;
	}

	public Integer getRedPacketsNum() {
		return redPacketsNum;
	}

	public void setRedPacketsNum(Integer redPacketsNum) {
		this.redPacketsNum = redPacketsNum;
	}

	public BigDecimal getRecRevenue() {
		return recRevenue;
	}

	public void setRecRevenue(BigDecimal recRevenue) {
		this.recRevenue = recRevenue;
	}

	public Date getLastSignDate() {
		return lastSignDate;
	}

	public void setLastSignDate(Date lastSignDate) {
		this.lastSignDate = lastSignDate;
	}

//	public BigDecimal getDayBet() {
//		return dayBet;
//	}
//
//	public void setDayBet(BigDecimal dayBet) {
//		this.dayBet = dayBet;
//	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getDayWinMoney() {
		return dayWinMoney;
	}

	public void setDayWinMoney(BigDecimal dayWinMoney) {
		this.dayWinMoney = dayWinMoney;
	}

	public BigDecimal getWeekWinMoney() {
		return weekWinMoney;
	}

	public void setWeekWinMoney(BigDecimal weekWinMoney) {
		this.weekWinMoney = weekWinMoney;
	}

	public BigDecimal getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}

	public BigDecimal getAggregateBetMoney() {
		return aggregateBetMoney;
	}

	public void setAggregateBetMoney(BigDecimal aggregateBetMoney) {
		this.aggregateBetMoney = aggregateBetMoney;
	}

	public BigDecimal getDayBetMoney() {
		return dayBetMoney;
	}

	public void setDayBetMoney(BigDecimal dayBetMoney) {
		this.dayBetMoney = dayBetMoney;
	}

	public String getIsAuthentication() {
		return isAuthentication;
	}

	public void setIsAuthentication(String isAuthentication) {
		this.isAuthentication = isAuthentication;
	}

	public String getIsVerifyPhone() {
		return isVerifyPhone;
	}

	public void setIsVerifyPhone(String isVerifyPhone) {
		this.isVerifyPhone = isVerifyPhone;
	}

	public String getIsVerifyEmail() {
		return isVerifyEmail;
	}

	public void setIsVerifyEmail(String isVerifyEmail) {
		this.isVerifyEmail = isVerifyEmail;
	}

	public Integer getAgentNum() {
		return agentNum;
	}

	public void setAgentNum(Integer agentNum) {
		this.agentNum = agentNum;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public BigDecimal getTotalRechage() {
		return totalRechage;
	}

	public void setTotalRechage(BigDecimal totalRechage) {
		this.totalRechage = totalRechage;
	}

	public BigDecimal getRechagePresent() {
		return rechagePresent;
	}

	public void setRechagePresent(BigDecimal rechagePresent) {
		this.rechagePresent = rechagePresent;
	}

	public BigDecimal getExchangeMoney() {
		return exchangeMoney;
	}

	public void setExchangeMoney(BigDecimal exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getFirstRechargeTime() {
		return firstRechargeTime;
	}

	public void setFirstRechargeTime(Date firstRechargeTime) {
		this.firstRechargeTime = firstRechargeTime;
	}


	public String getRegIp() {
		return regIp;
	}


	public Integer getRechargeWay() {
		return rechargeWay;
	}


	public void setRechargeWay(Integer rechargeWay) {
		this.rechargeWay = rechargeWay;
	}


	public void setRegIp(String regIp) {
		this.regIp = regIp;
	}

	// public String getSjCode() {
	// return sjCode;
	// }
	//
	// public void setSjCode(String sjCode) {
	// this.sjCode = sjCode;
	// }

}