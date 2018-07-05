package com.xy.hk.sflhc.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.hk.sflhc.SflhcConstants;
import com.xy.hk.sflhc.dao.ISflhcDAO;
import com.xy.hk.sflhc.model.SflhcGaSession;
import com.xy.hk.sflhc.model.SflhcGaTrend;
import com.xy.hk.sflhc.model.dto.SflhcDTO;

public class SflhcDAOHiberate extends AbstractBaseDAOHibernate implements
		ISflhcDAO {

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
	public SflhcGaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 20:40:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 00:03:00");
			now = DateTimeUtil.getDateTimeOfDays(now, 1);//第二天第一期
		}
		List<SflhcGaSession> list = getSession()
				.createQuery(
						"from SflhcGaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
				.setTimestamp(0, now).setTimestamp(1, now)
				.setString(2, SflhcConstants.OPEN_STATUS_INIT)
				.setString(3, SflhcConstants.OPEN_STATUS_OPENING)
				.setMaxResults(1).list();
		if (list != null && list.size() > 0) {
			return (SflhcGaSession) list.get(0);
		}
		return null;
	}

	public SflhcGaSession getPreviousSessionBySessionNo(String sessionNo) {
		String hql = " from SflhcGaSession gks where gks.sessionNo=? ";
		Query record = getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list = record.list();
		if (list != null && list.size() > 0) {
			return (SflhcGaSession) list.get(0);
		}
		return null;
	}

	public List<SflhcGaTrend> findSflhcGaTrendList() {
		String hql = " from SflhcGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record = getSession().createQuery(hql);
		List list = record.list();
		return list;
	}

	public PaginationSupport findSflhcGaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize) {
		Query query = makeQuerySQL(" from SflhcGaSession ho where 1=1 " + hql,
				para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL(
				"select count(ho.sessionId) from SflhcGaSession ho where 1=1 "
						+ hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public List<SflhcGaTrend> findSflhcGaTrendAllList() {
		String hql = " from SflhcGaTrend gks where 1=1 ";
		Query record = getSession().createQuery(hql);
		List list = record.list();
		return list;
	}

	@Override
	public PaginationSupport findSflhcGaBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL(
				" select new com.xy.hk.sflhc.model.dto.SflhcDTO(se.sessionNo,ho.totalPoint,ho.winCash,se.startTime,se.endTime,ho.totalPoint-ho.winCash) from SflhcGaBet ho,SflhcGaSession se where 1=1 "
						+ " and se.sessionId = ho.sessionId "
						+ hql
						+ " group by ho.sessionId order by ho.sessionId desc ",
				para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL(
				"select count(ho.betId) from SflhcGaBet ho,SflhcGaSession se where 1=1"
						+ " and  se.sessionId = ho.sessionId " + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL(
				" select new com.xy.hk.sflhc.model.dto.SflhcDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<SflhcDTO> findGaBetDetailById(String hql, List<Object> para) {
		Query query = getSession()
				.createQuery(
						" select new com.xy.hk.sflhc.model.dto.SflhcDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
								+ hql).setParameter(0, para.get(0));
		List<SflhcDTO> list = query.list();
		return list;
	}

	@Override
	public void deleteSflhcGaBet(String hql, List<Object> para) {
		String delHql=" delete from SflhcGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
