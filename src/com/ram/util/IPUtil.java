package com.ram.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.common.properties.IReadProperties;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;

public class IPUtil {
	protected static final Log log = LogFactory.getLog(IPUtil.class);
	private static IServiceLocator service = ServiceLocatorImpl.getInstance();
	private static IReadProperties read = (IReadProperties) service
			.getService("readProperties");

	/**
	 * 得到允许进行费用处理的IP地址
	 * 
	 * @return
	 */
	public static String getFeeAllowedIP() {
		String ip = read.getValue("SYSTEM.FEE.OPERATE.IP");

		return ip;
	}

	/**
	 * 得到允许费用操作的用户名
	 * 
	 * @return
	 */
	public static String getFeeOperaterLoginName() {
		String operator = read.getValue("SYSTEM.FEE.OPERATER.LOGINNAME");
		return operator;
	}

	/**
	 * 判断一个用户名，是否是允许费用操作的用户名
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isFeeAllowedLoginName(String loginName) {
		// log.info("判断用户是否能操作费用：loginName=" + loginName);
		// 注意，此处必须加上一个,号，用于区分不同配置文件中不同的的ip
		loginName = loginName + ",";
		String allowedLoginNames = getFeeOperaterLoginName();
		if (allowedLoginNames.indexOf("free") >= 0) {
			return true;
		}
		if (allowedLoginNames == null || allowedLoginNames.equals(""))
			return false;// allowedIP==null时或者设置的值为空时,表示禁止
		// allowedLoginNames = "," + allowedLoginNames + ",";

		// log.info("allowedLoginNames=" + allowedLoginNames);
		// log.info("allowedLoginNames.indexOf("+loginName+")=" +
		// allowedLoginNames.indexOf(loginName));
		return allowedLoginNames.indexOf(loginName) > -1 ? true : false;
	}

	/**
	 * 判断request来的ip是否是公司和网院局域网内的ip地址 该方法主要用来判断外网登陆和公司内网登陆是否需要验证码。
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isIntranetIP(HttpServletRequest request) {
		String ip = request.getRemoteHost();
		if (ip == null) {
			return false;
		}
		if (ip.indexOf("127.0.0.1") >= 0 || ip.indexOf("192.168.0") >= 0
				|| ip.indexOf("221.122.40.98") >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 判断传入的IP地址，是否是允许费用操作的地址
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isFeeAllowedIP(String ip) {
		// 注意，此处必须加上一个,号，用于区分不同配置文件中不同的的ip
		ip = ip + ",";
		String allowedIP = getFeeAllowedIP();
		if (allowedIP.indexOf("free") >= 0) {
			return true;
		}
		if (allowedIP == null || allowedIP.equals(""))
			return true;// allowedIP==null时,表示不禁止
		allowedIP = "," + allowedIP + "";
		return allowedIP.indexOf(ip) > -1 ? true : false;
	}

	/**
	 * 获取真实的ip地址
	 * @param request
	 * @return
	 * @author Mr.zang
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    	ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			
			if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")){  
                //根据网卡取本机配置的IP  
                InetAddress inet=null;  
                try {  
                    inet = InetAddress.getLocalHost();  
                    ip= inet.getHostAddress();  
                } catch (UnknownHostException e) {  
//                    e.printStackTrace();  
                }  
            }  
			
		}
		
		if(ip!=null && ip.length()>1){ //"***.***.***.***".length() = 15  
            if(ip.indexOf(",")>0){  
            	ip = ip.substring(0,ip.indexOf(","));  
            }  
        }  
		
		return ip;
	}

//	 public static String getIpAddr(HttpServletRequest request) {
//		    String ip = request.getHeader("x-forwarded-for");
//		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//		      ip = request.getHeader("Proxy-Client-IP");
//		    }
//		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//		      ip = request.getHeader("WL-Proxy-Client-IP");
//		    }
//		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//		      ip = request.getHeader("HTTP_CLIENT_IP");
//		    }
//		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//		      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//		    }
//		    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//		      ip = request.getRemoteAddr();
//		    }
//		    return ip;
//		  }
	
	
	public static String getRequestIP(HttpServletRequest request){
		String ip = request.getRemoteAddr();
		if(ip==null || ip.length()==0 || ip.startsWith("127.")){
			ip = request.getHeader("x-forwarded-for");
		}
		if(ip==null || ip.length()==0 || ip.startsWith("127.")){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		return ip;
	}
	public static void main(String[] args) {
		String ip="203.82.42.30,203.82.42.31";
		
		if(ip!=null && ip.length()>1){ //"***.***.***.***".length() = 15  
            if(ip.indexOf(",")>0){  
            	ip = ip.substring(0,ip.indexOf(","));  
            }  
        }  
		
	}
	
}
