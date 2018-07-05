package com.xy.pk10.bjpk10.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ram.model.UserLimit;
import com.ram.service.user.IUserService;
import com.xy.pk10.bjpk10.BjPk10Constants;
import com.xy.pk10.bjpk10.dao.IBjPk10DAO;
import com.xy.pk10.bjpk10.model.BjPk10GaBet;
import com.xy.pk10.bjpk10.model.BjPk10GaSession;
import com.xy.pk10.bjpk10.model.BjPk10GaTrend;
import com.xy.pk10.bjpk10.model.dto.BjPk10DTO;
import com.xy.pk10.bjpk10.service.IBjPk10Service;

public class BjPk10ServiceImpl extends BaseService implements IBjPk10Service {
	private IBjPk10DAO bjPk10DAO;
	private IUserService userService;
	private IGaService gaService;
	public void setBjPk10DAO(IBjPk10DAO bjPk10dao) {
		bjPk10DAO = bjPk10dao;
		super.dao = bjPk10DAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	public String updateInitTodaySession(String sessionNo1) {
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<BjPk10GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<BjPk10GaSession>();
			String startTimeStr = today + BjPk10Constants.BJ_PK10_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			HQUtils hq = new HQUtils("from BjPk10GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(new Date(), 1));
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=bjPk10DAO.findObjects(hq);
			BjPk10GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(BjPk10GaSession) list.get(0);
			}
			
			for (int i = 0; i < BjPk10Constants.BJ_PK10__MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjPk10Constants.BJ_PK10__TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				//String openResult = GameConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,sessionNo1);//期号
				//log.info("___[start today]__________________________sessionNo:"+sessionNo);
				BjPk10GaSession k10Session = new BjPk10GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,BjPk10Constants.BJ_PK10__TIME_INTERVAL));
				k10Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
//				bjPk10DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			bjPk10DAO.updateObjectList(saveList, null);
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
		
		HQUtils hq = new HQUtils("from BjPk10GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = bjPk10DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	public String updateInitSession() {
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
//		Date now = DateTimeUtil.stringToDate("2017-04-17 01:10:00", "yyyy-MM-dd HH:mm:ss");
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(now,1));
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkNextdaySessionInit(now);
		List<BjPk10GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<BjPk10GaSession>();
			String startTimeStr = today + BjPk10Constants.BJ_PK10_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			HQUtils hq = new HQUtils("from BjPk10GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(now);//DateTimeUtil.getDateBefore(new Date(), 1)
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=bjPk10DAO.findObjects(hq);
			BjPk10GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(BjPk10GaSession) list.get(0);
			}
			
			for (int i = 0; i < BjPk10Constants.BJ_PK10__MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjPk10Constants.BJ_PK10__TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				//String openResult = GameConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,"");//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				BjPk10GaSession k10Session = new BjPk10GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,BjPk10Constants.BJ_PK10__TIME_INTERVAL));
				k10Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
//				bjPk10DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			bjPk10DAO.updateObjectList(saveList, null);
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
	public boolean checkNextdaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from BjPk10GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(DateTimeUtil.getDateTimeOfDays(todayStart, 1));
		hq.addPars(DateTimeUtil.getDateTimeOfDays(todayEnd, 1));
		Integer count = bjPk10DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	//-----------------------独立业务方法写在这里----------------------------------------------------
	
	/**
	 * 获取今天的期号，按流水1-50 201523101 - 201523150
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(BjPk10GaSession beforeSession,int index,String sessionNo1){
		if(ParamUtils.chkString(sessionNo1.trim())){
			return  (Integer.parseInt(sessionNo1)+ index)+"";
		}else{
			return  (Integer.parseInt(beforeSession.getSessionNo())+ index)+"";
		}	
	}
	
//	/**
//	 * 开奖服务方法
//	 * @param sessionNoMap 开奖清单
//	 */
//	public void updateFetchService(Map<String, SessionItem> sessionNoMap){
//		//开奖
//		this.updateFetchAndOpenResult(sessionNoMap);
//		
//		//走势
//		
//	}
	
//	public List<BjPk10GaSession> updateFetchAndOpenResult2(Map<String, SessionItem> sessionNoMap){
//		BjPk10GaSession currentSession= bjPk10DAO.getCurrentSession();
//		final String lastSessionNo=((BjPk10GaSession)bjPk10DAO.getObject(BjPk10GaSession.class, currentSession.getSessionId()-1)).getSessionNo();
//		
//		//无开奖清单
//		if(sessionNoMap==null || sessionNoMap.isEmpty()){
//			return null;
//		}
//		//开奖成功场次
//		List<BjPk10GaSession> okList = new ArrayList<BjPk10GaSession>();
//		
//		for(String key:sessionNoMap.keySet()){
//			BjPk10GaSession session = bjPk10DAO.getPreviousSessionBySessionNo(key);
//			if(session!=null){
//				String openStatus1 = session.getOpenStatus();//开奖状态
//				if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//					SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//					String openResult = sessionItem.getResult();
//					session.setOpenResult(openResult);
//					boolean flag = openBjPk10SessionOpenResultMethod(session, session.getOpenResult());
//					if(flag){
//						session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//						session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//						bjPk10DAO.saveObject(session);
//					}else{
//						GameHelpUtil.log(Constants.GAME_TYPE_XY_BJPK10,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//					}
//				}
//			}
//		}
//				
//				GaSessionInfo sessionInfo=gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJPK10);
//				if(sessionInfo!=null){
//					SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//					if(sessionNoMap.get(lastSessionNo)!=null){
//						sessionInfo.setOpenResult(lastItem.getResult());
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(bjPk10DAO.getPreviousSessionBySessionNo(lastSessionNo).getEndTime());
//					}
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					bjPk10DAO.updateObject(sessionInfo, null);
//					CacheUtil.updateGameList();
//				}
//				sessionNoMap.clear();
//				currentSession=null;
//	
//				return null;
//	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		BjPk10GaSession curtSession = bjPk10DAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		BjPk10GaSession lastSession = (BjPk10GaSession)bjPk10DAO.getObject(BjPk10GaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJPK10);
		
		//迭代开奖无序
		List<BjPk10GaSession> openedList = new ArrayList<BjPk10GaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				BjPk10GaSession session = bjPk10DAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openBjPk10SessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							bjPk10DAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJPK10);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				bjPk10DAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(BjPk10GaSession session:openedList){
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
	
//	public void updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap){
//		BjPk10GaSession currentSession= bjPk10DAO.getCurrentSession();
//		String lastSessionNo=((BjPk10GaSession)bjPk10DAO.getObject(BjPk10GaSession.class, currentSession.getSessionId()-1)).getSessionNo();
//		
//		if(!sessionNoMap.isEmpty()){
//			for(String key:sessionNoMap.keySet()){
//				BjPk10GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(key);
//				if(session!=null){
//					String openStatus1 = session.getOpenStatus();//开奖状态
//					if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//						SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//						String openResult = sessionItem.getResult();
//						session.setOpenResult(openResult);
//						boolean flag = openBjPk10SessionOpenResultMethod(session, session.getOpenResult());
//						if(flag){
//							session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//							bjPk10DAO.saveObject(session);
//						}else{
//							GameHelpUtil.log(Constants.GAME_TYPE_XY_BJPK10,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//						}
//					}
//				}
//			}
//			
//			GaSessionInfo sessionInfo=gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJPK10);
//			if(sessionInfo!=null){
//				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//				if(sessionNoMap.get(lastSessionNo)!=null){
//					sessionInfo.setOpenResult(lastItem.getResult());
//					sessionInfo.setOpenSessionNo(lastSessionNo);
//					sessionInfo.setEndTime(bjPk10DAO.getPreviousSessionBySessionNo(lastSessionNo).getEndTime());
//				}
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//				bjPk10DAO.updateObject(sessionInfo, null);
//				CacheUtil.updateGameList();
//			}
//			sessionNoMap.clear();
//			currentSession=null;
//		}
//	}

//	public void updateFetchAndOpenResult() {
//		BjPk10GaSession currentSession= bjPk10DAO.getCurrentSession();
//		final String lastSessionNo=(Integer.parseInt(currentSession.getSessionNo())-1)+"";
//		final Map<String,String> sessionNoMap=new HashMap<String,String>();
//		final Map<String,String> timeMap=new HashMap<String,String>();
//		Thread t=new Thread(){
//            public void run(){
//            	Integer countRun=0;
//               try {      	   
//            	   while(true){
//            		   if(countRun==95){
//            			   interrupt();
//            			   countRun=null;
//            			   break;
//            		   }
//            		    countRun=countRun+1;
//
//            			//从这里 ---------------------------------------------------------------------------
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xybjpk10");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    sessionInfo=null;
////	               		log.info("___[bjpk10 start fetch result xml data...]________________"+DateTimeUtil.DateToStringAll(new Date()));
//	
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[bjpk10 start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/bjpk10.txt", true);
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
//	               		//到这里 ---------------------------------------------------------------------------		
//            		    sleep(3000);
//            		    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/bjpk10.txt", true);
////	               		log.info("___[fetch result xml data]"+resultXML);
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果5组数字
//	               			String time="";	      
//	               			
//	               			if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//		               			//开始解析场次开奖数据
//		               			NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
//		               				time = XmlUtil.getElementAttribute((Element)node, "opentime");
//	           						if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							interrupt();
//	           							countRun=null;
//	           						}
//		               			}
//	               			}else if(urlSwitch.equals("2")){//1=开彩网  2=彩票控
//	               				NodeList nList = xmlDoc.getElementsByTagName("item");
//	                   			for(int i =0;i<nList.getLength();i++){
//	                   				Node node = nList.item(i);
//	                  				sessionNo = XmlUtil.getElementAttribute((Element)node, "id");
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
//	               			}
//
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
//				for(String key:sessionNoMap.keySet()){
//					BjPk10GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(key);
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//							session.setOpenResult(sessionNoMap.get(key));
//							boolean flag = openBjPk10SessionOpenResultMethod(session, session.getOpenResult());
//							if(flag){
////								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//								bjPk10DAO.saveObject(session);
//								log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//							}else{
//								log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//							}
//						}
//					}
//				}
//				
//					GaSessionInfo sessionInfo=gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJPK10);
//					if(sessionInfo!=null){
//						sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//						if(sessionNoMap.get(lastSessionNo)!=null){
//							sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));
//							sessionInfo.setOpenSessionNo(lastSessionNo);
//							sessionInfo.setEndTime(bjPk10DAO.getPreviousSessionBySessionNo(lastSessionNo).getEndTime());
//						}
//						bjPk10DAO.updateObject(sessionInfo, null);
//						CacheUtil.updateGameList();
//					}
//					sessionNoMap.clear();
//					timeMap.clear();
//			}
////			currentSession=null;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		currentSession=null;
//	}
	
	public void updateTrendResult(BjPk10GaSession session){
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			List<BjPk10GaTrend> list=bjPk10DAO.findBjPk10GaTrendAllList();
			List<BjPk10GaTrend> savelist=new ArrayList<BjPk10GaTrend>();
			Map<String,Boolean> map=getTrendResult(session.getOpenResult());
			if(!map.isEmpty()){
				for(BjPk10GaTrend trend:list){
					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
						trend.setTrendCount(trend.getTrendCount()+1);
					}else{
						trend.setTrendCount(0);
					}
					savelist.add(trend);
				}
				bjPk10DAO.updateObjectList(savelist, null);
			}
		}
	}
	
//	public void updateFetchAndOpenTrendResult(Integer count){
//		if(count==9){//执行10次  还没有结果退出递归
//			count=null;
//			return;
//		}
//		BjPk10GaSession tempSession =bjPk10DAO.getCurrentSession();
//		String lastSessionNo=(Integer.parseInt(tempSession.getSessionNo())-1)+"";
//		BjPk10GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(lastSessionNo);
//		if(session==null){
//			session=(BjPk10GaSession) bjPk10DAO.getObject(BjPk10GaSession.class, tempSession.getSessionId()-1);
//		}
//		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
//			List<BjPk10GaTrend> list=bjPk10DAO.findBjPk10GaTrendAllList();
//			List<BjPk10GaTrend> savelist=new ArrayList<BjPk10GaTrend>();
//			Map<String,Boolean> map=getTrendResult(session.getOpenResult());
//			if(!map.isEmpty()){
//				for(int i=0;i<list.size();i++){
//					BjPk10GaTrend trend=list.get(i);
//					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
//						trend.setTrendCount(trend.getTrendCount()+1);
//					}else{
//						trend.setTrendCount(0);
//					}
//					savelist.add(trend);
//				}
//				bjPk10DAO.updateObjectList(savelist, null);
//				map.clear();
//				savelist=null;
//				list=null;
//				lastSessionNo=null;
//				session=null;
//				count=null;		
//				tempSession=null;
//				return;
//			}
//			count=null;	
//		}else{
//			Thread t1=new Thread(){
//	            public void run(){
//	               try {
//	            	   sleep(3000);
//	            	   interrupt();
//	               } catch (Exception e) {
//	            	   interrupt();
//	               }
//	            }
//			};
//			t1.start();
//			try {
//				t1.join();
//				t1=null;
//				count++;
//				lastSessionNo=null;
//				session=null;
//				tempSession=null;
//				updateFetchAndOpenTrendResult(count);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param sessionNo
	 * @param result 开奖号10组数字 英文逗号连接
	 * @return
	 */
	public boolean openBjPk10SessionOpenResultMethod(BjPk10GaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJPK10);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_BJPK10);
			hql.append( " and ho.sessionId =?");
			para.add(session.getSessionId());
			hql.append(" and ho.betFlag=?" );//目前固定参数
			para.add("1");
			//hql.append(" order by ho.betTime desc");
			
