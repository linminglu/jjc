package com.gf.dcb.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.service.IDcbService;
import com.gf.dcb.web.form.DcbForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class DcbAction extends BaseDispatchAction {
    private final IDcbService gfDcbService = (IDcbService) getService("gfDcbService");
	private final IGaService gaService = (IGaService) getService("gaService");
	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		DcbForm dcbForm = (DcbForm) form;
//		String startDate = dcbForm.getStartDate();
//		String endDate = dcbForm.getEndDate();
		String sessionNo = dcbForm.getSessionNo();
		String status= dcbForm.getStatus();
		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		User user = getUser(request);
		String userType = user.getUserType();

		hqls.append(" and ho.endTime < ? ");//
		para.add(DateTimeUtil.DateToStringAll(new Date()));

		hqls.append(" and ho.openStatus = ? ");
		para.add(Constants.OPEN_STATUS_INIT);
		hqls.append(" order by ho.sessionNo desc ");
		PaginationSupport ps = gfDcbService.findGfDcbGaSessionList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();

		request.setAttribute("list", list);
		request.setAttribute("status", status);
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		dcbForm.setStartIndex(startIndex + "");

		return mapping.findForward("init");
	}

	
	/**
	 * 保存并开奖
	 */
	public void openResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String openResult = ParamUtils.getParameter(request, "openResult");
		GfDcbGaSession session = (GfDcbGaSession)gfDcbService.getObject(GfDcbGaSession.class, sessionId);
		String flag = "success";
		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			String result=gfDcbService.updateGfDcbGaSessionOpenResultMethod(session,openResult,null);
			if(!ParamUtils.chkString(result)){
				flag = "success";
				
				// 获取当前用户
				User loginUser = null;
				loginUser = (User) request.getSession(false).getAttribute("loginUser");
				StringBuffer loginText = new StringBuffer();
				loginText.append("手动开奖：操作人");
				loginText.append(loginUser.getLoginName());
				loginText.append("[");
				loginText.append(loginUser.getUserId());
				loginText.append("]，给双色球第[");
				loginText.append(session.getSessionNo());
				loginText.append("]期手动开奖，开奖结果为[");
				loginText.append(openResult);
				loginText.append("]");
				userService.updateUserLog(request,loginUser,loginText.toString());

			}else{
				flag = "false&";
			}
		}else{
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 每一期投注、开奖总额列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward betList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

//		DcbForm dcbForm = (DcbForm) form;
//		String startDate = dcbForm.getStartDate();
//		String endDate = dcbForm.getEndDate();
//		String sessionNo = dcbForm.getSessionNo();
//
//		if (ParamUtils.chkString(sessionNo)) {
//			hqls.append(" and se.sessionNo like ?");
//			para.add("%"+sessionNo+"%");
//		}
//		if (ParamUtils.chkString(startDate)){
//			hqls.append(" and se.startTime >= ? ");
//			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
//		}
//		if (ParamUtils.chkString(endDate)){
//			hqls.append(" and se.endTime <= ? ");
//			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
//		}
//		User user = getUser(request);
//		String userType = user.getUserType();
//
//			
//			PaginationSupport ps = gfDcbService.findBj3GaBetList(hqls.toString(), para,
//					startIndex, pageSize);
//			List list = new ArrayList();
//			if(ps != null){
//				list = ps.getItems();
//			}
//			request.setAttribute("list", list);
//			request.setAttribute("sessionNo", sessionNo);
//			request.setAttribute("endDate", endDate);
//			request.setAttribute("startDate", startDate);
//			request.setAttribute("count", (ps.getTotalCount() + "").toString());
//			dcbForm.setStartIndex(startIndex + "");

		return mapping.findForward("betList");
	}

	/**
	 * 个人投注记录
	 */
	public ActionForward betManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		DcbForm dcbForm = (DcbForm) form;
		String userName = dcbForm.getUserName();
		String sessionNo = dcbForm.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and upper(ga.sessionNo) like ? ");
			para.add("%"+ sessionNo.trim().toUpperCase() +"%");
		}
		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.userName) like ? OR upper(u.userId) like ? ) ");
			para.add("%" + userName.trim().toUpperCase() + "%");
			para.add("%" + userName.trim().toUpperCase() + "%");
		}
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
	        hqls.append(" and upper(ga.gameType) = ? ");
	        para.add(Constants.GAME_TYPE_GF_DCB);
			PaginationSupport ps = gfDcbService.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			dcbForm.setStartIndex(startIndex + "");
//		}

		return mapping.findForward("betManager");
	}
	
	/**
	 * 投注详情
	 */
	public ActionForward betDetailManager(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response)throws Exception{
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
//        String betDetailId = request.getParameter("betDetailId");
//        
//		if(ParamUtils.chkString(betDetailId)){
//			hqls.append(" and upper(ga.betDetailId) like ? ");
//			para.add("%" + betDetailId.trim().toUpperCase() + "%");
//		}
//		List<Bj3DTO> list = new ArrayList();
//		list = gfDcbService.findGaBetDetailById(hqls.toString(), para);
//        Bj3DTO bj3 = new Bj3DTO();
//        if(list != null && list.size() > 0){
//        	bj3 = list.get(0);
//        }
//		
//		request.setAttribute("item", bj3);
		return mapping.findForward("betDetailManager");
	}
	
	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "success";
		flag=gfDcbService.updateInitSession();
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 退还未开奖的钱
	 */
	public void drawback(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		GfDcbGaSession session = (GfDcbGaSession)gfDcbService.getObject(GfDcbGaSession.class, sessionId);
		String flag = "success";
		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_GF_DCB);
			if(result){			
			}else{
				flag = "false";	
			}
		}else{
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
}
