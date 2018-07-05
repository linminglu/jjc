package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.OnLineUserRecord;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class OnlineUserAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	/* forward name="success" path="/onlineUser.jsp" */
	private final static String SUCCESS = "success";

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
	    List userList=com.framework.web.servlet.SessionListener.getOnlineReistedUserList();
	    if(userList==null){
	    	userList=new ArrayList();
	    }
	    
	    String serverName = request.getServerName();
	    Integer userRecordNumber = new Integer( userList.size());
	    Date currentTime = DateTimeUtil.getCurrentDate();
	    
	    String str = "2";
	    if( str.equals( user.getUserType() ) ){
		    OnLineUserRecord our = new OnLineUserRecord();
		    our.setServerName( serverName );
		    our.setUserRecordNumber( userRecordNumber );
		    our.setRecordTime( currentTime );
		    userService.saveOnlineUserRecord( our,user );
	    }
	    
		log.info("在线用户列表:当前共有"+userList.size()+"名用户在线");
		this.userLogService.saveLog(user,"访问在线用户列表，当前在线用户共"+userList.size()+"人");
	    List onLineUserList= userService.findOnlineUserDTOs(userList);
	    request.setAttribute("onLineUserList",onLineUserList);
	    request.setAttribute("loginUser",loginedUser);
		return mapping.findForward(this.SUCCESS);
	}
}
