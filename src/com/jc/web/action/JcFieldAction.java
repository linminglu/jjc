package com.jc.web.action;

import help.base.APIConstants;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.jc.JingcaiConstants;
import com.jc.model.JcField;
import com.jc.model.JcMatch;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.service.IJcService;
import com.jc.web.form.JcFieldForm;
import com.ram.model.User;

public class JcFieldAction extends BaseDispatchAction {

	private final IJcService jcService = (IJcService) getService("jcService");
	
	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		
		StringBuffer hql = new StringBuffer();
		List<Object> paras = new ArrayList<Object>();
		
		JcFieldForm fieldForm = (JcFieldForm) form;
		JcField field = fieldForm.getField();
		
		String title = field.getTitle();
		String startDate = fieldForm.getStartDate();
		String endDate = fieldForm.getEndDate();
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		if(!ParamUtils.chkInteger(mId)){
			mId = field.getmId();
		}
		
		String select = "";
		
		hql.append(" from JcField f where 1=1 ");
		
		if(ParamUtils.chkString(title)){
			hql.append(" and f.title like ? ");
			paras.add("%"+title+"%");
		}
		
		if(ParamUtils.chkString(startDate)){
			hql.append(" and f.startDate>=? ");
			paras.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		
		if(ParamUtils.chkString(endDate)){
			hql.append(" and f.startDate<=? ");
			paras.add(DateTimeUtil.parse(endDate + " 00:00:00"));
		}
		
		if(ParamUtils.chkInteger(mId)){
			hql.append(" and f.mId=? ");
			paras.add(mId);
		}
		
		String orderby = " order by f.fId desc";
		
		PaginationSupport ps = jcService.findObjectPage(select, hql.toString(), orderby, paras, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		
		List<Object> matchList = jcService.findObjects(JcMatch.class);
		request.setAttribute("matchList", matchList);
		
		return mapping.findForward("init");
	}
	
	public void getMatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from JcMatch m where 1=1");
		
		
		List<Object> matchList = jcService.findObjects(hq);
		if(matchList!=null && matchList.size()>0){
			JSONArray matchListJson = JSONArray.fromObject(matchList);
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(matchListJson.toString());
			out.flush();
			out.close();
		}
		
		
	}
	
	public void saveParam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		// 比赛局
		Integer fId = ParamUtils.getIntegerParameter(request, "param1_3");
		String fieldTitle = ParamUtils.getParameter(request, "param1_1");
		Integer mId = ParamUtils.getIntegerParameter(request, "param1_2"); 
		String startDate = ParamUtils.getParameter(request, "param1_5");
		String endDate = ParamUtils.getParameter(request, "param1_6");
		
		// 玩法
		String playIds = ParamUtils.getParameter(request, "playIds");
		String playTitles = ParamUtils.getParameter(request, "playTitles");
		String playStatus = ParamUtils.getParameter(request, "playStatus");
		
		// 投注项
		String optionIds = ParamUtils.getParameter(request, "optionIds");
		String optionTitles = ParamUtils.getParameter(request, "optionTitles");
		String optionPlays = ParamUtils.getParameter(request, "optionPlays");
		String optionRates = ParamUtils.getParameter(request, "optionRates");
		
		if(!ParamUtils.chkString(fieldTitle) && !ParamUtils.chkString(fieldTitle) && !ParamUtils.chkInteger(mId) &&
				!ParamUtils.chkString(startDate) && !ParamUtils.chkString(endDate) && !ParamUtils.chkString(playTitles)
				&& !ParamUtils.chkString(playStatus) && !ParamUtils.chkString(optionTitles) && !ParamUtils.chkString(optionPlays)
				&& !ParamUtils.chkString(optionRates)){
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if(message.equals("")){
			JcField field = null;
			if(ParamUtils.chkInteger(fId)){
				// 修改
				field = (JcField) jcService.getObject(JcField.class, fId);
				if(field!=null){
					field.setmId(mId);
					field.setTitle(fieldTitle);
					field.setStartDate(DateTimeUtil.stringToDate(startDate, "yyyy-MM-dd HH:mm:ss"));
					field.setEndDate(DateTimeUtil.StringToDate(endDate,"yyyy-MM-dd HH:mm:ss"));
				}
			}else{
				// 新增
				field = new JcField();
				field.setmId(mId);
				field.setTitle(fieldTitle);
				field.setStatus(Constants.PUB_STATUS_OPEN);
				field.setStartDate(DateTimeUtil.stringToDate(startDate, "yyyy-MM-dd HH:mm:ss"));
				field.setEndDate(DateTimeUtil.StringToDate(endDate,"yyyy-MM-dd HH:mm:ss"));
			}
			
			String msg = jcService.saveFields(field, playIds, playTitles, playStatus, optionIds, optionTitles, optionPlays, optionRates);
			if(msg.equals("")){
				code=APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				message = msg;
			}
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response,map);
	}
	
