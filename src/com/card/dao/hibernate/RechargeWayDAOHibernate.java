package com.card.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.card.dao.ICardDAO;
import com.card.dao.IRechargeWayDAO;
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
public class RechargeWayDAOHibernate extends AbstractBaseDAOHibernate implements IRechargeWayDAO{
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
	

}