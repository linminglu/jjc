package com.xy.pick11.jxpick11.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.ram.service.user.IUserService;
import com.xy.pick11.jxpick11.JxPick11Constants;
import com.xy.pick11.jxpick11.dao.IJxPick11DAO;
import com.xy.pick11.jxpick11.model.JxPick11GaBet;
import com.xy.pick11.jxpick11.model.JxPick11GaSession;
import com.xy.pick11.jxpick11.model.JxPick11GaTrend;
import com.xy.pick11.jxpick11.model.dto.JxPick11DTO;
import com.xy.pick11.jxpick11.service.IJxPick11Service;

public class JxPick11ServiceImpl extends BaseService implements IJxPick11Service {
	private IJxPick11DAO jxPick11DAO;
	private IUserService userService;
	private IGaService gaService;
	public void setJxPick11DAO(IJxPick11DAO jxPick11dao) {
		jxPick11DAO = jxPick11dao;
		super.dao = jxPick11DAO;
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
		List<JxPick11GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<JxPick11GaSession>();
			String startTimeStr = today + JxPick11Constants.JX_PICK11_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			for (int i = 0; i < JxPick11Constants.JX_PICK11_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*JxPick11Constants.JX_PICK11_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				String sessionNo = this.getTodaySessionNo(now, i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				JxPick11GaSession k10Session = new JxPick11GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,JxPick11Constants.JX_PICK11_TIME_INTERVAL));
				k10Session.setOpenStatus(JxPick11Constants.JX_PICK11_OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
//				jxPick11DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			jxPick11DAO.updateObjectList(saveList, null);
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
		
		HQUtils hq = new HQUtils("from JxPick11GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = jxPick11DAO.countObjects(hq);
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
		
		HQUtils hq = new HQUtils("from JxPick11GaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = jxPick11DAO.countObjects(hq);
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
		return DateTimeUtil.getYyMMddStr(date) + String.format("%02d", index);
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		JxPick11GaSession curtSession = jxPick11DAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		JxPick11GaSession lastSession = (JxPick11GaSession)jxPick11DAO.getObject(JxPick11GaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JXPICK11);
		
		//迭代开奖无序
		List<JxPick11GaSession> openedList = new ArrayList<JxPick11GaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				JxPick11GaSession session = jxPick11DAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openJxPick11SessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							jxPick11DAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_JXPICK11);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				jxPick11DAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(JxPick11GaSession session:openedList){
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
	
//	public void updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
//		JxPick11GaSession currentSession=jxPick11DAO.getCurrentSession();
//		final String lastSessionNo=(Integer.parseInt(currentSession.getSessionNo())-1)+"";
//		
//		if(!sessionNoMap.isEmpty()){
//			for(String key:sessionNoMap.keySet()){
//				JxPick11GaSession session =jxPick11DAO.getPreviousSessionBySessionNo(key);
//				if(session!=null){
//					String openStatus1 = session.getOpenStatus();//开奖状态
//					if(openStatus1.equals(JxPick11Constants.JX_PICK11_OPEN_STATUS_INIT) || openStatus1.equals(JxPick11Constants.JX_PICK11_OPEN_STATUS_OPENING)){
//						//更新开奖结果
//						SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//						String openResult = sessionItem.getResult();
//						session.setOpenResult(openResult);
//						boolean flag = openJxPick11SessionOpenResultMethod(session, session.getOpenResult());
//						if(flag){
//							session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//							session.setOpenStatus(JxPick11Constants.JX_PICK11_OPEN_STATUS_OPENED);
//							jxPick11DAO.saveObject(session);
//						}else{
//							GameHelpUtil.log(Constants.GAME_TYPE_XY_JXPICK11,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//						}
//					}
//					// 把资金明细里投注记录状态值改为有效
//					userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//							Constants.GAME_TYPE_XY_JXPICK11, Constants.PUB_STATUS_OPEN);
//				}
//			}
//			
//			GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_JXPICK11);
//			if(sessionInfo != null){
//				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//				if(lastItem!=null){
//					sessionInfo.setOpenSessionNo(lastSessionNo);
//					sessionInfo.setOpenResult(getCountResult(lastItem.getResult()));
//					sessionInfo.setEndTime(DateTimeUtil.StringToDate(lastItem.getTime(),"yyyy-MM-dd HH:mm:ss"));							
//				}
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());//当前期
//				jxPick11DAO.saveObject(sessionInfo);
//				CacheUtil.updateGameList();
//			}			
//			sessionNoMap.clear();
//			currentSession=null;
//		}
//	}
	
//	@Override
//	public void updateFetchAndOpenResult() {
//		JxPick11GaSession currentSession=jxPick11DAO.getCurrentSession();
//		JxPick11GaSession tempsession =jxPick11DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(JxPick11GaSession) jxPick11DAO.getObject(JxPick11GaSession.class, (currentSession.getSessionId()-1));
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
//            			//从这里 ---------------------------------------------------------------------------
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xyjxpick11");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    sessionInfo=null;
//
//	               		log.info("___[jxpick11 start fetch result xml data...]________________---------jxpick11______"+officalURL);
////	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[jxpick11 start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/jxpick11.txt", true);
//	    	            
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
////	               		log.info(resultXML);
//	               		//到这里 ---------------------------------------------------------------------------		
//	               	    sleep(3000);
////	               		log.info("___[fetch result xml data]"+resultXML);
////	            	    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/jxpick11.txt", true);
//
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果5组数字
//	               			String time="";
//	               			if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//	               			//开始解析场次开奖数据
//		               			NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				sessionNo=sessionNo.substring(sessionNo.length()-8);//2017041225 去掉前两位
////		               				sessionNo=sessionNo.substring(0,6)+sessionNo.substring(7,9);
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
//		               				time=XmlUtil.getElementAttribute((Element)node, "opentime");
//		               				if(i==0){
//		               					sessionNoMap.put("lastNo", sessionNo);
//		               				}
//
//	           						if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
//	           							countRun=null;
//	           							interrupt();
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
//	               		}else{
//	               			interrupt();
//	               			countRun=null;
//       						break;
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
//					JxPick11GaSession session =jxPick11DAO.getPreviousSessionBySessionNo(key);
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(JxPick11Constants.JX_PICK11_OPEN_STATUS_INIT) || openStatus1.equals(JxPick11Constants.JX_PICK11_OPEN_STATUS_OPENING)){
//							session.setOpenResult(sessionNoMap.get(key));
//							boolean flag = openJxPick11SessionOpenResultMethod(session, session.getOpenResult());
//							if(flag){
////								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(JxPick11Constants.JX_PICK11_OPEN_STATUS_OPENED);
//								jxPick11DAO.saveObject(session);
//								log.info("___[jxpick11][open result success sessionNO["+session.getSessionNo()+"]]");
//							}else{
//								log.info("___[jxpick11][open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//							}
//						}
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_JXPICK11);
//				if(sessionInfo != null){
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					log.info(DateTimeUtil.DateToStringAll(new Date())+" openResult>>>>>>>>>>:"+sessionNoMap.get(lastSessionNo));
//					if(sessionNoMap.get(lastSessionNo)!=null){
//						sessionInfo.setOpenResult(getCountResult(sessionNoMap.get(lastSessionNo)));
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(DateTimeUtil.StringToDate(timeMap.get(lastSessionNo),"yyyy-MM-dd HH:mm:ss"));							
//					}else{
//						String lastNo = sessionNoMap.get("lastNo");
//						if(ParamUtils.chkString(lastNo)){
//							sessionInfo.setOpenResult(getCountResult(sessionNoMap.get(lastNo)));
//							sessionInfo.setOpenSessionNo(lastNo);
//							sessionInfo.setEndTime(DateTimeUtil.StringToDate(timeMap.get(lastNo),"yyyy-MM-dd HH:mm:ss"));							
//						}
//					}
//					jxPick11DAO.updateObject(sessionInfo, null);
//					CacheUtil.updateGameList();
//				}			
//				sessionNoMap.clear();
//				timeMap.clear();
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
	public boolean openJxPick11SessionOpenResultMethod(JxPick11GaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JXPICK11);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_JXPICK11);
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
			JxPick11GaBet bet=new JxPick11GaBet();
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
					
					String flag = judgeWin(result,detail);//判断中奖 //0未中奖  1打和  2中奖
					detail.setOpenResult(result);
					
					if(flag.equals("2")){//中奖//中奖
						detail.setWinResult(GameConstants.WIN);
						//中奖金额
						BigDecimal wincash=GameHelpUtil.round(detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())));
						detail.setWinCash(wincash);
						//统计
						totalPoint = totalPoint.add(new BigDecimal(detail.getBetMoney()));
						betCash = betCash.add(wincash);
						//盈亏
						detail.setPayoff(GameHelpUtil.round(wincash.subtract(new BigDecimal(detail.getBetMoney()))));
						//备注
						String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
						//更新
						updateOpenData(detail,remark);
						
					}else if(flag.equals("1")){//打和
						detail.setWinResult(GameConstants.WIN_HE);
						//中奖金额
						BigDecimal wincash = GameHelpUtil.round(new BigDecimal(detail.getBetMoney()));//投注总钱数
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
						jxPick11DAO.saveObject(detail);
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
						Constants.GAME_TYPE_XY_JXPICK11, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			jxPick11DAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}	
	}
	
