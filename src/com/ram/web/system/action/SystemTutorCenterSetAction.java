package com.ram.web.system.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.ram.web.system.form.SystemTutorCenterSetForm;
import com.ram.RamConstants;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class SystemTutorCenterSetAction extends BaseDispatchAction {
	protected final Log log = LogFactory.getLog(getClass()); 
	private ISystemTutorCenterService systemTutorCenterService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	
	public final ActionForward init(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		
		HttpSession session = request.getSession(false);
		removeFormBean(mapping, request);//清除form中的方法
		SystemTutorCenterSetForm systemTutorCenterSetForm=(SystemTutorCenterSetForm)form;
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
        int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);		
        String searchName=ParamUtils.getParameter(request,"searchName");			
		log.info("searchName is:"+searchName);
		if(searchName.length()>0){				
		 DetachedCriteria query = DetachedCriteria.forClass(TutorCenter.class, "tutorCenter");			
		 query.add(Restrictions.ilike("tutorCenter.tcTitle", "%" + searchName+"%"));
		
		 getCourseInfo(request, startIndex, pageSize,query);
		
		 systemTutorCenterSetForm.setSearchName(searchName);
		 request.setAttribute("systemTutorCenterSetForm",systemTutorCenterSetForm);
		 
		 request.getSession(false).setAttribute("searchName",searchName);//查询翻页用	
		}else{	         
		 getCourseInfo(request, startIndex, pageSize,null);			
		}			 
		 saveToken(request);
        
       return (mapping.findForward("init"));
	}
	
	public final ActionForward add(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
		ServletException {
		log.info("SystemTutorCenterSetAction.add===================");
				
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
		
		log.info("SystemTutorCenterSetAction.DELETE===================");
		//String currentId = request.getParameter("currentId");
		Integer id = ParamUtils.getIntegerParameter(request,"tcId");
		log.info("currentId==" + id);
		log.info("============================="+id);
		systemTutorCenterService.deleteSystemTutorCenter(id, user);
		saveToken(request);
		return (mapping.findForward("delete"));
	}
	
	/** 
	 * 从数据库中提取课程信息数据

	 * @param  HttpServletRequest
	 * @param query DetachedCriteria
	 * @return boolean 查询结果不空 true 查询结果为空 false
	 */
    
    private void getCourseInfo(HttpServletRequest request, int startIndex, int pageSize,DetachedCriteria query){
    	PaginationSupport paginationSupport;
    	if(query == null){
    		paginationSupport = systemTutorCenterService.findALLTutorCenterPage(startIndex,pageSize);
    	}
    	else{
    		paginationSupport = systemTutorCenterService.findTutorCenterByCriteria(query, pageSize, startIndex);
    	}
		if(paginationSupport == null){
			request.setAttribute("tutorCenterList", new ArrayList());
			request.setAttribute("tutorCenterTotalCount", new Integer(0));			
		}
		else{
			List tutorCenterList = paginationSupport.getItems();
		
			int tutorCenterTotalCount = paginationSupport.getTotalCount();
			
			
			request.setAttribute("tutorCenterList", tutorCenterList);
			request.setAttribute("tutorCenterTotalCount", new Integer(tutorCenterTotalCount));			
		}
    }
    
}
