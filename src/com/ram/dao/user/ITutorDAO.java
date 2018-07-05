package com.ram.dao.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.Tutor;
import com.ram.model.User;

public interface ITutorDAO extends IDAO {
	
	/**
	 * 根据查询条件获得所有教师对象
	 */ 
	public PaginationSupport findAllTeachers(int startIndex,int pageSize, final DetachedCriteria detachedCriteria);
	
	/**
	 * 获得所有学习中心下的教师对象
     */ 
	public List findTeachers();
	/**
	 * 获得所有教师对象

	 */ 
	public List findAllTeachers();
	
	/**
	 * 获得所有教师对象
     */ 
	
	public List findAllTutors();
	
	/**
	 * 通过学习中心获得所有教师对象
	 */ 
	
	public List findAllTutorByTcId(Integer tcId);
	
	/**
	 * 获得在tutor里的用户
	 * @author Lu Congyu
	 * @date 06/07/12
	 * @param tcId
	 * @return
	 */
	public List findUserInTutorByTcId(Integer tcId);
	
	/**
	 * 获得是论文评定的教师用户
	 * @return
	 */
	public List findTutorByIsAppraise();
	
	/**
	 * 保存tutor对象
	 */ 
	public Tutor saveTutor(Tutor tutor, User user) throws DataAccessException;
	/**
	 * 通过课程ID获得所有教师对象
	 */ 
	public List findAllTutorByCourseId(Integer courseId,Integer semesterId);
	
	/**
	 * 通过课程ID和学习中心id获得所有教师对象
	 */ 
	public List findAllTutorByCourseIdAndTcId(Integer scheudleCourseId,Integer tcId,Integer semesterId);
}
