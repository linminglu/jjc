package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.apps.dao.IAddressDAO;
import com.apps.model.Address;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

public class AddressDAOHibernate extends AbstractBaseDAOHibernate implements
		IAddressDAO {

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

	public List<Address> findListByUserId(int uid) {
		Criteria criteria = getSession().createCriteria(Address.class);
		criteria.add(Restrictions.eq("userId", uid));
		criteria.addOrder(Order.desc("isDef"));// 降序
		List<Address> list = criteria.list();
		return list;
	}

	public void updateAddressDef(Integer userId) {
		Query query = getSession().createQuery("update Address set isDef='0' where userId=? ");
		query.setInteger(0, userId);
		query.executeUpdate();
	}

	@Override
	public Address getDefAddress(int uid) {
		Criteria criteria = getSession().createCriteria(Address.class);
		criteria.add(Restrictions.eq("userId", uid));
//		criteria.add(Restrictions.eq("isDef", “));
		criteria.addOrder(Order.desc("isDef"));// 降序
		List<Address> list = criteria.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
