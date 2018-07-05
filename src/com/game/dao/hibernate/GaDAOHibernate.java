package com.game.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.apps.model.Param;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.game.dao.IGaDAO;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.UserBetCount;
import com.game.model.dto.GaDTO;
import com.game.model.dto.SpDetailDTO;
import com.game.model.dto.WinCoDTO;

public class GaDAOHibernate extends AbstractBaseDAOHibernate 
implements IGaDAO {

	public Query makeQuerySQL(String hsql, List param) {
		Query q = getSession().createQuery(hsql);
		Iterator<Object> it = param.iterator();
		int i = 0;
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setTimestamp(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			}
			i++;
		}
		return q;
	}
	
	
	public List<GaDTO> getOptionTypeList(String gameType, String playType) {
		String hql="select   new com.game.model.dto.GaDTO(ho.optionType,ho.title) from GaBetOption ho where ho.gameType=? and ho.playType=?  group by ho.optionType,ho.title";
		Query query = getSession().createQuery(hql);
		query.setString(0, gameType);
		query.setString(1, playType);
		List<GaDTO> list = query.list();
		return list;
	}
	
	public List<GaBetOption> getBetPanelOptionList(String  gameType,String playType,String  optionType,Integer userId){
		String hql="select ho  from GaBetOption ho where ho.gameType=? and ho.playType=? and  ho.optionType=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, gameType);
		query.setString(1, playType);
		query.setString(2, optionType);
//		query.setInteger(1, userId);
		List<GaBetOption> list = query.list();
		return list;
	}
	public List<GaBetOption> getGaBetOptionByIds(String ids){
		String hql="select ho  from GaBetOption ho where ho.betOptionId in("+ids+")  order by find_in_set(ho.betOptionId,'"+ids+"') " ;
		Query query = getSession().createQuery(hql);
//		query.setInteger(1, userId);
		List<GaBetOption> list = query.list();
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<GaBetOption>();
	}
	
	@SuppressWarnings("unchecked")
	public List<GaBetDetail> findGaBetDetailList(String hql,List<Object> para){
		return makeQuerySQL("from GaBetDetail ho where 1=1 "+hql, para).list();
	}
	
	public GaDTO getGaDTO(String gameType,Date date){
		String todayYyyymmdd = DateTimeUtil.DateToString(date);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		String hql="select new com.game.model.dto.GaDTO(sum(gks.betMoney),sum(gks.winCash),sum(gks.paperMoney)) from GaBetDetail gks where gks.betTime>=? and gks.betTime<? and (gks.winResult=? or gks.winResult=?) ";
		if(ParamUtils.chkString(gameType)){
			hql=hql+" and gks.gameType=? ";
		}
		Query query = getSession().createQuery(hql);
		query.setTimestamp(0, todayStart);
		query.setTimestamp(1, todayEnd);
		query.setString(2, Constants.GAME_WIN_STATUS_WIN);
		query.setString(3, Constants.GAME_WIN_STATUS_NOWIN);
		if(ParamUtils.chkString(gameType)){
			query.setString(4, gameType);
		}
		query.setMaxResults(1);
		List<GaDTO> list =query.list();
		if(list!=null && list.size()>0){
			return (GaDTO)list.get(0);
		}
		return null;
	}
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,int statIndex,int pageNum){
		String findList = "select ho from GaBetDetail ho,User u where ho.userId=u.userId  ";
		String findCount = "select count(ho.betDetailId) from GaBetDetail ho,User u  where ho.userId=u.userId  ";

		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageNum);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findJcRecordlList(String hql, List<Object> para,int statIndex,int pageNum){
		String findList = "select new com.game.model.dto.GaBetDetailDTO(g.betDetailId, g.betOptionId, g.sessionId, "
				+ "g.betTime, g.betMoney, g.winResult, g.winCash, g.playName, g.room, g.optionTitle, g.betRate, j.mId)"
				+ " from GaBetDetail g, JcField j where g.sessionId=j.fId ";
		String findCount = "select count(g.betDetailId) from GaBetDetail g, JcField j where g.sessionId=j.fId ";

		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageNum);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public GaSessionInfo findGaSessionInfo(String gameType){
		String findList=" from GaSessionInfo ho where ho.gameType=? ";
		Query record = getSession().createQuery(findList);
		record.setString(0, gameType);
		List queList = record.list();
		if(queList!=null&&queList.size()>0){
			return (GaSessionInfo) queList.get(0);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<GaSessionInfo> findGaSessionInfoList(){
		return getSession()
		.createQuery("from GaSessionInfo ho where ho.status='1' order by ho.sort")
		.list();
		
	}
	@SuppressWarnings("unchecked")
	public List<GaSessionInfo> findGaSessionInfoList(String playCate){
		return getSession()
		.createQuery("from GaSessionInfo ho where ho.status='1' and ho.playCate='"+playCate+"' order by ho.sort")
		.list();
	}


	@Override
	public PaginationSupport findGaBetSponsor(String hql, List<Object> para,int statIndex,int pageNum) {
		String findList = "select new com.gf.dcb.model.dto.GfDcbDTO(sp,u)  from GaBetSponsor  sp,User u where sp.userId = u.userId and 1=1 ";
		String findCount = "select count(sp.jointId) from GaBetSponsor sp,User u where sp.userId = u.userId and 1=1 ";

		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageNum);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public List<GaBetOption> findGaBetOptionByGameType(String gameType) {
		Query query = getSession().createQuery(" from GaBetOption sp where sp.gameType=? order by sp.betOptionId desc");
		query.setString(0, gameType);
		List<GaBetOption> list = query.list();
	    return list;
	}
	
	@Override
	public GaBetSponsor findGaBetSponsorById(String id){
		GaBetSponsor sp =null;
		Query query = getSession().createQuery(" from GaBetSponsor sp where sp.jointId = ? and 1=1 ");
		query.setString(0, id);
		List<GaBetSponsor> list = query.list();
	    sp = list.get(0);
	    return sp;
	}


	@Override
	public PaginationSupport findGaBetSponsorDetail(String hql,
			List<Object> para, int statIndex, int pageSize) {
		String findList = "select new com.game.model.dto.SpDetailDTO(u.userName, sp.gameName, sp.money, sp.winCash, sp.betTime, sp.winResult, sp.sessionNo, sp.multiple, spd.title) "
				+ "from GaBetSponsorDetail spd ,GaBetSponsor sp,User u where spd.jointId =sp.jointId and sp.userId = u.userId ";
		String findCount = "select count(sp.jointId) from GaBetSponsorDetail spd ,GaBetSponsor sp,User u where spd.jointId =sp.jointId and sp.userId = u.userId ";

		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public List<WinCoDTO> findGaWinCountList(String gameType){
		   Query query = getSession().createQuery(" select new com.game.model.dto.WinCoDTO(co,u) from GaWinCount co,User u where co.gameType=? and co.userId = u.userId order by co.totalMoney desc")
		    		.setString(0, gameType).setMaxResults(10);
		    List<WinCoDTO> list = query.list();
			return list;
	}
	public List<SpDetailDTO>  findGaBetSponsorListByGameTypeAndBetType(String gameType,String betType){
		  Query query = getSession().createQuery("select new com.game.model.dto.SpDetailDTO(ho,u)  from GaBetSponsor ho,User u where ho.gameType=? and ho.betType = ? and u.userId=ho.userId  ")
		    		.setString(0, gameType)
		    	    .setString(1, betType);
		    List<SpDetailDTO> list = query.list();
			return list;
	}
	public List<SpDetailDTO> findGaBetPartListByJointId(Integer jointId){
		  Query query = getSession().createQuery("select new com.game.model.dto.SpDetailDTO(ho,u)  from GaBetPart ho,User u where ho.jointId=? and u.userId=ho.userId  ")
		    		.setInteger(0, jointId);
		    List<SpDetailDTO> list = query.list();
			return list;
	}


	@Override
	public List<GaBetSponsorDetail> findGaBetSponsorDetailListByJointId(
			Integer jointId) {
		  Query query = getSession().createQuery(" from GaBetSponsorDetail ho where ho.jointId =? and 1 = 1 ")
		    		.setInteger(0, jointId);
		 List<GaBetSponsorDetail> list = query.list();
		 return list;
	}


	@Override
	public List<GaBetSponsor> findGaBetSponsorList(String hql, List<Object> para) {
		String findList = " from GaBetSponsor  sp where 1=1 ";
		Query record = makeQuerySQL(findList + hql, para);
		List<GaBetSponsor> queList = record.list();

		return queList;

	}


	@Override
	public List<SpDetailDTO> findGaBetSponsorAndUserList(String hql,
			List<Object> para) {
		String findList = " select new com.game.model.dto.SpDetailDTO(ho,u)  from GaBetSponsor ho,User u where u.userId=ho.userId   ";
		Query record = makeQuerySQL(findList + hql, para);
		List<SpDetailDTO> queList = record.list();

		return queList;
	}
	
	public List<GaWinCount> findGaWinCountList(String hql,List<Object> para){
		   String findList = "  from GaWinCount ho where 1=1 ";
		   Query query =  makeQuerySQL(findList + hql, para);
		   List<GaWinCount> list = query.list();
		   return list;
	}


	@Override
	public GaDayBetCount findDayBetCount(StringBuffer hqls, List<Object> para) {
		String hql = " from GaDayBetCount ho where 1=1 ";
		Query query = makeQuerySQL(hql + hqls, para);
		List<GaDayBetCount> queList = query.list();
		GaDayBetCount betCount= null;
		if(queList !=null && queList.size() > 0){
			betCount = queList.get(0);
		}
		return betCount;
	}


	@Override
	public void updateUserDayWin() {
		String hql = " update User set dayWinMoney = 0,dayBetMoney = 0 where (userType='1' or userType='11') and status='1' ";
		Query query = getSession().createQuery(hql);
		query.executeUpdate();
	}

	@Override
	public void updateUserWeekWin() {
		String hql = " update User set weekWinMoney = 0 where (userType='1' or userType='11') and status='1' ";
		Query query = getSession().createQuery(hql);
		query.executeUpdate();
	}
	
	public PaginationSupport findGaSessionInfoList(String hql,
			List<Object> para, int startIndex, int pageSize){
		String hqls=" from GaSessionInfo ho  where 1=1 ";
		String countHql="select count(ho.infoId) from GaSessionInfo ho where 1=1 ";
		Query record = makeQuerySQL(hqls + hql , para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());

	}
		@Override
	public void updateBj3GaSession(Date date) {
		String findList=" delete from Bj3GaSession where 1=1 and startTime <= ? ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateMarkSixGaSession(Date date) {
		String findList=" delete from MarkSixGaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateGdK10GaSession(Date date) {
		String findList=" delete from GdK10GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateBjLu28GaSession(Date date) {
		String findList=" delete from BjLu28GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateBjPk10GaSession(Date date) {
		String findList=" delete from BjPk10GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateGxK10GaSession(Date date) {
		String findList=" delete from GxK10GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateCqSscGaSession(Date date) {
		String findList=" delete from CqSscGaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateJsK3GaSession(Date date) {
		String findList=" delete from JsK3GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updatePokerGaSession(Date date) {
		String findList=" delete from PokerGaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateTjSscGaSession(Date date) {
		String findList=" delete from TjSscGaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateXjSscGaSession(Date date) {
		String findList=" delete from XjSscGaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateGdPick11GaSession(Date date) {
		String findList=" delete from GdPick11GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateXjpLu28GaSession(Date date) {
		String findList=" delete from XjpLu28GaSession where 1=1 and startTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
	}


	@Override
	public void updateGaBetDetail(Date date) {
		String findList=" delete from GaBetDetail where 1=1 and betTime <= ?  ";
		Query record = getSession().createQuery(findList);
		record.setDate(0, date);
		record.executeUpdate();
		log.info("---------------------------> update");
	}

	@Override
	public PaginationSupport findDailyStatisticsList(String hql,
			List<Object> para, int startIndex, int pageSize) {
		String hqls="select new com.game.model.dto.GaDTO(sum(ho.totalRecharge),sum(ho.totalDrawMoney),sum(ho.totalBet),sum(ho.totalWin),ho.createTime,ho.userId,ho.userName) from DailyStatistics ho,User u where ho.userId = u.userId ";
		String countHql="select count(ho.id) from DailyStatistics ho,User u where ho.userId = u.userId ";
		String groupHql = " group by ho.userId order by ho.userId desc ";
		Query record = makeQuerySQL(hqls + hql +groupHql , para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());

	}


	@Override
	public PaginationSupport findDailyStatisticsList2(String hql,
			List<Object> para, int startIndex, int pageSize) {
		String hqls="select new com.game.model.dto.GaDTO(ho.totalRecharge,ho.totalDrawMoney,ho.totalBet,ho.totalWin,ho.createTime,ho.userId,ho.userName) from DailyStatistics ho,User u where ho.userId = u.userId ";
		String countHql="select count(ho.id) from DailyStatistics ho,User u where ho.userId = u.userId ";
		Query record = makeQuerySQL(hqls + hql , para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}


	@Override
	public List<GaDTO> findUserTradeDetailList(String hql,List<Object> para) {
		String hqls="select new com.game.model.dto.GaDTO(u.userId,sum(ho.cashMoney),ho.cashType) from UserTradeDetail ho,User u where ho.userId = u.userId  ";
		Query query =  makeQuerySQL(hqls + hql , para);
		List<GaDTO> list=query.list();
		return list;
	}


	@Override
	public List<GaDTO> findAgentStatisticsList(String hql,
			List<Object> para) {
		String hqls="select new com.game.model.dto.GaDTO(u.agentId,sum(ho.cashMoney),ho.cashType) from UserTradeDetail ho,User u where ho.userId = u.userId and ho.status='1' ";
		Query query =  makeQuerySQL(hqls + hql , para);
		List<GaDTO> list=query.list();
		return list;
	}


	@Override
	public List<GaDTO> findUserSubmemberList(String hqls, List<Object> para) {
		String hqls2="select new com.game.model.dto.GaDTO(u.agentId,u.agentName,count(u.userId)) from User u where 1=1 ";
		Query query =  makeQuerySQL(hqls2 + hqls , para);
		List<GaDTO> list=query.list();
		return list;
	}
	public List<GaDTO> findUserBetCountList(String hqls, List<Object> para){
		String hqls2="select new com.game.model.dto.GaDTO(ho.userId,sum(ho.betMoney),u.agentId) from GaBetDetail ho,User u where u.userId = ho.userId ";
		Query query =  makeQuerySQL(hqls2 + hqls , para);
		List<GaDTO> list=query.list();
		return list;
	}
	public void updateUserInfo(){
		String hql = "update User set userBalance=0,dayRecharge=0,turnTableNum=0,redPacketsNum=0,dayBetMoney=0";
		Query query = getSession().createQuery(hql);
		query.executeUpdate();
	}
	@SuppressWarnings("unchecked")
	public List<UserBetCount> findUserBetCountListForAgent(Date now){//ho.createTime>=? and
//		String findList="select new  com.game.model.dto.GaDTO(u,ho) from UserBetCount ho,User u  where  ho.createTime>=? and ho.createTime<=? and ho.agentStatus='0' and  u.userId = ho.agentId  ";
//		Query query = getSession().createQuery(findList);
//		query.setString(0, DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(date, 1))+" 00:00:00");
//		query.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()));
//		List<GaDTO> list=query.list();
//		return list;
		
		//调整根据dateFlag来查找前一天的 联合user查询有效的代理，如果下级会员设置为不返水，则不返代理
		Date qDate = DateTimeUtil.getDateBefore(now,1);
		Integer dateFlag = Integer.valueOf(DateTimeUtil.dateToString(qDate,"yyMMdd"));
		GameHelpUtil.log("dateFlag="+dateFlag);
		return getSession().createQuery("select ho " +
				"from UserBetCount ho,User u where u.userId = ho.agentId and ho.dateFlag=? and ho.agentStatus=?")
			.setInteger(0, dateFlag)
			.setString(1, Constants.PUB_STATUS_CLOSE)
			.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GaDTO> findUserBetCountList(Date now){//ho.betTime>=? and
//		return getSession().createQuery("select new  com.game.model.dto.GaDTO(u.isBetBack,ho) from UserBetCount ho,User u where ho.createTime>=? and ho.createTime<=? and ho.personStatus='0' and u.userId = ho.userId ")
//		.setString(0, DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(date, 1))+" 00:00:00")
//		.setString(1, DateTimeUtil.DateToString(DateTimeUtil.getDateBefore(date, 1))+" 23:59:59")
//		.list();
		
		//调整根据dateFlag来查找前一天的
		Date qDate = DateTimeUtil.getDateBefore(now,1);
		Integer dateFlag = Integer.valueOf(DateTimeUtil.dateToString(qDate,"yyMMdd"));
		GameHelpUtil.log("dateFlag="+dateFlag);
		return getSession().createQuery("select new com.game.model.dto.GaDTO(u.isBetBack,ho) " +
				"from UserBetCount ho,User u where u.userId = ho.userId and ho.dateFlag=? and ho.personStatus=?")
			.setInteger(0, dateFlag)
			.setString(1, Constants.PUB_STATUS_CLOSE)
			.list();
	}
	
	@Override
	public GaDTO getDetailEstimateMoney(String sessionNo, String gameType) {
	    Query query = getSession().createQuery(" select new com.game.model.dto.GaDTO(sum(ga.oneMoney),sum(ga.twoMoney),sum(ga.threeMoney),sum(ga.fourMoney),sum(ga.fiveMoney)) " +
	    		" from GaBetDetail ga where  ga.sessionNo=? and ga.gameType=? ").setString(0, sessionNo).setString(1, gameType);
	    List<GaDTO> list = query.list();
	    if(list!=null&&list.size()>0){
	    	return list.get(0);
	    }
		return null;
	}
	
	@Override
	public List<GaBetDetail> findGaBetDetailListByRL(String hsql, List<Object> para) {
		String hqls=" select ho from GaBetDetail ho, UserTradeDetailRl rl where ho.betDetailId = rl.betDetailId ";
		Query query =  makeQuerySQL(hqls + hsql , para);
		@SuppressWarnings("unchecked")
		List<GaBetDetail> list = query.list();
		return list;
	}
	
	@Override
	public void updateGaBetDetailWinResult(Integer sessionId, String gameType) {
		StringBuffer hql = new StringBuffer();
		List<Object> para = new ArrayList<Object>();
		hql.append("update GaBetDetail set winResult = ?,winCash=?,payoff=? ");
		para.add(Constants.GAME_WIN_STATUS_NOOPEN);
		para.add(0);
		para.add(0);
		hql.append(" where 1=1 and sessionId = ? ");
		para.add(sessionId);
		hql.append(" and gameType = ? ");
		para.add(gameType);
		Query query = makeQuerySQL(hql.toString() , para);
		query.executeUpdate();
	}


	@Override
	public void updateBetDetailWinResult(Integer sessionId, String gameType) {
		StringBuffer hql = new StringBuffer();
		List<Object> para = new ArrayList<Object>();
		hql.append("update GaBetSponsor set winResult=?,winCash=? ");
		para.add("2");
		para.add(0);
		hql.append(" where sessionId = ? ");
		para.add(sessionId);
		hql.append(" and gameType = ? ");
		para.add(gameType);
		Query query = makeQuerySQL(hql.toString() , para);
		query.executeUpdate();
		
		
		StringBuffer hql2 = new StringBuffer();
		List<Object> para2 = new ArrayList<Object>();
		hql2.append("update GaBetSponsorDetail set winResult=?,winMoney=? ");
		para2.add("2");
		para2.add(0);
		hql2.append(" where jointId in ( select jointId from GaBetSponsor where sessionId = ? ");
		para2.add(sessionId);
		hql2.append(" and gameType = ? )");
		para2.add(gameType);
		Query query2 = makeQuerySQL(hql2.toString() , para2);
		query2.executeUpdate();
	}
	
	@Override
	public BigDecimal getDayBetMoney(Integer uid) {
		Query query = getSession().createQuery("select sum(gbd.betMoney) from GaBetDetail gbd where gbd.userId=? and gbd.betTime>=? and gbd.betTime<=? ");
		query.setInteger(0, uid);
		query.setString(1, DateTimeUtil.getCurrentDayStartStr());
		query.setString(2, DateTimeUtil.getCurrentDayendStr());
		Integer temp=(Integer)query.uniqueResult();
		if(!ParamUtils.chkInteger(temp)){
			temp=0;
		}
		BigDecimal totalCount = new BigDecimal(temp);
		return totalCount;
	}


	@Override
	public Param getParam(String type) {
		Query query = getSession().createQuery(" from Param p where p.type = ? ");
		query.setString(0, type);
		
		return (Param) query.uniqueResult();
	}
	
	
}
