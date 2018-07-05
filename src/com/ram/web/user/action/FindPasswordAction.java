package com.ram.web.user.action;

import com.framework.web.action.BaseDispatchAction;

public class FindPasswordAction extends BaseDispatchAction {

	/* forward name="findPasswordSuccess" path="/findPasswordSuccess.jsp" */
	private final static String FINDPASSWORDSUCCESS = "findPasswordSuccess";

	/* forward name="findPasswordFailure" path="/findPasswordFailure.jsp" */
	private final static String FINDPASSWORDFAILURE = "findPasswordFailure";


	
//	public ActionForward findPassword(ActionMapping mapping, ActionForm form,
//	HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//		FindPasswordFrom fpForm=(FindPasswordFrom)form;
//		String loginName=fpForm.getLoginName();
//		String email=fpForm.getEmail();
//		
//		if(loginName!=null && email!=null){
//			IUserService userService=(IUserService)this.getService("userService");
//			Integer userId=userService.getUser(loginName);
//			//log.info("找回密码；userid=" + userId);
//			//log.info("找回密码：输入的email=" +email);
//			if(userId!=null){
//				User user=userService.getUser(userId.intValue());
//				//log.info("找回密码；user=" + user);
//				//log.info("找回密码；user.email=" + user.getUserEmail());
//				if(email.equals(user.getUserEmail())){
//					SendMailUtil.MailToStudent(user.getUserEmail(),
//							"北外网院学习平台找回密码提醒",
//							"你好：<br><br>" +
//							"你于"+DateTimeUtil.getJavaSQLDateNow()+"" +
//							"在IP地址为"+request.getRemoteAddr()+"的电脑" +
//							"申请找回自己的平台密码。<br><br>" +
//							"出于安全考虑，本邮件不告知你自己的用户名，<br>" +
//							"你的密码是"+user.getPassword()+"，请经常登录网院教学平台学习！<br>" +
//							"<br><a target='_blank' href='http://www.ramonline.com/'>北外网院</a>"+
//							"<br>" + DateTimeUtil.getJavaSQLDateNow()
//							);
//					
//					return mapping.findForward(FINDPASSWORDSUCCESS);
//				}else{
//					//log.info("找回密码：用户的邮件地址不匹配");
//					return mapping.findForward(FINDPASSWORDFAILURE);
//				}
//			}else{
//				//log.info("找回密码：用户不存在");
//				return mapping.findForward(FINDPASSWORDFAILURE);
//			}
//		}else{
//			//log.info("输入信息有误！");
//			return mapping.findForward(FINDPASSWORDFAILURE);
//		}
//		
//	}


}
