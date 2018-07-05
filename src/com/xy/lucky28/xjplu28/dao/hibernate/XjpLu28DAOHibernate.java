package com.xy.lucky28.xjplu28.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.lucky28.xjplu28.XjpLu28Constants;
import com.xy.lucky28.xjplu28.dao.IXjpLu28DAO;
import com.xy.lucky28.xjplu28.model.XjpLu28GaSession;
import com.xy.lucky28.xjplu28.model.XjpLu28GaTrend;
import com.xy.lucky28.xjplu28.model.dto.XjpLu28DTO;

public class XjpLu28DAOHibernate extends AbstractBaseDAOHibernate implements IXjpLu28DAO {
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
	@Override
	public XjpLu28GaSession getCurrentSession() {

		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		//String todayYyyymmdd = DateTimeUtil.DateToString(now);
		//Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		//Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, XjpLu28Constants.LUCKY28_TIME_INTERVAL);
		
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 06:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 08:00:00");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 08:01:00");
		}
		
		List<XjpLu28GaSession> list = getSession().createQuery("from XjpLu28GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, XjpLu28Constants.LUCKY28_OPEN_STATUS_INIT)
		.setString(3, XjpLu28Constants.LUCKY28_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (XjpLu28GaSession)list.get(0);
		}
		return null;
	
	}

	public XjpLu28GaSession getTempCurrentSession() {

		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		//String todayYyyymmdd = DateTimeUtil.DateToString(now);
		//Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		//Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, XjpLu28Constants.LUCKY28_TIME_INTERVAL);
		
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 06:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 08:00:00");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 08:01:00");
		}
		
		List<XjpLu28GaSession> list = getSession().createQuery("from XjpLu28GaSession gks where gks.startTime<=? and gks.endTime>?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
//		.setString(2, XjpLu28Constants.LUCKY28_OPEN_STATUS_INIT)
//		.setString(3, XjpLu28Constants.LUCKY28_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (XjpLu28GaSession)list.get(0);
		}
		return null;
	
	}
	
	@Override
	public XjpLu28GaSession getPreviousSessionBySessionNo(String sessionNo) {
		String  hql=" from XjpLu28GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (XjpLu28GaSession)list.get(0);
		}
		return null;
	}

	@Override
	public List<XjpLu28GaTrend> findXjpLu28GaTrendList() {
		String  hql=" from XjpLu28GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}

	@Override
	public PaginationSupport findXjpLu28GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " from XjpLu28GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from XjpLu28GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public List<XjpLu28GaTrend> findXjpLu28GaTrendAllList(){
		String  hql=" from XjpLu28GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	@Override
	public PaginationSupport findXjpLu28GaBetList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.lucky28.xjplu28.model.dto.XjpLu28BetDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from XjpLu28GaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from XjpLu28GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.lucky28.xjplu28.model.dto.XjpLu28DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<XjpLu28DTO> findGaBetDetailById(String hql, List<Object> para) {
		Query query = getSession().createQuery(" select new com.xy.lucky28.xjplu28.model.dto.XjpLu28DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "+ hql)
				.setParameter(0, para.get(0));
		List<XjpLu28DTO> list = query.list();
		return list;
	}
	@Override
	public void deleteXjpLu28GaBet(String hql, List<Object> para) {
		String delHql=" delete from XjpLu28GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
