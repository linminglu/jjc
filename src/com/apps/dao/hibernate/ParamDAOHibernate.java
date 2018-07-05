package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.dao.IParamDAO;
import com.apps.model.Param;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class ParamDAOHibernate extends AbstractBaseDAOHibernate implements
		IParamDAO {

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
		String findList = "from Param t where 1=1 ";
		String findCount = "select count(t.paramId) from Param t where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	public List<Param> findParamList() {
		String findList = "from Param t  ";
		Query query = getSession().createQuery(findList);
		List list = query.list();
		return list;
	}
	public List<Param> findParamList(String status) {
		String findList = "from Param t where  status=? order by sort desc";
		Query query = getSession().createQuery(findList);
		query.setString(0, status);
		List list = query.list();
		return list;
	}

	public void updateSort(Integer id, String flag) {
		String hql="";
		if(flag.equals("1")){//+1
			hql="update com.apps.model.Param set sort=sort+1 where paramId=?";
		}else{//-1
			hql="update com.apps.model.Param set sort=sort-1 where paramId=?";
		}
		Query query = getSession().createQuery(hql);
		query.setInteger(0, id);
		query.executeUpdate();
	}
	public void delParam(Integer paramId) {
		String hql = "delete from Param where paramId=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, paramId);
		query.executeUpdate();
	}

	public String getNamebyCode(Integer paramId) {
		String hql = " from Param where paramId=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, paramId);
		List<Param> c=query.list();
		String name="";
		for (Param param : c) {
			name=param.getTitle();
		}
		return name;
		
	}

	public List<Param> findParamList( String status,
			String readonly) {
		String findList = "from Param t where  status=? and readonly=?";
		Query query = getSession().createQuery(findList);
		query.setString(0, status);
		query.setString(1, readonly);
		List list = query.list();
		return list;
	}
	public String getValueByCode(String code ) {
		String hql = " from Param where code=?";
		Query query = getSession().createQuery(hql);
		query.setString(0, code);
		List<Param> c = query.list();
		String value = "";
		for (Param param : c) {
			value = param.getValue();
		}
		return value;
		
	}

	@Override
	public Param getParamByType(String defPay) {
		String findList = "from Param t where  type=? and 1=1 ";
		Query query = getSession().createQuery(findList);
		query.setString(0, defPay);
		List<Param> list = query.list();
		Param param = list.get(0);
		return param;
	}

	@Override
	public Param getParam(String hqls, List<Object> para) {
		String findList = "from Param ho where 1=1 ";
		Query query = makeQuerySQL(findList + hqls, para);		
		List<Param> list = query.list();
		Param param = null;
		if(list != null && list.size() >0){
			param = list.get(0);
		}
		return param;
	}
	public List<Param> findParamList(String hqls, List<Object> para){
		String findList = " from Param t where 1=1 ";
		Query record = makeQuerySQL(findList + hqls, para);
		List<Param> list = record.list();
		return list;
	}
}
