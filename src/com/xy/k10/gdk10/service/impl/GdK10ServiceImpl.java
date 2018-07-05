package com.xy.k10.gdk10.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.dto.SessionItem;
import com.apps.model.UserTradeDetailRl;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.GameConstants;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.service.IGaService;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.xy.k10.gdk10.GdK10Constants;
import com.xy.k10.gdk10.dao.IGdK10DAO;
import com.xy.k10.gdk10.model.GdK10GaBet;
import com.xy.k10.gdk10.model.GdK10GaSession;
import com.xy.k10.gdk10.model.GdK10GaTrend;
import com.xy.k10.gdk10.model.dto.GdK10DTO;
import com.xy.k10.gdk10.service.IGdK10Service;

public class GdK10ServiceImpl extends BaseService implements IGdK10Service {
	private IGdK10DAO gdK10DAO;
	private IUserService userService;
	private IGaService gaService;
	public void setGdK10DAO(IGdK10DAO gdK10dao) {
		gdK10DAO = gdK10dao;
		super.dao = gdK10DAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	/**
	 * 初始化场次
	 */
	public String updateInitSession(){
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<GdK10GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<GdK10GaSession>();
					
			String startTimeStr = today + GdK10Constants.GD_K10_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			for (int i = 0; i < GdK10Constants.GD_K10_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*GdK10Constants.GD_K10_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				//String openResult = GameConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(now, i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				GdK10GaSession k10Session = new GdK10GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,GdK10Constants.GD_K10_TIME_INTERVAL));
				k10Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
//				gdK10DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			gdK10DAO.updateObjectList(saveList, null);
			flag = "success";
//			log.info("___[today init completed]__________________________");
		} else {
			flag = "inited";
//			log.info("___[today has been inited]__________________________");
		}
		return flag;
	}
	
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
		
		HQUtils hq = new HQUtils("from GdK10GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gdK10DAO.countObjects(hq);
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
		
		HQUtils hq = new HQUtils("from GdK10GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gdK10DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	//-----------------------独立业务方法写在这里----------------------------------------------------
	
	/**
	 * 获取今天的期号   看数据是 年月日+当前第几期
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date today,int index){	
		if(index<10){
			return DateTimeUtil.DateToStringYY(today)+"0"+index;
		}else{
			return DateTimeUtil.DateToStringYY(today)+index;
		}
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		GdK10GaSession curtSession = gdK10DAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		GdK10GaSession lastSession = (GdK10GaSession)gdK10DAO.getObject(GdK10GaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_GDK10);
		
		//迭代开奖无序
		List<GdK10GaSession> openedList = new ArrayList<GdK10GaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				GdK10GaSession session = gdK10DAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openGdK10SessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							gdK10DAO.saveObject(session);
							openedList.add(session);
						}
						GameHelpUtil.log(gameCode,"End ... ["+sNo+"]["+(System.currentTimeMillis()-timingOpen)+"ms],status="+session.getOpenStatus()+",result="+result);
					}
				}else{
					GameHelpUtil.log(gameCode,"opening session is null::"+sNo);
				}
			}
			
			//更新彩种场次状态
			long timingGsi = System.currentTimeMillis();
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_GDK10);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				gdK10DAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(GdK10GaSession session:openedList){
				this.updateTrendResult(session);
			}
			GameHelpUtil.log(gameCode,"trend ... ["+openedList.size()+"]["+(System.currentTimeMillis()-startTrending)+"ms]");
			
			sessionNoMap.clear();
			return "success";
		} catch (Exception e) {
			GameHelpUtil.log(gameCode,"open err::=>"+e.getMessage(),e);
			return "err::throw exception...";
		}
	}
	
//	@Override
//	public void updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
//		GdK10GaSession currentSession=gdK10DAO.getCurrentSession();
//		GdK10GaSession tempsession =gdK10DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession == null){
//			tempsession=(GdK10GaSession) gdK10DAO.getObject(GdK10GaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//			if(!sessionNoMap.isEmpty()){
//				for(String key:sessionNoMap.keySet()){
//					GdK10GaSession session =gdK10DAO.getPreviousSessionBySessionNo(key);
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//							SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//							String openResult = sessionItem.getResult();
//							session.setOpenResult(openResult);
//							boolean flag = openGdK10SessionOpenResultMethod(session, session.getOpenResult());
//							if(flag){
//								session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//								gdK10DAO.saveObject(session);
//							}else{
//								GameHelpUtil.log(Constants.GAME_TYPE_XY_GDK10,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//							}
//						}
//						// 把资金明细里投注记录状态值改为有效
//						userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//								Constants.GAME_TYPE_XY_GDK10, Constants.PUB_STATUS_OPEN);
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_GDK10);
//				if(sessionInfo != null){
//					SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//					if(lastItem!=null){
//						sessionInfo.setOpenResult(lastItem.getResult());
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(tempsession.getEndTime());
//					}
//					
//				}
//				gdK10DAO.updateObject(sessionInfo, null);
//				CacheUtil.updateGameList();
//			}
//	}
	
//	@Override
//	public void updateFetchAndOpenResult() {
//		GdK10GaSession currentSession=gdK10DAO.getCurrentSession();
//		GdK10GaSession tempsession =gdK10DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(GdK10GaSession) gdK10DAO.getObject(GdK10GaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//		final Map<String,String> sessionNoMap=new HashMap<String,String>();
//		final Map<String,String> timeMap=new HashMap<String,String>();
//		Thread t=new Thread(){
//            public void run(){
//         	   Integer countRun=0;
//               try {
//
//            	   while(true){
//            		   if(countRun==190){
//            			   interrupt();
//            			   countRun=null;
//            			   break;
//            		   }
//            		    countRun=countRun+1;
//            		 
//	               		//从这里 ---------------------------------------------------------------------------
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xygdk10");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    sessionInfo=null;
//            		    
//	               		log.info("___[gdk10 start fetch result xml data...]________________---------gdk10");
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[gdk10 start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/gdk10.txt", true);
//	    	            
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
////	               		log.info(resultXML);
//	               		//到这里 ---------------------------------------------------------------------------		
//	               	    sleep(3000);
////	               		log.info("___[fetch result xml data]"+resultXML);
//	            	    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/gdk10.txt", true);
//
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);            			
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果8组数字
//	               			String time="";
//	            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//		               			//开始解析场次开奖数据
//		               			NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				sessionNo=sessionNo.substring(sessionNo.length()-9);//2017041225 去掉前两位
//		               				sessionNo=sessionNo.substring(0,6)+sessionNo.substring(7,9);
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
//		               				time=XmlUtil.getElementAttribute((Element)node, "opentime");
//	           						if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							countRun=null;
//	           							interrupt();
//	           						}
//		               			}
//	            		    }else if(urlSwitch.equals("2")){
//	                   			NodeList nList = xmlDoc.getElementsByTagName("item");
//	                   			for(int i =0;i<nList.getLength();i++){
//	                   				Node node = nList.item(i);
//	                  				sessionNo = XmlUtil.getElementAttribute((Element)node, "id");
//	                  				sessionNo=sessionNo.substring(sessionNo.length()-9);
//	                  				time=XmlUtil.getElementAttribute((Element)node, "dateline");  				
//	                  				NodeList employeeMeta = node.getChildNodes(); 
//	                  				result=employeeMeta.item(0).getNodeValue();
//	                  				if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							countRun=null;
//	           							interrupt();
//	           						}
//	                   			}
//	            		    }
////	               			interrupt();
//	               		}else{
////	               			interrupt();
////	               			countRun=null;
////       						break;
//	               		}
//            	   }
//               } catch (Exception e) {
////            	   countRun=null;
////            	   interrupt();
//               }
//            }
//        };
//        t.start();
//        
//        try {
//			t.join();//该方法是等 t 线程结束以后再执行后面的代码
//			t=null;
//			if(!sessionNoMap.isEmpty()){
//
//				for(String key:sessionNoMap.keySet()){
//					GdK10GaSession session =gdK10DAO.getPreviousSessionBySessionNo(key);
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//							session.setOpenResult(sessionNoMap.get(key));
//							boolean flag = openGdK10SessionOpenResultMethod(session, session.getOpenResult());
//							if(flag){
////								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//								gdK10DAO.saveObject(session);
//								log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//							}else{
//								log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//							}
//						}
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_GDK10);
//				if(sessionInfo != null){
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					log.info(DateTimeUtil.DateToStringAll(new Date())+" openResult>>>>>>>>>>:"+sessionNoMap.get(lastSessionNo));
//					if(sessionNoMap.get(lastSessionNo)!=null){
//						sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(tempsession.getEndTime());
//					}
//					
//				}
////				else{
////					sessionInfo=new  GaSessionInfo();
////					sessionInfo.setGameTitle("广东快乐十分");
////					sessionInfo.setGameType(Constants.GAME_TYPE_XY_GDK10);
////					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
////					if(sessionNoMap.get(lastSessionNo)!=null){
////						sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));
////						sessionInfo.setOpenSessionNo(lastSessionNo);
////						sessionInfo.setEndTime(tempsession.getEndTime());
////					}					
////				}
//				gdK10DAO.updateObject(sessionInfo, null);
//				CacheUtil.updateGameList();
//				sessionNoMap.clear();
//				timeMap.clear();
//				sessionInfo=null;
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//        currentSession=null;
//	}
	
	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param sessionNo
	 * @param result 开奖号10组数字 英文逗号连接
	 * @return
	 */
	public boolean openGdK10SessionOpenResultMethod(GdK10GaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_GDK10);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=? ");
			para.add(Constants.GAME_TYPE_XY_GDK10);
			hql.append(" and ho.sessionId=? ");
			para.add(session.getSessionId());
			hql.append(" and ho.betFlag=? " );
			para.add("1");
			//hql.append(" order by ho.betTime desc");
			
			//本期全部投注记录
			long startTiming = System.currentTimeMillis();
			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
			GameHelpUtil.log(gameCode,"BETS ... ["+list.size()+"]["+session.getSessionNo()+"]["+(System.currentTimeMillis()-startTiming)+"ms]");
			//本期投注统计表
			
			GdK10GaBet bet=new GdK10GaBet();
			BigDecimal totalPoint=new BigDecimal(0);//总投注
			BigDecimal betCash=new BigDecimal(0);//总中奖
			bet.setSessionId(session.getSessionId());
			bet.setSessionNo(session.getSessionNo());

			if(list!=null && !list.isEmpty()){
				//开奖投注用户ids --by.cuisy.20171209
				List<Integer> userIds = new ArrayList<Integer>();
				startTiming = System.currentTimeMillis();
				for(GaBetDetail detail:list){
					//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
					if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
						userIds.add(detail.getUserId());
					}//~
					
					String flag=judgeWin(result,detail);//0未中奖  1打和  2中奖
					detail.setOpenResult(result);
					
					if(detail.getPlayName().equals("连码")){
						if(Integer.parseInt(flag)>0){//中奖
							detail.setWinResult(GameConstants.WIN);
							
							BigDecimal winNum = new BigDecimal(flag);
							Integer betAll = detail.getBetMoney();
							
							// 计算总注数
							int weight = 2;
							int betNum = 1;
							
							if(detail.getBetName().equals("任选二")){
								weight = 2;
							}else if(detail.getBetName().equals("任选二组")){
								weight = 2;
							}else if(detail.getBetName().equals("任选三")){
								weight = 3;
							}else if(detail.getBetName().equals("任选四")){
								weight = 4;
							}else {
								weight = 5;
							}
							String[] arr = detail.getOptionTitle().split(",");
							
							if(arr.length<weight){
								betNum = 1;
							}else if(arr.length==weight){
								betNum = 1;
							}else{
								int numerator = 1;
								for(int i = 0;i<weight;i++){
									numerator = numerator * (arr.length-i);
								}
								int denominator = 1;
								for(int i = weight;i>0;i--){
									denominator = denominator * i;
								}
								betNum = numerator / denominator;
							}
							
							//中奖金额
							BigDecimal wincash=GameHelpUtil.round(detail.getBetRate().multiply(new BigDecimal(betAll/betNum)).multiply(winNum));
							detail.setWinCash(wincash);
							//统计
							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
							betCash=betCash.add(wincash);
							//盈亏
							detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
							//备注
							String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
							//更新
							updateOpenData(detail,remark);
						}else{
							//统计
							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
							detail.setWinCash(new BigDecimal(0));
							detail.setWinResult(GameConstants.WIN_NOT);//未中奖
							gdK10DAO.saveObject(detail);
						}
					}else{
						if(flag.equals("2")){//中奖
							detail.setWinResult(GameConstants.WIN);
							//中奖金额
							BigDecimal wincash=GameHelpUtil.round(detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())));
							detail.setWinCash(wincash);
							//统计
							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
							betCash=betCash.add(wincash);
							//盈亏
							detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
							//备注
							String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
							//更新
							updateOpenData(detail,remark);
							
						}else if(flag.equals("1")){//打和
							detail.setWinResult(GameConstants.WIN);
							//中奖金额
							BigDecimal wincash=GameHelpUtil.round(new BigDecimal(detail.getBetMoney()));//投注总钱数
							detail.setWinCash(wincash);
							//统计
							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
							betCash=betCash.add(wincash);
							//盈亏
							detail.setPayoff(new BigDecimal(0));
							//备注
							String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
							//更新
							updateOpenData(detail,remark.toString());
							
						}else{//未中奖
							//统计
							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
							detail.setWinCash(new BigDecimal(0));
							detail.setWinResult(GameConstants.WIN_NOT);//未中奖
							gdK10DAO.saveObject(detail);
						}
					}
				}
				GameHelpUtil.log(gameCode,"Calc ... ["+(System.currentTimeMillis()-startTiming)+"ms]");
				//批量更新开奖用户实时余额 --by.cuisy.20171209
				startTiming = System.currentTimeMillis();
				userService.updateUserMoney(userIds);
				GameHelpUtil.log(gameCode,"BatM ... uids="+userIds.size()+"["+(System.currentTimeMillis()-startTiming)+"ms]");
				
				//把资金明细里投注记录状态值改为有效
				long timginUtds = System.currentTimeMillis();
				userService.updateUserTradeDetailStatus(session.getSessionNo(), 
						Constants.GAME_TYPE_XY_GDK10, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			gdK10DAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}
	}
	
//	public boolean openGdK10SessionOpenResultMethod(GdK10GaSession session,String result){
//		try{
////			GdK10GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(sessionNo);
//			List<Object> para = new ArrayList<Object>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" and ho.gameType=? ");// 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			para.add(Constants.GAME_TYPE_XY_GDK10);
//			hql.append(" and ho.sessionId=? ");
//			para.add(session.getSessionId());
//			hql.append(" and ho.betFlag=? " );
//			para.add("1");
//			hql.append(" order by ho.betTime desc");
//			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
//			
//			GdK10GaBet bet=new GdK10GaBet();
//			BigDecimal  totalPoint=new BigDecimal(0);
//			BigDecimal  betCash=new BigDecimal(0);
//			bet.setSessionId(session.getSessionId());
//			bet.setSessionNo(session.getSessionNo());
//
//			
//			if(list!=null&&list.size()>0){
//				//开奖投注用户ids --by.cuisy.20171209
//				List<Integer> userIds = new ArrayList<Integer>();
//				
//				for(GaBetDetail detail:list){
//					
//					//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//					if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
//						userIds.add(detail.getUserId());
//					}//~
//					
//					String flag=judgeWin(result,detail);//0未中奖  1打和  2中奖
//					StringBuilder remark = new StringBuilder();
//					detail.setOpenResult(result);
//					
//					if(detail.getPlayName().equals("连码")){
//						if(Integer.parseInt(flag)>0){
//							// 中奖
//							BigDecimal winNum = new BigDecimal(flag);
//							detail.setWinResult(BjPk10Constants.BJ_PK10_WIN);//中奖
//							Integer betAll = detail.getBetMoney();
//							// 计算总注数
//							int weight = 2;
//							int betNum = 1;
//							
//							if(detail.getBetName().equals("任选二")){
//								weight = 2;
//							}else if(detail.getBetName().equals("任选二组")){
//								weight = 2;
//							}else if(detail.getBetName().equals("任选三")){
//								weight = 3;
//							}else if(detail.getBetName().equals("任选四")){
//								weight = 4;
//							}else {
//								weight = 5;
//							}
//							String[] arr = detail.getOptionTitle().split(",");
//							
//							if(arr.length<weight){
//								betNum = 1;
//							}else if(arr.length==weight){
//								betNum = 1;
//							}else{
//								int numerator = 1;
//								for(int i = 0;i<weight;i++){
//									numerator = numerator * (arr.length-i);
//								}
//								int denominator = 1;
//								for(int i = weight;i>0;i--){
//									denominator = denominator * i;
//								}
//								betNum = numerator / denominator;
//							}
//							
//							BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(betAll/betNum)).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(winNum);
//							detail.setWinCash(wincash);
//							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//							betCash=betCash.add(wincash);
//							
//							detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////						userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_GDK10, wincash, detail.getBetDetailId());	
//							
//							remark.append("彩票中奖 奖金 ").append(wincash).append("元");
//							try {
//								updateOpenData(detail,null,remark.toString());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else{
//							detail.setWinCash(new BigDecimal(0));
//							detail.setWinResult(BjPk10Constants.BJ_PK10_WIN_NOT);//未中奖
//							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//							try {
//								gdK10DAO.updateObject(detail, null);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}else{
//						if(flag.equals("2")){//中奖
//							
//							detail.setWinResult(BjPk10Constants.BJ_PK10_WIN);//中奖
//							BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//							detail.setWinCash(wincash);
//							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//							betCash=betCash.add(wincash);
//							
//							detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////						userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_GDK10, wincash, detail.getBetDetailId());	
//							
//							remark.append("彩票中奖 奖金 ")
//							.append(wincash).append("元");
////						user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_GDK10,remark.toString());
//							
////						BigDecimal userBal=null;
////						User user=(User) gaService.getObject(User.class, detail.getUserId());
////						if(user.getUserBalance()!=null){
////							userBal=user.getUserBalance();
////						}else{
////							userBal=new BigDecimal(0);
////						}
////						user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//							
//							try {
//								updateOpenData(detail,null,remark.toString());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//						}else if(flag.equals("1")){//打和
//							
//							detail.setWinResult(BjPk10Constants.BJ_PK10_WIN_HE);//打和
//							BigDecimal wincash=new BigDecimal(detail.getBetMoney());//投注总钱数
//							
//							
//							detail.setWinCash(wincash);
////							userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_GDK10, wincash, detail.getBetDetailId());	
//							
//							remark.append("彩票打和 退款")
//							.append(wincash).append("元");
////							user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_GDK10,remark.toString());
//							
////						BigDecimal userBal=null;
////						User user=(User) gaService.getObject(User.class, detail.getUserId());
////						if(user.getUserBalance()!=null){
////							userBal=user.getUserBalance();
////						}else{
////							userBal=new BigDecimal(0);
////						}
////						user.setUserBalance(userBal.add(wincash));
//							try {
//								updateOpenData(detail,null,remark.toString());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//							betCash=betCash.add(wincash);
//						}else{//未中奖
//							detail.setWinCash(new BigDecimal(0));
//							detail.setWinResult(BjPk10Constants.BJ_PK10_WIN_NOT);//未中奖
//							totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//							try {
//								gdK10DAO.updateObject(detail, null);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						
//					}
//				}
//				
//				//批量更新开奖用户实时余额 --by.cuisy.20171209
//				userService.updateUserMoney(userIds);
//			}
//			bet.setTotalPoint(totalPoint);
//			bet.setWinCash(betCash);
//			try {
//				gdK10DAO.saveObject(bet, null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}
//		
//	}
	
