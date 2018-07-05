package com.apps.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.BlackListCacheUtil;
import com.apps.model.BlackList;
import com.apps.service.IBlackListService;
import com.apps.util.JsonUtil;
import com.apps.web.form.BlackListForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class BlackListAction  extends BaseDispatchAction {
	private final IBlackListService blackListService = (IBlackListService) getService("blackListService");
	
	/**
	 * 黑名单列表
	 */
	public ActionForward initBlackList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BlackListForm blackListForm = (BlackListForm) form;
		String startTime = blackListForm.getStartDate();
		String endTime = blackListForm.getEndDate();


		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and b.createTime>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and b.createTime<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}

		PaginationSupport ps = blackListService.findBlackList(hqls.toString(),
				para, startIndex, pageSize);
		List<BlackList> items = ps.getItems();

		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		blackListForm.setStartIndex(startIndex + "");
		return mapping.findForward("initBlackList");
	}
	
	/**
	 * 删除黑名单
	 */
	public void deleteBalck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		Integer bid = ParamUtils.getIntegerParameter(request, "bid");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				){
			if(ParamUtils.chkInteger(bid)){
				try{	
					BlackList black=(BlackList) userService.getObject(BlackList.class, bid);
					String type=black.getType();
					String delType="";
					if("1".equals(type)){
						delType="黑名单用户ID";
					}else if("2".equals(type)){
						delType="黑名单IP地址";
					}else{
						delType="白名单IP地址";
					}
					String value=black.getValue();
					blackListService.delBlack(bid);
					
					BlackListCacheUtil.updateBlackList(type);
					
					if(type.equals("1")){
						BlackListCacheUtil.delBlacklistUser(Integer.valueOf(value));
					}else if(type.equals("2")){
						BlackListCacheUtil.delBlacklistIP(value);
					}else{
						BlackListCacheUtil.delBlacklistByType(type,value);
					}
					
					StringBuffer loginText = new StringBuffer();
					loginText.append("[删除黑名单]：操作人登录名[");
					loginText.append(loginUser.getLoginName());
					loginText.append("]，操作人ID[");
					loginText.append(loginUser.getUserId());
					loginText.append("]，删除[");
					loginText.append(delType);
					loginText.append("]，值[");
					loginText.append(value);
					loginText.append("]");
					userService.updateUserLog(request,loginUser,loginText.toString());			
					code=APIConstants.CODE_REQUEST_SUCCESS;				
					message="删除成功！";
				}catch(Exception e){
					e.printStackTrace();
					message="删除出错！";
				}
			}else{
				message="参数有误";
			}
		}else{
			message="无权限！请联系管理员！";
		}
		data.put("code", code);
		data.put("msg", message);
		JsonUtil.AjaxWriter(response, data);
	}
	
	/**
	 * 跳转添加黑名单  （没有用了 页面使用ajax直接保存 在下一个方法）
	 */
	public ActionForward preAddBlack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				){
			return mapping.findForward("preAddBlack");
		}else{
			request.setAttribute("msg", "无权限！请联系管理员！");
			return mapping.findForward("error");
		}
	}
	
	/**
	 * 保存黑名单
	 */
	public void saveBlack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				){
			String type = ParamUtils.getParameter(request, "blackType");
			String value = ParamUtils.getParameter(request, "blackValue");
			if(ParamUtils.chkString(type)&&ParamUtils.chkString(value)){
				value = value.trim();
				Map<String,String> map=null;
				if("1".equals(type)){
					map=BlackListCacheUtil.getBlacklistUser();
				}else if("2".equals(type)){
					map=BlackListCacheUtil.getBlacklistIP();
				}else {
					map=BlackListCacheUtil.getBlackMapByType(type);
				}
				if(map!=null&&map.get(value)!=null){
					message="已经在黑名单中了，不用重复添加！";
				}else{
					BlackList blacklist=new BlackList();
					blacklist.setType(type);
					blacklist.setValue(value);
					blacklist.setCreateTime(new Date());
					blacklist=(BlackList) blackListService.saveObjectDB(blacklist);
					
					BlackListCacheUtil.updateBlackList(type);
					if(blacklist.getType().equals("1")){
						type="黑名单用户ID";
						BlackListCacheUtil.saveBlacklistUser(Integer.valueOf(value));
					}else if("2".equals(type)){
						type="黑名单IP地址";
						BlackListCacheUtil.saveBlacklistIP(value);
					}else if("3".equals(type)){
						BlackListCacheUtil.saveBlackMapByType(type,value);//更新map
						type="白名单IP地址";
					}
					
					if(blacklist!=null){
						StringBuffer loginText = new StringBuffer();
						loginText.append("[添加黑名单]：操作人登录名[");
						loginText.append(loginUser.getLoginName());
						loginText.append("]，操作人ID[");
						loginText.append(loginUser.getUserId());
						loginText.append("]，黑名单类型[");
						loginText.append(type);
						loginText.append("]，值[");
						loginText.append(value);
						loginText.append("]");
						userService.updateUserLog(request,loginUser,loginText.toString());
					}
					code=APIConstants.CODE_REQUEST_SUCCESS;				
					message="添加成功！";
				}
			}else{
				message="参数错误！";
			}
		}else{
			message="没有权限！";
		}
		data.put("code", code);
		data.put("msg", message);
		JsonUtil.AjaxWriter(response, data);
	}
	
	
}
