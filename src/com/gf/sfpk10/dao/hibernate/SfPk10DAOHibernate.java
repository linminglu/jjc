package com.gf.sfpk10.dao.hibernate;

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
import com.gf.sfpk10.SfPk10Constants;
import com.gf.sfpk10.dao.ISfPk10DAO;
import com.gf.sfpk10.model.GfSfPk10GaOmit;
import com.gf.sfpk10.model.GfSfPk10GaSession;
import com.gf.sfpk10.model.GfSfPk10GaTrend;
import com.gf.sfpk10.model.dto.GfSfPk10DTO;
import com.xy.pk10.sfpk10.model.SfPk10GaSession;



public class SfPk10DAOHibernate extends AbstractBaseDAOHibernate implements ISfPk10DAO {
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

	public GfSfPk10GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		
		List<GfSfPk10GaSession> list = getSession().createQuery("from GfSfPk10GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId  ")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, SfPk10Constants.SF_PK10_OPEN_STATUS_INIT)
		.setString(3, SfPk10Constants.SF_PK10_OPEN_STATUS_OPENING)
		.list();
		if(list!=null && list.size()>0){
			return (GfSfPk10GaSession)list.get(0);
		}
		return null;
	}

	public GfSfPk10GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfSfPk10GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfSfPk10GaSession)list.get(0);
		}
		return null;
	}
	public List<GfSfPk10GaTrend> findGfSfPk10GaTrendList(){
		String  hql=" from GfSfPk10GaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfSfPk10GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfSfPk10GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfSfPk10GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findGfSfPk10GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.gf.sfpk10.model.dto.GfSfPk10DTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfSfPk10GaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_SFPK10
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfSfPk10GaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_SFPK10
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId ", para);
		Integer totalCount = count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.sfpk10.model.dto.GfSfPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<GfSfPk10DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.sfpk10.model.dto.GfSfPk10DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfSfPk10DTO> list = query.list();
		return list;
	}


	@Override
	public GfSfPk10GaOmit getSfPk10OmitBySessionNo(String sessionNo) {
		String hql = " from GfSfPk10GaOmit ho where ho.sessionNo=? ";
		List<GfSfPk10GaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfSfPk10GaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfSfPk10GaSession> list = getSession().createQuery("from GfSfPk10GaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfSfPk10GaSession)list.get(0);
		}
		return null;
	}
	public GfSfPk10GaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfSfPk10GaSession> list = getSession().createQuery("from GfSfPk10GaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfSfPk10GaSession)list.get(0);
		}
		return null;
	}
	public List<GfSfPk10GaOmit> findGfSfPk10GaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfSfPk10GaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfSfPk10GaOmit>list=query.list();
		return list;
	}
	
	public BigDecimal getHeZhiBetRate(String hezhi){
		String  hql="select gbo.betRate from GaBetOption gbo where gbo.playType=? and gbo.title=? ";
		Query query =getSession().createQuery(hql);
		//query.setString(0, SfPk10Constants.GAME_TYPE_GF_HEZHI);
		query.setString(1, hezhi);
		return (BigDecimal) query.uniqueResult();
	}
}
