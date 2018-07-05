package com.xy.pk10.jsft.service.impl;

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
import com.ram.service.user.IUserService;
import com.xy.pk10.jsft.JsftConstants;
import com.xy.pk10.jsft.dao.IJsftDAO;
import com.xy.pk10.jsft.model.JsftGaBet;
import com.xy.pk10.jsft.model.JsftGaSession;
import com.xy.pk10.jsft.model.JsftGaTrend;
import com.xy.pk10.jsft.model.dto.JsftDTO;
import com.xy.pk10.jsft.service.IJsftService;

public class JsftServiceImpl extends BaseService implements IJsftService {
	private IJsftDAO jsftDAO;
	private IUserService userService;
	private IGaService gaService;
	public void setJsftDAO(IJsftDAO jsftdao) {
		jsftDAO = jsftdao;
		super.dao = jsftDAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	@Override
	public String updateInitSession(int days) {
		String flag = "fail";//返回状态
		
		Date time = DateTimeUtil.getDateTimeOfDays(new Date(), days);//初始化指定日期
	
		//今天是否已经初始化场次
		boolean isSessionInit = this.checkSessionInit(time);
		if(!isSessionInit){
			List<JsftGaSession> saveList = new ArrayList<JsftGaSession>();
			String theDay = DateTimeUtil.DateToString(time);
			String startDateStr = theDay + JsftConstants.JSFT_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startDateStr);//开始时间

			String sessionNo = null;
			Date startTime = null;
			Date endTime = null;
			for (int i = 0; i < JsftConstants.JSFT__MAX_PART; i++) {
				startTime = DateTimeUtil.getDateTimeOfSeconds(startDate,i*JsftConstants.JSFT__TIME_INTERVAL);
				endTime = DateTimeUtil.getDateTimeOfSeconds(startDate,(i+1)*JsftConstants.JSFT__TIME_INTERVAL);
				sessionNo = this.getSessionNo(startDate, i+1);//期号
				
				JsftGaSession session = new JsftGaSession();
				session.setSessionNo(sessionNo);
				session.setStartTime(startTime);
				session.setEndTime(endTime);
				session.setOpenStatus(JsftConstants.JSFT_OPEN_STATUS_INIT);
				saveList.add(session);
			}
			jsftDAO.updateObjectList(saveList, null);
			flag = "success";
		} else {
			flag = "inited";
		}
		return flag;
	}
	
