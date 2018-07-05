package com.ram.web.permission.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.service.permission.ITreeService;
import com.framework.web.action.BaseDispatchAction;

/**
 * TreeCreateAction用于生成系统功能的树状结构菜单
 * @author admin
 *
 */
public class TreeCreateAction extends BaseDispatchAction {
	public ITreeService treeService = (ITreeService) getService("treeService");
	
	/**
	 * 生成树型菜单的action方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward gen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		treeService.execute();
		return mapping.findForward("genok");
	}
}
