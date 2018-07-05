package com.framework.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.framework.Constants;
import com.framework.service.IService;
import com.framework.util.PathUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring初始化
 * 
 * @author zhangkeyi
 * 
 */
public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {

	private static final Log log = LogFactory.getLog(StartupListener.class);

	private ServletContext context = null;

	public void contextInitialized(ServletContextEvent event) {
		
		log.fatal("StartupListener contextInitialized...");
		
		// add by lgj
		this.context = event.getServletContext();
		String realPath = context.getRealPath("/WEB-INF/classes");
		PathUtil.setServletClassesPath(realPath);

		// end by lgj

		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();
		String daoType = context.getInitParameter(Constants.DAO_TYPE);

		// if daoType is not specified, use DAO as default
		if (daoType == null) {
			log.warn("No 'daoType' context carameter, using hibernate");
			daoType = Constants.DAO_TYPE_HIBERNATE;
		}

		// Orion starts Servlets before Listeners, so check if the config
		// object already exists
		Map config = (HashMap) context.getAttribute(Constants.CONFIG);

		if (config == null) {
			config = new HashMap();
		}

		// Create a config object to hold all the app config values
		config.put(Constants.DAO_TYPE, daoType);
		context.setAttribute(Constants.CONFIG, config);

		// output the retrieved values for the Init and Context Parameters
		if (log.isDebugEnabled()) {
			log.debug("daoType: " + daoType);
			log.debug("populating drop-downs...");
		}

		setupContext(context);
	}

	public static void setupContext(ServletContext context) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		log.fatal("StartupListener.setupContext");
	}
}
