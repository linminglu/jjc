package com.ram.service.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.BusinessGroup;
import com.ram.model.User;


public interface IBusinessGroupService {

	public List findAllBusinessGroup();
	public List findMyCreatedBusinessGroup(int myUserId);
	public void saveBusinessGroup(BusinessGroup businessGroup,User user);
	
	public BusinessGroup getBusinessGroup(int businessGroupId);
	
	public void deleteBusinessGroup(int businessGroupId,User user);
	
	/**
	 * 判断用户是否在用户分组中
	 * @param userId
	 * @param businessGroupId
	 * @return
	 */
	public boolean IsUserInBusinessGroup(int userId,int businessGroupId); 
	public boolean ExistBusinessGroups(String businessGroupIds,User user);
	public PaginationSupport findALLBusinessGroupForPage(int startIndex,int pageSize);
	public PaginationSupport findALLBusinessGroupForPageByQuery(DetachedCriteria detachedCriteria,int pageSize,int startIndex);
	public PaginationSupport findALLBusinessGroupByQuery(DetachedCriteria detachedCriteria);
}
