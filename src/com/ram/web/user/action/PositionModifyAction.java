package com.ram.web.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Position;
import com.ram.model.User;
import com.ram.service.user.IManagerPositionService;
import com.ram.web.user.form.PositionForm;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class PositionModifyAction extends BaseDispatchAction {
	
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	
	/** 
	 * Position修改页面初始化
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
		Integer positionId = Integer.valueOf(request.getParameter("positionId"));
		Position position = (Position)managerPositionService.getPosition(positionId);
		PositionForm positionModifyForm = (PositionForm)form;
		positionModifyForm.setPosition(position);
        request.setAttribute("positionId", request.getParameter("positionId"));
        request.setAttribute("positionModifySaveForm", positionModifyForm);
		return mapping.findForward("init");
	}
	
	/** 
	 * Position的修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward modify(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		Integer positionId = Integer.valueOf(request.getParameter("positionId"));
		Position position = (Position)managerPositionService.getPosition(positionId);
		PositionForm positionForm = (PositionForm)form;
		position.setPositionTitle(positionForm.getPosition().getPositionTitle());
		managerPositionService.savePosition(position, user);
		return mapping.findForward("modify");
	}
}
