package com.game.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.Value;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.UserCacheUtil;
import com.apps.eff.dto.ValueConfig;
import com.apps.model.Param;
import com.apps.model.UserTradeDetail;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.dao.IGaDAO;
import com.game.model.DailyStatistics;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.UserBetCount;
import com.game.model.dto.GaDTO;
import com.game.model.dto.SpDetailDTO;
import com.game.model.dto.WinCoDTO;
import com.game.service.IGaService;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class GaServiceImpl  extends BaseService implements IGaService {
	private IGaDAO gaDAO;
	private IBaseDataService baseDataService;
	private IUserService userService;
	private IParamService paramService;
	public void setGaDAO(IGaDAO gadao) {
		gaDAO = gadao;
		super.dao = gaDAO;
	}
	
	public void setParamService(IParamService paramService) {
		this.paramService = paramService;
	}

	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public List<GaDTO> getOptionTypeList(String  gameType,String playType){
		return gaDAO.getOptionTypeList(gameType,playType);
	}
	
	public List<GaBetOption> getBetPanelOptionList(String gameType,
			String playType, String optionType, Integer userId) {
		return gaDAO.getBetPanelOptionList(gameType,playType,optionType,userId);
	}
	public List<GaBetOption> getGaBetOptionByIds(String ids){
		return gaDAO.getGaBetOptionByIds(ids);
	}
	public List<GaBetDetail> findGaBetDetailList(String hql,List<Object> para){
		return gaDAO.findGaBetDetailList(hql,para);
	}
	public GaDTO getGaDTO(String gameType,Date date){
		return gaDAO.getGaDTO(gameType,date);
	}
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,int statIndex,int pageNum){
		return gaDAO.findGaBetDetailList(hql, para, statIndex, pageNum);
	}
	
	public PaginationSupport findJcRecordlList(String hql, List<Object> para,int statIndex,int pageNum){
		return gaDAO.findJcRecordlList(hql, para, statIndex, pageNum);
	}
	
	public GaSessionInfo findGaSessionInfo(String gameType){
		return gaDAO.findGaSessionInfo(gameType);
	}
	public List<GaSessionInfo> findGaSessionInfoList(){
		return gaDAO.findGaSessionInfoList();
	}
	public List<GaSessionInfo> findGaSessionInfoList(String playCate){
		return gaDAO.findGaSessionInfoList(playCate);
	}
	public  void updateUserGains(){
//		HQUtils hq=new HQUtils(" from User u where u.userType='1' and u.status='1' ");
//		List<Object> list=gaDAO.findObjects(hq);
//		List<User> saveList=new ArrayList<User>();
//		if(list!=null&&list.size()>0){
//			for(int i=0;i<list.size();i++){
//				User user=(User) list.get(i);
//				user.setUserBalance(new BigDecimal(0));
//				saveList.add(user);
//			}
//			gaDAO.updateObjectList(saveList, null);
//		}
		gaDAO.updateUserInfo();
		
	}
	public void updateDayBetCount(String statDay){
		Date now = DateTimeUtil.getNow();//当前时间
		if(!ParamUtils.chkString(statDay)){
			//统计昨天 定时器时启动为0点1秒
			statDay = DateTimeUtil.dateToString(DateTimeUtil.getDateTimeOfMinutes(now, -5), "yyyy-MM-dd");
		}
		Date startDate = DateTimeUtil.parse(statDay+" 00:00:00");
		Date endDate = DateTimeUtil.parse(statDay+" 23:59:59");
		Integer dateFlag = Integer.valueOf(DateTimeUtil.dateToString(startDate, "yyMMdd"));
		
		GameHelpUtil.log("range",statDay,0);
		
		long sTing = System.currentTimeMillis();
		//---------gfxy--由于订单不在同一张，改为统计资金明细表-----------------------
		BigDecimal dayBetMoney = new BigDecimal(0);//总投注金额
		BigDecimal dayWinMoney = new BigDecimal(0);//总派彩
		BigDecimal dayOffMoney = new BigDecimal(0);//盈亏
		
		HQUtils hq = new HQUtils();
		hq.setSelect("select sum(utd.cashMoney) ");
		hq.addHsql("from UserTradeDetail utd where utd.type=? and utd.cashType=? and utd.createTime>=? and utd.createTime<=? ");
		hq.addPars(Constants.USER_TYPE_SUER);//只统计会员
		hq.addPars(Constants.CASH_TYPE_CASH_BUY_LOTO);
		hq.addPars(startDate);
		hq.addPars(endDate);
		Object betObj = gaDAO.getObject(hq);
		//投注
		if(betObj!=null) dayBetMoney = GameHelpUtil.round(new BigDecimal(betObj.toString()).multiply(new BigDecimal(-1)));
		
		hq = new HQUtils();
		hq.setSelect("select sum(utd.cashMoney) ");
		hq.addHsql("from UserTradeDetail utd where utd.type=? and utd.cashType=? and utd.createTime>=? and utd.createTime<=? ");
		hq.addPars(Constants.USER_TYPE_SUER);//只统计会员
		hq.addPars(Constants.CASH_TYPE_CASH_PRIZE);
		hq.addPars(startDate);
		hq.addPars(endDate);
		betObj = gaDAO.getObject(hq);
		//派彩
		if(betObj!=null) dayWinMoney = GameHelpUtil.round(new BigDecimal(betObj.toString()));
		//盈亏
		dayOffMoney = GameHelpUtil.round(dayBetMoney.subtract(dayWinMoney));
		
		GameHelpUtil.log("dayBetMoney",dayBetMoney,0);
		GameHelpUtil.log("dayWinMoney",dayWinMoney,0);
		GameHelpUtil.log("dayOffMoney",dayOffMoney,0);
		
		//保存
		GaDayBetCount betCount = null;
		//检测重复
		hq = new HQUtils("from GaDayBetCount ubc where ubc.dateFlag=?");
		hq.addPars(dateFlag);
		Object ubc = gaDAO.getObject(hq);
		betCount = ubc!=null?(GaDayBetCount)ubc:new GaDayBetCount();
		
		betCount.setBetMoney(dayBetMoney);
		betCount.setWinMoney(dayWinMoney);
		betCount.setPayoff(dayOffMoney);
		betCount.setCreateTime(now);
		betCount.setDateFlag(dateFlag);
		gaDAO.saveObject(betCount);
		
		GameHelpUtil.log("end",dateFlag,sTing);
		
		//------------------dadi----------------------------------
//		Date date=DateTimeUtil.getNowSQLDate();
//		String startDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -5))+" 00:00:00";
//		String endDate=DateTimeUtil.DateToString(DateTimeUtil.getDateTimeOfMinutes(date, -5))+" 23:59:59";//DateTimeUtil.getDateTimeOfMinutes(date, -5)
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
////		hqls.append(" and ho.betTime>= ? ");
////		para.add(startDate);
//		hqls.append(" and ho.betTime <= ? ");
//		para.add(endDate);
//		hqls.append(" and( ho.countStatus is null or ho.countStatus != ?)");
//		para.add("1");
//		hqls.append(" and ho.betFlag =?  ");
//		para.add("1");
//		hqls.append(" and ho.winResult != ?  ");
//		para.add(Constants.OPEN_STATUS_INIT); //已开奖
////		List<BaseDataDTO> list = baseDataService.findTotalBet(hqls.toString(), para);
//		
//		List<GaBetDetail> dlist = gaDAO.findGaBetDetailList(hqls.toString(), para);
//		List<GaBetDetail>  savelist=new ArrayList<GaBetDetail>();
//		GaDayBetCount betCount=new GaDayBetCount();
//	
//		BigDecimal betMoney=new BigDecimal(0);
//		BigDecimal winMoney=new BigDecimal(0);
//		if(dlist!=null&&dlist.size()>0){
//			for(GaBetDetail detail:dlist){
//				detail.setCountStatus("1");
//				savelist.add(detail);
//				betMoney=betMoney.add(new BigDecimal(detail.getBetMoney()));
//				winMoney=winMoney.add(detail.getWinCash());	
//			}
//			betCount.setBetMoney(betMoney);
//			betCount.setPayoff(betMoney.subtract(winMoney));
//			betCount.setWinMoney(winMoney);
//			betCount.setCreateTime(DateTimeUtil.getDateTimeOfMinutes(date, -5));
//		}else{
//			betCount.setBetMoney(new BigDecimal(0));
//			betCount.setPayoff(new BigDecimal(0));
//			betCount.setWinMoney(new BigDecimal(0));
//			betCount.setCreateTime(DateTimeUtil.getDateTimeOfMinutes(date, -5));
//		}
//		
//		gaDAO.saveObject(betCount);
//		gaDAO.updateObjectList(savelist, null);
		
	}
	public void updateUserBetCount(String statDay){
		Date now = DateTimeUtil.getNow();//当前时间
		if(!ParamUtils.chkString(statDay)){
			//统计昨天 定时器时启动为0点1秒
			statDay = DateTimeUtil.dateToString(DateTimeUtil.getDateTimeOfMinutes(now, -5), "yyyy-MM-dd");
		}
		Date startDate = DateTimeUtil.parse(statDay+" 00:00:00");
		Date endDate = DateTimeUtil.parse(statDay+" 23:59:59");
		Integer dateFlag = Integer.valueOf(DateTimeUtil.dateToString(startDate, "yyMMdd"));
		
		GameHelpUtil.log("range",statDay,0);
		
		long sTing = System.currentTimeMillis();
		//---------gfxy--由于订单不在同一张，改为统计资金明细表-----------------------
		HQUtils hq = new HQUtils();
		hq.setSelect("select new com.game.model.dto.GaDTO(utd.userId,sum(utd.cashMoney),u.agentId) ");
		hq.addHsql("from UserTradeDetail utd,User u where utd.userId=u.userId ");
		hq.addHsql("and utd.type=? and utd.cashType=? and utd.createTime>=? and utd.createTime<=? ");
		hq.addPars(Constants.USER_TYPE_SUER);//只统计会员
		hq.addPars(Constants.CASH_TYPE_CASH_BUY_LOTO);
		hq.addPars(startDate);
		hq.addPars(endDate);
		hq.setGroupby("group by utd.userId");
		List<Object> list = gaDAO.findObjects(hq);
		GameHelpUtil.log("find",list.size(),sTing);
		
		List<UserBetCount> saveList = new ArrayList<UserBetCount>();
		
		if(list!=null && !list.isEmpty()){
			BigDecimal betMoney = null;
			Integer userId = null;
			Integer agentId = null;
			for(Object obj:list){
				GaDTO dto = (GaDTO)obj;
				userId = dto.getUserId();
				agentId = dto.getAgentId();
				betMoney = dto.getBetMoneyBig();
				if(betMoney==null) betMoney = new BigDecimal(0);
				
				UserBetCount userBetCount = null;
				//检测重复
				hq = new HQUtils("from UserBetCount ubc where ubc.userId=? and ubc.dateFlag=?");
				hq.addPars(dto.getUserId());
				hq.addPars(dateFlag);
				Object ubc = gaDAO.getObject(hq);
				userBetCount = ubc!=null?(UserBetCount)ubc:new UserBetCount();
				
				userBetCount.setAgentStatus(Constants.PUB_STATUS_CLOSE);//代理返水状态
				userBetCount.setPersonStatus(Constants.PUB_STATUS_CLOSE);//个人返水状态
				userBetCount.setCreateTime(now);//按实际时间
				userBetCount.setDateFlag(dateFlag);//统计日期标识yyMMdd
				userBetCount.setTotalMoney(GameHelpUtil.round(betMoney.multiply(new BigDecimal(-1))));
				userBetCount.setUserId(userId);
				userBetCount.setAgentId(ParamUtils.chkInteger(agentId)?agentId:0);
				saveList.add(userBetCount);
				gaDAO.updateObjectList(saveList, null);
			}
		}
		GameHelpUtil.log("end",dateFlag,sTing);
		
		//-------------dadi--------------------------------
//		List<Object> para = new ArrayList<Object>();
//		StringBuffer hqls = new StringBuffer();
//		hqls.append(" and ho.betTime>= ? ");
//		para.add(startDate);
//		hqls.append(" and ho.betTime <= ? ");
//		para.add(endDate);
//		hqls.append(" and ho.betFlag =?  ");
//		para.add("1");
//		hqls.append(" group by ho.userId  ");
//		//今天投注的人记录
//		List<GaDTO> list=gaDAO.findUserBetCountList(hqls.toString(), para);
//		List<UserBetCount> saveList=new  ArrayList<UserBetCount>();
//		if(list!=null&&list.size()>0){
//			for(GaDTO dto:list){
//				UserBetCount  userBetCount=new UserBetCount();
//				userBetCount.setAgentStatus("0");
//				userBetCount.setPersonStatus("0");
//				userBetCount.setCreateTime(DateTimeUtil.getDateTimeOfMinutes(date, -5));
//				userBetCount.setTotalMoney(new BigDecimal(dto.getBetMoney()));
//				userBetCount.setUserId(dto.getUserId());
//				userBetCount.setAgentId(dto.getAgentId());
//				saveList.add(userBetCount);
//			}
//			gaDAO.updateObjectList(saveList, null);
//		}
		//----------------------------------------------
	}
	
	/**
	 * 静态化wap版网页
	 * @return
	 */
	public String updateStaticHtml(){
		updateStaticHtmlOfWap();
		updateStaticHtmlOfPc();
		return "success";
	}
	public String updateStaticHtmlOfWap(){
		String rootPath = Constants.getWebRootPath();
		String htmlPath = rootPath+"/game/_template";
		String _htmlData = "";//模板html内容
		String pagePath = "";//待生成页面
		
		//更新首页
		pagePath = htmlPath+"/index.html";
		_htmlData = ManageFile.loadTextFileUTF8(pagePath);
		_htmlData = _htmlData.replace("{#site.title#}",Constants.SITE_TITLE);
		_htmlData = _htmlData.replace("{#site.name#}",Constants.SITE_NAME);
		_htmlData = _htmlData.replace("{#site.domain#}",Constants.SITE_DOMAIN);
		_htmlData = _htmlData.replace("{#xyList#}", getStaticHomeHtmlOfXY());
		_htmlData = _htmlData.replace("{#gfList#}", getStaticHomeHtmlOfGF());
		ManageFile.writeUTF8File(pagePath.replace("/_template", ""),_htmlData);//更新
		
		//更新首页
		pagePath = htmlPath+"/xiazhu.html";
		_htmlData = ManageFile.loadTextFileUTF8(pagePath);
		_htmlData = _htmlData.replace("{#xyList#}", getStaticBetHomeHtmlOfXY());
		_htmlData = _htmlData.replace("{#gfList#}", getStaticBetHomeHtmlOfGF());
		ManageFile.writeUTF8File(pagePath.replace("/_template", ""),_htmlData);//更新
		
		return "success";
	}
	/**
	 * 静态化pc版网页
	 * @return
	 */
	public String updateStaticHtmlOfPc(){
		String rootPath = Constants.getWebRootPath();
		String htmlPath = rootPath+"/_template";
		String _htmlData = "";//模板html内容
		String pagePath = "";//待生成页面
		
		//更新首页
		pagePath = htmlPath+"/index.html";
		_htmlData = ManageFile.loadTextFileUTF8(pagePath);
		_htmlData = _htmlData.replace("{#site.title#}",Constants.SITE_TITLE);
		_htmlData = _htmlData.replace("{#site.name#}",Constants.SITE_NAME);
		_htmlData = _htmlData.replace("{#site.domain#}",Constants.SITE_DOMAIN);
		ManageFile.writeUTF8File(pagePath.replace("/_template", ""),_htmlData);//更新
		
		return "success";
	}
	
	public String getStaticHomeHtmlOfXY(){
		List<GaSessionInfo> list = gaDAO.findGaSessionInfoList(Constants.GAME_PLAY_CATE_XY);
		StringBuffer sb = new StringBuffer();
		for(Object obj:list){
			GaSessionInfo gsi = (GaSessionInfo)obj;
			sb.append("<li class=\"caizhong-cell\">");
			sb.append("<a href=\"/game/"+(Constants.getGFXYPre(gsi.getPlayCate()))+"/"+gsi.getGameCode()+"\">");
			sb.append("<div class=\"ico\">");
			sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\" src=\""+gsi.getImg()+"\">");
			sb.append("</div>");
			sb.append("<div class=\"name-box\"><div class=\"subtitle\">"+gsi.getExp()+"</div><div class=\"title\">"+gsi.getGameTitle()+"</div></div>");
			sb.append("</a>");
			sb.append("</li>");
		}
		return sb.toString();
	}
	public String getStaticHomeHtmlOfGF(){
		List<GaSessionInfo> list = gaDAO.findGaSessionInfoList(Constants.GAME_PLAY_CATE_GF);
		StringBuffer sb = new StringBuffer();
		for(Object obj:list){
			GaSessionInfo gsi = (GaSessionInfo)obj;
			sb.append("<li class=\"caizhong-cell\">");
			sb.append("<a href=\"/game/"+(Constants.getGFXYPre(gsi.getPlayCate()))+"/"+gsi.getGameCode()+"\">");
			sb.append("<div class=\"ico\">");
			sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\" src=\""+gsi.getImg()+"\">");
			sb.append("</div>");
			sb.append("<div class=\"name-box\"><div class=\"subtitle\">"+gsi.getExp()+"</div><div class=\"title\">"+gsi.getGameTitle()+"</div></div>");
			sb.append("</a>");
			sb.append("</li>");
		}
		return sb.toString();
	}
	public String getStaticBetHomeHtmlOfXY(){
		List<GaSessionInfo> list = gaDAO.findGaSessionInfoList(Constants.GAME_PLAY_CATE_XY);
		StringBuffer sb = new StringBuffer();
		for(Object obj:list){
			GaSessionInfo gsi = (GaSessionInfo)obj;
			sb.append("<li class=\"mui-table-view-cell\" data=\""+gsi.getGameCode()+","+gsi.getGameType()+"\">");
			sb.append("<a href=\"/game/"+(Constants.getGFXYPre(gsi.getPlayCate()))+"/"+gsi.getGameCode()+"\" class=\"mui-navigate-right\">");
			sb.append("<div class=\"logo-box\">");
			sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\" src=\""+gsi.getImg()+"\">");
			sb.append("</div>");
			sb.append("<div class=\"title-box\"><div class=\"title\">"+gsi.getGameTitle()+"</div><div class=\"subtitle\" id=\"sbtitle_"+gsi.getGameType()+"\">第"+gsi.getLatestSessionNo()+"期</div><div id=\"openNum_"+gsi.getGameType()+"\"></div></div>");
			sb.append("<span class=\"timer-show\" id=\"timerShow_"+gsi.getGameType()+"\"></span>");
			sb.append("</a>");
			sb.append("</li>");
		}
		return sb.toString();
	}
	public String getStaticBetHomeHtmlOfGF(){
		List<GaSessionInfo> list = gaDAO.findGaSessionInfoList(Constants.GAME_PLAY_CATE_GF);
		StringBuffer sb = new StringBuffer();
		for(Object obj:list){
			GaSessionInfo gsi = (GaSessionInfo)obj;
			sb.append("<li class=\"mui-table-view-cell\" data=\""+gsi.getGameCode()+","+gsi.getGameType()+"\">");
			sb.append("<a href=\"/game/"+(Constants.getGFXYPre(gsi.getPlayCate()))+"/"+gsi.getGameCode()+"\" class=\"mui-navigate-right\">");
			sb.append("<div class=\"logo-box\">");
			sb.append("<img class=\"lazy\" data-original=\""+gsi.getImg()+"\" src=\""+gsi.getImg()+"\">");
			sb.append("</div>");
			sb.append("<div class=\"title-box\"><div class=\"title\">"+gsi.getGameTitle()+"</div><div class=\"subtitle\" id=\"sbtitle_"+gsi.getGameType()+"\">第"+gsi.getLatestSessionNo()+"期</div><div id=\"openNum_"+gsi.getGameType()+"\"></div></div>");
			sb.append("<span class=\"timer-show\" id=\"timerShow_"+gsi.getGameType()+"\"></span>");
			sb.append("</a>");
			sb.append("</li>");
		}
		return sb.toString();
	}

	public boolean saveGfDrawback(Integer sessionId,String gameType){

		try{
			List<GaBetSponsor> spList = new ArrayList<GaBetSponsor>();
			List<User> uList = new ArrayList<User>();
			List<UserTradeDetail> trList = new ArrayList<UserTradeDetail>();
			
			List<Object> para = new ArrayList<Object>();
			StringBuffer hqls = new StringBuffer();
			hqls.append(" and ho.sessionId=? ");
			para.add(sessionId);
			hqls.append(" and ho.betFlag=? " );
			para.add(Constants.PUB_STATUS_OPEN);// 有效
			hqls.append(" and ho.gameType =? ");
			para.add(gameType);

			List<SpDetailDTO> list = this.findGaBetSponsorAndUserList(hqls.toString(), para);//发起购买表，查询当前期有效的发起者发起的购买订单

			if(list!=null && list.size()>=0){
				for(SpDetailDTO dto:list){
					GaBetSponsor sponsor =  dto.getGaBetSponsor();
					
					sponsor.setBetFlag(Constants.PUB_STATUS_CLOSE);
					sponsor.setWinResult(Constants.INVALID_REFUND);//投注无效，退款
					spList.add(sponsor);

					String isGuarantee = sponsor.getIsGuarantee();//是否保底
					if(Constants.SPONSOR.equals(sponsor.getBetType())&&Constants.GUARANTEE.equals(isGuarantee)){ //如果合买且保底
						User user = dto.getUser(); //发起者
						BigDecimal guaranteedMoney = sponsor.getGuaranteedMoney(); //保底金额
						
						BigDecimal userBal=ProductUtil.checkBigDecimal(user.getUserBalance());//用户今日收益
						BigDecimal userMoeny=ProductUtil.checkBigDecimal(user.getMoney());//用户余额
//						BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额						
						
						user.setUserBalance(userBal.add(guaranteedMoney));
						user.setMoney(userMoeny.add(guaranteedMoney)); //返还保底的钱
//						user.setAggregateBetMoney(aggregateBetMoney.subtract(guaranteedMoney));
						uList.add(user);
						
						// 2.保存明细
						StringBuilder remark = new StringBuilder();
						remark.append("订单").append(sponsor.getOrderNum()).append(",管理员退回保底款项; 金额:")
						    .append(guaranteedMoney).append("元");
						UserTradeDetail utd = new UserTradeDetail();
						utd.setUserId(user.getUserId());
						utd.setTradeType(Constants.TRADE_TYPE_INCOME);//收入
						utd.setCashType(Constants.CASH_TYPE_CASH_DRAW);//方案撤单
						utd.setCashMoney(guaranteedMoney);//金额
						utd.setCreateTime(DateTimeUtil.getNowSQLDate());
						utd.setRemark(remark.toString());
						utd.setRefId(sponsor.getJointId());
						utd.setUserMoney(user.getMoney());
						utd.setModelType(gameType);//投注退还
						trList.add(utd);
					}
					List<SpDetailDTO>  rlList = this.findGaBetPartListByJointId(sponsor.getJointId());
					if(rlList !=null&& rlList.size()>=0){
						for(SpDetailDTO paDto:rlList){
							GaBetPart betPart = paDto.getGaBetPart();
							User user= paDto.getUser();
							BigDecimal betMoney = betPart.getBetMoney();
							
							BigDecimal userBal=ProductUtil.checkBigDecimal(user.getUserBalance());//用户今日收益
							BigDecimal userMoeny=ProductUtil.checkBigDecimal(user.getMoney());//用户余额
//							BigDecimal aggregateBetMoney=ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
							
							user.setUserBalance(userBal.add(betMoney));
							user.setMoney(userMoeny.add(betMoney)); //返还投注底的钱
//							user.setAggregateBetMoney(aggregateBetMoney.subtract(betMoney));
							uList.add(user);
							
							// 2.保存明细
							StringBuilder remark = new StringBuilder();
							remark.append("订单").append(sponsor.getOrderNum()).append(",管理员退回投注款;金额:")
							    .append(betMoney).append("元");
							UserTradeDetail utd = new UserTradeDetail();
							utd.setUserId(user.getUserId());
							utd.setTradeType(Constants.TRADE_TYPE_INCOME);//收入
							utd.setCashType(Constants.CASH_TYPE_CASH_DRAW);//方案撤单
							utd.setCashMoney(betMoney);//金额
							utd.setCreateTime(DateTimeUtil.getNowSQLDate());
							utd.setRemark(remark.toString());
							utd.setRefId(betPart.getRid());
							utd.setUserMoney(user.getMoney());
							utd.setModelType(gameType);//投注退还
							trList.add(utd);
						}
					}
				}
			}
			gaDAO.updateObjectList(spList, null);
			gaDAO.updateObjectList(uList, null);
			gaDAO.updateObjectList(trList, null);
			return true;
		}catch (Exception e){
			return false;

		}

	}
	public boolean saveDrawback(Integer sessionId,String gameType){
		HQUtils hq=new HQUtils(" from GaBetDetail ga  where  1=1 and ga.betFlag ='1' ");
		hq.addHsql(" and ga.gameType= ? ");
		hq.addPars(gameType);
		hq.addHsql(" and ga.sessionId= ? ");
		hq.addPars(sessionId);
		List<Object> list=gaDAO.findObjects(hq);
		List<GaBetDetail> savelist=new ArrayList<GaBetDetail>();
		List<Integer> userIds = new ArrayList<Integer>();//--by.cuisy.20171209
		for(Object obj:list){
			GaBetDetail detail=(GaBetDetail) obj;
			BigDecimal betMoney=new BigDecimal(detail.getBetMoney());//投注金额
//			User user=(User) bjPk10DAO.getObject(User.class, detail.getUserId());
			StringBuilder remark = new StringBuilder();
			remark.append("退款 金额 ")
			    .append(betMoney).append("元");
			userService.saveTradeDetail(null, detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_DRAWBACK, betMoney, null, gameType, remark.toString());
//			userService.saveTradeDetail(null,detail.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_DRAWBACK, betMoney, null);
			detail.setBetFlag("0");//无效的投注
			savelist.add(detail);
			
			//--by.cuisy.20171209
			if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
				userIds.add(detail.getUserId());
			}
		}
		gaDAO.updateObjectList(savelist, null);
		
		//--by.cuisy.20171209
		userService.updateUserMoney(userIds);
		
		return true;
	}
	@Override
	public PaginationSupport findGaBetSponsor(String hql, List<Object> para,
			int statIndex, int pageNum) {
		return gaDAO.findGaBetSponsor(hql,para,statIndex,pageNum);
	}
	@Override
	public List<GaBetOption> findGaBetOptionByGameType(String gameType) {
		return gaDAO.findGaBetOptionByGameType(gameType);
	}
	@Override
	public GaBetSponsor findGaBetSponsorById(String id) {
		return gaDAO.findGaBetSponsorById(id);
	}
	@Override
	public User updateJointBet(User user, GaBetSponsor sp, BigDecimal betMoney,
			int buyNum2,int restNum,BigDecimal schedule) {
		GaBetPart pa =new GaBetPart();
		pa.setJointId(sp.getJointId());
		pa.setBetMoney(betMoney);
		pa.setBuyNum(buyNum2);
		pa.setUserId(user.getUserId());
		pa.setBuyTime(DateTimeUtil.getNowSQLDate());
		pa.setWinCash(new BigDecimal(0));
		pa.setWinPoint(new BigDecimal(0));
		pa.setBehavior(Constants.PARTICIPATOR);
		gaDAO.updateObject(pa, null);
		
//		BigDecimal aggregateBetMoney = ProductUtil.checkBigDecimal(user.getAggregateBetMoney());//累计投注金额
//		BigDecimal dayBetMoney = ProductUtil.checkBigDecimal(user.getDayBetMoney());//今日投注
//		
//		user.setAggregateBetMoney(aggregateBetMoney.add(betMoney));
//		user.setDayBetMoney(dayBetMoney.add(betMoney));

		
		StringBuilder remark = new StringBuilder();
		remark.append("订单:").append(sp.getOrderNum()).append(",购买彩票 扣款 ")
		    .append(betMoney).append("元");
		//生成交易明细
		user = userService.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_PAY, Constants.CASH_TYPE_CASH_SPONSOR, betMoney, pa.getRid(), Constants.GAME_TYPE_GF_DCB, remark.toString());
		
		sp.setRestNum(restNum);
		sp.setSchedule(schedule);
		if(restNum ==0){
			sp.setWinResult(Constants.INIT);
			
			String isGuarantee = sp.getIsGuarantee();
			if(Constants.GUARANTEE.equals(isGuarantee)){//保底
				int guaranteeNum = sp.getGuaranteedNum();
				BigDecimal guaranteeMoney = sp.getGuaranteedMoney();
				remark = new StringBuilder();
				remark.append("订单").append(sp.getOrderNum()).append(",退回保底款项份数:")
			    .append(guaranteeNum).append(";金额:").append(guaranteeMoney.toString()).append("元");
				String modelType =sp.getGameType();
				User temp=(User) userService.getObject(User.class, sp.getUserId());
				if(temp!=null){
					temp = userService.saveTradeDetail(temp, temp.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_GUA_BACK, guaranteeMoney, sp.getJointId(), modelType, remark.toString());
				}
			}
		}
		gaDAO.saveObject(sp);
		return user;
	}

	@Override
	public PaginationSupport findGaBetSponsorDetail(String hql,
			List<Object> para, int statIndex, int pageSize) {
		return gaDAO.findGaBetSponsorDetail(hql,para,statIndex,pageSize);
	}

	public List<WinCoDTO> findGaWinCountList(String gameType){
		return gaDAO.findGaWinCountList(gameType);
	}
	public List<SpDetailDTO>  findGaBetSponsorListByGameTypeAndBetType(String gameType,String betType){
		return gaDAO.findGaBetSponsorListByGameTypeAndBetType(gameType,betType);
	}
	public List<SpDetailDTO> findGaBetPartListByJointId(Integer jointId){
		return gaDAO.findGaBetPartListByJointId(jointId);
	}

	@Override
	public List<GaBetSponsorDetail> findGaBetSponsorDetailListByJointId(Integer jointId) {
		return gaDAO.findGaBetSponsorDetailListByJointId(jointId);
	}

	@Override
	public List<GaBetSponsor> findGaBetSponsorList(String hql, List<Object> para) {
		return gaDAO.findGaBetSponsorList(hql, para);
	}

	@Override
	public List<SpDetailDTO> findGaBetSponsorAndUserList(String hql,
			List<Object> para) {
		return gaDAO.findGaBetSponsorAndUserList(hql,para);
	}

	@Override
	public List<GaWinCount> findGaWinCountList(String hql, List<Object> para) {
		return gaDAO.findGaWinCountList(hql,para);
	}

	@Override
	public void updateUserDayWinAndBet() {
		HQUtils hq=new HQUtils(" from User u where u.userType='1' and u.status='1' ");
		List<Object> list=gaDAO.findObjects(hq);
		List<User> saveList=new ArrayList<User>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				User user=(User) list.get(i);
//				user.setDayWinMoney(new BigDecimal(0));
//				user.setDayBetMoney(new BigDecimal(0));
				saveList.add(user);
			}
			gaDAO.updateObjectList(saveList, null);
		}
	}

	@Override
	public void updateUserWeekWin() {
		gaDAO.updateUserWeekWin();
	}

	@Override
	public GaDayBetCount findDayBetCount(StringBuffer hqls, List<Object> para) {
		return gaDAO.findDayBetCount(hqls,para);
	}

	@Override
	public void updateUserDayWin() {
		gaDAO.updateUserDayWin();
	}
	
	public PaginationSupport findGaSessionInfoList(String hql,
			List<Object> para, int startIndex, int pageSize){
		return gaDAO.findGaSessionInfoList(hql, para, startIndex, pageSize);
	}
	@Override
	public void updateBj3GaSession(Date date) {
		gaDAO.updateBj3GaSession(date);
	}

	@Override
	public void updateMarkSixGaSession(Date date) {
		gaDAO.updateMarkSixGaSession(date);
	}

	@Override
	public void updateGdK10GaSession(Date date) {
		gaDAO.updateGdK10GaSession(date);
	}

	@Override
	public void updateBjLu28GaSession(Date date) {
		gaDAO.updateBjLu28GaSession(date);
		
	}

	@Override
	public void updateBjPk10GaSession(Date date) {
		gaDAO.updateBjPk10GaSession(date);		
	}

	@Override
	public void updateCqSscGaSession(Date date) {
		gaDAO.updateCqSscGaSession(date);		
	}

	@Override
	public void updateGdPick11GaSession(Date date) {
		gaDAO.updateGdPick11GaSession(date);		
	}

	@Override
	public void updateGxK10GaSession(Date date) {
		gaDAO.updateGxK10GaSession(date);		
	}

	@Override
	public void updateJsK3GaSession(Date date) {
		gaDAO.updateJsK3GaSession(date);		
	}

	@Override
	public void updatePokerGaSession(Date date) {
		gaDAO.updatePokerGaSession(date);		
	}

	@Override
	public void updateTjSscGaSession(Date date) {
		gaDAO.updateTjSscGaSession(date);		
	}

	@Override
	public void updateXjSscGaSession(Date date) {
		gaDAO.updateXjSscGaSession(date);		
	}

	@Override
	public void updateXjpLu28GaSession(Date date) {
		gaDAO.updateXjpLu28GaSession(date);		
	}

	@Override
	public void updateGaBetDetail(Date date) {
		gaDAO.updateGaBetDetail(date);				
	}

	@Override
	public PaginationSupport findDailyStatisticsList(String hql,
			List<Object> para, int startIndex, int pageSize) {
		return gaDAO.findDailyStatisticsList(hql,para,startIndex,pageSize);
	}

	@Override
	public PaginationSupport findDailyStatisticsList2(String hql,
			List<Object> para, int startIndex, int pageSize) {
		return gaDAO.findDailyStatisticsList2(hql,para,startIndex,pageSize);
	}

	@Override
	public void updateDailyStatistics(String statDay) {
		
		Date yesterdayStart = DateTimeUtil.getDateTimeOfDays(DateTimeUtil.getCurrentDayStart(), -1);//昨天
		Date yesterdayEnd = DateTimeUtil.getDateTimeOfDays(DateTimeUtil.getCurrentDayend(), -1);//昨天
		
		if(ParamUtils.chkString(statDay)){//传统计的日期yyyy-MM-dd
			yesterdayStart = DateTimeUtil.parse(statDay+" 00:00:00");
			yesterdayEnd = DateTimeUtil.parse(statDay+" 23:59:59");
		}
		
		GameHelpUtil.log("day",yesterdayStart+" ~ "+yesterdayEnd,0);
		
		StringBuffer hqls = new StringBuffer();
		List<Object> para = new ArrayList<Object>();
		hqls.append(" and u.status = ? ");
		para.add(Constants.PUB_STATUS_OPEN);
		hqls.append(" and u.userType = ? ");//会员
		para.add(Integer.valueOf(Constants.USER_TYPE_SUER));
		
		long sTing = System.currentTimeMillis();
		List<User> userList = userService.findUserList(hqls.toString(), para);
		GameHelpUtil.log("find user",userList.size(),sTing);
		
		StringBuffer hqls2 = new StringBuffer();
		List<Object> para2 = new ArrayList<Object>();
		hqls2.append(" and (ho.cashType = ? or ho.cashType = ? or ho.cashType = ? or ho.cashType = ? or ho.cashType = ? or ho.cashType = ? )");
		para2.add(Constants.CASH_TYPE_ONLINE);
		para2.add(Constants.CASH_TYPE_MANAGER_SET);
		para2.add(Constants.CASH_TYPE_CASH_SYS_CHARGE);
		para2.add(Constants.CASH_TYPE_CASH_OUT);
		para2.add(Constants.CASH_TYPE_CASH_BUY_LOTO);
		para2.add(Constants.CASH_TYPE_CASH_PRIZE);
		hqls2.append(" and ho.createTime >= ? ");
		para2.add(yesterdayStart);
		hqls2.append(" and ho.createTime <= ? ");
		para2.add(yesterdayEnd);
		hqls2.append(" group by u.userId,ho.cashType ");
		
		sTing = System.currentTimeMillis();
		List<GaDTO> rechargeList = gaDAO.findUserTradeDetailList(hqls2.toString(),para2);//总充值
		GameHelpUtil.log("find trade detail",rechargeList.size(),sTing);

		sTing = System.currentTimeMillis();
		List<DailyStatistics> saveList = new ArrayList<DailyStatistics>();
		DailyStatistics ds = null;
		for(User user: userList){
			Integer userId = user.getUserId();
			ds = new DailyStatistics();
			ds.setUserId(userId);
			ds.setUserName(user.getUserName());
			ds.setLoginName(user.getLoginName());
			ds.setAgentId(user.getAgentId());
			ds.setCreateTime(yesterdayEnd);
			ds.setTotalRecharge(new BigDecimal(0));
			ds.setTotalDrawMoney(new BigDecimal(0));
			ds.setTotalBet(new BigDecimal(0));
			ds.setTotalWin(new BigDecimal(0));
			for(GaDTO d:rechargeList){
				//log.info("--id--"+d.getUserId()+"---money--"+ d.getPaperMoney()+"---cashType--"+d.getCashType());

				if(userId.equals(d.getUserId())&& Constants.CASH_TYPE_CASH_OUT.equals(d.getCashType())){
					ds.setTotalDrawMoney(d.getPaperMoney());
//					rechargeList.remove(d);
//					log.info("---size--"+rechargeList.size());
				}else if(userId.equals(d.getUserId())&& Constants.CASH_TYPE_CASH_BUY_LOTO.equals(d.getCashType())){
					ds.setTotalBet(d.getPaperMoney());
//					rechargeList.remove(d);
//					log.info("---size--"+rechargeList.size());
				}else if(userId.equals(d.getUserId())&& Constants.CASH_TYPE_CASH_PRIZE.equals(d.getCashType())){
					ds.setTotalWin(d.getPaperMoney());
//					rechargeList.remove(d);
//					log.info("---size--"+rechargeList.size());
				}else if(userId.equals(d.getUserId())&& Constants.CASH_TYPE_ONLINE.equals(d.getCashType())){
					ds.setTotalRecharge(ds.getTotalDrawMoney().add(d.getPaperMoney()));
//					rechargeList.remove(d);
//					log.info("---size--"+rechargeList.size());
				}else if(userId.equals(d.getUserId())&& Constants.CASH_TYPE_MANAGER_SET.equals(d.getCashType())){
					ds.setTotalRecharge(ds.getTotalDrawMoney().add(d.getPaperMoney()));
//					rechargeList.remove(d);
//					log.info("---size--"+rechargeList.size());
				}
			}
			saveList.add(ds);
		}
		gaDAO.updateObjectList(saveList, null);
		GameHelpUtil.log("save","...",sTing);
	}

	@Override
	public List<GaDTO> findAgentStatisticsList(String hql,
			List<Object> para) {
		return gaDAO.findAgentStatisticsList(hql,para);
	}

	@Override
	public List<GaDTO> findUserSubmemberList(String hsql,
			List<Object> para) {
		return gaDAO.findUserSubmemberList(hsql,para);
	}
	@Override
	public List<GaDTO> findUserTradeDetailList(String hql, List<Object> para) {
		return gaDAO.findUserTradeDetailList(hql, para);
	}
	
	public String updateAgentBack(Date now){
		//从缓存中调用打码返水开关及设置
		ValueConfig config = CacheUtil.betBackConfig("agent");
		if(config==null || !config.isOpen()){
			GameHelpUtil.logErr("updateAgentBack.config err ...");
			return "err";
		}
		String execTime = config.getExecTime();
		String nowExecTime = DateTimeUtil.dateToString(now, "HH:mm");
		if(!execTime.equals(nowExecTime)){//小时分钟对上开始执行
			//GameHelpUtil.log("updateMemberBack not exec time["+execTime+"]");
			return "fail:";
		}
		GameHelpUtil.log("updateAgentBack start ... "+execTime);
		//上一天全部会员投注统计记录
		List<UserBetCount> list = gaDAO.findUserBetCountListForAgent(now);
		GameHelpUtil.log("find.UserBetCount",list.size(),0);
		if(list!=null && !list.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			User member;//会员
			User agent;//代理
			String isBack = Constants.PUB_STATUS_CLOSE;//会员单独返水开关[0,1]//默认不返
			BigDecimal betMoney;//投注总额
			BigDecimal backMoney;//返水的钱
			int okCount = 0;//成功返水
			int noCount = 0;//未返水(金额不够)
			int closeCount = 0;//关闭返水
			for(UserBetCount ubc:list){
				member = (User)gaDAO.getObject(User.class,ubc.getUserId());//会员
				agent = (User)gaDAO.getObject(User.class,ubc.getAgentId());//代理
				if(member!=null) isBack = member.getIsBetBack();//获取会员的个人返水开关
				if(isBack.equals(Constants.PUB_STATUS_OPEN)){//个人开关不返，则代理不返
					betMoney = ubc.getTotalMoney();//会员投注的总金额
					//获取返水的比例金额
					backMoney = GameHelpUtil.getBackMoney(betMoney,config);
					if(backMoney.compareTo(new BigDecimal(0))==1){//大于0则保存记录
						//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
						if(!StringUtil.chkListIntContains(userIds, ubc.getAgentId())){
							userIds.add(ubc.getAgentId());
						}//~
						
						StringBuffer remark = new StringBuffer();
						remark.append("ID："+ubc.getUserId()+"打码返代理("+ubc.getAgentId()+")：").append(backMoney);
						//保存明细数据
						updateBackData(agent,backMoney,remark.toString(),ubc);
						okCount++;
					}else{
						noCount++;
					}
				}else{
					closeCount++;
					GameHelpUtil.log("member back closed..."+ubc.getUserId()+"/"+ubc.getAgentId());
				}
			}
			//批量更新开奖用户实时余额 --by.cuisy.20171209
			userService.updateUserMoney(userIds);
			
			GameHelpUtil.log("back SUCCESS ok="+okCount+",no="+noCount+",close="+closeCount);
		}
		return "success";
	}
	
