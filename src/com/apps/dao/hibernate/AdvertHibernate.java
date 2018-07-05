package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.Session;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.apps.Constants;
import com.apps.dao.IAddressDAO;
import com.apps.dao.IAdvertDAO;
import com.apps.dao.ISmsDAO;
import com.apps.model.Address;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class AdvertHibernate extends AbstractBaseDAOHibernate implements
		IAdvertDAO {

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
		String findList = "from Advertising a where 1=1 ";
		String findCount = "select count(a.id) from Advertising a where 1=1 ";
		
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	public void updateSort(Integer id, String flag) {
		String hql="";
		if(flag.equals("1")){//+1
			hql="update Advertising set sort=sort+1 where id=?";
		}else{//-1
			hql="update Advertising set sort=sort-1 where id=?";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		query.executeUpdate();
	}
	public void updatePromotionSort(Integer id, String flag) {
		String hql="";
		if(flag.equals("1")){//+1
			hql="update Promotion set sort=sort+1 where id=?";
		}else{//-1
			hql="update Promotion set sort=sort-1 where id=?";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		query.executeUpdate();
	}

}
