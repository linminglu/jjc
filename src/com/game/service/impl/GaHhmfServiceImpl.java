package com.game.service.impl;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.service.IBaseDataService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.game.GameConstants;
import com.game.dao.IGaHhmfDAO;
import com.game.model.GaHhmfBet;
import com.game.model.GaHhmfBetDetail;
import com.game.model.GaHhmfBetOption;
import com.game.model.GaHhmfSession;
import com.game.model.dto.GaHhmfDTO;
import com.game.service.IGaHhmfService;
import com.ram.service.user.IUserService;

public class GaHhmfServiceImpl extends BaseService implements IGaHhmfService {
	private IGaHhmfDAO gaHhmfDAO;

	public void setGaHhmfDAO(IGaHhmfDAO gaHhmfDao) {
		gaHhmfDAO = gaHhmfDao;
		super.dao = gaHhmfDAO;
	}
	
	private  IBaseDataService baseDataService;
	
	protected IUserService userService;
	
	public IBaseDataService getBaseDataService() {
		return baseDataService;
	}

	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
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
		
		String flag = "fail";//返回状态
		
		Date now = DateTimeUtil.getJavaUtilDateNow();
		
		//今天日期处理 yyyy-MM-dd
		String today = DateTimeUtil.DateToString(now);
	
		//今天是否已经初始化场次
		boolean isTodaySessionInit = this.checkTodaySessionInit(now);
		if(!isTodaySessionInit){
			Date startDate = DateTimeUtil.parse(today + GameConstants.HHMF_START_TIME_END);//开始时间
			log.info("___[start today]["+DateTimeUtil.format(startDate)+"]__________________________");
			
			List<GaHhmfSession> saveList = new ArrayList<GaHhmfSession>();//对象集合
			
			for(int i=0;i<GameConstants.HHMF_MAX_PART;i++) {
				//当天所有场次
				Date sDate = new Date(startDate.getTime() + i*GameConstants.HHMF_TIME_INTERVAL * 1000);//开始时间
				Date eDate = new Date(startDate.getTime() + (i+1)*GameConstants.HHMF_TIME_INTERVAL * 1000);//结束时间
				
				String openResult = GameConstants.getRandomOpenHHMFResult();//54张牌中随机生成
				String sessionNo = (i+1)+"";//期号
				
				GaHhmfSession hhmf = new GaHhmfSession();
				hhmf.setSessionNo(sessionNo);
				hhmf.setStartTime(sDate);
				hhmf.setEndTime(eDate);
				hhmf.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_BET);
				hhmf.setOpenResult(openResult);//开奖号随机生成
				hhmf.setOpenType(GameConstants.getHHMFOpenResultType(openResult));
				hhmf.setUserCount(0);
				hhmf.setPointCount(0);
				hhmf.setSessionCash(0);
				//gaHhmfDAO.saveObject(hhmf);
				saveList.add(hhmf);
				
				//log.info("["+(i+1)+"]___[sessionNo="+sessionNo+"][sDate="+DateTimeUtil.format(sDate)+"][eDate()="+DateTimeUtil.format(eDate)+"]");
			}
			
