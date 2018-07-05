package com.apps.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.model.MessageCount;
import com.apps.service.ISmsService;
import com.apps.web.form.SmsForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.util.IPUtil;

/**
 * 短信记录管理
 * 
 * @author Mr.zang
 * 
 */
public class SmsAction extends BaseDispatchAction {
	private final ISmsService smsService = (ISmsService) getService("smsService");

	/**
	 * 短信记录列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		SmsForm smsForm = (SmsForm) form;
		String endDate = smsForm.getEndDate();
		String startDate = smsForm.getStartDate();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String receivePhone = smsForm.getReceivePhone();
		if (ParamUtils.chkString(receivePhone)) {
			hqls.append(" and m.receivePhone=?");
			para.add(receivePhone);
		}
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and m.sendTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and m.sendTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		// 用msgId效率较高，因为msgId有索引
		hqls.append(" order by m.msgId desc");
		PaginationSupport ps = smsService.findSmsList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}

	/**
	 * 短信记录统计
	 */
	public ActionForward statistics(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		SmsForm smsForm = (SmsForm) form;
		int totalCount = smsService.getSmsTotalCount();
		smsForm.setTotalCount(totalCount);
		return mapping.findForward("statistics");
	}

	public ActionForward test(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		String phone = "13311286661";
//		MessageCount sms = new MessageCount(phone, new Date(),
//				IPUtil.getIpAddr(request));
//		smsService.saveSms(sms);

		return super.execute(mapping, form, request, response);
	}

}
