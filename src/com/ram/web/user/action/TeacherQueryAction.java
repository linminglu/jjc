package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ram.RamConstants;
import com.ram.model.Tutor;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.TeacherForm;
import com.ram.web.user.form.TeacherQueryForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class TeacherQueryAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");

	/** 
	 * 根据查询条件显示Manager列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward query(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		TeacherQueryForm teacherQueryForm = (TeacherQueryForm)form;
		//HttpSession session = request.getSession(false);
		DetachedCriteria query = DetachedCriteria.forClass(Tutor.class, "tutor");

	    int uTcId = userService.getTcId_Of_User(user);
	    if(uTcId==1){
	    }else{
	    Integer tcIdInScope = systemTutorCenterService.tcIdInMyScope(teacherQueryForm.getTcCenter(), new Integer(uTcId));
	    teacherQueryForm.setTcCenter(tcIdInScope);
	    }
		if(teacherQueryForm.getTcCenter() != null){
			if(!teacherQueryForm.getTcCenter().toString().equals("0")){
				//session.setAttribute("tcId", teacherQueryForm.getTcCenter());
				request.setAttribute("tcId", teacherQueryForm.getTcCenter());
				query.add(Restrictions.eq("tutor.tutorCenter.tcId", teacherQueryForm.getTcCenter()));
			}
		}
		//else if(session.getAttribute("tcId") != null){
		//	query.add(Restrictions.eq("tutor.tutorCenter.tcId", (Integer)session.getAttribute("tcId")));
		//}
		if(teacherQueryForm.getUserName() != "" && teacherQueryForm.getUserName() != null){
			query.createAlias("user", "user").add(Restrictions.ilike("user.loginName", "%" + teacherQueryForm.getUserName() + "%")).add(Restrictions.ne("user.status", "0"));
			//session.setAttribute("userName", teacherQueryForm.getUserName());
			request.setAttribute("userName", teacherQueryForm.getUserName());
		}
		//else if(session.getAttribute("userName") != null){
		//	query.createAlias("user", "user").add(Restrictions.ilike("user.loginName", "%" + (String)session.getAttribute("userName") + "%")).add(Restrictions.ne("user.status", "0"));
		//}
		else{
			query.createAlias("user", "user").add(Restrictions.ne("user.status", "0"));
		}
		query.addOrder(Order.desc("user.userId"));
		int offset = ParamUtils.getIntParameter(request, "offset", 0);
		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
		int startIndex = offset * pageSize;
		getTutorInfo(request, startIndex, pageSize, query);
		request.setAttribute("queryFlg", "1");
		
	    int tcId = userService.getTcId_Of_User(user);
	    List tcs = systemTutorCenterService.getTutorCenterTree(tcId);
	    Collection  tc = new ArrayList();
	    if(tcId==1)tc.add(new LabelValueBean("全部学习中心", null));
	    tc.addAll(tcs);
		request.setAttribute("tcList", tc);
		
		return mapping.findForward("query");
	}
	
	/**
	 * 根据查询条件获得Tutor信息列表
	 * @param request
	 * @param startIndex
	 * @param pageSize
	 * @param query
	 * @return
	 */
	 private boolean getTutorInfo(HttpServletRequest request, int startIndex, int pageSize, DetachedCriteria query){
    	PaginationSupport paginationSupport;
    	List result = new ArrayList();
    	paginationSupport = userService.findALLTutorForPage(startIndex, pageSize, query);
		if(paginationSupport == null){
			return false;
		}
		else{
			List tutorList = paginationSupport.getItems();
			Iterator tutorItr = tutorList.iterator();
			while(tutorItr.hasNext()){
				Tutor tutor = (Tutor)((Object[])tutorItr.next())[1];
				//try{
					TeacherForm itemInfo = new TeacherForm();
					itemInfo.setTeacher(tutor);
					result.add(itemInfo);
				//}
				//catch(Exception ex){
				//	log.error(ex);
				//}
			}
			int tutorTotalCount = paginationSupport.getTotalCount();
			request.setAttribute("teacherList", result);
//			request.setAttribute("teacherTotalCount", new Integer(tutorTotalCount));
			request.setAttribute("count", new Integer(tutorTotalCount));
			request.setAttribute("rows", new Integer(pageSize));
			return true;
		}
    }
}
