package com.jc.web.action;

import help.base.APIConstants;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.jc.model.JcTeam;
import com.jc.model.Type;
import com.jc.service.IJcService;
import com.jc.web.form.JcMatchForm;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

public class JcMatchAction extends BaseDispatchAction {
	
	private final IJcService jcService = (IJcService) getService("jcService");

	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		
		JcMatchForm matchForm = (JcMatchForm) form;
		JcMatch jcMatch = matchForm.getMatch();
		
		String title = jcMatch.getTitle();
		String startDate = matchForm.getStartDate();
		String endDate = matchForm.getEndDate();
		
		StringBuffer hqls = new StringBuffer();
		List<Object> paras = new ArrayList<Object>();
		
		String select = "";
		
		hqls.append(" from JcMatch m where 1=1 ");
		
		if(ParamUtils.chkString(title)){
			hqls.append(" and m.title like ?");
			paras.add("%"+title+"%");
		}
		
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and m.matchTime>=?");
			paras.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and m.matchTime<=?");
			paras.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		
		String orderby = " order by m.mId desc ";
		
		PaginationSupport ps = jcService.findObjectPage(select, hqls.toString(), orderby, paras, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		
		List<JcTeam> teamList = jcService.getTeamList();
		request.setAttribute("teamList", teamList);
		List<Type> type1List = jcService.getTypeList(JingcaiConstants.TYPE_FIRST);
		List<Type> type2List = jcService.getTypeList(JingcaiConstants.TYPE_SECOND);
		request.setAttribute("type1List", type1List);
		request.setAttribute("type2List", type2List);
		
		return mapping.findForward("init");
	}
	
	public ActionForward preAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		JcMatchForm matchForm = (JcMatchForm) form;
		
		if(ParamUtils.chkInteger(mId)){
			JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, mId);
			matchForm.setMatch(match);
			
