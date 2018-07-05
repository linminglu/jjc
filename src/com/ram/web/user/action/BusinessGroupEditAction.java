package com.ram.web.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.model.BusinessGroup;
import com.ram.model.LearnerGroup;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IBusinessGroupService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.BusinessGroupForm;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;



public class BusinessGroupEditAction extends BaseDispatchAction {
	private final IBusinessGroupService businessGroupService = (IBusinessGroupService) getService("businessGroupService");

	private final IUserService userService = (IUserService) getService("userService");

	private final ISystemProgramService programService = (ISystemProgramService) getService("systemProgramService");

	private final ISystemTutorCenterService tcService = (ISystemTutorCenterService) getService("systemTutorCenterService");

	private final ISemesterService semesterService = (ISemesterService) getService("semesterService");

	/**
	 * modify方法进入修改界面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	
			int businessGroupId = ParamUtils.getIntParameter(request,"businessGroupId", 0);
			log.info("businessGroupId=" + businessGroupId);
			BusinessGroupForm bgForm = (BusinessGroupForm) form;

			BusinessGroup bg = null;
			if (businessGroupId > 0) {
				bg = businessGroupService.getBusinessGroup(businessGroupId);
				if (bg.getSemester() == null)
					bg.setSemester(new Semester());
				if (bg.getProgram() == null)
					bg.setProgram(new Program());
				if (bg.getTutorCenter() == null)
					bg.setTutorCenter(new TutorCenter());
				if (bg.getLearnerGroup() == null)
					bg.setLearnerGroup(new LearnerGroup());
				if (bg.getUserGroup() == null)
					bg.setUserGroup(new UserGroup());
				bgForm.setBusinessGroup(bg);
				bgForm.setBusinessGroupId(businessGroupId);

			}
		    initPage(mapping, bgForm, request, response);
			this.resetToken(request);
	
		return mapping.findForward("modify");
	}

	/**
	 * 进入创建界面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		initPage(mapping, form, request, response);

		return mapping.findForward("create");
	}

	/**
	 * 创建和修改的保存操作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		if (isTokenValid(request, true)) {
			BusinessGroupForm bgForm = (BusinessGroupForm) form;

			log.info("bgForm=" + bgForm.toString());
			log.info("bgForm.getBusinessGroup.getGroupName="
					+ bgForm.getBusinessGroup().getGroupName());
			log.info("bgForm.getLearnerGroup.getLearnerGroupTitle="
					+ bgForm.getLearnerGroup().getLearnerGroupTitle());
			log.info("bgForm.getProgram.getBusinessGroup().getProgramName="
					+ bgForm.getProgram().getProgramName());
			log.info("bgForm.getSemester.getSemesterTitle="
					+ bgForm.getSemester().getSemesterTitle());
			log.info("bgForm.getTutorCenter.getTcTitle="
					+ bgForm.getTutorCenter().getTcTitle());
			// 判断BusinessGroup在Form中是否存在
			
			BusinessGroup bg = null;

			int bgId = bgForm.getBusinessGroupId();
			bg = businessGroupService.getBusinessGroup(bgId);

			if (bgForm.getBusinessGroup().getGroupName() != null
					&& bgForm.getBusinessGroup().getGroupName().trim().length() > 0) {
				bg = new BusinessGroup();
				// bg = bgForm.getBusinessGroup();

				bg.setGroupName(bgForm.getBusinessGroup().getGroupName());

				if (bgForm.getLearnerGroup().getLearnerGroupId() != null) {
					bg.setLearnerGroup(bgForm.getLearnerGroup());
				} else {
					bg.setLearnerGroup(null);
				}

				if (bgForm.getProgram().getProgramId() != null) {
					bg.setProgram(bgForm.getProgram());
				} else {
					bg.setProgram(null);
				}

				if (bgForm.getSemester().getSemesterId() != null) {
					bg.setSemester(bgForm.getSemester());
				} else {
					bg.setSemester(null);
				}

				if (bgForm.getTutorCenter().getTcId() != null) {
					bg.setTutorCenter(bgForm.getTutorCenter());
				} else {
					bg.setTutorCenter(null);
				}

				if (bgForm.getUserGroup().getUserGroupId() != null) {
					bg.setUserGroup(bgForm.getUserGroup());
				} else {
					bg.setUserGroup(null);
				}
				bg.setCreateUserId(user.getUserId());
				businessGroupService.saveBusinessGroup(bg, user);
			}
			this.resetToken(request);
		}
		return mapping.findForward("save");

	}

	/**
	 * 进入创建界面和修改界面前的初始化
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 */
	private void initPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		BusinessGroupForm bgForm = (BusinessGroupForm) form;
		if (bgForm.getBusinessGroup().getGroupName() == null) {
			log.info("aaaaaaaaaa");
			bgForm.setUserGroup(new UserGroup());
			bgForm.setSemester(new Semester());
			bgForm.setLearnerGroup(new LearnerGroup());
			bgForm.setProgram(new Program());
			bgForm.setTutorCenter(new TutorCenter());
		} else {
			// 设置bgForm的各项属性

			bgForm.setUserGroup(bgForm.getBusinessGroup().getUserGroup());
			bgForm.setSemester(bgForm.getBusinessGroup().getSemester());
			bgForm.setLearnerGroup(bgForm.getBusinessGroup().getLearnerGroup());
			bgForm.setProgram(bgForm.getBusinessGroup().getProgram());
			bgForm.setTutorCenter(bgForm.getBusinessGroup().getTutorCenter());
		}

		List userGroupList = userService.getAllUserGroup();
		List semesterList = semesterService.findSemesters();
		List programList = programService.findSystemPrograms();
		//List tutorCenterList = tcService.findSystemTutorCenters();
		List tutorCenterList = tcService.getTutorCenterTree(userService.getTcId_Of_User(user));
		List businessGroupList = businessGroupService.findAllBusinessGroup();
//		List learnerGroupList = learnerGroupService.findAllLearnerGroup();

		request.setAttribute("businessGroupList", businessGroupList);
		request.setAttribute("userGroupList", userGroupList);
		request.setAttribute("semesterList", semesterList);
		request.setAttribute("programList", programList);
		request.setAttribute("tutorCenterList", tutorCenterList);
//		request.setAttribute("learnerGroupList", learnerGroupList);

		this.saveToken(request);
	}

}
