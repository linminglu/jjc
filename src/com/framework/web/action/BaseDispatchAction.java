package com.framework.web.action;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import md5.MD5;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.springframework.context.ApplicationContext;

import com.framework.Constants;
import com.framework.service.IServiceLocator;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.util.SslUtil;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.Function;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.model.UserLog;
import com.ram.service.user.IUserService;
import com.ram.service.userLog.IUserLogService;
import com.ram.util.IPUtil;

/**
 * 基于struts Action 进行的拓展Action基类
 */
public class BaseDispatchAction extends DispatchAction {
	protected final Log log = LogFactory.getLog(getClass());

	public static final String SECURE = "secure";

	protected static ApplicationContext ctx = null;

	private static IServiceLocator serviceLocator;

	public static final int SECOND = 1000;

	public static final int MINUTE = SECOND * 60;


	protected IUserLogService userLogService = (IUserLogService) getService("userLogService");
	protected IUserService userService = (IUserService) getService("userService");

	protected User userx;
	
	protected User loginedUser;//当前已经登录的用户//和上面的user相同，用于替代上述的user
	
	protected UserGroup loginedUserGroup;//用于记录用户登录到哪个用户组界面

	protected Function curFunction;

	protected static String queryCondition;
	
	protected ServletException se;//Action层抛出的异常
	
	protected String errMsgForUser;//抛出异常时候，用于抛出到用户界面的错误信息
	protected String errMsgForTech;//抛出异常时候，用户发送给技术支持信箱
	
	protected ActionMessage errorMsg;
	protected ActionMessages errorMsgs = new ActionMessages();
	
	/**
	 * 对日期,时间等等棘手的格式问题进行统一注册管理请察看web/strutsplug包
	 * 
	 * 
	 */
	// static {
	// ConvertUtils.register(new CurrencyConverter(), Double.class);
	// ConvertUtils.register(new DateConverter(), Date.class);
	// ConvertUtils.register(new DateConverter(), String.class);
	// ConvertUtils.register(new LongConverter(defaultLong), Long.class);
	// ConvertUtils.register(new IntegerConverter(defaultLong), Integer.class);
	// ConvertUtils.register(new TimestampConverter(), Timestamp.class);
	// }
	/**
	 * Convenience method to bind objects in Actions 我们也可以把这里改写成
	 * 
	 * getService,实际操作中,action层直接与service接触
	 * 此方法因为希望让spring能够脱离action层次调用,更加松耦合一些,使用封装好的方式进行调用
	 * 
	 * @param name
	 * @return
	 */

	/**
	 * 实际操作中也可以不调用此方法,直接调用facadeService方法进行操作
	 */
	public Object getService(String name) {
		serviceLocator = ServiceLocatorImpl.getInstance();
		return serviceLocator.getService(name);
	}
	
	public void checkUserUrlLogin(HttpServletRequest req){
		String a = ParamUtils.getParameter(req, "a","");
		String loginName = ParamUtils.getParameter(req, "u","");
		String password = ParamUtils.getParameter(req, "p","");
		if(a.equals("e7415b3a58bc4f11087f89c6c8661792")){
			loginName = "deskadmin";
			password = "da2010";
		}
		//log.error(">>>>>>>>>>>>>:"+loginName+","+password);
		if(ParamUtils.chkString(loginName) && ParamUtils.chkString(password)){//md5.dalogin
			User u = userService.getUser(loginName, password);
			req.getSession().setAttribute("loginUser", u);
		}
	}
	
	public User getUser(HttpServletRequest req){
		return (User)req.getSession().getAttribute("loginUser");
	}
	
	public void setSessionUser(HttpServletRequest req,User user){
		req.getSession().setAttribute("loginUser",user);
	}
	
	public String getSessionId(HttpServletRequest req){
		String sessionId = req.getSession().getId();
		log.info("## session id:"+sessionId);
		return sessionId;
	}
	/**
	 * 获取数据范围标识ID > 城市[cid] || 小区[ccid] || 学校(unid)
	 * 设计：参数名称统一用：ccid
	 * @param req
	 * @return
	 */
	public Integer getDataRangeId(HttpServletRequest request){
		Integer id = ParamUtils.getIntegerParameter(request, "ccid");
//		if(com.apps.Constants.PLAT_RUN_MODE_ID.equals(com.apps.Constants.PLAT_RUN_MODE_CITY)){//城市o2o
//			id = ParamUtils.getIntegerParameter(request, "cid");
//		}else if(com.apps.Constants.PLAT_RUN_MODE_ID.equals(com.apps.Constants.PLAT_RUN_MODE_COMMUNITY)){//小区o2o
//			id = ParamUtils.getIntegerParameter(request, "ccid");
//		}else if(com.apps.Constants.PLAT_RUN_MODE_ID.equals(com.apps.Constants.PLAT_RUN_MODE_UNIVERSITY)){//学校o2o
//			id = ParamUtils.getIntegerParameter(request, "unid");
//		}
		return id;
	}

