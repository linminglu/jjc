package com.game.service.impl;


import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.apps.Constants;
import com.apps.model.dto.OrderDTO;
import com.apps.pay.unionpay.Form_6_2_AppConsume;
import com.apps.pay.wechat.ClientCustomSSL;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.ParamUtils;
import com.game.GameConstants;
import com.game.dao.IGaSsqDAO;
import com.game.model.BetOrderRl;
import com.game.model.GaOrder;
import com.game.model.GaSsqBet;
import com.game.model.GaSsqBetDetail;
import com.game.model.GaSsqSession;
import com.game.service.IGaSsqService;

public class GaSsqServiceImpl extends BaseService implements IGaSsqService {
	private IGaSsqDAO gaSsqDAO;
	public void setGaSsqDAO(IGaSsqDAO gaSsqDao) {
		gaSsqDAO = gaSsqDao;
		super.dao = gaSsqDAO;
	}
	
	/**
	 * 初始化场次
	 * 00:00 生成当天的所有场次
	 * 每周二、四、七9.20开奖
	 * 一周3次一年52周，春节休一周，所以最多：51*3=153期
	 * 一次性生成一年的期数
	 */
	public String updateInitSession(){
		long t1 = System.currentTimeMillis();
		
		String flag = "fail";//返回状态
		
		String sessionYear = DateTimeUtil.getYyyy(DateTimeUtil.getJavaUtilDateNow());
		
		log.info("___[start]["+sessionYear+"]__________________________");
	
		for (int i = 0; i < GameConstants.SSQ_MAX_PART; i++) {
			
			String sessionNo = this.getTodaySessionNo(sessionYear, i+1);//期号
			GaSsqSession ssq = gaSsqDAO.getGaSsqSessionBySessionNo(sessionNo);
			if(ssq==null){
				ssq = new GaSsqSession();
				ssq.setSessionNo(sessionNo);
				ssq.setOpenStatus("0");
				ssq.setSessionYear(Integer.valueOf(sessionYear));//年份
				gaSsqDAO.saveObject(ssq);
				log.info("___[ssq init sessionNo=]"+sessionNo);
			}else{
				log.info("___[ssq sessionNo has been exist...]"+sessionNo);
			}

		}
		flag = "success";
		log.info("___[this year init completed]__________________________");
			
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
	public String getTodaySessionNo(String year,int index){
		String num = "";//
		if(index<10){
			num = "00"+index;
		}else if(index<100){
			num = "0"+index;
		}else{
			num = index+"";
		}
		return year+num;
	}
	
	
	/**
	 * 抓取开奖数据
	 */
	public void updateFetchAndOpenResult(){
		try {
			Document doc = Jsoup.connect("http://www.ydniu.com/open/ssq.aspx").get();
			
			//当前期信息抓取 - 直接保存到相关数据表~~~~~~~~~~~~~~~~~~~~~~~~
			String currentNo = "";//当前期
			String currentRed ="";//当前期开红球
			String currentBlue = "";//当前期开蓝球
			String currentOpenDate = "";//当前期开奖日期
			String currentCloseDate = "";//当前期兑奖截至日期
			String currentSale = "";//当前期销量
			String currentPool = "";//当前期奖池滚存
			String currentL1Count = "";//一等中奖注数
			String currentL1Money = "";//一等中奖金额
			String currentL2Count = "";//二等中奖注数
			String currentL2Money = "";//二等中奖金额
			String currentL3Count = "";//三等中奖注数
			String currentL3Money = "";//三等中奖金额
			String currentL4Count = "";//四等中奖注数
			String currentL4Money = "";//四等中奖金额
			String currentL5Count = "";//五等中奖注数
			String currentL5Money = "";//五等中奖金额
			String currentL6Count = "";//六等中奖注数
			String currentL6Money = "";//六等中奖金额
			
			
			//网页直接抓取
			currentNo = doc.getElementById("curIssueName").html();//期号
			
			Element labelTag = doc.getElementById("openNumber");
			Elements rediTags = labelTag.getElementsByTag("i");//红
			for(Element el:rediTags){
				currentRed += el.html()+",";
			}
			currentRed = currentRed.substring(0,currentRed.length()-1);
			
			Elements blueiTags = labelTag.getElementsByTag("em");//蓝
			for(Element el:blueiTags){
				currentBlue = el.html();
			}
			
			String openDateStr = doc.getElementById("openDate").html();//开奖日期
			currentOpenDate = openDateStr.split("，")[0].split("：")[1];
			currentCloseDate = openDateStr.split("，")[1].split("：")[1];
			
			currentSale = doc.getElementById("sumSales").html();//销量
			currentPool = doc.getElementById("prizePool").html();//奖池
			
			Element levelTag = doc.getElementById("t_WinType");//x等奖
			Elements trTags = levelTag.getElementsByTag("tr");
			
			for(int i=0;i<trTags.size();i++){
				Element tr = trTags.get(i);
				Elements tdTags = tr.getElementsByTag("td");
				if(i==0){
					currentL1Count = tdTags.get(1).html();
					currentL1Money = tdTags.get(2).html();
				}else if(i==1){
					currentL2Count = tdTags.get(1).html();
					currentL2Money = tdTags.get(2).html();
				}else if(i==2){
					currentL3Count = tdTags.get(1).html();
					currentL3Money = tdTags.get(2).html();
				}else if(i==3){
					currentL4Count = tdTags.get(1).html();
					currentL4Money = tdTags.get(2).html();
				}else if(i==4){
					currentL5Count = tdTags.get(1).html();
					currentL5Money = tdTags.get(2).html();
				}else if(i==5){
					currentL6Count = tdTags.get(1).html();
					currentL6Money = tdTags.get(2).html();
				}
			}
			//~~~~~~~~~~~~~
			
			//写业务，更新当前抓取到的数据
//			GaSsqSession session=gaSsqDAO.getGaSsqSessionBySessionNo(currentNo);
//			if(session!=null){
//				if(session.getOpenStatus()!=null&&session.getOpenResult().equals("0")){
//					session.setRedBall(currentRed);
//					session.setBlueBall(currentBlue);
//					session.setOpenStatus("2");
////					session.setFirstLever(new BigDecimal(currentL1Money));
//					session.setSecondLever(new BigDecimal(currentL2Money));
//					session.setOpenTime(new Date());
					this.openResult(currentNo,currentRed,currentBlue,currentL1Money,currentL2Money);
//				}
//			}
			
			log.info("currentNo="+currentNo);
			log.info("currentRed="+currentRed);
			log.info("currentBlue="+currentBlue);
			log.info("currentOpenDate="+currentOpenDate);
			log.info("currentCloseDate="+currentCloseDate);
			log.info("currentSale="+currentSale);
			log.info("currentPool="+currentPool);
			log.info("currentL1Count="+currentL1Count);
			log.info("currentL1Money="+currentL1Money);
			log.info("currentL2Count="+currentL2Count);
			log.info("currentL2Money="+currentL2Money);
			log.info("currentL3Count="+currentL3Count);
			log.info("currentL3Money="+currentL3Money);
			log.info("currentL4Count="+currentL4Count);
			log.info("currentL4Money="+currentL4Money);
			log.info("currentL5Count="+currentL5Count);
			log.info("currentL5Money="+currentL5Money);
			log.info("currentL6Count="+currentL6Count);
			log.info("currentL6Money="+currentL6Money);
			
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("___[fetch fail...]");
		}
		
		
	}
	
	public GaSsqSession getCurrentSession() {
		return gaSsqDAO.getCurrentSession();
	}
	

	public PaginationSupport findAllOpenSessionList(int pageNum,int pageSize){
		return gaSsqDAO.findAllOpenSessionList(pageNum,pageSize);
	}

	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findLotteryResultList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findOneUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findBetUserList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findBetDetailList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaSsqDAO.findUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public GaSsqSession getGaSsqSessionBySessionNo(String sessionNo){
		return gaSsqDAO.getGaSsqSessionBySessionNo(sessionNo);
	}
	public GaOrder getGaOrderByOrderNum(String orderNum){
		return gaSsqDAO.getGaOrderByOrderNum(orderNum);
	}
	public PaginationSupport findOrderList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		return gaSsqDAO.findOrderList(hqls, para, startIndex, pageSize);
	}
	public List findOrderView(Integer orderId){
		return gaSsqDAO.findOrderView(orderId);
	}
	
