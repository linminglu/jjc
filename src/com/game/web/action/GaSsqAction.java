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
import com.game.model.GaOrder;
import com.game.model.GaSsqSession;
import com.game.model.dto.GaSsqDTO;
import com.game.service.IGaSsqService;
import com.game.web.form.GaSsqForm;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 *双色球
 * 
 * @author ch
 * 
 */
public class GaSsqAction extends BaseDispatchAction {
	private final IGaSsqService gaSsqService = (IGaSsqService) getService("gaSsqService");

	/**
	 * 历史开奖结果
	 */
	public ActionForward initLotteryResults(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaSsqForm gaSsqForm = (GaSsqForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String sessionNoStart = gaSsqForm.getSessionNoStart();
		String sessionNoEnd = gaSsqForm.getSessionNoEnd();
		String startTime = gaSsqForm.getStartTime();
		String endTime = gaSsqForm.getEndTime();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and gss.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and gss.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		if(ParamUtils.chkString(sessionNoStart)){
			hqls.append(" and gss.sessionNo >= ?");
			para.add(Integer.parseInt(sessionNoStart));
		}
		if(ParamUtils.chkString(sessionNoEnd)){
			hqls.append(" and gss.sessionNo <= ?");
			para.add(Integer.parseInt(sessionNoEnd));
		}
		hqls.append(" order by gss.sessionId desc");
		PaginationSupport ps = gaSsqService.findLotteryResultList(hqls.toString(), para,
				startIndex, pageSize);
		List<GaSsqSession> list = ps.getItems();		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaSsqForm.setStartIndex(startIndex + "");
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
		GaSsqForm gaSsqForm = (GaSsqForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String startTime = gaSsqForm.getStartTime();
		String endTime = gaSsqForm.getEndTime();
		String loginName = gaSsqForm.getLoginName();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and gss.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and gss.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		PaginationSupport ps = null;
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
			hqls.append(" and go.type = '1' and go.payStatus = '1'");
			hqls.append(" order by gss.sessionId desc");
			ps = gaSsqService.findOneUserBetList(hqls.toString(), para,
					startIndex, pageSize);
			List<GaSsqDTO> items = ps.getItems();
			List<GaSsqSession> list = new ArrayList<GaSsqSession>();
			if(items != null && items.size()>0){
				for (GaSsqDTO dto : items) {
					GaSsqSession session = new GaSsqSession();
					session.setSessionId(dto.getGaSsqSession().getSessionId());
					session.setSessionNo(dto.getGaSsqSession().getSessionNo());
					session.setUserCount(dto.getGaSsqBet().getTotalNum());
					BigDecimal totalPoint = dto.getGaSsqBet().getTotalPoint();
					if(totalPoint != null){
						int tPoint=totalPoint.intValue();
						session.setPointCount(tPoint);
					}
					list.add(session);
				}
			}
			request.setAttribute("list", list);
		}else{
			hqls.append(" order by gss.sessionId desc");
			ps = gaSsqService.findBetList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
			request.setAttribute("list", list);
		}
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaSsqForm.setStartIndex(startIndex + "");		
		request.setAttribute("gaSsqForm", gaSsqForm);
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
		GaSsqForm gaSsqForm = (GaSsqForm) form;
		String loginName = gaSsqForm.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and gsb.sessionId = ?");
			para.add(sessionId);
		}
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
		}
		hqls.append(" and go.type = '1' and go.payStatus = '1'");
		hqls.append(" order by gss.sessionId desc");
		PaginationSupport ps = gaSsqService.findBetUserList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaSsqForm.setStartIndex(startIndex + "");
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
		GaSsqForm gaSsqForm = (GaSsqForm) form;
		String loginName = gaSsqForm.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and gsbd.sessionId = ?");
			para.add(sessionId);
		}
		if(ParamUtils.chkInteger(userId)){
			hqls.append(" and gsbd.userId = ?");
			para.add(userId);
		}
		if(ParamUtils.chkInteger(betId)){
			hqls.append(" and gsb.betId = ?");
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
		hqls.append(" order by gsbd.betTime desc");
		PaginationSupport ps = gaSsqService.findBetDetailList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaSsqForm.setStartIndex(startIndex + "");
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
		GaSsqForm gaSsqForm = (GaSsqForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String startTime = gaSsqForm.getStartTime();
		String endTime = gaSsqForm.getEndTime();
		String orderNum = gaSsqForm.getOrderNum();
		String loginName = gaSsqForm.getLoginName();
		String payStatus = gaSsqForm.getPayStatus();
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
		hqls.append(" and go.type = '1'");
		hqls.append(" order by go.createTime desc");
		PaginationSupport ps = gaSsqService.findOrderList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaSsqForm.setStartIndex(startIndex + "");
		return mapping.findForward("initOrder");
	}
	/**
	 * 订单详情
	 */
	public ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer oid = ParamUtils.getIntegerParameter(request, "id");
		GaOrder gaOrder = (GaOrder)gaSsqService.getObject(GaOrder.class, oid);
		Integer userId = gaOrder.getUserId();
		String userName = null;
		if(ParamUtils.chkInteger(userId)){
			User user = (User)gaSsqService.getObject(User.class, userId);
			if(user != null){
				userName = user.getUserName();
			}
		}
		List list = gaSsqService.findOrderView(oid);
		request.setAttribute("userName", userName);
		request.setAttribute("gaOrder", gaOrder);
		request.setAttribute("list", list);
		return mapping.findForward("view");
	}
}