	/**
	 * 检查今天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkSessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from JsftGaSession ho where ho.startTime>? and ho.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = jsftDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 根据时间及第几期生成期号
	 * @param now
	 * @param index
	 * @return
	 */
	public String getSessionNo(Date now,int index){
		return DateTimeUtil.dateToString(now, "yyMMdd")+String.format("%03d", index);
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		JsftGaSession curtSession = jsftDAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		JsftGaSession lastSession = (JsftGaSession)jsftDAO.getObject(JsftGaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JSFT);
		
		//迭代开奖无序
		List<JsftGaSession> openedList = new ArrayList<JsftGaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				JsftGaSession session = jsftDAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openJsftSessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							jsftDAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_JSFT);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				jsftDAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(JsftGaSession session:openedList){
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
//		JsftGaSession currentSession= jsftDAO.getCurrentSession();
//		if(currentSession != null){
//			JsftGaSession session=(JsftGaSession) jsftDAO.getObject(JsftGaSession.class, currentSession.getSessionId()-1);
//	        try {
//				if(session!=null){
//					String openStatus1 = session.getOpenStatus();//开奖状态
//					if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//						SessionItem sessionItem = sessionNoMap.get(session.getSessionNo());
//						session.setOpenResult(sessionItem.getResult());
//						
//						boolean flag = openJsftSessionOpenResultMethod(session, session.getOpenResult());
//						if(flag){
//							session.setOpenTime(DateTimeUtil.parse(sessionItem.getTime()));
//							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//							jsftDAO.saveObject(session);
//						}
//					}
//				}
//					
//				GaSessionInfo sessionInfo=gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_JSFT);
//				if(sessionInfo!=null){
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					sessionInfo.setOpenResult(session.getOpenResult());
//					sessionInfo.setOpenSessionNo(session.getSessionNo());
//					sessionInfo.setEndTime(session.getEndTime());
//					jsftDAO.updateObject(sessionInfo, null);
//					CacheUtil.updateGameList();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}

//	public void updateFetchAndOpenResult() {
//		JsftGaSession currentSession= jsftDAO.getCurrentSession();
//		if(currentSession != null){
//			JsftGaSession session=(JsftGaSession) jsftDAO.getObject(JsftGaSession.class, currentSession.getSessionId()-1);
//	        try {
//				if(session!=null){
//					String openStatus1 = session.getOpenStatus();//开奖状态
//					if(openStatus1.equals(GameConstants.OPEN_STATUS_INIT) || openStatus1.equals(GameConstants.OPEN_STATUS_OPENING)){
//						//为了开奖时间更像是从接口获取
//						Thread.sleep(JsftUtil.getRandomSecond()*1000);
//						if(ParamUtils.chkString(session.getOpenResult())){				
//						}else{
//							session.setOpenResult(JsftUtil.getRandomResult(1));
//						}
//						
//						boolean flag = openJsftSessionOpenResultMethod(session, session.getOpenResult());
//						if(flag){
//							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
//							jsftDAO.saveObject(session);
//							log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//						}else{
//							log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//						}
//					}
//				}
//			
//				GaSessionInfo sessionInfo=gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_JSFT);
//				if(sessionInfo!=null){
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					sessionInfo.setOpenResult(session.getOpenResult());
//					sessionInfo.setOpenSessionNo(session.getSessionNo());
//					sessionInfo.setEndTime(session.getEndTime());
//					jsftDAO.updateObject(sessionInfo, null);
//					CacheUtil.updateGameList();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void updateTrendResult(JsftGaSession session){
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			List<JsftGaTrend> list=jsftDAO.findJsftGaTrendAllList();
			List<JsftGaTrend> savelist=new ArrayList<JsftGaTrend>();
			Map<String,Boolean> map=getTrendResult(session.getOpenResult());
			if(!map.isEmpty()){
				for(JsftGaTrend trend:list){
					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
						trend.setTrendCount(trend.getTrendCount()+1);
					}else{
						trend.setTrendCount(0);
					}
					savelist.add(trend);
				}
				jsftDAO.updateObjectList(savelist, null);
			}
		}
	}
	
//	public void updateFetchAndOpenTrendResult(Integer count){
//		if(count==9){//执行10次  还没有结果退出递归
//			count=null;
//			return;
//		}
//		JsftGaSession tempSession =jsftDAO.getCurrentSession();
//		if(tempSession != null){
//			JsftGaSession session=null;
//
//			session=(JsftGaSession) jsftDAO.getObject(JsftGaSession.class, tempSession.getSessionId()-1);
//
//			if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
//				List<JsftGaTrend> list=jsftDAO.findJsftGaTrendAllList();
//				List<JsftGaTrend> savelist=new ArrayList<JsftGaTrend>();
//				Map<String,Boolean> map=getTrendResult(session.getOpenResult());
//				if(!map.isEmpty()){
//					for(int i=0;i<list.size();i++){
//						JsftGaTrend trend=list.get(i);
//						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
//							trend.setTrendCount(trend.getTrendCount()+1);
//						}else{
//							trend.setTrendCount(0);
//						}
//						savelist.add(trend);
//					}
//					jsftDAO.updateObjectList(savelist, null);
//					map.clear();
//					savelist=null;
//					list=null;
//					session=null;
//					count=null;		
//					tempSession=null;
//					return;
//				}
//				count=null;	
//			}else{
//				Thread t1=new Thread(){
//		            public void run(){
//		               try {
//		            	   sleep(3000);
//		            	   interrupt();
//		               } catch (Exception e) {
//		            	   interrupt();
//		               }
//		            }
//				};
//				t1.start();
//				try {
//					t1.join();
//					t1=null;
//					count++;
//
//					session=null;
//					tempSession=null;
//					updateFetchAndOpenTrendResult(count);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param sessionNo
	 * @param result 开奖号10组数字 英文逗号连接
	 * @return
	 */
	public boolean openJsftSessionOpenResultMethod(JsftGaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JSFT);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_JSFT);
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
			JsftGaBet bet=new JsftGaBet();
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
						jsftDAO.saveObject(detail);
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
						Constants.GAME_TYPE_XY_JSFT, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			jsftDAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}	
	}
	
