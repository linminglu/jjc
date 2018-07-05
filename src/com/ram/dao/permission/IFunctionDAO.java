package com.ram.dao.permission;

import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.Function;
import com.ram.model.User;

public interface IFunctionDAO extends IDAO {

	

  public List findAllFunctions(int firstResult);
  public int findAllFunctions_Num();
  /**
   * 定义根据functionTitle进行like查询
   * @param functionTitle
   * @return
   */
  public List findFunctionByLikeTitle(String functionTitle,int firstResult);
  public int findFunctionByLikeTitle_Num(String functionTitle);
  /**
   * 定义查询当前功能的直接子功能
   * @param curFunctionID
   * @return
   */
  public List findSubFunctionByCurrentFunction(int curFunctionId,int firstResult);
  public int findSubFunctionByCurrentFunction_Num(int curFunctionId);

  public Function getFunction(Integer functionId);
  public Function getFunction(String functionActionPath);
  
  public void saveFunction(Function function,User user);
  public void updateFunction(Function function,User user);
  public void removeFunction(Integer functionId,User user);
  public void deleteFunction(Integer functionId,User user);
  
  public List findAllFunctions();
  public List findSubFunctions(int parentFunctionId);
  public List findAllParentIdIsZero();
  public List findAllParentIdIsZeroByUserGroup(int userGroupId) ;
  public List findAllSubFunctionsNotBelongGroup(int parentFunctionId,int groupId);
  public List findAllSubFunctionsBelongGroup(int parentFunctionId,int groupId);
}
