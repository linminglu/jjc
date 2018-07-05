package com.game.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;
import com.game.model.dto.GaK3DTO;
import com.game.service.IGaK3Service;
import com.game.web.form.GaK3Form;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 * 快三
 * 
 * @author ch
 * 
 */
public class GaK3Action extends BaseDispatchAction {
	private final IGaK3Service gaK3Service = (IGaK3Service) getService("gaK3Service");

	/**
	 * 历史开奖结果
	 */
	public ActionForward initLotteryResults(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaK3Form gaK3Form = (GaK3Form) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String sessionNoStart = gaK3Form.getSessionNoStart();
		String sessionNoEnd = gaK3Form.getSessionNoEnd();
		String startTime = gaK3Form.getStartTime();
		String endTime = gaK3Form.getEndTime();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and gks.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and gks.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		if(ParamUtils.chkString(sessionNoStart)){
			hqls.append(" and gks.sessionNo >= ?");
			para.add(Integer.parseInt(sessionNoStart));
		}
		if(ParamUtils.chkString(sessionNoEnd)){
			hqls.append(" and gks.sessionNo <= ?");
			para.add(Integer.parseInt(sessionNoEnd));
		}
		hqls.append(" order by gks.sessionId desc");
		PaginationSupport ps = gaK3Service.findLotteryResultList(hqls.toString(), para,
				startIndex, pageSize);
		List<GaK3Session> list = ps.getItems();		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaK3Form.setStartIndex(startIndex + "");
		return mapping.findForward("initLotteryResults");
	}
	/**
	 * 会员投注查询
	 */
	public ActionForward initBetList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaK3Form gaK3Form = (GaK3Form) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String startTime = gaK3Form.getStartTime();
		String endTime = gaK3Form.getEndTime();
		String loginName = gaK3Form.getLoginName();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and gks.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and gks.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		PaginationSupport ps = null;
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
//			hqls.append(" and go.type = '2' and go.payStatus = '1'");
			hqls.append(" order by gks.sessionId desc");
			ps = gaK3Service.findOneUserBetList(hqls.toString(), para,
					startIndex, pageSize);
			List<GaK3DTO> items = ps.getItems();
			List<GaK3Session> list = new ArrayList<GaK3Session>();
			if(items != null && items.size()>0){
				for (GaK3DTO dto : items) {
					GaK3Session session = new GaK3Session();
					session.setSessionId(dto.getGaK3Session().getSessionId());
					session.setSessionNo(dto.getGaK3Session().getSessionNo());
					session.setUserCount(dto.getGaK3Bet().getTotalNum());
					BigDecimal totalPoint = dto.getGaK3Bet().getTotalPoint();
					if(totalPoint != null){
						int tPoint=totalPoint.intValue();
						session.setPointCount(tPoint);
					}
					list.add(session);
				}
			}
			request.setAttribute("list", list);
		}else{
			hqls.append(" order by gks.sessionId desc");
			ps = gaK3Service.findBetList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
			request.setAttribute("list", list);
		}
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaK3Form.setStartIndex(startIndex + "");		
		request.setAttribute("gaK3Form", gaK3Form);
		return mapping.findForward("initBetList");
	}
	/**
	 * 投注所有用户
	 */
	public ActionForward betUserList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		GaK3Form gaK3Form = (GaK3Form) form;
		String loginName = gaK3Form.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and gkb.sessionId = ?");
			para.add(sessionId);
		}
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
		}
//		hqls.append(" and go.type = '2' and go.payStatus = '1'");
		hqls.append(" order by gks.sessionId desc");
		PaginationSupport ps = gaK3Service.findBetUserList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaK3Form.setStartIndex(startIndex + "");
		return mapping.findForward("betUserList");
	}
	/**
	 * 投注详情
	 */
	public ActionForward betDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		Integer sessionId = ParamUtils.getIntegerParameter(request, "sessionId");
		Integer userId = ParamUtils.getIntegerParameter(request, "id");
		Integer betId = ParamUtils.getIntegerParameter(request, "betId");
		String loginName2 = ParamUtils.getParameter(request, "loginName");
		GaK3Form gaK3Form = (GaK3Form) form;
		String loginName = gaK3Form.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and gkbd.sessionId = ?");
			para.add(sessionId);
		}
		if(ParamUtils.chkInteger(userId)){
			hqls.append(" and gkbd.userId = ?");
			para.add(userId);
		}
		if(ParamUtils.chkInteger(betId)){
			hqls.append(" and gkb.betId = ?");
			para.add(betId);
		}
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
		}else{
			request.setAttribute("action2", "1");
		}
		if(ParamUtils.chkString(loginName2)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName2);
		}
		hqls.append(" order by gkbd.betTime desc");
		PaginationSupport ps = gaK3Service.findBetDetailList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaK3Form.setStartIndex(startIndex + "");
		return mapping.findForward("betDetail");
	}
	/**
	 * 订单列表
	 */
	public ActionForward initOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaK3Form gaK3Form = (GaK3Form) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String startTime = gaK3Form.getStartTime();
		String endTime = gaK3Form.getEndTime();
		String orderNum = gaK3Form.getOrderNum();
		String loginName = gaK3Form.getLoginName();
		String payStatus = gaK3Form.getPayStatus();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and go.createTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and go.createTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		if(ParamUtils.chkString(orderNum)){
			hqls.append(" and go.orderNum = ?");
			para.add(orderNum);
		}
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
			request.setAttribute("loginName", loginName);
		}
		if(ParamUtils.chkString(payStatus)){
			hqls.append(" and go.payStatus = ?");
			para.add(payStatus);
			request.setAttribute("payStatus", payStatus);
		}
		hqls.append(" and go.type = '2'");
		hqls.append(" order by go.createTime desc");
		PaginationSupport ps = gaK3Service.findOrderList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaK3Form.setStartIndex(startIndex + "");
		return mapping.findForward("initOrder");
	}
	/**
	 * 订单详情
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer oid = ParamUtils.getIntegerParameter(request, "id");
		GaOrder gaOrder = (GaOrder)gaK3Service.getObject(GaOrder.class, oid);
		Integer userId = gaOrder.getUserId();
		String userName = null;
		if(ParamUtils.chkInteger(userId)){
			User user = (User)gaK3Service.getObject(User.class, userId);
			if(user != null){
				userName = user.getUserName();
			}
		}
		List list = gaK3Service.findOrderView(oid);
		request.setAttribute("userName", userName);
		request.setAttribute("gaOrder", gaOrder);
		request.setAttribute("list", list);
		return mapping.findForward("view");
	}
	

}
