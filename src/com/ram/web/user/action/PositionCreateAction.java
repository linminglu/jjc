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
public class PositionCreateAction extends BaseDispatchAction {
	
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	
	/** 
	 * Position列表页面初始化
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
		return mapping.findForward("init");
	}
	
	/** 
	 * Position的创建
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
  		
		PositionForm positionForm = (PositionForm)form;
		Position position = positionForm.getPosition();
		managerPositionService.savePosition(position, user);
		return mapping.findForward("create");
	}
}