//	public boolean openJsftSessionOpenResultMethod(JsftGaSession session,String result){
//		try{
////			JsftGaSession session =jsftDAO.getPreviousSessionBySessionNo(sessionNo);
//			List<Object> para = new ArrayList<Object>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" and ho.gameType=?  ");// 0=三份彩  1=北京飞艇  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//			para.add(Constants.GAME_TYPE_XY_JSFT);
//			hql.append( " and ho.sessionId =? ");
//			para.add(session.getSessionId());
//			hql.append(" and ho.betFlag=? " );
//			para.add("1");
//			hql.append(" order by ho.betTime desc");
//			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
//			
//			JsftGaBet bet=new JsftGaBet();
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
//					boolean flag=judgeWin(result,detail);
//					detail.setOpenResult(result);
//					if(flag){//中奖
//						
//						detail.setWinResult(JsftConstants.JSFT_WIN);//中奖
//						BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//						detail.setWinCash(wincash);
//						
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//						BigDecimal userBal=null;
//					
//						detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
//						StringBuffer remark=new StringBuffer();
//						remark.append("彩票中奖 奖金 ")
//						    .append(wincash).append("元");
////						user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJPK10,remark.toString());
////						User user=(User) jsftDAO.getObject(User.class, detail.getUserId());
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
//					}else{
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						detail.setWinCash(new BigDecimal(0));
//						detail.setWinResult(JsftConstants.JSFT_WIN_NOT);//未中奖
//						try {
//							jsftDAO.updateObject(detail, null);
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
//			jsftDAO.saveObject(bet, null);
//			return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}	
//	}

	public void updateOpenData(GaBetDetail detail,String remark){
		jsftDAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_JSFT,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		jsftDAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_JSFT,remark);
//	}

	public JsftGaSession getCurrentSession(){
		return jsftDAO.getCurrentSession();
	}
	public JsftGaSession getPreviousSessionBySessionNo(String sessionNo){
		return jsftDAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,JsftGaSession session,User user,BigDecimal betAll){
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_JSFT);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
//		//3/5次
//		String estimateResult=session.getEstimateResult();
//		String[] array = null;//预设开奖结果数组
//		if(ParamUtils.chkString(estimateResult)){
//			array=estimateResult.split("\\|");
//		}
		
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
			betDetail.setPlayName(JsftConstants.getPlayTypeName(betOption.getPlayType()));
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			jsftDAO.saveObject(betDetail);
			
//			//3/5次
//			if(!Constants.USER_TYPE_TEST.equals(userType)){
//				if(array != null && array.length >0){
//					for(int j=0;j<array.length;j++){
//						String openResult=array[j];
//						boolean flag=judgeWin(openResult,betDetail);
//						BigDecimal wincash=new  BigDecimal(0);
//						if(flag){
//							wincash=betDetail.getBetRate().multiply(new BigDecimal(betDetail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//						}
//						if(j==0){
//							betDetail.setOneMoney(wincash);
//						}else if(j==1){
//							betDetail.setTwoMoney(wincash);
//						}else if(j==2){
//							betDetail.setThreeMoney(wincash);	
//						}else if(j==3){
//							betDetail.setFourMoney(wincash);
//						}else if(j==4){
//							betDetail.setFiveMoney(wincash);
//						}
//					}
//				}	
//			}
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_JSFT,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		jsftDAO.updateObjectList(rlList, null);
		
		return user;
	}
	
