package com.xy.hk.marksix.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.hk.marksix.MarkSixConstants;
import com.xy.hk.marksix.dao.IMarkSixDAO;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.MarkSixGaTrend;
import com.xy.hk.marksix.model.dto.MarkSixDTO;

public class MarkSixDAOHiberate extends AbstractBaseDAOHibernate implements
		IMarkSixDAO {

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

	public MarkSixGaSession getCurrentSession(Date now) {
		Query query = getSession().createQuery("from MarkSixGaSession ho where ho.endTime >? order by ho.sessionNo asc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<MarkSixGaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public MarkSixGaSession getPreviousSessionBySessionNo(String sessionNo) {
		String hql = " from MarkSixGaSession gks where gks.sessionNo=? ";
		Query record = getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list = record.list();
		if (list != null && list.size() > 0) {
			return (MarkSixGaSession) list.get(0);
		}
		return null;
	}

	public List<MarkSixGaTrend> findMarkSixGaTrendList() {
		String hql = " from MarkSixGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record = getSession().createQuery(hql);
		List list = record.list();
		return list;
	}

	public PaginationSupport findMarkSixGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL(" from MarkSixGaSession ho where 1=1 " + hql,
				para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL(
				"select count(ho.sessionId) from MarkSixGaSession ho where 1=1 "
						+ hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public List<MarkSixGaTrend> findMarkSixGaTrendAllList() {
		String hql = " from MarkSixGaTrend gks where 1=1 ";
		Query record = getSession().createQuery(hql);
		List list = record.list();
		return list;
	}

	@Override
	public PaginationSupport findMarkSixGaBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL(
				" select new com.xy.hk.marksix.model.dto.MarkSixDTO(se.sessionNo,ho.totalPoint,ho.winCash,se.startTime,se.endTime,ho.totalPoint-ho.winCash) from MarkSixGaBet ho,MarkSixGaSession se where 1=1 "
						+ " and se.sessionId = ho.sessionId "
						+ hql
						+ " group by ho.sessionId order by ho.sessionId desc ",
				para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL(
				"select count(ho.betId) from MarkSixGaBet ho,MarkSixGaSession se where 1=1"
						+ " and  se.sessionId = ho.sessionId " + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL(
				" select new com.xy.hk.marksix.model.dto.MarkSixDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
						+ " and ga.userId = u.userId "
						+ hql
						+ " order by ga.betDetailId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL(
				"select count(ga.betDetailId) from GaBetDetail ga, User u where 1=1"
						+ " and  ga.userId = u.userId " + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public List<MarkSixDTO> findGaBetDetailById(String hql, List<Object> para) {
		Query query = getSession()
				.createQuery(
						" select new com.xy.hk.marksix.model.dto.MarkSixDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
								+ hql).setParameter(0, para.get(0));
		List<MarkSixDTO> list = query.list();
		return list;
	}

	public MarkSixGaSession getPreSession(Date now) {
		Query query = getSession().createQuery("from MarkSixGaSession ho where ho.endTime <=? order by ho.sessionNo desc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<MarkSixGaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void deleteMarkSixGaBet(String hql, List<Object> para) {
		String delHql=" delete from MarkSixGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}
}
