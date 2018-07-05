package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.apps.dao.IAddressDAO;
import com.apps.dao.INoticeDAO;
import com.apps.dao.ISmsDAO;
import com.apps.model.Address;
import com.apps.model.Notice;
import com.apps.model.Param;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class NoticeDAOHibernate extends AbstractBaseDAOHibernate implements
		INoticeDAO {

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

	public PaginationSupport findNoticeList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Notice n where 1=1";
		String findCount = "select count(n.id) from Notice n where 1=1  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public void updateNotice(Notice notice) {
		getSession().update(notice);
		
	}
	
	public void saveNotice(Notice notice){
		getSession().save(notice);
	}
	public Notice getLatestNotice(String type){
		String findList = "from Notice n where  1=1 and n.type=?  order by n.createTime desc ";
		Query query = getSession().createQuery(findList);
		query.setString(0, type);
		List<Notice> list = query.list();
		if(list!=null&&list.size()>0){
			Notice notice = list.get(0);
			return notice;
		}
		return null;
	}

}
