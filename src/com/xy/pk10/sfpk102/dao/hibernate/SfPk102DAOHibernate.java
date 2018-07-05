package com.xy.pk10.sfpk102.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.pk10.sfpk102.SfPk102Constants;
import com.xy.pk10.sfpk102.dao.ISfPk102DAO;
import com.xy.pk10.sfpk102.model.SfPk102GaSession;
import com.xy.pk10.sfpk102.model.SfPk102GaTrend;
import com.xy.pk10.sfpk102.model.dto.SfPk102DTO;


public class SfPk102DAOHibernate extends AbstractBaseDAOHibernate 
implements ISfPk102DAO {
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


	public SfPk102GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		
		List<SfPk102GaSession> list = getSession().createQuery("from SfPk102GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId  ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, SfPk102Constants.SF_PK102_OPEN_STATUS_INIT)
		.setString(3, SfPk102Constants.SF_PK102_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (SfPk102GaSession)list.get(0);
		}
		return null;
	}
	public SfPk102GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from SfPk102GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (SfPk102GaSession)list.get(0);
		}
		return null;
	}
	public List<SfPk102GaTrend> findSfPk102GaTrendList(){
		String  hql=" from SfPk102GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public List<SfPk102GaTrend> findSfPk102GaTrendAllList(){
		String  hql=" from SfPk102GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public PaginationSupport  findSfPk102GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from SfPk102GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from SfPk102GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findSfPk102GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.pk10.sfpk102.model.dto.SfPk102DTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from SfPk10GaBet ho where 1=1 "
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
		Query query = makeQuerySQL( " select new com.xy.pk10.sfpk102.model.dto.SfPk102DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<SfPk102DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.pk10.sfpk102.model.dto.SfPk102DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<SfPk102DTO> list = query.list();
		return list;
	}


	@Override
	public void deleteSfPk102GaBet(String hql, List<Object> para) {
		String delHql=" delete from SfPk102GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
