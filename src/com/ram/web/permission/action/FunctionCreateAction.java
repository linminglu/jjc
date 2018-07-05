package com.ram.web.permission.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.Function;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;
import com.ram.web.permission.form.FunctionForm;
import com.framework.web.action.BaseDispatchAction;

import com.framework.util.ParamUtils;

public class FunctionCreateAction extends BaseDispatchAction {
	public IFunctionService functionService = (IFunctionService) getService("functionService");

	/**
	 * 添加功能的init入口方法
	 * 
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
		saveToken(request);
		// 获取父function
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		Function parentFunction = null;
		if (parentFunctionId == 0) {
			parentFunction = new Function();
			parentFunction.setFunctionId(new Integer(0));
			parentFunction.setFunctionTitle("根");
			parentFunction.setFunctionLinkAddress("");
		} else {
			parentFunction = functionService.getFunction(new Integer(
					parentFunctionId));
		}
		// 将父function写入attribute
		request.setAttribute("parentFunction", parentFunction);
		return mapping.findForward("init");
	}

	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		// 获取当前要创建的功能的父ID
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		log.info("parentFunctionId=" + parentFunctionId);

		if (isTokenValid(request)) {
			// 获取formbean
			FunctionForm functionCreateForm = (FunctionForm) form;

			// 创建新function实例
			Function function = new Function();

			function.setFunctionTitle(functionCreateForm.getFunction()
					.getFunctionTitle());
			function.setFunctionLinkAddress(functionCreateForm.getFunction()
					.getFunctionLinkAddress());

			function
					.setSubFunctionNumber(RamConstants.FUNCTION_NONE_SUB_FUNCTION);
			function.setParentId(new Integer(parentFunctionId));
			function.setIsSystemFunction("0");
			function.setOrderSn(new Integer(0));

			// 将父function的subFunctionNumber字段改为1
			if (parentFunctionId > 0) {

				Function parentFunction = functionService
						.getFunction(new Integer(parentFunctionId));

				if (parentFunction != null) {

					parentFunction
							.setSubFunctionNumber(RamConstants.FUNCTION_SUB_FUNCTION_NUMBER);

					functionService.saveFunction(parentFunction, user);

					function.setRootId(parentFunction.getRootId());

				} else {

					function.setRootId(new Integer(0));
				}

			} else {
				function.setRootId(new Integer(0));

			}

			// 保存新的function
			functionService.saveFunction(function, user);

			userLogService.saveLog(user, "创建了一个新的功能:"
					+ function.getFunctionTitle() + "!");

			//
			resetToken(request);
		}
		return mapping.findForward("save");
	}
}
