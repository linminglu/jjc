package help.gf.ssq;

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
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.game.model.GaSessionInfo;
import com.gf.dcb.DcbConstants;
import com.gf.dcb.model.GfDcbGaOmit;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.model.GfDcbGaTrend;
import com.gf.dcb.service.IDcbService;
import com.gf.dcb.util.DcbUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.gf.pick11.gdpick11.GdPick11Constants;
import com.ram.RamConstants;
import com.ram.model.User;

public class SsqManager extends BaseDispatchAction {
	private final IDcbService gfDcbService = (IDcbService) getService("gfDcbService");

	/**
	 * 面板选项
	 * 
	 * @param mapping
	 */

	public void betPanel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONArray redItems = new JSONArray();// 红球数据集合
		JSONArray blueItems = new JSONArray();// 蓝球数据集合
		JSONObject redObj = null;
		JSONObject blueObj = null;
		List<GfDcbGaTrend> list = null;
		if (message.equals("")) {
			list = gfDcbService.findDcbTrendList();
			if (list != null && list.size() > 0) {
				for (GfDcbGaTrend tr : list) {
					
					
					if(DcbConstants.RED_BALL.equals(tr.getType())){
						redObj = new JSONObject();
						redObj.put("ballName", tr.getTrendTitle());
						redObj.put("trend", tr.getTrendCount());
						redItems.put(redObj);
					}else{
						blueObj = new JSONObject();
						blueObj.put("ballName", tr.getTrendTitle());
						blueObj.put("trend", tr.getTrendCount());
						blueItems.put(blueObj);
					}
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
				message = "操作成功";
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = "操作失败";
			}
			data.put("blueItems", blueItems);
			data.put("redItems", redItems);
		}
		map.put("data", data);
		map.put("msg", message);
		map.put("code", code);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 下注
	 */
	public void bet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String betBallArray = ParamUtils.getParameter(deParameter,
				"betBallArray", "");// [{b:12,13,16,17,19|2}] 投注的球
		String sessionArray = ParamUtils.getParameter(deParameter,
				"sessionArray", "");// [{s:12,m:2}] 期号，倍数
		String betType = ParamUtils.getParameter(deParameter, "betType"); //购买类型 0=代购1=合买
		String isAddNo = ParamUtils.getParameter(deParameter, "isAddNo","0"); //是否追号--代购
		String isWinStop = ParamUtils.getParameter(deParameter, "isWinStop","0"); //是否中奖后停止---代购
		String num = ParamUtils.getParameter(deParameter, "num","0"); //总份数  ---合买
		String buyNum = ParamUtils.getParameter(deParameter, "buyNum","0"); //购买份数---合买
		String isGuaranteed =  ParamUtils.getParameter(deParameter, "isGuaranteed","0"); //是否保底---合买
		String guaranteedNum =  ParamUtils.getParameter(deParameter, "guaranteedNum","0"); //保底份数---合买
		String ucode = ParamUtils.getParameter(deParameter, "u");

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String message = "";
			
		JSONArray jsonArray = new JSONArray(sessionArray);
		JSONObject seObj = jsonArray.getJSONObject(0);
		String sessionNo =  seObj.get("s").toString(); //追号第一期或者合买期号
		String multipleFirst = seObj.get("m").toString();
		
		if (!ParamUtils.chkString(ucode) || !ParamUtils.chkString(betBallArray)
				|| !ParamUtils.chkString(sessionNo)|| !ParamUtils.chkString(betType)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		} else {// 判断是否在投注时间内
			GfDcbGaSession ga3Session = gfDcbService.getPreviousSessionBySessionNo(sessionNo);
			GfDcbGaSession tempSession = (GfDcbGaSession) gfDcbService.getObject(GfDcbGaSession.class, ga3Session.getSessionId()-1);
			Date nowDate = new Date();
			Date startTime = tempSession.getEndTime();
			Date endTime = ga3Session.getEndTime();
			Date fengPanTime = DateTimeUtil.dateDiffSec(endTime, -(int)DcbConstants.DCB_BET_END_INTERVAL);// 封盘时间
			long sec1 = DateTimeUtil.dateDiffToSec(startTime, nowDate);
			long sec2 = DateTimeUtil.dateDiffToSec(nowDate, fengPanTime);
			long sec3 = DateTimeUtil.dateDiffToSec(nowDate, endTime);
			if (sec1 < 0) {// 还没到投注时间
				message = "本期还未开盘，请稍后";
			} else if (sec3 < 0) {// 过了开奖时间
				message = "本期已结束，请选择其他投注";
			} else if (sec3 > 0 && sec2 < 0) {// 正好在封盘时间内
				message = "本期已封盘，请等待下期投注";
			}
			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_GF_CQSSC);
			if(Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getStatus()) || 
					Constants.PUB_STATUS_CLOSE.equals(gaSessionInfo.getBetAvoid())){
				message = "当前彩种维护中";
			}
		}

		if (message.equals("")) {
			GfDcbGaSession session = gfDcbService.getCurrentSession();
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
						String status = user.getStatus();
						if(Constants.PUB_STATUS_CLOSE.equals(status)){
							code = APIConstants.CODE_REQUEST_ERROR;
							message = "用户无效";
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}
						JSONArray betBallJson = new JSONArray(betBallArray);
						ArrayList<String> list = new ArrayList<String>();
						
						for(int i=0;i<betBallJson.length();i++){
							JSONObject betBallObj = betBallJson.getJSONObject(i);
							String betBall = betBallObj.get("b").toString();
//							String[] redBall = betBall.split("\\|")[0].split(",");
//							String[] blueBall = betBall.split("\\|")[1].split(",");
							if(!DcbUtil.chkBetting(betBall)){
								message = "无效投注，请检查后在试";
								code = APIConstants.CODE_REQUEST_ERROR;
								map.put("code", code);
								map.put("msg", message);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
							list.add(betBall);
						}
						
						Map<String,Integer> seMap = new HashMap<String,Integer>();//
						int betNum = DcbUtil.getTotalBetNum(list); //投注总注数
						int totalMultiple=0; //投注倍数

						if(ParamUtils.chkString(betType)){ //如果不为空
							BigDecimal betMoney = new BigDecimal(0);//本次投注金额
							BigDecimal userMoney = ProductUtil.checkBigDecimal(user.getMoney()); //用户余额

							if(Constants.PROCUREMENT_SERVICE.equals(betType)){ //代购
								if(Integer.parseInt(isAddNo)==0){//不追号
									if(jsonArray.length()>1){ //投注期号却多
										message = "没有追号不能投注多期！";
										code = APIConstants.CODE_REQUEST_ERROR;
										map.put("code", code);
										map.put("msg", message);
										JsonUtil.AjaxWriter(response, map);
										return;							
									}
								}
								for(int i=0;i<jsonArray.length();i++){
									JSONObject sessionObj = jsonArray.getJSONObject(i);
									int nextSeNo = Integer.parseInt(sessionObj.get("s").toString()) ;
									int multiple = Integer.parseInt(sessionObj.get("m").toString());
									totalMultiple = totalMultiple +multiple;
									if(nextSeNo<Integer.parseInt(sessionNo)||multiple==0){
										message = "期号或者倍数不对";
										code = APIConstants.CODE_REQUEST_ERROR;
										map.put("code", code);
										map.put("msg", message);
										JsonUtil.AjaxWriter(response, map);
										return;
									}
									System.out.println(nextSeNo+"::"+multiple);
									seMap.put(Integer.toString(nextSeNo), multiple);
								}
								
								betMoney = new BigDecimal(betNum*1*totalMultiple);
								
								// -1 小于 0等于 1大于
								if(betMoney.compareTo(userMoney) == 1){
									message = "您的余额不足！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								user = gfDcbService.saveProcurementServiceBet(user,list,seMap,isAddNo,isWinStop,betNum);

							}else if(Constants.SPONSOR.equals(betType)){ //合买
								Integer multiple=Integer.parseInt(multipleFirst);//合买倍数
								int num2= Integer.parseInt(num);
								int buyNum2=Integer.parseInt(buyNum);
								int guaranteedNum2= Integer.parseInt(guaranteedNum);
								if(num2==0 || buyNum2== 0||buyNum2>num2){
									message = "总份数或者购买份数必须大于0！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								if(Constants.GUARANTEE.equals(isGuaranteed)){ //保底
									BigDecimal guarantPrecent = new BigDecimal(guaranteedNum2).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);
									if(buyNum2+guaranteedNum2>num2||guarantPrecent.compareTo(new BigDecimal(Constants.GUARANTEE_PERCENTAGE))<0){ //保底+购买>总份数||保底少于20%
										message = "保底份额必须大于20%！";
										code = APIConstants.CODE_REQUEST_ERROR;
										map.put("code", code);
										map.put("msg", message);
										JsonUtil.AjaxWriter(response, map);
										return;
									}
									
									betMoney = new BigDecimal(betNum*2*multiple*(buyNum2+guaranteedNum2)).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN); 
								}else{//不保底
									betMoney = new BigDecimal(betNum*2*multiple*buyNum2).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN); 
								}
								// -1 小于 0等于 1大于
								if(betMoney.compareTo(userMoney) == 1){
									message = "您的余额不足！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								if(new BigDecimal(betNum*2*multiple).divide(new BigDecimal(num2),2, BigDecimal.ROUND_HALF_EVEN).compareTo(new BigDecimal(1))<0){//每份金额不能少于1元
									message = "每份不能少于1积分！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;
								}
								if(jsonArray.length()>1){ //合买却投注多期
									message = "合买不能投注多期！";
									code = APIConstants.CODE_REQUEST_ERROR;
									map.put("code", code);
									map.put("msg", message);
									JsonUtil.AjaxWriter(response, map);
									return;							
								}
						
								user = gfDcbService.saveSponsorBet(session,multiple,user,list,num2,buyNum2,isGuaranteed,guaranteedNum2, betNum,betMoney);

							}else{
								message = "参数不正确";
								code = APIConstants.CODE_REQUEST_ERROR;
								map.put("code", code);
								map.put("msg", message);
								JsonUtil.AjaxWriter(response, map);
								return;
							}
						}else{ //betType 为空
							message = "参数不正确";
							code = APIConstants.CODE_REQUEST_ERROR;
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}
						data.put("point", ProductUtil.BigFormatJud(user.getUserpoints()));
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
	 * 最新开奖
	 * 
	 * @param mapping
	 */
	public void latestLottery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(DcbConstants.DCB_OPEN_STATUS_OPENING);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		PaginationSupport ps = gfDcbService.findGfDcbGaSessionList(hqls.toString(),
				para, 0, 10);
		List<GfDcbGaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				GfDcbGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("sessionNo", session.getSessionNo());
				
				String array[] = session.getOpenResult().split(",|，|\\+|\\|");
				JSONArray jsonArray = new JSONArray();
				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				it.put("resultItems", jsonArray);
				items.put(it);
			}
			data.put("items", items);
			code = APIConstants.CODE_REQUEST_SUCCESS;
			msg = APIConstants.TIPS_OPERATION_SUCC;
			map.put("data", data);
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("code", code);
		map.put("msg", msg);
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
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ho.openStatus=? ");// 已开奖
		para.add(Constants.OPEN_STATUS_OPENED);
		hqls.append(" order by  ho.sessionId desc ");// 已开奖
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;
		PaginationSupport ps = gfDcbService.findGfDcbGaSessionList(hqls.toString(),
				para, statIndex, pageSize);
		List<GfDcbGaSession> list = ps.getItems();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			JSONArray items = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				GfDcbGaSession session = list.get(i);
				JSONObject it = new JSONObject();
				it.put("openTime",
						DateTimeUtil.DateToStringHHMM(session.getOpenTime()));
				it.put("sessionNo", session.getSessionNo());
				JSONArray jsonArray = new JSONArray();

				if(session.getOpenResult()!= null){
					String array[] = session.getOpenResult().split(",|，|\\+|\\|");
					for (int j = 0; j < array.length; j++) {
						jsonArray.put(array[j]);
					}
				}
				it.put("resultItems", jsonArray);
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
			code = APIConstants.CODE_NOT_FOUND;
			msg = APIConstants.TIPS_DATA_NOT;
		}
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 开奖信息
	 */
	public void sessionInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String ucode = ParamUtils.getParameter(deParameter, "u");

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(ParamUtils.chkString(ucode)){
			User user = null;
			Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			user = (User) userService.getObject(User.class, uid);
			if (user == null) {
				data.put("money", "0");
				data.put("point", "0");
			}else{
				BigDecimal balance = user.getMoney();
				if (balance == null)
					balance = new BigDecimal(0);// 判空处理
				BigDecimal points = user.getUserpoints();
				if (points == null)
					points = new BigDecimal(0);// 判空处理

				data.put("money",balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				data.put("point",points.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			}
		}else{
			data.put("money", "0");
			data.put("point", "0");
		}
		GfDcbGaSession session = gfDcbService.getCurrentSession();
		if (session != null) {
			long openTime = (session.getEndTime().getTime() - System
					.currentTimeMillis()) / 1000; // 转成秒
			long betTime = 0;
			if (openTime >= 30*60) {
				betTime = openTime - 30*60;
			}
			data.put("sessionNo", session.getSessionNo()); //当前期号 
			data.put("betTime", Long.toString(betTime));//现在距离封盘时间秒数
			data.put("openTime", Long.toString(openTime));//现在距离开奖时间秒数
			data.put("awardDate",
					DateTimeUtil.DateToStringHHMM(session.getAwardTime())); //最迟领奖时间
			data.put("openDate",
					DateTimeUtil.DateToStringHHMM(session.getEndTime())); //开奖时间
			
			GfDcbGaSession preSession = gfDcbService.getPreviousSessionBySessionNo(String.valueOf(Integer.parseInt(session.getSessionNo())-1));
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
				preSession = (GfDcbGaSession) gfDcbService.getObject(GfDcbGaSession.class, session.getSessionId()-1);
				if(preSession != null){
					data.put("preSessionNo", preSession.getSessionNo());//上期期号
					String array[] = preSession.getOpenResult().split(",|，|\\+|\\|");
					JSONArray jsonArray = new JSONArray();
					for (int j = 0; j < array.length; j++) {
						jsonArray.put(array[j]);
					}
					data.put("resultItems", jsonArray);//上期期号开奖结果
				}else{
					data.put("preSessionNo", "");//上期期号
					data.put("resultItems",  new JSONArray());//上期期号开奖结果
				}
			}

			code = APIConstants.CODE_REQUEST_SUCCESS;
		}else{//查询不到当前期
			data.put("sessionNo", "");
			data.put("openTime", "");
			data.put("betTime", "");
			data.put("preSessionNo", "");//上期期号
			data.put("resultItems",  new JSONArray());//上期期号开奖结果
		}
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		hqls.append("and ho.sessionNo > ? ");
		para.add(session.getSessionNo());
		hqls.append(" order by ho.sessionNo asc");
		PaginationSupport ps = gfDcbService.findGfDcbGaSessionList(hqls.toString(), para, 0, 50);
		List<GfDcbGaSession> list = ps.getItems();
		if(list !=null){
			JSONArray noArr = new JSONArray();
			for(int i=0;i<list.size();i++){
				GfDcbGaSession se = list.get(i);
				noArr.put(se.getSessionNo());
			}
			data.put("noArray", noArr);//50期期号
		}
		
		map.put("data", data);
		map.put("code", code);
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
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 0=30期 1=50期 2=100期   3=从多少期到多少期
		String startSessionNo= ParamUtils.getParameter(deParameter, "startSessionNo", "");//开始期号
		String endSessionNo= ParamUtils.getParameter(deParameter, "endSessionNo", "");//结束期号
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		String msg = "";
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		List<GfDcbGaOmit> list=new ArrayList<GfDcbGaOmit>();
		int num=GdPick11Constants.GD_PICK11_MAX_PART;

		if(type.equals("0")){
			num=30;
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfDcbService.findGfDcbGaOmitList(hqls.toString(),para,num);
		}else if(type.equals("1")){
			num=50;
			hqls.append(" order by  ho.sessionNo desc");
			list=gfDcbService.findGfDcbGaOmitList(hqls.toString(),para,num);
		}else if(type.equals("2")){
			num=100;
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfDcbService.findGfDcbGaOmitList(hqls.toString(),para,num);
		}else if(type.equals("3")){
			num=1000;//最大查询1000条
			if(!ParamUtils.chkString(startSessionNo)||!ParamUtils.chkString(endSessionNo)){
				msg="期数填写有误，请重新填写";
			}else{
				if(Integer.parseInt(startSessionNo)>Integer.parseInt(endSessionNo)){
					msg="开始期号不能大于结束期号";
				}
			}
			hqls.append(" and ho.sessionNo>=? ");// 已开奖
			para.add(startSessionNo);
			hqls.append(" and ho.sessionNo<=? ");// 已开奖
			para.add(endSessionNo);
			hqls.append(" order by  ho.sessionNo desc");// 已开奖
			list=gfDcbService.findGfDcbGaOmitList(hqls.toString(),para,num);
		}
		if(msg.equals("")){
			JSONArray jsonArray = new JSONArray();
//			int chuxian=0;//出现次数
			
			if (list != null && list.size() > 0) {
				
				for(GfDcbGaOmit omit:list){
					JSONObject obj = new JSONObject();
					obj.put("sessionNo", omit.getSessionNo());
					JSONArray subitems = new JSONArray();
					for(int i=1;i<=33;i++){
						Field f = GfDcbGaOmit.class.getDeclaredField("red"+i);
						f.setAccessible(true);
						if(f.get(omit).toString().equals("0")){
							subitems.put(i+"&");
						}else{
							subitems.put(f.get(omit).toString());
						}
					}
					obj.put("reditems", subitems);
					JSONArray blueitems = new JSONArray();
					for(int i=1;i<=16;i++){
						Field f = GfDcbGaOmit.class.getDeclaredField("blue"+i);
						f.setAccessible(true);
						if(f.get(omit).toString().equals("0")){
							blueitems.put(i+"&");
						}else{
							blueitems.put(f.get(omit).toString());
						}
					}
					obj.put("blueitems", blueitems);
//					String array[]=omit.getOpenResult().split(",");
//					JSONArray openitems = new JSONArray();
//					for(int i=0;i<array.length;i++){
//						openitems.put(array[i]);
//					}
//					obj.put("openitems", openitems);
					jsonArray.put(obj);
				}
				data.put("items", jsonArray);
				
				msg = APIConstants.TIPS_OPERATION_SUCC;
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				msg = APIConstants.TIPS_DATA_NOT;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}


		map.put("data", data);
		map.put("code", code);
		map.put("msg", msg);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 游戏规则
	 */
	public void rules(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", RamConstants.getWebServer() + "/help/ssqRule.html"
				+ "?t=" + System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

}
