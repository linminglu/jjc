package com.xy.ssc.five.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.ssc.five.FiveConstants;
import com.xy.ssc.five.dao.IFiveDAO;
import com.xy.ssc.five.model.FiveGaSession;
import com.xy.ssc.five.model.FiveGaTrend;
import com.xy.ssc.five.model.dto.FiveDTO;

public class FiveDAOHibernate   extends AbstractBaseDAOHibernate 
implements IFiveDAO {
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


	public FiveGaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
//		String todayYyyymmdd = DateTimeUtil.DateToString(now);
//		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 01:55:00");
//		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 09:50:00");
//		
//		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 21:50:00");
//		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 21:55:00");
//		
//		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
//			now=DateTimeUtil.parse(todayYyyymmdd+" 09:51:00");
//		}else if(now.compareTo(todayStart1)>=0&&now.compareTo(todayEnd1)==-1){
//			now=DateTimeUtil.parse(todayYyyymmdd+" 21:56:00");
//		}
//		
//		
//		List<FiveGaSession> list = getSession().createQuery("from FiveGaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
//		.setTimestamp(0, now)
//		.setTimestamp(1, now)
//		.setString(2, FiveConstants.FIVE_OPEN_STATUS_INIT)
//		.setString(3, FiveConstants.FIVE_OPEN_STATUS_OPENING)
//		.setMaxResults(1)
//		.list();
//		if(list!=null && list.size()>0){
//			return (FiveGaSession)list.get(0);
//		}
//		return null;
		
		Query query = getSession().createQuery("from FiveGaSession ho where ho.endTime >? order by ho.sessionNo asc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<FiveGaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public FiveGaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from FiveGaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (FiveGaSession)list.get(0);
		}
		return null;
	}
	public List<FiveGaTrend> findFiveGaTrendList(){
		String  hql=" from FiveGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public List<FiveGaTrend> findFiveGaTrendAllList(){
		String  hql=" from FiveGaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findFiveGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from FiveGaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from FiveGaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findFiveGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.ssc.five.model.dto.FiveDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from FiveGaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  FiveGaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.ssc.five.model.dto.FiveDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<FiveDTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.ssc.five.model.dto.FiveDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<FiveDTO> list = query.list();
		return list;
	}


	@Override
	public void deleteFiveGaBet(String hql, List<Object> para) {
		String delHql=" delete from FiveGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}
	
}
