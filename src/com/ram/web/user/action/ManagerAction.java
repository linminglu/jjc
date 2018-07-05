package com.ram.web.user.action;

import help.base.APIConstants;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.UserCacheUtil;
import com.apps.model.CityBusArea;
import com.apps.model.CityCommunity;
import com.apps.model.UserTradeDetail;
import com.apps.model.UserTradeDetailRl;
import com.apps.service.IBaseDataService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetDetail;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaSessionInfo;
import com.game.model.UserLevel;
import com.game.model.dto.GaDTO;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.model.UserLimit;
import com.ram.model.UserLog;
import com.ram.model.dto.UserDTO;
import com.ram.model.dto.UserMoneyDTO;
import com.ram.service.user.IUserService;
import com.ram.util.MD5;
import com.ram.web.user.form.ManagerForm;

public class ManagerAction extends BaseDispatchAction {

	private final IUserService userService = (IUserService) getService("userService");
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final IGaService gaService = (IGaService) getService("gaService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		String userType = ParamUtils.getParameter(request, "userType");
		String category=ParamUtils.getParameter(request, "category");//一级代理或者二级代理   二级代理传的是2
		String uid = ParamUtils.getParameter(request, "uid");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		Integer agentId=ParamUtils.getIntegerParameter(request, "agentId");
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName();
		// String userType = frm.getUserType();
		// Integer orgId = frm.getOrgId();
		//String title = frm.getTitle();
		/*if(ParamUtils.chkString(title)){
			hsql.append(" and ");
			para.add(title);
		}*/

		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(userType)) {
			if (userType.equals("2")) {// 对2进行判断，因为2代表管理员，2分为
										// usertypr=2系统管理员，usertype=5维护管理员
				hsql.append(" and  (u.userType = '2' or u.userType = '5') ");
			}else if(userType.equals("1")){
				hsql.append(" and  u.userType = '1' ");
			}else if(userType.equals("11")) {
				hsql.append(" and   u.userType = '11' ");//测试用户
			}else if(userType.equals("12")) {
				hsql.append(" and   u.userType = '12' ");//代理用户
			}else{
//				hsql.append(" and   (u.userType = '5' or u.userType = '6' or u.userType = '7')");
			}
			if(ParamUtils.chkInteger(agentId)){
				User agentUser=(User) userService.getObject(User.class, agentId);
//				if(ParamUtils.chkInteger(agentUser.getAgentId())){
					hsql.append(" and  u.agentId = ? ");
					para.add(agentId);
//				}else{
//					hsql.append(" and  u.agentId1 = ? ");
//					para.add(agentId);
//				}
			}else{
				String type = user.getUserType();
				if (Constants.USER_TYPE_ADMIN.equals(type)
						|| Constants.USER_TYPE_SUPERADMIN.equals(type)) {
//					hsql.append(" and  (u.agentId = 0  or u.agentId is null)");
					if(ParamUtils.chkString(category)&&"1".equals(category)){
						hsql.append(" and  (u.agentId = 0  or u.agentId is null)");
					}else if(ParamUtils.chkString(category)&&"2".equals(category)){
						hsql.append(" and  u.agentId>0 ");
					}
				} else if(Constants.USER_TYPE_AGENT.equals(type)) {
					if(userType.equals("12")){
						hsql.append(" and  u.agentId = ? ");
						para.add(user.getUserId());
					}else{
						if(ParamUtils.chkInteger(user.getAgentId())){
							hsql.append(" and  u.agentId = ? ");
							para.add(user.getUserId());
						}else{
							hsql.append(" and  u.agentId1 = ? ");
							para.add(user.getUserId());
						}
					}		
				}
			}
		}
		
		
		if (ParamUtils.chkString(uid)) {
			hsql.append(" and u.userId = ? ");
			para.add(uid);
		}
		
		hsql.append(" order by u.registDateTime desc");

		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		User user2 = getUser(request);
		request.setAttribute("adminUserType", user2.getUserType());
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("userType", userType);
		request.setAttribute("category", category);
		request.setAttribute("agentId", agentId+"");
		request.setAttribute("user", user);
		return mapping.findForward("init");
	}
	
	/**
	 * 用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		String ip = frm.getIpAddress();//ip地址
		String userType = frm.getUserType();//用户类型
		String status = frm.getStatus();//状态
		String accountNo = frm.getAccountNo();//银行卡号
		
		if(ParamUtils.chkString(ip)){
			hsql.append(" and u.lastLoginIp = ? ");
			para.add(ip);
		}
		if(ParamUtils.chkString(accountNo)){
			hsql.append(" and u.accountNo is not null  and u.accountNo = ? ");
			para.add(accountNo);
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and u.status = ? ");
			para.add(status);
		}
		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		
		if(ParamUtils.chkString(userType)){
			hsql.append(" and u.userType = ? ");
			para.add(userType);
		} else {
			hsql.append(" and (u.userType=? or u.userType=? or u.userType=? or u.userType=?)");
			para.add(Constants.USER_TYPE_SUER);
			para.add(Constants.USER_TYPE_ADMIN);
			para.add(Constants.USER_TYPE_CUS_SERVICE);
			para.add(Constants.USER_TYPE_FINANCE);
		}
		
		hsql.append(" order by u.registDateTime desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		
		request.setAttribute("loginName", loginName);
		request.setAttribute("ip", ip);
		request.setAttribute("userType", userType);
		request.setAttribute("status", status);
		request.setAttribute("accountNo", accountNo);
		
		request.setAttribute("managerList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("userList");
	}
	
	/**
	 * 用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userListByBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		String ip = frm.getIpAddress();//ip地址
		String userType = frm.getUserType();//用户类型
		String status = frm.getStatus();//状态
		String accountNo = frm.getAccountNo();//银行卡号
		
		if(ParamUtils.chkString(ip)){
			hsql.append(" and u.lastLoginIp = ? ");
			para.add(ip);
		}
		if(ParamUtils.chkString(accountNo)){
			hsql.append(" and u.accountNo is not null  and u.accountNo = ? ");
			para.add(accountNo);
		}
		if(ParamUtils.chkString(status)){
			hsql.append(" and u.status = ? ");
			para.add(status);
		}
		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		
		if(ParamUtils.chkString(userType)){
			hsql.append(" and u.userType = ? ");
			para.add(userType);
		} else {
			hsql.append(" and (u.userType=? or u.userType=? or u.userType=? or u.userType=?)");
			para.add(Constants.USER_TYPE_SUER);
			para.add(Constants.USER_TYPE_ADMIN);
			para.add(Constants.USER_TYPE_CUS_SERVICE);
			para.add(Constants.USER_TYPE_FINANCE);
		}
		
		hsql.append(" order by u.money desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		
		request.setAttribute("loginName", loginName);
		request.setAttribute("ip", ip);
		request.setAttribute("userType", userType);
		request.setAttribute("status", status);
		request.setAttribute("accountNo", accountNo);
		
		request.setAttribute("managerList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("userListByBalance");
	}
	
	/**
	 * 更改状态
	 */
