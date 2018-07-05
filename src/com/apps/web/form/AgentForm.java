package com.apps.web.form;

import java.math.BigDecimal;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Seller;
import com.ram.model.User;

public class AgentForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String userName;
	private String userType;
	private String orderNum;
	private String sessionNo;
	private int startIndex;
	private String loginName;
	private User user = new User();

	private String userGroup = null;
	private Integer managerId = null;
	private String[] selectIndex;
	private String ipAddress;
	private String uType;
	private String birthday;
	private String type;
	private FormFile cover;
	private Integer sid;
	private Integer cid;
	private Integer busaid;
	private Integer ccid;
	private String password;
	private String userLx;
	private String winResult;//订单状态（中奖，未中奖，未开奖）
	private String title;
	private String gameType;//彩票类型
	private String optionTitle;//具体下注类型
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public String[] getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(String[] selectIndex) {
		this.selectIndex = selectIndex;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getuType() {
		return uType;
	}

	public void setuType(String uType) {
		this.uType = uType;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FormFile getCover() {
		return cover;
	}

	public void setCover(FormFile cover) {
		this.cover = cover;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserLx() {
		return userLx;
	}

	public void setUserLx(String userLx) {
		this.userLx = userLx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public BigDecimal getUserpoints() {
		return userpoints;
	}

	public void setUserpoints(BigDecimal userpoints) {
		this.userpoints = userpoints;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String operateType;//��������
	private BigDecimal userpoints;
	private String agentName;
	private Integer agentId;
	private String oldPassword;//������
	private String repassword;//�ظ�����
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	private String cashType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	

	public String getWinResult() {
		return winResult;
	}

	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getSessionNo() {
		return sessionNo;
	}

	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	
	
}
