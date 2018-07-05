package com.apps.web.action;

import help.base.APIConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.Activity;
import com.apps.model.dto.NewsDTO;
import com.apps.service.INewsService;
import com.apps.util.JsonUtil;
import com.apps.web.form.NewsForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.NewsCategory;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public class NewsAction  extends BaseDispatchAction {
	private final INewsService newsService = (INewsService) getService("newsService");
	
	/**
	 * 新闻资讯列表
	 */
	public ActionForward initNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		NewsForm newsForm = (NewsForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String title = newsForm.getTitle();
		Integer tid=newsForm.getTid();
		if (ParamUtils.chkString(title)) {
			hqls.append(" and ho.title like ?");
			para.add("%" + title + "%");
		}
		if(ParamUtils.chkInteger(tid)){
			hqls.append(" and ho.tid= ?");
			para.add(tid);
		}
		hqls.append(" order by ho.nid desc");
		PaginationSupport ps = newsService.findNewsInformationList(hqls.toString(),
				para, startIndex, pageSize);
		// List list = ps.getItems();
		List<NewsDTO> list = ps.getItems();
		request.setAttribute("list", list);
		List<NewsType> typeList=newsService.findNewsTypeList();
		request.setAttribute("typeList", typeList);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("initNews");
	}
	
	
	/**
	 * 创建或编辑
	 */
	public ActionForward preAddNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		String action = ParamUtils.getParameter(request, "action");
		NewsForm newsForm = (NewsForm) form;
		String cids="";
		if (ParamUtils.chkInteger(id)) {
			NewsInformation newsInfo = (NewsInformation) newsService.getObject(NewsInformation.class, id);
			newsForm.setNewsInfo(newsInfo);
			newsForm.setTid(newsInfo.getTid());
			List<NewsCategory> list=newsService.findNewsCategoryListByNid(newsInfo.getNid());
			if(list!=null&&list.size()>0){
				for(NewsCategory cate:list){
					cids=cids+cate.getCid()+",";
				}
				cids=cids.substring(0, cids.length()-1);
			}
		}
		log.info("pids>>>>>>"+cids);
		List<NewsType> typeList=newsService.findNewsTypeList();
		List<NewsCategory> cateList=newsService.findNewsCategoryList();
		request.setAttribute("typeList", typeList);
		request.setAttribute("cateList", cateList);
		request.setAttribute("pids", cids);
		request.setAttribute("action", action);
		return mapping.findForward("preAddNews");
	}
	
	/**
	 * 保存新闻
	 */
	public ActionForward saveNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int[] ids = ParamUtils.getIntParameters(request, "ids", 0);
		NewsForm newsForm = (NewsForm) form;
		Integer tid=newsForm.getTid();
		NewsInformation newsInfo = newsForm.getNewsInfo();

		if (ParamUtils.chkInteger(newsInfo.getNid())) {
			newsInfo.setTid(tid);
			newsService.updateNewsInformation(newsInfo,ids);
		} else {
			newsInfo.setStatus(Constants.PUB_STATUS_OPEN);
			newsInfo.setTid(tid);
			newsInfo.setSubmitTime(new Date());
			newsService.saveNewsInformation(newsInfo,ids);
		}
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("saveNews");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
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
		NewsInformation newsInfo = (NewsInformation) newsService.getObject(
				NewsInformation.class, id);
		String status = newsInfo.getStatus();
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				newsInfo.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				newsInfo.setStatus(Constants.PUB_STATUS_OPEN);
			}
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
			newsService.saveObject(newsInfo, null);
		} catch (Exception e) {
			e.printStackTrace();
			message="切换状态失败！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	

}