//	public void updateAgentBack_old(Date date){
//		String value=CacheUtil.getBetBack("agentBetBack");
//		String time=value.split("&")[0];
//		if(time.length()>=8){//15:00:00
//			time=time.substring(0, time.length()-3);
//		}
//		BigDecimal rate=new BigDecimal(value.split("&")[1]).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);
//		if(DateTimeUtil.dateToString(date, "HH:mm").equals(time)){
//			List<GaDTO> list=gaDAO.findUserBetCountList(date);
////			List<UserBetCount> countList=new ArrayList<UserBetCount>();
////			List<User> agentList=new ArrayList<User>();
////			List<UserTradeDetail> tlist=new ArrayList<UserTradeDetail>();
//			if(list!=null&&list.size()>0){
//				List<Integer> userIds = new ArrayList<Integer>();
//				log.info("updateAgentBack______"+list.size());
//				for(int i=0;i<list.size();i++){
//				
//					UserBetCount userBetCount=list.get(i).getUserBetCount();
//					
//					
//					//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//					if(!StringUtil.chkListIntContains(userIds, userBetCount.getAgentId())){
//						userIds.add(userBetCount.getAgentId());
//					}//~
//					
//					
//					User agent=(User) gaDAO.getObject(User.class, userBetCount.getAgentId());
//					BigDecimal totalBet=userBetCount.getTotalMoney();
//					StringBuffer remark=new StringBuffer();
//					remark.append("下级会员ID:"+userBetCount.getUserId()+"，代理ID:"+userBetCount.getAgentId()).append(",返水金额：").append(totalBet.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP)).append("元");
//					
//					BigDecimal recRevenue=agent.getRecRevenue();
//					if(recRevenue==null){
//						recRevenue=new BigDecimal(0);
//					}
//
//					agent.setRecRevenue(recRevenue.add(totalBet.multiply(rate)));
//
//					userBetCount.setAgentStatus("1");
//					updateBackData(agent,totalBet.multiply(rate),remark.toString(),userBetCount);
////					countList.add(userBetCount);
//				}
//
//				//批量更新开奖用户实时余额 --by.cuisy.20171209
//				userService.updateUserMoney(userIds);
////				userService.updateObjectList(countList, null);
//			}
//		}
//	}
	
	public void updateBackData(User agent,BigDecimal money,String remark,UserBetCount userBetCount){
//		userService.updateUserBetCountAgentStatus(userBetCount);
//		user=userService.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN, money, userBetCount.getCountId(), null,remark);
		
		//更新状态
		userBetCount.setAgentStatus(Constants.PUB_STATUS_OPEN);
		gaDAO.saveObject(userBetCount);
		//更新代理收益
		BigDecimal recRevenue = agent.getRecRevenue();
		if(recRevenue==null) recRevenue = new BigDecimal(0);
		agent.setRecRevenue(GameHelpUtil.round(recRevenue.add(money)));
		//更新明细
		userService.saveTradeDetail(agent,userBetCount.getAgentId(), Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN, money, userBetCount.getCountId(), null,remark);
	}
	
	public String updateMemberBack(Date now){
		//从缓存中调用打码返水开关及设置
		ValueConfig config = CacheUtil.betBackConfig("member");
		if(config==null || !config.isOpen()){
			GameHelpUtil.logErr("updateMemberBack.config err ...");
			return "err";
		}
		String execTime = config.getExecTime();
		String nowExecTime = DateTimeUtil.dateToString(now, "HH:mm");
		if(!execTime.equals(nowExecTime)){//小时分钟对上开始执行
			//GameHelpUtil.log("updateMemberBack not exec time["+execTime+"]");
			return "fail:";
		}
		GameHelpUtil.log("updateMemberBack start ... "+execTime);
		//上一天全部会员投注统计记录
		List<GaDTO> list = gaDAO.findUserBetCountList(now);
		GameHelpUtil.log("find.UserBetCount",list.size(),0);
		if(list!=null && !list.isEmpty()){
			List<Integer> userIds = new ArrayList<Integer>();
			UserBetCount ubc;
			String isBack;//会员单独返水开关[0,1]
			BigDecimal betMoney;//投注总额
			BigDecimal backMoney;//返水的钱
			int okCount = 0;//成功返水
			int noCount = 0;//未返水(金额不够)
			int closeCount = 0;//关闭返水
			for(GaDTO dto:list){
				isBack = dto.getIsBetBack();//获取会员的个人返水开关
				ubc = dto.getUserBetCount();
				if(isBack.equals(Constants.PUB_STATUS_OPEN)){
					betMoney = ubc.getTotalMoney();
					//获取返水的比例金额
					backMoney = GameHelpUtil.getBackMoney(betMoney,config);
					if(backMoney.compareTo(new BigDecimal(0))==1){//大于0则保存记录
						//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
						if(!StringUtil.chkListIntContains(userIds, ubc.getUserId())){
							userIds.add(ubc.getUserId());
						}//~
						
						StringBuffer remark = new StringBuffer();
						remark.append("ID："+ubc.getUserId()+"打码自返：").append(backMoney);
						//保存明细数据
						updateBackMemberData(null,backMoney,remark.toString(),ubc);
						okCount++;
					}else{
						noCount++;
					}
				}else{
					closeCount++;
					GameHelpUtil.log("member back closed..."+ubc.getUserId());
				}
			}
			//批量更新开奖用户实时余额 --by.cuisy.20171209
			userService.updateUserMoney(userIds);
			
			GameHelpUtil.log("back SUCCESS ok="+okCount+",no="+noCount+",close="+closeCount);
		}
		return "success";
	}

