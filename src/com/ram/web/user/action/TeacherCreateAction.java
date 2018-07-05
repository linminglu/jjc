package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserGroupRl;
import com.ram.model.UserRoleRl;
import com.ram.service.permission.IRoleService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.TeacherForm;
import com.ram.web.user.form.UserForm;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class TeacherCreateAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService)getService("userService");
	private final IRoleService roleSerivce = (IRoleService)getService("roleService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");

	/** 
	 * 显示新建teacher页面
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
		List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
		Iterator item = tutorCenterList.iterator();
		Collection result = new ArrayList();
		while(item.hasNext()){
			TutorCenter tutorCenter = (TutorCenter)item.next();
			LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
			result.add(labelValueBean);
		}
		request.setAttribute("tcList", result);
		return mapping.findForward("init");
	}
	
	/** 
	 * 创建teacher,返回teacher列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward create(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		TeacherForm teacherform = (TeacherForm)form;
		Tutor tutorForm = teacherform.getTeacher();
		HttpSession session = request.getSession(false);
		
		if(session.getAttribute("userForm") != null){
			// 创建管理者信息
			UserForm userForm = (UserForm)session.getAttribute("userForm");
			tutorForm.setBirthday(teacherform.getBirthday() == null ? null : new java.sql.Date(teacherform.getBirthday().getTime()));
			tutorForm.setUser(userForm.getUser());
			tutorForm.setTutorCenter(systemTutorCenterService.getSystemTutorCenter(teacherform.getTcId()));
			Tutor tutor = userService.saveTutor(tutorForm, user);
			UserGroupRl userGroupRl = new UserGroupRl();
			UserRoleRl userRoleRl = new UserRoleRl();
			User userInfo = tutor.getUser();
			userGroupRl.setUser(userInfo);
			userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(userForm.getUserGroup())));
			userService.saveUserGroupRl(userGroupRl, user);
			userRoleRl.setUser(userInfo);
			userRoleRl.setRole(roleSerivce.getRole(Integer.valueOf(userForm.getRol())));
			roleSerivce.saveUserRoleRl(userRoleRl, user);
			session.removeAttribute("userForm");	
		}
		return mapping.findForward("create");
	}
	
	
}
