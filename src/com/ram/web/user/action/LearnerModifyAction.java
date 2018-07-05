package com.ram.web.user.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.ram.RamConstants;
import com.ram.model.Learner;
import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.LearnerForm;
import com.framework.common.properties.IReadProperties;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 * 
 */
public class LearnerModifyAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService) getService("userService");

	private final ISystemTutorCenterService systemTutorCenterService = (ISystemTutorCenterService) getService("systemTutorCenterService");

	private final ISemesterService semesterService = (ISemesterService) getService("semesterService");

	private final ISystemProgramService systemProgramService = (ISystemProgramService) getService("systemProgramService");


	private final IReadProperties read = (IReadProperties) getService("readProperties");

	/**
	 * 显示修改Manager页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("in LearnerModifyAction.java ---init");
		Learner learner = null;
		LearnerForm learnerform = new LearnerForm();
		String learnerId=null;//add by hulei
		String userId=null;
		if (request.getParameter("userId") != null) {
			userId=request.getParameter("userId");
			learner = userService.getLearnerByUserId(Integer.parseInt(userId));
			learnerform.setLearnerId(learner.getLearnerId());
			request.setAttribute("userId", userId);
			
		} else if(request.getParameter("learnerId")!=null){//add by hulei
			learnerId=request.getParameter("learnerId");
			learner = userService.getLearner(Integer.parseInt(learnerId));
			request.setAttribute("learnerId", learnerId);
			request.setAttribute("userId",learner.getUser().getUserId());
		}
		if (learner == null) {
			User user = (User) request.getSession().getAttribute("loginUser");
			learner = userService.getLearnerByUserId(user.getUserId().intValue());
			request.setAttribute("learnerId", learner.getLearnerId());
			request.setAttribute("userId",user.getUserId());
		}
		
		learnerform.setLearner(learner);
		learnerform.setUser(learner.getUser());
		if (learner.getBirthday() == null) {
			learnerform.setBirthday(DateTimeUtil.getJavaSQLDateNow());
		} else {
			learnerform.setBirthday(learner.getBirthday());
		}
		List tutorCenterList = systemTutorCenterService
				.findSystemTutorCenters();
		List semesterList = semesterService.findSemesters();
		List programList = systemProgramService.findSystemPrograms();
		List specialityList = null;
//		List specialityList = courseService.getSpeciality();
		request.setAttribute("semesterList", semesterList);
		request.setAttribute("programList", programList);
		request.setAttribute("tcList", tutorCenterList);
		request.setAttribute("specialityList", specialityList);
		request.setAttribute("learnerModifySaveForm", learnerform);
		return mapping.findForward("init");
	}

	/**
	 * 录取 录取的时候，修改learner录取状态 
	 * 此处还要修改，因为招生中心存在录取为正式生、培训生等区别
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward makeEnroll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		Learner learner = userService.getLearner(Integer.parseInt(request
				.getParameter("learnerId")));
		User userInfo = learner.getUser();

		// 修改学生的learner的enrollstatus状态为被录取状态2
//		learner.setEnrollStatus(String
//				.valueOf(RamConstants.ENROLLMENT_STATUS_YijingLUqu));
		userService.saveLearner(learner, user);
		return mapping.findForward("makeEnroll");
	}

	/**
	 * 修改Learner,并且返回Learner列表页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("in LearnerModifyAction.java ---modify");
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		LearnerForm learnerform = (LearnerForm) form;
		Learner learner = userService.getLearnerByUserId(Integer.parseInt(request
				.getParameter("userId")));
		User userInfo = learner.getUser();
		String picture = null;
		if (!"".equals(learnerform.getUser().getLoginName())) {
			if (userService.checkUserExistAgain(learnerform.getUser()
					.getLoginName(), userInfo.getUserId())) {
				request.setAttribute("loginName_exist", "1");
				request.setAttribute("userId", learner.getLearnerId());
				return mapping.findForward("init");
			}
		}
		learner.setAddress(learnerform.getLearner().getAddress());
		learner.setBirthday(learnerform.getBirthday());
		learner.setHomePhone(learnerform.getLearner().getHomePhone());
		learner.setMobile(learnerform.getLearner().getMobile());
		learner.setMsn(learnerform.getLearner().getMsn());
		learner.setPostCode(learnerform.getLearner().getPostCode());
		learner.setQq(learnerform.getLearner().getQq());
		learner.setTcId(learnerform.getLearner().getTcId());
		learner.setEnrollStatus(learnerform.getLearner().getEnrollStatus());
		learner.setLearnSex(learnerform.getLearner().getLearnSex());
		learner.setEducation(learnerform.getLearner().getEducation());
		learner.setSemesterId(learnerform.getLearner().getSemesterId());
//		learner.setSpecialityId(learnerform.getLearner().getSpecialityId());
		learner.setTcId(learnerform.getLearner().getTcId());
		learner.setProgramId(learnerform.getLearner().getProgramId());
		learner.setSemesterId(learnerform.getLearner().getSemesterId());
//		learner.setSpecialityId(learnerform.getLearner().getSpecialityId());
		learner.setWorkplace(learnerform.getLearner().getWorkplace());
		learner.setInvoiceTitle(learnerform.getLearner().getInvoiceTitle());
		// userInfo.setLoginName(learnerform.getUser().getLoginName());
		userInfo.setUserEmail(learnerform.getUser().getUserEmail());
//		if (learnerform.getUser().getUserNameEn() != null
//				&& learnerform.getUser().getUserNameEn().equals("") == false) {
//			userInfo.setUserNameEn(learnerform.getUser().getUserNameEn());
//		}
		if (learnerform.getUser().getUserNameZh() != null
				&& learnerform.getUser().getUserNameZh().equals("") == false) {
			userInfo.setUserNameZh(learnerform.getUser().getUserNameZh());
		}
		if (learnerform.getUser().getPassword() != null
				&& learnerform.getUser().getPassword().equals("") == false) {
			userInfo.setPassword(learnerform.getUser().getPassword());
		}
		String dir = read.getValue("SYSTEM.FILESERVER.PATH")
				+ read.getValue("FILEUPLOAD.FOLDER") + "/userface/";
		try {
			if (!(new File(dir).isDirectory())) {
				new File(dir).mkdir();
			}
		} catch (SecurityException e) {
		}
		if (learnerform.getPicture() != null
				&& learnerform.getPicture().getFileSize() > 0) {
			picture = uploadFile(dir, learnerform.getPicture());
			if (picture != null) {
				if (userInfo.getPicture() != null) {
					try {
						new File(dir + "/" + userInfo.getPicture()).delete();
					} catch (SecurityException e) {
					}
				}
				userInfo.setPicture(picture);
			}
		}
		userService.saveLearner(learner, user);
		return mapping.findForward("modify");
	}

	/**
	 * 上传文件处理
	 * 
	 * @param dir
	 * @param file
	 * @return
	 */
	public String uploadFile(String dir, FormFile file) {
		ArrayList arg = (new ArrayList());
		arg.add(".jpg");
		arg.add(".bmp");
		arg.add(".png");
		arg.add(".gif");
		String suffix = file.getFileName().substring(
				file.getFileName().lastIndexOf(".")).toLowerCase();
		String filename = System.currentTimeMillis() + suffix;
		if (arg.indexOf(suffix) == -1)
			return null;
		try {
			InputStream si = file.getInputStream();
			OutputStream os = new FileOutputStream(dir + "/" + filename);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = si.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			si.close();
			file.destroy();
		} catch (Exception e) {
			filename = null;
		}
		return filename;
	}
	
	/**
	 * 恢复学生在学状态（复学）
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward reEnrollStatus(ActionMapping mapping, ActionForm form,
									    HttpServletRequest request, HttpServletResponse response){
		User user = null;
		user = ( User )request.getSession().getAttribute( "loginUser ");
		
		int learnerId = ParamUtils.getIntParameter( request,"learnerId",0 );
		Learner learner = null;
		learner = userService.getLearner( learnerId );
		User learnerUser=null;
		if( learner != null ){
			learner.setEnrollStatus( "3" );
			learnerUser=learner.getUser();
			learnerUser.setStatus("1");
			
		}
		userService.saveLearner( learner,user );
		userService.saveUser(learnerUser,user);
		return mapping.findForward( "modify" );
	}
}