//	public void updateMemberBack(Date date){
//		String status=CacheUtil.getBetBack("memberBetBackStatus");// 个人返水总开关
//		if (Constants.PUB_STATUS_OPEN.equals(status)) {
//			String value=CacheUtil.getBetBack("memberBetBack");
//			String time=value.split("&")[0];
//			if(time.length()>=8){//15:00:00
//				time=time.substring(0, time.length()-3);
//			}
//			BigDecimal minMoney=new BigDecimal(value.split("&")[1]);
////			BigDecimal maxMoney=new BigDecimal(value.split("&")[2]);
//			BigDecimal rate=new BigDecimal(value.split("&")[3]).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);
//			
//			if(DateTimeUtil.dateToString(date, "HH:mm").equals(time)){
//				List<GaDTO> list=gaDAO.findUserBetDetailList(date);
////				List<User> agentList=new ArrayList<User>();
////				List<UserTradeDetail> tlist=new ArrayList<UserTradeDetail>();
////				List<UserBetCount> countList=new ArrayList<UserBetCount>();
//				log.info("updateMemberBack_____"+list.size());
//				if(list!=null&&list.size()>0){
//					List<Integer> userIds = new ArrayList<Integer>();
//					for(int i=0;i<list.size();i++){	
//						String isBetBack = list.get(i).getIsBetBack();// 每个用户打码返水开关
//						if (Constants.PUB_STATUS_OPEN.equals(isBetBack)) {
//							UserBetCount userBetCount=list.get(i).getUserBetCount();
//							
//							
//							BigDecimal totalBet=userBetCount.getTotalMoney();
//							if(totalBet.compareTo(minMoney)>-1){
//								//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
//								if(!StringUtil.chkListIntContains(userIds, userBetCount.getUserId())){
//									userIds.add(userBetCount.getUserId());
//								}//~
//								
//								User agent=(User) gaDAO.getObject(User.class, userBetCount.getUserId());	
//								StringBuffer remark=new StringBuffer();
//								remark.append("会员ID："+userBetCount.getUserId()+"，每日打码返水,返水金额：").append(totalBet.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP)).append("元");						
//								
////							userBetCount.setPersonStatus("1");
////							countList.add(userBetCount);
//								updateBackMemberData(agent,totalBet.multiply(rate),remark.toString(),userBetCount);
//							}else{
//								userService.updateUserBetCountPersonStatus(userBetCount,"2");
//							}
//						}
//					}
//					//批量更新开奖用户实时余额 --by.cuisy.20171209
//					userService.updateUserMoney(userIds);
////					userService.updateObjectList(countList, null);
//				}
//			}
//		}
//	}
	
	public void updateBackMemberData(User user,BigDecimal money,String remark,UserBetCount userBetCount){
		//userService.updateUserBetCountPersonStatus(userBetCount,"1");
		//更新状态
		userBetCount.setPersonStatus(Constants.PUB_STATUS_OPEN);
		gaDAO.saveObject(userBetCount);
		//更新明细
		userService.saveTradeDetail(user,userBetCount.getUserId(), Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF, money, userBetCount.getCountId(), null,remark);
	}
	public void saveSessionInfo(GaSessionInfo sessionInfo){
		
		Integer infoId=sessionInfo.getInfoId();
		if(ParamUtils.chkInteger(infoId)){
			GaSessionInfo temp=(GaSessionInfo) gaDAO.getObject(GaSessionInfo.class, infoId);
			temp.setKaicaiUrl(sessionInfo.getKaicaiUrl());
			temp.setCaipiaoUrl(sessionInfo.getCaipiaoUrl());
			temp.setUrlSwitch(sessionInfo.getUrlSwitch());
			gaDAO.updateObject(temp, null);		
		}else{
			sessionInfo.setStatus(Constants.PUB_STATUS_OPEN);
			gaDAO.saveObjectDB(sessionInfo);
		}
	}
	
	@Override
	public GaDTO getDetailEstimateMoney(String sessionNo, String gameType) {
		return gaDAO.getDetailEstimateMoney(sessionNo,gameType);
	}

	
	@Override
	public String getMinResult(String sessionNo, String gameType, String estimateResult, Integer openNum) {
		String result = "";
		if(ParamUtils.chkString(estimateResult)){
			String[]  array = estimateResult.split("\\|");
			if(ParamUtils.chkInteger(openNum)){
				GaDTO dto=this.getDetailEstimateMoney(sessionNo,gameType);
				if(dto!=null){
					BigDecimal oneMoney=dto.getOneMoney();
					BigDecimal twoMoney=dto.getTwoMoney();
					BigDecimal threeMoney=dto.getThreeMoney();
					BigDecimal fourMoney=dto.getFourMoney();
					BigDecimal fiveMoney=dto.getFiveMoney();
						
					GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"oneMoney:"+oneMoney+",twoMoney:"+twoMoney+",threeMoney:"+threeMoney+",fourMoney:"+fourMoney+",fiveMoney:"+fiveMoney);
		
					if(oneMoney==null){
						oneMoney=new BigDecimal(0);
					}
					BigDecimal minMoney=oneMoney;
					if(twoMoney==null){
						twoMoney=new BigDecimal(0);
					}
					if(threeMoney==null){
						threeMoney=new BigDecimal(0);
					}
					if(fourMoney==null){
						fourMoney=new BigDecimal(0);
					}
					if(fiveMoney==null){
						fiveMoney=new BigDecimal(0);
					}
					
					if(openNum==3){
						if(minMoney.compareTo(twoMoney)>0){				
							minMoney=twoMoney;	
						}
						if(minMoney.compareTo(threeMoney)>0){
							minMoney=threeMoney;
						}
						if(minMoney.compareTo(oneMoney)==0){
							result=array[0];
						}else  if(minMoney.compareTo(twoMoney)==0){
							result=array[1];
						}else  if(minMoney.compareTo(threeMoney)==0){
							result=array[2];
						}	
					}else if(openNum==5){
						if(minMoney.compareTo(twoMoney)>0){				
							minMoney=twoMoney;	
						}
						if(minMoney.compareTo(threeMoney)>0){
							minMoney=threeMoney;
						}			
						if(minMoney.compareTo(fourMoney)>0){
							minMoney=fourMoney;
						}
						if(minMoney.compareTo(threeMoney)>0){
							minMoney=fiveMoney;
						}
						if(minMoney.compareTo(oneMoney)==0){
							result=array[0];
						}else  if(minMoney.compareTo(twoMoney)==0){
							result=array[1];
						}else  if(minMoney.compareTo(threeMoney)==0){
							result=array[2];
						}else  if(minMoney.compareTo(fourMoney)==0){
							result=array[3];
						}else  if(minMoney.compareTo(fiveMoney)==0){
							result=array[4];
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public List<GaBetDetail> findGaBetDetailListByTradeId(Integer tradeDetailId) {
		List<GaBetDetail> list = new ArrayList<GaBetDetail>();
		
		UserTradeDetail utd = (UserTradeDetail)gaDAO.getObject(UserTradeDetail.class,tradeDetailId);
		if(utd!=null && utd.getCashType().equals(Constants.CASH_TYPE_CASH_PRIZE)){//中奖派彩 直接通过refId
			if(ParamUtils.chkInteger(utd.getRefId())){
				list.add((GaBetDetail)gaDAO.getObject(GaBetDetail.class,utd.getRefId()));
			}
		}else{
			List<Object> para = new ArrayList<Object>();
			StringBuffer hsql = new StringBuffer();
			hsql.append(" and rl.tradeDetailId = ? ");
			para.add(tradeDetailId);
			list = gaDAO.findGaBetDetailListByRL(hsql.toString(),para);
		}
		return list;
	}

	@Override
	public boolean saveXyRevokePrize(Integer sessionId, String gameType,
			String sessionNo) {
		HQUtils hq=new HQUtils(" from GaBetDetail ga  where  1=1 and ga.betFlag ='1' ");
		hq.addHsql(" and ga.gameType= ? ");
		hq.addPars(gameType);
		hq.addHsql(" and ga.sessionId= ? ");
		hq.addPars(sessionId);
		hq.addHsql(" and ga.winResult= ? ");
		hq.addPars(Constants.GAME_WIN_STATUS_WIN);//已中奖
		List<Object> list=gaDAO.findObjects(hq);
		List<Integer> userIds = new ArrayList<Integer>();//--by.cuisy.20171209

		for(Object obj:list){
			GaBetDetail detail=(GaBetDetail) obj;
			BigDecimal winCash = detail.getWinCash();//中奖金额
			StringBuilder remark = new StringBuilder();
			remark.append("彩派错误，撤回彩派金额 ").append(winCash).append("元");
			User user=UserCacheUtil.getUser(detail.getUserId());
			userService.saveTradeDetail(null, detail.getUserId(), Constants.TRADE_TYPE_PAY, 
					Constants.CASH_TYPE_CASH_REVOKE_PRIZE, winCash, detail.getBetDetailId(), gameType, remark.toString(),null,detail.getType(),user.getLoginName());
			//--by.cuisy.20171209
			if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
				userIds.add(detail.getUserId());
			}
		}
		
		// 更新UserTradeDetail中投注记录为不统计
		userService.updateUserTradeDetailStatus(sessionNo,gameType,Constants.PUB_STATUS_CLOSE);

		// 更新用户今日输赢
		userService.updateUserBalanceMoney(userIds);

		// 更新用户投注明细状态为未开奖
		gaDAO.updateGaBetDetailWinResult(sessionId,gameType);
		
		//--by.cuisy.20171209
		userService.updateUserMoney(userIds);

		return true;
	}
	@Override
	public boolean saveGfRevokePrize(Integer sessionId, String gameType,
			String sessionNo) {
		HQUtils hq=new HQUtils(" from GaBetSponsor gs where gs.betFlag ='1' ");
		hq.addHsql(" and gs.gameType= ? ");
		hq.addPars(gameType);
		hq.addHsql(" and gs.sessionId= ? ");
		hq.addPars(sessionId);
		hq.addHsql(" and gs.winResult= ? ");
		hq.addPars("4");//已中奖
		List<Object> list=gaDAO.findObjects(hq);
		List<Integer> userIds = new ArrayList<Integer>();//--by.cuisy.20171209
		
		for(Object obj:list){
			GaBetSponsor detail=(GaBetSponsor) obj;
			BigDecimal winCash = detail.getWinCash();//中奖金额
			StringBuilder remark = new StringBuilder();
			remark.append("彩派错误，撤回彩派金额 ").append(winCash).append("元");
			User user=UserCacheUtil.getUser(detail.getUserId());
			userService.saveTradeDetail(null, detail.getUserId(), Constants.TRADE_TYPE_PAY, 
					Constants.CASH_TYPE_CASH_REVOKE_PRIZE, winCash, detail.getJointId(), gameType, remark.toString(),null,user.getUserType(),user.getLoginName());
			//--by.cuisy.20171209
			if(!StringUtil.chkListIntContains(userIds, detail.getUserId())){
				userIds.add(detail.getUserId());
			}
		}
		
		
		// 更新用户今日输赢
		userService.updateUserBalanceMoney(userIds);
		
		// 更新用户投注明细状态为未开奖
		gaDAO.updateBetDetailWinResult(sessionId,gameType);
		
		//--by.cuisy.20171209
		userService.updateUserMoney(userIds);
		
		return true;
	}

	
	@Override
	public BigDecimal getDayBetMoney(Integer uid) {
		return gaDAO.getDayBetMoney(uid);
	}

	@Override
	public Param getParam(String type) {
		return gaDAO.getParam(type);
	}
	
	
}
