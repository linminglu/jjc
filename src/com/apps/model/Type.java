package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类
 * 
 * @author Mr.zang
 * 
 */
public class Type implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer tid;
	private String title;// 标题
	private String status;// 状态
	private String sort;// 排序
	private String img;
	private String type;// [type_cate表定义]栏目类型 1.电商栏目 2.订餐栏目 3.团购栏目 7.秒杀 8.礼品卡 9.摇一摇 11.供求信息 12.物业管理 13.便民服务 14游戏娱乐 15.答题赚分 16.积分兑换 17.幸运抽奖 18.我的积分
	private Integer parentId;// 父级栏目id
	private String isMore = "0";// 是否是更多，1.是 0.不是
	private BigDecimal score;//积分

	private String position;//显示位置1.首页主要大类(每页最多8个) 2.首页静态栏目 次要位置(最多3个)
	
	private String isShow;// 是否显示二级分类
	private String template;// 模版，针对电商 1.百度外卖模版 2.京东到家模版
	
	//临时字段
	private String ptitle;
	//~

	public Type() {
	}

	
	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getIsMore() {
		return isMore;
	}

	public void setIsMore(String isMore) {
		this.isMore = isMore;
	}


	public BigDecimal getScore() {
		return score;
	}


	public void setScore(BigDecimal score) {
		this.score = score;
	}


	public String getPtitle() {
		return ptitle;
	}


	public void setPtitle(String ptitle) {
		this.ptitle = ptitle;
	}


	public String getIsShow() {
		return isShow;
	}


	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}


	public String getTemplate() {
		return template;
	}


	public void setTemplate(String template) {
		this.template = template;
	}
	

}
