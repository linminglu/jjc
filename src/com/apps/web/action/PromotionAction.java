package com.apps.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.apps.Constants;
import com.apps.model.Advertising;
import com.apps.model.Promotion;
import com.apps.model.PromotionRule;
import com.apps.model.Seller;
import com.apps.model.SellerUserRl;
import com.apps.model.Type;
import com.apps.service.IAdvertService;
import com.apps.service.IBaseDataService;
import com.apps.service.ISellerService;
import com.apps.service.ITypeService;
import com.apps.util.ImageUtils;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.AdvertForm;
import com.apps.web.form.PromotionForm;
import com.apps.web.form.SellerForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DateUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 * 优惠活动
 * 
 * @author hzb
 * 
 */
public class PromotionAction extends BaseDispatchAction {
	private final IAdvertService advertService = (IAdvertService) getService("advertService");

	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		PromotionForm promotionForm = (PromotionForm) form;
		String endDate = promotionForm.getEndDate();
		String startDate = promotionForm.getStartDate();

		HQUtils hq = new HQUtils();
		hq.setStartIndex(startIndex);
		hq.setPageSize(pageSize);
		hq.addHsql(" from Promotion p where 1=1 ");
		if (ParamUtils.chkString(startDate)) {
			hq.addHsql(" and p.startDate>=?");
			hq.addPars(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hq.addHsql(" and p.startDate<=?");
			hq.addPars(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		hq.addHsql(" order by p.sort desc,p.id desc ");
		PaginationSupport ps = advertService.findObjectPage(hq);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}


	/**
	 * 修改/创建
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		PromotionForm promotionForm = (PromotionForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		if (ParamUtils.chkInteger(id)) {
			Promotion promotion = (Promotion) advertService.getObject(
					Promotion.class, id);
			if (promotion != null) {
				promotionForm.setStartDate(DateUtil.dateToStrHHMM(promotion.getStartDate()));
				promotionForm.setPromotion(promotion);
			}
		}
		request.setAttribute("promotionForm", promotionForm);
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preModify");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		PromotionForm promotionForm = (PromotionForm) form;
		Promotion promotion = promotionForm.getPromotion();
		Integer id = promotion.getId();
		String startDate = promotionForm.getStartDate();
		FormFile file = promotionForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/promotion";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
		}
		if (ParamUtils.chkInteger(id)) {
			Promotion old = (Promotion) advertService.getObject(Promotion.class, id);
			old.setTitle(promotion.getTitle());
			old.setStartDate(DateUtil.convertStringToDate(startDate));
			old.setContent(promotion.getContent());
			if (ParamUtils.chkString(imgUrl)) {
				old.setImg(imgUrl);
			}
			advertService.saveObject(old, null);
		} else {
			Promotion newPromotion = new Promotion();
			newPromotion.setTitle(promotion.getTitle());
			newPromotion.setStartDate(DateUtil.convertStringToDate(startDate));
			newPromotion.setContent(promotion.getContent());
			newPromotion.setCreateDate(new Date());
			newPromotion.setStatus(Constants.PUB_STATUS_OPEN);
			newPromotion.setSort("0");
			if (ParamUtils.chkString(imgUrl)) {
				newPromotion.setImg(imgUrl);
			}
			advertService.saveObjectDB(newPromotion);
		}
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 把上传的图片处理成标准尺寸的
	 * 
	 * @param seller
	 *            对象
	 * @param imgUrl
	 *            原图的虚拟路径
	 * @param path
	 *            file_upload 下的文件夹
	 */
	public void imgCut(Advertising advert, String imgUrl, String path) {
		String webRoot = Constants.getWebRootPath();// 项目绝对地址

		String yuan = webRoot + imgUrl;// 源文件绝对地址
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1,
				imgUrl.length());
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		// 大图虚拟目录
		String savepath = Constants.getFileUploadPath()
				+ UploadUtil.getFolder(path) + "/" + fileName + "_b"
				+ UploadUtil.getFileExt(imgUrl);
		String adType = advert.getAdType();

		if (adType.equals(Constants.ADVERTISING_LUNBO)) {// 不同广告尺寸不同
			ImageUtils.scale2(yuan, webRoot + savepath,
					Constants.IMG_HEIGHT_SELLER, Constants.IMG_WIDTH_SELLER,
					true);
		}
		advert.setImg(savepath);
	}
	
	/**
	 * 删除
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		if (ParamUtils.chkInteger(id)) {
			advertService.deleteObject(Promotion.class, id, null);
		}
		return mapping.findForward("del");
	}

	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Promotion promotion = (Promotion) advertService.getObject(
				Promotion.class, id);
		String status = promotion.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				promotion.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				promotion.setStatus(Constants.PUB_STATUS_OPEN);
			}
			advertService.saveObject(promotion, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 排序
	 */
	public ActionForward sotr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		PromotionForm promotionForm = (PromotionForm) form;
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		advertService.updatePromotionSort(id, flag);
		return init(mapping, form, request, response);
	}

	
	/**
	 * 最新活动列表
	 */
	public ActionForward initActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		AdvertForm advertForm = (AdvertForm) form;
		String endDate = advertForm.getEndDate();
		String startDate = advertForm.getStartDate();
		String adType = advertForm.getAdType();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and a.createDate>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and a.createDate<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		hqls.append(" and a.adType=?");
		para.add(adType);
		hqls.append(" order by a.sort desc,a.id desc");
		PaginationSupport ps = advertService.findList(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}
}
