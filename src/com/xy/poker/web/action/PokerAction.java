package com.xy.poker.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xy.pk10.bjpk10.BjPk10Constants;
import com.xy.pk10.jsft.model.dto.JsftDTO;
import com.xy.poker.PokerConstants;
import com.xy.poker.model.PokerGaBet;
import com.xy.poker.model.PokerGaSession;
import com.xy.poker.model.dto.PokerDTO;
import com.xy.poker.service.IPokerService;
import com.xy.poker.web.form.PokerForm;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public class PokerAction extends BaseDispatchAction{
	private final IPokerService pokerService = (IPokerService) getService("pokerService");
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

		PokerForm cqSscForm = (PokerForm) form;
		String startDate = cqSscForm.getStartDate();
		String endDate = cqSscForm.getEndDate();
		String sessionNo = cqSscForm.getSessionNo();
		String status= cqSscForm.getStatus();
		
		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		
		User user = getUser(request);
		String userType = user.getUserType();
		PokerGaSession session = pokerService.getCurrentSession();
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
		PaginationSupport ps = pokerService.findPokerGaSessionList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("status", status);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		cqSscForm.setStartIndex(startIndex + "");
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
			String oResult = ""; //转换后的开奖结果
			String[] result = openResult.split(",");
			Map<String, String> map = new HashMap<String, String>();
			for(int i = 1; i<=13; i++){
				map.put(i+"", i+"");
			}
			map.put("A", "A");
			map.put("J", "J");
			map.put("Q", "Q");
			map.put("K", "K");
			for(String s : result){
				String[] one = s.split("_");
				if(map.get(one[1].toUpperCase()) == null){
					flag = "false";
					break;
				}
				if(one[1].equalsIgnoreCase("J")){
					one[1] = "11";
				}else if(one[1].equalsIgnoreCase("Q")){
					one[1] = "12";
				}else if(one[1].equalsIgnoreCase("K")){
					one[1] = "13";
				}else if(one[1].equalsIgnoreCase("A")){
					one[1] = "01";
				}else{
					if(Integer.parseInt(one[1])< 10){
						one[1] = "0"+one[1];
					}
				}
				oResult = oResult + one[0] + one[1] + ",";
			}
			oResult = oResult.substring(0,oResult.length()-1);
			PokerGaSession session = (PokerGaSession)pokerService.getObject(PokerGaSession.class, sessionId);
			if(session!=null&&session.getOpenStatus().equals(PokerConstants.POKER_OPEN_STATUS_INIT)){
				if(ParamUtils.chkString(session.getOpenResult())){
					message="已保存";
					flag = "success";
				}else{
					boolean result2=pokerService.saveAndOpenResult(session,oResult);
					if(result2){
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

		PokerForm pokerForm = (PokerForm) form;
		String sessionNo = pokerForm.getSessionNo();
		String startDate = pokerForm.getStartDate();
		String endDate = pokerForm.getEndDate();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and ho.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
		if (ParamUtils.chkString(startDate)){
			hqls.append(" and ho.startTime >= ? ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)){
			hqls.append(" and ho.endTime <= ? ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		User user = getUser(request);
		hqls.append(" order by ho.sessionId desc ");
		PaginationSupport ps = pokerService.findPokerGaBetList(hqls.toString(), para,
					startIndex, pageSize);
		List list = new ArrayList();
		if(ps != null){
			list = ps.getItems();
		}
		
		HQUtils hq2 = new HQUtils();
		hq2.addHsql("select new com.xy.poker.model.dto.PokerDTO(sum(ho.totalPoint),sum(ho.winCash),sum(ho.totalPoint)-sum(ho.winCash)) from PokerGaBet ho where 1=1 ");
		if (ParamUtils.chkString(startDate)){
			hq2.addHsql(" and ho.startTime >= ? ");
			hq2.addPars(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)){
			hq2.addHsql(" and ho.endTime <= ? ");
			hq2.addPars(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		PokerDTO dto = (PokerDTO) pokerService.getObject(hq2);
		pokerForm.setPokerDto(dto);
		
		request.setAttribute("list", list);
		request.setAttribute("sessionNo", sessionNo);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		pokerForm.setStartIndex(startIndex + "");
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

		PokerForm pokerForm = (PokerForm) form;
		String userName = pokerForm.getUserName();
		String sessionNo = pokerForm.getSessionNo();

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
	        para.add(Constants.GAME_TYPE_XY_POKER);
			PaginationSupport ps = pokerService.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			pokerForm.setStartIndex(startIndex + "");
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
		List<PokerDTO> list = new ArrayList();
		list = pokerService.findGaBetDetailById(hqls.toString(), para);
		PokerDTO poker = new PokerDTO();
        if(list != null && list.size() > 0){
        	poker = list.get(0);
        }
		
		request.setAttribute("item", poker);
		return mapping.findForward("betDetailManager");
	}

	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "success";
//		String message="";
		flag=pokerService.updateInitTodaySession();
		if(flag.equals("success")||flag.equals("inited")){
			pokerService.updateInitSession();
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
		PokerGaSession session = (PokerGaSession)pokerService.getObject(PokerGaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenResult().equals(BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_XY_POKER);
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
				PokerGaSession session = (PokerGaSession)userService.getObject(PokerGaSession.class, sessionId);
				if(session!=null&&session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
					boolean result = pokerService.saveRevokePrize(session);
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
