package com.xy.pk10.sfpk10.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.bj3.model.dto.Bj3DTO;
import com.xy.pk10.sfpk10.SfPk10Constants;
import com.xy.pk10.sfpk10.dao.ISfPk10DAO;
import com.xy.pk10.sfpk10.model.SfPk10GaSession;
import com.xy.pk10.sfpk10.model.SfPk10GaTrend;
import com.xy.pk10.sfpk10.model.dto.SfPk10DTO;


public class SfPk10DAOHibernate extends AbstractBaseDAOHibernate 
implements ISfPk10DAO {
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
	public SfPk10GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		List<SfPk10GaSession> list = getSession().createQuery("from SfPk10GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId  ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, SfPk10Constants.SF_PK10_OPEN_STATUS_INIT)
		.setString(3, SfPk10Constants.SF_PK10_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (SfPk10GaSession)list.get(0);
		}
		return null;
	}
	public SfPk10GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from SfPk10GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (SfPk10GaSession)list.get(0);
		}
		return null;
	}
	public List<SfPk10GaTrend> findSfPk10GaTrendList(){
		String  hql=" from SfPk10GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SfPk10GaTrend> findSfPk10GaTrendAllList(){
		return getSession().createQuery("from SfPk10GaTrend gks").list();
	}
	
	public PaginationSupport  findSfPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from SfPk10GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from SfPk10GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findSfPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.pk10.sfpk10.model.dto.SfPk10DTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from SfPk10GaBet ho where 1=1 "
				+ hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  SfPk10GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.pk10.sfpk10.model.dto.SfPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<SfPk10DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.pk10.sfpk10.model.dto.SfPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<SfPk10DTO> list = query.list();
		return list;
	}


	@Override
	public void deleteSfPk10GaBet(String hql, List<Object> para) {
		String delHql=" delete from SfPk10GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
