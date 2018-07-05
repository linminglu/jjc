package com.jc.service.impl;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.UserTradeDetailRl;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.model.GaBetDetail;
import com.jc.JingcaiConstants;
import com.jc.dao.IJcDAO;
import com.jc.model.JcField;
import com.jc.model.JcMatch;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.model.JcTeam;
import com.jc.model.Type;
import com.jc.service.IJcService;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class JcServiceImpl extends BaseService implements IJcService {
	
	private IJcDAO jcDAO;
	private IUserService userService;

	public void setJcDAO(IJcDAO jcDAO) {
		this.jcDAO = jcDAO;
		super.dao = jcDAO;
	}
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public List<Type> getTypeList(String type){
		return jcDAO.getTypeList(type);
	}
	
	public List<JcTeam> getTeamList(){
		return jcDAO.getTeamList();
	}
	
	public JcTeam getTeamByTitle(String title){
		return jcDAO.getTeamByTitle(title);
	}
	
	public String saveFields(JcField field, String playIds, String playTitles, String playStatuses, String optionIds, String optionTitles, String optionPlays, String optionRates){
		String msg = "";
		
		jcDAO.saveObject(field);
		
		String[] playId = playIds.split(",");
		String[] playTitle = playTitles.split(",");
		String[] playStatus = playStatuses.split(",");
		List<JcPlayType> playList = new ArrayList<JcPlayType>();
		
		if(playTitle!=null && playTitle.length>0){
			for (int i = 0; i < playTitle.length; i++) {
				String title = playTitle[i];
				String status = playStatus[i];
				Integer pId = null;
				if(playId!=null && playId.length>i){
					pId = Integer.valueOf(playId[i]);
				}
				JcPlayType playType = null;
				if(ParamUtils.chkInteger(pId)){
					//修改
					playType  = (JcPlayType) jcDAO.getObject(JcPlayType.class, pId);
					if(playType!=null && ParamUtils.chkString(title) && ParamUtils.chkString(status)){
						playType.setfId(field.getfId());
						playType.setTitle(title);
						playType.setStatus(status);
						playType.setOpenStatus(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
						jcDAO.saveObject(playType);
					}
				}else{
					//新增
					if(ParamUtils.chkString(title) && ParamUtils.chkString(status)){
						playType = new JcPlayType(field.getfId(), title, status, JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
						jcDAO.saveObject(playType);
					}
				}
				playList.add(playType);
			}
		}else{
			msg = "玩法输入不能为空";
		}
		
		if(msg.equals("")){
			String[] optionId = optionIds.split(",");
			String[] optionTitle = optionTitles.split(",");
			String[] optionPlay = optionPlays.split(",");
			String[] optionRate = optionRates.split(",");
			if(optionTitle.length>0 && optionPlay.length>0 && optionRate.length>0){
				for (int i = 0; i < optionTitle.length; i++) {
					String title = optionTitle[i];
					String play = optionPlay[i];
					String rate = optionRate[i];
					Integer oId = null;
					if(optionId!=null && optionId.length>i){
						oId = Integer.valueOf(optionId[i]);
					}
					
					JcPlayType playT = null;
					if(playList!=null && playList.size()>0){
						for (JcPlayType playType : playList) {
							if(play.equals(String.valueOf(playType.getpTId())) || play.equals(playType.getTitle())){
								playT = playType;
								break;
							}
						}
					}
					
					if(ParamUtils.chkInteger(oId)){
						// 修改
						JcOption opt = (JcOption) jcDAO.getObject(JcOption.class, oId);
						if(opt!=null && playT!=null){
							opt.setpTId(playT.getpTId());
							opt.setTitle(title);
							opt.setRate(new BigDecimal(rate));
							opt.setfId(field.getfId());
							jcDAO.saveObject(opt);
						}
					}else{
						// 新增
						if(playT!=null){
							JcOption option = new JcOption(playT.getpTId(),title, new BigDecimal(rate), field.getfId());
							jcDAO.saveObject(option);
						}
					}
				}
				
			}else{
				msg = "投注项不能为空";
			}
		}
		
		return msg;
	}
	
	public List<JcPlayType> getJcPlayTypesByFid(Integer fId, String status){
		return jcDAO.getJcPlayTypesByFid(fId, status);
	}
	
	public List<JcOption> getJcOptionsByPtId(Integer ptId){
		return jcDAO.getJcOptionsByPtId(ptId);
	}
	
	public void deletePlayTypeAndOption(Integer playId){
		// 先删除玩法下面的投注项
		List<JcOption> list = jcDAO.getJcOptionsByPtId(playId);
		if(list!=null && list.size()>0){
			for (JcOption option : list) {
				jcDAO.deleteObject(JcOption.class, option.getoId(), null);
			}
		}
		
		// 再删除玩法
		JcPlayType play = (JcPlayType) jcDAO.getObject(JcPlayType.class, playId);
		if(play!=null){
			jcDAO.deleteObject(JcPlayType.class, playId, null);
		}
	}
	
	public void deleteField(Integer fId){
		List<JcPlayType> plist = jcDAO.getJcPlayTypesByFid(fId,null);
		if(plist!=null && plist.size()>0){
			for (JcPlayType play : plist) {
				List<JcOption> oplist = jcDAO.getJcOptionsByPtId(play.getpTId());
				if(oplist!=null && oplist.size()>0){
					for (JcOption option : oplist) {
						jcDAO.deleteObject(JcOption.class, option.getoId(), null);
					}
				}
				
				jcDAO.deleteObject(JcPlayType.class, play.getpTId(), null);
			}
		}
		
		jcDAO.deleteObject(JcField.class, fId, null);
	}
	
	public List<JcField> getJcFieldByMid(Integer mid, String status){
		return jcDAO.getJcFieldByMid(mid, status);
	}
	
	public User saveUserBetInfo(JcField field,BigDecimal money,JcOption option, User user){
		BigDecimal paperMoney=user.getMoney();//TODO 默认用余额购买
		if(paperMoney==null) paperMoney = new BigDecimal(0);//判空处理
		BigDecimal tempMoney=new BigDecimal(0);
		
		GaBetDetail betDetail=new GaBetDetail();
		
		betDetail.setBetRate(option.getRate());
		betDetail.setBetOptionId(option.getoId());
		betDetail.setLoginName(user.getLoginName());
		betDetail.setWinResult("0");//未开奖
		betDetail.setBetFlag("1");//有效投注
		betDetail.setWinCash(BigDecimal.ZERO);
		betDetail.setPayoff(BigDecimal.ZERO);
		betDetail.setSessionId(field.getfId());// 此处为比赛局ID
		
		betDetail.setBetTime(new Date());
		betDetail.setBetMoney(money.intValue());
		
		betDetail.setUserId(user.getUserId());
		betDetail.setType(user.getUserType());
		
		if(paperMoney.compareTo(new BigDecimal(0))==1){
			if(money.compareTo(paperMoney)!=-1){
				betDetail.setPaperMoney(paperMoney);
				paperMoney=paperMoney.subtract(paperMoney);
//				tempMoney=tempMoney.add(money);
			}else{
				betDetail.setPaperMoney(money);
				paperMoney=paperMoney.subtract(money);
//				tempMoney=tempMoney.add(money);
			}
		}
		betDetail.setRoom(field.getTitle());
		betDetail.setSessionNo(null);//TODO
		
		JcMatch match = (JcMatch) jcDAO.getObject(JcMatch.class, field.getmId());
		if(match!=null){
			betDetail.setPlayName(match.getTitle());
			Type type = (Type) jcDAO.getObject(Type.class, match.gettId1());
			if(type!=null){
				betDetail.setGameName(type.getTitle()); //一级名称
				betDetail.setGameType(String.valueOf(type.getTid()));
			}
			BigDecimal total = match.getTotalPrice();
			if(total==null){
				total = BigDecimal.ZERO;
			}
			match.setTotalPrice(total.add(money));
		}
		
		JcPlayType playType = (JcPlayType) jcDAO.getObject(JcPlayType.class, option.getpTId());
		if(playType!=null){
			betDetail.setBetName(playType.getTitle());
			betDetail.setBetId(playType.getpTId());
		}
		
		betDetail.setOptionTitle(option.getTitle());
		
		UserTradeDetailRl  rl=new UserTradeDetailRl();
		betDetail=(GaBetDetail)jcDAO.saveObjectDB(betDetail);
		rl.setBetDetailId(betDetail.getBetDetailId());
		rl.setGfxy("0");
		
		StringBuilder remark = new StringBuilder();
		remark.append("竞猜 扣款 ").append(money).append("元");
		Integer tradeDetailId=userService.saveTradeDetail(null,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_BUY_JC, money, betDetail.getBetDetailId(), 
				Constants.GAME_TYPE_JC,remark.toString(),String.valueOf(field.getfId()),user.getUserType(),user.getLoginName());

		userService.updateUserMoney(user.getUserId());

		userService.updateUserBanlance(user.getUserId());
		rl.setTradeDetailId(tradeDetailId);
		jcDAO.updateObject(rl, null);
		
		return user;
	}
	
	/**
	 * 计算赔率
	 */
	public void updateJingcaiOdds(){
		HQUtils hq = new HQUtils();
		hq.addHsql(" from GaBetDetail ga where 1=1 ");
//		hq.addHsql(" and ga.gameType=? ");
//		hq.addPars(Constants.GAME_TYPE_WZRY); // TODO 此处更新所有的投注？
		hq.addHsql(" and ga.winResult=?");
		hq.addPars(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
		hq.addHsql(" and ga.betId!=0 ");
		
		// 用于存储同一场竞猜，以便于计算赔率
		List<Integer> sessionIdList = new ArrayList<Integer>();
		
		List<Object> objectList = jcDAO.findObjects(hq);
		if(objectList!=null && objectList.size()>0){
			for (Object object : objectList) {
				GaBetDetail betDetail = (GaBetDetail) object;
				Integer sessionId = betDetail.getBetId();//玩法Id，同一个玩法ID一致
				if(!sessionIdList.contains(sessionId)){
					sessionIdList.add(sessionId);
				}
			}
		}
		
		Integer jishu = JingcaiConstants.JINGCAI_QUIZ_BASE_NUMMBER;
		BigDecimal taxRate = JingcaiConstants.JINGCAI_QUIZ_TAX_RATE;
		
		List<String> optionTitleList = new ArrayList<String>();//存储所有的竞猜结果
		List<Integer> moneyList = new ArrayList<Integer>();
		
		if(sessionIdList!=null && sessionIdList.size()>0){
			for (Integer sessionId : sessionIdList) {
				
				HQUtils hqs = new HQUtils();
				hqs.addHsql(" from GaBetDetail ga where 1=1 ");
//				hqs.addHsql(" and ga.gameType=? ");
//				hqs.addPars(Constants.GAME_TYPE_WZRY);
				hqs.addHsql(" and ga.betId=?");
				hqs.addPars(sessionId);
				hqs.addHsql(" and ga.winResult=?");
				hqs.addPars(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
				hqs.addHsql(" and ga.betId!=0 ");
				List<Object> objList = findObjects(hqs);
				Integer tmoney = 0;
				Integer fId = 0;
				// 先查询出同一玩法的所有投注，然后计算不同投注项结果的投注总金额，以及所有的投注总金额，最后修改该玩法的赔率
				if(objList!=null && objList.size()>0){
					for (Object object : objList) {
						GaBetDetail betDetail = (GaBetDetail) object;
						Integer money = betDetail.getBetMoney();
						String optionTitle = betDetail.getOptionTitle();
						tmoney = tmoney + money;
						fId = betDetail.getSessionId();//比赛局ID
						
						if(optionTitleList!=null && optionTitleList.size()>0){
							for (int i = 0; i < optionTitleList.size(); i++) {
								if(optionTitle.equals(optionTitleList.get(i))){
									moneyList.set(i, moneyList.get(i)+ money);
								}
							}
						}
						
						if(!optionTitleList.contains(optionTitle)){
							optionTitleList.add(optionTitle);
							moneyList.add(money);
						}
					}
					BigDecimal totalMoney = BigDecimal.valueOf(tmoney);
					
					JcField field = (JcField) jcDAO.getObject(JcField.class, fId);
					if(field!=null){
						JcMatch  match = (JcMatch) jcDAO.getObject(JcMatch.class, field.getmId());
						if(match!=null){
							String rercentage = match.getRercentage();
							if(ParamUtils.chkString(rercentage)){
								taxRate = new BigDecimal(rercentage);
							}
						}
					}
					BigDecimal realMoney = totalMoney.multiply((new BigDecimal(1).subtract(taxRate)));
					
					// 赔率计算,A的赔率=(realmoney+jishu)/(teamAMoney+jishu)
					// 先修改竞猜结果表中的赔率
					BigDecimal odds = BigDecimal.ZERO;
					List<JcOption> list = jcDAO.getJcOptionsByPtId(sessionId);
					if(list!=null && list.size()>0){
						for (JcOption option : list) {
							if(optionTitleList!=null && optionTitleList.size()>0){
								for (int i = 0; i < optionTitleList.size(); i++) {
									if(option.getTitle().equals(optionTitleList.get(i))){
										odds =  (realMoney.add(BigDecimal.valueOf(jishu))).divide((BigDecimal.valueOf(moneyList.get(i)).add(BigDecimal.valueOf(jishu))),2,RoundingMode.HALF_UP);
									}
								}
								
								// 此投注项无人投注
								if(!optionTitleList.contains(option.getTitle())){
									odds = (realMoney.add(BigDecimal.valueOf(jishu))).divide(BigDecimal.valueOf(jishu),2,RoundingMode.HALF_UP);
								}
								
								option.setRate(odds);
								jcDAO.saveObject(option);
								
								// 修改投注表中的赔率
								HQUtils hql = new HQUtils();
								hql.addHsql(" from GaBetDetail ga where 1=1");
//								hql.addHsql(" and ga.gameType=? ");
//								hql.addPars(Constants.GAME_TYPE_WZRY); 
								hql.addHsql(" and ga.winResult=?");
								hql.addPars(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
								hql.addHsql(" and ga.sessionId=?");
								hql.addPars(sessionId);
								hql.addHsql(" and ga.betOptionId=? ");
								hql.addPars(option.getoId());
								
								List<Object> objeList = jcDAO.findObjects(hql);
								if(objeList!=null && objeList.size()>0){
									for (Object object : objeList) {
										GaBetDetail betDetail = (GaBetDetail) object;
										betDetail.setBetRate(odds);
										jcDAO.saveObject(betDetail);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public String updateOPenResult(JcPlayType playType, JcOption option){
		String msg = "";
		
		playType.setOpenResult(option.getTitle());
		playType.setOpenStatus(JingcaiConstants.QUIZ_STATUS_OPENING);
		jcDAO.saveObject(playType);
		
		Integer jishu = JingcaiConstants.JINGCAI_QUIZ_BASE_NUMMBER;
		BigDecimal taxRate = JingcaiConstants.JINGCAI_QUIZ_TAX_RATE;
		
		List<String> optionTitleList = new ArrayList<String>();//存储所有的竞猜结果
		List<Integer> moneyList = new ArrayList<Integer>();
		
		
		HQUtils hqs = new HQUtils();
		hqs.addHsql(" from GaBetDetail ga where 1=1 ");
//		hqs.addHsql(" and ga.gameType=? ");
//		hqs.addPars(Constants.GAME_TYPE_WZRY);
		hqs.addHsql(" and ga.betId=?");
		hqs.addPars(playType.getpTId());
		hqs.addHsql(" and ga.winResult=?");
		hqs.addPars(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
		hqs.addHsql(" and ga.betId!=0 ");
		List<Object> objList = findObjects(hqs);
		Integer tmoney = 0;
		Integer fId = 0;
		// 先查询出同一玩法的所有投注，然后计算不同投注项结果的投注总金额，以及所有的投注总金额，最后修改该玩法的赔率
		if(objList!=null && objList.size()>0){
			for (Object object : objList) {
				GaBetDetail betDetail = (GaBetDetail) object;
				Integer money = betDetail.getBetMoney();
				String optionTitle = betDetail.getOptionTitle();
				tmoney = tmoney + money;
				fId = betDetail.getSessionId();//比赛局ID
				
				if(optionTitleList!=null && optionTitleList.size()>0){
					for (int i = 0; i < optionTitleList.size(); i++) {
						if(optionTitle.equals(optionTitleList.get(i))){
							moneyList.set(i, moneyList.get(i)+ money);
						}
					}
				}
				
				if(!optionTitleList.contains(optionTitle)){
					optionTitleList.add(optionTitle);
					moneyList.add(money);
				}
			}
			
			BigDecimal totalMoney = BigDecimal.valueOf(tmoney);
			
			
			JcField field = (JcField) jcDAO.getObject(JcField.class, fId);
			if(field!=null){
				JcMatch  match = (JcMatch) jcDAO.getObject(JcMatch.class, field.getmId());
				if(match!=null){
					String rercentage = match.getRercentage();
					if(ParamUtils.chkString(rercentage)){
						taxRate = new BigDecimal(rercentage);
					}
				}
			}
			BigDecimal realMoney = totalMoney.multiply((new BigDecimal(1).subtract(taxRate)));
			
			// 平台抽税金额
			BigDecimal platMoney = totalMoney.multiply(taxRate);
			
			List<Integer> userIds = new ArrayList<Integer>();
			BigDecimal totalPoint = BigDecimal.ZERO;
			BigDecimal betCash = BigDecimal.ZERO;
			
			// 赔率计算,A的赔率=(realmoney+jishu)/(teamAMoney+jishu)
			// 先修改竞猜结果表中的赔率
			BigDecimal odds = BigDecimal.ZERO;
			List<JcOption> list = jcDAO.getJcOptionsByPtId(playType.getpTId());
			if(list!=null && list.size()>0){
				for (JcOption coption : list) {
					if(optionTitleList!=null && optionTitleList.size()>0){
						for (int i = 0; i < optionTitleList.size(); i++) {
							if(coption.getTitle().equals(optionTitleList.get(i))){
								odds =  (realMoney.add(BigDecimal.valueOf(jishu))).divide((BigDecimal.valueOf(moneyList.get(i)).add(BigDecimal.valueOf(jishu))),2,RoundingMode.HALF_UP);
							}
						}
						
						// 此投注项无人投注
						if(!optionTitleList.contains(coption.getTitle())){
							odds = (realMoney.add(BigDecimal.valueOf(jishu))).divide(BigDecimal.valueOf(jishu),2,RoundingMode.HALF_UP);
						}
						
						coption.setRate(odds);
						jcDAO.saveObject(coption);
						
						// 修改投注表中的赔率
						HQUtils hql = new HQUtils();
						hql.addHsql(" from GaBetDetail ga where 1=1");
//						hql.addHsql(" and ga.gameType=? ");
//						hql.addPars(Constants.GAME_TYPE_WZRY); 
						hql.addHsql(" and ga.winResult=?");
						hql.addPars(JingcaiConstants.QUIZ_STATUS_NOT_OPEN);
						hql.addHsql(" and ga.sessionId=?");
						hql.addPars(playType.getfId());
						hql.addHsql(" and ga.betOptionId=? ");
						hql.addPars(coption.getoId());
						
						List<Object> objeList = jcDAO.findObjects(hql);
						if(objeList!=null && objeList.size()>0){
							for (Object object : objeList) {
								GaBetDetail betDetail = (GaBetDetail) object;
								betDetail.setBetRate(odds);
								String optionTitle = betDetail.getOptionTitle();
								Integer userId = betDetail.getUserId();
								betDetail.setOpenResult(playType.getOpenResult());
								if(!StringUtil.chkListIntContains(userIds, userId)){
									userIds.add(userId);
								}//~
								
								User temp=(User) jcDAO.getObject(User.class, userId);
								String userType=temp.getUserType();
								BigDecimal winCash = BigDecimal.ZERO;
								if(optionTitle.equals(playType.getOpenResult())){// 中奖
									winCash = odds.multiply(BigDecimal.valueOf(betDetail.getBetMoney()));
									betDetail.setWinCash(winCash);
									betDetail.setWinResult(Constants.GAME_WIN_STATUS_WIN);
									betCash=betCash.add(winCash);
									totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
									betDetail.setPayoff(winCash.subtract(new BigDecimal(betDetail.getBetMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
									
									StringBuffer remark=new StringBuffer();
									remark.append("竞猜中奖 奖金 ") .append(winCash).append("元");
									userService.updateOpenData(betDetail,null,remark.toString(),userType);
									
								}else{// 未中奖
									betDetail.setWinResult(Constants.GAME_WIN_STATUS_NOWIN);
									betDetail.setWinCash(winCash);
									totalPoint=totalPoint.add(new BigDecimal(betDetail.getBetMoney()));
									
									jcDAO.updateObject(betDetail, null);
								}
								jcDAO.saveObject(betDetail);
							}
						}
					}
				}
				
				userService.updateUserMoney(userIds);
				msg = "开奖成功";
			}
		}else{
			msg = "此玩法无人投注";
		}
		
		return msg;
	}
	
	public List<JcPlayType> getJcPlayNotOpen(Integer fId){
		return jcDAO.getJcPlayNotOpen(fId);
	}

	@Override
	public PaginationSupport findJcOptionList(String hql, List<Object> para,
			int statIndex, int pageNum) {
		return jcDAO.findJcOptionList(hql, para, statIndex, pageNum);
	}

	@Override
	public String saveMatch(String param) {
		JSONObject matchObj = new JSONObject(param);
		if (matchObj != null ) {
			String matchTitle = matchObj.getString("matchTitle");
			String subTitle = matchObj.getString("subTitle");
			String firstType = matchObj.getString("firstType");
			String secondType = matchObj.getString("secondType");
			String firstTeam = matchObj.getString("firstTeam");
			String secondTeam = matchObj.getString("secondTeam");
			String redTeam = matchObj.getString("redTeam");
			String blueTeam = matchObj.getString("blueTeam");
			String boNum = matchObj.getString("boNum");
			String matchType = matchObj.getString("matchType");
			String openTime = matchObj.getString("openTime");
			String matchTime = matchObj.getString("matchTime");
			String rercentage = matchObj.getString("rercentage");
			
			if (ParamUtils.chkString(matchTitle) && ParamUtils.chkString(subTitle)
					&& ParamUtils.chkString(firstType) && ParamUtils.chkString(secondType)
					&& ParamUtils.chkString(firstTeam) && ParamUtils.chkString(secondTeam)
					&& ParamUtils.chkString(redTeam) && ParamUtils.chkString(blueTeam)
					&& ParamUtils.chkString(boNum) && ParamUtils.chkString(matchType)
					&& ParamUtils.chkString(openTime) && ParamUtils.chkString(matchTime)
					&& ParamUtils.chkString(rercentage)) {
				// 先保存比赛
				JcMatch match = new JcMatch();
				match.setTitle(matchTitle);
				match.setSubTitle(subTitle);
				match.settId1(Integer.valueOf(firstType));
				match.settId2(Integer.valueOf(secondType));
				JcTeam team1 = (JcTeam) jcDAO.getObject(JcTeam.class, Integer.valueOf(firstTeam));
				if(team1!=null){
					match.setTeam1Name(team1.getTitle());
					match.setTeam1Img(team1.getImg());
					if(firstTeam.equals(redTeam)){
						match.setRed(team1.getTitle());
					}else if(firstTeam.equals(blueTeam)){
						match.setBlue(team1.getTitle());
					}
				} else {
					return APIConstants.CODE_NOT_FOUND;
				}
				JcTeam team2 = (JcTeam) jcDAO.getObject(JcTeam.class, Integer.valueOf(secondTeam));
				if(team2!=null){
					match.setTeam2Name(team2.getTitle());
					match.setTeam2Img(team2.getImg());
					if(secondTeam.equals(redTeam)){
						match.setRed(team2.getTitle());
					}else if(secondTeam.equals(blueTeam)){
						match.setBlue(team2.getTitle());
					}
				} else {
					return APIConstants.CODE_NOT_FOUND;
				}
				match.setBoNum(Integer.valueOf(boNum));
				match.setType(matchType);
				match.setOpenTime(DateTimeUtil.stringToDate(openTime, "yyyy-MM-dd HH:mm:ss"));
				match.setMatchTime(DateTimeUtil.StringToDate(matchTime, "yyyy-MM-dd HH:mm:ss"));
				match.setRercentage(rercentage);
				match.setIsRecommend(Constants.PUB_STATUS_CLOSE);
				match = (JcMatch) jcDAO.saveObjectDB(match);
				Integer matchId = match.getmId();
				
				JSONArray fieldArr = matchObj.getJSONArray("fieldArr");
				if (fieldArr != null) {
					for (int i = 0;i < fieldArr.length();i++) {
						JSONObject fieldObj = fieldArr.getJSONObject(i);
						// 保存场次
						String fieldTitle = fieldObj.getString("fieldTitle");
						String startDate = fieldObj.getString("startDate");
						String endDate = fieldObj.getString("endDate");
						JcField field = new JcField();
						field.setmId(matchId);
						field.setTitle(fieldTitle);
						field.setStatus(Constants.PUB_STATUS_OPEN);
						field.setStartDate(DateTimeUtil.stringToDate(startDate, "yyyy-MM-dd HH:mm:ss"));
						field.setEndDate(DateTimeUtil.stringToDate(endDate, "yyyy-MM-dd HH:mm:ss"));
						field = (JcField) jcDAO.saveObjectDB(field);
						Integer fieldId = field.getfId();
						
						JSONArray playArr = fieldObj.getJSONArray("playArr");
						if (playArr != null) {
							for (int j = 0;j < playArr.length();j++) {
								JSONObject playObj = playArr.getJSONObject(j);
								// 保存玩法
								String playTitle = playObj.getString("playTitle");
								JcPlayType play = new JcPlayType();
								play.setfId(fieldId);
								play.setTitle(playTitle);
								play.setStatus(Constants.PUB_STATUS_OPEN);
								play.setOpenStatus(Constants.PUB_STATUS_CLOSE);
								play = (JcPlayType) jcDAO.saveObjectDB(play);
								
								JSONArray optionArr = playObj.getJSONArray("optionArr");
								if (optionArr != null) {
									for (int k = 0;k < optionArr.length();k++) {
										JSONObject optionObj = optionArr.getJSONObject(k);
										// 保存投注项
										String optionTitle = optionObj.getString("optionTitle");
										String rate = optionObj.getString("rate");
										JcOption option = new JcOption();
										option.setpTId(play.getpTId());
										option.setTitle(optionTitle);
										option.setRate(new BigDecimal(rate));
										option.setfId(fieldId);
										jcDAO.saveObjectDB(option);
									}
								} else {
									return APIConstants.CODE_NOT_FOUND;
								}
							}
						} else {
							return APIConstants.CODE_NOT_FOUND;
						}
					}
				} else {
					return APIConstants.CODE_NOT_FOUND;
				}
			} else {
				return APIConstants.CODE_NOT_FOUND;
			}
			
		} 
		return APIConstants.CODE_REQUEST_SUCCESS;
		
	}
	
	public void deleteMatch(Integer mId){
		List<JcField> fieldList = jcDAO.getJcFieldByMid(mId, null);
		if(fieldList!=null && fieldList.size()>0){
			for (JcField field : fieldList) {
				Integer fId = field.getfId();
				deleteField(fId);
			}
		}
		
		jcDAO.deleteObject(JcMatch.class, mId, null);
	}
	
	public String saveUpdateMatch(String param) {
		JSONObject matchObj = new JSONObject(param);
		if (matchObj != null ) {
			String mId = matchObj.getString("mId");
			String matchTitle = matchObj.getString("matchTitle");
			String subTitle = matchObj.getString("subTitle");
			String firstType = matchObj.getString("firstType");
			String secondType = matchObj.getString("secondType");
			String firstTeam = matchObj.getString("firstTeam");
			String secondTeam = matchObj.getString("secondTeam");
			String redTeam = matchObj.getString("redTeam");
			String blueTeam = matchObj.getString("blueTeam");
			String boNum = matchObj.getString("boNum");
			String matchType = matchObj.getString("matchType");
			String openTime = matchObj.getString("openTime");
			String matchTime = matchObj.getString("matchTime");
			String rercentage = matchObj.getString("rercentage");
			
			if (ParamUtils.chkString(matchTitle) && ParamUtils.chkString(subTitle)
					&& ParamUtils.chkString(firstType) && ParamUtils.chkString(secondType)
					&& ParamUtils.chkString(firstTeam) && ParamUtils.chkString(secondTeam)
					&& ParamUtils.chkString(redTeam) && ParamUtils.chkString(blueTeam)
					&& ParamUtils.chkString(boNum) && ParamUtils.chkString(matchType)
					&& ParamUtils.chkString(openTime) && ParamUtils.chkString(matchTime)
					&& ParamUtils.chkString(rercentage) && ParamUtils.chkString(mId)) {
				// 先保存比赛
				JcMatch match = (JcMatch) jcDAO.getObject(JcMatch.class, Integer.valueOf(mId));
				if(match==null){
					return APIConstants.CODE_NOT_FOUND;
				}
				match.setTitle(matchTitle);
				match.setSubTitle(subTitle);
				match.settId1(Integer.valueOf(firstType));
				match.settId2(Integer.valueOf(secondType));
				JcTeam team1 = (JcTeam) jcDAO.getObject(JcTeam.class, Integer.valueOf(firstTeam));
				if(team1!=null){
					match.setTeam1Name(team1.getTitle());
					match.setTeam1Img(team1.getImg());
					if(firstTeam.equals(redTeam)){
						match.setRed(team1.getTitle());
					}else if(firstTeam.equals(blueTeam)){
						match.setBlue(team1.getTitle());
					}
				} else {
					return APIConstants.CODE_NOT_FOUND;
				}
				JcTeam team2 = (JcTeam) jcDAO.getObject(JcTeam.class, Integer.valueOf(secondTeam));
				if(team2!=null){
					match.setTeam2Name(team2.getTitle());
					match.setTeam2Img(team2.getImg());
					if(secondTeam.equals(redTeam)){
						match.setRed(team2.getTitle());
					}else if(secondTeam.equals(blueTeam)){
						match.setBlue(team2.getTitle());
					}
				} else {
					return APIConstants.CODE_NOT_FOUND;
				}
				match.setBoNum(Integer.valueOf(boNum));
				match.setType(matchType);
				match.setOpenTime(DateTimeUtil.stringToDate(openTime, "yyyy-MM-dd HH:mm:ss"));
				match.setMatchTime(DateTimeUtil.StringToDate(matchTime, "yyyy-MM-dd HH:mm:ss"));
				match.setRercentage(rercentage);
				match.setIsRecommend(Constants.PUB_STATUS_CLOSE);
				match = (JcMatch) jcDAO.saveObjectDB(match);
				Integer matchId = match.getmId();
				
				JSONArray fieldArr = matchObj.getJSONArray("fieldArr");
				if (fieldArr == null) {
					return APIConstants.CODE_NOT_FOUND;
				} 
				
				List<JcField> filedList = jcDAO.getJcFieldByMid(matchId, null);
				
				for (int i = 0;i < fieldArr.length();i++) {
					JSONObject fieldObj = fieldArr.getJSONObject(i);
					// 保存场次
					String fId = fieldObj.getString("fId");
					String fieldTitle = fieldObj.getString("fieldTitle");
					String startDate = fieldObj.getString("startDate");
					String endDate = fieldObj.getString("endDate");
					JcField field = null;
					if(ParamUtils.chkString(fId) && !fId.equals("undefined")){
						field = (JcField) jcDAO.getObject(JcField.class, Integer.valueOf(fId));
						if(field==null){
							return APIConstants.TIPS_DATA_NOT;
						}
					}else{
						field = new JcField();
					}
					
					if(filedList!=null && filedList.size()>0){
						Iterator<JcField> it = filedList.iterator();
						while(it.hasNext()){
							JcField fields = it.next();
						    if(fields.equals(field)){
						        it.remove();
						    }
						}
					}
					
					if(i==fieldArr.length()-1 && filedList!=null && filedList.size()>0){
						for (JcField jcField : filedList) {
							deleteField(jcField.getfId());
						}
					}
					
					field.setmId(matchId);
					field.setTitle(fieldTitle);
					field.setStatus(Constants.PUB_STATUS_OPEN);
					field.setStartDate(DateTimeUtil.stringToDate(startDate, "yyyy-MM-dd HH:mm:ss"));
					field.setEndDate(DateTimeUtil.stringToDate(endDate, "yyyy-MM-dd HH:mm:ss"));
					if(!fieldTitle.equals("undefined")){
						field = (JcField) jcDAO.saveObjectDB(field);
					}
					
					Integer fieldId = field.getfId();
					List<JcPlayType> playList = null;
					if(ParamUtils.chkInteger(fieldId)){
						playList = jcDAO.getJcPlayTypesByFid(fieldId, null);
					}
					
					JSONArray playArr = fieldObj.getJSONArray("playArr");
					if (playArr == null) {
						return APIConstants.CODE_NOT_FOUND;
					} 
					for (int j = 0;j < playArr.length();j++) {
						JSONObject playObj = playArr.getJSONObject(j);
						// 保存玩法
						String ptId = playObj.getString("ptId");
						String playTitle = playObj.getString("playTitle");
						JcPlayType play = null;
						if(ParamUtils.chkString(ptId)){
							play = (JcPlayType) jcDAO.getObject(JcPlayType.class, Integer.valueOf(ptId));
							if(play==null){
								return APIConstants.TIPS_DATA_NOT;
							}
						}else{
							play = new JcPlayType();
						}
						
						if(playList!=null && playList.size()>0){
							Iterator<JcPlayType> itp = playList.iterator();
							while(itp.hasNext()){
								JcPlayType plays = itp.next();
							    if(plays.equals(play)){
							    	itp.remove();
							    }
							}
						}
						
						if(j==playArr.length()-1 && playList!=null && playList.size()>0){
							for (JcPlayType jcPlayType : playList) {
								deletePlayTypeAndOption(jcPlayType.getpTId());
							}
						}
						
						play.setfId(fieldId);
						play.setTitle(playTitle);
						play.setStatus(Constants.PUB_STATUS_OPEN);
						play.setOpenStatus(Constants.PUB_STATUS_CLOSE);
						play = (JcPlayType) jcDAO.saveObjectDB(play);
						Integer playId = play.getpTId();
						
						List<JcOption> optList = jcDAO.getJcOptionsByPtId(playId);
						
						JSONArray optionArr = playObj.getJSONArray("optionArr");
						if (optionArr == null) {
							return APIConstants.CODE_NOT_FOUND;
						} 
						
						for (int k = 0;k < optionArr.length();k++) {
							JSONObject optionObj = optionArr.getJSONObject(k);
							// 保存投注项
							String oId = optionObj.getString("oId");
							String optionTitle = optionObj.getString("optionTitle");
							String rate = optionObj.getString("rate");
							JcOption option = null;
							if(ParamUtils.chkString(oId)){
								option = (JcOption) jcDAO.getObject(JcOption.class, Integer.valueOf(oId));
								if(option==null){
									return APIConstants.TIPS_DATA_NOT;
								}
							}else{
								option = new JcOption();
							}
							
							if(optList!=null && optList.size()>0){
								Iterator<JcOption> ito = optList.iterator();
								while(ito.hasNext()){
									JcOption opts = ito.next();
								    if(opts.equals(option)){
								    	ito.remove();
								    }
								}
							}
							
							if(k==optionArr.length()-1 && optList!=null && optList.size()>0){
								for (JcOption jcOption : optList) {
									jcDAO.deleteObject(JcOption.class, jcOption.getoId(), null);
								}
							}
							
							option.setpTId(play.getpTId());
							option.setTitle(optionTitle);
							option.setRate(new BigDecimal(rate));
							option.setfId(fieldId);
							jcDAO.saveObjectDB(option);
						}
					}
				}
			} else {
				return APIConstants.CODE_NOT_FOUND;
			}
			
		}
		return APIConstants.CODE_REQUEST_SUCCESS;
	}
}
