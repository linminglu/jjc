package com.xy.ssc.bjssc.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.ssc.bjssc.BjSscConstants;
import com.xy.ssc.bjssc.dao.IBjSscDAO;
import com.xy.ssc.bjssc.model.BjSscGaSession;
import com.xy.ssc.bjssc.model.BjSscGaTrend;
import com.xy.ssc.bjssc.model.dto.BjSscDTO;

public class BjSscDAOHibernate   extends AbstractBaseDAOHibernate 
implements IBjSscDAO {
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


	public BjSscGaSession getCurrentSession() {
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
//		List<BjSscGaSession> list = getSession().createQuery("from BjSscGaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
//		.setTimestamp(0, now)
//		.setTimestamp(1, now)
//		.setString(2, BjSscConstants.BJ_SSC_OPEN_STATUS_INIT)
//		.setString(3, BjSscConstants.BJ_SSC_OPEN_STATUS_OPENING)
//		.setMaxResults(1)
//		.list();
//		if(list!=null && list.size()>0){
//			return (BjSscGaSession)list.get(0);
//		}
//		return null;
		Query query = getSession().createQuery("from BjSscGaSession ho where ho.endTime >? order by ho.sessionNo asc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<BjSscGaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
		
	}
	public BjSscGaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from BjSscGaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (BjSscGaSession)list.get(0);
		}
		return null;
	}
	public List<BjSscGaTrend> findBjSscGaTrendList(){
		String  hql=" from BjSscGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public List<BjSscGaTrend> findBjSscGaTrendAllList(){
		String  hql=" from BjSscGaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findBjSscGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from BjSscGaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from BjSscGaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findBjSscGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.ssc.bjssc.model.dto.BjSscDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from BjSscGaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  BjSscGaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.ssc.bjssc.model.dto.BjSscDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<BjSscDTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.ssc.bjssc.model.dto.BjSscDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<BjSscDTO> list = query.list();
		return list;
	}


	@Override
	public void deleteBjSscGaBet(String hql, List<Object> para) {
		String delHql=" delete from BjSscGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}
	
}
