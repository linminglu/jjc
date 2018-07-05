package com.ram.web.system.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Program;
import com.ram.model.User;
import com.ram.service.system.ISystemProgramService;
import com.ram.web.system.form.SystemProgramSetForm;
import com.framework.web.action.BaseDispatchAction;

public class SystemProgramSetModifyAction extends BaseDispatchAction {

	private ISystemProgramService systemProgramService = (ISystemProgramService)getService("systemProgramService");
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {				
//		SystemProgramSetForm systemProgramSetForm = (SystemProgramSetForm)form;
//		Program prgram = systemProgramSetForm.getProgram();
		String currentId = request.getParameter("currentId");
		if(currentId != null){
			Integer id = Integer.valueOf(currentId);
			log.info("xxxxxx" + id.toString());
			Program program = systemProgramService.getSystemProgram(id);
			SystemProgramSetForm systemProgramSetForm = new SystemProgramSetForm();
			systemProgramSetForm.setProgram(program);
			request.setAttribute("systemProgramSetForm", systemProgramSetForm);
			request.setAttribute("currentId", currentId);
		} else {
			log.info("else===================== ");
		}		
		
		log.info("SystemProgramSetModifyAction.init===================");
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
		
		systemProgramService.saveSystemProgram(systemProgramSetForm.getProgram(), user);
		List systemProgramList = systemProgramService.findSystemPrograms();
		log.info("==================" + systemProgramList.size());
		request.setAttribute("systemProgramList",systemProgramList);
		saveToken(request);
		return (mapping.findForward("save"));
	}

}