//	public boolean openJxPick11SessionOpenResultMethod(JxPick11GaSession session,String result){
//		log.info("___[jxpick11][open stat***************** sessionNO["+session.getSessionNo()+"]]");
//		try{
////			JxPick11GaSession session =bjPk10DAO.getPreviousSessionBySessionNo(sessionNo);
//			List<Object> para = new ArrayList<Object>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" and ho.gameType=? ");// 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			para.add(Constants.GAME_TYPE_XY_JXPICK11);
//			hql.append(" and ho.sessionId=? ");
//			para.add(session.getSessionId());
//			hql.append(" and ho.betFlag=? " );
//			para.add("1");
//			hql.append(" order by ho.betTime desc");
//			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
//			log.info("[jxPick11开奖记录=]___________________"+list.size());
//			JxPick11GaBet bet=new JxPick11GaBet();
//			BigDecimal  totalPoint=new BigDecimal(0);
//			BigDecimal  betCash=new BigDecimal(0);
//			bet.setSessionId(session.getSessionId());
//			bet.setSessionNo(session.getSessionNo());
//			bet.setBetTime(session.getEndTime());
//			if(list!=null&&list.size()>0){
//				
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
//					String openResult = "";
//					if(ParamUtils.chkString(session.getOpenResult())){
//						openResult = this.getCountResult(session.getOpenResult());
//					}
//					detail.setOpenResult(openResult);
//					StringBuffer remark=new StringBuffer();
//					String flag=judgeWin(result,detail);//0未中奖  1打和  2中奖
//					if(flag.equals("2")){//中奖
//						
//						detail.setWinResult(JxPick11Constants.JX_PICK11_WIN);//中奖
//						BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//						detail.setWinCash(wincash);
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//	
//						detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
//					
//						remark.append("彩票中奖 奖金 ").append(wincash).append("元");
//						
////						user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//						try {
//							updateOpenData(detail,null,remark.toString());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//					}else if(flag.equals("1")){//打和
//					
//						detail.setWinResult(JxPick11Constants.JX_PICK11_WIN_HE);//打和
//						BigDecimal wincash=new BigDecimal(detail.getBetMoney());//投注总钱数
//
//						detail.setWinCash(wincash);
////							userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_JXPICK11, wincash, detail.getBetDetailId());				
//
//						remark.append("彩票打和 退款")
//						    .append(wincash).append("元");
////							user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_JXPICK11,remark.toString());
//
//						try {
//							updateOpenData(detail,null,remark.toString());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//					}else{//未中奖
//						detail.setWinCash(new BigDecimal(0));
//						detail.setWinResult(JxPick11Constants.JX_PICK11_WIN_NOT);//未中奖
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						try {
//							jxPick11DAO.updateObject(detail, null);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				
//				//批量更新开奖用户实时余额 --by.cuisy.20171209
//				userService.updateUserMoney(userIds);
//			}
//			
//			bet.setTotalPoint(totalPoint);
//			bet.setWinCash(betCash);
//			try {
//				jxPick11DAO.saveObject(bet, null);
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
		jxPick11DAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_JXPICK11,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		jxPick11DAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_JXPICK11,remark);