	public void updateOpenData(GaBetDetail detail,String remark){
		gdK10DAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_GDK10,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		gdK10DAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_GDK10,remark);
//	}
	
//	public void updateOpenData(GaBetDetail detail,User user){
//		
//		gdK10DAO.saveObject(detail);
//		gdK10DAO.saveObject(user);
////		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_GDK10,remark);
//		
//	}
	
	public GdK10GaSession getCurrentSession(){
		return gdK10DAO.getCurrentSession();
	}
	public GdK10GaSession getPreviousSessionBySessionNo(String sessionNo){
		return gdK10DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GdK10GaSession session,User user,BigDecimal betAll){
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_GDK10);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		Map<String, GaBetDetail> optionsMap = new HashMap<String, GaBetDetail>();
		
		GaBetOption firstItem = list.get(0);
		Integer totalMoney = 0;
		String cuTitle = "";
		if(firstItem.getPlayType().equals("9")){// 连码
			for (int i = 0; i < list.size(); i++) {
				GaBetDetail betDetail = null;
				GaBetOption betOption = list.get(i);
				String numStr = betOption.getOptionTitle().substring(0, betOption.getOptionTitle().length()-1);
				
				if(cuTitle.equals(betOption.getTitle())){
					betDetail=optionsMap.get(cuTitle);
				}else{
					cuTitle = betOption.getTitle();
					betDetail=new GaBetDetail();
					betDetail.setBetOptionId(betOption.getBetOptionId());
					betDetail.setOptionTitle("");
					betDetail.setBetName(betOption.getTitle());
					betDetail.setBetRate(betOption.getBetRate());
					optionsMap.put(betOption.getTitle(), betDetail);
				}
				 
				if(Integer.parseInt(numStr)<10){
					numStr = "0"+numStr;
				}
				betDetail.setOptionTitle(betDetail.getOptionTitle()+ numStr+",");
			}
			
			for(GaBetDetail betDetail : optionsMap.values()){
				if(betDetail.getOptionTitle().endsWith(",")){
					betDetail.setOptionTitle(betDetail.getOptionTitle().substring(0, betDetail.getOptionTitle().length()-1));
				}
				
				betDetail.setUserId(user.getUserId());
				betDetail.setLoginName(user.getLoginName());
				betDetail.setType(userType);
				
				betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
				betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
				betDetail.setSessionId(session.getSessionId());
				
//				betDetail.setBetOptionId(firstItem.getBetOptionId());//在上面循环判断中赋值
				betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
				// 计算每一注的总金额
				String[] strArr = betDetail.getOptionTitle().split(",");
				int weight = 2;
				if(betDetail.getBetName().equals("任选二")){
					weight = 2;
				}else if(betDetail.getBetName().equals("任选二组")){
					weight = 2;
				}else if(betDetail.getBetName().equals("任选三")){
					weight = 3;
				}else if(betDetail.getBetName().equals("任选四")){
					weight = 4;
				}else {
					weight = 5;
				}
				int numerator = 1;
				for(int i = 0;i<weight;i++){
					numerator = numerator * (strArr.length-i);
				}
				int denominator = 1;
				for(int i = weight;i>0;i--){
					denominator = denominator * i;
				}
				// 一次投注所有的金额是一样的
				totalMoney = (numerator / denominator)*betMap.get(firstItem.getBetOptionId());
				betDetail.setBetMoney(totalMoney);
				
				betDetail.setRoom(room);
				betDetail.setSessionNo(session.getSessionNo());
				betDetail.setGameName(gsi.getGameTitle());
				
				if(firstItem.getPlayType().equals("0")){//两面盘
					betDetail.setPlayName("两面盘");
				}else if(firstItem.getPlayType().equals("9")){//连码
					betDetail.setPlayName("连码");
				} else{
					betDetail.setPlayName("第"+firstItem.getPlayType()+"球");
				}
				
				betDetail.setGameType(gsi.getGameType());//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
				
				//不能为空字段初始化
				betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
				betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
				betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				
				gdK10DAO.saveObject(betDetail);
				
				//关联构造
				UserTradeDetailRl rl = new UserTradeDetailRl();
				rl.setBetDetailId(betDetail.getBetDetailId());
				rlList.add(rl);
			}
			
		}else{
			
			for (int i = 0; i < list.size(); i++) {
				GaBetOption betOption = list.get(i);
				GaBetDetail betDetail=new GaBetDetail();
				if(betOption!=null){
					betDetail.setBetRate(betOption.getBetRate());
				}
				betDetail.setUserId(user.getUserId());
				betDetail.setLoginName(user.getLoginName());
				betDetail.setType(userType);
				
				betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
				betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
				betDetail.setSessionId(session.getSessionId());
				
				betDetail.setBetOptionId(betOption.getBetOptionId());
				betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
				
				betDetail.setRoom(room);
				betDetail.setSessionNo(session.getSessionNo());
				betDetail.setGameName(gsi.getGameTitle());
				
				if(betOption.getPlayType().equals("0")){//两面盘
					betDetail.setPlayName("两面盘");
				}else if(betOption.getPlayType().equals("9")){//连码
					betDetail.setPlayName("连码");
				} else{
					betDetail.setPlayName("第"+betOption.getPlayType()+"球");
				}
				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
				betDetail.setOptionTitle(betOption.getOptionTitle());
				betDetail.setGameType(gsi.getGameType());
				
				//不能为空字段初始化
				betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
				betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
				betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
				
				gdK10DAO.saveObject(betDetail);
				
				//关联构造
				UserTradeDetailRl rl = new UserTradeDetailRl();
				rl.setBetDetailId(betDetail.getBetDetailId());
				rlList.add(rl);
				
			}
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_GDK10,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());
		
		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		gdK10DAO.updateObjectList(rlList, null);

