package com.xy.bj3.dao.hibernate;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.GameConstants;
import com.xy.bj3.dao.IBj3DAO;
import com.xy.bj3.model.Bj3GaSession;
import com.xy.bj3.model.Bj3GaTrend;
import com.xy.bj3.model.dto.Bj3DTO;

public class Bj3DAOHibernate extends AbstractBaseDAOHibernate 
implements IBj3DAO {

	@Override
	public Bj3GaSession getCurrentSession() {
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd"); //时间格式为年月日
		SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss"); // 时间时分秒格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long nowStamp = null; // 现在时分秒的时间戳
		Long sStamp = null; // 夜场结束时分秒的时间戳
		
		Long eStamp = null; // 正常开始时分秒的时间戳
		String nowS; // 当前时间String类型
		Date now = null; //当前时间Date类型
		
		try {
			nowStamp = hmsFormat.parse(hmsFormat.format(new Date())).getTime();
//			nowStamp = DateTimeUtil.stringToDate("06:00:00", "HH:mm:ss").getTime();
			sStamp = hmsFormat.parse(GameConstants.BJ3_EVENING_END_TIME).getTime();
			eStamp = hmsFormat.parse(GameConstants.BJ3_NORMAL_START_TIME).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nowS = sdf.format(new Date()); // 当前系统时间
		ParsePosition pos = new ParsePosition(0);
		now= sdf.parse(nowS, pos);
		
		// 如果当前时间是6点到9点时间，把当前时间设为9点，这样就能查到下一期彩票。
		if(sStamp <= nowStamp && nowStamp < eStamp){ //如果刚好是6点，也要把它设为9点。不然会报错，查不到
			try {
				now = sdf.parse(ymdFormat.format(new Date()) + GameConstants.BJ3_NORMAL_START_TIME);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Bj3GaSession> list = getSession().createQuery("from Bj3GaSession bj3s where bj3s.startTime<=? and "
				+ "bj3s.endTime > ?  and (bj3s.openStatus=? or bj3s.openStatus=?) order by bj3s.sessionId")
				.setTimestamp(0, now)
				.setTimestamp(1, now)
				.setString(2, GameConstants.BJ3_OPEN_STATUS_INIT)
				.setString(3, GameConstants.BJ3_OPEN_STATUS_OPENING)
//				.setMaxResults(1)
				.list();
		if(list!=null && list.size()>0){
			return (Bj3GaSession)list.get(0);
		}
		return null;
	}

	@Override
	public Bj3GaSession getPreviousSessionBySessionNo(String sessionNo) {
		String  hql=" from Bj3GaSession bj3 where bj3.sessionNo=? ";
		Query record =getSession().createQuery(hql);
		record.setString(0, sessionNo);
		List list=record.list();
		if(list!=null && list.size()>0){
			return (Bj3GaSession)list.get(0);
		}
		return null;
	}

	@Override
	public List<Bj3GaTrend> findBj3GaTrendList() {
		String hql = " from Bj3GaTrend bj3 where bj3.trendCount > 1 order by bj3.trendCount desc";
		List<Bj3GaTrend> list;
		list = getSession().createQuery(hql).list();
		return list;
	}
	
	public List<Bj3GaTrend> findBj3GaTrendAllList(){
		String  hql=" from Bj3GaTrend gks where 1=1 ";
		Query record =getSession().createQuery(hql);
		List list=record.list();
		return list;
	}

	@Override
	public PaginationSupport findBj3GaSessionList(String hqsl,List<Object> para,int pageNum,int pageSize) {
		Query query = makeQuerySQL( " from Bj3GaSession ho where 1=1 "+hqsl, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.sessionId) from Bj3GaSession ho where 1=1 "+hqsl, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
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

	@Override
	public List<Bj3GaSession> getCurrentSession(int start, int end) {
		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd"); //时间格式为年月日
		SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss"); // 时间时分秒格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long nowStamp = null; // 现在时分秒的时间戳
		Long sStamp = null; // 夜场结束时分秒的时间戳
		
		Long eStamp = null; // 正常开始时分秒的时间戳
		String nowS; // 当前时间String类型
		Date now = null; //当前时间Date类型
		Date now2 = null;//新增
		Date now3 = null;//新增
		Date now4 = null; // 新增
		
		try {
			nowStamp = hmsFormat.parse(hmsFormat.format(new Date())).getTime();
			sStamp = hmsFormat.parse(GameConstants.BJ3_EVENING_END_TIME).getTime();
			eStamp = hmsFormat.parse(GameConstants.BJ3_NORMAL_START_TIME).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nowS = sdf.format(new Date()); // 当前系统时间
		ParsePosition pos = new ParsePosition(0);
		now= sdf.parse(nowS, pos);
		
		// 如果当前时间是6点到9点时间，把当前时间设为9点，这样就能查到下一期彩票。
		if(sStamp < nowStamp && nowStamp < eStamp){
			try {
				now = sdf.parse(ymdFormat.format(new Date()) + GameConstants.BJ3_NORMAL_START_TIME);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		now3 = new Date( now.getTime() + end* 60 *1000 *GameConstants.BJ3_TIME_INTERVAL);
		now2 = new Date( now.getTime() - start * 60 *1000*GameConstants.BJ3_TIME_INTERVAL);
		try {
			now4 = sdf.parse(ymdFormat.format(new Date()) + GameConstants.BJ3_NORMAL_START_TIME);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(now2.getTime() < now4.getTime()){
			now2 = new Date( now2.getTime()-GameConstants.BJ3_PAUSE_PART *60*60 *1000);
		}
		
		List<Bj3GaSession> list = getSession().createQuery("from Bj3GaSession bj3s where bj3s.startTime>? and "
				+ "bj3s.endTime <= ? order by bj3s.sessionId")
				.setTimestamp(0, now2)
				.setTimestamp(1, now3)
//				.setMaxResults(1)
				.list();
		return list;
	}

	@Override
	public PaginationSupport findBj3GaBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.bj3.model.dto.Bj3BetDTO(ho.sessionNo,ho.totalPoint,ho.winCash,ho.totalPoint-ho.winCash) from Bj3GaBet ho where 1=1 "
				+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(ho.betId) from Bj3GaBet ho where 1=1" +
				hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.xy.bj3.model.dto.Bj3DTO(ga,u) from GaBetDetail ga,User u where 1=1 "
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
	public List<Bj3DTO> findGaBetDetailById(String hql, List<Object> para) {
	    Query query = getSession().createQuery(" select new com.xy.bj3.model.dto.Bj3DTO(ga,u) from GaBetDetail ga,User u where 1=1 and ga.userId = u.userId "
	    		+ hql)
	    		.setParameter(0, para.get(0));
	    List<Bj3DTO> list = query.list();
		return list;
	}

	@Override
	public void deleteBj3GaBet(String hql, List<Object> para) {
		String delHql=" delete from Bj3GaBet where 1=1 ";
		Query record = makeQuerySQL(delHql+hql,para);
		record.executeUpdate();
	}
}
