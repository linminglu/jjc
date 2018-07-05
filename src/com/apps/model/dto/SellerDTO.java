package com.apps.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.apps.model.Promotion;
import com.apps.model.Seller;
import com.ram.model.User;

public class SellerDTO {

	public User user = new User();
	public Seller seller = new Seller();
	public Promotion promotion = new Promotion();

	private Integer sid;// 商家id
	private String sname;// 如果用户商家的话就是商家名
	private String logo;// 店铺logo
	private Integer salesNum;// 销量
	private Float evalStar;// 评分
	private BigDecimal sendUpPrices;// 起送费
	private BigDecimal freightPrices;// 运费
	private Integer freightTime;// 运送时间

	private Integer collectId;// 收藏id
	private Date createTime;// 收藏时间
	private Integer uid;//

	private String address;// 地址
	private String remarks;// 商家简介
	private String telePhone;// 商家电话
	private String isMinus;// 是否是 满减 0否 1是
	private String isFreight;// 是否 免运费 0否 1是
	private String isInvoice;// 是否 开发票 0否 1是

	private Date createDate;// 创建时间
	private String status;//
	private String userName;//
	private String longAlt;// 经纬度
	private String isHot;//是否热卖

	public SellerDTO() {
	}

	/**
	 * 商家收藏
	 * 
	 * @param sid
	 *            商家id
	 * @param sname
	 *            商家名
	 * @param logo
	 *            商家logo
	 * @param salesNum
	 *            销量
	 * @param evalStar
	 *            评分
	 * @param sendUpPrices
	 *            起送费
	 * @param freightPrices
	 *            运费
	 * @param freightTime
	 *            运送时间
	 * @param uid
	 *            用户id
	 * @param collectId
	 *            收藏id
	 * @param createTime
	 *            收藏时间
	 */
	public SellerDTO(Integer sid, String sname, String logo, int salesNum,
			Float evalStar, BigDecimal sendUpPrices, BigDecimal freightPrices,
			Integer freightTime, String remarks, String longAlt,
			Integer collectId, Date createTime, Integer uid) {
		this.sid = sid;
		this.sname = sname;
		this.logo = logo;
		this.salesNum = salesNum;
		this.evalStar = evalStar;
		this.sendUpPrices = sendUpPrices;
		this.freightPrices = freightPrices;
		this.freightTime = freightTime;
		this.remarks = remarks;
		this.longAlt = longAlt;
		this.collectId = collectId;
		this.createTime = createTime;
		this.uid = uid;
	}

	/**
	 * 商家列表
	 * 
	 * @param sid
	 *            商家id
	 * @param sname
	 *            商家名
	 * @param logo
	 *            商家logo
	 * @param salesNum
	 *            销量
	 * @param evalStar
	 *            评分
	 * @param sendUpPrices
	 *            起送费
	 * @param freightPrices
	 *            运费
	 * @param freightTime
	 *            运送时间
	 * @param isMinus
	 *            是否是 满减 0否 1是
	 * @param isFreight
	 *            是否 免运费 0否 1是
	 * @param isInvoice
	 *            是否 开发票 0否 1是
	 * @param createDate
	 *            创建时间
	 * @param status
	 *            状态
	 */
	public SellerDTO(Integer sid, String sname, String logo, int salesNum,
			Float evalStar, BigDecimal sendUpPrices, BigDecimal freightPrices,
			Integer freightTime, String isMinus, String isFreight,
			String isInvoice, Date createDate, String status,String isHot) {
		this.sid = sid;
		this.sname = sname;
		this.logo = logo;
		this.salesNum = salesNum;
		this.evalStar = evalStar;
		this.sendUpPrices = sendUpPrices;
		this.freightPrices = freightPrices;
		this.freightTime = freightTime;
		this.isMinus = isMinus;
		this.isFreight = isFreight;
		this.isInvoice = isInvoice;
		this.createDate = createDate;
		this.status = status;
		this.isHot = isHot;
	}

	/**
	 * 促销列表
	 * 
	 * @param promotion
	 * @param sname
	 */
	public SellerDTO(Promotion promotion, String sname) {
		this.promotion = promotion;
		this.sname = sname;
	}

	public String getLongAlt() {
		return longAlt;
	}

	public void setLongAlt(String longAlt) {
		this.longAlt = longAlt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getIsMinus() {
		return isMinus;
	}

	public void setIsMinus(String isMinus) {
		this.isMinus = isMinus;
	}

	public String getIsFreight() {
		return isFreight;
	}

	public void setIsFreight(String isFreight) {
		this.isFreight = isFreight;
	}

	public String getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(int salesNum) {
		this.salesNum = salesNum;
	}

	public Float getEvalStar() {
		return evalStar;
	}

	public void setEvalStar(Float evalStar) {
		this.evalStar = evalStar;
	}

	public BigDecimal getSendUpPrices() {
		return sendUpPrices;
	}

	public void setSendUpPrices(BigDecimal sendUpPrices) {
		this.sendUpPrices = sendUpPrices;
	}

	public BigDecimal getFreightPrices() {
		return freightPrices;
	}

	public void setFreightPrices(BigDecimal freightPrices) {
		this.freightPrices = freightPrices;
	}

	public Integer getFreightTime() {
		return freightTime;
	}

	public void setFreightTime(Integer freightTime) {
		this.freightTime = freightTime;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

}
