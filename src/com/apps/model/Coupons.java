package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券
 * 
 * @author Mr.zang
 */
public class Coupons implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private BigDecimal sum  ;// 金额
	private String type;// 1.通用券 2.专用券
	private String tp;// 1.优惠券 2.兑换券
	private Integer sid;// 商家id，专用劵时有此数据
	private Integer points;// 积分
	private BigDecimal lastSum;// 最低消费金额

	private Date validDate;// 有效期
	private Date createDate;// 数据时间

	private String template;// 模版 1.蓝色 2.绿色 3.红色 4.黄色
	private Integer total;// 总数量
	private Integer num;// 剩余数量
	private String remarks;// 备注
	private String status;// 状态 1.有效 0.无效
	
	private  String  goods;//兑换物品名称
	
	private String joinAward="0";//是否参加抽奖摇一摇 1=参与  0=默认不参加
	
	
	
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getTp() {
		return tp;
	}

	public void setTp(String tp) {
		this.tp = tp;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public BigDecimal getLastSum() {
		return lastSum;
	}

	public void setLastSum(BigDecimal lastSum) {
		this.lastSum = lastSum;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJoinAward() {
		return joinAward;
	}

	public void setJoinAward(String joinAward) {
		this.joinAward = joinAward;
	}

}
