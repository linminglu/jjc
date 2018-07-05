package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.dao.ISmsDAO;
import com.apps.model.MessageCount;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;

public class SmsDAOHibernate extends AbstractBaseDAOHibernate implements
		ISmsDAO {

	public PaginationSupport findSmsList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from MessageCount m where 1=1 ";
		String findCount = "select count(m.msgId) from MessageCount m where 1=1 ";
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
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

	public int getSmsTotalCount() {
		String findCount = "select count(m.msgId) from MessageCount m ";
		Query query = getSession().createQuery(findCount);
		Integer totalCount = (Integer) query.uniqueResult();
		return totalCount;
	}
	
	@Override
	public int getMobileCountDay(String mobile) {
		String dateTime = DateTimeUtil.DateToString(new Date());
		String findCount = "select count(*) from MessageCount m where m.receivePhone=? and m.sendTime>=? and m.sendTime<=? ";
		Query query = getSession().createQuery(findCount);
		query.setString(0, mobile);
		query.setString(1, dateTime+" 00:00:00");
		query.setString(2, dateTime+" 23:59:59");
		Integer totalCount = (Integer) query.uniqueResult();
		return totalCount;
	}

	@Override
	public MessageCount getMessageLately(String mobile) {
		String findCount = "from MessageCount m where m.receivePhone=?  order by m.msgId desc ";
		Query query = getSession().createQuery(findCount);
		query.setString(0, mobile);
		List list = query.list();
		if(list.size()>0){
			return (MessageCount) list.get(0);
		}
		return null;
	}

	@Override
	public int getIPCountDay(String ipAddr) {
		String dateTime = DateTimeUtil.DateToString(new Date());
		String findCount = "select count(*) from MessageCount m where m.sendIp=? and m.sendTime>=? and m.sendTime<=? ";
		Query query = getSession().createQuery(findCount);
		query.setString(0, ipAddr);
		query.setString(1, dateTime+" 00:00:00");
		query.setString(2, dateTime+" 23:59:59");
		Integer totalCount = (Integer) query.uniqueResult();
		return totalCount;
	}
}
