package com.gf.ssc.cqssc.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.dto.AgentDTO;
import com.apps.util.JsonUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.gf.ssc.cqssc.model.GfCqSscGaSession;
import com.gf.ssc.cqssc.service.ICqSscService;
import com.gf.ssc.cqssc.web.form.CqSscForm;




public class CqSscAction extends BaseDispatchAction {
    private final ICqSscService gfCqSscService = (ICqSscService) getService("gfCqSscService");
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

		CqSscForm cqSscForm = (CqSscForm) form;
		String startDate = cqSscForm.getStartDate();
		String endDate = cqSscForm.getEndDate();
		String sessionNo = cqSscForm.getSessionNo();
		String status= cqSscForm.getStatus();
		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo = ? ");
			para.add(sessionNo);
		}
		
		GfCqSscGaSession session = gfCqSscService.getCurrentSession();
		if (session != null) {
			Integer sesionId = 	session.getSessionId();	
			hqls.append(" and ho.sessionId > ? ");
			para.add(sesionId-5);
			hqls.append(" and ho.sessionId < ? ");
			para.add(sesionId+5);
		} else {
			hqls.append(" and ho.openStatus = ? ");
			para.add(Constants.OPEN_STATUS_INIT);
		}
		
		User user = getUser(request);
		String userType = user.getUserType();

		hqls.append(" order by ho.sessionNo desc ");
		PaginationSupport ps = gfCqSscService.findGaSessionList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();

		request.setAttribute("list", list);
		request.setAttribute("status", status);
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		cqSscForm.setStartIndex(String.valueOf(startIndex));
		return mapping.findForward("init");
	}
	


