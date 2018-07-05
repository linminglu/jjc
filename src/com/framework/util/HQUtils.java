package com.framework.util;

import java.util.ArrayList;
import java.util.List;

public class HQUtils {

	/**
	 * HQL查询语句
	 */
	private StringBuffer queryString=new StringBuffer();
	private StringBuffer queryCount=new StringBuffer();

	private String select = "";// 查询头
	private String selectCount = "select count(*) ";
	private StringBuffer hsql=new StringBuffer();// 主查询

	private Integer startIndex;//开始偏移量
	private Integer pageSize;//第页数量
	private Integer pageIndex;//页码
	

	/**
	 * 参数集合对象
	 */
	private List<Object> pars=new ArrayList<Object>();

	/**
	 * 排序字段
	 */
	private String orderby;

	/**
	 * 分组字段
	 */
	private String groupby;
	
	public HQUtils(){
		
	}
	
	public HQUtils(String hql){
		hsql.append(hql);
	}
	
	public String getQueryString(){
		if(ParamUtils.chkString(select)) this.queryString.append(select);
		this.queryString.append(hsql.toString());
		if(ParamUtils.chkString(orderby)) this.queryString.append(orderby);
		if(ParamUtils.chkString(groupby)) this.queryString.append(groupby);
		//System.out.println("_______exec.sql:"+this.queryString.toString());
		return this.queryString.toString();
	}
	public String getQueryCount(){
		if(ParamUtils.chkString(selectCount)) {
			this.queryCount.append(selectCount);
		}
		String strhsql = hsql.toString();
		strhsql = this.queryCount + " from " + strhsql.split("from")[1];
		return strhsql;
	}

	public void addHsql(String s) {
		this.hsql.append(s);
	}

	public String getHsql(String s) {
		return this.hsql.toString();
	}

	public void addPars(Object par) {
		this.pars.add(par);
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<Object> getPars() {
		return pars;
	}

	public void setPars(List<Object> pars) {
		this.pars = pars;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	public void setStartIndexByPageIndex(Integer pageIndex) {
		if(pageIndex==null || pageIndex==0 || pageIndex==1) this.startIndex = 1;
		this.pageIndex = pageIndex;
		this.startIndex = (pageIndex-1)*this.pageSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public StringBuffer getHsql() {
		return hsql;
	}

	public void setHsql(StringBuffer hsql) {
		this.hsql = hsql;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	

}
