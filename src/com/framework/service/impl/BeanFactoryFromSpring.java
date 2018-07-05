/**
 * @(#)BeanFactorySpringImpl.java 2005-8-1
 * 
 * Copyright (c) 1999-2005 。 北京市西三环北路19号 外研大厦配楼3405室 All rights
 * reserved.
 * 
 */
package com.framework.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ClassUtils;

import com.framework.Constants;

/**
 * @version 2005-8-1 1.0
 * @author Sunan
 * 
 * Creation date: 2005-8-1
 */
public class BeanFactoryFromSpring {

	private static ApplicationContext context;

	/**
	 * 启动日志服务
	 */
	protected final static Log log = LogFactory
			.getLog(BeanFactoryFromSpring.class);
	
	
	static{
		init();
	}

	private static void init() {
		String pkg = ClassUtils.classPackageAsResourcePath(Constants.class);
		log.info(pkg);
		String[] paths = { pkg
				+ "/configuration/spring/applicationContext-*.xml" };
		log.info("************************************************************************");
		log.info("**********spring 配置文件调试，以下为spring启动时加载的配置文件列表：************");
		log.info("**********（如果在程序发布时出现此信息，请关闭Log4j配置文件中的INFO项）***********");
		for (int i = 0, j = paths.length; i < j; i++) {
			log.info("paths[" + i +"]" + paths[i]);
		}
		log.info("***********spring 配置文件加载完毕*****************************************");
		context = new ClassPathXmlApplicationContext(paths);
	}

	/**
	 * 通过Spring获得bean的实例
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {

		return context.getBean(name);
	}

	/**
	 * 获得service的实例
	 * 
	 * @param serviceName
	 * @return
	 */
	public static Object getService(String serviceName) {
		return getBean(serviceName);
	}

}
