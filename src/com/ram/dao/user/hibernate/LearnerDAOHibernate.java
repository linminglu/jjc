package com.ram.dao.user.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.dao.user.ILearnerDAO;
import com.ram.model.Learner;
import com.ram.model.User;

/**

 */
public class LearnerDAOHibernate extends AbstractBaseDAOHibernate implements
	ILearnerDAO {

	private final String GET_LEARNER_BY_ID = "from Learner learner where learner.learnerId = ?";
	private final String GET_LEARNER_BY_USER_ID = "from Learner learner where learner.user.userId = ?";
	private final String GET_LEARNER_BY_LEARNER_ID = "from Learner learner inner join fetch learner.user where learner.learnerId = ?";
	private final String GET_ALL_LEARNERS = "from Learner learner where learner.user.status != ?";
	private final String GET_LEARNER_COUNT = "select count(*) from Learner learner where learner.user.status != ?";
	private final String MODIFY_LEARNER_STATUS = "update Learner set enrollStatus = ? where learnerId = ?";
	private final String STUDY_QUERY_COUNT = "select count(*) from Learner l, User u where l.user.userId=u.userId and u.status != '0' ";
	private final String STUDY_QUERY = "from Learner l left join fetch l.user u where 1=1 ";
	
	/**
	 * 创建或者保存一个学生
	 */
	public void saveLearner(Learner learner, User user) throws DataAccessException {
		saveObject(learner, user);
	}

	/**
	 * 根据学生ID获得一个学生对象
	 */
	public Learner getLearner(int learnerId) throws DataAccessException {
		Query query = getSession().createQuery(GET_LEARNER_BY_ID);
		query.setInteger(0, learnerId);
		List learnerList = query.list();
		return learnerList.size()==0?null:(Learner)learnerList.get(0);
	}
	
	/**
	 * 根据userId获得一个学生对象
	 */
	public Learner getLearnerByUserId(int userId) throws DataAccessException {
		Query query = getSession().createQuery(GET_LEARNER_BY_USER_ID);
		query.setInteger(0, userId);
		List learnerList = query.list();
		if(learnerList==null || learnerList.size()==0){
			return null;
		}else{
			Iterator learnerItem = learnerList.iterator();
			return (Learner)learnerItem.next();
		}
	}

	/**
	 * 获得所有学生信息(分页)
	 */
	public List getAllLearners(int startIndex, int pageSize) {
		Query query = getSession().createQuery(GET_ALL_LEARNERS);
		query.setString(0, "0");
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List learnerList = query.list();
		return learnerList;
	}

	/**
	 * 根据查询条件获得学生信息(分页)
	 */
	public PaginationSupport findAllLearners(final int startIndex, final int pageSize,
			final DetachedCriteria detachedCriteria) {
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}
	
	/**
	 * 根据查询条件获得学生信息
	 */
	public List findLearnersByCriteria(final DetachedCriteria detachedCriteria) {
		return findAllByCriteria(detachedCriteria);
	}
	
	/**
	 * 改变学生的学籍状态
	 * status
	 * 1：注册未录取
	 * 2：被录取
	 * 3：在学
	 * 4：退学
	 * 5：毕业
	 * 6：流失
	 */
	public void modifyLearnerStatus(int learnerId, int status, User user) throws DataAccessException{
		Query query = getSession().createQuery(MODIFY_LEARNER_STATUS);
		query.setInteger(0, learnerId);
		query.setInteger(1, status);
		query.executeUpdate();
	}
	
	/**
	 * 获得学生用户的数目
	 */
	public int getLearnNumber(){
		Query query = getSession().createQuery(GET_LEARNER_COUNT);
		query.setString(0, "0");
		Iterator item = query.iterate();
		int count = 0;
		if(item.hasNext()){
			count = ((Integer)item.next()).intValue();
		}
		return count;
	}
	
	public Learner getLearnerByLearnerId(Integer learnerId) throws DataAccessException{
		Query query = getSession().createQuery(GET_LEARNER_BY_LEARNER_ID);
		query.setInteger(0, learnerId.intValue());
		return (Learner)query.uniqueResult();
	}
	/*
	 *  获得未在可预约用户表中的在学学生列表
	 */
	public List findLearnersCanExamAndNotInTable(){
		String str = "select learner from Learner as learner where learner.enrollStatus=? and learner.learnerId not in (select canExamUser.learnerId from CanExamUser canExamUser) and learner.user.userId in (select distinct(le.userId) from LearnerEnrollment le where le.courseStatus in (:status))";
		Query query = getSession().createQuery(str);
//		query.setString(0,RamConstants.ENROLLMENT_STATUS_STUDYING.toString());
//		query.setParameterList("status",RamConstants.EXAMBOOK_COURSE_STATUS);
		return query.list();
	}
	
	public List findAllLearners(){
		String hql="from Learner as l order by l.userId";
		Query q=getSession().createQuery(hql);
		return q.list();
	}
	
	/*
	 *  获得所有在学学生列表
	 */
	public List findStudyLearners(){
		String str = "select learner from Learner as learner where learner.enrollStatus=? and learner.user.userId in (select distinct(le.userId) from LearnerEnrollment le where le.courseStatus in (:status))";
		Query query = getSession().createQuery(str);
//		query.setString(0,RamConstants.ENROLLMENT_STATUS_STUDYING.toString());
//		query.setParameterList("status",RamConstants.EXAMBOOK_COURSE_STATUS);
		return query.list();
	}
	
	public int studyQuery(String where, List list){
		Query query = getSession().createQuery(STUDY_QUERY_COUNT + where);
		Iterator it = list.iterator();
		int i = 0;
		Object obj;
		while(it.hasNext()){
			obj = it.next();
			if(obj instanceof String){
				query.setString(i, (String)obj);
			}else if(obj instanceof Date){
				query.setDate(i, (Date)obj);
			}else if(obj instanceof Integer){
				query.setInteger(i, ((Integer)obj).intValue());
			}else if(obj instanceof Integer[]){
				query.setParameterList("tcIdList", (Integer[])obj);
			}
			i++;
		}
		Iterator item = query.iterate();
		int count = 0;
		if(item.hasNext()){
			count = ((Integer)item.next()).intValue();
		}
		return count;
	}
	
	public List studyQuery(int offset, int rows, String where, List list){
		Query query = getSession().createQuery(STUDY_QUERY + where);
		Iterator it = list.iterator();
		int i = 0;
		Object obj;
		while(it.hasNext()){
			obj = it.next();
			if(obj instanceof String){
				query.setString(i, (String)obj);
			}else if(obj instanceof Date){
				query.setDate(i, (Date)obj);
			}else if(obj instanceof Integer){
				query.setInteger(i, ((Integer)obj).intValue());
			}else if(obj instanceof Integer[]){
				query.setParameterList("tcIdList", (Integer[])obj);
			}
			i++;
		}
		query.setFirstResult(offset * rows);
		query.setMaxResults(rows);
		return query.list();
	}
}
