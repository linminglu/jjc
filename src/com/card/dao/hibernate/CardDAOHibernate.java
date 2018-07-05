package com.card.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.card.dao.ICardDAO;
import com.card.model.Card;
import com.card.model.CardGeneLog;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.card.model.dto.CardDTO;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;

 /* @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class CardDAOHibernate extends AbstractBaseDAOHibernate implements ICardDAO{
	/**
	 * 获得学习卡
	 * @param cardCode
	 * @return
	 */
	public Card getCard(String cardCode){
		Card card = null;
		String hql = "from Card card where card.cardCode=:cardCode ";
		List<Card> list = getHibernateTemplate().findByNamedParam(hql,
              new String[]{"cardCode"},
              new Object[]{cardCode});
	    if(list != null && list.size() > 0){
	    	card = list.get(0);
	    }
		return card;
	}
	
	public Card getCardByPassword(String cardPwd){
		Card card = null;
		String hql = "from Card card where card.cardPwd=:cardPwd ";
		List<Card> list = getHibernateTemplate().findByNamedParam(hql,
              new String[]{"cardPwd"},
              new Object[]{cardPwd});
	    if(list != null && list.size() > 0){
	    	card = list.get(0);
	    }
		return card;
	}
	
	/**
	 * 获得学习卡
	 * @param cardCode
	 * @param cardPwd
	 * @return
	 */
	public Card getCard(String cardCode,String cardPwd){
		Card card = null;
		String hql = "from Card card where card.cardCode=:cardCode and card.cardPwd=:cardPwd ";
    List<Card> list = getHibernateTemplate().findByNamedParam(hql,
              new String[]{"cardCode","cardPwd"},
              new Object[]{cardCode,cardPwd});
    if(list != null && list.size() > 0){
    	card = list.get(0);
    }
		return card;
	}
	/**
	 * 查找学习卡生成记录
	 * @param protocolCode
	 * @param batchCode
	 * @return
	 */
	public CardGeneLog getCardGeneLog(String protocolCode,String batchCode){
		CardGeneLog cardGeneLog = null;
		String hql = "from CardGeneLog cardGeneLog where cardGeneLog.protocolCode=:protocolCode and cardGeneLog.batchCode=:batchCode ";
    List<CardGeneLog> list = getHibernateTemplate().findByNamedParam(hql,
              new String[]{"protocolCode","batchCode"},
              new Object[]{protocolCode,batchCode});
    if(list != null && list.size() > 0){
    	cardGeneLog = list.get(0) ;
    }
		return cardGeneLog;
	}
	public CardGeneLog findCardGeneLogByHql(String hql) {
		// TODO Auto-generated method stub
		List list=getHibernateTemplate().find(hql);
		if(list!=null&&list.size()>0){
			return (CardGeneLog)getHibernateTemplate().find(hql).get(0);
		}
		return null;
	}
	


	@Override
	public PaginationSupport findCardItemList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String hql="from  CardItem ho where 1=1  ";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL("select count(ho.itemId)  from  CardItem ho where 1=1 " + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public CardItem getCardItemById(Integer itemId){
		CardItem cardItem = new CardItem();
		Query query = getSession().createQuery(" from CardItem ho where ho.itemId = ? order by ho.itemId desc ");
		query.setParameter(0, itemId);
		List uList = query.list();
		if(uList != null){
			cardItem = (CardItem) uList.get(0);
		}
		return cardItem;
	}
	
	/**
	 * 根据订单号查询订单
	 * @param orderNum
	 * @return
	 */
	public CardRechargeOrder getRechargeOrderByOrderNum(String orderNum){
		Criteria criteria = getSession().createCriteria(CardRechargeOrder.class);
		criteria.add(Restrictions.eq("orderNum", orderNum));
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderId"));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (CardRechargeOrder) list.get(0);
		}
		return null;
	}
	public CardItemOrder getGiftCardOrderByOrderNum(String orderNum){
		Criteria criteria = getSession().createCriteria(CardItemOrder.class);
		criteria.add(Restrictions.eq("orderNum", orderNum));
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderId"));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (CardItemOrder) list.get(0);
		}
		return null;
	}
	
	
	
	public PaginationSupport findCardRechargeItemList(String hqls,
			List<Object> para, int startIndex, int pageSize){
		String hql="from  CardRechargeItem ci where 1=1";
		String sql = "select count(ci.itemId)  from  CardRechargeItem ci where 1=1";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
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
	
	public void updateCardRechargeItem(CardRechargeItem cardRechargeItem){
		getSession().update(cardRechargeItem);
	}
	
	public void saveCardRechargeItem(CardRechargeItem cardRechargeItem){
		getSession().save(cardRechargeItem);
	}
	
	public PaginationSupport findRechargeOrderList(String hqls,
			List<Object> para, int startIndex, int pageSize){
		String hql = "select new com.card.model.dto.CardDTO(ri,ro,u) from  CardRechargeItem ri,User u,CardRechargeOrder ro where ri.itemId = ro.itemId and u.userId = ro.userId ";
		String sql = "select count(ro.orderId) from  CardRechargeItem ri,User u,CardRechargeOrder ro where ri.itemId = ro.itemId and u.userId = ro.userId";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findOfflineRechargeOrderList(String hqls,
			List<Object> para, int startIndex, int pageSize){
		String hql = "select new com.card.model.dto.CardDTO(ro,u) from  User u,CardRechargeOrder ro where u.userId = ro.userId ";
		String sql = "select count(ro.orderId) from  User u,CardRechargeOrder ro where u.userId = ro.userId";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findCardItemOrderList(String hqls,
			List<Object> para, int startIndex, int pageSize){
		String hql = "select new com.card.model.dto.CardDTO(ci,co,u) from  CardItem ci,User u,CardItemOrder co where co.itemId = ci.itemId and u.userId = co.userId  ";
		String sql = "select count(co.orderId) from  CardItem ci,User u,CardItemOrder co where co.itemId = ci.itemId and u.userId = co.userId ";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public CardDTO getCardItemOrder(Integer orderId){
		String hql = "select new com.card.model.dto.CardDTO(ci,co,u) from  CardItem ci,User u,CardItemOrder co where co.itemId = ci.itemId and u.userId = co.userId and co.orderId = ? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, orderId);
		return (CardDTO) query.uniqueResult();
	}

}