package com.framework.service.impl;
	
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.framework.service.IServiceLocator;
	
	/**
	 * @author zhangkeyi
	 * spring启动服务
	
	 */
	public final class ServiceLocatorImpl
	    implements IServiceLocator {
	  /**
	   * ApplicationContext,Spring����
	   */
	  private static ApplicationContext context;
	  private static IServiceLocator iServiceLocator = new ServiceLocatorImpl();
	  /**
	   * 启动日志服务
	   */
	  protected final static  Log log = LogFactory.getLog(ServiceLocatorImpl.class);
	  /**
	   *������
	   */
	  
	  static {
	  	init();
	  }
	  
	  private ServiceLocatorImpl() {
	    
	  }
	
	  //~ Methods ================================================================
	
	  /**
	   * @param serviceName
	   *
	   * @return Object
	
	   */
	  public Object getService(final String serviceName) {
	
	    return context.getBean(serviceName);
	  }
	
	  /**
	   * 初始化spring	   */
	  private static void init() {
			log.fatal("Sping 启动...");
		   //String pkg = ClassUtils.classPackageAsResourcePath(RamConstants.class);
		    //log.fatal("Sping 配置文件所在路径：" + pkg);
			log.fatal("Sping 开始加载配置文件...");
			String[] paths = {"/config/applicationContext-*.xml"};
		    context = new ClassPathXmlApplicationContext(paths);
		    log.fatal("Spring 配置文件加载完毕!");
	  }
	  
	  public static IServiceLocator getInstance(){
		  return iServiceLocator;
	  }
	  
	  /**
	   * add by hulei
	   * 本方法紧紧提供本类的main函数使用。
	   */
	  public  void doNothing(){
		  
	  }
	
	  public static void main(final String[] args) {
	    IServiceLocator serviceLocatorImpl = ServiceLocatorImpl.getInstance();
	    serviceLocatorImpl.doNothing();
	    
	  }
	  
	
	}
