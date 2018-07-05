package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.apps.Constants;
import com.apps.dao.ITypeDAO;
import com.apps.model.SellerTypeRl;
import com.apps.model.Type;
import com.apps.model.TypeCate;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class TypeDAOHibernate extends AbstractBaseDAOHibernate implements
		ITypeDAO {

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

	public PaginationSupport findList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Type t where 1=1 ";
		String findCount = "select count(t.tid) from Type t where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}
	
	public PaginationSupport findTypeList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Type t where 1=1 ";
		String findCount = "select count(t.tid) from Type t where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	public List<Type> findTypeList() {
		String findList = "from Type t where 1=1 ";
		Query query = getSession().createQuery(findList);
		List list = query.list();
		return list;
	}

	public void updateSort(Integer id, String flag) {
		String hql = "";
		if (flag.equals("1")) {// +1
			hql = "update com.apps.model.Type set sort=sort+1 where tid=?";
		} else {// -1
			hql = "update com.apps.model.Type set sort=sort-1 where tid=?";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		query.executeUpdate();
	}

	@Override
	public List<Type> findTypeList(String type) {
		Criteria criteria = getSession().createCriteria(Type.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("parentId",0));
		criteria.addOrder(Order.desc("sort"));// 降序
		return criteria.list();
	}
	public SellerTypeRl findSellerTypeRlBySid(Integer sid){
		String sql = "select str SellerTypeRl str where  str.sid=? ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, sid);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (SellerTypeRl) list.get(0);
		}
		return null;
	}
	
	public List<Type> findGRTypeList(){
		Query query = getSession().createQuery("from Type t where t.parentId = 0 and t.type in (14,25,26) ");
		List list = query.list();
		return list;
	}
	
	@Override
	public Type getTypeBySid(String sid) {
		String sql = "select t from Type t,SellerTypeRl str where t.tid=str.tid and str.sid=? ";
		Query query = getSession().createQuery(sql);
		query.setString(0, sid);

		List list = query.list();
		if (list != null && list.size() > 0) {
			return (Type) list.get(0);
		}
		return null;
	}

	@Override
	public List<Type> findTypeList(Integer tid) {
//		String sql = "from Type t where t.parentId=? ";
//		Query query = getSession().createQuery(sql);
//		query.setInteger(0, tid);
//		List list = query.list();
//		return list;
		
		Criteria criteria = getSession().createCriteria(Type.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.eq("parentId",tid));
		criteria.addOrder(Order.desc("sort"));// 降序
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TypeCate> findTypeCate(){
		return getSession().createCriteria(TypeCate.class)
		.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN))
		.addOrder(Order.asc("typeCateId"))
		.list();
	}

}
