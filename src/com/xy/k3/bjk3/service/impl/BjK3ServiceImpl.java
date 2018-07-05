package com.xy.k3.bjk3.service.impl;

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
import com.xy.k3.bjk3.BjK3Constants;
import com.xy.k3.bjk3.dao.IBjK3DAO;
import com.xy.k3.bjk3.model.BjK3GaBet;
import com.xy.k3.bjk3.model.BjK3GaSession;
import com.xy.k3.bjk3.model.BjK3GaTrend;
import com.xy.k3.bjk3.model.dto.BjK3DTO;
import com.xy.k3.bjk3.service.IBjK3Service;

public class BjK3ServiceImpl  extends BaseService implements IBjK3Service {
	private IBjK3DAO bjK3DAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setBjK3DAO(IBjK3DAO bjK3DAO) {
		this.bjK3DAO = bjK3DAO;
		super.dao = bjK3DAO;
		
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
	public String updateInitSession(String sessionNo1) {
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<BjK3GaSession> saveList = null;
		if(!isTodaySessionInit){
			saveList = new ArrayList<BjK3GaSession>();
			String startTimeStr = today + BjK3Constants.BJK3_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
			HQUtils hq = new HQUtils("from BjK3GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(new Date(), 1));
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=bjK3DAO.findObjects(hq);
			BjK3GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(BjK3GaSession) list.get(0);
			}
			
			for (int i = 0; i < BjK3Constants.BJK3_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjK3Constants.BJK3_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,sessionNo1);//期号 
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				BjK3GaSession bjK3Session = new BjK3GaSession();
				bjK3Session.setSessionNo(sessionNo);
				bjK3Session.setStartTime(curSessionDate);
				bjK3Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,BjK3Constants.BJK3_TIME_INTERVAL));
				bjK3Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				saveList.add(bjK3Session);
			}
			bjK3DAO.updateObjectList(saveList, null);
			flag = "success";
		} else {
			flag = "inited";
		}
		return flag;
	}
	
	/**
	 * 获取今天的期号
	 */
	public String getTodaySessionNo(BjK3GaSession beforeSession,int index,String sessionNo1){
		if(ParamUtils.chkString(sessionNo1.trim())){
			return  (Integer.parseInt(sessionNo1)+ index)+"";
		}else{
			return  (Integer.parseInt(beforeSession.getSessionNo())+ index)+"";
		}	
	}
	
	/**
	 * 初始化明天场次
	 */
	public String updateInitTomorrowSession() {
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(now,1));
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkNextdaySessionInit(now);
		List<BjK3GaSession> saveList = null;
		if(!isTodaySessionInit){
			saveList = new ArrayList<BjK3GaSession>();
			String startTimeStr = today + BjK3Constants.BJK3_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
			HQUtils hq = new HQUtils("from BjK3GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(now);
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=bjK3DAO.findObjects(hq);
			BjK3GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(BjK3GaSession) list.get(0);
			}
			
			for (int i = 0; i < BjK3Constants.BJK3_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjK3Constants.BJK3_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,"");//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				BjK3GaSession bjK3Session = new BjK3GaSession();
				bjK3Session.setSessionNo(sessionNo);
				bjK3Session.setStartTime(curSessionDate);
				bjK3Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,BjK3Constants.BJK3_TIME_INTERVAL));
				bjK3Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				saveList.add(bjK3Session);
			}
			bjK3DAO.updateObjectList(saveList, null);
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
	public boolean checkTodaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from BjK3GaSession bgs where bgs.startTime>? and bgs.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = bjK3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 检查明天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkNextdaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from BjK3GaSession bgs where bgs.startTime>? and bgs.startTime<?");
		hq.addPars(DateTimeUtil.getDateTimeOfDays(todayStart, 1));
		hq.addPars(DateTimeUtil.getDateTimeOfDays(todayEnd, 1));
		Integer count = bjK3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 获取今天的期号   看数据是 年月日+当前第几期
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date date,int index){	
		return DateTimeUtil.getYyMMddStr(date) + String.format("%03d", index);
	}
	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public boolean openBjK3SessionOpenResultMethod(BjK3GaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJK3);
		try{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_BJK3);
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
			BjK3GaBet bet=new BjK3GaBet();
			BigDecimal totalPoint=new BigDecimal(0);//总投注
			BigDecimal betCash=new BigDecimal(0);//总中奖
			bet.setSessionId(session.getSessionId());
			bet.setSessionNo(session.getSessionNo());
			
			String countResult = "";//处理后的开奖结果
			String openResult = result;
			if(ParamUtils.chkString(openResult)){
				String array[]=openResult.split(",");
				int sum=0;				
				if(array.length>=2){
					sum=sum+Integer.parseInt(array[0])+Integer.parseInt(array[1])+Integer.parseInt(array[2]);
				}
				countResult=openResult+","+sum;
			}
			
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
						
