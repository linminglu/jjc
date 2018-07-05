package com.ram.dao.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.Learner;
import com.ram.model.User;

/**
 * @author lixiaodong 
 */
public interface ILearnerDAO extends IDAO{
	public void saveLearner(Learner learner, User user) throws DataAccessException;
	public Learner getLearner(int learnerId) throws DataAccessException;
	public Learner getLearnerByLearnerId(Integer learnerId) throws DataAccessException;
	public Learner getLearnerByUserId(int userId) throws DataAccessException;
	public void modifyLearnerStatus(int learnerId, int status, User user) throws DataAccessException;
	public List getAllLearners(int startIndex,int pageSize);
	public PaginationSupport findAllLearners(int startIndex,int pageSize, DetachedCriteria detachedCriteria);
	public int getLearnNumber();
	public List findLearnersByCriteria(final DetachedCriteria detachedCriteria);
	/*
	 * 获得未在可预约用户表中的在学学生列表
	 */
	public List findLearnersCanExamAndNotInTable();
	/*
	 * 获得所有在学学生列表
	 */
	public List findStudyLearners();	
	
	public List findAllLearners();

	
	/**
	 * 学生查询结果总数
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @param where
	 * @param list
	 * @return
	 */
	public int studyQuery(String where, List list);
	
	/**
	 * 学生查询结果
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @param offset
	 * @param rows
	 * @param where
	 * @param list
	 * @return
	 */
	public List studyQuery(int offset, int rows, String where, List list);
}
