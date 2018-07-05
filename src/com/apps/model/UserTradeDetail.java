package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户资金明细
 * 
 * @author Mr.zang
 */
public class UserTradeDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tradeDetailId;
	private Integer userId;
	private String tradeType;// 收支 1.收入2.支出
	private String cashType;// 资金来源 1.在线充值 2.卡充 3.余额支付 4.分销佣金
	private BigDecimal cashMoney;//
	private Date createTime;
	private Integer refId=0;// 数据来源id
	private String remark;//备注
	private String modelType;//彩票类型

	private  BigDecimal  userMoney;//用户余额 交易后的余额
	
	private String sessionNo;//期号
	private String type="0";//用户类型 那种类型的用户
	
	private String loginName;
	
	private String status;//是否统计 1=统计，0=不统计
	private String gfxy;//投注标识1=官方2=信用
	
	public UserTradeDetail() {
	}

	public UserTradeDetail(Integer userId, String tradeType, String cashType,
			BigDecimal cashMoney, Date createTime) {
		this.userId = userId;
		this.tradeType = tradeType;
		this.cashType = cashType;
		this.cashMoney = cashMoney;
		this.createTime = createTime;
	}
	public UserTradeDetail(Integer userId, String tradeType, String cashType,
			BigDecimal cashMoney, Date createTime,String remark,String modelType) {
		this.userId = userId;
		this.tradeType = tradeType;
		this.cashType = cashType;
		this.cashMoney = cashMoney;
		this.createTime = createTime;
		this.remark = remark;
		this.modelType = modelType;
	}
	
	//@ManagerAction.balanceDetail()
		public UserTradeDetail(Integer tradeDetailId,
				Integer userId,
				String loginName,
				String tradeType,
				String cashType,
				BigDecimal cashMoney,
				BigDecimal userMoney,
				Date createTime,
				String modelType,
				String remark,
				String sessionNo,
				Integer refId
			){
			this.tradeDetailId=tradeDetailId;
			this.userId=userId;
			this.loginName=loginName;
			this.tradeType=tradeType;
			this.cashType=cashType;
			this.cashMoney=cashMoney;
			this.userMoney=userMoney;
			this.createTime=createTime;
			this.modelType=modelType;
			this.remark=remark;
			this.sessionNo=sessionNo;
			this.refId=refId;
		}

	public Integer getTradeDetailId() {
		return tradeDetailId;
	}

	public void setTradeDetailId(Integer tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public BigDecimal getCashMoney() {
		return cashMoney;
	}

	public void setCashMoney(BigDecimal cashMoney) {
		this.cashMoney = cashMoney;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public BigDecimal getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(BigDecimal userMoney) {
		this.userMoney = userMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getSessionNo() {
		return sessionNo;
	}

	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getGfxy() {
		return gfxy;
	}

	public void setGfxy(String gfxy) {
		this.gfxy = gfxy;
	}
	
}
