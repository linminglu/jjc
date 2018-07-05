package com.gf.k3.jsk3.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.dto.SessionItem;
import com.apps.model.UserTradeDetailRl;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.dto.SpDetailDTO;
import com.game.service.IGaService;
import com.gf.dcb.DcbConstants;
import com.gf.dcb.util.DcbUtil;
import com.gf.fivecolor.FiveConstants;
import com.gf.k3.jsk3.JsK3Constants;
import com.gf.k3.jsk3.dao.IJsK3DAO;
import com.gf.k3.jsk3.model.GfJsK3GaOmit;
import com.gf.k3.jsk3.model.GfJsK3GaSession;
import com.gf.k3.jsk3.model.GfJsK3GaTrend;
import com.gf.k3.jsk3.model.dto.GfJsK3DTO;
import com.gf.k3.jsk3.service.IJsK3Service;
import com.gf.k3.jsk3.util.JsK3Util;
import com.gf.pick11.gdpick11.GdPick11Constants;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class JsK3ServiceImpl extends BaseService implements IJsK3Service {
	private IJsK3DAO gfJsK3DAO;
	private IUserService userService;
	private IGaService gaService;
//	public void setJsK3DAO(IJsK3DAO gfJsK3DAO) {
//		gfJsK3DAO = gfJsK3DAO;
//		super.dao = gfJsK3DAO;
//	}

	public void setGfJsK3DAO(IJsK3DAO gfJsK3DAO) {
		this.gfJsK3DAO = gfJsK3DAO;
		super.dao = gfJsK3DAO;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	/**
	 * 初始场次当天和第二天
	 * @param code
	 * @param initDay
	 * @return
	 */
	public String updateInitSession(String initDay){
		//获取彩种初始参数
		String gameType = Constants.GAME_TYPE_GF_JSK3;
		
		HQUtils hq = new HQUtils("from GaSessionInfo gsi where gsi.gameType=?");
		hq.addPars(gameType);
		GaSessionInfo cps = (GaSessionInfo)gfJsK3DAO.getObject(hq);//彩种信息
		if(cps==null){
			return "not found gameType["+gameType+"]";
		}
		
		String code = cps.getGameCode();//彩种代码
		String setStr = cps.getGameSet();//彩种设置 09:00:00/23:00:00/10/84
		
		if(!ParamUtils.chkString(setStr)){
			return "not found config ["+code+"]";
		}
		String[] setArr = setStr.split("\\/");
		if(setArr.length!=4){
			return "error config ["+code+"]";
		}
		String sStr = setArr[0];//开始时间
		String eStr = setArr[1];//结束时间
		Integer interval = Integer.valueOf(setArr[2]);//频次分钟
		Integer maxPart = Integer.valueOf(setArr[3]);//总场次
		
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(now);//仅今天的日期
		if(ParamUtils.chkString(initDay)){//指定始化日期
			today = initDay;
			now = DateTimeUtil.parse(today+" 00:00:00");
		}
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<Object> saveList=null;
		if(!isTodaySessionInit){
			saveList=new ArrayList<Object>();
			
			String startTimeStr = today +" "+ sStr;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
			for (int i = 0; i < maxPart; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i* interval * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				String sessionNo = this.getTodaySessionNo(now, i+1);//期号
				
				this.buildSessionObject(gameType, saveList, sessionNo, curSessionDate, interval);
				
			}
			gfJsK3DAO.updateObjectList(saveList, null);
			flag = "success";
			log.info("___[today["+today+"] init completed]__________________________"+code);
			GameHelpUtil.log("jsk3", "___[today["+today+"] init completed]__________________________"+code);
		} else {
			flag = "inited";
			log.info("___[today["+today+"] has been inited]__________________________"+code);
			GameHelpUtil.log("jsk3", "___[today["+today+"] has been inited]__________________________"+code);
		}
		return flag;
	}
	
	public List<Object> buildSessionObject(String gameType,List<Object>saveList,String sessionNo,Date curSessionDate,Integer interval){
		GfJsK3GaSession session = new GfJsK3GaSession();
		session.setSessionNo(sessionNo);
		session.setStartTime(curSessionDate);
		session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,interval));
		session.setOpenStatus(GdPick11Constants.GD_PICK11_OPEN_STATUS_INIT);
		saveList.add(session);
		return saveList;
	}
	
