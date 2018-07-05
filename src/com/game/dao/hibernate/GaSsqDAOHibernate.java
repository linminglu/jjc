package com.game.dao.hibernate;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.game.GameConstants;
import com.game.dao.IGaSsqDAO;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;
import com.game.model.GaSsqBet;
import com.game.model.GaSsqSession;

public class GaSsqDAOHibernate extends AbstractBaseDAOHibernate 
	implements IGaSsqDAO {


	
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
	
	public GaSsqSession getCurrentSession(){
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前系统时间
		//String todayYyyymmdd = DateTimeUtil.DateToString(now);
		//Date todayStart = DateTimeUtil.parse(todayYyyymmdd+" 00:00:00");
		//Date todayEnd = DateTimeUtil.parse(todayYyyymmdd+" 23:59:59");
//		Date nowEnd = DateTimeUtil.getDateTimeOfMinutes(now, GameConstants.K10_TIME_INTERVAL);
		
		List<GaSsqSession> list = getSession().createQuery("from GaSsqSession gss where gss.startTime<=? and gss.endTime>? and gss.openStatus=?")
		.setTimestamp(0, now)
		.setTimestamp(1, now)
		.setString(2, GameConstants.OPEN_STATUS_INIT)
//		.setString(3, GameConstants.OPEN_STATUS_OPENING)
		.setMaxResults(1)
		.list();
		if(list!=null && list.size()>0){
			return (GaSsqSession)list.get(0);
		}
		return null;
	}

	
	public PaginationSupport findAllOpenSessionList(int pageNum,int pageSize){
		List<Object> para=new ArrayList<Object>();
		Query query = makeQuerySQL( " from GaSsqSession ho where ho.openStatus='2' order by ho.endTime desc",para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from GaSsqSession ho where ho.openStatus='2' ", para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "from GaSsqSession gss where gss.openStatus = 2 ";
		String findCount = "select count(gss.sessionId) from  GaSsqSession gss where gss.openStatus = 2 ";
		
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
		String findList = "from GaSsqSession gss where gss.openStatus != 0  ";
		String findCount = "select count(gss.sessionId) from GaSsqSession gss where gss.openStatus != 0 ";
		
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
		String findList = "select new com.game.model.dto.GaSsqDTO(gsb,gss) from GaSsqBet gsb,GaSsqSession gss,User u,GaOrder go,BetOrderRl bor where gsb.sessionId=gss.sessionId and gsb.userId=u.userId and gss.openStatus !=0 and gsb.betId = bor.betId and bor.orderId = go.orderId ";
		String findCount = "select count(gsb.betId) from GaSsqBet gsb,GaSsqSession gss,User u,GaOrder go,BetOrderRl bor where gsb.sessionId=gss.sessionId and gsb.userId=u.userId and gss.openStatus !=0 and gsb.betId = bor.betId and bor.orderId = go.orderId ";

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
		String findList = "select new com.game.model.dto.GaSsqDTO(gsb,gss,u.loginName) from GaSsqBet gsb,GaSsqSession gss,User u,GaOrder go,BetOrderRl bor where gsb.sessionId=gss.sessionId and gsb.userId=u.userId and gsb.betId = bor.betId and bor.orderId = go.orderId ";
		String findCount = "select count(gsb.betId) from GaSsqBet gsb,GaSsqSession gss,User u,GaOrder go,BetOrderRl bor where gsb.sessionId=gss.sessionId and gsb.userId=u.userId and gsb.betId = bor.betId and bor.orderId = go.orderId ";

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
		String findList = "select new com.game.model.dto.GaSsqDTO(gsbd,gss) from GaSsqBetDetail gsbd,GaSsqSession gss,User u,GaSsqBet gsb where gsbd.sessionId=gss.sessionId and gsbd.userId=u.userId and gsb.betId = gsbd.betId";
		String findCount = "select count(gsbd.betDetailId) from GaSsqBetDetail gsbd,GaSsqSession gss,User u,GaSsqBet gsb where gsbd.sessionId=gss.sessionId and gsbd.userId=u.userId and gsb.betId = gsbd.betId";

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
																																//ga_order o,ga_ssq_bet b,bet_order_rl r where b.user_id=22497 and b.bet_id=r.bet_id and r.order_id=o.order_id and o.pay_status='1' and o.type='1';
		String findList = "select new com.game.model.dto.GaSsqDTO(gs.sessionNo,ho)  from GaSsqSession gs,BetOrderRl r,GaSsqBet ho,GaOrder o where  ho.betId = r.betId  and r.orderId =o.orderId and o.payStatus='1' and o.type='1' and gs.sessionId=ho.sessionId  ";
		String findCount = "select count(ho.betId) from BetOrderRl r,GaSsqBet ho,GaOrder o where  ho.betId = r.betId  and r.orderId =o.orderId and o.payStatus='1' and o.type='1'";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public GaSsqSession getGaSsqSessionBySessionNo(String sessionNo){
		String hql="  from GaSsqSession ho where ho.sessionNo=? and ho.openStatus='0' ";	
		Query query = getSession().createQuery(hql);
		// query.setCacheable(true);
		query.setString(0, sessionNo);
//		query.setString(1, password);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (GaSsqSession) list.get(0);
		} else {
			return null;
		}
	}
	
	public GaOrder getGaOrderByOrderNum(String orderNum){
		String hql="  from GaOrder ho where ho.orderNum=?  ";	
		Query query = getSession().createQuery(hql);
		// query.setCacheable(true);
		query.setString(0, orderNum);
//		query.setString(1, password);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (GaOrder) list.get(0);
		} else {
			return null;
		}
	}
	
	public PaginationSupport findOrderList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findList = "select new com.game.model.dto.GaSsqDTO(go,u.loginName,u.userName) from GaOrder go,User u where go.userId=u.userId ";
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
		Query query = getSession().createQuery("select new com.game.model.dto.GaSsqDTO(gss.sessionNo,gsb.redBall,gsb.blueBall,gsb.totalPoint) from BetOrderRl r,GaSsqBet gsb,GaSsqSession gss where r.betId = gsb.betId and gsb.sessionId = gss.sessionId and r.orderId = ?");
		query.setInteger(0, orderId);
		List list = query.list();
		return list;
	}
	public List<GaSsqBet> findGaSsqBetListBySessionId(Integer sessionId){
		Query query = getSession().createQuery("select gsb from BetOrderRl r,GaSsqBet gsb,GaOrder o where  gsb.betId = r.betId  and r.orderId =o.orderId and o.payStatus='1' and gsb.sessionId = ? and gsb.openStatus='0' ");
		query.setInteger(0, sessionId);
		List list = query.list();
		return list;
	}

}
