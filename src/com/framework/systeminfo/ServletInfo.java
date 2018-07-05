package com.framework.systeminfo;

import javax.servlet.ServletContext;
/**
 * @author zhangkeyi
 *
 * 读取当前的servlet版本信息，反映当前系统状态
 * 
 */
public class ServletInfo {

    private String serverInfo   = null;
    private int majorVersion    = 0;
    private int minorVersion    = 0;
    private String servletVersion = null;

    public ServletInfo(ServletContext context) {
        serverInfo      = context.getServerInfo();
        majorVersion    = context.getMajorVersion();
        minorVersion    = context.getMinorVersion();
        servletVersion  = new StringBuffer().append(majorVersion).append('.').append(minorVersion).toString();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public String getServletVersion() {
        return servletVersion;
    }
}
