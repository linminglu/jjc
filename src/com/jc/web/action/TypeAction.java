package com.jc.web.action;

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
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.jc.JingcaiConstants;
import com.jc.model.Type;
import com.jc.service.IJcService;
import com.jc.web.form.TypeForm;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class TypeAction extends BaseDispatchAction {
	
	private final IJcService jcService = (IJcService) getService("jcService");
	
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		
		TypeForm typeForm = (TypeForm) form;
		Type type = typeForm.getType();
		
		String title = type.getTitle();
		
		StringBuffer hql = new StringBuffer();
		List<Object> pars = new ArrayList<Object>();
		
		String select = "";
		hql.append(" from Type t where 1=1");
		
		hql.append(" and t.status=? ");
		pars.add(JingcaiConstants.TYPE_STATUS_YES);
		
		if(ParamUtils.chkString(title)){
			hql.append(" and t.title like ? ");
			pars.add("%"+title+"%");
			request.setAttribute("title", title);
		}
		
		String orderby = " order by t.tid desc";
		
		PaginationSupport ps = jcService.findObjectPage(select, hql.toString(), orderby, pars, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		
		
		return mapping.findForward("init");
	}

	public ActionForward preAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer tid = ParamUtils.getIntegerParameter(request, "tid");
		TypeForm typeForm = (TypeForm) form;
		
		if(ParamUtils.chkInteger(tid)){
			Type type = (Type) jcService.getObject(Type.class, tid);
			typeForm.setType(type);
		}

		request.setAttribute("typeForm", typeForm);
		saveToken(request);
		List<Type> type1List = jcService.getTypeList(JingcaiConstants.TYPE_FIRST);
		request.setAttribute("typeList", type1List);
		return mapping.findForward("create");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		TypeForm typeForm = (TypeForm) form;
		Type typeF = typeForm.getType();
		
		FormFile file = typeForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/type";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			typeF.setImg(imgUrl);
		}
		
		Integer tid = typeF.getTid();
		if(ParamUtils.chkInteger(tid)){
			//修改
			Type type = (Type) jcService.getObject(Type.class, tid);
			if(type!=null){
				type.setTitle(typeF.getTitle());
				type.setStatus(Constants.PUB_STATUS_OPEN);
				type.setType(typeF.getType());
				if(ParamUtils.chkString(typeF.getImg())){
					type.setImg(typeF.getImg());
				}
				jcService.saveObject(type, loginedUser);
			}
			
		}else{
			// 新增
			typeF.setTid(null);
			typeF.setStatus(Constants.PUB_STATUS_OPEN);
			jcService.saveObject(typeF, loginedUser);
		}
		
		return mapping.findForward("toInit");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer tid = ParamUtils.getIntegerParameter(request, "tid");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");
		
		if(ParamUtils.chkInteger(tid)){
			Type type = (Type) jcService.getObject(Type.class, tid);
			if(type!=null){
				jcService.deleteObject(Type.class, tid, loginedUser);
			}
		}
		
		return mapping.findForward("toInit");
	}
	
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员、客服、财务才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)
				||Constants.USER_TYPE_CUS_SERVICE.equals(loginUserType)){
			Integer tid = ParamUtils.getIntegerParameter(request, "tid");
			Type type = (Type) jcService.getObject(Type.class, tid);
			String status = type.getStatus();
			try {
				
				if (status.equals(Constants.PUB_STATUS_OPEN)) {
					type.setStatus(Constants.PUB_STATUS_CLOSE);
				} else {
					type.setStatus(Constants.PUB_STATUS_OPEN);
				}
				jcService.saveObject(type, null);
				
				code=APIConstants.CODE_REQUEST_SUCCESS;
				message="切换状态成功！";
			} catch (Exception e) {
				e.printStackTrace();
				message="切换状态失败！";
			}
		}else{
			message="无权限！请联系管理员！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
}
