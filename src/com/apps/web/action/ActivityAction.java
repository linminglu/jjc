package com.apps.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.apps.Constants;
import com.apps.model.Activity;
import com.apps.service.IActivityService;
import com.apps.service.ITypeService;
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
 * 优惠活动
 * 
 */
public class ActivityAction extends BaseDispatchAction {
	private final IActivityService activityService = (IActivityService) getService("activityService");
	private final ITypeService typeService = (ITypeService) getService("typeService");

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
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and a.activityTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and a.activityTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		hqls.append(" order by a.id desc");
		PaginationSupport ps = activityService.findList(hqls.toString(), para,
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
		Activity activity = (Activity) typeService.getObject(
				Activity.class, id);
		advertForm.setActivity(activity);
		request.setAttribute("advertForm", advertForm);
		return mapping.findForward("preModify");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		AdvertForm advertForm = (AdvertForm) form;
		Activity activity = advertForm.getActivity();

		FormFile file = advertForm.getFile();
		FormFile file2 = advertForm.getFile2();
		String showImg = "";
		String hideImg = "";
		
		if (file != null && file.getFileName() != "") {
			String savePath = "/activity";
			showImg = UploadUtil.uploadOneFile(file, savePath);
			activity.setShowImg(showImg);
		}
		if (file2 != null && file2.getFileName() != "") {
			String savePath = "/activity";
			hideImg = UploadUtil.uploadOneFile(file2, savePath);
			activity.setHideImg(hideImg);
		}

		activity = activityService.saveActivity(activity);
//		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
//		ActionForward forward = mapping.findForward("save");
//		StringBuffer path = new StringBuffer();
//		path.append(forward.getPath());
//		path.append("&pager.offset=" + startIndex);
//		path.append("&adType=" + adType);
//		return new ActionForward(forward.getName(), path.toString(), true);
		return mapping.findForward("save");
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
	public void imgCut(Activity activity, String imgUrl, String path) {
		String webRoot = Constants.getWebRootPath();// 项目绝对地址

		String yuan = webRoot + imgUrl;// 源文件绝对地址
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1,
				imgUrl.length());
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		// 大图虚拟目录
		String savepath = Constants.getFileUploadPath()
				+ UploadUtil.getFolder(path) + "/" + fileName + "_b"
				+ UploadUtil.getFileExt(imgUrl);
//		String adType = activity.getAdType();
//
//		if (adType.equals(Constants.ADVERTISING_LUNBO)) {// 不同广告尺寸不同
//			ImageUtils.scale2(yuan, webRoot + savepath,
//					Constants.IMG_HEIGHT_SELLER, Constants.IMG_WIDTH_SELLER,
//					true);
//		}
//		activity.setImg(savepath);
	}

	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Activity activity = (Activity) activityService.getObject(
				Activity.class, id);
		String status = activity.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				activity.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				activity.setStatus(Constants.PUB_STATUS_OPEN);
			}
			activityService.saveObject(activity, null);
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
		AdvertForm advertForm = (AdvertForm) form;
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		String adType = ParamUtils.getParameter(request, "adType");
		advertForm.setAdType(adType);
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		activityService.updateSort(id, flag);
		return init(mapping, form, request, response);
	}

}
