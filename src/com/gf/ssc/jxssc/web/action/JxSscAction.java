package com.gf.ssc.jxssc.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.model.dto.AgentDTO;
import com.apps.util.JsonUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.gf.ssc.jxssc.model.GfJxSscGaSession;
import com.gf.ssc.jxssc.service.IJxSscService;
import com.gf.ssc.jxssc.web.form.JxSscForm;




public class JxSscAction extends BaseDispatchAction {
    private final IJxSscService cqSscService = (IJxSscService) getService("cqSscService");
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


		JxSscForm cqSscForm = (JxSscForm) form;
		String startDate = cqSscForm.getStartDate();
		String endDate = cqSscForm.getEndDate();
		String sessionNo = cqSscForm.getSessionNo();
		String status= cqSscForm.getStatus();

		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo = ? ");
			para.add(sessionNo);
		}
		
		User user = getUser(request);
		String userType = user.getUserType();


		hqls.append(" and ho.endTime < ? ");
		para.add(DateTimeUtil.DateToStringAll(new Date()));


		hqls.append(" and ho.openStatus = ? ");
		para.add(Constants.OPEN_STATUS_INIT);
		hqls.append(" order by ho.sessionNo asc ");
		PaginationSupport ps = cqSscService.findGaSessionList(hqls.toString(), para,
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
//		GfJxSscGaSession session = (GfJxSscGaSession)cqSscService.getObject(GfJxSscGaSession.class, sessionId);
//		String flag = "success";
//		String message="";
//		if(session!=null&&session.getOpenStatus().equals(BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)){
//			if(ParamUtils.chkString(session.getOpenResult())){
//				message="已保存";
//				flag = "success";
//			}else{
//				boolean result=cqSscService.saveOpenResult(session,openResult);
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
		GfJxSscGaSession session = (GfJxSscGaSession)cqSscService.getObject(GfJxSscGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			
			String result=cqSscService.saveAndOpenResult(session,openResult);
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

		JxSscForm cqSscForm = (JxSscForm) form;
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
		para.add(Constants.GAME_TYPE_GF_JXSSC);
//		hqls.append(" group by ho.sessionId order by ho.sessionId desc ");
		
		PaginationSupport ps = cqSscService.findGameBetAndWinList(hqls.toString(), para,
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
//		JxSscForm cqSscForm = (JxSscForm) form;
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
//			PaginationSupport ps = cqSscService.findGaBetDetail(hqls.toString(), para,
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
//		list = cqSscService.findGaBetDetailById(hqls.toString(), para);
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

		flag=cqSscService.updateInitSession(0);

		if(flag.equals("success")||flag.equals("inited")){
			cqSscService.updateInitSession(1);
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
		GfJxSscGaSession session = (GfJxSscGaSession)cqSscService.getObject(GfJxSscGaSession.class, sessionId);
		String flag = "success";


		if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_GF_JXSSC);
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
