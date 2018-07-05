package com.cash.web.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.model.dto.CashDTO;
import com.cash.service.ICashService;
import com.cash.web.form.CashForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.util.ExcelUtil;

public class CashAction extends BaseDispatchAction{
	private final ICashService cashService = (ICashService) getService("cashService");
	
	/**
	 * 提现记录列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		String auditStatus2 = ParamUtils.getParameter(request, "status"); //top.jsp传过来的参数。
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CashForm cashForm = (CashForm) form;
		String userName = cashForm.getUserName();
		String startDate = cashForm.getStartDate();
		String endDate = cashForm.getEndDate();
		String auditStatus = cashForm.getUserApplyCash().getAuditStatus();
//		String bindName = cashForm.getBindName();
//		String bankName = cashForm.getBankName();
//		String bindAccount = cashForm.getBindAccount();
		if(ParamUtils.chkString(userName)){
			hqls.append(" and (upper(u.loginName) = ? OR upper(u.userId) = ? )");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if(ParamUtils.chkString(startDate)){
			hqls.append(" and upper(uc.createTime) >= ? ");
			para.add(startDate.toUpperCase());
		}
		if(ParamUtils.chkString(endDate)){
			hqls.append(" and upper(uc.createTime) <= ? ");
			para.add(endDate.toUpperCase());
		}
		if(ParamUtils.chkString(auditStatus)){
			hqls.append(" and upper(uc.auditStatus) = ? ");
			para.add(auditStatus.toUpperCase());
		}
		
		if(ParamUtils.chkString(auditStatus2)){
			hqls.append(" and upper(uc.auditStatus) = ? ");
			para.add(auditStatus2.toUpperCase());
		}

//		if(ParamUtils.chkString(bindName)){
//			hqls.append(" and ub.bindName = ? ");
//			para.add(bindName);
//		}
//		if(ParamUtils.chkString(bankName)){
//			hqls.append(" and ub.bankName = ? ");
//			para.add(bankName);
//		}
//		if(ParamUtils.chkString(bindAccount)){
//			hqls.append(" and ub.bindAccount = ? ");
//			para.add(bindAccount);
//		}
		hqls.append(" order by uc.createTime desc ");
		PaginationSupport ps = cashService.findUserCashBankList(hqls.toString(), para, startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("userName", userName);
//		request.setAttribute("bindName", bindName);
//		request.setAttribute("bankName", bankName);
//		request.setAttribute("bindAccount", bindAccount);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		cashForm.setStartIndex(startIndex + "");
		return mapping.findForward("init");
	}
	/**
	 * 审核提现
	 */
	public ActionForward auditing(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		CashForm cashForm = (CashForm) form;
		
		if (ParamUtils.chkInteger(id)) {
			UserApplyCash userApplyCash = (UserApplyCash)cashService.getObject(UserApplyCash.class, id);
			cashForm.setUserApplyCash(userApplyCash);
			Integer userId = userApplyCash.getUserId();
			if(ParamUtils.chkInteger(userId)){
				User user = (User)cashService.getObject(User.class, userId);
				cashForm.setUser(user);
			}
		}
		saveToken(request);
		return mapping.findForward("auditing");
		
	}
	/**
	 * 保存审核提现
	 */
	public ActionForward saveAuditing(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		CashForm cashForm = (CashForm) form;
		Integer applyCashId = cashForm.getUserApplyCash().getApplyCashId();
		if(isTokenValid(request)){
			if(ParamUtils.chkInteger(applyCashId)){
				UserApplyCash userApplyCash = (UserApplyCash)cashService.getObject(UserApplyCash.class, applyCashId);
				String auditStatus = cashForm.getUserApplyCash().getAuditStatus();
				if(userApplyCash!=null && userApplyCash.getAuditTime()==null){//只能审核一次
					if(Constants.CASH_AUDIT_TYPE_REFUSE.equals(auditStatus)){
						String remark = cashForm.getUserApplyCash().getRemark();
						userApplyCash.setRemark(remark);
					}
					userApplyCash.setAuditStatus(auditStatus);
					userApplyCash.setAuditTime(new Date());
				}else{
					auditStatus = "-1";
				}
				
				
				User user = (User) userService.getObject(User.class, userApplyCash.getUserId());
			
				// 获取当前用户
				User loginUser = null;
				loginUser = (User) request.getSession(false).getAttribute("loginUser");
				StringBuffer loginText = new StringBuffer();
				loginText.append("提现审核：操作人");
				loginText.append(loginUser.getLoginName());
				loginText.append("[");
				loginText.append(loginUser.getUserId());
				loginText.append("]，提现申请人[");
				loginText.append(user.getLoginName());
				loginText.append("]，提现金额[");
				loginText.append(userApplyCash.getCashMoney());
				loginText.append("]，审核结果[");
	
				if(Constants.CASH_AUDIT_TYPE_REFUSE.equals(auditStatus)){
					
					cashService.updateUserApplyCash(userApplyCash);
					
					loginText.append("拒绝]");
	
				}else if(Constants.CASH_AUDIT_TYPE_PASS.equals(auditStatus)){
					cashService.updateUserApplyCashPass(userApplyCash);
					
					loginText.append("通过]");
				}else if("-1".equals(auditStatus)){
					loginText.append("失败]");
				}
				userService.updateUserLog(request,loginUser,loginText.toString());
			}
		}else{
			request.setAttribute("err", "tokenErr");
		}
		
		return mapping.findForward("saveAuditing");
		
	}
	/**
	 * 账户列表
	 */
	public ActionForward bindBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession(false).getAttribute("loginUser");
		List<UserBankBind> list=cashService.findBankBindListByUid(user.getUserId());
		request.setAttribute("list", list);
		return mapping.findForward("bindBank");
		
	}
	/**
	 * 跳转到添加账户页面
	 */
	public ActionForward preAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<SysBank> bankList = cashService.findSysBankList();
		request.setAttribute("bankList", bankList);
		return mapping.findForward("preAdd");
		
	}
	
	/**
	 * 确认默认账户
	 */
	public ActionForward saveDefBind(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		return mapping.findForward("saveDefBind");
		
	}
	
	/**
	 * 保存账户
	 */
	public ActionForward saveBindBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CashForm cashForm = (CashForm) form;
		UserBankBind bankBind = cashForm.getUserBankBind();
		Integer bankBindId = bankBind.getBankBindId();
		User user = (User) request.getSession(false).getAttribute("loginUser");
		bankBind.setUserId(user.getUserId());
		bankBind.setBindType("1");
		Integer bankId = cashForm.getBankId();
		SysBank sysBank = (SysBank)cashService.getObject(SysBank.class, bankId);
		List<UserBankBind> list=cashService.findBankBindListByUid(user.getUserId());					
		if(list!=null&list.size()>0){
			List<UserBankBind> newList=new ArrayList<UserBankBind>();
			for(UserBankBind bank1:list){
				bank1.setIsDef("0");
				newList.add(bank1);
			}
			cashService.updateObjectList(newList, null);
		}
		if(ParamUtils.chkInteger(bankBindId)){
			
		}else{
			bankBind.setIsDef("1");
			if(sysBank != null){
				bankBind.setBankName(sysBank.getBankName());
			}
			bankBind.setBankId(bankId);
			cashService.saveObjectDB(bankBind);
		}
		return mapping.findForward("saveBindBank");
		
	}
	/**
	 * 删除账户
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer bindId= ParamUtils.getIntegerParameter(request, "id");
		UserBankBind bank=(UserBankBind) cashService.getObject(UserBankBind.class, bindId);
		User user = getUser(request);
		if(bank!=null){
			Integer userId = bank.getUserId();
			if (userId.intValue() == user.getUserId()) {
				// 删除
				cashService.deleteObject(UserBankBind.class, bindId, null);
			}
		}
		return mapping.findForward("del");
		
	}
	/**
	 * 银行列表
	 */
	public ActionForward initBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CashForm cashForm = (CashForm) form;
		String bankName = cashForm.getBankName();
		if(ParamUtils.chkString(bankName)){
			hqls.append(" and s.bankName like ? ");
			para.add("%"+bankName+"%");
		}
		PaginationSupport ps = cashService.findSysBankList(hqls.toString(), para, startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("bankName", bankName);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		cashForm.setStartIndex(startIndex + "");
		return mapping.findForward("initBank");
	}
	/**
	 * 跳转添加银行页面 
	 */
	public ActionForward preAddBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		CashForm cashForm = (CashForm) form;
		if(ParamUtils.chkInteger(id)){
			SysBank sysBank = (SysBank)cashService.getObject(SysBank.class, id);
			cashForm.setSysBank(sysBank);
		}
		return mapping.findForward("preAddBank");
	}
	/**
	 * 保存银行
	 */
	public ActionForward saveBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CashForm cashForm = (CashForm) form;
		SysBank sysBank = cashForm.getSysBank();
		Integer bankId = sysBank.getBankId();
		if(ParamUtils.chkInteger(bankId)){
			cashService.updateBank(sysBank);
		}else{
			sysBank.setStatus("1");
			cashService.saveObjectDB(sysBank);
		}
		return mapping.findForward("saveBank");	
	}
	/**
	 * 删除银行
	 */
	public ActionForward delBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer bankId= ParamUtils.getIntegerParameter(request, "id");
		SysBank bank=(SysBank) cashService.getObject(SysBank.class, bankId);
		if(bank!=null){
			cashService.deleteObject(SysBank.class, bankId, null);
		}
		return mapping.findForward("delBank");	
	}
	
	/**
	 * 导出用户提现记录表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public final ActionForward applyCashExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CashForm cashForm = (CashForm) form;
		String userName = cashForm.getUserName();
		String startDate = cashForm.getStartDate();
		String endDate = cashForm.getEndDate();
		String auditStatus = cashForm.getUserApplyCash().getAuditStatus();
		if(ParamUtils.chkString(userName)){
			hqls.append(" and upper(u.loginName) = ? ");
			para.add(userName.trim().toUpperCase());
		}
		if(ParamUtils.chkString(startDate)){
			hqls.append(" and upper(uc.createTime) >= ? ");
			para.add(startDate.toUpperCase());
		}
		if(ParamUtils.chkString(endDate)){
			hqls.append(" and upper(uc.createTime) <= ? ");
			para.add(endDate.toUpperCase());
		}
		if(ParamUtils.chkString(auditStatus)){
			hqls.append(" and upper(uc.auditStatus) = ? ");
			para.add(auditStatus.toUpperCase());
		}
		hqls.append(" order by uc.createTime desc ");
		List<CashDTO> list = cashService.findUserCashBankList(hqls.toString(), para);
		
		String modelPath = request.getSession().getServletContext().getRealPath("") + "/cash/exportModel/apply_cash_export"+".xls";
		System.out.println(modelPath);
		
		Map<String,Object>map = cashService.applyCashExport(list, modelPath);
		byte[] byteArray = (byte[])map.get("byteArray");
		ExcelUtil.exportToResponseForXls(response, byteArray, "提现管理_"+StringUtil.getSaveAsName());
		return null;
	}
	
	/**
	 * 查询新提现申请条数。
	 */
	public void untreated(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		int num = 0;
		int startIndex = 0;
		int pageSize = 100;

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		hqls.append(" and upper(uc.auditStatus) = ? ");
		para.add(Constants.CASH_AUDIT_TYPE_ING); //待审核

		PaginationSupport ps = cashService.findUserCashBankList(hqls.toString(), para, startIndex, pageSize);
		List list = ps.getItems();

		num = list.size();
		JsonUtil.AjaxWriter(response, num);
	}

}