		return user;
	}
	
//	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GdK10GaSession gaK10Session,User user,BigDecimal betAll){
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//		Map<String, GaBetDetail> optionsMap = new HashMap<String, GaBetDetail>();
//		BigDecimal paperMoney=user.getUserScore();
//		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
//		BigDecimal tempMoney=new BigDecimal(0);
//		GaBetOption firstItem = list.get(0);
//		Integer totalMoney = 0;
//		String cuTitle = "";
//		if(firstItem.getPlayType().equals("9")){
//			// 连码
//			for (int i = 0; i < list.size(); i++) {
//				
//				GaBetDetail betDetail = null;
//				GaBetOption betOption = list.get(i);
//				String numStr = betOption.getOptionTitle().substring(0, betOption.getOptionTitle().length()-1);
//				
//				if(cuTitle.equals(betOption.getTitle())){
//					betDetail=optionsMap.get(cuTitle);
//				}else{
//					cuTitle = betOption.getTitle();
//					betDetail=new GaBetDetail();
//					betDetail.setBetOptionId(betOption.getBetOptionId());
//					betDetail.setOptionTitle("");
//					betDetail.setBetName(betOption.getTitle());
//					betDetail.setBetRate(betOption.getBetRate());
//					optionsMap.put(betOption.getTitle(), betDetail);
//				}
//				 
//				if(Integer.parseInt(numStr)<10){
//					numStr = "0"+numStr;
//				}
//				betDetail.setOptionTitle(betDetail.getOptionTitle()+ numStr+",");
//			}
//			
//			for(GaBetDetail betDetail : optionsMap.values()){
//				if(betDetail.getOptionTitle().endsWith(",")){
//					betDetail.setOptionTitle(betDetail.getOptionTitle().substring(0, betDetail.getOptionTitle().length()-1));
//				}
////				GaBetDetail betDetail=new GaBetDetail();
////				if(firstItem!=null){
////					betDetail.setBetRate(firstItem.getBetRate());
////				}
//				betDetail.setUserId(user.getUserId());
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(gaK10Session.getSessionId());
//				
////				betDetail.setBetOptionId(firstItem.getBetOptionId());
//				betDetail.setBetTime(new Date());
//				// 计算每一注的总金额
//				String[] strArr = betDetail.getOptionTitle().split(",");
//				int weight = 2;
//				if(betDetail.getBetName().equals("任选二")){
//					weight = 2;
//				}else if(betDetail.getBetName().equals("任选二组")){
//					weight = 2;
//				}else if(betDetail.getBetName().equals("任选三")){
//					weight = 3;
//				}else if(betDetail.getBetName().equals("任选四")){
//					weight = 4;
//				}else {
//					weight = 5;
//				}
//				int numerator = 1;
//				for(int i = 0;i<weight;i++){
//					numerator = numerator * (strArr.length-i);
//				}
//				int denominator = 1;
//				for(int i = weight;i>0;i--){
//					denominator = denominator * i;
//				}
//				// 一次投注所有的金额是一样的
//				totalMoney = (numerator / denominator)*betMap.get(firstItem.getBetOptionId());
//				betDetail.setBetMoney(totalMoney);
//				
//				if(paperMoney.compareTo(new BigDecimal(0))==1){
//					if(new BigDecimal(betMap.get(firstItem.getBetOptionId())).compareTo(paperMoney)!=-1){
//						betDetail.setPaperMoney(paperMoney);
//						paperMoney=paperMoney.subtract(paperMoney);
//						tempMoney=tempMoney.add(new BigDecimal(betMap.get(firstItem.getBetOptionId())));
//					}else{
//						betDetail.setPaperMoney(new BigDecimal(betMap.get(firstItem.getBetOptionId())));
//						paperMoney=paperMoney.subtract(new BigDecimal(betMap.get(firstItem.getBetOptionId())));
//						tempMoney=tempMoney.add(new BigDecimal(betMap.get(firstItem.getBetOptionId())));
//					}
//				}
//				
////			betDetail.setPaperMoney(paperMoney);
//				betDetail.setRoom(room);
//				betDetail.setSessionNo(gaK10Session.getSessionNo());
//				betDetail.setGameName("广东快乐十分");
//				if(firstItem.getPlayType().equals("0")){//两面盘
//					betDetail.setPlayName("两面盘");
//				}else if(firstItem.getPlayType().equals("9")){//连码
//					betDetail.setPlayName("连码");
//				} else{
//					betDetail.setPlayName("第"+firstItem.getPlayType()+"球");
//				}
////				betDetail.setBetName(firstItem.getTitle());
////			betDetail.setOptionTitle(firstItem.getOptionTitle());
////				betDetail.setOptionTitle(optionTitle);
//				
//				betDetail.setGameType(Constants.GAME_TYPE_XY_GDK10);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//			}
//			
//		}else{
//			
//			for (int i = 0; i < list.size(); i++) {
//				GaBetOption betOption = list.get(i);
//				GaBetDetail betDetail=new GaBetDetail();
//				if(betOption!=null){
//					betDetail.setBetRate(betOption.getBetRate());
//				}
//				betDetail.setUserId(user.getUserId());
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(gaK10Session.getSessionId());
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
//				betDetail.setSessionNo(gaK10Session.getSessionNo());
//				betDetail.setGameName("广东快乐十分");
//				if(betOption.getPlayType().equals("0")){//两面盘
//					betDetail.setPlayName("两面盘");
//				}else if(betOption.getPlayType().equals("9")){//连码
//					betDetail.setPlayName("连码");
//				} else{
//					betDetail.setPlayName("第"+betOption.getPlayType()+"球");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_GDK10);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//				
//			}
//		}
//		
//
//		
//
////			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_GDK10, betAll.subtract(tempMoney), null);
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
//		//更新收益
////		BigDecimal userBalance=user.getUserBalance();
////		if(userBalance==null){
////			userBalance=new BigDecimal(0);
////		}
////		user.setUserBalance(userBalance.subtract(betAll));
////		//更新收益
////		BigDecimal dayBet=user.getDayBet();
////		if(dayBet==null){
////			dayBet=new BigDecimal(0);
////		}
////		user.setDayBet(dayBet.add(betAll));
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_GDK10,remark.toString());
//
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//		userService.updateUserBanlance(user.getUserId());
//		
//		gdK10DAO.updateObjectList(betDetailList, null);
//		return user;
//	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "总和/龙虎";
			 }else{
				 return "第"+optionType+"球";
			 }
		 }else if(playType.equals("9")){//连码
			 if(optionType.equals("0")){
				 return "任选二";
			 }else if(optionType.equals("1")){
				 return "任选二组";
			 }else if(optionType.equals("2")){
				 return "任选三";
			 }else if(optionType.equals("3")){
				 return "任选四";
			 }else if(optionType.equals("4")){
				 return "任选五";
			 }else{
				 return "";
			 }
		 }else{//第1 -8 球
			return "第"+playType+"球";
		}
	 }
	 
	public List<GdK10GaTrend> findGdK10GaTrendList(){
		return gdK10DAO.findGdK10GaTrendList();
	}
	public PaginationSupport  findGdK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		return gdK10DAO.findGdK10GaSessionList(hql,para,pageNum,pageSize);
	}
	public PaginationSupport  findGdK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		return gdK10DAO.findGdK10GaBetList(hql,para,pageNum,pageSize);
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
			if(detail.getBetName().equals("总和/龙虎")){
				Map<String,Boolean>  map=getResult(array);
				if(detail.getOptionTitle().equals("总和大")||detail.getOptionTitle().equals("总和小")){
					if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
						return "2";
					}else{
						if(map.get("和")!=null&&map.get("和")){
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
			}else{
				int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
				Map<String,Boolean>  map=getSingleBallResult(array[seq-1]);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}
		}else if(detail.getPlayName().equals("连码")){//先用中文比对吧  后续改进
			int weight = 2;
			// 0:任选 1:二组
			int type = 0;
			
			if(detail.getBetName().equals("任选二")){
				// 任选二
				weight = 2;
			}else if(detail.getBetName().equals("任选二组")){
				// 任选二组
				weight = 2;
				type = 1;
			}else if(detail.getBetName().equals("任选三")){
				// 任选三
				weight = 3;
			}else if(detail.getBetName().equals("任选四")){
				// 任选四
				weight = 4;
			}else {
				// 任选五
				weight = 5;
			}
			int winNum = 0;
			int num = 0;
			String tmpArr[]=detail.getOptionTitle().split(",");
			// 对投注进行比较
			if(type==0){
				// 任选，不需要位置连续
				for(int i = 0;i<tmpArr.length;i++){
					if(tmpArr[i].equals(array[0])
							||tmpArr[i].equals(array[1])
							||tmpArr[i].equals(array[2])
							||tmpArr[i].equals(array[3])
							||tmpArr[i].equals(array[4])
							||tmpArr[i].equals(array[5])
							||tmpArr[i].equals(array[6])
							||tmpArr[i].equals(array[7])){
						num++;
					}
				}
				
				if(num>0){
					if(num<weight){
						winNum = 0;
					}else if(num==weight){
						winNum = 1;
					}else{
						int numerator = 1;
						for(int i = 0;i<weight;i++){
							numerator = numerator * (num-i);
						}
						int denominator = 1;
						for(int i = weight;i>0;i--){
							denominator = denominator * i;
						}
						winNum = numerator / denominator;
					}
				}
			}else{
				List<String> a = new ArrayList<String>();
				
				for(int i = 0;i<tmpArr.length;i++){
					a.add(tmpArr[i]);
				}
				Map<String,Boolean> map = new HashMap<String, Boolean>();
				String result = "";
				// 获取所有投注
				combind(a,weight,map,result);
				Set<String> keys = map.keySet();
				
				// 任选二组，需要两(?)个连续位置，顺序不限
				for(String key:keys){
					num = 0;
					String arr[]=key.split(",");
					for(int i = 0;i<array.length;i++){
						if(arr[0].equals(array[i])){
							if(i==0){
								if(arr[1].equals(array[i+1])){
									num ++;
								}
							}else if(i==array.length-1){
								if(arr[1].equals(array[i-1])){
									num ++;
								}
							}else{
								if(arr[1].equals(array[i-1])||arr[1].equals(array[i+1])){
									num ++;
								}
							}
						}
					}
					winNum += num;
				}
			}
			
			return ""+winNum;
		}else{//第1球~第8球
			int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
			int value=Integer.parseInt(array[seq-1]);//第几球的值
			//下注的具体是几号
			int index=Integer.parseInt(detail.getOptionTitle().replaceAll("号", ""));//把几号的号字去掉只保留数字
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
	 *  总和龙虎 
	 * 
	 */
	public Map<String,Boolean> getResult(String[]  array){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=0;
		for(int i=0;i<array.length;i++){
			sum=sum+Integer.parseInt(array[i]);
		}
		if(sum%2==0){
			map.put("总和双", true);
		}else{
			map.put("总和单", true);
		}
		if(sum>=85&&sum<=132){
			map.put("总和大", true);
		}else if(sum>=36&&sum<=83){
			map.put("总和小", true);
		}else if(sum==84){
			map.put("和", true);
		}
		if(sum>=100){
			int val=sum%100%10;
			if(val>=5){
				map.put("总和尾大", true);
			}else{
				map.put("总和尾小", true);
			}
		}else{
			int val=sum%10;
			if(val>=5){
				map.put("总和尾大", true);
			}else{
				map.put("总和尾小", true);
			}
		}
		if(Integer.parseInt(array[0])>Integer.parseInt(array[7])){
			map.put("龙", true);
		}else{
			map.put("虎", true);
		}
		return map;
	}
	/**
	 *  两面盘 第1-8球的中奖结果
	 * 
	 */
	public Map<String,Boolean> getSingleBallResult(String value){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=Integer.parseInt(value);
		if(sum%2==0){
			map.put("双", true);
		}else{
			map.put("单", true);
		}
		if(sum>=11){
			map.put("大", true);
		}else if(sum<=10){
			map.put("小", true);
		}
		if(sum>=10){
			int val=sum%10;
			if(val>=5){
				map.put("尾大", true);
			}else{
				map.put("尾小", true);
			}
		}else{
			int val=sum;
			if(val>=5){
				map.put("尾大", true);
			}else{
				map.put("尾小", true);
			}
		}
		if(sum==1||sum==5||sum==9||sum==13||sum==17){
			map.put("东", true);
		}else if(sum==2||sum==6||sum==10||sum==14||sum==18){
			map.put("南", true);
		}else if(sum==3||sum==7||sum==11||sum==15||sum==19){
			map.put("西", true);
		}else if(sum==4||sum==8||sum==12||sum==16||sum==20){
			map.put("北", true);
		}
		
		if(sum==1||sum==2||sum==3||sum==4||sum==5||sum==6||sum==7){
			map.put("中", true);
		}else if(sum==8||sum==9||sum==10||sum==11||sum==12||sum==13||sum==14){
			map.put("发", true);
		}else if(sum==15||sum==16||sum==17||sum==18||sum==19||sum==20){
			map.put("白", true);
		}
		
		if(value.length()==2){
			String c1 = value.substring(0, 1);
			String c2 = value.substring(1, 2);
			Integer sumC = Integer.parseInt(c1)+Integer.parseInt(c2);
			if(sumC%2==1){
				map.put("合单", true);
			}else if(sumC%2==0){
				map.put("合双", true);
			}
		}
		return map;
	}

	public void updateTrendResult(GdK10GaSession session) {
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			Map<String,Boolean> resultMap; // 开奖结果
			HQUtils hq = new HQUtils(" from GdK10GaTrend gdk10 ");
			List<Object> trendList = gdK10DAO.findObjects(hq); //上期冷热排行榜
			List<GdK10GaTrend> saveList = new ArrayList<GdK10GaTrend>(); // 本期冷热排行榜

			String results = session.getOpenResult();
			
			if(results != null && results.length() > 0){
				resultMap = this.transResult(results);
			}else{
				return;
			}
			if(trendList != null && trendList.size() > 0 ){
				for(int i = 0; i < trendList.size(); i ++){
					GdK10GaTrend trend = (GdK10GaTrend) trendList.get(i);
					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
						trend.setTrendCount(trend.getTrendCount() + 1);
					}else{
						trend.setTrendCount(0);
					}
					saveList.add(trend);
				}
			}
			gdK10DAO.updateObjectList(saveList, null);
		}
	}
	
