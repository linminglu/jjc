package com.gf.k3.jxk3.dao.hibernate;

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
import com.gf.k3.jxk3.JxK3Constants;
import com.gf.k3.jxk3.dao.IJxK3DAO;
import com.gf.k3.jxk3.model.GfJxK3GaOmit;
import com.gf.k3.jxk3.model.GfJxK3GaSession;
import com.gf.k3.jxk3.model.GfJxK3GaTrend;
import com.gf.k3.jxk3.model.dto.GfJxK3DTO;



public class JxK3DAOHibernate extends AbstractBaseDAOHibernate implements IJxK3DAO {
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


	public GfJxK3GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JXK3_BEGIN_TIME);


		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" 23:00:00");
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JXK3_END_TIME);
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JXK3_BEGIN_TIME);
		}else  if(now.compareTo(todayStart1)>0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_JXK3_BEGIN_TIME),1);
		}
			
		List<GfJxK3GaSession> list = getSession().createQuery("from GfJxK3GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, JxK3Constants.JX_K3_OPEN_STATUS_INIT)
		.setString(3, JxK3Constants.JX_K3_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJxK3GaSession)list.get(0);
		}
		return null;
	}
	public GfJxK3GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfJxK3GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfJxK3GaSession)list.get(0);
		}
		return null;
	}
	public List<GfJxK3GaTrend> findGfJxK3GaTrendList(){
		String  hql=" from GfJxK3GaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfJxK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfJxK3GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfJxK3GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findGfJxK3GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.gf.k3.jxk3.model.dto.GfJxK3DTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfJxK3GaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_JXK3
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfJxK3GaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_JXK3
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId ", para);
		Integer totalCount = count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.k3.jxk3.model.dto.GfJxK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<GfJxK3DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.k3.jxk3.model.dto.GfJxK3DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfJxK3DTO> list = query.list();
		return list;
	}


	@Override
	public GfJxK3GaOmit getJxK3OmitBySessionNo(String sessionNo) {
		String hql = " from GfJxK3GaOmit ho where ho.sessionNo=? ";
		List<GfJxK3GaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfJxK3GaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfJxK3GaSession> list = getSession().createQuery("from GfJxK3GaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJxK3GaSession)list.get(0);
		}
		return null;
	}
	public GfJxK3GaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfJxK3GaSession> list = getSession().createQuery("from GfJxK3GaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfJxK3GaSession)list.get(0);
		}
		return null;
	}
	public List<GfJxK3GaOmit> findGfJxK3GaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfJxK3GaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfJxK3GaOmit>list=query.list();
		return list;
	}
	
	public BigDecimal getHeZhiBetRate(String hezhi){
		String  hql="select gbo.betRate from GaBetOption gbo where gbo.playType=? and gbo.title=? and gbo.gameType=?";
		Query query =getSession().createQuery(hql);
		query.setString(0, JxK3Constants.GAME_TYPE_GF_HEZHI);
		query.setString(1, hezhi);
		query.setString(2, Constants.GAME_TYPE_GF_JXK3);
		return (BigDecimal) query.uniqueResult();
	}
}
