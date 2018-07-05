package com.game.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaSessionInfo;
import com.game.service.IGaService;
import com.game.web.form.GaForm;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

public class GaAction extends BaseDispatchAction {
	private final IGaService gaService = (IGaService) getService("gaService");
	
	/**
	 * 初始化网址选择
	 */
	public ActionForward initGameUrl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		GaForm gaForm = (GaForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String urlSwitch = ParamUtils.getParameter(request, "urlSwitch");
		hqls.append(" and ho.status=?");
		para.add(Constants.PUB_STATUS_OPEN);
		if(ParamUtils.chkString(urlSwitch)){
			hqls.append(" and ho.urlSwitch=? ");
			para.add(urlSwitch);
		}
		

		PaginationSupport ps = gaService.findGaSessionInfoList(hqls.toString(), para,
				startIndex, pageSize);
		List<GaSessionInfo> list = ps.getItems();		
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		gaForm.setStartIndex(startIndex + "");
		return mapping.findForward("initGameUrl");
	}
	
	/**
	 * 创建或编辑
	 */
	public ActionForward preAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		GaForm gaForm = (GaForm) form;
		if (ParamUtils.chkInteger(id)) {
			GaSessionInfo sessionInfo = (GaSessionInfo) gaService.getObject(GaSessionInfo.class, id);
			gaForm.setSessionInfo(sessionInfo);
		}
		return mapping.findForward("preAdd");
	}
	
	
	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		GaForm gaForm = (GaForm) form;
		GaSessionInfo sessionInfo = gaForm.getSessionInfo();
		gaService.saveSessionInfo(sessionInfo);
		
		CacheUtil.updateGameOpenUrl();
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
	}
	
}