//	public void changeStatus(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception, NoFunctionPermissionException {
//		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
//		User user = (User) gaService.getObject(
//				User.class, userId);
//		String status = user.getStatus();
//		String flag = "success";
//		try {
//			if (status.equals(Constants.PUB_STATUS_OPEN)) {
//				user.setStatus(Constants.PUB_STATUS_CLOSE);
//			} else {
//				user.setStatus(Constants.PUB_STATUS_OPEN);
//			}
//			gaService.saveObject(user, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//			flag = "error";
//		}
//		JsonUtil.AjaxWriter(response, flag);
//	}
	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				||Constants.USER_TYPE_CUS_SERVICE.equals(loginUserType)){
			Integer userId = ParamUtils.getIntegerParameter(request, "userId");
			User user = (User) gaService.getObject(User.class, userId);
			String status = user.getStatus();
			String  startStart="";
			String  endStart="";
			try {
				String loginName=user.getLoginName();
				String password=user.getPassword();
				
				if (status.equals(Constants.PUB_STATUS_OPEN)) {
					user.setStatus(Constants.PUB_STATUS_CLOSE);
					startStart="开";
					endStart="关";
//					String u = Constants.DES_KEY_UID + "=" + userId + "&"
//					+ Constants.DES_KEY_LOGINNAME + "=" + loginName + "&"
//					+ Constants.DES_KEY_PASSWORD + "=" + password+ "&"
//					+ Constants.DES_KEY_STATUS + "=" + status+"&"
//					+ Constants.DES_KEY_SESSION + "=" + user.getSession();
//					BlackListCacheUtil.saveBlacklistU(DesUtils.encrypt(u));
				} else {
					user.setStatus(Constants.PUB_STATUS_OPEN);
					startStart="关";
					endStart="开";
//					String u = Constants.DES_KEY_UID + "=" + userId + "&"
//						+ Constants.DES_KEY_LOGINNAME + "=" + loginName + "&"
//						+ Constants.DES_KEY_PASSWORD + "=" + password+ "&"
//						+ Constants.DES_KEY_STATUS + "=1&"
//						+ Constants.DES_KEY_SESSION + "=" + user.getSession();
//					BlackListCacheUtil.delBlacklistU(DesUtils.encrypt(u));
				}
				gaService.saveObject(user, null);
				UserCacheUtil.saveUser(user);
				StringBuffer loginText = new StringBuffer();
				loginText.append("[切换用户状态]：操作人登录名[");
				loginText.append(loginUser.getLoginName());
				loginText.append("]，操作人ID[");
				loginText.append(loginUser.getUserId());
				loginText.append("]，被操作用户ID[");
				loginText.append(userId);
				loginText.append("]，状态从[");
				loginText.append(startStart);
				loginText.append("]，变成[");
				loginText.append(endStart);
				loginText.append("]");
				userService.updateUserLog(request,loginUser,loginText.toString());			
				
				code=APIConstants.CODE_REQUEST_SUCCESS;
				message="切换状态成功！";
			} catch (Exception e) {
				e.printStackTrace();
				message="切换状态失败！";
			}
		}else{
			message="无权限！请联系管理员！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}

	/**
	 * 代理的下级会员列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward subUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();

		Integer agentId = ParamUtils.getIntegerParameter(request, "agentId");

		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName();
		
		if(!ParamUtils.chkInteger(agentId)){
			agentId = frm.getAgentId();
		}
		if(ParamUtils.chkInteger(agentId)){
			hsql.append(" and u.agentId = ? ");
			para.add(agentId);
		}
		if(ParamUtils.chkString(loginName)){
			hsql.append(" and (u.loginName = ? or u.userName = ? or u.userId = ?) ");
			para.add(loginName);
			para.add(loginName);
			para.add(loginName);
		}
		hsql.append(" order by u.registDateTime desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		@SuppressWarnings("unchecked")
		List<User> items = ps.getItems();
		request.setAttribute("agentId", String.valueOf(agentId));
		request.setAttribute("managerList", items);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("subUserList");
	}

	/**
	 * 下级代理
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward agentList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		hsql.append(" and (u.userType = ? or u.userType = ? or u.userType = ? ) ");
		para.add(Constants.USER_TYPE_AGENT_ONE);
		para.add(Constants.USER_TYPE_AGENT_TWO);
		para.add(Constants.USER_TYPE_AGENT_THREE);
		hsql.append(" order by u.userId desc");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("agentList");
	}

	/**
	 * 批量删除用户
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// //获取当前用户
		// // User user = null;
		// user = ( User )request.getSession().getAttribute( "loginUser" );
		//
		// ManagerForm managerForm = ( ManagerForm )form;
		// String[] indexs = managerForm.getSelectIndex();
		// if( this.isTokenValid( request,true )){
		// int[] delIndexs = null;
		// if( indexs != null && indexs.length > 0 ){
		// delIndexs = new int[indexs.length];
		// for(int i = 0; i < indexs.length; i++){
		// delIndexs[i] = Integer.parseInt(indexs[i]);
		// }
		// // userServicedeleteUser(delIndexs, user);
		// }
		// }
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
//		List<BuyOrder> buyOrdersList = userService.findBuyOrders(userId);
//		List<EatOrder> eatOrdersList = userService.findEatOrders(userId);
//		List<StoreOrder> storeOrdersList = userService.findStoreOrders(userId);
//		if((buyOrdersList != null && buyOrdersList.size() > 0)||(eatOrdersList != null && eatOrdersList.size()>0) 
//				|| (storeOrdersList != null && storeOrdersList.size() > 0 )){
//			return mapping.findForward("errorDeleteUser");
//		}else{
//			if (ParamUtils.chkIntegerPlus(userId))
//				userService.delUserAbsolute(userId, this.getUser(request));
//			
			return mapping.findForward("delete");
//		}
	}

	public ActionForward log(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();

		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName();
		String ipAddress = frm.getIpAddress();
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();

		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and upper(ul.loginName) like ?");
			para.add("%" + loginName.trim().toUpperCase() + "%");
		}
		if (ParamUtils.chkString(ipAddress)) {
			hsql.append(" and ul.ipAddress like ?");
			para.add("%" + ipAddress.trim() + "%");
		}
		if (ParamUtils.chkString(startDate)) {
			hsql.append(" and ul.dateTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hsql.append(" and ul.dateTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		PaginationSupport ps = userService.findUserLog(hsql.toString(), para,
				startIndex, pageSize);

		request.setAttribute("list", ps.getItems());
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("log");
	}

	/**
	 * 使用户状态变成无效
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward pauseUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		User user = null;
		user = (User) request.getSession(false).getAttribute("loginUser");
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		if (userId != null) {
			userService.modifyUserStatus(userId.intValue(),
					Constants.PUB_STATUS_CLOSE, user);
			StringBuffer loginText = new StringBuffer();
			loginText.append("修改用户状态：管理员");
			loginText.append(user.getLoginName());
			loginText.append("[");
			loginText.append(user.getUserId());
			loginText.append("]，修改用户[");
			loginText.append(userService.getUser(userId).getLoginName());
			loginText.append("]状态为无效");
			userService.updateUserLog(request,user,loginText.toString());
		}
		String userType = ParamUtils.getParameter(request, "userType");
		request.setAttribute("userType", userType);
		// return mapping.findForward("pauseUser");

		ActionForward forward = mapping.findForward("pauseUser");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&userType=" + userType);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 使用户状态变成有效
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward releaseUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		User user = null;
		user = (User) request.getSession(false).getAttribute("loginUser");

		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		if (userId != null) {
			userService.modifyUserStatus(userId.intValue(),
					Constants.PUB_STATUS_OPEN, user);
			StringBuffer loginText = new StringBuffer();
			loginText.append("修改用户状态：管理员");
			loginText.append(user.getLoginName());
			loginText.append("[");
			loginText.append(user.getUserId());
			loginText.append("]，修改用户[");
			loginText.append(userService.getUser(userId).getLoginName());
			loginText.append("]状态为有效");
			userService.updateUserLog(request,user,loginText.toString());

		}
		// return mapping.findForward("releaseUser");
		String userType = ParamUtils.getParameter(request, "userType");
		request.setAttribute("userType", userType);
		ActionForward forward = mapping.findForward("releaseUser");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&userType=" + userType);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 转到修改用户页面
	 */
	public ActionForward preModifyUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");

		ManagerForm managerForm = (ManagerForm) form;
		User user = userService.getUser(userId);
		managerForm.setUser(user);

		HQUtils hq = new HQUtils();
		hq.addHsql("from RechargeWay rw where rw.type=? ");
		hq.addPars(Constants.RECHARGE_WAY_1);
		List<Object> rwList = userService.findObjects(hq);
		request.setAttribute("rwList", rwList);// 充值通道
		return mapping.findForward("preModifyUser");
	}
	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		 User curUser = ParamUtils.getCurrentUser(request);
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		ManagerForm managerForm = (ManagerForm) form;
		User user = userService.getUser(userId);
		managerForm.setUser(user);
		managerForm.setPassword(user.getPassword());
		if (user.getBirthday() != null) {
			managerForm.setBirthday(DateTimeUtil.DateToString(user
					.getBirthday()));
		}
		String userType = ParamUtils.getParameter(request, "userType");
		request.setAttribute("userType", userType);
		// managerForm.setType(userType);
		// request.setAttribute("userId", userId);
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		String  type=loginUser.getUserType();
		request.setAttribute("uType", type);//登录的人类型
		
		HQUtils hq = new HQUtils();
		hq.addHsql("from RechargeWay rw where rw.type=? ");
		hq.addPars(Constants.RECHARGE_WAY_1);
		List<Object> rwList = userService.findObjects(hq);
		request.setAttribute("rwList", rwList);// 充值通道
		return mapping.findForward("preModify");
	}

	/**
	 * 修改Manager,并且返回Manager列表页面
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		ManagerForm frm = (ManagerForm) form;
//		String userType = frm.getUserType();
		User userFrm = frm.getUser();
		String userType=userFrm.getUserType();
		String newPassword = frm.getPassword();//新密码
		Integer agentId = userFrm.getAgentId();
		
		if(ParamUtils.chkString(newPassword)){
			newPassword = MD5.exc(newPassword);
			if(!newPassword.equals(userFrm.getPassword())){
				userFrm.setPassword(newPassword);
			}
		}
		if(loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)||Constants.USER_TYPE_SUPERADMIN.equals(loginUser.getUserType())){
			userFrm.setAgentId(0);
		}else if(loginUser.getUserType().equals(Constants.USER_TYPE_AGENT)){
			userFrm.setAgentId(loginUser.getUserId());
			userFrm.setAgentName(loginUser.getUserName());
		}
		if(ParamUtils.chkInteger(agentId)){
			User agent=userService.getAgentById(agentId);
			if(agent==null){
				request.setAttribute("msg", "代理不存在");
				return mapping.findForward("error");
			}else{
				userFrm.setAgentId(agent.getUserId());
				userFrm.setAgentName(agent.getLoginName());
			}
		}
		String cashPassword = userFrm.getCashPassword();
		
		if(ParamUtils.chkString(cashPassword)){
			userFrm.setCashPassword(MD5.exc(cashPassword).toUpperCase());
		}
		userFrm.setUserType(userType);
		String birthday = frm.getBirthday();
		if (userType.equals(Constants.USER_TYPE_AGENT_ONE)
				|| userType.equals(Constants.USER_TYPE_AGENT_TWO)
				|| userType.equals(Constants.USER_TYPE_AGENT_TWO)) {// 如果是代理则返回代理列表
			userService.saveModifyUserInfo(userFrm);
			return mapping.findForward("statusAgentList");
		} else {
			if (ParamUtils.chkString(birthday)) {
				userFrm.setBirthday(DateTimeUtil.StringToDate(birthday));
			}
			userService.saveModifyUserInfo(userFrm);
			ActionForward forward = mapping.findForward("modify");
			StringBuffer path = new StringBuffer();
			path.append(forward.getPath());
			return new ActionForward(forward.getName(), path.toString(), true);
		}

	}

	/**
	 * 获取用户的详细信息，包括任务和充值
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userInfoShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		// User user = (User)
		// request.getSession(false).getAttribute("loginUser");
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		ManagerForm managerForm = (ManagerForm) form;
		User userinfo = userService.getUser(userId);
		managerForm.setUser(userinfo);
		return mapping.findForward("preModify");
	}

	/**
	 * 初始化密码
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward initializePassword(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		User user = null;
		user = (User) request.getSession().getAttribute("loginUser");

		User userpwd = userService.getUser(userId);
		// userpwd.setPassword("888888");
		userpwd.setPassword(MD5.exc("888888"));

		userService.saveUser(userpwd, user);

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			out.print(userpwd.getPassword());
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 显示用户创建页面
	 */
	public ActionForward preAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = new ManagerForm();
		String userType = ParamUtils.getParameter(request, "userType");
		managerForm.setType(userType);
		request.setAttribute("managerForm", managerForm);
		request.setAttribute("userType", userType);

		User user = (User) request.getSession(false).getAttribute("loginUser");
		String type = user.getUserType();
		request.setAttribute("uType", type);
		return mapping.findForward("preAdd");
	}

	/**
	 * 创建用户，并显示创建用户的选择以便创建老师和管理者
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm frm = (ManagerForm) form;
		User userFrm = frm.getUser();
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		if (userService.checkUserExist(userFrm.getLoginName())) {
			request.setAttribute("msg", "用户名已存在");
			return mapping.findForward("error");
		}
		String userType = userFrm.getUserType();
		
		if(loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN)||Constants.USER_TYPE_SUPERADMIN.equals(loginUser.getUserType())){
			HQUtils hq=new HQUtils(" from User u where u.userType='2' and u.loginName!='admin' ");
			Integer count=userService.countObjects(hq);
			if(count!=null&&count>=5){
				request.setAttribute("msg", "新建的管理员不能超过5个");
				return mapping.findForward("error");
			}
			userFrm.setAgentId(0);
		}else if(loginUser.getUserType().equals(Constants.USER_TYPE_AGENT)){
			userFrm.setAgentId(loginUser.getUserId());
			userFrm.setAgentName(loginUser.getLoginName());
		}
		userFrm.setUserType(userType);
		String birthday = frm.getBirthday();
		if (userType.equals(Constants.USER_TYPE_AGENT_ONE)
				|| userType.equals(Constants.USER_TYPE_AGENT_TWO)
				|| userType.equals(Constants.USER_TYPE_AGENT_THREE)) {// 如果是代理则跳代理列表
			userService.saveUserInfo(userFrm);
			return mapping.findForward("statusAgentList");
		} else {
			if (ParamUtils.chkString(birthday)) {
				userFrm.setBirthday(DateTimeUtil.StringToDate(birthday));
			}
			userService.saveUserInfo(userFrm);
			ActionForward forward = mapping.findForward("add");
			StringBuffer path = new StringBuffer();
			path.append(forward.getPath());
			return new ActionForward(forward.getName(), path.toString(), true);
		}
	}
	public ActionForward findCityBusAreaList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<CityBusArea> cityList = baseDataService.findCityBusArea(cid);
		JSONArray list=new JSONArray();
		if(cityList!=null&&cityList.size()>0){
			JSONArray jsonArray = JSONArray.fromObject(cityList);
			String jsonData = jsonArray.toString();
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(jsonData.toString());
		}
		return null;
	}
	
	public ActionForward findCityCommunityList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<CityCommunity> cityList = baseDataService.findCityCommunityByAId(cid);
		JSONArray list=new JSONArray();
		if(cityList!=null&&cityList.size()>0){
			JSONArray jsonArray = JSONArray.fromObject(cityList);
			String jsonData = jsonArray.toString();
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(jsonData.toString());
		}
		return null;
	}
	/**
	 * 用户详情
	 */
	public ActionForward userView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		String uType = ParamUtils.getParameter(request, "userType");
		User user = (User)userService.getObject(User.class, userId);
		HQUtils hq = new HQUtils();
		hq.addHsql("from UserBankBind ub where ub.userId= ? ");
		hq.addPars(userId);
		List<Object> bankList = userService.findObjects(hq);
		String userType = user.getUserType();
		String userTypeZh = Constants.getUserTypeZh(userType);
