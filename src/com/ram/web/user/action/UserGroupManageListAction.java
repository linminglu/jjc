package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class UserGroupManageListAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	
	/** 
	 * 管理者的职位列表页面初始化
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {

		int userId=ParamUtils.getIntParameter(request,"userId",0);
		User selectedUser=null;
		if(userId!=0){
			selectedUser=userService.getUser(userId);
		}else{
			//如果是从删除用户组界面过来，获得当前被删用户组用户的userId
			userId=ParamUtils.getIntParameter(request,"deleteUserId",0);
			selectedUser=userService.getUser(userId);
		}
		request.setAttribute("selectedUser",selectedUser);
		log.info("xxxxx,xxx,userId=" + userId);
		List userGroupList = userService.getUserGroupById(userId);
		
		request.setAttribute("userGroupList", userGroupList);
		request.setAttribute("userId", request.getParameter("userId"));
		log.info("selected user is :" + selectedUser.getLoginName());
		if(selectedUser.getUserType().equals(RamConstants.UserTypeIsTutor1)){
			log.info("goto tutor");
			return mapping.findForward("tutorInit");
		}else{
			log.info("goto manager");
			return mapping.findForward("managerInit");
		}
		
//		if("manager".equals(request.getParameter("type"))){
//			log.info("goto managerInit");
//			return mapping.findForward("managerInit");
//		}
//		else{
//			log.info("goto tutorInit");
//			return mapping.findForward("tutorInit");
//		}
	}
	
	/** 
	 * 删除指定用户组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		int userGroupId = Integer.parseInt(request.getParameter("userGroupId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		request.setAttribute("deleteUserId",new Integer(userId));
		userService.deleteUserAndGroupRl(userId, userGroupId, user);
		return mapping.findForward("delete");
	}
}

