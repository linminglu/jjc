package com.ram.web.system.action;

import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.web.system.form.SystemTutorCenterSetForm;
import com.framework.web.action.BaseDispatchAction;

public class SystemTutorCenterSetAddAction extends BaseDispatchAction {
	protected final Log log = LogFactory.getLog(getClass());
	private ISystemTutorCenterService systemTutorCenterService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		log.info("SystemTutorCenterSetAddAction.init===================");
		//最上级的parentId=0,从0开始往下查
		int start = 0;		
		Collection tutorCenterCollection = systemTutorCenterService.getTutorCenterTree(start);//getTutorCenterCollection(start);
		request.setAttribute("tutorCenterCollection", tutorCenterCollection);
		removeFormBean(mapping, request);
		saveToken(request);		
		return mapping.findForward("init");
	}
	
	public final ActionForward save(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		log.info("SystemTutorCenterSetAddAction.save===================");
		SystemTutorCenterSetForm systemTutorCenterSetForm = (SystemTutorCenterSetForm)form;
		log.info("id is:"+systemTutorCenterSetForm.getTutorCenter().getTcId());
		
		TutorCenter tutorCenter = systemTutorCenterSetForm.getTutorCenter();
		Integer parentTcId = tutorCenter.getParentTcId();
		log.info("parentid======= " + parentTcId);
		int level = systemTutorCenterService.getTcLevelByParentTcId(parentTcId.intValue());
		tutorCenter.setTcLevel(new Integer(level + 1));
		
		systemTutorCenterService.saveSystemTutorCenter(tutorCenter, user);		
		saveToken(request);
		return (mapping.findForward("save"));
	}
	

}
