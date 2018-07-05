package com.xy.hk.marksix.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.xy.hk.marksix.MarkSixConstants;
import com.xy.hk.marksix.model.MarkSixGaBet;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.dto.MarkSixDTO;
import com.xy.hk.marksix.service.IMarkSixService;
import com.xy.hk.marksix.web.form.MarkSixForm;
import com.xy.pk10.bjpk10.BjPk10Constants;
import com.xy.pk10.jsft.model.dto.JsftDTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public class MarkSixAction extends BaseDispatchAction{
	private final IMarkSixService markSixService = (IMarkSixService) getService("markSixService");
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

		MarkSixForm sixForm = (MarkSixForm) form;
		String startDate = sixForm.getStartDate();
		String endDate = sixForm.getEndDate();
		String sessionNo = sixForm.getSessionNo();
		String status= sixForm.getStatus();
		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
			hqls.append(" and ho.endTime < ? ");
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.DAY_OF_MONTH, 5);
			para.add(DateTimeUtil.DateToStringAll(nowTime.getTime()));
//			hqls.append(" and ho.openStatus = ? ");
//			para.add(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT);
			hqls.append(" order by ho.sessionNo desc ");
			PaginationSupport ps = markSixService.findMarkSixGaSessionList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
//			if(){
//				
//			}
			request.setAttribute("list", list);
			request.setAttribute("status", status);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			sixForm.setStartIndex(startIndex + "");
//		}

		// request.setAttribute("storeProductForm", productForm);
		return mapping.findForward("init");
	}
	

	/**
	 * 保存开奖结果
	 */
	public void openResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String openResult = ParamUtils.getParameter(request, "openResult");
		String flag = "success";
		String message="";

		if(ParamUtils.chkString(openResult)){
			MarkSixGaSession session = (MarkSixGaSession)markSixService.getObject(MarkSixGaSession.class, sessionId);
			if(session!=null&&session.getOpenStatus().equals(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT)){
				if(ParamUtils.chkString(session.getOpenResult())){
					message="已保存";
					flag = "success";
				}else{
					boolean result2=markSixService.saveAndOpenResult(session,openResult);
					if(result2){
						message="保存成功";
						flag = "success";
					}else{
						message="保存出错";
						flag = "false";
					}
				}		
			}else{
				message="期号不存在或者已开奖";
				flag = "false";
			}

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

		MarkSixForm markSixForm = (MarkSixForm) form;
		String startDate = markSixForm.getStartDate();
		String endDate = markSixForm.getEndDate();
		String sessionNo = markSixForm.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and se.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		if (ParamUtils.chkString(startDate)){
			hqls.append(" and se.startTime >= ? ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)){
			hqls.append(" and se.endTime <= ? ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		User user = getUser(request);
		
		PaginationSupport ps = markSixService.findMarkSixGaBetList(hqls.toString(), para,
					startIndex, pageSize);
		List list = new ArrayList();
		if(ps != null){
			list = ps.getItems();
		}
		
		HQUtils hq2 = new HQUtils();
		hq2.addHsql("select new com.xy.hk.marksix.model.dto.MarkSixDTO(sum(ho.totalPoint),sum(ho.winCash),sum(ho.totalPoint)-sum(ho.winCash)) from MarkSixGaBet ho where 1=1 ");
		if (ParamUtils.chkString(startDate)){
			hq2.addHsql(" and ho.startTime >= ? ");
			hq2.addPars(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)){
			hq2.addHsql(" and ho.endTime <= ? ");
			hq2.addPars(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		MarkSixDTO dto = (MarkSixDTO) markSixService.getObject(hq2);
		markSixForm.setMarSixDto(dto);
		
		request.setAttribute("list", list);
		request.setAttribute("sessionNo", sessionNo);
	    request.setAttribute("endDate", endDate);
		request.setAttribute("startDate", startDate);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		markSixForm.setStartIndex(startIndex + "");
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

		MarkSixForm markSixForm = (MarkSixForm) form;
		String userName = markSixForm.getUserName();
		String sessionNo = markSixForm.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and upper(ga.sessionNo) like ? ");
			para.add("%"+ sessionNo.trim().toUpperCase() +"%");
		}
		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
	        hqls.append(" and upper(ga.gameType) = ? ");
	        para.add(Constants.GAME_TYPE_XY_MARKSIX );
			PaginationSupport ps = markSixService.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			markSixForm.setStartIndex(startIndex + "");
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
        String betDetailId = request.getParameter("betDetailId");
        
		if(ParamUtils.chkString(betDetailId)){
			hqls.append(" and upper(ga.betDetailId) like ? ");
			para.add("%" + betDetailId.trim().toUpperCase() + "%");
		}
		List<MarkSixDTO> list = new ArrayList();
		list = markSixService.findGaBetDetailById(hqls.toString(), para);
		MarkSixDTO markSix = new MarkSixDTO();
        if(list != null && list.size() > 0){
        	markSix = list.get(0);
        }
		
		request.setAttribute("item", markSix);
		return mapping.findForward("betDetailManager");
	}

	/**
	 * 初始化全年期号。
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws NoFunctionPermissionException
	 */
	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "success";
		flag=markSixService.updateInitSession();
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 管理六合彩投注及开奖日期。
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws NoFunctionPermissionException
	 */
	public ActionForward dateManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		MarkSixForm sixForm = (MarkSixForm) form;
		
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
			hqls.append(" and ho.endTime < ? ");
			Calendar nowTime = Calendar.getInstance();
			nowTime.add(Calendar.DAY_OF_MONTH, 5);
			para.add(DateTimeUtil.DateToStringAll(nowTime.getTime()));
//			para.add(DateTimeUtil.DateToStringAll(new Date()));
			hqls.append(" and ho.openStatus = ? ");
			para.add(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT);
			hqls.append(" order by ho.sessionNo desc ");
			PaginationSupport ps = markSixService.findMarkSixGaSessionList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();

			request.setAttribute("list", list);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			sixForm.setStartIndex(startIndex + "");
//		}
		return mapping.findForward("dateManager");
	}
	
	/**
	 * 保存对投注、开奖日期的修改。
	 */
	public void modifyDate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String startTime = ParamUtils.getParameter(request, "startTime");
		String endTime = ParamUtils.getParameter(request, "endTime");
		String flag = "false";
		String message="";
		boolean result = false;
		MarkSixGaSession session = (MarkSixGaSession)markSixService.getObject(MarkSixGaSession.class, sessionId);
		if(session!=null&&session.getOpenStatus().equals(MarkSixConstants.MARKSIX_OPEN_STATUS_INIT)){
			if(ParamUtils.chkString(startTime)){
				session.setStartTime(DateTimeUtil.parse(startTime));
				result = true;
			}
			if(ParamUtils.chkString(endTime)){
				session.setEndTime(DateTimeUtil.parse(endTime));
			    result=markSixService.modifyDate(session,endTime);
			}
			markSixService.saveObject(session, null);
			
			if(result){
				message="保存成功";
				flag = "success";
			}else{
				message="保存出错";
				flag = "false&";
			}	
		}else{
			message="期号不存在或者已开奖";
			flag = "false";
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
		MarkSixGaSession session = (MarkSixGaSession)markSixService.getObject(MarkSixGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_XY_MARKSIX);
			if(result){	
				session.setOpenStatus("3");//退款
				gaService.saveObject(session, null);
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
				MarkSixGaSession session = (MarkSixGaSession)userService.getObject(MarkSixGaSession.class, sessionId);
				if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
					boolean result = markSixService.saveRevokePrize(session);
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