			gaHhmfDAO.updateObjectList(saveList, null);//批量保存
			
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
	 * 检查今天的场次是否已经生成
	 * false=未生成
	 * true=已生成
	 * @return
	 */
	public boolean checkTodaySessionInit(Date now){
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		HQUtils hq = new HQUtils("from GaHhmfSession gks where gks.startTime>=? and gks.startTime<=?");
		hq.addPars(todayStart);
		hq.addPars(todayEnd);
		Integer count = gaHhmfDAO.countObjects(hq);
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
		
		Date now = DateTimeUtil.getJavaUtilDateNow();
		GaHhmfSession session = gaHhmfDAO.getCurrentSession(now);//当前期
		if(session!=null){
			//now = DateTimeUtil.getJavaUtilDateNow();
			//Date sTime = session.getStartTime();//开始时间
			Date eTime = session.getEndTime();//截止时间
			log.info("___"+DateTimeUtil.format(eTime)+","+DateTimeUtil.format(now));
			long diff = eTime.getTime()-now.getTime();//时间间隔
			String openStatus = session.getOpenStatus();
			if(diff>0){
				if(diff>10000){//投注中
					
					session.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_BET);
					gaHhmfDAO.saveObject(session);
//					System.out.println(diff+"<<<<<<<<<<<<<<<<<<0<<<<<<<<<<<<<<<<<<<<diff"+session.getOpenStatus());
				}else if(diff<=10000 && diff>5000){//开奖中
					//开奖-----
					if(!GameConstants.HHMF_OPEN_STATUS_OPENING.equals(openStatus)){
						session.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_OPENING);
//						System.out.println(diff+"<<<<<<<<<<<<<<<<<<1<<<<<<<<<<<<<<<<<<<<diff"+session.getOpenStatus());
						session.setOpenTime(new Date());

						//调用开奖程序
						
						this.saveOpenResult(session);
//						gaHhmfDAO.saveObject(session);
					}
				}else{//空闲中-已开奖
					if(!GameConstants.HHMF_OPEN_STATUS_WAITING.equals(openStatus)){
						session.setOpenStatus(GameConstants.HHMF_OPEN_STATUS_WAITING);
						gaHhmfDAO.saveObject(session);
//						log.info("___[save......2][no]："+session.getSessionNo()+"  [id]："+session.getSessionId()+"[openStatus:"+session.getOpenStatus()+"]");
					}
				}

				flag = "success";
				log.info("___[beting/opening/waiting]["+diff+"]["+session.getSessionNo()+"][openStatus:"+session.getOpenStatus()+"]");
			}else{
				log.info("___[data wrong......]["+diff+"]["+session.getSessionNo()+"]");
			}
			
		}else{
			log.info("___[data wrong not found current session......]");
		}
		return flag;
	} 
	
	
	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findLotteryResultList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findOneUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findBetUserList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findBetDetailList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public List<GaHhmfDTO> findColorCountList(){
		return gaHhmfDAO.findColorCountList();
	}
	public List<GaHhmfDTO> findRecent30GaHhmfSessionList(){
		return gaHhmfDAO.findRecent30GaHhmfSessionList();
	}
	public GaHhmfSession getCurrentSession(Date now){
		return gaHhmfDAO.getCurrentSession(now);
	}
	public Integer findCurrentSessionCountPointsByBetType(String betType,Date now){
		return gaHhmfDAO.findCurrentSessionCountPointsByBetType(betType,now);
	}
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findUserBetList(hqls, para, startIndex, pageSize);
		return ps;
	}

	public List<GaHhmfBetDetail> findUserBetDetailList(Integer sessionId,Integer uid){
		return gaHhmfDAO.findUserBetDetailList(sessionId,uid);
	}
	
	public GaHhmfBet findGaHhmfBetBySessionIdAndUid(Integer sessionId,Integer uid){
		return gaHhmfDAO.findGaHhmfBetBySessionIdAndUid(sessionId,uid);
	}

	public PaginationSupport findBetOptionList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = gaHhmfDAO.findBetOptionList(hqls, para, startIndex, pageSize);
		return ps;
	}
	public void updateSort(Integer id, String flag) {
		gaHhmfDAO.updateSort(id, flag);
	}

	public List<GaHhmfBetOption> findOddsList(String hql, List<Object> para){
		return gaHhmfDAO.findOddsList(hql,para);
	}
	public GaHhmfBetOption getGaHhmfOddsByType(String betType){
		HQUtils hq=new HQUtils( " from GaHhmfBetOption ho where 1=1 ");
		hq.addHsql(" and ho.optionType=? ");
		hq.addPars(betType);
		return (GaHhmfBetOption) gaHhmfDAO.getObject(hq);
	}

	public Integer getBetPointCountBySessionIdAndBetType(Integer sessionId,String betType){
		return gaHhmfDAO.getBetPointCountBySessionIdAndBetType(sessionId,betType);
	}
	
	/**
	 * 开奖
	 */
	public void saveOpenResult(GaHhmfSession session){
		
		List<GaHhmfBet> betList=new ArrayList<GaHhmfBet>();
		
		//获取游戏的难度等级
		String level=baseDataService.getPointsByType(Constants.POINT_TYPE_BET_HHMF).setScale(0, RoundingMode.UP).toString();
		//根据难度等级获取开奖结果
		String openResult=this.getGaHhmfSessionOpenRsult(session,level);
		if(!openResult.equals(session.getOpenResult())){
			session.setOpenResult(openResult);
			if(openResult.split("-").length>1){//格式为1-J,2-8,55
				session.setOpenType(openResult.split("-")[0]);
			}else{
				session.setOpenType(Constants.GAME_HHMF_WANG);
			}
			gaHhmfDAO.saveObject(session,null);
		}
		System.out.println("firstopenResult-------->"+openResult);
	
		if(openResult.split("-").length>1){
			openResult=openResult.split("-")[0];
		}else{
			openResult=Constants.GAME_HHMF_WANG;
		}
		
		List<GaHhmfBetDetail> saveList=new ArrayList<GaHhmfBetDetail>();
		System.out.println("secondopenResult-------->"+openResult);
		//中奖的投注
		HQUtils hq=new HQUtils( " from GaHhmfBetDetail ho where 1=1 and ho.winResult='0' ");
		hq.addHsql(" and ho.sessionId=? ");
		hq.addPars(session.getSessionId());
		hq.addHsql("   and ho.betType=?  ");
		hq.addPars(openResult);
		List list=gaHhmfDAO.findObjects(hq);
		
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				GaHhmfBetDetail dtetail=(GaHhmfBetDetail) list.get(i);
				Integer betPoint = dtetail.getBetPoint();
				GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, dtetail.getBetId());
				BigDecimal betWinCash=(bet.getWinCash()==null?new BigDecimal(0):bet.getWinCash());
