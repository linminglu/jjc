package com.xy.pk10.sfpk102.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.xy.pk10.jsft.model.dto.JsftDTO;
import com.xy.pk10.sfpk102.SfPk102Constants;
import com.xy.pk10.sfpk102.model.SfPk102GaBet;
import com.xy.pk10.sfpk102.model.SfPk102GaSession;
import com.xy.pk10.sfpk102.model.dto.SfPk102DTO;
import com.xy.pk10.sfpk102.service.ISfPk102Service;
import com.xy.pk10.sfpk102.web.form.SfPk102Form;
import com.xy.ssc.cqssc.model.CqSscGaSession;



public class SfPk102Action extends BaseDispatchAction {
	private final ISfPk102Service sfPk102Service = (ISfPk102Service) getService("sfPk102Service");
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

		SfPk102Form sfPk102Form = (SfPk102Form) form;
		String startDate = sfPk102Form.getStartDate();
		String endDate = sfPk102Form.getEndDate();
		String sessionNo = sfPk102Form.getSessionNo();
		String status=sfPk102Form.getStatus();
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		SfPk102GaSession session = sfPk102Service.getCurrentSession();
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
		hqls.append(" order by ho.sessionNo desc ");
		PaginationSupport ps = sfPk102Service.findSfPk102GaSessionList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("status", status);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		sfPk102Form.setStartIndex(startIndex + "");
		return mapping.findForward("init");
	}
	

	/**
	 * 保存开奖结果
	 */
	public void saveOpenResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String openResult = ParamUtils.getParameter(request, "openResult");
		
		SfPk102GaSession session = (SfPk102GaSession)sfPk102Service.getObject(SfPk102GaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenStatus().equals(SfPk102Constants.SF_PK102_OPEN_STATUS_INIT)){
			if(ParamUtils.chkString(session.getOpenResult())){
				message="已保存";
				flag = "success";
			}else{
				boolean result=sfPk102Service.saveOpenResult(session,openResult);
				if(result){
					message="保存成功";
					flag = "success";
				}else{
					message="保存出错";
					flag = "false&";
				}
			}		
		}else{
			message="期号不存在或者已开奖";
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 保存并开奖
	 */
	public void openResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		String openResult = ParamUtils.getParameter(request, "openResult");
		SfPk102GaSession session = (SfPk102GaSession)sfPk102Service.getObject(SfPk102GaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenStatus().equals(SfPk102Constants.SF_PK102_OPEN_STATUS_INIT)){
			boolean result=sfPk102Service.saveAndOpenResult(session,openResult);
			if(result){
				message="开奖成功";
				flag = "success";
			}else{
				message="开奖失败";
				flag = "false&";
			}
		}else{
			message="期号不存在或者已开奖";
			flag = "false";
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	
	/**
	 * 列表
	 */
	public ActionForward countSession(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		SfPk102Form sfPk102Form = (SfPk102Form) form;
		String startDate = sfPk102Form.getStartDate();
		String endDate = sfPk102Form.getEndDate();
		String sessionNo = sfPk102Form.getSessionNo();
		String status=sfPk102Form.getStatus();
//		if (ParamUtils.chkString(startDate)) {
//			hqls.append(" and ho.endTime>=?");
//			para.add(startDate));
//		}
//		if (ParamUtils.chkString(endDate)) {
//			hqls.append(" and ho.endTime<=?");
//			para.add(endDate);
//		}
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
//			hqls.append(" and ho.endTime< ? ");
//			para.add(DateTimeUtil.DateToStringAll(new Date()));
			hqls.append(" and ho.openStatus = ? ");
			para.add(SfPk102Constants.SF_PK102_OPEN_STATUS_OPENED);
			hqls.append(" order by ho.sessionNo desc ");
			PaginationSupport ps = sfPk102Service.findSfPk102GaSessionList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
//			if(){
//				
//			}
			request.setAttribute("list", list);
			request.setAttribute("status", status);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			sfPk102Form.setStartIndex(startIndex + "");
//		}

		// request.setAttribute("storeProductForm", productForm);
		return mapping.findForward("init");
	}
	

	public ActionForward betList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		SfPk102Form sfPk102Form = (SfPk102Form) form;
		String sessionNo = sfPk102Form.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		User user = getUser(request);
		String userType = user.getUserType();
		hqls.append(" order by ho.sessionId desc ");
		PaginationSupport ps = sfPk102Service.findSfPk102GaBetList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		
		HQUtils hq2 = new HQUtils();
		
		hq2.addHsql("select new com.xy.pk10.sfpk102.model.dto.SfPk102DTO(sum(ho.totalPoint),sum(ho.winCash),sum(ho.totalPoint)-sum(ho.winCash)) from SfPk102GaBet ho where 1=1 ");
		SfPk102DTO dto = (SfPk102DTO) sfPk102Service.getObject(hq2);
		sfPk102Form.setSfPk102Dto(dto);
		
		request.setAttribute("list", list);
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		sfPk102Form.setStartIndex(startIndex + "");
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

		SfPk102Form sfPk102Form = (SfPk102Form) form;
		String userName = sfPk102Form.getUserName();
		String sessionNo = sfPk102Form.getSessionNo();

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
	        para.add( Constants.GAME_TYPE_XY_SFPK102 );
			PaginationSupport ps = sfPk102Service.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			sfPk102Form.setStartIndex(startIndex + "");
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
		List<SfPk102DTO> list = new ArrayList();
		list = sfPk102Service.findGaBetDetailById(hqls.toString(), para);
		SfPk102DTO sfPk10 = new SfPk102DTO();
        if(list != null && list.size() > 0){
        	sfPk10 = list.get(0);
        }
		
		request.setAttribute("item", sfPk10);
		return mapping.findForward("betDetailManager");

	}
	
	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String sessionNo = ParamUtils.getParameter(request, "sessionNo");
		
		String flag = "success";
		flag=sfPk102Service.updateInitSession(0);
		if(flag.equals("success")||flag.equals("inited")){
			sfPk102Service.updateInitSession(1);
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
		SfPk102GaSession session = (SfPk102GaSession)sfPk102Service.getObject(SfPk102GaSession.class, sessionId);
		String flag = "success";
		if(session!=null&&session.getOpenStatus().equals(SfPk102Constants.SF_PK102_OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_XY_SFPK102);
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
				SfPk102GaSession session = (SfPk102GaSession)userService.getObject(SfPk102GaSession.class, sessionId);
				if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
					boolean result = sfPk102Service.saveRevokePrize(session);
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