	/*
	 * 参数分别为开奖结果的 红球  蓝球  一等奖奖金  二等奖奖金
	 */
	public void openResult(String sessionNo,String redBall,String blueBall,String firstLevel,String secondLevel){
		System.out.println("sessionNo------------------->"+sessionNo);
		GaSsqSession session=gaSsqDAO.getGaSsqSessionBySessionNo(sessionNo);
		
		if(session!=null){
			if("2".equals(session.getOpenStatus())){//已开奖
				
			}else{//未开奖
				Integer sessionId=session.getSessionId();
				List<GaSsqBet> list=gaSsqDAO.findGaSsqBetListBySessionId(sessionId);
				List<GaSsqBet> saveBetList=new ArrayList<GaSsqBet>();
				List<GaSsqBetDetail> saveBetDetailList=new ArrayList<GaSsqBetDetail>();
				if(list!=null&&list.size()>0){
					for(GaSsqBet ssqBet:list){
						if(ssqBet.getBetType().equals(GameConstants.SSQ_BET_TYPE_DANSHI)){//单式
							this.saveSingleBet(saveBetList,saveBetDetailList,ssqBet,redBall,blueBall,ssqBet.getRedBall(),ssqBet.getBlueBall(),"-1",firstLevel,secondLevel);
//							if(ssqBet.getBlueBall().equals(blueBall)){
//								List sameList=same(redBall,ssqBet.getRedBall(),ssqBet.getRedBile()).get(0);
//								
//								GaSsqBetDetail detail=new GaSsqBetDetail();
//								detail.setBetId(ssqBet.getBetId());
	//
//								detail.setWinResult("1");
//								detail.setRedBall(ssqBet.getRedBall());
//								detail.setBlueBall(blueBall);
//								ssqBet.setOpenStatus("1");
//															
//								if(sameList.size()<=2){//选6+1中2+1或中1+1或中0+1  六等奖
//									detail.setWinLever("6");
//									detail.setWinCash(new BigDecimal(5));
//									ssqBet.setWinCash(new BigDecimal(5));
//								}else if(sameList.size()==3){//中3+1	  五等奖
//									detail.setWinLever("5");
//									detail.setWinCash(new BigDecimal(10));
//									ssqBet.setWinCash(new BigDecimal(10));	
//								}else if(sameList.size()==4){//中4+1	  四等奖
//									detail.setWinLever("4");
//									detail.setWinCash(new BigDecimal(200));
//									ssqBet.setWinCash(new BigDecimal(200));	
//								}else if(sameList.size()==5){//中5+1	  三等奖
//									detail.setWinLever("3");
//									detail.setWinCash(new BigDecimal(3000));
//									ssqBet.setWinCash(new BigDecimal(3000));	
//								}else if(sameList.size()==6){//中6+1	  一等奖
//									detail.setWinLever("1");
//									detail.setWinCash(new BigDecimal(firstLevel));
//									ssqBet.setWinCash(new BigDecimal(firstLevel));	
//								}
//								gaSsqDAO.saveObject(detail,null);
//								gaSsqDAO.saveObject(ssqBet,null);
//							}else{
//								List sameList=same(redBall,ssqBet.getRedBall(),ssqBet.getRedBile()).get(0);
//								if(sameList.size()<4){
//									ssqBet.setOpenStatus("0");
//								}else{
//									GaSsqBetDetail detail=new GaSsqBetDetail();
//									detail.setBetId(ssqBet.getBetId());
	//
//									detail.setWinResult("1");
//									detail.setRedBall(ssqBet.getRedBall());
//									detail.setBlueBall(blueBall);
//									ssqBet.setOpenStatus("1");
//									if(sameList.size()==4){//中4+0  五等奖
//										detail.setWinLever("5");
//										detail.setWinCash(new BigDecimal(10));
//										ssqBet.setWinCash(new BigDecimal(10));	
//									}else if(sameList.size()==5){
//										detail.setWinLever("4");
//										detail.setWinCash(new BigDecimal(200));
//										ssqBet.setWinCash(new BigDecimal(200));	
//									}else if(sameList.size()==6){
//										detail.setWinLever("2");
//										detail.setWinCash(new BigDecimal(secondLevel));
//										ssqBet.setWinCash(new BigDecimal(secondLevel));	
//									}
//									gaSsqDAO.saveObject(detail,null);								
//								}
//								gaSsqDAO.saveObject(ssqBet,null);		
//							}
							//
						}else if(ssqBet.getBetType().equals(GameConstants.SSQ_BET_TYPE_FUSHI)){//复式
							List <String> redCombineList=combine(ssqBet.getRedBall().split(","),6);
							String[] blueArray=ssqBet.getBlueBall().split(",");
							for(int k=0;k<blueArray.length;k++){
								for (int m=0;m<redCombineList.size();m++){
									saveSingleBet(saveBetList,saveBetDetailList,ssqBet,redBall,blueBall,redCombineList.get(m),blueArray[k],"-1",firstLevel,secondLevel);
								}			
							}
							

						}else if(ssqBet.getBetType().equals(GameConstants.SSQ_BET_TYPE_DANTUO)){//胆球
							List <String> redCombineList=combine((ssqBet.getRedBall()+","+ssqBet.getRedBile()).split(","),6);
							String[] blueArray=ssqBet.getBlueBall().split(",");
							for(int k=0;k<blueArray.length;k++){
								for (int m=0;m<redCombineList.size();m++){
									saveSingleBet(saveBetList,saveBetDetailList,ssqBet,redBall,blueBall,redCombineList.get(m),blueArray[k],"-1",firstLevel,secondLevel);
								}			
							}
						}
					}
					gaSsqDAO.updateObjectList(saveBetList, null);
					gaSsqDAO.updateObjectList(saveBetList, null);
				}
				session.setRedBall(redBall);
				session.setBlueBall(blueBall);
				session.setOpenStatus(GameConstants.SSQ_OPEN_STATUS_OPENED);
				session.setOpenTime(new Date());
				gaSsqDAO.saveObject(session,null);
			}
		}else{
			
		}
		
	}
	
