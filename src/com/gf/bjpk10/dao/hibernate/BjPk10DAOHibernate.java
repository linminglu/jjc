package com.gf.bjpk10.dao.hibernate;

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
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.gf.bjpk10.BjPk10Constants;
import com.gf.bjpk10.dao.IBjPk10DAO;
import com.gf.bjpk10.model.GfBjPk10GaOmit;
import com.gf.bjpk10.model.GfBjPk10GaSession;
import com.gf.bjpk10.model.GfBjPk10GaTrend;
import com.gf.bjpk10.model.dto.GfBjPk10DTO;



public class BjPk10DAOHibernate extends AbstractBaseDAOHibernate implements IBjPk10DAO {
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


	public GfBjPk10GaSession getCurrentSession() {
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
		
		List<GfBjPk10GaSession> list = getSession().createQuery("from GfBjPk10GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId asc ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT)
		.setString(3, BjPk10Constants.BJ_PK10_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (GfBjPk10GaSession)list.get(0);
		}
		return null;
	}
	
	public GfBjPk10GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfBjPk10GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfBjPk10GaSession)list.get(0);
		}
		return null;
	}
	public List<GfBjPk10GaTrend> findGfBjPk10GaTrendList(){
		String  hql=" from GfBjPk10GaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfBjPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfBjPk10GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfBjPk10GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findGfBjPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		
		Query query = makeQuerySQL( " select new com.gf.bjpk10.model.dto.GfBjPk10DTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfBjPk10GaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_BJPK10
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfBjPk10GaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_BJPK10
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId ", para);
		Integer totalCount = count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.bjpk10.model.dto.GfBjPk10DTO(ga,gsd,u) from GaBetSponsor ga,User u ,GaBetSponsorDetail gsd where 1=1 "
				+ " and ga.jointId = gsd.jointId "
				+ " and ga.userId = u.userId "+hql+" order by ga.jointId desc ", para);
		query.setFirstResult(pageNum);
		
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ga.jointId) from GaBetSponsor ga, User u ga_bet_sponsor_detail gsd where 1=1" 
				+ " and ga.jointId = gsd.jointId "
				
				+ " and  ga.userId = u.userId "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public List<GfBjPk10DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.bjpk10.model.dto.GfBjPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfBjPk10DTO> list = query.list();
		return list;
	}


	@Override
	public GfBjPk10GaOmit getBjPk10OmitBySessionNo(String sessionNo) {
		String hql = " from GfBjPk10GaOmit ho where ho.sessionNo=? ";
		List<GfBjPk10GaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfBjPk10GaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfBjPk10GaSession> list = getSession().createQuery("from GfBjPk10GaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfBjPk10GaSession)list.get(0);
		}
		return null;
	}
	public GfBjPk10GaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfBjPk10GaSession> list = getSession().createQuery("from GfBjPk10GaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfBjPk10GaSession)list.get(0);
		}
		return null;
	}
	public List<GfBjPk10GaOmit> findGfBjPk10GaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfBjPk10GaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfBjPk10GaOmit>list=query.list();
		return list;
	}
	
	public BigDecimal getHeZhiBetRate(String hezhi){
		String  hql="select gbo.betRate from GaBetOption gbo where gbo.playType=? and gbo.title=? ";
		Query query =getSession().createQuery(hql);
		//query.setString(0, BjPk10Constants.GAME_TYPE_GF_HEZHI);
		query.setString(1, hezhi);
		return (BigDecimal) query.uniqueResult();
	}
}