//		
//	}
	
//	public void updateOpenData(GaBetDetail detail,User user){
//		
//		jxPick11DAO.saveObject(detail);
//		jxPick11DAO.saveObject(user);
////		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_JXPICK11,remark);
//		
//	}
	
	
	public JxPick11GaSession getCurrentSession(){
		return jxPick11DAO.getCurrentSession();
	}
	public JxPick11GaSession getPreviousSessionBySessionNo(String sessionNo){
		return jxPick11DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public void saveUserBetInfo(String room,Map<String,Integer> betMap,List<GaBetOption> list,JxPick11GaSession session,User user,BigDecimal betAll){
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_JXPICK11);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		String id;//optionId
		Integer p;//money
		String optionTitle;
		GaBetOption betOption = null;
		for (Map.Entry<String, Integer> entry : betMap.entrySet()) { 
			//初始化
			id = entry.getKey();
			p = entry.getValue();
			optionTitle = "";
			
			List<GaBetOption> optionList = gaService.getGaBetOptionByIds(id);//此注的所有投注号对象
			for (GaBetOption gbo : optionList) {
				optionTitle = optionTitle + gbo.getOptionTitle()+",";
			}
			optionTitle = optionTitle.substring(0, optionTitle.length()-1);
			betOption = optionList.get(0);
			
			GaBetDetail betDetail = new GaBetDetail();
			betDetail.setBetRate(betOption.getBetRate());
			betDetail.setUserId(user.getUserId());
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			betDetail.setSessionId(session.getSessionId());
			
			betDetail.setBetOptionId(betOption.getBetOptionId());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			betDetail.setBetMoney(p);
			
			betDetail.setUserId(user.getUserId());
			betDetail.setLoginName(user.getLoginName());
			betDetail.setType(userType);
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());
			betDetail.setGameName(gsi.getGameTitle());
			
			if(betOption.getPlayType().equals("0")){//两面盘
				betDetail.setPlayName("两面盘");
			}else if(betOption.getPlayType().equals("1")){//1-5球
				betDetail.setPlayName("1-5球");
			}else if(betOption.getPlayType().equals("2")){//任选
				betDetail.setPlayName("任选");
			}else if(betOption.getPlayType().equals("3")){//组选
				betDetail.setPlayName("组选");
			}else if(betOption.getPlayType().equals("4")){//直选
				betDetail.setPlayName("直选");
			}
			betDetail.setBetName(betOption.getTitle());
			betDetail.setOptionTitle(optionTitle);
			betDetail.setGameType(Constants.GAME_TYPE_XY_JXPICK11);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			jxPick11DAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_JXPICK11,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());
		
		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		jxPick11DAO.updateObjectList(rlList, null);
	}
	
