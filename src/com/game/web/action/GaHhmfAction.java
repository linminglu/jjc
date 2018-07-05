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
import com.game.model.GaHhmfBetOption;
import com.game.model.GaHhmfSession;
import com.game.model.dto.GaHhmfDTO;
import com.game.service.IGaHhmfService;
import com.game.web.form.GaHhmfForm;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 *黑红梅方
 * 
 * @author ch
 * 
 */
public class GaHhmfAction extends BaseDispatchAction {
	private final IGaHhmfService gaHhmfService = (IGaHhmfService) getService("gaHhmfService");

	/**
	 * 历史开奖结果
	 */
	public ActionForward initLotteryResults(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String sessionNoStart = gaHhmfForm.getSessionNoStart();
		String sessionNoEnd = gaHhmfForm.getSessionNoEnd();
		String startTime = gaHhmfForm.getStartTime();
		String endTime = gaHhmfForm.getEndTime();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and ghs.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and ghs.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		if(ParamUtils.chkString(sessionNoStart)){
			hqls.append(" and ghs.sessionNo >= ?");
			para.add(Integer.parseInt(sessionNoStart));
		}
		if(ParamUtils.chkString(sessionNoEnd)){
			hqls.append(" and ghs.sessionNo <= ?");
			para.add(Integer.parseInt(sessionNoEnd));
		}
		hqls.append(" order by ghs.sessionId desc");
		PaginationSupport ps = gaHhmfService.findLotteryResultList(hqls.toString(), para,
				startIndex, pageSize);
		List<GaHhmfSession> list = ps.getItems();
		if(list != null && list.size() > 0){
			for (GaHhmfSession session : list) {
				String openResult = session.getOpenResult();
				if(ParamUtils.chkString(openResult) && !openResult.equals("55")){
					String[] result = openResult.split("-");
					if(result.length > 1){
						String colour = result[0];
						String number = result[1];
						session.setColour(colour);
						session.setNumber(number);
					}
				}
				if(ParamUtils.chkString(openResult) && openResult.equals("55")){//王
					session.setColour("5");
				}
			}
		}
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaHhmfForm.setStartIndex(startIndex + "");
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
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String startTime = gaHhmfForm.getStartTime();
		String endTime = gaHhmfForm.getEndTime();
		String loginName = gaHhmfForm.getLoginName();
		if (ParamUtils.chkString(startTime)) {
			hqls.append(" and ghs.startTime >= ?");
			para.add(DateTimeUtil.parse(startTime + " 00:00:00"));
		}
		if (ParamUtils.chkString(endTime)) {
			hqls.append(" and ghs.startTime <= ?");
			para.add(DateTimeUtil.parse(endTime + " 23:59:59"));
		}
		PaginationSupport ps = null;
		if(ParamUtils.chkString(loginName)){
			hqls.append(" and u.loginName = ?");
			para.add(loginName);
			hqls.append(" order by ghs.sessionId desc");
			ps = gaHhmfService.findOneUserBetList(hqls.toString(), para,
					startIndex, pageSize);
			List<GaHhmfDTO> items = ps.getItems();
			List<GaHhmfSession> list = new ArrayList<GaHhmfSession>();
			if(items != null && items.size()>0){
				for (GaHhmfDTO dto : items) {
					GaHhmfSession session = new GaHhmfSession();
					session.setSessionId(dto.getGaHhmfSession().getSessionId());
					session.setSessionNo(dto.getGaHhmfSession().getSessionNo());
					session.setUserCount(dto.getGaHhmfBet().getTotalNum());
					BigDecimal totalPoint = dto.getGaHhmfBet().getTotalPoint();
					if(totalPoint != null){
						int tPoint=totalPoint.intValue();
						session.setPointCount(tPoint);
					}
					list.add(session);
				}
			}
			request.setAttribute("list", list);
		}else{
			hqls.append(" order by ghs.sessionId desc");
			ps = gaHhmfService.findBetList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
			request.setAttribute("list", list);
		}
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaHhmfForm.setStartIndex(startIndex + "");		
		request.setAttribute("gaHhmfForm", gaHhmfForm);
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
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		String loginName = gaHhmfForm.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and ghb.sessionId = ?");
			para.add(sessionId);
		}
		
		hqls.append(" order by ghs.sessionId desc");
		PaginationSupport ps = gaHhmfService.findBetUserList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaHhmfForm.setStartIndex(startIndex + "");
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
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		String loginName = gaHhmfForm.getLoginName();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(sessionId)){
			hqls.append(" and ghbd.sessionId = ?");
			para.add(sessionId);
		}
		if(ParamUtils.chkInteger(userId)){
			hqls.append(" and ghbd.userId = ?");
			para.add(userId);
		}
		if(ParamUtils.chkInteger(betId)){
			hqls.append(" and ghb.betId = ?");
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
		hqls.append(" order by ghbd.betTime desc");
		PaginationSupport ps = gaHhmfService.findBetDetailList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaHhmfForm.setStartIndex(startIndex + "");
		return mapping.findForward("betDetail");
	}
	/**
	 * 赔率列表
	 */
	public ActionForward initBetOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		hqls.append(" order by ghbo.sort desc");
		PaginationSupport ps = gaHhmfService.findBetOptionList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaHhmfForm.setStartIndex(startIndex + "");
		return mapping.findForward("initBetOption");
	}
	/**
	 * 修改赔率
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		Integer betOptionId = ParamUtils.getIntegerParameter(request, "id");
		if(ParamUtils.chkInteger(betOptionId)){
			GaHhmfBetOption option = (GaHhmfBetOption)gaHhmfService.getObject(GaHhmfBetOption.class, betOptionId);
			gaHhmfForm.setGaHhmfBetOption(option);
		}
		return mapping.findForward("preModify");
	}
	/**
	 * 保存赔率
	 */
	public ActionForward saveBetOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		GaHhmfForm gaHhmfForm = (GaHhmfForm) form;
		GaHhmfBetOption option = gaHhmfForm.getGaHhmfBetOption();
		Integer betOptionId = option.getBetOptionId();
		if(ParamUtils.chkInteger(betOptionId)){
			GaHhmfBetOption betOption = (GaHhmfBetOption)gaHhmfService.getObject(GaHhmfBetOption.class, betOptionId);
			betOption.setBetRate(option.getBetRate());
			gaHhmfService.saveObject(betOption, null);
		}
		return mapping.findForward("saveBetOption");
	}
	/**
	 * 排序
	 */
	public ActionForward sort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		gaHhmfService.updateSort(id, flag);
		return initBetOption(mapping, form, request, response);
	}
	

}
