package com.ram.web.user.action;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class DeskMobileRegisterAction extends BaseDispatchAction{

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException {
		String referer = request.getHeader("Referer");
		String reqURL = request.getRequestURL().toString();
		log.fatal("============Referer=" + referer);//页面访问来源
		log.fatal("============reqURL=" + reqURL);//请求页面地址

		return mapping.findForward("init");
	}

	
	public void launch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData = userService.updateMobileLaunch(code, sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
	public void trial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
		String code = ParamUtils.getParameter(request,"softwareCode");
		String sn = ParamUtils.getParameter(request, "sn","").trim();
		String mac = ParamUtils.getParameter(request, "mac","").trim();
		String v = ParamUtils.getParameter(request, "v","").trim();
		String ip = request.getRemoteAddr();
		
		String returnData = userService.updateMobileTrial(code, sn, mac, v, ip);
		
		//response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out  = response.getWriter();
		out.println(returnData);
		out.flush();
		out.close();
	}
	
}