package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.apps.Constants;
import com.apps.dao.ISellerDAO;
import com.apps.model.Promotion;
import com.apps.model.PromotionRule;
import com.apps.model.Seller;
import com.apps.model.SellerUserRl;
import com.apps.model.Type;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.ram.model.User;
import com.ram.model.dto.UserDTO;

public class SellerDAOHibernate extends AbstractBaseDAOHibernate implements
		ISellerDAO {

	public PaginationSupport findSellerList(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		// 判断语句中是否包含了根据类型筛选
		boolean isSerachType = hqls.contains("and rl.tid=?");
		String findList = null;
		String findCount = null;
		if (isSerachType) {
			findList = "select new com.apps.model.dto.SellerDTO(s.sid,s.title,s.logo,s.salesNum,s.evalStar,s.sendUpPrices,s.freightPrices,s.freightTime,s.isMinus,s.isFreight,s.isInvoice,s.createDate,s.status,s.isHot) from Seller s,SellerTypeRl rl where rl.sid=s.sid ";
			findCount = "select count(s.sid) from Seller s,SellerTypeRl rl where rl.sid=s.sid ";
		} else {
			findList = "select new com.apps.model.dto.SellerDTO(s.sid,s.title,s.logo,s.salesNum,s.evalStar,s.sendUpPrices,s.freightPrices,s.freightTime,s.isMinus,s.isFreight,s.isInvoice,s.createDate,s.status,s.isHot) from Seller s where 1=1 ";
			findCount = "select count(s.sid) from Seller s where 1=1 ";
		}

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
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

	public void delSellerType(Integer sid) {
		String hql = "delete from SellerTypeRl where sid=?";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		query.executeUpdate();
	}

	public List<Type> findSellerType(Integer sid) {
		String hql = "select t from Type t,SellerTypeRl rl where t.tid=rl.tid and t.parentId=0 and rl.sid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		return query.list();
	}

	public List<Seller> findAllList(String status) {
		String hql = "select new com.apps.model.Seller(s.sid,s.title) from Seller s where s.status=? order by s.sort desc,s.sid desc ";
		Query query = getSession().createQuery(hql);
		query.setString(0, status);
		return query.list();
	}
	public List<Seller> findAllListByColumnId(Integer columnId){
		String hql = "select new com.apps.model.Seller(s.sid,s.title) from Seller s where s.columnId=? order by s.sort desc,s.sid desc ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, columnId);
		return query.list();
	}
	public List<Seller> findAllListByTid(String status, Integer tid) {
		String hql = "select new com.apps.model.Seller(s.sid,s.title) from Seller s,SellerTypeRl st where s.status=? and s.sid=st.sid and st.tid=? order by s.sort desc,s.sid desc ";
		Query query = getSession().createQuery(hql);
		query.setString(0, status);
		query.setInteger(1, tid);
		return query.list();
	}
	public List<Seller> findAllListByTids(String status,String tids){
		String hql = "select new com.apps.model.Seller(s.sid,s.title) from Seller s,SellerTypeRl st where s.status=? and s.sid=st.sid and st.tid in("+tids+") order by s.sort desc,s.sid desc ";
		Query query = getSession().createQuery(hql);
		query.setString(0, status);
		return query.list();
	}
	public SellerUserRl getSellerUser(Integer userId) {
		String hql = "from SellerUserRl rl where rl.uid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (SellerUserRl) list.get(0);
		}
		return null;
	}
	public SellerUserRl getSellerUser(Integer sid,String type){
		String hql = "from SellerUserRl rl where rl.sid=? and rl.type=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		query.setString(1, type);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (SellerUserRl) list.get(0);
		}
		return null;
	}

	public PaginationSupport findSellerCommEat(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select new com.eat.model.dto.EatCommDTO(c,s.title,u.userName) from EatComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId ";
		String findCount = "select count(c.commId) from EatComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public PaginationSupport findSellerCommStore(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select new com.store.model.dto.StoreCommDTO(c,s.title,u.userName) from StoreComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId ";
		String findCount = "select count(c.commId) from StoreComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findPromotionList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select new com.apps.model.dto.SellerDTO(pro,s.title) from Promotion pro,Seller s where pro.sid=s.sid ";
		String findCount = "select count(pro.id) from Promotion pro,Seller s where pro.sid=s.sid ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public List<PromotionRule> findPromotionRule(Integer id) {
		String hql = "from PromotionRule pr where pr.promotionId=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		List list = query.list();
		return list;
	}

	public void delPromotionRule(Integer id) {
		getSession()
				.createQuery("delete from PromotionRule where promotionId=?")
				.setInteger(0, id.intValue()).executeUpdate();
	}

	public List<Promotion> findPromotion(String nowDate) {
		String hql = "from Promotion pr where pr.startDate<=? and pr.endDate>=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, nowDate);
		query.setString(1, nowDate);
		List list = query.list();
		return list;
	}

	public void updateSellerMinusStatus(List<Integer> sids, String status) {
		if (sids != null && sids.size() > 0) {
			String hqls = "update Seller set isMinus=? where sid in (:ids) ";
			Query query = getSession().createQuery(hqls);
			query.setString(0, status);
			query.setParameterList("ids", sids);
			query.executeUpdate();
		}

	}

	public List<PromotionRule> findSellerPromotionRule(Integer sid,
			String nowDate) {
		String hql = "select prl from Promotion pr,PromotionRule prl where pr.id=prl.promotionId and pr.startDate<=? and pr.endDate>=? and pr.sid=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, nowDate);
		query.setString(1, nowDate);
		query.setInteger(2, sid);
		List list = query.list();
		return list;
	}

	public Seller getSellerByUserId(Integer uid) {
		String hql = "select s from Seller s,SellerUserRl sur where s.sid=sur.sid and sur.uid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, uid);
		List list = query.list();

		if (list != null && list.size() > 0) {
			return (Seller) list.get(0);
		}
		return null;
	}

	public PaginationSupport search(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select s from Seller s where 1=1 ";
		String findCount = "select count(s.sid) from Seller s where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
//		System.out.println(findCount + hqls);
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		if(!ParamUtils.chkInteger(totalCount)){
			totalCount=0;
		}
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public List<Promotion> findPromotionFail(String nowDate) {
		String hql = "from Promotion pr where pr.startDate>? or pr.endDate<? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, nowDate);
		query.setString(1, nowDate);
		List list = query.list();
		return list;
	}

	@Override
	public List<Type> findTypeBySid(Integer sid) {
		String hql = "select t from Type t,SellerTypeRl str where t.tid=str.tid and str.sid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		List list = query.list();
		return list;
	}

	public PaginationSupport findSellerListSql(String sqls, List<Object> para,
			int statIndex, int pageSize, String longAlt) {
		StringBuilder findSellerList = new StringBuilder();
		StringBuilder findSellerCount = new StringBuilder();
		boolean isSerachType = sqls.contains("and str.t_id=?");
		// 证明是有离我最近排序的
		if (ParamUtils.chkString(longAlt)) {
			String[] split = longAlt.split(",");
			if (split.length == 2) {
				String longitude = split[0];// 经度
				String latitude = split[1];// 纬度
				findSellerList.append("select * from ( ");

				// 下面语句是为了获得商家的距离
				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.sendUp_prices,s.freight_prices,s.is_minus,s.is_freight,s.is_invoice,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time,");
				findSellerList
						.append("(2*ATAN2(SQRT(SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)*SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)+");
				findSellerList
						.append("COS(substring_index(s.long_alt,',',-1)*PI()/180)*COS("
								+ latitude
								+ "*PI()/180)*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2)");
				findSellerList
						.append("*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2)),SQRT(1-SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)");
				findSellerList
						.append("*SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)+COS(substring_index(s.long_alt,',',-1)*PI()/180)*COS("
								+ latitude + "*PI()/180)");
				findSellerList
						.append("*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2) *SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2))))*6378137/1000 as juli ");

				if (isSerachType) {
					findSellerList
							.append("from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				} else {
					findSellerList.append("from seller s  where 1=1  ");
				}

				findSellerList.append(sqls);
				findSellerList.append(" ) as jwd order by juli asc");

				findSellerCount.append("select count(s.s_id) as count ");
				if (isSerachType) {
					findSellerCount
							.append("from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				} else {
					findSellerCount.append("from seller s  where 1=1  ");
				}
				findSellerCount.append(sqls);
			}
		} else {
			// 判断语句中是否包含了根据类型筛选

			if (isSerachType) {
				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.sendUp_prices,s.freight_prices,s.is_minus,s.is_freight,s.is_invoice,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				findSellerList.append(sqls);

				findSellerCount
						.append("select count(s.s_id) as count from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				findSellerCount.append(sqls);
			} else {

				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.sendUp_prices,s.freight_prices,s.is_minus,s.is_freight,s.is_invoice,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time from seller s where 1=1 ");
				findSellerList.append(sqls);
				findSellerCount
						.append("select count(s.s_id) as count from seller s where 1=1 ");
				findSellerCount.append(sqls);
			}
		}

		SQLQuery record = makeSqlQuery(findSellerList.toString(), para);
		record.addScalar("s_id", Hibernate.INTEGER);
		record.addScalar("title", Hibernate.STRING);
		record.addScalar("logo", Hibernate.STRING);
		record.addScalar("sales_num", Hibernate.STRING);// 销量
		record.addScalar("eval_star", Hibernate.FLOAT);// 评分
		record.addScalar("sendUp_prices", Hibernate.BIG_DECIMAL);// 起送价
		record.addScalar("freight_prices", Hibernate.BIG_DECIMAL);// 配送费
		record.addScalar("is_minus", Hibernate.STRING);
		record.addScalar("is_freight", Hibernate.STRING);
		record.addScalar("is_invoice", Hibernate.STRING);
		record.addScalar("status", Hibernate.STRING);
		record.addScalar("is_hot", Hibernate.STRING);
		record.addScalar("is_open", Hibernate.STRING);
		record.addScalar("long_alt", Hibernate.STRING);
		record.addScalar("start_time", Hibernate.TIME);// s.start_time,s.end_time
		record.addScalar("end_time", Hibernate.TIME);// s.start_time,s.end_time
		if (ParamUtils.chkString(longAlt)) {
			record.addScalar("juli", Hibernate.DOUBLE);
		}
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		SQLQuery count = makeSqlQuery(findSellerCount.toString(), para);
		count.addScalar("count", Hibernate.INTEGER);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public SQLQuery makeSqlQuery(String sqls, List param) {
		SQLQuery q = getSession().createSQLQuery(sqls);
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
	public PaginationSupport findSellerHot(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Seller s where 1=1 ";
		String findCount = "select count(s.sid) from Seller s where 1=1  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public List<User> findSellerUserBySid(Integer sid) {
		String hql = "select u from User u,SellerUserRl sur where u.userId=sur.uid and sur.sid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		List list = query.list();
		return list;
	}
	
	public List<Seller> findSellerByCid(Integer cid) {
		String hql = "from Seller s where s.cid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, cid);
		return query.list();
	}
	

	@Override
	public int getSellerCountByTid(Integer tid) {
		String hql = "select count(*) from SellerTypeRl str where str.tid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, tid);
		return (Integer) query.uniqueResult();
	}

	@Override
	public PaginationSupport findSellerListSqlBuy(String sqls,
			List<Object> para, int statIndex, int pageSize, String longAlt) {
		StringBuilder findSellerList = new StringBuilder();
		StringBuilder findSellerCount = new StringBuilder();
		boolean isSerachType = sqls.contains("and str.t_id=?");
		// 证明是有离我最近排序的
		if (ParamUtils.chkString(longAlt)) {
			String[] split = longAlt.split(",");
			if (split.length == 2) {
				String longitude = split[0];// 经度
				String latitude = split[1];// 纬度
				findSellerList.append("select * from ( ");

				// 下面语句是为了获得商家的距离
				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time,s.average,s.remarks,s.label,");
				findSellerList
						.append("(2*ATAN2(SQRT(SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)*SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)+");
				findSellerList
						.append("COS(substring_index(s.long_alt,',',-1)*PI()/180)*COS("
								+ latitude
								+ "*PI()/180)*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2)");
				findSellerList
						.append("*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2)),SQRT(1-SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)");
				findSellerList
						.append("*SIN(("
								+ latitude
								+ "-substring_index(s.long_alt,',',-1))*PI()/180/2)+COS(substring_index(s.long_alt,',',-1)*PI()/180)*COS("
								+ latitude + "*PI()/180)");
				findSellerList
						.append("*SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2) *SIN(("
								+ longitude
								+ "-substring_index(s.long_alt,',',1))*PI()/180/2))))*6378137/1000 as juli ");

				if (isSerachType) {
					findSellerList
							.append("from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				} else {
					findSellerList.append("from seller s  where 1=1  ");
				}

				findSellerList.append(sqls);
				findSellerList.append(" ) as jwd order by juli asc");

				findSellerCount.append("select count(s.s_id) as count ");
				if (isSerachType) {
					findSellerCount
							.append("from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				} else {
					findSellerCount.append("from seller s  where 1=1  ");
				}
				findSellerCount.append(sqls);
			}
		} else {
			// 判断语句中是否包含了根据类型筛选

			if (isSerachType) {
				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time,s.average,s.remarks,s.label from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				findSellerList.append(sqls);

				findSellerCount
						.append("select count(s.s_id) as count from seller s ,seller_type_rl str where str.s_id=s.s_id ");
				findSellerCount.append(sqls);
			} else {

				findSellerList
						.append("select s.s_id,s.title,s.logo,s.sales_num,s.eval_star,s.status,s.is_hot,s.is_open,s.long_alt,s.start_time,s.end_time,s.average,s.remarks,s.label from seller s where 1=1 ");
				findSellerList.append(sqls);
				findSellerCount
						.append("select count(s.s_id) as count from seller s where 1=1 ");
				findSellerCount.append(sqls);
			}
		}

		SQLQuery record = makeSqlQuery(findSellerList.toString(), para);
		record.addScalar("s_id", Hibernate.INTEGER);
		record.addScalar("title", Hibernate.STRING);
		record.addScalar("logo", Hibernate.STRING);
		record.addScalar("sales_num", Hibernate.STRING);// 销量
		record.addScalar("eval_star", Hibernate.FLOAT);// 评分
		record.addScalar("status", Hibernate.STRING);
		record.addScalar("is_hot", Hibernate.STRING);
		record.addScalar("is_open", Hibernate.STRING);
		record.addScalar("long_alt", Hibernate.STRING);
		record.addScalar("start_time", Hibernate.TIME);// s.start_time,s.end_time
		record.addScalar("end_time", Hibernate.TIME);// s.start_time,s.end_time
		record.addScalar("average", Hibernate.INTEGER);// s.start_time,s.end_time
		record.addScalar("remarks", Hibernate.STRING);// s.start_time,s.end_time
		record.addScalar("label", Hibernate.STRING);// s.start_time,s.end_time
		if (ParamUtils.chkString(longAlt)) {
			record.addScalar("juli", Hibernate.DOUBLE);
		}
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		SQLQuery count = makeSqlQuery(findSellerCount.toString(), para);
		count.addScalar("count", Hibernate.INTEGER);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findSellerCommBuy(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select new com.buy.model.dto.BuyCommDTO(c,s.title,u.userName) from BuyComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId ";
		String findCount = "select count(c.commId) from BuyComment c,Seller s,User u where c.sid=s.sid and u.userId=c.userId  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public boolean chkSellerUserTask(Integer userId, String type) {
		String hql = "from SellerUserRl sur where sur.uid=? and sur.type=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setString(1, type);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public List<UserDTO> findSellerUser(Integer sid) {
		String hql = "select new com.ram.model.dto.UserDTO(u,sur.userName) from SellerUserRl sur,User u where sur.uid=u.userId and sur.sid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		List list = query.list();
		return list;
	}

	@Override
	public List<User> findSellerUserBySid(Integer sid, String type) {
		String hql = "select u from User u,SellerUserRl sur where u.userId=sur.uid and sur.sid=? and sur.type=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, sid);
		query.setString(1, type);
		List list = query.list();
		return list;
	}

	@Override
	public SellerUserRl getSellerUserType(Integer userId) {
		String hql = "from SellerUserRl sur where sur.uid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userId);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return (SellerUserRl) list.get(0);
		}
		return null;
	}
	public void delSellerUserRl(Integer sid){
		Query query = getSession().createQuery("delete from SellerUserRl where sid = ? ");
		query.setInteger(0, sid);
		query.executeUpdate();
	}
}