//				Integer sessionWinCash=(session.getSessionCash()==null?0:session.getSessionCash());
//				BigDecimal betRate=((GaHhmfBetOption)gaHhmfDAO.getObject(GaHhmfBetOption.class, dtetail.getBetOptionId())).getBetRate();
//				BigDecimal winCash=null;
//				if(openResult.equals(dtetail.getBetType())){
					if(openResult.equals(Constants.GAME_HHMF_HEITAO)){                                                                                     
						dtetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
						dtetail.setWinCash(winCash);
						saveList.add(dtetail);
						bet.setWinCash(betWinCash.add(winCash));
						betList.add(bet);
						session.setSessionCash(session.getSessionCash()+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						System.out.println("hwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
					}else if(openResult.equals(Constants.GAME_HHMF_HONGTAO)){
						dtetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
						dtetail.setWinCash(winCash);
						bet.setWinCash(betWinCash.add(winCash));
//						System.out.println("rwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
						saveList.add(dtetail);
						session.setSessionCash(session.getSessionCash()+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
						betList.add(bet);
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
					}else if(openResult.equals(Constants.GAME_HHMF_MEIHUA)){
						dtetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
						dtetail.setWinCash(winCash);
						saveList.add(dtetail);
						bet.setWinCash(betWinCash.add(winCash));
						betList.add(bet);
						session.setSessionCash(session.getSessionCash()+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						System.out.println("mwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
					}else if(openResult.equals(Constants.GAME_HHMF_FANGKUAI)){
						dtetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
						dtetail.setWinCash(winCash);
						saveList.add(dtetail);						
						bet.setWinCash(betWinCash.add(winCash));
						betList.add(bet);
						session.setSessionCash(session.getSessionCash()+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						System.out.println("fwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
					}else if(openResult.equals(Constants.GAME_HHMF_WANG)){
						dtetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
						dtetail.setWinCash(winCash);
						saveList.add(dtetail);
						bet.setWinCash(betWinCash.add(winCash));
						betList.add(bet);
						session.setSessionCash(session.getSessionCash()+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						System.out.println("dwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
					}
//				}else{
//					dtetail.setWinResult("2");
//					saveList.add(dtetail);
//				}
			}
//			gaHhmfDAO.updateObjectList(saveList, null);
		}
		
		//没有中奖的
		HQUtils hq1=new HQUtils( " from GaHhmfBetDetail ho where 1=1 and ho.winResult='0' ");
		hq1.addHsql(" and ho.sessionId=? ");
		hq1.addPars(session.getSessionId());
		hq1.addHsql("  and ho.betType !=?  ");
		hq1.addPars(openResult);
		List  list1=gaHhmfDAO.findObjects(hq1);
		if(list1!=null&&list1.size()>0){
			for(int i=0;i<list1.size();i++){
				GaHhmfBetDetail dtetail1=(GaHhmfBetDetail) list1.get(i);
				dtetail1.setWinResult(Constants.GAME_WIN_STATUS_NOWIN);
				saveList.add(dtetail1);
			}
		}
		gaHhmfDAO.updateObjectList(saveList, null);
		gaHhmfDAO.updateObjectList(betList, null);
		
		
//		List<GaHhmfBetDetail> betDetailList=null;
//		List<GaHhmfBet> betList=null;
//		
//	
//		HQUtils hq=new HQUtils( " from GaHhmfBetDetail ho where 1=1 ");
//		hq.addHsql(" and ho.sessionId=? and ho.winResult='0' ");
//		hq.addPars(session.getSessionId());
//		List list=gaHhmfDAO.findObjects(hq);
//		if(list!=null&&list.size()>0){
//			//获取游戏的难度等级
//			String level=baseDataService.getPointsByType(Constants.POINT_TYPE_BET_HHMF).setScale(0, RoundingMode.UP).toString();
//			String openResult=this.getGaHhmfSessionOpenRsult(session,level);
//			if(!openResult.equals(session.getOpenResult())){
//				session.setOpenResult(openResult);
//				if(openResult.split("-").length>1){
//					session.setOpenType(openResult.split("-")[0]);
//				}else{
//					session.setOpenType(Constants.GAME_HHMF_WANG);
//				}
//				gaHhmfDAO.saveObject(session,null);
//			}
//			
//			System.out.println("firopenResult-------->"+openResult);
//			
//			if(openResult.split("-").length>1){
//				openResult=openResult.split("-")[0];
//			}else{
//				openResult=Constants.GAME_HHMF_WANG;
//			}
////			System.out.println("secopenResult-------->"+openResult);
//			List<GaHhmfBetDetail> saveList=new ArrayList<GaHhmfBetDetail>();
//			for(int i=0;i<list.size();i++){
//				GaHhmfBetDetail dtetail=(GaHhmfBetDetail) list.get(i);
//				Integer betPoint = dtetail.getBetPoint();
////				GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, dtetail.getBetId());
////				BigDecimal winCash=null;
//				if(openResult.equals(dtetail.getBetType())){
//					if(openResult.equals(Constants.GAME_HHMF_HEITAO)){                                                                                     
//						dtetail.setWinResult("1");
//						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
//						dtetail.setWinCash(winCash);
//						saveList.add(dtetail);
////						System.out.println("hwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
//					}else if(openResult.equals(Constants.GAME_HHMF_HONGTAO)){
//						dtetail.setWinResult("1");
//						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
//						dtetail.setWinCash(winCash);
////						System.out.println("rwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						saveList.add(dtetail);
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
//					}else if(openResult.equals(Constants.GAME_HHMF_MEIHUA)){
//						dtetail.setWinResult("1");
//						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
//						dtetail.setWinCash(winCash);
//						saveList.add(dtetail);
////						System.out.println("mwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
//					}else if(openResult.equals(Constants.GAME_HHMF_FANGKUAI)){
//						dtetail.setWinResult("1");
//						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
//						dtetail.setWinCash(winCash);
//						saveList.add(dtetail);
////						System.out.println("fwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
//					}else if(openResult.equals(Constants.GAME_HHMF_WANG)){
//						dtetail.setWinResult("1");
//						BigDecimal  winCash=dtetail.getBetRate().multiply(new BigDecimal(betPoint));
//						dtetail.setWinCash(winCash);
//						saveList.add(dtetail);
////						System.out.println("dwinCash-------->"+Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()));
//						userService.savePointDetail(dtetail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(winCash.setScale(0, RoundingMode.UP).toString()), null);
//					}
////					gaHhmfDAO.saveObject(dtetail);
//					
//				}else{
//					dtetail.setWinResult("2");
//					saveList.add(dtetail);
//				}
//			}
//			gaHhmfDAO.updateObjectList(saveList, null);
//		}
	}
	
	public String  getGaHhmfSessionOpenRsult(GaHhmfSession session,String level){
		String openResult="";
		
		if(level.equals(Constants.GAME_HHMF_LEVEL_DI)){//低级
			openResult=session.getOpenResult();
		}else if(level.equals(Constants.GAME_HHMF_LEVEL_ZHONG)){//中级
			
			List<String> randomList=new ArrayList<String>();
			for(int i=0;i<5;i++){
				randomList.add("0");
			}
			randomList.add("1");
			randomList.add("1");
			randomList.add("1");
			randomList.add("2");
			randomList.add("2");
			Collections.shuffle(randomList);
			int index=new Random().nextInt(10);
			
			
//			List<Integer> list=new ArrayList<Integer>();
//			Map<String,String> map=new HashMap<String,String>();
				
			HQUtils hq=new HQUtils( " from GaHhmfBetOption ho order by ho.optionType ");
			List optionList=gaHhmfDAO.findObjects(hq);
			Map<String,Integer> smap=new HashMap<String,Integer>();

				
				
			if(optionList!=null&&optionList.size()>0){
	//				GaHhmfBetOption option=((GaHhmfBetOption) optionList.get(0)).getBetRate().subtract(new BigDecimal(1));
					int htWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
							Constants.GAME_HHMF_HEITAO)).multiply(((GaHhmfBetOption) optionList.get(0)).getBetRate().subtract(new BigDecimal(1))).intValue();
	
					int rtWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
							Constants.GAME_HHMF_HONGTAO)).multiply(((GaHhmfBetOption) optionList.get(1)).getBetRate().subtract(new BigDecimal(1))).intValue();
	
					int mfWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
							Constants.GAME_HHMF_MEIHUA)).multiply(((GaHhmfBetOption) optionList.get(2)).getBetRate().subtract(new BigDecimal(1))).intValue();
	
					int fpWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
							Constants.GAME_HHMF_FANGKUAI)).multiply(((GaHhmfBetOption) optionList.get(3)).getBetRate().subtract(new BigDecimal(1))).intValue();
	
					int dwWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
							Constants.GAME_HHMF_WANG)).multiply(((GaHhmfBetOption) optionList.get(4)).getBetRate().subtract(new BigDecimal(1))).intValue();
					
					if(htWin==rtWin&&mfWin==rtWin&&fpWin==dwWin&&dwWin==mfWin){
						openResult=session.getOpenResult();
					}else{
						int num=0;
						while (true) {
							String r=(new Random().nextInt(4)+1)+"";
							if(smap.get(r)==null){
								if(r.equals("1")){
									smap.put(r, htWin);
								}else if(r.equals("2")){
									smap.put(r, rtWin);
								}else if(r.equals("3")){
									smap.put(r, mfWin);
								}else if(r.equals("4")){
									smap.put(r, dwWin);
								}
								num++;
							}
							if(num==4){
								break;
							}			
						}
						
						String maxIndex=(new Random().nextInt(4)+1)+"";
						String minIndex=maxIndex;
						int max=smap.get(maxIndex);
						int min=smap.get(minIndex);
						for(String key:smap.keySet()){
							int tmp=smap.get(key);
							if(tmp>max){
								 	max=tmp;
								 	maxIndex=key;
							 }
							 if(tmp<min){
								 	min=tmp;
								 	minIndex=key;
							}
						}
						
						
						
//						list.add(htWin);
//						list.add(rtWin);
//						list.add(mfWin);
//						list.add(fpWin);
////						Collections.shuffle(list);
//		//				list.add(dwWin);
////						int max=0;
////						int min=0;
//						int minindex=0;
//						int maxindex=0;
//						for(int i=0;i<list.size();i++){
//							map.put(""+i, ""+i);
//							 if(i==0){
//								    max=list.get(0);
//								    min=list.get(0);
//							 }
//							 int tmp=list.get(i);
//							 if(tmp>max){
//								 	max=tmp;
//								 	maxindex=i;
//							 }
//							 if(tmp<min){
//								 	min=tmp;
//								 	minindex=i;
//							}
//						}
						
						
						
						if("0".equals(randomList.get(index))){
								if(dwWin<=min){
									if(new Random().nextInt(27)==9){
										openResult="55";
									}else{
										openResult=minIndex+"-"+GameConstants.getRandomOpenHHMFResultValue();
									}
								}else{
									openResult=minIndex+"-"+GameConstants.getRandomOpenHHMFResultValue();
								}
						}else if("1".equals(randomList.get(index))){
							if(dwWin>=min&&dwWin<=max){
								if(new Random().nextInt(27)==7){
									openResult="55";
								}else{
									smap.remove(minIndex);
									smap.remove(maxIndex);
									while (true) {
										int r=new Random().nextInt(4)+1;
										if(smap.get(r+"")!=null){
//											if(r!=4){
												openResult=r+"-"+GameConstants.getRandomOpenHHMFResultValue();
//											}
											break;
										}
									}
								}
							}else{
								smap.remove(maxIndex);
								smap.remove(minIndex);
								while (true) {
									int r=new Random().nextInt(4)+1;
									if(smap.get(r+"")!=null){
										if(r!=4){
											openResult=r+"-"+GameConstants.getRandomOpenHHMFResultValue();
										}
										break;
									}
								}
							}			
						}else if("2".equals(randomList.get(index))){
							if(dwWin>=max){
								if(new Random().nextInt(27)==7){
									openResult="55";
								}else{
									openResult=maxIndex+"-"+GameConstants.getRandomOpenHHMFResultValue();
								}
							}else{
								openResult=maxIndex+"-"+GameConstants.getRandomOpenHHMFResultValue();
							}				
						}	
					}
					smap.clear();
				}else{
					openResult=session.getOpenResult();
				}
		}else if(level.equals(Constants.GAME_HHMF_LEVEL_GAO)){//高级
			
			List<String> randomList=new ArrayList<String>();
			for(int i=0;i<5;i++){
				randomList.add("1");
			}
			randomList.add("0");
			randomList.add("0");
			randomList.add("0");
			randomList.add("0");
			randomList.add("0");
			Collections.shuffle(randomList);
			int index=new Random().nextInt(10);
			
			if("1".equals(randomList.get(index))){
				List<Integer> list=new ArrayList<Integer>();
				HQUtils hq=new HQUtils( " from GaHhmfBetOption ho order by ho.optionType ");
				List optionList=gaHhmfDAO.findObjects(hq);
				
//				int htWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), Constants.GAME_HHMF_HEITAO)).multiply(new BigDecimal(2.8)).intValue();
//				int rtWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), Constants.GAME_HHMF_HONGTAO)).multiply(new BigDecimal(2.8)).intValue();
//				int mfWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), Constants.GAME_HHMF_MEIHUA)).multiply(new BigDecimal(3)).intValue();
//				int fpWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), Constants.GAME_HHMF_FANGKUAI)).multiply(new BigDecimal(3)).intValue();
//				int dwWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), "5")).multiply(new BigDecimal(19)).intValue();
				
				int htWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
						Constants.GAME_HHMF_HEITAO)).multiply(((GaHhmfBetOption) optionList.get(0)).getBetRate().subtract(new BigDecimal(1))).intValue();

				int rtWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
						Constants.GAME_HHMF_HONGTAO)).multiply(((GaHhmfBetOption) optionList.get(1)).getBetRate().subtract(new BigDecimal(1))).intValue();

				int mfWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
						Constants.GAME_HHMF_MEIHUA)).multiply(((GaHhmfBetOption) optionList.get(2)).getBetRate().subtract(new BigDecimal(1))).intValue();

				int fpWin=new BigDecimal(gaHhmfDAO.getBetPointCountBySessionIdAndBetType(session.getSessionId(), 
						Constants.GAME_HHMF_FANGKUAI)).multiply(((GaHhmfBetOption) optionList.get(3)).getBetRate().subtract(new BigDecimal(1))).intValue();
				list.add(htWin);
				list.add(rtWin);
				list.add(mfWin);
				list.add(fpWin);