	/**
	 * 返回相同的红球和不相同的红球
	 */
	public List<List<String>> same(String redBall,String betRedBall,String  betRedBile){
		List<List<String>> newList=new ArrayList<List<String>>();
		String[] redBallArray=redBall.split(",");
		String[] betRedBallArray=null;
		if(!"-1".equals(betRedBile)){
			betRedBallArray=(betRedBall+","+betRedBile).split(",");
		}else{
			betRedBallArray=betRedBall.split(",");
		}
		int count=0;
		List<String> sameList=new ArrayList<String>();
		List<String> notList=new ArrayList<String>();
		for(int i=0;i<betRedBallArray.length;i++){
			for(int j=0;j<redBallArray.length;j++){
				if(betRedBallArray[i].equals(redBallArray[j])){
					sameList.add(betRedBallArray[i]);
					count++;
				}else{
					notList.add(betRedBallArray[i]);
				}
			}
		}
		newList.add(sameList);
		newList.add(notList);
		return newList;
	}
	
	//是否有蓝球中
//	public String blueSame(String  betBlue,String openBlue){
//		String blueBall="";
//		String[] blueArray=betBlue.split(",");
//		for(int i=0;i<blueArray.length;i++){
//			if(blueArray[i].equals(openBlue)){
//				blueBall=openBlue;
//			}
//		}
//		return blueBall;
//	}
	
	
	/**
	 * 单式计算中奖方法  List<GaSsqBet> saveBetList=new ArrayList<GaSsqBet>();
				List<GaSsqBetDetail> saveBetDetailList=new ArrayList<GaSsqBetDetail>();
	 */
	public  void  saveSingleBet(List<GaSsqBet> saveBetList,List<GaSsqBetDetail> saveBetDetailList,GaSsqBet ssqBet,String openRed,String openBlue,String betRed,String betBlue,String betbile,String firstLevel,String secondLevel){
		if(betBlue.equals(openBlue)){//蓝球相同
			List sameList=same(openRed,betRed,betbile).get(0);
			
			GaSsqBetDetail detail=new GaSsqBetDetail();
			detail.setBetId(ssqBet.getBetId());

			detail.setWinResult(GameConstants.SSQ_WIN_STATUS_YES);
			detail.setRedBall(betRed);
			detail.setBlueBall(betBlue);
			BigDecimal winCash=(ssqBet.getWinCash()==null?new BigDecimal(0):new BigDecimal(5));
			if(!GameConstants.SSQ_WIN_STATUS_YES.equals(ssqBet.getOpenStatus())){
				ssqBet.setOpenStatus(GameConstants.SSQ_WIN_STATUS_YES);
			}	
			if(sameList.size()<=2){//选6+1中2+1或中1+1或中0+1    六等奖
				detail.setWinLever("6");
				detail.setWinCash(new BigDecimal(5).multiply(new BigDecimal(ssqBet.getMultiple())));
				ssqBet.setWinCash(winCash.add(new BigDecimal(5).multiply(new BigDecimal(ssqBet.getMultiple()))));
			}else if(sameList.size()==3){//中3+1	  五等奖
				detail.setWinLever("5");
				detail.setWinCash(new BigDecimal(10).multiply(new BigDecimal(ssqBet.getMultiple())));
				ssqBet.setWinCash(winCash.add(new BigDecimal(10).multiply(new BigDecimal(ssqBet.getMultiple()))));	
			}else if(sameList.size()==4){//中4+1	  四等奖
				detail.setWinLever("4");
				detail.setWinCash(new BigDecimal(200).multiply(new BigDecimal(ssqBet.getMultiple())));
				ssqBet.setWinCash(winCash.add(new BigDecimal(200).multiply(new BigDecimal(ssqBet.getMultiple()))));	
			}else if(sameList.size()==5){//中5+1	  三等奖
				detail.setWinLever("3");
				detail.setWinCash(new BigDecimal(3000).multiply(new BigDecimal(ssqBet.getMultiple())));
				ssqBet.setWinCash(winCash.add(new BigDecimal(3000).multiply(new BigDecimal(ssqBet.getMultiple()))));	
			}else if(sameList.size()==6){//中6+1	  一等奖
				detail.setWinLever("1");
				detail.setWinCash(new BigDecimal(firstLevel).multiply(new BigDecimal(ssqBet.getMultiple())));
				ssqBet.setWinCash(winCash.add(new BigDecimal(firstLevel).multiply(new BigDecimal(ssqBet.getMultiple()))));	
			}
//			gaSsqDAO.saveObject(detail,null);
//			gaSsqDAO.saveObject(ssqBet,null);
			saveBetList.add(ssqBet);
			saveBetDetailList.add(detail);
		}else{//蓝球不相同
			List sameList=same(openRed,betRed,betbile);
			if(sameList.size()<4){
				if(GameConstants.SSQ_WIN_STATUS_YES.equals(ssqBet.getOpenStatus())){				
				}else{
					ssqBet.setOpenStatus(GameConstants.SSQ_WIN_STATUS_NO);
				}			
			}else{
				BigDecimal winCash=(ssqBet.getWinCash()==null?new BigDecimal(0):new BigDecimal(5));
				GaSsqBetDetail detail=new GaSsqBetDetail();
				detail.setBetId(ssqBet.getBetId());

				detail.setWinResult(GameConstants.SSQ_WIN_STATUS_YES);
				detail.setRedBall(betRed);
				detail.setBlueBall(betBlue);
				ssqBet.setOpenStatus(GameConstants.SSQ_WIN_STATUS_YES);
				if(sameList.size()==4){//中4+0  五等奖
					detail.setWinLever("5");
					detail.setWinCash(new BigDecimal(10).multiply(new BigDecimal(ssqBet.getMultiple())));
					ssqBet.setWinCash(winCash.add(new BigDecimal(10).multiply(new BigDecimal(ssqBet.getMultiple()))));	
				}else if(sameList.size()==5){//中5+0 四等奖
					detail.setWinLever("4");
					detail.setWinCash(new BigDecimal(200).multiply(new BigDecimal(ssqBet.getMultiple())));
					ssqBet.setWinCash(winCash.add(new BigDecimal(200).multiply(new BigDecimal(ssqBet.getMultiple()))));	
				}else if(sameList.size()==6){////中6+0 二等奖
					detail.setWinLever("2");
					detail.setWinCash(new BigDecimal(secondLevel).multiply(new BigDecimal(ssqBet.getMultiple())));
					ssqBet.setWinCash(winCash.add(new BigDecimal(secondLevel).multiply(new BigDecimal(ssqBet.getMultiple()))));	
				}
//				gaSsqDAO.saveObject(detail,null);				
			
				saveBetDetailList.add(detail);
			}
//			gaSsqDAO.saveObject(ssqBet,null);
			saveBetList.add(ssqBet);
		}
	}
//	public List<String> getSaveRedBallList(List<String> notList,List<String> sameList,int level){
//		List<String> resultList=new ArrayList<String>();
//		if(level==4){
//			for(int i=0;i<notList.size();i++){
//				for(int j=i+1;j<notList.size();j++){
//					List<String> newList=new ArrayList<String>();
//					newList.addAll(sameList);
//					newList.add(notList.get(i));
//					newList.add(notList.get(j));
//					Collections.sort(newList);
//					String result="";
//					for(int m=0;m<newList.size();m++){
//						result=result+newList.get(m)+",";
//					}
//					result=result.substring(0, result.length()-1);
//					resultList.add(result);
//				}
//			}
//		}else if(level==5){
//			for(int i=0;i<notList.size();i++){
//					List<String> newList=new ArrayList<String>();
//					newList.addAll(sameList);
//					newList.add(notList.get(i));
//					Collections.sort(newList);
//					String result="";
//					for(int m=0;m<newList.size();m++){
//						result=result+newList.get(m)+",";
//					}
//					result=result.substring(0, result.length()-1);
//					resultList.add(result);
//			}
//		}
//		return resultList;
//	}
	
