package com.ram.service.permission.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.dao.permission.IGroupFunctionRlDAO;
import com.ram.model.Function;
import com.ram.model.GroupFunctionRl;
import com.ram.model.User;
import com.ram.service.permission.IGroupFunctionRlService;

public class GroupFunctionRlServiceImpl extends BaseService implements
IGroupFunctionRlService {

	private IGroupFunctionRlDAO groupFunctionRlDAO;
	private IFunctionDAO functionDAO;
	
	public void setGroupFunctionRlDAO(IGroupFunctionRlDAO groupFunctionRlDAO) {
		this.groupFunctionRlDAO = groupFunctionRlDAO;
	}	
	
	public IFunctionDAO getFunctionDAO() {
		return functionDAO;
	}

	public void setFunctionDAO(IFunctionDAO functionDAO) {
		this.functionDAO = functionDAO;
	}

	/**
	 *查找某个组的某个功能下的子功能
	 *返回的list是一个包含Function对象的list
	 */
	public List findSubFunctionsByGroup(Integer groupId, Integer parentFunctionId) {
		return groupFunctionRlDAO.findSubFunctionsByGroup(groupId,parentFunctionId);
	}

	/**
	 * 查找某个组的顶层功能
	 * 返回的list是一个包含Function对象的list
	 */
	public List findTopFunctionsByGroup(Integer groupId) {
		Integer TopFunctionParentID=new Integer(0);//一级功能的父功能Id为0
		return groupFunctionRlDAO.findSubFunctionsByGroup(groupId,TopFunctionParentID);
	}

	public void saveGroupFunctionRl(GroupFunctionRl groupFunctionRl,User user) {
		log.info("为用户组添加功能：用户组id="+groupFunctionRl.getGroupId()+"功能id="+groupFunctionRl.getFunctionId());
		this.checking();
		if(groupFunctionRl.getOrderSn()==null)groupFunctionRl.setOrderSn(new Integer(0));
		groupFunctionRlDAO.saveFunctionWithGroup(groupFunctionRl.getGroupId(),groupFunctionRl.getFunctionId(),user);
		
	}

	/**
	 * 删除某个组的某个功能及其下的所有子功能
	 * @param groupId
	 * @param functionId
	 */
	public void removeFunctionOfGroup(Integer groupId, Integer functionId,User user) {
		log.info("删除用户组中的功能：用户组id="+groupId+"功能id="+functionId+ ",及其下的所有子功能！");
		this.removeSubFunctionsOfGroup(groupId,functionId,user);

		GroupFunctionRl gf=(GroupFunctionRl)groupFunctionRlDAO.getGroupFunctionRl(groupId,functionId);
		if(gf!=null){
			groupFunctionRlDAO.deleteObject(GroupFunctionRl.class,gf.getGroupFunctionId(),user);
		}
	}

	/**
	 * 把指定父功能下的直接子功能全部分配给某个组
	 */
	public void addSubFunctionsToGroup(int parentFunctionId, int groupId,User user) {
		List list=groupFunctionRlDAO.findSubFunctonsByFunction(parentFunctionId);
		for(int i=0;i<list.size();i++){
			Function f=(Function)list.get(i);
			groupFunctionRlDAO.saveFunctionWithGroup(new Integer(groupId),f.getFunctionId(),user);
		}
		
	}

	/**
	 * 递归删除指定组下的子功能
	 * @param groupId
	 * @param functionId
	 */
	private void removeSubFunctionsOfGroup(Integer groupId,Integer functionId,User user){
		List subFunctionList=functionDAO.findAllSubFunctionsBelongGroup(functionId.intValue(),groupId.intValue());
		log.info("用户组:usergroupId=" + groupId + "下有"+subFunctionList.size()+"个子功能");
		if(subFunctionList.size()>0){
			for(int i=0;i<subFunctionList.size();i++){
				Function f=(Function)subFunctionList.get(i);
				this.removeFunctionOfGroup(groupId,f.getFunctionId(),user);
			}
		}else{
			GroupFunctionRl gf=(GroupFunctionRl)groupFunctionRlDAO.getGroupFunctionRl(groupId,functionId);
			if(gf!=null){
				groupFunctionRlDAO.deleteObject(GroupFunctionRl.class,gf.getGroupFunctionId(),user);
				log.info("删除用户组："+groupId+"的功能：" + functionId);
			}
		}
	}

	public void modifyOrderSn(int groupId,int functionId,int orderValue,User user) {
		GroupFunctionRl gfRl=
			(GroupFunctionRl)groupFunctionRlDAO.getGroupFunctionRl(new Integer(groupId),new Integer(functionId));
		if(gfRl.getOrderSn()==null){
			gfRl.setOrderSn(new Integer(0));
		}
		gfRl.setOrderSn(new Integer(gfRl.getOrderSn().intValue() + orderValue));
		groupFunctionRlDAO.saveObject(gfRl,user);
	}
	
	
}
