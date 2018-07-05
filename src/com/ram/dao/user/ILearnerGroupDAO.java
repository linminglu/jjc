package com.ram.dao.user;

import java.util.List;

import com.ram.model.LearnerGroup;
import com.ram.model.User;

public interface ILearnerGroupDAO {

	public void saveLearnerGroup(LearnerGroup learnerGroup,User user);
	
	/**
	 * 查询所有的学习小组列表
	 * @return List
	 */
	public List findAllLearnerGroup(Integer tcId,Integer scheduleCourseId,Integer userId);
	
	public void deleteLearnerGroup(int learnerGroupId,User user);
	
	/**
	 * 根据学习小组Id查询学习小组对象
	 * @param learnerGroupId
	 * @return List
	 */
	public LearnerGroup getLearnerGroup(int learnerGroupId);
	
	
	/**
	 * 根据用户Id，找出其所在的学习小组对象，即找出其所有的学习小组Id
	 * @param userId
	 * @return
	 */
	public List findUserLearnerGroupByUserId(Integer userId);
	
}
