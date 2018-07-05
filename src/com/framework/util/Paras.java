package com.framework.util;

/**
 *
 * <p>Title:定义一个sql语句的条件参数类 </p>
 * <p>Description: 可以使用有序的参数集合传送给sql/hql语句 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author lixiaodong
 * @version 1.0
 */

public class Paras {
	
	/**
	 * 和参数相对应的对象
	 */
	private Object obj;
	
	/**
	 * 参数类型编码，于java.sql.types中的类型保持一致
	 */
	private int typeNo;
	
	/**
	 * 参数名称，主要用于Query.setParameterList的时候用
	 */
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public int getTypeNo() {
		return typeNo;
	}
	public void setTypeNo(int typeNo) {
		this.typeNo = typeNo;
	}
	public Paras(int typeNo,Object obj){
		this.typeNo = typeNo;
		this.obj = obj;
	}
}  
