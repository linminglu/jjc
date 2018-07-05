package com.gf.bjkl8.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.gf.bjkl8.BjKl8Constants;
import com.gf.bjkl8.dao.IBjKl8DAO;
import com.gf.bjkl8.model.GfBjKl8GaBet;
import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.GfBjKl8GaTrend;
import com.gf.bjkl8.model.dto.GfBjKl8DTO;
import com.gf.bjkl8.service.IBjKl8Service;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.xml.XmlUtil;
import com.game.GameConstants;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.service.IGaService;
import com.gf.k3.jsk3.model.GfJsK3GaSession;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.URLUtil;

public class BjKl8ServiceImpl  extends BaseService implements IBjKl8Service {
	private IBjKl8DAO gfBjKl8DAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setGfBjKl8DAO(IBjKl8DAO gfBjKl8DAO) {
		this.gfBjKl8DAO = gfBjKl8DAO;
		super.dao = gfBjKl8DAO;
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
	public String updateInitTodaySession(String sessionNo1){
//		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		//String today = DateTimeUtil.DateToString(now);
//		Date dateAfter = DateTimeUtil.getDateAfter(now, 1, "yyyy-MM-dd HH:mm:ss");
		String dateAfterString = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		List<GfBjKl8GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<GfBjKl8GaSession>();
			String startTimeStr = dateAfterString + BjKl8Constants.LUCKY28_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			HQUtils hq = new HQUtils("from GfBjKl8GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(now, 1));
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=gfBjKl8DAO.findObjects(hq);
			GfBjKl8GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(GfBjKl8GaSession) list.get(0);
			}
			for (int i = 0; i < BjKl8Constants.LUCKY28_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjKl8Constants.LUCKY28_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				long endDiffTime = startDate.getTime() + (i+1)*BjKl8Constants.LUCKY28_TIME_INTERVAL * 60 * 1000;
				Date endSessionDate = new Date(endDiffTime);
				
				//String openResult = BjLu28Constants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,sessionNo1);//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				GfBjKl8GaSession bjLu28GaSession = new GfBjKl8GaSession();
				bjLu28GaSession.setSessionNo(sessionNo);
				bjLu28GaSession.setStartTime(curSessionDate);
				bjLu28GaSession.setEndTime(endSessionDate);
				bjLu28GaSession.setOpenStatus(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT);
				//bjLu28GaSession.setOpenResult(openResult);//开奖号由系统抓取获取得
//				bjLu28DAO.saveObject(bjLu28GaSession);
				saveList.add(bjLu28GaSession);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			gfBjKl8DAO.updateObjectList(saveList, null);
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
		List<GfBjKl8GaSession> saveList = null;
		if(!isTodaySessionInit){
//			log.info("___[start today]__________________________");
			saveList = new ArrayList<GfBjKl8GaSession>();
			String startTimeStr = dateAfterString + BjKl8Constants.LUCKY28_START_TIME_S;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
//			log.info("___[startTimeStr="+startTimeStr+"]");
			HQUtils hq = new HQUtils("from GfBjKl8GaSession gks where gks.startTime>? and gks.startTime<? order by gks.sessionId desc ");
			String todayYyyymmdd = DateTimeUtil.DateToString(new Date());
			Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
			Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
			hq.addPars(todayStart);
			hq.addPars(todayEnd);
			List<Object> list=gfBjKl8DAO.findObjects(hq);
			GfBjKl8GaSession beforeSession=null;
			if(list!=null&&list.size()>0){
				beforeSession=(GfBjKl8GaSession) list.get(0);
			}
			for (int i = 0; i < BjKl8Constants.LUCKY28_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*BjKl8Constants.LUCKY28_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				long endDiffTime = startDate.getTime() + (i+1)*BjKl8Constants.LUCKY28_TIME_INTERVAL * 60 * 1000;
				Date endSessionDate = new Date(endDiffTime);
				
				//String openResult = BjLu28Constants.getRandomK10Result();//随机生成5个中奖数字[1-21]
				String sessionNo = this.getTodaySessionNo(beforeSession, i+1,"");//期号
				log.info("___[start today]__________________________sessionNo:"+sessionNo);
				GfBjKl8GaSession bjKl8GaSession = new GfBjKl8GaSession();
				bjKl8GaSession.setSessionNo(sessionNo);
				bjKl8GaSession.setStartTime(curSessionDate);
				bjKl8GaSession.setEndTime(endSessionDate);
				bjKl8GaSession.setOpenStatus(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT);
				//bjLu28GaSession.setOpenResult(openResult);//开奖号由系统抓取获取得
//				bjLu28DAO.saveObject(bjLu28GaSession);
				saveList.add(bjKl8GaSession);
				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			gfBjKl8DAO.updateObjectList(saveList, null);
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
		
		HQUtils hq = new HQUtils("from GfBjKl8GaSession bgs where bgs.startTime>? and bgs.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gfBjKl8DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	/**
	 * 获取今天的期号，按流水1-50 201523101 - 201523150
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(GfBjKl8GaSession beforeSession,int index,String sessionNo1){
//		if(beforeSession==null){
//			return  (817217+ index)+"";
//		}else
		if(ParamUtils.chkString(sessionNo1.trim())){
			return  (Integer.parseInt(sessionNo1.trim())+ index)+"";
		}else{
			return  (Integer.parseInt(beforeSession.getSessionNo())+ index)+"";
		}
	
	}
	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public boolean updateBjKl8SessionOpenResultMethod(GfBjKl8GaSession lu28session,String result){
//		HQUtils hq = new HQUtils("from BjLu28GaSession bgs where bgs.sessionNo=?");
//		String sessionNo = lu28session.getSessionNo();
//		hq.addPars(sessionNo);
//		BjLu28GaSession session = (BjLu28GaSession)bjLu28DAO.getObject(hq);
		HQUtils hq1 = new HQUtils("from GaBetDetail gd where gd.sessionId=? and gd.betFlag='1' and gd.gameType='4' ");
		hq1.addPars(lu28session.getSessionId());
		String openResult = lu28session.getOpenResult();
		Map<String, Boolean> map = null;
		if(lu28session.getCountResult()!=null){
			map = numberCountResult(lu28session.getCountResult());
		}else{
			 map = numberResult(openResult);
		}
		List<Object>  list= gfBjKl8DAO.findObjects(hq1);
		
		GfBjKl8GaBet bet=new GfBjKl8GaBet();
		BigDecimal  totalPoint=new BigDecimal(0);
		BigDecimal  betCash=new BigDecimal(0);
		bet.setSessionId(lu28session.getSessionId());
		bet.setSessionNo(lu28session.getSessionNo());
		
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				GaBetDetail betDetail = (GaBetDetail)list.get(i);
				String optionTitle = betDetail.getOptionTitle();
				Integer userId = betDetail.getUserId();
				User user = userService.getUser(userId);
				//中奖
				if(map.get(optionTitle) !=null && map.get(optionTitle)){
					betDetail.setWinResult("1");//中奖
//					int winCash = (new BigDecimal(betDetail.getBetMoney())).add(new BigDecimal(new BigDecimal(betDetail.getBetMoney()).multiply(betDetail.getBetRate()).intValue())).intValue();					
//					betDetail.setWinCash(new BigDecimal(new BigDecimal(betDetail.getBetMoney()).multiply(betDetail.getBetRate()).intValue()));
					BigDecimal wincash=betDetail.getBetRate().multiply(new BigDecimal(betDetail.getBetMoney()).setScale(2, BigDecimal.ROUND_DOWN));
					betDetail.setWinCash(wincash);
					totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
					betCash=betCash.add(wincash);
					betDetail.setPayoff(wincash.subtract(new BigDecimal(betDetail.getBetMoney())).setScale(2, BigDecimal.ROUND_DOWN));
					BigDecimal userBal=null;
					if(user.getUserBalance()!=null){
						userBal=user.getUserBalance();
					}else{
						userBal=new BigDecimal(0);
					}
					user.setUserBalance(userBal.add(wincash).setScale(2, BigDecimal.ROUND_DOWN));
//					userService.saveTradeDetail(user,betDetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_BET_BJLU28, wincash, betDetail.getBetDetailId());						
//					userService.savePointDetail(betDetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BJ_LUCKY28, winCash, null);
					
					StringBuffer remark=new StringBuffer();
					remark.append("彩票中奖 奖金 ")
					    .append(wincash).append("元");
					userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, wincash, betDetail.getBetDetailId(), Constants.GAME_TYPE_GF_BJKL8,remark.toString());

				}else{
					totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
					betDetail.setWinResult("2");//未中奖
//					betDetail.setWinCash(new BigDecimal("-"+betDetail.getBetMoney()));
					betDetail.setWinCash(new BigDecimal(0));
				}
				String countResult= lu28session.getCountResult(); //计算结果
				if(countResult == null || countResult.length()<=0){
					
					Map<String,String>  coumap=this.openResult(openResult);
					countResult=coumap.get("number1")+","+coumap.get("number2")+","+coumap.get("number3")+","+coumap.get("number")+","+coumap.get("colour");
				}
				betDetail.setOpenResult(countResult);

				gfBjKl8DAO.updateObject(betDetail, user);
			}
		}
		
		bet.setTotalPoint(totalPoint);
		bet.setWinCash(betCash);
		bet.setBetTime(new Date());
		gfBjKl8DAO.saveObject(bet, null);
		return true;
	}
	/**
	 * 中奖结果
	 * @param openResult
	 * @return
	 */
	public Map<String,Boolean>  numberResult(String openResult){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
		String[] split = openResult.split(",");
		int sum1=0;
		int sum2=0;
		int sum3=0;
		for (int i = 0; i < split.length; i++) {
			if(i<6){
				sum1=sum1+Integer.valueOf(split[i]).intValue();
			}else if(i>=6 && i<12){
				sum2=sum2+Integer.valueOf(split[i]).intValue();
			}else if(i>=12 && i<18){
				sum3=sum3+Integer.valueOf(split[i]).intValue();
			}
		}
		int number1=sum1 % 10;
		int number2=sum2 % 10;
		int number3=sum3 % 10;
		int number=number1 + number2 + number3;
		if(number<=13){
			map.put("小", true);
		}else if(number>=14 && number<=27){
			map.put("大", true);
		}
		if(number%2==0){
			map.put("双", true);
		}else if(number%2!=0){
			map.put("单", true);
		}
		if(number>=14 && number%2==0){
			map.put("大双", true);
		}else if(number>14 && number%2!=0){
			map.put("大单", true);
		}
		if(number<14 && number%2==0){
			map.put("小双", true);
		}else if(number<14 && number%2!=0){
			map.put("小单", true);
		}
		if(number>=0 && number<=4){
			map.put("极小", true);
		}else if(number>=23 && number<=27){
			map.put("极大", true);
		}
		if(number!=13 && number%3==1){
			map.put("绿波", true);
		}else if(number!=14 && number%3==2){
			map.put("蓝波", true);
		}else if(number!=0 && number!=27 && number%3==0){
			map.put("红波", true);
		}
		if(number1==number2 && number2==number3){
			map.put("豹子", true);
		}
		map.put(""+number, true);
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
		int number1=Integer.parseInt(split[0]);
		int number2=Integer.parseInt(split[1]);
		int number3=Integer.parseInt(split[2]);
		int number=number1 + number2 + number3;
		if(number<=13){
			map.put("小", true);
		}else if(number>=14 && number<=27){
			map.put("大", true);
		}
		if(number%2==0){
			map.put("双", true);
		}else if(number%2!=0){
			map.put("单", true);
		}
		if(number>=14 && number%2==0){
			map.put("大双", true);
		}else if(number>14 && number%2!=0){
			map.put("大单", true);
		}
		if(number<14 && number%2==0){
			map.put("小双", true);
		}else if(number<14 && number%2!=0){
			map.put("小单", true);
		}
		if(number>=0 && number<=4){
			map.put("极小", true);
		}else if(number>=23 && number<=27){
			map.put("极大", true);
		}
		if(number!=13 && number%3==1){
			map.put("绿波", true);
		}else if(number!=14 && number%3==2){
			map.put("蓝波", true);
		}else if(number!=0 && number!=27 && number%3==0){
			map.put("红波", true);
		}
		if(number1==number2 && number2==number3){
			map.put("豹子", true);
		}
		map.put(""+number, true);
		return map;
	}
	
	/**
	 * 开奖结果及波色
	 * @param openResult
	 * @return
	 */
	public Map<String,String>  openResult(String openResult){
		Map<String,String> map=new HashMap<String,String>();
		String[] split = openResult.split(",");
		int sum1=0;
		int sum2=0;
		int sum3=0;
		for (int i = 0; i < split.length; i++) {
			if(i<6){
				sum1=sum1+Integer.valueOf(split[i]).intValue();
			}else if(i>=6 && i<12){
				sum2=sum2+Integer.valueOf(split[i]).intValue();
			}else if(i>=12 && i<18){
				sum3=sum3+Integer.valueOf(split[i]).intValue();
			}
		}
		int number1=sum1 % 10;
		int number2=sum2 % 10;
		int number3=sum3 % 10;
		int number=number1 + number2 + number3;
		map.put("number1", number1+"");
		map.put("number2", number2+"");
		map.put("number3", number3+"");
		map.put("number", number+"");
		if(number!=13 && number%3==1){//绿波
			map.put("colour", "1");
		}else if(number!=14 && number%3==2){//蓝波
			map.put("colour", "2");
		}else if(number!=0 && number!=27 && number%3==0){//红波
			map.put("colour", "3");
		}else{
			map.put("colour", "0");//无波色
		}
		if(number<=13){
			map.put("大小", "小");
		}else if(number>=14 && number<=27){
			map.put("大小", "大");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}
		if(number>=0 && number<=4){
			map.put("极值", "极小");
		}else if(number>=23 && number<=27){
			map.put("极值", "极大");
		}else{
			map.put("极值", "-");
		}
		if(number1==number2 && number2==number3){
			map.put("豹子", "豹子");
		}else{
			map.put("豹子", "-");
		}
		return map;
	}
	
	public Map<String,String>  openCountResult(String openResult){
		Map<String,String> map=new HashMap<String,String>();
		String[] split = openResult.split(",");

		int number1=Integer.parseInt(split[0]);
		int number2=Integer.parseInt(split[1]);
		int number3=Integer.parseInt(split[2]);
		int number=number1 + number2 + number3;
		map.put("number1", number1+"");
		map.put("number2", number2+"");
		map.put("number3", number3+"");
		map.put("number", number+"");
		if(number!=13 && number%3==1){//绿波
			map.put("colour", "1");
		}else if(number!=14 && number%3==2){//蓝波
			map.put("colour", "2");
		}else if(number!=0 && number!=27 && number%3==0){//红波
			map.put("colour", "3");
		}else{
			map.put("colour", "0");//无波色
		}
		if(number<=13){
			map.put("大小", "小");
		}else if(number>=14 && number<=27){
			map.put("大小", "大");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}
		if(number>=0 && number<=4){
			map.put("极值", "极小");
		}else if(number>=23 && number<=27){
			map.put("极值", "极大");
		}else{
			map.put("极值", "-");
		}
		if(number1==number2 && number2==number3){
			map.put("豹子", "豹子");
		}else{
			map.put("豹子", "-");
		}
		return map;
	}
	
	/**
	 * 混合结果判断
	 * @param openResult
	 * @return
	 */
	public Map<String,String>  judgeResult(String openResult){
		Map<String,String> map=new HashMap<String,String>();
		String[] split = openResult.split(",");
		int sum1=0;
		int sum2=0;
		int sum3=0;
		for (int i = 0; i < split.length; i++) {
			if(i<6){
				sum1=sum1+Integer.valueOf(split[i]).intValue();
			}else if(i>=6 && i<12){
				sum2=sum2+Integer.valueOf(split[i]).intValue();
			}else if(i>=12 && i<18){
				sum3=sum3+Integer.valueOf(split[i]).intValue();
			}
		}
		int number1=sum1 % 10;
		int number2=sum2 % 10;
		int number3=sum3 % 10;
		int number=number1 + number2 + number3;
		if(number<=13){
			map.put("大小", "小");
		}else if(number>=14 && number<=27){
			map.put("大小", "大");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}
		if(number>=0 && number<=4){
			map.put("极值", "极小");
		}else if(number>=23 && number<=27){
			map.put("极值", "极大");
		}else{
			map.put("极值", "-");
		}
		if(number!=13 && number%3==1){//绿波
			map.put("波色", "绿波");
		}else if(number!=14 && number%3==2){//蓝波
			map.put("波色", "蓝波");
		}else if(number!=0 && number!=27 && number%3==0){//红波
			map.put("波色", "红波");
		}else{
			map.put("波色", "-");//无波色
		}
		if(number1==number2 && number2==number3){
			map.put("number", number1+"");
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
		int number=number1 + number2 + number3;
		if(number<=13){
			map.put("大小", "小");
		}else if(number>=14 && number<=27){
			map.put("大小", "大");
		}
		if(number%2==0){
			map.put("单双", "双");
		}else if(number%2!=0){
			map.put("单双", "单");
		}
		if(number>=0 && number<=4){
			map.put("极值", "极小");
		}else if(number>=23 && number<=27){
			map.put("极值", "极大");
		}else{
			map.put("极值", "-");
		}
		if(number!=13 && number%3==1){//绿波
			map.put("波色", "绿波");
		}else if(number!=14 && number%3==2){//蓝波
			map.put("波色", "蓝波");
		}else if(number!=0 && number!=27 && number%3==0){//红波
			map.put("波色", "红波");
		}else{
			map.put("波色", "-");//无波色
		}
		if(number1==number2 && number2==number3){
			map.put("number", number1+"");
			map.put("豹子", "豹子");
		}else{
			map.put("豹子", "-");
		}
		return map;
	}
	public void updateFetchAndOpenResult() {
		GfBjKl8GaSession currentSession= gfBjKl8DAO.getCurrentSession();
		final String lastSessionNo=(Integer.parseInt(currentSession.getSessionNo())-1)+"";
		final Map<String,String> sessionNoMap=new HashMap<String,String>();
		final Map<String,String> timeMap=new HashMap<String,String>();
		Thread t=new Thread(){
            public void run(){
               try {
            	   int countRun=0;
            	   while(true){
            		   if(countRun==19){
            			   interrupt();
            			   break;
            		   }
            		    countRun=countRun+1;
            	
	               		//从这里 ---------------------------------------------------------------------------
            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("gfbjkl8");
            		    String officalURL ="";
            		    String urlSwitch=sessionInfo.getUrlSwitch();
            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
            		    }else if(urlSwitch.equals("2")){
            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
            		    }
	               		log.info("___[bjKl8 start fetch result xml data...]________________");
//	               		ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+"___[bjLu28 start fetch result xml data...]________________", Constants.getWebRootPath()+"/gamelogo/bjlu28.txt", true);
	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
	               		//到这里 ---------------------------------------------------------------------------		
	            	    sleep(3000);
	               		//log.info("___[fetch result xml data]"+resultXML);
	            	    //ManageFile.writeTextToFile(DateTimeUtil.DateToStringAll(new Date())+resultXML, Constants.getWebRootPath()+"/gamelogo/bjlu28.txt", true);
	               		if(ParamUtils.chkString(resultXML)){
	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
	               				               			
	               			//开始解析场次开奖数据
	               			NodeList nList = xmlDoc.getElementsByTagName("row");
	               			String sessionNo="";//场次号
	               			String result="";//开奖结果5组数字
	               			String time="";
//	               			boolean isnew=false;
	               			for(int i =0;i<nList.getLength();i++){
	               				Node node = nList.item(i);
	               				//数据库来源开彩网
	               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
	               				result = XmlUtil.getElementAttribute((Element)node, "opencode");	
	               				time = XmlUtil.getElementAttribute((Element)node, "opentime");	
	               				//数据库来源zao28    https://api.zao28.com
//	               				sessionNo = XmlUtil.getElementAttribute((Element)node, "issue");//期号
//	               				result = XmlUtil.getElementAttribute((Element)node, "srccode");	//开奖结果
//	               				time = XmlUtil.getElementAttribute((Element)node, "time");//开奖时间
	               				if(ParamUtils.chkString(result)){
	               					if(result.contains("+")){
	               						result = result.substring(0, result.indexOf("+"));
	               					}              					
	               				}
           						if(sessionNoMap.get(sessionNo)==null){
           							sessionNoMap.put(sessionNo, result);
           							timeMap.put(sessionNo, time);
           						}
           						if(sessionNo.equals(lastSessionNo)){
           							interrupt();
           						}
	               			}
//	               			interrupt();
	               		}else{
	               			interrupt();
       						break;
	               		}
            	   }
               } catch (Exception e) {
            	   interrupt();
               }
            }
        };
        t.start();
        
        try {
			t.join();//该方法是等 t 线程结束以后再执行后面的代码
			if(!sessionNoMap.isEmpty()){
				for(String key:sessionNoMap.keySet()){
					HQUtils hq = new HQUtils("from BjLu28GaSession gks where gks.sessionNo=?");
					hq.addPars(key);
					GfBjKl8GaSession session = (GfBjKl8GaSession)gfBjKl8DAO.getObject(hq);
					if(session!=null){
						String openStatus = session.getOpenStatus();//开奖状态
						
						if(openStatus.equals(BjKl8Constants.LUCKY28_OPEN_STATUS_INIT) || openStatus.equals(BjKl8Constants.LUCKY28_OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
							//更新开奖结果
							String openResult = sessionNoMap.get(key);
							session.setOpenResult(openResult);
							gfBjKl8DAO.saveObject(session);
							
							//进入开奖程序方法
							boolean flag = this.updateBjKl8SessionOpenResultMethod(session, openResult);
							
							if(flag){
								String countResult="";
								Map<String,String>  map=this.openResult(openResult);
								countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
								session.setCountResult(countResult);
								//更新场次状态，TODO 盈亏数据也需要在开奖
//								session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
								session.setOpenStatus(BjKl8Constants.LUCKY28_OPEN_STATUS_OPENED);
								gfBjKl8DAO.saveObject(session);
//								log.info("___[open result success sessionNO["+sessionNo+"]]");
							}else{
//								log.info("___[open result fail sessionNO["+sessionNo+"], please check...]");
							}
						}
					}
				}
				
				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_BJKL8);
				if(sessionInfo!=null){

//					HQUtils hq = new HQUtils("from BjLu28GaSession gks where gks.sessionNo=?");
//					hq.addPars(lastSessionNo);
//					BjLu28GaSession session = (BjLu28GaSession)bjLu28DAO.getObject(hq);
//					sessionInfo.setOpenSessionNo(currentSession.getSessionNo());
					if(sessionNoMap.get(lastSessionNo)!=null){
						String countResult="";
						Map<String,String>  map=this.openResult(sessionNoMap.get(lastSessionNo));
						countResult=map.get("number1")+","+map.get("number2")+","+map.get("number3")+","+map.get("number")+","+map.get("colour");
						sessionInfo.setOpenResult(countResult);
						sessionInfo.setOpenSessionNo(lastSessionNo);
						sessionInfo.setEndTime(gfBjKl8DAO.getPreviousSessionBySessionNo(lastSessionNo).getEndTime());
					}
					sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
					

					gfBjKl8DAO.saveObject(sessionInfo);							
				}
				sessionNoMap.clear();
				timeMap.clear();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public GfBjKl8GaSession getCurrentSession() {
		return gfBjKl8DAO.getCurrentSession();
	}
	
	public GfBjKl8GaSession getPreviousSessionBySessionNo(String sessionNo) {
		return gfBjKl8DAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
			List<GaBetOption> list, GfBjKl8GaSession session, User user,
			BigDecimal betAll) {

		List<GaBetDetail> betDetailList=new ArrayList<GaBetDetail>();
		BigDecimal paperMoney=user.getUserScore();
		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
		BigDecimal tempMoney=new BigDecimal(0);
		for (int i = 0; i < list.size(); i++) {
				GaBetOption betOption = list.get(i);
				GaBetDetail betDetail=new GaBetDetail();
				if(betOption!=null){
					betDetail.setBetRate(betOption.getBetRate());
				}
				betDetail.setWinResult("0");//未开奖
				betDetail.setBetFlag("1");//有效投注
				betDetail.setSessionId(session.getSessionId());
				betDetail.setUserId(user.getUserId());
				betDetail.setBetOptionId(betOption.getBetOptionId());
				betDetail.setBetTime(new Date());
				betDetail.setBetMoney(betMap.get(betOption.getBetOptionId()));
				if(paperMoney.compareTo(new BigDecimal(0))==1){
					if(new BigDecimal(betMap.get(betOption.getBetOptionId())).compareTo(paperMoney)!=-1){
						betDetail.setPaperMoney(paperMoney);
						paperMoney=paperMoney.subtract(paperMoney);
						tempMoney=tempMoney.add(new BigDecimal(betMap.get(betOption.getBetOptionId())));
					}else{
						betDetail.setPaperMoney(new BigDecimal(betMap.get(betOption.getBetOptionId())));
						paperMoney=paperMoney.subtract(new BigDecimal(betMap.get(betOption.getBetOptionId())));
						tempMoney=tempMoney.add(new BigDecimal(betMap.get(betOption.getBetOptionId())));
					}
				}
				
//				betDetail.setPaperMoney(paperMoney);
				betDetail.setRoom(room);
				betDetail.setSessionNo(session.getSessionNo());
				betDetail.setGameName("北京快乐8");

				if(betOption.getPlayType().equals("0")){//两面盘
					betDetail.setPlayName("两面盘");
				}else{
					betDetail.setPlayName("特码");
				}
				betDetail.setBetName(this.getBetNameByOptionType(betOption.getPlayType(),betOption.getOptionType().toString()));
				betDetail.setOptionTitle(betOption.getOptionTitle());
				betDetail.setGameType(Constants.GAME_TYPE_GF_BJKL8);//游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
				betDetailList.add(betDetail);
		}
		
		BigDecimal userBalance=user.getUserBalance();
		if(userBalance==null){
			userBalance=new BigDecimal(0);
		}
		user.setUserBalance(userBalance.subtract(betAll));
		
		if(paperMoney.compareTo(betAll)==1||paperMoney.compareTo(betAll)==0){//红包足够投注，不用使用余额里面的钱
			user.setUserScore(user.getUserScore().subtract(tempMoney));
			gfBjKl8DAO.updateObject(user, null);
		}else{
//			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BET_BJLU28, betAll.subtract(tempMoney), null);
			StringBuilder remark = new StringBuilder();
			remark.append("购买彩票 扣款 ")
			    .append(betAll.subtract(tempMoney)).append("元");
			user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO, betAll.subtract(tempMoney), null, Constants.GAME_TYPE_GF_BJKL8,remark.toString());
		}
		
		gfBjKl8DAO.updateObjectList(betDetailList, null);
	
		return user;
	}
	
	public List<GfBjKl8GaTrend> findBjKl8TrendList() {
		return gfBjKl8DAO.findBjKl8GaTrendList();
	}
	
	public PaginationSupport findBjKl8GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return gfBjKl8DAO.findBjKl8GaSessionList(hql,para,pageNum,pageSize);
	}
	
	 public String getBetNameByOptionType(String playType,String optionType){
		 if(playType.equals("0")){//两面盘
			 if(optionType.equals("0")){
				 return "双面";
			 }else if(optionType.equals("1")){
				 return "波色/豹子";
			 }
		 }else if(playType.equals("1")){//1-10名
			 if(optionType.equals("0")){
				 return "特码";
			 }
		}
		 return "";
	 }
	 
	 public void updateFetchAndOpenTrendResult(Integer count){
			if(count==9){//执行10次
				count=null;
				return;
			}
			String lastSessionNo=(Integer.parseInt(gfBjKl8DAO.getCurrentSession().getSessionNo())-1)+"";
			GfBjKl8GaSession session =gfBjKl8DAO.getPreviousSessionBySessionNo(lastSessionNo);
			if(session.getOpenStatus().equals(BjKl8Constants.LUCKY28_OPEN_STATUS_OPENED)){
				List<GfBjKl8GaTrend> list=gfBjKl8DAO.findBjKl8GaTrendAllList();
				List<GfBjKl8GaTrend> savelist=new ArrayList<GfBjKl8GaTrend>();
				Map<String,Boolean> map=numberResult(session.getOpenResult());
				if(!map.isEmpty()){
					for(int i=0;i<list.size();i++){
						GfBjKl8GaTrend trend=list.get(i);
						if(map.get(trend.getTrendTitle())!=null&&map.get(trend.getTrendTitle())==true){
							trend.setTrendCount(trend.getTrendCount()+1);				
						}else{
							trend.setTrendCount(0);
						}
						savelist.add(trend);
					}
					gfBjKl8DAO.updateObjectList(savelist, null);
					count=null;
					lastSessionNo=null;
					session=null;
					return;
				}

			}else{
				Thread t1=new Thread(){
		            public void run(){
		               try {
		            	   sleep(3000);
		            	   interrupt();
		               } catch (Exception e) {
		            	   interrupt();
		               }
		            }
				};
				t1.start();
				try {
					t1.join();
					t1=null;
					count++;
					lastSessionNo=null;
					session=null;
					updateFetchAndOpenTrendResult(count);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public boolean saveOpenResult(GfBjKl8GaSession session,String openResult){
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
				int number=Integer.parseInt(buffer.substring(buffer.indexOf(",")));
				if(number!=13 && number%3==1){//绿波
					buffer=buffer+","+1;
				}else if(number!=14 && number%3==2){//蓝波
					buffer=buffer+","+2;
				}else if(number!=0 && number!=27 && number%3==0){//红波
					buffer=buffer+","+3;
				}else{
					buffer=buffer+","+0;
				}
//				session.setOpenResult(openResult);
				session.setCountResult(buffer);
				gfBjKl8DAO.updateObject(session, null);
				flag=true;
			}
			return flag;
		}
		
		public boolean saveAndOpenResult(GfBjKl8GaSession session,String openResult){
			String buffer="";
			boolean flag=false;
			if(ParamUtils.chkString(openResult)){
				String regex = ",|，|\\s+";
				String array[]=openResult.split(regex);
				for(int i=0;i<array.length;i++){
					if(ParamUtils.chkString(array[i].trim())){
						buffer=buffer+array[i].trim()+",";
					}
				}
				buffer=buffer.substring(0, buffer.length()-1);
				int number=Integer.parseInt(array[array.length - 1].trim());
				if(number!=13 && number%3==1){//绿波
					buffer=buffer+","+1;
				}else if(number!=14 && number%3==2){//蓝波
					buffer=buffer+","+2;
				}else if(number!=0 && number!=27 && number%3==0){//红波
					buffer=buffer+","+3;
				}else{
					buffer=buffer+","+0;
				}
//				session.setOpenResult(openResult);
				session.setCountResult(buffer);
				boolean flag1 = updateBjKl8SessionOpenResultMethod(session, session.getCountResult());
				if(flag1){      
					session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
					session.setOpenStatus(GameConstants.OPEN_STATUS_OPENED);
					gfBjKl8DAO.updateObject(session, null);
					log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
					flag=true;
				}else{
					log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
				}
			}
			return flag;
		}
		@Override
		public PaginationSupport findBjKl8GaBetList(String hql,
				List<Object> para, int pageNum, int pageSize) {
			return gfBjKl8DAO.findBjKl8GaBetList(hql, para, pageNum, pageSize);
		}

		@Override
		public PaginationSupport findGaBetDetail(String hql, List<Object> para,
				int pageNum, int pageSize) {
			return gfBjKl8DAO.findGaBetDetail(hql, para, pageNum, pageSize);
		}
		@Override
		public List<GfBjKl8DTO> findGaBetDetailById(String hql,
				List<Object> para) {
			return gfBjKl8DAO.findGaBetDetailById(hql, para);
		}

}
