package com.ram.dao.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.BusinessGroup;
import com.ram.model.User;

public interface IBusinessGroupDAO {

	public void saveBusinessGroup(BusinessGroup businessGroup,User user);
	
	public List findAllBusinessGroup();
	public List findMyCreatedBusinessGroup(int myUserId);
	public void deleteBusinessGroup(int businessGroupId,User user);
	
	public BusinessGroup getBusinessGroup(int businessGroupId);
	
	public List getAllBusinessGroups(int startIndex, int perPageCount) throws DataAccessException;
	public int getBusinessGroupsCount() throws DataAccessException;

	public boolean ExistBusinessGroups(String businessGroupIds,User user);
	public PaginationSupport getAllBusinessGroupByQuery(DetachedCriteria detachedCriteria,int pageSize, int startIndex);
	public PaginationSupport getAllBusinessGroupByQuery(DetachedCriteria detachedCriteria);
}