//	@Override
//	public void updateGaTrend() {
//		GdK10GaSession gdK10GaSession = gdK10DAO.getCurrentSession(); // 当前期数信息
//		if(gdK10GaSession != null){
//			String curNo = gdK10GaSession.getSessionNo(); // 当前期号
//			String preNo = String.valueOf((Integer.parseInt(curNo) - 1)); //上期期号
//			Map<String,Boolean> resultMap; // 开奖结果
//			
//			HQUtils hq = new HQUtils(" from GdK10GaTrend gdk10 ");
//			List<Object> trendList = gdK10DAO.findObjects(hq); //上期冷热排行榜
//			List<GdK10GaTrend> saveList = new ArrayList<GdK10GaTrend>(); // 本期冷热排行榜
//
//			GdK10GaSession gdK10PreSession = getPreviousSessionBySessionNo(preNo);
//			if(gdK10PreSession==null){
//				gdK10PreSession=(GdK10GaSession) gdK10DAO.getObject(GdK10GaSession.class, gdK10GaSession.getSessionId()-1);
//			}
//			String results = gdK10PreSession.getOpenResult();
//			
//			if(results != null && results.length() > 0){
//				resultMap = this.transResult(results);
//			}else{
//				return;
//			}
//			if(trendList != null && trendList.size() > 0 ){
//				for(int i = 0; i < trendList.size(); i ++){
//					GdK10GaTrend trend = (GdK10GaTrend) trendList.get(i);
//					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
//						trend.setTrendCount(trend.getTrendCount() + 1);
//					}else{
//						trend.setTrendCount(0);
//					}
//					saveList.add(trend);
//				}
//			}
//			gdK10DAO.updateObjectList(saveList, null);
//		}else{
//			log.info("获取当前期数信息失败！");
//		}
//		gdK10GaSession=null;
//	}
	
	/**
	 * 把开奖结果转换为两面盘对应选项。
	 * @return 
	 */
	public Map<String, Boolean> transResult(String results){
		String array[] = results.split(",");
		Map<String, Boolean> result;
		int[] arr; // 存储每一位开奖结果
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 两面盘第1-8球开奖结果

		result = getResult(array); // 总和龙虎
        arr = new int[8];
        for(int i = 0; i < arr.length; i++){
        	arr[i] = Integer.parseInt(array[i]);
        }
        for(int i = 0; i< arr.length; i++){
        	if(arr[i] <= 10){
        		resultMap.put("第" + (i+1) + "球 小", true);
        	}else{
        		resultMap.put("第" + (i+1) + "球 大", true);
        	}
        	if(arr[i] %2 == 0){
        		resultMap.put("第" + (i+1) + "球 双", true);
        	}else{
        		resultMap.put("第" + (i+1) + "球 单", true);
        	}
        	if(arr[i] %10 <5){
        		resultMap.put("第" + (i+1) + "球 尾小", true);
        	}else{
        		resultMap.put("第" + (i+1) + "球 尾大", true);
        	}
        }
        result.putAll(resultMap);
		return result;
		
	}

	@Override
	public String updateTomorrowSession() {
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//明天日期处理 yyyy-MM-dd
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd"); //年月日时间格式
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1); // 获取明天时间

		String today = simpleDateFormat.format(c.getTime());
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTomorrowSessionInit(c.getTime());
		List<GdK10GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<GdK10GaSession>();
			String startTimeStr = today + GdK10Constants.GD_K10_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			for (int i = 0; i < GdK10Constants.GD_K10_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*GdK10Constants.GD_K10_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				//String openResult = GameConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(c.getTime(), i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				GdK10GaSession k10Session = new GdK10GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,GdK10Constants.GD_K10_TIME_INTERVAL));
				k10Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
//				gdK10DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			gdK10DAO.updateObjectList(saveList, null);
			flag = "success";
//			log.info("___[today init completed]__________________________");
		} else {
			flag = "inited";
//			log.info("___[today has been inited]__________________________");
		}
		return flag;
	}
	
	public boolean saveOpenResult(GdK10GaSession session,String openResult){
		String buffer="";
		boolean flag=false;
		if(ParamUtils.chkString(openResult)){
			String array[]=openResult.split(",");
			for(int i=0;i<array.length;i++){
				if(ParamUtils.chkString(array[i].trim())){
					buffer=buffer+array[i].trim()+",";
				}
			}
			buffer=buffer.substring(0, buffer.length()-1);
			session.setOpenResult(openResult);
			gdK10DAO.updateObject(session, null);
			flag=true;
		}
		return flag;
	}
	
	public boolean saveAndOpenResult(GdK10GaSession session,String openResult){
		String buffer="";
		boolean flag=false;
		if(ParamUtils.chkString(openResult)){
			String array[]=openResult.split(",");
			for(int i=0;i<array.length;i++){
				if(ParamUtils.chkString(array[i].trim())){
					buffer=buffer+array[i].trim()+",";
				}
			}
			buffer=buffer.substring(0, buffer.length()-1);
			session.setOpenResult(openResult);		
			boolean flag1 = openGdK10SessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
				gdK10DAO.updateObject(session, null);
				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
				flag=true;
			}else{
				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			}
		}
		return flag;
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return gdK10DAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<GdK10DTO> findGaBetDetailById(String hql, List<Object> para) {
		return gdK10DAO.findGaBetDetailById(hql,para);
	}

	// 排列组合生成投注
	public String combind(List<String> a,int n,Map<String,Boolean> map,String result){
		if(n>0){
			for(int i =0;i<a.size();i++){
				List<String> copyA = new ArrayList<String>();
				String copyResult = "";
				// 拷贝一份
				for(String number:a){
					copyA.add(number);
				}
				String number = a.get(i);
				if(result.length()==0){
					copyResult += number;
				}else{
					copyResult += result +","+number;
				}
				
				
				if(n==1){
					copyResult = popSort(copyResult);
					map.put(copyResult, true);
				}else{
					copyA.remove(i);
					combind(copyA,n-1,map,copyResult);
				}
			}
		}else{
			System.out.println(result);
			return result;
		}
		
		
		return null;
	}
	public String popSort(String nums){
		
		String [] arr = nums.split(",");
		
		for(int i = 0;i<arr.length;i++){
			for(int j=0;j<arr.length-1-i;j++){
				if(Integer.parseInt(arr[j])>Integer.parseInt(arr[j+1])){
					String tmp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = tmp;
				}
			}
		}
		String result = ""+arr[0];
		for(int i = 1;i<arr.length;i++){
			result = result+ ","+ arr[i];
		}
		return result;
	}
	@Override
	public boolean saveRevokePrize(GdK10GaSession session) {
		//删除GdK10GaBet表的记录
		List<Object> para = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" and sessionId = ? ");
		para.add(session.getSessionId());
		gdK10DAO.deleteGdK10GaBet(hql.toString(),para);

		boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_GDK10,session.getSessionNo());
		if(result){
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
}