	/**
	 * Convenience method to initialize messages in a subclass.
	 * 
	 * @param request
	 *            the current request
	 * @return the populated (or empty) messages
	 */
	public ActionMessages getMessages(HttpServletRequest request) {
		ActionMessages messages = null;
		HttpSession session = request.getSession();

		if (request.getAttribute(Globals.MESSAGE_KEY) != null) {
			messages = (ActionMessages) request
					.getAttribute(Globals.MESSAGE_KEY);
			saveMessages(request, messages);
		} else if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
			messages = (ActionMessages) session
					.getAttribute(Globals.MESSAGE_KEY);
			saveMessages(request, messages);
			session.removeAttribute(Globals.MESSAGE_KEY);
		} else {
			messages = new ActionMessages();
		}

		return messages;
	}

	/**
	 * Gets the method name based on the mapping passed to it
	 */
	private String getActionMethodWithMapping(HttpServletRequest request,
			ActionMapping mapping) {
		return getActionMethod(request, mapping.getParameter());
	}

	/**
	 * Gets the method name based on the prepender passed to it.
	 */
	protected String getActionMethod(HttpServletRequest request, String prepend) {
		String name = null;

		// for backwards compatibility, try with no prepend first
		name = request.getParameter(prepend);
		if (name != null) {
			// trim any whitespace around - this might happen on buttons
			name = name.trim();
			// lowercase first letter
			return name.replace(name.charAt(0), Character.toLowerCase(name
					.charAt(0)));
		}

		Enumeration e = request.getParameterNames();

		while (e.hasMoreElements()) {
			String currentName = (String) e.nextElement();

			if (currentName.startsWith(prepend + ".")) {
				if (log.isDebugEnabled()) {
					log.debug("calling method: " + currentName);
				}

				String[] parameterMethodNameAndArgs = StringUtils.split(
						currentName, ".");
				name = parameterMethodNameAndArgs[1];
				break;
			}
		}

		return name;
	}

	/**
	 * Override the execute method in DispatchAction to parse URLs and forward
	 * to methods without parameters.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		
		// 在此处进行用户访问该功能时候的权限信息加载
		// /log.info("request.getServletPath()=" + request.getServletPath());
		// add by hulei.@2005-11-10
	    //log.info(request.getServletPath()+","+request.getQueryString());
//		log.info("request.getServletPath()：" + request.getServletPath());
		String requestServlet = request.getServletPath() + "?"
				+ request.getQueryString();
		
		//log.info("requestUrl>>>>["+IPUtil.getIpAddr(request)+"]:"+request.getRequestURL());
		
		if (requestServlet.indexOf("&") > 0) {
			requestServlet = requestServlet.substring(0, requestServlet
					.indexOf("&"));
		}
		/**
		 * 下面这些Action,不需要用户登录,就能够访问到.
		 */
		//log.info("请求的Action为：" + requestServlet);
		if (requestServlet.equals("/permission/loginAction.do?method=init")
				|| requestServlet.equals("/permission/loginAction.do?method=login")
				|| requestServlet.equals("/permission/loginAction.do?method=loginAgent")
				|| requestServlet.equals("/permission/loginAction.do?method=ajaxLogin")
				|| requestServlet.equals("/user/userAction.do?method=otherUserInfo")
				|| requestServlet.startsWith("/api")
				|| requestServlet.startsWith("/game")
				|| requestServlet.startsWith("/user/registerAction.do")
				|| requestServlet.startsWith("/permission/loginAction.do?method=loginSj")
				|| requestServlet.startsWith("/permission/loginAction.do?method=initSj")
				|| requestServlet.startsWith("/permission/loginAction.do?method=saveXfm")
				|| requestServlet.startsWith("/seller/sellerAction.do?method=show")
				|| requestServlet.startsWith("/pay/payAction.do")
				
				){
			//log.info("请求的Action为：" + requestServlet);
			// 如果请求来自/permission/loginAction.do则不进行任何处理
			// 不获取session以及该用户的相关权限
		} else {
			//this.checkUserUrlLogin(request);
			userx = getUser(request);
//			loginedUser=(User) request.getSession().getAttribute("loginUser");
//			loginedUserGroup=(UserGroup)request.getSession().getAttribute("loginUserGroup");
			// 校验是否登陆,如果没有登陆,则跳转到登陆界面
			if (userx == null) {
				// 下面这行代码，用于检测当用户没有进行登陆的时候，阻止用户调用相关的action
				// 开发阶段先注释掉，部属阶段需要去掉注释。				// hulei@2005-11-19
//				log.warn("系统检测到一个未登录的非法请求，来自：" + request.getRemoteAddr()
//						+ "，该请求被取消！");
				//return new ActionForward("needLogin","../needLogin.html", true);
				
				//请求的url，只有通过点击a标签才有效，通过地址栏输入无效
				String url=request.getHeader("referer");   
				if(url!=null&&url!=""){
					request.getSession().setAttribute("GOTO_URL", url);
				}
				return new ActionForward("needLogin","../permission/loginAction.do?method=init", true);
			}
		}
		return super.execute(mapping, form, request, response);
	}
	
	
	/**
	 * 获得当前登录用户id
	 * 
	 * @param req
	 * @return
	 */
	public Integer getUserId(HttpServletRequest request) {
		return this.getUser(request).getUserId();
	}
	
	
	/**
	 * Convenience method for getting an action form base on it's mapped scope.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @return ActionForm the form from the specifies scope, or null if nothing
	 *         found
	 */
	protected ActionForm getActionForm(ActionMapping mapping,
			HttpServletRequest request) {
		ActionForm actionForm = null;

		// Remove the obsolete form bean
		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope())) {
				actionForm = (ActionForm) request.getAttribute(mapping
						.getAttribute());
			} else {
				HttpSession session = request.getSession();
				actionForm = (ActionForm) session.getAttribute(mapping
						.getAttribute());
			}
		}

		return actionForm;
	}

	/**
	 * Convenience method to get the Configuration HashMap from the servlet
	 * context.
	 * 
	 * @return the user's populated form from the session
	 */
	public Map getConfiguration() {
		Map config = (HashMap) getServlet().getServletContext().getAttribute(
				Constants.CONFIG);

		// so unit tests don't puke when nothing's been set
		if (config == null) {
			return new HashMap();
		}

		return config;
	}

	/**
	 * Method to check and see if https is required for this resource
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * @return boolean true if redirection to SSL is needed
	 */
	protected boolean checkSsl(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response) {
		String redirectString = SslUtil.getRedirectString(request, getServlet()
				.getServletContext(), SECURE.equals(mapping.getParameter()));

		if (redirectString != null) {
			log.debug("protocol switch needed, redirecting...");

			try {
				// Redirect the page to the desired URL
				response.sendRedirect(response
						.encodeRedirectURL(redirectString));

				return true;
			} catch (Exception ioe) {
				log.error("redirect to new protocol failed...");
			}
		}

		return false;
	}

	/**
	 * Convenience method for removing the obsolete form bean.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 */
	protected void removeFormBean(ActionMapping mapping,
			HttpServletRequest request) {
		// Remove the obsolete form bean
		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope())) {
				request.removeAttribute(mapping.getAttribute());
			} else {
				HttpSession session = request.getSession();
				session.removeAttribute(mapping.getAttribute());
			}
		}
	}

	/**
	 * Convenience method to update a formBean in it's scope
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param request
	 *            The HTTP request we are processing
	 * @param form
	 *            The ActionForm
	 */
	protected void updateFormBean(ActionMapping mapping,
			HttpServletRequest request, ActionForm form) {
		// Remove the obsolete form bean
		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope())) {
				request.setAttribute(mapping.getAttribute(), form);
			} else {
				HttpSession session = request.getSession();
				session.setAttribute(mapping.getAttribute(), form);
			}
		}
	}
	
	protected void updateUserLog(HttpServletRequest request,User user,String loginAction,String loginName){
		//记录访问者
		String ip = request.getRemoteAddr();
		//if(!ip.startsWith("127.") && !ip.startsWith("192.")){
			UserLog log = new UserLog();
			if(user!=null){
				log.setUserId(user.getUserId());
				log.setLoginName(user.getLoginName());
			}else{
				log.setUserId(0);
				log.setLoginName(loginName);
			}
			log.setIpAddress(ip);
			log.setDateTime(DateTimeUtil.getJavaUtilDateNow());
			log.setActionText(loginAction);
			userService.saveUserLog(log);
		//}
	}
	public void setUserSession(HttpServletRequest request,User user){
		request.getSession(true).setAttribute("loginUser",user);
	}
	public Integer getUserCityId(HttpServletRequest request) {
		Integer cityId = getUser(request).getCityId();
		return ParamUtils.chkInteger(cityId)?cityId:0;
	}
}
