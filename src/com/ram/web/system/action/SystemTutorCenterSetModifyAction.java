package com.ram.web.system.action;

import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.web.system.form.SystemTutorCenterSetForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class SystemTutorCenterSetModifyAction extends BaseDispatchAction {

	private ISystemTutorCenterService systemTutorCenterService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {				

            Integer id = ParamUtils.getIntegerParameter(request,"tcId");
	        TutorCenter tutorCenter = systemTutorCenterService.getSystemTutorCenter(id);
		    SystemTutorCenterSetForm systemTutorCenterSetForm = new SystemTutorCenterSetForm();
			systemTutorCenterSetForm.setTutorCenter(tutorCenter);
			systemTutorCenterSetForm.setParentTcId(tutorCenter.getParentTcId());
			request.setAttribute("systemTutorCenterSetForm", systemTutorCenterSetForm);
		
			//		最上级的parentId=0,从0开始往下查
		int start = 0;		
		Collection tutorCenterCollection = systemTutorCenterService.getTutorCenterTree(start);// getTutorCenterCollection(start);
		request.setAttribute("tutorCenterCollection", tutorCenterCollection);
		
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
		SystemTutorCenterSetForm systemTutorCenterSetForm = (SystemTutorCenterSetForm)form;
		
		
		
		log.info("=====parentId=============" + systemTutorCenterSetForm.getParentTcId());
		TutorCenter tutorCenter = null;
	
		if(systemTutorCenterSetForm.getParentTcId().intValue() > 0 ){
			tutorCenter = 
				systemTutorCenterService.getSystemTutorCenter(systemTutorCenterSetForm.getParentTcId());
			systemTutorCenterSetForm.getTutorCenter().setTcLevel(new Integer(tutorCenter.getTcLevel().intValue() + 1));
		} else {
			systemTutorCenterSetForm.getTutorCenter().setTcLevel(new Integer(0));
			log.info("-------else-------------");
		}
		
		log.info("=====2=============" + systemTutorCenterSetForm.getTutorCenter().getTcLevel());
		log.info("=====3=============" + systemTutorCenterSetForm.getParentTcId());
		systemTutorCenterSetForm.getTutorCenter().setParentTcId(systemTutorCenterSetForm.getParentTcId());
		systemTutorCenterService.saveSystemTutorCenter(systemTutorCenterSetForm.getTutorCenter(), user);
		List systemTutorCenterList = systemTutorCenterService.findSystemTutorCenters();
		log.info("=====学习中心的个数=============" + systemTutorCenterList.size());
		request.setAttribute("systemTutorCenterList",systemTutorCenterList);
		saveToken(request);
		return (mapping.findForward("save"));
	}
	


}