	/**
	 * 所有组合（红球） num=6
	 */
	 private List<String> combine(String[] a, int num) {
//		 String[] a=(String[])redBallList.toArray(new String[redBallList.size()]);
		  List<String> list = new ArrayList<String>();
		  StringBuffer sb = new StringBuffer();
		  String[] b = new String[a.length];
		  for (int i = 0; i < b.length; i++) {
		   if (i < num) {
		    b[i] = "1";
		   } else
		    b[i] = "0";
		  }

		  int point = 0;
		  int nextPoint = 0;
		  int count = 0;
		  int sum = 0;
		  String temp = "1";
		  while (true) {
		   // 判断是否全部移位完毕
		   for (int i = b.length - 1; i >= b.length - num; i--) {
		    if (b[i].equals("1"))
		     sum += 1;
		   }
		   // 根据移位生成数据
		   for (int i = 0; i < b.length; i++) {
		    if (b[i].equals("1")) {
		     point = i;
		     sb.append(a[point]);
		     sb.append(",");
		     count++;
		     if (count == num)
		      break;
		    }
		   }
		   // 往返回值列表添加数据
		   list.add(sb.toString().substring(0, sb.toString().length()-1));

		   // 当数组的最后num位全部为1 退出
		   if (sum == num) {
		    break;
		   }
		   sum = 0;

		   // 修改从左往右第一个10变成01
		   for (int i = 0; i < b.length - 1; i++) {
		    if (b[i].equals("1") && b[i + 1].equals("0")) {
		     point = i;
		     nextPoint = i + 1;
		     b[point] = "0";
		     b[nextPoint] = "1";
		     break;
		    }
		   }
		   // 将 i-point个元素的1往前移动 0往后移动
		   for (int i = 0; i < point - 1; i++)
		    for (int j = i; j < point - 1; j++) {
		     if (b[i].equals("0")) {
		      temp = b[i];
		      b[i] = b[j + 1];
		      b[j + 1] = temp;
		     }
		    }
		   // 清空 StringBuffer
		   sb.setLength(0);
		   count = 0;
		  }
		  // 
//		  System.out.println("数据长度 " + list.size());
		  return list;

		 }
	 