//	public void saveUserBetInfo(String room,Map<String,Integer> betMap,List<GaBetOption> list,JxPick11GaSession gaPick11Session,User user,BigDecimal betAll){
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//		BigDecimal paperMoney=user.getUserScore();
//		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
//		BigDecimal tempMoney=new BigDecimal(0);
//		
//		for (Map.Entry<String, Integer> entry : betMap.entrySet()) { 
//			String id = entry.getKey();//有可能是用逗号隔开的多个
//			Integer p = entry.getValue();// 投注金额
//			String optionTitle = "";
//			List<GaBetOption> optionList = gaService.getGaBetOptionByIds(id);//此注的所有投注号对象
//			for (GaBetOption gbo : optionList) {
//				optionTitle = optionTitle + gbo.getOptionTitle()+",";
//			}
//			optionTitle = optionTitle.substring(0, optionTitle.length()-1);
//			GaBetOption betOption = optionList.get(0);
//			GaBetDetail betDetail=new GaBetDetail();
//			if(betOption!=null){
//				betDetail.setBetRate(betOption.getBetRate());
//			}
//			betDetail.setUserId(user.getUserId());
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(gaPick11Session.getSessionId());
//			
//			betDetail.setBetOptionId(betOption.getBetOptionId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(p);
//			if(paperMoney.compareTo(new BigDecimal(0))==1){
//				if(new BigDecimal(p).compareTo(paperMoney)!=-1){
//					betDetail.setPaperMoney(paperMoney);
//					paperMoney=paperMoney.subtract(paperMoney);
//					tempMoney=tempMoney.add(new BigDecimal(p));
//				}else{
//					betDetail.setPaperMoney(new BigDecimal(p));
//					paperMoney=paperMoney.subtract(new BigDecimal(p));
//					tempMoney=tempMoney.add(new BigDecimal(p));
//				}
//			}
//			
//			betDetail.setRoom(room);
//			betDetail.setSessionNo(gaPick11Session.getSessionNo());
//			betDetail.setGameName("江西11选5");
//			if(betOption.getPlayType().equals("0")){//两面盘
//				betDetail.setPlayName("两面盘");
//			}else if(betOption.getPlayType().equals("1")){//1-5球
//				betDetail.setPlayName("1-5球");
//			}else if(betOption.getPlayType().equals("2")){//任选
//				betDetail.setPlayName("任选");
//			}else if(betOption.getPlayType().equals("3")){//组选
//				betDetail.setPlayName("组选");
//			}else if(betOption.getPlayType().equals("4")){//直选
//				betDetail.setPlayName("直选");
//			}
//			betDetail.setBetName(betOption.getTitle());
//			betDetail.setOptionTitle(optionTitle);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_JXPICK11);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			betDetailList.add(betDetail);
//		}
//		
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
////		
//
////			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_JXPICK11, betAll.subtract(tempMoney), null);
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
//		
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_JXPICK11,remark.toString());
//		
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//
//		userService.updateUserBanlance(user.getUserId());
////		userService.savePointDetail(user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPPick11, betAll.intValue(), null);
//		
//		jxPick11DAO.updateObjectList(betDetailList, null);
//	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "总和";
			 }else{
				 return "第"+optionType+"球";
			 }
		 }else{//第1 -5 球
			 return "第"+(Integer.parseInt(optionType)+1)+"球";
		}
	 }
	 
	public List<JxPick11GaTrend> findJxPick11GaTrendList(){
		return jxPick11DAO.findJxPick11GaTrendList();
	}
	public PaginationSupport  findJxPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		return jxPick11DAO.findJxPick11GaSessionList(hql,para,pageNum,pageSize);
	}
	public PaginationSupport  findJxPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		return jxPick11DAO.findJxPick11GaBetList(hql,para,pageNum,pageSize);
	}
	
	/**
	 * 判断用户是否中奖   返回结果  0=未中奖   1=和    2=中奖
	 * @param results
	 * @param detail
	 * @return
	 */
	public String  judgeWin(String results,GaBetDetail detail){
		String array[]=results.split(",");//拆分结果
		String[] optionArr = detail.getOptionTitle().split(",");// 投注号
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
		} else if (detail.getPlayName().equals("1-5球")) {//1-5球
			int seq=Integer.parseInt(detail.getBetName().substring(1, detail.getBetName().length()-1));
			int value=Integer.parseInt(array[seq-1]);//第几球的值
			//下注的具体是几号
			int index=Integer.parseInt(detail.getOptionTitle());
			if(value==index){
				return "2";
			}else{
				return "0";
			}
		} else if (detail.getPlayName().equals("任选")) {//任选
			if (detail.getBetName().equals("一中一1.9")) {
				for (String result : array) {
					if (result.equals(optionArr[0])) {
						return "2";//中奖
					}
				}
				return "0";
			} else if (detail.getBetName().equals("二中二5.1")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 2)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("三中三16")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 3)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("四中四60")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 4)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("五中五420")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 5)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("六中五70")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 5)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("七中五10")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 5)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("八中五7.57")) {
				int winNum = 0;
				for (String result : array) {
					for (String option : optionArr) {
						if (option.equals(result)) winNum = ++winNum;// 有一个号选对
					}
				}
				if (winNum == 5)  return "2";//中奖
				return "0";
			} else {
				return "0";
			}
		} else if (detail.getPlayName().equals("组选")) {//组选
			if (detail.getBetName().equals("前二25")) {
				int winNum = 0;
				for (String option : optionArr) {
					if (option.equals(array[0]) || option.equals(array[1])) 
						winNum = ++winNum;// 有一个号选对
				}
				if (winNum == 2)  return "2";//中奖
				return "0";
			} else if (detail.getBetName().equals("前三150")) {
				int winNum = 0;
				for (String option : optionArr) {
					if (option.equals(array[0]) || option.equals(array[1])
							|| option.equals(array[2])) 
						winNum = ++winNum;// 有一个号选对
				}
				if (winNum == 3)  return "2";//中奖
				return "0";
			} else {
				return "0";
			} 
		} else if (detail.getPlayName().equals("直选")) {//直选
			if (detail.getBetName().equals("前二50")) {
				if(array[0].equals(optionArr[0]) && array[1].equals(optionArr[1])){
					return "2";//中奖
				}
				return "0";
			} else if (detail.getBetName().equals("前三900")) {
				if(array[0].equals(optionArr[0]) && array[1].equals(optionArr[1])
						&& array[2].equals(optionArr[2])){
					return "2";//中奖
				}
				return "0";
			} else {
				return "0";
			} 
		} else {
			return "0";
		}
	}
	
	public static void main(String[] args) {
		String[] array = {"06","10","11","01","02"};
		String[] optionArr = {"01","02","06"};
		int winNum = 0;
		for (String result : array) {
			for (String option : optionArr) {
				if (option.equals(result)) winNum = ++winNum;// 有一个号选对
			}
		}
		System.out.println(winNum==3);
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

	public void updateTrendResult(JxPick11GaSession session) {
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			Map<String,Boolean> resultMap; // 开奖结果
			HQUtils hq = new HQUtils(" from JxPick11GaTrend gdk10 ");
			List<Object> trendList = jxPick11DAO.findObjects(hq); //上期冷热排行榜
			List<JxPick11GaTrend> saveList = new ArrayList<JxPick11GaTrend>(); // 本期冷热排行榜

			String results = session.getOpenResult();
			
			if(results != null && results.length() > 0){
				resultMap = this.transResult(results);
			}else{
				return;
			}
			if(trendList != null && trendList.size() > 0 ){
				for(int i = 0; i < trendList.size(); i ++){
					JxPick11GaTrend trend = (JxPick11GaTrend) trendList.get(i);
					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
						trend.setTrendCount(trend.getTrendCount() + 1);
					}else{
						trend.setTrendCount(0);
					}
					saveList.add(trend);
				}
			}
			jxPick11DAO.updateObjectList(saveList, null);
		}
	}
	
