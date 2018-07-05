package com.ram.web.system.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class SemesterListAction extends BaseDispatchAction {

	ISemesterService semesterService = (ISemesterService) getService("semesterService");

	/**
	 * 学期列表初始化
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

		//Semester semester = null;
		//SemesterForm semesterForm = (SemesterForm) form;
		List list = semesterService.findSemesters();

		request.setAttribute("semesterList", list);
		return mapping.findForward("init");
	}

	/**
	 * 删除学期
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public final ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		int semesterId = ParamUtils.getIntParameter(request, "semesterId", 0);
//		User user=(User)request.getSession().getAttribute("loginUser");
		semesterService.removeSemester(new Integer(semesterId),user);
		return mapping.findForward("delete");
	}

	/**
	 * 修改学期
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		return mapping.findForward("create");
	}
	
	public final ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		int semesterId=ParamUtils.getIntParameter(request,"semesterId",0);
		request.setAttribute("semesterId",new Integer(semesterId));
		
		
		return mapping.findForward("modify");
	} 
	
	
	/**
	 * 本方法用于检查系统与数据库之间的连接是否可用
	 * 并且通过检查结果，确定系统的运行情况是否可用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward checkSystemConnection(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {
			List list = semesterService.findSemesters();
			if(list.size()>0){
				response.getWriter().print("success");
			}			
		} catch (IOException e) {
			String err="LoadBalance检测服务器("+request.getServerName()+")与数据库连接是否正常的时候，报告错误:" + e.getMessage();
		}
		return null;

	} 
}
