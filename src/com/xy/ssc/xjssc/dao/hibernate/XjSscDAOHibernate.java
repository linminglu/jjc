package com.xy.ssc.xjssc.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.bj3.model.dto.Bj3DTO;
import com.xy.ssc.xjssc.XjSscConstants;
import com.xy.ssc.xjssc.dao.IXjSscDAO;
import com.xy.ssc.xjssc.model.XjSscGaSession;
import com.xy.ssc.xjssc.model.XjSscGaTrend;
import com.xy.ssc.xjssc.model.dto.XjSscDTO;

public class XjSscDAOHibernate   extends AbstractBaseDAOHibernate 
implements IXjSscDAO {
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


	public XjSscGaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 02:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 10:00:00");
		
//		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 21:50:00");
//		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 21:55:00");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 10:05:00");
		}
//		else if(now.compareTo(todayStart1)>=0&&now.compareTo(todayEnd1)==-1){
//			now=DateTimeUtil.parse(todayYyyymmdd+" 21:56:00");
//		}
		
		List<XjSscGaSession> list = getSession().createQuery("from XjSscGaSession gks where gks.startTime<=? and gks.endTime>? order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (XjSscGaSession)list.get(0);
		}
		return null;
	}
	
	public XjSscGaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from XjSscGaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (XjSscGaSession)list.get(0);
		}
		return null;
	}
	public List<XjSscGaTrend> findXjSscGaTrendList(){
		String  hql=" from XjSscGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public List<XjSscGaTrend> findXjSscGaTrendAllList(){
		String  hql=" from XjSscGaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findXjSscGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from XjSscGaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from XjSscGaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findXjSscGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.ssc.xjssc.model.dto.XjSscDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from XjSscGaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  XjSscGaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.ssc.xjssc.model.dto.XjSscDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<XjSscDTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.ssc.xjssc.model.dto.XjSscDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<XjSscDTO> list = query.list();
		return list;
	}


	@Override
	public void deleteXjSscGaBet(String hql, List<Object> para) {
		String delHql=" delete from XjSscGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}
	
}
