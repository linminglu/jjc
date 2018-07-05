package help.xy.hk;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.model.Param;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.dto.WinCoDTO;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.model.User;
import com.xy.hk.marksix.MarkSixConstants;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.MarkSixGaTrend;
import com.xy.hk.marksix.service.IMarkSixService;
import com.xy.hk.marksix.util.MarkSixUtil;

public class MarkSixManager extends BaseDispatchAction {
	private final IMarkSixService markSixService = (IMarkSixService) getService("markSixService");
	private final IGaService gaService = (IGaService) getService("gaService");

	/**
	 * 当前期开奖信息.
	 */
	public void currentTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型 1.IOS2安卓
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		JSONObject map = new JSONObject();// 最外层

		JSONObject data = new JSONObject();// 返回数据层
		MarkSixGaSession gaSixSession = markSixService.getCurrentSession(new Date());
		if (gaSixSession != null) {
			JSONObject obj = new JSONObject();// 返回数据层

			if (ParamUtils.chkString(ucode)) {
				User user =null;
				Integer uid =0;
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				if(decryptMap!=null){
					uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
					user = (User) userService.getObject(User.class, uid);
				}
				BigDecimal money =null;
				if (user != null) {
					BigDecimal balance = user.getUserBalance();
					money = TradeCacheUtil.getUserMoney(user.getUserId());
					if (balance == null) {
						obj.put("gains", "0.0");
					} else {
						obj.put("gains",
								user.getUserBalance()
										.setScale(2, BigDecimal.ROUND_HALF_UP)
										.toString());
					}
				} else {
					obj.put("gains", "0.0");
				}
				if (money == null)
					money = new BigDecimal(0);// 判空处理
				obj.put("money",money.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			} else {
				obj.put("gains", "0.0");
				obj.put("money", "0");
			}

			long openTimeMill = (gaSixSession.getEndTime().getTime() - System
					.currentTimeMillis())/1000; 
//			long openTime = openTimeMill - MarkSixConstants.MARKSIX_TIME_OPENING_FREEZE;
			long betTime=0;
			if(openTimeMill >= MarkSixConstants.MARKSIX_TIME_OPENING_FREEZE){
				betTime = openTimeMill - MarkSixConstants.MARKSIX_TIME_OPENING_FREEZE;
			}

			obj.put("gameName", "香港六合彩");
			obj.put("room", "");
			obj.put("sessionNo", gaSixSession.getSessionNo());
			obj.put("openTime", Long.toString(openTimeMill)); //距离当前期开奖时间。格式为秒
			obj.put("betTime", betTime); // 截止投注时间 格式为秒
			String format="MM/dd HH:mm";
			String openDate = DateTimeUtil.dateToString(gaSixSession.getEndTime(), format);
			obj.put("openDate", openDate); //开奖时间
			MarkSixGaSession beforSession = markSixService
					.getPreviousSessionBySessionNo((Integer.parseInt(gaSixSession
							.getSessionNo()) - 1) + "");
			if (beforSession == null) {
				beforSession = (MarkSixGaSession) markSixService.getObject(
						MarkSixGaSession.class, gaSixSession.getSessionId() - 1);
			}

			if (beforSession != null) {
				obj.put("lastSessionNo",
						beforSession.getSessionNo() == null ? "" : beforSession
								.getSessionNo());
				JSONArray openResult = new JSONArray();
				if (ParamUtils.chkString(beforSession.getOpenResult())) {
					String[] results = beforSession.getOpenResult().split(",|，|\\+");
					for (int i = 0; i < results.length; i++) {
						openResult.put(results[i]);
					}
				} else {
					log.info("--------香港六合彩开奖结果为空--------");
				}
				obj.put("openResult", openResult);
			} else {
				map.put("msg", "第一期");
				obj.put("sessionNo", "");
				obj.put("openResult", new JSONArray());
			}
			String isWeb="0";
			String webUrl="";
			if(device.equals("2")&&"1.0.0".equals(ver)){
				isWeb="1";
				webUrl=StringUtil.checkAPIHttpUrl("/game/init.jsp?t=&m=");//t游戏类型m加密
			}
			obj.put("isWeb", isWeb);
			obj.put("webUrl", webUrl);
			data.put("obj", obj);
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "操作成功！");
			map.put("data", data);
		} else {
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "开奖时间未到！");
			map.put("data", data);
		}
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 面板选项
	 * 
	 * @param mapping
	 */

	public void betPanel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String playType = ParamUtils.getParameter(deParameter, "playType", "0");// 玩法类型
		// 0=特码
		// 1=正码1-6球
		// 2=特码生肖
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String gameName = "markSix0";
		int type = Integer.parseInt(playType);
		data.put("quickBet", "0"); // 0为可以自选下注与快捷下注   1为只能快捷下注。
		data.put("minBet", 0);
		data.put("maxBet", 100);
		data.put("combination", "0"); //0为不必排列组合，前台可以自己提交投注， 1为必须先排列组合投注项。
		for(int i=0; i<=51; i++){
			if(type == i){
				gameName = "markSix" + i;
			}
			if(type==0||type==1){
				data.put("quickBet", "0");
				data.put("minBet", 1);
				data.put("maxBet", 49);
				data.put("combination", "0");
			}else if(type==10){ //过关    过关不应该是排列组合。
				data.put("quickBet", "1");
				data.put("minBet", 2);
				data.put("maxBet", 8);
				data.put("combination", "1");
			}else if(type == 11 || type == 12 || type == 13 || type ==31 || type==35 
					|| type==38 || type==41){ //二全中,二中特，特串,二肖连中,二肖连不中,38二尾连中，41二尾连不中
				data.put("quickBet", "1");                                 
				data.put("minBet", 2);
				data.put("maxBet", 5);
				data.put("combination", "1");
			}else if(type == 14 || type == 15 || type == 32 || type==36 || type==39|| type== 42){
				//三全中,三中二,三肖连中,三肖连不中,39三尾连中,42三尾连不中
				data.put("quickBet", "1");
				data.put("minBet", 3);
				data.put("maxBet", 6);
				data.put("combination", "1");
			}else if(type == 16 || type == 33 || type==37|| type== 40 || type==43){ //四全中,四肖连中,四肖连不中,40四尾连中,43四尾连不中
				data.put("quickBet", "1");
				data.put("minBet", 4);
				data.put("maxBet", 7);
				data.put("combination", "1");
			}else if(type == 34){ //五肖连中
				data.put("quickBet", "1");
				data.put("minBet", 5);
				data.put("maxBet", 8);
				data.put("combination", "1");
			}else if(type >=21 && type <= 30){
				for(int j=2; j <= 11; j++){
					if(type == (19 +j)){//二肖---十一肖   必须排列组合。
						data.put("quickBet", "1");
						data.put("minBet", j);
						data.put("maxBet", j);
						data.put("combination", "1");
					}
				}
			}else if(type>=44 && type<=51){ // 全不中  5全不中-12全不中
				data.put("quickBet", "1");
				data.put("minBet", type-39);
				data.put("maxBet", type-36);
				data.put("combination", "1");
			}
		}
		
		String itemsString = CacheUtil.getLotteryBetting(gameName);
		JSONArray betItems = new JSONArray(itemsString);
		data.put("betItems", betItems);
		map.put("data", data);
		map.put("msg", "操作成功");
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 下注
	 */
	public void bet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String sessionNo = ParamUtils
				.getParameter(deParameter, "sessionNo", "");
		String optionArray = ParamUtils.getParameter(deParameter,
				"optionArray", "");// [{optionId:12,betPoints:2}]
		
		String room = ParamUtils.getParameter(deParameter, "room", "A");
		String ucode = ParamUtils.getParameter(deParameter, "u");

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
		if (!ParamUtils.chkString(ucode) || !ParamUtils.chkString(optionArray)
				|| !ParamUtils.chkString(sessionNo)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		} else {// 判断是否在投注时间内
			MarkSixGaSession ga3Session = markSixService
					.getPreviousSessionBySessionNo(sessionNo);
			MarkSixGaSession tempSession = (MarkSixGaSession) markSixService.getObject(MarkSixGaSession.class, ga3Session.getSessionId()-1);
			Date nowDate = new Date();
			Date startTime = tempSession.getEndTime();
			Date endTime = ga3Session.getEndTime();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -(int)MarkSixConstants.MARKSIX_TIME_OPENING_FREEZE);// 封盘时间
			long sec1 = DateTimeUtil.dateDiffToSec(startTime, nowDate);
			long sec2 = DateTimeUtil.dateDiffToSec(nowDate, fengPanTime);
			long sec3 = DateTimeUtil.dateDiffToSec(nowDate, endTime);
			if (sec1 < 0) {// 还没到投注时间
				message = "本期还未开盘，请稍后";
			} else if (sec3 < 0) {// 过了开奖时间
				message = "本期已结束，请选择其他投注";
			} else if (sec3 > 0 && sec2 < 0) {// 正好在封盘时间内
				message = "本期已开盘，请等待下期投注";
			}
			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_MARKSIX);
			if(Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getStatus()) || 
					Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getBetAvoid())){
				Param tip = gaService.getParam(Constants.PARAM_LOTTERY_CLOSE_TIP);
				if(tip!=null&&ParamUtils.chkString(tip.getValue())){
					message = tip.getValue();
				}else{
					message = "彩种维护";
				}
			}
		}

		if (message.equals("")) {
			MarkSixGaSession ga3Session = markSixService.getCurrentSession(new Date());
			if (sessionNo.equals(ga3Session.getSessionNo())) {
				User user = null;
				Integer uid =0;
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				if(decryptMap!=null){
					uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
					user = userService.getValidUser(uid);
				}
				if (user == null) {
					message = "用户不存在";
				} else {
					
					String status = user.getStatus();
				    if(Constants.PUB_STATUS_CLOSE.equals(status)){
					    code = APIConstants.CODE_REQUEST_ERROR;
					    message = "用户无效";
					    map.put("code", code);
					    map.put("msg", message);
					    JsonUtil.AjaxWriter(response, map);
					    return;
				    }
					
					HQUtils hq = new HQUtils(
							"select sum(ho.betMoney) from GaBetDetail ho where 1=1 ");
					hq.addHsql(" and ho.gameType=?  ");
					hq.addPars(Constants.GAME_TYPE_XY_MARKSIX);
					hq.addHsql(" and ho.sessionId=?  ");
					hq.addPars(ga3Session.getSessionId());
					hq.addHsql(" and ho.userId=?  ");
					hq.addPars(uid);
					List<Object> moneylist = gaService.findObjects(hq);
					Integer betMoney = 0; // 单期已投注的总额。
					if (moneylist != null && moneylist.size() > 0) {
						if (moneylist.get(0) != null) {
							betMoney = (Integer) moneylist.get(0);
						} else {
							betMoney = 0;
						}
					}
					BigDecimal balance = TradeCacheUtil.getUserMoney(uid);
					if (balance == null)
						balance = new BigDecimal(0);// 判空处理
					
					JSONArray jsonArray = new JSONArray(optionArray);
					BigDecimal betAll = new BigDecimal(0);// 总投注额
					String ids = "";
					Map<Integer, Integer> betMap = new HashMap<Integer, Integer>();
					
					JSONObject jsonObj0 = jsonArray.getJSONObject(0);
					Integer type=0;
					Integer betPoint0=0;
					if (jsonObj0.get("id") != null) {
						GaBetOption  option0=(GaBetOption) gaService.getObject(GaBetOption.class, 
								Integer.parseInt(jsonObj0.get("id").toString()));
						betPoint0 = Integer.parseInt(jsonObj0.get(
								"p").toString()); 
						type=Integer.parseInt(option0.getPlayType());
					}
					int betNumber=0;
					if(type == 11 || type == 12 || type == 13 || type ==31 || type==35 
							|| type==38 || type==41){ //二全中,二中特，特串,二肖连中,二肖连不中,38二尾连中，41二尾连不中
						if(jsonArray.length() >= 2 && jsonArray.length() <= 5){
							betNumber = MarkSixUtil.getTotal(jsonArray.length(), 2);
						}
					}else if(type == 14 || type == 15 || type == 32 || type==36 || type==39|| type== 42){
						//三全中,三中二,三肖连中,三肖连不中,39三尾连中,42三尾连不中
						if(jsonArray.length() >= 3 && jsonArray.length() <= 6){
							betNumber = MarkSixUtil.getTotal(jsonArray.length(), 3);
						}
					}else if(type == 16 || type == 33 || type==37|| type== 40 || type==43){ //四全中,四肖连中,四肖连不中,40四尾连中,43四尾连不中
						if(jsonArray.length() >= 4 && jsonArray.length() <= 7){
							betNumber = MarkSixUtil.getTotal(jsonArray.length(), 4);
						}
					}else if(type == 34){ //五肖连中
						if(jsonArray.length() >= 5 && jsonArray.length() <= 8){
							betNumber = MarkSixUtil.getTotal(jsonArray.length(), 5);
						}
					}else if(type>=44 && type<=51){ // 全不中  5全不中-12全不中
						if(jsonArray.length() >= type-39 && jsonArray.length() <= type-36){
							betNumber = MarkSixUtil.getTotal(jsonArray.length(), type-39);
						}
					}else if(type == 10 || (type >=21 && type <= 30)){ // 过关 ,合肖
						if(jsonArray.length() <= 0){
						}else{
							betNumber = 1;
						}
					}else{
						betNumber=jsonArray.length();
					}
					if(betNumber==0){
						message = "参数不正确！";
						map.put("code", code);
						map.put("msg", message);
						data.put("money", ProductUtil.BigFormatJud(balance));
						map.put("data", data);
						JsonUtil.AjaxWriter(response, map);
						return;
					}else{				
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObj = jsonArray.getJSONObject(i);						
							if (jsonObj.get("id") != null) {
								Integer betPoint = Integer.parseInt(jsonObj.get(
										"p").toString()); // 单注投注金额
								if (betPoint > MarkSixConstants.MARKSIX_MAX_BET_POINT) {
									message = "单注不能超过"+MarkSixConstants.MARKSIX_MAX_BET_POINT+"！";
									break;
								}
								ids = ids + jsonObj.get("id").toString()
										+ ",";
								betMap.put(Integer.parseInt(jsonObj.get("id")
										.toString()), Integer.parseInt(jsonObj.get(
										"p").toString()));
								if(!ParamUtils.chkInteger(betPoint)){
									message="投注不能为0！";
									map.put("code",APIConstants.CODE_REQUEST_ERROR);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
							} else {
								message="参数不正确！";
								break;
							}
						}
						if((type>=0&&type<=9)||(type>=17&&type<=20)){
							for(Integer id:betMap.keySet()){
								betAll=betAll.add(new BigDecimal(betMap.get(id)));
							}
						}else{
							betAll = betAll.add(new BigDecimal(betPoint0).multiply(new BigDecimal(betNumber)));
						}
	
					}
			

					if (message.equals("")) {
						if (ParamUtils.chkString(ids)) {
							ids = ids.substring(0, ids.length() - 1);
//							if (betAll.intValue() + betMoney > MarkSixConstants.MARKSIX_MAX_TOTAL_BET_POINT) {
//								data.put("money", ProductUtil
//										.BigFormatJud(balance));
//								message = "单期投注总额不能超过50000";
//							} else 
							if (betAll.intValue() > balance.intValue()) {
								data.put("money", ProductUtil
										.BigFormatJud(balance));
								message = "您的积分不足！";
							} else {
								List<GaBetOption> list = gaService
										.getGaBetOptionByIds(ids);
								for (GaBetOption option : list) {
									String gameType=option.getGameType();
									if(!Constants.GAME_TYPE_XY_MARKSIX.equals(gameType)){
										map.put("code", APIConstants.CODE_REQUEST_ERROR);
										map.put("msg", "请选择正确投注项");
										JsonUtil.AjaxWriter(response, map);
										return;
									}
								}
							
								user=markSixService.saveUserBetInfo(room, betMap, list,
										ga3Session, user, betAll);
								data.put("money", ProductUtil.BigFormatJud(TradeCacheUtil.getUserMoney(uid)));
								code = APIConstants.CODE_REQUEST_SUCCESS;
								message = "投注成功！";
							}
						} else {
							message = "请选择投注项";
						}
					}
				}
			} else {
				message = "已封盘，投注无效";
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 冷热排行
	 */
	public void hotRanking(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		List<MarkSixGaTrend> list = markSixService.findMarkSixTrendList();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (MarkSixGaTrend trend : list) {
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("title", trend.getTrendTitle());
				obj.put("num", trend.getTrendCount());
				jsonArray.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
			log.info("--------香港六合彩冷热排行榜为空。-------");
		}
		data.put("items", jsonArray);
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 走势列表
	 */
	public void trend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=号码 
																	// 1=特码
																	// 2=正码
		int pageNum = ParamUtils.getIntParameter(deParameter, "pgnm", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pgts", 12);// 只查询12条
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = markSixService.findMarkSixGaSessionList(hqls.toString(),
				para, pageNum, pageSize);
		List<MarkSixGaSession> list = ps.getItems();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			if (type.equals("0")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("一");
				items.put("二");
				items.put("三");
				items.put("四");
				items.put("五");
				items.put("六");
				items.put("七");
				jsonArray.put(items);
				for (MarkSixGaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					String array[] = results.split(",|，|\\+");
					for (int i = 0; i < array.length; i++) {
						items.put(array[i]);
					}
					jsonArray.put(items);
				}
			} else if (type.equals("1")) { //特码
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("大小");
				items.put("单双");
				items.put("尾大小");
				items.put("禽/兽");
				items.put("波色");
				jsonArray.put(items);
				for (MarkSixGaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getSpecialCode(results, items,session.getStartTime());
					jsonArray.put(items);
				}
			} else if (type.equals("2")) { //正码
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("大小");
				items.put("单双");
				items.put("尾大小");
				items.put("龙虎");
				jsonArray.put(items);
				for (MarkSixGaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getNormalCode(results, items);
					jsonArray.put(items);
				}
			} 
			data.put("items", jsonArray);
			map.put("msg", "操作成功");
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		} else {
			map.put("msg", "暂无数据");
			map.put("code", APIConstants.CODE_NOT_FOUND);
		}
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}


	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/markSixRule.html"
				+ "?t=" + System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 历史开奖记录
	 * 
	 * @param mapping
	 */
	public void openList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR; 
		String msg = "";

		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = markSixService.findMarkSixGaSessionList(hqls.toString(),
				para, statIndex, pageSize);
		List<MarkSixGaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONObject obj = new JSONObject();
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				MarkSixGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "六合彩");
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				String array[] = session.getOpenResult().split(",|，|\\+");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				JSONArray sumArray = new JSONArray();
				this.getSum(session.getOpenResult(), sumArray,session.getStartTime());
				it.put("sumItems", sumArray); // 特码
				it.put("longhu", this.getLongOrHu(session.getOpenResult())); // 龙/虎
				items.put(it);
			}

			data.put("total", ps.getTotalCount());
			data.put("pageIndex", pageIndex);
			data.put("pageSize", pageSize);// 每页条数
			data.put("pageNum", pageNum);// 当前页数量
			data.put("items", items);

			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
			map.put("data", data);
		} else {
			code = APIConstants.CODE_REQUEST_ERROR;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 最新开奖
	 * 
	 * @param mappinglatestLottery
	 */
	public void latestLottery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR; 
		String msg = "";
		
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(MarkSixConstants.MARKSIX_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = markSixService.findMarkSixGaSessionList(hqls.toString(),
				para, 0, 10);
		List<MarkSixGaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			JSONObject obj = new JSONObject();
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				MarkSixGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "六合彩");
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				String array[] = session.getOpenResult().split(",|，|\\+");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				JSONArray sumArray = new JSONArray();
				this.getSum(session.getOpenResult(), sumArray,session.getStartTime());
				it.put("sumItems", sumArray); // 特码
				it.put("longhu", this.getLongOrHu(session.getOpenResult())); // 龙/虎
				items.put(it);
			}
			data.put("items", items);
			
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
			map.put("data", data);
		} else {
			code = APIConstants.CODE_REQUEST_ERROR;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 排列组合投注项
	 */
	public void combinaInfo(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String optionId = ParamUtils.getParameter(deParameter,"optionId", "");// String类型，例如 1，2，3，6，7
		String gameType = ParamUtils.getParameter(deParameter, "gameType", "");// 彩票类型例如：12
		String playType = ParamUtils.getParameter(deParameter, "playType", ""); // 玩法类型：例如 11

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		if (!ParamUtils.chkString(optionId) || !ParamUtils.chkString(gameType) 
				|| !ParamUtils.chkString(playType)){
			msg = APIConstants.PARAMS_EMPTY_MSG;
		} else {
			if(gameType.equals(Constants.GAME_TYPE_XY_MARKSIX)){
				int type = Integer.parseInt(playType);
				int betNumber = 0;
				String[] options = optionId.split(",|，");
				code = APIConstants.CODE_REQUEST_SUCCESS;
				msg = APIConstants.TIPS_OPERATION_SUCC;

				if(type == 11 || type == 12 || type == 13 || type ==31 || type==35 
						|| type==38 || type==41){ //二全中,二中特，特串,二肖连中,二肖连不中,38二尾连中，41二尾连不中
					if(options.length >= 2 && options.length <= 5){
						betNumber = MarkSixUtil.getTotal(options.length, 2);
					}else{
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}	
				}else if(type == 14 || type == 15 || type == 32 || type==36 || type==39|| type== 42){
					//三全中,三中二,三肖连中,三肖连不中,39三尾连中,42三尾连不中
					if(options.length >= 3 && options.length <= 6){
						betNumber = MarkSixUtil.getTotal(options.length, 3);
					}else{
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}	
				}else if(type == 16 || type == 33 || type==37|| type== 40 || type==43){ //四全中,四肖连中,四肖连不中,40四尾连中,43四尾连不中
					if(options.length >= 4 && options.length <= 7){
						betNumber = MarkSixUtil.getTotal(options.length, 4);
					}else{
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}
				}else if(type == 34){ //五肖连中
					if(options.length >= 5 && options.length <= 8){
						betNumber = MarkSixUtil.getTotal(options.length, 5);
					}else{
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}
				}else if(type>=44 && type<=51){ // 全不中  5全不中-12全不中
					if(options.length >= type-39 && options.length <= type-36){
						betNumber = MarkSixUtil.getTotal(options.length, type-39);
					}else{
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}
				}else if(type == 10 || (type >=21 && type <= 30)){ // 过关 ,合肖
					if(options.length <= 0){
						msg = "参数错误！";
						code = APIConstants.CODE_REQUEST_ERROR;
					}else{
						betNumber = 1;
					}
				}
				data.put("betNumber", betNumber+"");
			}
		} 
		
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);

	}
	
	/**
	 * 走势图的特码
	 * 
	 * @param results
	 * @return
	 */
	public void getSpecialCode(String results, JSONArray items,Date startTime) {
		String array[] = results.split(",|，|\\+");
		int speCode = Integer.parseInt(array[array.length-1]); //特码
		if (speCode <= 24) {
			items.put("小");
		} else if(speCode >= 25 && speCode < 49){
			items.put("大");
		}else if(speCode == 49){
			items.put("和");
		}
		
		if(speCode == 49){
			items.put("和");
		}else{
			if (speCode %2 == 0) {
				items.put("双");
			} else if(speCode %2 == 1){
				items.put("单");
			}
		}
		
		if(speCode == 49){
			items.put("和");
		}else{
			if (speCode % 10 >= 5) {
				items.put("大");
			} else if(speCode %10 <= 4){
				items.put("小");
			}
		}
		
		String zodiac=MarkSixUtil.getZodiac(results,startTime);
		if("牛、马、羊、鸡、狗、猪".indexOf(zodiac)>-1){
			items.put("家禽");
		}else{
			items.put("野兽");
		}
		
		String red="01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";//红波数字
		String blue="03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";//蓝波数字
		String green="05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";//绿波数字
		if(red.indexOf(array[array.length-1])>-1){
			items.put("红波");
		}else  if(blue.indexOf(array[array.length-1])>-1){
			items.put("蓝波");
		}else  if(green.indexOf(array[array.length-1])>-1){
			items.put("绿波");
		}		
		
	}

	/**
	 * 获取开奖走势的正码。
	 * 
	 * @param results
	 * @return
	 */
	public void getNormalCode(String results, JSONArray items) {
		String array[] = results.split(",|，|\\+");
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum = sum + Integer.parseInt(array[i]);
		}
		if (sum >= 175) {
			items.put("大");
		} else {
			items.put("小");
		}

		if (sum % 2 == 0) {
			items.put("双");
		} else {
			items.put("单");
		}
		
		if (sum %10 >= 5) {
			items.put("大");
		} else {
			items.put("小");
		}
		
		if(Integer.parseInt(array[0]) > Integer.parseInt(array[array.length-1])){
			items.put("龙");
		}else{
			items.put("虎");
		}
		
	}

	/**
	 * 历史开奖的特码
	 * 
	 * @param results
	 * @return
	 */
	public void getSum(String results, JSONArray items,Date startTime) {
		String array[] = results.split(",|，|\\+");
		int speCode = Integer.parseInt(array[array.length-1]); //特码
		items.put(speCode+"");
		if (speCode <= 24) {
			items.put("小");
		} else if(speCode >= 25 && speCode < 49){
			items.put("大");
		}else if(speCode == 49){
			items.put("和");
		}
		
		if(speCode == 49){
			items.put("和");
		}else{
			if (speCode %2 == 0) {
				items.put("双");
			} else if(speCode %2 == 1){
				items.put("单");
			}
		}
		
		if(speCode == 49){
			items.put("和");
		}else{
			if (speCode % 10 >= 5) {
				items.put("尾大");
			} else if(speCode %10 <= 4){
				items.put("尾小");
			}
		}
		
		String zodiac=MarkSixUtil.getZodiac(results,startTime);
		if("牛、马、羊、鸡、狗、猪".indexOf(zodiac)>-1){
			items.put("家禽");
		}else{
			items.put("野兽");
		}

		String red="01,02,07,08,12,13,18,19,23,24,29,30,34,35,40,45,46";//红波数字
		String blue="03,04,09,10,14,15,20,25,26,31,36,37,41,42,47,48";//蓝波数字
		String green="05,06,11,16,17,21,22,27,28,32,33,38,39,43,44,49";//绿波数字
		if(red.indexOf(array[array.length-1])>-1){
			items.put("红波");
		}else  if(blue.indexOf(array[array.length-1])>-1){
			items.put("蓝波");
		}else  if(green.indexOf(array[array.length-1])>-1){
			items.put("绿波");
		}		
	}

	/**
	 * 六合彩历史开奖龙虎判断
	 * 
	 * @param results
	 * @return
	 */
	public String getLongOrHu(String results) {
		String array[] = results.split(",|，|\\+");
		String items = "";
		if (Integer.parseInt(array[0]) > Integer
				.parseInt(array[array.length - 1])) {
			items = items + "龙";
		} else if (Integer.parseInt(array[0]) < Integer
				.parseInt(array[array.length - 1])) {
			items = items + "虎";
		}
		return items;
	}
	
	/**
	 * 中奖排行榜
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void winRanking(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		List<WinCoDTO> list = null;
		if (message.equals("")) {
			list = gaService.findGaWinCountList(Constants.GAME_TYPE_XY_MARKSIX);
			if (list != null && list.size() > 0) {
				JSONArray items = new JSONArray();// 数据集合
				JSONObject obj = null;
				for (WinCoDTO co : list) {
					obj = new JSONObject();
					String name = co.getUser().getUserName();
					if(name.length()<2){
						name = name +"***";
					}else{
						name = name.substring(0,2) + "***";
					}
					obj.put("userName", name); //用户名
					obj.put("totalMoney", co.getGaWinCount().getTotalMoney()); //金额
					items.put(obj);
					data.put("items", items);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "操作成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = "操作失败";
			}
		}
		map.put("data", data);
		map.put("msg", message);
		map.put("code", code);
		JsonUtil.AjaxWriter(response, map);
	}

}
