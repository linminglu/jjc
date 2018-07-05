package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家基本信息</br>
 * 
 * @author Mr.zang
 * 
 */
public class Seller implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer sid;
	private String address;// 地址

	private String title;// 商家名
	private String remarks;// 商家简介,通知
	private String telePhone;// 商家电话
	private Integer salesNum = 0;// 销量
	private Float evalStar = 0f;// 评分
	private String logo;// 店铺logo
	private String sort;// 排序
	private Date createDate;// 创建时间
	private String longAlt;// 经纬度
	// 营业时间
	private Date startTime;
	private Date endTime;
	private BigDecimal sendUpPrices;// 起送价格
	private BigDecimal freightPrices;// 运费
	private Integer freightTime;// 运送时间
	private String isMinus;// 是否是 满减 0否 1是
	private String minusStr;// 满减说明
	private String isFreight;// 是否 免运费 0否 1是
	private String isInvoice;// 是否支持 开发票 0否 1是
	private String isHot;// 是否是热卖
	private String status;// 状态
	private String isOpen;// 是否正常营业 0.暂停营业 1.正常
	private String keywords;// 关键字
	private Integer cid;// 城市id
	private Integer areaId;// 辖区id
	private String average;// 人均消费
	private String label;// 标签

	private Integer columnId;// 首页栏目id
	private Integer tid;// 二级栏目id
	private Integer tid3;// 三级栏目id

	private String link;
	private Integer busaid;//商圈id
	private Integer ccid;//小区id
	
	private BigDecimal startMoney;//起始价
	private BigDecimal ratio;//比例
	private BigDecimal maxMoney;//封顶
	public Seller() {
	}

	public Seller(Integer sid, String title) {
		this.sid = sid;
		this.title = title;
	}

	
	
	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Integer getTid3() {
		return tid3;
	}

	public void setTid3(Integer tid3) {
		this.tid3 = tid3;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}

	public String getLongAlt() {
		return longAlt;
	}

	public void setLongAlt(String longAlt) {
		this.longAlt = longAlt;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 起送价格
	 */
	public BigDecimal getSendUpPrices() {
		return sendUpPrices;
	}

	/**
	 * 起送价格
	 */
	public void setSendUpPrices(BigDecimal sendUpPrices) {
		this.sendUpPrices = sendUpPrices;
	}

	/**
	 * 送餐时间
	 */
	public Integer getFreightTime() {
		return freightTime;
	}

	/**
	 * 送餐时间
	 */
	public void setFreightTime(Integer freightTime) {
		this.freightTime = freightTime;
	}

	/**
	 * 运费
	 */
	public BigDecimal getFreightPrices() {
		return freightPrices;
	}

	/**
	 * 运费
	 */
	public void setFreightPrices(BigDecimal freightPrices) {
		this.freightPrices = freightPrices;
	}

	/**
	 * 是否立减
	 */
	public String getIsMinus() {
		return isMinus;
	}

	/**
	 * 是否立减
	 */
	public void setIsMinus(String isMinus) {
		this.isMinus = isMinus;
	}

	/**
	 * 立减描述文字
	 */
	public String getMinusStr() {
		return minusStr;
	}

	/**
	 * 立减描述文字
	 */
	public void setMinusStr(String minusStr) {
		this.minusStr = minusStr;
	}

	/**
	 * 是否免送
	 */
	public String getIsFreight() {
		return isFreight;
	}

	/**
	 * 是否免送
	 */
	public void setIsFreight(String isFreight) {
		this.isFreight = isFreight;
	}

	/**
	 * 是否支持开发票
	 */
	public String getIsInvoice() {
		return isInvoice;
	}

	/**
	 * 是否支持开发票
	 */
	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
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

	public Float getEvalStar() {
		return evalStar;
	}

	public void setEvalStar(Float evalStar) {
		this.evalStar = evalStar;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

	public BigDecimal getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(BigDecimal startMoney) {
		this.startMoney = startMoney;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}
	

}
