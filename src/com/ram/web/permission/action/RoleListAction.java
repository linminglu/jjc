package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.permission.IRoleService;
import com.framework.web.action.BaseDispatchAction;
import com.framework.util.ParamUtils;

public class RoleListAction extends BaseDispatchAction {
	private IRoleService roleService = (IRoleService) getService("roleService");

	/**
	 * 显示列表
	 * 
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
		
		//得到所有有效的记录(status不为0的数据)
		List list = roleService.findAllValidRoles();
		request.setAttribute("roleList", list);

		return mapping.findForward("init");
	}

	/**
	 * 删除
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("test-------------------");
		String id = ParamUtils.getParameter(request, "roleId");

		// 把status设为无效状态
		//从session中取得User对象
		User user = null;
		//roleService.removeRole(Integer.valueOf(id),user);
		roleService.deleteRole(Integer.valueOf(id),user);
		return mapping.findForward("list");
	}

}
