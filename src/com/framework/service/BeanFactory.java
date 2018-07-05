/**
 * @(#)BeanFactory.java        2005-8-1
 *
 * Copyright (c) 1999-2005 。
 * 北京市西三环北路19号 外研大厦配楼3405室
 * All rights reserved.
 *
 */
package com.framework.service;

/**
 * @version     2005-8-1    TODO 添加版本信息
 * @author Sunan
 *
 * Creation date: 2005-8-1
 */
public interface BeanFactory {
	
	public Object getBean(String name);

}
