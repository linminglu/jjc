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
public class PositionListAction extends BaseDispatchAction {
	
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	
	/** 
	 * 显示Position管理列表页面初始化
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
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		List positionList = managerPositionService.getAllPosition();
		request.setAttribute("positionList", positionList);
		return mapping.findForward("init");
	}
	
	/** 
	 * 删除指定position
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
  		
		Integer positionId = Integer.valueOf(request.getParameter("positionId"));
		managerPositionService.deletePosition(positionId, user);
		return mapping.findForward("delete");
	}
}
