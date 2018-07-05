package com.apps.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.apps.dao.ICouponsDAO;
import com.apps.model.Coupons;
import com.apps.model.CouponsLog;
import com.apps.model.dto.CouponsDTO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;

public class CouponsDAOHibernate extends AbstractBaseDAOHibernate implements
		ICouponsDAO {

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

	public PaginationSupport findCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		String findList = " from Coupons c where 1=1  ";
		String findCount = "select count(c.id) from Coupons c where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public PaginationSupport findMyCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		String findList = "select new com.apps.model.dto.CouponsDTO(c,cl) from Coupons c,CouponsLog cl where c.id=cl.couponsId  ";
		String findCount = "select count(c.id) from Coupons c,CouponsLog cl where c.id=cl.couponsId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public boolean chkCoupons(Integer uid, Integer id) {
		Criteria criteria = getSession().createCriteria(CouponsLog.class);
		criteria.add(Restrictions.eq("uid", uid));
		criteria.add(Restrictions.eq("couponsId", id));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public Coupons getUserCoupons(int couponsId, int uid) {
		String hql = "select c from Coupons c ,CouponsLog cl where c.id=cl.couponsId and cl.couponsId=? and cl.uid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, couponsId);
		query.setInteger(1, uid);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (Coupons) list.get(0);
		}
		return null;
	}

	public PaginationSupport findCouponsListHou(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		String findList = "select new com.apps.model.dto.CouponsDTO(c) from Coupons c where 1=1 ";
		String findCount = "select count(c.id) from Coupons c where 1=1 ";
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public CouponsLog getCouponsLog(Integer couponsId, Integer userId) {
		String hql = "select cl from CouponsLog cl where cl.couponsId=? and cl.uid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, couponsId);
		query.setInteger(1, userId);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (CouponsLog) list.get(0);
		}
		return null;
	}

	public PaginationSupport findMyCouponsList(String hqls, List<Object> para) {
		String findList = "select new com.apps.model.dto.CouponsDTO(c,cl.code) from Coupons c,CouponsLog cl where c.id=cl.couponsId  ";
		String findCount = "select count(c.id) from Coupons c,CouponsLog cl where c.id=cl.couponsId ";
		Query record = makeQuerySQL(findList + hqls, para);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public CouponsLog getCouponsByPhoneAndCode(String phone, String code) {
		String hql = "select cl from CouponsLog cl,User u where cl.uid=u.userId and u.loginName=? and cl.code=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, phone);
		query.setString(1, code);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (CouponsLog) list.get(0);
		}
		return null;
	}

	public Coupons getCouponsByCode(String code) {
		String hql = "select c from CouponsLog cl,Coupons c where cl.couponsId=c.id and cl.code=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, code);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (Coupons) list.get(0);
		}
		return null;
	}

	@Override
	public int getUsableNum(Integer uid, BigDecimal productPrice, Integer sid) {
		String hql = "select count(cl.id) from CouponsLog cl,Coupons c where cl.couponsId=c.id and c.status='1' and cl.uid=? and c.validDate>=? and c.lastSum<=? and (c.type='1' or (c.type='2' and c.sid=? )) ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, uid);
		query.setString(1, DateTimeUtil.getDateTime());
		query.setString(2, productPrice.toString());
		query.setInteger(3, sid);
		int totalCount = (Integer) query.uniqueResult();
		return totalCount;
	}

	@Override
	public List<CouponsDTO> findUsableList(Integer uid, BigDecimal price,
			int sid) {
		String hql = "select new com.apps.model.dto.CouponsDTO(c,cl) from Coupons c,CouponsLog cl where c.id=cl.couponsId and c.status='1' and cl.uid=? and c.validDate>=? and c.lastSum<=? and (c.type='1' or (c.type='2' and c.sid=? )) ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, uid);
		query.setString(1, DateTimeUtil.getDateTime());
		query.setString(2, price.toString());
		query.setInteger(3, sid);
		return query.list();
	}

}
