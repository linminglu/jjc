package com.apps.web.action;

import help.base.APIConstants;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.IPUtil;

/**
 * @author Mr.zang
 */
public class MobileAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService) getService("userService");

	/**
	 */
	public ActionForward QRCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String u = ParamUtils.getParameter(request, "u");// 邀请码
		// String pageType = ParamUtils.getParameter(request, "pageType");//
		// 页面来源

		// System.out.println("_________u:"+u);

		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		Integer uid = Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
		User user = userService.getUser(uid);

		request.setAttribute("user", user);
		return mapping.findForward("QRCode");
	}

	/**
	 * 推广注册初始化
	 */
	public ActionForward regInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		String key = ParamUtils.getParameter(request, "key");// 推广用户key
		String isValid = "fail";
		if (ParamUtils.chkString(key)) {
			isValid = "ok";
		}

		request.setAttribute("inviteCode", key);
		request.setAttribute("isValid", isValid);
		return mapping.findForward("regInit");
	}

	/**
	 * 推广注册保存
	 */
	public ActionForward reg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String loginName = ParamUtils.getParameter(request, "loginName");
		String password = ParamUtils.getParameter(request, "password");
		String inpcode = ParamUtils.getParameter(request, "inpcode", "");// 验证码
		String inviteCode = ParamUtils.getParameter(request, "inviteCode", "");// 邀请码

		String sdCode = (String) request.getSession().getAttribute(
				APIConstants.SESSION_SMS_CODE_NAME);// 短信验证码放到session里
		String errMsg = "";
		String errCode = "";
		String smsPhone = (String) request.getSession().getAttribute(
				APIConstants.SESSION_SMS_PHONE);

		Map<String, String> blacklist = Constants.getBlacklist();

		String black = blacklist.get(inviteCode);
		if (ParamUtils.chkString(inviteCode) && inviteCode.equals(black)) {
			request.getSession()
					.removeAttribute(APIConstants.SESSION_SMS_PHONE);
			request.getSession().removeAttribute(
					APIConstants.SESSION_SMS_CODE_NAME);
			errMsg = "验证码不正确";
			log.info("________________________________________________________黑名单");
		}

		log.error("M___inpcode>" + inpcode + "___sdCode>" + sdCode
				+ "___loginName>" + loginName + "___smsPhone>" + smsPhone
				+ "___date>" + DateTimeUtil.getDateTime()
				+ "]rec>>>>[/reg]sessionid>>>" + request.getSession().getId());
		// if(inpcode.trim().length()<=0||sdCode==null){
		if (!ParamUtils.chkString(inpcode) || !ParamUtils.chkString(sdCode)) {
			// request.setAttribute("msg", "验证码不正确");
			errMsg = "验证码不正确";
		} else if (!loginName.equals(smsPhone)) {
			log.error("_________________________________刷__/reg");
			errMsg = APIConstants.USER_TIPS_CODE_ERROR;
		}
		if (sdCode!=null&& !sdCode.equals(inpcode)) {
			log.info("_________________验证码不正确");
			errMsg = "验证码不正确";
		}
		if (!errMsg.equals("")) {
			errCode = "sdCodeErr";
		} else {
			loginName = loginName.trim();
			User user = userService.getUserByLoginName(loginName);
			if (user != null) {// 已经注册过了
				errMsg = APIConstants.USER_TIPS_UN_EXISTS;
				errCode = "repeat";
			} else {
				User regUser = new User();
				regUser.setLoginName(loginName);
				regUser.setUserName(loginName);
				regUser.setCellPhone(loginName);
				regUser.setPassword(password);
				regUser.setUserType(Constants.USER_TYPE_SUER);
				regUser.setStatus(Constants.PUB_STATUS_OPEN);
				regUser.setRegistDateTime(new Date());
				regUser.setLoginTimes(0);
				regUser.setInvitationCode(inviteCode);
				regUser = (User) userService.saveObjectDB(regUser);
				request.setAttribute("user", regUser);
				request.getSession().removeAttribute(
						APIConstants.SESSION_SMS_PHONE);
				request.getSession().removeAttribute(
						APIConstants.SESSION_SMS_CODE_NAME);
				
//				Date lastLoginDate = regUser.getLastLoginDate();

				regUser.setLastLoginDate(new Date());
				Integer loginTimes = ParamUtils
						.chkInteger(regUser.getLoginTimes()) ? regUser
						.getLoginTimes() : 0;
			    regUser.setLoginTimes(loginTimes + 1);
				regUser.setLastLoginIp(IPUtil.getIpAddr(request));
				userService.saveUser(regUser);
				// 推荐短信2.发送短信
//				if (lastLoginDate == null) {
//					if(ParamUtils.chkString(inviteCode)&&!inviteCode.equals(black)){
//						userService.saveVipLevel(regUser);
//					}
//				}
				
				// 注册成功
				// 2.发送短信
				// userService.saveVipLevel(regUser);
				return mapping.findForward("regSucc");
			}
		}
		request.setAttribute("loginName", loginName);
		request.setAttribute("inpcode", inpcode);
		request.setAttribute("inviteCode", inviteCode);
		request.setAttribute("errMsg", errMsg);
		request.setAttribute("errCode", errCode);
		return mapping.findForward("reg");
	}

	/**
	 * 推广注册初始化
	 */
	public ActionForward applyShop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		String key = ParamUtils.getParameter(request, "key");// 推广用户key
		String isValid = "fail";
		if (ParamUtils.chkString(key)) {
			isValid = "ok";
		}

		request.setAttribute("inviteCode", key);
		request.setAttribute("isValid", isValid);
		return mapping.findForward("applyShop");
	}
}
