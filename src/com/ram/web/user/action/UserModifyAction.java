package com.ram.web.user.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;

import com.ram.RamConstants;
import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IManagerPositionService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.UserInfoForm;
import com.framework.common.properties.IReadProperties;
import com.framework.util.ParamUtils;
import com.framework.util.RequestUtil;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class UserModifyAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService= (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
    private final IReadProperties read = (IReadProperties) getService("readProperties");
	
	/** 
	 * 显示用户修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		log.info("in UserModifyAction.java ---init");
		HttpSession session = request.getSession(false);
		Integer userId = null;
		String userType = null;
		User user=null;
		userId=(Integer)request.getAttribute("userId");
		log.info("in UserModifyAction.java ---init--userId=" + userId);
		if(userId!=null){
			user=userService.getUser(userId.intValue());
			if(user!=null){
				log.info("user.loginName=" + user.getLoginName());
				log.info("user.userNamezh=" + user.getUserNameZh());
				log.info("user.getUserType=" + user.getUserType());
			}
		}else{
			//user=(User)request.getAttribute("loginUser");
			user = (User)request.getSession().getAttribute("loginUser");
			user = userService.getUser(user.getUserId().intValue());
		}
		userId = user.getUserId();
		userType = user.getUserType();
		log.info("222user.loginName=" + user.getLoginName());
		log.info("222user.userNamezh=" + user.getUserNameZh());
		log.info("222user.getUserType=" + user.getUserType());		
		if(RamConstants.UserTypeIsTutor1.equals(userType)){//如果是教师1
			log.info("user type is 教师");
			UserInfoForm tutorForm = (UserInfoForm)form;
			List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
			
			Iterator item = tutorCenterList.iterator();
			Collection result = new ArrayList();
			while(item.hasNext()){
				TutorCenter tutorCenter = (TutorCenter)item.next();
				LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
				result.add(labelValueBean);
			}
			request.setAttribute("tcList", result);
			
			tutorForm.setUser(user);
			
			Tutor tutor = userService.getTutor(userId.intValue());
			log.info("tutor.birthday is :" + tutor.getBirthday());
			//tutor.setBirthday(DateTimeUtil.getDateFormart_YYYY_MM_DD(tutor.getBirthday()));
			log.info("tutor.birthday is :" + tutor.getBirthday());
			if(tutor!=null){
				tutorForm.setTutor(tutor);
				tutorForm.setBirthday(tutor.getBirthday());
			}else{
				tutorForm.setTutor(new Tutor());
			}
			request.setAttribute("tutorInfoModifyForm", tutorForm);
			return mapping.findForward("init");
		}

		else if(RamConstants.UserTypeIsLearner3.equals(userType)){//如果是学生3
			log.info("user type is 学生");
			UserInfoForm learnerForm = (UserInfoForm)form;
			List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
			Iterator item = tutorCenterList.iterator();
			Collection result = new ArrayList();
			while(item.hasNext()){
				TutorCenter tutorCenter = (TutorCenter)item.next();
				LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
				result.add(labelValueBean);
			}
			request.setAttribute("tcList", result);
			learnerForm.setUser(user);
			Learner learner = userService.getLearnerByUserId(userId.intValue());
			if(learner!=null){
				learnerForm.setLearner(learner);			
				learnerForm.setBirthday(learner.getBirthday());
			}else{
				learnerForm.setLearner(new Learner());
			}
			request.setAttribute("learnerInfoModifyForm", learnerForm);
			return mapping.findForward("init");
		}
		else {//如果是管理员2
			log.info("user type is 管理员");
			UserInfoForm managerForm = (UserInfoForm)form;
			List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
			Iterator item = tutorCenterList.iterator();
			Collection result = new ArrayList();
			while(item.hasNext()){
				TutorCenter tutorCenter = (TutorCenter)item.next();
				LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
				result.add(labelValueBean);
			}
			request.setAttribute("tcList", result);
			managerForm.setUser(user);
			
			Manager manager = userService.getManager(userId.intValue());
			log.info("manager.birthday is :" + manager.getBirthday());
			//manager.setBirthday(DateTimeUtil.getDateFormart_YYYY_MM_DD(manager.getBirthday()));
			log.info("manager.birthday is :" + manager.getBirthday());
			if(manager!=null){
				managerForm.setManager(manager);			
				managerForm.setBirthday(manager.getBirthday());
			}else{
				managerForm.setManager(new Manager());							
			}
			request.setAttribute("managerInfoModifyForm", managerForm);
			return mapping.findForward("init");
		}		
		
	}
	
	/** 
	 * 用户信息修改页面并返回用户信息修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		log.info("in UserModifyAction.java ---modify");
		HttpSession session = request.getSession(false);
		Integer userId = null;
		String userType = null;
		String pictrue = null;
		User user=null;
		int uid = ParamUtils.getIntParameter(request, "user.userId", 0);
		userId=(Integer)request.getAttribute("userId");
		userId=userId==null?(uid==0?null:new Integer(uid)):userId;
		if(userId!=null){
			user=userService.getUser(userId.intValue());
		}else{
			user=(User)request.getAttribute("loginUser");			
		}
		if(user != null){
			userId = user.getUserId();
			userType = user.getUserType();
		}
		request.setAttribute("userId", user.getUserId());
		String dir = read.getValue("SYSTEM.FILESERVER.PATH") + read.getValue("FILEUPLOAD.FOLDER") + "/userface/";
		try{
			if(!(new File(dir).isDirectory())){new File(dir).mkdir();}
//			if(proveForm.getZfile() != null && proveForm.getZfile().getFileSize() > 0){
//				if(prove.getProveZhTpl()!=null){
//					new File(dir+"/"+prove.getProveZhTpl()).delete();
//				}
//				prove.setProveZhTpl(uploadFile(dir, proveForm.getZfile()));
//			}
//			if(proveForm.getEfile() != null && proveForm.getEfile().getFileSize() > 0){
//				if(prove.getProveEnTpl()!=null){
//					new File(dir+"/"+prove.getProveEnTpl()).delete();
//				}
//				prove.setProveEnTpl(uploadFile(dir, proveForm.getEfile()));
//			}
		}catch(SecurityException e){}
		if(RamConstants.UserTypeIsTutor1.equals(userType)){
			//如果当前用户是教师			UserInfoForm tutorForm = (UserInfoForm)form;
			Tutor tutor = userService.getTutor(userId.intValue());
			//User user = tutor.getUser();
			tutor.setAddress(tutorForm.getTutor().getAddress());
			tutor.setBirthday(tutorForm.getBirthday());
			tutor.setHomePhone(tutorForm.getTutor().getHomePhone());
			tutor.setMobile(tutorForm.getTutor().getMobile());
			tutor.setMsn(tutorForm.getTutor().getMsn());
			tutor.setPostCode(tutorForm.getTutor().getPostCode());
			tutor.setQq(tutorForm.getTutor().getQq());
			//tutor.setTutorCenter(tutorForm.getTutor().getTutorCenter());
			//if(tutorForm.getUser().getLoginName()!=null)user.setLoginName(tutorForm.getUser().getLoginName());
//			if(tutorForm.getUser().getPassword()!=null&&tutorForm.getUser().getPassword().equals("")==false){
//				user.setPassword(tutorForm.getUser().getPassword());
//			}
			user.setUserEmail(tutorForm.getUser().getUserEmail());
//			user.setUserNameEn(tutorForm.getUser().getUserNameEn());
			user.setUserNameZh(tutorForm.getUser().getUserNameZh());
//			user.setNickname( tutorForm.getUser().getNickname() );
//			if( tutorForm.getUserSystemImage().equals( "000" )){
//				user.setUserimage( tutorForm.getUser().getUserimage() );
//			}else{
//				user.setUserimage( tutorForm.getUserSystemImage() + ".gif" );
//			}
			if(tutorForm.getPictrue() != null && tutorForm.getPictrue().getFileSize() > 0){
				pictrue = uploadFile(dir, tutorForm.getPictrue());
				if(pictrue != null){
					if(user.getPicture()!=null){
						try{
						new File(dir+"/"+user.getPicture()).delete();
						}catch(SecurityException e){}
					}
					user.setPicture(pictrue);
				}
			}
			tutor.setUser(user);
			userService.saveTutor(tutor, user);
			return mapping.findForward("modify");
		}
		else if(RamConstants.UserTypeIsManager2.equals(userType)){
			//如果当前用户是管理员
			UserInfoForm managerForm = (UserInfoForm)form;
			
			user.setLoginName(managerForm.getUser().getLoginName());
//			user.setPassword(managerForm.getUser().getPassword());
			user.setUserEmail(managerForm.getUser().getUserEmail());
//			user.setUserNameEn(managerForm.getUser().getUserNameEn());
			user.setUserNameZh(managerForm.getUser().getUserNameZh());	
			
//			user.setNickname( managerForm.getUser().getNickname() );
			if( managerForm.getUserSystemImage().equals( "000" )){
//				user.setUserimage( managerForm.getUser().getUserimage() );
			}else{
//				user.setUserimage( managerForm.getUserSystemImage() + ".gif" );
			}
			
			Manager manager = userService.getManager(userId.intValue());
			if(manager==null){
				manager=new Manager();
			}
			manager.setAddress(managerForm.getManager().getAddress());
			manager.setBirthday(managerForm.getBirthday());
			manager.setHomePhone(managerForm.getManager().getHomePhone());
			manager.setMobile(managerForm.getManager().getMobile());
			manager.setMsn(managerForm.getManager().getMsn());
			manager.setPostCode(managerForm.getManager().getPostCode());
			manager.setQq(managerForm.getManager().getQq());
			manager.setTcId(managerForm.getManager().getTcId());
			if(managerForm.getPictrue() != null && managerForm.getPictrue().getFileSize() > 0){
				pictrue = uploadFile(dir, managerForm.getPictrue());
				if(pictrue != null){
					if(user.getPicture() != null){
						try{
						new File(dir+"/"+user.getPicture()).delete();
						}catch(SecurityException e){}
					}
					user.setPicture(pictrue);
				}
			}
			manager.setUser(user);
			//

			managerPositionService.saveManager(manager, user);
			return mapping.findForward("modify");
		}
		else{
			//如果当前用户是学生
			UserInfoForm learnForm = (UserInfoForm)form;
			Learner learner = userService.getLearnerByUserId(userId.intValue());
			//User userInfo = learner.getUser();
			
			learner.setAddress(learnForm.getLearner().getAddress());
			learner.setBirthday(learnForm.getBirthday());
			learner.setHomePhone(learnForm.getLearner().getHomePhone());
			learner.setMobile(learnForm.getLearner().getMobile());
			learner.setWorkplace(learnForm.getLearner().getWorkplace());
			learner.setMsn(learnForm.getLearner().getMsn());
			learner.setPostCode(learnForm.getLearner().getPostCode());
			learner.setQq(learnForm.getLearner().getQq());
			//learner.setTcId(learnForm.getLearner().getTcId());
			//learner.setEnrollStatus(learnForm.getLearner().getEnrollStatus());
			learner.setLearnSex(learnForm.getLearner().getLearnSex());
			learner.setEducation(learnForm.getLearner().getEducation());
			//userInfo.setLoginName(learnForm.getUser().getLoginName());//学生无法修改自己的用户名
			learner.setInvoiceTitle(learnForm.getLearner().getInvoiceTitle());
			user.setUserEmail(learnForm.getUser().getUserEmail());
//			user.setUserNameEn(learnForm.getUser().getUserNameEn());
			user.setUserNameZh(learnForm.getUser().getUserNameZh());
//			user.setNickname( learnForm.getUser().getNickname() );
			if( learnForm.getUserSystemImage().equals( "000" )){
//				user.setUserimage( learnForm.getUser().getUserimage() );
			}else{
//				user.setUserimage( learnForm.getUserSystemImage() + ".gif" );
			}
//			if(learnForm.getUser().getPassword()!=null&&learnForm.getUser().getPassword().equals("")==false){
//				user.setPassword(learnForm.getUser().getPassword());
//			}
			if(learnForm.getPictrue() != null && learnForm.getPictrue().getFileSize() > 0 
				&& learnForm.getPictrue().getFileSize() < 819200){//小于800k
				pictrue = uploadFile(dir, learnForm.getPictrue());
				if(pictrue != null){
					if(user.getPicture() != null){
						try{
						new File(dir+"/"+user.getPicture()).delete();
						}catch(SecurityException e){}
					}
					user.setPicture(pictrue);
				}
			}
			learner.setUser(user);
			userService.saveLearner(learner, user);
			return mapping.findForward("modify");
		}
	}
	
	/**
	 * 修改密码
	 * 此方法与xmlHttpRequest对应
	 * @author Lu Congyu
	 * @date 06/10/13
	 */
	public ActionForward pwd(ActionMapping mapping, ActionForm form,
		HttpServletRequest req, HttpServletResponse res){
		//获取当前用户
  		User user = null;
  		user = ( User )req.getSession( false ).getAttribute( "loginUser" );
  		
		String pwd = ParamUtils.getParameter(req, "pwd", "");
		String password = ParamUtils.getParameter(req, "password", "");
		String rs = "yes";
		if(user.getLoginName().equals(password)){//新密码和登录名相同
			rs = "same";
		}else if(password.length()<6){//新密码长度小于6个字符
			rs = "len";
		}else if(user.getPassword().equals(pwd)){//输入的旧密码正确
			user.setPassword(password);
			userService.saveUser(user, user);
		}else{rs = "error";}
		try{
			PrintWriter out = res.getWriter();
			res.setContentType("text/plain");
			out.print(rs);
		}catch(IOException e){}
		return null;
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