//		User user2 = getUser(request);
//		String userType2 = user2.getUserType();
//		if(Constants.USER_TYPE_ADMIN.equals(userType2)||Constants.USER_TYPE_USER_CCID.equals(userType2)){
			request.setAttribute("userTypeZh", userTypeZh);

		request.setAttribute("userType", uType);
		request.setAttribute("user", user);
		request.setAttribute("bankList", bankList==null?new ArrayList<Object>():bankList);
		return mapping.findForward("userView");
	}
	
	/**
	 * 跳转到修改登录用户信息页面
	 */
	public ActionForward loginUserModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = getUserId(request);
		User user = null;
		if (ParamUtils.chkInteger(userId)) {
			user = userService.getUser(userId);
		}
		ManagerForm frm = (ManagerForm) form;
		frm.setUser(user);
		request.getSession(true).setAttribute("loginUser", user);
		return mapping.findForward("loginUserModify");
	}
	/**
	 * 保存登录用户信息
	 */
	public ActionForward loginUserSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		User user = managerForm.getUser();
		Integer userId = user.getUserId();
		String password = user.getPassword();
		String userName = user.getUserName();
		String gender = user.getGender();
		String cellPhone = user.getCellPhone();
		String address = user.getAddress();
		if(ParamUtils.chkInteger(userId)){
			User user2 = (User)userService.getObject(User.class, userId);
			if(ParamUtils.chkString(password) && !user2.getPassword().equals(password)){
				user2.setPassword(MD5.exc(password));
			}
			user2.setUserName(userName);
			user2.setGender(gender);
			user2.setCellPhone(cellPhone);
			user2.setAddress(address);
			userService.saveUser(user2);
		}
		request.setAttribute("msg", "保存成功");
		return mapping.findForward("success");
	}
	/**
	 * 删除用户
	 */
	public void delUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer uid = ParamUtils.getIntegerParameter(request, "uid");
		String userType = ParamUtils.getParameter(request, "userType");
		JSONObject jo = new JSONObject();
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(ParamUtils.chkInteger(uid)){
			code = userService.delUser(uid,userType);
		}
		jo.put("code", code);
		JsonUtil.AjaxWriter(response, jo);
	}
	/**
	 * 修改金额
	 */
	public ActionForward preModifyBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// User curUser = ParamUtils.getCurrentUser(request);
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		String category = ParamUtils.getParameter(request, "category");
		ManagerForm managerForm = (ManagerForm) form;
		User user = userService.getUser(userId);
		if(user.getUserBalance() == null){
			user.setUserBalance(new BigDecimal(0));
		}
		if(ParamUtils.chkString(category)){
			request.setAttribute("category", category);
		}
		managerForm.setUser(user);
		
		String userType = ParamUtils.getParameter(request, "userType");
		request.setAttribute("userType", userType);
	
		return mapping.findForward("preModifyBalance");
	}
	/**
	 * 修改并保存金额
	 */
	public ActionForward modifyBalance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		User user = managerForm.getUser();
		if(user!=null){
			String userType = user.getUserType();
			String type = managerForm.getType();//加款类型
			Integer userId = user.getUserId();
			User user2 = getUser(request);
			String userType2 = user2.getUserType();
			
			if(Constants.USER_TYPE_ADMIN.equals(userType2)||Constants.USER_TYPE_SUPERADMIN.equals(userType2)||Constants.USER_TYPE_CUS_SERVICE.equals(userType2)){
				BigDecimal userpoints = managerForm.getUserpoints();// 充值的钱
				userService.modifyBalance(userId, userpoints, type);
				
				if (userType.equals(Constants.USER_TYPE_AGENT_ONE)
						|| userType.equals(Constants.USER_TYPE_AGENT_TWO)
						|| userType.equals(Constants.USER_TYPE_AGENT_TWO)) {// 如果是代理则返回代理列表
					return mapping.findForward("statusAgentList");
				} else {
					ActionForward forward = mapping.findForward("modifyBalance");
					StringBuffer path = new StringBuffer();
					path.append(forward.getPath());
					path.append("&userType=1");
					return new ActionForward(forward.getName(), path.toString(), true);
				}
			}else{
				request.setAttribute("msg", "您无此权限进行该操作！");
				return mapping.findForward("error");	
			}
		}else{
			request.setAttribute("msg", "出错了！");
			return mapping.findForward("error");	
		}
		
	}
	
	/**
	 * 余额明细
	 */
	public ActionForward balance(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();

		ManagerForm frm = (ManagerForm) form;
		String userId = frm.getUserId();
		String loginName = frm.getLoginName();
		String startDate = frm.getStartDate();//yyyy-MM-dd HH:mm:ss
		String endDate = frm.getEndDate();//yyyy-MM-dd HH:mm:ss
		String gameType = frm.getGameType();
		String cashType = frm.getCashType();
		String betDetailId = frm.getBetDetailId();//投注订单
		
		if(ParamUtils.chkString(userId)){
		    hsql.append(" and de.userId=? ");
			para.add(userId);
		}
        if(ParamUtils.chkString(loginName)){
		    hsql.append(" and de.loginName=? ");
		    para.add(loginName);
		}
		if(ParamUtils.chkString(gameType)){
			hsql.append(" and de.modelType = ? ");
			para.add(gameType);
		}
		
		if(ParamUtils.chkString(cashType)){
			hsql.append(" and de.cashType = ? ");
			para.add(cashType);
		}
		if(ParamUtils.chkString(startDate)){
			hsql.append(" and de.createTime >= ? ");
			para.add(startDate);
		}
		if(ParamUtils.chkString(endDate)){
			hsql.append(" and de.createTime <= ? ");
			para.add(endDate);
		}
		
		if(ParamUtils.chkString(betDetailId)){
			hsql.append(" and (exists(select rl.tradeDetailId from UserTradeDetailRl rl where rl.tradeDetailId=de.tradeDetailId and rl.betDetailId=?)");
			hsql.append(" or de.refId=?) ");
			para.add(betDetailId);
			para.add(betDetailId);
		}
		
		hsql.append(" and u.userType < ? ");
		para.add(Constants.USER_TYPE_TEST);

		hsql.append(" order by de.tradeDetailId desc ");

		PaginationSupport ps = userService.findUserTradeDetail(hsql.toString(), para,
				startIndex, pageSize);
		@SuppressWarnings("unchecked")
		List<UserTradeDetail> list = ps.getItems();

		if(list != null && list.size() >0){
			for(UserTradeDetail detail:list){
				
				String cashType2 = detail.getCashType();//数据库资金类型
				String cashType3 = Constants.getCashTradeTypeNameZh(cashType2);//中文资金类型
				detail.setCashType(cashType3);
				String gameType2 = detail.getModelType();//彩票类型
				String gameType3 = Constants.getGameName(gameType2);
				detail.setModelType(gameType3);
				
				Integer refId = detail.getRefId();
			}
		}
		
		request.setAttribute("list", list);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		frm.setStartIndex(String.valueOf(startIndex));
		request.setAttribute("userId", ParamUtils.chkString(userId)?userId.toString():"");
		request.setAttribute("loginName", loginName);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("gameType", gameType);
		request.setAttribute("cashType", cashType);
		return mapping.findForward("balance");
	}
	public ActionForward balanceDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();

		ManagerForm frm = (ManagerForm) form;
		String userId = frm.getUserId();
		String loginName = frm.getLoginName();
		String startDate = frm.getStartDate();//yyyy-MM-dd HH:mm:ss
		String endDate = frm.getEndDate();//yyyy-MM-dd HH:mm:ss
		String gameType = frm.getGameType();
		String cashType = frm.getCashType();
		String sessionNo = frm.getSessionNo();
		String betDetailId = frm.getBetDetailId();//投注订单
		
		if(ParamUtils.chkString(userId)){
		    hsql.append(" and de.userId=? ");
			para.add(userId);
		}
        if(ParamUtils.chkString(loginName)){
		    hsql.append(" and de.loginName=? ");
		    para.add(loginName);
		}
		if(ParamUtils.chkString(gameType)){
			hsql.append(" and de.modelType = ? ");
			para.add(gameType);
		}
		
		if(ParamUtils.chkString(cashType)){
			hsql.append(" and de.cashType = ? ");
			para.add(cashType);
		}
		if(ParamUtils.chkString(startDate)){
			hsql.append(" and de.createTime >= ? ");
			para.add(startDate);
		}
		if(ParamUtils.chkString(endDate)){
			hsql.append(" and de.createTime <= ? ");
			para.add(endDate);
		}
		if(ParamUtils.chkString(sessionNo)){
			hsql.append(" and de.sessionNo = ? ");
			para.add(sessionNo.trim());
		}
		if(ParamUtils.chkString(betDetailId)){
			hsql.append(" and (exists(select rl.tradeDetailId from UserTradeDetailRl rl where rl.tradeDetailId=de.tradeDetailId and rl.betDetailId=?)");
			hsql.append(" or de.refId=?) ");
			para.add(betDetailId);
			para.add(betDetailId);
		}
		
		hsql.append(" and u.userType < ? ");
		para.add(Constants.USER_TYPE_TEST);

		hsql.append(" order by de.tradeDetailId desc ");

		PaginationSupport ps = userService.findUserTradeDetail(hsql.toString(), para,
				startIndex, pageSize);
		@SuppressWarnings("unchecked")
		List<UserTradeDetail> list = ps.getItems();

		if(list != null && list.size() >0){
			for(UserTradeDetail detail:list){
				
				String cashType2 = detail.getCashType();//数据库资金类型
				String cashType3 = Constants.getCashTradeTypeNameZh(cashType2);//中文资金类型
				detail.setCashType(cashType3);
				String gameType2 = detail.getModelType();//彩票类型
				String gameType3 = Constants.getGameName(gameType2);
				detail.setModelType(gameType3);
			}
		}
		List<GaSessionInfo> gaList = gaService.findGaSessionInfoList();
		List<GaSessionInfo> gaXyList = new ArrayList<GaSessionInfo>();
		List<GaSessionInfo> gaGfList = new ArrayList<GaSessionInfo>();
		if (gaList != null && gaList.size() > 0) {
			for (GaSessionInfo ga : gaList) {
				if (ga.getPlayCate().equals("1")) {
					ga.setGameTitle("官方"+ga.getGameTitle());
					gaGfList.add(ga);
				} else {
					ga.setGameTitle("信用"+ga.getGameTitle());
					gaXyList.add(ga);
				}
			}
		}
		request.setAttribute("gaXyList", gaXyList);
		request.setAttribute("gaGfList", gaGfList);
		request.setAttribute("list", list);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		frm.setStartIndex(String.valueOf(startIndex));
		request.setAttribute("userId", ParamUtils.chkString(userId)?userId.toString():"");
		request.setAttribute("loginName", loginName);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("gameType", gameType);
		request.setAttribute("cashType", cashType);
		request.setAttribute("sessionNo", sessionNo);
		return mapping.findForward("balanceDetail");
	}
	
	/**
	 * 查看指定账单的投注详情
	 */
	public void view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		//User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		//String loginUserType = loginUser.getUserType();//登录用户类型
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		Integer tradeDetailId = ParamUtils.getIntegerParameter(request, "tradeDetailId");
		UserTradeDetail detail=(UserTradeDetail) gaService.getObject(UserTradeDetail.class, tradeDetailId);
		
		String gfxyType = detail.getGfxy();
		if(gfxyType.equals(Constants.GAME_PLAY_CATE_GF)){
			Integer joinId = null;//官方订单id
			if(detail.getCashType().equals(Constants.CASH_TYPE_CASH_PRIZE)){//中奖派彩本表refId
				joinId = detail.getRefId();
			}else{
				UserTradeDetailRl rl=baseDataService.findUserTradeDetailRlByTradeId(detail.getTradeDetailId());
				joinId = rl.getBetDetailId();
			}
			
			GaBetSponsor sponsor=(GaBetSponsor) gaService.getObject(GaBetSponsor.class,joinId);
			List<GaBetSponsorDetail> list  = gaService.findGaBetSponsorDetailListByJointId(sponsor.getJointId());
			if(list!=null&&list.size()>0){
				
				org.json.JSONArray jsonArray = new org.json.JSONArray();
				BigDecimal totalMoney = new BigDecimal(0);//总共投注值
				for(GaBetSponsorDetail gaBetDetail:list){
					BigDecimal betMoney = ProductUtil.checkBigDecimal(gaBetDetail.getBetMoney());
					totalMoney = totalMoney.add(betMoney);
					JSONObject obj = new JSONObject();// 返回数据层
					obj.put("betDetailId", sponsor.getJointId());
					obj.put("sessionNo", sponsor.getSessionNo());
					obj.put("playName", ParamUtils.chkStringNotNull(sponsor.getGameName()));
					obj.put("betName", ParamUtils.chkStringNotNull(gaBetDetail.getTitle()));
					obj.put("optionTitle", ParamUtils.chkStringNotNull(gaBetDetail.getOptionTitle()));
					obj.put("betMoney",ProductUtil.checkBigDecimal(betMoney));
					obj.put("winCash", ProductUtil.checkBigDecimal(gaBetDetail.getWinMoney()));
					obj.put("winResult", ParamUtils.chkStringNotNull(gaBetDetail.getWinResult()));
					jsonArray.put(obj);
				}
				data.put("totalMoney", totalMoney);
				data.put("items", jsonArray);
				
				String cashType=detail.getCashType();
				if(cashType.equals(Constants.CASH_TYPE_CASH_PRIZE)||cashType.equals(Constants.CASH_TYPE_CASH_BUY_LOTO)){
					data.put("cashType", cashType);
				}else{
					data.put("cashType", "");
				}
				
				msg="请求成功";
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				msg="没有数据！";
				code = APIConstants.CODE_NOT_FOUND;
			}
		}else{
			// 
			List<GaBetDetail> list = new ArrayList<GaBetDetail>();
			list = gaService.findGaBetDetailListByTradeId(tradeDetailId);
			if(list!=null&&list.size()>0){
				
				org.json.JSONArray jsonArray = new org.json.JSONArray();
				Integer totalMoney = 0;//总共投注值
				for(GaBetDetail gaBetDetail:list){
					Integer betMoney = gaBetDetail.getBetMoney();
					totalMoney = totalMoney + betMoney;
					JSONObject obj = new JSONObject();// 返回数据层
					obj.put("betDetailId", gaBetDetail.getBetDetailId());
					obj.put("sessionNo", gaBetDetail.getSessionNo());
					obj.put("playName", ParamUtils.chkStringNotNull(gaBetDetail.getPlayName()));
					obj.put("betName", ParamUtils.chkStringNotNull(gaBetDetail.getPlayName()+(gaBetDetail.getBetName()!=null?"-"+gaBetDetail.getBetName():"")));
					obj.put("optionTitle", ParamUtils.chkStringNotNull(gaBetDetail.getOptionTitle()));
					obj.put("betMoney", gaBetDetail.getBetMoney());
					obj.put("winCash", ProductUtil.checkBigDecimal(gaBetDetail.getWinCash()));
					obj.put("winResult", ParamUtils.chkStringNotNull(gaBetDetail.getWinResult()));
					jsonArray.put(obj);
				}
				data.put("totalMoney", totalMoney);
				data.put("items", jsonArray);
				
				String cashType=detail.getCashType();
				if(cashType.equals(Constants.CASH_TYPE_CASH_PRIZE)||cashType.equals(Constants.CASH_TYPE_CASH_BUY_LOTO)){
					data.put("cashType", cashType);
				}else{
					data.put("cashType", "");
				}
				
				msg="请求成功";
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				msg="没有数据！";
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		
		data.put("gfxyType", gfxyType);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 用户个人投注记录
	 */
	public ActionForward betList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		String userId=ParamUtils.getParameter(request, "userId");//通过用户列表查询投注记录

		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		
		if(ParamUtils.chkString(userId)){
			hsql.append(" and upper(u.userId) = ? ");
			para.add(userId); 
		}

		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName();
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		} else if(ParamUtils.chkString(userId)){
			hsql.append(" and upper(u.userId) = ? ");
			para.add(userId.trim().toUpperCase());
			loginName = userId;
			frm.setLoginName(loginName);
		}
		if(ParamUtils.chkString(startDate)){
			hsql.append(" and be.betTime >= ?  ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if(ParamUtils.chkString(endDate)){
			hsql.append(" and be.betTime <= ?  ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}

		hsql.append(" order by be.betTime desc ");

		PaginationSupport ps = userService.findBetList(hsql.toString(), para,
				startIndex, pageSize);
		List<UserDTO> list = ps.getItems();

		request.setAttribute("list", list);
		request.setAttribute("loginName", loginName);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		frm.setStartIndex(startIndex + "");
		return mapping.findForward("betList");
	}
	/**
	 * 今日用户注册统计
	 */
	public ActionForward todayRegist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		
		ManagerForm frm = (ManagerForm) form;
		String userName = frm.getLoginName();
		if(ParamUtils.chkString(userName)){
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		Date start = DateTimeUtil.getCurrentDayStart(); //今天的零点。
		hsql.append(" and upper(u.registDateTime) >= ? ");
		para.add(start);
		
		hsql.append(" order by u.registDateTime desc ");
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<UserDTO> list = ps.getItems();
		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		frm.setStartIndex(startIndex + "");
		return mapping.findForward("todayRegist");
	}
	
	/**
	 * 师傅徒弟列表
	 */
	public ActionForward masterPrentice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		
		ManagerForm frm = (ManagerForm) form;
		String userName = frm.getLoginName();
		if(ParamUtils.chkString(userName)){
			hsql.append(" and (upper(u.loginName) = ? or upper(u.userId) = ? or upper(u.invitationCode) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		
		PaginationSupport ps = userService.findUser(hsql.toString(), para,
				startIndex, pageSize);
		List<UserDTO> list = ps.getItems();
		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		frm.setStartIndex(startIndex + "");
		return mapping.findForward("masterPrentice");
	}
	
	/**
	 * 修改密码
	 */
	public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession(false).getAttribute("loginUser");
		ManagerForm frm = (ManagerForm) form;
		frm.setUser(user);
		return mapping.findForward("modifyPassword");
	}
	/**
	 * 保存修改密码
	 */
	public ActionForward savePassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ManagerForm frm = (ManagerForm) form;
		User temp=frm.getUser();
		User user=(User) userService.getObject(User.class, temp.getUserId());	
		String oldPassword=frm.getOldPassword();
		String password=frm.getPassword();
		String repassword=frm.getRepassword();
		if(!password.equals(repassword)){
			request.setAttribute("msg", "两次输入的新密码不一致");
			return mapping.findForward("error");
		}
		if(!MD5.exc(oldPassword).equals(user.getPassword())){
			request.setAttribute("msg", "原密码输入不正确");
			return mapping.findForward("error");
		}

		user.setPassword(MD5.exc(frm.getPassword()));
		userService.saveObject(user, null);
		request.setAttribute("msg", "修改成功");
		return mapping.findForward("error");
	}
	
	
		/**
	 * 跳转扣款
	 */
	public ActionForward preModifyMoney(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// User curUser = ParamUtils.getCurrentUser(request);
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		String category = ParamUtils.getParameter(request, "category");
		ManagerForm managerForm = (ManagerForm) form;
		User user = userService.getUser(userId);
		if(user.getUserBalance() == null){
			user.setUserBalance(new BigDecimal(0));
		}
		managerForm.setUser(user);
		
		if(ParamUtils.chkString(category)){
			request.setAttribute("category", category);
		}
		
		String userType = ParamUtils.getParameter(request, "userType");
		request.setAttribute("userType", userType);
	
		return mapping.findForward("preModifyMoney");
	}
	/**
	 * 扣款
	 */
	public ActionForward modifyMoney(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManagerForm managerForm = (ManagerForm) form;
		User user = managerForm.getUser();
		String userType = user.getUserType();
		
		String category = ParamUtils.getParameter(request, "category");
		String type = managerForm.getType();//扣款类型
		
		BigDecimal userpoints = managerForm.getUserpoints();//扣款金额
		User tempUser = (User) userService.getObject(User.class,user.getUserId());
		BigDecimal money = ProductUtil.checkBigDecimal(tempUser.getMoney());//用户余额
		if(userpoints == null || userpoints.compareTo(new BigDecimal(0)) <=0){
			request.setAttribute("msg", "请输入扣款金额！");
			return mapping.findForward("error");	
		}
		if(money.compareTo(userpoints) == -1){
			request.setAttribute("msg", "用户余额不足！");
			return mapping.findForward("error");
		}
		userService.modifyMoney(user.getUserId(),userpoints,type);
		
		// 获取当前用户
		User loginUser = null;
		loginUser = (User) request.getSession(false).getAttribute("loginUser");
		StringBuffer loginText = new StringBuffer();
		loginText.append("扣款：操作人");
		loginText.append(loginUser.getLoginName());
		loginText.append("[");
		loginText.append(loginUser.getUserId());
		loginText.append("]，给用户[");
		loginText.append(user.getLoginName());
		loginText.append("]扣款，金额为[");
		loginText.append(userpoints.toString());
		loginText.append("]");
		userService.updateUserLog(request,loginUser,loginText.toString());
		
		if (userType.equals(Constants.USER_TYPE_AGENT_ONE)
				|| userType.equals(Constants.USER_TYPE_AGENT_TWO)
				|| userType.equals(Constants.USER_TYPE_AGENT_TWO)) {// 如果是代理则返回代理列表
			return mapping.findForward("statusAgentList");
		} else {
			ActionForward forward = mapping.findForward("modifyMoney");
			StringBuffer path = new StringBuffer();
			path.append(forward.getPath());
			path.append("&userType=1");
			return new ActionForward(forward.getName(), path.toString(), true);
		}
	}
	
	/**
	 * 用户每日统计数据列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward dailyStatisticsList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		PaginationSupport ps =null;

		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
			para.add(loginName.trim().toUpperCase());
		}
		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate);
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate);
			
			ps = gaService.findDailyStatisticsList(hsql.toString(), para,
					startIndex, pageSize);
		}else{
			hsql.append(" order by ho.createTime desc");
			ps = gaService.findDailyStatisticsList2(hsql.toString(), para,
					startIndex, pageSize);
		}

		List<GaDTO> items = ps.getItems();
		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("dailyStatisticsList");
	}
	
	/**
	 * 代理统计数据列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward agentStatisticsList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		List<Object> para3 = new ArrayList<Object>();
		StringBuffer hqls3 = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的ID
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		String userType = frm.getUserType();
		PaginationSupport ps =null;
		List<GaDTO> saveList = new ArrayList<GaDTO>();
		
		if(ParamUtils.chkString(userType)){
			hqls3.append(" and u.userType = ? ");
			para3.add(userType);
		}else{
			hqls3.append(" and (u.userType = ? or u.userType = ? or u.userType = ? )");
			para3.add(Constants.USER_TYPE_AGENT_ONE);
			para3.add(Constants.USER_TYPE_AGENT_TWO);
			para3.add(Constants.USER_TYPE_AGENT_THREE);
		}
		if (ParamUtils.chkString(loginName)) {
			hqls3.append(" and (u.userId = ? or u.loginName = ? ) ");
			para3.add(loginName);
			para3.add(loginName);
		}
		ps = userService.findUser(hqls3.toString(), para3, startIndex, pageSize);
		List<User> userList = ps.getItems();
		
		//代理下会员数量
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hsql2 = new StringBuffer();
		hsql2.append(" and u.agentId != 'null' ");
		hsql2.append(" group by u.agentId ");
		List<GaDTO> agentList = gaService.findUserSubmemberList(hsql2.toString(),para2);

		if(ParamUtils.chkString(startDate) && ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate + " 00:00:00");
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate + " 23:59:59");
		}
		hsql.append(" and (u.userId = ? or u.agentId = ? )");
		hsql.append(" group by ho.cashType ");
		
		GaDTO dto = null;
		List<GaDTO> tradelist = null; //代理及会员交易记录
		for(User user: userList){
			Integer userId = user.getUserId();
			para.add(userId);
			para.add(userId);
			tradelist = gaService.findAgentStatisticsList(hsql.toString(),para);
			para.remove(userId);
			para.remove(userId);

			dto = new GaDTO();
			dto.setUserId(userId);
			dto.setUserName(user.getLoginName());
			dto.setNumber(0);
			dto.setWinCash(new BigDecimal(0));
			dto.setTotalBet(new BigDecimal(0));
			for(GaDTO agent:agentList){
				if(userId.equals(agent.getUserId())){
					dto.setNumber(agent.getNumber());
					break;
				}
			}
			for(GaDTO traDto:tradelist){
				if(userId.equals(traDto.getUserId())){
					String cashType = traDto.getCashType();
					BigDecimal money = traDto.getPaperMoney();
					if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)){
						dto.setTotalBet(money.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)){
						dto.setWinCash(money);
					}
				}
			}
			dto.setPaperMoney(dto.getWinCash().subtract(dto.getTotalBet()));
			saveList.add(dto);
		}

		request.setAttribute("list", saveList);
		request.setAttribute("count", String.valueOf(saveList.size()));
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("agentStatisticsList");
	}

	/**
	 * 用户等级
	 */
	public ActionForward userLevelList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		ManagerForm frm = (ManagerForm) form;
		String levelName = frm.getAgentName();
		
		HQUtils hq = new HQUtils();
		hq.setStartIndex(startIndex);
		hq.setPageSize(pageSize);
		hq.addHsql(" from UserLevel ul where 1=1 ");
		if(ParamUtils.chkString(levelName)){
			hq.addHsql(" and ul.levelName like ? ");
			hq.addPars("%"+levelName+"%");
		}
		PaginationSupport ps =  gaService.findObjectPage(hq);
		List<User> items = ps.getItems();
		request.setAttribute("levelList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("levelList");
	}
	
	/**
	 * 用户等级预添加/编辑
	 */
	public ActionForward userLevelPreAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = new ManagerForm();
		
		int id = ParamUtils.getIntegerParameter(request, "id");
		if(ParamUtils.chkInteger(id)){
			UserLevel ul = (UserLevel) gaService.getObject(UserLevel.class, id);
			if(ul!=null){
				managerForm.setUserLevel(ul);
			}
		}
		request.setAttribute("managerForm", managerForm);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		request.setAttribute("id", id);
		return mapping.findForward("levelpreAdd");
	}
	
	/**
	 * 用户等级保存
	 */
	public ActionForward addLevel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = (ManagerForm) form;
		UserLevel userLevel = managerForm.getUserLevel();
		int id = ParamUtils.getIntegerParameter(request, "id");
		
		String msg = userService.addLevel(userLevel,id);
		
		if(msg.length()>0){
			
			UserLevel ul = (UserLevel) gaService.getObject(UserLevel.class, id);
			if(ul!=null){
				managerForm.setUserLevel(ul);
			}
			request.setAttribute("msg", msg);
			request.setAttribute("managerForm", managerForm);
			request.setAttribute("id", id);
			return mapping.findForward("levelpreAdd");
		}else{
			return mapping.findForward("levelInit");
		}
		
	}

	/**
	 * 用户等级删除
	 */
	public ActionForward userLevelDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = (ManagerForm) form;
		int id = ParamUtils.getIntegerParameter(request, "id");
		
		userService.deleteObject(UserLevel.class, id, null);
		
		return mapping.findForward("levelInit");
		
	}
	/**
	 * 用户详细信息
	 */
	public void userinfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer userId = ParamUtils.getIntegerParameter(request, "userId");
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		
		if (!ParamUtils.chkInteger(userId)){
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if (message.equals("")) {
			User user=(User) userService.getObject(User.class, userId);
			
			if(user!=null){
				JSONObject obj = new JSONObject();
				String accountNo = user.getAccountNo();
				String bankName = user.getBankName();
				String bindName = user.getBindName();
				String agentName = user.getAgentName();
				String userType = user.getUserType();
				String loginName = user.getLoginName();
				String userName = user.getUserName();
				BigDecimal money = user.getMoney();
				Date lastLoginDate = user.getLastLoginDate();
				String lastLoginIp = user.getLastLoginIp();
				Date registDateTime = user.getRegistDateTime();
				
				
				obj.put("uid", userId);
				obj.put("loginName", loginName);
				obj.put("cellPhone", ParamUtils.chkStringNotNull(user.getCellPhone()));
				obj.put("userName",ParamUtils.chkStringNotNull(userName));
				obj.put("money",ProductUtil.BigFormat(money));
				obj.put("lastLoginDate",DateTimeUtil.DateToStringAll(lastLoginDate));
				obj.put("registDate",DateTimeUtil.DateToString(registDateTime));
				
				obj.put("lastLoginIp", ParamUtils.chkStringNotNull(lastLoginIp));
				
				obj.put("agentName", ParamUtils.chkStringNotNull(agentName));
				
				
				obj.put("userType", ParamUtils.chkStringNotNull(userType));
				obj.put("accountNo", ParamUtils.chkStringNotNull(accountNo));
				obj.put("bankName", ParamUtils.chkStringNotNull(bankName));
				obj.put("bindName", ParamUtils.chkStringNotNull(bindName));
				
				data.put("obj", obj);
				code=APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				message="无此用户";
			}
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	
	/**
	 * 用户盈亏统计
	 */
	@SuppressWarnings("unchecked")
	public ActionForward userMoneyReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(ManagerUtils.checkRole(loginUserType)){
			int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
			int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
					RamConstants.MAXPAGEITEMS);

			List<User> userList = new ArrayList<User>();//用户list
			List<UserMoneyDTO> list = new ArrayList<UserMoneyDTO>();//用户盈亏列表list
			List<GaDTO> tradeList = null;//用户盈亏列表list
			
			//通过用户盈亏统计列表查询
			ManagerForm frm = (ManagerForm) form;
			String userName = frm.getLoginName();
			String userType = frm.getUserType();
			String startDate = frm.getStartDate();
			String endDate = frm.getEndDate();
			//默认为本月第一天的日期
			if(!ParamUtils.chkString(startDate)){
				startDate = DateTimeUtil.getMonthFirstDay();
				frm.setStartDate(startDate);
			}
			if(!ParamUtils.chkString(endDate)){
				endDate = DateTimeUtil.DateToString(new Date());
				frm.setEndDate(endDate);
			}
			
			//查询出用户
			PaginationSupport ps = userService.findUserList(userName, userType, startDate,endDate,startIndex,pageSize);
			userList = ps.getItems();

			if(userList != null && userList.size()>0){
				StringBuffer ids = new StringBuffer();//用户id串
				for(User user:userList){
					UserMoneyDTO dto = new UserMoneyDTO();//用户盈亏dto
					Integer userId = user.getUserId();
					String loginName = user.getLoginName();
					dto.setUserId(userId);
					dto.setUserName(loginName);
					list.add(dto);
					
					ids.append("'").append(String.valueOf(userId)).append("'").append(",");
				}
				ids = ids.deleteCharAt(ids.length()-1);
			
				tradeList = userService.findUserTradeDetailList(ids.toString(),startDate,endDate);
				//用户投注已开奖部分
				List<GaDTO> betCountList = userService.findUserBetCountList(ids.toString(),startDate,endDate);
				//下级会员数量
				List<GaDTO> memberList = userService.findSubNumList(ids.toString());
				
				list = userService.updateUserMoneyDTO(list,tradeList,betCountList,memberList);
				
				//如果是第一页，查询统计的数据
				if(startIndex == 0){
					//查询出所有用户(不分页）
					PaginationSupport psSum = userService.findUserList(userName, userType, startDate,endDate,0,99999);
					List<User> sumUserList = psSum.getItems();
					if(sumUserList != null && sumUserList.size()>0){
						StringBuffer idsSum = new StringBuffer();//用户id串
						for(User user:sumUserList){
							Integer userId = user.getUserId();
							idsSum.append("'").append(String.valueOf(userId)).append("'").append(",");
						}
						idsSum = idsSum.deleteCharAt(idsSum.length()-1);

						UserMoneyDTO sumDto = new UserMoneyDTO();//用户盈亏合计dto
						sumDto = userService.countSumMoneyDTO(idsSum.toString(),startDate,endDate);
						frm.setUserMoneyDTO(sumDto);
					}
				}
			}

			request.setAttribute("list", list);
			request.setAttribute("count", String.valueOf(ps.getTotalCount()));
			frm.setStartIndex(String.valueOf(startIndex));
			request.setAttribute("loginName", userName);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			return mapping.findForward("userMoneyReportList");
		}else{
			request.setAttribute("msg", "无权限！请联系管理员！");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userLimitList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		ManagerForm frm = (ManagerForm) form;
		String loginName = frm.getLoginName(); //查询的用户名字
		String userId = frm.getUserId(); //查询的用户名字
		
//		
		if (ParamUtils.chkString(loginName)) {
			hsql.append(" and  u.loginName = ?  ");
			para.add(loginName.trim());
		}
		if (ParamUtils.chkString(userId)) {
			hsql.append(" and  u.uid = ?  ");
			para.add(userId.trim());
		}
		hsql.append(" order by u.id desc");
		PaginationSupport ps = userService.findUserLimitList(hsql.toString(), para,
				startIndex, pageSize);
		List<User> items = ps.getItems();
		request.setAttribute("managerList", items);
		request.setAttribute("loginName", loginName);
		request.setAttribute("userId", userId);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("userLimitList");
	}
	
	/**
	 * 显示用户创建页面
	 */
	public ActionForward userLimitPreAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = new ManagerForm();
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		if(ParamUtils.chkInteger(id)){
			UserLimit userlimit = (UserLimit) userService.getObject(UserLimit.class, id);
			managerForm.setUserlimit(userlimit);
		}
		request.setAttribute("managerForm", managerForm);

		User user = (User) request.getSession(false).getAttribute("loginUser");
		return mapping.findForward("userLimitPreAdd");
	}
	
	/**
	 * 添加用户限制
	 */
	public ActionForward userLimitAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm frm = (ManagerForm) form;
		UserLimit userlimit = frm.getUserlimit();
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		if (userlimit.getLoginName()!=null) {
			
			String[] nameArr = userlimit.getLoginName().split(",");
			List<UserLimit> saveList = new ArrayList<UserLimit>();
			List<UserLog> logSaveList = new ArrayList<UserLog>();
			if(nameArr.length>0){
				for(int i = 0;i<nameArr.length;i++){
					String tmp = nameArr[i];
					UserLimit record = null;
					User user = null;
					UserLog log = new UserLog();
					if(tmp.length()==11){
						user = userService.findUserByLoginName(tmp.trim());
						record = userService.findUserLimitByUid(user.getUserId());
					}else{
						user = (User) userService.getObject(User.class, Integer.valueOf(tmp.trim()));
						record = userService.findUserLimitByUid(user.getUserId());
					}
					// log
					log.setUserId(user.getUserId());
					log.setDateTime(new Date());
					log.setLoginName(loginUser.getLoginName());
					
					if(ParamUtils.chkInteger(userlimit.getId())
							||record!=null){
						// 修改
						UserLimit oldLimit ;
						if(ParamUtils.chkInteger(userlimit.getId())){
							oldLimit = (UserLimit) userService.getObject(UserLimit.class, userlimit.getId());
						}else{
							oldLimit = record;
						}
						oldLimit.setUid(user.getUserId());
						oldLimit.setBetMoney(userlimit.getBetMoney());
						oldLimit.setCashTip(userlimit.getCashTip());
						oldLimit.setCashMessage(userlimit.getCashMessage());
						oldLimit.setLoginName(user.getLoginName());
						oldLimit.setBetMoneyStatus("1");
						oldLimit.setCreateTime(new Date());
						if(ParamUtils.chkString(userlimit.getCashMessage())){
							oldLimit.setCashMessage(userlimit.getCashMessage());
						}else{
							oldLimit.setCashMessage("0");
						}
						
						saveList.add(oldLimit);
						
						// log
						log.setActionText("修改 用户"+user.getUserId()+"的提款打码量限制为"+userlimit.getBetMoney()+"%");
						logSaveList.add(log);
						
					}else{
						// 新增
						userlimit.setUid(user.getUserId());
						UserLimit newlimit = new UserLimit();
						newlimit.setUid(user.getUserId());
						newlimit.setBetMoney(userlimit.getBetMoney());
						newlimit.setCashTip(userlimit.getCashTip());
						newlimit.setCashMessage(userlimit.getCashMessage());
						newlimit.setLoginName(user.getLoginName());
						newlimit.setBetMoneyStatus("1");
						newlimit.setCreateTime(new Date());
						if(ParamUtils.chkString(userlimit.getCashMessage())){
							newlimit.setCashMessage(userlimit.getCashMessage());
						}else{
							newlimit.setCashMessage("0");
						}
						saveList.add(newlimit);
						// log
						log.setActionText("新增 用户"+user.getUserId()+"的提款打码量限制为"+userlimit.getBetMoney()+"%");
						logSaveList.add(log);
						
					}
					
				}
				userService.updateObjectList(saveList, null);
				userService.updateObjectList(logSaveList, null);
				return mapping.findForward("userLimitInit");
			}else{
				ActionForward forward = mapping.findForward("userLimitInit");
				StringBuffer path = new StringBuffer();
				path.append(forward.getPath());
				return new ActionForward(forward.getName(), path.toString(), true);
			}
			
		}else{
			ActionForward forward = mapping.findForward("userLimitInit");
			StringBuffer path = new StringBuffer();
			path.append(forward.getPath());
			return new ActionForward(forward.getName(), path.toString(), true);
		}
	}
	
	/**
	 * 删除
	 */
	public ActionForward userLimitDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ManagerForm managerForm = new ManagerForm();
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		if(ParamUtils.chkInteger(id)){
			UserLimit userlimit = (UserLimit) userService.getObject(UserLimit.class, id);
			if(userlimit!=null){
				userService.deleteObject(UserLimit.class, userlimit.getId(), null);
				
				UserLog log = new UserLog();
				log.setUserId(userlimit.getUid());
				log.setDateTime(new Date());
				log.setLoginName(loginUser.getLoginName());
				userService.saveObject(log, null);
			}
		}
		return mapping.findForward("userLimitInit");
	}
	
	/**
	 * 更改用户限制状态
	 */
	public void changeUserLimitStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				||Constants.USER_TYPE_CUS_SERVICE.equals(loginUserType)){
			Integer limitId = ParamUtils.getIntegerParameter(request, "limitId");
			UserLimit limit = (UserLimit) gaService.getObject(UserLimit.class, limitId);
			String status = limit.getBetMoneyStatus();
			String  startStart="";
			String  endStart="";
			try {
				Integer uid=limit.getUid();
				
				if (status.equals(Constants.PUB_STATUS_OPEN)) {
					limit.setBetMoneyStatus(Constants.PUB_STATUS_CLOSE);
					startStart="开";
					endStart="关";
				} else {
					limit.setBetMoneyStatus(Constants.PUB_STATUS_OPEN);
					startStart="关";
					endStart="开";
				}
				userService.saveObject(limit, null);
				StringBuffer loginText = new StringBuffer();
				loginText.append("[更改限制状态]：操作人登录名[");
				loginText.append(loginUser.getLoginName());
				loginText.append("]，操作人ID[");
				loginText.append(loginUser.getUserId());
				loginText.append("]，被操作用户ID[");
				loginText.append(uid);
				loginText.append("]，状态从[");
				loginText.append(startStart);
				loginText.append("]，变成[");
				loginText.append(endStart);
				loginText.append("]");
				UserLog log = new UserLog();
				log.setUserId(uid);
				log.setDateTime(new Date());
				log.setActionText(loginText.toString());
				log.setLoginName(loginUser.getLoginName());
				
				userService.saveObject(log, null);
				code=APIConstants.CODE_REQUEST_SUCCESS;
				message="切换状态成功！";
			} catch (Exception e) {
				e.printStackTrace();
				message="切换状态失败！";
			}
		}else{
			message="无权限！请联系管理员！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
}
