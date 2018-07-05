package help.gf.jsft;

import help.base.APIConstants;

import java.lang.reflect.Field;
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
import com.apps.model.Param;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.gf.jsft.JsFtConstants;
import com.gf.jsft.model.GfJsFtGaOmit;
import com.gf.jsft.model.GfJsFtGaSession;
import com.gf.jsft.model.GfJsFtGaTrend;
import com.gf.jsft.service.IJsFtService;
import com.gf.jsft.util.JsFtUtil;
import com.gf.fivecolor.FiveConstants;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.dto.WinCoDTO;
import com.game.service.IGaService;
import com.gf.pick11.gdpick11.GdPick11Constants;
import com.gf.pick11.gdpick11.model.GfGdPick11GaOmit;
import com.gf.pick11.gdpick11.model.GfGdPick11GaSession;
import com.gf.pick11.gdpick11.model.GfGdPick11GaTrend;
import com.gf.pick11.gdpick11.service.IGdPick11Service;
import com.gf.pick11.gdpick11.util.GdPick11Util;
import com.ram.RamConstants;
import com.ram.model.User;

public class JsFtManager extends BaseDispatchAction {
	private final IJsFtService gfJsFtService = (IJsFtService) getService("gfJsFtService");
	private final IGaService gaService = (IGaService) getService("gaService");
	
	
	
	/**
	 * 彩票玩法
	 * 
	 * @param mapping
	 */
	public void gamePlayType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		List<GaBetOption> opList = null;
		if (message.equals("")) {
			opList = gaService.findGaBetOptionByGameType(Constants.GAME_TYPE_GF_JSFT);
			
			GaSessionInfo  gaSessionInfo= gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_JSFT);//江苏快3
			if(gaSessionInfo !=null){
				data.put("img", ParamUtils.chkStringNotNull(StringUtil.checkAPIHttpUrl(gaSessionInfo.getImg())));//图片地址
				data.put("describe", gaSessionInfo.getDes());//描述
			}else{//找不到该游戏
				data.put("img", "");//图片地址
				data.put("describe", "");//描述
			}