//	/**
//	 * 初始化场次
//	 */
//	public String updateInitSession(){
////		log.info("___[start]__________________________");
//		
//		String flag = "fail";//返回状态
//		
//		//今天日期处理 yyyy-MM-dd
//		Date now = DateTimeUtil.getJavaUtilDateNow();
//		String today = DateTimeUtil.DateToString(now);
//	
//		//今天是否已经初始化场次
//		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
//		List<GfJsK3GaSession> saveList=null;
//		if(!isTodaySessionInit){
//			saveList=new ArrayList<GfJsK3GaSession>();
////			log.info("___[start today]__________________________");
//			
//			String startTimeStr = today + JsK3Constants.JS_K3_START_TIME_STR;//开始时间串
//			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
//			
////			log.info("___[startTimeStr="+startTimeStr+"]");
//			
//			for (int i = 0; i < JsK3Constants.JS_K3_MAX_PART; i++) {
//				//计算出当前场次的时间
//				long diffTime = startDate.getTime() + i*JsK3Constants.JS_K3_TIME_INTERVAL * 60 * 1000;
//				Date curSessionDate = new Date(diffTime);
//				
//				String sessionNo = this.getTodaySessionNo(now, i+1);//期号
//				log.info("___[start today]__________________________sessionNo:"+sessionNo);
//				GfJsK3GaSession k10Session = new GfJsK3GaSession();
//				k10Session.setSessionNo(sessionNo);
//				k10Session.setStartTime(curSessionDate);
//				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,JsK3Constants.JS_K3_TIME_INTERVAL));
//				k10Session.setOpenStatus(JsK3Constants.JS_K3_OPEN_STATUS_INIT);
//				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
////				JsK3DAO.saveObject(k10Session);
//				saveList.add(k10Session);
//				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
//			}
//			gfJsK3DAO.updateObjectList(saveList, null);
//			flag = "success";
////			log.info("___[today init completed]__________________________");
//		} else {
//			flag = "inited";
////			log.info("___[today has been inited]__________________________");
//		}
//		return flag;
//	}
	
	/**
	 * 检查今天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkTodaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from GfJsK3GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gfJsK3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 检查明天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkTomorrowSessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from GfJsK3GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gfJsK3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	//-----------------------独立业务方法写在这里----------------------------------------------------
	
	/**
	 * 获取今天的期号   看数据是 年月日+当前第几期
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date date,int index){	
		return DateTimeUtil.getYyMMddStr(date) + String.format("%03d", index);
	}
	
	public List<GfJsK3GaSession> updateAndOpenResult(Map<String, SessionItem> sessionNoMap){
		List<GfJsK3GaSession> sessionlist=new ArrayList<GfJsK3GaSession>();
		GfJsK3GaSession currentSession=gfJsK3DAO.getCurrentSession();
		GfJsK3GaSession tempsession =gfJsK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
		if(tempsession==null){
			tempsession=(GfJsK3GaSession) gfJsK3DAO.getObject(GfJsK3GaSession.class, (currentSession.getSessionId()-1));
		}
		final String lastSessionNo=tempsession.getSessionNo();
		
		if(!sessionNoMap.isEmpty()){
			for(String key:sessionNoMap.keySet()){
				GfJsK3GaSession session =gfJsK3DAO.getPreviousSessionBySessionNo(key);
				if(session!=null){
					String openStatus1 = session.getOpenStatus();//开奖状态
					if(openStatus1.equals(JsK3Constants.JS_K3_OPEN_STATUS_INIT) || openStatus1.equals(JsK3Constants.JS_K3_OPEN_STATUS_OPENING)){
						SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
						String openResult = sessionItem.getResult();
						session.setOpenResult(openResult);
						
						session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
						session.setOpenStatus(JsK3Constants.JS_K3_OPEN_STATUS_OPENED);
						session.setSaveTime(new Date());
						
						gfJsK3DAO.saveObject(session);
					}
					sessionlist.add(session);
				}
				
				if(tempsession.getSessionNo().equals(key)){
					GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_JSK3);					
					if(sessionInfo != null){
						SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
						if(lastItem!=null){
							sessionInfo.setOpenResult(lastItem.getResult());
							sessionInfo.setOpenSessionNo(lastSessionNo);
							sessionInfo.setEndTime(DateTimeUtil.StringToDate(lastItem.getTime(),"yyyy-MM-dd HH:mm:ss"));							
						}
						sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
						gfJsK3DAO.updateObject(sessionInfo, null);
					}
				}
			}
		}
        
        return sessionlist;
	}
	
//	@Override
//	public List<GfJsK3GaSession> updateAndOpenResult() {
//		List<GfJsK3GaSession> sessionlist=new ArrayList<GfJsK3GaSession>();
//		GfJsK3GaSession currentSession=gfJsK3DAO.getCurrentSession();
//		GfJsK3GaSession tempsession =gfJsK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(GfJsK3GaSession) gfJsK3DAO.getObject(GfJsK3GaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//		final Map<String,String> sessionNoMap=new HashMap<String,String>();
//		final Map<String,String> timeMap=new HashMap<String,String>();
//		Thread t=new Thread(){
//			public void run() {
//				Integer countRun = 0;
//				out: while (true) {
//					if (countRun == 49) {
//						interrupt();
//						countRun = null;
//						break;
//					}
//					try {
//						countRun = countRun + 1;
//
//						// 从这里
//						// ---------------------------------------------------------------------------
//
//						GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("gfjsk3");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//
//						log.info("___[JsK3 start fetch result xml data...]________________---------JsK3");
//						GameHelpUtil.log("jsk3", "___[JsK3 start fetch result xml data...]________________---------JsK3");
//						// ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new
//						// Date())+"___[JsK3 start fetch result xml data...]________________",
//						// Constants.getWebRootPath()+"/gamelogo/JsK3.txt",
//						// true);
//
//						String resultXML = URLUtil.HttpRequestUTF8(officalURL);
//						// log.info(resultXML);
//						// 到这里
//						// ---------------------------------------------------------------------------
//						sleep(3000);
//						// log.info("___[fetch result xml data]"+resultXML);
//						// ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new
//						// Date())+resultXML,
//						// Constants.getWebRootPath()+"/gamelogo/JsK3.txt",
//						// true);
//
//						if (ParamUtils.chkString(resultXML)) {
//							Document xmlDoc = XmlUtil
//									.getDOMDocumentFromString(resultXML);
//
//							// 开始解析场次开奖数据
//							NodeList nList = xmlDoc.getElementsByTagName("row");
//							String sessionNo = "";// 场次号
//							String result = "";// 开奖结果5组数字
//							String time = "";
//							String isFirstSession = "";
//							for (int i = 0; i < nList.getLength(); i++) {
//								Node node = nList.item(i);
//								sessionNo = XmlUtil.getElementAttribute(
//										(Element) node, "expect");
//								sessionNo = sessionNo.substring(sessionNo
//										.length() - 9);// 2017041225 去掉前两位
//								// sessionNo=sessionNo.substring(0,6)+sessionNo.substring(7,9);
//								result = XmlUtil.getElementAttribute(
//										(Element) node, "opencode");
//								time = XmlUtil.getElementAttribute(
//										(Element) node, "opentime");
//								if (i == 0) {
//									sessionNoMap.put("lastNo", sessionNo);
//								}
//
//								if (sessionNoMap.get(sessionNo) == null) {
//									sessionNoMap.put(sessionNo, result);
//									timeMap.put(sessionNo, time);
//								}
//								if (i == 0) {
//									isFirstSession = sessionNo;
//								}
//								// if(sessionNo.equals(lastSessionNo)){
//								// countRun=null;
//								// interrupt();
//								// }
//							}
//							if (lastSessionNo.equals(isFirstSession)) {
//								interrupt();
//								break out;
//							}
//
//							// interrupt();
//						} else {
//							interrupt();
//							countRun = null;
//							break;
//						}
//					} catch (Exception e) {
//						countRun = null;
//						interrupt();
//						GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:358,"+e.toString());
//					}
//				}
//
//			}
//        };
//        t.start();
//        
//        try {
//			t.join();//该方法是等 t 线程结束以后再执行后面的代码
//			t=null;
//			if(!sessionNoMap.isEmpty()){
//				for(String key:sessionNoMap.keySet()){
//					GfJsK3GaSession session =gfJsK3DAO.getPreviousSessionBySessionNo(key);
//					
//					if(tempsession.getSessionNo().equals(key)){
//						GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_JSK3);					
//						if(sessionInfo != null){
//							sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//							if(sessionNoMap.get(lastSessionNo)!=null){
//								sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));
//								sessionInfo.setOpenSessionNo(lastSessionNo);
//								sessionInfo.setEndTime(DateTimeUtil.StringToDate(timeMap.get(lastSessionNo),"yyyy-MM-dd HH:mm:ss"));							
//							}else{
//								String lastNo = sessionNoMap.get("lastNo");
//								if(ParamUtils.chkString(lastNo)){
//									sessionInfo.setOpenResult(sessionNoMap.get(lastNo));
//									sessionInfo.setOpenSessionNo(lastNo);
//									sessionInfo.setEndTime(DateTimeUtil.StringToDate(timeMap.get(lastNo),"yyyy-MM-dd HH:mm:ss"));							
//								}
//							}
//							gfJsK3DAO.updateObject(sessionInfo, null);
//						}
//					}
//					
//					
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(JsK3Constants.JS_K3_OPEN_STATUS_INIT) || openStatus1.equals(JsK3Constants.JS_K3_OPEN_STATUS_OPENING)){
//							session.setOpenResult(sessionNoMap.get(key));	
////							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//							session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//							session.setOpenStatus(JsK3Constants.JS_K3_OPEN_STATUS_OPENED);
//							session.setSaveTime(new Date());
//							gfJsK3DAO.saveObject(session);
//							sessionlist.add(session);
//							log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//							GameHelpUtil.log("jsk3", "___[open result success sessionNO["+session.getSessionNo()+"]]");
//						}
//					}
//				}
//				sessionNoMap.clear();
//				timeMap.clear();
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:414,"+e.toString());
//		}
//        currentSession=null;
//        
//        return sessionlist;
//	}
	
	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public String updateJsK3SessionOpenResultMethod(GfJsK3GaSession session,String result,String orderNum){
		try{
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<GaBetSponsorDetail> spDeList = new ArrayList<GaBetSponsorDetail>();
			Map<Integer,BigDecimal> userWinMap=new HashMap<Integer,BigDecimal>();//key:userId  value:中奖金额			
			List<Integer> puserIds = new ArrayList<Integer>();

			List<Integer> userIds = new ArrayList<Integer>();
			
			// 把资金明细里投注记录状态值改为有效
			userService.updateUserTradeDetailStatus(session.getSessionNo(), 
					Constants.GAME_TYPE_GF_JSK3, Constants.PUB_STATUS_OPEN);
			
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and sp.sessionId=? ");
			para.add(session.getSessionId());
			hqls.append(" and sp.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and sp.winResult=? " );
			para.add(Constants.INIT);// 未开奖
			hqls.append(" and sp.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_JSK3);
			if(orderNum !=null){
				hqls.append(" and sp.orderNum =? ");
				para.add(orderNum);	
			}
			List<GaBetSponsor> list = gaService.findGaBetSponsorList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单
			String type="";
			if(list!=null&&list.size()>0){
				for(int i = 0; i < list.size(); i++){
					GaBetSponsor sp = list.get(i);
					BigDecimal winCash = new BigDecimal(0); //一个订单中奖金额。
					BigDecimal winPoint = new BigDecimal(0); //一个订单获得总积分
//				    String orderNum = sp.getOrderNum();
				    int multiple = sp.getMultiple();//倍数
				    List<GaBetSponsorDetail>  spde = gaService.findGaBetSponsorDetailListByJointId(sp.getJointId());//合买的具体投注项列表
				    if(spde !=null && spde.size() >0){
						for(int j=0;j<spde.size();j++){
							GaBetSponsorDetail det = (GaBetSponsorDetail) spde.get(j);
							String betBall = det.getOptionTitle();//具体投注内容
							String playType = det.getPlayType().toString();//玩法
							//如果是和值玩法的话，根据开奖的和值单独查询赔率
							BigDecimal betRate = new BigDecimal(0);
							if (JsK3Constants.GAME_TYPE_GF_HEZHI.equals(playType)) {
								String[] results = result.split(",");
								Integer hezhi = Integer.valueOf(results[0])+Integer.valueOf(results[1])
										+Integer.valueOf(results[2]);
								betRate = gfJsK3DAO.getHeZhiBetRate("和值"+hezhi.toString());//
							} else {
								betRate = det.getBetRate();//
							}
							BigDecimal point = det.getPointMultiple();//倍率
							
							BigDecimal winMoney = JsK3Util.judgeWinMoney(playType,betBall,betRate,result).multiply(new BigDecimal(multiple));
							if(winMoney.compareTo(new BigDecimal(0))==1){ 
								det.setWinResult(Constants.WIN);
								winCash = winCash.add(winMoney);
								winPoint = winPoint.add(winMoney.multiply(point));
							}else{
								det.setWinResult(Constants.WIN_NO);
							}
							det.setWinMoney(winMoney);
							spDeList.add(det);
						}
				    }
				    if(winCash.compareTo(new BigDecimal(0))==1){
				    	sp.setWinResult(Constants.WIN);
				    }else{
				    	sp.setWinResult(Constants.WIN_NO);
				    }
				    sp.setWinCash(winCash);
				    sp.setWinPoint(winPoint);
				    sp.setOpenResult(result);
				    sp.setOpenTime(new Date());
//				    sp.setBetFlag(Constants.PUB_STATUS_CLOSE);
				    spList.add(sp);
				    //查询参与购买用户，合买与代购同样适用
					if(winCash.compareTo(new BigDecimal(0))==1){
						List<SpDetailDTO>  part = gaService.findGaBetPartListByJointId(sp.getJointId());
						if(part != null && part.size()>0){
							for(SpDetailDTO dto:part){
								GaBetPart  betPa = dto.getGaBetPart();
								BigDecimal perWincash = null;
								BigDecimal perWinPoint = new BigDecimal(0);
								perWincash = new BigDecimal(betPa.getBuyNum()).divide(new BigDecimal(sp.getNum()),6, BigDecimal.ROUND_HALF_EVEN).multiply(winCash);//每一个人分得金额

								if(winPoint.compareTo(new BigDecimal(0))==1){
								    perWinPoint = new BigDecimal(betPa.getBuyNum()).divide(new BigDecimal(sp.getNum()),6, BigDecimal.ROUND_HALF_EVEN).multiply(winPoint).setScale(0, BigDecimal.ROUND_HALF_EVEN);//每一个人分得积分
								}
								betPa.setWinCash(perWincash);
								betPa.setWinPoint(perWinPoint);
								paList.add(betPa);
								
								Integer uid=betPa.getUserId();
								//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
								if(!StringUtil.chkListIntContains(userIds, uid)){
									userIds.add(uid);
								}
								if(!StringUtil.chkListIntContains(puserIds, uid)){
									puserIds.add(uid);
								}
								
								if(userWinMap.get(uid)!=null){
									userWinMap.put(uid, userWinMap.get(uid).add(perWincash));
								}else{
									userWinMap.put(uid,perWincash);
								}
								
								//用户的交易明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单:").append(sp.getOrderNum()).append(";系统结算，彩派  ")
							    .append(perWincash).append("元");						
								this.updateOpenData(betPa,remark.toString(),sp.getSessionNo());
								//用户的积分明细
								if(winPoint.compareTo(new BigDecimal(0))==1){
									StringBuilder pointRemark = new StringBuilder();
									pointRemark.append("订单").append(sp.getOrderNum()).append(",系统结算:").append(perWinPoint.toString());
									this.updateOpenDataPoints(betPa,remark.toString());
								}
								
							}
						}
						//如果是代购且追号且中奖了且中奖就停止追号
						if(Constants.PROCUREMENT_SERVICE.equals(sp.getBetType())&&Constants.ADD_NO.equals(sp.getIsAddNo())
								&&Constants.WIN_STOP.equals(sp.getIsWinStop())){
							List<Object> para2 = new ArrayList<Object>();
							StringBuffer hqls2 = new StringBuffer();
							hqls2.append(" and ho.batchNum=? ");
							para2.add(sp.getBatchNum());
							hqls2.append(" and ho.winResult=? ");
							para2.add(Constants.INIT);
							hqls2.append(" and ho.betFlag=? ");
							para2.add(Constants.PUB_STATUS_OPEN);
							hqls2.append(" and ho.gameType =? ");
							para2.add(Constants.GAME_TYPE_GF_JSK3);
							hqls2.append(" and ho.sessionId >? ");
							para2.add(sp.getSessionId());
							hqls2.append(" and ho.betType = ? ");
							para2.add(Constants.PROCUREMENT_SERVICE);
							hqls2.append(" order by ho.sessionId asc");
							
							List<SpDetailDTO>  stopList= gaService.findGaBetSponsorAndUserList(hqls2.toString(),para2);//发起购买表，查询需要修改订单状态并退款的订单。
				    		if(stopList!= null && stopList.size()>0){
				    			type="1";
				    			for(SpDetailDTO dto:stopList){
				    				GaBetSponsor betSp =(GaBetSponsor) dto.getGaBetSponsor();
				    				betSp.setWinResult(Constants.INVALID_REFUND);//无效退款
				    			    betSp.setBetFlag(Constants.PUB_STATUS_CLOSE); //无效
				    			    spList.add(betSp);
				    				
				    				BigDecimal refund = betSp.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP);//退款金额
				    				
									if(!StringUtil.chkListIntContains(userIds, betSp.getUserId())){
										userIds.add(betSp.getUserId());
									}

									StringBuilder remark = new StringBuilder();
									remark.append("追号已中奖，订单").append(betSp.getOrderNum()).append("取消,退款金额:")
									    .append(refund.toString()).append("元");
									
									this.updateOpenDataRefund(betSp,remark.toString());
				    			}
				    		}
						}
					}
				}		
			}
			
			if(ParamUtils.chkString(type)){
				userService.updateUserMoney(userIds,type);
			}else{
				userService.updateUserMoney(userIds);
			}
			userService.updateUserPoints(puserIds);
			if(userWinMap.size()>0){
				updateWinCount(userWinMap);
			}
			gfJsK3DAO.updateObjectList(spList, null);
			gfJsK3DAO.updateObjectList(spDeList, null);
			gfJsK3DAO.updateObjectList(paList, null);

			return "";
		}catch(Exception e){
			e.printStackTrace();
			GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:670,"+e.toString());
			return session.getSessionNo();
		}
		
	}
	
	public  void updateWinCount(Map<Integer,BigDecimal> moneyMap){
		List<GaWinCount> gaWinCoList = new ArrayList<GaWinCount>();
		for(Integer key:moneyMap.keySet()){
			StringBuffer hql = new StringBuffer();
			List<Object> parame = new ArrayList<Object>();
			hql.append(" and ho.userId = ? ");
			parame.add(key);
			hql.append(" and ho.gameType = ? ");
			parame.add(Constants.GAME_TYPE_GF_JSK3);
			List<GaWinCount> gaWinCountList = gaService.findGaWinCountList(hql.toString(),parame);
			GaWinCount gaWinCount = null;
			if(gaWinCountList.size()== 0){
				gaWinCount = new GaWinCount();
				gaWinCount.setUserId(key);
				gaWinCount.setGameType(Constants.GAME_TYPE_GF_JSK3);
				gaWinCount.setTotalMoney(moneyMap.get(key));
				gaWinCoList.add(gaWinCount);
			}else{
				gaWinCount = gaWinCountList.get(0);
				BigDecimal totalMoeny=ProductUtil.checkBigDecimal(gaWinCount.getTotalMoney());//用户余额
				gaWinCount.setTotalMoney(totalMoeny.add(moneyMap.get(key)));
				gaWinCoList.add(gaWinCount);
			}	
		}
		gfJsK3DAO.updateObjectList(gaWinCoList, null);
	}
	
	/**
	 * 保存用户余额
	 * @param userId
	 * @param string
	 */
	private void updateOpenData(GaBetPart part,String remark,String sessionNo) {
		User user = (User)gfJsK3DAO.getObject(User.class, part.getUserId());
		userService.saveTradeDetail(user,part.getUserId(), Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, part.getWinCash(), part.getRid(), 
				Constants.GAME_TYPE_GF_JSK3,remark,
				sessionNo,user.getUserType(),user.getLoginName()
				);
	}
	
	/**
	 * 保存用户余额
	 * @param userId
	 * @param string
	 */
	private void updateOpenDataRefund(GaBetSponsor betSp,String remark) {
		userService.saveTradeDetail(null,betSp.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAWBACK, betSp.getMoney(), betSp.getJointId(), Constants.GAME_TYPE_GF_JSK3,remark);
	}
	
	/**
	 * 保存用户积分明细
	 * @param userId
	 * @param string
	 */
	private void updateOpenDataPoints(GaBetPart part,String remark) {
		userService.savePointDetail(part.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, part.getWinPoint(), part.getRid(),remark);
	}
	
	public GfJsK3GaSession getCurrentSession(){
		return gfJsK3DAO.getCurrentSession();
	}
	public GfJsK3GaSession getPreviousSessionBySessionNo(String sessionNo){
		return gfJsK3DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	public void saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GfJsK3GaSession gaPick11Session,User user,BigDecimal betAll){
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//		BigDecimal paperMoney=user.getUserScore();
//		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
//		BigDecimal tempMoney=new BigDecimal(0);
//		for (int i = 0; i < list.size(); i++) {
//				GaBetOption betOption = list.get(i);
//				GaBetDetail betDetail=new GaBetDetail();
//				if(betOption!=null){
//					betDetail.setBetRate(betOption.getBetRate());
//				}
//				betDetail.setUserId(user.getUserId());
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(gaPick11Session.getSessionId());
//				
//				betDetail.setBetOptionId(betOption.getBetOptionId());
//				betDetail.setBetTime(new Date());
//				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
//				if(paperMoney.compareTo(new BigDecimal(0))==1){
//					if(new BigDecimal(betMap.get(betOption.getBetOptionId())).compareTo(paperMoney)!=-1){
//						betDetail.setPaperMoney(paperMoney);
//						paperMoney=paperMoney.subtract(paperMoney);
//						tempMoney=tempMoney.add(new BigDecimal(betMap.get(betOption.getBetOptionId())));
//					}else{
//						betDetail.setPaperMoney(new BigDecimal(betMap.get(betOption.getBetOptionId())));
//						paperMoney=paperMoney.subtract(new BigDecimal(betMap.get(betOption.getBetOptionId())));
//						tempMoney=tempMoney.add(new BigDecimal(betMap.get(betOption.getBetOptionId())));
//					}
//				}
//				
////				betDetail.setPaperMoney(paperMoney);
//				betDetail.setRoom(room);
//				betDetail.setSessionNo(gaPick11Session.getSessionNo());
//				betDetail.setGameName("广东11选5");
//				if(betOption.getPlayType().equals("0")){//两面盘
//					betDetail.setPlayName("两面盘");
//				}else if(betOption.getPlayType().equals("1")){//1-5球
//					betDetail.setPlayName("1-5球");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_GF_JSK3);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//		}
//		
//		//更新收益
//		BigDecimal userBalance=user.getUserBalance();
//		if(userBalance==null){
//			userBalance=new BigDecimal(0);
//		}
//		user.setUserBalance(userBalance.subtract(betAll));
//		//更新收益
//		
//		if(paperMoney.compareTo(betAll)==1||paperMoney.compareTo(betAll)==0){//红包足够投注，不用使用余额里面的钱
//			user.setUserScore(user.getUserScore().subtract(tempMoney));
//			gfJsK3DAO.updateObject(user, null);
//		}else{
//			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.GAME_TYPE_GF_JSK3, betAll.subtract(tempMoney), null);
//		}
////		userService.savePointDetail(user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPPick11, betAll.intValue(), null);
//		
//		gfJsK3DAO.updateObjectList(betDetailList, null);
	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "总和";
			 }else{
				 return "第"+optionType+"球";
			 }
		 }else{//第1 -5 球
			return "第"+playType+"球";
		}
	 }
	 
	public List<GfJsK3GaTrend> findGfJsK3GaTrendList(){
		return gfJsK3DAO.findGfJsK3GaTrendList();
	}
	public PaginationSupport  findGfJsK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		return gfJsK3DAO.findGfJsK3GaSessionList(hql,para,pageNum,pageSize);
	}
	public PaginationSupport  findGfJsK3GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		return gfJsK3DAO.findGfJsK3GaBetList(hql,para,pageNum,pageSize);
	}
	
	/**
	 * 判断用户是否中奖   返回结果  0=未中奖   1=和    2=中奖
	 * @param results
	 * @param detail
	 * @return
	 */
	public String  judgeWin(String results,GaBetDetail detail){
		String array[]=results.split(",");//拆分结果
		if(detail.getPlayName().equals("两面盘")){//先用中文比对吧  后续改进
			if(detail.getBetName().equals("总和")){
				Map<String,Boolean>  map=getSumResult(array);
				if(detail.getOptionTitle().equals("和大")||detail.getOptionTitle().equals("和小")){ //?
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						if(map.get("和")!=null&&map.get("和")){
							return "1";
						}else{
							return "0";
						}
					}
				}else if(detail.getOptionTitle().equals("龙")||detail.getOptionTitle().equals("虎")){
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						if(map.get("龙虎和")!=null&&map.get("龙虎和")){
							return "1";
						}else{
							return "0";
						}
					}
				}else{
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						return "0";
					}
				}
			}else{ // 第1球，第2球...第5球
				int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
				Map<String,Boolean>  map=getSingleBallResult(array[seq-1]);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					if(map.get("和")!=null&&map.get("和")==true){
						return "1";
					}else{
						return "0";
					}	
				}
			}
		}else{//1-5球
			int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
			int value=Integer.parseInt(array[seq-1]);//第几球的值
			//下注的具体是几号
			int index=Integer.parseInt(detail.getOptionTitle());
			if(value==index){
				return "2";
			}else{
				return "0";
			}
		}	
	}
	/**
	 *  两面盘 根据传递的数字判断是否中奖  
	 * secondNum可传0，主要是判断1-5龙虎用的这个
	 */
	public Map<String,Boolean> getResult(int firstNum,int secondNum){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		if(firstNum>=6){
			map.put("大", true);
		}else{
			map.put("小", true);
		}
		if(firstNum%2==0){
			map.put("双", true);
		}else{
			map.put("单", true);
		}
		if(secondNum>0){
			if(firstNum>secondNum){
				map.put("龙", true);
			}else{
				map.put("虎", true);
			}
		}
		return map;
	}
	
	
	/**
	 *  总和
	 */
	public Map<String,Boolean> getSumResult(String[]  array){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=0;
		for(int i=0;i<array.length;i++){
			sum=sum+Integer.parseInt(array[i]);
		}
		if(sum%2==0){
			map.put("和双", true);
		}else{
			map.put("和单", true);
		}
		if(sum>30){
			map.put("和大", true);
		}else if(sum<30){
			map.put("和小", true);
		}else if(sum==30){
			map.put("和", true);
		}

		int val=sum%10;
		if(val>=5){
			map.put("尾大", true);
		}else{
			map.put("尾小", true);
		}

		if(Integer.parseInt(array[0])>Integer.parseInt(array[4])){
			map.put("龙", true);
		}else if(Integer.parseInt(array[0])<Integer.parseInt(array[4])){
			map.put("虎", true);
		}
		return map;
	}
	/**
	 *  两面盘 第1-5球的中奖结果
	 * 
	 */
	public Map<String,Boolean> getSingleBallResult(String value){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=Integer.parseInt(value);
		if(sum == 11){
			map.put("和", true);
		}else{
			if(sum%2==0){
				map.put("双", true);
			}else{
				map.put("单", true);
			}
			if(sum>=6){
				map.put("大", true);
			}else if(sum<=5){
				map.put("小", true);
			}
		}
		return map;
	}

	@Override
	public void updateGaTrend() {
		GfJsK3GaSession GfJsK3GaSession = gfJsK3DAO.getCurrentSession(); // 当前期数信息
		if(GfJsK3GaSession != null){
			String curNo = GfJsK3GaSession.getSessionNo(); // 当前期号
			String preNo = String.valueOf((Integer.parseInt(curNo) - 1)); //上期期号
			GfJsK3GaSession JsK3PreSession = getPreviousSessionBySessionNo(preNo);
			if(JsK3PreSession==null){
				JsK3PreSession=(GfJsK3GaSession) gfJsK3DAO.getObject(GfJsK3GaSession.class, GfJsK3GaSession.getSessionId()-1);
			}
			Map<String,Boolean> resultMap; // 开奖结果
			
			HQUtils hq = new HQUtils(" from GfJsK3GaTrend gdk10 ");
			List<Object> trendList = gfJsK3DAO.findObjects(hq); //上期冷热排行榜
			List<GfJsK3GaTrend> saveList = new ArrayList<GfJsK3GaTrend>(); // 本期冷热排行榜

			String results = JsK3PreSession.getOpenResult();
			
			if(results != null && results.length() > 0){
				resultMap = this.transResult(results);
			}else{
				return;
			}
			if(trendList != null && trendList.size() > 0 ){
				for(int i = 0; i < trendList.size(); i ++){
					GfJsK3GaTrend trend = (GfJsK3GaTrend) trendList.get(i);
					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
						trend.setTrendCount(trend.getTrendCount() + 1);
					}else{
						trend.setTrendCount(0);
					}
					saveList.add(trend);
				}
			}
			gfJsK3DAO.updateObjectList(saveList, null);
		}else{
			log.info("获取当前期数信息失败！");
		}
		GfJsK3GaSession=null;
	}
	
	/**
	 * 把开奖结果转换为冷热排行榜对应值。
	 * @return 
	 */
	public Map<String, Boolean> transResult(String results){
		String array[] = results.split(",");
		int[] arr; // 存储每一位开奖结果
		int sum = 0;
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 冷热排行榜各种值

		boolean isSanHao = true;
		for(int i = 0; i < array.length; i++){
			if (!"1".equals(array[i])) {
				isSanHao = false;
			}
        }
		
		if (isSanHao) {
			resultMap.put("111", true);
		}
		
//        arr = new int[array.length];
//        for(int i = 0; i < array.length; i++){
//        	arr[i] = Integer.parseInt(array[i]);
//        	sum = sum + arr[i];
//        }
//        for(int i = 0; i< arr.length; i++){
//        	if(arr[i] == 11){
//        		resultMap.put("第" + (i+1) + "球 和", true);
//        	}else{
//            	if(arr[i] <= 5){
//            		resultMap.put("第" + (i+1) + "球 小", true);
//            	}else{
//            		resultMap.put("第" + (i+1) + "球 大", true);
//            	}
//            	if(arr[i] %2 == 0){
//            		resultMap.put("第" + (i+1) + "球 双", true);
//            	}else{
//            		resultMap.put("第" + (i+1) + "球 单", true);
//            	}
//        	}
//        }
//		if(sum%2==0){
//			resultMap.put("总和 双", true);
//		}else{
//			resultMap.put("总和 单", true);
//		}
//		if(sum>30){
//			resultMap.put("总和 大", true);
//		}else if(sum<30){
//			resultMap.put("总和 小", true);
//		}else if(sum==30){
//			resultMap.put("总和 和", true);
//		}
//
//		int val=sum%10;
//		if(val>=5){
//			resultMap.put("总和 尾大", true);
//		}else{
//			resultMap.put("总和 尾小", true);
//		}
		return resultMap;
		
	}

	@Override
