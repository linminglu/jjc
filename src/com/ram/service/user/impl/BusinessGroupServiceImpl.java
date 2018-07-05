package com.ram.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.ram.dao.system.ISemesterDAO;
import com.ram.dao.system.ISystemProgramDAO;
import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.dao.user.IBusinessGroupDAO;
import com.ram.dao.user.IUserGroupDAO;
import com.ram.model.BusinessGroup;
import com.ram.model.LearnerGroup;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserGroup;
import com.ram.service.user.IBusinessGroupService;

public class BusinessGroupServiceImpl extends BaseService implements
		IBusinessGroupService {
	
		

	private IBusinessGroupDAO businessGroupDAO = null;
	private ISemesterDAO semesterDAO = null;
	private IUserGroupDAO userGroupDAO = null;
	private ISystemProgramDAO systemProgramDAO = null;
	private ISystemTutorCenterDAO systemTutorCenterDAO = null;

	public void setBusinessGroupDAO(IBusinessGroupDAO businessGroupDAO) {
		this.businessGroupDAO = businessGroupDAO;
	}

	public void setSystemProgramDAO(ISystemProgramDAO systemProgramDAO) {
	}

	public void setSystemTutorCenterDAO(ISystemTutorCenterDAO systemTutorCenterDAO) {
	}
	
	public boolean ExistBusinessGroups(String businessGroupIds,User user){
		boolean ret = this.businessGroupDAO.ExistBusinessGroups(businessGroupIds,user);
		return ret;
	}


	public IBusinessGroupDAO getBusinessGroupDAO() {
		return businessGroupDAO;
	}

	public ISemesterDAO getSemesterDAO() {
		return semesterDAO;
	}

	public ISystemProgramDAO getSystemProgramDAO() {
		return systemProgramDAO;
	}

	public ISystemTutorCenterDAO getSystemTutorCenterDAO() {
		return systemTutorCenterDAO;
	}

	public IUserGroupDAO getUserGroupDAO() {
		return userGroupDAO;
	}

	public void setSemesterDAO(ISemesterDAO semesterDAO) {
	}


	public void setUserGroupDAO(IUserGroupDAO userGroupDAO) {
	}

	public List findMyCreatedBusinessGroup(int myUserId){
		List arr=new ArrayList();
		 
		List list=businessGroupDAO.findMyCreatedBusinessGroup(myUserId);
		for(int i=0;i<list.size();i++){
			BusinessGroup bg=(BusinessGroup)list.get(i);
			if(bg.getGroupName()==null) bg.setGroupName("");
			if(bg.getLearnerGroup()==null)bg.setLearnerGroup(new LearnerGroup());
			if(bg.getProgram()==null)bg.setProgram(new Program());
			if(bg.getSemester()==null)bg.setSemester(new Semester());
			if(bg.getTutorCenter()==null)bg.setTutorCenter(new TutorCenter());
			if(bg.getUserGroup()==null)bg.setUserGroup(new UserGroup());
			arr.add(bg);
		}
		return arr;		
	}
	
	public List findAllBusinessGroup() {
		List arr=new ArrayList();
		 
		List list=businessGroupDAO.findAllBusinessGroup();
		for(int i=0;i<list.size();i++){
			BusinessGroup bg=(BusinessGroup)list.get(i);
			if(bg.getGroupName()==null) bg.setGroupName("");
			if(bg.getLearnerGroup()==null)bg.setLearnerGroup(new LearnerGroup());
			if(bg.getProgram()==null)bg.setProgram(new Program());
			if(bg.getSemester()==null)bg.setSemester(new Semester());
			if(bg.getTutorCenter()==null)bg.setTutorCenter(new TutorCenter());
			if(bg.getUserGroup()==null)bg.setUserGroup(new UserGroup());
			arr.add(bg);
		}
		return arr;
	}

	public void saveBusinessGroup(BusinessGroup businessGroup,User user){
		this.businessGroupDAO.saveBusinessGroup(businessGroup,user);
	}
	
	public BusinessGroup getBusinessGroup(int businessGroupId){
		return this.businessGroupDAO.getBusinessGroup(businessGroupId);
	}



	public void deleteBusinessGroup(int businessGroupId,User user) {
		this.businessGroupDAO.deleteBusinessGroup(businessGroupId,user);
		
	}
	
	public boolean IsUserInBusinessGroup(int userId, int businessGroupId) {
		//User user=userDAO.
		return false;
	}
	
	public PaginationSupport findALLBusinessGroupForPage(int startIndex,int pageSize){
		List businessGroupList = businessGroupDAO.getAllBusinessGroups(startIndex,pageSize);
		int num = businessGroupDAO.getBusinessGroupsCount();
		PaginationSupport ps = new PaginationSupport(businessGroupList, num, pageSize, startIndex);
		return ps;
	}

	/**
	 * 获得Bulletin对象列表(分页),带查询。
	 */
	public PaginationSupport findALLBusinessGroupForPageByQuery(DetachedCriteria detachedCriteria,int pageSize, int startIndex){
		PaginationSupport ps = businessGroupDAO.getAllBusinessGroupByQuery(detachedCriteria,pageSize,startIndex);
		return ps;
	}
	
	
	public PaginationSupport findALLBusinessGroupByQuery(DetachedCriteria detachedCriteria){
		PaginationSupport ps = businessGroupDAO.getAllBusinessGroupByQuery(detachedCriteria);
		return ps;
	}	
}
