package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.ram.RamConstants;
import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.TeacherForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class TeacherListAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");
	
	/** 
	 * 显示teacher列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
		HttpServletRequest request,	HttpServletResponse response) {
//		int offset = ParamUtils.getIntParameter(request, "offset", 0);
//		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
//		int startIndex = offset * pageSize;
//		getTeacherInfo(request, startIndex, pageSize);
//		if(request.getSession(false).getAttribute("tcId") != null){
//			request.getSession(false).removeAttribute("tcId");
//		}
//		if(request.getSession(false).getAttribute("userName") != null){
//			request.getSession(false).removeAttribute("userName");
//		}
//
//	    int tcId = userService.getTcId_Of_User(user);
//	    List tcs = systemTutorCenterService.getTutorCenterTree(tcId);
//	    Collection  tc = new ArrayList();
//	    if(tcId==1)tc.add(new LabelValueBean("全部学习中心", null));
//	    tc.addAll(tcs);
//	    
//		request.setAttribute("tcList", tc);
		return mapping.findForward("init");
	}
	
	
	/** 
	 * 批量删除用户(user.status=-1)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		String[] indexs = null;
		int[] delIndexs = null;
		if((indexs = request.getParameterValues("selIndexs")) != null){
			delIndexs = new int[indexs.length];
			for(int i = 0; i < indexs.length; i++){
				delIndexs[i] = Integer.parseInt(indexs[i]);
			}
			userService.deleteUser(delIndexs, user);
		}
		
		// 没有查询条件的teacher信息的获得
//		int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
//		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
//		getTeacherInfo(request, startIndex, pageSize);
		
		return mapping.findForward("delete");
	}
	
	/** 
	 * 使用户状态变成有效
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward releaseUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		String id = request.getParameter("userId");
		if(id != null){
			int userId = Integer.parseInt(id);
			userService.modifyUserStatus(userId, "1", user);
		}
		return mapping.findForward("releaseUser");
	}
	
	/** 
	 * 使用户状态变成无效
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward pauseUser(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		String id = request.getParameter("userId");
		if(id != null){
			int userId = Integer.parseInt(id);
			userService.modifyUserStatus(userId, "2", user);
		}
		return mapping.findForward("pauseUser");
	}
	
	/**
	 * 根据查询条件获得teacher信息列表
	 * @param request
	 * @param startIndex
	 * @param pageSize
	 * @param query
	 * @return
	 */
	 private boolean getTeacherInfo(HttpServletRequest request, int startIndex, int pageSize){
    	PaginationSupport paginationSupport;
    	List result = new ArrayList();
    	paginationSupport = userService.findALLTutorForPage(startIndex, pageSize);
		if(paginationSupport == null){
			return false;
		}
		else{
			List teacherList = paginationSupport.getItems();
			Iterator teacherItr = teacherList.iterator();
			while(teacherItr.hasNext()){
				Tutor teacher = (Tutor)teacherItr.next();
				try{
					TeacherForm itemInfo = new TeacherForm();
					itemInfo.setTeacher(teacher);
					result.add(itemInfo);
				}
				catch(Exception ex){
					log.error(ex);
				}
			}
			int teacherTotalCount = paginationSupport.getTotalCount();
			request.setAttribute("teacherList", result);
//			request.setAttribute("teacherTotalCount", new Integer(teacherTotalCount));
			request.setAttribute("count", new Integer(teacherTotalCount));
			request.setAttribute("rows", new Integer(pageSize));
			return true;
		}
    }
	
	 /** 
	 * 显示选定用户的详细信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	 public ActionForward show(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		String id = request.getParameter("teacherId");
		if(id != null){
			int teacherId = Integer.parseInt(id);
			Tutor teacher = userService.getTutor(teacherId);
			TeacherForm itemInfo = new TeacherForm();
			if(teacher.getTutorCenter() == null){
				itemInfo.setTcCenterName("");
			}
			else{
				itemInfo.setTcCenterName(systemTutorCenterService.getSystemTutorCenter(teacher.getTutorCenter().getTcId()).getTcTitle());
			}
			itemInfo.setTeacher(teacher);
			request.setAttribute("teacher", itemInfo);
		}
		return mapping.findForward("show");
	}
}
