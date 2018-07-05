package com.game.dao.hibernate;


import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.game.GameConstants;
import com.game.dao.IGaK3DAO;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;

public class GaK3DAOHibernate extends AbstractBaseDAOHibernate 
	implements IGaK3DAO {
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

	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaK3Session gks where gks.openStatus = 2 ";
		String findCount = "select count(gks.sessionId) from  GaK3Session gks where gks.openStatus = 2 ";
		
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaK3Session gks where gks.openStatus != 0  ";
		String findCount = "select count(gks.sessionId) from GaK3Session gks where gks.openStatus != 0 ";
		
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
//		String findList = "select new com.game.model.dto.GaK3DTO(gkb,gks) from GaK3Bet gkb,GaK3Session gks,User u,GaOrder go,BetOrderRl bor where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and gks.openStatus !=0 and bor.betId = gkb.betId and bor.orderId = go.orderId";
//		String findCount = "select count(gkb.betId) from GaK3Bet gkb,GaK3Session gks,User u,GaOrder go,BetOrderRl bor where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and gks.openStatus !=0 and bor.betId = gkb.betId and bor.orderId = go.orderId";
		String findList = "select new com.game.model.dto.GaK3DTO(gkb,gks) from GaK3Bet gkb,GaK3Session gks,User u where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and gks.openStatus !=0 ";
		String findCount = "select count(gkb.betId) from GaK3Bet gkb,GaK3Session gks,User u where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and gks.openStatus !=0 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize){
//		String findList = "select new com.game.model.dto.GaK3DTO(gs.sessionNo,ho)  from GaK3Session gs,BetOrderRl r,GaK3Bet  ho,GaOrder o where  ho.betId = r.betId  and r.orderId =o.orderId and o.payStatus='1' and o.type='2' and gs.sessionId=ho.sessionId  ";
//		String findCount = "select count(ho.betId) from BetOrderRl r,GaK3Bet  ho,GaOrder o where  ho.betId = r.betId  and r.orderId =o.orderId and o.payStatus='1' and o.type='2' ";
		String findList = "select new com.game.model.dto.GaK3DTO(gs.sessionNo,ho) from GaK3Session gs,GaK3Bet ho where gs.sessionId=ho.sessionId  ";
		String findCount = "select count(ho.betId) from GaK3Session gs,GaK3Bet ho where gs.sessionId=ho.sessionId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize){
//		String findList = "select new com.game.model.dto.GaK3DTO(gkb,gks,u.loginName) from GaK3Bet gkb,GaK3Session gks,User u,GaOrder go,BetOrderRl bor where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and bor.betId = gkb.betId and bor.orderId = go.orderId";
//		String findCount = "select count(gkb.betId) from GaK3Bet gkb,GaK3Session gks,User u,GaOrder go,BetOrderRl bor where gkb.sessionId=gks.sessionId and gkb.userId=u.userId and bor.betId = gkb.betId and bor.orderId = go.orderId";
		String findList = "select new com.game.model.dto.GaK3DTO(gkb,gks,u.loginName) from GaK3Bet gkb,GaK3Session gks,User u where gkb.sessionId=gks.sessionId and gkb.userId=u.userId ";
		String findCount = "select count(gkb.betId) from GaK3Bet gkb,GaK3Session gks,User u where gkb.sessionId=gks.sessionId and gkb.userId=u.userId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaK3DTO(gkbd,gks) from GaK3BetDetail gkbd,GaK3Session gks,User u,GaK3Bet gkb where gkbd.sessionId=gks.sessionId and gkbd.userId=u.userId and gkb.betId = gkbd.betId";
		String findCount = "select count(gkbd.betDetailId) from GaK3BetDetail gkbd,GaK3Session gks,User u,GaK3Bet gkb where gkbd.sessionId=gks.sessionId and gkbd.userId=u.userId and gkb.betId = gkbd.betId";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public GaK3Session getCurrentSession(){
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		//String todayYyyymmdd = DateTimeUtil.DateToString(now);
		//Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
		Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, GameConstants.K3_TIME_ADD);
		
		List<GaK3Session> list = getSession().createQuery("from GaK3Session gks where gks.startTime<=? and gks.endTime>? and gks.openStatus=?")
		.setTimestamp(0, nowEnd)
		.setTimestamp(1, nowEnd)
		.setString(2, GameConstants.OPEN_STATUS_INIT)
//		.setString(3, GameConstants.OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GaK3Session)list.get(0);
		}
		return null;
	}
	public GaK3Session getGaK3SessionBySessionNo(String sessionNo){
		String hql="  from GaK3Session ho where ho.sessionNo=? and ho.openStatus='0' ";	
		Query query = getSession().createQuery(hql);
		// query.setCacheable(true);
		query.setString(0, sessionNo);
//		query.setString(1, password);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (GaK3Session) list.get(0);
		} else {
			return null;
		}

	}
	public GaK3Session getGaK3SessionBySessionNo(String sessionNo,String openStatus){
		String hql="  from GaK3Session ho where ho.sessionNo=? and ho.openStatus=? ";	
		Query query = getSession().createQuery(hql);
		query.setString(0, sessionNo);
		query.setString(1, openStatus);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (GaK3Session) list.get(0);
		} else {
			return null;
		}
	}

	public GaOrder 	getGaOrderByOrderNum(String orderNum){
		String hql="  from GaOrder ho where ho.orderNum=?  ";	
		Query query = getSession().createQuery(hql);
		query.setString(0, orderNum);

		List list = query.list();
		if (list != null && list.size() > 0) {
			return (GaOrder) list.get(0);
		} else {
			return null;
		}
	}
	
	public PaginationSupport findOrderList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaK3DTO(go,u.loginName,u.userName) from GaOrder go,User u where go.userId=u.userId ";
		String findCount = "select count(go.orderId) from GaOrder go,User u where go.userId=u.userId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public List findOrderView(Integer orderId){
		Query query = getSession().createQuery("select new com.game.model.dto.GaK3DTO(gks.sessionNo,gkb.betValue,gkb.totalPoint) from BetOrderRl r,GaK3Bet gkb,GaK3Session gks where r.betId = gkb.betId and gkb.sessionId = gks.sessionId and r.orderId = ?");
		query.setInteger(0, orderId);
		List list = query.list();
		return list;
	}
	
	public List findBetList(Integer sessionId){
//		Query query = getSession().createQuery("select gkb from GaK3Bet gkb,BetOrderRl r,GaOrder o where gkb.sessionId = ? and gkb.openStatus='0' and gkb.betId=r.betId and r.orderId=o.orderId and o.type='2' and o.payStatus='1' ");
		Query query = getSession().createQuery("select gkb from GaK3Bet gkb where gkb.sessionId = ? ");
		query.setInteger(0, sessionId);
		List list = query.list();
		return list;
	}
		
}
