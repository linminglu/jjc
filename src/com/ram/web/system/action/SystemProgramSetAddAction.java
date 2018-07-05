package com.ram.web.system.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Program;
import com.ram.model.User;
import com.ram.service.system.ISystemProgramService;
import com.ram.web.system.form.SystemProgramSetForm;
import com.framework.web.action.BaseDispatchAction;

public class SystemProgramSetAddAction extends BaseDispatchAction {
	protected final Log log = LogFactory.getLog(getClass());
	private ISystemProgramService systemProgramService = (ISystemProgramService)getService("systemProgramService");
	
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {				
		log.info("SystemProgramSetAddAction.init===================");
		removeFormBean(mapping, request);
		saveToken(request);
		return (mapping.findForward("init"));
	}
	
	public final ActionForward save(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {		
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		log.info("SystemProgramSetModifyAction.save===================");
		SystemProgramSetForm systemProgramSetForm = (SystemProgramSetForm)form;
		log.info("id is:"+systemProgramSetForm.getProgram().getProgramId());
		
		Program program = systemProgramSetForm.getProgram();
		systemProgramService.saveSystemProgram(program, user);
		
		saveToken(request);
		return (mapping.findForward("save"));
	}
}
