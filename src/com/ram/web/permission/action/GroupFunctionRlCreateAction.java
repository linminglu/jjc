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
import com.ram.service.permission.ITreeForUserGroupService;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class GroupFunctionRlCreateAction extends BaseDispatchAction {
	private IGroupFunctionRlService groupFunctionRlService = (IGroupFunctionRlService)getService("groupFunctionRlService");
	private IFunctionService functionService=(IFunctionService)getService("functionService");
	private IUserService userService=(IUserService)getService("userService");
	private ITreeForUserGroupService userGroupTreeService=(ITreeForUserGroupService)getService("treeForUserGroupService");
	public void setGroupFunctionRlService(
			IGroupFunctionRlService groupFunctionRlService) {
		this.groupFunctionRlService = groupFunctionRlService;
	}
	
	/**
	 * 进入功能分配的初始界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException {
		//Integer groupId=(Integer)request.getAttribute("groupId");
		int userGroupId=ParamUtils.getIntAttribute(request,"userGroupId",0);
		int parentFunctionId=ParamUtils.getIntAttribute(request,"parentFunctionId",0);
		//将当前用户组和父功能对象取出来放在request中
		UserGroup userGroup=userService.getUserGroupByID(userGroupId);
		request.setAttribute("userGroup",userGroup);
		Function parentFunction =null;
		if(parentFunctionId==0){
			//如果是0，则设置为根功能
			parentFunction=new Function();
			parentFunction.setFunctionId(new Integer(0));
			parentFunction.setFunctionTitle("根");
		}else{
			parentFunction=functionService.getFunction(new Integer(parentFunctionId));
		}
		request.setAttribute("parentFunction",parentFunction);
		
		List functionList=functionService.findAllSubFunctionsNotBelongGroup(parentFunctionId,userGroupId);
		request.setAttribute("functionList",functionList);
		
		return mapping.findForward("init");
	}
	
	/**
	 * 将某个功能分配各某个组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		int userGroupId=ParamUtils.getIntParameter(request,"userGroupId",0);
		int functionId=ParamUtils.getIntParameter(request,"functionId",0);
		int parentFunctionId=ParamUtils.getIntParameter(request,"parentFunctionId",0);
		
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		request.setAttribute("userGroupId",String.valueOf(userGroupId));
		request.setAttribute("functionId",String.valueOf(functionId));
		request.setAttribute("parentFunctionId",String.valueOf(parentFunctionId));
		
		if(userGroupId!=0 && functionId!=0){
			GroupFunctionRl groupFunctionRl=new GroupFunctionRl();
			groupFunctionRl.setFunctionId(new Integer(functionId));
			groupFunctionRl.setGroupId(new Integer(userGroupId));
			log.info("save order sn");
			groupFunctionRl.setOrderSn(new Integer(0));
			log.info("order sn have saved");
			groupFunctionRlService.saveGroupFunctionRl(groupFunctionRl,user);
			
			userLogService.saveLog(user,"用户组id=" + userGroupId+",所分配的功能id=" + functionId +",功能的父功能id=" + parentFunctionId);
		}
		//分配菜单以后，重新生成该组的用户树菜单
		userGroupTreeService.execute(userGroupId);
		userLogService.saveLog(user,"生成了用户组userGroupId="+userGroupId+"的树状菜单");
		return mapping.findForward("save");
	}
	
	public final ActionForward addAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int userGroupId=ParamUtils.getIntParameter(request,"userGroupId",0);
		int parentFunctionId=ParamUtils.getIntParameter(request,"parentFunctionId",0);
		
		request.setAttribute("userGroupId",String.valueOf(userGroupId));
		request.setAttribute("parentFunctionId",String.valueOf(parentFunctionId));
		if(userGroupId!=0 ){
			groupFunctionRlService.addSubFunctionsToGroup(parentFunctionId,userGroupId,user);
			userLogService.saveLog(user,"把当前功能：functionId="+parentFunctionId+"下的所有直接子功能分配给组：userGroupId=" + userGroupId);
		}
		
		userGroupTreeService.execute(userGroupId);
		
		userLogService.saveLog(user,"生成了用户组userGroupId="+userGroupId+"的树状菜单");		
		return mapping.findForward("addAll");		
		
	}
}
