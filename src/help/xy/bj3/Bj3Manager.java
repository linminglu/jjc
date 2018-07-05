package help.xy.bj3;

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
import com.game.GameConstants;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.dto.WinCoDTO;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.xy.bj3.Bj3Constants;
import com.xy.bj3.model.Bj3GaSession;
import com.xy.bj3.model.Bj3GaTrend;
import com.xy.bj3.service.IBj3Service;

public class Bj3Manager extends BaseDispatchAction {
	private final IBj3Service bj3Service = (IBj3Service) getService("bj3Service");
	private final IGaService gaService = (IGaService) getService("gaService");
	private final IUserService userService = (IUserService) getService("userService");
	/**
	 * 上期开奖号字串
	 */
	public void previousNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Bj3GaSession currentSession = bj3Service.getCurrentSession();
		if (null == currentSession) {
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "未到开奖时间");
			map.put("data", data);
		} else {
			Bj3GaSession bj3GaSession = bj3Service
					.getPreviousSessionBySessionNo((Integer
							.parseInt(currentSession.getSessionNo()) - 1) + "");
			if (bj3GaSession != null) {
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("sessionNo", bj3GaSession.getSessionNo() == null ? ""
						: bj3GaSession.getSessionNo());
				JSONArray openResult = new JSONArray();
				if (bj3GaSession.getOpenResult() != null) {
					String[] results = bj3GaSession.getOpenResult().split(",");
					for (int i = 0; i < results.length; i++) {
						openResult.put(results[i]);
					}
				} else {
					log.info("------开奖结果为空。------");
				}
				obj.put("openResult", openResult);
				data.put("obj", obj);

				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "操作成功");
				map.put("data", data);
			} else {
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
		Bj3GaSession ga3Session = bj3Service.getCurrentSession();
		if (ga3Session != null) {
			JSONObject obj = new JSONObject();// 返回数据层
			if (ParamUtils.chkString(ucode)) {
				User user =null;
				Integer uid =0;
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				if(decryptMap!=null){
					uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
					user = (User) userService.getObject(User.class, uid);
				}
				BigDecimal money = null;
				if (user != null) {
					money = TradeCacheUtil.getUserMoney(user.getUserId());
					BigDecimal balance = user.getUserBalance();
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

			long openTime = (ga3Session.getEndTime().getTime() - System
					.currentTimeMillis()) / 1000; // 转成秒
			long betTime = 0;
			if (openTime >= Bj3Constants.BJ3_TIME_OPENING_FREEZE) {
				betTime = openTime - Bj3Constants.BJ3_TIME_OPENING_FREEZE;
			}
			obj.put("gameName", "北京三分彩");//
			obj.put("room", "");
			obj.put("sessionNo", ga3Session.getSessionNo());
			obj.put("openTime", Long.toString(openTime));
			if (betTime < 0L) {
				obj.put("betTime", "0");
			} else {
				obj.put("betTime", Long.toString(betTime));
			}
			obj.put("openDate",
					DateTimeUtil.DateToStringHHmmss(ga3Session.getEndTime()));
			Bj3GaSession beforSession = bj3Service
					.getPreviousSessionBySessionNo((Integer.parseInt(ga3Session
							.getSessionNo()) - 1) + "");
			if (beforSession == null) {
				beforSession = (Bj3GaSession) bj3Service.getObject(
						Bj3GaSession.class, ga3Session.getSessionId() - 1);
			}

			if (beforSession != null) {
				obj.put("lastSessionNo",
						beforSession.getSessionNo() == null ? "" : beforSession
								.getSessionNo());
				JSONArray openResult = new JSONArray();
				if (beforSession.getOpenResult() != null) {
					String[] results = beforSession.getOpenResult().split(",");
					for (int i = 0; i < results.length; i++) {
						openResult.put(results[i]);
					}
				} else {
					log.info("--------北京三分彩开奖结果为空--------");
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
		// String gameType = ParamUtils.getParameter(deParameter, "gameType",
		// "0");//游戏类型 0-->三分彩
		String playType = ParamUtils.getParameter(deParameter, "playType", "0");// 玩法类型
		// 0=两面盘
		// 1=1-5球
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String gameName = "bj3Liangmianpan";
		if (playType.equals("0")) {
			gameName = "bj3Liangmianpan";
		} else if (playType.equals("1")) {
			gameName = "bj315";
		} else if (playType.equals("2")) {
			gameName="bj3Qianerzhixuan";
		} else if (playType.equals("3")) {
			gameName="bj3Houerzhixuan";
		} else if (playType.equals("4")) {
			gameName="bj3Qiansanzhixuan";
		} else if (playType.equals("5")) {
			gameName="bj3Zhongsanzhixuan";
		} else if (playType.equals("6")) {
			gameName="bj3Housanzhixuan";
		} else if (playType.equals("7")) {
			gameName="bj3Qianerzuxuan";
		} else if (playType.equals("8")) {
			gameName="bj3Houerzuxuan";
		} else if (playType.equals("9")) {
			gameName="bj3Qiansanzusan";
		} else if (playType.equals("10")) {
			gameName="bj3Qiansanzusan";
		} else if (playType.equals("11")) {
			gameName="bj3Housanzusan";
		} else if (playType.equals("12")) {
			gameName="bj3Qiansanzuliu";
		} else if (playType.equals("13")) {
			gameName="bj3Zhongsanzuliu";
		} else if (playType.equals("14")) {
			gameName="bj3Housanzuliu";
		} else if (playType.equals("15")) {
			gameName="bj3Qiansan";
		} else if (playType.equals("16")) {
			gameName="bj3Zhongsan";
		} else if (playType.equals("17")) {
			gameName="bj3Housan";
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
			Bj3GaSession ga3Session = bj3Service
					.getPreviousSessionBySessionNo(sessionNo);
			Bj3GaSession tempSession = (Bj3GaSession) bj3Service.getObject(Bj3GaSession.class, ga3Session.getSessionId()-1);
			Date nowDate = new Date();
			Date startTime = tempSession.getEndTime();
			Date endTime = ga3Session.getEndTime();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -Bj3Constants.BJ3_TIME_OPENING_FREEZE);// 封盘时间
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

			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_BJ3);
			System.out.println("gaSessionInfo:::::::::"+gaSessionInfo);
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
			Bj3GaSession ga3Session = bj3Service.getCurrentSession();
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
					hq.addPars(Constants.GAME_TYPE_XY_BJ3);
					hq.addHsql(" and ho.sessionId=?  ");
					hq.addPars(ga3Session.getSessionId());
					hq.addHsql(" and ho.userId=?  ");
					hq.addPars(uid);
					List<Object> moneylist = gaService.findObjects(hq);
					Integer betMoney = 0;
					if (moneylist != null && moneylist.size() > 0) {
						if (moneylist.get(0) != null) {
							betMoney = (Integer) moneylist.get(0);
						} else {
							betMoney = 0;
						}
					}
					JSONArray jsonArray = new JSONArray(optionArray);
					BigDecimal betAll = new BigDecimal(0);// 总投注额
					String ids = "";
					Map<String, Integer> betMap = new HashMap<String, Integer>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						if (jsonObj.get("id") != null) {
							Integer betPoint = Integer.parseInt(jsonObj.get(
									"p").toString()); // 单注投注金额
							if (betPoint > GameConstants.BJ3_MAX_BET_POINT) {
								message = "单注不能超过"+GameConstants.BJ3_MAX_BET_POINT+"！";
								break;
							}
							if(!ParamUtils.chkInteger(betPoint)){
								message="投注不能为0！";
								break;
							}
							String[] arrId = jsonObj.get("id").toString().split("\\|");// 有可能是0,1|0,1这种形式
							for (String id : arrId) {
								ids=ids+id+",";
							}
							betAll=betAll.add(new BigDecimal(jsonObj.get("p").toString()));
							betMap.put(jsonObj.get("id").toString(), Integer.parseInt(jsonObj.get("p").toString()));
						} else {
							message="参数不正确！";
							break;
						}
					}
					if (message.equals("")) {
						if (ParamUtils.chkString(ids)) {
							BigDecimal balance =TradeCacheUtil.getUserMoney(uid);
							if (balance == null)
								balance = new BigDecimal(0);// 判空处理
							
							ids = ids.substring(0, ids.length() - 1);
//							if (betAll.intValue() + betMoney > 50000) {
//								data.put("money", ProductUtil
//										.BigFormatJud(balance.add(paperMoney)));
//								message = "单期投注总额不能超过50000";
//							} else 
							if (betAll.intValue() > balance.intValue()) {
								data.put("money", ProductUtil.BigFormatJud(balance));
								message = "您的余额不足！";
							} else {
								List<GaBetOption> list = gaService.getGaBetOptionByIds(ids);
								for (GaBetOption option : list) {
									String type=option.getGameType();
									if(!Constants.GAME_TYPE_XY_BJ3.equals(type)){
										map.put("code", APIConstants.CODE_REQUEST_ERROR);
										map.put("msg", "请选择正确投注项");
										JsonUtil.AjaxWriter(response, map);
										return;
									}
								}
								if(!betMap.isEmpty()){
									user=bj3Service.saveUserBetInfo(room, betMap, list,
											ga3Session, user, betAll);
								}
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
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		List<Bj3GaTrend> list = bj3Service.findBj3GaTrendList();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (Bj3GaTrend trend : list) {
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
			log.info("--------北京三分彩冷热排行榜为空。-------");
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
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=号码 1=大小
																	// 2=单双
																	// 3=总和/龙虎
		int pageNum = ParamUtils.getIntParameter(deParameter, "pgnm", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pgts", 12);// 只查询12条
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(GameConstants.BJ3_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = bj3Service.findBj3GaSessionList(hqls.toString(),
				para, pageNum, pageSize);
		List<Bj3GaSession> list = ps.getItems();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			if (type.equals("0")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("万");
				items.put("千");
				items.put("佰");
				items.put("拾");
				items.put("个");
				jsonArray.put(items);
				for (Bj3GaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					String array[] = results.split(",");
					for (int i = 0; i < array.length; i++) {
						items.put(array[i]);
					}
					jsonArray.put(items);
				}
			} else if (type.equals("1")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("万");
				items.put("千");
				items.put("佰");
				items.put("拾");
				items.put("个");
				jsonArray.put(items);
				for (Bj3GaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getLargeOrSmall(results, items);
					jsonArray.put(items);
				}
			} else if (type.equals("2")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("万");
				items.put("千");
				items.put("佰");
				items.put("拾");
				items.put("个");
				jsonArray.put(items);
				for (Bj3GaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getDoubleOrSingle(results, items);
					jsonArray.put(items);
				}
			} else if (type.equals("3")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("总和");
				items.put("大小");
				items.put("单双");
				items.put("龙虎");
				jsonArray.put(items);
				for (Bj3GaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getSum(results, items);
					this.getLongOrHu(results, items);
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

	/**
	 * 判断每一个数字大小
	 * 
	 * @param results
	 * @return
	 */
	public void getLargeOrSmall(String results, JSONArray items) {
		String array[] = results.split(",");
		for (int i = 0; i < array.length; i++) {
			if (Integer.parseInt(array[i]) < 5) {
				items.put("小");
			} else {
				items.put("大");
			}
		}
	}

	/**
	 * 判断每一个数字单双
	 * 
	 * @param results
	 * @return
	 */
	public void getDoubleOrSingle(String results, JSONArray items) {
		String array[] = results.split(",");
		for (int i = 0; i < array.length; i++) {
			if (Integer.parseInt(array[i]) % 2 == 0) {
				items.put("双");
			} else {
				items.put("单");
			}
		}
	}

	/**
	 * 总和判断
	 * 
	 * @param results
	 * @return
	 */
	public void getSum(String results, JSONArray items) {
		String array[] = results.split(",");
		int result = 0;
		for (int i = 0; i < array.length; i++) {
			result = Integer.parseInt(array[i]) + result;
		}
		items.put(result + "");
		if (result > 22) {
			items.put("大");
		} else {
			items.put("小");
		}
		if (result % 2 == 0) {
			items.put("双");
		} else {
			items.put("单");
		}
	}

	/**
	 * 三分彩（历史开奖/走势）龙虎判断
	 * 
	 * @param results
	 * @return
	 */
	public void getLongOrHu(String results, JSONArray items) {
		String array[] = results.split(",");
		if (Integer.parseInt(array[0]) > Integer
				.parseInt(array[array.length - 1])) {
			items.put("龙");
		} else if (Integer.parseInt(array[0]) < Integer
				.parseInt(array[array.length - 1])) {
			items.put("虎");
		} else {
			items.put("和");
		}
	}

	/**
	 * 三分彩（历史开奖/走势）龙虎判断,重载方法
	 * 
	 * @param results
	 * @return
	 */
	public String getLongOrHu(String results) {
		String array[] = results.split(",");
		String items = "";
		if (Integer.parseInt(array[0]) > Integer
				.parseInt(array[array.length - 1])) {
			items = items + "龙";
		} else if (Integer.parseInt(array[0]) < Integer
				.parseInt(array[array.length - 1])) {
			items = items + "虎";
		} else {
			items = items + "和";
		}
		return items;
	}

	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/bj3rule.html"
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
		para.add(GameConstants.BJ3_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = bj3Service.findBj3GaSessionList(hqls.toString(),
				para, statIndex, pageSize);
		List<Bj3GaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONObject obj = new JSONObject();
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				Bj3GaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "三分彩");
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				String array[] = session.getOpenResult().split(",");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				JSONArray sumArray = new JSONArray();
				this.getSum(session.getOpenResult(), sumArray);
				it.put("sumItems", sumArray);
				it.put("longhu", this.getLongOrHu(session.getOpenResult()));
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
		para.add(GameConstants.BJ3_OPEN_STATUS_OPENED);
		hqls.append(" order by ho.sessionId desc ");// 已开奖
		PaginationSupport ps = bj3Service.findBj3GaSessionList(hqls.toString(),
				para, 0, 10);
		List<Bj3GaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			JSONObject obj = new JSONObject();
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				Bj3GaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "三分彩");
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				String array[] = session.getOpenResult().split(",");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				JSONArray sumArray = new JSONArray();
				this.getSum(session.getOpenResult(), sumArray);
				it.put("sumItems", sumArray);
				it.put("longhu", this.getLongOrHu(session.getOpenResult()));
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
			list = gaService.findGaWinCountList(Constants.GAME_TYPE_XY_BJ3);
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
