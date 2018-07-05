package help.gf.bjkl8;

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
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.gf.bjkl8.BjKl8Constants;
import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.GfBjKl8GaTrend;
import com.gf.bjkl8.service.IBjKl8Service;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.GameConstants;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.service.IGaService;
import com.gf.k3.bjk3.model.BjK3GaSession;
import com.gf.pick11.gdpick11.GdPick11Constants;
import com.ram.RamConstants;
import com.ram.model.User;


public class BjKl8Manager extends BaseDispatchAction {
	private final IBjKl8Service gfBjKl8Service = (IBjKl8Service) getService("gfBjKl8Service");
	private final IGaService gaService = (IGaService) getService("gaService");
	/**
	 * 上期开奖号字串
	 */
	public void previousNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层

		JSONObject data = new JSONObject();// 返回数据层
		GfBjKl8GaSession currentSession=gfBjKl8Service.getCurrentSession();
		if(null==currentSession){
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "未到开奖时间");
			map.put("data", data);
		} else {
			GfBjKl8GaSession session = gfBjKl8Service
					.getPreviousSessionBySessionNo((Integer
							.parseInt(currentSession.getSessionNo()) - 1) + "");
			if (session != null) {
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("sessionNo", session.getSessionNo() == null ? ""
						: session.getSessionNo());
				JSONArray openResult = new JSONArray();
				if (session.getOpenResult() != null) {

					Map<String, String> resultMap = gfBjKl8Service
							.openResult(session.getOpenResult());
					String number1 = resultMap.get("number1");
					String number2 = resultMap.get("number2");
					String number3 = resultMap.get("number3");
					String number = resultMap.get("number");
					String colour = resultMap.get("colour");
					//String string = number1+","+number2+","+number3+","+number+","+colour;
					openResult.put(number1);
					openResult.put(number2);
					openResult.put(number3);
					openResult.put(number);
					openResult.put(colour);
				}else{//10 5 7 6 4 1 2 9 8 3 	
					openResult.put("5");
					openResult.put("3");
					openResult.put("2");
					openResult.put("10");
					openResult.put("1");
				}
				obj.put("openResult", openResult);
				data.put("obj", obj);
				
				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "操作成功");
				map.put("data", data);
			}else{
				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "第一期");
				map.put("data", data);
			}
		}

		JsonUtil.AjaxWriter(response, map);
	}

	
	public void currentTime(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型 1.IOS2安卓
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		GfBjKl8GaSession session=gfBjKl8Service.getCurrentSession();
		if(session!=null){
			JSONObject obj = new JSONObject();// 返回数据层
			if(ParamUtils.chkString(ucode)){
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				Integer uid = Integer.valueOf(decryptMap
						.get(Constants.DES_KEY_UID));
				User user = (User) userService.getObject(User.class, uid);
				if (user != null) {
					BigDecimal balance = user.getUserBalance();
					if (balance == null) {
						obj.put("gains", "0.0");
					} else {
						obj.put("gains",
								user.getUserBalance()
										.setScale(2, BigDecimal.ROUND_DOWN)
										.toString());
					}
				}else{
					obj.put("gains", "0.0");
				}
				BigDecimal balance = user.getMoney();
				if (balance == null)
					balance = new BigDecimal(0);// 判空处理
				BigDecimal paperMoney = user.getUserScore();
				if (paperMoney == null)
					paperMoney = new BigDecimal(0);// 判空处理
				obj.put("money",
						balance.add(paperMoney)
								.setScale(2, BigDecimal.ROUND_DOWN).toString());
			} else {
				obj.put("gains", "0.0");
				obj.put("money", "0");
			}
			long openTime=(session.getEndTime().getTime()-System.currentTimeMillis())/1000; //转成秒			
			long betTime=0;
			if(openTime>=30){
				betTime=openTime-30;
			}
			obj.put("gameName", "PC蛋蛋");
			obj.put("room", "");
			obj.put("sessionNo", session.getSessionNo());
			obj.put("openDate",
					DateTimeUtil.DateToStringHHmmss(session.getEndTime()));
			GfBjKl8GaSession beforSession = gfBjKl8Service
					.getPreviousSessionBySessionNo((Integer.parseInt(session
							.getSessionNo()) - 1) + "");
			if (beforSession != null) {
				obj.put("lastSessionNo",
						beforSession.getSessionNo() == null ? "" : beforSession
								.getSessionNo());
				JSONArray openResult = new JSONArray();
				if(beforSession.getCountResult()!=null){
					String number1 = beforSession.getCountResult().split(",")[0];
					String number2 = beforSession.getCountResult().split(",")[1];
					String number3 = beforSession.getCountResult().split(",")[2];
					String number = beforSession.getCountResult().split(",")[3];
					String colour = beforSession.getCountResult().split(",")[4];
					openResult.put(number1);
					openResult.put(number2);
					openResult.put(number3);
					openResult.put(number);
					openResult.put(colour);
				} else {
					if (beforSession.getOpenResult() != null) {
						Map<String, String> resultMap = gfBjKl8Service
								.openResult(beforSession.getOpenResult());
						String number1 = resultMap.get("number1");
						String number2 = resultMap.get("number2");
						String number3 = resultMap.get("number3");
						String number = resultMap.get("number");
						String colour = resultMap.get("colour");
						openResult.put(number1);
						openResult.put(number2);
						openResult.put(number3);
						openResult.put(number);
						openResult.put(colour);
						resultMap.clear();
					}
				}
				obj.put("openResult", openResult);
			}else{
				map.put("msg", "第一期");
				obj.put("lastSessionNo", "");
				obj.put("openResult", new JSONArray());
			}
			obj.put("openTime", Long.toString(openTime));
			obj.put("openDate",
					DateTimeUtil.DateToStringHHmmss(session.getEndTime()));
			if (betTime < 0L) {
				obj.put("betTime", "0");
			}else{
				obj.put("betTime", Long.toString(betTime));
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
		}else{
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "开奖时间未到！");
			map.put("data", data);
		}
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 面板选项
	 * @param mapping
	 */
	
	public void betPanel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String playType = ParamUtils.getParameter(deParameter, "playType", "0");// 玩法类型
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String gameName="bj28Liangmianpan";
		if(playType.equals("0")){
			gameName="bj28Liangmianpan";
		}else if(playType.equals("1")){
			gameName="bj28Tema";
		}
		String itemsString = CacheUtil.getLotteryBetting(gameName);
		JSONArray betItems=new JSONArray(itemsString);
		data.put("betItems", betItems);
		map.put("data", data);
		map.put("msg", "操作成功");
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 下注
	 * @param mapping
	 */
	
	public void bet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String sessionNo = ParamUtils
				.getParameter(deParameter, "sessionNo", "");
		String optionArray = ParamUtils.getParameter(deParameter,
				"optionArray", "");// [{optionId:12,betPoints:2}]
		String room = ParamUtils.getParameter(deParameter, "room", "A");
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		JSONObject data = new JSONObject();
		if (!ParamUtils.chkString(sessionNo)
				|| !ParamUtils.chkString(optionArray)
				|| !ParamUtils.chkString(ucode)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
			map.put("msg", message);
			map.put("code", APIConstants.CODE_REQUEST_ERROR);
		}else{
			GfBjKl8GaSession gaK10Session = gfBjKl8Service
					.getPreviousSessionBySessionNo(sessionNo);
			GfBjKl8GaSession tempSession = (GfBjKl8GaSession) gfBjKl8Service.getObject(GfBjKl8GaSession.class, gaK10Session.getSessionId()-1);
			Date nowDate = new Date();
			Date startTime = tempSession.getEndTime();
			Date endTime = gaK10Session.getEndTime();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -30);// 封盘时间
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
			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_GF_BJKL8);
			if(Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getStatus()) || 
					Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getBetAvoid())){
				message = "当前彩种维护中";
			}
			if(!message.equals("")){
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
				return;
			}
		}

		if (message.equals("")) {
			GfBjKl8GaSession bjLu28Session = gfBjKl8Service.getCurrentSession();
			if (bjLu28Session != null
					&& sessionNo.equals(bjLu28Session.getSessionNo())) {
				User user = null;
				if (ucode != null && ucode.length() > 1
						&& ParamUtils.chkString(optionArray)) {

					Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
					Integer uid = Integer.valueOf(decryptMap
							.get(Constants.DES_KEY_UID));
					user = userService.getValidUser(uid);
					if (user == null) {
						map.put("msg", "用户不存在");
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
					} else {

						BigDecimal balance = user.getMoney();
						if (balance == null)
							balance = new BigDecimal(0);// 判空处理
						BigDecimal paperMoney = user.getUserScore();
						if (paperMoney == null)
							paperMoney = new BigDecimal(0);// 判空处理
						
						HQUtils hq = new HQUtils(
								"select sum(ho.betMoney) from GaBetDetail ho where 1=1 ");
						hq.addHsql(" and ho.gameType=?  ");
						hq.addPars(Constants.GAME_TYPE_GF_BJKL8);
						hq.addHsql(" and ho.sessionId=?  ");
						hq.addPars(bjLu28Session.getSessionId());
						hq.addHsql(" and ho.userId=?  ");
						hq.addPars(uid);
						List<Object> moneylist = gaService.findObjects(hq);
						Integer betMoney = 0;  //当前期已投注过的总金额。
						if (moneylist != null && moneylist.size() > 0) {
							if (moneylist.get(0) != null) {
								betMoney = (Integer) moneylist.get(0);
							} else {
								betMoney = 0;
							}
						}

						JSONArray jsonArray = new JSONArray(optionArray);
						BigDecimal betAll = new BigDecimal(0);
						StringBuffer ids = new StringBuffer();
						Map<Integer,Integer> betMap=new HashMap<Integer,Integer>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							String optionId = (String) jsonObj.optString("id");
							
							if(optionId !=null && optionId.length() > 0){
								Integer betPoint = Integer.parseInt(jsonObj.get("p").toString()); // 单注投注金额
								Integer opid=Integer.parseInt(jsonObj.get("id").toString());
								if(opid==563||opid==564||(opid>=569&&opid<=572)){
									if(betPoint > GameConstants.TOTAL_BET_FIVE_THOUSAND){
										map.put("code", APIConstants.CODE_REQUEST_ERROR);
										map.put("msg", "单注不能超过5000！");
										JsonUtil.AjaxWriter(response, map);
										return;
									}
									if(!ParamUtils.chkInteger(betPoint)){
										message="投注不能为0！";
										map.put("code",APIConstants.CODE_REQUEST_ERROR);
										map.put("msg", message);
										JsonUtil.AjaxWriter(response, map);
										return;
									}

								} else {
									if (betPoint > BjKl8Constants.SINGLE_NOTE_MAX_BET_POINT) {
										map.put("code",
												APIConstants.CODE_REQUEST_ERROR);
										map.put("msg", "单注不能超过10000！");
										JsonUtil.AjaxWriter(response, map);
										return;								
									}
								}
																
								ids=ids.append(jsonObj.get("id").toString()).append(",");
								betAll=betAll.add(new BigDecimal(jsonObj.get("p").toString()));
								betMap.put(Integer.parseInt(jsonObj.get("id").toString()), Integer.parseInt(jsonObj.get("p").toString()));
							}else{
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "参数不正确！");
								JsonUtil.AjaxWriter(response, map);
								return;
							}
						}

						if (betAll.intValue() + betMoney > BjKl8Constants.TOTAL_MAX_BET_POINT) {
							message = "单期投注总额不能超过50000";
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}else if (betAll.intValue() > balance.add(paperMoney)
								.intValue()) {
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "您的余额不足！");
							JsonUtil.AjaxWriter(response, map);
							return;
						}else{
							String strIds=ids.toString();
							if(ParamUtils.chkString(strIds)){
								strIds=strIds.substring(0, strIds.length()-1);
							}else{
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "请选择投注项");
								JsonUtil.AjaxWriter(response, map);
								return;
							}
							List<GaBetOption> list = gaService
									.getGaBetOptionByIds(strIds);
							
							for (GaBetOption option : list) {
								String type=option.getGameType();
								if(!Constants.GAME_TYPE_GF_BJKL8.equals(type)){
									map.put("code", APIConstants.CODE_REQUEST_ERROR);
									map.put("msg", "请选择正确投注项");
									JsonUtil.AjaxWriter(response, map);
									return;
								}
							}
							
							user=gfBjKl8Service.saveUserBetInfo(room, betMap, list,
									bjLu28Session, user, betAll);
						}
						BigDecimal money = user.getMoney();
						if (money == null)
							money = new BigDecimal(0);
						data.put("money", ProductUtil.BigFormatJud(money));
						map.put("data", data);
						map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
						map.put("msg", "投注成功！");
					}
				}else{
					map.put("msg", "参数不正确");
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
				}
			}else{
				map.put("msg", "参数不正确");
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
			}
		}
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 冷热排行
	 * @param 
	 */
	public void hotRanking(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		
		List<GfBjKl8GaTrend> list=gfBjKl8Service.findBjKl8TrendList();
		JSONArray jsonArray = new JSONArray();
		if(list!=null&&list.size()>0){
			for(GfBjKl8GaTrend trend:list){
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("title", trend.getTrendTitle());
				obj.put("num", trend.getTrendCount());
				jsonArray.put(obj);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
		}else{
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		data.put("items", jsonArray);
		map.put("msg", msg);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 走势列表
	 * @param 
	 */
	public void trend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=号码 1=混合
		int pageNum = ParamUtils.getIntParameter(deParameter, "pgnm", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pgts", 12);// 只查询12条
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus = ? ");// 已开奖
		para.add(BjKl8Constants.LUCKY28_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = gfBjKl8Service.findBjKl8GaSessionList(
				hqls.toString(), para, pageNum, pageSize);
		List<GfBjKl8GaSession> list = ps.getItems();
		JSONArray jsonArray = new JSONArray();
		if(list!=null&&list.size()>0){
			if(type.equals("0")){//号码
				JSONArray items= new JSONArray();
				items.put("期号");
				items.put("一");
				items.put("二");
				items.put("三");
				items.put("和");
				jsonArray.put(items);
				for(GfBjKl8GaSession session:list){
					items= new JSONArray();
					items.put(session.getSessionNo());
					if(session.getCountResult()!=null){
						String number1 = session.getCountResult().split(",")[0];
						String number2 = session.getCountResult().split(",")[1];
						String number3 = session.getCountResult().split(",")[2];
						String number = session.getCountResult().split(",")[3];
						items.put(number1);
						items.put(number2);
						items.put(number3);
						items.put(number);
					} else {
						Map<String, String> resultMap = gfBjKl8Service
								.openResult(session.getOpenResult());
						String number1 = resultMap.get("number1");
						String number2 = resultMap.get("number2");
						String number3 = resultMap.get("number3");
						String number = resultMap.get("number");
						items.put(number1);
						items.put(number2);
						items.put(number3);
						items.put(number);
					}
					jsonArray.put(items);
				}
			}else if(type.equals("1")){//混合
				JSONArray items= new JSONArray();
				items.put("期号");
				items.put("大小");
				items.put("单双");
				items.put("极值");
				items.put("波色");
				items.put("豹子");
				jsonArray.put(items);
				for(GfBjKl8GaSession session:list){
					items= new JSONArray();
					items.put(session.getSessionNo());

					String results = session.getOpenResult();
					Map<String, String> resultMap = null;
					if (session.getCountResult() != null) {
						resultMap = gfBjKl8Service.judgeCountResult(session
								.getCountResult());
					} else {
						resultMap = gfBjKl8Service.judgeResult(results);
					}	
					String string1 = resultMap.get("大小");
					String string2 = resultMap.get("单双");
					String string3 = resultMap.get("极值");
					String string4 = resultMap.get("波色");
					String string5 = resultMap.get("豹子");
					items.put(string1);
					items.put(string2);
					if(ParamUtils.chkString(string3))
						items.put(string3);
					else
						items.put("-");
					items.put(string4);
					if("豹子".equals(string5)){
						String number = resultMap.get("number");
						items.put(string5 + number);
					} else
						items.put("-");
					jsonArray.put(items);
				}
			}
			data.put("items", jsonArray);
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
		}else{
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("data", data);
		map.put("msg", msg);
		map.put("code", code);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	
	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/bjlu28.html"
				+ "?t=" + System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 最新开奖
	 * 
	 * @param mapping
	 */
	public void latestLottery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(GdPick11Constants.GD_PICK11_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		PaginationSupport ps = gfBjKl8Service.findBjKl8GaSessionList(
				hqls.toString(), para, statIndex, pageSize);
		List<BjK3GaSession> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BjK3GaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("sessionNo", session.getSessionNo());
				String array[] = session.getOpenResult().split(",");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
		} else {
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
	 * 历史开奖记录
	 * @param mapping
	 */
	public void openList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus = ? ");//已开奖
		para.add(BjKl8Constants.LUCKY28_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");//已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = gfBjKl8Service.findBjKl8GaSessionList(
				hqls.toString(), para, statIndex, pageSize);
		List<GfBjKl8GaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONObject obj = new JSONObject();
			JSONArray items = new JSONArray();
			for(int i=0;i<list.size();i++){
				GfBjKl8GaSession session=list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "北京快乐8");
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				JSONArray jsonArray = new JSONArray();
				Map<String, String> resultMap = null;
				if (session.getCountResult() != null) {
					resultMap = gfBjKl8Service.openCountResult(session
							.getCountResult());
				} else {
					resultMap = gfBjKl8Service.openResult(session
							.getOpenResult());
				}
				String number1 = resultMap.get("number1");
				String number2 = resultMap.get("number2");
				String number3 = resultMap.get("number3");
				String number = resultMap.get("number");
				String colour = resultMap.get("colour");
				jsonArray.put(number1);
				jsonArray.put(number2);
				jsonArray.put(number3);
				jsonArray.put(number);
				jsonArray.put(colour);
				it.put("resultItems", jsonArray);
				JSONArray sumItems=new JSONArray();
				String string1 = resultMap.get("大小");
				String string2 = resultMap.get("单双");
				String string3 = resultMap.get("极值");
				String string4 = resultMap.get("豹子");
				sumItems.put(number);
				sumItems.put(string1+string2);
				sumItems.put(string3);
				sumItems.put(string4);
				it.put("sumItems", sumItems);
				String value="";
				if(colour.equals("0")){
					value="无波色";
				}else  if(colour.equals("1")){
					value="绿波";
				}else  if(colour.equals("2")){
					value="蓝波";
				}else  if(colour.equals("3")){
					value="红波";
				}
				it.put("longhu", value);
				items.put(it);
			}
			data.put("items", items);
			data.put("total", ps.getTotalCount());
			data.put("pageIndex", pageIndex);
			data.put("pageSize", pageSize);// 每页条数
			data.put("pageNum", pageNum);// 当前页数量		

			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
			map.put("data", data);
		}else{
			code = APIConstants.CODE_REQUEST_ERROR;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
}
