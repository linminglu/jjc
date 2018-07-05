package com.framework.service;

public interface IServiceLocator {
    public Object getService(final String serviceName) ;
    
	  /**
	   * add by hulei
	   * 本方法紧紧提供本类的main函数使用。
	   */
    public void doNothing();
}
