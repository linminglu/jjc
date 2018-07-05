package com.gf.pick11.sdpick11.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.Constants;
import com.gf.dcb.model.GfDcbGaOmit;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.gf.pick11.sdpick11.SdPick11Constants;
import com.gf.pick11.sdpick11.dao.ISdPick11DAO;
import com.gf.pick11.sdpick11.model.GfSdPick11GaOmit;
import com.gf.pick11.sdpick11.model.GfSdPick11GaSession;
import com.gf.pick11.sdpick11.model.GfSdPick11GaTrend;
import com.gf.pick11.sdpick11.model.dto.GfSdPick11DTO;



public class SdPick11DAOHibernate extends AbstractBaseDAOHibernate implements ISdPick11DAO {
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


	public GfSdPick11GaSession getCurrentSession() {
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		String todayYyyymmdd = DateTimeUtil.DateToString(now);
		Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_SDPICK11_BEGIN_TIME);

		Date todayStart1 = DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_SDPICK11_END_TIME);
		Date todayEnd1 = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		
		if(now.compareTo(todayStart)>=0&&now.compareTo(todayEnd)==-1){
			now=DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_SDPICK11_BEGIN_TIME);
		}else  if(now.compareTo(todayStart1)>0&&now.compareTo(todayEnd1)<=0){
			now=DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(todayYyyymmdd+" "+Constants.GAME_SDPICK11_BEGIN_TIME),1);
		}
			
		List<GfSdPick11GaSession> list = getSession().createQuery("from GfSdPick11GaSession gks where gks.startTime<=? and gks.endTime>? and (gks.openStatus=? or gks.openStatus=?) order by gks.sessionId")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, SdPick11Constants.SD_PICK11_OPEN_STATUS_INIT)
		.setString(3, SdPick11Constants.SD_PICK11_OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfSdPick11GaSession)list.get(0);
		}
		return null;
	}
	public GfSdPick11GaSession getPreviousSessionBySessionNo(String sessionNo){
		String  hql=" from GfSdPick11GaSession gks where gks.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (GfSdPick11GaSession)list.get(0);
		}
		return null;
	}
	public List<GfSdPick11GaTrend> findGfSdPick11GaTrendList(){
		String  hql=" from GfSdPick11GaTrend gks order by gks.trendId ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}
	public PaginationSupport  findGfSdPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " from GfSdPick11GaSession ho where 1=1 "+hql,para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GfSdPick11GaSession ho where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport  findSdPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize){
		Query query = makeQuerySQL( " select new com.gf.pick11.sdpick11.model.dto.GfSdPick11DTO(se.sessionNo,sum(ho.money),sum(ho.winCash),se.startTime,se.endTime,sum(ho.money)-sum(ho.winCash)) from GaBetSponsor ho,GfSdPick11GaSession se where 1=1 "
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_SDPICK11
				+ " and se.sessionId = ho.sessionId "+hql+" group by ho.sessionId order by ho.sessionId desc ", para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaBetSponsor ho,GfSdPick11GaSession se where 1=1" 
				+ " and ho.gameType ="+ Constants.GAME_TYPE_GF_SDPICK11
				+" and  se.sessionId = ho.sessionId "+hql+" group by ho.sessionId", para);
		Integer totalCount = (Integer) count.list().size();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.gf.pick11.sdpick11.model.dto.GfSdPick11DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<GfSdPick11DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.gf.pick11.sdpick11.model.dto.GfSdPick11DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<GfSdPick11DTO> list = query.list();
		return list;
	}


	@Override
	public GfSdPick11GaOmit getSdPick11OmitBySessionNo(String sessionNo) {
		String hql = " from GfSdPick11GaOmit ho where ho.sessionNo=? ";
		List<GfSdPick11GaOmit> list;
		list = getSession().createQuery(hql).setString(0, sessionNo).list();
		if(list!=null&&list.size()>0){//
			return list.get(0);
		}else{
			return null;
		}
		
	}
	public GfSdPick11GaSession getFirstSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfSdPick11GaSession> list = getSession().createQuery("from GfSdPick11GaSession gks where gks.startTime>=?  order by gks.sessionId")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfSdPick11GaSession)list.get(0);
		}
		return null;
	}
	public GfSdPick11GaSession getEndSessionByDate(String date){
		Date now=DateTimeUtil.parse(date);
		List<GfSdPick11GaSession> list = getSession().createQuery("from GfSdPick11GaSession gks where gks.startTime<?  order by gks.sessionId desc")
		.setTimestamp(0, now)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GfSdPick11GaSession)list.get(0);
		}
		return null;
	}
	public List<GfSdPick11GaOmit> findGfSdPick11GaOmitList(String hqls,
			List<Object> para, int num){
		String  hql=" from GfSdPick11GaOmit ho where 1=1  ";
		Query query =makeQuerySQL(hql+hqls,para);
		query.setMaxResults(num);
		List<GfSdPick11GaOmit>list=query.list();
		return list;
	}
}
