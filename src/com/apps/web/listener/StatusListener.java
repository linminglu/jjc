package com.apps.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



/**
 * 监听器
 * 
 * @author zang
 * 
 */
public class StatusListener implements ServletContextListener {
	/**
	 * 上下文销毁
	 */
	public void contextDestroyed(ServletContextEvent sce) {
	}

	/**
	 * 上下文初始化
	 */
	public void contextInitialized(ServletContextEvent sce) {
//		new TimerManager(0, 0, 0);
	}

}
