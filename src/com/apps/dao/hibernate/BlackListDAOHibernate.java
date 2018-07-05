package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.dao.IBaseDataDAO;
import com.apps.dao.IBlackListDAO;
import com.apps.model.BlackList;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class BlackListDAOHibernate extends AbstractBaseDAOHibernate implements
IBlackListDAO {
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

public PaginationSupport findBlackList(String hqls, List<Object> para,
		int startIndex, int pageSize){
	String hql = " from BlackList b where 1=1 ";
	String sql = "select count(b.bid) from BlackList b where 1=1  ";
	Query query = makeQuerySQL(hql + hqls, para);
	query.setFirstResult(startIndex);
	query.setMaxResults(pageSize);
	List queList = query.list();
	// 总记录数
	Query count = makeQuerySQL(sql + hqls, para);
	Integer totalCount = (Integer) count.uniqueResult();
	return new PaginationSupport(queList, totalCount.intValue());
}
public void delBlack(Integer bid){
	Query query = getSession().createQuery("delete from BlackList   where bid = ?");
	query.setInteger(0, bid);
	query.executeUpdate();
}
public List<BlackList> findBlackList(String type){
	String sql = "from BlackList b where b.type=? order by b.bid desc ";
	Query query = getSession().createQuery(sql);
	query.setString(0, type);
	List list = query.list();
	return list;
}


}
