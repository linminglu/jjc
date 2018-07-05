package com.framework.web.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.ram.RamConstants;
import com.ram.model.User;


public class SessionListener implements HttpSessionListener,
		HttpSessionAttributeListener {

	static {
		System.out.println("Session Listener Startup...");
		System.out.println("Webcontext:"+RamConstants.getFileServerContext());
	}

	protected final Log log = LogFactory.getLog(getClass());

	private static int onlineTotalUser = 0;// 在线人数


	private static List onlineRegistedUserList = new ArrayList();

	/**
	 * 仅仅统计在线人数
	 */
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		onlineTotalUser++;
		//log.info("1111111在线总人数统计," + new java.util.Date() + ",Session更新++");
	}

	/**
	 * 仅仅统计在线总人数
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		onlineTotalUser--;
		if (onlineTotalUser < 0)
			onlineTotalUser = 0;
		
	    HttpSession session=se.getSession();  
//        ServletContext application=session.getServletContext();  
      
        try{  
        	String loginName="";
        	 if(session.getAttribute("loginName")!=null){
        		 loginName=session.getAttribute("loginName").toString();  
        	 }
             Map<String, HttpSession> USER_SESSION=Constants.getUserSession();
     	     Map<String, String> SESSIONID_USER=Constants.getSessionIdUser();
    	     String sessionId=session.getId();  
     	     USER_SESSION.remove(SESSIONID_USER.remove(sessionId));
     	     session=USER_SESSION.remove(loginName);
	   	     if(session!=null&&ParamUtils.chkString(loginName)){
		         SESSIONID_USER.remove(session.getId()); 
		         session.removeAttribute("loginName");
		     } 
         }  
         catch(Exception e){  
                System.out.println(e.getMessage());  
         }  
		
		
	}

	/**
	 * 已经登录的注册用户处理
	 */
	public void attributeAdded(HttpSessionBindingEvent se) {
		if ("loginUser".equals(se.getName())) {
			User user = (User) se.getSession().getAttribute("loginUser");
			log.info("系统Session检测到用户登录：userid=" + user.getUserId().intValue()
					+ ",loginName=" + user.getLoginName() + ",username="
					+ user.getUserNameZh());

			for(int i=0;i<onlineRegistedUserList.size();i++){
				User tmpUser=(User)onlineRegistedUserList.get(i);
				
				if(tmpUser.getLoginName().equals(user.getLoginName())){
					//log.info("该用户已经存在，现在删除,........");
					onlineRegistedUserList.remove(i);
				}else{
					//log.info("该用户还不存在。。。。");
				}
			}
				onlineRegistedUserList.add(se.getValue());

			log.info("用户" + user.getLoginName() + "在"
					+ DateTimeUtil.getDateTime() + "登录");

		}

	}

	/**
	 * 已经登录用户退出时候的处理
	 */
	public void attributeRemoved(HttpSessionBindingEvent se) {
		//log.info("在AttributeRemove方法中");
		//log.info("se.getName()"+se.getName());
		if ("loginUser".equals(se.getName())) {
			//log.info("44444444remove:" + se.getName());
			onlineRegistedUserList.remove(se.getValue());

		}
		//log.info("xxx");
	}

	/**
	 * 返回所有在线人数
	 * 
	 * @return
	 */
	public static int getOnlineTotalUser() {
		return onlineTotalUser;
	}

	/**
	 * 得到在线注册用户数
	 * 
	 * @return
	 */
	public int getOnlineRegUser() {
		return this.onlineRegistedUserList.size();
	}

	public static int getOnlineRegistedUser() {
		return onlineRegistedUserList.size();
	}

	public static List getOnlineReistedUserList() {
		return onlineRegistedUserList;
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