//	public String updateTomorrowSession() {
////		log.info("___[start]__________________________");
//		
//		String flag = "fail";//返回状态
//		
//		//明天日期处理 yyyy-MM-dd
//		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd"); //年月日时间格式
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, 1); // 获取明天时间
//
//		String today = simpleDateFormat.format(c.getTime());
//	
//		//今天是否已经初始化场次
//		boolean isTodaySessionInit = this.checkTomorrowSessionInit(c.getTime());
//		List<GfJsK3GaSession> saveList=null;
//		if(!isTodaySessionInit){
////			log.info("___[start today]__________________________");
//			saveList=new ArrayList<GfJsK3GaSession>();
//			String startTimeStr = today + JsK3Constants.JS_K3_START_TIME_STR;//开始时间串
//			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
//			
////			log.info("___[startTimeStr="+startTimeStr+"]");
//			
//			for (int i = 0; i < JsK3Constants.JS_K3_MAX_PART; i++) {
//				//计算出当前场次的时间
//				long diffTime = startDate.getTime() + i*JsK3Constants.JS_K3_TIME_INTERVAL * 60 * 1000;
//				Date curSessionDate = new Date(diffTime);
//				
//				String sessionNo = this.getTodaySessionNo(c.getTime(), i+1);//期号
//				log.info("___[start today]__________________________sessionNo:"+sessionNo);
//				GfJsK3GaSession k10Session = new GfJsK3GaSession();
//				k10Session.setSessionNo(sessionNo);
//				k10Session.setStartTime(curSessionDate);
//				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,JsK3Constants.JS_K3_TIME_INTERVAL));
//				k10Session.setOpenStatus(JsK3Constants.JS_K3_OPEN_STATUS_INIT);
////				JsK3DAO.saveObject(k10Session);
//				saveList.add(k10Session);
//				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
//			}
//			gfJsK3DAO.updateObjectList(saveList, null);
//			flag = "success";
////			log.info("___[today init completed]__________________________");
//		} else {
//			flag = "inited";
////			log.info("___[today has been inited]__________________________");
//		}
//		return flag;
//	}
	
	public boolean saveOpenResult(GfJsK3GaSession session,String openResult){

		boolean flag=false;
		session.setOpenResult(openResult);
		gfJsK3DAO.updateObject(session, null);
		flag=true;
		return flag;
	}
	
	public boolean saveAndOpenResult(GfJsK3GaSession session,String openResult){
		boolean flag=false;
		session.setOpenResult(openResult);		
		String flag1 = updateJsK3SessionOpenResultMethod(session, session.getOpenResult(),null);
		if(!ParamUtils.chkString(flag1)){
			session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
			session.setOpenStatus(JsK3Constants.JS_K3_OPEN_STATUS_OPENED);
			gfJsK3DAO.updateObject(session, null);
			log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
			GameHelpUtil.log("jsk3", "___[open result success sessionNO["+session.getSessionNo()+"]]");
			flag=true;
		}else{
			log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			GameHelpUtil.log("jsk3", "___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
		}
		return flag;
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return gfJsK3DAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<GfJsK3DTO> findGaBetDetailById(String hql, List<Object> para) {
		return gfJsK3DAO.findGaBetDetailById(hql,para);
	}
	public String getCountResult(String openResult){
		String countResult="";
		if(ParamUtils.chkString(openResult)){
			String array[]=openResult.split(",");
			int sum=0;				
			for(int i=0;i<array.length;i++){
				sum=sum+Integer.parseInt(array[i]);
				countResult=countResult+Integer.parseInt(array[i])+",";
			}
			countResult=countResult+sum;
			return countResult;
		}
		return "";
	}
	
	/**
	 * 发起合买
	 */
	public User saveSponsorBet(User user, List<String> list,
			Map<String, Integer> seMap, int num, int buyNum,String isGuaranteed,int guNum,BigDecimal betmoney,BigDecimal needMoney,int betNum,GfJsK3GaSession currentSession,BigDecimal buyMoney) {
		List<Object> saveList = new ArrayList<Object>();
		String batchNum =""; //批次号，是否属于同一追号
		batchNum = System.currentTimeMillis() +DcbUtil.getTwo();
		List<GaBetOption>  opList= gaService.findGaBetOptionByGameType(Constants.GAME_TYPE_GF_JSK3);
		for (String key : seMap.keySet()) {
			String sessionNo = key; //期号
			int multiple = seMap.get(key); //倍数
//			GfJsK3GaSession se = this.getPreviousSessionBySessionNo(sessionNo);//获取期号信息
//			int betNum = FcUtil.getTotalBetNum(list);
			String orderNum = "";
//			BigDecimal money = new BigDecimal(betNum *2 /num*buyNum); //购买金额
			GaBetSponsor sp = new GaBetSponsor();
			sp.setUserId(user.getUserId());
			sp.setSessionId(currentSession.getSessionId());
			sp.setBetTime(new Date());
			sp.setMoney(needMoney);
			sp.setSchedule(new BigDecimal(buyNum).divide(new BigDecimal(num),2,BigDecimal.ROUND_HALF_EVEN));
			sp.setNum(num);
			sp.setBetNum(betNum);
			sp.setRestNum(num-buyNum);
			if(num-buyNum ==0){
				sp.setWinResult(Constants.INIT); //未开奖
			}else{
				sp.setWinResult(Constants.UNFINISHED); //未完成
			}
			sp.setIsGuarantee(isGuaranteed);//是否保底
			if(Constants.GUARANTEE.equals(isGuaranteed)){//如果保底
				sp.setGuaranteedMoney(betmoney.divide(new BigDecimal(num),2,BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(guNum)));
				sp.setGuaranteedNum(guNum);
			}

			sp.setBetFlag(Constants.PUB_STATUS_OPEN); //有效
			sp.setGameType(Constants.GAME_TYPE_GF_JSK3);
			sp.setGameName(Constants.getGameName(Constants.GAME_TYPE_GF_JSK3));
			sp.setSessionNo(sessionNo);
			sp.setMultiple(multiple);
			sp.setBetType(FiveConstants.SPONSOR);//合买
			sp.setPreMoney(betmoney.divide(new BigDecimal(num),2,BigDecimal.ROUND_HALF_EVEN));
//			sp.setIsAddNo(FcConstants.NOT_ADD_NO);
//			sp.setIsWinStop(FcConstants.WIN_NO_STOP);
			sp.setBatchNum(batchNum);
			sp.setWinCash(new BigDecimal(0));
			sp = (GaBetSponsor) gfJsK3DAO.saveObjectDB(sp);
			orderNum = sessionNo + Constants.getGameOrderName(Constants.GAME_TYPE_GF_JSK3) +sp.getJointId();
			sp.setOrderNum(orderNum);
		    saveList.add(sp);
			
			GaBetPart pa =new GaBetPart();
			pa.setJointId(sp.getJointId());
			pa.setBetMoney(buyMoney);
			pa.setBuyNum(buyNum);
			pa.setUserId(user.getUserId());
			pa.setBuyTime(new Date());
			pa.setWinCash(new BigDecimal(0));
			pa.setWinPoint(new BigDecimal(0));
			pa.setBehavior(Constants.ORIGINATE);
			saveList.add(pa);
			pa.setWinCash(new BigDecimal(0));
			String remark="订单："+orderNum+"，购买彩票 扣款"+buyMoney+" 元";
			user=userService.saveTradeDetail(user, user.getUserId(),  Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, buyMoney, sp.getJointId(),Constants.GAME_TYPE_GF_JSK3,remark);
			for(String op : list){
				int playType = Integer.parseInt(op.split("\\+")[0]); //玩法
				String betBall = op.split("\\+")[1]; //投注的球
				BigDecimal betRate = null;
				String title = null; //玩法对应的中文
				BigDecimal point = null;//积分倍数

				for(int i=0;i<opList.size();i++){
					GaBetOption betOp = (GaBetOption) opList.get(i);
					if(playType == Integer.parseInt(betOp.getPlayType())){
						betRate = betOp.getBetRate();
						title= betOp.getTitle();
						point = betOp.getPointMultiple();
					}
				}
				GaBetSponsorDetail de = new GaBetSponsorDetail();
				de.setBetRate(betRate);
				de.setJointId(sp.getJointId());
				de.setOptionTitle(betBall);
				de.setOrderNum(orderNum);
				de.setPlayType(playType);
				de.setWinResult(FiveConstants.FC_INIT);
				de.setTitle(title);
				de.setPointMultiple(point);
				saveList.add(de);
			}

//			BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
//			BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
//			user.setDayBetMoney(dayBetMoney.add(buyMoney));
//			user.setAggregateBetMoney(aggregateBetMoney.add(buyMoney));
			
			remark="订单："+orderNum+"，保底预扣款 "+betmoney.subtract(buyMoney).toString()+" 元";
			user=userService.saveTradeDetail(user, user.getUserId(),  Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_FROZEN, betmoney.subtract(buyMoney), sp.getJointId(),Constants.GAME_TYPE_GF_JSK3,remark);
			userService.updateUserMoneyAndBetMoney(user.getUserId());
			userService.updateUserBanlance(user.getUserId());
		}
		gfJsK3DAO.updateObjectList(saveList, null);
//		userService.saveTradeDetail(user, user.getUserId(),  Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_JsK3, betmoney, regId);
		
		return user;
	}

	/**
	 * 代购 自己购买
	 */
	public User saveProcurementServiceBet(User user,List<String> list, Map<String, Integer> seMap,String isAddNo, String isWinStop,BigDecimal betmoney,int betNum,GfJsK3GaSession currentSession) {
		List<Object> saveList = new ArrayList<Object>();
		String batchNum =""; //批次号，是否属于同一追号
		batchNum = System.currentTimeMillis() +DcbUtil.getTwo();
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		
		List<GaBetOption>  opList= gaService.findGaBetOptionByGameType(Constants.GAME_TYPE_GF_JSK3);
		String sessionNo = "";
		for (String key : seMap.keySet()) {
			sessionNo = key; //期号
			int multiple = seMap.get(key); //倍数
			GfJsK3GaSession se = this.getPreviousSessionBySessionNo(sessionNo);//获取期号信息
//			int betNum = DcbUtil.getTotalBetNum(list);
			String orderNum = "";
			BigDecimal money = new BigDecimal(betNum*1*multiple);//投注金额
//			HQUtils hql = new HQUtils("from GaBetSponsor sp where 1 = 1 ");
//			hql.addHsql(" and op.gameType =? ");
//			hql.addPars(Constants.GAME_TYPE_GF_DCB);
//			hql.addHsql(" and op.gameType =? ");
//			hql.addPars(Constants.GAME_TYPE_GF_DCB);
//			List<Object>  list= JsK3DAO.findObjects(hql);

			
			GaBetSponsor sp = new GaBetSponsor();
			sp.setUserId(user.getUserId());
			sp.setSessionId(se.getSessionId());
			sp.setBetTime(new Date());
			sp.setMoney(money);
			sp.setSchedule(new BigDecimal(1));
			sp.setNum(1);
			sp.setBetNum(betNum);
			sp.setRestNum(0);
//			sp.setGuaranteedNum(new BigDecimal(0)); //保底份额
			sp.setWinResult(Constants.INIT); //未开奖
//			sp.setIsGuarantee(DcbConstants.NOT_GUARANTEE);//不保底
			sp.setBetFlag(Constants.PUB_STATUS_OPEN); //有效
			sp.setGameType(Constants.GAME_TYPE_GF_JSK3);
			sp.setGameName(Constants.getGameName(Constants.GAME_TYPE_GF_JSK3));
			sp.setSessionNo(sessionNo);
			sp.setMultiple(multiple);
			sp.setBetType(DcbConstants.PROCUREMENT_SERVICE);//代购也就是自买
			sp.setIsAddNo(isAddNo);
			sp.setIsWinStop(isWinStop);
			sp.setBatchNum(batchNum);
			sp.setWinCash(new BigDecimal(0));
			sp.setPreMoney(money);
			sp = (GaBetSponsor) gfJsK3DAO.saveObjectDB(sp);
			orderNum = sessionNo + Constants.getGameOrderName(Constants.GAME_TYPE_GF_JSK3) +sp.getJointId();
			sp.setOrderNum(orderNum);
		    saveList.add(sp);
		    
		  //关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(sp.getJointId());
			rlList.add(rl);
			
			GaBetPart pa =new GaBetPart();
			pa.setJointId(sp.getJointId());
			pa.setBetMoney(money);
			pa.setBuyNum(1);
			pa.setUserId(user.getUserId());
			pa.setBuyTime(new Date());
			pa.setWinCash(new BigDecimal(0));
			pa.setWinPoint(new BigDecimal(0));
			pa.setBehavior(Constants.ORIGINATE);
			saveList.add(pa);
			
//			String[] options = bet.split(";");
			for(String op : list){
				int playType = Integer.parseInt(op.split("\\+")[0]);
				String opTitle = op.split("\\+")[1];
				BigDecimal betRate = null;
				BigDecimal point=null;
				String title="";
				for(int i=0;i<opList.size();i++){
					GaBetOption betOp = (GaBetOption) opList.get(i);
					if(playType == Integer.parseInt(betOp.getPlayType())){
						betRate = betOp.getBetRate();
						title= betOp.getTitle();
						point=betOp.getPointMultiple();
					}
				}
				GaBetSponsorDetail de = new GaBetSponsorDetail();
				de.setBetRate(betRate);
				de.setJointId(sp.getJointId());
				de.setOptionTitle(opTitle);
				de.setOrderNum(orderNum);
				de.setPlayType(playType);
				de.setTitle(title);
				de.setWinResult(Constants.INIT);
				de.setPointMultiple(point);
				
				int betCount = JsK3Util.getTotalBetNum(playType+"",opTitle);//投注注数
				Integer betMoney = betCount*1*multiple;//每注的金额
				de.setBetMoney(GameHelpUtil.round(new BigDecimal(betMoney)));

				saveList.add(de);
			}
		}
		
		BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
		BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
		user.setDayBetMoney(dayBetMoney.add(betmoney));
		user.setAggregateBetMoney(aggregateBetMoney.add(betmoney));
		
		String remark="订单批次号："+batchNum+"，彩票预购 扣款 "+betmoney+" 元";
		gfJsK3DAO.updateObjectList(saveList, null);
		
		//user=userService.saveTradeDetail(user, user.getUserId(),  Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, betmoney, null,Constants.GAME_TYPE_GF_AHK3,remark);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(),
				Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO,betmoney, null,
				Constants.GAME_TYPE_GF_JSK3,remark, sessionNo, user.getUserType(),user.getLoginName());
		
		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoneyAndBetMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_GF);
		}
		gfJsK3DAO.updateObjectList(rlList, null);
		
		return user;
	}
	
	
	
