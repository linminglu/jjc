package com.ram.web.system.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Semester;
import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.ram.web.system.form.SemesterForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class SemesterModifyAction extends BaseDispatchAction {

	private ISemesterService semesterService = (ISemesterService) getService("semesterService");

	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		Integer IntegerSemesterId = (Integer) request
				.getAttribute("semesterId");
		log.info("需要修改的学期：" + IntegerSemesterId.intValue());
		Semester semester = (Semester) semesterService
				.getSemester(IntegerSemesterId);

		SemesterForm semesterForm = (SemesterForm) form;

		semesterForm.setSemester(semester);

		semesterForm.setDateEnd(semester.getDateEnd());

		semesterForm.setDateStart(semester.getDateStart());

		// request.setAttribute("semesterId",semester.getSemesterId());
		return mapping.findForward("init");
	}

	/**
	 * 保存Post过来的功能修改后的数据
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		// User user=(User)request.getSession().getAttribute("loginUser");
		log.info("开始修改一个semester");
		try {
			// 获取正在修改的semester的id值			//获取当前用户
			User user = null;
			user = ( User )request.getSession( false ).getAttribute( "loginUser" );

			int semesterId = ParamUtils.getIntParameter(request,
					"semester.semesterId", 0);
			Semester semester = semesterService.getSemester(new Integer(
					semesterId));
			// 从form中获取semester
			SemesterForm semesterForm = (SemesterForm) form;
			Semester pageSemester = semesterForm.getSemester();
			//判断form中是否设置该学期为当前学期，如果是，那么把数据库中其他的学期均设置为非当前学期
			if(pageSemester.getSemesterStatus().equals("1")){
				log.info("判断form中是否设置该学期为当前学期，如果是，那么把数据库中其他的学期均设置为非当前学期");
				semesterService.saveAllSemesterNotCurrentSemester(user);
			}
			
			// 将get出来的对象设置新值
			semester.setSemesterTitle(pageSemester.getSemesterTitle());
			semester.setDateEnd(semesterForm.getDateEnd());
			semester.setDateStart(semesterForm.getDateStart());
			semester.setEnrollSeason(pageSemester.getEnrollSeason());
			semester.setEnrollYear(pageSemester.getEnrollYear());
			semester.setSemesterStatus(pageSemester.getSemesterStatus());
			semester.setIsEnrollSemester(pageSemester.getIsEnrollSemester());
			log.info("将当前修改的学期设置为："+pageSemester.getSemesterStatus()+"状态");
			semester.setUpdateDateTime(new java.sql.Date(System
					.currentTimeMillis()));
			
			// semester.setUpdateUserId(user.getUserId());
			// 保存对象
			semesterService.saveSemester(semester,user);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping.findForward("save");
	}
}
