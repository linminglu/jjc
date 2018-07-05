package com.game.dao.hibernate;


import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.game.GameConstants;
import com.game.dao.IGaHhmfDAO;
import com.game.model.GaHhmfBet;
import com.game.model.GaHhmfBetDetail;
import com.game.model.GaHhmfBetOption;
import com.game.model.GaHhmfSession;
import com.game.model.dto.GaHhmfDTO;
import com.game.model.GaHhmfSession;

public class GaHhmfDAOHibernate extends AbstractBaseDAOHibernate 
	implements IGaHhmfDAO {
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

	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaHhmfSession ghs where ghs.openStatus = 2 ";
		String findCount = "select count(ghs.sessionId) from  GaHhmfSession ghs where ghs.openStatus = 2 ";
		
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaHhmfSession ghs where ghs.openStatus != 0  ";
		String findCount = "select count(ghs.sessionId) from GaHhmfSession ghs where ghs.openStatus != 0 ";
		
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaHhmfDTO(ghb,ghs) from GaHhmfBet ghb,GaHhmfSession ghs,User u where ghb.sessionId=ghs.sessionId and ghb.userId=u.userId and ghs.openStatus !=0 ";
		String findCount = "select count(ghb.betId) from GaHhmfBet ghb,GaHhmfSession ghs,User u where ghb.sessionId=ghs.sessionId and ghb.userId=u.userId and ghs.openStatus !=0  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaHhmfDTO(ghb,ghs,u.loginName) from GaHhmfBet ghb,GaHhmfSession ghs,User u where ghb.sessionId=ghs.sessionId and ghb.userId=u.userId ";
		String findCount = "select count(ghb.betId) from GaHhmfBet ghb,GaHhmfSession ghs,User u where ghb.sessionId=ghs.sessionId and ghb.userId=u.userId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaHhmfDTO(ghbd,ghs) from GaHhmfBetDetail ghbd,GaHhmfSession ghs,User u,GaHhmfBet ghb where ghbd.sessionId=ghs.sessionId and ghbd.userId=u.userId and ghb.betId = ghbd.betId";
		String findCount = "select count(ghbd.betDetailId) from GaHhmfBetDetail ghbd,GaHhmfSession ghs,User u,GaHhmfBet ghb where ghbd.sessionId=ghs.sessionId and ghbd.userId=u.userId and ghb.betId = ghbd.betId";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List<GaHhmfDTO> findColorCountList(){
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//select new com.game.model.dto.GaHhmfDTO(openType,count(ho.sessionId))
		String findList = "select new com.game.model.dto.GaHhmfDTO(ho.openType,count(ho.sessionId))  from GaHhmfSession ho where ho.startTime>=? and ho.endTime<=? group by ho.openType ";
		Query record =getSession().createQuery(findList);
		record.setTimestamp(0, todayStart);
		record.setTimestamp(1, now);

		List queList = record.list();
		return queList;
	}
	public List<GaHhmfDTO> findRecent30GaHhmfSessionList(){
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		String findList = " select  new com.game.model.dto.GaHhmfDTO(ho.sessionId,ho.openResult,ho.sessionNo)  from GaHhmfSession ho where ho.startTime>=? and ho.startTime<=?  and ho.openStatus='2' order by ho.startTime desc ";	
		Query record =getSession().createQuery(findList);
		record.setTimestamp(0, todayStart);
		record.setTimestamp(1, now);
		record.setMaxResults(30);
		List queList = record.list();	
		return queList;
	}

	
	public GaHhmfSession getCurrentSession(Date now){
		if(now==null) now = DateTimeUtil.getJavaUtilDateNow();
		//Date nowEnd = DateTimeUtil.getDateTimeOfSeconds(now, GameConstants.HHMF_TIME_INTERVAL);
		

		@SuppressWarnings("unchecked")
		//List<GaHhmfSession> list = getSession().createQuery("from GaHhmfSession gks where gks.startTime>=? and gks.startTime<? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		List<GaHhmfSession> list = getSession().createQuery("from GaHhmfSession gks where gks.startTime<=? and gks.endTime>? order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
//		.setString(2, GameConstants.HHMF_OPEN_STATUS_BET)
//		.setString(3, GameConstants.HHMF_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GaHhmfSession)list.get(0);
		}
		return null;
	}
	
	public Integer findCurrentSessionCountPointsByBetType(String betType,Date now){
//		Date now = DateTimeUtil.getJavaUtilDateNow();
		String findCount = "select sum(ghd.betPoint) from GaHhmfSession ho,GaHhmfBetDetail ghd where ho.startTime<=? and ho.endTime>? and ghd.sessionId=ho.sessionId and ghd.betType=? ";
		Query record =getSession().createQuery(findCount);
		record.setTimestamp(0, now);
		record.setTimestamp(1, now);
		record.setString(2, betType);
		List queList=record.list();
		if(queList!=null && queList.size()>0){
//			System.out.println(queList.get(0)+"sum");
			if(queList.get(0)==null){
				return 0;
			}else{
				return (Integer) queList.get(0);
			}
		}
		return 0;
	}
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaHhmfDTO(ghs.sessionId,ghs.sessionNo,ghbd.betType,ghbd.betPoint,ghbd.winResult,ghbd.winCash) from GaHhmfBetDetail ghbd,GaHhmfSession ghs where ghbd.sessionId=ghs.sessionId  ";
		String findCount = "select count(ghbd.betDetailId) from GaHhmfBetDetail ghbd,GaHhmfSession ghs  where ghbd.sessionId=ghs.sessionId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List<GaHhmfBetDetail> findUserBetDetailList(Integer sessionId,Integer uid){
		//select new com.game.model.dto.GaHhmfDTO(openType,count(ho.sessionId))
		String findList = "select ho  from GaHhmfBetDetail ho,User u where ho.userId=u.userId and u.userId=? and ho.sessionId=? ";
		Query record =getSession().createQuery(findList);
		record.setInteger(0, uid);
		record.setInteger(1, sessionId);
		List queList = record.list();
		return queList;
	}
	

	public GaHhmfBet findGaHhmfBetBySessionIdAndUid(Integer sessionId,Integer uid){
		String findList = "select ho  from GaHhmfBet ho,User u where ho.userId=u.userId and u.userId=? and ho.sessionId=? ";
		Query record =getSession().createQuery(findList);
		record.setInteger(0, uid);
		record.setInteger(1, sessionId);
		List queList = record.list();
		if(queList!=null&&queList.size()>0){
			return (GaHhmfBet) queList.get(0);
		}
		return null;
	}
	public List<GaHhmfBetOption> findOddsList(String hql, List<Object> para){
		String findList = "select ho  from GaHhmfBetOption ho where 1=1 ";
		Query record = makeQuerySQL(findList + hql, para);
		List<GaHhmfBetOption> queList = record.list();
		return queList;
	}
	public PaginationSupport findBetOptionList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaHhmfBetOption ghbo where 1=1  ";
		String findCount = "select count(ghbo.betOptionId) from GaHhmfBetOption ghbo where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public void updateSort(Integer id, String flag) {
		String hql = "";
		if (flag.equals("1")) {// +1
			hql = "update com.game.model.GaHhmfBetOption set sort=sort+1 where betOptionId=?";
		} else {// -1
			hql = "update com.game.model.GaHhmfBetOption set sort=sort-1 where betOptionId=?";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		query.executeUpdate();
	}
	
	public Integer getBetPointCountBySessionIdAndBetType(Integer sessionId,String betType){
		String findList = "select sum(ho.betPoint)  from GaHhmfBetDetail ho where  ho.betType=? and ho.sessionId=? ";
		Query record =getSession().createQuery(findList);
		record.setString(0, betType);
		record.setInteger(1, sessionId);
		List queList = record.list();
		if(queList!=null&&queList.size()>0){
			if(queList.get(0)==null){
				return 0;
			}else{
				return (Integer) queList.get(0);
			}
		}
		return 0;
	}
	
}
