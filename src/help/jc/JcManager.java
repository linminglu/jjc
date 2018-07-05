package help.jc;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.TradeCacheUtil;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.dto.GaBetDetailDTO;
import com.jc.JingcaiConstants;
import com.jc.model.JcField;
import com.jc.model.JcMatch;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.model.JcTeam;
import com.jc.model.Type;
import com.jc.model.dto.JcDTO;
import com.jc.service.IJcService;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class JcManager extends BaseDispatchAction {
	
	private final IJcService jcService = (IJcService) getService("jcService");
	private final IUserService userService = (IUserService) getService("userService");
	
	
	/**
	 * 分类 
	 */
	public void type(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type", "1");
		String firstId = ParamUtils.getParameter(deParameter, "firstId", "");// 一级分类id
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "操作成功";
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from Type t where 1=1");
		hq.addHsql(" and t.status=? ");
		hq.addPars(JingcaiConstants.TYPE_STATUS_YES);
		if(ParamUtils.chkString(type)){
			hq.addHsql(" and t.type=? ");
			hq.addPars(type);
		}
		if(ParamUtils.chkString(firstId)){
			hq.addHsql(" and t.parentId=? ");
			hq.addPars(firstId);
		}
		
		List<Object> list = jcService.findObjects(hq);
		if(list!=null && list.size()>0){
			JSONObject obj = null;
			for (Object object : list) {
				Type types = (Type) object;
				obj = new JSONObject();
				obj.put("tid", types.getTid());
				obj.put("title", types.getTitle());
				obj.put("img", types.getImg());
				items.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			data.put("items", items);
		}else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		data.put("items", items);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 比赛
	 */
	public void match(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer tId  = ParamUtils.getIntegerParameter(deParameter, "tId");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);//页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);//每页记录数量
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "操作成功";
		String message = "";
		
		if (!ParamUtils.chkInteger(tId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		HQUtils hql = new HQUtils();
		hql.addHsql(" from Type t where 1=1");
		hql.addHsql(" and t.parentId=? ");
		hql.addPars(tId);
		
		
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		
		if(message.equals("")){
			JSONObject tobj = null;
			List<Object> objList = jcService.findObjects(hql);
			if(objList!=null && objList.size()>0){
				Type typeo = (Type) objList.get(0);
				for (Object object : objList) {
					Type typec = (Type) object;
					tobj = new JSONObject();
					tobj.put("tid", typec.getTid());
					tobj.put("title", typec.getTitle());
					tobj.put("img", typec.getImg());
					
//					if(typeo.equals(typec)){
						
						HQUtils hq = new HQUtils();
						hq.addHsql(" from JcMatch m where 1=1");
						
						hq.addHsql(" and m.tId2=? ");
						hq.addPars(typec.getTid());
						
						hq.setPageSize(pageSize);
						hq.setStartIndex(statIndex);
						hq.setOrderby(" order by m.mId desc");
						JSONArray itemc = new JSONArray();
						PaginationSupport ps = userService.findObjectPage(hq);
						List<Object> list = ps.getItems();
						if(list!=null && list.size()>0){
							pageNum = list.size();
							JSONObject obj = null;
							for (Object objectc : list) {
								JcMatch match = (JcMatch) objectc;
								obj = new JSONObject();
								obj.put("mId", match.getmId());
								obj.put("title", match.getTitle());
								obj.put("red", match.getRed());// 红队
								obj.put("team1Name", match.getTeam1Name());
								obj.put("team1Img", match.getTeam1Img());
								obj.put("team2Name", match.getTeam2Name());
								obj.put("team2Img", match.getTeam2Img());
								obj.put("subTitle", match.getSubTitle());
								obj.put("matchTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringAll(match.getMatchTime())));
								obj.put("boNum", JingcaiConstants.getBoName(match.getBoNum()));
								
								itemc.put(obj);
							}
							tobj.put("items", itemc);
							data.put("total", ps.getTotalCount());// 总条数
						}
//					}
					
					items.put(tobj);
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}
			}else {
				code = APIConstants.CODE_NOT_FOUND;
				msg = APIConstants.TIPS_DATA_NOT;
			}
		}else{
			msg = message;
		}
		
		data.put("items", items);
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		data.put("items", items);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	public void matchType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer tId  = ParamUtils.getIntegerParameter(deParameter, "tId");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);//页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);//每页记录数量
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "操作成功";
		
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from JcMatch m where 1=1");
		
		if(ParamUtils.chkInteger(tId)){
			hq.addHsql(" and m.tId2=? ");
			hq.addPars(tId);
		}
		
		hq.setPageSize(pageSize);
		hq.setStartIndex(statIndex);
		hq.setOrderby(" order by m.matchTime asc");
		
		PaginationSupport ps = userService.findObjectPage(hq);
		List<Object> list = ps.getItems();
		if(list!=null && list.size()>0){
			pageNum = list.size();
			JSONObject obj = null;
			for (Object objectc : list) {
				JcMatch match = (JcMatch) objectc;
				obj = new JSONObject();
				obj.put("mId", match.getmId());
				obj.put("title", match.getTitle());
				obj.put("red", match.getRed());// 红队
				obj.put("team1Name", match.getTeam1Name());
				obj.put("team1Img", match.getTeam1Img());
				obj.put("team2Name", match.getTeam2Name());
				obj.put("team2Img", match.getTeam2Img());
				obj.put("subTitle", match.getSubTitle());
				obj.put("matchTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringAll(match.getMatchTime())));
				obj.put("boNum", JingcaiConstants.getBoName(match.getBoNum()));
				
				items.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			data.put("items", items);
			data.put("total", ps.getTotalCount());// 总条数
		}else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		
		
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		data.put("items", items);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 比赛局/场
	 */
	public void field(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer mId  = ParamUtils.getIntegerParameter(deParameter, "mId");
		String u = ParamUtils.getParameter(deParameter, "u");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "";
		
		if (!ParamUtils.chkInteger(mId)) {
			msg = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if (msg.equals("")) {
			HQUtils hq = new HQUtils();
			hq.addHsql(" from JcMatch m where 1=1");
			
			if(ParamUtils.chkInteger(mId)){
				hq.addHsql(" and m.mId=? ");
				hq.addPars(mId);
			}
			
			Object object = jcService.getObject(hq);
			JcMatch match = null;
			if(object!=null){
				match = (JcMatch) object;
			}else{
				msg = "比赛不存在";
				code = APIConstants.CODE_NOT_FOUND;
			}
			
			JSONObject obj = null;
			JSONObject objp = null;
			JSONObject objo = null;
			if(match!=null){
				obj = new JSONObject();
				obj.put("title", match.getTitle());
				obj.put("team1Name", match.getTeam1Name());
				obj.put("team1Img", match.getTeam1Img());
				obj.put("team2Name", match.getTeam2Name());
				obj.put("team2Img", match.getTeam2Img());
				obj.put("subTitle", match.getSubTitle());
				obj.put("matchTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringMMddHHmm(match.getMatchTime())));
				obj.put("boNum", JingcaiConstants.getBoName(match.getBoNum()));
				
				List<JcField> flist = jcService.getJcFieldByMid(match.getmId(),JingcaiConstants.STATUS_YES);
				if(flist!=null && flist.size()>0){
					for (JcField field : flist) {
						JSONObject fobj = new JSONObject();
						if(field!=null){
							fobj = new JSONObject();
							fobj.put("fId", field.getfId());
							fobj.put("fieldTitle", field.getTitle());
	//						fobj.put("mId", field.getmId());
	//						fobj.put("fieldStatus", field.getStatus());
							fobj.put("startDate", DateTimeUtil.DateToStringAll(field.getStartDate()));
							fobj.put("endDate", DateTimeUtil.DateToStringAll(field.getEndDate()));
							long endDate = field.getEndDate().getTime();
							long nowDate = new Date().getTime();
							
							String status = Constants.PUB_STATUS_CLOSE;// 状态0.不可以投注1.可以投注
							if (nowDate < endDate) {
								status = Constants.PUB_STATUS_OPEN;
							}
							fobj.put("status", status);
							
							JSONArray itemp = new JSONArray();// 数据集合
							
							List<JcPlayType> list = jcService.getJcPlayTypesByFid(field.getfId(),JingcaiConstants.STATUS_YES);
							if(list!=null && list.size()>0){
								for (JcPlayType playType : list) {
									objp = new JSONObject();
									objp.put("pTId", playType.getpTId());
	//								objp.put("fId", playType.getfId());
									objp.put("status", playType.getStatus());
									objp.put("openStatus", playType.getOpenStatus());// 开奖状态0=未开将 1=已开奖
									objp.put("playTitle", playType.getTitle());
									
									JSONArray itemo = null;// 数据集合
									
									List<JcOption> optionlist = jcService.getJcOptionsByPtId(playType.getpTId());
									if(optionlist!=null && optionlist.size()>0){
										// 竞猜比分
										if(optionlist.size()>13){
											for (int i = 0; i < optionlist.size(); i++) {
												if(i<=12){
													if(i==0){
														objp = new JSONObject();
														objp.put("pTId", playType.getpTId());
														objp.put("status", playType.getStatus());
														objp.put("playTitle", match.getTeam1Name()+"胜");
														
														itemo = new JSONArray();
													}
													
													JcOption option = optionlist.get(i);
													objo = new JSONObject();
													objo.put("oId", option.getoId());
													objo.put("optionTitle", option.getTitle());
													objo.put("rate", option.getRate());
													
													itemo.put(objo);
												}else if(i<=17){
													if(i==13){
														objp.put("itemo", itemo);
														itemp.put(objp);
														
														itemo = new JSONArray();
														
														objp = new JSONObject();
														objp.put("pTId", playType.getpTId());
														objp.put("status", playType.getStatus());
														objp.put("playTitle", "平局");
													}
													
													
													JcOption option = optionlist.get(i);
													objo = new JSONObject();
													objo.put("oId", option.getoId());
													objo.put("optionTitle", option.getTitle());
													objo.put("rate", option.getRate());
													
													itemo.put(objo);
												}else{
													if(i==18){
														objp.put("itemo", itemo);
														itemp.put(objp);
														
														itemo = new JSONArray();
														
														objp = new JSONObject();
														objp.put("pTId", playType.getpTId());
														objp.put("status", playType.getStatus());
														objp.put("playTitle", match.getTeam1Name()+"负");
													}
													
													
													JcOption option = optionlist.get(i);
													objo = new JSONObject();
													objo.put("oId", option.getoId());
													objo.put("optionTitle", option.getTitle());
													objo.put("rate", option.getRate());
													
													itemo.put(objo);
													
													if(i==optionlist.size()-1){
														objp.put("itemo", itemo);
														itemp.put(objp);
													}
												}
											}
										}else{
											// 竞猜其他
											itemo = new JSONArray();
											for (JcOption option : optionlist) {
												objo = new JSONObject();
												objo.put("oId", option.getoId());
												objo.put("optionTitle", option.getTitle());
												objo.put("rate", option.getRate());
												
												itemo.put(objo);
											}
											
											if(optionlist.size()<=13){
												objp.put("itemo", itemo);
												itemp.put(objp);
											}
										}
									}
								}
								fobj.put("pitems", itemp);
							}
							code = APIConstants.CODE_REQUEST_SUCCESS;
						}else{
							code = APIConstants.CODE_NOT_FOUND;
						}
						items.put(fobj);
					}
					obj.put("items", items);
					BigDecimal money = new BigDecimal(0);
					if (ParamUtils.chkString(u)) {
						Map<String, String> decryptMap = DesUtils.decryptMap(u);
						if(decryptMap!=null){
							Integer uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
							if (ParamUtils.chkInteger(uid)) {
								BigDecimal userMoney = TradeCacheUtil.getUserMoney(uid);
								if (userMoney != null) {
									money = userMoney;
								}
							}
						}
					}
					obj.put("money", ProductUtil.BigFormat(money));
					data.put("obj", obj);
				}else {
					code = APIConstants.CODE_NOT_FOUND;
					msg = APIConstants.TIPS_DATA_NOT;
				}
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 下注
	 */
	public void bet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String totalprice = ParamUtils.getParameter(deParameter, "totalprice");
		Integer oId = ParamUtils.getIntegerParameter(deParameter, "oId");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "";
		
		JcField field = null;
		JcOption option = null;
		if (!ParamUtils.chkString(ucode)|| !ParamUtils.chkString(totalprice) || !ParamUtils.chkInteger(oId)) {
			msg = APIConstants.PARAMS_EMPTY_MSG;
		}else{
			option = (JcOption) jcService.getObject(JcOption.class, oId);
			if(option!=null){
				field = (JcField) jcService.getObject(JcField.class, option.getfId());
				
				JcPlayType play = (JcPlayType) jcService.getObject(JcPlayType.class, option.getpTId());
				if(play!=null){
					String openResult = play.getOpenStatus();
					if(JingcaiConstants.QUIZ_STATUS_OPENING.equals(openResult)){
						msg = "该玩法已开奖不可再投注";
					}
				}else{
					msg = "玩法不存在";
				}
			}
			
		}
		
		if(field!=null){
			// 判断是否在投注时间内
			Date nowDate = new Date();
			Date endTime = field.getEndDate();
			Date startTime = field.getStartDate();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, JingcaiConstants.JINGCAI_TIME_OPENING_FREEZE);// 封盘时间
			long sec1 = DateTimeUtil.dateDiffToSec(startTime, nowDate);
			long sec2 = DateTimeUtil.dateDiffToSec(nowDate, fengPanTime);
			long sec3 = DateTimeUtil.dateDiffToSec(nowDate, endTime);
			if (sec1 < 0) {// 还没到投注时间
				msg = "本期还未开盘，请稍后";
			} else if (sec3 < 0) {// 过了开奖时间
				msg = "本期已结束，请选择其他投注";
			} else if (sec3 > 0 && sec2 < 0) {// 正好在封盘时间内
				msg = "本期已开盘，请等待下期投注";
			}
			
			BigDecimal money = BigDecimal.ZERO;
			if(ParamUtils.chkString(totalprice)){
				try {
					money = new BigDecimal(totalprice);
				} catch (Exception e) {
					msg = "投注金额有误！";
				}
			}else{
				msg = "投注金额为空！";
			}
			
			if(option==null){
				msg = "请选择正确的投注项";
			}
			
			if(!msg.equals("")){
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", msg);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
				return;
			}
			
			if(msg.equals("")){
				User user = null;
				Integer uid =0;
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				if(decryptMap!=null){
					uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
					user = userService.getUser(uid);
				}
				
				if(user==null){
					map.put("msg", "用户不存在");
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("data", data);
					JsonUtil.AjaxWriter(response, map);
				    return;
				}else{
					String status = user.getStatus();
				    if(Constants.PUB_STATUS_CLOSE.equals(status)){
					    msg = "用户无效";
					    map.put("code", APIConstants.CODE_REQUEST_ERROR);
					    map.put("msg", msg);
					    JsonUtil.AjaxWriter(response, map);
					    return;
				    }
				}
				
				BigDecimal balance = TradeCacheUtil.getUserMoney(uid);
				if(balance.compareTo(money)==-1){
					msg = "余额不足！";
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
				    map.put("msg", msg);
					JsonUtil.AjaxWriter(response, map);
				    return;
				}
				
				user = jcService.saveUserBetInfo(field, money, option, user);
				
				BigDecimal balance1=TradeCacheUtil.getUserMoney(uid);
				data.put("money", balance1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				code = APIConstants.CODE_REQUEST_SUCCESS;
				msg = "投注成功！";
			}
		}else{
			msg = "比赛局不存在";
			code  = APIConstants.CODE_NOT_FOUND;
		}
		
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public void recommend(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Map<String, String> deParameter = ParamUtils.deParameter(request);
//		Integer tId  = ParamUtils.getIntegerParameter(deParameter, "tId");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);//页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);//每页记录数量
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "操作成功";
		
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from JcMatch m where 1=1");
		hq.addHsql(" and m.isRecommend=? ");
		hq.addPars(JingcaiConstants.IS_RECOMMAND_YES);
		
//		if(ParamUtils.chkInteger(tId)){
//			hq.addHsql(" and m.tId2=? ");
//			hq.addPars(tId);
//		}
		
		hq.setPageSize(pageSize);
		hq.setStartIndex(statIndex);
		hq.setOrderby(" order by m.matchTime asc");
		
		PaginationSupport ps = userService.findObjectPage(hq);
		List<Object> list = ps.getItems();
		if(list!=null && list.size()>0){
			pageNum = list.size();
			JSONObject obj = null;
			for (Object objectc : list) {
				JcMatch match = (JcMatch) objectc;
				obj = new JSONObject();
				obj.put("mId", match.getmId());
				obj.put("title", match.getTitle());
				obj.put("red", match.getRed());// 红队
				obj.put("team1Name", match.getTeam1Name());
				obj.put("team1Img", match.getTeam1Img());
				obj.put("team2Name", match.getTeam2Name());
				obj.put("team2Img", match.getTeam2Img());
				obj.put("subTitle", match.getSubTitle());
				obj.put("matchTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringAll(match.getMatchTime())));
				obj.put("boNum", JingcaiConstants.getBoName(match.getBoNum()));
				
				items.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			data.put("items", items);
			data.put("total", ps.getTotalCount());// 总条数
		}else {
			code = APIConstants.CODE_NOT_FOUND;
		}
		
		
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		data.put("items", items);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 比赛下的投注项(开奖用)
	 */
	public void jcOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer mId = ParamUtils.getIntegerParameter(request, "mId");// 比赛id
		
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		if (!ParamUtils.chkInteger(mId)) {
			message = "必要参数为空";
		}
		
		if (message.equals("")) {
			HQUtils hq = new HQUtils("select new com.jc.model.dto.JcDTO"
					+ "(jf.fId, jf.title, jp.pTId, jp.title, jp.openResult, jp.openStatus, jo.oId, jo.title, jo.rate)"
					+ " from JcField jf, JcPlayType jp, JcOption jo where jf.fId=jp.fId and jp.pTId=jo.pTId ");
			hq.addHsql(" and jf.mId=? ");
			hq.addPars(mId);
			//hq.addHsql(" order by jo.fId desc ");
			List<Object> list = jcService.findObjects(hq);
			JSONArray items = new JSONArray();// 比赛数组
			if (list != null & list.size() > 0) {
				for (Object obj : list) {
					JcDTO dto = (JcDTO) obj;
					Integer itemsLength = items.length();
					Integer fId = dto.getfId();// 局id
					String fieldTitle = dto.getFieldTitle();// 局名称
					
					Integer pTId = dto.getpTId();// 玩法id
					String playTitle = dto.getPlayTitle();// 玩法名称
					String openResult = dto.getOpenResult();// 开奖结果
					String openStatus = dto.getOpenStatus();// 开奖状态0未开奖1已开奖
					
					Integer optionId = dto.getOptionId();// 投注项id
					String optionTitle = dto.getOptionTitle();// 投注项名称
					BigDecimal rate = dto.getRate();// 赔率
					if (itemsLength==0) {
						JSONObject fieldObj = new JSONObject();// 局对象
						fieldObj.put("fId", fId);
						fieldObj.put("fieldTitle", ParamUtils.chkStringNotNull(fieldTitle));
						
						JSONArray playArr = new JSONArray();// 玩法数组
						JSONObject playObj = new JSONObject();// 玩法对象
						playObj.put("pTId", pTId);
						playObj.put("playTitle", ParamUtils.chkStringNotNull(playTitle));
						playObj.put("openResult", ParamUtils.chkStringNotNull(openResult));
						playObj.put("openStatus", ParamUtils.chkStringNotNull(openStatus));
						
						JSONArray optionArr = new JSONArray();// 投注项数组
						JSONObject optionObj = new JSONObject();// 投注项对象
						optionObj.put("optionId", optionId);
						optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
						optionObj.put("rate", ProductUtil.BigFormat(rate));
						
						optionArr.put(optionObj);
						playObj.put("optionArr", optionArr);
						playArr.put(playObj);
						fieldObj.put("playArr", playArr);
						items.put(fieldObj);
					} else {
						JSONObject lastFieldObj = items.getJSONObject(itemsLength - 1);// items里的最后一条数据
						Integer lastFId = lastFieldObj.getInt("fId");
						if (fId.intValue() == lastFId.intValue()) {// 属于同一局
							JSONArray lastPlayArr = lastFieldObj.getJSONArray("playArr");// 玩法数组
							JSONObject lastPlayObj = lastPlayArr.getJSONObject(lastPlayArr.length() - 1);// 最后一个玩法对象
							Integer lastpTId = lastPlayObj.getInt("pTId");
							if (lastpTId.intValue() == pTId.intValue()) {// 属于同一玩法
								JSONArray lastOptionArr = lastPlayObj.getJSONArray("optionArr");// 投注项数组
								JSONObject optionObj = new JSONObject();// 投注项对象
								optionObj.put("optionId", optionId);
								optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
								optionObj.put("rate", ProductUtil.BigFormat(rate));
								lastOptionArr.put(optionObj);
							} else {// 不属于同一玩法
								JSONObject playObj = new JSONObject();// 玩法对象
								playObj.put("pTId", pTId);
								playObj.put("playTitle", ParamUtils.chkStringNotNull(playTitle));
								playObj.put("openResult", ParamUtils.chkStringNotNull(openResult));
								playObj.put("openStatus", ParamUtils.chkStringNotNull(openStatus));
								
								JSONArray optionArr = new JSONArray();// 投注项数组
								JSONObject optionObj = new JSONObject();// 投注项对象
								optionObj.put("optionId", optionId);
								optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
								optionObj.put("rate", ProductUtil.BigFormat(rate));
								
								optionArr.put(optionObj);
								playObj.put("optionArr", optionArr);
								lastPlayArr.put(playObj);
							}
						} else {// 不属于同一局
							JSONObject fieldObj = new JSONObject();// 局对象
							fieldObj.put("fId", fId);
							fieldObj.put("fieldTitle", ParamUtils.chkStringNotNull(fieldTitle));
							
							JSONArray playArr = new JSONArray();// 玩法数组
							JSONObject playObj = new JSONObject();// 玩法对象
							playObj.put("pTId", pTId);
							playObj.put("playTitle", ParamUtils.chkStringNotNull(playTitle));
							playObj.put("openResult", ParamUtils.chkStringNotNull(openResult));
							playObj.put("openStatus", ParamUtils.chkStringNotNull(openStatus));
							
							JSONArray optionArr = new JSONArray();// 投注项数组
							JSONObject optionObj = new JSONObject();// 投注项对象
							optionObj.put("optionId", optionId);
							optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
							optionObj.put("rate", ProductUtil.BigFormat(rate));
							
							optionArr.put(optionObj);
							playObj.put("optionArr", optionArr);
							playArr.put(playObj);
							fieldObj.put("playArr", playArr);
							items.put(fieldObj);
						}
					}
//					if (itemsLength==0) {
//						JSONObject macthObj = new JSONObject();// 比赛对象
//						macthObj.put("matchId", matchId);
//						macthObj.put("playName", ParamUtils.chkStringNotNull(playName));
//						macthObj.put("betTime", DateTimeUtil.DateToStringMMddHHmm(betTime));
//						macthObj.put("winResult", ParamUtils.chkStringNotNull(winResult));
//						
//						JSONArray roomArr = new JSONArray();// 场次数组
//						JSONObject roomObj = new JSONObject();// 场次对象
//						roomObj.put("room", ParamUtils.chkStringNotNull(room));
//						roomObj.put("sessionId", sessionId);
//						
//						JSONArray optionArr = new JSONArray();// 投注项数组
//						JSONObject optionObj = new JSONObject();// 投注项对象
//						optionObj.put("betMoney", betMoney);
//						optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
//						optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
//						optionObj.put("betRate", ProductUtil.BigFormat(betRate));
//						optionObj.put("winCash", ProductUtil.BigFormat(winCash));
//						
//						optionArr.put(optionObj);
//						roomObj.put("optionArr", optionArr);
//						roomArr.put(roomObj);
//						macthObj.put("roomArr", roomArr);
//						items.put(macthObj);
//					} else {
//						JSONObject lastMacthObj = items.getJSONObject(itemsLength - 1);// items里的最后一条数据
//						Integer lastMatchId = lastMacthObj.getInt("matchId");
//						if (matchId.intValue() == lastMatchId.intValue()) {// 属于同一比赛
//							JSONArray lastRoomArr = lastMacthObj.getJSONArray("roomArr");// 场次数组
//							JSONObject lastRoomObj = lastRoomArr.getJSONObject(lastRoomArr.length() - 1);// 最后一个场次对象
//							Integer lastsessionId = lastRoomObj.getInt("sessionId");
//							if (sessionId.intValue() == lastsessionId.intValue()) {// 属于同一场
//								JSONArray lastOptionArr = lastRoomObj.getJSONArray("optionArr");// 投注项数组
//								JSONObject optionObj = new JSONObject();// 投注项对象
//								optionObj.put("betMoney", betMoney);
//								optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
//								optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
//								optionObj.put("betRate", ProductUtil.BigFormat(betRate));
//								optionObj.put("winCash", ProductUtil.BigFormat(winCash));
//								lastOptionArr.put(optionObj);
//							} else {// 不属于同一场
//								JSONObject roomObj = new JSONObject();// 场次对象
//								roomObj.put("room", ParamUtils.chkStringNotNull(room));
//								roomObj.put("sessionId", sessionId);
//								
//								JSONArray optionArr = new JSONArray();// 投注项数组
//								JSONObject optionObj = new JSONObject();// 投注项对象
//								optionObj.put("betMoney", betMoney);
//								optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
//								optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
//								optionObj.put("betRate", ProductUtil.BigFormat(betRate));
//								optionObj.put("winCash", ProductUtil.BigFormat(winCash));
//								
//								optionArr.put(optionObj);
//								roomObj.put("optionArr", optionArr);
//								lastRoomArr.put(roomObj);
//							}
//						} else {// 不属于同一比赛
//							JSONObject macthObj = new JSONObject();// 比赛对象
//							macthObj.put("matchId", matchId);
//							macthObj.put("playName", ParamUtils.chkStringNotNull(playName));
//							macthObj.put("betTime", DateTimeUtil.DateToStringMMddHHmm(betTime));
//							macthObj.put("winResult", ParamUtils.chkStringNotNull(winResult));
//							
//							JSONArray roomArr = new JSONArray();// 场次数组
//							JSONObject roomObj = new JSONObject();// 场次对象
//							roomObj.put("room", ParamUtils.chkStringNotNull(room));
//							roomObj.put("sessionId", sessionId);
//							
//							JSONArray optionArr = new JSONArray();// 投注项数组
//							JSONObject optionObj = new JSONObject();// 投注项对象
//							optionObj.put("betMoney", betMoney);
//							optionObj.put("optionTitle", ParamUtils.chkStringNotNull(optionTitle));
//							optionObj.put("betMoney", ProductUtil.BigFormat(betMoney));
//							optionObj.put("betRate", ProductUtil.BigFormat(betRate));
//							optionObj.put("winCash", ProductUtil.BigFormat(winCash));
//							
//							optionArr.put(optionObj);
//							roomObj.put("optionArr", optionArr);
//							roomArr.put(roomObj);
//							macthObj.put("roomArr", roomArr);
//							items.put(macthObj);
//						}
//					}
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "操作成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = "没有更多了";
			}
			data.put("items", items);
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 队伍 
	 */
	public void teamList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String secondId = ParamUtils.getParameter(deParameter, "secondId", "");// 二级分类id
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "操作成功";
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from JcTeam t where 1=1 ");
		if(ParamUtils.chkString(secondId)){
			hq.addHsql(" and t.tId2=? ");
			hq.addPars(secondId);
		}
		
		List<Object> list = jcService.findObjects(hq);
		if(list!=null && list.size()>0){
			JSONObject obj = null;
			for (Object object : list) {
				JcTeam team = (JcTeam) object;
				obj = new JSONObject();
				obj.put("tid", team.getId());
				obj.put("title", team.getTitle());
				items.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			data.put("items", items);
		}else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		data.put("items", items);
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	/**
	 * 保存比赛 (后台)
	 */
	public void saveMatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String param = ParamUtils.getParameter(request, "param", "");// json格式的字符串
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		
		if (!ParamUtils.chkString(param)) {
			msg = APIConstants.PARAMS_EMPTY_MSG;
		} else {
			String result=jcService.saveMatch(param);
			if (APIConstants.CODE_REQUEST_SUCCESS.equals(result)) {
				code = APIConstants.CODE_REQUEST_SUCCESS;
				msg = "保存成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				msg = "请填写完整信息";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public void getMatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer mId  = ParamUtils.getIntegerParameter(deParameter, "mId");
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		
		if (!ParamUtils.chkInteger(mId)) {
			msg = APIConstants.PARAMS_EMPTY_MSG;
		}
		
		if (msg.equals("")) {
			HQUtils hq = new HQUtils();
			hq.addHsql(" from JcMatch m where 1=1");
			
			if(ParamUtils.chkInteger(mId)){
				hq.addHsql(" and m.mId=? ");
				hq.addPars(mId);
			}
			
			Object object = jcService.getObject(hq);
			JcMatch match = null;
			if(object!=null){
				match = (JcMatch) object;
			}else{
				msg = "比赛不存在";
				code = APIConstants.CODE_NOT_FOUND;
			}
			
			JSONObject obj = null;
			JSONObject objp = null;
			JSONObject objo = null;
			if(match!=null){
				obj = new JSONObject();
				obj.put("title", match.getTitle());
				JcTeam team1 = jcService.getTeamByTitle(match.getTeam1Name());
				if(team1!=null){
					obj.put("team1Name", team1.getId());
				}
				JcTeam team2 = jcService.getTeamByTitle(match.getTeam2Name());
				if(team2!=null){
					obj.put("team2Name", team2.getId());
				}
				obj.put("subTitle", match.getSubTitle());
				obj.put("openTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringAll(match.getOpenTime())));
				obj.put("matchTime", ParamUtils.chkStringNotNull(DateTimeUtil.DateToStringAll(match.getMatchTime())));
				obj.put("boNum", match.getBoNum());
				obj.put("tid1", match.gettId1());
				obj.put("tid2", match.gettId2());
				if(match.getRed().equals(team1.getTitle())){
					obj.put("red", team1.getId());
				}else if(match.getRed().equals(team2.getTitle())){
					obj.put("red", team2.getId());
				}
				if(match.getBlue().equals(team1.getTitle())){
					obj.put("blue", team1.getId());
				}else if(match.getBlue().equals(team2.getTitle())){
					obj.put("blue", team2.getId());
				}
				obj.put("type", match.getType());
				obj.put("rercentage", match.getRercentage());
				
				List<JcField> flist = jcService.getJcFieldByMid(match.getmId(),null);
				if(flist!=null && flist.size()>0){
					for (JcField field : flist) {
						JSONObject fobj = new JSONObject();
						if(field!=null){
							fobj = new JSONObject();
							fobj.put("fId", field.getfId());
							fobj.put("fieldTitle", field.getTitle());
	//						fobj.put("mId", field.getmId());
	//						fobj.put("fieldStatus", field.getStatus());
							fobj.put("startDate", DateTimeUtil.DateToStringAll(field.getStartDate()));
							fobj.put("endDate", DateTimeUtil.DateToStringAll(field.getEndDate()));
							
							JSONArray itemp = new JSONArray();// 数据集合
							
							List<JcPlayType> list = jcService.getJcPlayTypesByFid(field.getfId(),null);
							if(list!=null && list.size()>0){
								for (JcPlayType playType : list) {
									objp = new JSONObject();
									objp.put("pTId", playType.getpTId());
//									objp.put("fId", playType.getfId());
//									objp.put("status", playType.getStatus());
//									objp.put("openStatus", playType.getOpenStatus());// 开奖状态0=未开将 1=已开奖
									objp.put("playTitle", playType.getTitle());
									
									JSONArray itemo = null;// 数据集合
									
									List<JcOption> optionlist = jcService.getJcOptionsByPtId(playType.getpTId());
									if(optionlist!=null && optionlist.size()>0){
										// 竞猜其他
										itemo = new JSONArray();
										for (JcOption option : optionlist) {
											objo = new JSONObject();
											objo.put("oId", option.getoId());
											objo.put("optionTitle", option.getTitle());
											objo.put("rate", option.getRate());
											
											itemo.put(objo);
										}
									}
									objp.put("itemo", itemo);
									itemp.put(objp);
								}
								fobj.put("pitems", itemp);
							}
						}
						items.put(fobj);
					}
					obj.put("items", items);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}else{
				code = APIConstants.CODE_NOT_FOUND;
			}
			data.put("obj", obj);
		}
		
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	public void saveUpdateMatch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String param = ParamUtils.getParameter(request, "param", "");// json格式的字符串
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray items = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		
		if (!ParamUtils.chkString(param)) {
			msg = APIConstants.PARAMS_EMPTY_MSG;
		} else {
			String result=jcService.saveUpdateMatch(param);
			if (APIConstants.CODE_REQUEST_SUCCESS.equals(result)) {
				code = APIConstants.CODE_REQUEST_SUCCESS;
				msg = "保存成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				msg = "请填写完整信息";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
}
