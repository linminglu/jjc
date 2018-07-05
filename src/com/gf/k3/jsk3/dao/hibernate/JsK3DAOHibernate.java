package com.gf.k3.jsk3.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.Constants;
import com.gf.dcb.model.GfDcbGaOmit;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.gf.k3.jsk3.JsK3Constants;
import com.gf.k3.jsk3.dao.IJsK3DAO;
import com.gf.k3.jsk3.model.GfJsK3GaOmit;
import com.gf.k3.jsk3.model.GfJsK3GaSession;
import com.gf.k3.jsk3.model.GfJsK3GaTrend;
import com.gf.k3.jsk3.model.dto.GfJsK3DTO;



public class JsK3DAOHibernate extends AbstractBaseDAOHibernate implements IJsK3DAO {
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


	public GfJsK3GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JSK3_BEGIN_TIME);


		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JSK3_END_TIME);
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JSK3_BEGIN_TIME);
		}else  if(now.compareTo(todayStart1)>0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JSK3_BEGIN_TIME),1);
		}
			
		List<GfJsK3GaSession> list = getSession().createQuery("from GfJsK3GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, JsK3Constants.JS_K3_OPEN_STATUS_INIT)
		.setString(3, JsK3Constants.JS_K3_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJsK3GaSession)list.get(0);
		}
		return null;
	}
	public GfJsK3GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfJsK3GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfJsK3GaSession)list.get(0);
		}
		return null;
	}
	public List<GfJsK3GaTrend> findGfJsK3GaTrendList(){
		String  hql=" from GfJsK3GaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfJsK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfJsK3GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfJsK3GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findGfJsK3GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.gf.k3.jsk3.model.dto.GfJsK3DTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfJsK3GaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_JSK3
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfJsK3GaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_JSK3
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId ", para);
		Integer totalCount = count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.k3.jsk3.model.dto.GfJsK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<GfJsK3DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.k3.jsk3.model.dto.GfJsK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfJsK3DTO> list = query.list();
		return list;
	}


	@Override
	public GfJsK3GaOmit getJsK3OmitBySessionNo(String sessionNo) {
		String hql = " from GfJsK3GaOmit ho where ho.sessionNo=? ";
		List<GfJsK3GaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfJsK3GaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfJsK3GaSession> list = getSession().createQuery("from GfJsK3GaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJsK3GaSession)list.get(0);
		}
		return null;
	}
	public GfJsK3GaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfJsK3GaSession> list = getSession().createQuery("from GfJsK3GaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJsK3GaSession)list.get(0);
		}
		return null;
	}
	public List<GfJsK3GaOmit> findGfJsK3GaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfJsK3GaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfJsK3GaOmit>list=query.list();
		return list;
	}
	
	public BigDecimal getHeZhiBetRate(String hezhi){
		String  hql="select gbo.betRate from GaBetOption gbo where gbo.playType=? and gbo.title=? and gbo.gameType=?";
		Query query =getSession().createQuery(hql);
		query.setString(0, JsK3Constants.GAME_TYPE_GF_HEZHI);
		query.setString(1, hezhi);
		query.setString(2, Constants.GAME_TYPE_GF_JSK3);
		return (BigDecimal) query.uniqueResult();
	}
}
