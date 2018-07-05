package com.ram.web.permission.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Function;
import com.ram.model.User;
import com.ram.service.permission.IFunctionService;
import com.ram.web.permission.form.FunctionForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class FunctionListAction extends BaseDispatchAction {

	public IFunctionService functionService = (IFunctionService) getService("functionService");

	/**
	 * 功能列表的init入口方法
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
		
		FunctionForm functionForm = (FunctionForm) form;
		// 获取父function
		
		int parentFunctionId = ParamUtils.getIntParameter(request,
				"parentFunctionId", 0);
		if (parentFunctionId == 0) {
			parentFunctionId = ParamUtils.getIntAttribute(request,
					"parentFunctionId", 0);
		}
		
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
		int startIndex = 0;
		PaginationSupport ps = functionService.findALLSubFunctionsPage(
				parentFunctionId, startIndex);
		List functionList = ps.getItems();
		for (int i = 0; i < functionList.size(); i++) {
			Function f = (Function) functionList.get(i);
		}
		request.setAttribute("functionList", functionList);
		request.setAttribute("ps", ps);
		request.setAttribute("parentFunctionId", new Integer(parentFunctionId));
		saveToken(request);
		return mapping.findForward("init");
	}

	public final ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		Function function = functionService
				.getFunction(new Integer(functionId));
		functionService.removeFunction(functionService.getFunction(new Integer(
				functionId)), user);
		request.setAttribute("parentFunctionId", String.valueOf(function
				.getParentId()));
		userLogService.saveLog(user, "删除了一个功能:" + function.getFunctionTitle());
		return mapping.findForward("delete");
	}

	public final ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		request.setAttribute("functionId", new Integer(functionId));

		return mapping.findForward("modify");
	}

	/**
	 * 上调顺序
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward orderUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		Function f = (Function) functionService.getFunction(new Integer(
				functionId));
		if (f != null) {
			functionService.saveOrderUp(functionId, user);
			request.setAttribute("functionId", new Integer(functionId));
			request.setAttribute("parentFunctionId", f.getParentId().intValue()
					+ "");
		}
		return mapping.findForward("orderUp");
	}

	/**
	 * 下调顺序
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward orderDown(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		int functionId = ParamUtils.getIntParameter(request, "functionId", 0);
		
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		Function f = (Function) functionService.getFunction(new Integer(
				functionId));
		if (f != null) {
		
			functionService.saveOrderDown(functionId, user);
		
			request.setAttribute("functionId", new Integer(functionId));
			request.setAttribute("parentFunctionId", f.getParentId().intValue()
					+ "");
		}
		return mapping.findForward("orderDown");
	}
}
