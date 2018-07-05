package com.ram.web.system.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.User;
import com.ram.service.system.ISystemProgramService;
import com.framework.web.action.BaseDispatchAction;

public class SystemProgramSetAction extends BaseDispatchAction {
	protected final Log log = LogFactory.getLog(getClass());
//	private IServiceLocator serviceLocator = ServiceLocatorImpl.getInstance();
//	private Object obj = serviceLocator.getService("systemProgramService");
//	private ISystemProgramService systemProgramService = (ISystemProgramService)obj;
	private ISystemProgramService systemProgramService = (ISystemProgramService)getService("systemProgramService");
	
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		
		//HttpSession session = request.getSession(false);
		List systemProgramList = systemProgramService.findSystemPrograms();
		log.info("==size====" + systemProgramList.size());
		request.setAttribute("systemProgramList",systemProgramList);		
		log.info("SystemProgramSetAction===================");
		saveToken(request);
		return (mapping.findForward("init"));
	}
	
	public final ActionForward add(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		log.info("SystemProgramSetAction.add===================");
		
		saveToken(request);
		return (mapping.findForward("add"));
	}
	
	public final ActionForward delete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		log.info("SystemProgramSetAction.DELETE===================");
		String currentId = request.getParameter("currentId");
		Integer id = Integer.valueOf(currentId);
		
		systemProgramService.removeSystemProgram(id, user);
		saveToken(request);
		return (mapping.findForward("delete"));
	}
}
