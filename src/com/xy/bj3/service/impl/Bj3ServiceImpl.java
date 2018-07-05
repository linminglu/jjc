package com.xy.bj3.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.xy.bj3.dao.IBj3DAO;
import com.xy.bj3.model.Bj3GaBet;
import com.xy.bj3.model.Bj3GaSession;
import com.xy.bj3.model.Bj3GaTrend;
import com.xy.bj3.model.dto.Bj3DTO;
import com.xy.bj3.service.IBj3Service;

public class Bj3ServiceImpl extends BaseService implements IBj3Service {
	private IBj3DAO bj3DAO;
	private IUserService userService;
	private IGaService gaService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public void setBj3DAO(IBj3DAO bj3dao) {
		bj3DAO = bj3dao;
		super.dao = bj3DAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setGaService(IGaService gaService){
		this.gaService = gaService;
	}
	
	@Override
	public String updateInitSession(int num) {
		String flag = "fail"; // 失败返回
		//今天是否已经初始化场次
		boolean isTomorrowSessionInit = this.checkSessionInit(num);
		List<Bj3GaSession> saveList = null;
		if(!isTomorrowSessionInit){
			saveList = new ArrayList<Bj3GaSession>();
			for(int i=0; i<GameConstants.BJ3_MAX_PART; i++){
				String str = String.format("%03d", i+1); //格式化输出 ，例如：001
				String date; // 年月日
				String sessionNo; // 彩票期号
				int sessionSDate; // 分钟数转换为期号开始时间戳
				int sessionEDate; // 分钟数转换为期号结束时间戳
				SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd"); //年月日时间格式
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //年月日时分秒格式
				SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd"); // yyMMdd格式
				Long todayStamp = null; // 今天年月日的时间戳
				Date sDate = null; //期号开始时间
				Date eDate = null; //期号结束时间
				Long sDateStamp; // 期号开始时间戳
				Long eDateStamp; // 期号结束时间戳
				
				if(i<GameConstants.BJ3_EVENING_PART){
					// 如果是120场以内，即早上6点前
					sessionSDate = i*3*60*1000;
					sessionEDate = (i+1)*3*60*1000;
				}else{
					//如果是大于120场，就要把开始时间加上6点到9点的3个小时。
					sessionSDate = i*3*60*1000 + GameConstants.BJ3_PAUSE_PART*60*60*1000;
					sessionEDate = (i+1)*3*60*1000 + GameConstants.BJ3_PAUSE_PART*60*60*1000;							
				}
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, num); // 获取时间
				date = simpleDateFormat.format(c.getTime());
				sessionNo = yyMMdd.format(c.getTime()) + str;
				
				try {
					todayStamp = simpleDateFormat.parse(date).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				sDateStamp = todayStamp + sessionSDate;
				eDateStamp = todayStamp + sessionEDate;
				try {
					sDate = sdf.parse(sdf.format(sDateStamp));
					eDate = sdf.parse(sdf.format(eDateStamp));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Bj3GaSession bj3GaSession = new Bj3GaSession();
				bj3GaSession.setSessionNo(sessionNo);
				bj3GaSession.setStartTime(sDate);
				bj3GaSession.setEndTime(eDate);
				bj3GaSession.setOpenStatus(GameConstants.BJ3_OPEN_STATUS_INIT);
//				bj3DAO.saveObject(bj3GaSession);
				saveList.add(bj3GaSession);
			}
			bj3DAO.updateObjectList(saveList, null);
			flag = "success";
		} else {
			flag = "inited";
		}
		return flag;
	}

	//-----------------------独立业务方法写在这里----------------------------------------------------

	/**
	 * 检查场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkSessionInit(int num){
		//今天日期处理 yyyy-MM-dd
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd"); //年月日时间格式
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, num); // 获取时间

		String todayYyyymmdd = simpleDateFormat.format(c.getTime());
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from Bj3GaSession b3gs where b3gs.startTime>? and b3gs.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = bj3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}

	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		Bj3GaSession curtSession = bj3DAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		Bj3GaSession lastSession = (Bj3GaSession)bj3DAO.getObject(Bj3GaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJ3);
		
		//迭代开奖无序
		List<Bj3GaSession> openedList = new ArrayList<Bj3GaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				Bj3GaSession session = bj3DAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openBj3SessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							bj3DAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJ3);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				bj3DAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(Bj3GaSession session:openedList){
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
//		Bj3GaSession currentSession=bj3DAO.getCurrentSession();
//		Bj3GaSession tempsession =bj3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//			tempsession=(Bj3GaSession) bj3DAO.getObject(Bj3GaSession.class, (currentSession.getSessionId()-1));
//		}
//		String lastSessionNo=tempsession.getSessionNo();
//		
//		Bj3GaSession session =bj3DAO.getPreviousSessionBySessionNo(lastSessionNo);
//		if(session!=null){
//			String openStatus1 = session.getOpenStatus();//开奖状态
//			if(openStatus1.equals(GameConstants.BJ3_OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.BJ3_OPEN_STATUS_OPENING)){
//				SessionItem sessionItem = sessionNoMap.get(session.getSessionNo());
//				session.setOpenResult(sessionItem.getResult());
//				boolean flag = openBj3SessionOpenResultMethod(session, session.getOpenResult());
//				if(flag){
//					session.setOpenTime(DateTimeUtil.parse(sessionItem.getTime()));
//					session.setOpenStatus(GameConstants.BJ3_OPEN_STATUS_OPENED);
//					bj3DAO.saveObject(session);
//				}else{
//					GameHelpUtil.log(Constants.GAME_TYPE_XY_BJ3,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//				}
//			}
//					
//			GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJ3);
//			if(sessionInfo != null){
//				if(session != null){
//					sessionInfo.setOpenSessionNo(lastSessionNo);
//					sessionInfo.setOpenResult(session.getOpenResult());
//					sessionInfo.setEndTime(tempsession.getEndTime());
//				}
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//				bj3DAO.saveObject(sessionInfo);
//				CacheUtil.updateGameList();
//			}
//			
//			// 把资金明细里投注记录状态值改为有效
//			userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//					Constants.GAME_TYPE_XY_BJ3, Constants.PUB_STATUS_OPEN);
//		}
//	} 
	
//	@Override
//	public void updateFetchAndOpenResult() {
//		Bj3GaSession currentSession=bj3DAO.getCurrentSession();
//		Bj3GaSession tempsession =bj3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(Bj3GaSession) bj3DAO.getObject(Bj3GaSession.class, (currentSession.getSessionId()-1));
//		}
//		String lastSessionNo=tempsession.getSessionNo();
//		
////		Map<String,String> sessionNoMap=new HashMap<String,String>();
////		Thread t=new Thread(){
////            public void run(){
////         	   Integer countRun=0;
////               try {
////
////            	   while(true){
////            		   if(countRun==19){
////            			   interrupt();
////            			   countRun=null;
////            			   break;
////            		   }
////            		    countRun=countRun+1;
////            		   
////	               		//从这里 ---------------------------------------------------------------------------
////	               	    String officalURL = Constants.getGameBj3OpenResultUrl()+System.currentTimeMillis();
////	               		log.info("___[bj3 start fetch result xml data...]________________");
////	
////	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
////	               		//到这里 ---------------------------------------------------------------------------		
////	               	 sleep(3000);
////	               		log.info("___[fetch result xml data]"+resultXML+"---------bj3");
////	               		if(ParamUtils.chkString(resultXML)){
////	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
////	               				               			
////	               			//开始解析场次开奖数据
////	               			NodeList nList = xmlDoc.getElementsByTagName("row");
////	               			String sessionNo="";//场次号
////	               			String result="";//开奖结果5组数字
////
//////	               			boolean isnew=false;
////	               			for(int i =0;i<nList.getLength();i++){
////	               				Node node = nList.item(i);
////	               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
////	               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
////           						if(sessionNoMap.get(sessionNo)==null){
////           							sessionNoMap.put(sessionNo, result);
////           						}
////           						if(sessionNo.equals(lastSessionNo)){
////           							interrupt();
////           							countRun=null;
////           						}
////	               			}
//////	               			interrupt();
////	               		}else{
////	               			interrupt();
////   							countRun=null;
////       						break;
////	               		}
////            	   }
////               } catch (Exception e) {
////            	   countRun=null;
////            	   interrupt();
////               }
////            }
////        };
////        t.start();
//        
//        try {
//
////			t.join();//该方法是等 t 线程结束以后再执行后面的代码
////			t=null;
//			Bj3GaSession session =bj3DAO.getPreviousSessionBySessionNo(lastSessionNo);
////			if(!sessionNoMap.isEmpty()){
////				for(String key:sessionNoMap.keySet()){
//		
//					if(session!=null){
//						String openStatus1 = session.getOpenStatus();//开奖状态
//						if(openStatus1.equals(GameConstants.BJ3_OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.BJ3_OPEN_STATUS_OPENING)){
////							session.setOpenResult(sessionNoMap.get(key));
//							if(ParamUtils.chkString(session.getOpenResult())){
//							}else{
//								Bj3OpenResult br=new Bj3OpenResult();
//								session.setOpenResult(br.getRandomResult());
//							}
//							boolean flag = openBj3SessionOpenResultMethod(session, session.getOpenResult());
//							if(flag){
//								session.setOpenTime(DateTimeUtil.getYMDHMNow());
//								session.setOpenStatus(GameConstants.BJ3_OPEN_STATUS_OPENED);
//								bj3DAO.saveObject(session);
//								log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//							}else{
//								log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//							}
//						}
////					}
////				}
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJ3);
//				if(sessionInfo != null){
////					HQUtils hq = new HQUtils("from Bj3GaSession bj3 where bj3.sessionNo =? ");
////					hq.addPars(lastSessionNo);
////					Bj3GaSession bj3 = (Bj3GaSession) bj3DAO.getObject(hq);
//					if(session != null){
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setOpenResult(session.getOpenResult());
//						sessionInfo.setEndTime(tempsession.getEndTime());
//					}
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					bj3DAO.saveObject(sessionInfo);
//					CacheUtil.updateGameList();
//				}
////				sessionNoMap.clear();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	} 

	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param sessionNo
	 * @param result 开奖号5组数字 英文逗号连接 [1,2,3,4,5]
	 * @return
	 */
	public boolean openBj3SessionOpenResultMethod(Bj3GaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJ3);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_BJ3);
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
			Bj3GaBet bet=new Bj3GaBet();
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
					
					BigDecimal wincash = judgeWin(result,detail);//判断中奖 //0未中奖  1打和  2中奖
					detail.setOpenResult(result);
					
					if(wincash.compareTo(new BigDecimal(0)) == 1){//中奖
						detail.setWinResult(GameConstants.WIN);
						//中奖金额
						wincash = GameHelpUtil.round(wincash);
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
						
					}else if(wincash.compareTo(new BigDecimal(0)) == 0){//打和
						detail.setWinResult(GameConstants.WIN_HE);
						//中奖金额
						wincash = GameHelpUtil.round(new BigDecimal(detail.getBetMoney()));//投注总钱数
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
						bj3DAO.saveObject(detail);
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
						Constants.GAME_TYPE_XY_BJ3, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			bj3DAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}	
	}
	
//	public boolean openBj3SessionOpenResultMethod(Bj3GaSession session,String result){
//		try{
//			List<Object> para = new ArrayList<Object>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" and ho.gameType=?  ");// 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			para.add(Constants.GAME_TYPE_XY_BJ3);
//			hql.append(" and ho.sessionId=?  ");
//			para.add(session.getSessionId());
//			hql.append(" order by ho.betTime desc");
//			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
//			Bj3GaBet bet=new Bj3GaBet();
//			BigDecimal  totalPoint=new BigDecimal(0);
//			BigDecimal  betCash=new BigDecimal(0);
//			bet.setSessionId(session.getSessionId());
//			bet.setSessionNo(session.getSessionNo());
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
//					BigDecimal wincash = judgeWin(result,detail);//0未中奖  1打和  2中奖
//					detail.setOpenResult(result);
//					if(wincash.compareTo(new BigDecimal(0)) == 1){//中奖
//						detail.setWinResult(GameConstants.BJ3_WIN);//中奖
//						wincash=wincash.setScale(2, BigDecimal.ROUND_HALF_UP);
//						detail.setWinCash(wincash);
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//						detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
//					
//						StringBuffer remark=new StringBuffer();
//						remark.append("彩票中奖 奖金 ")
//						    .append(wincash).append("元");
////						user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJ3,remark.toString());
//
////						User user=(User) bj3DAO.getObject(User.class, detail.getUserId());
////						BigDecimal userBal=null;
////						if(user.getUserBalance()!=null){
////							userBal=user.getUserBalance();
////						}else{
////							userBal=new BigDecimal(0);
////						}
////						user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//						try {
//							updateOpenData(detail,null,remark.toString());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}else if(wincash.compareTo(new BigDecimal(0)) == 0){//打和
//						detail.setWinResult(GameConstants.BJ3_WIN_HE);//打和
//						wincash=new BigDecimal(detail.getBetMoney());//投注总钱数
//						detail.setWinCash(wincash);
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//					
////						userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_BJ3, wincash, detail.getBetDetailId());
//						StringBuffer remark=new StringBuffer();
//						remark.append("彩票打和 退款")
//						    .append(wincash).append("元");
////						user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJ3,remark.toString());
//				
////						BigDecimal userBal=null;
////						User user=(User) bj3DAO.getObject(User.class, detail.getUserId());
////						if(user.getUserBalance()!=null){
////							userBal=user.getUserBalance();
////						}else{
////							userBal=new BigDecimal(0);
////						}
////						user.setUserBalance(userBal.add(wincash));
//						
//						try {
//							updateOpenData(detail,null,remark.toString());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//					}else{
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						detail.setWinCash(new BigDecimal(0));
//						detail.setWinResult(GameConstants.BJ3_WIN_NOT);//未中奖
//						try {
//							bj3DAO.updateObject(detail, null);
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
//			bj3DAO.saveObject(bet, null);
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public void updateOpenData(GaBetDetail detail,String remark){
		bj3DAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_BJ3,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		bj3DAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJ3,remark);
//	}