//					}else if(flag.equals("1")){//打和
//						detail.setWinResult(GameConstants.WIN_HE);
//						//中奖金额
//						BigDecimal wincash = GameHelpUtil.round(new BigDecimal(detail.getBetMoney()));//投注总钱数
//						detail.setWinCash(wincash);
//						//统计
//						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//						betCash=betCash.add(wincash);
//						//盈亏
//						detail.setPayoff(new BigDecimal(0));
//						//备注
//						String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
//						//更新
//						updateOpenData(detail,remark.toString());
						
					}else{//未中奖
						//统计
						totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
						detail.setWinCash(new BigDecimal(0));
						detail.setWinResult(GameConstants.WIN_NOT);//未中奖
						bjK3DAO.saveObject(detail);
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
						Constants.GAME_TYPE_XY_BJK3, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			bjK3DAO.saveObject(bet);
			return true;
		}catch(Exception e){
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}	
	}
	
//	public boolean openBjK3SessionOpenResultMethod(BjK3GaSession bjK3session,String result){
//		HQUtils hql = new HQUtils("from GaBetDetail gd where 1 = 1");
//		hql.addHsql(" and gd.sessionId=? ");
//		hql.addPars(bjK3session.getSessionId());
//		hql.addHsql(" and gd.betFlag=? " );
//		hql.addPars("1");
//		hql.addHsql(" and gd.gameType=? ");
//		hql.addPars(Constants.GAME_TYPE_XY_BJK3);
//		
//		List<Object>  list= bjK3DAO.findObjects(hql);
//		
//		BjK3GaBet bet=new BjK3GaBet();
//		BigDecimal  totalPoint=new BigDecimal(0);
//		BigDecimal  betCash=new BigDecimal(0);
//		bet.setSessionId(bjK3session.getSessionId());
//		bet.setSessionNo(bjK3session.getSessionNo());
//		
//		String countResult = "";
//		String openResult = bjK3session.getOpenResult();
//		if(ParamUtils.chkString(openResult)){
//			String array[]=openResult.split(",");
//			int sum=0;				
//			if(array.length>=2){
//				sum=sum+Integer.parseInt(array[0])+Integer.parseInt(array[1])+Integer.parseInt(array[2]);
//			}
//			countResult=openResult+","+sum;
//		}
//
//		if(list!=null && list.size()>0){
//			//开奖投注用户ids --by.cuisy.20171209
//			List<Integer> userIds = new ArrayList<Integer>();
//			
//			for(Object object:list){
//				GaBetDetail detail=(GaBetDetail) object;
//				//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//				if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
//					userIds.add(detail.getUserId());
//				}//~
//				
//				
//				String flag=judgeWin(bjK3session.getOpenResult(),detail);//0未中奖  1打和  2中奖
//				detail.setOpenResult(countResult);
//				if(flag.equals("2")){//中奖
////					User user=(User) gaService.getObject(User.class, detail.getUserId());
//					detail.setWinResult(BjK3Constants.BJK3_WIN);//中奖
//					BigDecimal wincash=detail.getBetRate().multiply(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP);
//					detail.setWinCash(wincash);
//					totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//					betCash=betCash.add(wincash);
//				
//					detail.setPayoff(wincash.subtract(new BigDecimal(detail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
////					userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_GDPICK11, wincash, detail.getBetDetailId());	
//					StringBuffer remark=new StringBuffer();
//					remark.append("彩票中奖 奖金 ").append(wincash).append("元");
////					user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJK3,remark.toString());
////					BigDecimal userBal=null;
////					if(user.getUserBalance()!=null){
////						userBal=user.getUserBalance();
////					}else{
////						userBal=new BigDecimal(0);
////					}
////					user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//					
//					try {
//						updateOpenData(detail,null,remark.toString());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}					
//				}else{//未中奖
//					detail.setWinCash(new BigDecimal(0));
//					detail.setWinResult(BjK3Constants.BJK3_WIN_NOT);//未中奖
//					totalPoint=totalPoint.add(new BigDecimal(detail.getBetMoney()));
//					try {
//						bjK3DAO.updateObject(detail, null);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//			//批量更新开奖用户实时余额 --by.cuisy.20171209
//			userService.updateUserMoney(userIds);
//		}
//		bet.setTotalPoint(totalPoint);
//		bet.setWinCash(betCash);
//		bjK3DAO.saveObject(bet, null);
//		return true;
//	}
	
	public void updateOpenData(GaBetDetail detail,String remark){
		bjK3DAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_BJK3,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		bjK3DAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_BJK3,remark);