//				list.add(dwWin);
				int min=0;
				int minindex=0;
				for(int i=0;i<list.size();i++){
					 if(i==0){
						    min=list.get(0);
					 }
					 int tmp=list.get(i);
					 if(tmp<min){
						 	min=tmp;
						 	minindex=i;
					}
				}
				if(minindex!=4){
					openResult=(minindex+1)+"-"+GameConstants.getRandomOpenHHMFResultValue();
				}
			}else{
				openResult=session.getOpenResult();
			}
			
			
//			else{
//				openResult="55";
//			}
		}
		return openResult;
	}
	
	
	
	public void saveUserBet(JSONObject map,GaHhmfSession currentSession,Integer uid,String betType,String betPoints,Date now){

		JSONObject data = new JSONObject();// 返回数据层
		
		HQUtils hq=new HQUtils(" from GaHhmfBetDetail ho where 1=1 ");
		hq.addHsql(" and ho.sessionId=? ");
		hq.addPars(currentSession.getSessionId());
		hq.addHsql(" and ho.betType=? ");
		hq.addPars(betType);
		hq.addHsql(" and ho.userId=? ");
		hq.addPars(uid);
		
		Object object= gaHhmfDAO.getObject(hq);
		GaHhmfBetDetail betDetail=null;
		int betDetailPoint=0;
		if(object!=null){
			
			betDetail=(GaHhmfBetDetail) object;
			if(Constants.GAME_HHMF_WANG.equals(betDetail.getBetType())){
			
				if(betDetail.getBetPoint()<=(Constants.POINT_HHMF_MAX_WANG-Constants.POINT_HHMF_MAX_EVERY_BET)){
					betDetailPoint=betDetail.getBetPoint();
					betDetail.setBetTime(now);
					betDetail.setWinResult("0");
					betDetail.setWinCash(new BigDecimal(0));
					betDetail.setBetPoint(betDetail.getBetPoint()+Integer.parseInt(betPoints));
					GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, betDetail.getBetId());
					bet.setTotalPoint(bet.getTotalPoint().add(new BigDecimal(betPoints)));
					bet.setBetTime(now);
					gaHhmfDAO.saveObject(bet, null);
					gaHhmfDAO.saveObject(betDetail, null);	
					
					if(betDetail.getBetDetailId()!=null){
						GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
						if(betDetailPoint!=newDetail.getBetPoint()){
							int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
							currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
							currentSession.setSessionCash(0);
							gaHhmfDAO.saveObject(currentSession, null);
//							userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
							data.put("type", betType);
							data.put("points", betPoints);
//							code = APIConstants.CODE_REQUEST_SUCCESS;
//							message="投注成功";
							map.put("data", data);
							map.put("code",  APIConstants.CODE_REQUEST_SUCCESS);
							map.put("msg", "投注成功");
							return;
						}else{
//							code = APIConstants.CODE_REQUEST_ERROR;
//							message="投注失败";
							map.put("code",  APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "投注失败");
							return;
						}
					}else{
//						code = APIConstants.CODE_REQUEST_ERROR;
//						message="投注失败";
						map.put("code",  APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "投注失败");
						return;
					}
					
				}else{
//					code = APIConstants.CODE_REQUEST_ERROR;
//					message="超过最大投注限额";
					map.put("code",  APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "超过最大投注限额");
					return;
				}
			}else{
				System.out.println(betDetail.getBetPoint()+"<<<<<<<<<<<------------------------points");
				if(betDetail.getBetPoint()<=(Constants.POINT_HHMF_MAX_HHMF-Constants.POINT_HHMF_MAX_EVERY_BET)){
					betDetailPoint=betDetail.getBetPoint();
					betDetail.setBetTime(now);
					betDetail.setWinResult("0");
					betDetail.setWinCash(new BigDecimal(0));
					betDetail.setBetPoint(betDetail.getBetPoint()+Integer.parseInt(betPoints));
					GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, betDetail.getBetId());
					bet.setTotalPoint(bet.getTotalPoint().add(new BigDecimal(betPoints)));
					bet.setBetTime(now);
					gaHhmfDAO.saveObject(bet, null);
					gaHhmfDAO.saveObject(betDetail, null);	
					
					if(betDetail.getBetDetailId()!=null){
						GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
						if(betDetailPoint!=newDetail.getBetPoint()){
							int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
							currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
							currentSession.setSessionCash(0);
							gaHhmfDAO.saveObject(currentSession, null);
//							userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
							data.put("type", betType);
							data.put("points", betPoints);
//							code = APIConstants.CODE_REQUEST_SUCCESS;
//							message="投注成功";
							map.put("data", data);
							map.put("code",  APIConstants.CODE_REQUEST_SUCCESS);
							map.put("msg", "投注成功");
							return;
//							System.out.println("");
						}else{
//							code = APIConstants.CODE_REQUEST_ERROR;
//							message="投注失败";
							map.put("code",  APIConstants.CODE_REQUEST_ERROR);
							map.put("msg", "投注失败");
							return;
						}
					}else{
//						code = APIConstants.CODE_REQUEST_ERROR;
//						message="投注失败";
						map.put("code",  APIConstants.CODE_REQUEST_ERROR);
						map.put("msg", "投注失败");
						return;
					}
				}else{
//					code = APIConstants.CODE_REQUEST_ERROR;
//					message="超过最大投注限额";
					map.put("code",  APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "超过最大投注限额");
					return;
				}
			}			
		}else{
			GaHhmfBet bet=new GaHhmfBet();
			bet.setBetTime(now);
			bet.setSessionId(currentSession.getSessionId());
			bet.setUserId(uid);
			bet.setTotalPoint(new BigDecimal(betPoints));
			gaHhmfDAO.saveObject(bet, null);	
//						bet.setTotalNum(totalNum);
			betDetail=new GaHhmfBetDetail();
			betDetail.setBetTime(now);
			betDetail.setWinResult("0");
			betDetail.setWinCash(new BigDecimal(0));
			betDetail.setBetPoint(Integer.parseInt(betPoints));
			betDetail.setBetType(betType);
			betDetail.setUserId(uid);
			betDetail.setSessionId(currentSession.getSessionId());
			betDetail.setBetId(bet.getBetId());

			GaHhmfBetOption odds=this.getGaHhmfOddsByType(betType);
			betDetail.setBetRate(odds.getBetRate());
			betDetail.setBetOptionId(odds.getBetOptionId());
			gaHhmfDAO.saveObject(betDetail, null);	
			
			if(betDetail.getBetDetailId()!=null){
				GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
				if(betDetailPoint!=newDetail.getBetPoint()){
					int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
					currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
					currentSession.setSessionCash(0);
					gaHhmfDAO.saveObject(currentSession, null);
//					userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
					data.put("type", betType);
					data.put("points", betPoints);
//					code = APIConstants.CODE_REQUEST_SUCCESS;
//					message="投注成功";
					map.put("data", data);
					map.put("code",  APIConstants.CODE_REQUEST_SUCCESS);
					map.put("msg", "投注成功");
					return;
				}else{
//					code = APIConstants.CODE_REQUEST_ERROR;
//					message="投注失败";
					map.put("code",  APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "投注失败");
					return;
				}
			}else{
//				code = APIConstants.CODE_REQUEST_ERROR;
//				message="投注失败";
				map.put("code",  APIConstants.CODE_REQUEST_ERROR);
				map.put("msg", "投注失败");
				return;
			}
		}

	}
	
	
	public String saveUserBet(String code,String message,JSONObject data,GaHhmfSession currentSession,Integer uid,String betType,String betPoints,Date now){

//		JSONObject data = new JSONObject();// 返回数据层
		
		HQUtils hq=new HQUtils(" from GaHhmfBetDetail ho where 1=1 ");
		hq.addHsql(" and ho.sessionId=? ");
		hq.addPars(currentSession.getSessionId());
		hq.addHsql(" and ho.betType=? ");
		hq.addPars(betType);
		hq.addHsql(" and ho.userId=? ");
		hq.addPars(uid);
		
		Object object= gaHhmfDAO.getObject(hq);
		GaHhmfBetDetail betDetail=null;
		int betDetailPoint=0;
		if(object!=null){
			
			betDetail=(GaHhmfBetDetail) object;
			if(Constants.GAME_HHMF_WANG.equals(betDetail.getBetType())){
				if(betDetail.getBetPoint()<=(Constants.POINT_HHMF_MAX_WANG-Constants.POINT_HHMF_MAX_EVERY_BET)){
					betDetailPoint=betDetail.getBetPoint();
					betDetail.setBetTime(now);
					betDetail.setWinResult("0");
					betDetail.setBetPoint(betDetail.getBetPoint()+Integer.parseInt(betPoints));
					GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, betDetail.getBetId());
					bet.setTotalPoint(bet.getTotalPoint().add(new BigDecimal(betPoints)));
					bet.setBetTime(now);
					gaHhmfDAO.saveObject(bet, null);
					gaHhmfDAO.saveObject(betDetail, null);	
					
					if(betDetail.getBetDetailId()!=null){
						GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
						if(betDetailPoint!=newDetail.getBetPoint()){
							int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
							currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
							gaHhmfDAO.saveObject(currentSession, null);
//							userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
							data.put("type", betType);
							data.put("points", betPoints);
							code = APIConstants.CODE_REQUEST_SUCCESS;
							message="投注成功";
						}else{
							code = APIConstants.CODE_REQUEST_ERROR;
							message="投注失败";
						}
					}else{
						code = APIConstants.CODE_REQUEST_ERROR;
						message="投注失败";
					}
					
				}else{
					code = APIConstants.CODE_REQUEST_ERROR;
					message="超过最大投注限额";
				}
			}else{
				if(betDetail.getBetPoint()<=(Constants.POINT_HHMF_MAX_HHMF-Constants.POINT_HHMF_MAX_EVERY_BET)){
					betDetailPoint=betDetail.getBetPoint();
					betDetail.setBetTime(now);
					betDetail.setWinResult("0");
					betDetail.setBetPoint(betDetail.getBetPoint()+Integer.parseInt(betPoints));
					GaHhmfBet bet=(GaHhmfBet) gaHhmfDAO.getObject(GaHhmfBet.class, betDetail.getBetId());
					bet.setTotalPoint(bet.getTotalPoint().add(new BigDecimal(betPoints)));
					bet.setBetTime(now);
					gaHhmfDAO.saveObject(bet, null);
					gaHhmfDAO.saveObject(betDetail, null);	
					
					if(betDetail.getBetDetailId()!=null){
						GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
						if(betDetailPoint!=newDetail.getBetPoint()){
							int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
							currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
							gaHhmfDAO.saveObject(currentSession, null);
//							userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
							data.put("type", betType);
							data.put("points", betPoints);
							code = APIConstants.CODE_REQUEST_SUCCESS;
							message="投注成功";
//							System.out.println("");
						}else{
							code = APIConstants.CODE_REQUEST_ERROR;
							message="投注失败";
						}
					}else{
						code = APIConstants.CODE_REQUEST_ERROR;
						message="投注失败";
					}
				}else{
					code = APIConstants.CODE_REQUEST_ERROR;
					message="超过最大投注限额";
				}
			}			
		}else{
			GaHhmfBet bet=new GaHhmfBet();
			bet.setBetTime(now);
			bet.setSessionId(currentSession.getSessionId());
			bet.setUserId(uid);
			bet.setTotalPoint(new BigDecimal(betPoints));
			gaHhmfDAO.saveObject(bet, null);	
//						bet.setTotalNum(totalNum);
			betDetail=new GaHhmfBetDetail();
			betDetail.setBetTime(now);
			betDetail.setWinResult("0");
			betDetail.setBetPoint(Integer.parseInt(betPoints));
			betDetail.setBetType(betType);
			betDetail.setUserId(uid);
			betDetail.setSessionId(currentSession.getSessionId());
			betDetail.setBetId(bet.getBetId());

			GaHhmfBetOption odds=this.getGaHhmfOddsByType(betType);
			betDetail.setBetRate(odds.getBetRate());
			betDetail.setBetOptionId(odds.getBetOptionId());
			gaHhmfDAO.saveObject(betDetail, null);	
			
			if(betDetail.getBetDetailId()!=null){
				GaHhmfBetDetail newDetail=(GaHhmfBetDetail) gaHhmfDAO.getObject(GaHhmfBetDetail.class, betDetail.getBetDetailId());
				if(betDetailPoint!=newDetail.getBetPoint()){
					int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
					currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
					gaHhmfDAO.saveObject(currentSession, null);
//					userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
					data.put("type", betType);
					data.put("points", betPoints);
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message="投注成功";
				}else{
					code = APIConstants.CODE_REQUEST_ERROR;
					message="投注失败";
				}
			}else{
				code = APIConstants.CODE_REQUEST_ERROR;
				message="投注失败";
			}
			
		}
