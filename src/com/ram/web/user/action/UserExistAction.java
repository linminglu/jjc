package com.ram.web.user.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.framework.web.action.BaseDispatchAction;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.service.user.IUserService;

public class UserExistAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService) getService("userService");

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		// request.setCharacterEncoding("utf-8");
		// response.setCharacterEncoding("utf-8");
		// response.setContentType("text/html;charset=utf-8");
		String loginName = request.getParameter("loginName");
		boolean isExist = userService.checkUserExist(loginName);
		PrintWriter out = response.getWriter();
		out.println(isExist);
		out.flush();// 清空缓冲区
		out.close();// 关闭输入流
		return null;
	}
}
