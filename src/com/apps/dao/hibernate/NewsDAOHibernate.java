package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.apps.dao.INewsDAO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.NewsCategory;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public class NewsDAOHibernate  extends AbstractBaseDAOHibernate 
	implements INewsDAO {

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
	public PaginationSupport findNewsInformationList(String hqls,
			List<Object> para, int startIndex, int pageSize) {
		String findList = "select  new com.apps.model.dto.NewsDTO(ho,t.title) from NewsInformation ho,NewsType t where t.tid=ho.tid  ";
		String findCount = "select count(ho.id) from NewsInformation ho,NewsType t where t.tid=ho.tid  ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	

	public List<NewsType> findNewsTypeList() {
		String hql=" from NewsType ho where 1=1 and  ho.status='1' ";
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	public List<NewsCategory> findNewsCategoryList(){
		String hql=" from NewsCategory ho where 1=1 and  ho.status='1' ";
		Query query = getSession().createQuery(hql);
		return query.list();	
	}


	public List<NewsCategory> findNewsCategoryListByNid(Integer nid) {
		String hql="select ho  from NewsCategory ho,NewsCategoryRl rl where 1=1 and  ho.status='1' and ho.cid=rl.cid and rl.nid=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, nid);
		return query.list();
	}


	public void deleteNewsCategoryRl(Integer nid) {
		String hql = "delete from NewsCategoryRl  where nid =? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, nid);
		query.executeUpdate();
		
	}

	public NewsInformation getPrevNewsInformation() {
		String hql=" from NewsInformation ho order by ho.nid desc "; 
		Query query = getSession().createQuery(hql);
		query.setMaxResults(1);
		List<NewsInformation> list=query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
}
