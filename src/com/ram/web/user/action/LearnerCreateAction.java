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
import com.ram.model.UserGroupRl;
import com.ram.model.UserRoleRl;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.LearnerForm;
import com.framework.common.properties.IReadProperties;
import com.framework.util.DateTimeUtil;
import com.framework.web.action.BaseDispatchAction;
/**
 * @author lixiaodong 
 */
public class LearnerCreateAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final ISemesterService semesterService= (ISemesterService)getService("semesterService");
	private final ISystemProgramService systemProgramService= (ISystemProgramService)getService("systemProgramService");
	private final IReadProperties read = (IReadProperties) getService("readProperties");
	
	/** 
	 * 显示创建Manager页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		LearnerForm learnerForm = (LearnerForm)form;
		List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
		List semesterList = semesterService.findSemesters();
		List programList = systemProgramService.findSystemPrograms();
		List specialityList=null;
//		List specialityList=courseService.getSpeciality();
		
		request.setAttribute("semesterList", semesterList);
		request.setAttribute("programList", programList);
		request.setAttribute("tcList", tutorCenterList);
		request.setAttribute("specialityList", specialityList);
		learnerForm.setSemesterId(semesterService.getCurrentSemester().getSemesterId());
		request.setAttribute("learnerCreateSaveForm", learnerForm);
		return mapping.findForward("init");
	}
	
	/** 
	 * 创建Learner,并且返回Learner列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		LearnerForm learnerform = (LearnerForm)form;
		Learner learner = learnerform.getLearner();
		String pictrue = null;
		
		learner.setBirthday(learnerform.getBirthday());
		learner.setProgramId(new Integer(1));
		learner.setEnrollStatus("1");
		User userInfo = learnerform.getUser();
		userInfo.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setLastLoginIp(request.getRemoteAddr());
		userInfo.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setStatus("1");
		userInfo.setLoginTimes(new Integer(0));
		userInfo.setUserType(RamConstants.UserTypeIsLearner3);
		
		String dir = read.getValue("SYSTEM.FILESERVER.PATH") + read.getValue("FILEUPLOAD.FOLDER") + "/userface/";
		try{
			if(!(new File(dir).isDirectory())){new File(dir).mkdir();}
		}catch(SecurityException e){}
		if(learnerform.getPicture()!= null && learnerform.getPicture().getFileSize() > 0){
			pictrue = uploadFile(dir, learnerform.getPicture());
			if(pictrue != null){
				if(userInfo.getPicture() != null){
					try{
					new File(dir+"/"+userInfo.getPicture()).delete();
					}catch(SecurityException e){}
				}
				userInfo.setPicture(pictrue);
			}
		}
		learner.setUser(userInfo);
		learner.setSemesterId(learnerform.getSemesterId());
		UserGroupRl userGroupRl = new UserGroupRl();
		UserRoleRl userRoleRl = new UserRoleRl();
		userService.saveLearner(learner, user);
		userGroupRl.setUser(userInfo);
		userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(RamConstants.UserTypeIsLearner3)));
		userService.saveUserGroupRl(userGroupRl, user);
		return mapping.findForward("create");
	}
	/**
	 * 上传文件处理
	 * @param dir
	 * @param file
	 * @return
	 */
	public String uploadFile(String dir, FormFile file){
		ArrayList arg = (new ArrayList());
		arg.add(".jpg");
		arg.add(".bmp");
		arg.add(".png");
		arg.add(".gif");
		String suffix = file.getFileName().substring(file.getFileName().lastIndexOf(".")).toLowerCase();
		String filename = System.currentTimeMillis() + suffix;
		if(arg.indexOf(suffix)==-1) return null;
		try{
			InputStream si = file.getInputStream();
			OutputStream os = new FileOutputStream(dir + "/" + filename);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while((bytesRead = si.read(buffer, 0, 8192)) != -1){
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			si.close();
			file.destroy();
		}catch(Exception e){filename = null;}
		return filename;
	}
}
