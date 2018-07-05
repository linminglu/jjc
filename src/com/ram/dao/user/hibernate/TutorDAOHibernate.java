package com.ram.dao.user.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.RamConstants;
import com.ram.dao.user.ITutorDAO;
import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;

public class TutorDAOHibernate extends AbstractBaseDAOHibernate implements
		ITutorDAO {
	
	private final String GET_ALL_TUTOR = "from Tutor as tutor inner join fetch tutor.user inner join fetch tutor.tutorCenter where tutor.user.status != ?";
	private final String FIND_ALL_TUTOR="from Tutor as tutor where tutor.user.status != ?";
	private final String FIND_ALL_TUTOR_BY_TCID="from Tutor as tutor where tutor.user.status !=? and tutor.tutorCenter.tcId=?";
	private final String FIND_USER_IN_TUTOR_BY_TC_ID = "from User as u where u.userId in " +
						 "(select t.user.userId from Tutor t where t.tutorCenter.tcId=?)";
	
	private final String FIND_TUTOR_BY_COURSEID="select tutor from Tutor as tutor,Course as s,TutorCourse as sc where tutor.user.status !=? and s.courseId=sc.course.courseId and sc.tutor.tutorId=tutor.tutorId and s.courseId=?  and sc.semesterId=?";
	private final String FIND_TUTOR_BY_COURSEID_AND_TCID="select tutor from Tutor as tutor,Course as s,TutorCourse as sc where tutor.user.status !=? and s.courseId=sc.course.courseId and sc.tutor.tutorId=tutor.tutorId and s.courseId=?  and tutor.tutorCenter.tcId=? and sc.semesterId=? and sc.tutorType like ?";
	
	/**
	 * 根据查询条件获得所有教师对象
	 */ 
	public PaginationSupport findAllTeachers(int startIndex,int pageSize, final DetachedCriteria detachedCriteria){
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);

	}
	
	/**
	 * 获得所有学习中心下的教师对象     */ 
	public List findAllTeachers(){
//		Query query = getSession().createQuery(GET_ALL_TUTOR);
//		query.setString(0, RamConstants.DELETE_STATUS);
//		List tutorList = query.list();
//		return tutorList;
		
		Criteria criteria = getSession().createCriteria(TutorCenter.class);
		criteria.setFetchMode("tutors",FetchMode.JOIN).createAlias("tutors.user","user");
		criteria.setFetchMode("user", FetchMode.JOIN).add(Expression.eq("user.status","1"));
	    // 注释部运行时出现错误
        //criteria.setFetchMode("tutors",FetchMode.JOIN).setFetchMode("tutors.user",FetchMode.JOIN).
        //add(Expression.eq("user.status","1"));
		//动态抓取防止学习中心出现重复值

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("tcId"));
		List tutorList = criteria.list();
       
		return tutorList;
		
	}
	/**
	 * 获得所有教师对象     */ 
	
	public List findTeachers(){
		Query query = getSession().createQuery(GET_ALL_TUTOR);
		query.setString(0, RamConstants.DELETE_STATUS);
		List tutorList = query.list();
		return tutorList;
	}
	
	public List findAllTutors(){
		Query query = getSession().createQuery(FIND_ALL_TUTOR);
		query.setString(0, RamConstants.DELETE_STATUS);
		List tutorList = query.list();
		return tutorList;
	}
	
	/**
	 * 通过学习中心获得所有教师对象
	 */ 
	public List findAllTutorByTcId(Integer tcId){
		Query query = getSession().createQuery(FIND_ALL_TUTOR_BY_TCID);
		query.setString(0, RamConstants.DELETE_STATUS);
		query.setInteger(1,tcId.intValue());
		List tutorList = query.list();
		return tutorList;
	}
	
	public List findUserInTutorByTcId(Integer tcId){
		Query query = getSession().createQuery(FIND_USER_IN_TUTOR_BY_TC_ID);
		query.setInteger(0, tcId.intValue());
		List tutorList = query.list();
		return tutorList;
	}	

	
	public List findTutorByIsAppraise(){
		String sql = "from User as u where u.userId in " +
		 "(select t.user.userId from Tutor t where t.isAppraise=1) order by u.userNameZh";
		Query query = getSession().createQuery(sql);
		return query.list();
	}
	
	/**
	 * 保存tutor对象
	 */ 
	public Tutor saveTutor(Tutor tutor, User user) throws DataAccessException{
		saveObject(tutor, user);
		return tutor;
	}
	
	/**
	 * 通过课程ID获得所有教师对象
	 */ 
	public List findAllTutorByCourseId(Integer courseId,Integer semesterId){
		Query query = getSession().createQuery(FIND_TUTOR_BY_COURSEID);
		query.setString(0, RamConstants.DELETE_STATUS);
		query.setInteger(1,courseId.intValue());
		query.setInteger(2,semesterId.intValue());
		List tutorList = query.list();
		return tutorList;
	}
	
	/**
	 * 通过课程ID和学习中心id获得所有教师对象
	 */ 
	public List findAllTutorByCourseIdAndTcId(Integer scheduleCourseId,Integer tcId,Integer semesterId){
		Query query = getSession().createQuery(FIND_TUTOR_BY_COURSEID_AND_TCID);
		query.setString(0, RamConstants.DELETE_STATUS);
		query.setInteger(1,scheduleCourseId.intValue());
		query.setInteger(2,tcId.intValue());
		query.setInteger(3,semesterId.intValue());
		query.setString(4,"%" +"辅导教师"+"%");
		List tutorList = query.list();
		return tutorList;
	}

}
