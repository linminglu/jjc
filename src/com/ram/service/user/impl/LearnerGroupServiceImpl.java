package com.ram.service.user.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.system.ISemesterDAO;
import com.ram.dao.system.ISystemProgramDAO;
import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.dao.user.IBusinessGroupDAO;
import com.ram.dao.user.ILearnerGroupDAO;
import com.ram.dao.user.IUserGroupDAO;
import com.ram.model.LearnerGroup;
import com.ram.service.user.ILearnerGroupService;

public class LearnerGroupServiceImpl extends BaseService 
implements
		ILearnerGroupService {

	private ILearnerGroupDAO learnerGroupDAO = null;

	public void setBusinessGroupDAO(IBusinessGroupDAO businessGroupDAO) {
	}



	public void setSystemProgramDAO(ISystemProgramDAO systemProgramDAO) {
	}



	public void setSystemTutorCenterDAO(ISystemTutorCenterDAO systemTutorCenterDAO) {
	}



	public void setSemesterDAO(ISemesterDAO semesterDAO) {
	}


	public void setUserGroupDAO(IUserGroupDAO userGroupDAO) {
	}

	public void setLearnerGroupDAO(ILearnerGroupDAO learnerGroupDAO) {
		this.learnerGroupDAO = learnerGroupDAO;
	}

	/**
	 * 查询所有的学习小组列表
	 * @return List
	 */
	public List findAllLearnerGroup(Integer tcId,Integer scheduleCourseId,Integer userId) {
		return this.learnerGroupDAO.findAllLearnerGroup(tcId,scheduleCourseId,userId);
	}

	/**
	 * 根据学习小组Id查询学习小组对象
	 * @param learnerGroupId
	 * @return List
	 */
	public LearnerGroup getLearnerGroup(int learnerGroupId){
		return this.learnerGroupDAO.getLearnerGroup(learnerGroupId);
	}
	
	public List findUserLearnerGroupByUserId(Integer userId){
		return learnerGroupDAO.findUserLearnerGroupByUserId(userId);
	}

}