//	}

	/**
	 * 判断用户是否中奖   返回结果  0=未中奖   1=和    2=中奖
	 * @param results
	 * @param detail
	 * @return
	 */
	public String  judgeWin(String results,GaBetDetail detail){
		String array[]=results.split(",");//拆分结果
		if(detail.getPlayName().equals("两面盘")){//先用中文比对吧  后续改进
			if(detail.getBetName().equals("和值")){
				Map<String,Boolean>  map = getResult(array);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}else{
				return "未知投注";
			}
		}else if(detail.getPlayName().equals("两连")){//两连
			if(detail.getBetName().equals("两连")){
				Map<String,Boolean>  map = getSerial(array);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}else if(detail.getBetName().equals("独胆")){
				Map<String,Boolean>  map = getAlone(array);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}else if(detail.getBetName().equals("豹子")){
				Map<String,Boolean>  map = getLeopard(array);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}else if(detail.getBetName().equals("对子")){
				Map<String,Boolean>  map = getDouble(array);
				if(map.get(detail.getOptionTitle())!=null&&map.get(detail.getOptionTitle())==true){
					return "2";
				}else{
					return "0";
				}
			}else{
				return "未知投注";
			}
		}else{
			return "未知玩法";
		}
	}

	/**
	 *  和值
	 * 
	 */
	public Map<String,Boolean> getResult(String[]  array){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		int sum=0;
		for(int i=0;i<array.length;i++){
			sum=sum+Integer.parseInt(array[i]);
		}
		if(Integer.parseInt(array[0])==Integer.parseInt(array[1]) && Integer.parseInt(array[1])==Integer.parseInt(array[2])){
			
		}else{
			if(sum%2 == 0){
				map.put("双", true);
			}else if(sum%2 == 1){
				map.put("单", true);
			}
			if(sum <= 17 && sum >= 11){
				map.put("大", true);
			}else if( sum >= 4 && sum <= 10){
				map.put("小", true);
			}
		}
		map.put(sum + "", true);
		return map;
	}
	/**
	 * 两连
	 */
	public Map<String, Boolean> getSerial(String[] array){
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(array[0] + "、" + array[1], true);
		map.put(array[0] + "、" + array[2], true);
		map.put(array[1] + "、" + array[2], true);
		map.put(array[1] + "、" + array[0], true);
		map.put(array[2] + "、" + array[0], true);
		map.put(array[2] + "、" + array[1], true);
		return map;
	}
	/**
	 * 独胆
	 */
	public Map<String, Boolean> getAlone(String[] array){
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(array[0] + "", true);
		if(!array[1].equals(array[0])){
			map.put(array[1], true);
		}
		if(!array[2].equals(array[0]) && !array[2].equals(array[1])){
			map.put(array[2], true);			
		}
		return map;
	}
	/**
	 * 豹子
	 */
	public Map<String, Boolean> getLeopard(String[] array){
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if(array[0].equals(array[1]) && array[1].equals(array[2])){
			map.put(array[0] + "、" + array[1] + "、" + array[2], true);
			map.put("任意豹子", true);
		}
		return map;
	}
	/**
	 * 对子
	 */
	public Map<String, Boolean> getDouble(String[] array){
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if(array[0].equals(array[1]) && array[1].equals(array[2])){
		}else{
			if(array[0].equals(array[1])){
				map.put(array[0] + "、" + array[1], true);
			}else if(array[0].equals(array[2])){
				map.put(array[0] + "、" + array[2], true);
			}else if(array[1].equals(array[2])){
				map.put(array[1] + "、" + array[2], true);
			}
		}
		return map;
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		BjK3GaSession curtSession = bjK3DAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		BjK3GaSession lastSession = (BjK3GaSession)bjK3DAO.getObject(BjK3GaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJK3);
		
		//迭代开奖无序
		List<BjK3GaSession> openedList = new ArrayList<BjK3GaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				BjK3GaSession session = bjK3DAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openBjK3SessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenResult(result);//设置开奖号
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							bjK3DAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJK3);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				bjK3DAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(BjK3GaSession session:openedList){
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
//		BjK3GaSession currentSession= bjK3DAO.getCurrentSession();
//		BjK3GaSession tempsession =bjK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession==null){
//			tempsession=(BjK3GaSession) bjK3DAO.getObject(BjK3GaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//		
//			if(!sessionNoMap.isEmpty()){
//				for(String key:sessionNoMap.keySet()){
//					HQUtils hq = new HQUtils("from BjK3GaSession gks where gks.sessionNo=?");
//					hq.addPars(key);
//					BjK3GaSession session = (BjK3GaSession)bjK3DAO.getObject(hq);
//					if(session!=null){
//						String openStatus = session.getOpenStatus();//开奖状态
//						
//						if(openStatus.equals(BjK3Constants.BJK3_OPEN_STATUS_INIT) || openStatus.equals(BjK3Constants.BJK3_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//							//更新开奖结果
//							SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//							String openResult = sessionItem.getResult();
//							session.setOpenResult(openResult);
//							bjK3DAO.saveObject(session);
//							
//							//进入开奖程序方法
//							boolean flag = this.openBjK3SessionOpenResultMethod(session, openResult);
//							
//							if(flag){
//								String countResult="";
//								//更新场次状态，TODO 盈亏数据也需要在开奖
//								session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(BjK3Constants.BJK3_OPEN_STATUS_OPENED);
//								bjK3DAO.saveObject(session);
//							}else{
//								GameHelpUtil.log(Constants.GAME_TYPE_XY_BJK3,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//							}
//						}
//						// 把资金明细里投注记录状态值改为有效
//						userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//								Constants.GAME_TYPE_XY_BJK3, Constants.PUB_STATUS_OPEN);
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJK3);
//				if(sessionInfo!=null){
//					SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//					if(lastItem!=null){
//						sessionInfo.setOpenResult(getCountResult(lastItem.getResult()));
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(DateTimeUtil.StringToDate(lastItem.getTime(),"yyyy-MM-dd HH:mm:ss"));							
//					}
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					bjK3DAO.saveObject(sessionInfo);	
//					CacheUtil.updateGameList();
//				}
//			}
//	}
	
