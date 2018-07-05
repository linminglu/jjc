package com.apps.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.model.Message;
import com.apps.service.IMessageService;
import com.apps.service.ITypeService;
import com.apps.util.JsonUtil;
import com.apps.web.form.AgentForm;
import com.apps.web.form.MessageForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.service.user.IUserService;

/**
 * 消息Action
 * 
 */
public class MessageAction extends BaseDispatchAction {
	private final IMessageService messageService = (IMessageService) getService("messageService");
	private final IUserService userService = (IUserService) getService("userService");

	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		MessageForm messageForm = (MessageForm) form;
		String endDate = messageForm.getEndDate();
		String startDate = messageForm.getStartDate();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		User user = (User) request.getSession(false).getAttribute("loginUser");
		Integer userId= user.getUserId();
		if (!(Constants.USER_TYPE_ADMIN.equals(user.getUserType())
				|| Constants.USER_TYPE_SUPERADMIN.equals(user.getUserType()))) {
			hqls.append(" and ho.userId = ? ");
			para.add(userId);
		}

		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ho.createTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ho.createTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		hqls.append(" order by ho.id desc");
		PaginationSupport ps = messageService.findList(hqls.toString(), para,
				startIndex, pageSize);
		List<Message> list = ps.getItems();
		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}

	/**
	 * 创建
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
//
//		Integer userId = this.getUserId(request);
//		if (!ParamUtils.chkInteger(userId)) {// 判断是否是正确的用户id
//			request.setAttribute("msg", "不正确的用户ID！！");
//			return mapping.findForward("error");
//		}
//		User user = (User) userService.getObject(User.class, userId);
//		
//		if (Constants.USER_TYPE_ADMIN.equals(user.getUserType())
//				|| Constants.USER_TYPE_SUPERADMIN.equals(user.getUserType())) {
//			hqls.append(" and  (u.agentId = 0  or u.agentId is null)");
//		} else if(Constants.USER_TYPE_AGENT.equals(user.getUserType())) {
//			hqls.append(" and  u.agentId = ? ");
//			para.add(user.getUserId());
//		}
//		
//		List<User> list = userService.findUserList(hqls.toString(), para);
//		request.setAttribute("list", list);
		return mapping.findForward("create");
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		MessageForm messageForm = (MessageForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		if(ParamUtils.chkInteger(id)){
			Message message = (Message) messageService.getObject(
					Message.class, id);
			messageForm.setMessage(message);
		}
		request.setAttribute("messageForm", messageForm);
		return mapping.findForward("preModify");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		String loginName = message.getLoginName();
		Integer userId = null;
		if(loginName !=null && loginName.equals("所有人")){
			userId = 0;
		}else{
			User user = userService.findUserByLoginName(loginName);
			if(user!=null){
				userId = user.getUserId();
			}else{
				return mapping.findForward("create");
			}
		}

		message.setUserId(userId);
		message = messageService.saveMessage(message);
		return mapping.findForward("save");
	}

	/**
	 * 确认用户是否存在
	 */
	public void findUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String loginName = ParamUtils.getParameter(request, "loginName");
		String flag = "error";
		if(loginName !=null && loginName.equals("所有人")){
			flag = "success";
		}else{
			User user = userService.findUserByLoginName(loginName);
			if(user!=null){
				flag = "success";
			}
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
//	/**
//	 * 更改状态
//	 */
//	public void changeStatus(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//					throws Exception, NoFunctionPermissionException {
//		Integer id = ParamUtils.getIntegerParameter(request, "id");
//		Message message = (Message) messageService.getObject(
//				Message.class, id);
//		String status = message.getStatus();
//		String flag = "success";
//		try {
//			if (status.equals(Constants.PUB_STATUS_OPEN)) {
//				message.setStatus(Constants.PUB_STATUS_CLOSE);
//			} else {
//				message.setStatus(Constants.PUB_STATUS_OPEN);
//			}
//			messageService.saveObject(message, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag = "error";
//		}
//		JsonUtil.AjaxWriter(response, flag);
//	}

//	/**
//	 * 排序
//	 */
//	public ActionForward sotr(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception, NoFunctionPermissionException {
//		MessageForm messageForm = (MessageForm) form;
//		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
//		String adType = ParamUtils.getParameter(request, "adType");
//		messageForm.setAdType(adType);
//		Integer id = ParamUtils.getIntegerParameter(request, "id");
//		messageService.updateSort(id, flag);
//		return init(mapping, form, request, response);
//	}

}
