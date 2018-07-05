package com.gf.dcb.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.model.UserTradeDetail;
import com.apps.util.ProductUtil;
import com.gf.dcb.DcbConstants;
import com.gf.dcb.dao.IDcbDAO;
import com.gf.dcb.model.GfDcbGaOmit;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.model.GfDcbGaTrend;
import com.gf.dcb.model.dto.GfDcbDTO;
import com.gf.dcb.service.IDcbService;
import com.gf.dcb.util.DcbUtil;
import com.gf.fivecolor.model.GfFiveGaSession;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.util.xml.XmlUtil;
import com.game.model.GaBetDetail;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.dto.SpDetailDTO;
import com.game.service.IGaService;
import com.gf.pick11.gdpick11.model.GfGdPick11GaOmit;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.URLUtil;
import com.gf.ssc.cqssc.model.GfCqSscGaSession;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.gf.three.model.GfThreeGaSession;

public class DcbServiceImpl  extends BaseService implements IDcbService {
	private IDcbDAO gfDcbDAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setGfDcbDAO(IDcbDAO gfDcbDAO) {
		this.gfDcbDAO = gfDcbDAO;
		super.dao = gfDcbDAO;
		
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
	
		String flag = "false"; //初始化是否成功
		boolean isInitNo = this.checkInitSessionNo(); //是否初始化今年期号。
		
		if(!isInitNo){
			List<GfDcbGaSession> saList = new ArrayList<GfDcbGaSession>();
			int yyyy = DateTimeUtil.getRightYear(); // 年  格式为 yyyy
			String year = DateTimeUtil.getYy(DateTimeUtil.getJavaUtilDateNow()); //年  格式为yy
			int days;//某年(year)的天数
			days = new GregorianCalendar().isLeapYear(yyyy) ? 359 : 358; //春节7天没有开奖
			int j = 1 ;// 第一期期号
			for(int i=0; i<days; i++){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
				Date firthDay = null;
				try {
					firthDay = format.parse(yyyy + "-01-01");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				Date dateAfter = DateTimeUtil.getDateAfter(firthDay, i, "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();
				 c.setTime(dateAfter);  
				 int dayForWeek = 0;
				 if(c.get(Calendar.DAY_OF_WEEK) == 1){
				  dayForWeek = 7;  
				 }else{  
				  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
				 }
				 
				 if(dayForWeek == 2 || dayForWeek == 4 || dayForWeek == 7){
						String sessionNo = year + String.format("%03d", j);
						j = j + 1;
						int beforDays = i-2;
						if(dayForWeek == 7){
							beforDays = i-3;
						}
						Date dateAfter2 = DateTimeUtil.getDateAfter(firthDay, beforDays, "yyyy-MM-dd HH:mm:ss");   

						String startTime = DateTimeUtil.DateToString(dateAfter2) + " 21:30:00"; //开盘时间
						String endTime = DateTimeUtil.DateToString(dateAfter) + " 20:00:00"; // 封盘时间

						GfDcbGaSession GfDcbGaSession = new GfDcbGaSession();
						GfDcbGaSession.setSessionNo(sessionNo);
						GfDcbGaSession.setStartTime(DateTimeUtil.parse(startTime));
						GfDcbGaSession.setEndTime(DateTimeUtil.parse(endTime));
						GfDcbGaSession.setOpenStatus(Constants.OPEN_STATUS_INIT);
						GfDcbGaSession.setAwardTime(DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(endTime),29));
						saList.add(GfDcbGaSession);
				 }
			}
			gfDcbDAO.updateObjectList(saList, null);
			flag = "success";
			this.updateSessionNo(DateTimeUtil.StringToDate("2017-02-03"),DateTimeUtil.StringToDate("2017-12-31"),13);
		}else{
			flag = "inited";
		}
		return flag;
	}
	
	@Override
	public void updateSessionNo(Date startDate,Date endDate,int no){

		int days = DateTimeUtil.daysBetween(startDate, endDate) +1;//时间间隔天数
		List<GfDcbGaSession> saveList = new ArrayList<GfDcbGaSession>();
		for(int i=0; i<days; i++){
			Date dateAfter = DateTimeUtil.getDateAfter(startDate, i, "yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			 c.setTime(dateAfter);
			 int dayForWeek = 0; //每天时间是周几
			 if(c.get(Calendar.DAY_OF_WEEK) == 1){
			  dayForWeek = 7;  
			 }else{  
			  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
			 }
			 
			 if(dayForWeek == 2 || dayForWeek == 4 || dayForWeek == 7){ //如果是周二，周四，周天，则开奖
					String year = DateTimeUtil.getYy(startDate); //年  格式为yy
					String sessionNo = year + String.format("%03d", no);
					no = no + 1;
					int beforDays = i-2;     //推算前2天或者前三天是开始投注时间
					if(dayForWeek == 7){//如果是周天
						beforDays = i-3;
					}
					Date start = DateTimeUtil.getDateAfter(startDate, beforDays, "yyyy-MM-dd"); //开盘年月日

					String startTime = DateTimeUtil.DateToString(start) + " 21:30:00"; //开盘时间
					String endTime = DateTimeUtil.DateToString(dateAfter) + " 20:00:00"; // 封盘时间
					GfDcbGaSession GfDcbGaSession = gfDcbDAO.getPreviousSessionBySessionNo(sessionNo);
					GfDcbGaSession.setStartTime(DateTimeUtil.parse(startTime));
					GfDcbGaSession.setEndTime(DateTimeUtil.parse(endTime));
					saveList.add(GfDcbGaSession);
			 }
       }
	   gfDcbDAO.updateObjectList(saveList, null);
	}
	
	/**
	 * 检查期号是否已生成
	 */
	private boolean checkInitSessionNo() {
		String sessionNo;
		String year = DateTimeUtil.getYy(DateTimeUtil.getJavaUtilDateNow()); //格式为yy 例如：07
		sessionNo = year + "001";
		HQUtils hq = new HQUtils(" from GfDcbGaSession six where six.sessionNo = ? ");
		hq.addPars(sessionNo);
		Integer count = gfDcbDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}
	
	/**
	 * 把开奖结果转换冷热排行榜的值
	 * @param result
	 * @return
	 */
	private Map<String, Boolean> transHotResult(String result) {
		String[]arrResult = result.split("\\+")[0].split(",");
		String blue= result.split("\\+")[1];
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>(); // 开奖结果
		for(int i=0; i<= 5; i++){
			resultMap.put("红球"+arrResult[i], true);
		}
		resultMap.put("蓝球"+blue, true);    
		return resultMap;		
	}

	/**
	 * 抓取开奖结果并开奖
	 */
	public List<GfDcbGaSession> updateAndOpenResult() {
		List<GfDcbGaSession> sessionlist = new ArrayList<GfDcbGaSession>();

		GfDcbGaSession currentSession= gfDcbDAO.getCurrentSession();
		GfDcbGaSession tempsession =gfDcbDAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
		if(tempsession!=null){
		}else{
			tempsession=(GfDcbGaSession) gfDcbDAO.getObject(GfDcbGaSession.class, (currentSession.getSessionId()-1));
		}
		final String lastSessionNo=tempsession.getSessionNo();
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
            	
            		    GaSessionInfo sessionInfo=CacheUtil.getGameOpenUrl().get("gfdcb");
            		    String officalURL ="";
            		    String urlSwitch=sessionInfo.getUrlSwitch();
            		    if(urlSwitch.equals("1")){//1=开彩网  2=彩票控
            		    	officalURL = sessionInfo.getKaicaiUrl()+"&timestamp="+System.currentTimeMillis();
            		    }else if(urlSwitch.equals("2")){
            		    	officalURL = sessionInfo.getCaipiaoUrl()+"&timestamp="+System.currentTimeMillis();
            		    }
	               		String resultXML = URLUtil.HttpRequestUTF8(officalURL);
	               		log.info("----resultXML---->"+resultXML);
	            	    sleep(3000);
	               		if(ParamUtils.chkString(resultXML)){
	               			Document xmlDoc = XmlUtil.getDOMDocumentFromString(resultXML);
	               				               			
	               			//开始解析场次开奖数据
	               			NodeList nList = xmlDoc.getElementsByTagName("row");
	               			String sessionNo="";//场次号
	               			String result="";//开奖结果5组数字
	               			String time="";//开奖时间
	               			String isFirstSession = "";
	               			for(int i =0;i<nList.getLength();i++){
	               				Node node = nList.item(i);
	               				sessionNo = XmlUtil.getElementAttribute((Element)node, "expect");
	               				result = XmlUtil.getElementAttribute((Element)node, "opencode");
	               				time = XmlUtil.getElementAttribute((Element)node, "opentime");
	               				sessionNo=sessionNo.substring(2);

	               				if(i==0){
	               					isFirstSession = sessionNo;
	               					sessionNoMap.put("lastNo", sessionNo);
	               				}
           						if(sessionNoMap.get(sessionNo)==null){
           							sessionNoMap.put(sessionNo, result);
           							timeMap.put(sessionNo, time);    	    	               		
           						}
//           						if(i==nList.getLength()-1){
//           							interrupt();
//           						}
	               			}
   						  if(lastSessionNo.equals(isFirstSession)){
							  interrupt();
							  break;
						  }

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
//					log.info("----k--->"+key+"------value----"+sessionNoMap.get(key));
					if(!key.equals("lastNo")){
						HQUtils hq = new HQUtils("from GfDcbGaSession se where se.sessionNo= ?");
						hq.addPars(key);
						GfDcbGaSession session = (GfDcbGaSession)gfDcbDAO.getObject(hq);

						if(session!=null){
							String openStatus = tempsession.getOpenStatus();//开奖状态
							String openResult = sessionNoMap.get(key);

							if(openStatus.equals(Constants.OPEN_STATUS_INIT) || openStatus.equals(Constants.OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
								//更新开奖结果
								session.setOpenResult(openResult);
								session.setOpenTime(DateTimeUtil.stringToDate(timeMap.get(key), "yyyy-MM-dd HH:mm:ss"));
								session.setOpenStatus(Constants.OPEN_STATUS_OPENED);
								gfDcbDAO.saveObject(session);
								sessionlist.add(session);
							}
						}
					}
				}
				
				GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_DCB);
				if(sessionInfo==null){
					sessionInfo = new GaSessionInfo();
					sessionInfo.setGameTitle("双色球");
					sessionInfo.setGameType(Constants.GAME_TYPE_GF_DCB);
				}
				sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
				if(sessionNoMap.get(lastSessionNo)!=null&&tempsession!=null){
					sessionInfo.setOpenResult(sessionNoMap.get(lastSessionNo));
					sessionInfo.setOpenSessionNo(tempsession.getSessionNo());
					sessionInfo.setEndTime(tempsession.getEndTime());
				}else{
					String lastNo = sessionNoMap.get("lastNo");
					if(ParamUtils.chkString(lastNo)){
						sessionInfo.setOpenResult(sessionNoMap.get(lastNo));
						sessionInfo.setOpenSessionNo(lastNo);
						sessionInfo.setEndTime(DateTimeUtil.StringToDate(timeMap.get(lastNo),"yyyy-MM-dd HH:mm:ss"));							
					}
				}
				gfDcbDAO.saveObject(sessionInfo);

				sessionNoMap.clear();
				timeMap.clear();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sessionlist;
	}

	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public String updateGfDcbGaSessionOpenResultMethod(GfDcbGaSession dcbsession,String result,String orderNum){

		try{
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<GaBetSponsorDetail> spDeList = new ArrayList<GaBetSponsorDetail>();
			Map<Integer,BigDecimal> userWinMap=new HashMap<Integer,BigDecimal>();//key:userId  value:中奖金额			
			List<Integer> puserIds = new ArrayList<Integer>();
			List<Integer> userIds = new ArrayList<Integer>();
			
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and sp.sessionId=? ");
			para.add(dcbsession.getSessionId());
			hqls.append(" and sp.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and sp.winResult=? " );
			para.add(Constants.INIT);// 未开奖
			hqls.append(" and sp.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_DCB);
			if(orderNum != null){
				hqls.append(" and sp.orderNum =? ");
				para.add(orderNum);
			}

			List<GaBetSponsor> list = gaService.findGaBetSponsorList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单

			String type="";
			if(list!=null&&list.size()>0){
				for(int i = 0; i < list.size(); i++){
					GaBetSponsor sp = list.get(i);
					int winCash = 0; //一个订单中奖金额。
				    List<GaBetSponsorDetail>  spde = gaService.findGaBetSponsorDetailListByJointId(sp.getJointId());//合买的具体投注项列表
				    if(spde !=null && spde.size() >0){
						for(GaBetSponsorDetail det:spde){

							
							String betBall = det.getOptionTitle();//具体投注内容
							List<String> winList = DcbUtil.judgeWin(betBall, result); //中奖列表
							
							int winSum = 0;//单项中奖总金额
							if(winList!= null && winList.size()>0){
								winSum = DcbUtil.countBonuses(winList, sp.getMultiple());
							}

							if(winSum>0){
								det.setWinResult(Constants.WIN);
								winCash = winCash + winSum;
							}else{
								det.setWinResult(Constants.WIN_NO);
							}
							det.setWinMoney(new BigDecimal(winSum));
							spDeList.add(det);
						}
				    }
				    BigDecimal winCashDe = new BigDecimal(winCash); //中奖金额BigDecimal格式
				    if(winCash >0){
				    	sp.setWinResult(Constants.WIN);
				    }else{
				    	sp.setWinResult(Constants.WIN_NO);
				    }
				    sp.setWinCash(winCashDe);
				    sp.setOpenResult(result);
				    sp.setOpenTime(DateTimeUtil.getNowSQLDate());
//				    sp.setBetFlag(Constants.PUB_STATUS_CLOSE);
				    spList.add(sp);
				    
				    //如果中奖了，才查询投注人，并分配奖金
				    if(winCash>0){
						List<SpDetailDTO>  part = gaService.findGaBetPartListByJointId(sp.getJointId());
						if(part != null && part.size()>0){
							for(int z=0;z<part.size();z++){
								SpDetailDTO dto= part.get(z);
								GaBetPart  betPa = dto.getGaBetPart();
								BigDecimal perWincash = null;
								
								perWincash = new BigDecimal(betPa.getBuyNum()).divide(new BigDecimal(sp.getNum()), 6, BigDecimal.ROUND_HALF_EVEN).multiply(winCashDe);//每一个人分得金额
								betPa.setWinCash(perWincash);
								paList.add(betPa);

								Integer uid=betPa.getUserId();
								//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
								if(!StringUtil.chkListIntContains(userIds, uid)){
									userIds.add(uid);
								}
								if(!StringUtil.chkListIntContains(puserIds, uid)){
									puserIds.add(uid);
								}
								
								if(userWinMap.get(uid)!=null){
									userWinMap.put(uid, userWinMap.get(uid).add(perWincash));
								}else{
									userWinMap.put(uid,perWincash);
								}
								
								//用户的交易明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单:").append(sp.getOrderNum()).append(";系统结算，彩派  ")
							    .append(perWincash).append("元");						
								this.updateOpenData(betPa,remark.toString(),sp.getSessionNo());
							}
						}
						//如果是代购且追号且中奖了且中奖就停止追号
						if(Constants.PROCUREMENT_SERVICE.equals(sp.getBetType())&&Constants.ADD_NO.equals(sp.getIsAddNo())
								&&Constants.WIN_STOP.equals(sp.getIsWinStop())){
							List<Object> para2 = new ArrayList<Object>();
							StringBuffer hqls2 = new StringBuffer();
							hqls2.append(" and ho.sessionId>? ");
							para2.add(sp.getSessionId());
							hqls2.append(" and ho.batchNum=? ");
							para2.add(sp.getBatchNum());
							hqls2.append(" and ho.winResult=? ");
							para2.add(Constants.INIT);
							hqls2.append(" and ho.betFlag=? ");
							para2.add(Constants.PUB_STATUS_OPEN);
							hqls2.append(" and ho.gameType =? ");
							para2.add(Constants.GAME_TYPE_GF_DCB);
							hqls2.append(" order by ho.sessionId asc");
							List<SpDetailDTO>  stopList= gaService.findGaBetSponsorAndUserList(hqls2.toString(),para2);//发起购买表，查询需要修改订单状态并退款的订单。
				    		if(stopList!= null && stopList.size()>0){
				    			type="1";
				    			for(SpDetailDTO dto:stopList){
				    				GaBetSponsor betSp = dto.getGaBetSponsor();
				    				betSp.setWinResult(Constants.INVALID_REFUND);//无效退款
				    			    betSp.setBetFlag(Constants.PUB_STATUS_CLOSE); //无效
				    			    spList.add(betSp);
			    				
				    				BigDecimal refund = betSp.getMoney().setScale(2, BigDecimal.ROUND_DOWN);//退款金额
				    				
				    				if(!StringUtil.chkListIntContains(userIds, betSp.getUserId())){
										userIds.add(betSp.getUserId());
									}

									StringBuilder remark = new StringBuilder();
									remark.append("追号已中奖，订单").append(betSp.getOrderNum()).append("取消,退款金额:")
									    .append(refund.toString()).append("元");
									
									this.updateOpenDataRefund(betSp,remark.toString());
				    			}
		    		       }
				        }
				    }
				}		
			}

			if(ParamUtils.chkString(type)){
				userService.updateUserMoney(userIds,type);
			}else{
				userService.updateUserMoney(userIds);
			}
			userService.updateUserPoints(puserIds);
			if(userWinMap.size()>0){
				updateWinCount(userWinMap);
			}
			gfDcbDAO.updateObjectList(spList, null);
			gfDcbDAO.updateObjectList(spDeList, null);
			gfDcbDAO.updateObjectList(paList, null);
			
			dcbsession.setOpenResult(result);
			dcbsession.setOpenStatus(Constants.OPEN_STATUS_OPENED);
			dcbsession.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
			gfDcbDAO.saveObject(dcbsession, null);

			return "";
		}catch(Exception e){
			return dcbsession.getSessionNo();
		}
	}
	
	public  void updateWinCount(Map<Integer,BigDecimal> moneyMap){
		List<GaWinCount> gaWinCoList = new ArrayList<GaWinCount>();
		for(Integer key:moneyMap.keySet()){
			StringBuffer hql = new StringBuffer();
			List<Object> parame = new ArrayList<Object>();
			hql.append(" and ho.userId = ? ");
			parame.add(key);
			hql.append(" and ho.gameType = ? ");
			parame.add(Constants.GAME_TYPE_GF_DCB);
			List<GaWinCount> gaWinCountList = gaService.findGaWinCountList(hql.toString(),parame);
			GaWinCount gaWinCount = null;
			if(gaWinCountList.size()== 0){
				gaWinCount = new GaWinCount();
				gaWinCount.setUserId(key);
				gaWinCount.setGameType(Constants.GAME_TYPE_GF_DCB);
				gaWinCount.setTotalMoney(moneyMap.get(key));
				gaWinCoList.add(gaWinCount);
			}else{
				gaWinCount = gaWinCountList.get(0);
				BigDecimal totalMoeny=ProductUtil.checkBigDecimal(gaWinCount.getTotalMoney());//用户余额
				gaWinCount.setTotalMoney(totalMoeny.add(moneyMap.get(key)));
				gaWinCoList.add(gaWinCount);
			}	
		}
		gfDcbDAO.updateObjectList(gaWinCoList, null);
	}

    /**
     * 计算合买。
     */
	public void updateCountJointBet(String sessionNo){
		GfDcbGaSession se =null;
		if(sessionNo == null){
			se=this.getCurrentSession();
		}else{
			se = this.getPreviousSessionBySessionNo(sessionNo);
		}

		if(se != null){
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<Integer> userIds = new ArrayList<Integer>();
			
//			List<GaBetDetail> deList = new ArrayList<GaBetDetail>();
//			List<GaBetSponsorDetail> spDeList = new ArrayList<GaBetSponsorDetail>();
//			List<User> uList = new ArrayList<User>();
//			List<UserTradeDetail> trList = new ArrayList<UserTradeDetail>();
			
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ho.sessionId=? ");
			para.add(se.getSessionId());
			hqls.append(" and ho.restNum > ? ");
			para.add(0);//查询投注进度不满100%的
			hqls.append(" and ho.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and ho.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_DCB);
			hqls.append(" and ho.betType= ? ");
			para.add(Constants.SPONSOR);

			List<SpDetailDTO> list = gaService.findGaBetSponsorAndUserList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单

			if(list!=null && list.size()>=0){
				for(SpDetailDTO dto:list){
					GaBetSponsor sponsor =  dto.getGaBetSponsor();
					String isGuarantee = sponsor.getIsGuarantee();//是否保底
					if(Constants.GUARANTEE.equals(isGuarantee)){ //如果保底
//						User user= dto.getUser();
						int guaranteedNum = sponsor.getGuaranteedNum(); //保底份数
						int restNum = sponsor.getRestNum(); //剩余份数
						if(guaranteedNum >= restNum){//保底份数 >=剩余份数 ,投注有效，把保底拿去投注
							GaBetPart betPart = new GaBetPart();
							BigDecimal guaBetMoney = sponsor.getPreMoney().multiply(new BigDecimal(restNum));//实际使用保底投注的金额

							betPart.setUserId(sponsor.getUserId());
							betPart.setBuyNum(restNum);
							betPart.setBuyTime(DateTimeUtil.getNowSQLDate());;
							betPart.setBetMoney(guaBetMoney);
							betPart.setJointId(sponsor.getJointId());
							betPart.setWinCash(new BigDecimal(0));
//							betPart.setWinPoint(new BigDecimal(0));
							betPart.setBehavior(Constants.PARTICIPATOR);
							paList.add(betPart);

							if(guaranteedNum > restNum){//需要退钱
								BigDecimal reMoney = new BigDecimal(guaranteedNum-restNum).multiply(sponsor.getPreMoney());//需要退钱数量

								if(!StringUtil.chkListIntContains(userIds, sponsor.getUserId())){
									userIds.add(sponsor.getUserId());
								}
								
								// 2.保存明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单").append(sponsor.getOrderNum()).append(",退回保底款项分数:")
								    .append(guaranteedNum-restNum).append(";金额:").append(reMoney).append("元");
								this.updateUserBaodiBack(sponsor,reMoney,remark.toString());
							}
							if("1".equals(sponsor.getIsPrivacy())){
								sponsor.setIsPrivacy("3");
							}

							sponsor.setSchedule(new BigDecimal(1));
							sponsor.setRestNum(0);
							sponsor.setBetFlag(Constants.PUB_STATUS_OPEN);
							sponsor.setWinResult(Constants.INIT);
							spList.add(sponsor);
							
						}else{ //投注不满 100% 投注无效，退款
							sponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);	
							sponsor.setWinResult(Constants.INVALID_REFUND);//投注无效，退款
							spList.add(sponsor);

							BigDecimal guMoney = sponsor.getGuaranteedMoney();
							
							if(!StringUtil.chkListIntContains(userIds, sponsor.getUserId())){
								userIds.add(sponsor.getUserId());
							}
							
							// 2.保存明细
							StringBuilder remark = new StringBuilder();
							remark.append("订单").append(sponsor.getOrderNum()).append(",未满员退回保底款项; 金额:")
							    .append(guMoney).append("元");
							this.updateUserBetBack(sponsor,remark.toString());

							List<SpDetailDTO>  rlList = gaService.findGaBetPartListByJointId(sponsor.getJointId());
							if(rlList !=null&& rlList.size()>=0){
								for(SpDetailDTO paDto:rlList){
									GaBetPart betPart = paDto.getGaBetPart();
									BigDecimal betMoney = betPart.getBetMoney();
									
									if(!StringUtil.chkListIntContains(userIds, betPart.getUserId())){
										userIds.add(betPart.getUserId());
									}
									
									// 2.保存明细
									StringBuilder remark2 = new StringBuilder();
									remark2.append("订单").append(sponsor.getOrderNum()).append(",未满员退回投注款;金额:")
									    .append(betMoney).append("元");
									this.updateUserBetBack(betPart,betMoney,remark.toString());
								}
							}
						}
					}else{ //不保底
						sponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);	
						sponsor.setWinResult(Constants.INVALID_REFUND);//投注无效，退款
						spList.add(sponsor);
						
						List<SpDetailDTO>  rlList = gaService.findGaBetPartListByJointId(sponsor.getJointId());
						if(rlList !=null&& rlList.size()>=0){
							for(SpDetailDTO paDto:rlList){
								GaBetPart betPart = paDto.getGaBetPart();
								BigDecimal betMoney = betPart.getBetMoney();
								
								if(!StringUtil.chkListIntContains(userIds, betPart.getUserId())){
									userIds.add(betPart.getUserId());
								}
								
								// 2.保存明细
								StringBuilder remark = new StringBuilder();
								remark.append("订单").append(sponsor.getOrderNum()).append(",未满员退回投注款;金额:")
								    .append(betMoney).append("元");
								this.updateUserBetBack(betPart, betMoney, remark.toString());
							}
						}
					}
				}
			}
			
			userService.updateUserMoneyAndBetMoney(userIds);
			gfDcbDAO.updateObjectList(spList, null);
			gfDcbDAO.updateObjectList(paList, null);
		}
	}
	
	/**
	 * 保存用户余额
	 * @param userId
	 * @param string
	 */
	private void updateOpenData(GaBetPart part,String remark,String sessionNo) {
		User user = (User)gfDcbDAO.getObject(User.class, part.getUserId());
		userService.saveTradeDetail(user,part.getUserId(), Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_PRIZE, part.getWinCash(), part.getRid(), 
				Constants.GAME_TYPE_GF_DCB,remark,
				sessionNo,user.getUserType(),user.getLoginName()
				);
	}
	
	/**
	 * 保存用户余额
	 * @param userId
	 * @param string
	 */
	private void updateOpenDataRefund(GaBetSponsor betSp,String remark) {
		userService.saveTradeDetail(null,betSp.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAWBACK, betSp.getMoney(), betSp.getJointId(), Constants.GAME_TYPE_GF_AHK3,remark);
	}
	
			
	public GfDcbGaSession getCurrentSession() {
		return gfDcbDAO.getCurrentSession();
	}
	
	public GfDcbGaSession getPreviousSessionBySessionNo(String sessionNo) {
		return gfDcbDAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	private String setPlayName(String playType) {
		Map<String, String> map = CacheUtil.getPlayName();		
		return map.get(playType);
	}
	public List<GfDcbGaTrend> findTrendList() {
		return gfDcbDAO.findGaTrendList();
	}
	
	@Override
	public PaginationSupport findGfDcbGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return gfDcbDAO.findGfDcbGaSessionList(hql,para,pageNum,pageSize);
	}
		 
	 public void updateFetchAndOpenTrendResult(){
		    log.info("----- ------ssq ----");
			String lastSessionNo=(Integer.parseInt(gfDcbDAO.getCurrentSession().getSessionNo())-1)+"";
			GfDcbGaSession session =gfDcbDAO.getPreviousSessionBySessionNo(lastSessionNo);

			if(session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
				List<GfDcbGaTrend> list=gfDcbDAO.findGaTrendList();
				List<GfDcbGaTrend> savelist=new ArrayList<GfDcbGaTrend>();
				Map<String,Boolean> map=transHotResult(session.getOpenResult());;
				for(int i=0;i<list.size();i++){
					GfDcbGaTrend trend=list.get(i);
					if("0".equals(trend.getType())){//红球
						log.info("----- red----"+i + "---"+map.get("红球"+trend.getTrendTitle()));
						if(map.get("红球"+trend.getTrendTitle()) != null && map.get("红球"+trend.getTrendTitle()) == true){
							trend.setTrendCount(0);
						}else{
							trend.setTrendCount(trend.getTrendCount() + 1);
						}
					}else if("1".equals(trend.getType())){//蓝球
						if(map.get("蓝球"+trend.getTrendTitle()) != null && map.get("蓝球"+trend.getTrendTitle()) == true){
							trend.setTrendCount(0);
						}else{
							trend.setTrendCount(trend.getTrendCount() + 1);
						}
						log.info("----- bule----"+i + "---"+map.get("蓝球"+trend.getTrendTitle()));
					}
					savelist.add(trend);
				}
				gfDcbDAO.updateObjectList(savelist, null);
				lastSessionNo=null;
				session=null;
			}
		}
	 
		public boolean saveOpenResult(GfDcbGaSession session,String openResult){
			boolean flag=false;
			gfDcbDAO.updateObject(session, null);
			flag=true;
			return flag;
		}
		
//		public boolean saveAndOpenResult(GfDcbGaSession session,String openResult){
//
//			boolean flag=false;
//			session.setOpenResult(openResult); //这里是用户手动输入的值。没有经过计算。
//			boolean flag1 = updateDcbSessionOpenResultMethod(session, session.getOpenResult());
//			if(flag1){      
//				session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
//				session.setOpenStatus(DcbConstants.DCB_OPEN_STATUS_OPENED);
//				gfDcbDAO.updateObject(session, null);
//				log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
//					flag=true;
//			}else{
//				log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
//			}
//			return flag;
//		}
		@Override
		public PaginationSupport findGfDcbGaBetList(String hql,
				List<Object> para, int pageNum, int pageSize) {
			return gfDcbDAO.findGfDcbGaBetList(hql, para, pageNum, pageSize);
		}

		@Override
		public PaginationSupport findGaBetDetail(String hql, List<Object> para,
				int pageNum, int pageSize) {
			return gfDcbDAO.findGaBetDetail(hql, para, pageNum, pageSize);
		}
		@Override
		public List<GfDcbDTO> findGaBetDetailById(String hql,
				List<Object> para) {
			return gfDcbDAO.findGaBetDetailById(hql, para);
		}
		@Override
		public boolean modifyDate(GfDcbGaSession session,
				String endTime) {
			String sessinNo = session.getSessionNo();
			int nextSessionNo = Integer.parseInt(sessinNo) + 1;
			Date firthDay = DateTimeUtil.getDateAfter(DateTimeUtil.parse(endTime), 2,"yyyy-MM-dd HH:mm:ss");
			Date lastDay = null;
			try {
				lastDay = new SimpleDateFormat("yyyy-MM-dd").parse(DateTimeUtil.getRightYear() + "-12-31");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int days = DateTimeUtil.daysBetween(firthDay, lastDay);
			for(int i=0; i<days; i++){
				
				Date dateAfter = DateTimeUtil.getDateAfter(firthDay, i, "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();  
				 c.setTime(dateAfter);  
				 int dayForWeek = 0;  
				 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
				  dayForWeek = 7;  
				 }else{  
				  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
				 }				 

				 if(dayForWeek == 2 || dayForWeek == 4 || dayForWeek == 6){
						int sessionNo = nextSessionNo;
						nextSessionNo = nextSessionNo + 1;
						int beforDays = i-2;
						if(dayForWeek == 2){
							beforDays = i-3;
						}
						Date dateAfter2 = DateTimeUtil.getDateAfter(firthDay, beforDays, "yyyy-MM-dd HH:mm:ss");   
						String startTime2;
						if(sessionNo == (Integer.parseInt(sessinNo) + 1)){
							startTime2 = endTime; //开盘时间
						}else{
							startTime2 = DateTimeUtil.DateToString(dateAfter2) + " 22:30:00"; //开盘时间
						}
						String endTime2 = DateTimeUtil.DateToString(dateAfter) + " 21:30:00"; // 封盘时间

						GfDcbGaSession GfDcbGaSession = gfDcbDAO.getPreviousSessionBySessionNo(sessionNo+"");
						GfDcbGaSession.setStartTime(DateTimeUtil.parse(startTime2));
						GfDcbGaSession.setEndTime(DateTimeUtil.parse(endTime2));
						gfDcbDAO.updateObject(GfDcbGaSession, null);
				 }
			}
			return true;
		}

		public List<GfDcbGaTrend> findDcbTrendList() {
			return gfDcbDAO.findGaTrendList();
		}

		public User saveSponsorBet(GfDcbGaSession se,int multiple,User user, List<String> list,
				 int num, int buyNum,String isGuaranteed,int guNum,int betNum,BigDecimal betMoney) {
			List<Object> saveList = new ArrayList<Object>();
			String batchNum =""; //批次号，是否属于同一追号
			batchNum = System.currentTimeMillis() +DcbUtil.getTwo();
			String orderNum = "";
			BigDecimal money = new BigDecimal(betNum *2*multiple); //方案总金额
			BigDecimal preMoney = money.divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);//每份金额
			BigDecimal guaranteedMoney = new BigDecimal(guNum).multiply(preMoney);//保底金额
			
			GaBetSponsor sp = new GaBetSponsor();
			sp.setUserId(user.getUserId());
//			sp.setOrderNum(orderNum);
			sp.setSessionId(se.getSessionId());
			sp.setBetTime(DateTimeUtil.getCurrentDate());
			sp.setMoney(money);
			sp.setSchedule(new BigDecimal(buyNum).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN));
			sp.setNum(num);
			sp.setBetNum(betNum);
			sp.setRestNum(num-buyNum);
			sp.setPreMoney(preMoney);
			sp.setWinCash(new BigDecimal(0));
			if(num-buyNum ==0){
				sp.setWinResult(Constants.INIT); //未开奖
			}else{
				sp.setWinResult(Constants.UNFINISHED); //未完成
			}
			sp.setIsGuarantee(isGuaranteed);//是否保底
			if(Constants.GUARANTEE.equals(isGuaranteed)){//如果保底
				sp.setGuaranteedMoney(guaranteedMoney);
				sp.setGuaranteedNum(guNum);
			}

			sp.setBetFlag(Constants.PUB_STATUS_OPEN); //有效
			sp.setGameType(Constants.GAME_TYPE_GF_DCB);
			sp.setGameName(DcbConstants.GAME_NAME);
			sp.setSessionNo(se.getSessionNo());
			sp.setMultiple(multiple);
			sp.setBetType(Constants.SPONSOR);//合买
//			sp.setIsAddNo(DcbConstants.NOT_ADD_NO);
//			sp.setIsWinStop(DcbConstants.WIN_NO_STOP);
			sp.setBatchNum(batchNum);
			sp = (GaBetSponsor) gfDcbDAO.saveObjectDB(sp);
			orderNum = se.getSessionNo() + "SSQ" +sp.getJointId();
			sp.setOrderNum(orderNum);
			gfDcbDAO.saveObject(sp);
			
			GaBetPart pa =new GaBetPart();
			BigDecimal lotteryMoney = money.multiply(new BigDecimal(buyNum)).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);

			pa.setJointId(sp.getJointId());
			pa.setBetMoney(lotteryMoney);
			pa.setBuyNum(buyNum);
			pa.setUserId(user.getUserId());
			pa.setBuyTime(DateTimeUtil.getCurrentDate());
			pa.setWinCash(new BigDecimal(0));
//			pa.setWinPoint(new BigDecimal(0));
			pa.setBehavior(Constants.ORIGINATE);
			gfDcbDAO.saveObject(pa);
			
			for(String op : list){
				GaBetSponsorDetail de = new GaBetSponsorDetail();
				de.setJointId(sp.getJointId());
				de.setOptionTitle(op);
				de.setOrderNum(orderNum);
//				de.setPlayType(playType);
				de.setWinResult(Constants.INIT);
				saveList.add(de);
			}
						
			// 2.保存明细   订单：593894，购买彩票 扣款 2 元  方案：17088SSQ00318480 保底扣款 1 元

//			BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
//			BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
//			user.setDayBetMoney(dayBetMoney.add(lotteryMoney));
//			user.setAggregateBetMoney(aggregateBetMoney.add(lotteryMoney));

			if(Constants.GUARANTEE.equals(isGuaranteed)){//保底
			    //保底的钱
				BigDecimal guaranteeMoney = sp.getGuaranteedMoney();
				StringBuilder remark = new StringBuilder();
				remark.append("订单:").append(sp.getOrderNum()).append(" 保底预扣款 ")
				    .append(guaranteeMoney).append("元");
				user=userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_FROZEN, guaranteeMoney, sp.getJointId(), Constants.GAME_TYPE_GF_DCB, remark.toString());
			}
			StringBuilder remark = new StringBuilder();
			remark.append("订单:").append(sp.getOrderNum()).append(",购买彩票 扣款 ")
			    .append(lotteryMoney).append("元");
			user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, lotteryMoney, pa.getRid(), Constants.GAME_TYPE_GF_DCB, remark.toString());

			gfDcbDAO.updateObjectList(saveList, null);
			return user;
		}
		
		@Override
		public User saveProcurementServiceBet(User user,
				ArrayList<String> list, Map<String, Integer> seMap,
				String isAddNo, String isWinStop,int betNum) {
			List<Object> saveList = new ArrayList<Object>();
			String batchNum =""; //批次号，是否属于同一追号
			batchNum = System.currentTimeMillis() +DcbUtil.getTwo();
			boolean isFirst=true;//是否是追号第一期
			
			for (String key : seMap.keySet()) {
				String sessionNo = key; //期号
				int multiple = seMap.get(key); //倍数
				GfDcbGaSession se = this.getPreviousSessionBySessionNo(sessionNo);//获取期号信息
				String orderNum = "";
				BigDecimal money = new BigDecimal(betNum*1*multiple);//投注金额
				
				GaBetSponsor sp = new GaBetSponsor();
				sp.setUserId(user.getUserId());
				sp.setSessionId(se.getSessionId());
				sp.setBetTime(DateTimeUtil.getCurrentDate());
				sp.setMoney(money);
				sp.setSchedule(new BigDecimal(1));
				sp.setNum(1);
				sp.setBetNum(betNum);
				sp.setRestNum(0);
//				sp.setGuaranteedNum(new BigDecimal(0)); //保底份额
				sp.setWinResult(Constants.INIT); //未开奖
//				sp.setIsGuarantee(DcbConstants.NOT_GUARANTEE);//不保底
				sp.setBetFlag(Constants.PUB_STATUS_OPEN); //有效
				sp.setGameType(Constants.GAME_TYPE_GF_DCB);
				sp.setGameName(DcbConstants.GAME_NAME);
				sp.setSessionNo(sessionNo);
				sp.setMultiple(multiple);
				sp.setBetType(Constants.PROCUREMENT_SERVICE);
				sp.setIsAddNo(isAddNo);
				sp.setIsWinStop(isWinStop);
				sp.setBatchNum(batchNum);
				sp.setWinCash(new BigDecimal(0));
				sp.setPreMoney(money);
				sp = (GaBetSponsor) gfDcbDAO.saveObjectDB(sp);
				orderNum = sessionNo + "SSQ" +sp.getJointId();
				sp.setOrderNum(orderNum);
			    saveList.add(sp);
				
				GaBetPart pa =new GaBetPart();
				pa.setJointId(sp.getJointId());
				pa.setBetMoney(money);
				pa.setBuyNum(1);
				pa.setUserId(user.getUserId());
				pa.setBuyTime(DateTimeUtil.getCurrentDate());
				pa.setWinCash(new BigDecimal(0));
//				pa.setWinPoint(new BigDecimal(0));
				pa.setBehavior(Constants.ORIGINATE);
				saveList.add(pa);
				
				for(String op : list){
					GaBetSponsorDetail de = new GaBetSponsorDetail();
//					de.setBetRate(betRate);
					de.setJointId(sp.getJointId());
					de.setOptionTitle(op);
					de.setOrderNum(orderNum);
//					de.setPlayType(playType);
					de.setWinResult(Constants.INIT);
					saveList.add(de);
				}
				
//				BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
//				BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
//				user.setDayBetMoney(dayBetMoney.add(money));
//				user.setAggregateBetMoney(aggregateBetMoney.add(money));

				// 2.保存明细   订单：594693，彩票预购 扣款 2 元  订单：594688，购买彩票 扣款 2 元

				StringBuilder remark = new StringBuilder();

				if(isFirst){
					remark.append("订单:").append(sp.getOrderNum()).append(",购买彩票 扣款 ")
				    .append(money).append("元");
					isFirst = false;
					user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, money, pa.getRid(), Constants.GAME_TYPE_GF_DCB, remark.toString());

				}else{
					remark.append("订单:").append(sp.getOrderNum()).append(",购买预扣 扣款 ")
				    .append(money).append("元");
					user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, money, pa.getRid(), Constants.GAME_TYPE_GF_DCB, remark.toString());
				}
			}
			gfDcbDAO.updateObjectList(saveList, null);
			
			return user;
		}
