package com.gf.xyft.dao.hibernate;

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
import com.gf.xyft.XyFtConstants;
import com.gf.xyft.dao.IXyFtDAO;
import com.gf.xyft.model.GfXyFtGaOmit;
import com.gf.xyft.model.GfXyFtGaSession;
import com.gf.xyft.model.GfXyFtGaTrend;
import com.gf.xyft.model.dto.GfXyFtDTO;
import com.xy.pk10.xyft.model.XyftGaSession;



public class XyFtDAOHibernate extends AbstractBaseDAOHibernate implements IXyFtDAO {
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
	
	public GfXyFtGaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		Query query = getSession().createQuery("from GfXyFtGaSession ho where ho.endTime >? order by ho.sessionNo asc ");
		query.setTimestamp(0, now);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		
		List<GfXyFtGaSession> list = query.list();
		if(list != null && list.size()>0){
			return list.get(0);	
		}else{
			return null;
		}
	}

	
	public GfXyFtGaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfXyFtGaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfXyFtGaSession)list.get(0);
		}
		return null;
	}
	public List<GfXyFtGaTrend> findGfXyFtGaTrendList(){
		String  hql=" from GfXyFtGaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfXyFtGaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfXyFtGaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfXyFtGaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findGfXyFtGaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.gf.xyft.model.dto.GfXyFtDTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfXyFtGaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_XYFT
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfXyFtGaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_XYFT
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId", para);
		Integer totalCount = count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.xyft.model.dto.GfXyFtDTO(ga,gsd,u) from GaBetSponsor ga,User u ,GaBetSponsorDetail gsd where 1=1 "
				+ " and ga.jointId = gsd.jointId "
				+ " and ga.userId = u.userId "+hql+" order by ga.jointId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ga.jointId) from GaBetSponsor ga, User u where 1=1" +
				" and  ga.userId = u.userId "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	

	@Override
	public List<GfXyFtDTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.xyft.model.dto.GfXyFtDTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfXyFtDTO> list = query.list();
		return list;
	}


	@Override
	public GfXyFtGaOmit getXyFtOmitBySessionNo(String sessionNo) {
		String hql = " from GfXyFtGaOmit ho where ho.sessionNo=? ";
		List<GfXyFtGaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfXyFtGaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfXyFtGaSession> list = getSession().createQuery("from GfXyFtGaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfXyFtGaSession)list.get(0);
		}
		return null;
	}
	public GfXyFtGaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfXyFtGaSession> list = getSession().createQuery("from GfXyFtGaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfXyFtGaSession)list.get(0);
		}
		return null;
	}
	public List<GfXyFtGaOmit> findGfXyFtGaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfXyFtGaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfXyFtGaOmit>list=query.list();
		return list;
	}
	
	public BigDecimal getHeZhiBetRate(String hezhi){
		String  hql="select gbo.betRate from GaBetOption gbo where gbo.playType=? and gbo.title=? ";
		Query query =getSession().createQuery(hql);
		//query.setString(0, XyFtConstants.GAME_TYPE_GF_HEZHI);
		query.setString(1, hezhi);
		return (BigDecimal) query.uniqueResult();
	}
}