//	/**
//	 * 开奖前更新合买记录表，清算用户保底钱数，结算最终投注的钱数
//	 */
//	public void  updateGaBetSponsor(){
//		List<User> userList=new ArrayList<User>();
//		List<UserTradeDetail> tradeList=new ArrayList<UserTradeDetail>();
//		List<GaBetSponsor> gaBetSponsorList=new ArrayList<GaBetSponsor>();
//		List<GaBetPart> partlist=new ArrayList<GaBetPart>();
//		List<GfDcbDTO>  gbslist=gaService.findGaBetSponsorListByGameTypeAndBetType(Constants.GAME_TYPE_GF_JsK3,Constants.PUB_STATUS_OPEN);//合买
//		if(gbslist!=null&&gbslist.size()>0){
//			for(GfDcbDTO dto:gbslist){
//				GaBetSponsor gaBetSponsor=dto.getGaBetSponsor();
//				User user=dto.getUser();
//				if(gaBetSponsor.getRestNum()==0||gaBetSponsor.getRestNum()>gaBetSponsor.getGuaranteedNum()){//剩余份数等于0或者保底份额小于剩余份额，全部退还用户保底份额钱
//					BigDecimal baodi=gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getGuaranteedNum())).setScale(2, BigDecimal.ROUND_UP);
//					user.setMoney(user.getMoney().add(baodi));
//					userList.add(user);
//					UserTradeDetail udt=new UserTradeDetail();
//					udt.setCashMoney(baodi);
//					udt.setUserMoney(user.getMoney());
//					udt.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回保底款项份数："+gaBetSponsor.getGuaranteedNum()+"，金额："+baodi.toString()+"元");
//					udt.setCashType(Constants.CASH_TYPE_CASH_GUA_BACK);
//					udt.setTradeType(Constants.TRADE_TYPE_INCOME);
//					udt.setUserId(user.getUserId());
//					udt.setCreateTime(new Date());
//					udt.setModelType(Constants.GAME_TYPE_GF_JsK3);
//					udt.setRefId(gaBetSponsor.getJointId());
//					tradeList.add(udt);
//					if(gaBetSponsor.getRestNum()>gaBetSponsor.getGuaranteedNum()){
//						gaBetSponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);//无效
//						gaBetSponsorList.add(gaBetSponsor);
//						List<GfDcbDTO> list=gaService.findGaBetPartListByJointId(gaBetSponsor.getJointId());
//						if(list!=null&&list.size()>0){
//							for(GfDcbDTO dto1:list){
//								GaBetPart  part=dto1.getGaBetPart();
//								User user1=dto1.getUser();
//								user1.setMoney(user1.getMoney().add(part.getBetMoney()));
//								userList.add(user1);
//								UserTradeDetail udt1=new UserTradeDetail();
//								udt1.setCashMoney(part.getBetMoney());
//								udt1.setUserMoney(user1.getMoney());
//								udt1.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回参与份数："+part.getBuyNum()+"，金额："+part.getBetMoney().setScale(2, BigDecimal.ROUND_UP).toString()+"元");
//								udt1.setCashType(Constants.CASH_TYPE_CASH_DRAW);
//								udt1.setTradeType(Constants.TRADE_TYPE_INCOME);
//								udt1.setUserId(user1.getUserId());
//								udt1.setCreateTime(new Date());
//								udt1.setModelType(Constants.GAME_TYPE_GF_JsK3);
//								udt1.setRefId(gaBetSponsor.getJointId());
//								tradeList.add(udt1);
//							}
//						}
//					}
//				}else{//保底份额超出剩余份额   退回发起者一部分钱，等于则不退回
//					if(gaBetSponsor.getRestNum()<gaBetSponsor.getGuaranteedNum()){
//						BigDecimal tuihuan=gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getGuaranteedNum()-gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP);
//						user.setMoney(user.getMoney().add(tuihuan));
//						userList.add(user);
//						UserTradeDetail udt=new UserTradeDetail();
//						udt.setCashMoney(tuihuan);
//						udt.setUserMoney(user.getMoney());
//						udt.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回保底款项份数："+gaBetSponsor.getGuaranteedNum()+"，金额："+tuihuan.toString()+"元");
//						udt.setCashType(Constants.CASH_TYPE_CASH_GUA_BACK);
//						udt.setTradeType(Constants.TRADE_TYPE_INCOME);
//						udt.setUserId(user.getUserId());
//						udt.setCreateTime(new Date());
//						udt.setModelType(Constants.GAME_TYPE_GF_JsK3);
//						udt.setRefId(gaBetSponsor.getJointId());
//						tradeList.add(udt);
//						
//						GaBetPart  temp=new GaBetPart();
//						temp.setBuyNum(gaBetSponsor.getRestNum());
//						temp.setBetMoney(gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP));
//						temp.setUserId(user.getUserId());
//						temp.setBuyTime(new Date());
//						temp.setJointId(gaBetSponsor.getJointId());
//						partlist.add(temp);						
//					}else if(gaBetSponsor.getRestNum()==gaBetSponsor.getGuaranteedNum()){
//						GaBetPart  temp=new GaBetPart();
//						temp.setBuyNum(gaBetSponsor.getRestNum());
//						temp.setBetMoney(gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP));
//						temp.setUserId(user.getUserId());
//						temp.setBuyTime(new Date());
//						temp.setJointId(gaBetSponsor.getJointId());
//						partlist.add(temp);							
//					}
//				}
//			}
//		}
//		gaService.updateObjectList(userList, null);
//		gaService.updateObjectList(tradeList, null);
//		gaService.updateObjectList(gaBetSponsorList, null);
//		gaService.updateObjectList(partlist, null);
//	}
	
	/**
	 * 更新每期遗漏数据
	 */
	@Override
	public void updateFetchAndOpenOmit(GfJsK3GaSession tempsession){
		log.info(">>>>>>>>>>>>>>>>>>>>遗漏");
		GameHelpUtil.log("jsk3", ">>>>>>>>>>>>>>>>>>>>遗漏");
//		GfJsK3GaSession session=JsK3DAO.getCurrentSession();
//		if(session!=null){
//			GfJsK3GaSession tempsession =JsK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");

			if(tempsession!=null){
				if(ParamUtils.chkString(tempsession.getOpenResult())){
					String openResult=tempsession.getOpenResult();
					String array[]=openResult.split(",");

					GfJsK3GaOmit omit=gfJsK3DAO.getJsK3OmitBySessionNo(tempsession.getSessionNo());
					
					if(omit==null){
						GfJsK3GaOmit preomit=gfJsK3DAO.getJsK3OmitBySessionNo((Integer.parseInt(tempsession.getSessionNo())-1)+"");
						if(preomit == null){
							GfJsK3GaSession se = (GfJsK3GaSession) gfJsK3DAO.getObject(GfJsK3GaSession.class, tempsession.getSessionId()-1);
							preomit=gfJsK3DAO.getJsK3OmitBySessionNo(se.getSessionNo());
						}

						if(preomit!=null){
							omit=new GfJsK3GaOmit();
							Map<String,String> map=new HashMap<String,String>();
							for(int i=1;i<=11;i++){
								map.put(""+i, i+"");
							}
							Field f =null;
							int sum=0;//总和
							int min=11;//最大值
							int max=1;//最小值
							int jishu=0;//奇数数量
							int oushu=0;//偶数数量
							int zhishu=0;//质数数量
							int heshu=0;//合数数量
							int da=0;//大  数量
							int xiao=0;//小  数量

							for(int i=0;i<array.length;i++){
									try {
										f= GfJsK3GaOmit.class.getDeclaredField("fenbu"+Integer.parseInt(array[i]));
										f.setAccessible(true);  
										f.set(omit, 0);
										map.put(Integer.parseInt(array[i])+"", "");
									} catch (Exception e) {
										GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:1512,"+e.toString());
										return;
									}
									sum=sum+Integer.parseInt(array[i]);
									if(min>Integer.parseInt(array[i])){
										min=Integer.parseInt(array[i]);
									}
									if(max<Integer.parseInt(array[i])){
										max=Integer.parseInt(array[i]);
									}
									if(Integer.parseInt(array[i])%2==0){
										oushu=oushu+1;
									}else{
										jishu=jishu+1;
									}
									if(isPrime(Integer.parseInt(array[i]))){
										zhishu=zhishu+1;
									}else{
										heshu=heshu+1;
									}
									if(Integer.parseInt(array[i])>=6){
										da=da+1;
									}else{
										xiao=xiao+1;
									}
							}
							for(String key:map.keySet()){
								if(ParamUtils.chkString(map.get(key))){
									try {
										f= GfJsK3GaOmit.class.getDeclaredField("fenbu"+key);
										f.setAccessible(true);
										f.set(omit, Integer.parseInt(f.get(preomit).toString())+1);
									} catch (Exception e) {
										GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:1545,"+e.toString());
										e.printStackTrace();
										return;
									} 
								}
							}
							omit.setHe(sum);
							omit.setJiou(jishu+":"+oushu);
							omit.setKuadu(max-min);
							omit.setZhihe(zhishu+":"+heshu);
							omit.setDaxiao(da+":"+xiao);
							omit.setOpenResult(tempsession.getOpenResult());
							omit.setSessionNo(tempsession.getSessionNo());
							gfJsK3DAO.saveObjectDB(omit);
						}else{
							Map<String,String> map=new HashMap<String,String>();
							for(int i=1;i<=11;i++){
								map.put(""+i, i+"");
							}
							Field f =null;
							omit=new GfJsK3GaOmit();
							int sum=0;//总和
							int min=11;//最大值
							int max=1;//最小值
							int jishu=0;//奇数数量
							int oushu=0;//偶数数量
							int zhishu=0;//质数数量
							int heshu=0;//合数数量
							int da=0;//大  数量
							int xiao=0;//小  数量
							for(int i=0;i<array.length;i++){
								sum=sum+Integer.parseInt(array[i]);
								if(min>Integer.parseInt(array[i])){
									min=Integer.parseInt(array[i]);
								}
								if(max<Integer.parseInt(array[i])){
									max=Integer.parseInt(array[i]);
								}
								if(Integer.parseInt(array[i])%2==0){
									oushu=oushu+1;
								}else{
									jishu=jishu+1;
								}
								if(isPrime(Integer.parseInt(array[i]))){
									zhishu=zhishu+1;
								}else{
									heshu=heshu+1;
								}
								if(Integer.parseInt(array[i])>=6){
									da=da+1;
								}else{
									xiao=xiao+1;
								}
							}
							try {
								for(String key:map.keySet()){
									if(ParamUtils.chkString(map.get(key))){
											f= GfJsK3GaOmit.class.getDeclaredField("fenbu"+key);
											f.setAccessible(true);							
											f.set(omit, 0);
									}
								}
							} catch (Exception e) {
								GameHelpUtil.log("jsk3", "JsK3ServiceImpl.java,line:1608,"+e.toString());
								e.printStackTrace();
								return;
							}
							omit.setHe(sum);
							omit.setJiou(jishu+":"+oushu);
							omit.setKuadu(max-min);
							omit.setZhihe(zhishu+":"+heshu);
							omit.setDaxiao(da+":"+xiao);
							omit.setOpenResult(tempsession.getOpenResult());
							omit.setSessionNo(tempsession.getSessionNo());
							gfJsK3DAO.saveObjectDB(omit);
						}
					}
				}else{
					log.info(">>>>>>>>>>>>>>>>>>>>遗漏前一期无结果");
					GameHelpUtil.log("jsk3", ">>>>>>>>>>>>>>>>>>>>遗漏前一期无结果");
				}	
			}else{
				log.info(">>>>>>>>>>>>>>>>>>>>遗漏前一期不存在");
				GameHelpUtil.log("jsk3", ">>>>>>>>>>>>>>>>>>>>遗漏前一期不存在");
			}
//		}

		log.info(">>>>>>>>>>>>>>>>>>>>遗漏结束");
		GameHelpUtil.log("jsk3", ">>>>>>>>>>>>>>>>>>>>遗漏结束");
	}	
	
    /**
     * 计算合买。
     */
	public void updateCountJointBet(String sessionNo){
		
		GfJsK3GaSession se = null;
		if(sessionNo == null){
			se=this.getCurrentSession();
		}else{
			se = this.getPreviousSessionBySessionNo(sessionNo);
		}

		if(se != null){
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<Integer> userIds = new ArrayList<Integer>();

			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ho.sessionId=? ");
			para.add(se.getSessionId());
			hqls.append(" and ho.restNum > ? ");
			para.add(0);//查询投注进度不满100%的
			hqls.append(" and ho.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and ho.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_JSK3);
			hqls.append(" and ho.betType= ? ");
			para.add(Constants.SPONSOR);

			List<SpDetailDTO> list = gaService.findGaBetSponsorAndUserList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单

			if(list!=null && list.size()>=0){
				for(SpDetailDTO dto:list){
					GaBetSponsor sponsor =  dto.getGaBetSponsor();
					String isGuarantee = sponsor.getIsGuarantee();//是否保底
					if(Constants.GUARANTEE.equals(isGuarantee)){ //如果保底
//						User user= dto.getUser();
						int guaranteedNum = sponsor.getGuaranteedNum(); //保底份数
						int restNum = sponsor.getRestNum(); //剩余份数
						if(guaranteedNum >= restNum){//保底份数 >=剩余份数 ,投注有效，把保底拿去投注
							GaBetPart betPart = new GaBetPart();
							BigDecimal guaBetMoney = sponsor.getPreMoney().multiply(new BigDecimal(restNum));//实际使用保底投注的金额

							betPart.setUserId(sponsor.getUserId());
							betPart.setBuyNum(restNum);
							betPart.setBuyTime(DateTimeUtil.getNowSQLDate());;
							betPart.setBetMoney(guaBetMoney);
							betPart.setJointId(sponsor.getJointId());
							betPart.setWinCash(new BigDecimal(0));
							betPart.setWinPoint(new BigDecimal(0));
							betPart.setBehavior(Constants.PARTICIPATOR);
							paList.add(betPart);

							if(guaranteedNum > restNum){//需要退钱
								BigDecimal reMoney = new BigDecimal(guaranteedNum-restNum).multiply(sponsor.getPreMoney());//需要退钱数量
								
								if(!StringUtil.chkListIntContains(userIds, sponsor.getUserId())){
									userIds.add(sponsor.getUserId());
								}
								// 2.保存明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单").append(sponsor.getOrderNum()).append(",退回保底款项分数:")
								    .append(guaranteedNum-restNum).append(";金额:").append(reMoney).append("元");
								this.updateUserBaodiBack(sponsor,reMoney,remark.toString());
							}
							if("1".equals(sponsor.getIsPrivacy())){
								sponsor.setIsPrivacy("3");
							}

							sponsor.setSchedule(new BigDecimal(1));
							sponsor.setRestNum(0);
							sponsor.setBetFlag(Constants.PUB_STATUS_OPEN);
							sponsor.setWinResult(Constants.INIT);
							spList.add(sponsor);
							
						}else{ //投注不满 100% 投注无效，退款
							sponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);	
							sponsor.setWinResult(Constants.INVALID_REFUND);//投注无效，退款
							if("1".equals(sponsor.getIsPrivacy())){
								sponsor.setIsPrivacy("3");
							}
							spList.add(sponsor);

							BigDecimal guMoney = sponsor.getGuaranteedMoney();
							
							if(!StringUtil.chkListIntContains(userIds, sponsor.getUserId())){
								userIds.add(sponsor.getUserId());
							}
							
							// 2.保存明细
							StringBuilder remark = new StringBuilder();
							remark.append("订单").append(sponsor.getOrderNum()).append(",未满员退回保底款项; 金额:")
							    .append(guMoney).append("元");
							this.updateUserBetBack(sponsor,remark.toString());

							List<SpDetailDTO>  rlList = gaService.findGaBetPartListByJointId(sponsor.getJointId());
							if(rlList !=null&& rlList.size()>=0){
								for(SpDetailDTO paDto:rlList){
									GaBetPart betPart = paDto.getGaBetPart();
									BigDecimal betMoney = betPart.getBetMoney();
									
									if(!StringUtil.chkListIntContains(userIds, betPart.getUserId())){
										userIds.add(betPart.getUserId());
									}
									
									// 2.保存明细
									StringBuilder remark2 = new StringBuilder();
									remark2.append("订单").append(sponsor.getOrderNum()).append(",未满员退回投注款;金额:")
									    .append(betMoney).append("元");
									this.updateUserBetBack(betPart,betMoney,remark.toString());
								}
							}
						}
					}else{ //不保底
						sponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);	
						sponsor.setWinResult(Constants.INVALID_REFUND);//投注无效，退款
						spList.add(sponsor);
						
						List<SpDetailDTO>  rlList = gaService.findGaBetPartListByJointId(sponsor.getJointId());
						if(rlList !=null&& rlList.size()>=0){
							for(SpDetailDTO paDto:rlList){
								GaBetPart betPart = paDto.getGaBetPart();
								BigDecimal betMoney = betPart.getBetMoney();
								
								if(!StringUtil.chkListIntContains(userIds, betPart.getUserId())){
									userIds.add(betPart.getUserId());
								}
								
								// 2.保存明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单").append(sponsor.getOrderNum()).append(",未满员退回投注款;金额:")
								    .append(betMoney).append("元");
								this.updateUserBetBack(betPart, betMoney, remark.toString());
							}
						}

					}
				}
			}
			userService.updateUserMoneyAndBetMoney(userIds);
			gfJsK3DAO.updateObjectList(spList, null);
			gfJsK3DAO.updateObjectList(paList, null);
		}
	}
	
	
	private void updateUserBaodiBack(GaBetSponsor sponsor,BigDecimal reMoney,String remark) {//退回部分保底金额
		userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_GUA_BACK, reMoney, sponsor.getJointId(), Constants.GAME_TYPE_GF_JSK3,remark);
	}
	
	
	private void updateUserBetBack(GaBetSponsor sponsor,String remark) {//方案撤单  退回全部保底
		userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, sponsor.getGuaranteedMoney(), sponsor.getJointId(), Constants.GAME_TYPE_GF_JSK3,remark);
	}
	
	
	private void updateUserBetBack(GaBetPart betPart, BigDecimal betMoney,
			String remark) {//退回参与用户金额
		userService.saveTradeDetail(null,betPart.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, betMoney, betPart.getRid(), Constants.GAME_TYPE_GF_JSK3,remark);
	}
	
	public boolean isPrime(int n){
	    for(int i=2;i<=n/2;i++){
	         if(n%i == 0)
	              return false; 
	    }
	    return true; 
	}

	public GfJsK3GaSession getFirstSessionByDate(String date) {
		return gfJsK3DAO.getFirstSessionByDate(date);
	}
	public GfJsK3GaSession getEndSessionByDate(String date){
		return gfJsK3DAO.getEndSessionByDate(date);
	}
	public List<GfJsK3GaOmit> findGfJsK3GaOmitList(String string,
			List<Object> para, int num){
		return gfJsK3DAO.findGfJsK3GaOmitList(string,para,num);
	}

	@Override
	public void updateDayBetCount(GfJsK3GaSession tempsession) {
//		GfJsK3GaSession session=JsK3DAO.getCurrentSession();
//		if(session!=null){
//			GfJsK3GaSession tempsession =JsK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");
			if(tempsession!=null){
				if(ParamUtils.chkString(tempsession.getOpenStatus())&&Constants.OPEN_STATUS_OPENED.equals(tempsession.getOpenStatus())){
				//如果上期已开奖
					List<Object> spPara = new ArrayList<Object>();
					StringBuffer spHqls = new StringBuffer();
					spHqls.append(" and sp.sessionId=? ");
					spPara.add(tempsession.getSessionId());
					spHqls.append(" and sp.betFlag=? " );
					spPara.add(Constants.PUB_STATUS_OPEN);// 有效
					spHqls.append(" and sp.gameType =? ");
					spPara.add(Constants.GAME_TYPE_GF_JSK3);
					
					List<GaBetSponsor> list = gaService.findGaBetSponsorList(spHqls.toString(), spPara);//发起购买表，查询当前期有效的发起者发起的购买订单
					if(list!=null&&list.size()>0){
						BigDecimal totalBetMoney = new BigDecimal(0); //下注
						BigDecimal totalWinMoney = new BigDecimal(0); //中奖
						BigDecimal payOff = new BigDecimal(0); //盈亏

						for(GaBetSponsor sp: list){
							totalBetMoney = totalBetMoney.add(sp.getMoney());
							totalWinMoney = totalWinMoney.add(sp.getWinCash());
						}
						payOff = totalBetMoney.subtract(totalWinMoney);
					
						Date date=DateTimeUtil.getNowSQLDate();
						String startDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, 0))+" 00:00:00";
						String endDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, 0))+" 23:59:59";//DateTimeUtil.getDateTimeOfMinutes(date, -5)

						List<Object> para = new ArrayList<Object>();
						StringBuffer hqls = new StringBuffer();
						
						hqls.append(" and ho.createTime >=? ");
						para.add(startDate);
						hqls.append(" and ho.createTime <= ? ");
						para.add(endDate);

						GaDayBetCount dayBetCount = null;
						dayBetCount = gaService.findDayBetCount(hqls,para);
						if(dayBetCount == null){
							dayBetCount = new GaDayBetCount();
							dayBetCount.setCreateTime(date);
						}else{
							totalBetMoney = totalBetMoney.add(ProductUtil.checkBigDecimal(dayBetCount.getBetMoney()));
							totalWinMoney = totalWinMoney.add(ProductUtil.checkBigDecimal(dayBetCount.getWinMoney()));
							payOff = payOff.add(ProductUtil.checkBigDecimal(dayBetCount.getPayoff()));
						}
						dayBetCount.setBetMoney(totalBetMoney);
						dayBetCount.setWinMoney(totalWinMoney);
						dayBetCount.setPayoff(payOff);
						gaService.saveObjectDB(dayBetCount);					
					}
				}
//			}
		}		
	}
	
	@Override
	public boolean saveRevokePrize(GfJsK3GaSession session) {

		boolean result = gaService.saveGfRevokePrize(session.getSessionId(), Constants.GAME_TYPE_GF_JSK3,session.getSessionNo());
		if(result){
			session.setOpenResult("");
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
}
