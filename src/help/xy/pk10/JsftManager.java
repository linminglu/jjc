package help.xy.pk10;

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
import com.xy.pk10.jsft.JsftConstants;
import com.xy.pk10.jsft.model.JsftGaSession;
import com.xy.pk10.jsft.model.JsftGaTrend;
import com.xy.pk10.jsft.service.IJsftService;

public class JsftManager extends BaseDispatchAction {
	private final IJsftService jsftService = (IJsftService) getService("jsftService");
	private final IGaService gaService = (IGaService) getService("gaService");

	/**
	 * 上期开奖号字串
	 */
	public void previousNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JsftGaSession currentSession = jsftService.getCurrentSession();
		if (null == currentSession) {
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "未到开奖时间");
			map.put("data", data);
		} else {
			JsftGaSession gaJsftSession = jsftService
					.getPreviousSessionBySessionNo((Integer
							.parseInt(currentSession.getSessionNo()) - 1) + "");
			if (gaJsftSession != null) {
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("sessionNo",
						gaJsftSession.getSessionNo() == null ? ""
								: gaJsftSession.getSessionNo());
				JSONArray openResult = new JSONArray();
				if (gaJsftSession.getOpenResult() != null) {
					String[] results = gaJsftSession.getOpenResult().split(
							",");
					for (int i = 0; i < results.length; i++) {
						openResult.put(results[i]);
					}
				}
				obj.put("openResult", openResult);
				data.put("obj", obj);

				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "操作成功");
				map.put("data", data);
			} else {
				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "第一期");
				JSONObject obj = new JSONObject();// 返回数据层
				obj.put("sessionNo", "");
				obj.put("openResult", new JSONArray());
				data.put("obj", obj);
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
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS2安卓
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JsftGaSession gaJsftSession = jsftService.getCurrentSession();
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		if (gaJsftSession != null) {
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
					money = TradeCacheUtil.getUserMoney(uid);
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

				obj.put("money",
						money.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			} else {
				obj.put("gains", "0.0");
				obj.put("money", "0");
			}

			long openTime = (gaJsftSession.getEndTime().getTime() - System
					.currentTimeMillis()) / 1000; // 转成秒
			long betTime = 0;
			if (openTime >= JsftConstants.JSFT_TIME_OPENING_FREEZE) {
				betTime = openTime - JsftConstants.JSFT_TIME_OPENING_FREEZE;
			}
			obj.put("gameName", "幸运飞艇");
			obj.put("room", "");
			obj.put("sessionNo", gaJsftSession.getSessionNo());
			obj.put("openDate", DateTimeUtil.DateToStringHHmmss(gaJsftSession
					.getEndTime()));
			JsftGaSession beforSession = (JsftGaSession) jsftService.getObject(JsftGaSession.class, gaJsftSession.getSessionId()-1);
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
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					code = APIConstants.CODE_NOT_FOUND;
				}
				obj.put("openResult", openResult);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_REQUEST_SUCCESS;
				map.put("msg", "xitong");
				obj.put("lastSessionNo", "");
				obj.put("openResult", new JSONArray());
			}

			if (betTime < 0L) {
				obj.put("betTime", "0");
			} else {
				obj.put("betTime", Long.toString(betTime));
			}
			obj.put("openTime", Long.toString(openTime));
			String isWeb = "0";
			String webUrl = "";
			if (device.equals("2") && "1.0.0".equals(ver)) {
				isWeb = "1";
				webUrl = StringUtil.checkAPIHttpUrl("/game/init.jsp?t=&m=");// t游戏类型m加密
			}
			obj.put("isWeb", isWeb);
			obj.put("webUrl", webUrl);
			data.put("obj", obj);
			map.put("code", code);
			map.put("msg", "操作成功");
			map.put("data", data);
		} else {
			map.put("code", APIConstants.CODE_REQUEST_ERROR);
			map.put("msg", "系统维护中");
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
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String gameName = "jsftLiangmianpan";
		if (playType.equals("0")) {
			gameName = "jsftLiangmianpan";
		} else if (playType.equals("1")) {
			gameName = "jsft110";
		} else if (playType.equals("2")) {
			gameName = "jsftGuanyajun";
		}
		String itemsString = CacheUtil.getLotteryBetting(gameName);
		JSONArray betItems = new JSONArray(itemsString);
		data.put("betItems", betItems);
		map.put("data", data);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "操作成功");
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 下注
	 * 
	 * @param mapping
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
			JsftGaSession gaJsftSession = jsftService
					.getPreviousSessionBySessionNo(sessionNo);
			JsftGaSession tempSession = (JsftGaSession) jsftService
					.getObject(JsftGaSession.class,
							gaJsftSession.getSessionId() - 1);
			Date nowDate = new Date();
			Date startTime = tempSession.getEndTime();
			Date endTime = gaJsftSession.getEndTime();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -JsftConstants.JSFT_TIME_OPENING_FREEZE);// 封盘时间
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
			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_JSFT);
			if(Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getStatus()) || 
					Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getBetAvoid())){
				Param tip = gaService.getParam(Constants.PARAM_LOTTERY_CLOSE_TIP);
				if(tip!=null&&ParamUtils.chkString(tip.getValue())){
					message = tip.getValue();
				}else{
					message = "彩种维护";
				}
			}

			if (!message.equals("")) {
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", message);
				map.put("data", data);
				JsonUtil.AjaxWriter(response, map);
				return;
			}
		}

		if (message.equals("")) {
			JsftGaSession gaJsftSession = jsftService.getCurrentSession();
			if (sessionNo.equals(gaJsftSession.getSessionNo())) {
				User user = null;
				if(ParamUtils.chkString(optionArray)){
					Integer uid =0;
					Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
					if(decryptMap!=null){
						uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
						user = userService.getValidUser(uid);
					}
					if (user == null) {
						map.put("msg", "用户不存在");
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("data", data);
					} else {
						String status = user.getStatus();
					    if(Constants.PUB_STATUS_CLOSE.equals(status)){
						    message = "用户无效";
						    map.put("code", APIConstants.CODE_REQUEST_ERROR);
						    map.put("msg", message);
						    JsonUtil.AjaxWriter(response, map);
						    return;
					    }
						
						HQUtils hq = new HQUtils(
								"select sum(ho.betMoney) from GaBetDetail ho where 1=1 ");
						hq.addHsql(" and ho.gameType=?  ");
						hq.addPars(Constants.GAME_TYPE_XY_JSFT);
						hq.addHsql(" and ho.sessionId=?  ");
						hq.addPars(gaJsftSession.getSessionId());
						hq.addHsql(" and ho.userId=?  ");
						hq.addPars(uid);
						List<Object> moneylist = gaService.findObjects(hq);
						Integer betMoney = 0; // 当前期已投注过的总金额。
						if (moneylist != null && moneylist.size() > 0) {
							if (moneylist.get(0) != null) {
								betMoney = (Integer) moneylist.get(0);
							} else {
								betMoney = 0;
							}
						}

						JSONArray jsonArray = new JSONArray(optionArray);
						BigDecimal betAll = new BigDecimal(0);
						String ids = "";
						Map<Integer, Integer> betMap = new HashMap<Integer, Integer>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							if (jsonObj.get("id") != null) {
								Integer betPoint = Integer.parseInt(jsonObj
										.get("p").toString()); // 单注投注金额
								if (betPoint > JsftConstants.SINGLE_NOTE_MAX_BET_POINT) {
									map.put("code",
											APIConstants.CODE_REQUEST_ERROR);
									map.put("msg", "单注不能超过"+JsftConstants.SINGLE_NOTE_MAX_BET_POINT+"！");
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								if (!ParamUtils.chkInteger(betPoint)) {
									message = "投注不能为0！";
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}

								ids = ids + jsonObj.get("id").toString() + ",";
								betAll = betAll.add(new BigDecimal(jsonObj.get(
										"p").toString()));
								betMap.put(Integer.parseInt(jsonObj.get("id")
										.toString()), Integer.parseInt(jsonObj
										.get("p").toString()));
							} else {
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "参数不正确！");
								map.put("data", data);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
						}
						
						BigDecimal balance = TradeCacheUtil.getUserMoney(uid);
						if (balance == null)
							balance = new BigDecimal(0);// 判空处理
//						if (betAll.intValue() + betMoney > JsftConstants.TOTAL_MAX_BET_POINT) {
//							data.put("money", ProductUtil.BigFormatJud(balance));
//							message = "单期投注总额不能超过50000";
//							map.put("code", APIConstants.CODE_REQUEST_ERROR);
//							map.put("msg", message);
//							map.put("data", data);
//							JsonUtil.AjaxWriter(response, map);
//							return;
//						} else 
						if (betAll.intValue() > balance.intValue()) {
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "您的积分不足！");
							map.put("data", data);
							JsonUtil.AjaxWriter(response, map);
							return;
						} else {
							if (ParamUtils.chkString(ids)) {
								ids = ids.substring(0, ids.length() - 1);
							} else {
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "请选择投注项");
								JsonUtil.AjaxWriter(response, map);
								return;
							}
							List<GaBetOption> list = gaService
									.getGaBetOptionByIds(ids);
							for (GaBetOption option : list) {
								String gameType = option.getGameType();
								if (!Constants.GAME_TYPE_XY_JSFT
										.equals(gameType)) {
									map.put("code",
											APIConstants.CODE_REQUEST_ERROR);
									map.put("msg", "请选择正确投注项");
									JsonUtil.AjaxWriter(response, map);
									return;
								}
							}

							user = jsftService.saveUserBetInfo(room, betMap,
									list, gaJsftSession, user, betAll);
						}
						BigDecimal balance1 = TradeCacheUtil.getUserMoney(uid);

						data.put(
								"money",
								balance1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
						map.put("msg", "投注成功！");
						map.put("data", data);
					}
				} else {
					map.put("msg", "参数不正确");
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("data", data);
				}
			} else {
				map.put("msg", "参数不正确");
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("data", data);
			}
		}
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 冷热排行
	 * 
	 * @param
	 */
	public void hotRanking(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		List<JsftGaTrend> list = jsftService.findJsftGaTrendList();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (JsftGaTrend trend : list) {
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
		}
		map.put("code", code);
		map.put("msg", msg);
		data.put("items", jsonArray);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 走势列表
	 * 
	 * @param
	 */
	public void trend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=号码
																		// 1=大小
		// 2=单双
		// 3=冠亚军和
		// 4=1-5龙虎
		int pageNum = ParamUtils.getIntParameter(deParameter, "pgnm", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pgts", 12);// 只查询12条
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(JsftConstants.JSFT_OPEN_STATUS_OPENED);

		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = jsftService.findJsftGaSessionList(
				hqls.toString(), para, pageNum, pageSize);
		List<JsftGaSession> list = ps.getItems();
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
				items.put("八");
				items.put("九");
				items.put("十");
				jsonArray.put(items);
				for (JsftGaSession session : list) {
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
				items.put("一");
				items.put("二");
				items.put("三");
				items.put("四");
				items.put("五");
				items.put("六");
				items.put("七");
				items.put("八");
				items.put("九");
				items.put("十");
				jsonArray.put(items);
				for (JsftGaSession session : list) {
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
				items.put("一");
				items.put("二");
				items.put("三");
				items.put("四");
				items.put("五");
				items.put("六");
				items.put("七");
				items.put("八");
				items.put("九");
				items.put("十");
				jsonArray.put(items);
				for (JsftGaSession session : list) {
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
				items.put("和");
				items.put("大小");
				items.put("单双");
				jsonArray.put(items);
				for (JsftGaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getSum(results, items);
					jsonArray.put(items);
				}
			} else if (type.equals("4")) {
				JSONArray items = new JSONArray();
				items.put("期号");
				items.put("一");
				items.put("二");
				items.put("三");
				items.put("四");
				items.put("五");
				jsonArray.put(items);
				for (JsftGaSession session : list) {
					items = new JSONArray();
					items.put(session.getSessionNo().substring(
							session.getSessionNo().length() - 3));
					String results = session.getOpenResult();
					this.getLongOrHu(results, items);
					jsonArray.put(items);
				}
			}
			data.put("items", jsonArray);
			msg = APIConstants.TIPS_OPERATION_SUCC;
			code = APIConstants.CODE_REQUEST_SUCCESS;
		} else {
			msg = APIConstants.TIPS_DATA_NOT;
			code = APIConstants.CODE_NOT_FOUND;
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
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
			if (Integer.parseInt(array[i]) < 6) {
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
	 * 冠亚军和判断
	 * 
	 * @param results
	 * @return
	 */
	public void getSum(String results, JSONArray items) {
		String array[] = results.split(",");
		int result = 0;
		result = Integer.parseInt(array[0]) + Integer.parseInt(array[1]);
		items.put(result + "");
		if (result > 11) {
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
	 * 1-5龙虎判断
	 * 
	 * @param results
	 * @return
	 */
	public void getLongOrHu(String results, JSONArray items) {
		String array[] = results.split(",");
		String value = "";
		if (Integer.parseInt(array[0]) > Integer.parseInt(array[9])) {
			items.put("龙");
		} else {
			items.put("虎");
		}
		if (Integer.parseInt(array[1]) > Integer.parseInt(array[8])) {
			items.put("龙");
		} else {
			items.put("虎");
		}
		if (Integer.parseInt(array[2]) > Integer.parseInt(array[7])) {
			items.put("龙");
		} else {
			items.put("虎");
		}
		if (Integer.parseInt(array[3]) > Integer.parseInt(array[6])) {
			items.put("龙");
		} else {
			items.put("虎");
		}
		if (Integer.parseInt(array[4]) > Integer.parseInt(array[5])) {
			items.put("龙");
		} else {
			items.put("虎");
		}
	}

	/**
	 * 1-5龙虎判断
	 * 
	 * @param results
	 * @return
	 */
	public String getLongOrHu(String results) {
		String array[] = results.split(",");
		String value = "";
		if (Integer.parseInt(array[0]) > Integer.parseInt(array[9])) {
			value = value + "龙";
		} else {
			value = value + "虎";
		}
		if (Integer.parseInt(array[1]) > Integer.parseInt(array[8])) {
			value = value + "龙";
		} else {
			value = value + "虎";
		}
		if (Integer.parseInt(array[2]) > Integer.parseInt(array[7])) {
			value = value + "龙";
		} else {
			value = value + "虎";
		}
		if (Integer.parseInt(array[3]) > Integer.parseInt(array[6])) {
			value = value + "龙";
		} else {
			value = value + "虎";
		}
		if (Integer.parseInt(array[4]) > Integer.parseInt(array[5])) {
			value = value + "龙";
		} else {
			value = value + "虎";
		}
		return value;
	}

	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/jsftRules.html"+ "?t=" + System.currentTimeMillis());
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
		para.add(JsftConstants.JSFT_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = jsftService.findJsftGaSessionList(
				hqls.toString(), para, statIndex, pageSize);
		List<JsftGaSession> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				JsftGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "急速飞艇");
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
				String value = this.getLongOrHu(session.getOpenResult());
				it.put("longhu", value);
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		data.put("items", items);
		data.put("total", ps.getTotalCount());
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		map.put("data", data);
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
		para.add(JsftConstants.JSFT_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = jsftService.findJsftGaSessionList(
				hqls.toString(), para, 0, 10);
		List<JsftGaSession> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			JSONObject obj = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				JsftGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("gameName", "幸运飞艇");
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
				String value = this.getLongOrHu(session.getOpenResult());
				it.put("longhu", value);
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
	 * 
	 * @param mapping
	 */
	public void open(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JsftGaSession currentSession = jsftService.getCurrentSession();
		if(currentSession!=null){
			String sessionNo = currentSession.getSessionNo();
//			int sessionNoBefore=Integer.valueOf(sessionNo)-1;
			JsftGaSession beforeSession=null;
			if(beforeSession==null){
				Integer sessionId = currentSession.getSessionId();
				int sessionIdBe=sessionId-1;
				beforeSession=(JsftGaSession) jsftService.getObject(JsftGaSession.class, sessionIdBe);
			}		
			String openResult = beforeSession.getOpenResult();
			if(openResult==null){
				beforeSession=null;
				if(beforeSession==null){
					Integer sessionId = currentSession.getSessionId();
					int sessionIdBe=sessionId-2;
					beforeSession=(JsftGaSession) jsftService.getObject(JsftGaSession.class, sessionIdBe);
				}
			}		
			openResult = beforeSession.getOpenResult();
			
			JSONObject obj=new JSONObject();
			obj.put("periodNumber", beforeSession.getSessionNo());
			obj.put("period", beforeSession.getSessionNo());
			obj.put("periodDate", beforeSession.getSessionNo());
			obj.put("awardTime", DateTimeUtil.DateToStringAll(beforeSession.getOpenTime()));
			
			
			System.out.println(openResult+"___________");
			String[] split = openResult.split(",");
			StringBuffer re=new StringBuffer();
			for (String string : split) {
				re.append(Integer.valueOf(string)+",");
				
			}
			re=re.delete(re.length()-1, re.length());
			
			obj.put("awardNumbers", ParamUtils.chkStringNotNull(re.toString()));
			
			map.put("current", obj);
			
			JSONObject nextobj=new JSONObject();
			nextobj.put("periodNumber", currentSession.getSessionNo());
			nextobj.put("period", currentSession.getSessionNo());
			nextobj.put("periodDate", currentSession.getSessionNo());
			nextobj.put("awardTime", DateTimeUtil.DateToStringAll(currentSession.getEndTime()));
			nextobj.put("delayTimeInterval ", 10);
			
			Date endTime = currentSession.getEndTime();
			Date date=new Date();
			
			long dateDiffToSec = DateTimeUtil.dateDiffToSec(endTime, date);
			nextobj.put("awardTimeInterval", dateDiffToSec*1000);
			map.put("next", nextobj);
		}
		map.put("time", System.currentTimeMillis());
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
			list = gaService.findGaWinCountList(Constants.GAME_TYPE_XY_JSFT);
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
