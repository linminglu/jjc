package com.ram.service.user;

import java.util.List;

import com.ram.model.LearnerGroup;


public interface ILearnerGroupService {

	/**
	 * 查询所有的学习小组列表
	 * @return List
	 */
	public List findAllLearnerGroup(Integer tcId,Integer scheduleCourseId,Integer userId);
	
	
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
