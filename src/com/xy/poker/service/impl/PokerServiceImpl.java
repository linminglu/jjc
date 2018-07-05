package com.xy.poker.service.impl;

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
import com.xy.poker.PokerConstants;
import com.xy.poker.dao.IPokerDAO;
import com.xy.poker.model.PokerGaBet;
import com.xy.poker.model.PokerGaSession;
import com.xy.poker.model.PokerGaTrend;
import com.xy.poker.model.dto.PokerDTO;
import com.xy.poker.service.IPokerService;
import com.xy.poker.util.PokerUtil;

public class PokerServiceImpl  extends BaseService implements IPokerService {
	private IPokerDAO pokerDAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setPokerDAO(IPokerDAO pokerDAO) {
		this.pokerDAO = pokerDAO;
		super.dao = pokerDAO;
		
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	
	/**
	 * 初始化今天场次
	 */
	public String updateInitTodaySession(){
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		//String today = DateTimeUtil.DateToString(now);
//		Date dateAfter = DateTimeUtil.getDateAfter(now, 1, "yyyy-MM-dd HH:mm:ss");
		String dateAfterString = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<PokerGaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<PokerGaSession>();
			String startTimeStr = dateAfterString + PokerConstants.POKER_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");

			for (int i = 0; i < PokerConstants.POKER_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*PokerConstants.POKER_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				long endDiffTime = startDate.getTime() + (i+1)*PokerConstants.POKER_TIME_INTERVAL * 60 * 1000;
				Date endSessionDate = new Date(endDiffTime);
				
				//String openResult = PokerConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(curSessionDate, i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				PokerGaSession pokerGaSession = new PokerGaSession();
				pokerGaSession.setSessionNo(sessionNo);
				pokerGaSession.setStartTime(curSessionDate);
				pokerGaSession.setEndTime(endSessionDate);
				pokerGaSession.setOpenStatus(PokerConstants.POKER_OPEN_STATUS_INIT);
				//pokerGaSession.setOpenResult(openResult);//开奖号由系统抓取获取得
//				pokerDAO.saveObject(pokerGaSession);
				saveList.add(pokerGaSession);
				
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			pokerDAO.updateObjectList(saveList, null);
			flag = "success";
//			log.info("___[today init completed]__________________________");
		} else {
			flag = "inited";
//			log.info("___[today has been inited]__________________________");
		}
		return flag;
	}
	
		
	
	/**
	 * 初始化场次
	 */
	public String updateInitSession(){
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		//String today = DateTimeUtil.DateToString(now);
		Date dateAfter = DateTimeUtil.getDateAfter(now, 1, "yyyy-MM-dd HH:mm:ss");
		String dateAfterString = DateTimeUtil.DateToString(dateAfter);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(dateAfter);
		List<PokerGaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<PokerGaSession>();
			String startTimeStr = dateAfterString + PokerConstants.POKER_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");

			for (int i = 0; i < PokerConstants.POKER_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*PokerConstants.POKER_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				long endDiffTime = startDate.getTime() + (i+1)*PokerConstants.POKER_TIME_INTERVAL * 60 * 1000;
				Date endSessionDate = new Date(endDiffTime);
				
				//String openResult = PokerConstants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(curSessionDate, i+1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				PokerGaSession pokerGaSession = new PokerGaSession();
				pokerGaSession.setSessionNo(sessionNo);
				pokerGaSession.setStartTime(curSessionDate);
				pokerGaSession.setEndTime(endSessionDate);
				pokerGaSession.setOpenStatus(PokerConstants.POKER_OPEN_STATUS_INIT);
				//pokerGaSession.setOpenResult(openResult);//开奖号由系统抓取获取得
//				pokerDAO.saveObject(pokerGaSession);
				saveList.add(pokerGaSession);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			pokerDAO.updateObjectList(saveList, null);
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
		
		HQUtils hq = new HQUtils("from PokerGaSession bgs where bgs.startTime>? and bgs.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = pokerDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	/**
	 * 获取今天的期号，按流水1-50 201523101 - 201523150
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date date,int index){
		return DateTimeUtil.DateToStringYY(date) + String.format("%02d", index);
	}
	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public boolean openPokerSessionOpenResultMethod(PokerGaSession session,String result){
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_POKER);
		try {
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and ho.gameType=?");
			para.add(Constants.GAME_TYPE_XY_POKER);
			hql.append( " and ho.sessionId =?");
			para.add(session.getSessionId());
			hql.append(" and ho.betFlag=?" );//目前固定参数
			para.add("1");
			//hql.append(" order by ho.betTime desc");
			
			Map<String, Boolean> map = null;
			if(session.getCountResult()!=null){
				map = numberCountResult(session.getCountResult());
			}else{
				map = numberCountResult(PokerUtil.openVer(result));
			}
			//本期全部投注记录
			long startTiming = System.currentTimeMillis();
			List<GaBetDetail> list=gaService.findGaBetDetailList(hql.toString(), para);
			GameHelpUtil.log(gameCode,"BETS ... ["+list.size()+"]["+session.getSessionNo()+"]["+(System.currentTimeMillis()-startTiming)+"ms]");
			
			//本期投注统计表
			PokerGaBet bet=new PokerGaBet();
			BigDecimal totalPoint=new BigDecimal(0);//总投注
			BigDecimal betCash=new BigDecimal(0);//总中奖
			bet.setSessionId(session.getSessionId());
			bet.setSessionNo(session.getSessionNo());

			if(list!=null && !list.isEmpty()){
				//开奖投注用户ids --by.cuisy.20171209
				List<Integer> userIds = new ArrayList<Integer>();
				startTiming = System.currentTimeMillis();
				
				for(GaBetDetail betDetail:list){
					//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
					if(!StringUtil.chkListIntContains(userIds, betDetail.getUserId())){
						userIds.add(betDetail.getUserId());
					}//~
					
					String optionTitle = betDetail.getOptionTitle();
					
					String countResult= session.getCountResult(); //计算结果
					if(countResult == null || countResult.length()<=0){
						countResult = PokerUtil.openVer(result);				
					}
					betDetail.setOpenResult(countResult);

					
					//中奖
					if(map.get(optionTitle) !=null && map.get(optionTitle)==true){
						betDetail.setWinResult(GameConstants.WIN);
						//中奖金额
						BigDecimal wincash=GameHelpUtil.round(betDetail.getBetRate().multiply(new BigDecimal(betDetail.getBetMoney())));
						betDetail.setWinCash(wincash);
						//统计
						totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
						betCash=betCash.add(wincash);
						//盈亏
						betDetail.setPayoff(GameHelpUtil.round(wincash.subtract(new BigDecimal(betDetail.getBetMoney()))));
						
						//备注
						String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
						
						//更新
						updateOpenData(betDetail,remark.toString());
						
					}else{
						if(optionTitle.equals("大")||optionTitle.equals("小")){
							if(map.get("和")!=null&&map.get("和")==true){
								betDetail.setWinResult(GameConstants.WIN_HE);
								//中奖金额
								BigDecimal wincash = GameHelpUtil.round(new BigDecimal(betDetail.getBetMoney()));//投注总钱数
								betDetail.setWinCash(wincash);
								//统计
								totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
								betCash=betCash.add(wincash);
								//盈亏
								betDetail.setPayoff(new BigDecimal(0));
								//备注
								String remark=GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_PRIZE, wincash);
								//更新
								updateOpenData(betDetail,remark.toString());
		
							}else if(optionTitle.equals("大")&&map.get("小")!=null&&map.get("小")==true){
								totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
								betDetail.setWinResult("2");//未中奖
//								betDetail.setWinCash(new BigDecimal("-"+betDetail.getBetMoney()));
								betDetail.setWinCash(new BigDecimal(0));
								try {
									pokerDAO.updateObject(betDetail, null);
								}catch (Exception e) {
									e.printStackTrace();
								}
							}else if(optionTitle.equals("小")&&map.get("大")!=null&&map.get("大")==true){
								betDetail.setWinResult(GameConstants.WIN_NOT);//未中奖
								//统计
								totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
								betDetail.setWinCash(new BigDecimal(0));
								pokerDAO.saveObject(betDetail);
							}
						}else{
							betDetail.setWinResult(GameConstants.WIN_NOT);//未中奖
							//统计
							totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
							betDetail.setWinCash(new BigDecimal(0));
							pokerDAO.saveObject(betDetail);
							pokerDAO.saveObject(betDetail);
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
						Constants.GAME_TYPE_XY_POKER, Constants.PUB_STATUS_OPEN);
				GameHelpUtil.log(gameCode,"BatD ... ["+(System.currentTimeMillis()-timginUtds)+"ms]");
			}
			bet.setTotalPoint(GameHelpUtil.round(totalPoint));
			bet.setWinCash(GameHelpUtil.round(betCash));
			pokerDAO.saveObject(bet);
			
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			GameHelpUtil.log(gameCode,"open err::["+session.getSessionNo()+"]=>"+e.getMessage(),e);
			return false;
		}
		
	}
	
//	public boolean openPokerSessionOpenResultMethod(PokerGaSession pokersession,String result){
//		HQUtils hq1 = new HQUtils("from GaBetDetail gd where gd.sessionId=? and gd.betFlag='1' and gd.gameType='8' ");
//		hq1.addPars(pokersession.getSessionId());
//		String openResult = pokersession.getOpenResult();
//		Map<String, Boolean> map = null;
//		if(pokersession.getCountResult()!=null){
//			map = numberCountResult(pokersession.getCountResult());
//		}else{
//			 map = numberCountResult(PokerUtil.openVer(openResult));
//		}
//		List<Object>  list= pokerDAO.findObjects(hq1);
//		
//		PokerGaBet bet=new PokerGaBet();
//		BigDecimal  totalPoint=new BigDecimal(0);
//		BigDecimal  betCash=new BigDecimal(0);
//		bet.setSessionId(pokersession.getSessionId());
//		bet.setSessionNo(pokersession.getSessionNo());
//
//		if(list!=null && list.size()>0){
//			//开奖投注用户ids --by.cuisy.20171209
//			List<Integer> userIds = new ArrayList<Integer>();
//			
//			for(Object object:list){
//				GaBetDetail betDetail=(GaBetDetail) object;
//				//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//				if(!StringUtil.chkListIntContains(userIds, betDetail.getUserId())){
//					userIds.add(betDetail.getUserId());
//				}//~
//				
//				String optionTitle = betDetail.getOptionTitle();
//				Integer userId = betDetail.getUserId();
//				
//				StringBuffer remark=new StringBuffer();
//				String countResult= pokersession.getCountResult(); //计算结果
//				if(countResult == null || countResult.length()<=0){
//					countResult = PokerUtil.openVer(openResult);				
//				}
//				betDetail.setOpenResult(countResult);
//
//				
//				//中奖
//				if(map.get(optionTitle) !=null && map.get(optionTitle)==true){
//					betDetail.setWinResult("1");//中奖
////					int winCash = (new BigDecimal(betDetail.getBetMoney())).add(new BigDecimal(new BigDecimal(betDetail.getBetMoney()).multiply(betDetail.getBetRate()).intValue())).intValue();					
////					betDetail.setWinCash(new BigDecimal(new BigDecimal(betDetail.getBetMoney()).multiply(betDetail.getBetRate()).intValue()));
//					BigDecimal wincash=betDetail.getBetRate().multiply(new BigDecimal(betDetail.getBetMoney()).setScale(2, BigDecimal.ROUND_HALF_UP));
//					betDetail.setWinCash(wincash);
//					totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
//					betCash=betCash.add(wincash);
//					betDetail.setPayoff(wincash.subtract(new BigDecimal(betDetail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
//
//					
////					userService.saveTradeDetail(user,betDetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_POKER, wincash, betDetail.getBetDetailId());				
//					remark.append("彩票中奖 奖金 ")
//					    .append(wincash).append("元");
////					user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, betDetail.getBetDetailId(), Constants.GAME_TYPE_XY_POKER,remark.toString());
////					User user = userService.getUser(userId);
////					BigDecimal userBal=null;
////					if(user.getUserBalance()!=null){
////						userBal=user.getUserBalance();
////					}else{
////						userBal=new BigDecimal(0);
////					}
////					user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_HALF_UP));
//
//					try {
//						updateOpenData(betDetail,null,remark.toString());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//				}else{
//					if(optionTitle.equals("大")||optionTitle.equals("小")){
//						if(map.get("和")!=null&&map.get("和")==true){
//							BigDecimal wincash=new BigDecimal(betDetail.getBetMoney());
//							totalPoint=totalPoint.add(wincash);
//							betDetail.setWinResult("3");//打和
//							betDetail.setWinCash(wincash);
//
////							userService.saveTradeDetail(user,betDetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_POKER, wincash, betDetail.getBetDetailId());		
//
//							remark.append("彩票打和 退款")
//							    .append(wincash).append("元");
////							user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, betDetail.getBetDetailId(), Constants.GAME_TYPE_XY_POKER,remark.toString());
//							
//
//							betCash=betCash.add(wincash);
////							BigDecimal userBal=null;
////							User user = userService.getUser(userId);
////							if(user.getUserBalance()!=null){
////								userBal=user.getUserBalance();
////							}else{
////								userBal=new BigDecimal(0);
////							}
////							user.setUserBalance(userBal.add(wincash));
//							
//							try {
//								updateOpenData(betDetail,null,remark.toString());
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//	
//						}else if(optionTitle.equals("大")&&map.get("小")!=null&&map.get("小")==true){
//							totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
//							betDetail.setWinResult("2");//未中奖
////							betDetail.setWinCash(new BigDecimal("-"+betDetail.getBetMoney()));
//							betDetail.setWinCash(new BigDecimal(0));
//							try {
//								pokerDAO.updateObject(betDetail, null);
//							}catch (Exception e) {
//								e.printStackTrace();
//							}
//						}else if(optionTitle.equals("小")&&map.get("大")!=null&&map.get("大")==true){
//							totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
//							betDetail.setWinResult("2");//未中奖
////							betDetail.setWinCash(new BigDecimal("-"+betDetail.getBetMoney()));
//							betDetail.setWinCash(new BigDecimal(0));
//							try {
//								pokerDAO.updateObject(betDetail, null);
//							}catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}else{
//						totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
//						betDetail.setWinResult("2");//未中奖
////						betDetail.setWinCash(new BigDecimal("-"+betDetail.getBetMoney()));
//						betDetail.setWinCash(new BigDecimal(0));
//						try {
//							pokerDAO.updateObject(betDetail, null);
//						}catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			//批量更新开奖用户实时余额 --by.cuisy.20171209
//			userService.updateUserMoney(userIds);
//		}
//		
//		bet.setTotalPoint(totalPoint);
//		bet.setWinCash(betCash);
//		try {
//			pokerDAO.saveObject(bet, null);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		return true;
//	}
	
	public void updateOpenData(GaBetDetail detail,String remark){
		pokerDAO.saveObject(detail);
		userService.saveTradeDetail(null,detail.getUserId(), 
				Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), 
				detail.getBetDetailId(), 
				Constants.GAME_TYPE_XY_POKER,
				remark,detail.getSessionNo(),detail.getType(),detail.getLoginName());
	}
	
//	public void updateOpenData(GaBetDetail detail,User user,String remark){
//		pokerDAO.saveObject(detail);
//		user=userService.saveTradeDetail(user,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), Constants.GAME_TYPE_XY_POKER,remark);
//	}
	
	/**
	 * 中奖结果
	 * @param openResult
	 * @return
	 */
	public Map<String,Boolean>  numberResult(String openResult){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		String[] split = openResult.split(",");
	
		int number=Integer.parseInt(split[3]);
		int number4=Integer.parseInt(split[4]);
		if(number>=0&&number<=3){
			map.put("小", true);
		}else if(number>=6 && number<=9){
			map.put("大", true);
		}else{
			map.put("和", true);
		}
		if(number%2==0){
			map.put("双", true);
		}else if(number%2!=0){
			map.put("单", true);
		}

		if(number4==1){
			map.put("对子", true);
		}else if(number4==2){
			map.put("同花", true);
		}else if(number4==3){
			map.put("顺子", true);
		}else if(number4==4){
			map.put("同花顺", true);
		}else if(number4==5){
			map.put("豹子", true);
		}
		return map;
	}
	
	
	/**
	 * 中奖结果
	 * @param openResult
	 * @return
	 */
	public Map<String,Boolean>  numberCountResult(String openResult){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		String[] split = openResult.split(",");

		int number=Integer.parseInt(split[3]);
		int number4=Integer.parseInt(split[4]);

		if(number>=0&&number<=3){
			map.put("小", true);
		}else if(number>=6 && number<=9){
			map.put("大", true);
		}else{
			map.put("和", true);
		}
		if(number%2==0){
			map.put("双", true);
		}else if(number%2!=0){
			map.put("单", true);
		}
		if(number4==1){
			map.put("对子", true);
		}else if(number4==2){
			map.put("同花", true);
		}else if(number4==3){
			map.put("顺子", true);
		}else if(number4==4){
			map.put("同花顺", true);
		}else if(number4==5){
			map.put("豹子", true);
		}else{
			map.put("散牌", true);
		}
		map.put(""+number, true);
		return map;
	}
	
	
	public Map<String,String>  openCountResult(String openResult){
		Map<String,String> map=new HashMap<String,String>();
		String[] split = openResult.split(",");

		int number1=Integer.parseInt(split[0]);
		int number2=Integer.parseInt(split[1]);
		int number3=Integer.parseInt(split[2]);
		int number=Integer.parseInt(split[3]);
		int number4=Integer.parseInt(split[4]);
		map.put("number1", number1+"");
		map.put("number2", number2+"");
		map.put("number3", number3+"");
		map.put("number", number+"");
		map.put("colour", number4+"");
		


		if(number>=0&&number<=3){
			map.put("大小", "小");
		}else if(number>=6 && number<=9){
			map.put("大小", "大");
		}else{
			map.put("大小", "和");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}
		if(number1==number2 && number2==number3){
			map.put("豹子", "豹子");
		}else{
			map.put("豹子", "-");
		}
		return map;
	}
	
	
	public Map<String,String>  judgeCountResult(String openResult){
		Map<String,String> map=new HashMap<String,String>();
		String[] split = openResult.split(",");

		int number1=Integer.parseInt(split[0]);
		int number2=Integer.parseInt(split[1]);
		int number3=Integer.parseInt(split[2]);
		int number=Integer.parseInt(split[3]);
		int number4=Integer.parseInt(split[4]);
		if(number>=6&&number<=9){
			map.put("大小", "大");
		}else if(number>=0 && number<=3){
			map.put("大小", "小");
		}else{
			map.put("大小", "和");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}

		if(number4==1){//绿波
			map.put("花色", "对子");
		}else if(number4==2){//蓝波
			map.put("花色", "同花");
		}else if(number4==3){//红波
			map.put("花色", "顺子");
		}else if(number4==4){//红波
			map.put("花色", "同花顺");
		}else{
			map.put("花色", "-");//无花色
		}
		if(number1==number2 && number2==number3){
			map.put("number", number1+"");
			map.put("豹子", "豹子");
		}else{
			map.put("豹子", "-");
		}
		return map;
	}
	
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap) {
		if(sessionNoMap==null || sessionNoMap.isEmpty()) return "fail::no open sessionNo";
		//当前场次及开奖场次处理
		PokerGaSession curtSession = pokerDAO.getCurrentSession();
		if(curtSession==null) return "fail::no current session";
		PokerGaSession lastSession = (PokerGaSession)pokerDAO.getObject(PokerGaSession.class,curtSession.getSessionId()-1);
		if(lastSession==null) return "fail::no last session::id="+curtSession.getSessionId();
		
		//开奖场次期号
		String lastSessionNo = lastSession.getSessionNo();
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_POKER);
		
		//迭代开奖无序
		List<PokerGaSession> openedList = new ArrayList<PokerGaSession>();//开奖场次
		String sNo;//期号
		SessionItem sessionItem;//开奖结果
		String status;//状态
		String result;//开奖号
		try {
			GameHelpUtil.log(gameCode,"-------- OPENing --------");
			for(Map.Entry<String, SessionItem> entry:sessionNoMap.entrySet()){
				sNo = entry.getKey();sessionItem = entry.getValue();
				PokerGaSession session = pokerDAO.getPreviousSessionBySessionNo(sNo);
				if(session!=null){//开奖
					status = session.getOpenStatus();
					result = sessionItem.getResult();
					session.setOpenResult(result);//设置开奖号
					String countResult=PokerUtil.openVer(result);//计算开奖号
					session.setCountResult(countResult);
					if(status.equals(GameConstants.OPEN_STATUS_INIT) || status.equals(GameConstants.OPEN_STATUS_OPENING)){
						GameHelpUtil.log(gameCode,"Start ... ["+sNo+"],status="+status+",result="+result);
						long timingOpen = System.currentTimeMillis();
						boolean flag = openPokerSessionOpenResultMethod(session, result);
						if(flag){
							session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());//本系统开奖时间
							session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
							
							pokerDAO.saveObject(session);
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
			GaSessionInfo sessionInfo = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_POKER);
			if(sessionInfo!=null){
				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
				if(lastItem!=null){
					sessionInfo.setOpenResult(lastItem.getResult());
					sessionInfo.setOpenSessionNo(lastSessionNo);
					sessionInfo.setEndTime(lastSession.getEndTime());
				}
				sessionInfo.setLatestSessionNo(curtSession.getSessionNo());
				pokerDAO.saveObject(sessionInfo);
				CacheUtil.updateGameList();
				GameHelpUtil.log(gameCode,"gsi last ... ["+(System.currentTimeMillis()-timingGsi)+"ms]"+lastSessionNo+","+lastItem.getResult());
			}
			
			//更新走势 上面已成功开奖的场次
			long startTrending = System.currentTimeMillis();
			for(PokerGaSession session:openedList){
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
//		PokerGaSession currentSession= pokerDAO.getCurrentSession();
//		final String lastSessionNo=(Integer.parseInt(currentSession.getSessionNo())-1)+"";
//		
//		if(!sessionNoMap.isEmpty()){
//			for(String key:sessionNoMap.keySet()){
//				HQUtils hq = new HQUtils("from PokerGaSession gks where gks.sessionNo=?");
//				hq.addPars(key);
//				PokerGaSession session = (PokerGaSession)pokerDAO.getObject(hq);
//				if(session!=null){
//					String openStatus = session.getOpenStatus();//开奖状态
//					
//					if(openStatus.equals(PokerConstants.POKER_OPEN_STATUS_INIT) || openStatus.equals(PokerConstants.POKER_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//						//更新开奖结果
//						SessionItem sessionItem = (SessionItem)sessionNoMap.get(key);
//						String openResult = sessionItem.getResult();
//						session.setOpenResult(openResult);
//						String countResult=PokerUtil.openVer(openResult);
//						session.setCountResult(countResult);
//						//进入开奖程序方法
//						boolean flag = this.openPokerSessionOpenResultMethod(session, countResult);
//						
//						if(flag){
//							//更新场次状态，TODO 盈亏数据也需要在开奖
//							session.setOpenTime(DateTimeUtil.stringToDate(sessionItem.getTime(), "yyyy-MM-dd HH:mm:ss"));
//							session.setOpenStatus(PokerConstants.POKER_OPEN_STATUS_OPENED);
//							pokerDAO.saveObject(session);
//						}else{
//							GameHelpUtil.log(Constants.GAME_TYPE_XY_POKER,"open flag FAIL -_- ["+session.getSessionNo()+"]");
//						}
//					}
//					// 把资金明细里投注记录状态值改为有效
//					userService.updateUserTradeDetailStatus(session.getSessionNo(), 
//							Constants.GAME_TYPE_XY_POKER, Constants.PUB_STATUS_OPEN);
//				}
//			}
//			
//			GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_POKER);
//			if(sessionInfo!=null){
//				SessionItem lastItem = (SessionItem)sessionNoMap.get(lastSessionNo);//上一期
//				if(lastItem!=null){
//					String countResult=PokerUtil.openVer(lastItem.getResult());
//					sessionInfo.setOpenResult(countResult);
//					sessionInfo.setOpenSessionNo(lastSessionNo);
//					sessionInfo.setEndTime(pokerDAO.getPreviousSessionBySessionNo(lastSessionNo).getEndTime());
//				}
//				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//				
//				pokerDAO.saveObject(sessionInfo);
//				CacheUtil.updateGameList();
//			}
//			sessionNoMap.clear();
//		}
//	}
	
//	public void updateFetchAndOpenResult() {
//		PokerGaSession currentSession= pokerDAO.getCurrentSession();
//		PokerGaSession tempsession =pokerDAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
//		if(tempsession!=null){
//		}else{
//			tempsession=(PokerGaSession) pokerDAO.getObject(PokerGaSession.class, (currentSession.getSessionId()-1));
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
//	               		//从这里 ---------------------------------------------------------------------------
//            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("xypoker");
//            		    String officalURL ="";
//            		    String urlSwitch=sessionInfo.getUrlSwitch();
//            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }else if(urlSwitch.equals("2")){
//            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
//            		    }
//            		    sessionInfo=null;
//            		    
//	               		log.info("___[poker start fetch result xml data...]________________");
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[poker start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/poker.txt", true);
//
//	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
//	               		//到这里 ---------------------------------------------------------------------------		
//	            	    sleep(3000);
//	            	    ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/poker.txt", true);
//	               		//log.info("___[fetch result xml data]"+resultXML);
//	               		if(ParamUtils.chkString(resultXML)){
//	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
//	               			String sessionNo="";//场次号
//	               			String result="";//开奖结果5组数字
//	               			String time="";//开奖结果5组数字	         
//	               			
//	               			if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
//		               			//开始解析场次开奖数据
//		               			NodeList nList = xmlDoc.getElementsByTagName("row");
//		               			for(int i =0;i<nList.getLength();i++){
//		               				Node node = nList.item(i);
//		               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
//		               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
//		               				time = XmlUtil.getElementAttribute((Element)node, "opentime");
////		               				if(ParamUtils.chkString(result)){
////		               					result = result.substring(0, result.indexOf("+"));
////		               				}
//		               				sessionNo=sessionNo.substring(2);
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
//		               				sessionNo=sessionNo.substring(2);
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
//            	   interrupt();
//               }
//            }
//        };
//        t.start();
//        
//        try {
//			t.join();//该方法是等 t 线程结束以后再执行后面的代码
//			if(!sessionNoMap.isEmpty()){
//				for(String key:sessionNoMap.keySet()){
//					HQUtils hq = new HQUtils("from PokerGaSession gks where gks.sessionNo=?");
//					hq.addPars(key);
//					PokerGaSession session = (PokerGaSession)pokerDAO.getObject(hq);
//					if(session!=null){
//						String openStatus = session.getOpenStatus();//开奖状态
//						
//						if(openStatus.equals(PokerConstants.POKER_OPEN_STATUS_INIT) || openStatus.equals(PokerConstants.POKER_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
//							//更新开奖结果
//							String openResult = sessionNoMap.get(key);
//							session.setOpenResult(openResult);
////							pokerDAO.saveObject(session);
//							String countResult=PokerUtil.openVer(openResult);
//							session.setCountResult(countResult);
//							//进入开奖程序方法
//							boolean flag = this.openPokerSessionOpenResultMethod(session, countResult);
//							
//							if(flag){
////								Map<String,String>  map=this.openResult(openResult);
////								countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
//			
//								//更新场次状态，TODO 盈亏数据也需要在开奖
////								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
//								session.setOpenStatus(PokerConstants.POKER_OPEN_STATUS_OPENED);
//								pokerDAO.saveObject(session);
////								log.info("___[open result success sessionNO["+sessionNo+"]]");
//							}else{
////								log.info("___[open result fail sessionNO["+sessionNo+"], please check...]");
//							}
//						}
//					}
//				}
//				
//				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_XY_POKER);
//				if(sessionInfo!=null){
//
////					HQUtils hq = new HQUtils("from PokerGaSession gks where gks.sessionNo=?");
////					hq.addPars(lastSessionNo);
////					PokerGaSession session = (PokerGaSession)pokerDAO.getObject(hq);
////					sessionInfo.setOpenSessionNo(currentSession.getSessionNo());
//					if(sessionNoMap.get(lastSessionNo)!=null){
//						String countResult=PokerUtil.openVer(sessionNoMap.get(lastSessionNo));
//						sessionInfo.setOpenResult(countResult);
//						sessionInfo.setOpenSessionNo(lastSessionNo);
//						sessionInfo.setEndTime(tempsession.getEndTime());
//					}
//					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
//					
//					pokerDAO.saveObject(sessionInfo);
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
	
	public PokerGaSession getCurrentSession() {
		return pokerDAO.getCurrentSession();
	}
	
	public PokerGaSession getPreviousSessionBySessionNo(String sessionNo) {
		return pokerDAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
			List<GaBetOption> list, PokerGaSession session, User user,
			BigDecimal betAll) {
		//彩种缓存
		GaSessionInfo gsi = CacheUtil.getGaSessionInfo(Constants.GAME_TYPE_XY_POKER);
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
			
			betDetail.setBetOptionId(betOption.getBetOptionId());
			betDetail.setBetTime(DateTimeUtil.getNow());//投注时间
			betDetail.setRoom(room);//房间目前固定A
			betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
			
			betDetail.setRoom(room);
			betDetail.setSessionNo(session.getSessionNo());
			betDetail.setGameName(gsi.getGameTitle());
			betDetail.setGameType(gsi.getGameType());

			if(betOption.getPlayType().equals("0")){//两面盘
				betDetail.setPlayName("两面盘");
			}else{
				betDetail.setPlayName("特码");
			}
			betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
			betDetail.setOptionTitle(betOption.getOptionTitle());
			
			//不能为空字段初始化
			betDetail.setBetId(GameConstants.DEF_ID);//目前未用默认0
			betDetail.setPaperMoney(new BigDecimal(GameConstants.DEF_NUMBER));//目前未用默认0 红包
			betDetail.setWinCash(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			betDetail.setPayoff(new BigDecimal(GameConstants.DEF_NUMBER));//初始值
			
			pokerDAO.saveObject(betDetail);
			
			//关联构造
			UserTradeDetailRl rl = new UserTradeDetailRl();
			rl.setBetDetailId(betDetail.getBetDetailId());
			rlList.add(rl);
		}
		
		//更新账户信息
		String remark = GameHelpUtil.getRemark(Constants.CASH_TYPE_CASH_BUY_LOTO, betAll);
		Integer tradeDetailId = userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll, null, 
				Constants.GAME_TYPE_XY_POKER,remark,session.getSessionNo(),user.getUserType(),user.getLoginName());

		//更新用户实时余额  --by.cuisy.20171209
		userService.updateUserMoney(user.getUserId());
		userService.updateUserBanlance(user.getUserId());
		
		//保存关联
		for(UserTradeDetailRl rl:rlList){
			rl.setTradeDetailId(tradeDetailId);
			rl.setGfxy(Constants.GAME_PLAY_CATE_XY);
		}
		pokerDAO.updateObjectList(rlList, null);
	
		return user;
	}
	
//	public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
//			List<GaBetOption> list, PokerGaSession session, User user,
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
//				betDetail.setGameName("快乐扑克3");
//
//				if(betOption.getPlayType().equals("0")){//两面盘
//					betDetail.setPlayName("两面盘");
//				}else{
//					betDetail.setPlayName("特码");
//				}
//				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
//				betDetail.setOptionTitle(betOption.getOptionTitle());
//				betDetail.setGameType(Constants.GAME_TYPE_XY_POKER);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
//				betDetailList.add(betDetail);
//		}
//		
//		StringBuilder remark = new StringBuilder();
//		remark.append("购买彩票 扣款 ")
//		    .append(betAll.subtract(tempMoney)).append("元");
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_XY_POKER,remark.toString());
//
//		//更新用户实时余额  --by.cuisy.20171209
//		userService.updateUserMoney(user.getUserId());
//		userService.updateUserBanlance(user.getUserId());
//		
//		pokerDAO.updateObjectList(betDetailList, null);
//	
//		return user;
//	}
	
	public List<PokerGaTrend> findPokerTrendList() {
		return pokerDAO.findPokerGaTrendList();
	}
	
	public PaginationSupport findPokerGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return pokerDAO.findPokerGaSessionList(hql,para,pageNum,pageSize);
	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "双面";
			 }else if(optionType.equals("1")){
				 return "花色";
			 }
		 }else if(playType.equals("1")){//1-10名
			 if(optionType.equals("0")){
				 return "特码";
			 }
		}
		 return "";
	 }
	 
	 public void updateTrendResult(PokerGaSession session){
			if(session.getOpenStatus().equals(GameConstants.OPEN_STATUS_OPENED)){
				List<PokerGaTrend> list=pokerDAO.findPokerGaTrendAllList();
				List<PokerGaTrend> savelist=new ArrayList<PokerGaTrend>();
				Map<String,Boolean> map=numberResult(session.getCountResult());
				if(!map.isEmpty()){
					for(int i=0;i<list.size();i++){
						PokerGaTrend trend=list.get(i);
						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
							trend.setTrendCount(trend.getTrendCount()+1);				
						}else{
							trend.setTrendCount(0);
						}
						savelist.add(trend);
					}
					pokerDAO.updateObjectList(savelist, null);
				}
			}
		}
	 
//	 public void updateFetchAndOpenTrendResult(Integer count){
//			if(count==9){//执行10次
//				count=null;
//				return;
//			}
//			String lastSessionNo=(Integer.parseInt(pokerDAO.getCurrentSession().getSessionNo())-1)+"";
//			PokerGaSession session =pokerDAO.getPreviousSessionBySessionNo(lastSessionNo);
//			if(session.getOpenStatus().equals(PokerConstants.POKER_OPEN_STATUS_OPENED)){
//				List<PokerGaTrend> list=pokerDAO.findPokerGaTrendAllList();
//				List<PokerGaTrend> savelist=new ArrayList<PokerGaTrend>();
//				Map<String,Boolean> map=numberResult(session.getCountResult());
//				if(!map.isEmpty()){
//					for(int i=0;i<list.size();i++){
//						PokerGaTrend trend=list.get(i);
//						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
//							trend.setTrendCount(trend.getTrendCount()+1);				
//						}else{
//							trend.setTrendCount(0);
//						}
//						savelist.add(trend);
//					}
//					pokerDAO.updateObjectList(savelist, null);
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
		public boolean saveOpenResult(PokerGaSession session,String openResult){
			boolean flag=false;
//			session.setOpenResult(openResult);
			session.setCountResult(openResult);
			pokerDAO.updateObject(session, null);
			flag=true;
			return flag;
		}
		