	public ActionForward preAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer fId = ParamUtils.getIntegerParameter(request, "fId");
		request.setAttribute("fId", fId);
		
		return mapping.findForward("create");
	}
	
	public void loadParam(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		org.json.JSONArray items = new org.json.JSONArray();// 数据集合
		
		JSONObject obj = null;
		JSONObject objp = null;
		JSONObject objo = null;
		
		Integer fId = ParamUtils.getIntegerParameter(request, "fId");
		
		if (message.equals("") && ParamUtils.chkInteger(fId)) {
			JcField field = (JcField) jcService.getObject(JcField.class, fId);
			if(field!=null){
				obj = new JSONObject();
				obj.put("fId", field.getfId());
				obj.put("fieldTitle", field.getTitle());
				obj.put("mId", field.getmId());
				obj.put("fieldStatus", field.getStatus());
				obj.put("startDate", DateTimeUtil.DateToStringAll(field.getStartDate()));
				obj.put("endDate", DateTimeUtil.DateToStringAll(field.getEndDate()));
				JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, field.getmId());
				if(match!=null){
					obj.put("red", match.getRed());
					obj.put("blue", match.getBlue());
				}
				
				List<JcPlayType> list = jcService.getJcPlayTypesByFid(field.getfId(),null);
				if(list!=null && list.size()>0){
					for (JcPlayType playType : list) {
						objp = new JSONObject();
						objp.put("pTId", playType.getpTId());
						objp.put("fId", playType.getfId());
						objp.put("status", playType.getStatus());
						objp.put("playTitle", playType.getTitle());
						
						org.json.JSONArray itemo = new org.json.JSONArray();// 数据集合
						
						List<JcOption> optionlist = jcService.getJcOptionsByPtId(playType.getpTId());
						if(optionlist!=null && optionlist.size()>0){
							for (JcOption option : optionlist) {
								objo = new JSONObject();
								objo.put("oId", option.getoId());
								objo.put("ptId", option.getpTId());
								objo.put("optionTitle", option.getTitle());
								objo.put("rate", option.getRate());
								
								itemo.put(objo);
							}
						}
						objp.put("itemo", itemo);
						
						items.put(objp);
					}
					obj.put("pitems", items);
					data.put("obj", obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public void del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		Integer playId = ParamUtils.getIntegerParameter(request, "playId");
		Integer optId = ParamUtils.getIntegerParameter(request, "optId");
		
		if(ParamUtils.chkInteger(playId)){
			//删除玩法，删除玩法的时候会删除玩法下面的投注项
			jcService.deletePlayTypeAndOption(playId);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		
		if(ParamUtils.chkInteger(optId)){
			// 删除投注项
			JcOption option = (JcOption) jcService.getObject(JcOption.class, optId);
			if(option!=null){
				jcService.deleteObject(JcOption.class, optId, loginedUser);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		data.put("code", code);
		data.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		
		Integer fId = ParamUtils.getIntegerParameter(request, "fId");
		User user = getUser(request);
		
		if(ParamUtils.chkInteger(fId) && Constants.USER_TYPE_ADMIN.equals(user.getUserType())){
			jcService.deleteField(fId);
		}
		
		return mapping.findForward("toInit");
	}
	
	public ActionForward fieldPlay(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		
		StringBuffer hql = new StringBuffer();
		List<Object> paras = new ArrayList<Object>();
		
		JcFieldForm fieldForm = (JcFieldForm) form;
		JcField field = fieldForm.getField();
		
		String title = field.getTitle();
		String startDate = fieldForm.getStartDate();
		String endDate = fieldForm.getEndDate();
		
		String select = "";
		
		hql.append(" from JcField f where 1=1 ");
		
		if(ParamUtils.chkString(title)){
			hql.append(" and f.title like ? ");
			paras.add("%"+title+"%");
		}
		
		if(ParamUtils.chkString(startDate)){
			hql.append(" and f.startDate>=? ");
			paras.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		
		if(ParamUtils.chkString(endDate)){
			hql.append(" and f.startDate<=? ");
			paras.add(DateTimeUtil.parse(endDate + " 00:00:00"));
		}
		
		String orderby = " order by f.fId desc";
		
		PaginationSupport ps = jcService.findObjectPage(select, hql.toString(), orderby, paras, startIndex, pageSize);
		
		List<JcField> list = ps.getItems();
		if(list!=null && list.size()>0){
			for (JcField jfield : list) {
				List<JcPlayType> playList = jcService.getJcPlayTypesByFid(jfield.getfId(), null);
				jfield.setPlayList(playList);
			}
		}
		
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		
		List<Object> matchList = jcService.findObjects(JcMatch.class);
		request.setAttribute("matchList", matchList);
		
		return mapping.findForward("fieldPlay");
	}
	
	public ActionForward preOpen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer fId = ParamUtils.getIntegerParameter(request, "fId");
		JcFieldForm fieldForm = (JcFieldForm) form;
		
		
		if(ParamUtils.chkInteger(fId)){
			JcField field  = (JcField) jcService.getObject(JcField.class, fId);
			if(field!=null){
				JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, field.getmId());
				if(match!=null){
					field.setmTitle(match.getTitle());
					field.setRed(match.getRed());
					field.setBlue(match.getBlue());
				}
				fieldForm.setField(field);
			}
		}
		
		List<JcPlayType> playList = jcService.getJcPlayTypesByFid(fId, null);
		request.setAttribute("playList", playList);
		
		
		
		return mapping.findForward("toOpen");
	}
	
	public void getOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer ptId = ParamUtils.getIntegerParameter(request, "ptId");
		
		List<JcOption> optionList = null;
		if(ParamUtils.chkInteger(ptId)){
			optionList = jcService.getJcOptionsByPtId(ptId);
		}
		
		JSONArray optionListJson = null;
		if(optionList!=null && optionList.size()>0){
			optionListJson = JSONArray.fromObject(optionList);
			
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(optionListJson!=null){
			out.println(optionListJson.toString());
		}else{
			out.println("");
		}
		out.flush();
		out.close();
	}
	
	public void getRateByOId(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer oId = ParamUtils.getIntegerParameter(request, "oId");
		String rate = "";
		if(ParamUtils.chkInteger(oId)){
			JcOption option = (JcOption) jcService.getObject(JcOption.class, oId);
			if(option!=null){
				rate = option.getRate().toString();
			}
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(rate);
		out.flush();
		out.close();
	}
	
	public void saveOpen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer oId = ParamUtils.getIntegerParameter(request, "oId");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String msg = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		if(ParamUtils.chkInteger(oId)){
			JcOption option = (JcOption) jcService.getObject(JcOption.class, oId);
			if(option!=null){
				JcPlayType playType = (JcPlayType) jcService.getObject(JcPlayType.class, option.getpTId());
				if (playType!=null) {
					String status = playType.getOpenStatus();
					if(JingcaiConstants.QUIZ_STATUS_OPENING.equals(status)){
						msg = "您选择的玩法已开过奖了";
						code = APIConstants.CODE_REQUEST_SUCCESS;
					}
					
					if(msg.equals("") && ParamUtils.chkInteger(playType.getpTId()) && ParamUtils.chkInteger(option.getoId())){
						msg = jcService.updateOPenResult(playType, option);
						code = APIConstants.CODE_REQUEST_SUCCESS;
						msg = "开奖成功";
					}
				}else{
					msg = "请选择玩法或开奖结果";
				}
			}else{
				msg = "请选择玩法或开奖结果";
			}
		}else{
			msg = "请选择玩法或开奖结果";
		}
		
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public void getTeam(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		
		if(ParamUtils.chkInteger(mId)){
			JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, mId);
			if(match!=null){
				JSONObject obj = new JSONObject();
				obj.put("red", match.getRed());
				obj.put("blue", match.getBlue());
				data.put("obj", obj);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		
		data.put("code", code);
		data.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
}
