package com.ram.web.userLog.action;

import java.io.FileWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ram.RamConstants;
import com.ram.exception.course.business.NotExistElCourseException;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserLog;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.ram.web.userLog.form.UserLogForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

public class UserLogAction extends BaseDispatchAction {

	private Date beginDate = new Date(System.currentTimeMillis());

	private Date endDate = new Date(System.currentTimeMillis());

	private String loginName;

	private String actionText;

	private UserLogForm lForm = new UserLogForm();

	private int startIndex;

	private int pageSize;

	private List userLogList = new ArrayList();
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService tcService = (ISystemTutorCenterService) getService("systemTutorCenterService");

	/**
	 * 这个方法是初始化所有用户日志查询的方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws NotExistElCourseException
	 */
	public final ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		//获取当前用户
  		User user = null;
  		user = ( User )req.getSession( false ).getAttribute( "loginUser" );
  		
		UserLogForm userLogForm=new UserLogForm();
		int tcId = userService.getTcId_Of_User(user);
		userLogForm.setTcId(new Integer(tcId));
		req.setAttribute("userLogQueryInitForm", userLogForm);
		tcInit(req, res);
		return mapping.findForward("init");
	}

	/**
	 * 这个方法是初始化我的日志查询的方法
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws NotExistElCourseException
	 */
	public final ActionForward mInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, NotExistElCourseException {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		UserLogForm userLogForm = new UserLogForm();
		userLogForm.setStartDateTime(new Date(System.currentTimeMillis()));
		userLogForm.setEndDateTime(new Date(System.currentTimeMillis()));
		request.setAttribute("userLogQueryForm",userLogForm);
		request.setAttribute("loginName", user.getLoginName());
		return mapping.findForward("init");
	}

	/**
	 * 这个方法是初始化的方法
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws NotExistElCourseException
	 */
	public final ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		UserLogForm userLogForm = (UserLogForm) form;

		this.loginName = userLogForm.getUserLog().getLoginName();
		log.info("loginName=" + loginName);
		
//		this.beginDate = userLogForm.getStartDateTime();
//		
//		this.endDate = userLogForm.getEndDateTime();
//		if (endDate != null) {
//			this.endDate = new java.sql.Date(endDate.getTime() + RamConstants.mmOfADay);// DateTimeUtil.StringToDate(DateUtil.getNextSomeDay(endDate,1));
//		}
		Date eginDate = userLogForm.getStartDateTime();
		if( eginDate != null ){
			this.beginDate = eginDate;
		}
		Date enDate = userLogForm.getEndDateTime();
		if (enDate != null) {
			this.endDate = new java.sql.Date( enDate.getTime() + RamConstants.mmOfADay );
		}
//		this.actionText = userLogForm.getActionText();
		this.actionText = userLogForm.getUserLog().getActionText();
		this.startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		this.pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		
		DetachedCriteria query = DetachedCriteria.forClass(UserLog.class,"userLog");
		if (loginName != null) {
			query.add(Restrictions.ilike("userLog.loginName", "%" + loginName + "%"));
		}
		if (actionText != null) {
			query.add(Restrictions.ilike("userLog.actionText", "%" + actionText + "%"));
		}
		
		//分页处理
		if (this.beginDate != null && this.endDate != null) {
//			getUserLogInfo(request, startIndex, pageSize, null, loginName,this.beginDate, this.endDate);
			query.add(Restrictions.le("userLog.dateTime", endDate));
			query.add(Restrictions.ge("userLog.dateTime", beginDate));
			getUserLogInfo(request, startIndex, pageSize, query);
		} else {
			Date beginDate = lForm.getStartDateTime();
			log.info("lform.beginDate=" + beginDate.toString());
			Date endDate = new Date(lForm.getEndDateTime().getTime() + RamConstants.mmOfADay);
			log.info("lform.endDate=" + endDate.toString());
//			getUserLogInfo(request, startIndex, pageSize, null, lForm.getUserLog().getLoginName(), beginDate, endDate);
			query.add(Restrictions.le("userLog.dateTime", endDate));
			query.add(Restrictions.ge("userLog.dateTime", beginDate));
			getUserLogInfo(request, startIndex, pageSize, query);
		}

		return mapping.findForward("query");
	}

	/**
	 * 这个方法是查询所有用户日志的方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws NotExistElCourseException
	 */
	public final ActionForward mQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		UserLogForm userLogForm = (UserLogForm) form;
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		this.loginName = user.getLoginName();
		Date eginDate = userLogForm.getStartDateTime();
		if( eginDate != null ){
			this.beginDate = eginDate;
		}
		Date enDate = userLogForm.getEndDateTime();
		if (enDate != null) {
			this.endDate = new java.sql.Date( enDate.getTime() + RamConstants.mmOfADay );
		}
		
