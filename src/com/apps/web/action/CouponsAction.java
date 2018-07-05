package com.apps.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.apps.Constants;
import com.apps.model.Coupons;
import com.apps.model.Seller;
import com.apps.model.SellerUserRl;
import com.apps.model.dto.CouponsDTO;
import com.apps.service.ICouponsService;
import com.apps.service.ISellerService;
import com.apps.util.JsonUtil;
import com.apps.web.form.CouponsForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class CouponsAction extends BaseDispatchAction {
	private final ISellerService sellerService = (ISellerService) getService("sellerService");
	private final ICouponsService couponsService = (ICouponsService) getService("couponsService");

	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int statIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		CouponsForm couponsForm = (CouponsForm) form;
		List<Object> para = new ArrayList<Object>();

		String startDate = couponsForm.getStartDate();
		String endDate = couponsForm.getEndDate();
		// ----------------------------------------------------------
		Integer sid = couponsForm.getSid();
		String type = couponsForm.getType();
		StringBuffer hqls = new StringBuffer();
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and c.validDate>=? ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and c.validDate<=? ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		if (ParamUtils.chkString(type)) {
			hqls.append(" and c.type=? ");
			para.add(type);
		}

		User user = getUser(request);
		String userType = user.getUserType();
		List<Seller> sellerList = null;
		if (userType.equals(Constants.USER_TYPE_SELLER_BUY)
				|| userType.equals(Constants.USER_TYPE_SELLER_EAT)
				|| userType.equals(Constants.USER_TYPE_SELLER_STORE)) {
			Integer userId = user.getUserId();
			SellerUserRl sellerUser = sellerService.getSellerUser(userId);
			Integer sellerId = sellerUser.getSid();
			Seller seller = (Seller) couponsService.getObject(Seller.class,
					sellerId);
			sellerList = new ArrayList<Seller>();
			sellerList.add(seller);

			hqls.append(" and c.sid=?");
			para.add(sellerId);
		} else if (userType.equals(Constants.USER_TYPE_ADMIN)) {
			sellerList = sellerService.findAllList(Constants.PUB_STATUS_OPEN);
			if (ParamUtils.chkInteger(sid)) {
				hqls.append(" and c.sid=?");
				para.add(sid);
			}
		}

		hqls.append(" order by c.id desc ");
		PaginationSupport ps = couponsService.findCouponsListHou(
				hqls.toString(), para, statIndex, pageSize);
		List<CouponsDTO> list = ps.getItems();
		List<CouponsDTO> items = new ArrayList<CouponsDTO>();
		for (CouponsDTO couponsDTO : list) {
			Coupons coupons = couponsDTO.getCoupons();
			String type2 = coupons.getType();
			if (type2.equals("2")) {
				Integer sid2 = coupons.getSid();
				Seller seller = (Seller) sellerService.getObject(Seller.class,
						sid2);
				String title = seller.getTitle();
				couponsDTO.setTitle(title);
			}
			items.add(couponsDTO);
		}

		request.setAttribute("sellerList", sellerList);
		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		couponsForm.setStartIndex(statIndex + "");
		return mapping.findForward("init");
	}

	/**
	 * 更改商家优惠券的状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Coupons coupons = (Coupons) couponsService.getObject(Coupons.class, id);
		String status = coupons.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				coupons.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				coupons.setStatus(Constants.PUB_STATUS_OPEN);
			}
			couponsService.saveObject(coupons, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 添加优惠劵信息
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// SellerForm from = (SellerForm) form;

		User user = getUser(request);

		String userType = user.getUserType();
		Integer sid = null;
		if (userType.equals(Constants.USER_TYPE_SELLER_BUY)
				|| userType.equals(Constants.USER_TYPE_SELLER_EAT)
				|| userType.equals(Constants.USER_TYPE_SELLER_STORE)) {
			Integer userId = user.getUserId();
			SellerUserRl sellerUser = sellerService.getSellerUser(userId);
			sid = sellerUser.getSid();
		}
		List<Seller> sellerList = null;
		if (ParamUtils.chkInteger(sid)) {
			Seller seller = (Seller) couponsService
					.getObject(Seller.class, sid);
			sellerList = new ArrayList<Seller>();
			sellerList.add(seller);
		} else {
			sellerList = sellerService.findAllList(Constants.PUB_STATUS_OPEN);
		}
		request.setAttribute("sellerList", sellerList);

		return mapping.findForward("create");
	}

	/**
	 * 保存优惠劵信息
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		CouponsForm couponsForm = (CouponsForm) form;
		Coupons coupons = couponsForm.getCoupons();
		String validDate = couponsForm.getValidDate();
		validDate = validDate + " 23:59:59";
		String format = "yyyy-MM-dd HH:mm:ss";
		coupons.setValidDate(DateTimeUtil.stringToDate(validDate, format));
		couponsService.saveCoupons(coupons);
		Integer sid = coupons.getSid();
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		path.append("&sid=" + sid);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		CouponsForm couponsForm = (CouponsForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Coupons coupons = (Coupons) couponsService.getObject(Coupons.class, id);
//		Integer sid = coupons.getSid();
//		Seller seller = null;
//		if (ParamUtils.chkInteger(sid)) {
//			seller = (Seller) couponsService.getObject(Seller.class, sid);
//		}

		List<Seller> sellerList = sellerService.findAllList(Constants.PUB_STATUS_OPEN);
//		List<Seller> sellerList = new ArrayList<Seller>();
//		if (seller != null) {
//			sellerList.add(seller);
//		}
		request.setAttribute("sellerList", sellerList);
		couponsForm.setCoupons(coupons);
		String format = "yyyy-MM-dd ";
		couponsForm.setValidDate(DateTimeUtil.dateToString(
				coupons.getValidDate(), format));
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preModify");
	}

	/**
	 * 删除
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		couponsService.deleteObject(Coupons.class, id, null);
		CouponsForm couponsForm = (CouponsForm) form;
		String startIndex = couponsForm.getStartIndex();

		request.setAttribute("pager.offset", startIndex);
		return init(mapping, form, request, response);
	}

}
