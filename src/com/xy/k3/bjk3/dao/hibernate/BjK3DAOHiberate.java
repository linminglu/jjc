package com.xy.k3.bjk3.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.k3.bjk3.BjK3Constants;
import com.xy.k3.bjk3.dao.IBjK3DAO;
import com.xy.k3.bjk3.model.BjK3GaSession;
import com.xy.k3.bjk3.model.BjK3GaTrend;
import com.xy.k3.bjk3.model.dto.BjK3DTO;

public class BjK3DAOHiberate  extends AbstractBaseDAOHibernate implements IBjK3DAO {

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
	public BjK3GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 09:00:00");

		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 09:01:00");
		}
		
		List<BjK3GaSession> list = getSession().createQuery("from BjK3GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, BjK3Constants.BJK3_OPEN_STATUS_INIT)
		.setString(3, BjK3Constants.BJK3_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (BjK3GaSession)list.get(0);
		}
		return null;
	}

	
	public BjK3GaSession getPreviousSessionBySessionNo(String sessionNo) {
		String  hql=" from BjK3GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (BjK3GaSession)list.get(0);
		}
		return null;
	}

	
	public List<BjK3GaTrend> findBjK3GaTrendList() {
		String  hql=" from BjK3GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}

	
	public PaginationSupport findBjK3GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " from BjK3GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from BjK3GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List<BjK3GaTrend> findBjK3GaTrendAllList(){
		String  hql=" from BjK3GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	@Override
	public PaginationSupport findBjK3GaBetList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.k3.bjk3.model.dto.BjK3BetDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from BjK3GaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from BjK3GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.k3.bjk3.model.dto.BjK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<BjK3DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.k3.bjk3.model.dto.BjK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<BjK3DTO> list = query.list();
		return list;
	}

	@Override
	public void deleteBjK3GaBet(String hql, List<Object> para) {
		String delHql=" delete from BjK3GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
