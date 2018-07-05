package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ram.RamConstants;
import com.ram.model.RecommendedUserInfo;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.web.user.form.RecommendedUserForm;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author cuishiyong
 */
public class RecommendedUserAction extends BaseDispatchAction {
	
	private final IUserService userService = (IUserService)getService("userService");
	
	
	/**
	 * 初始化邮件推荐页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer recommendUserId = ParamUtils.getIntegerParameter(request,"userId");
		RecommendedUserForm ruForm = new RecommendedUserForm();
		ruForm.setRecommendUserId(recommendUserId);
		request.setAttribute("recommendedUserForm",ruForm);
		saveToken(request);
		return mapping.findForward("init");
	}
	
	/**
	 * 保存
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		RecommendedUserForm ruForm = (RecommendedUserForm)form;
		Integer recommendUserId = ruForm.getRecommendUserId();
		String[] userNames = ruForm.getUserNames();
		String[] userEmails = ruForm.getUserEmails();
		if(isTokenValid(request,true)){
			User u = null;
			String nameStr = null;
			if(recommendUserId != null && recommendUserId.intValue()>0){
				u = userService.getUser(recommendUserId.intValue());
				if(u == null){
					u = new User();
				}
				nameStr = u.getUserNameZh();
			}
			Date dt = DateTimeUtil.getNowSQLDate();
			for(int i=0;i<userNames.length;i++){
				if(userNames[i] != null && userNames[i].length()>0){
					RecommendedUserInfo rui = new RecommendedUserInfo();
					rui.setUserName(userNames[i]);
					rui.setUserEmail(userEmails[i]);
					rui.setRecommendedUserId(recommendUserId);
					rui.setRecommendedUserName(nameStr);
					rui.setRecommendedDateTime(dt);
					userService.saveRecommendedUserInfo(rui,u);
				}
			}
		}
		request.setAttribute("recommendedUserForm",ruForm);
		return mapping.findForward("save");
	}
	
	/**
	 * 被推荐用户列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		RecommendedUserForm ruForm = (RecommendedUserForm)form;
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
//		查询条件
		String nameKey = ruForm.getNameKey();
		
//		构造where条件
		String iWhere = "";
		List iList = new ArrayList();
		if(nameKey != null && nameKey.trim().equals("") == false){
			iWhere += " and upper(rui.recommendedUserName) like ?";
			iList.add("%" + nameKey.trim().toUpperCase() + "%");
		}
		int totalCount = userService.getRecommendedUserNum(iWhere,iList);
		iWhere += " order by rui.recommendedDateTime desc";
		List userList = userService.findRecommendedUser(iWhere,iList,startIndex,pageSize);
		
		request.setAttribute("totalCount",(totalCount+"").trim());
		request.setAttribute("userList",userList);
		request.setAttribute("nameKey",nameKey);
		return mapping.findForward("query");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		User user = (User)request.getSession().getAttribute("loginUser");
		Integer ruId = ParamUtils.getIntegerParameter(request,"ruId");
		if(ruId != null && ruId.intValue()>0){
			userService.delRecommendedUserInfo(ruId,user);
		}
		return mapping.findForward("del");
	}
	
}
