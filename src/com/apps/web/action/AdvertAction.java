package com.apps.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.Advertising;
import com.apps.service.IAdvertService;
import com.apps.service.IBaseDataService;
import com.apps.service.ISellerService;
import com.apps.service.ITypeService;
import com.apps.util.ImageUtils;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.AdvertForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 广告
 * 
 * @author Mr.zang
 * 
 */
public class AdvertAction extends BaseDispatchAction {
	private final IAdvertService advertService = (IAdvertService) getService("advertService");
	private final ITypeService typeService = (ITypeService) getService("typeService");
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");

	/**
	 * 列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
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

	/**
	 * 创建
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("create");
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		AdvertForm advertForm = (AdvertForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Advertising advert = (Advertising) typeService.getObject(
				Advertising.class, id);
		advertForm.setAdvert(advert);
		request.setAttribute("advertForm", advertForm);
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
		AdvertForm advertForm = (AdvertForm) form;
		Advertising advert = advertForm.getAdvert();

		FormFile file = advertForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/slide";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			advert.setImg(imgUrl);
		}
		String adType2 = advertForm.getAdType();
		String adType = advert.getAdType();
		if (adType == null || adType.equals("")) {
			adType = adType2;
			advert.setAdType(adType);
		}

		advert = advertService.saveAdvert(advert);
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		path.append("&adType=" + adType);
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
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Advertising advert = (Advertising) advertService.getObject(
				Advertising.class, id);
		String status = advert.getStatus();
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				advert.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				advert.setStatus(Constants.PUB_STATUS_OPEN);
			}
			advertService.saveObject(advert, null);
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
		} catch (Exception e) {
			e.printStackTrace();
			message="切换状态失败！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}

	/**
	 * 排序
	 */
	public ActionForward sotr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		AdvertForm advertForm = (AdvertForm) form;
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		String adType = ParamUtils.getParameter(request, "adType");
		advertForm.setAdType(adType);
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		advertService.updateSort(id, flag);
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