//	@Override
//	public void updateGaTrend() {
//		JxPick11GaSession jxPick11GaSession = jxPick11DAO.getCurrentSession(); // 当前期数信息
//		if(jxPick11GaSession != null){
//			String curNo = jxPick11GaSession.getSessionNo(); // 当前期号
//			String preNo = String.valueOf((Integer.parseInt(curNo) - 1)); //上期期号
//			JxPick11GaSession jxPick11PreSession = getPreviousSessionBySessionNo(preNo);
//			if(jxPick11PreSession==null){
//				jxPick11PreSession=(JxPick11GaSession) jxPick11DAO.getObject(JxPick11GaSession.class, jxPick11GaSession.getSessionId()-1);
//			}
//			Map<String,Boolean> resultMap; // 开奖结果
//			
//			HQUtils hq = new HQUtils(" from JxPick11GaTrend gdk10 ");
//			List<Object> trendList = jxPick11DAO.findObjects(hq); //上期冷热排行榜
//			List<JxPick11GaTrend> saveList = new ArrayList<JxPick11GaTrend>(); // 本期冷热排行榜
//
//			String results = jxPick11PreSession.getOpenResult();
//			
//			if(results != null && results.length() > 0){
//				resultMap = this.transResult(results);
//			}else{
//				return;
//			}
//			if(trendList != null && trendList.size() > 0 ){
//				for(int i = 0; i < trendList.size(); i ++){
//					JxPick11GaTrend trend = (JxPick11GaTrend) trendList.get(i);
//					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
//						trend.setTrendCount(trend.getTrendCount() + 1);
//					}else{
//						trend.setTrendCount(0);
//					}
//					saveList.add(trend);
//				}
//			}
//			jxPick11DAO.updateObjectList(saveList, null);
//		}else{
//			log.info("获取当前期数信息失败！");
//		}
//		jxPick11GaSession=null;
//	}
	
	/**
	 * 把开奖结果转换为冷热排行榜对应值。
	 * @return 
	 */
	public Map<String, Boolean> transResult(String results){
		String array[] = results.split(",");
		int[] arr; // 存储每一位开奖结果
		int sum = 0;
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 冷热排行榜各种值

        arr = new int[array.length];
        for(int i = 0; i < array.length; i++){
        	arr[i] = Integer.parseInt(array[i]);
        	sum = sum + arr[i];
        }
        for(int i = 0; i< arr.length; i++){
        	if(arr[i] == 11){
        		resultMap.put("第" + (i+1) + "球 和", true);
        	}else{
            	if(arr[i] <= 5){
            		resultMap.put("第" + (i+1) + "球 小", true);
            	}else{
            		resultMap.put("第" + (i+1) + "球 大", true);
            	}
            	if(arr[i] %2 == 0){
            		resultMap.put("第" + (i+1) + "球 双", true);
            	}else{
            		resultMap.put("第" + (i+1) + "球 单", true);
            	}
        	}
        }
		if(sum%2==0){
			resultMap.put("总和 双", true);
		}else{
			resultMap.put("总和 单", true);
		}
		if(sum>30){
			resultMap.put("总和 大", true);
		}else if(sum<30){
			resultMap.put("总和 小", true);
		}else if(sum==30){
			resultMap.put("总和 和", true);
		}

		int val=sum%10;
		if(val>=5){
			resultMap.put("总和 尾大", true);
		}else{
			resultMap.put("总和 尾小", true);
		}

		if(Integer.parseInt(array[0])>Integer.parseInt(array[4])){
			resultMap.put("龙", true);
		}else if(Integer.parseInt(array[0])<Integer.parseInt(array[4])){
			resultMap.put("虎", true);
		}
		return resultMap;
		
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
		List<JxPick11GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<JxPick11GaSession>();
			String startTimeStr = today + JxPick11Constants.JX_PICK11_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			
			for (int i = 0; i < JxPick11Constants.JX_PICK11_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*JxPick11Constants.JX_PICK11_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				String sessionNo = this.getTodaySessionNo(c.getTime(), i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				JxPick11GaSession k10Session = new JxPick11GaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,JxPick11Constants.JX_PICK11_TIME_INTERVAL));
				k10Session.setOpenStatus(JxPick11Constants.JX_PICK11_OPEN_STATUS_INIT);
//				jxPick11DAO.saveObject(k10Session);
				saveList.add(k10Session);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			jxPick11DAO.updateObjectList(saveList, null);
			flag = "success";
//			log.info("___[today init completed]__________________________");
		} else {
			flag = "inited";
//			log.info("___[today has been inited]__________________________");
		}
		return flag;
	}
	
	public boolean saveOpenResult(JxPick11GaSession session,String openResult){

		boolean flag=false;
		session.setOpenResult(openResult);
		jxPick11DAO.updateObject(session, null);
		flag=true;
		return flag;
	}
	
	public boolean saveAndOpenResult(JxPick11GaSession session,String openResult){
		boolean flag=false;
		session.setOpenResult(openResult);		
		boolean flag1 = openJxPick11SessionOpenResultMethod(session, session.getOpenResult());
		if(flag1){
			session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
			session.setOpenStatus(JxPick11Constants.JX_PICK11_OPEN_STATUS_OPENED);
			jxPick11DAO.updateObject(session, null);
			log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
			flag=true;
		}else{
			log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
		}
		return flag;
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return jxPick11DAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<JxPick11DTO> findGaBetDetailById(String hql, List<Object> para) {
		return jxPick11DAO.findGaBetDetailById(hql,para);
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
	@Override
	public boolean saveRevokePrize(JxPick11GaSession session) {
		//删除JxPick11GaBet表的记录
		List<Object> para = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" and sessionId = ? ");
		para.add(session.getSessionId());
		jxPick11DAO.deleteJxPick11GaBet(hql.toString(),para);

		boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_JXPICK11,session.getSessionNo());
		if(result){
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
}