	 public void saveUserBet(JSONObject map,GaSsqSession session,Integer uid,String ditems,Integer countSession,Integer times
				,Integer countBet,Integer money,String payType){
		 
			JSONObject data = new JSONObject();// 返回数据层
			if(session!=null){
				Date date=new Date();
				if(date.after(session.getStartTime())&&date.before(session.getEndTime())){
					JSONArray jsonArray = new JSONArray(ditems);
					int sendBetNum=0;//传递过来的注数
					int sendBetMoney=0;//传递过来的钱数
					int allBet=0;//所有注数
					List<GaSsqBet> gsbList=new ArrayList<GaSsqBet>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						if(jsonObj.get("redBall")!=null&&jsonObj.get("blueBall")!=null&&jsonObj.get("redBile")!=null
								&&jsonObj.get("betNum")!=null&&jsonObj.get("betMoney")!=null){
							int betNum=Integer.parseInt((String) jsonObj.get("betNum"));
							int betMoney=Integer.parseInt((String) jsonObj.get("betMoney"));
							sendBetNum+=betNum;
							sendBetMoney+=betMoney;
							
							String redBall=(String) jsonObj.get("redBall");
							String blueBall=(String) jsonObj.get("blueBall");
							String redBile=(String) jsonObj.get("redBile");
							int redBallNum=redBall.split(",").length;
							int redBileNum=0;
							if(!redBile.equals("-1")){
								redBileNum=redBile.split(",").length;
							}
							int blueBallNum=blueBall.split(",").length;
							if(redBileNum>5||(redBallNum+redBileNum)<6||blueBallNum<1){
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "投注球个数不正确！");
//								JsonUtil.AjaxWriter(response, map);
								return;
							}
//							System.out.println("redBallNum:"+redBallNum+"   redBileNum:"+redBileNum+"  blueBallNum:"+blueBallNum);
							int thisBetNum=getBetTotal(redBallNum,redBileNum,blueBallNum);
//							System.out.println("thisBetNum:------->"+thisBetNum);
//							System.out.println("betNum:------->"+betNum);
							if(thisBetNum!=betNum){
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "投注注数计算出错！");
//								JsonUtil.AjaxWriter(response, map);
								return;
							}
							if(betMoney!=thisBetNum*2){//每注2元
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "投注金额计算出错！");
//								JsonUtil.AjaxWriter(response, map);
								return;
							}
							allBet=allBet+thisBetNum;						
							
							for(int j=0;j<countSession;j++){//多少期
								GaSsqBet gsb=new GaSsqBet();
								gsb.setBetTime(date);
								gsb.setRedBall(redBall);
								gsb.setBlueBall(blueBall);
								if(!redBile.equals("-1")){//默认 如果无胆球 胆球就传的-1
									gsb.setRedBile(redBile);
									gsb.setBetType(GameConstants.SSQ_BET_TYPE_DANTUO);//胆拖
								}else{
									if(redBileNum>6||blueBallNum>2){
										gsb.setBetType(GameConstants.SSQ_BET_TYPE_FUSHI);//复式
									}else{
										gsb.setBetType(GameConstants.SSQ_BET_TYPE_DANSHI);//单式
									}
								}
								gsb.setOpenStatus(GameConstants.SSQ_OPEN_STATUS_INIT);//未开奖
								gsb.setSessionId(session.getSessionId()+j);//期号
								gsb.setTotalNum(thisBetNum*times);
								gsb.setTotalPoint(new BigDecimal(thisBetNum*2*times));
								gsb.setMultiple(times);
								gsb.setUserId(uid);
								gsbList.add(gsb);
							}
							
						}else{
							map.put("code", APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "参数不正确！");
//							JsonUtil.AjaxWriter(response, map);
							return;
						}
					}
					
					if(sendBetNum!=allBet){//||allBet*times*countSession!=countBet
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "注数计算出错！");
//						JsonUtil.AjaxWriter(response, map);
						return;
					}
					if(allBet*times*countSession*2!=money||sendBetMoney!=allBet*2){
						map.put("code", APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "金额计算出错！");
//						JsonUtil.AjaxWriter(response, map);
						return;
					}
					
					List<GaSsqSession> slist=new ArrayList<GaSsqSession>();
					for(int j=0;j<countSession;j++){//多少期
						if(j==0){
							session.setSessionCash(0);				
							session.setPointCount((session.getPointCount()==null?0:session.getPointCount())+allBet);
						}else{
							GaSsqSession newSession=(GaSsqSession) gaSsqDAO.getObject(GaSsqSession.class, session.getSessionId()+j);
							if(newSession!=null){
								newSession.setSessionCash(0);				
								newSession.setPointCount((session.getPointCount()==null?0:session.getPointCount())+allBet);
								slist.add(newSession);
							}else{
								map.put("code", APIConstants.CODE_REQUEST_ERROR);
								map.put("msg", "不能投注下一期");
//								JsonUtil.AjaxWriter(response, map);
								return;
							}
						}			
					}
					
					
					GaOrder order=new GaOrder();
					order.setUserId(uid);
					order.setPayType(payType);// 在线支付方式
					order.setType(Constants.GAME_TYPE_SSQ);
					order.setOrderNum(ProductUtil.createOrderNum(Constants.MODULE_SSQ));
					order.setMoney(new BigDecimal(money));
					order.setPayStatus("0");//0是未付款 1是已付款
					order.setBetNum(allBet);
					order.setCreateTime(new Date());
					gaSsqDAO.saveObject(order, null);
					
					for(GaSsqBet gsb:gsbList){
						gaSsqDAO.saveObject(gsb, null);
						BetOrderRl rl=new BetOrderRl();
						rl.setBetId(gsb.getBetId());
						rl.setOrderId(order.getOrderId());
						gaSsqDAO.saveObject(rl, null);
					}