//	public void updateFetchAndOpenResult() {
//		BjK3GaSession currentSession= bjK3DAO.getCurrentSession();
//		BjK3GaSession tempsession =bjK3DAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(BjK3GaSession) bjK3DAO.getObject(BjK3GaSession.class, (currentSession.getSessionId()-1));
//		}
//		final String lastSessionNo=tempsession.getSessionNo();
//		final Map<String,String> sessionNoMap=new HashMap<String,String>();
//		final Map<String,String> timeMap=new HashMap<String,String>();
//		Thread t=new Thread(){
//            public void run(){
//               try {
//            	   Integer countRun=0;
//            	   while(true){
//            		   if(countRun==190){
//            			   interrupt();
//            			   break;
//            		   }
//            		    countRun=countRun+1;
//            	
//            			//从这里 ---------------------------------------------------------------------------
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xybjk3");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    
//	               		log.info("___[bjK3 start fetch result xml data...]________________");
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[bjK3 start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/bjlu28.txt", true);
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
//	               		//到这里 ---------------------------------------------------------------------------		
//	            	    sleep(3000);
//	               		//log.info("___[fetch result xml data]"+resultXML);
//	            	    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/bjlu28.txt", true);
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果5组数字
//	               			String time="";
//	               			if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//		               			//开始解析场次开奖数据
//		               			NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				//数据库来源开彩网
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				sessionNo=sessionNo.substring(sessionNo.length()-9);
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");	
//		               				time = XmlUtil.getElementAttribute((Element)node, "opentime");	
//		               				//数据库来源zao28    https://api.zao28.com
////		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "issue");//期号
////		               				result = XmlUtil.getElementAttribute((Element)node, "srccode");	//开奖结果
////		               				time = XmlUtil.getElementAttribute((Element)node, "time");//开奖时间
//		               				if(i==0){
//		               					sessionNoMap.put("lastNo", sessionNo);
//		               				}
//
//		               				if(ParamUtils.chkString(result)){
//		               					if(result.contains("+")){
//		               						result = result.substring(0, result.indexOf("+"));
//		               					}           					
//		               				}
//	           						if(sessionNoMap.get(sessionNo)==null){
//	           							sessionNoMap.put(sessionNo, result);
//	           							timeMap.put(sessionNo, time);
//	           						}
//	           						if(sessionNo.equals(lastSessionNo)){
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
////	               			interrupt();
////       						break;
//	               		}
//            	   }
//               } catch (Exception e) {
////            	   interrupt();
//               }
//            }
//        };
//        t.start();
//        
//        try {
//			t.join();//该方法是等 t 线程结束以后再执行后面的代码
//			if(!sessionNoMap.isEmpty()){
//				for(String key:sessionNoMap.keySet()){
//					HQUtils hq = new HQUtils("from BjK3GaSession gks where gks.sessionNo=?");
//					hq.addPars(key);
//					BjK3GaSession session = (BjK3GaSession)bjK3DAO.getObject(hq);
//					if(session!=null){
//						String openStatus = session.getOpenStatus();//开奖状态
//						
//						if(openStatus.equals(BjK3Constants.BJK3_OPEN_STATUS_INIT) || openStatus.equals(BjK3Constants.BJK3_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//							//更新开奖结果
//							String openResult = sessionNoMap.get(key);
//							session.setOpenResult(openResult);
//							bjK3DAO.saveObject(session);
//							
//							//进入开奖程序方法
//							boolean flag = this.openBjK3SessionOpenResultMethod(session, openResult);
//							
//							if(flag){
//								String countResult="";
////								Map<String,String>  map=this.openResult(openResult);
////								countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
////								session.setCountResult(countResult);
//								//更新场次状态，TODO 盈亏数据也需要在开奖
////								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(BjK3Constants.BJK3_OPEN_STATUS_OPENED);
//								bjK3DAO.saveObject(session);
////								log.info("___[open result success sessionNO["+sessionNo+"]]");
//							}else{
////								log.info("___[open result fail sessionNO["+sessionNo+"], please check...]");
//							}
//						}
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_BJK3);
//				if(sessionInfo!=null){
//
////					HQUtils hq = new HQUtils("from BjK3GaSession gks where gks.sessionNo=?");
////					hq.addPars(lastSessionNo);
////					BjK3GaSession session = (BjK3GaSession)bjK3DAO.getObject(hq);
////					sessionInfo.setOpenSessionNo(currentSession.getSessionNo());
//					if(sessionNoMap.get(lastSessionNo)!=null){
//
////						String countResult="";
////						Map<String,String>  map=this.openResult(sessionNoMap.get(lastSessionNo));
////						countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
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
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					bjK3DAO.saveObject(sessionInfo);	
//					CacheUtil.updateGameList();
//				}
//				sessionNoMap.clear();
//				timeMap.clear();
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public BjK3GaSession getCurrentSession() {
		return bjK3DAO.getCurrentSession();
	}
	
	public BjK3GaSession getPreviousSessionBySessionNo(String sessionNo) {
		return bjK3DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public void saveUserBetInfo(String room, Map<Integer, Integer> betMap,
			List<GaBetOption> list, BjK3GaSession session, User user,
			BigDecimal betAll) {
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_BJK3);
		//投注与明细关联
		List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
		//用户类型
		String userType = user.getUserType();
		
		//迭代投注选项
		for(GaBetOption betOption:list){
			GaBetDetail betDetail=new GaBetDetail();
			betDetail.setBetRate(betOption.getBetRate());
			betDetail.setWinResult(GameConstants.OPEN_STATUS_INIT);//未开奖
			betDetail.setBetFlag(GameConstants.STATUS_1);//有效投注
			
			betDetail.setSessionId(session.getSessionId());
			betDetail.setUserId(user.getUserId());
			betDetail.setType(userType);
			betDetail.setLoginName(user.getLoginName());
			
			betDetail.setBetOptionId(betOption.getBetOptionId());//投注项
			betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));//投注金额
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());
			betDetail.setGameName(gsi.getGameTitle());
	
			if(betOption.getPlayType().equals("0")){//两面盘
				betDetail.setPlayName("两面盘");
			}else{
				betDetail.setPlayName("两连");
			}
			betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
			betDetail.setOptionTitle(betOption.getOptionTitle());
			betDetail.setGameType(gsi.getGameType());
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			bjK3DAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_BJK3,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		bjK3DAO.updateObjectList(rlList, null);
	}
	
