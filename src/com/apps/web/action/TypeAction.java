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
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.Type;
import com.apps.service.ITypeService;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.TypeForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 类型
 * 
 * @author Mr.zang
 * 
 */
public class TypeAction extends BaseDispatchAction {
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
		String types = ParamUtils.getParameter(request, "types");//栏目类型
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
//		if(ParamUtils.chkString(column)){
//			hqls.append("  and type=? ");
//			para.add(column);
		
			//需要显示出所有栏目
//			hqls.append(" and t.status=? ");
//			para.add(Constants.PUB_STATUS_OPEN);
			
			hqls.append(" and t.parentId=? ");
			para.add("0");
			
			if(ParamUtils.chkString(types)){
				hqls.append(" and t.type=?");
				para.add(types);
			}

//		}
		PaginationSupport  ps = typeService.findList(hqls.toString(), para,
				startIndex, pageSize);
		hqls.append(" order by t.status desc,t.sort desc");
		
//		if(ParamUtils.chkString(column)){
//			ps = typeService.findList(hqls.toString(), para,
//					startIndex, pageSize);
//		}
	
		List list = ps.getItems();
	
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("typeCateList", typeService.findTypeCate());
		return mapping.findForward("init");
	}

	
	/**
	 * 团购分类
	 */
	public ActionForward group(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		TypeForm typeForm = (TypeForm) form;

		String tid=typeForm.getTid();
		if(!ParamUtils.chkString(tid)){//  用于修改  团购二级分类 传过来的typeid 方便查看 修改的type项
			String ctid=ParamUtils.getParameter(request, "tid");
			if(ParamUtils.chkString(ctid)){
				tid=ctid;
				typeForm.setTid(ctid);
			}
		}
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String infoType=ParamUtils.getParameter(request, "infoType");
		if(!ParamUtils.chkString(infoType)){
			infoType=typeForm.getColumn();
		}
		Integer typeId=null;
		List<Type> typeList = typeService.findTypeList(Constants.MODULE_GROUP);
		if(typeList!=null&&typeList.size()>0){
			typeId=typeList.get(0).getTid();
		}
		if("1".equals(infoType)){//二级类型
			/*List<Type> list=new ArrayList<Type>();
			if(ParamUtils.chkInteger(typeId)){
				list=typeService.findTypeList(typeId);
			}*/
			List<Type> groupList = typeService.findGRTypeList();
			typeForm.setColumn(infoType);
			if(ParamUtils.chkString(tid)){
				hqls.append(" and t.parentId=? ");
				para.add(tid);
			}else if(groupList != null && groupList.size() > 0 ){
				String parentId="";
				for (Type type : groupList) {
					Integer tid2 = type.getTid();
					parentId = parentId + tid2 + ",";
				}
				parentId = parentId.substring(0, parentId.length()-1);
				hqls.append(" and t.parentId  in("+parentId+")");
			}
			hqls.append(" order by t.status desc,t.sort desc");
			PaginationSupport  ps = typeService.findTypeList(hqls.toString(), para,
					startIndex, pageSize);
			request.setAttribute("infoType", infoType);
			request.setAttribute("groupList", groupList==null?new ArrayList<Type>():groupList);
			request.setAttribute("list", ps.getItems());
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
		}
		if("2".equals(infoType)){//团购二级分类
			typeForm.setColumn(infoType);
			List<Type> groupList=new ArrayList<Type>();
			if(ParamUtils.chkInteger(typeId)){
				groupList=typeService.findTypeList(typeId);
			}
			if(ParamUtils.chkString(tid)){
				hqls.append(" and t.parentId=? ");
				para.add(tid);
			}else{
				if(groupList.size()>0){
					String parentId="";
					for(int i=0;i<groupList.size();i++){
						parentId=parentId+groupList.get(i).getTid()+",";
					}
					parentId=parentId.substring(0, parentId.length()-1);
					hqls.append(" and t.parentId  in("+parentId+")");
				}
			}
			hqls.append(" order by t.status desc,t.sort desc");
			PaginationSupport  ps = typeService.findList(hqls.toString(), para,
					startIndex, pageSize);
			request.setAttribute("infoType", infoType);
			request.setAttribute("list", ps.getItems());
			request.setAttribute("groupList", groupList);
			request.setAttribute("count", (ps.getTotalCount() + "").toString());
		}
		return mapping.findForward("group");
	}

	
	
	/**
	 * 创建
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TypeForm typeForm = (TypeForm) form;
		String infoType = ParamUtils.getParameter(request, "infoType");
		typeForm.setColumn(infoType);
		if("2".equals(infoType)){
			Integer typeId=null;
			List<Type> typeList = typeService.findTypeList(Constants.MODULE_GROUP);
			if(typeList!=null&&typeList.size()>0){
				typeId=typeList.get(0).getTid();
			}
			List<Type> list = typeService.findTypeList(typeId);
			request.setAttribute("list", list);
		}else{
			List<Type> typeList = typeService.findGRTypeList();
			request.setAttribute("list", typeList == null?new ArrayList<Type>():typeList);
		}
		request.setAttribute("infoType", infoType);
		request.setAttribute("typeCateList", typeService.findTypeCate());
		return mapping.findForward("create");
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		TypeForm typeForm = (TypeForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "tid");
		String infoType= ParamUtils.getParameter(request, "infoType");
		if(ParamUtils.chkString(infoType)){
			typeForm.setColumn(infoType);
			request.setAttribute("infoType", infoType);
			Integer tid = Integer.parseInt(typeForm.getTid());
			List<Type> typeList = new ArrayList<Type>();
			Type type = (Type)typeService.getObject(Type.class, tid);
			Integer parentId = type.getParentId();
			Type type2 = (Type)typeService.getObject(Type.class, parentId);
			typeList.add(type2);
			typeForm.setTid(parentId.toString());
			request.setAttribute("list", typeList);
			/*if("2".equals(infoType)){
			}else{
				request.setAttribute("list", new ArrayList<Type>());
			}*/
		}
		Type type = (Type) typeService.getObject(Type.class, id);
		typeForm.setType(type);
		request.setAttribute("typeForm", typeForm);
		
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		request.setAttribute("typeCateList", typeService.findTypeCate());
		return mapping.findForward("preModify");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		TypeForm typeForm = (TypeForm) form;
		Type type = typeForm.getType();
		FormFile file = typeForm.getFile();
		String infoType = typeForm.getColumn();
