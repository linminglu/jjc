package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IBusinessGroupService;
import com.ram.service.user.IUserService;
import com.framework.web.action.BaseDispatchAction;
import com.framework.util.ParamUtils;

public class BusinessGroupListAction extends BaseDispatchAction {
	// <forward name="init" path="/businessGroupList.jsp" />
	// <forward name="delete" path="/businessGroupAction.do?method=delete"/>

	private final IBusinessGroupService businessGroupService = (IBusinessGroupService) getService("businessGroupService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		List businessGroupList = this.businessGroupService
//				.findAllBusinessGroup();
		List businessGroupList = this.businessGroupService.findMyCreatedBusinessGroup(user.getUserId().intValue());
		request.setAttribute("businessGroupList", businessGroupList);
		this.saveToken(request);
		return mapping.findForward("init");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		int businessGroupId = ParamUtils.getIntParameter(request,
				"businessGroupId", 0);
		businessGroupService.deleteBusinessGroup(businessGroupId, user);

		this.saveToken(request);
		return mapping.findForward("delete");
	}

}
