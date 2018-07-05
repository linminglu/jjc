package com.cash.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.cash.dao.ICashDAO;
import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.model.dto.CashDTO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;


public class CashDAOHibernate extends AbstractBaseDAOHibernate implements ICashDAO {

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
	public UserBankBind getUserBankBindByUid(Integer userId) {
		UserBankBind bank=null;
		Query query = getSession().createQuery("from UserBankBind ho where ho.userId = ? and ho.isDef ='1'   order by  ho.bankBindId desc");
		query.setInteger(0, userId);
		List<UserBankBind> list=query.list();
		if(list!=null&list.size()>0){
			bank=list.get(0);	
		}
		return bank;
	}

	@Override
	public List<UserBankBind> findBankBindListByUid(Integer userId) {
		Query query = getSession().createQuery("from UserBankBind ho where ho.userId = ? order by  ho.bankBindId desc");
		query.setInteger(0, userId);
		return query.list();
	}

	@Override
	public PaginationSupport findUserApplyCashList(String hqls,
			List<Object> para, int statIndex, int pageSize) {
		String findList = "select ho from UserApplyCash ho,User u where 1=1 and ho.userId=u.userId ";
		String findCount = "select count(ho.applyCashId) from UserApplyCash ho,User u  where 1=1   and ho.userId=u.userId ";

		Query record = makeQuerySQL(findList + hqls +" order by ho.applyCashId desc", para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findUserCashBankList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		String findList = "select new com.cash.model.dto.CashDTO(uc,u) from UserApplyCash uc,User u where uc.userId = u.userId  ";
		String findCount = "select count(uc.applyCashId) from UserApplyCash uc,User u where uc.userId = u.userId   ";
		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	@SuppressWarnings("unchecked")
	public List<CashDTO> findUserCashBankList(String hqls, List<Object> para){
		String findList = "select new com.cash.model.dto.CashDTO(uc,u) from UserApplyCash uc,User u where uc.userId = u.userId  ";
		Query record = makeQuerySQL(findList + hqls, para);
		List<CashDTO> queList = new ArrayList<CashDTO>();
		queList = record.list();
		return queList;
	}
	
	public void updateUserApplyCash(UserApplyCash userApplyCash){
		getSession().update(userApplyCash);
	}
	
	@Override
	public UserBankBind getUserBankBindByBankAccount(String bankAccount) {
		UserBankBind bank=null;
		Query query = getSession().createQuery("from UserBankBind ho where ho.bindAccount = ?   ");
		query.setString(0, bankAccount);
		List<UserBankBind> list=query.list();
		if(list!=null&list.size()>0){
			bank=list.get(0);	
		}
		return bank;
	}

	/**
	 * 资金明细
	 */
	public PaginationSupport findUserTradeDetailList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		String findList = "from UserTradeDetail ho where 1=1  ";
		String findCount = "select count(ho.tradeDetailId) from UserTradeDetail ho where 1=1 ";
		Query record = makeQuerySQL(findList + hqls + " order by ho.tradeDetailId desc", para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	/**
	 * 积分明细
	 */
	public PaginationSupport findUserPointDetailList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		String findList = "from UserPointDetail ho where 1=1 ";
		String findCount = "select count(ho.tradeDetailId) from UserPointDetail ho where 1=1  ";
		Query record = makeQuerySQL(findList + hqls + " order by ho.tradeDetailId desc", para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findSysBankList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		String findList = "from SysBank s where 1=1 ";
		String findCount = "select count(s.bankId) from SysBank s where 1=1  ";
		Query record = makeQuerySQL(findList + hqls + " order by s.bankId desc", para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public void updateBank(SysBank sysBank){
		getSession().update(sysBank);
	}
	public List<SysBank> findSysBankList(){
		Query query = getSession().createQuery(" from SysBank s ");
		List list = query.list();
		return list;
	}
	@Override
	public int getDayDrawingNum(Integer uid) {
		Query query = getSession().createQuery("select count(uac.applyCashId) from UserApplyCash uac where uac.userId=? and uac.createTime>=? and uac.createTime<=? ");
		query.setInteger(0, uid);
		query.setString(1, DateTimeUtil.getCurrentDayStartStr());
		query.setString(2, DateTimeUtil.getCurrentDayendStr());
		Integer totalCount = (Integer) query.uniqueResult();
		return totalCount;
	}
}
