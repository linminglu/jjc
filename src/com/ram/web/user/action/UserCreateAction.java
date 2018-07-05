package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.ram.model.Role;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.permission.IRoleService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.UserForm;
import com.framework.util.DateTimeUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
/**
 * @author lixiaodong 
 */
public class UserCreateAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final IRoleService roleService = (IRoleService)getService("roleService");
	
	/**
	 * 显示用户创建页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userGreateForm = (UserForm)form;
		if(request.getParameter("type") != null){
			if(request.getParameter("type").equals("manager")){
				//创建管理员
				userGreateForm.setUserKind(2);
			}
			else if(request.getParameter("type").equals("teacher")){
				//创建教师
				userGreateForm.setUserKind(1);
			}
			else{
				//创建学生
				userGreateForm.setUserKind(3);
			}
		}
		List groupInfo = userService.getAllUserGroup();
		List roleInfo = roleService.findAllValidRoles();
		Iterator groupItem = groupInfo.iterator();
		Iterator roleItem = roleInfo.iterator();
		List groupList = new ArrayList();
		List roleList = new ArrayList();
		while(groupItem.hasNext()){
			UserGroup userGroup = (UserGroup)groupItem.next();
			LabelValueBean itemInfo = new LabelValueBean(userGroup.getUserGroupName(), userGroup.getUserGroupId().toString());
			groupList.add(itemInfo);
		}
		while(roleItem.hasNext()){
			Role role = (Role)roleItem.next();
			LabelValueBean itemInfo = new LabelValueBean(role.getRoleName(), role.getRoleId().toString());
			roleList.add(itemInfo);
		}
		request.getSession().setAttribute("userGroupCollection", groupList);
		request.getSession().setAttribute("roleCollection", roleList);
		request.setAttribute("userCreateSaveForm", userGreateForm);
		return mapping.findForward("init");
	}
	
	/**
	 * 创建用户，并显示创建用户的选择以便创建老师和管理者
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm)form;
		User user = userForm.getUser();
		user.setStatus("1");
		user.setLastLoginIp(request.getRemoteAddr());
		user.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
		user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
		user.setLoginTimes(new Integer(0));
		// 根据userKind判断创建管理者还是创建老师
		if(userForm.getUserKind()==Integer.parseInt(RamConstants.UserTypeIsTutor1)){
			//创建老师用户
			user.setUserType(RamConstants.UserTypeIsTutor1);
		}
		else{
			//创建管理用户
			user.setUserType(RamConstants.UserTypeIsManager2);
		}		
		request.getSession(false).setAttribute("userForm", userForm);
		if(userService.checkUserExist(user.getLoginName())){
			request.setAttribute("loginName_exist", "1");
			return mapping.findForward("init");
		}
		else{
			request.getSession().removeAttribute("userGroupCollection");
			request.getSession().removeAttribute("roleCollection");
		}
		// 根据userKind判断创建管理者还是创建老师
		if(1 == userForm.getUserKind()){
			//创建老师用户
			return mapping.findForward("createTutor");
		}
		else{
			//创建管理用户
			return mapping.findForward("createManager");
		}
	}
	
	/**
	 * 检查输入的用户名是否存在
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserForm userForm = (UserForm)form;
		User user = userForm.getUser();
		if(!"".equals(user.getLoginName())){
			if(userService.checkUserExist(user.getLoginName())){
				request.setAttribute("loginName_exist", "1");
			}
			else{
				request.setAttribute("loginName_notExist", "1");
			}
		}
		else{
			request.setAttribute("loginName_null_error", "1");
		}
		request.setAttribute("userCreateSaveForm", userForm);
		return mapping.findForward("init");
	}
}