		public boolean saveAndOpenResult(PokerGaSession session,String openResult){

			boolean flag=false;
			session.setOpenResult(openResult); //这里是用户手动输入的值。没有经过计算。
//			session.setCountResult(openResult);
			boolean flag1 = openPokerSessionOpenResultMethod(session, session.getOpenResult());
			if(flag1){      
				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
				session.setOpenStatus(PokerConstants.POKER_OPEN_STATUS_OPENED);
				pokerDAO.updateObject(session, null);
				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
					flag=true;
			}else{
				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
			}
			return flag;
		}
		@Override
		public PaginationSupport findPokerGaBetList(String hql,
				List<Object> para, int pageNum, int pageSize) {
			return pokerDAO.findPokerGaBetList(hql, para, pageNum, pageSize);
		}

		@Override
		public PaginationSupport findGaBetDetail(String hql, List<Object> para,
				int pageNum, int pageSize) {
			return pokerDAO.findGaBetDetail(hql, para, pageNum, pageSize);
		}
		@Override
		public List<PokerDTO> findGaBetDetailById(String hql,
				List<Object> para) {
			return pokerDAO.findGaBetDetailById(hql, para);
		}
		@Override
		public boolean saveRevokePrize(PokerGaSession session) {
			//删除PokerGaBet表的记录
			List<Object> para = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" and sessionId = ? ");
			para.add(session.getSessionId());
			pokerDAO.deletePokerGaBet(hql.toString(),para);

			boolean result = gaService.saveXyRevokePrize(session.getSessionId(), Constants.GAME_TYPE_XY_POKER,session.getSessionNo());
			if(result){
				session.setOpenStatus(Constants.OPEN_STATUS_INIT);//未开奖
				gaService.saveObject(session, null);
			}
			return result;
		}
}