			//本期全部投注记录
			long startTiming = System.currentTimeMillis();
			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
			GameHelpUtil.log(gameCode,"BETS ... ["+list.size()+"]["+session.getSessionNo()+"]["+(System.currentTimeMillis()-startTiming)+"ms]");
			//本期投注统计表
			BjPk10GaBet bet=new BjPk10GaBet();
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
					
					boolean flag=judgeWin(result,detail);//判断中奖
					detail.setOpenResult(result);
					if(flag){//中奖
						detail.setWinResult(GameConstants.WIN);
						//中奖金额
						BigDecimal wincash=GameHelpUtil.round(detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())));
						detail.setWinCash(wincash);
						//统计
						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
						betCash=betCash.add(wincash);
						//盈亏
						detail.setPayoff(GameHelpUtil.round(wincash.subtract(new BigDecimal(detail.getBetMoney()))));
						//备注
						String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
						
						//更新
						updateOpenData(detail,remark.toString());
						
					}else{//不中
						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
						detail.setWinCash(new BigDecimal(0));
						detail.setWinResult(GameConstants.WIN_NOT);//未中奖
						bjPk10DAO.saveObject(detail);
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
						Constants.GAME_TYPE_XY_BJPK10, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			bjPk10DAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}	
	}
//	public boolean openBjPk10SessionOpenResultMethod(BjPk10GaSession session,String result){
//		try{
////			BjPk10GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(sessionNo);
//			List<Object> para = new ArrayList<Object>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" and ho.gameType=?  ");// 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			para.add(Constants.GAME_TYPE_XY_BJPK10);
//			hql.append( " and ho.sessionId =? ");
//			para.add(session.getSessionId());
//			hql.append(" and ho.betFlag=? " );
//			para.add("1");
//			hql.append(" order by ho.betTime desc");
//			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
//			
//			BjPk10GaBet bet=new BjPk10GaBet();
//			BigDecimal  totalPoint=new BigDecimal(0);
//			BigDecimal  betCash=new BigDecimal(0);
//			bet.setSessionId(session.getSessionId());
//			bet.setSessionNo(session.getSessionNo());
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
//					boolean flag=judgeWin(result,detail);
//					StringBuffer remark=new StringBuffer();
//					detail.setOpenResult(result);
//					if(flag){//中奖
//					
//						detail.setWinResult(BjPk10Constants.BJ_PK10_WIN);//中奖
//						BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//						detail.setWinCash(wincash);
//						
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//					
//						detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////						bjPk10DAO.updateObject(user, null);
////						userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_BJPK10, wincash, detail.getBetDetailId());		
//						
//						remark.append("彩票中奖 奖金 ")
//						    .append(wincash).append("元");
//						
////						User user=(User) bjPk10DAO.getObject(User.class, detail.getUserId());
////						BigDecimal userBal=null;
////						if(user.getUserBalance()!=null){
////							userBal=user.getUserBalance();
////						}else{
////							userBal=new BigDecimal(0);
////						}
////						user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
////				
//						try {
//							updateOpenDate(detail,null,remark.toString());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//					}else{
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						detail.setWinCash(new BigDecimal(0));
//						detail.setWinResult(BjPk10Constants.BJ_PK10_WIN_NOT);//未中奖
//						try {
//							bjPk10DAO.updateObject(detail, null);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}	
//					}
//				}
//				//批量更新开奖用户实时余额 --by.cuisy.20171209
//				userService.updateUserMoney(userIds);
//			}
//			bet.setTotalPoint(totalPoint);
//			bet.setWinCash(betCash);
//			try {
//				bjPk10DAO.saveObject(bet, null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}	
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}	
//	}
	
	public void updateOpenData(GaBetDetail detail,String remark){
		bjPk10DAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_BJPK10,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
//	public void updateOpenDate(GaBetDetail detail,User user,String remark){
//		bjPk10DAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJPK10,remark);
//	}
	
	
	public BjPk10GaSession getCurrentSession(){
		return bjPk10DAO.getCurrentSession();
	}
	public BjPk10GaSession getPreviousSessionBySessionNo(String sessionNo){
		return bjPk10DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,BjPk10GaSession session,User user,BigDecimal betAll){
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJPK10);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		//迭代投注选项
		for(GaBetOption betOption:list){
			GaBetDetail betDetail=new GaBetDetail();
			betDetail.setBetRate(ParamUtils.chkBigDecimalNotNull(betOption.getBetRate()));
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			betDetail.setSessionNo(session.getSessionNo());
			betDetail.setUserId(user.getUserId());
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			betDetail.setBetOptionId(betOption.getBetOptionId());//投注项
			betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));//投注金额
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			betDetail.setRoom(room);//房间目前固定A
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());
			betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
			betDetail.setOptionTitle(betOption.getOptionTitle());
			betDetail.setPlayName(BjPk10Constants.getPlayTypeName(betOption.getPlayType()));
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			bjPk10DAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_BJPK10,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		bjPk10DAO.updateObjectList(rlList, null);
		
		return user;
	}
