package com.ram.web.user.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.Tutor;
import com.ram.model.User;
import com.ram.model.UserGroupRl;
import com.ram.service.system.ISemesterService;
import com.ram.service.user.IManagerPositionService;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.UserRegistForm;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
/**
 * @author lixiaodong 
 */
public class UserRegistAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	private final IManagerPositionService managerPositionService = (IManagerPositionService)getService("managerPositionService");
	private final ISemesterService semesterService= (ISemesterService)getService("semesterService");
	
	/**
	 * 显示用户创建页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("init");
	}
	
	/**
	 * 创建用户，并显示创建用户的选择以便创建老师和管理者

	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward regist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserRegistForm userRegistSaveForm = (UserRegistForm)form;
		User operateUser=(User)request.getAttribute("loginUser");
		
		User userInfo = userRegistSaveForm.getUser();
		if(userService.checkUserExist(userInfo.getLoginName())){
			request.setAttribute("loginName_exist", "1");
			return mapping.findForward("init");
			
		}
		else{
			request.getSession().removeAttribute("userGroupCollection");
			request.getSession().removeAttribute("roleCollection");
		}
		if(!userRegistSaveForm.getUser().getPassword().equals(userRegistSaveForm.getPasswordConfirm())){
			request.setAttribute("passwordWrong", "1");
			return mapping.findForward("init");
		}
		userInfo.setStatus("1");
		userInfo.setLastLoginIp(request.getRemoteAddr());
		userInfo.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setLoginTimes(new Integer(0));
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		// 根据userKind判断创建管理者,老师,还是学生
		if(userRegistSaveForm.getUserKind()==Integer.parseInt(RamConstants.UserTypeIsTutor1)){
			//创建老师用户
			userInfo.setUserType(RamConstants.UserTypeIsTutor1);
			Tutor tutor = new Tutor();
			tutor.setUser(userInfo);
			userService.saveTutor(tutor, user);
			UserGroupRl userGroupRl = new UserGroupRl();
			userGroupRl.setUser(userInfo);
			userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(RamConstants.UserTypeIsTutor1)));
			userService.saveUserGroupRl(userGroupRl, user);
		}
		else if(userRegistSaveForm.getUserKind()==Integer.parseInt(RamConstants.UserTypeIsManager2)){
			//创建管理用户---注意，此处修改为用户注册的时候，注册管理员的时候，类型为管理员，但是用户组为新用户组4，防止权限泄露
			userInfo.setUserType(RamConstants.UserTypeIsManager2);
			Manager manager = new Manager();
			manager.setUser(userInfo);
			log.info("userInfo.getUserId=" + userInfo.getUserId());
			manager=managerPositionService.saveAndReturnManager(manager, operateUser);
			log.info("userInfo.getUserId=" + manager.getUser().getUserId());
			UserGroupRl userGroupRl = new UserGroupRl();
			userGroupRl.setUser(manager.getUser());
			//但是用户组为新用户组4，防止权限泄露
			userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(RamConstants.UserTypeIsNewUser4)));
			userService.saveUserGroupRl(userGroupRl, operateUser);
		}
		else{
			//创建学生
			userInfo.setUserType(RamConstants.UserTypeIsLearner3);
			Learner learner = new Learner();
			learner.setSemesterId(semesterService.getCurrentSemester().getSemesterId());
			learner.setProgramId(new Integer(1));
			learner.setUser(userInfo);
			learner.setEnrollStatus("1");
			userService.saveLearner(learner, user);
			UserGroupRl userGroupRl = new UserGroupRl();
			userGroupRl.setUser(userInfo);
			userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(RamConstants.UserTypeIsLearner3)));
			userService.saveUserGroupRl(userGroupRl, user);
		}
		
		return mapping.findForward("success");
	}

	public ActionForward createMang(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		UserRegistForm userRegistSaveForm = (UserRegistForm)form;
		User operateUser=(User)request.getAttribute("loginUser");
		
		User userInfo = userRegistSaveForm.getUser();
		if(userService.checkUserExist(userInfo.getLoginName())){
			request.setAttribute("loginName_exist", "1");
			return mapping.findForward("init");			
		}
		userInfo.setStatus("1");
		userInfo.setLastLoginIp(request.getRemoteAddr());
		userInfo.setRegistDateTime(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
		userInfo.setLoginTimes(new Integer(0));
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		userInfo.setUserType(RamConstants.UserTypeIsManager2);
		Manager manager = new Manager();
		manager.setUser(userInfo);
		log.info("userInfo.getUserId=" + userInfo.getUserId());
		manager=managerPositionService.saveAndReturnManager(manager, operateUser);
		log.info("userInfo.getUserId=" + manager.getUser().getUserId());
		UserGroupRl userGroupRl = new UserGroupRl();
		userGroupRl.setUser(manager.getUser());
		//但是用户组为新用户组4，防止权限泄露

		userGroupRl.setUserGroup(userService.getUserGroupByID(Integer.parseInt(RamConstants.UserTypeIsNewUser4)));
		userService.saveUserGroupRl(userGroupRl, operateUser);
		
		return mapping.findForward("success");
	}
	
	/**
	 * 检查输入的用户名是否存在
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkUse(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws Exception {
//		UserRegistForm userRegistSaveForm = (UserRegistForm)form;
//		User userInfo = userRegistSaveForm.getUser();
//		if(!"".equals(userInfo.getLoginName())){
//			if(userService.checkUserExist(userInfo.getLoginName())){
//				request.setAttribute("loginName_exist", "1");
//			}
//			else{
//				request.setAttribute("loginName_notExist", "1");
//			}
//		}
//		else{
//			request.setAttribute("loginName_null_error", "1");
//		}
//		request.setAttribute("userRegistSaveForm", userRegistSaveForm);
//		return mapping.findForward("init");
		
		String username = ParamUtils.getParameter(req, "username", "").trim();
		if(username.length()>0){
			if(userService.checkUserExist(username)){username = "";}
		}
		try{//为ajax的 onreadystatuschange 提供返回内容
			PrintWriter out = res.getWriter();
			res.setContentType("text/plain");
			out.print(username.length()==0?"yes":"no");
		}catch(IOException e){}
		return null;
	}
}
