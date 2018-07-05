package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Function;
import com.ram.model.GroupFunctionRl;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.permission.IFunctionService;
import com.ram.service.permission.IGroupFunctionRlService;
import com.ram.service.user.IUserService;
import com.ram.web.permission.form.GroupFunctionRlForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class GroupFunctionRlListAction extends BaseDispatchAction {
	private IGroupFunctionRlService groupFunctionRlService = (IGroupFunctionRlService) getService("groupFunctionRlService");

	private IFunctionService functionService = (IFunctionService) getService("functionService");

	private IUserService userService = (IUserService) getService("userService");

	public void setGroupFunctionRlService(
			IGroupFunctionRlService groupFunctionRlService) {
		this.groupFunctionRlService = groupFunctionRlService;
	}

	public final ActionForward afterSave(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		int userGroupId = 0;
		int parentFunctionId = 0;

		userGroupId = ParamUtils.getIntAttribute(request, "userGroupId", 0);
		parentFunctionId = ParamUtils.getIntAttribute(request,
				"paerntFunctionId", 0);

		request.setAttribute("userGroupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));

		UserGroup userGroup = null;
		if (userGroupId == 0) {
			log.error("在进入用户组功能分配界面的时候出现错误：没有指定分配的用户组！");
			log.error("error at:GroupFunctionRlListAction.java---afterSave()");
		} else {
			userGroup = (UserGroup) userService.getUserGroupByID(userGroupId);
		}
		request.setAttribute("userGroup", userGroup);

		GroupFunctionRlForm groupFunctionRlForm = (GroupFunctionRlForm) form;
		groupFunctionRlForm.setFunctionId(new Integer(parentFunctionId));
		groupFunctionRlForm.setGroupId(new Integer(userGroupId));
		// 获取当前用户当前功能下已经分配的功能
		List functionList = groupFunctionRlService.findSubFunctionsByGroup(
				new Integer(userGroupId), new Integer(parentFunctionId));

		if (functionList.size() > 0) {
			request.setAttribute("functionList", functionList);
		}

		// 获取该级菜单下可以分配的功能：
		List systemFunctionList = functionService
				.findAllSubFunctions(parentFunctionId);
		request.setAttribute("systemFunctionList", systemFunctionList);
		return mapping.findForward("afterSave");
	}

	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);

		request.setAttribute("userGroupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));
		//将当前选择的用户组对象存入request
		UserGroup userGroup = null;
		if (userGroupId == 0) {
			log.error("在进入用户组功能分配界面的时候出现错误：没有指定分配的用户组！");
			log.error("error at:GroupFunctionRlListAction.java---init()");
		} else {
			userGroup = (UserGroup) userService.getUserGroupByID(userGroupId);
		}
		request.setAttribute("userGroup", userGroup);
		//将当前的父功能存入request
		Function parentFunction=null;
		if (parentFunctionId == 0) {
			parentFunction=new Function();
			parentFunction.setFunctionId(new Integer(0));
			parentFunction.setFunctionTitle("根");
		} else {
			parentFunction=functionService.getFunction(new Integer(parentFunctionId));
		}		
		request.setAttribute("parentFunction",parentFunction);
		//
		
		GroupFunctionRlForm groupFunctionRlForm = (GroupFunctionRlForm) form;
		groupFunctionRlForm.setFunctionId(new Integer(parentFunctionId));
		groupFunctionRlForm.setGroupId(new Integer(userGroupId));
		// 获取当前用户当前功能下已经分配的功能
		List functionList = groupFunctionRlService.findSubFunctionsByGroup(
				new Integer(userGroupId), new Integer(parentFunctionId));

		if (functionList.size() > 0) {
			request.setAttribute("functionList", functionList);
		}

		// 获取该级菜单下可以分配的功能：
		List systemFunctionList = functionService
				.findAllSubFunctions(parentFunctionId);
		request.setAttribute("systemFunctionList", systemFunctionList);
		return mapping.findForward("init");
	}

	/**
	 * 删除某个组下已经分配的功能
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		request.setAttribute("groupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));
		groupFunctionRlService.removeFunctionOfGroup(new Integer(userGroupId), new Integer(functionId),user);
		return mapping.findForward("remove");
	}

	/**
	 * 排序上升
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward orderUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		request.setAttribute("groupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));
		
		groupFunctionRlService.modifyOrderSn(userGroupId, functionId,1,user);
		
		return mapping.findForward("remove");
	}	
	
	public final ActionForward orderDown(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		request.setAttribute("groupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));
		groupFunctionRlService.modifyOrderSn(userGroupId, functionId,-1,user);
		return mapping.findForward("remove");
	}
	
	public final ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		
		
		request.setAttribute("userGroupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));

		return mapping.findForward("create");
	}
	
	
	public final ActionForward addAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException {
		int userGroupId = ParamUtils.getIntParameter(request, "userGroupId", 0);
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		
		request.setAttribute("userGroupId", String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId", String
				.valueOf(parentFunctionId));

		return mapping.findForward("addAll");		
	
		
	}
	
	}