//	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,BjPk10GaSession gaK10Session,User user,BigDecimal betAll){
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//
//		BigDecimal paperMoney=user.getUserScore();
//		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
//		BigDecimal tempMoney=new BigDecimal(0);
//		
//		for (int i = 0; i < list.size(); i++) {
//				GaBetOption betOption = list.get(i);
//				GaBetDetail betDetail=new GaBetDetail();
//				if(betOption!=null){
//					betDetail.setBetRate(betOption.getBetRate());
//				}
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(gaK10Session.getSessionId());
//				
//				betDetail.setUserId(user.getUserId());
//				
//				betDetail.setBetOptionId(betOption.getBetOptionId());
//				betDetail.setBetTime(new Date());
//				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
//				
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
//				betDetail.setRoom(room);
//				betDetail.setSessionNo(gaK10Session.getSessionNo());
//				betDetail.setGameName("北京赛车 PK10");
//				if(betOption.getPlayType().equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_TWO_FACE)){
//					betDetail.setPlayName("两面盘");
//				}else if(betOption.getPlayType().equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_ONE_TO_TEN)){
//					betDetail.setPlayName("1-10名");
//				}else if(betOption.getPlayType().equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_SUM)){
//					betDetail.setPlayName("冠亚军和");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_BJPK10);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//		}
//		
////		BigDecimal userBalance=user.getUserBalance();
////		if(userBalance==null){
////			userBalance=new BigDecimal(0);
////		}
////		user.setUserBalance(userBalance.subtract(betAll));
////		BigDecimal dayBet=user.getDayBet();
////		if(dayBet==null){
////			dayBet=new BigDecimal(0);
////		}
////		user.setDayBet(dayBet.add(betAll));
//		
//		
//
////			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPK10, betAll.subtract(tempMoney), null);
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
//		userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_BJPK10,remark.toString());
//
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//
//		userService.updateUserBanlance(user.getUserId());
////		userService.saveTradeDetail(user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPK10, betAll, null);
//		bjPk10DAO.updateObjectList(betDetailList, null);
//		return user;
//	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_TWO_FACE)){//两面盘
			 if(optionType.equals("0")){
				 return "冠军";
			 }else if(optionType.equals("1")){
				 return "亚军";
			 }else{
				 return "第"+(Integer.parseInt(optionType)+1)+"名";
			 }
		 }else if(playType.equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_ONE_TO_TEN)){//1-10名
			 if(optionType.equals("0")){
				 return "冠军";
			 }else if(optionType.equals("1")){
				 return "亚军";
			 }else{
				 return "第"+(Integer.parseInt(optionType)+1)+"名";
			 }
		}else if(playType.equals(BjPk10Constants.BJ_PK10_PLAY_TYPE_SUM)){//冠亚军和
			return "冠亚军和";
		}
		 return "";
	 }
	 
	public List<BjPk10GaTrend> findBjPk10GaTrendList(){
		return bjPk10DAO.findBjPk10GaTrendList();
	}
	public PaginationSupport  findBjPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		return bjPk10DAO.findBjPk10GaSessionList(hql,para,pageNum,pageSize);
	}

	public PaginationSupport  findBjPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		return bjPk10DAO.findBjPk10GaBetList(hql,para,pageNum,pageSize);
	}
	
	/**
	 * 判断用户是否中奖
	 * @param results
	 * @param detail
	 * @return
	 */
	public boolean judgeWin(String results,GaBetDetail detail){
		String array[]=results.split(",");//拆分结果
		if(detail.getPlayName().equals("两面盘")){//先用中文比对吧  后续改进
			if(detail.getBetName().equals("冠军")){
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[0]),Integer.parseInt(array[9]));
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false;
				}
			}else if(detail.getBetName().equals("亚军")){
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[1]),Integer.parseInt(array[8]));
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false;
				}
			}else if(detail.getBetName().equals("第3名")){
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[2]),Integer.parseInt(array[7]));
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false;
				}
			}else if(detail.getBetName().equals("第4名")){
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[3]),Integer.parseInt(array[6]));
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false;
				}
			}else if(detail.getBetName().equals("第5名")){
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[4]),Integer.parseInt(array[5]));
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false; 
				}
			}else{
				int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));//第几名 取中间的那个数字几
				Map<String,Boolean>  map=getResult(Integer.parseInt(array[seq-1]),0);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return true;
				}else{
					return false; 
				}
			}
		}else if(detail.getPlayName().equals("1-10名")){
			//下注的具体是几号
			int index=Integer.parseInt(detail.getOptionTitle().replaceAll("号", ""));//把几号的号字去掉只保留数字
			if(detail.getBetName().equals("冠军")){
				if(Integer.parseInt(array[0])==index){
					return true;
				}else{
					return false;
				}
			}else if(detail.getBetName().equals("亚军")){
				if(Integer.parseInt(array[1])==index){
					return true;
				}else{
					return false;
				}
			}else{
				int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));//第几名 取中间的那个数字几
				if(Integer.parseInt(array[seq-1])==index){
					return true;
				}else{
					return false;
				}
			}
		}else if(detail.getPlayName().equals("冠亚军和")){
			int sum=Integer.parseInt(array[0])+Integer.parseInt(array[1]);
			Map<String,Boolean>  map=getResult(sum);
			if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
				return true;
			}else{
				return false;
			}
		}		
		return  false;
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
	 *  冠亚军和 根据传递的数字判断是否中奖  
	 * secondNum可传0，主要是判断1-5龙虎用的这个
	 */
	public Map<String,Boolean> getResult(int sum){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		if(sum%2==0){
			map.put("双", true);
		}else{
			map.put("单", true);
		}
		if(sum>11){
			map.put("大", true);
		}else{
			map.put("小", true);
		}
		map.put(sum+"", true);
		return map;
	}
	public Map<String,Boolean> getTrendResult(String results){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		if(ParamUtils.chkString(results)){
			String array[]=results.split(",");
			for(int i=0;i<array.length;i++){
				if(Integer.parseInt(array[i])%2==0){
					if(i==0){
						map.put("冠军 双", true);
					}else if(i==1){
						map.put("亚军 双", true);
					}else{
						map.put("第"+(i+1)+"名 双", true);
					}
				}else{
					if(i==0){
						map.put("冠军 单", true);
					}else if(i==1){
						map.put("亚军 单", true);
					}else{
						map.put("第"+(i+1)+"名 单", true);
					}
				}
				
				if(Integer.parseInt(array[i])>=6){
					if(i==0){
						map.put("冠军 大", true);
					}else if(i==1){
						map.put("亚军 大", true);
					}else{
						map.put("第"+(i+1)+"名 大", true);
					}
				}else{
					if(i==0){
						map.put("冠军 小", true);
					}else if(i==1){
						map.put("亚军 小", true);
					}else{
						map.put("第"+(i+1)+"名 小", true);
					}
				}			
			}
			
			if(Integer.parseInt(array[0])>Integer.parseInt(array[9])){
				map.put("冠军 龙", true);
			}else{
				map.put("冠军 虎", true);
			}
			if(Integer.parseInt(array[1])>Integer.parseInt(array[8])){
				map.put("亚军 龙", true);
			}else{
				map.put("亚军 虎", true);
			}
			if(Integer.parseInt(array[2])>Integer.parseInt(array[7])){
				map.put("第3名 龙", true);
			}else{
				map.put("第3名 虎", true);
			}
			if(Integer.parseInt(array[3])>Integer.parseInt(array[6])){
				map.put("第4名 龙", true);
			}else{
				map.put("第4名 虎", true);
			}
			if(Integer.parseInt(array[4])>Integer.parseInt(array[5])){
				map.put("第5名 龙", true);
			}else{
				map.put("第5名 虎", true);
			}
		}
		return map;
	}
	public boolean saveOpenResult(BjPk10GaSession session,String openResult){
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
			bjPk10DAO.updateObject(session, null);
			flag=true;
		}
		return flag;
		
	}
	
	public boolean saveAndOpenResult(BjPk10GaSession session,String openResult){
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
			boolean flag1 = openBjPk10SessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
				bjPk10DAO.updateObject(session, null);
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
		return bjPk10DAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<BjPk10DTO> findGaBetDetailById(String hql, List<Object> para) {
		return bjPk10DAO.findGaBetDetailById(hql, para);
	}

	@Override
	public boolean saveRevokePrize(BjPk10GaSession session) {
		//删除BjPk10GaBet表的记录
		List<Object> para = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" and sessionId = ? ");
		para.add(session.getSessionId());
		bjPk10DAO.deleteBjPk10GaBet(hql.toString(),para);

		boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_BJPK10,session.getSessionNo());
		if(result){
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
	
}