//		System.out.println("code"+code+"message"+message);
		
		return code+"-"+message;
//		map.put("code", code);
//		map.put("msg", message);
//		map.put("data", data);
	}
	public void saveUserCancel(JSONObject map,GaHhmfSession currentSession,Integer uid){
		
		List<GaHhmfBetDetail> list=gaHhmfDAO.findUserBetDetailList(currentSession.getSessionId(),uid);

		GaHhmfBet gaHhmfbet=gaHhmfDAO.findGaHhmfBetBySessionIdAndUid(currentSession.getSessionId(),uid);
		if(list!=null&&list.size()>0&&gaHhmfbet!=null){
			BigDecimal betPoints=gaHhmfbet.getTotalPoint();
			gaHhmfDAO.deleteObject(GaHhmfBet.class, gaHhmfbet.getBetId(), null);
			gaHhmfDAO.updateObjectList(null, list);
			currentSession.setPointCount(currentSession.getPointCount()-betPoints.intValue());
			gaHhmfDAO.saveObject(currentSession, null);
			//返还用户积分
//			userService.savePointDetail(uid, Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_BET_HHMF, betPoints.intValue(), null);
			map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
			map.put("msg", "回收成功");
		}else{
			map.put("code", APIConstants.CODE_REQUEST_ERROR);
			map.put("msg", "本局还没有投注");
		}
		
	}
	
	public void saveUserContinueBet(JSONObject map,GaHhmfSession currentSession,Integer uid,String pitems,Date now){
		JSONObject data = new JSONObject();
		if(currentSession.getOpenStatus().equals(Constants.GAME_WIN_STATUS_NOOPEN)){
			JSONArray jsonArray = new JSONArray(pitems);
			JSONArray items = new JSONArray();// 数据集合
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				if(jsonObj.get("betType")!=null&&jsonObj.get("betPoints")!=null){
					String betType=(String) jsonObj.get("betType");
					String betPoints=(String) jsonObj.get("betPoints");
					if(Integer.parseInt(betPoints)!=0){
						JSONObject obj = new JSONObject();// 最外层
						GaHhmfBet bet=new GaHhmfBet();
						bet.setBetTime(now);
						bet.setSessionId(currentSession.getSessionId());
						bet.setUserId(uid);
						bet.setTotalPoint(new BigDecimal(betPoints));
						gaHhmfDAO.saveObject(bet, null);	
//						bet.setTotalNum(totalNum);
						GaHhmfBetDetail betDetail=new GaHhmfBetDetail();
						betDetail.setBetTime(now);
						betDetail.setWinResult(Constants.GAME_WIN_STATUS_NOOPEN);
						betDetail.setBetPoint(Integer.parseInt(betPoints));
						betDetail.setWinCash(new BigDecimal(0));
						betDetail.setBetType(betType);
						betDetail.setUserId(uid);
						betDetail.setSessionId(currentSession.getSessionId());
						betDetail.setBetId(bet.getBetId());
						
						GaHhmfBetOption odds=this.getGaHhmfOddsByType(betType);
						betDetail.setBetRate(odds.getBetRate());
						betDetail.setBetOptionId(odds.getBetOptionId());
						gaHhmfDAO.saveObject(betDetail, null);	
						
						int pointCount=(currentSession.getPointCount()==null?0:currentSession.getPointCount());
						currentSession.setPointCount(pointCount+Integer.parseInt(betPoints));
						currentSession.setSessionCash(0);
						gaHhmfDAO.saveObject(currentSession, null);
						obj.put("type",betType);//上期中奖号码	
						obj.put("points",betPoints);//上期中奖号码
						items.put(obj);
//						userService.savePointDetail(uid, Constants.TRADE_TYPE_PAY, Constants.POINT_TYPE_BET_HHMF, Integer.parseInt(betPoints), null);
						data.put("items", items);
//						code = APIConstants.CODE_REQUEST_SUCCESS;
//						message="续押成功";
						map.put("data", data);
						map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
						map.put("msg", "续押成功");
					}
				}else{
					map.put("code", APIConstants.CODE_REQUEST_ERROR);
					map.put("msg", "参数不正确！");
					return;
				}
			}
		}else{
			map.put("code", APIConstants.CODE_REQUEST_ERROR);
			map.put("msg", "不是投注时间");
		}
	}
}
