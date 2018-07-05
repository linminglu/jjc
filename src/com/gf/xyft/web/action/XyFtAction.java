package com.gf.xyft.web.action;

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
import com.apps.util.JsonUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.gf.bjpk10.model.dto.GfBjPk10DTO;
import com.gf.pick11.gdpick11.model.GfGdPick11GaSession;
import com.gf.xyft.XyFtConstants;
import com.gf.xyft.model.GfXyFtGaSession;
import com.gf.xyft.model.dto.GfXyFtDTO;
import com.gf.xyft.service.IXyFtService;
import com.gf.xyft.web.form.XyFtForm;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class XyFtAction extends BaseDispatchAction{
	private final IXyFtService gfXyFtService = (IXyFtService) getService("gfXyFtService");
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

		XyFtForm jsK3Form = (XyFtForm) form;
		String startDate = jsK3Form.getStartDate();
		String endDate = jsK3Form.getEndDate();
		String sessionNo = jsK3Form.getSessionNo();
		String status= jsK3Form.getStatus();
		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
			hqls.append(" and ho.endTime< ? ");
			para.add(DateTimeUtil.DateToStringAll(new Date()));
			hqls.append(" and ho.openStatus = ? ");
			para.add(XyFtConstants.XY_FT_OPEN_STATUS_INIT);
			hqls.append(" order by ho.sessionNo desc ");
			PaginationSupport ps = gfXyFtService.findGfXyFtGaSessionList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
//			if(){
//				
//			}
			request.setAttribute("list", list);
			request.setAttribute("status", status);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			jsK3Form.setStartIndex(startIndex + "");
//		}

		// request.setAttribute("storeProductForm", productForm);
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
	
		GfXyFtGaSession session = (GfXyFtGaSession)gfXyFtService.getObject(GfXyFtGaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenResult().equals(XyFtConstants.XY_FT_OPEN_STATUS_INIT)){
			if(ParamUtils.chkString(session.getOpenResult())){
				message="已保存";
				flag = "success";
			}else{
				boolean result=gfXyFtService.saveOpenResult(session,openResult);
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
		GfXyFtGaSession session = (GfXyFtGaSession)gfXyFtService.getObject(GfXyFtGaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenStatus().equals(XyFtConstants.XY_FT_OPEN_STATUS_INIT)){
			boolean result=gfXyFtService.saveAndOpenResult(session,openResult);
			if(result){
				message="开奖成功";
				flag = "success";
				
				// 获取当前用户
				User loginUser = null;
				loginUser = (User) request.getSession(false).getAttribute("loginUser");
				StringBuffer loginText = new StringBuffer();
				loginText.append("手动开奖：操作人");
				loginText.append(loginUser.getLoginName());
				loginText.append("[");
				loginText.append(loginUser.getUserId());
				loginText.append("]，给广东11选5第[");
				loginText.append(session.getSessionNo());
				loginText.append("]期手动开奖，开奖结果为[");
				loginText.append(openResult);
				loginText.append("]");
				userService.updateUserLog(request,loginUser,loginText.toString());

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
	
	public ActionForward betList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems", RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		XyFtForm xyFtForm = (XyFtForm) form;
		String startDate = xyFtForm.getStartDate();
		String endDate = xyFtForm.getEndDate();
		String sessionNo = xyFtForm.getSessionNo();

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
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
//			hqls.append(" and ho.betTime< ? ");
//			para.add(DateTimeUtil.DateToStringAll(new Date()));
//			hqls.append(" group by ho.sessionId order by ho.sessionId desc ");
			
			PaginationSupport ps = gfXyFtService.findGfXyFtGaBetList(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			HQUtils hq2 = new HQUtils();
			hq2.addHsql("select new com.gf.xyft.model.dto.GfXyFtDTO(sum(ho.money),sum(ho.winCash),sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho where 1=1 ");
			hq2.addHsql(" and ho.gameType = ?");
			hq2.addPars(Constants.GAME_TYPE_GF_XYFT);
			
			if (ParamUtils.chkString(startDate)){
				hq2.addHsql(" and ho.startTime >= ? ");
				hq2.addPars(DateTimeUtil.parse(startDate + " 00:00:00"));
			}
			if (ParamUtils.chkString(endDate)){
				hq2.addHsql(" and ho.endTime <= ? ");
				hq2.addPars(DateTimeUtil.parse(endDate + " 23:59:59"));
			}
			GfXyFtDTO dto = (GfXyFtDTO)gfXyFtService.getObject(hq2);
			xyFtForm.setGfXyftDto(dto);
			
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", endDate);
			request.setAttribute("startDate", startDate);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			xyFtForm.setStartIndex(startIndex + "");
//		}
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

		XyFtForm jsK3Form = (XyFtForm) form;
		String userName = jsK3Form.getUserName();
		String sessionNo = jsK3Form.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and upper(ga.sessionNo) like ? ");
			para.add("%"+ sessionNo.trim().toUpperCase() +"%");
		}
		if (ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.userName) like ? or upper(u.userId) like ? ) ");
			para.add("%" + userName.trim().toUpperCase() + "%");
			para.add("%" + userName.trim().toUpperCase() + "%");
		}
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
	        hqls.append(" and ga.gameType = ? ");
	        para.add(Constants.GAME_TYPE_GF_XYFT);
			PaginationSupport ps = gfXyFtService.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			jsK3Form.setStartIndex(startIndex + "");
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
		List<GfXyFtDTO> list = new ArrayList();
		list = gfXyFtService.findGaBetDetailById(hqls.toString(), para);
		GfXyFtDTO gdk10 = new GfXyFtDTO();
        if(list != null && list.size() > 0){
        	gdk10 = list.get(0);
        }
		
		request.setAttribute("item", gdk10);
		return mapping.findForward("betDetailManager");
	}
	
	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "success";
		flag=gfXyFtService.updateInitSession(0);
		if(flag.equals("success")||flag.equals("inited")){
			gfXyFtService.updateInitSession(1);
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
		GfXyFtGaSession session = (GfXyFtGaSession)gfXyFtService.getObject(GfXyFtGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(XyFtConstants.XY_FT_OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_GF_XYFT);
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
				GfXyFtGaSession session = (GfXyFtGaSession)userService.getObject(GfXyFtGaSession.class, sessionId);
				if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
					boolean result = gfXyFtService.saveRevokePrize(session);
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