//	public void saveUserBetInfo(String room, Map<Integer, Integer> betMap,
//			List<GaBetOption> list, BjK3GaSession session, User user,
//			BigDecimal betAll) {
//
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
//				betDetail.setWinResult("0");//未开奖
//				betDetail.setBetFlag("1");//有效投注
//				betDetail.setSessionId(session.getSessionId());
//				betDetail.setUserId(user.getUserId());
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
//				betDetail.setSessionNo(session.getSessionNo());
//				betDetail.setGameName("江苏快3");
//
//				if(betOption.getPlayType().equals("0")){//两面盘
//					betDetail.setPlayName("两面盘");
//				}else{
//					betDetail.setPlayName("两连");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_BJK3);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//		}
//		
//
//		
//
////			userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJK3, betAll.subtract(tempMoney), null);
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
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
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_BJK3,remark.toString());
//
//
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//		userService.updateUserBanlance(user.getUserId());
//		
//		bjK3DAO.updateObjectList(betDetailList, null);	
//	}
	
	public List<BjK3GaTrend> findBjK3TrendList() {
		return bjK3DAO.findBjK3GaTrendList();
	}
	
	public PaginationSupport findBjK3GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return bjK3DAO.findBjK3GaSessionList(hql,para,pageNum,pageSize);
	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "和值";
			 }else{
				 return "未知";
			 }
		 }else if(playType.equals("1")){//两连
			 if(optionType.equals("0")){
				 return "两连";
			 }else if(optionType.equals("1")){
				 return "独胆";
			 }else if(optionType.equals("2")){
				 return "豹子";
			 }else if(optionType.equals("3")){
				 return "对子";
			 }else {
				 return "未知";
			 }
		}
		 return "";
	 }
	 
	 public void updateTrendResult(BjK3GaSession session){
		if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
			List<BjK3GaTrend> list=bjK3DAO.findBjK3GaTrendAllList();
			List<BjK3GaTrend> savelist=new ArrayList<BjK3GaTrend>();
			Map<String,Boolean> map=getResult(session.getOpenResult().split(","));
			if(!map.isEmpty()){
				for(BjK3GaTrend trend:list){
					if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
						trend.setTrendCount(trend.getTrendCount()+1);
					}else{
						trend.setTrendCount(0);
					}
					savelist.add(trend);
				}
				bjK3DAO.updateObjectList(savelist, null);
			}
		}
	}
	 
