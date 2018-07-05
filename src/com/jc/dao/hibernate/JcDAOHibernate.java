package com.jc.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.ParamUtils;
import com.jc.JingcaiConstants;
import com.jc.dao.IJcDAO;
import com.jc.model.JcField;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;
import com.jc.model.JcTeam;
import com.jc.model.Type;

public class JcDAOHibernate extends AbstractBaseDAOHibernate implements IJcDAO {

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	@SuppressWarnings("unchecked")
	public List<Type> getTypeList(String type){
		String hql = " from Type t where 1=1 and t.type=? ";
		return getSession().createQuery(hql).setString(0, type).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<JcTeam> getTeamList(){
		String hql = " from JcTeam t where 1=1";
		return getSession().createQuery(hql).list();
	}
	
	@SuppressWarnings("rawtypes")
	public JcTeam getTeamByTitle(String title){
		String hql = " from JcTeam t where 1=1 and t.title=? ";
		List list = getSession().createQuery(hql).setString(0, title).list();
		if(list!=null && list.size()>0){
			return (JcTeam) list.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public JcPlayType getJcPlayTypeByTitle(String title){
		String hql = " from JcPlayType p where p.title=? ";
		List list = getSession().createQuery(hql).setString(0, title).list();
		if(list!=null && list.size()>0){
			return (JcPlayType) list.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<JcPlayType> getJcPlayTypesByFid(Integer fId, String status){
		String hql = " from JcPlayType p where p.fId=?";
		if(ParamUtils.chkString(status)){
			hql += " and p.status="+status;
		}
		List list = getSession().createQuery(hql).setInteger(0, fId).list();
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<JcOption> getJcOptionsByPtId(Integer ptId){
		String hql = " from JcOption o where o.pTId=?";
		List list = getSession().createQuery(hql).setInteger(0, ptId).list();
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<JcField> getJcFieldByMid(Integer mid,String status){
		String hql = " from JcField f where f.mId=? ";
		if(ParamUtils.chkString(status)){
			hql += " and f.status= "+status;
			
		}
		List list = getSession().createQuery(hql).setInteger(0, mid).list();
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	
	public List<JcPlayType> getJcPlayNotOpen(Integer fId){
		String hql = " from JcPlayType p where p.fId=? and p.openStatus=? ";
		List list = getSession().createQuery(hql).setInteger(0, fId).setString(1, JingcaiConstants.QUIZ_STATUS_NOT_OPEN).list();
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}

	@Override
	public PaginationSupport findJcOptionList(String hql, List<Object> para,
			int statIndex, int pageNum) {
		String findList = "select new com.jc.model.dto.JcDTO(jf.title, jp.pTId, jp.title, jp.openResult, jp.openStatus, "
				+ "jo.oId, jo.title, jo.rate )"
				+ " from JcField jf, JcPlayType jp, JcOption jo where jf.fId=jp.fId and jp.pTId=jo.pTId ";
		String findCount = "select count(jo.oId) from JcField jf, JcPlayType jp, JcOption jo where jf.fId=jp.fId and jp.pTId=jo.pTId ";

		Query record = makeQuerySQL(findList + hql, para);
		record.setFirstResult(statIndex);
		record.setMaxResults(pageNum);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
}
