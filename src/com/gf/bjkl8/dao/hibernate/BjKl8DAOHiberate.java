package com.gf.bjkl8.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.gf.bjkl8.BjKl8Constants;
import com.gf.bjkl8.dao.IBjKl8DAO;
import com.gf.bjkl8.model.GfBjKl8GaSession;
import com.gf.bjkl8.model.GfBjKl8GaTrend;
import com.gf.bjkl8.model.dto.GfBjKl8DTO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;

public class BjKl8DAOHiberate  extends AbstractBaseDAOHibernate implements IBjKl8DAO {

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
	
	public GfBjKl8GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		//String todayYyyymmdd = DateTimeUtil.DateToString(now);
		//Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		//Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, BjLu28Constants.LUCKY28_TIME_INTERVAL);
		
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 09:00:00");
		
		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 23:55:00");
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 09:03:00");
		}else if(now.compareTo(todayStart1)>=0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" 09:03:00"),1);
		}
		
		List<GfBjKl8GaSession> list = getSession().createQuery("from GfBjKl8GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, BjKl8Constants.LUCKY28_OPEN_STATUS_INIT)
		.setString(3, BjKl8Constants.LUCKY28_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfBjKl8GaSession)list.get(0);
		}
		return null;
	}

	
	public GfBjKl8GaSession getPreviousSessionBySessionNo(String sessionNo) {
		String  hql=" from GfBjKl8GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfBjKl8GaSession)list.get(0);
		}
		return null;
	}

	
	public List<GfBjKl8GaTrend> findBjKl8GaTrendList() {
		String  hql=" from GfBjKl8GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}

	
	public PaginationSupport findBjKl8GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " from GfBjKl8GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfBjKl8GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List<GfBjKl8GaTrend> findBjKl8GaTrendAllList(){
		String  hql=" from GfBjKl8GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	@Override
	public PaginationSupport findBjKl8GaBetList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.bjkl8.model.dto.GfBjKl8BetDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from GfBjKl8GaBet ho where 1=1 "
				+ hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from GfBjKl8GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.bjkl8.model.dto.GfBjKl8DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
				+ " and ga.userId = u.userId "+hql+" order by ga.betDetailId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ga.betDetailId) from GaBetDetail ga, User u where 1=1" +
				" and  ga.userId = u.userId "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public List<GfBjKl8DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new new com.gf.bjkl8.model.dto.GfBjKl8DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfBjKl8DTO> list = query.list();
		return list;
	}

}
