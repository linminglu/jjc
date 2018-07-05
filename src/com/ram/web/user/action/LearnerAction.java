/**
 * @(#)LearnerAction.java 1.0 06/07/27
 */

package com.ram.web.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.Learner;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * 学生祥细信息
 * @author Lu Congyu
 */
public class LearnerAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService tutorCenterService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final ISemesterService semesterService = (ISemesterService)getService("semesterService");
	private final ISystemProgramService programService = (ISystemProgramService)getService("systemProgramService");
	
	public ActionForward profile(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response){
		int userid = ParamUtils.getIntParameter(request, "id", 0);//用户ID
		Learner l = userService.getLearnerByUserId(userid);
		TutorCenter tc = null;
		Program p = null;
		Semester s = null;
//		Schedule sc = null;
		if(l!=null){
			tc = tutorCenterService.getSystemTutorCenter(l.getTcId());
			p = programService.getSystemProgram(l.getProgramId());
			s = semesterService.getSemester(l.getSemesterId());
			if(l.getProgramId()!= null && l.getSemesterId() != null){
//				sc = scheduleService.getScheduleIdBySemProg(l.getSemesterId().intValue(), l.getProgramId().intValue());
			}
		}		
		
		request.setAttribute("l", l);
		request.setAttribute("tc", tc);
		request.setAttribute("p", p);
		request.setAttribute("s", s);
//		request.setAttribute("sc", sc);
		return mapping.findForward("profile");
	}
}