	/**
	 * 判断用户是否中奖   返回结果      >0中奖    =0打和     <0未中奖
	 * @param results
	 * @param detail
	 * @return
	 */
	public BigDecimal judgeWin(String result,GaBetDetail detail){
		Map<String, Boolean> resultMap = this.transResult(result); // 开奖结果
		String array[] = result.split(",");//拆分结果
		if (detail.getPlayName().equals("两面盘")) { // 如果投注的是两面盘
			// 中奖金额
			BigDecimal winMoney = detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney()));
			if(detail.getBetName().equals("总和/龙虎")){
				if(detail.getOptionTitle().equals("龙") || detail.getOptionTitle().equals("虎")){
					if(resultMap.get("和") !=null&&resultMap.get("和") == true){
						return new BigDecimal(0);
					}else{
						if (resultMap.get(detail.getOptionTitle()) != null) return winMoney;
						else return new BigDecimal(-1);
					}
				}else{
					if (resultMap.get(detail.getOptionTitle()) != null) return winMoney;
					else return new BigDecimal(-1);
				}
			}else{
				if (resultMap.get(detail.getBetName()+" "+detail.getOptionTitle()) != null) return winMoney;
				else return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("1-5球")) { // 如果投注的是1-5球
			if (resultMap.get(detail.getBetName()+" "+detail.getOptionTitle()) != null) 
				return detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney()));
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("前二直选")) {// 
			// 规则  前二、后二0~9任选2个号进行投注，当开奖结果与所选的号码相同时（且顺序一致），即为中奖。如02749，前二直选02收买中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1|0,1
			String[] optionArr = optionTitle.split("\\|");
			String[] firstArr = optionArr[0].split(",");// 第一位号
			String[] secondArr = optionArr[1].split(",");// 第二位号
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(firstArr.length * secondArr.length));
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			for (String first : firstArr) {
				if (array[0].equals(first)) flag1 = true;
			}
			for (String second : secondArr) {
				if (array[1].equals(second)) flag2 = true;
			}
			if (flag1 && flag2) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("后二直选")) {// 
			// 规则  前二、后二0~9任选2个号进行投注，当开奖结果与所选的号码相同时（且顺序一致），即为中奖。如02749，前二直选02收买中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1|0,1
			String[] optionArr = optionTitle.split("\\|");
			String[] firstArr = optionArr[0].split(",");// 第一位号
			String[] secondArr = optionArr[1].split(",");// 第二位号
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(firstArr.length * secondArr.length));
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			for (String first : firstArr) {
				if (array[3].equals(first)) flag1 = true;
			}
			for (String second : secondArr) {
				if (array[4].equals(second)) flag2 = true;
			}
			if (flag1 && flag2) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("前三直选")) {// 
			// 规则 前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（且顺序一致），
			      //即为中奖。如12356，前三直选买123则为中奖；如13259，则中三买325则为中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1|0,1
			String[] optionArr = optionTitle.split("\\|");
			String[] firstArr = optionArr[0].split(",");// 第一位号
			String[] secondArr = optionArr[1].split(",");// 第二位号
			String[] thirdArr = optionArr[2].split(",");// 第三位号
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(firstArr.length * secondArr.length * thirdArr.length));
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			boolean flag3 = false;// 第三位是否相等
			for (String first : firstArr) {
				if (array[0].equals(first)) flag1 = true;
			}
			for (String second : secondArr) {
				if (array[1].equals(second)) flag2 = true;
			}
			for (String third : thirdArr) {
				if (array[2].equals(third)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("中三直选")) {// 
			// 规则 前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（且顺序一致），
		      //即为中奖。如12356，前三直选买123则为中奖；如13259，则中三买325则为中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1|0,1
			String[] optionArr = optionTitle.split("\\|");
			String[] firstArr = optionArr[0].split(",");// 第一位号
			String[] secondArr = optionArr[1].split(",");// 第二位号
			String[] thirdArr = optionArr[2].split(",");// 第三位号
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(firstArr.length * secondArr.length * thirdArr.length));
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			boolean flag3 = false;// 第三位是否相等
			for (String first : firstArr) {
				if (array[1].equals(first)) flag1 = true;
			}
			for (String second : secondArr) {
				if (array[2].equals(second)) flag2 = true;
			}
			for (String third : thirdArr) {
				if (array[3].equals(third)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("后三直选")) {// 
			// 规则 前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（且顺序一致），
		      //即为中奖。如12356，前三直选买123则为中奖；如13259，则中三买325则为中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1|0,1
			String[] optionArr = optionTitle.split("\\|");
			String[] firstArr = optionArr[0].split(",");// 第一位号
			String[] secondArr = optionArr[1].split(",");// 第二位号
			String[] thirdArr = optionArr[2].split(",");// 第三位号
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(firstArr.length * secondArr.length * thirdArr.length));
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			boolean flag3 = false;// 第三位是否相等
			for (String first : firstArr) {
				if (array[2].equals(first)) flag1 = true;
			}
			for (String second : secondArr) {
				if (array[3].equals(second)) flag2 = true;
			}
			for (String third : thirdArr) {
				if (array[4].equals(third)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("前二组选")) {//
			// 规则 前二、后二0~9任选2个号进行投注，当开奖结果与所选的号码相同时（顺序不限），即为中奖。如02749，前二组选买02或20都中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(getBetNum(optionArr.length, 2)));
			for (String option : optionArr) {
				if (array[0].equals(option)) flag1 = true;
				if (array[1].equals(option)) flag2 = true;
			}
			if (flag1 && flag2) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("后二组选")) {// 
			// 规则 前二、后二0~9任选2个号进行投注，当开奖结果与所选的号码相同时（顺序不限），即为中奖。如02749，前二组选买02或20都中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			boolean flag1 = false;// 第一位是否相等
			boolean flag2 = false;// 第二位是否相等
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(getBetNum(optionArr.length, 2)));
			for (String option : optionArr) {
				if (array[3].equals(option)) flag1 = true;
				if (array[4].equals(option)) flag2 = true;
			}
			if (flag1 && flag2) {// 中奖(此玩法只可能中一注)
				return detail.getBetRate().multiply(oneBetMoney);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("前三组三")) {// 
			// 规则    开奖号码中的3个数字有两个相同，即为组三。前三、中三、后三组三，0~9至少任选2个号进行复式投注，
				 //投注的号码与中奖号码相同（顺序不限），则为中奖。例如，前三组三选择号码4、5 ，将组合成2注（一对4一个5和一对5一个4），
				 //开奖结果为： 44512 、 45412 、 54412、45512、54512、55412 之一皆中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(optionArr.length*(optionArr.length-1)));
			boolean flag1 = false;
			boolean flag2 = false;
			if (array[0].equals(array[1])) {
				for (String option : optionArr) {
					if (option.equals(array[0])) flag1 = true;
					if (option.equals(array[2])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[0].equals(array[2])) {
				for (String option : optionArr) {
					if (option.equals(array[0])) flag1 = true;
					if (option.equals(array[1])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[1].equals(array[2])) {
				for (String option : optionArr) {
					if (option.equals(array[1])) flag1 = true;
					if (option.equals(array[0])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (!flag1 || !flag2) return new BigDecimal(-1);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("中三组三")) {// 
			// 规则    开奖号码中的3个数字有两个相同，即为组三。前三、中三、后三组三，0~9至少任选2个号进行复式投注，
			 //投注的号码与中奖号码相同（顺序不限），则为中奖。例如，前三组三选择号码4、5 ，将组合成2注（一对4一个5和一对5一个4），
			 //开奖结果为： 44512 、 45412 、 54412、45512、54512、55412 之一皆中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(optionArr.length*(optionArr.length-1)));
			boolean flag1 = false;
			boolean flag2 = false;
			if (array[1].equals(array[2])) {
				for (String option : optionArr) {
					if (option.equals(array[1])) flag1 = true;
					if (option.equals(array[3])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[1].equals(array[3])) {
				for (String option : optionArr) {
					if (option.equals(array[1])) flag1 = true;
					if (option.equals(array[2])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[2].equals(array[3])) {
				for (String option : optionArr) {
					if (option.equals(array[2])) flag1 = true;
					if (option.equals(array[1])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (!flag1 || !flag2) return new BigDecimal(-1);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("后三组三")) {// 
			// 规则    开奖号码中的3个数字有两个相同，即为组三。前三、中三、后三组三，0~9至少任选2个号进行复式投注，
			 //投注的号码与中奖号码相同（顺序不限），则为中奖。例如，前三组三选择号码4、5 ，将组合成2注（一对4一个5和一对5一个4），
			 //开奖结果为： 44512 、 45412 、 54412、45512、54512、55412 之一皆中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(optionArr.length*(optionArr.length-1)));
			boolean flag1 = false;
			boolean flag2 = false;
			if (array[2].equals(array[3])) {
				for (String option : optionArr) {
					if (option.equals(array[2])) flag1 = true;
					if (option.equals(array[4])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[2].equals(array[4])) {
				for (String option : optionArr) {
					if (option.equals(array[2])) flag1 = true;
					if (option.equals(array[3])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (array[3].equals(array[4])) {
				for (String option : optionArr) {
					if (option.equals(array[3])) flag1 = true;
					if (option.equals(array[2])) flag2 = true;
				}
				if (flag1 && flag2) return detail.getBetRate().multiply(oneBetMoney);
				else return new BigDecimal(-1);
			}
			if (!flag1 || !flag2) return new BigDecimal(-1);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("前三组六")) {// 
			// 规则   前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（顺序不限），即为中奖。如13579，前三组六买153或153或135都中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(getBetNum(optionArr.length, 3)));
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			for (String option : optionArr) {
				if (array[0].equals(option)) flag1 = true;
				if (array[1].equals(option)) flag2 = true;
				if (array[2].equals(option)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) return detail.getBetRate().multiply(oneBetMoney);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("中三组六")) {// 
			// 规则   前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（顺序不限），即为中奖。如13579，前三组六买153或153或135都中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(getBetNum(optionArr.length, 3)));
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			for (String option : optionArr) {
				if (array[1].equals(option)) flag1 = true;
				if (array[2].equals(option)) flag2 = true;
				if (array[3].equals(option)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) return detail.getBetRate().multiply(oneBetMoney);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("后三组六")) {// 
			// 规则   前三、中三、后三0~9任选3个号进行投注，当开奖结果与所选的号码相同时（顺序不限），即为中奖。如13579，前三组六买153或153或135都中奖。
			String optionTitle = detail.getOptionTitle();// 格式  0,1
			String[] optionArr = optionTitle.split(",");
			// 每注投的钱数=总投注金额/总投注数
			BigDecimal oneBetMoney = new BigDecimal(detail.getBetMoney()).
					divide(new BigDecimal(getBetNum(optionArr.length, 3)));
			boolean flag1 = false;
			boolean flag2 = false;
			boolean flag3 = false;
			for (String option : optionArr) {
				if (array[2].equals(option)) flag1 = true;
				if (array[3].equals(option)) flag2 = true;
				if (array[4].equals(option)) flag3 = true;
			}
			if (flag1 && flag2 && flag3) return detail.getBetRate().multiply(oneBetMoney);
			else return new BigDecimal(-1);
		} else if (detail.getPlayName().equals("前三")) {// 
			// 规则    豹子：如号码为24448，则中三为豹子，如号码为34666，则后三为豹子
			//顺子：开奖的三位数相连则为顺子，如号码为23487，则前三为顺子，如号码为28978则中三和后三都为顺子（注：0和1为相连的数，0和9不是相连的数）
			//对子：指相连的三位数有任意2位数相同（豹子除外）为对子。如号码为46686，则前三，中三，后三为对子，如号码为11261则前三为对子。
			//半顺：指三位数中有任意2位数相连为半顺（不包括顺子，对子，数字09不算相连之数），如号码为29387则前三（23相连）中三（89）相连 后三（87），为半顺。
			Integer first = Integer.valueOf(array[0]);
			Integer second = Integer.valueOf(array[1]);
			Integer third = Integer.valueOf(array[2]);
			String optionTitle = detail.getOptionTitle();
			// 中奖金额
			BigDecimal winMoney = detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney()));
			if ("豹子".equals(optionTitle)) {
				if (array[0].equals(array[1]) && array[1].equals(array[2])) return winMoney;
				else return new BigDecimal(-1);
			} else if ("顺子".equals(optionTitle)) {
				boolean flag = checkShunZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("对子".equals(optionTitle)) {
				boolean flag = checkDuiZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("半顺".equals(optionTitle)) {
				boolean flag = checkBanShun(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("中三")) {// 
			// 规则    豹子：如号码为24448，则中三为豹子，如号码为34666，则后三为豹子
			//顺子：开奖的三位数相连则为顺子，如号码为23487，则前三为顺子，如号码为28978则中三和后三都为顺子（注：0和1为相连的数，0和9不是相连的数）
			//对子：指相连的三位数有任意2位数相同（豹子除外）为对子。如号码为46686，则前三，中三，后三为对子，如号码为11261则前三为对子。
			//半顺：指三位数中有任意2位数相连为半顺（不包括顺子，对子，数字09不算相连之数），如号码为29387则前三（23相连）中三（89）相连 后三（87），为半顺。
			Integer first = Integer.valueOf(array[1]);
			Integer second = Integer.valueOf(array[2]);
			Integer third = Integer.valueOf(array[3]);
			String optionTitle = detail.getOptionTitle();
			// 中奖金额
			BigDecimal winMoney = detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney()));
			if ("豹子".equals(optionTitle)) {
				if (array[0].equals(array[1]) && array[1].equals(array[2])) return winMoney;
				else return new BigDecimal(-1);
			} else if ("顺子".equals(optionTitle)) {
				boolean flag = checkShunZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("对子".equals(optionTitle)) {
				boolean flag = checkDuiZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("半顺".equals(optionTitle)) {
				boolean flag = checkBanShun(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else {
				return new BigDecimal(-1);
			}
		} else if (detail.getPlayName().equals("后三")) {// 
			// 规则    豹子：如号码为24448，则中三为豹子，如号码为34666，则后三为豹子
			//顺子：开奖的三位数相连则为顺子，如号码为23487，则前三为顺子，如号码为28978则中三和后三都为顺子（注：0和1为相连的数，0和9不是相连的数）
			//对子：指相连的三位数有任意2位数相同（豹子除外）为对子。如号码为46686，则前三，中三，后三为对子，如号码为11261则前三为对子。
			//半顺：指三位数中有任意2位数相连为半顺（不包括顺子，对子，数字09不算相连之数），如号码为29387则前三（23相连）中三（89）相连 后三（87），为半顺。
			Integer first = Integer.valueOf(array[2]);
			Integer second = Integer.valueOf(array[3]);
			Integer third = Integer.valueOf(array[4]);
			String optionTitle = detail.getOptionTitle();
			// 中奖金额
			BigDecimal winMoney = detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney()));
			if ("豹子".equals(optionTitle)) {
				if (array[0].equals(array[1]) && array[1].equals(array[2])) return winMoney;
				else return new BigDecimal(-1);
			} else if ("顺子".equals(optionTitle)) {
				boolean flag = checkShunZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("对子".equals(optionTitle)) {
				boolean flag = checkDuiZi(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else if ("半顺".equals(optionTitle)) {
				boolean flag = checkBanShun(first, second, third);
				if (flag) return winMoney;
				else return new BigDecimal(-1);
			} else {
				return new BigDecimal(-1);
			}
		} else {
			log.info("暂时没有该玩法！");
			return new BigDecimal(-1);
		}
	}
	
	/**
	 * 判断这3个数是否为顺子（213也算顺子）
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public static boolean checkShunZi(Integer num1, Integer num2, Integer num3) {
		if (Math.abs(num1 - num2) == 1) {
			if (Math.abs(num1 - num3) == 1 || Math.abs(num1 - num3) == 2) {
				if (Math.abs(num1 - num3) == 1) {
					if (Math.abs(num2 - num3) == 2) return true;
					else return false;
				} else {
					if (Math.abs(num2 - num3) == 1) return true;
					else return false;
				}
			} else {
				return false;
			}
		} else if (Math.abs(num1 - num2) == 2) {
			if (Math.abs(num1 - num3) == 1 && Math.abs(num2 - num3) == 1) return true;
			else return false;
		} else {
			return false;
		}
	}
	/**
	 * 判断这3个数是否为对子
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public static boolean checkDuiZi(Integer num1, Integer num2, Integer num3) {
		if (num1 == num2 && num1 != num3) return true;
		else if (num1 == num3 && num1 != num2) return true;
		else if (num2 == num3 && num2 != num1) return true;
		else return false;
	}
	
	/**
	 * 判断这3个数是否为半顺
	 * @param num1
	 * @param num2
	 * @param num3
	 * @return
	 */
	public static boolean checkBanShun(Integer num1, Integer num2, Integer num3) {
		if (Math.abs(num1 - num2) == 1 || Math.abs(num1 - num3) == 1
				|| Math.abs(num2 - num3) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取m个号选n的组合
	 */
	public static int getBetNum(int m, int n) {
		int fenzi=1;
		int fenmu=1;
		for(int k=m;k>=(m-n+1);k--){
			fenzi=fenzi*k;
		}
		for(int k=1;k<=n;k++){
			fenmu=fenmu*k;
		}
		return fenzi/fenmu;
	}
	
	@Override
	public Bj3GaSession getCurrentSession() {
		
		return bj3DAO.getCurrentSession();
	}

	@Override
	public Bj3GaSession getPreviousSessionBySessionNo(String sessionNo) {
		return bj3DAO.getPreviousSessionBySessionNo(sessionNo);
	}

	@Override
	public List<Bj3GaTrend> findBj3GaTrendList() {
		return bj3DAO.findBj3GaTrendList();
	}

	@Override
	public PaginationSupport findBj3GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return bj3DAO.findBj3GaSessionList(hql, para, pageNum, pageSize);
	}

	public User saveUserBetInfo(String room,Map<String,Integer> betMap,List<GaBetOption> list,Bj3GaSession session,User user,BigDecimal betAll){
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJ3);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		String id;//optionId
		Integer p;//money
		String optionTitle;
		GaBetOption betOption = null;
		for(Map.Entry<String, Integer> entry:betMap.entrySet()){
			//初始化
			id = entry.getKey();
			p = entry.getValue();
			optionTitle = "";
			
			String[] arrId = id.split("\\|");// 有可能是0,1|0,1这种形式
			for (String arr : arrId) {// 查找所有的投注号
				List<GaBetOption> optionList = gaService.getGaBetOptionByIds(arr);//此注的所有投注号对象
				betOption = optionList.get(0);
				if (ParamUtils.chkString(optionTitle)) {
					optionTitle = optionTitle + "|";
				}
				for (GaBetOption gbo : optionList) {
					optionTitle = optionTitle + gbo.getOptionTitle()+",";
				}
				optionTitle = optionTitle.substring(0, optionTitle.length()-1);
			}
			
			//投注记录
			GaBetDetail betDetail = new GaBetDetail();
			betDetail.setBetRate(betOption.getBetRate());
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
			
			if (betOption.getPlayType().equals("0")) {
				betDetail.setPlayName("两面盘");
			} else if (betOption.getPlayType().equals("1")) {
				betDetail.setPlayName("1-5球");
			} else if (betOption.getPlayType().equals("2")) {
				betDetail.setPlayName("前二直选");
			} else if (betOption.getPlayType().equals("3")) {
				betDetail.setPlayName("后二直选");
			} else if (betOption.getPlayType().equals("4")) {
				betDetail.setPlayName("前三直选");
			} else if (betOption.getPlayType().equals("5")) {
				betDetail.setPlayName("中三直选");
			} else if (betOption.getPlayType().equals("6")) {
				betDetail.setPlayName("后三直选");
			} else if (betOption.getPlayType().equals("7")) {
				betDetail.setPlayName("前二组选");
			} else if (betOption.getPlayType().equals("8")) {
				betDetail.setPlayName("后二组选");
			} else if (betOption.getPlayType().equals("9")) {
				betDetail.setPlayName("前三组三");
			} else if (betOption.getPlayType().equals("10")) {
				betDetail.setPlayName("中三组三");
			} else if (betOption.getPlayType().equals("11")) {
				betDetail.setPlayName("后三组三");
			} else if (betOption.getPlayType().equals("12")) {
				betDetail.setPlayName("前三组六");
			} else if (betOption.getPlayType().equals("13")) {
				betDetail.setPlayName("中三组六");
			} else if (betOption.getPlayType().equals("14")) {
				betDetail.setPlayName("后三组六");
			} else if (betOption.getPlayType().equals("15")) {
				betDetail.setPlayName("前三");
			} else if (betOption.getPlayType().equals("16")) {
				betDetail.setPlayName("中三");
			} else if (betOption.getPlayType().equals("17")) {
				betDetail.setPlayName("后三");
			}
			betDetail.setBetName(betOption.getTitle());
			betDetail.setOptionTitle(optionTitle);
			betDetail.setGameType(Constants.GAME_TYPE_XY_BJ3);
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			bj3DAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_BJ3,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		bj3DAO.updateObjectList(rlList, null);
		
		return  user;
	}
	
//	public User saveUserBetInfo(String room, Map<String, Integer> betMap,
//			List<GaBetOption> list, Bj3GaSession ga3Session, User user,
//			BigDecimal betAll) {
//		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
//		BigDecimal paperMoney=user.getUserScore();
//		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
//		BigDecimal tempMoney=new BigDecimal(0);
//		for (Map.Entry<String, Integer> entry : betMap.entrySet()) { 
//			String id = entry.getKey();//
//			Integer p = entry.getValue();// 投注金额
//			String optionTitle = "";
//			
//			GaBetOption betOption = null;
//			String[] arrId = id.split("\\|");// 有可能是0,1|0,1这种形式
//			for (String arr : arrId) {// 查找所有的投注号
//				List<GaBetOption> optionList = gaService.getGaBetOptionByIds(arr);//此注的所有投注号对象
//				betOption = optionList.get(0);
//				if (ParamUtils.chkString(optionTitle)) {
//					optionTitle = optionTitle + "|";
//				}
//				for (GaBetOption gbo : optionList) {
//					optionTitle = optionTitle + gbo.getOptionTitle()+",";
//				}
//				optionTitle = optionTitle.substring(0, optionTitle.length()-1);
//			}
//			GaBetDetail betDetail=new GaBetDetail();
//			if(betOption!=null){
//				betDetail.setBetRate(betOption.getBetRate());
//			}
//			betDetail.setWinResult("0");//未开奖
//			betDetail.setBetFlag("1");//有效投注
//			betDetail.setSessionId(ga3Session.getSessionId());
//			
//			betDetail.setBetOptionId(betOption.getBetOptionId());
//			betDetail.setBetTime(new Date());
//			betDetail.setBetMoney(p);
//			
//			betDetail.setUserId(user.getUserId());
//			
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
//			betDetail.setSessionNo(ga3Session.getSessionNo());
//			betDetail.setGameName("三分彩");
//			if (betOption.getPlayType().equals("0")) {
//				betDetail.setPlayName("两面盘");
//			} else if (betOption.getPlayType().equals("1")) {
//				betDetail.setPlayName("1-5球");
//			} else if (betOption.getPlayType().equals("2")) {
//				betDetail.setPlayName("前二直选");
//			} else if (betOption.getPlayType().equals("3")) {
//				betDetail.setPlayName("后二直选");
//			} else if (betOption.getPlayType().equals("4")) {
//				betDetail.setPlayName("前三直选");
//			} else if (betOption.getPlayType().equals("5")) {
//				betDetail.setPlayName("中三直选");
//			} else if (betOption.getPlayType().equals("6")) {
//				betDetail.setPlayName("后三直选");
//			} else if (betOption.getPlayType().equals("7")) {
//				betDetail.setPlayName("前二组选");
//			} else if (betOption.getPlayType().equals("8")) {
//				betDetail.setPlayName("后二组选");
//			} else if (betOption.getPlayType().equals("9")) {
//				betDetail.setPlayName("前三组三");
//			} else if (betOption.getPlayType().equals("10")) {
//				betDetail.setPlayName("中三组三");
//			} else if (betOption.getPlayType().equals("11")) {
//				betDetail.setPlayName("后三组三");
//			} else if (betOption.getPlayType().equals("12")) {
//				betDetail.setPlayName("前三组六");
//			} else if (betOption.getPlayType().equals("13")) {
//				betDetail.setPlayName("中三组六");
//			} else if (betOption.getPlayType().equals("14")) {
//				betDetail.setPlayName("后三组六");
//			} else if (betOption.getPlayType().equals("15")) {
//				betDetail.setPlayName("前三");
//			} else if (betOption.getPlayType().equals("16")) {
//				betDetail.setPlayName("中三");
//			} else if (betOption.getPlayType().equals("17")) {
//				betDetail.setPlayName("后三");
//			}
//			betDetail.setBetName(betOption.getTitle());
//			betDetail.setOptionTitle(optionTitle);
//			betDetail.setGameType(Constants.GAME_TYPE_XY_BJ3);
//			betDetailList.add(betDetail);
//		}
//	
//		
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
////		BigDecimal userBalance=user.getUserBalance();
////		if(userBalance==null){
////			userBalance=new BigDecimal(0);
////		}
////		user.setUserBalance(userBalance.subtract(betAll));
////		
////		BigDecimal dayBet=user.getDayBet();
////		if(dayBet==null){
////			dayBet=new BigDecimal(0);
////		}
////		user.setDayBet(dayBet.add(betAll));
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_BJ3,remark.toString());
//
//
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//		userService.updateUserBanlance(user.getUserId());
//		bj3DAO.updateObjectList(betDetailList, null);
//		
//		return user;	
//		
//	}
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "总和/龙虎";
			 }else{
				 return "第"+optionType+"球";
			 }
		 }else{//第1 -5球
			return "第"+(Integer.parseInt(optionType)+1)+"球";
		}
	 }

	 public void updateTrendResult(Bj3GaSession session){
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			List<Bj3GaTrend> list=bj3DAO.findBj3GaTrendAllList();
			List<Bj3GaTrend> savelist=new ArrayList<Bj3GaTrend>();
			Map<String,Boolean> map=getTrendResult(session.getOpenResult());
			if(!map.isEmpty()){
				for(Bj3GaTrend trend:list){
					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
						trend.setTrendCount(trend.getTrendCount()+1);
					}else{
						trend.setTrendCount(0);
					}
					savelist.add(trend);
				}
				bj3DAO.updateObjectList(savelist, null);
			}
		}
	}
//	public void updateGaTrend() {
//		Bj3GaSession bj3GaSession = bj3DAO.getCurrentSession(); // 当前期数信息
//		if(bj3GaSession != null){
//			String curNo = bj3GaSession.getSessionNo(); // 当前期号
//			String preNo = String.valueOf((Integer.parseInt(curNo) - 1)); //上期期号
//			Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 开奖结果
//	
//			HQUtils hq = new HQUtils(" from Bj3GaTrend bj3 where 1=1 ");
//			List<Object> trendList = bj3DAO.findObjects(hq); //上期冷热排行榜
//			List<Bj3GaTrend> saveList = new ArrayList<Bj3GaTrend>(); // 本期冷热排行榜
//
//			Bj3GaSession bj3PreSession = getPreviousSessionBySessionNo(preNo);
//			if(bj3PreSession==null){
//				bj3PreSession= (Bj3GaSession) bj3DAO.getObject(Bj3GaSession.class, bj3GaSession.getSessionId()-1);
//			}
//
//			String result = bj3PreSession.getOpenResult();
//			
//			if(result != null && result.length() > 0){
//				resultMap = this.transHotResult(result);			
//			}else{
//				log.info("开奖结果为空。");
//				return;
//			}
//			if(trendList != null && trendList.size() > 0 ){
//				for(int i = 0; i < trendList.size(); i ++){
//					Bj3GaTrend trend = (Bj3GaTrend) trendList.get(i);
//					if(resultMap.get(trend.getTrendTitle()) != null && resultMap.get(trend.getTrendTitle()) == true){
//						trend.setTrendCount(trend.getTrendCount() + 1);
//					}else{
//						trend.setTrendCount(0);
//					}
//					saveList.add(trend);
//				}
//			}
//			bj3DAO.updateObjectList(saveList, null);
//		}else{
//			log.info("获取当前期数信息失败！");
//		}
//	}

	/**
	 * 把开奖结果转换为相应投注选项
	 * @param result  String类型的开奖结果
	 * @return
	 */
	public Map<String, Boolean> transResult(String result){
		String[] arrResult = result.split(",");; // String类型的开奖结果。
		int[] arr; // 存储每一位开奖结果
		int sum = 0; // 开奖数字和
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 开奖结果		
		
        arr = new int[5];
        for(int i = 0; i < arrResult.length; i++){
        	arr[i] = Integer.parseInt(arrResult[i]);
        	sum = sum + arr[i];
        }
        if(0 <= sum && sum <= 22){
        	resultMap.put("总和小", true);
        }else if(sum >=23 && sum <= 45){
        	resultMap.put("总和大", true);
        }else{
        	log.info("参数错误，请检查！");
        }
        if(sum %2 == 0){
        	resultMap.put("总和双", true);
        }else{
        	resultMap.put("总和单", true);
        }
        if(arr[0] > arr[4]){
        	resultMap.put("龙", true);
        }else if(arr[0] < arr[4]){
        	resultMap.put("虎", true);
        }else if(arr[0] == arr[4]){
        	resultMap.put("和", true);
        }
        for(int i = 0; i< arr.length; i++){
        	if(arr[i] <= 4){
        		resultMap.put("第" + (i+1) + "球 小", true);
        	}else{
        		resultMap.put("第" + (i+1) + "球 大", true);
        	}
        	if(arr[i] %2 == 0){
        		resultMap.put("第" + (i+1) + "球 双", true);
        	}else{
        		resultMap.put("第" + (i+1) + "球 单", true);
        	}
        	resultMap.put("第" + (i+1) +"球 "+arr[i]+"号", true);
        }     
		return resultMap;
	}
	
	/**
	 * 把开奖结果转换为冷热排行榜选项
	 * @param result  String类型的开奖结果
	 * @return
	 */
	public Map<String,Boolean> getTrendResult(String results){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		if(ParamUtils.chkString(results)){
			String array[]=results.split(",");
			int sum=0;
			for(int i=0;i<array.length;i++){
				if(Integer.parseInt(array[i])%2==0){
						map.put("第"+(i+1)+"球 双", true);
				}else{
						map.put("第"+(i+1)+"球 单", true);
				}		
				if(Integer.parseInt(array[i])>=5){
						map.put("第"+(i+1)+"球 大", true);
				}else{
					map.put("第"+(i+1)+"球 小", true);
				}
				sum=sum+Integer.parseInt(array[i]);
			}
			if(sum%2==0){
				map.put("总和双", true);
			}else{
				map.put("总和单", true);
			}
			if(sum>=23){
				map.put("总和大", true);
			}else{
				map.put("总和小", true);
			}
			if(Integer.parseInt(array[0])==Integer.parseInt(array[4])){
				map.put("和", true);
			}else if(Integer.parseInt(array[0])>Integer.parseInt(array[4])){
				map.put("龙", true);
			}else if(Integer.parseInt(array[0])<Integer.parseInt(array[4])){
				map.put("虎", true);
			}

		}
		return map;
	}
//	public Map<String, Boolean> transHotResult(String result){
//		String[] arrResult = result.split(",");; // String类型的开奖结果。
//		int[] arr; // 存储每一位开奖结果
//		int sum = 0; // 开奖数字和
//		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 开奖结果		
//		
//		arr = new int[5];
//		for(int i = 0; i < arrResult.length; i++){
//			arr[i] = Integer.parseInt(arrResult[i]);
//			sum = sum + arr[i];
//		}
//		if(0 <= sum && sum <= 22){
//			resultMap.put("总和小", true);
//		}else if(sum >=23 && sum <= 45){
//			resultMap.put("总和大", true);
//		}else{
//			log.info("参数错误，请检查！");
//		}
//		if(sum %2 == 0){
//			resultMap.put("总和双", true);
//		}else{
//			resultMap.put("总和单", true);
//		}
//		if(arr[0] > arr[4]){
//			resultMap.put("龙", true);
//		}else if(arr[0] < arr[4]){
//			resultMap.put("虎", true);
//		}else if(arr[0] == arr[4]){
//			resultMap.put("和", true);
//		}
//		for(int i = 0; i< arr.length; i++){
//			if(arr[i] <= 4){
//				resultMap.put("第" + (i+1) + "球 小", true);
//			}else{
//				resultMap.put("第" + (i+1) + "球 大", true);
//			}
//			if(arr[i] %2 == 0){
//				resultMap.put("第" + (i+1) + "球 双", true);
//			}else{
//				resultMap.put("第" + (i+1) + "球 单", true);
//			}
//		}     
//		return resultMap;
//	}

	@Override
	public List<Bj3GaSession> findSessionNoByCurrent(int start, int end) {
		
		return bj3DAO.getCurrentSession(start,end);
	}
	
	public boolean saveOpenResult(Bj3GaSession session,String openResult){
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
			bj3DAO.updateObject(session, null);
			flag=true;
		}
		return flag;
		
	}
	
	public boolean saveAndOpenResult(Bj3GaSession session,String openResult){
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
			boolean flag1 = openBj3SessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
				bj3DAO.updateObject(session, null);
				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
				flag=true;
			}else{
				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			}
		}
		return flag;
	}

	@Override
	public PaginationSupport findBj3GaBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return bj3DAO.findBj3GaBetList(hql, para, pageNum, pageSize);
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return bj3DAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<Bj3DTO> findGaBetDetailById(String hql, List<Object> para) {
		return bj3DAO.findGaBetDetailById(hql, para);
	}

	@Override
	public boolean saveRevokePrize(Bj3GaSession session) {
		//删除Bj3GaBet表的记录
		List<Object> para = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" and sessionId = ? ");
		para.add(session.getSessionId());
		bj3DAO.deleteBj3GaBet(hql.toString(),para);

		boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_BJ3,session.getSessionNo());
		if(result){
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
}
