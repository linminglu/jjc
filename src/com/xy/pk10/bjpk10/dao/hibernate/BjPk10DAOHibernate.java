package com.xy.pk10.bjpk10.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.bj3.model.dto.Bj3DTO;
import com.xy.pk10.bjpk10.BjPk10Constants;
import com.xy.pk10.bjpk10.dao.IBjPk10DAO;
import com.xy.pk10.bjpk10.model.BjPk10GaSession;
import com.xy.pk10.bjpk10.model.BjPk10GaTrend;
import com.xy.pk10.bjpk10.model.dto.BjPk10DTO;

public class BjPk10DAOHibernate extends AbstractBaseDAOHibernate 
implements IBjPk10DAO {
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


	public BjPk10GaSession getCurrentSession() {
//		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 09:02:00");
		
		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 23:57:00");
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" 09:03:00");
		}else if(now.compareTo(todayStart1)>=0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" 09:03:00"),1);
		}
		
//		Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, BjPk10Constants.BJ_PK10__TIME_INTERVAL);
		
//		log.info(DateTimeUtil.DateToStringAll(now));
//		log.info(DateTimeUtil.DateToStringAll(nowEnd));
//		log.info("<<<<<<<<<<<<<<<<<<<<<<<<");
		List<BjPk10GaSession> list = getSession().createQuery("from BjPk10GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId asc ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)
		.setString(3, BjPk10Constants.BJ_PK10_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (BjPk10GaSession)list.get(0);
		}
		return null;
	}
	public BjPk10GaSession getPreviousSessionBySessionNo(String sessionNo){
		try {
			return (BjPk10GaSession)getSession().createQuery("from BjPk10GaSession gks where gks.sessionNo=?")
			.setString(0, sessionNo)
			.uniqueResult();
		} catch (Exception e) {
			log.error("not found sessionNo["+sessionNo+"]"+e.getMessage());
		}
		return null;
	}
	public List<BjPk10GaTrend> findBjPk10GaTrendList(){
		String  hql=" from BjPk10GaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public List<BjPk10GaTrend> findBjPk10GaTrendAllList(){
		String  hql=" from BjPk10GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public PaginationSupport  findBjPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from BjPk10GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from BjPk10GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findBjPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.pk10.bjpk10.model.dto.BjPk10DTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from BjPk10GaBet ho where 1=1 "
				+ hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  BjPk10GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.pk10.bjpk10.model.dto.BjPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<BjPk10DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.pk10.bjpk10.model.dto.BjPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<BjPk10DTO> list = query.list();
		return list;
	}


	@Override
	public void deleteBjPk10GaBet(String hql, List<Object> para) {
		String delHql=" delete from BjPk10GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
