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
public class ManagerPositionCreateAction extends BaseDispatchAction {
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	
	/** 
	 * 管理者可选职位列表页面初始化
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
		List positionList = managerPositionService.getManagerPosition(managerId);
		request.setAttribute("positionList", positionList);
		return mapping.findForward("init");
	}
	
	/** 
	 * 添加管理者的职位
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
  		
		if(request.getParameterValues("selIndex") != null){
			int managerId = Integer.parseInt(request.getParameter("managerId"));
			String[] positionIndexes = request.getParameterValues("selIndex");
			int[] positionId = new int[positionIndexes.length];
			for(int i = 0;i < positionId.length;i++){
				positionId[i] = Integer.parseInt(positionIndexes[i]);
			}
			managerPositionService.addManagerPositionRL(managerId, positionId, user);
		}
		return mapping.findForward("create");
	}
}