//					gaSsqService.updateObjectList(gsbList, null);
					gaSsqDAO.updateObjectList(slist, null);
					
					// 普通订单信息
					JSONObject obj = new JSONObject();
					obj.put("title", "双色球");
					Integer oid = order.getOrderId();
					String key = Constants.DES_KEY_OID + "=" + oid;

					obj.put("key", DesUtils.encrypt(key));
					obj.put("oid", oid);
					obj.put("totalPrice",ProductUtil.BigFormatJud(order.getMoney()));
					data.put("obj", obj);

					if (payType.equals(APIConstants.PAY_TYPE_ALIPAY)) {
						// 支付宝信息
						JSONObject alipayObj = new JSONObject();
						alipayObj.put("partner", Constants.getAlipayPartner());
						alipayObj.put("rsa_private", Constants.getAlipayRsaPrivate());
						alipayObj.put("_input_charset", Constants.PAY_ALIPAY_CHARSET);
						alipayObj.put("notify_url", Constants.getAlipayCallBackGameSsq());
						alipayObj.put("out_trade_no", order.getOrderNum());
						alipayObj.put("subject", "双色球");
						alipayObj
								.put("payment_type", Constants.PAY_ALIPAY_PAYMENT_TYPE);
						alipayObj.put("seller_id", Constants.getAlipaySellerId());
						alipayObj.put("total_fee",ProductUtil.BigFormatJud(order.getMoney()));
						alipayObj.put("body", "双色球");

						data.put("alipayObj", alipayObj);
					} else if (payType.equals(APIConstants.PAY_TYPE_UNIONPAY)) {// 银联
						OrderDTO dto = new OrderDTO();
						dto.setCallback(Constants.getUnionCallBackInfoPublish());
						dto.setOrderNum(order.getOrderNum());
						dto.setTotalPrice(order.getMoney());
						Map<String, String> resmap = Form_6_2_AppConsume.resmap(dto);
						JSONObject unionpayObj = new JSONObject();
						unionpayObj.put("respCode", resmap.get("respCode"));// 银联生成交易流水号的返回码
						String tn = resmap.get("tn");
						unionpayObj.put("tn", tn);// 银联生成的流水号
						unionpayObj.put("orderId", resmap.get("orderId"));// 银联生成的订单编号
						unionpayObj.put("txnTime", resmap.get("txnTime"));// 银联生成的交易时间

//						order.setTradeNo(tn);
//						orderService.saveObject(order, null);
						data.put("unionpayObj", unionpayObj);
					} else if (payType.equals(APIConstants.PAY_TYPE_WECHAT)) {// 微信
						try{
						JSONObject weixinObj = new JSONObject();
						HashMap<String, String> resmap = ClientCustomSSL.payOrder(
								order.getOrderNum(), "双色球",
								ParamUtils.BigFormatIntString(order.getMoney()),
								Constants.getWeChatCallBackGameSsq());
						weixinObj.put("prepayid", resmap.get("prepayid"));// 生成交易流水号的返回码
						weixinObj.put("appid", resmap.get("appid"));// 商户app号
						weixinObj.put("noncestr", resmap.get("noncestr"));// 随机安全码
						weixinObj.put("package", resmap.get("package"));// 包名
						weixinObj.put("partnerid", resmap.get("partnerid"));// 商户使用ID
						weixinObj.put("timestamp", resmap.get("timestamp"));// 时间戳
						weixinObj.put("sign", resmap.get("sign"));// 参数签名
						data.put("weChatObj", weixinObj);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
//					code = APIConstants.CODE_REQUEST_SUCCESS;
//					message="投注成功";
					map.put("data", data);
					map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
					map.put("msg", "投注成功！");
				}else{
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "不能投注！");
//					code = APIConstants.CODE_REQUEST_ERROR;
//					message="不能投注";
				}
			}else{
//				code = APIConstants.CODE_REQUEST_ERROR;
//				message="该期不能投注了";
				map.put("code", APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", "本期不能投注了！");
			}
	 }
	  //计算所有排列组合的总数量  rednum红球数量  bile胆球数量  bluenum胆球数量
		public int  getBetTotal(int redNum,int bile,int blueNum) { 

			int m=redNum;
			int n=6-bile;
			int mTotal=1;
			int nTotal=1;
			for(int i=m;i>=(m-n+1);i--){
				mTotal=mTotal*i;
			}
			for(int j=1;j<=n;j++){
				nTotal=nTotal*j;
			}
			return blueNum*mTotal/nTotal;
	    }

}
