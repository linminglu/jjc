package com.ram.service.permission;

import java.util.List;

import com.ram.model.GroupFunctionRl;
import com.ram.model.User;

public interface IGroupFunctionRlService {
	
	public List findTopFunctionsByGroup(Integer groupId);

	public List findSubFunctionsByGroup(Integer groupId, Integer parentFunctionId);

	public void saveGroupFunctionRl(GroupFunctionRl groupFunctionRl,User user);
	
	public void removeFunctionOfGroup(Integer groupId,Integer functionId,User user);
	
	public void addSubFunctionsToGroup(int parentFunctionId,int groupId,User user);
	
	public void modifyOrderSn(int groupId,int functionId,int orderValue,User user);
}