//	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,JsftGaSession gaK10Session,User user,BigDecimal betAll){
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
//				betDetail.setGameName("幸运飞艇");
//				if(betOption.getPlayType().equals(JsftConstants.JSFT_PLAY_TYPE_TWO_FACE)){
//					betDetail.setPlayName("两面盘");
//				}else if(betOption.getPlayType().equals(JsftConstants.JSFT_PLAY_TYPE_ONE_TO_TEN)){
//					betDetail.setPlayName("1-10名");
//				}else if(betOption.getPlayType().equals(JsftConstants.JSFT_PLAY_TYPE_SUM)){
//					betDetail.setPlayName("冠亚军和");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_JSFT);//游戏类型 0=三份彩  1=北京飞艇  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
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
////			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPK10, betAll.subtract(tempMoney), null);
//			StringBuilder remark = new StringBuilder();
//			remark.append("购买彩票 扣款 ")
//			    .append(betAll.subtract(tempMoney)).append("元");
//			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_BJPK10,remark.toString());
//
//			//更新用户实时余额  --by.cuisy.20171209
//			userService.updateUserMoney(user.getUserId());
//			userService.updateUserBanlance(user.getUserId());
//
////		userService.saveTradeDetail(user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJPK10, betAll, null);
//		jsftDAO.updateObjectList(betDetailList, null);
//		return user;
//	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals(JsftConstants.JSFT_PLAY_TYPE_TWO_FACE)){//两面盘
			 if(optionType.equals("0")){
				 return "冠军";
			 }else if(optionType.equals("1")){
				 return "亚军";
			 }else{
				 return "第"+(Integer.parseInt(optionType)+1)+"名";
			 }
		 }else if(playType.equals(JsftConstants.JSFT_PLAY_TYPE_ONE_TO_TEN)){//1-10名
			 if(optionType.equals("0")){
				 return "冠军";
			 }else if(optionType.equals("1")){
				 return "亚军";
			 }else{
				 return "第"+(Integer.parseInt(optionType)+1)+"名";
			 }
		}else if(playType.equals(JsftConstants.JSFT_PLAY_TYPE_SUM)){//冠亚军和
			return "冠亚军和";
		}
		 return "";
	 }
	 
	public List<JsftGaTrend> findJsftGaTrendList(){
		return jsftDAO.findJsftGaTrendList();
	}
	public PaginationSupport  findJsftGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		return jsftDAO.findJsftGaSessionList(hql,para,pageNum,pageSize);
	}

	public PaginationSupport  findJsftGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		return jsftDAO.findJsftGaBetList(hql,para,pageNum,pageSize);
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
	public boolean saveOpenResult(JsftGaSession session,String openResult){
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
			jsftDAO.updateObject(session, null);
			flag=true;
		}
		
		log.info("flag_________________________"+flag);
		return flag;
		
	}
	
	public boolean saveAndOpenResult(JsftGaSession session,String openResult){
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
			boolean flag1 = openJsftSessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
				jsftDAO.updateObject(session, null);
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
		return jsftDAO.findGaBetDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public List<JsftDTO> findGaBetDetailById(String hql, List<Object> para) {
		return jsftDAO.findGaBetDetailById(hql, para);
	}
	@Override
	public boolean saveRevokePrize(JsftGaSession session) {
		//删除JsftGaBet表的记录
		List<Object> para = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" and sessionId = ? ");
		para.add(session.getSessionId());
		jsftDAO.deleteJsftGaBet(hql.toString(),para);

		boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_JSFT,session.getSessionNo());
		if(result){
			session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
			gaService.saveObject(session, null);
		}
		return result;
	}
}