//	/**
//	 * 保存开奖结果
//	 */
//	public void saveOpenResult(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception, NoFunctionPermissionException {
//		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
//		String openResult = ParamUtils.getParameter(request, "openResult");
//	
//		GfCqSscGaSession session = (GfCqSscGaSession)gfCqSscService.getObject(GfCqSscGaSession.class, sessionId);
//		String flag = "success";
//		String message="";
//		if(session!=null&&session.getOpenStatus().equals(BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)){
//			if(ParamUtils.chkString(session.getOpenResult())){
//				message="已保存";
//				flag = "success";
//			}else{
//				boolean result=gfCqSscService.saveOpenResult(session,openResult);
//				if(result){
//					message="保存成功";
//					flag = "success";
//				}else{
//					message="保存出错";
//					flag = "false&";
//				}
//			}		
//		}else{
//			message="期号不存在或者已开奖";
//			flag = "false";
//		}
//		JsonUtil.AjaxWriter(response, flag);
//	}
	
	/**
	 * 保存并开奖
	 */
	public void openResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String openResult = ParamUtils.getParameter(request, "openResult");
		GfCqSscGaSession session = (GfCqSscGaSession)gfCqSscService.getObject(GfCqSscGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			
			String result=gfCqSscService.saveAndOpenResult(session,openResult);
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
				loginText.append("]，给重庆时时彩第[");
				loginText.append(session.getSessionNo());
				loginText.append("]期手动开奖，开奖结果为[");
				loginText.append(openResult);
				loginText.append("]");
				userService.updateUserLog(request,loginUser,loginText.toString());

			}else{
				flag = "false";
			}
		}else{
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	public ActionForward betList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		CqSscForm cqSscForm = (CqSscForm) form;
		String startDate = cqSscForm.getStartDate();
		String endDate = cqSscForm.getEndDate();
		String sessionNo = cqSscForm.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and se.sessionNo = ? ");
			para.add(sessionNo);
		}
		if (ParamUtils.chkString(startDate)){
			hqls.append(" and se.startTime >= ? ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)){
			hqls.append(" and se.endTime <= ? ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}

		hqls.append(" and ho.betFlag = ? ");
		para.add(Constants.PUB_STATUS_OPEN);
		hqls.append(" and ho.gameType = ? ");
		para.add(Constants.GAME_TYPE_GF_CQSSC);
//		hqls.append(" group by ho.sessionId order by ho.sessionId desc ");
		
		PaginationSupport ps = gfCqSscService.findGameBetAndWinList(hqls.toString(), para,
				startIndex, pageSize);
		
		List<AgentDTO> list = ps.getItems();
		
		request.setAttribute("list", list);
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("endDate", endDate);
		request.setAttribute("startDate", startDate);
		request.setAttribute("cqSscForm", cqSscForm);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		cqSscForm.setStartIndex(String.valueOf(startIndex));
	

		return mapping.findForward("betList");
	}
	


//	/**
//	 * 个人投注记录
//	 */
//	public ActionForward betManager(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request,HttpServletResponse response)throws Exception{
//		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
//		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
//
//		CqSscForm cqSscForm = (CqSscForm) form;
//		String userName = cqSscForm.getUserName();
//		String sessionNo = cqSscForm.getSessionNo();
//
//		if (ParamUtils.chkString(sessionNo)) {
//			hqls.append(" and upper(ga.sessionNo) like ? ");
//			para.add("%"+ sessionNo.trim().toUpperCase() +"%");

//		}


//		if (ParamUtils.chkString(userName)){
//			hqls.append(" and (upper(u.userName) like ? OR upper(u.userId) like ? ) ");
//			para.add("%" + userName.trim().toUpperCase() + "%");
//			para.add("%" + userName.trim().toUpperCase() + "%");
//		}
//		User user = getUser(request);
//		String userType = user.getUserType();
////		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
//	        hqls.append(" and upper(ga.gameType) = ? ");
//	        para.add(Constants.GAME_TYPE_GF_BJ3);
//			PaginationSupport ps = gfCqSscService.findGaBetDetail(hqls.toString(), para,
//					startIndex, pageSize);
//			List list = new ArrayList();
//			if(ps != null){
//				list = ps.getItems();
//			}
//			request.setAttribute("list", list);
//			request.setAttribute("sessionNo", sessionNo);
//			request.setAttribute("endDate", userName);
//			request.setAttribute("count", (ps.getTotalCount() + "").toString());
//			cqSscForm.setStartIndex(startIndex + "");
////		}
//
//		return mapping.findForward("betManager");
//	}

	
//	/**
//	 * 投注详情
//	 */
//	public ActionForward betDetailManager(ActionMapping mapping,ActionForm form,
//			HttpServletRequest request,HttpServletResponse response)throws Exception{
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
//        String betDetailId = request.getParameter("betDetailId");
//        
//		if(ParamUtils.chkString(betDetailId)){
//			hqls.append(" and upper(ga.betDetailId) like ? ");
//			para.add("%" + betDetailId.trim().toUpperCase() + "%");
//		}
//		List<Bj3DTO> list = new ArrayList();
//		list = gfCqSscService.findGaBetDetailById(hqls.toString(), para);
//        Bj3DTO bj3 = new Bj3DTO();
//        if(list != null && list.size() > 0){
//        	bj3 = list.get(0);
//        }
//		
//		request.setAttribute("item", bj3);
//		return mapping.findForward("betDetailManager");
//	}
	
	/**
	 * 手动初始化期号
	 */
	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		String flag = "success";

		flag=gfCqSscService.updateInitTodaySession();

		if(flag.equals("success")||flag.equals("inited")){
			gfCqSscService.updateInitSession();
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 退还未开奖的钱
	 */
	public void drawback(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		GfCqSscGaSession session = (GfCqSscGaSession)gfCqSscService.getObject(GfCqSscGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_GF_CQSSC);
			if(result){			

			}else{
				flag = "false";	
			}
		}else{
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}	
	
	/**
	 * 撤回已经派彩的金额
	 */
	public void revokePrize(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		JSONObject map = new JSONObject();// 最外层
		String code = "";
		String msg = "";

		//管理员和超级管理员才能查询
		if(ManagerUtils.checkRole(loginUserType)){
			Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
			if(ParamUtils.chkInteger(sessionId)){
				GfCqSscGaSession session = (GfCqSscGaSession)gfCqSscService.getObject(GfCqSscGaSession.class, sessionId);

				if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
					boolean result = gfCqSscService.saveRevokePrize(session);
					if(result){
						if(!ManagerUtils.checkHidden(loginUserType)){
							
							StringBuffer loginText = new StringBuffer();
							loginText.append("撤回派彩：操作人");
							loginText.append(loginUser.getLoginName());
							loginText.append("[");
							loginText.append(loginUser.getUserId());
							loginText.append("]，给期号[");
							loginText.append(session.getSessionNo());
							loginText.append("]撤回错误派彩金额。");
							userService.updateUserLog(request,loginUser,loginText.toString());
						}
					
						msg="撤回派彩成功";
						code = APIConstants.CODE_REQUEST_SUCCESS;
					}else{
						msg="撤回派彩失败";
						code = APIConstants.CODE_SERVER_ERROR;
					}
				}else{
					msg="期号不存在或者未开奖！";
					code = APIConstants.CODE_REQUEST_ERROR;
				}
			}else{
				msg = APIConstants.PARAMS_EMPTY_MSG;
				code = APIConstants.CODE_REQUEST_ERROR;
			}
		}else{
			msg="无权限！请联系管理员！";
			code = APIConstants.CODE_UNAUTHORIZED_ERROR;
		}
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
}
