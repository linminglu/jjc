package com.gf.bjkl8.web.action;

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
import com.gf.bjkl8.BjKl8Constants;
import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.dto.GfBjKl8DTO;
import com.gf.bjkl8.service.IBjKl8Service;
import com.gf.bjkl8.web.form.BjKl8Form;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class BjKl8Action extends BaseDispatchAction{
	private final IBjKl8Service gfBjKl8Service = (IBjKl8Service) getService("gfBjKl8Service");
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

		BjKl8Form cqSscForm = (BjKl8Form) form;
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
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
			hqls.append(" and ho.endTime< ? ");
			para.add(DateTimeUtil.DateToStringAll(new Date()));
			hqls.append(" and ho.openStatus = ? ");
			para.add(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT);
			hqls.append(" order by ho.sessionNo desc ");
			PaginationSupport ps = gfBjKl8Service.findBjKl8GaSessionList(hqls.toString(), para,
					startIndex, pageSize);
			List list = ps.getItems();
//			if(){
//				
//			}
			request.setAttribute("list", list);
			request.setAttribute("status", status);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			cqSscForm.setStartIndex(startIndex + "");
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
	
		GfBjKl8GaSession session = (GfBjKl8GaSession)gfBjKl8Service.getObject(GfBjKl8GaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenResult().equals(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT)){
			if(ParamUtils.chkString(session.getOpenResult())){
				message="已保存";
				flag = "success";
			}else{
				boolean result=gfBjKl8Service.saveOpenResult(session,openResult);
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
		GfBjKl8GaSession session = (GfBjKl8GaSession)gfBjKl8Service.getObject(GfBjKl8GaSession.class, sessionId);
		String flag = "success";
		String message="";
		if(session!=null&&session.getOpenStatus().equals(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT)){
			boolean result=gfBjKl8Service.saveAndOpenResult(session,openResult);
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

		BjKl8Form bjLu28Form = (BjKl8Form) form;
//		String startDate = bjLu28Form.getStartDate();
//		String endDate = bjLu28Form.getEndDate();
		String sessionNo = bjLu28Form.getSessionNo();

		if (ParamUtils.chkString(sessionNo)) {
			hqls.append(" and se.sessionNo like ?");
			para.add("%"+sessionNo+"%");
		}
//		if (ParamUtils.chkString(startDate)){
//			hqls.append(" and se.startTime >= ? ");
//			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
//		}
//		if (ParamUtils.chkString(endDate)){
//			hqls.append(" and se.endTime <= ? ");
//			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
//		}
		User user = getUser(request);
		String userType = user.getUserType();
//		if (userType.equals(Constants.USER_TYPE_ADMIN)) {
//			hqls.append(" and ho.betTime< ? ");
//			para.add(DateTimeUtil.DateToStringAll(new Date()));
//			hqls.append(" group by ho.sessionId order by ho.sessionId desc ");
			
			PaginationSupport ps = gfBjKl8Service.findBjKl8GaBetList(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
//			request.setAttribute("endDate", endDate);
//			request.setAttribute("startDate", startDate);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			bjLu28Form.setStartIndex(startIndex + "");
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

		BjKl8Form bjLu28Form = (BjKl8Form) form;
		String userName = bjLu28Form.getUserName();
		String sessionNo = bjLu28Form.getSessionNo();

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
	        hqls.append(" and upper(ga.gameType) = ? ");
	        para.add(Constants.GAME_TYPE_GF_BJKL8);
			PaginationSupport ps = gfBjKl8Service.findGaBetDetail(hqls.toString(), para,
					startIndex, pageSize);
			List list = new ArrayList();
			if(ps != null){
				list = ps.getItems();
			}
			request.setAttribute("list", list);
			request.setAttribute("sessionNo", sessionNo);
			request.setAttribute("endDate", userName);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
			bjLu28Form.setStartIndex(startIndex + "");
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
		List<GfBjKl8DTO> list = new ArrayList();
		list = gfBjKl8Service.findGaBetDetailById(hqls.toString(), para);
		GfBjKl8DTO bjLu28 = new GfBjKl8DTO();
        if(list != null && list.size() > 0){
        	bjLu28 = list.get(0);
        }
		
		request.setAttribute("item", bjLu28);
		return mapping.findForward("betDetailManager");
	}

	public void initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String sessionNo = ParamUtils.getParameter(request, "sessionNo");
		String flag = "success";
//		String message="";
		flag=gfBjKl8Service.updateInitTodaySession(sessionNo);
		if(flag.equals("success")||flag.equals("inited")){
			gfBjKl8Service.updateInitSession();
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
		GfBjKl8GaSession session = (GfBjKl8GaSession)gfBjKl8Service.getObject(GfBjKl8GaSession.class, sessionId);
		String flag = "success";

		if(session!=null&&session.getOpenStatus().equals(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT)){
			boolean result=gaService.saveDrawback(session.getSessionId(),Constants.GAME_TYPE_GF_BJKL8);
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
}
