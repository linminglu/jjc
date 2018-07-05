package com.gf.ssc.jxssc.service.impl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.apps.model.UserTradeDetailRl;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.GameConstants;
import com.game.model.GaBetOption;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.dto.SpDetailDTO;
import com.game.model.dto.WinCoDTO;
import com.game.service.IGaService;
import com.gf.ssc.jxssc.JxSscConstants;
import com.gf.ssc.jxssc.dao.IJxSscDAO;
import com.gf.ssc.jxssc.model.GfJxSscGaOmit;
import com.gf.ssc.jxssc.model.GfJxSscGaSession;
import com.gf.ssc.jxssc.model.GfJxSscGaTrend;
import com.gf.ssc.jxssc.service.IJxSscService;
import com.gf.ssc.jxssc.util.JxSscUtil;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class JxSscServiceImpl  extends BaseService implements IJxSscService {
	private IJxSscDAO jxSscDAO;
	private IUserService userService;
	private IGaService gaService;
	
	public void setJxSscDAO(IJxSscDAO jxSscDAO) {
		this.jxSscDAO = jxSscDAO;
		super.dao = jxSscDAO;
		
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	@Override
	public String updateInitSession(int day) {
		
		String flag = "fail";//返回状态
		
		//今天日期处理 yyyy-MM-dd
		Date now = DateTimeUtil.getJavaUtilDateNow();
		String today = DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfDays(now, day));
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkNestdaySessionInit(DateTimeUtil.getDateTimeOfDays(now, day));
		if(!isTodaySessionInit){
			
			String startTimeStr = today + JxSscConstants.JX_SSC_START_TIME_STR;//开始时间串
			Date startDate = DateTimeUtil.strToDateMul(startTimeStr);//开始时间
			
			for (int i = 0; i < JxSscConstants.JX_SSC_MAX_PART; i++) {//经观察得知
				//计算出当前场次的时间
				long diffTime = startDate.getTime() + i*JxSscConstants.JX_SSC_TIME_INTERVAL_DAY * 60 * 1000;
				Date curSessionDate = new Date(diffTime);
				
				String sessionNo = this.getTodaySessionNo(curSessionDate, i+1);//期号
				GfJxSscGaSession k10Session = new GfJxSscGaSession();
				k10Session.setSessionNo(sessionNo);
				k10Session.setStartTime(curSessionDate);
				k10Session.setEndTime(DateTimeUtil.getDateTimeOfMinutes(curSessionDate,JxSscConstants.JX_SSC_TIME_INTERVAL_DAY));
				k10Session.setOpenStatus(GameConstants.OPEN_STATUS_INIT);
				jxSscDAO.saveObject(k10Session);
				
			}
			
			flag = "success";
		} else {
			flag = "inited";
		}
		return flag;
	}

	/**
	 * 获取今天的期号
	 * @param now
	 * @param index
	 * @return
	 */
	public String getTodaySessionNo(Date now,int index){
		if(index<10){
			return DateTimeUtil.DateToStringYY(now)+"00"+index;
		}else if(index<100){
			return DateTimeUtil.DateToStringYY(now)+"0"+index;		
		}else{
			return DateTimeUtil.DateToStringYY(now)+index;
		}
	}
	/**
	 * 检查今天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkNestdaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 09:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 22:00:00");
		
		HQUtils hq = new HQUtils("from GfJxSscGaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count =jxSscDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}

	/**
	 * 检查今天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkTodaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 09:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 22:00:00");
		
		HQUtils hq = new HQUtils("from GfJxSscGaSession gks where gks.startTime>? and gks.startTime<?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = jxSscDAO.countObjects(hq);
		return ParamUtils.chkInteger(count)?true:false;
	}

	/**
	 * 开奖方法
	 * @param sessionNo
	 * @param result
	 * @return
	 */
	public String updateJxSscSessionOpenResultMethod(GfJxSscGaSession fcsession,String result,String orderNum){
		try{
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<GaBetSponsorDetail> spDeList = new ArrayList<GaBetSponsorDetail>();
			Map<Integer,BigDecimal> userWinMap=new HashMap<Integer,BigDecimal>();//key:userId  value:中奖金额			
			List<Integer> puserIds = new ArrayList<Integer>();
			List<Integer> userIds = new ArrayList<Integer>();

 	    	//ManageFile.writeTextToFile("重庆时时彩开奖开始》》》》》》》》》》》》》》》》"+DateTimeUtil.DateToStringAll(new Date())+"  [sessionNO]"+fcsession.getSessionNo()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and sp.sessionId=? ");
			para.add(fcsession.getSessionId());
			hqls.append(" and sp.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and sp.winResult=? " );
			para.add(Constants.INIT);// 未开奖
			hqls.append(" and sp.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_JXSSC);
			if(orderNum != null){
				hqls.append(" and sp.orderNum =? ");
				para.add(orderNum);	
			}
			List<GaBetSponsor> list = gaService.findGaBetSponsorList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单
			String type="";
			if(list!=null&&list.size()>0){
				for(int i = 0; i < list.size(); i++){
					GaBetSponsor sp = list.get(i);
					BigDecimal winCash = new BigDecimal(0); //一个订单中奖金额。
					BigDecimal winPoint = new BigDecimal(0); //一个订单获得总积分
				    int multiple = sp.getMultiple();//倍数
				    List<GaBetSponsorDetail>  spde = gaService.findGaBetSponsorDetailListByJointId(sp.getJointId());//合买的具体投注项列表
				    if(spde !=null && spde.size() >0){
						for(int j=0;j<spde.size();j++){
							GaBetSponsorDetail det = (GaBetSponsorDetail) spde.get(j);
							String betBall = det.getOptionTitle();//具体投注内容
							String playType = det.getPlayType().toString();//玩法
							BigDecimal betRate = det.getBetRate();//
							BigDecimal point = det.getPointMultiple();//倍率
							BigDecimal winMoney = JxSscUtil.judgeWinMoney(playType,betBall,betRate,result).multiply(new BigDecimal(multiple));
							if(winMoney.compareTo(new BigDecimal(0))==1){ 
								det.setWinResult(Constants.WIN);
								winCash = winCash.add(winMoney);
								winPoint = winPoint.add(winMoney.multiply(point));
							}else{
								det.setWinResult(Constants.WIN_NO);
							}
							det.setWinMoney(winMoney);
							spDeList.add(det);
						}
				    }
				    if(winCash.compareTo(new BigDecimal(0))==1){
				    	sp.setWinResult(Constants.WIN);
				    }else{
				    	sp.setWinResult(Constants.WIN_NO);
				    }
				    sp.setWinCash(winCash);
				    sp.setWinPoint(winPoint);
				    sp.setOpenResult(result);
				    sp.setOpenTime(DateTimeUtil.getNowSQLDate());
//				    sp.setBetFlag(Constants.PUB_STATUS_CLOSE);//设为无效
				    spList.add(sp);
				    //查询参与购买用户，合买与代购同样适用
					if(winCash.compareTo(new BigDecimal(0))==1){
						List<SpDetailDTO>  part = gaService.findGaBetPartListByJointId(sp.getJointId());
						if(part != null && part.size()>0){
							for(SpDetailDTO dto:part){
								GaBetPart  betPa = dto.getGaBetPart();
								BigDecimal perWincash = null;
								BigDecimal perWinPoint = new BigDecimal(0);
								
								perWincash = new BigDecimal(betPa.getBuyNum()).divide(new BigDecimal(sp.getNum()),6,BigDecimal.ROUND_HALF_EVEN).multiply(winCash).setScale(2, BigDecimal.ROUND_HALF_EVEN);//每一个人分得金额
								if(winPoint.compareTo(new BigDecimal(0))==1){
								    perWinPoint = new BigDecimal(betPa.getBuyNum()).divide(new BigDecimal(sp.getNum()),6,BigDecimal.ROUND_HALF_EVEN).multiply(winPoint).setScale(0, BigDecimal.ROUND_HALF_EVEN);//每一个人分得积分	
								}
								betPa.setWinCash(perWincash);
								betPa.setWinPoint(perWinPoint);
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
								//用户的积分明细
								if(winPoint.compareTo(new BigDecimal(0))==1){
									StringBuilder pointRemark = new StringBuilder();
									pointRemark.append("订单").append(sp.getOrderNum()).append(",系统结算:").append(perWinPoint.toString());
									this.updateOpenDataPoints(betPa,remark.toString());
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
								para2.add(Constants.GAME_TYPE_GF_JXSSC);
								hqls2.append(" and ho.betType = ? ");
								para2.add(Constants.PROCUREMENT_SERVICE);
								hqls2.append(" order by ho.sessionId asc");
								
								List<SpDetailDTO>  stopList= gaService.findGaBetSponsorAndUserList(hqls2.toString(),para2);//发起购买表，查询需要修改订单状态并退款的订单。
					    		if(stopList!= null && stopList.size()>0){
					    			type="1";
					    			for(SpDetailDTO dto:stopList){
					    				GaBetSponsor betSp =(GaBetSponsor) dto.getGaBetSponsor();
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
			}
	    	////ManageFile.writeTextToFile("1---》"+DateTimeUtil.DateToStringAll(new Date())+"  [spList]"+spList.size()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
	    	//ManageFile.writeTextToFile("2---》"+DateTimeUtil.DateToStringAll(new Date())+"  [spDeList]"+spDeList.size()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
	    	//ManageFile.writeTextToFile("3---》"+DateTimeUtil.DateToStringAll(new Date())+"  [paList]"+paList.size()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
			
			if(ParamUtils.chkString(type)){
				userService.updateUserMoney(userIds,type);
			}else{
				userService.updateUserMoney(userIds);
			}
			userService.updateUserPoints(puserIds);
			if(userWinMap.size()>0){
				updateWinCount(userWinMap);
			}
			jxSscDAO.updateObjectList(spList, null);
			jxSscDAO.updateObjectList(spDeList, null);
			jxSscDAO.updateObjectList(paList, null);
			
	    	//ManageFile.writeTextToFile("重庆时时彩开奖结束》》》》》》》》》》》》》》》》"+DateTimeUtil.DateToStringAll(new Date())+"  [sessionNO]"+fcsession.getSessionNo()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);

			return "";
		}catch(Exception e){
			e.printStackTrace();
			//ManageFile.writeTextToFile(e+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
			return fcsession.getSessionNo();
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
			parame.add(Constants.GAME_TYPE_GF_JXSSC);
			List<GaWinCount> gaWinCountList = gaService.findGaWinCountList(hql.toString(),parame);
			GaWinCount gaWinCount = null;
			if(gaWinCountList.size()== 0){
				gaWinCount = new GaWinCount();
				gaWinCount.setUserId(key);
				gaWinCount.setGameType(Constants.GAME_TYPE_GF_JXSSC);
				gaWinCount.setTotalMoney(moneyMap.get(key));
				gaWinCoList.add(gaWinCount);
			}else{
				gaWinCount = gaWinCountList.get(0);
				BigDecimal totalMoeny=ProductUtil.checkBigDecimal(gaWinCount.getTotalMoney());//用户余额
				gaWinCount.setTotalMoney(totalMoeny.add(moneyMap.get(key)));
				gaWinCoList.add(gaWinCount);
			}	
		}
		jxSscDAO.updateObjectList(gaWinCoList, null);
	}

    /**
     * 计算合买。
     */
	public void updateCountJointBet(String sessionNo){
		GfJxSscGaSession se = null;
		if(sessionNo == null){
			se=this.getCurrentSession();
		}else{
			se = this.getPreviousSessionBySessionNo(sessionNo);
		}

	    //ManageFile.writeTextToFile("重庆时时彩计算合买》》》》》》》》》》》》》》"+DateTimeUtil.DateToStringAll(new Date())+"  [sessionNO]"+se.getSessionNo()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);

		if(se != null){
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<GaBetPart> paList = new ArrayList<GaBetPart>();
			List<Integer> userIds = new ArrayList<Integer>();
			
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ho.sessionId=? ");
			para.add(se.getSessionId());
			hqls.append(" and ho.restNum > ? ");
			para.add(0);//查询投注进度不满100%的
			hqls.append(" and ho.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and ho.gameType =? ");
			para.add(Constants.GAME_TYPE_GF_JXSSC);
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
						int totalNum = sponsor.getNum(); //总份数
						if(guaranteedNum >= restNum){//保底份数 >=剩余份数 ,投注有效，把保底拿去投注
							GaBetPart betPart = new GaBetPart();
							BigDecimal guaBetMoney = sponsor.getPreMoney().multiply(new BigDecimal(restNum));//实际使用保底投注的金额

							betPart.setUserId(sponsor.getUserId());
							betPart.setBuyNum(restNum);
							betPart.setBuyTime(DateTimeUtil.getNowSQLDate());;
							betPart.setBetMoney(guaBetMoney);
							betPart.setJointId(sponsor.getJointId());
							betPart.setWinCash(new BigDecimal(0));
							betPart.setWinPoint(new BigDecimal(0));
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
							if("1".equals(sponsor.getIsPrivacy())){
								sponsor.setIsPrivacy("3");
							}
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
 	    	//ManageFile.writeTextToFile("重庆时时spList》》》》》》》》》》》》》》》》"+DateTimeUtil.DateToStringAll(new Date())+"  [sessionNO]"+spList.size()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
 	    	//ManageFile.writeTextToFile("重庆时时彩paList》》》》》》》》》"+DateTimeUtil.DateToStringAll(new Date())+"  [sessionNO]"+paList.size()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);

			userService.updateUserMoneyAndBetMoney(userIds);
			jxSscDAO.updateObjectList(spList, null);
			jxSscDAO.updateObjectList(paList, null);
		}
	}
			
	public GfJxSscGaSession getCurrentSession() {
		return jxSscDAO.getCurrentSession();
	}
	
	public GfJxSscGaSession getPreviousSessionBySessionNo(String sessionNo) {
		return jxSscDAO.getPreviousSessionBySessionNo(sessionNo);
	}
	
	@Override
	public PaginationSupport findGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		return jxSscDAO.findGaSessionList(hql,para,pageNum,pageSize);
	}
		 
	 public void updateFetchAndOpenTrendResult(GfJxSscGaSession session){
//			String lastSessionNo=(Integer.parseInt(jxSscDAO.getCurrentSession().getSessionNo())-1)+"";
//			GfJxSscGaSession session =jxSscDAO.getPreviousSessionBySessionNo(lastSessionNo);
//			if(!DateTimeUtil.DateToString(session.getEndTime()).equals(DateTimeUtil.DateToString(DateTimeUtil.getNowSQLDate()))){
//				return ;
//			}
			if(session.getOpenStatus().equals(Constants.OPEN_STATUS_OPENED)){
				List<GfJxSscGaTrend> list=jxSscDAO.findGaTrendList();
				List<GfJxSscGaTrend> savelist=new ArrayList<GfJxSscGaTrend>();
				Map<String,Boolean> map=getTrendResult(session.getOpenResult());
				if(!map.isEmpty()){
					for(int i=0;i<list.size();i++){
						GfJxSscGaTrend trend=list.get(i);
						if(map.get(trend.getTrendTitle()) != null && map.get(trend.getTrendTitle()) == true){
							trend.setTrendCount(0);
						}else{
							trend.setTrendCount(trend.getTrendCount() + 1);
						}
						savelist.add(trend);
					}
					jxSscDAO.updateObjectList(savelist, null);
//					lastSessionNo=null;
//					session=null;
					return;
				}
			}
		}
	 
		private Map<String, Boolean> getTrendResult(String openResult) {

			Map<String,Boolean> map = new HashMap<String, Boolean>();
			String[] results = openResult.split(",|\\+|\\|");
			for(String re : results){
				map.put(re, true);
			}
		    return map;
		}
		
		@Override
		public List<GfJxSscGaTrend> findFcTrendList() {
			return jxSscDAO.findGaTrendList();
		}
		
		@Override
		public User saveSponsorBet(GfJxSscGaSession se,int multiple,User user, List<String> list,
				int num, int buyNum,String isGuaranteed,int guNum,int betNum,BigDecimal betMoney) {
			List<Object> saveList = new ArrayList<Object>();
			String batchNum =""; //批次号，是否属于同一追号
			batchNum = System.currentTimeMillis() +JxSscUtil.getTwo();
			String orderNum = "";

			//方案总金额
			BigDecimal money = new BigDecimal(betNum *2*multiple);
			List<GaBetOption>  opList= gaService.findGaBetOptionByGameType(Constants.GAME_TYPE_GF_JXSSC);
			BigDecimal preMoney = money.divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);//每份金额
			BigDecimal guaranteedMoney = new BigDecimal(guNum).multiply(preMoney);//保底金额

			GaBetSponsor sp = new GaBetSponsor();
			sp.setUserId(user.getUserId());
			sp.setSessionId(se.getSessionId());
			sp.setBetTime(DateTimeUtil.getCurrentDate());
			sp.setMoney(money);
			sp.setSchedule(new BigDecimal(buyNum).divide(new BigDecimal(num),2,BigDecimal.ROUND_HALF_EVEN));
			sp.setNum(num);
			sp.setBetNum(betNum);
			sp.setRestNum(num-buyNum);
			sp.setPreMoney(preMoney);

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
			sp.setGameType(Constants.GAME_TYPE_GF_JXSSC);
			sp.setGameName(JxSscConstants.GAME_NAME);
			sp.setSessionNo(se.getSessionNo());
			sp.setMultiple(multiple);
			sp.setBetType(Constants.SPONSOR);//合买
//			sp.setIsAddNo(FcConstants.NOT_ADD_NO);
//			sp.setIsWinStop(FcConstants.WIN_NO_STOP);
			sp.setBatchNum(batchNum);
			sp.setWinCash(new BigDecimal(0));
			sp = (GaBetSponsor) jxSscDAO.saveObjectDB(sp);
			orderNum = se.getSessionNo() + "JXSSC" +sp.getJointId();
			sp.setOrderNum(orderNum);
			jxSscDAO.saveObject(sp);
			
			GaBetPart pa =new GaBetPart();
			BigDecimal lotteryMoney = money.multiply(new BigDecimal(buyNum)).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);

			pa.setJointId(sp.getJointId());
			pa.setBetMoney(lotteryMoney);
			pa.setBuyNum(buyNum);
			pa.setUserId(user.getUserId());
			pa.setBuyTime(DateTimeUtil.getNowSQLDate());
			pa.setWinCash(new BigDecimal(0));
			pa.setWinPoint(new BigDecimal(0));
			pa.setBehavior(Constants.ORIGINATE);
			jxSscDAO.saveObject(pa);

			for(String op : list){
				int playType = Integer.parseInt(op.split("\\+")[0]); //玩法
				String betBall = op.split("\\+")[1]; //投注的球
				BigDecimal betRate = null;
				String title = null; //玩法对应的中文
				BigDecimal point = null;//积分倍数
				
				for(int i=0;i<opList.size();i++){
					GaBetOption betOp = (GaBetOption) opList.get(i);
					if(playType == Integer.parseInt(betOp.getPlayType())){
						betRate = betOp.getBetRate();
						title = betOp.getTitle();
						point = betOp.getPointMultiple();
					}
				}
				GaBetSponsorDetail de = new GaBetSponsorDetail();
				de.setBetRate(betRate);
				de.setJointId(sp.getJointId());
				de.setOptionTitle(betBall);
				de.setOrderNum(orderNum);
				de.setPlayType(playType);
				de.setWinResult(Constants.INIT);
				de.setTitle(title);
				de.setPointMultiple(point);
				saveList.add(de);
			}
						
			// 2.保存明细   订单：593894，购买彩票 扣款 2 元  方案：17088SSQ00318480 保底扣款 1 元

			BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
			BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
			user.setDayBetMoney(dayBetMoney.add(lotteryMoney));
			user.setAggregateBetMoney(aggregateBetMoney.add(lotteryMoney));

			if(Constants.GUARANTEE.equals(isGuaranteed)){//保底
			    //保底的钱
				BigDecimal guaranteeMoney = money.multiply(new BigDecimal(guNum)).divide(new BigDecimal(num),2, BigDecimal.ROUND_HALF_EVEN);
				StringBuilder remark = new StringBuilder();
				remark.append("订单:").append(sp.getOrderNum()).append(" 保底预扣款 ")
				    .append(guaranteeMoney).append("元");
				user=userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_FROZEN, guaranteeMoney, sp.getJointId(), Constants.GAME_TYPE_GF_JXSSC, remark.toString());
			}
			StringBuilder remark = new StringBuilder();
			remark.append("订单:").append(sp.getOrderNum()).append(",购买彩票 扣款 ")
			    .append(lotteryMoney).append("元");
			user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, lotteryMoney, pa.getRid(), Constants.GAME_TYPE_GF_JXSSC, remark.toString());

			jxSscDAO.updateObjectList(saveList, null);
			return user;
		}
		
		@Override
		public User saveProcurementServiceBet(User user,
				List<String> list, Map<String, Integer> seMap,
				String isAddNo, String isWinStop,int betNum) {
			List<Object> saveList = new ArrayList<Object>();
			String batchNum =""; //批次号，是否属于同一追号
			batchNum = System.currentTimeMillis() +JxSscUtil.getTwo();
			boolean isFirst=true; //是否是追号第一期
			//投注与明细关联
			List<UserTradeDetailRl> rlList = new ArrayList<UserTradeDetailRl>();
			
			List<GaBetOption>  opList= gaService.findGaBetOptionByGameType(Constants.GAME_TYPE_GF_JXSSC);
			String sessionNo = "";
			Integer tradeDetailId = 0;
			for (String key : seMap.keySet()) {
				sessionNo = key; //期号
				int multiple = seMap.get(key); //倍数
				GfJxSscGaSession se = this.getPreviousSessionBySessionNo(sessionNo);//获取期号信息
				String orderNum = "";
				BigDecimal money = new BigDecimal(betNum*1*multiple);//投注金额
				
				GaBetSponsor sp = new GaBetSponsor();
				sp.setUserId(user.getUserId());
				sp.setOrderNum(orderNum);
				sp.setSessionId(se.getSessionId());
				sp.setBetTime(DateTimeUtil.getNowSQLDate());
				sp.setMoney(money);
				sp.setSchedule(new BigDecimal(1));
				sp.setNum(1);//总份数
				sp.setBetNum(betNum);//总注数
				sp.setRestNum(0);
//				sp.setGuaranteedNum(new BigDecimal(0)); //保底份额
				sp.setWinResult(Constants.INIT); //未开奖
//				sp.setIsGuarantee(FcConstants.NOT_GUARANTEE);//不保底
				sp.setBetFlag(Constants.PUB_STATUS_OPEN); //有效
				sp.setGameType(Constants.GAME_TYPE_GF_JXSSC);
				sp.setGameName(JxSscConstants.GAME_NAME);
				sp.setSessionNo(sessionNo);
				sp.setMultiple(multiple);
				sp.setBetType(Constants.PROCUREMENT_SERVICE);
				sp.setIsAddNo(isAddNo);
				sp.setIsWinStop(isWinStop);
				sp.setBatchNum(batchNum);
				sp.setWinCash(new BigDecimal(0));
				sp.setPreMoney(money);
				sp = (GaBetSponsor) jxSscDAO.saveObjectDB(sp);
				orderNum = sessionNo + "JXSSC" +sp.getJointId();
				sp.setOrderNum(orderNum);
			    saveList.add(sp);
			    
			  //关联构造
				UserTradeDetailRl rl = new UserTradeDetailRl();
				rl.setBetDetailId(sp.getJointId());
				rlList.add(rl);
				
				GaBetPart pa =new GaBetPart();
				pa.setJointId(sp.getJointId());
				pa.setBetMoney(money);
				pa.setBuyNum(1);
				pa.setUserId(user.getUserId());
				pa.setBuyTime(DateTimeUtil.getNowSQLDate());
				pa.setWinCash(new BigDecimal(0));
				pa.setWinPoint(new BigDecimal(0));
				pa.setBehavior(Constants.ORIGINATE);
				jxSscDAO.saveObject(pa);
				
				for(String op : list){
					int playType = Integer.parseInt(op.split("\\+")[0]); //玩法
					String betBall = op.split("\\+")[1]; //投注的球
					BigDecimal betRate = null;
					String title = null; //玩法对应的中文
					BigDecimal point = null;
					
					for(int i=0;i<opList.size();i++){
						GaBetOption betOp = (GaBetOption) opList.get(i);
						if(playType == Integer.parseInt(betOp.getPlayType())){
							betRate = betOp.getBetRate();
							title= betOp.getTitle();
							point = betOp.getPointMultiple();
						}
					}
					GaBetSponsorDetail de = new GaBetSponsorDetail();
					de.setBetRate(betRate);
					de.setJointId(sp.getJointId());
					de.setOptionTitle(betBall);
					de.setOrderNum(orderNum);
					de.setPlayType(playType);
					de.setWinResult(Constants.INIT);
					de.setTitle(title);
					de.setPointMultiple(point);
					
					int betCount = JxSscUtil.getTotalBetNum(playType+"",betBall);//投注注数
					Integer betMoney = betCount*1*multiple;//每注的金额
					de.setBetMoney(GameHelpUtil.round(new BigDecimal(betMoney)));

					saveList.add(de);
				}
				
				BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
				BigDecimal dayBetMoney=ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
				user.setDayBetMoney(dayBetMoney.add(money));
				user.setAggregateBetMoney(aggregateBetMoney.add(money));

				// 2.保存明细   订单：594693，彩票预购 扣款 2 元  订单：594688，购买彩票 扣款 2 元
				StringBuilder remark = new StringBuilder();
				if(isFirst){
					remark.append("订单:").append(sp.getOrderNum()).append(",购买彩票 扣款 ")
				    .append(money).append("元");
					isFirst = false;
					//user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, money, pa.getRid(), Constants.GAME_TYPE_GF_JXSSC, remark.toString());
					tradeDetailId = userService.saveTradeDetail(user,user.getUserId(),
							Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_LOTO,money, null,
							Constants.GAME_TYPE_GF_JXSSC,remark.toString(), sessionNo, user.getUserType(),user.getLoginName());

				}else{
					remark.append("订单:").append(sp.getOrderNum()).append(",购买预扣 扣款 ")
				    .append(money).append("元");
					user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_BUY_LOTO, money, pa.getRid(), Constants.GAME_TYPE_GF_JXSSC, remark.toString());
				}
			}
			jxSscDAO.updateObjectList(saveList, null);
			//更新用户实时余额  --by.cuisy.20171209
			userService.updateUserMoneyAndBetMoney(user.getUserId());
			userService.updateUserBanlance(user.getUserId());
			
			//保存关联
			for(UserTradeDetailRl rl:rlList){
				rl.setTradeDetailId(tradeDetailId);
				rl.setGfxy(Constants.GAME_PLAY_CATE_GF);
			}
			jxSscDAO.updateObjectList(rlList, null);
			
			return user;
		}
		
		@Override
		public List<WinCoDTO> findGaWinCountList() {
			return jxSscDAO.findGaWinCountList();
		}
		
		/**
		 * 保存用户余额
		 * @param userId
		 * @param string
		 */
		private void updateOpenData(GaBetPart part,String remark,String sessionNo) {
			User user = (User)jxSscDAO.getObject(User.class, part.getUserId());
			userService.saveTradeDetail(user,part.getUserId(), Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_CASH_PRIZE, part.getWinCash(), part.getRid(), 
					Constants.GAME_TYPE_GF_JXSSC,remark,
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
		
		/**
		 * 保存用户积分明细
		 * @param userId
		 * @param string
		 */
		private void updateOpenDataPoints(GaBetPart part,String remark) {
			userService.savePointDetail(part.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, part.getWinPoint(), part.getRid(),remark);
		}
		
		@Override
		public void updateFetchAndOpenOmit(GfJxSscGaSession tempsession){
//			GfJxSscGaSession session=jxSscDAO.getCurrentSession();
//			if(session!=null){
//				GfJxSscGaSession tempsession =jxSscDAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");
				if(tempsession!=null){
					if(ParamUtils.chkString(tempsession.getOpenResult())){
						String openResult=tempsession.getOpenResult();
						String array[]=openResult.split(",");
						GfJxSscGaOmit omit=jxSscDAO.getFcGaOmitBySessionNo(tempsession.getSessionNo());
						if(omit==null){
							GfJxSscGaOmit preomit=jxSscDAO.getFcGaOmitBySessionNo((Integer.parseInt(tempsession.getSessionNo())-1)+"");
							if(preomit == null){
								GfJxSscGaSession se = (GfJxSscGaSession) jxSscDAO.getObject(GfJxSscGaSession.class, tempsession.getSessionId()-1);
								preomit=jxSscDAO.getFcGaOmitBySessionNo(se.getSessionNo());
							}
							if(preomit!=null){
								omit=new GfJxSscGaOmit();
							      Field[] field = omit.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
							        try {
							            for (int j = 0; j < field.length; j++) { // 遍历所有属性
							                String name = field[j].getName(); // 获取属性的名字
							                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
							                String type = field[j].getGenericType().toString(); // 获取属性的类型
							                
							                if (type.equals("class java.lang.Integer")) {
							                	if(!"Oid".equals(name)){
								                    Method m = omit.getClass().getMethod("get" + name);
								                    Method preM = preomit.getClass().getMethod("get" + name);
								                    Integer value = (Integer) preM.invoke(preomit);
								                    if (value == null) {
								                        m = omit.getClass().getMethod("set"+name,Integer.class);
								                        m.invoke(omit, 0);
								                    }else{
								                        m = omit.getClass().getMethod("set"+name,Integer.class);
								                        m.invoke(omit, value+1);
								                    }
							                	}
							                }
							                // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
							            }
							        } catch (NoSuchMethodException e) {
							            e.printStackTrace();
							        } catch (SecurityException e) {
							            e.printStackTrace();
							        } catch (IllegalAccessException e) {
							            e.printStackTrace();
							        } catch (IllegalArgumentException e) {
							            e.printStackTrace();
							        } catch (InvocationTargetException e) {
							            e.printStackTrace();
							        }
							        
								Field f =null;
								try {
									f= GfJxSscGaOmit.class.getDeclaredField("ge"+Integer.parseInt(array[4]));
									f.setAccessible(true);  
									f.set(omit, 0);
									f= GfJxSscGaOmit.class.getDeclaredField("shi"+Integer.parseInt(array[3]));
									f.setAccessible(true);  
									f.set(omit, 0);
									f= GfJxSscGaOmit.class.getDeclaredField("bai"+Integer.parseInt(array[2]));
									f.setAccessible(true);  
									f.set(omit, 0);
									f= GfJxSscGaOmit.class.getDeclaredField("qian"+Integer.parseInt(array[1]));
									f.setAccessible(true);  
									f.set(omit, 0);
									f= GfJxSscGaOmit.class.getDeclaredField("wan"+Integer.parseInt(array[0]));
									f.setAccessible(true);  
									f.set(omit, 0);
									for(int i=0;i<array.length;i++){
										f= GfJxSscGaOmit.class.getDeclaredField("zu"+Integer.parseInt(array[i]));
										f.setAccessible(true);  
										f.set(omit, 0);
									}
								} catch (Exception e) {
									return;
								}
							}else{
								omit=new GfJxSscGaOmit();
							      Field[] field = omit.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
							        try {
							            for (int j = 0; j < field.length; j++) { // 遍历所有属性
							                String name = field[j].getName(); // 获取属性的名字
							                name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
							                String type = field[j].getGenericType().toString(); // 获取属性的类型
							                if (type.equals("class java.lang.Integer")) {
							                	if(!"Oid".equals(name)){
								                    Method m = omit.getClass().getMethod("get" + name);
								                    m = omit.getClass().getMethod("set"+name,Integer.class);
								                    m.invoke(omit, 0);
							                	}
							                }
							                // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
							            }
							        } catch (NoSuchMethodException e) {
							            e.printStackTrace();
							        } catch (SecurityException e) {
							            e.printStackTrace();
							        } catch (IllegalAccessException e) {
							            e.printStackTrace();
							        } catch (IllegalArgumentException e) {
							            e.printStackTrace();
							        } catch (InvocationTargetException e) {
							            e.printStackTrace();
							        }

							}
							omit.setOpenResult(tempsession.getOpenResult());
							omit.setSessionNo(tempsession.getSessionNo());
							jxSscDAO.saveObjectDB(omit);
						}
					}		
				}
//			}
		}
		@Override
		public List<GfJxSscGaSession> updateAndOpenResult() {
			GfJxSscGaSession currentSession= jxSscDAO.getCurrentSession();
			List<GfJxSscGaSession> sessionlist = new ArrayList<GfJxSscGaSession>();

			if(currentSession == null){
				return null;
			}else{
				GfJxSscGaSession tempsession =jxSscDAO.getPreviousSessionBySessionNo((Integer.parseInt(currentSession.getSessionNo())-1)+"");	
				if(tempsession!=null){
				}else{
					tempsession=(GfJxSscGaSession) jxSscDAO.getObject(GfJxSscGaSession.class, (currentSession.getSessionId()-1));
				}
				String openStatus = tempsession.getOpenStatus();//开奖状态
				if(openStatus.equals(Constants.OPEN_STATUS_INIT) || openStatus.equals(Constants.OPEN_STATUS_OPENING)){//状态为初始化 或 开奖中 则开始开奖
					//更新开奖结果
					JxSscOpenResult fc = new JxSscOpenResult();
					String openResult = "";
					if(ParamUtils.chkString(tempsession.getOpenResult())){
						openResult=tempsession.getOpenResult();
					}else{
						openResult=fc.getRandomResult();
						tempsession.setOpenResult(openResult);
					}		
					tempsession.setOpenTime(DateTimeUtil.getCurrentDate());
					tempsession.setOpenStatus(Constants.OPEN_STATUS_OPENED);
					jxSscDAO.saveObject(tempsession);
					sessionlist.add(tempsession);
					
		            GaSessionInfo sessionInfo = gaService.findGaSessionInfo(Constants.GAME_TYPE_GF_THREE);
		            if(sessionInfo!=null){
		        		sessionInfo.setOpenResult(openResult);
		        		sessionInfo.setOpenSessionNo(tempsession.getSessionNo());
		        		sessionInfo.setEndTime(tempsession.getEndTime());
		        		sessionInfo.setLatestSessionNo(currentSession.getSessionNo());
		        		jxSscDAO.saveObject(sessionInfo);
		        		
		            }
		            
					List<GfJxSscGaTrend> list=jxSscDAO.findGaTrendList();
					List<GfJxSscGaTrend> savelist=new ArrayList<GfJxSscGaTrend>();
					Map<String,Boolean> map=getTrendResult(openResult);
					if(!map.isEmpty()){
						for(int i=0;i<list.size();i++){
							GfJxSscGaTrend trend=list.get(i);
							if(map.get(trend.getTrendTitle()) != null && map.get(trend.getTrendTitle()) == true){
								trend.setTrendCount(0);
							}else{
								trend.setTrendCount(trend.getTrendCount() + 1);
							}
							savelist.add(trend);
						}
						jxSscDAO.updateObjectList(savelist, null);
					}

				}
				return sessionlist;
			}
		}

		public List<GfJxSscGaOmit> findGfJxSscGaOmitList(String hql,
				List<Object> para, int num) {
			return jxSscDAO.findGfJxSscGaOmitList(hql,para,num);

		}

		public PaginationSupport findGfJxSscGaSessionList(String hql,
				List<Object> para, int startIndex, int pageSize) {
			return jxSscDAO.findGfJxSscGaSessionList(hql,para,startIndex,pageSize);
		}			

		
		@Override
		public PaginationSupport findGameBetAndWinList(String hqls,
				List<Object> para, int startIndex, int pageSize) {
			return  jxSscDAO.findGameBetAndWinList(hqls,para,startIndex,pageSize);
		}
		
		@Override
		public String saveAndOpenResult(GfJxSscGaSession session,
				String openResult) {
			String buffer="";
			String flag = "";
			if(ParamUtils.chkString(openResult)){
				String array[]=openResult.split(",");
				for(int i=0;i<array.length;i++){
					if(ParamUtils.chkString(array[i].trim())){
						buffer=buffer+array[i].trim()+",";
					}
				}
				buffer=buffer.substring(0, buffer.length()-1);
				session.setOpenResult(openResult);		
				flag = updateJxSscSessionOpenResultMethod(session, session.getOpenResult(),null);
				if(!ParamUtils.chkString(flag)){      
					session.setOpenTime(DateTimeUtil.getJavaUtilDateNow());
					session.setOpenStatus(Constants.OPEN_STATUS_OPENED);
					jxSscDAO.updateObject(session, null);
//					log.info("___[open result success sessionNO["+session.getSessionNo()+"]]");
				}else{
//					log.info("___[open result fail sessionNO["+session.getSessionNo()+"], please check...]");
				}
			}
			return flag;
		}
		
		@Override
		public void updateDayBetCount(GfJxSscGaSession tempsession) {
//			GfJxSscGaSession session=jxSscDAO.getCurrentSession();
//			if(session!=null){
//				GfJxSscGaSession tempsession =jxSscDAO.getPreviousSessionBySessionNo((Integer.parseInt(session.getSessionNo())-1)+"");
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
						spPara.add(Constants.GAME_TYPE_GF_JXSSC);
						
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
							String startDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -5))+" 00:00:00";
							String endDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -5))+" 23:59:59";//DateTimeUtil.getDateTimeOfMinutes(date, -5)

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
			userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_GUA_BACK, reMoney, sponsor.getJointId(), Constants.GAME_TYPE_GF_JXSSC,remark);
		}
		
		
		private void updateUserBetBack(GaBetSponsor sponsor,String remark) {//方案撤单  退回全部保底
			userService.saveTradeDetail(null,sponsor.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, sponsor.getGuaranteedMoney(), sponsor.getJointId(), Constants.GAME_TYPE_GF_JXSSC,remark);
		}
		
		
		private void updateUserBetBack(GaBetPart betPart, BigDecimal betMoney,
				String remark) {//退回参与用户金额
			userService.saveTradeDetail(null,betPart.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_DRAW, betMoney, betPart.getRid(), Constants.GAME_TYPE_GF_JXSSC,remark);
		}

}