//		if("1".equals(infoType)){
//			Integer typeId = null;
//			List<Type> typeList = typeService.findTypeList(Constants.MODULE_GROUP);
//			if(typeList!=null&&typeList.size()>0){
//				typeId=typeList.get(0).getTid();
//			}
//			type.setParentId(typeId);
//			type.setType(Constants.MODULE_GROUP);
//		}else if("2".equals(infoType)){
//			Integer tid = Integer.parseInt(typeForm.getTid());
//			type.setParentId(tid);
//			type.setType(Constants.MODULE_GROUP);
//		}
		if(ParamUtils.chkString(typeForm.getTid())){
			Integer tid = Integer.parseInt(typeForm.getTid());
			type.setParentId(tid);
		}
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/type";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			type.setImg(imgUrl);
		}
		Type type2 = typeService.saveType(type);
		Type newtype=(Type) typeService.getObject(Type.class, type2.getTid());

		ActionForward forward =null;
		
		StringBuffer path = new StringBuffer();
		if(ParamUtils.chkString(typeForm.getColumn())){
			forward = mapping.findForward("groupSave");
			path.append(forward.getPath());
			
			if("2".equals(typeForm.getColumn())){
				path.append("&tid=" + newtype.getParentId());
			}
			path.append("&infoType=" + typeForm.getColumn());
		}else{
			int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
			forward = mapping.findForward("save");			
			path.append(forward.getPath());
			path.append("&pager.offset=" + startIndex);
		}
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 保存
	 */
	public ActionForward groupSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		TypeForm typeForm = (TypeForm) form;
		String infoType=typeForm.getColumn();
		Type type = typeForm.getType();
		FormFile file = typeForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/type";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			type.setImg(imgUrl);
		}
		typeService.saveType(type);
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("groupSave");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);

		if(ParamUtils.chkString(infoType)){
			if("2".equals(infoType)){
				path.append("&tid=" + type.getParentId());
			}
			path.append("&infoType=" + infoType);
		}		
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "tid");
		Type type = (Type) typeService.getObject(Type.class, id);
		String status = type.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				type.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				type.setStatus(Constants.PUB_STATUS_OPEN);
			}
			typeService.saveObject(type, null);
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
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		Integer id = ParamUtils.getIntegerParameter(request, "tid");
		typeService.updateSort(id, flag);
		return init(mapping, form, request, response);
	}

	/**
	 * 排序
	 */
	public ActionForward groupSotr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String infoType=ParamUtils.getParameter(request, "infoType");
		String flag = ParamUtils.getParameter(request, "flag");// 1升序 0降序
		Integer id = ParamUtils.getIntegerParameter(request, "tid");
		TypeForm typeForm = (TypeForm) form;
		typeService.updateSort(id, flag);
		Type type=(Type) typeService.getObject(Type.class, id);
		typeForm.setTid(type.getParentId().toString());	
		typeForm.setColumn(infoType);
		return group(mapping, typeForm, request, response);
	}
	
	/**
	 * 根据类型获得子类
	 */
	public void loadType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer tid = ParamUtils.getIntegerParameter(request, "tid");
		List<Type> list = typeService.findTypeList(tid);

		JSONArray items = new JSONArray();
		for (Type type : list) {
			JSONObject obj = new JSONObject();
			obj.put("tid", type.getTid());//
			obj.put("title", type.getTitle());//
			items.put(obj);
		}
		JsonUtil.AjaxWriter(response, items);
	}
	
	/**
	 * 删除栏目
	 * 包括首页栏目和二级栏目等
	 * 如果栏目有子栏目或栏目下有商品等已经应用则不能删除
	 */
	public void delType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer tid = ParamUtils.getIntegerParameter(request, "tid");
		JSONObject jo = new JSONObject();
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(ParamUtils.chkInteger(tid)){
			 code = typeService.delType(tid);
		}
		jo.put("code", code);
		JsonUtil.AjaxWriter(response, jo);
	}

}
