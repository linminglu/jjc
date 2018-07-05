/**
 * @(#)RecursionBean.java 1.0 06/05/26
 */

package com.ram.model;
import java.io.Serializable;
/**
 * 递归BEAN,主要用在分类的保存
 * @author Lu Congyu
 */

public class RecursionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RecursionBean(){}
	
	public RecursionBean(Integer id, Integer cid, String title){
		this.id = id;
		this.cid = cid;
		this.title = title;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public void setCid(Integer cid){
		this.cid = cid;
	}
	
	public Integer getCid(){
		return this.cid;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	
	
	private Integer id;
	private Integer cid; // 父ID
	private String title;
}