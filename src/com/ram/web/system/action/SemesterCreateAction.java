package com.ram.web.system.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.Semester;
import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.ram.web.system.form.SemesterForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class SemesterCreateAction extends BaseDispatchAction {
	public ISemesterService semesterService = (ISemesterService) getService("semesterService");

	/**
	 * 添加功能的init入口方法
	 * 
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
		saveToken(request);
		SemesterForm semesterCreateForm = (SemesterForm) form;
		semesterCreateForm.setSemester(new Semester());
		
		
		return mapping.findForward("init");
	}

	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//获取当前用户
		User user = null;
		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
		
		log.info("1保存新创建的学期");
		//if (isTokenValid(request)) {
			log.info("2保存新创建的学期");
			// 获取当前登录用户
			//User user = (User) request.getSession().getAttribute("loginUser");
			// 获取formbean
			SemesterForm semesterCreateForm = (SemesterForm) form;
			// 创建新semester实例
			Semester semester = new Semester();
			semester.setSemesterTitle(semesterCreateForm.getSemester()
					.getSemesterTitle());
			semester.setDateEnd(semesterCreateForm.getDateEnd());
			semester.setDateStart(semesterCreateForm.getDateStart());
			semester.setEnrollSeason(semesterCreateForm.getSemester()
					.getEnrollSeason());
			semester.setEnrollYear(semesterCreateForm.getSemester()
					.getEnrollYear());
			semester.setSemesterStatus(RamConstants.DELETE_STATUS);
			semester.setUpdateDateTime(new java.sql.Date(System
					.currentTimeMillis()));
			
			// semester.setUpdateUserId()
			// 保存新的semester
			semesterService.saveSemester(semester,user);
			log.info("创建了一个新的学期:" + semester.getSemesterTitle() + "!");
			resetToken(request);
		//}
		log.info("3保存新创建的学期");
		return mapping.findForward("save");
	}
}
