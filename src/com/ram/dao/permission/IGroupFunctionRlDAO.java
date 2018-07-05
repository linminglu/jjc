package com.ram.dao.permission;

import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.GroupFunctionRl;
import com.ram.model.User;

public interface IGroupFunctionRlDAO extends IDAO {

	/**
	 * 查找某个组下的某个功能的下的子功能
	 * 
	 * @param parentFunctionId
	 * @param groupId
	 * @return
	 */
	public List findSubFunctionsByGroup(Integer groupId, Integer parentFunctionId);

	public List findSubFunctonsByFunction(int functionId);
	
	public void saveFunctionWithGroup(Integer groupId, Integer functionId,User user);

	public GroupFunctionRl getGroupFunctionRl(Integer groupId,
			Integer functionId);

	public void saveGroupFunctionRl(GroupFunctionRl groupFunctionRl, User user);

	public void updateGroupFunctionRl(GroupFunctionRl groupFunctionRl, User user);

	public void removeGroupFunctionRl(Integer groupFunctionRlId, User user);

	public void deleteGroupFunctionRl(Integer groupFunctionRlId, User user);
	public boolean hasRelationInDB(int userGroupId,int functionId);
}
