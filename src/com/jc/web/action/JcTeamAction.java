package com.jc.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.apps.util.UploadUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.jc.JingcaiConstants;
import com.jc.model.JcTeam;
import com.jc.model.Type;
import com.jc.service.IJcService;
import com.jc.web.form.JcTeamForm;
import com.sun.org.apache.bcel.internal.classfile.Constant;

public class JcTeamAction extends BaseDispatchAction {
	
	private final IJcService jcService = (IJcService) getService("jcService");
	
	
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		JcTeamForm teamForm = (JcTeamForm) form;
		String title = teamForm.getJcTeam().getTitle();
		
		StringBuffer hql = new StringBuffer();
		List<Object> paras = new ArrayList<Object>();
		
		String select = "";
		hql.append(" from JcTeam t where 1=1");
		
		if(ParamUtils.chkString(title)){
			hql.append(" and t.title like ?");
			paras.add("%"+title+"%");
			request.setAttribute("title", title);
		}
		
		String orderby = " order by t.Id ";
		
		PaginationSupport ps = jcService.findObjectPage(select, hql.toString(), orderby, paras, startIndex, pageSize);
		
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", String.valueOf(ps.getTotalCount()));
		
		List<Type> type1List = jcService.getTypeList(JingcaiConstants.TYPE_FIRST);
		List<Type> type2List = jcService.getTypeList(JingcaiConstants.TYPE_SECOND);
		request.setAttribute("type1List", type1List);
		request.setAttribute("type2List", type2List);
		
		return mapping.findForward("init");
	}

	
	public ActionForward preAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer Id = ParamUtils.getIntegerParameter(request, "Id");
		JcTeamForm teamForm = (JcTeamForm) form;
		
		if(ParamUtils.chkInteger(Id)){
			JcTeam team = (JcTeam) jcService.getObject(JcTeam.class, Id);
			if(team!=null){
				teamForm.setJcTeam(team);
			}
		}
		
		saveToken(request);
		request.setAttribute("jcTeamForm", teamForm);
		List<Type> type1List = jcService.getTypeList(JingcaiConstants.TYPE_FIRST);
		List<Type> type2List = jcService.getTypeList(JingcaiConstants.TYPE_SECOND);
		request.setAttribute("type1List", type1List);
		request.setAttribute("type2List", type2List);
		
		return mapping.findForward("create");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		JcTeamForm teamForm = (JcTeamForm) form;
		JcTeam jcTeam = teamForm.getJcTeam();
		
		Integer Id = null;
		if(jcTeam!=null){
			Id = jcTeam.getId();
		}
		
		FormFile file = teamForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/team";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			jcTeam.setImg(imgUrl);
		}
		
		if(ParamUtils.chkInteger(Id)){
			// 修改
			JcTeam team = (JcTeam) jcService.getObject(JcTeam.class, Id);
			if(team!=null){
				team.setRemarks(jcTeam.getRemarks());
				team.setTitle(jcTeam.getTitle());
				team.settId1(jcTeam.gettId1());
				team.settId2(jcTeam.gettId2());
				if(ParamUtils.chkString(jcTeam.getImg())){
					team.setImg(jcTeam.getImg());
				}
				
				jcService.saveObject(team, loginedUser);
			}
		}else{
			// 新增
			if(isTokenValid(request, true)){
				jcTeam.setId(null);
				jcService.saveObject(jcTeam, loginedUser);
			}
		}
		
		return mapping.findForward("toInit");
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer Id = ParamUtils.getIntegerParameter(request, "Id");
		if(ParamUtils.chkInteger(Id)){
			JcTeam jcTeam = (JcTeam) jcService.getObject(JcTeam.class, Id);
			if(jcTeam!=null){
				jcService.deleteObject(JcTeam.class, Id, loginedUser);
			}
		}
		
		return mapping.findForward("toInit");
	}
}
