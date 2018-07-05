package com.ram.web.user.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.TeacherForm;
import com.framework.common.properties.IReadProperties;
import com.framework.util.DateTimeUtil;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class TeacherModifyAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final IReadProperties read = (IReadProperties) getService("readProperties");
	
	/** 
	 * 显示修改teacher页面
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
		TeacherForm teacherform = (TeacherForm)form;
		Tutor tutor = null;
		String tutorId=null;
		if(request.getParameter("id") != null){
			tutorId=request.getParameter("id");
			tutor = userService.getTutor(Integer.parseInt(tutorId));
			teacherform.setTeacherId(Integer.valueOf(tutorId));
			request.setAttribute("userId", tutorId);
		}
		else if(request.getParameter("teacherId")!=null){
			tutorId=request.getParameter("teacherId");
			tutor = userService.getTutor(Integer.parseInt(tutorId));
			request.setAttribute("userId",tutorId);
		}
		if(tutor==null){
			User user=(User)request.getSession().getAttribute("loginUser");
			tutor=userService.getTutor(user.getUserId().intValue());
		}		
		List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
		Iterator item = tutorCenterList.iterator();
		Collection result = new ArrayList();
		while(item.hasNext()){
			TutorCenter tutorCenter = (TutorCenter)item.next();
			LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
			result.add(labelValueBean);
		}
		request.setAttribute("tcList", result);
		teacherform.setTeacher(tutor);
		teacherform.setUser(tutor.getUser());
		if(tutor.getBirthday() == null){
			teacherform.setBirthday(DateTimeUtil.getJavaSQLDateNow());
		}
		else{
			teacherform.setBirthday(tutor.getBirthday());
		}
		if(tutor.getTutorCenter() != null){
			teacherform.setTcId(tutor.getTutorCenter().getTcId());
		}
		request.setAttribute("teacherModifySaveForm", teacherform);
		return mapping.findForward("init");
	}
	
	/** 
	 * 修改Manager,并且返回Manager列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		String picture = null;
		TeacherForm teacherform = (TeacherForm)form;
		Tutor tutor = userService.getTutor(Integer.parseInt(request.getParameter("userId")));
		User userInfo = tutor.getUser();
		if(!"".equals(teacherform.getUser().getLoginName())){
			if(userService.checkUserExistAgain(teacherform.getUser().getLoginName(), userInfo.getUserId())){
				request.setAttribute("loginName_exist", "1");
				request.setAttribute("userId", tutor.getTutorId());
				return mapping.findForward("init");
			}
		}
		tutor.setAddress(teacherform.getTeacher().getAddress());
		tutor.setBirthday(teacherform.getBirthday());
		tutor.setHomePhone(teacherform.getTeacher().getHomePhone());
		tutor.setMobile(teacherform.getTeacher().getMobile());
		tutor.setMsn(teacherform.getTeacher().getMsn());
		tutor.setPostCode(teacherform.getTeacher().getPostCode());
		tutor.setQq(teacherform.getTeacher().getQq());
		tutor.setTutorCenter(systemTutorCenterService.getSystemTutorCenter(teacherform.getTcId()));
		tutor.setIsAppraise(teacherform.getTeacher().getIsAppraise());
		userInfo.setLoginName(teacherform.getUser().getLoginName());
		if(teacherform.getUser().getPassword()!=null && teacherform.getUser().getPassword().length()>0){
			userInfo.setPassword(teacherform.getUser().getPassword());
		}
		userInfo.setUserEmail(teacherform.getUser().getUserEmail());
//		userInfo.setUserNameEn(teacherform.getUser().getUserNameEn());
		userInfo.setUserNameZh(teacherform.getUser().getUserNameZh());
		String dir = read.getValue("SYSTEM.FILESERVER.PATH") + read.getValue("FILEUPLOAD.FOLDER") + "/userface/";
		try{
			if(!(new File(dir).isDirectory())){new File(dir).mkdir();}
		}catch(SecurityException e){}
		if(teacherform.getPicture() != null && teacherform.getPicture().getFileSize() > 0){
			picture = uploadFile(dir, teacherform.getPicture());
			if(picture != null){
				if(userInfo.getPicture()!=null){
					try{
					new File(dir+"/"+userInfo.getPicture()).delete();
					}catch(SecurityException e){}
				}
				userInfo.setPicture(picture);
			}
		}
		userService.saveTutor(tutor, user);
		return mapping.findForward("modify");
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
