package com.xy.pk10.jsft.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.xy.pk10.jsft.JsftConstants;
import com.xy.pk10.jsft.dao.IJsftDAO;
import com.xy.pk10.jsft.model.JsftGaSession;
import com.xy.pk10.jsft.model.JsftGaTrend;
import com.xy.pk10.jsft.model.dto.JsftDTO;


public class JsftDAOHibernate extends AbstractBaseDAOHibernate 
implements IJsftDAO {
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


	public JsftGaSession getCurrentSession() {
//		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		
		List<JsftGaSession> list = getSession().createQuery("from JsftGaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId  ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, JsftConstants.JSFT_OPEN_STATUS_INIT)
		.setString(3, JsftConstants.JSFT_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (JsftGaSession)list.get(0);
		}
		return null;
	}
	public JsftGaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from JsftGaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (JsftGaSession)list.get(0);
		}
		return null;
	}
	public List<JsftGaTrend> findJsftGaTrendList(){
		String  hql=" from JsftGaTrend gks where gks.trendCount >1 order by gks.trendCount desc";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public List<JsftGaTrend> findJsftGaTrendAllList(){
		String  hql=" from JsftGaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	
	public PaginationSupport  findJsftGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from JsftGaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from JsftGaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport  findJsftGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.xy.pk10.jsft.model.dto.JsftDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from JsftGaBet ho where 1=1 "
				+ hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from  JsftGaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.pk10.jsft.model.dto.JsftDTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<JsftDTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.pk10.jsft.model.dto.JsftDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<JsftDTO> list = query.list();
		return list;
	}


	@Override
	public void deleteJsftGaBet(String hql, List<Object> para) {
		String delHql=" delete from JsftGaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}

}