//	 public void updateFetchAndOpenTrendResult(Integer count){
//			if(count==9){//执行10次
//				count=null;
//				return;
//			}
//			BjK3GaSession cureent = bjK3DAO.getCurrentSession();
//			String lastSessionNo=(Integer.parseInt(cureent.getSessionNo())-1)+"";
//			BjK3GaSession session =bjK3DAO.getPreviousSessionBySessionNo(lastSessionNo);
//			if(session == null){
//				session = (BjK3GaSession) bjK3DAO.getObject(BjK3GaSession.class, cureent.getSessionId()-1);
//			}
//			if(session.getOpenStatus().equals(BjK3Constants.BJK3_OPEN_STATUS_OPENED)){
//				List<BjK3GaTrend> list=bjK3DAO.findBjK3GaTrendAllList();
//				List<BjK3GaTrend> savelist=new ArrayList<BjK3GaTrend>();
//				Map<String,Boolean> map=getResult(session.getOpenResult().split(","));
//				if(!map.isEmpty()){
//					for(int i=0;i<list.size();i++){
//						BjK3GaTrend trend=list.get(i);
//						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
//							trend.setTrendCount(trend.getTrendCount()+1);				
//						}else{
//							trend.setTrendCount(0);
//						}
//						savelist.add(trend);
//					}
//					bjK3DAO.updateObjectList(savelist, null);
//					count=null;
//					lastSessionNo=null;
//					session=null;
//					return;
//				}
//
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
//					lastSessionNo=null;
//					session=null;
//					updateFetchAndOpenTrendResult(count);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		public boolean saveOpenResult(BjK3GaSession session,String openResult){
			boolean flag=false;
			session.setOpenResult(openResult);
			bjK3DAO.updateObject(session, null);
			flag=true;
			return flag;
		}
		
		public boolean saveAndOpenResult(BjK3GaSession session,String openResult){
			boolean flag=false;
			session.setOpenResult(openResult);
			boolean flag1 = openBjK3SessionOpenResultMethod(session, openResult);
			if(flag1){      
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(BjK3Constants.BJK3_OPEN_STATUS_OPENED);
				bjK3DAO.updateObject(session, null);
				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
				flag=true;
			}else{
				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			}
			return flag;
		}
		@Override
		public PaginationSupport findBjK3GaBetList(String hql,
				List<Object> para, int pageNum, int pageSize) {
			return bjK3DAO.findBjK3GaBetList(hql, para, pageNum, pageSize);
		}

		@Override
		public PaginationSupport findGaBetDetail(String hql, List<Object> para,
				int pageNum, int pageSize) {
			return bjK3DAO.findGaBetDetail(hql, para, pageNum, pageSize);
		}
		@Override
		public List<BjK3DTO> findGaBetDetailById(String hql,
				List<Object> para) {
			return bjK3DAO.findGaBetDetailById(hql, para);
		}
		
		@Override
		public Map<String, String> judgeCountResult(String openResult) {
			Map<String,String> map=new HashMap<String,String>();
			String[] split = openResult.split(",");

			int number1=Integer.parseInt(split[0]);
			int number2=Integer.parseInt(split[1]);
			int number3=Integer.parseInt(split[2]);
			int number=number1+number2+number3;
			map.put("number1", number1+"");
			map.put("number2", number2+"");
			map.put("number3", number3+"");
			map.put("number", number+"");
//			int number=Integer.parseInt(split[3]);
			if(number1==number2 && number2==number3){
				map.put("花色", "豹子");
				map.put("colour", "3");
				map.put("大小", "-");
				map.put("单双", "-");
			}else{
				if(number>=11&&number<=17){
					map.put("大小", "大");
				}else if(number>=4 && number<=10){
					map.put("大小", "小");
				}
				if(number != 18 && number%2==0){
					map.put("单双", "双");
				}else if(number != 3 && number%2==1){
					map.put("单双", "单");
				}
				if((number1 == number2) ||(number1 == number3) || (number2 == number3)){
					map.put("花色", "对子");
					map.put("colour", "3");
				}else{
					map.put("花色", "-");
					map.put("colour", "3");
				}
			}
			map.put("colour", "3");
			return map;

		}
		
		@Override
		public Map<String, String> getTrendResult(String result){
			Map<String, String> results = new HashMap<String, String>();
			String array[] = result.split(",");
			int sum=0;
			int arr[] = new int[array.length];
			for(int i=0;i<array.length;i++){
				sum=sum+Integer.parseInt(array[i]);
				arr[i] = Integer.parseInt(array[i]);
			}
			results.put("总和", sum + "");
					
			if(arr[0] == arr[1] && arr[1] == arr[2]){//豹子
				results.put("豹子", array[0]);
				results.put("大小","-");
				results.put("单双", "-");
				results.put("对子", "-");
			}else{
				if(sum >= 11 && sum <= 17){
					results.put("大小", "大");
				}else if(sum <= 10 && sum >= 4){
					results.put("大小", "小");
				}
				
				if(sum%2 == 0){
					results.put("单双", "双");
				}else if(sum%2 == 1){
					results.put("单双", "单");
				}
				
				results.put("豹子", "-");
				
				if(arr[0] == arr[1] && arr[1] != arr[2]){
					results.put("对子", array[0]);
				}else if(arr[0] == arr[2] && arr[0] != arr[1]){
					results.put("对子", array[0]);
				}else if(arr[1] == arr[2] && arr[0] != arr[1]){
					results.put("对子", array[1]);
				}else{
					results.put("对子", "-");
				}
			}
			return results;
		}
		
		public String getCountResult(String openResult){
			String countResult="";
			if(ParamUtils.chkString(openResult)){
				String array[]=openResult.split(",");
				int sum=0;				
				for(int i=0;i<array.length;i++){
					sum=sum+Integer.parseInt(array[i]);
				}
				countResult=	openResult+","+sum;
					
				if(array[0].equals(array[1])&&array[0].equals(array[2])){
					countResult=countResult+","+3;
				}else{
					if(array[0].equals(array[1])&&!array[0].equals(array[2])){
						countResult=countResult+","+3;
					}else if(array[0].equals(array[2])&&!array[0].equals(array[1])){
						countResult=countResult+","+3;
					}else if(array[1].equals(array[2])&&!array[0].equals(array[1])){
						countResult=countResult+","+3;
					}else{
						countResult=countResult+","+3;
					}
				}
				return countResult;
			}
			
			return "";
		}
		@Override
		public boolean saveRevokePrize(BjK3GaSession session) {
			//删除BjK3GaBet表的记录
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and sessionId = ? ");
			para.add(session.getSessionId());
			bjK3DAO.deleteBjK3GaBet(hql.toString(),para);

			boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_BJK3,session.getSessionNo());
			if(result){
				session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
				gaService.saveObject(session, null);
			}
			return result;
		}
}
