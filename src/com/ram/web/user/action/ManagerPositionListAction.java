package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.user.IManagerPositionService;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class ManagerPositionListAction extends BaseDispatchAction {
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	
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
		int managerId = Integer.parseInt(request.getParameter("managerId"));
		List positionList = managerPositionService.getManagerPositionRL(managerId);
		request.setAttribute("positionList", positionList);
		request.setAttribute("managerId", request.getParameter("managerId"));
		return mapping.findForward("init");
	}
	
	/** 
	 * 删除指定管理者的职位
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
  		
		int positionId = Integer.parseInt(request.getParameter("positionId"));
		int managerId = Integer.parseInt(request.getParameter("managerId"));
		managerPositionService.deleteManagerPositionRL(managerId, positionId, user);
		return mapping.findForward("delete");
	}
}
