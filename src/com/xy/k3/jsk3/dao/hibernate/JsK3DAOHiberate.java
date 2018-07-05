package com.xy.k3.jsk3.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.k3.jsk3.JsK3Constants;
import com.xy.k3.jsk3.dao.IJsK3DAO;
import com.xy.k3.jsk3.model.JsK3GaSession;
import com.xy.k3.jsk3.model.JsK3GaTrend;
import com.xy.k3.jsk3.model.dto.JsK3DTO;

public class JsK3DAOHiberate  extends AbstractBaseDAOHibernate implements IJsK3DAO {

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
	
	@SuppressWarnings("unchecked")
	public JsK3GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 08:30:00");


		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 22:10:00");
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 08:33:00");
		}else  if(now.compareTo(todayStart1)>0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" 08:33:00"),1);
		}
		
		List<JsK3GaSession> list = getSession().createQuery("from JsK3GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, JsK3Constants.JSK3_OPEN_STATUS_INIT)
		.setString(3, JsK3Constants.JSK3_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (JsK3GaSession)list.get(0);
		}
		return null;
	}

	
	public JsK3GaSession getPreviousSessionBySessionNo(String sessionNo) {
		String  hql=" from JsK3GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (JsK3GaSession)list.get(0);
		}
		return null;
	}

	
	public List<JsK3GaTrend> findJsK3GaTrendList() {
		String  hql=" from JsK3GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}

	
	public PaginationSupport findJsK3GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " from JsK3GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from JsK3GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List<JsK3GaTrend> findJsK3GaTrendAllList(){
		String  hql=" from JsK3GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	@Override
	public PaginationSupport findJsK3GaBetList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.k3.jsk3.model.dto.JsK3BetDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from JsK3GaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from JsK3GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.k3.jsk3.model.dto.JsK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<JsK3DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.k3.jsk3.model.dto.JsK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<JsK3DTO> list = query.list();
		return list;
	}

	@Override
	public void deleteJsK3GaBet(String hql, List<Object> para) {
		String delHql=" delete from JsK3GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
