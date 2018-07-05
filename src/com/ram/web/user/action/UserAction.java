package com.ram.web.user.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.apps.util.ImageUtils;
import com.apps.util.JsonUtil;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.UserForm;

public class UserAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("init");
	}

	/**
	 * 编辑个人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward profile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = this.getUserId(request);
		User user = userService.getUser(userId);
		request.getSession(true).setAttribute("loginUser", user);
		// UserInfo userInfo = userService.getUserInfo(userId);
		// if (userInfo == null) {
		// userInfo = new UserInfo();
		// }

		UserForm uForm = (UserForm) form;
		uForm.setUser(user);
		// uForm.setUserInfo(userInfo);
		return mapping.findForward("profile");
	}

	/**
	 * 修改密码初始化
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = this.getUserId(request);
		User user = userService.getUser(userId);
		UserForm uForm = (UserForm) form;
		uForm.setUser(user);
		return mapping.findForward("password");
	}

	/**
	 * 修改安全问题初始化
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward security(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = this.getUserId(request);
		User user = userService.getUser(userId);
		UserForm uForm = (UserForm) form;
		uForm.setUser(user);
		return mapping.findForward("security");
	}

	/**
	 * 更改个人信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveProfile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm uForm = (UserForm) form;
		User userForm = uForm.getUser();
		Integer userId = userForm.getUserId();
		User userSaver = null;
		if (ParamUtils.chkInteger(userId)) {
			userSaver = userService.getUser(userId);
			userSaver.setUserName(userForm.getUserName());
			// userSaver.setUserEmail(userForm.getUserEmail());
			userSaver.setGender(userForm.getGender());
			userSaver.setCellPhone(userForm.getCellPhone());
			userSaver.setMotto(userForm.getMotto());
			userSaver.setExtend1(userForm.getExtend1());
			userSaver.setExtend2(userForm.getExtend2());
			userSaver.setExtend3(userForm.getExtend3());
		} else {
			userSaver = userForm;
		}
		userService.saveUser(userSaver, userSaver);
		request.setAttribute("error", "ok");
		return this.myhome(mapping, uForm, request, response);
	}

	/**
	 * 修改密码
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward savePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserForm uForm = (UserForm) form;
		User userFormInfo = uForm.getUser();// 网页中填写的user信息
		// log.info("原密码：" + userFormInfo.getPassword() + "newPwd:"
		// + uForm.getNewPassword() + "cNewPwd:"
		// + uForm.getConfirmPassword());

		// if (uForm.getNewPassword().trim().length() < 6) {
		// request.setAttribute("msg", "密码要大于6位!!");
		// return mapping.findForward("error");
		// }
		// if (uForm.getConfirmPassword().trim().length() < 6) {
		// request.setAttribute("msg", "密码要大于6位!!");
		// return mapping.findForward("error");
		// }
		// if (!uForm.getNewPassword().trim()
		// .equals(uForm.getConfirmPassword().trim())) {
		// request.setAttribute("msg", "两次输入新密码不一样!!");
		// return mapping.findForward("error");
		// }

		Integer userId = userFormInfo.getUserId();
		User user = null;
		if (ParamUtils.chkInteger(userId)) {
			user = userService.getUser(userId);// 数据库中的user信息
			if (user.getPassword().trim().equals(uForm.getPassword().trim())) {

				String newPassword = uForm.getNewPassword();
				String newPassword2 = uForm.getNewPassword2();
				if (newPassword.equals(newPassword2)) {
					user.setPassword(newPassword);
					userService.saveUser(user, loginedUser);
				}
				request.setAttribute("error", "修改成功！！");
			} else {
				request.setAttribute("error", "原密码不正确！");
			}
		} else {
			log.info("## userId---->" + userFormInfo.getUserId());
			request.setAttribute("error", "不正确的用户ID！");
		}
		return mapping.findForward("savePassword");
	}

	/**
	 * 修改安全问题
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveSecurity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// UserForm uForm = (UserForm) form;
		// User userForm = uForm.getUser();
		// Integer userId = userForm.getUserId();
		// User userSaver = null;
		// if (ParamUtils.chkInteger(userId)) {
		// userSaver = userService.getUser(userId);
		// userSaver.setSecurityQuestion(userForm.getSecurityQuestion());
		// userSaver.setAnswer(userForm.getAnswer());
		// } else {
		// userSaver = userForm;
		// }
		// userService.saveUser(userSaver, userSaver);
		// // this.updateUserLog(request, userSaver, "修改安全问题");
		// request.getSession().setAttribute("suss", "Modified successfully!!");
		return mapping.findForward("saveSecurity");
	}

	public ActionForward forgotPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// System.out.println("11111111111111111111");
		return mapping.findForward("forgotPassword");
	}

	/**
	 * 上传头像
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadLogo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = this.getUserId(request);
		if (!ParamUtils.chkInteger(userId)) {// 判断是否是正确的用户id
			request.setAttribute("msg", "不正确的用户ID！！");
			return mapping.findForward("error");
		}
		String logoSrc = ParamUtils.getParameter(request, "logoSrc");
		String logoMiniSrc = ParamUtils.getParameter(request, "logoMiniSrc");
		String temFileName = ParamUtils.getParameter(request, "temFileName");

		// System.out.println(temFileName+"%%%%%");

		User user = userService.getUser(userId);
		user.setLogo(logoSrc);
		user.setLogoMini(logoSrc);
		// user.setLogoMini(logoMiniSrc);
		userService.saveUser(user, user);
		request.getSession(true).setAttribute("loginUser", user);

		// 删除临时文件
		// int lastIndexOf = logoSrc.lastIndexOf(".");
		// int length = logoSrc.length();
		// String suff = logoSrc.substring(lastIndexOf, length);
		String path = request.getSession().getServletContext().getRealPath("/")
				+ "upload\\";
		// deleteFile(path+"tmp\\"+getUserId(request)+suff);
		deleteFile(path + "tmp\\" + temFileName);

		return mapping.findForward("uploadLogo");

	}

	/**
	 * 上传头像
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward proResetLogo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Integer userId = getUserId(request);
		User user = userService.getUser(userId);
		request.getSession(true).setAttribute("loginUser", user);

		return mapping.findForward("proResetLogo");

	}

	/**
	 * 上传头像
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadLogo2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String logoSrc = ParamUtils.getParameter(request, "logoSrc");
		String logoMiniSrc = ParamUtils.getParameter(request, "logoMiniSrc");
		User user = getUser(request);
		String info = "";
		try {
			userService.modifyLogo(user, logoSrc, logoMiniSrc);
			info = "true";
		} catch (Exception e) {
			info = "error";
			e.printStackTrace();
		}

		AjaxWriter(response, info);
		return null;
		// return mapping.findForward("uploadLogo2");
	}

	/**
	 * 查看其他用户信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward otherUserInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		User otherUser = userService.getUser(userId);
		request.setAttribute("otherUser", otherUser);
		return mapping.findForward("otherUserInfo");

	}

	/**
	 * 进入个人中心
	 * 
	 * @return
	 */
	public ActionForward myhome(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = getUserId(request);
		User user = null;
		if (ParamUtils.chkInteger(userId)) {
			user = userService.getUser(userId);
		}
		UserForm userForm = (UserForm) form;
		userForm.setUser(user);
		request.getSession(true).setAttribute("loginUser", user);
		return mapping.findForward("myhome");
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * ajax输出流
	 * 
	 * @param response
	 * @param info
	 */
	private void AjaxWriter(HttpServletResponse response, String info) {
		PrintWriter out;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.println(info);
			out.flush();// 清空缓冲区
			out.close();// 关闭输入流
		} catch (IOException e) {
			log.error(e.toString());
		}
	}
	
	/**
	 * 确认用户是否存在
	 */
	public void findUserByIdOrName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String loginName = ParamUtils.getParameter(request, "loginName");
		String flag = "error";
		StringBuffer notExistName = new StringBuffer();
		String[] nameArr = loginName.split(",");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		if(nameArr.length>0){
			for(int i = 0;i<nameArr.length;i++){
				String tmp = nameArr[i];
				if(tmp.length()==11){
					// 手机号
					User user = userService.findUserByLoginName(tmp);
					if(user!=null){
						flag = "success";
					}else{
						flag = "fail";
						if(notExistName.length()==0){
							notExistName.append(tmp);
						}else{
							notExistName.append(","+tmp);
						}
					}
				}else{
					// ID
					User user = (User) userService.getObject(User.class, Integer.valueOf(tmp));
					if(user!=null){
						flag = "success";
					}else{
						flag = "fail";
						if(notExistName.length()==0){
							notExistName.append(tmp);
						}else{
							notExistName.append(","+tmp);
						}
					}
				}
			}
		}
		data.put("flag", flag);
		data.put("notExistName", notExistName);
		
		JsonUtil.AjaxWriter(response, data);
	}
}