			if(match!=null){
				if(match.getOpenTime()!=null){
					matchForm.setOpenTime(DateTimeUtil.DateToStringAll(match.getOpenTime()));
				}
				
				if(match.getMatchTime()!=null){
					matchForm.setMatchTime(DateTimeUtil.DateToStringAll(match.getMatchTime()));
				}
				
				String team1Title = match.getTeam1Name();
				if(ParamUtils.chkString(team1Title)){
					JcTeam team1 = jcService.getTeamByTitle(team1Title);
					if(team1!=null){
						match.setTeam1Name(String.valueOf(team1.getId()));
					}
					
					if(team1Title.equals(match.getRed())){
						match.setRed(String.valueOf(team1.getId()));
					}else if(team1Title.equals(match.getBlue())){
						match.setBlue(String.valueOf(team1.getId()));
					}
					
				}
				
				String team2Title = match.getTeam2Name();
				if(ParamUtils.chkString(team2Title)){
					JcTeam team2 = jcService.getTeamByTitle(team2Title);
					if(team2!=null){
						match.setTeam2Name(String.valueOf(team2.getId()));
					}
					
					if(team2Title.equals(match.getRed())){
						match.setRed(String.valueOf(team2.getId()));
					}else if(team2Title.equals(match.getBlue())){
						match.setBlue(String.valueOf(team2.getId()));
					}
				}
			}
		}
		saveToken(request);
		List<JcTeam> teamList = jcService.getTeamList();
		request.setAttribute("teamList", teamList);
		List<Type> type1List = jcService.getTypeList(JingcaiConstants.TYPE_FIRST);
		List<Type> type2List = jcService.getTypeList(JingcaiConstants.TYPE_SECOND);
		request.setAttribute("type1List", type1List);
		request.setAttribute("type2List", type2List);
		
		return mapping.findForward("create");
	}
	
	public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		request.setAttribute("mId", mId);
		
		return mapping.findForward("update");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		JcMatchForm matchForm = (JcMatchForm) form;
		JcMatch match = matchForm.getMatch();
		String msg = "";
		
		String openTime = matchForm.getOpenTime();
		String matchTime = matchForm.getMatchTime();
		if(ParamUtils.chkString(openTime)){
			match.setOpenTime(DateTimeUtil.stringToDate(openTime, "yyyy-MM-dd HH:mm:ss"));
		}
		
		if(ParamUtils.chkString(matchTime)){
			match.setMatchTime(DateTimeUtil.StringToDate(matchTime, "yyyy-MM-dd HH:mm:ss"));
		}
		
		if(match!=null){
			String team1Id = match.getTeam1Name();
			if(ParamUtils.chkString(team1Id)){
				JcTeam team1 = (JcTeam) jcService.getObject(JcTeam.class, Integer.valueOf(team1Id));
				if(team1!=null){
					match.setTeam1Name(team1.getTitle());
					match.setTeam1Img(team1.getImg());
				}
				
				if(team1Id.equals(match.getRed())){
					match.setRed(team1.getTitle());
				}else if(team1Id.equals(match.getBlue())){
					match.setBlue(team1.getTitle());
				}
				
			}
			
			String team2Id = match.getTeam2Name();
			if(ParamUtils.chkString(team2Id)){
				JcTeam team2 = (JcTeam) jcService.getObject(JcTeam.class, Integer.valueOf(team2Id));
				if(team2!=null){
					match.setTeam2Name(team2.getTitle());
					match.setTeam2Img(team2.getImg());
				}
				
				if(team2Id.equals(match.getRed())){
					match.setRed(team2.getTitle());
				}else if(team2Id.equals(match.getBlue())){
					match.setBlue(team2.getTitle());
				}
			}
			
			if(team1Id.equals(team2Id) || match.getRed().equals(match.getBlue())){
				msg = "对战队伍不能是同一支";
			}
		}
		
		if(msg!=""){
			List<JcTeam> teamList = jcService.getTeamList();
			request.setAttribute("teamList", teamList);
			return mapping.findForward("create");
		}
		
		match.setTotalPrice(BigDecimal.ZERO);
		
		Integer mId = match.getmId();
		if(ParamUtils.chkInteger(mId)){
			// 修改
			JcMatch jcMatch = (JcMatch) jcService.getObject(JcMatch.class, mId);
			if(jcMatch!=null){
				match.setIsRecommend(jcMatch.getIsRecommend());
				jcService.saveObject(match, loginedUser);
			}
			
		}else{
			// 新增
			match.setmId(null);
			match.setIsRecommend(JingcaiConstants.IS_RECOMMAND_NO);
			jcService.saveObject(match, loginedUser);
		}
		
		
		return mapping.findForward("toInit");
	}
	
	public ActionForward initMatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",JingcaiConstants.MAXPAGEITEMS);
		
		JcMatchForm matchForm = (JcMatchForm) form;
		JcMatch jcMatch = matchForm.getMatch();
		
		String title = jcMatch.getTitle();
		String startDate = matchForm.getStartDate();
		String endDate = matchForm.getEndDate();
		
		Integer mId = jcMatch.getmId();
		String team1Name = jcMatch.getTeam1Name();
		
		StringBuffer hqls = new StringBuffer();
		List<Object> paras = new ArrayList<Object>();
		
		String select = "";
		
		hqls.append(" from JcMatch m where 1=1 ");
		
		if(ParamUtils.chkString(title)){
			hqls.append(" and m.title like ?");
			paras.add("%"+title+"%");
		}
		
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and m.matchTime>=?");
			paras.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and m.matchTime<=?");
			paras.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		
		if(ParamUtils.chkInteger(mId)){
			hqls.append(" and m.mId=? ");
			paras.add(mId);
		}
		
		String orderby = " order by m.mId desc ";
		
		PaginationSupport ps = jcService.findObjectPage(select, hqls.toString(), orderby, paras, startIndex, pageSize);
		
		List<JcMatch> list = ps.getItems();
		if(list!=null && list.size()>0){
			for (JcMatch match : list) {
				List<JcField> fieldList = jcService.getJcFieldByMid(match.getmId(), null);
				match.setFieldList(fieldList);
				if(fieldList!=null && fieldList.size()>0){
					for (JcField field : fieldList) {
						List<JcPlayType> playList = jcService.getJcPlayTypesByFid(field.getfId(), null);
						field.setPlayList(playList);
					}
				}
			}
		}
		
		request.setAttribute("list", ps.getItems());
		request.setAttribute("totalCount", ps.getTotalCount()+"");
		
		List<JcTeam> teamList = jcService.getTeamList();
		request.setAttribute("teamList", teamList);
		
		
		return mapping.findForward("initMatch");
	}
	
	public ActionForward preOpen(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		JcMatchForm matchForm = (JcMatchForm) form;
		
		JcMatch match = null;
		if(ParamUtils.chkInteger(mId)){
			match = (JcMatch) jcService.getObject(JcMatch.class, mId);
			
			String team1Title = match.getTeam1Name();
			if(ParamUtils.chkString(team1Title)){
				JcTeam team1 = jcService.getTeamByTitle(team1Title);
				if(team1!=null){
					match.setTeam1Name(String.valueOf(team1.getId()));
				}
			}
			
			String team2Title = match.getTeam2Name();
			if(ParamUtils.chkString(team2Title)){
				JcTeam team2 = jcService.getTeamByTitle(team2Title);
				if(team2!=null){
					match.setTeam2Name(String.valueOf(team2.getId()));
				}
			}
			
			matchForm.setMatch(match);
		}
		
		List<JcTeam> teamList = jcService.getTeamList();
		request.setAttribute("teamList", teamList);
		
		List<JcField> fieldList = jcService.getJcFieldByMid(match.getmId(), null);
		Integer fId = 0;
		if(fieldList!=null && fieldList.size()>0){
			fId = fieldList.get(0).getfId();
		}
		request.setAttribute("fieldList", fieldList);
		
		List<JcPlayType> playList = jcService.getJcPlayTypesByFid(fId, null);
		Integer playId = 0;
		if(playList!=null && playList.size()>0){
			playId = playList.get(0).getpTId();
		}
		request.setAttribute("playList", playList);
		
		List<JcOption> optionList = jcService.getJcOptionsByPtId(playId);
		request.setAttribute("optionList", optionList);
		
		return mapping.findForward("openResult");
	}
	
	public void getPlayTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer fId = ParamUtils.getIntegerParameter(request, "fId");
		
		List<JcPlayType> playList = null;
		if(ParamUtils.chkInteger(fId)){
			playList = jcService.getJcPlayNotOpen(fId);
		}
		
		JSONArray playListJson = null;
		if(playList!=null && playList.size()>0){
			playListJson = JSONArray.fromObject(playList);
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(playListJson!=null){
			out.println(playListJson.toString());
		}else{
			out.println("");
		}
		out.flush();
		out.close();
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
			Integer mId = ParamUtils.getIntegerParameter(request, "mId");
			JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, mId);
			String status = match.getIsRecommend();
			try {
				
				if (status.equals(JingcaiConstants.IS_RECOMMAND_YES)) {
					match.setIsRecommend(JingcaiConstants.IS_RECOMMAND_NO);
				} else {
					match.setIsRecommend(JingcaiConstants.IS_RECOMMAND_YES);
				}
				jcService.saveObject(match, null);
				
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
	
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType) && ParamUtils.chkInteger(mId)){
			JcMatch match = (JcMatch) jcService.getObject(JcMatch.class, mId);
			if(match!=null){
				jcService.deleteMatch(mId);
			}
		}
		
		return mapping.findForward("toInit");
	}
}