//		this.actionText = userLogForm.getActionText();
		this.actionText = userLogForm.getUserLog().getActionText();
		this.startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		this.pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);

		
		DetachedCriteria query = DetachedCriteria.forClass(UserLog.class,"userLog");
		if (loginName != null) {
			query.add(Restrictions.ilike("userLog.loginName",loginName ));
		}
		if (actionText != null) {
			query.add(Restrictions.ilike("userLog.actionText", "%" + actionText + "%"));
		}
		// 分页处理
		if (beginDate != null) {
//			getUserLogInfo(request, startIndex, pageSize, null, user.getLoginName(), this.beginDate, this.endDate);
			query.add(Restrictions.le("userLog.dateTime", endDate));
			query.add(Restrictions.ge("userLog.dateTime", beginDate));
			getUserLogInfo(request, startIndex, pageSize, query);
		} else {
			Date bbeginDate = lForm.getStartDateTime();
			Date eendDate = lForm.getEndDateTime();
			eendDate = new Date(eendDate.getTime() + RamConstants.mmOfADay);
//			getUserLogInfo(request, startIndex, pageSize, null, user.getLoginName(), bbeginDate, eendDate);
			
			query.add(Restrictions.le("userLog.dateTime", eendDate));
			query.add(Restrictions.ge("userLog.dateTime", bbeginDate));
			getUserLogInfo(request, startIndex, pageSize, query);
		}
		return mapping.findForward("query");
	}

	/**
	 * 测试方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public final ActionForward test(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		UserLogForm userLogForm = (UserLogForm) form;
		this.startIndex = ParamUtils
				.getIntParameter(request, "pager.offset", 0);
		this.pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		DetachedCriteria query = DetachedCriteria.forClass(UserLog.class,
				"userLog");
		if (userLogForm.getUserLog().getLoginName() != null) {
			query.add(Restrictions.ilike("userLog.loginName", "%"
					+ userLogForm.getUserLog().getLoginName() + "%"));
		}
		if (userLogForm.getUserLog().getActionText() != null) {
			query.add(Restrictions.ilike("userLog.actionText", "%"
					+ userLogForm.getUserLog().getActionText() + "%"));
		}
		Date endDate=new Date(userLogForm.getEndDateTime().getTime() + RamConstants.mmOfADay);
		query.add(Restrictions.le("userLog.dateTime", endDate));

		query.add(Restrictions.ge("userLog.dateTime", userLogForm
				.getStartDateTime()));
		getUserLogInfo(request, startIndex, pageSize, query);
		return mapping.findForward("query");

	}

	/**
	 * 从数据库中提取投诉信息数据
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param query
	 *            DetachedCriteria
	 * @return boolean 查询结果不空 true 查询结果为空 false
	 */

	private void getUserLogInfo(HttpServletRequest request, int startIndex,
			int pageSize, DetachedCriteria query, String loginName,
			Date beginDate, Date endDate) {
		PaginationSupport paginationSupport;
		if (query == null) {
			paginationSupport = userLogService.findALLUserLogPage(startIndex,
					pageSize, loginName, beginDate, endDate);
		} else {
			paginationSupport = userLogService.findUserLogsByCriteria(query,
					pageSize, startIndex);

		}
		if (paginationSupport == null) {
			request.setAttribute("userLogList", new ArrayList());
			request.setAttribute("userLogTotalCount", new Integer(0));
		} else {
			this.userLogList = paginationSupport.getItems();
			int userLogTotalCount = paginationSupport.getTotalCount();
			request.setAttribute("userLogList", userLogList);

			request.setAttribute("userLogTotalCount", new Integer(
					userLogTotalCount));
			lForm.getUserLog().setLoginName(loginName);
			lForm.setStartDateTime((java.sql.Date) beginDate);
			lForm.setEndDateTime((java.sql.Date) endDate);
		}
	}

	/**
	 * 得到
	 * 
	 * @param request
	 * @param response
	 * @param logInfo
	 */
	private void getUserLogInfo(HttpServletRequest request, int startIndex,
			int pageSize, DetachedCriteria query) {
		PaginationSupport paginationSupport;
		if (query == null) {
			log.info("==================aaa=");
			paginationSupport = userLogService.findALLUserLogPage(startIndex,
					pageSize, loginName, beginDate, endDate);
		} else {
			log.info("==================aaabbbb=");
			paginationSupport = userLogService.findUserLogsByCriteria(query,
					pageSize, startIndex);

		}
		if (paginationSupport == null) {
			request.setAttribute("userLogList", new ArrayList());
			request.setAttribute("userLogTotalCount", new Integer(0));
		} else {
			this.userLogList = paginationSupport.getItems();
			int userLogTotalCount = paginationSupport.getTotalCount();
			request.setAttribute("userLogList", userLogList);

			request.setAttribute("userLogTotalCount", new Integer(
					userLogTotalCount));
			lForm.getUserLog().setLoginName(loginName);
			lForm.setStartDateTime(beginDate);
			lForm.setEndDateTime(endDate);
		}
	}

	/**
	 * 这个方法是将日志记录写入文件的方法
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws NotExistElCourseException
	 */
	public final ActionForward write(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		log.info("this.loginName=" + this.loginName);
		List list = this.userLogList;
		log.info("list.size=" + list.size());
		String filePath = "c:\\aaa.doc";
		UserLog userLog = null;
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				userLog = (UserLog) list.get(i);
				sb.append(userLog.getLoginName()).append(",").append(
						userLog.getDateTime().toString()).append(",").append(
						userLog.getActionText()).append("\n");
			}
			log.info(sb.toString());
			FileWriter fw = new FileWriter(filePath);
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mapping.findForward("write");
	}
	

	private void tcInit(HttpServletRequest req, HttpServletResponse res){
		//获取当前用户
  		User user = null;
  		user = ( User )req.getSession( false ).getAttribute( "loginUser" );
  		
		if(user.getUserType().equals(RamConstants.UserTypeIsManager2)){
		    
			int tcId=userService.getTcId_Of_User(user);
		    List tcs = tcService.getTutorCenterTree(tcId);
			
			Collection  tc = new ArrayList();
			if(tcId==1)	tc.add(new LabelValueBean("全部", null));
			Iterator it = tcs.iterator();
			while(it.hasNext()){
				tc.add(it.next());
			}
		
			req.setAttribute("tcList", tc);
		}
	}
}