			if(opList != null && opList.size() > 0){
				JSONArray barItem = new JSONArray();// 数据集合
				JSONObject barObj = null;
				for (GaBetOption op : opList) {
					barObj = new JSONObject();
					barObj.put("playType", op.getPlayType());//玩法类型
					barObj.put("playName", op.getTitle());//玩法名字
					barObj.put("betRate", op.getBetRate().toString());//赔率
					barObj.put("pointMultiple",op.getPointMultiple());//积分倍率
					barItem.put(barObj);
					data.put("items", barItem);
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
	
	
	
	/**
	 * 开奖
	 *
	 */
	public void sessionInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		if (message.equals("")) {
			if(ParamUtils.chkString(ucode)){
				User user = null;
				Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
				Integer uid = Integer.valueOf(decryptMap
						.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
				if (user == null) {
					data.put("money", "0"); //
					data.put("point", "0");//
				}else{
					BigDecimal balance = user.getMoney();
					if (balance == null)
						balance = new BigDecimal(0);// 判空处理
					BigDecimal point = user.getUserpoints();
					if (point == null)
						point = new BigDecimal(0);// 判空处理
					data.put("money",balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					data.put("point",balance.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
				}
			}else{
				data.put("money", "0"); 
				data.put("point", "0");//
			}
			
			
			GfJsFtGaSession session = gfJsFtService.getCurrentSession();
			if (session != null) {
				long openTime = (session.getEndTime().getTime() - System
						.currentTimeMillis()) / 1000; // 转成秒
				long betTime = 0;
				if (openTime >= JsFtConstants.JS_FT_TIME_OPENING_FREEZE) {
					betTime = openTime - JsFtConstants.JS_FT_TIME_OPENING_FREEZE;
				}
				data.put("sessionNo", session.getSessionNo()); //当前期号 
				data.put("betTime", Long.toString(betTime));//现在距离截止投注时间秒数
				data.put("openTime", Long.toString(openTime));//现在距离截止投注时间秒数
				data.put("openDate","");
				GfJsFtGaSession preSession = gfJsFtService.getPreviousSessionBySessionNo(String.valueOf(Long.parseLong(session.getSessionNo())-1));
				if(preSession == null){
					preSession = (GfJsFtGaSession) gfJsFtService.getObject(GfJsFtGaSession.class, session.getSessionId()-1);
				}
				if(preSession != null){
					String preSessionNo = preSession.getSessionNo();
					if(preSessionNo.length()>0){
						data.put("preSessionNo", preSessionNo);//上期期号
					}
					String openResult = preSession.getOpenResult();
					if(ParamUtils.chkString(openResult)){
						String array[] = preSession.getOpenResult().split(",|，|\\+|\\|");
						JSONArray jsonArray = new JSONArray();
						for (int j = 0; j < array.length; j++) {
							jsonArray.put(array[j]);
						}
						data.put("resultItems", jsonArray);//上期期号开奖结果
					}else{
						data.put("resultItems",  new JSONArray());//上期期号开奖结果
					}
				}else{
					data.put("preSessionNo", "");//上期期号
					data.put("resultItems", new JSONArray());
				}
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();

				hqls.append("and ho.sessionNo > ? ");
				para.add(session.getSessionNo());
				hqls.append(" order by ho.sessionNo asc");
				PaginationSupport ps = gfJsFtService.findGfJsFtGaSessionList(hqls.toString(), para, 0, 50);
				List<GfJsFtGaSession> list = ps.getItems();
				JSONArray noArr = new JSONArray();
				if(list !=null){
					for(int i=0;i<list.size();i++){
						GfJsFtGaSession se = list.get(i);
						noArr.put(se.getSessionNo());
					}
				}
				data.put("noArray", noArr);//50期期号
				code = APIConstants.CODE_REQUEST_SUCCESS;	
			}else{//查询不到当前期
				data.put("sessionNo", "");
				data.put("openTime", "");//现在距离开奖时间秒数
				data.put("betTime", "");//现在距离截止投注时间秒数
				data.put("openDate","");
				data.put("preSessionNo", "");//上期期号
				data.put("resultItems", new JSONArray());
				data.put("noArray", new JSONArray());//50期期号
			}
		}
		map.put("data", data);
		map.put("msg", message);
		map.put("code", code);
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
//		String playType = ParamUtils.getParameter(deParameter, "playType",FcConstants.PLAY_TYPE_SIX);//默认是三星直选
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		List<GfJsFtGaTrend> list = null;
		if (message.equals("")) {
			JSONArray items = new JSONArray();// 十位数据集合
//			JSONArray unit = new JSONArray();// 个位数据集合
			JSONObject obj = null;
			list = gfJsFtService.findGfJsFtGaTrendList();
			if (list != null && list.size() > 0) {
				for (GfJsFtGaTrend tr : list) {//String.format("%03d", j);
					obj = new JSONObject();
//					obj.put("ballName", String.format("%02d", Integer.parseInt(tr.getTrendTitle()))); //球
					obj.put("ballName", tr.getTrendTitle()); //球
					obj.put("trend", tr.getTrendCount()); //最近开奖距离现在场次
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "操作成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = "操作失败";
			}
			data.put("items", items);
//			data.put("unit", unit);
		}
		map.put("data", data);
		map.put("msg", message);
		map.put("code", code);
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

		String betBallArray = ParamUtils.getParameter(deParameter,
			    "betBallArray", "");// [{type:1,betBall:12,13,16,17,19|2}] 投注的球
		String sessionArray = ParamUtils.getParameter(deParameter,
				"sessionArray", "");// [{sessionNo:12,multiple:2}] 期号，倍数
		
		String betType = ParamUtils.getParameter(deParameter, "betType"); //购买类型 0=代购1=合买
		String isAddNo = ParamUtils.getParameter(deParameter, "isAddNo",FiveConstants.NOT_ADD_NO); //是否追号--代购
		String isWinStop = ParamUtils.getParameter(deParameter, "isWinStop",FiveConstants.WIN_NO_STOP); //是否中奖后停止---代购
		String num = ParamUtils.getParameter(deParameter, "num","0"); //总份数  ---合买
		String buyNum = ParamUtils.getParameter(deParameter, "buyNum","0"); //购买份数---合买
		String isGuaranteed =  ParamUtils.getParameter(deParameter, "isGuaranteed",FiveConstants.NOT_GUARANTEE); //是否保底---合买
		String guaranteedNum =  ParamUtils.getParameter(deParameter, "guaranteedNum","0"); //保底份数---合买
		String ucode = ParamUtils.getParameter(deParameter, "u");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";

		String sessionNo ="";
		JSONArray jsonArray =null;//期号，倍数
		if (!ParamUtils.chkString(ucode) || !ParamUtils.chkString(betBallArray)||
				!ParamUtils.chkString(sessionArray)|| !ParamUtils.chkString(betType)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		} else {// 判断是否在投注时间内
			jsonArray = new JSONArray(sessionArray);
			JSONObject seObj = jsonArray.getJSONObject(0);
			sessionNo = seObj.get("s").toString(); //追号第一期或者合买期号
			if(sessionNo==null||"".equals(sessionNo)){
				message = "参数有误";
			}else{
				GfJsFtGaSession gaSession = gfJsFtService.getPreviousSessionBySessionNo(sessionNo);
				GfJsFtGaSession tempSession = (GfJsFtGaSession) gfJsFtService.getObject(GfJsFtGaSession.class, gaSession.getSessionId()-1);
				Date nowDate = new Date();
				Date startTime = tempSession.getEndTime();
				Date endTime = gaSession.getEndTime();
				Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -JsFtConstants.JS_FT_TIME_OPENING_FREEZE);// 封盘时间
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
				GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_GF_JSFT);
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
		}

		if (message.equals("")) {
			GfJsFtGaSession session = gfJsFtService.getCurrentSession();
			if (!sessionNo.equals(session.getSessionNo())) {
				message = "参数错误！";
			}else{
				if (sessionNo.equals(session.getSessionNo())) {
					User user = null;
					Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
					Integer uid = Integer.valueOf(decryptMap
							.get(Constants.DES_KEY_UID));
					user = userService.getValidUser(uid);
					if (user == null) {
						code = APIConstants.CODE_REQUEST_ERROR;
						message = "用户不存在";
					} else {
						JSONArray betBallJson = new JSONArray(betBallArray);
						List<String> list = new ArrayList<String>();
						Map<String,Integer> seMap = new HashMap<String,Integer>();
						boolean isVerification=false;//验证通过与否
						for(int i=0;i<betBallJson.length();i++){
							JSONObject betBallObj = betBallJson.getJSONObject(i);
							String type = betBallObj.get("t").toString(); //玩法
							String betBall = betBallObj.get("b").toString(); //投注球
							if(JsFtConstants.GAME_TYPE_GF_DAN_SHUANG_JI.equals(type)
									|| JsFtConstants.GAME_TYPE_GF_DAN_SHUANG_YA.equals(type)
									|| JsFtConstants.GAME_TYPE_GF_DAN_SHUANG_GUAN.equals(type)){
								isVerification=JsFtUtil.chkDAN_SHUANG(betBall);
							}else if(JsFtConstants.GAME_TYPE_GF_DAXI_AO_JI.equals(type)
									|| JsFtConstants.GAME_TYPE_GF_DAXI_AO_YA.equals(type)
									|| JsFtConstants.GAME_TYPE_GF_DAXI_AO_GUAN.equals(type)){
								isVerification=JsFtUtil.chkDAXI_AO(betBall);
							}else if(JsFtConstants.GAME_TYPE_GF_DING_WEI_DAN_1_5.equals(type)
									|| JsFtConstants.GAME_TYPE_GF_DING_WEI_DAN_6_10.equals(type)){
								isVerification=JsFtUtil.chkDING_WEI_DAN(betBall);
							}else if(JsFtConstants.GAME_TYPE_GF_QIAN_SAN.equals(type)){
								isVerification=JsFtUtil.chkQIAN_SAN(betBall);
							}else if(JsFtConstants.GAME_TYPE_GF_QIAN_ER.equals(type)){
								isVerification=JsFtUtil.chkQIAN_ER(betBall);
							}else if(JsFtConstants.GAME_TYPE_GF_QIAN_YI.equals(type)){
								isVerification=JsFtUtil.chkQIAN_YI(betBall);
							}
							
							if(!isVerification){
								code = APIConstants.CODE_REQUEST_ERROR;
								message = "投注错误！";
								map.put("code", code);
								map.put("msg", message);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
							list.add(type+"+"+betBall);
						}
						
						int betTotalNum = JsFtUtil.getTotalBetNum(list);//总投注注数
						if(betTotalNum<0){
							message = "投注选项不对！";
							code = APIConstants.CODE_REQUEST_ERROR;
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}
						int multiple =0;
						BigDecimal betMoney=new BigDecimal(0);//本次总需花费金额
						BigDecimal buyMoney=new BigDecimal(0);//认购金额
						if(betType.equals(Constants.PUB_STATUS_OPEN)){//合买
							if(jsonArray.length()!=1){
								message = "投注数量不对！";
								code = APIConstants.CODE_REQUEST_ERROR;
								map.put("code", code);
								map.put("msg", message);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
							for(int i=0;i<jsonArray.length();i++){
								JSONObject sessionObj = jsonArray.getJSONObject(i);
								String nextSeNo =sessionObj.get("s").toString();
								multiple = Integer.parseInt(sessionObj.get("m").toString());
								if(Long.parseLong(nextSeNo)<Long.parseLong(sessionNo)||multiple<=0){
									message = "期号或者倍数不对";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								seMap.put(nextSeNo, multiple);
							}
						}else{//代购
							if(isAddNo.equals(FiveConstants.ADD_NO)){//追号
								if(jsonArray.length()<2){
									message = "投注数量不对！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
							}else{
								if(jsonArray.length()!=1){
									message = "投注数量不对！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
							}
							for(int i=0;i<jsonArray.length();i++){
								JSONObject sessionObj = jsonArray.getJSONObject(i);
								String nextSeNo =sessionObj.get("s").toString();
								multiple = Integer.parseInt(sessionObj.get("m").toString());
								if(Long.parseLong(nextSeNo)<Long.parseLong(sessionNo)||multiple<=0){
									message = "期号或者倍数不对";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}else{
									seMap.put(nextSeNo, multiple);
									betMoney=betMoney.add(new BigDecimal(betTotalNum*1).multiply(new BigDecimal(multiple)));
								}					
							}				
						}
						
						int num2 = Integer.parseInt(num); //总份数
						int buyNum2 = Integer.parseInt(buyNum); //购买份数
						int guNum=Integer.parseInt(guaranteedNum);//保底份数
						if(betTotalNum<=0){
							message = "您没有投注！";
							code = APIConstants.CODE_REQUEST_ERROR;
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}else{
							if(betType.equals(Constants.PUB_STATUS_OPEN)){//合买
								if(new BigDecimal(betTotalNum*2*multiple).divide(new BigDecimal(num2),2, BigDecimal.ROUND_HALF_EVEN).compareTo(new BigDecimal(1))<0){//每份金额不能少于1元
									message = "每份不能少于1积分！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								if(buyNum2<0){
									message = "购买不能少于1份！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								
								if(isGuaranteed.equals("1")){
									if((buyNum2+guNum)>num2){
										message = "保底份额和认购份额之和不能大于总份额！";
										code = APIConstants.CODE_REQUEST_ERROR;
										map.put("code", code);
										map.put("msg", message);
										JsonUtil.AjaxWriter(response, map);
										return;
									}
								}
								if(new BigDecimal(guNum).divide(new BigDecimal(betTotalNum),2, BigDecimal.ROUND_HALF_EVEN).compareTo(new BigDecimal(Constants.GUARANTEE_PERCENTAGE))<0){//保底份额不能少于20%
									message = "保底份额不能少于20%！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								betMoney = new BigDecimal(betTotalNum*2*multiple).divide(new BigDecimal(num2),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(buyNum2+guNum)); //本次投注金额
								buyMoney = new BigDecimal(betTotalNum*2*multiple).divide(new BigDecimal(num2),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(buyNum2));
							}
							BigDecimal userMoney = ProductUtil.checkBigDecimal(user.getMoney()); //用户余额
							if(userMoney==null){
								userMoney=new BigDecimal(0);
							}
							if(betMoney.compareTo(userMoney) == 1){
								message = "您的余额不足！";
								code = APIConstants.CODE_REQUEST_ERROR;
								map.put("code", code);
								map.put("msg", message);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
						}

						if(FiveConstants.SPONSOR.equals(betType)){//合买
							user = gfJsFtService.saveSponsorBet(user,list,seMap,num2,buyNum2,isGuaranteed,guNum,betMoney,new BigDecimal(betTotalNum*2*multiple),betTotalNum,session,buyMoney);
						}else if(FiveConstants.PROCUREMENT_SERVICE.equals(betType)){//代购							
							user = gfJsFtService.saveProcurementServiceBet(user,list,seMap,isAddNo,isWinStop,betMoney,betTotalNum,session);
						}
					  data.put("money", ProductUtil.BigFormatJud(user.getMoney()));
					  code = APIConstants.CODE_REQUEST_SUCCESS;
					  message = "投注成功！";
					}
				}else{
					code = APIConstants.CODE_REQUEST_ERROR;
					message = "已封盘，投注无效";				
				}
			}
		}else{
			code = APIConstants.CODE_REQUEST_ERROR;
//			message = "投注无效";
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 冷热排行
	 * 
	 */
	public void hotRanking(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		List<GfJsFtGaTrend> list = gfJsFtService.findGfJsFtGaTrendList();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (GfJsFtGaTrend trend : list) {
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
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=今天 1=昨天 2=前天 3=18期  4=50期  5=100期  6=200期   7=500期  8=1000期

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		List<GfJsFtGaOmit> list=new ArrayList<GfJsFtGaOmit>();
		int num=JsFtConstants.JS_FT_MAX_PART;
		if(type.equals("0")){
			num=17;
			GfJsFtGaSession session=gfJsFtService.getFirstSessionByDate(DateTimeUtil.DateToString(new Date())+" 00:00:00");
			hqls.append(" and ho.sessionNo>=? ");// 已开奖
			para.add(session.getSessionNo());
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfJsFtService.findGfJsFtGaOmitList(hqls.toString(),para,num);
		}else if(type.equals("1")){
			GfJsFtGaSession session=gfJsFtService.getFirstSessionByDate(DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(new Date(), -1))+" 00:00:00");
			GfJsFtGaSession endSession=gfJsFtService.getEndSessionByDate(DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(new Date(), -1))+" 00:00:00");
			hqls.append(" and ho.sessionNo>=? ");// 已开奖
			para.add(session.getSessionNo());
			hqls.append(" and ho.sessionNo<=? ");// 已开奖
			para.add(endSession.getSessionNo());
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfJsFtService.findGfJsFtGaOmitList(hqls.toString(),para,num);
		}else if(type.equals("2")){
			GfJsFtGaSession session=gfJsFtService.getFirstSessionByDate(DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(new Date(), -2))+" 00:00:00");
			GfJsFtGaSession endSession=gfJsFtService.getEndSessionByDate(DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(new Date(), -2))+" 00:00:00");
			hqls.append(" and ho.sessionNo>=? ");// 已开奖
			para.add(session.getSessionNo());
			hqls.append(" and ho.sessionNo<=? ");// 已开奖
			para.add(endSession.getSessionNo());
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfJsFtService.findGfJsFtGaOmitList(hqls.toString(),para,num);
		}else{
			GfJsFtGaSession session=gfJsFtService.getCurrentSession();
			if(session!=null){
				hqls.append(" and ho.sessionNo<=? ");// 已开奖
				para.add(session.getSessionNo());
				if(type.equals("3")){
					num=18;
				}else if(type.equals("4")){
					num=50;
				}else if(type.equals("5")){
					num=100;
				}else if(type.equals("6")){
					num=200;
				}else if(type.equals("7")){
					num=500;
				}else if(type.equals("8")){
					num=1000;
				}
				hqls.append(" order by  ho.sessionNo desc ");// 已开奖
				list=gfJsFtService.findGfJsFtGaOmitList(hqls.toString(),para,num);
			}
		}


		JSONArray jsonArray = new JSONArray();
//		int chuxian=0;//出现次数
		
		if (list != null && list.size() > 0) {
			
			for(GfJsFtGaOmit omit:list){
				JSONObject obj = new JSONObject();
				obj.put("sessionNo", omit.getSessionNo());
				JSONArray subitems = new JSONArray();
				for(int i=1;i<=6;i++){
					Field f = GfJsFtGaOmit.class.getDeclaredField("fenbu"+i);
					f.setAccessible(true);
					if(f.get(omit).toString().equals("0")){
						subitems.put(i+"&");
					}else{
						subitems.put(f.get(omit).toString());
					}
				}
				obj.put("subitems", subitems);
				String array[]=omit.getOpenResult().split(",");
				JSONArray openitems = new JSONArray();
				for(int i=0;i<array.length;i++){
					openitems.put(array[i]);
				}
				obj.put("openitems", openitems);
				obj.put("he",omit.getHe()+"");
				obj.put("kuadu",omit.getKuadu()+"");
				obj.put("daxiao",omit.getDaxiao());
				obj.put("jiou",omit.getJiou());
				obj.put("zhihe",omit.getZhihe());
				jsonArray.put(obj);
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
			if(Integer.parseInt(array[i]) == 11){
				items.put("和");
			}else{
				if (Integer.parseInt(array[i]) < 6) {
					items.put("小");
				} else {
					items.put("大");
				}		
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
			if (Integer.parseInt(array[i]) == 11){
				items.put("和");
			}else{
				if (Integer.parseInt(array[i]) % 2 == 0) {
					items.put("双");
				} else {
					items.put("单");
				}	
			}
		}
	}


	/**
	 * 走势图的总和/龙虎的判断
	 * 
	 * @param results
	 * @return
	 */
	public void getHistorySum(String results, JSONArray items) {
		String array[] = results.split(",");
		int result = 0;
		for(int i=0; i<array.length; i++){
			result = result + Integer.parseInt(array[i]);
		}
		items.put(result + "");
		
		if(result == 30){
			items.put("和");
		}else if(result > 30){
			items.put("大");
		}else if(result < 30){
			items.put("小");
		}
		
		if (result % 2 == 0) {
			items.put("双");
		} else {
			items.put("单");
		}
		
		int theUnit = result %10; // 尾数/个位数
		if(theUnit >= 5){
			items.put("尾大");
		}else{
			items.put("尾小");
		}
		
	}

	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/gdPick11.html"
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
		PaginationSupport ps = gfJsFtService.findGfJsFtGaSessionList(
				hqls.toString(), para, statIndex, pageSize);
		List<GfJsFtGaSession> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GfJsFtGaSession session = list.get(i);
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
	 */
	public void openList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);//页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);//每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(JsFtConstants.JS_FT_OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = gfJsFtService.findGfJsFtGaSessionList(
				hqls.toString(), para, statIndex, pageSize);
		List<GfJsFtGaSession> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < list.size(); i++) {
				GfJsFtGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				it.put("openResult", session.getOpenResult());
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
		}else{
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
			list = gaService.findGaWinCountList(Constants.GAME_TYPE_GF_JSFT);
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
