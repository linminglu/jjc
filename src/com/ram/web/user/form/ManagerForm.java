package com.ram.web.user.form;

import java.math.BigDecimal;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Seller;
import com.game.model.UserLevel;
import com.ram.model.User;
import com.ram.model.UserLimit;
import com.ram.model.dto.UserMoneyDTO;

public class ManagerForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private User user = new User();
	private Seller seller = new Seller();// 商家
	private String userGroup = null;
	private Integer managerId = null;
	private String[] selectIndex;

	private String loginName;
	private String ipAddress;
	private String startDate;
	private String endDate;
	private String userType;
	private String uType;
	private String birthday;
	private String type;
	private FormFile cover;
	private Integer sid;
	
	private String userId;
	private String gameType;//彩票类型
	private String cashType;
	private String sessionNo;
	private String betDetailId;//投注详情id

	private Integer cid;
	private Integer busaid;
	private Integer ccid;
	private String password;
	private String userLx;
	private String title;
	private String operateType;//操作类型
	private BigDecimal userpoints;
	
	private String startIndex;
	private String userName;
	
	private String agentName;
	private Integer agentId;
	private String oldPassword;//旧密码
	private String repassword;//重复密码
	
	private UserLevel userLevel = new UserLevel();// 用户等级属性
	private UserMoneyDTO userMoneyDTO;
	private String status;//用户状态
	private String accountNo;//银行卡号
	private UserLimit userlimit = new UserLimit();//银行卡号
	
	
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getuType() {
		return uType;
	}

	public void setuType(String uType) {
		this.uType = uType;
	}

	public FormFile getCover() {
		return cover;
	}

	public void setCover(FormFile cover) {
		this.cover = cover;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String[] getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(String[] selectIndex) {
		this.selectIndex = selectIndex;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	/**
	 * @return the startIndex
	 */
	public String getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public String getSessionNo() {
		return sessionNo;
	}

	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}

	public String getBetDetailId() {
		return betDetailId;
	}

	public void setBetDetailId(String betDetailId) {
		this.betDetailId = betDetailId;
	}

	public UserLevel getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}

	public UserMoneyDTO getUserMoneyDTO() {
		return userMoneyDTO;
	}

	public void setUserMoneyDTO(UserMoneyDTO userMoneyDTO) {
		this.userMoneyDTO = userMoneyDTO;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserLimit getUserlimit() {
		return userlimit;
	}

	public void setUserlimit(UserLimit userlimit) {
		this.userlimit = userlimit;
	}



	

}
