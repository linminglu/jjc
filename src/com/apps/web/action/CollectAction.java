package com.apps.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.model.dto.CollectDTO;
import com.apps.model.dto.SellerDTO;
import com.apps.service.ICollectService;
import com.apps.web.form.CollectForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 * 收藏
 * 
 * @author Mr.zang
 * 
 */
public class CollectAction extends BaseDispatchAction {
	private final ICollectService collectService = (ICollectService) getService("collectService");

	/**
	 * 收藏商家列表
	 */
	public ActionForward collectSeller(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CollectForm managerForm = (CollectForm) form;
		String title = managerForm.getTitle();
		String name = managerForm.getName();
		hqls.append(" and co.type='1' ");
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();

		if (ParamUtils.chkString(title)) {
			hqls.append(" and upper(s.title) like ? ");
			para.add("%" + title.trim().toUpperCase() + "%");
		}

		if (ParamUtils.chkString(name)) {
			hqls.append(" and (upper(u.loginName) like ? or upper(u.userName) like ? or upper(u.userEmail) like ?)");
			para.add("%" + name.trim().toUpperCase() + "%");
			para.add("%" + name.trim().toUpperCase() + "%");
			para.add("%" + name.trim().toUpperCase() + "%");
		}

		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and co.createTime>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and co.createTime<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}

		PaginationSupport ps = collectService.findCollectSeller(
				hqls.toString(), para, startIndex, pageSize);

		List<SellerDTO> items = ps.getItems();

		List<SellerDTO> list = new ArrayList<SellerDTO>();
		for (SellerDTO sellerDTO : items) {
			Integer uid = sellerDTO.getUid();
			User user = userService.getUser(uid);
			sellerDTO.setUserName(user.getUserName());
			list.add(sellerDTO);
		}
		request.setAttribute("collectList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("collectSeller");
	}
	/**
	 * 收藏商品列表
	 */
	public ActionForward collectProduct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		String type = ParamUtils.getParameter(request, "type");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CollectForm managerForm = (CollectForm) form;
		String title = managerForm.getTitle();
		String name = managerForm.getName();
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String type2 = managerForm.getType();
		String collectType = managerForm.getCollectType();
		if(ParamUtils.chkString(type2)){
			type = type2;
		}

		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and co.createTime>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and co.createTime<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}
		if(ParamUtils.chkString(name)){
			hqls.append(" and u.userName like ?");
			para.add("%" + name+ "%");
		}
		PaginationSupport ps = null;
		if("2".equals(type)){
			hqls.append(" and co.type=? ");
			para.add(type);
			if (ParamUtils.chkString(title)) {
				hqls.append(" and b.title like ? ");
				para.add("%" + title + "%");
			}
			ps = collectService.findCollectProduct(
					hqls.toString(), para, startIndex, pageSize);
		}
		if("3".equals(type)){
//			hqls.append(" and co.type=? ");
//			para.add(type);
			if(ParamUtils.chkString(collectType)){
				hqls.append(" and co.type = ?");
				para.add(collectType);
				request.setAttribute("collectType", collectType);
			}
			if (ParamUtils.chkString(title)) {
				hqls.append(" and upper(m.title) like ? ");
				para.add("%" + title.trim().toUpperCase() + "%");
			}
			ps = collectService.findCollectMytInfo(
					hqls.toString(), para, startIndex, pageSize);
		}
		List list = ps.getItems();
		request.setAttribute("productList", list);
		request.setAttribute("type", type);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("collectProduct");
	}

}
