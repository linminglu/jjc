package com.xy.k10.cqk10.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.k10.cqk10.dao.ICqK10DAO;
import com.xy.k10.cqk10.model.CqK10GaSession;
import com.xy.k10.cqk10.model.CqK10GaTrend;
import com.xy.k10.cqk10.model.dto.CqK10DTO;



public class CqK10DAOHibernate extends AbstractBaseDAOHibernate implements ICqK10DAO {
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


	public CqK10GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
//		String todayYyyymmdd = DateTimeUtil.DateToString(now);
//		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
//		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 09:00:00");
//
//
//		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
//		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
//		
//		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
//			now=DateTimeUtil.parse(todayYyyymmdd+" 09:03:00");
//		}else  if(now.compareTo(todayStart1)>0&&now.compareTo(todayEnd1)<=0){
//			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" 09:03:00"),1);
//		}
//			
//		List<CqK10GaSession> list = getSession().createQuery("from CqK10GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
//		.setTimestamp(0, now)
//		.setTimestamp(1, now)
//		.setString(2, CqK10Constants.CQ_K10_OPEN_STATUS_INIT)
//		.setString(3, CqK10Constants.CQ_K10_OPEN_STATUS_OPENING)
//		.setMaxResults(1)
//		.list();
//		if(list!=null && list.size()>0){
//			return (CqK10GaSession)list.get(0);
//		}
//		return null;
		Query query = getSession().createQuery("from CqK10GaSession ho where ho.endTime >? order by ho.sessionNo asc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<CqK10GaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}

		
		
	}
	public CqK10GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from CqK10GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (CqK10GaSession)list.get(0);
		}
		return null;
	}
	public List<CqK10GaTrend> findCqK10GaTrendList(){
		String  hql=" from CqK10GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findCqK10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from CqK10GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from CqK10GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findCqK10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.k10.cqk10.model.dto.CqK10DTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from CqK10GaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from CqK10GaBet ho where 1=1"
				+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.k10.cqk10.model.dto.CqK10DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<CqK10DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.k10.cqk10.model.dto.CqK10DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<CqK10DTO> list = query.list();
		return list;
	}


	@Override
	public void deleteCqK10GaBet(String hql, List<Object> para) {
		String delHql=" delete from CqK10GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
