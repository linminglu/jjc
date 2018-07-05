package com.game.service.impl;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.apps.Constants;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.game.GameConstants;
import com.game.dao.IGaK3DAO;
import com.game.model.GaK3Bet;
import com.game.model.GaK3BetDetail;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;
import com.game.service.IGaK3Service;
import com.ram.service.user.IUserService;

public class GaK3ServiceImpl extends BaseService implements IGaK3Service {
	private IGaK3DAO gaK3DAO;
	private IUserService userService;

	public void setGaK3DAO(IGaK3DAO gaK3DAO) {
		this.gaK3DAO = gaK3DAO;
		super.dao = gaK3DAO;
	}
		
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	/**
	 * 初始化场次
	 * 00:00 生成当天的所有场次
	 */
	public String updateInitSession(){
		long t1 = System.currentTimeMillis();
		
		log.info("___[start]__________________________");
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		
		if(!isTodaySessionInit){
			log.info("___[start today]__________________________");
			
			String startTimeStr = today + GameConstants.K3_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
			log.info("___[startTimeStr="+startTimeStr+"]");
			
			for (int i = 0; i < GameConstants.K3_MAX_PART; i++) {
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*GameConstants.K3_TIME_INTERVAL * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				//String openResult = GameConstants.getRandomK3Result();//随机生成3个中奖数字
				String sessionNo = this.getTodaySessionNo(now, i+1);//期号
				
				GaK3Session k3 = new GaK3Session();
				k3.setSessionNo(sessionNo);
				k3.setStartTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,-GameConstants.K3_TIME_INTERVAL));
				k3.setEndTime(curSessionDate);
				k3.setOpenStatus(GameConstants.K3_OPEN_STATUS_INIT);
				//k10Session.setOpenResult(openResult);//开奖号由系统抓取获取得
				k3.setUserCount(0);
				k3.setPointCount(0);
				k3.setSessionCash(0);
				gaK3DAO.saveObject(k3);

				//log.info("___[out]__[sessionNo="+sessionNo+"][diffTime="+diffTime+"][startDate.getTime()="+startDate.getTime()+"]");
			}
			flag = "success";
			log.info("___[today init completed]__________________________");
		} else {
			flag = "inited";
			log.info("___[today has been inited]__________________________");
		}
		
		long t2 = System.currentTimeMillis();
		log.info("___[spend time]["+((t2-t1)/1000)+"s]__________________________");
		
		return flag;
	}
	
	/**
	 * 获取今天的期号
	 * @param today
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date today,int index){
		String num = index<10?"00"+index:"0"+index;
		return DateTimeUtil.getYyMMddStr(today)+num;
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
		
		HQUtils hq = new HQUtils("from GaK3Session gks where gks.startTime>=? and gks.startTime<=?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gaK3DAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 检查场次
	 * 针对当前场次三个状态：
	 * 投注中：可以投注，上期已开奖 15s
	 * 开奖中：正在开奖 5s
	 * 空闲中：等待下期 5s
	 */
	public String updateCheckSession(){
		String flag = "fail";//返回状态
		
//		Date now = DateTimeUtil.getJavaUtilDateNow();
//		GaK3Session session = gaK3DAO.getCurrentSession(now);//当前期
//		if(session!=null){
//			//now = DateTimeUtil.getJavaUtilDateNow();
//			//Date sTime = session.getStartTime();//开始时间
//			Date eTime = session.getEndTime();//截止时间
//			log.info("___"+DateTimeUtil.format(eTime)+","+DateTimeUtil.format(now));
//			long diff = eTime.getTime()-now.getTime();//时间间隔
//			if(diff>0){
//				if(diff>10000){//投注中
//					
//				}else if(diff<=10000 && diff>5000){//开奖中
//					
//					//开奖-----
//					//调用开奖程序
//					
//					
//					session.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_OPENING);
//					gaK3DAO.saveObject(session);
//					
//				}else{//空闲中-已开奖
//					
//					session.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_WAITING);
//					gaK3DAO.saveObject(session);
//				}
//				flag = "success";
//				log.info("___[beting/opening/waiting]["+diff+"]["+session.getSessionNo()+"][openStatus:"+session.getOpenStatus()+"]");
//			}else{
//				log.info("___[data wrong......]["+diff+"]["+session.getSessionNo()+"]");
//			}
//			
//		}else{
//			log.info("___[data wrong not found current session......]");
//		}
		return flag;
	}
	
	/**
	 * 抓取开奖数据
	 */
	public void updateFetchAndOpenResult(){
		try {
			Document doc = Jsoup.connect("http://chart.ydniu.com/trend/k3hb/").get();//第三方网站抓取
			Element table = doc.getElementById("tabtrend");//直接从网页获取开奖结果网页元素
			Element tbody = table.getElementsByTag("tbody").first();
			
			Elements trs = tbody.getElementsByTag("tr");
			
			//System.out.println(trs.size());
			for(Element el:trs){
				Elements tds = el.getElementsByTag("td");
				String sNo="";//期号
				String s1="";//开奖号
				String s2="";
				String s3="";
				for(int i=0;i<4;i++){//前面4个td分别是期号，开奖号
					Element td = tds.get(i);
					if(i==0){
						sNo = td.html();
					}else if(i==1){
						s1 = td.html();
					}else if(i==2){
						s2 = td.html();
					}else if(i==3){
						s3 = td.html();
					}
				}
				
				//根据期号查询开奖，这里获取到的都是已经开奖的期号，先判断是否开奖，如没有开奖，则立即开
				//开奖程序
				GaK3Session k3 = gaK3DAO.getGaK3SessionBySessionNo(sNo.substring(2));
				//判断是否已开奖
				if(k3!=null){
					k3.setOpenResult(s1+","+s2+","+s3);
					k3.setOpenTime(new Date());
					k3.setOpenStatus("2");
					
					//开奖统计
					
					
					
					this.saveBetDetail(k3);
					gaK3DAO.saveObject(k3);
					
				}
				
				log.info("___[fetch][sNo="+sNo+"]["+s1+","+s2+","+s3+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("___[fetch fail...]");
		}
		
		
	}
	
	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findLotteryResultList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findOneUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findBetUserList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findBetDetailList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public GaK3Session getCurrentSession() {
		return gaK3DAO.getCurrentSession();
	}
	public GaK3Session getGaK3SessionBySessionNo(String sessionNo){
		return gaK3DAO.getGaK3SessionBySessionNo(sessionNo);
	}
	public GaOrder 	getGaOrderByOrderNum(String orderNum){
		return gaK3DAO.getGaOrderByOrderNum(orderNum);
	}
	public PaginationSupport findOrderList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		return gaK3DAO.findOrderList(hqls, para, startIndex, pageSize);
	}
	
	public List findOrderView(Integer orderId){
		return gaK3DAO.findOrderView(orderId);
	}
	
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaK3DAO.findUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	
	public  void openResult(String sessionNo,String result){
		
	}
	/**
	 * 快三开奖结果
	 * @param sessionNo
	 * @param betType
	 * @param betValue
	 * @return
	 */
	public Map<String,Boolean>  openResult(GaK3Session session,String betType,String betValue){
		Map<String,Boolean> map=new HashMap<String,Boolean>();
//		GaK3Session session = gaK3DAO.getGaK3SessionBySessionNo(sessionNo, "2");
		if(session != null){
			String openResult = session.getOpenResult();
			log.info("openResult");
			if(ParamUtils.chkString(betValue) && ParamUtils.chkString(openResult)){
				String[] result = openResult.split(",");
				String[] value= betValue.split(",");
				//和值
				if("1".equals(betType)){
					int resultSum = 0;
					int valueSum = 0;
					for (int i = 0; i < result.length; i++) {
						resultSum += Integer.parseInt(result[i]);
					}
					for (int i = 0; i < value.length; i++) {
						if(resultSum == Integer.parseInt(value[i]))
							map.put("1", true);
						else
							map.put("1", false);
//						valueSum += Integer.parseInt(value[i]);
					}
					
				}else {
					map.put("1", false);
				}
				//三同号通选
				if("2".equals(betType)){
					if(openResult.equals("1,1,1")||openResult.equals("2,2,2")||openResult.equals("3,3,3")
							||openResult.equals("4,4,4")||openResult.equals("5,5,5")||openResult.equals("6,6,6")){
						map.put("2", true);
					}else{
						map.put("2", false);
					}
				}else{
					map.put("2", false);
				}
				//三同号单选
				if("3".equals(betType)){
					String s = "";
					for (int i = 0; i < result.length; i++) {
						s +=result[i]+"";
					}
					for (int i = 0; i < value.length; i++) {
						if(s.equals(value[i])){
							map.put("3", true);
							break;
						}else{
							map.put("3", false);
						}
					}
				}else{
					map.put("3", false);
				}
				//二同号复选
				if("4".equals(betType)){
					if((result[0].equals(result[1])&&!result[1].equals(result[2]))){
						String s = "";
						for (int i = 0; i < result.length; i++) {
							s +=result[i]+"";
						}
						String resultSubstring = s.substring(0, 1);
						for (int i = 0; i < value.length; i++) {
							String valueSubstring = value[i].substring(0, 1);
							if(resultSubstring.equals(valueSubstring)){
								map.put("4", true);
								break;
							}else{
								map.put("4", false);
							}
						}
					}else if(result[1].equals(result[2])&&!result[0].equals(result[1])){
						String s = "";
						for (int i = 0; i < result.length; i++) {
							s +=result[i]+"";
						}
						String resultSubstring = s.substring(1, 2);
						for (int i = 0; i < value.length; i++) {
							String valueSubstring = value[i].substring(1, 2);
							if(resultSubstring.equals(valueSubstring)){
								map.put("4", true);
								break;
							}else{
								map.put("4", false);
							}
						}
					}else{
						map.put("4", false);
					}
				}else{
					map.put("4", false);
				}
				//二同号单选
				if("5".equals(betType)){
					Map<String,String> m=new HashMap<String,String>();
					Map<String,String> n=new HashMap<String,String>();
					if((result[0].equals(result[1])&&!result[1].equals(result[2]))){
						String s = "";
						for (int i = 0; i < result.length; i++) {
							s +=result[i]+"";
						}
						String resultSubstring = s.substring(0, 2);  
						String[] z = betValue.split("#");
						String left = z[0];
						String right = z[1];
						String[] sm = left.split(",");
						String[] sn = right.split(",");
						for (int i = 0; i < sm.length; i++) {
							if(resultSubstring.equals(sm[i])){
								m.put("两号相同", "1");
								break;
							}
						}
						for (int i = 0; i < sn.length; i++) {
							if(result[2].equals(sn[i])){
								n.put("单号相同", "1");
								break;
							}
						}
						if(m != null && n!= null && "1".equals(m.get("两号相同")) && "1".equals(n.get("单号相同"))){
							map.put("5", true);
						}else{
							map.put("5", false);
						}
					}else if(result[1].equals(result[2])&&!result[0].equals(result[1])){
						String s = "";
						for (int i = 0; i < result.length; i++) {
							s +=result[i]+"";
						}
						String resultSubstring = s.substring(1, 3);
						String[] z = betValue.split("#");
						String left = z[0];
						String right = z[1];
						String[] sm = left.split(",");
						String[] sn = right.split(",");
						for (int i = 0; i < sm.length; i++) {
							if(resultSubstring.equals(sm[i])){
								m.put("两号相同", "1");
								break;
							}
						}
						for (int i = 0; i < sn.length; i++) {
							if(result[0].equals(sn[i])){
								n.put("单号相同", "1");
								break;
							}
						}
						if(m != null && n != null && m.get("两号相同").equals("1") && m.get("单号相同").equals("1")){
							map.put("5", true);
						}else{
							map.put("5", false);
						}
					}else{
						map.put("5", false);
					}
				}else{
					map.put("5", false);
				}
				//三不同号
				if("6".equals(betType)){
					if(betValue.indexOf(openResult) > -1){
						map.put("6", true);
					}else{
						map.put("6", false);
					}
				}else{
					map.put("6", false);
				}
				//二不同号
				if("7".equals(betType)){
					int k = 0;
					if(!result[0].equals(result[1])){
						for (int i = 0; i < value.length; i++) {
							for (int j = 0; j <2; j++) {
								if(value[i].equals(result[j])){
									k++;
									if(k==2) break;
								}
							}
							if(k==2){
								break;
							}
						}
						if(k>=2){
							map.put("7", true);
						}else{
							map.put("7", false);
						}
					}else if(!result[0].equals(result[2])){
						String s1 = result[0];
						String s2 = result[2];
						String[] s={s1,s2};
						for (int i = 0; i < value.length; i++) {
							for (int j = 0; j <s.length; j++) {
								if(value[i].equals(s[j])){
									k++;
									if(k==2) break;
								}
							}
							if(k==2){
								break;
							}
						}
						if(k>=2){
							map.put("7", true);
						}else{
							map.put("7", false);
						}
					}else if(!result[1].equals(result[2])){
						for (int i = 0; i < value.length; i++) {
							for (int j = 1; j <=2; j++) {
								if(value[i].equals(result[j])){
									k++;
									if(k==2) break;
								}
							}
							if(k==2){
								break;
							}
						}
						if(k>=2){
							map.put("7", true);
						}else{
							map.put("7", false);
						}
					}else{
						map.put("7", false);
					}
					
				}else{
					map.put("7", false);
				}
				//三连号通选
				if("8".equals(betType)){
					if(openResult.equals("1,2,3")||openResult.equals("2,3,4")||openResult.equals("3,4,5")
							||openResult.equals("4,5,6")){
						map.put("8", true);
					}else{
						map.put("8", false);
					}
				}else{
					map.put("8", false);
				}
			}
			
		}
		return map;
	}
//	public static void main(String[] args){
//		Map<String, Boolean> map = openResult(null, "1", "4");
//		System.out.println(map.get("1"));
//		
//	}
	public void saveBetDetail(GaK3Session session){
//		GaK3Session session = gaK3DAO.getGaK3SessionBySessionNo(sessionNo, "2");
//		session = (GaK3Session)gaK3DAO.getObject(GaK3Session.class, 1);
		if(session != null && "2".equals(session.getOpenStatus())){
			String openResult = session.getOpenResult();
			if(ParamUtils.chkString(openResult)){
				Integer sessionId = session.getSessionId();
				List<GaK3Bet> saveBetList=new ArrayList<GaK3Bet>();
				List<GaK3BetDetail> saveBetDetailList=new ArrayList<GaK3BetDetail>();
				List<GaK3Bet> list = gaK3DAO.findBetList(sessionId);
				Integer winCashCount = session.getSessionCash();
				if(!ParamUtils.chkInteger(winCashCount)) winCashCount = 0;
				if(list != null && list.size() > 0){
					for (GaK3Bet gaK3Bet : list) {
						String betValue = gaK3Bet.getBetValue();
						String betType = gaK3Bet.getBetType();
						Integer multiple = gaK3Bet.getMultiple();
						if(ParamUtils.chkString(betValue) && ParamUtils.chkString(betType)){
							Map<String, Boolean> map = openResult(session, betType, betValue);
							log.info(map.get("1")+""+map.get("2")+map.get("3")+map.get("4")+map.get("5")+
									map.get("6")+map.get("7")+map.get("8"));
							if(map != null){
								GaK3BetDetail gaKeBetDetail = new GaK3BetDetail();
								if(map.get("1")){
									String[] result = openResult.split(",");
									int resultSum = 0;
									for (int i = 0; i < result.length; i++) {
										resultSum += Integer.parseInt(result[i]);
									}
//									String content="";
									BigDecimal winCash=(gaK3Bet.getWinCash()==null?new BigDecimal(0):gaK3Bet.getWinCash());
									for(int j=0;j<betValue.split(",").length;j++){
										resultSum=Integer.parseInt(betValue.split(",")[j]);
										if(resultSum == 4 || resultSum == 17){
											gaK3Bet.setWinCash(new BigDecimal(80 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 5 || resultSum == 16){
											gaK3Bet.setWinCash(new BigDecimal(40 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 6 || resultSum == 15){
											gaK3Bet.setWinCash(new BigDecimal(25 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 7 || resultSum == 14){
											gaK3Bet.setWinCash(new BigDecimal(16 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 8 || resultSum == 13){
											gaK3Bet.setWinCash(new BigDecimal(12 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 9 || resultSum == 12){
											gaK3Bet.setWinCash(new BigDecimal(10 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 10 || resultSum == 11){
											gaK3Bet.setWinCash(new BigDecimal(9 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
										if(resultSum == 3 || resultSum == 18){
											gaK3Bet.setWinCash(new BigDecimal(240 * multiple * Constants.USER_MONEY_AND_POINT).add(winCash));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(resultSum+"");
											break;
										}
//										content=content+resultSum+",";								
									}
//									content=content.substring(0, content.length()-1);
//									gaKeBetDetail.setBetContent(content);
								}else if(map.get("2")){
									gaK3Bet.setWinCash(new BigDecimal(40 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									gaKeBetDetail.setBetContent(session.getOpenResult());
								}else if(map.get("3")){
									gaK3Bet.setWinCash(new BigDecimal(240 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									gaKeBetDetail.setBetContent(session.getOpenResult());
								}else if(map.get("4")){
									gaK3Bet.setWinCash(new BigDecimal(15 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									String openResult2 = session.getOpenResult();
									String[] s = openResult2.split(",");
									if(s[0].equals(s[1]))
										gaKeBetDetail.setBetContent(s[0]+""+s[1]+"*");
									if(s[1].equals(s[2]))
										gaKeBetDetail.setBetContent(s[1]+""+s[2]+"*");
									
								}else if(map.get("5")){
									gaK3Bet.setWinCash(new BigDecimal(80 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									gaKeBetDetail.setBetContent(betValue);
								}else if(map.get("6")){
									gaK3Bet.setWinCash(new BigDecimal(40 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									gaKeBetDetail.setBetContent(session.getOpenResult());
								}else if(map.get("7")){
									gaK3Bet.setOpenStatus("1");
									String[] value = betValue.split(",");
									String[] result = openResult.split(",");
//									int k = 0;
//									StringBuffer sb = new StringBuffer();
//									if(!result[0].equals(result[1])){
//										for (int i = 0; i < value.length; i++) {
//											for (int j = 0; j <2; j++) {
//												if(value[i].equals(result[j])){
//													k++;
//													sb.append(value[i]).append(",");
//													if(k==2) break;
//												}
//											}
//										}
//										if(k>=2){
//											String s = sb.toString().substring(0, sb.length()-1);
//											gaKeBetDetail.setBetContent(s);
//											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT));
//											gaKeBetDetail.setBetNum(1);
//										}
//									}else if(!result[0].equals(result[2])){
//										String s1 = result[0];
//										String s2 = result[2];
//										String[] s={s1,s2};
//										for (int i = 0; i < value.length; i++) {
//											for (int j = 0; j <s.length; j++) {
//												if(value[i].equals(s[j])){
//													k++;
//													sb.append(value[i]).append(",");
//													if(k==2) break;
//												}
//											}
//										}
//										if(k>=2){
//											String st = sb.toString().substring(0, sb.length()-1);
//											gaKeBetDetail.setBetContent(st);
//											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT));
//											gaKeBetDetail.setBetNum(1);
//										}
//									}else if(!result[1].equals(result[2])){
//										for (int i = 0; i < value.length; i++) {
//											for (int j = 1; j <=2; j++) {
//												if(value[i].equals(result[j])){
//													k++;
//													sb.append(value[i]).append(",");
//													if(k==2) break;
//												}
//											}
//										}
//										if(k>=2){
//											String s = sb.toString().substring(0, sb.length()-1);
//											gaKeBetDetail.setBetContent(s);
//											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT));
//											gaKeBetDetail.setBetNum(1);
//										}
//									}
//									
//										BigDecimal winCash = new BigDecimal(0);
//										Integer betNum = 0;
//										StringBuffer sb2 = new StringBuffer();
//										for (int i = 0; i < value.length-1; i++) {
//											for (int j = 0; j <result.length-1; j++) {
//												if(value[i].equals(result[j])){
//													k++;
//													sb2 = sb2.append(value[i]).append(",");
//													if(k>=2){
//														BigDecimal cash = new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT);
//														winCash = winCash.add(cash);
//														betNum++;
//													}
//												}
//											}
//										}
//										if(k>=2){
//											String s = sb2.toString().substring(0, sb2.length()-1);
//											gaKeBetDetail.setBetContent(s);
//											gaK3Bet.setWinCash(winCash);
//											gaKeBetDetail.setBetNum(betNum);
//										}
//										String h="";
//										for (int i = 0; i < value.length; i++) {
//											Arrays.binarySearch(result, value[i]);
//											if(Arrays.binarySearch(result, value[i]) !=-1){
//												h=h+value[i]+",";
//											}
//										}
//										
//										if(h.length() ==2){
//											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT));
//											gaK3Bet.setOpenStatus("1");
//											String h1 = h.substring(0, h.length()-1);
//											gaKeBetDetail.setBetContent(h1);
//											gaKeBetDetail.setBetNum(1);
//											gaKeBetDetail.setBetPoint(2);
//										}
//										if(h.length() > 2){
//											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT*3));
//											gaK3Bet.setOpenStatus("1");
//											gaKeBetDetail.setBetContent(openResult);
//											gaKeBetDetail.setBetNum(3);
//											gaKeBetDetail.setBetPoint(6);
//										}
										List<String> a = new ArrayList<String>();
										List<String> b = new ArrayList<String>();
										for (int i = 0; i < result.length; i++) {
											a.add(result[i]);
										}
										for (int i = 0; i < value.length; i++) {
											b.add(value[i]);
										}
										Collection<String> c = CollectionUtils.intersection(a, b);
										String[] arr = c.toArray(new String[c.size()]);
										Arrays.sort(arr);
										String ss = "";
										for (int i = 0; i < arr.length; i++) {
											ss = ss + arr[i]+",";
										}
										ss=ss.substring(0, ss.length()-1);
										if(arr.length ==2){
											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(ss);
											gaKeBetDetail.setBetNum(1);
											gaKeBetDetail.setBetPoint(2 * Constants.USER_MONEY_AND_POINT);
										}
										if(arr.length > 2){
											gaK3Bet.setWinCash(new BigDecimal(8 * multiple * Constants.USER_MONEY_AND_POINT*3));
											gaK3Bet.setOpenStatus("1");
											gaKeBetDetail.setBetContent(ss);
											gaKeBetDetail.setBetNum(3);
											gaKeBetDetail.setBetPoint(6 * Constants.USER_MONEY_AND_POINT);
										}
									
								}else if(map.get("8")){
									gaK3Bet.setWinCash(new BigDecimal(10 * multiple * Constants.USER_MONEY_AND_POINT));
									gaK3Bet.setOpenStatus("1");
									gaKeBetDetail.setBetContent(session.getOpenResult());
								}else{
									gaK3Bet.setWinCash(new BigDecimal(0));
									gaK3Bet.setOpenStatus("2");
								}
//								gaK3DAO.saveObject(gaK3Bet, null);
								saveBetList.add(gaK3Bet);
								if(map.get("1")|| map.get("2") || map.get("3") || map.get("4") || map.get("5") 
										|| map.get("6")||map.get("7") || map.get("8") ){
									gaKeBetDetail.setUserId(gaK3Bet.getUserId());
									gaKeBetDetail.setBetId(gaK3Bet.getBetId());
									gaKeBetDetail.setSessionId(sessionId);
									gaKeBetDetail.setBetTime(gaK3Bet.getBetTime());
									gaKeBetDetail.setBetType(gaK3Bet.getBetType());
									if(!map.get("7")){
										gaKeBetDetail.setBetNum(1);
										gaKeBetDetail.setBetPoint(2 * Constants.USER_MONEY_AND_POINT);
									}
									gaKeBetDetail.setWinResult("1");
									gaKeBetDetail.setWinCash(gaK3Bet.getWinCash());
									gaKeBetDetail.setBetFlag("1");
//									gaK3DAO.saveObjectDB(gaKeBetDetail);
									saveBetDetailList.add(gaKeBetDetail);
									winCashCount = winCashCount + (gaK3Bet.getWinCash().intValue());
									session.setSessionCash(winCashCount);
//									userService.savePointDetail(gaK3Bet.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HBK3, gaK3Bet.getWinCash().intValue(), null);
								}
							}
						}
					}
				}
				gaK3DAO.saveObject(session, null);
				gaK3DAO.updateObjectList(saveBetList, null);
				gaK3DAO.updateObjectList(saveBetDetailList, null);
			}
		}
	}
	
	public void saveUserBet(JSONObject map,GaK3Session session,Integer uid,String kitems,Integer countSession,Integer times
			,Integer countBet,Integer money,String payType){
		JSONObject data = new JSONObject();// 返回数据层
		if(session!=null){
			Date date=new Date();
			Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(date, GameConstants.K3_TIME_ADD);
			if(nowEnd.after(session.getStartTime())&&nowEnd.before(session.getEndTime())){
				JSONArray jsonArray = new JSONArray(kitems);
				int sendBetNum=0;//传递过来的注数
				int sendBetMoney=0;//传递过来的钱数
				int allBet=0;//所有注数
				List<GaK3Bet> gkbList=new ArrayList<GaK3Bet>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					if(jsonObj.get("betType")!=null&&jsonObj.get("betContent")!=null
							&&jsonObj.get("betNum")!=null&&jsonObj.get("betMoney")!=null){
						int betNum=Integer.parseInt((String) jsonObj.get("betNum"));
						int betMoney=Integer.parseInt((String) jsonObj.get("betMoney"));
						sendBetNum+=betNum;
						sendBetMoney+=betMoney;
						
						String betType=(String) jsonObj.get("betType");
						String betContent=(String) jsonObj.get("betContent");
						
						int thisBetNum=getBetTotal(betType,betContent);
						if(thisBetNum!=betNum){
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "投注注数计算出错！");
//							JsonUtil.AjaxWriter(response, map);
							return;
						}
						if(betMoney!=thisBetNum*200){//每注2元
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "投注金额计算出错！");
//							JsonUtil.AjaxWriter(response, map);
							return;
						}
						allBet=allBet+thisBetNum;
						for(int j=0;j<countSession;j++){
							GaK3Bet gkb=new GaK3Bet();
							gkb.setBetTime(date);
							gkb.setBetType(betType);
							gkb.setBetValue(betContent);
							gkb.setOpenStatus("0");
							gkb.setTotalNum(thisBetNum*times);
							gkb.setTotalPoint(new BigDecimal(thisBetNum*200*times));
							gkb.setMultiple(times);
							gkb.setUserId(uid);
							gkb.setSessionId(session.getSessionId()+j);//期号
							gkbList.add(gkb);
						}

					}else{
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "参数不正确！");
//						JsonUtil.AjaxWriter(response, map);
						return;
					}
				}
				
				if(sendBetNum!=allBet){//||allBet*times*countSession!=countBet
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "注数计算出错！");
//					JsonUtil.AjaxWriter(response, map);
					return;
				}
				if(allBet*times*countSession*200!=money||sendBetMoney!=allBet*200){
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "金额计算出错！");
//					JsonUtil.AjaxWriter(response, map);
					return;
				}
				Integer userCount = session.getUserCount();
				if(!ParamUtils.chkInteger(userCount)) userCount = 0;
				userCount = userCount + sendBetNum;
				Integer pointCount = session.getPointCount();
				if(!ParamUtils.chkInteger(pointCount)) pointCount = 0;
				pointCount = pointCount + sendBetMoney;
				session.setUserCount(userCount);
				session.setPointCount(pointCount);
				userService.saveObject(session, null);
//				userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HBK3, sendBetMoney, null);
				gaK3DAO.updateObjectList(gkbList, null);
//				GaOrder order=new GaOrder();
//				order.setUserId(uid);
//				order.setPayType(payType);// 在线支付方式
//				order.setType(Constants.GAME_TYPE_K3);
//				order.setOrderNum(ProductUtil.createOrderNum(Constants.MODULE_K3));
//				order.setMoney(new BigDecimal(money));
//				order.setPayStatus("0");
//				order.setBetNum(allBet);
//				order.setCreateTime(new Date());
//				gaK3DAO.saveObject(order, null);
				
//				for(GaK3Bet gsb:gkbList){
//					gaK3DAO.saveObject(gsb, null);
//					BetOrderRl rl=new BetOrderRl();
//					rl.setBetId(gsb.getBetId());
//					rl.setOrderId(order.getOrderId());
//					gaK3DAO.saveObject(rl, null);
//				}
			
				
				
				// 普通订单信息
//				JSONObject obj = new JSONObject();
//				obj.put("title", "快3");
//				Integer oid = order.getOrderId();
//				String key = Constants.DES_KEY_OID + "=" + oid;
//
//				obj.put("key", DesUtils.encrypt(key));
//				obj.put("oid", oid);
//				obj.put("totalPrice",ProductUtil.BigFormatJud(order.getMoney()));
//				data.put("obj", obj);
//
//				if (payType.equals(APIConstants.PAY_TYPE_ALIPAY)) {
//					// 支付宝信息
//					JSONObject alipayObj = new JSONObject();
//					alipayObj.put("partner", Constants.getAlipayPartner());
//					alipayObj.put("rsa_private", Constants.getAlipayRsaPrivate());
//					alipayObj.put("_input_charset", Constants.PAY_ALIPAY_CHARSET);
//					alipayObj.put("notify_url", Constants.getAlipayCallBackGameK3());
//					alipayObj.put("out_trade_no", order.getOrderNum());
//					alipayObj.put("subject", "快3");
//					alipayObj
//							.put("payment_type", Constants.PAY_ALIPAY_PAYMENT_TYPE);
//					alipayObj.put("seller_id", Constants.getAlipaySellerId());
//					alipayObj.put("total_fee",ProductUtil.BigFormatJud(order.getMoney()));
//					alipayObj.put("body", "快3");
//
//					data.put("alipayObj", alipayObj);
//				} else if (payType.equals(APIConstants.PAY_TYPE_UNIONPAY)) {// 银联
//					OrderDTO dto = new OrderDTO();
//					dto.setCallback(Constants.getUnionCallBackInfoPublish());
//					dto.setOrderNum(order.getOrderNum());
//					dto.setTotalPrice(order.getMoney());
//					Map<String, String> resmap = Form_6_2_AppConsume.resmap(dto);
//					JSONObject unionpayObj = new JSONObject();
//					unionpayObj.put("respCode", resmap.get("respCode"));// 银联生成交易流水号的返回码
//					String tn = resmap.get("tn");
//					unionpayObj.put("tn", tn);// 银联生成的流水号
//					unionpayObj.put("orderId", resmap.get("orderId"));// 银联生成的订单编号
//					unionpayObj.put("txnTime", resmap.get("txnTime"));// 银联生成的交易时间
//
////						order.setTradeNo(tn);
////						orderService.saveObject(order, null);
//					data.put("unionpayObj", unionpayObj);
//				} else if (payType.equals(APIConstants.PAY_TYPE_WECHAT)) {// 微信
//					JSONObject weixinObj = new JSONObject();
//					try{
//						HashMap<String, String> resmap = ClientCustomSSL.payOrder(
//								order.getOrderNum(), "快3",
//								ParamUtils.BigFormatIntString(order.getMoney()),
//								Constants.getWeChatCallBackGameK3());
//						weixinObj.put("prepayid", resmap.get("prepayid"));// 生成交易流水号的返回码
//						weixinObj.put("appid", resmap.get("appid"));// 商户app号
//						weixinObj.put("noncestr", resmap.get("noncestr"));// 随机安全码
//						weixinObj.put("package", resmap.get("package"));// 包名
//						weixinObj.put("partnerid", resmap.get("partnerid"));// 商户使用ID
//						weixinObj.put("timestamp", resmap.get("timestamp"));// 时间戳
//						weixinObj.put("sign", resmap.get("sign"));// 参数签名
//						data.put("weChatObj", weixinObj);
//					}catch(Exception e){
//						e.printStackTrace();
//					}		
//				}
//				code = APIConstants.CODE_REQUEST_SUCCESS;
//				message="投注成功";
				map.put("data", data);
				map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
				map.put("msg", "投注成功！");
			}else{
//				code = APIConstants.CODE_REQUEST_ERROR;
//				message="不能投注";
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", "不能投注！");
			}
		}else{
//			code = APIConstants.CODE_REQUEST_ERROR;
//			message="该期不能投注了";
			map.put("code", APIConstants.CODE_REQUEST_ERROR);
			map.put("msg", "该期不能投注了！");
		}
	}
	
	public int  getBetTotal(String type,String content) {  
		int total=0;
		if(type.equals("1")){
			total=content.split(",").length;
		}else if(type.equals("2")){
			total=1;
		}else if(type.equals("3")){
			total=content.split(",").length;
		}else if(type.equals("4")){
			total=content.split(",").length;
		}else if(type.equals("5")){
			String xt=content.split("#")[0];
			String bt=content.split("#")[1];
//			if(xt.split(",").length>=1||bt.split(",").length<1){
				total=(xt.split(",").length)*(bt.split(",").length);
//			}else{
//				total=0;
//			}			
		}else if(type.equals("6")){
			int num=content.split(",").length;
			total=1;
			if(num>=3){
				for(int i=num;i>(num-3);i--){
					total=i*total;
				}
				total=total/6;
			}else{
				total=0;
			}		
		}else if(type.equals("7")){
			int num=content.split(",").length;
			total=1;
			if(num>=2){
				for(int i=num;i>(num-2);i--){
					total=i*total;
				}
				total=total/2;
			}else{
				total=0;
			}		
		}else if(type.equals("8")){
			total=1;
		}
		return total;
    }
}
