package com.apps.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.apps.model.Type;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.apps.dao.ICollectDAO;
import com.apps.model.Collection;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

public class CollectDAOHibernate extends AbstractBaseDAOHibernate implements
		ICollectDAO {

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
	public PaginationSupport findCollectSeller(String hqls, List<Object> para,
			int pageIndex, int pageSize) {

		String hqlCollectList = "select new com.apps.model.dto.SellerDTO(s.sid,s.title,s.logo,s.salesNum,s.evalStar,s.sendUpPrices,s.freightPrices,s.freightTime,s.remarks,s.longAlt,co.collectId,co.createTime,co.userId) from User u,Seller s,Collection co where s.sid=co.itemId and co.userId=u.userId ";
		String hqlCollectListCount = "select count(co.collectId) from User u,Seller s,Collection co where u.userId=co.userId and s.sid=co.itemId ";

		Query record = makeQuerySQL(hqlCollectList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(hqlCollectListCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findCollectProduct(String hqls, List<Object> para,
			int pageIndex, int pageSize){
		String hqlCollectList = "select new com.apps.model.dto.CollectDTO(u,b,co) from User u,BuyProduct b,Collection co where co.itemId = b.pid and co.userId=u.userId ";
		String hqlCollectListCount = "select count(co.collectId) from User u,BuyProduct b,Collection co where co.itemId = b.pid and co.userId=u.userId ";

		Query record = makeQuerySQL(hqlCollectList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(hqlCollectListCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findCollectMytInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize){
		String hqlCollectList = "select new com.apps.model.dto.CollectDTO(u,m,co) from User u,MytInfo m,Collection co where co.itemId = m.infoId and co.userId=u.userId ";
		String hqlCollectListCount = "select count(co.collectId) from User u,MytInfo m,Collection co where co.itemId = m.infoId and co.userId=u.userId ";

		Query record = makeQuerySQL(hqlCollectList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(hqlCollectListCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public PaginationSupport findCollectPorduct(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		String hqlCollectList = "select new com.buy.model.dto.BuyProductDTO(p.pid,p.imgMini,p.title,p.content,p.price,p.oldPrice,s.longAlt,co.collectId) from BuyProduct p,Collection co,Seller s where s.sid=p.sid and co.itemId=p.pid  ";
		String hqlCollectListCount = "select count(co.collectId) from BuyProduct p,Collection co,Seller s where s.sid=p.sid and co.itemId=p.pid  ";

		Query record = makeQuerySQL(hqlCollectList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		
		// 总记录数
		Query count = makeQuerySQL(hqlCollectListCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public PaginationSupport findCollectInfo(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		String hqlCollectList = "select new com.myt.model.dto.MytDTO(p.infoId,p.imgUrl,p.title,p.content,p.tgPrice,p.ylPrice,p.longAlt,co.collectId,t.type,co.createTime) from MytInfo p,Collection co,Type t  where  co.itemId=p.infoId and t.tid=p.typeId ";
		String hqlCollectListCount = "select count(co.collectId) from MytInfo p,Collection co,Type t  where  co.itemId=p.infoId and t.tid=p.typeId   ";

		Query record = makeQuerySQL(hqlCollectList + hqls, para);
		record.setFirstResult(pageIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		
		// 总记录数
		Query count = makeQuerySQL(hqlCollectListCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public Collection getCollect(int uid, int pid, String type) {
		Criteria criteria = getSession().createCriteria(Collection.class);
		criteria.add(Restrictions.eq("userId", uid));
		criteria.add(Restrictions.eq("itemId", pid));
		criteria.add(Restrictions.eq("type", type));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (Collection) list.get(0);
		}
		return null;
	}
}
