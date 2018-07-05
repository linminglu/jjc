/**
 * @(#)NewStudentEleRegisterAction.java 1.0 06/09/29
 */

package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * 新生电子注册
 * @author Lu Congyu
 */
public class NewStudentEleRegisterAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService tcService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final ISemesterService semesterService = (ISemesterService)getService("semesterService");
	private final ISystemProgramService programService = (ISystemProgramService)getService("systemProgramService");

	
	public ActionForward showOne(ActionMapping mapping, ActionForm from,
		HttpServletRequest req, HttpServletResponse res){
		
		String where = " and l.enrollRegisterStatus=? ";
		List list = new ArrayList();
		list.add(new Integer(1));
		int rows = 50;
		int offset = ParamUtils.getIntParameter(req, "offset", 0);
		int count = userService.findAllLearner(where, list);
		int pages = (int)(count/rows);
		pages = count%rows > 0 ? pages + 1 : pages;		
		List l = userService.findAllLearner(offset, rows, where, list);
		
		req.setAttribute("learner", l);
		req.setAttribute("count", new Integer(count));
		req.setAttribute("rows", new Integer(rows));
		req.setAttribute("program", programService.findSystemPrograms());
		req.setAttribute("semester", semesterService.findSemesters());
		req.setAttribute("tclist", tcService.findSystemTutorCenters());
		
		return mapping.findForward("showOne");
	}
	
	public ActionForward updateOne(ActionMapping mapping, ActionForm form,
		HttpServletRequest req, HttpServletResponse res){
		Integer learnerId = ParamUtils.getIntegerParameter(req, "id");
		if(learnerId!=null)userService.updateLearnerEnrollRegisterStatus(learnerId);
		return mapping.findForward("updateOne");
	}
	
	public ActionForward showTwo(ActionMapping mapping, ActionForm from,
		HttpServletRequest req, HttpServletResponse res){
			
		String where = " and l.enrollRegisterStatus=? ";
		List list = new ArrayList();
		list.add(new Integer(2));
		int rows = 50;
		int offset = ParamUtils.getIntParameter(req, "offset", 0);
		int count = userService.findAllLearner(where, list);
		int pages = (int)(count/rows);
		pages = count%rows > 0 ? pages + 1 : pages;		
		List l = userService.findAllLearner(offset, rows, where, list);
			
		req.setAttribute("learner", l);
		req.setAttribute("count", new Integer(count));
		req.setAttribute("rows", new Integer(rows));
		req.setAttribute("program", programService.findSystemPrograms());
		req.setAttribute("semester", semesterService.findSemesters());
		req.setAttribute("tclist", tcService.findSystemTutorCenters());
			
		return mapping.findForward("showTwo");
	}
	
	public ActionForward updateTwo(ActionMapping mapping, ActionForm form,
		HttpServletRequest req, HttpServletResponse res){
		int[] id = ParamUtils.getIntParameters(req, "id", 0);
		String regDate = ParamUtils.getParameter(req, "regdate");
		List learnerId = new ArrayList(); 
		for(int i=0;i<id.length;i++){
			if(id[i]>0)learnerId.add(new Integer(id[i]));
		}
		if(learnerId.size()>0)userService.updateLearnerEnrollRegisterStatus(regDate, learnerId);
		return mapping.findForward("updateTwo");
	}
	
	public ActionForward showThree(ActionMapping mapping, ActionForm from,
		HttpServletRequest req, HttpServletResponse res){
			
		String where = " and l.enrollRegisterStatus=? ";
		List list = new ArrayList();
		list.add(new Integer(3));
		int rows = 50;
		int offset = ParamUtils.getIntParameter(req, "offset", 0);
		int count = userService.findAllLearner(where, list);
		int pages = (int)(count/rows);
		pages = count%rows > 0 ? pages + 1 : pages;		
		List l = userService.findAllLearner(offset, rows, where, list);
			
		req.setAttribute("learner", l);
		req.setAttribute("count", new Integer(count));
		req.setAttribute("rows", new Integer(rows));
		req.setAttribute("program", programService.findSystemPrograms());
		req.setAttribute("semester", semesterService.findSemesters());
		req.setAttribute("tclist", tcService.findSystemTutorCenters());
			
		return mapping.findForward("showThree");
	}
}