//		@Override
//		public User saveUserBetInfo(String room, Map<Integer, Integer> betMap,
//				List<GaBetOption> list, GfDcbGaSession session, User user,
//				BigDecimal betAll) {
//			// TODO Auto-generated method stub
//			return null;
//		}
		
	
//		/**
//		 * 开奖前更新合买记录表，清算用户保底钱数，结算最终投注的钱数
//		 */
//		public void  updateGaBetSponsor(){
//			List<User> userList=new ArrayList<User>();
//			List<UserTradeDetail> tradeList=new ArrayList<UserTradeDetail>();
//			List<GaBetSponsor> gaBetSponsorList=new ArrayList<GaBetSponsor>();
//			List<GaBetPart> partlist=new ArrayList<GaBetPart>();
//			List<SpDetailDTO>  gbslist=gaService.findGaBetSponsorListByGameTypeAndBetType(Constants.GAME_TYPE_GF_DCB,Constants.PUB_STATUS_OPEN);//合买
//			if(gbslist!=null&&gbslist.size()>0){
//				for(SpDetailDTO dto:gbslist){
//					GaBetSponsor gaBetSponsor=dto.getGaBetSponsor();
//					User user=dto.getUser();
//					if(gaBetSponsor.getRestNum()==0||gaBetSponsor.getRestNum()>gaBetSponsor.getGuaranteedNum()){//剩余份数等于0或者保底份额小于剩余份额，全部退还用户保底份额钱
//						BigDecimal baodi=gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getGuaranteedNum())).setScale(2, BigDecimal.ROUND_UP);
//						user.setMoney(user.getMoney().add(baodi));
//						userList.add(user);
//						UserTradeDetail udt=new UserTradeDetail();
//						udt.setCashMoney(baodi);
//						udt.setUserMoney(user.getMoney());
//						udt.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回保底款项份数："+gaBetSponsor.getGuaranteedNum()+"，金额："+baodi.toString()+"元");
//						udt.setCashType(Constants.CASH_TYPE_CASH_GUA_BACK);
//						udt.setTradeType(Constants.TRADE_TYPE_INCOME);
//						udt.setUserId(user.getUserId());
//						udt.setCreateTime(DateTimeUtil.getNowSQLDate());
//						udt.setModelType(Constants.GAME_TYPE_GF_DCB);
//						udt.setRefId(gaBetSponsor.getJointId());
//						tradeList.add(udt);
//						if(gaBetSponsor.getRestNum()>gaBetSponsor.getGuaranteedNum()){
//							gaBetSponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);//无效
//							gaBetSponsorList.add(gaBetSponsor);
//							List<SpDetailDTO> list=gaService.findGaBetPartListByJointId(gaBetSponsor.getJointId());
//							if(list!=null&&list.size()>0){
//								for(SpDetailDTO dto1:list){
//									GaBetPart  part=dto1.getGaBetPart();
//									User user1=dto1.getUser();
//									user1.setMoney(user1.getMoney().add(part.getBetMoney()));
//									userList.add(user1);
//									UserTradeDetail udt1=new UserTradeDetail();
//									udt1.setCashMoney(part.getBetMoney());
//									udt1.setUserMoney(user1.getMoney());
//									udt1.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回参与份数："+part.getBuyNum()+"，金额："+part.getBetMoney().setScale(2, BigDecimal.ROUND_UP).toString()+"元");
//									udt1.setCashType(Constants.CASH_TYPE_CASH_DRAW);
//									udt1.setTradeType(Constants.TRADE_TYPE_INCOME);
//									udt1.setUserId(user1.getUserId());
//									udt1.setCreateTime(DateTimeUtil.getNowSQLDate());
//									udt1.setModelType(Constants.GAME_TYPE_GF_DCB);
//									udt1.setRefId(gaBetSponsor.getJointId());
//									tradeList.add(udt1);
//								}
//							}
//						}
//					}else{//保底份额超出剩余份额   退回发起者一部分钱，等于则不退回
//						if(gaBetSponsor.getRestNum()<gaBetSponsor.getGuaranteedNum()){
//							BigDecimal tuihuan=gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getGuaranteedNum()-gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP);
//							user.setMoney(user.getMoney().add(tuihuan));
//							userList.add(user);
//							UserTradeDetail udt=new UserTradeDetail();
//							udt.setCashMoney(tuihuan);
//							udt.setUserMoney(user.getMoney());
//							udt.setRemark("订单："+gaBetSponsor.getOrderNum()+"，退回保底款项份数："+gaBetSponsor.getGuaranteedNum()+"，金额："+tuihuan.toString()+"元");
//							udt.setCashType(Constants.CASH_TYPE_CASH_GUA_BACK);
//							udt.setTradeType(Constants.TRADE_TYPE_INCOME);
//							udt.setUserId(user.getUserId());
//							udt.setCreateTime(DateTimeUtil.getNowSQLDate());
//							udt.setModelType(Constants.GAME_TYPE_GF_DCB);
//							udt.setRefId(gaBetSponsor.getJointId());
//							tradeList.add(udt);
//							
//							GaBetPart  temp=new GaBetPart();
//							temp.setBuyNum(gaBetSponsor.getRestNum());
//							temp.setBetMoney(gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP));
//							temp.setUserId(user.getUserId());
//							temp.setBuyTime(DateTimeUtil.getNowSQLDate());
//							temp.setJointId(gaBetSponsor.getJointId());
//							partlist.add(temp);						
//						}else if(gaBetSponsor.getRestNum()==gaBetSponsor.getGuaranteedNum()){
//							GaBetPart  temp=new GaBetPart();
//							temp.setBuyNum(gaBetSponsor.getRestNum());
//							temp.setBetMoney(gaBetSponsor.getMoney().divide(new BigDecimal(gaBetSponsor.getNum()),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(gaBetSponsor.getRestNum())).setScale(2, BigDecimal.ROUND_UP));
//							temp.setUserId(user.getUserId());
//							temp.setBuyTime(DateTimeUtil.getNowSQLDate());
//							temp.setJointId(gaBetSponsor.getJointId());
//							partlist.add(temp);							
//						}
//					}
//				}
//			}
//			gaService.updateObjectList(userList, null);
//			gaService.updateObjectList(tradeList, null);
//			gaService.updateObjectList(gaBetSponsorList, null);
//			gaService.updateObjectList(partlist, null);
//		}
		
		/**
		 * 更新每期遗漏数据
		 */
		public void updateFetchAndOpenOmit(GfDcbGaSession tempsession){
//			GfDcbGaSession session=gfDcbDAO.getCurrentSession();
//			if(session!=null){
//				GfDcbGaSession tempsession =gfDcbDAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");
				if(tempsession!=null){
					if(ParamUtils.chkString(tempsession.getOpenResult())){
						String openResult=tempsession.getOpenResult();
						String array[]=openResult.split("\\+")[0].split(",");
						String blueBall=openResult.split("\\+")[1];
						GfDcbGaOmit omit=gfDcbDAO.getGfDcbGaOmitBySessionNo(tempsession.getSessionNo());
						if(omit==null){
							GfDcbGaOmit preomit=gfDcbDAO.getGfDcbGaOmitBySessionNo((Integer.parseInt(tempsession.getSessionNo())-1)+"");
							if(preomit == null){
								GfDcbGaSession se = (GfDcbGaSession) gfDcbDAO.getObject(GfDcbGaSession.class, tempsession.getSessionId()-1);
								preomit=gfDcbDAO.getGfDcbGaOmitBySessionNo(se.getSessionNo());
							}

							if(preomit!=null){
								omit=new GfDcbGaOmit();
								Map<String,String> map=new HashMap<String,String>();
								Map<String,String> bluemap=new HashMap<String,String>();
								for(int i=1;i<=33;i++){
									if(i<17){
										bluemap.put(""+i, i+"");
									}
									map.put(""+i, i+"");
								}
								Field f =null;
								for(int i=0;i<array.length;i++){
										try {
											f= GfDcbGaOmit.class.getDeclaredField("red"+Integer.parseInt(array[i]));
											f.setAccessible(true);  
											f.set(omit, 0);
											map.put(Integer.parseInt(array[i])+"", "");
										} catch (Exception e) {
											return;
										}
								}
								for(String key:map.keySet()){
									if(ParamUtils.chkString(map.get(key))){
										try {
											f= GfDcbGaOmit.class.getDeclaredField("red"+key);
											f.setAccessible(true);
//											if(f.getType().getName().equalsIgnoreCase("Integer")){
												f.set(omit, Integer.parseInt(f.get(preomit).toString())+1);
//											}
											
										} catch (Exception e) {
											e.printStackTrace();
											return;
										} 
									}
								}
								try {
									f= GfDcbGaOmit.class.getDeclaredField("blue"+Integer.parseInt(blueBall));
									f.setAccessible(true);  
									f.set(omit, 0);
									bluemap.put(Integer.parseInt(blueBall)+"", "");
									for(String key:bluemap.keySet()){
										if(ParamUtils.chkString(bluemap.get(key))){
												f= GfDcbGaOmit.class.getDeclaredField("blue"+key);
												f.setAccessible(true);							
												f.set(omit, Integer.parseInt(f.get(preomit).toString())+1);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									return;
								}	
								omit.setOpenResult(tempsession.getOpenResult());
								omit.setSessionNo(tempsession.getSessionNo());
								gfDcbDAO.saveObjectDB(omit);
							}else{
								Map<String,String> map=new HashMap<String,String>();
								Map<String,String> bluemap=new HashMap<String,String>();
								for(int i=1;i<=33;i++){
									if(i<17){
										bluemap.put(""+i, i+"");
									}
									map.put(""+i, i+"");
								}
								Field f =null;
								omit=new GfDcbGaOmit();
								try {
									for(String key:map.keySet()){
										if(ParamUtils.chkString(map.get(key))){			
												f= GfDcbGaOmit.class.getDeclaredField("red"+key);
												f.setAccessible(true);							
												f.set(omit, 0);
										}
									}
									for(String key:bluemap.keySet()){
										if(ParamUtils.chkString(bluemap.get(key))){
												f= GfDcbGaOmit.class.getDeclaredField("blue"+key);
												f.setAccessible(true);							
												f.set(omit, 0);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									return;
								}
								omit.setOpenResult(tempsession.getOpenResult());
								omit.setSessionNo(tempsession.getSessionNo());
								gfDcbDAO.saveObjectDB(omit);
							}
						}
					}		
				}
//			}
		}

		public List<GfDcbGaOmit> findGfDcbGaOmitList(String hqls,
					List<Object> para, int num){
			return  gfDcbDAO.findGfDcbGaOmitList(hqls,para,num);
		}
		
		@Override
		public void updateDayBetCount(GfDcbGaSession tempsession) {
//			GfDcbGaSession session=gfDcbDAO.getCurrentSession();
//			if(session!=null){
//				GfDcbGaSession tempsession =gfDcbDAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");
				if(tempsession!=null){
					if(ParamUtils.chkString(tempsession.getOpenStatus())&&Constants.OPEN_STATUS_OPENED.equals(tempsession.getOpenStatus())){
					//如果上期已开奖
						List<Object> spPara = new ArrayList<Object>();
						StringBuffer spHqls = new StringBuffer();
						spHqls.append(" and sp.sessionId=? ");
						spPara.add(tempsession.getSessionId());
						spHqls.append(" and sp.betFlag=? " );
						spPara.add(Constants.PUB_STATUS_OPEN);// 有效
						spHqls.append(" and sp.gameType =? ");
						spPara.add(Constants.GAME_TYPE_GF_DCB);
						
						List<GaBetSponsor> list = gaService.findGaBetSponsorList(spHqls.toString(), spPara);//发起购买表，查询当前期有效的发起者发起的购买订单
						if(list!=null&&list.size()>0){
							BigDecimal totalBetMoney = new BigDecimal(0); //下注
							BigDecimal totalWinMoney = new BigDecimal(0); //中奖
							BigDecimal payOff = new BigDecimal(0); //盈亏

							for(GaBetSponsor sp: list){
								totalBetMoney = totalBetMoney.add(sp.getMoney());
								totalWinMoney = totalWinMoney.add(sp.getWinCash());
							}
							payOff = totalBetMoney.subtract(totalWinMoney);
						
							Date date=DateTimeUtil.getNowSQLDate();
							String startDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -3))+" 00:00:00";
							String endDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -3))+" 23:59:59";//DateTimeUtil.getDateTimeOfMinutes(date, -5)

							List<Object> para = new ArrayList<Object>();
							StringBuffer hqls = new StringBuffer();
							
							hqls.append(" and ho.createTime >=? ");
							para.add(startDate);
							hqls.append(" and ho.createTime <= ? ");
							para.add(endDate);

							GaDayBetCount dayBetCount = null;
							dayBetCount = gaService.findDayBetCount(hqls,para);
							if(dayBetCount == null){
								dayBetCount = new GaDayBetCount();
								dayBetCount.setCreateTime(date);
							}else{
								totalBetMoney = totalBetMoney.add(ProductUtil.checkBigDecimal(dayBetCount.getBetMoney()));
								totalWinMoney = totalWinMoney.add(ProductUtil.checkBigDecimal(dayBetCount.getWinMoney()));
								payOff = payOff.add(ProductUtil.checkBigDecimal(dayBetCount.getPayoff()));
							}
							dayBetCount.setBetMoney(totalBetMoney);
							dayBetCount.setWinMoney(totalWinMoney);
							dayBetCount.setPayoff(payOff);
							gaService.saveObjectDB(dayBetCount);					
						}
					}
				}
//			}		
		}
		
		private void updateUserBaodiBack(GaBetSponsor sponsor,BigDecimal reMoney,String remark) {//退回部分保底金额
			userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_GUA_BACK, reMoney, sponsor.getJointId(), Constants.GAME_TYPE_GF_DCB,remark);
		}
		
		
		private void updateUserBetBack(GaBetSponsor sponsor,String remark) {//方案撤单  退回全部保底
			userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, sponsor.getGuaranteedMoney(), sponsor.getJointId(), Constants.GAME_TYPE_GF_DCB,remark);
		}
		
		
		private void updateUserBetBack(GaBetPart betPart, BigDecimal betMoney,
				String remark) {//退回参与用户金额
			userService.saveTradeDetail(null,betPart.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, betMoney, betPart.getRid(), Constants.GAME_TYPE_GF_DCB,remark);
		}
}
