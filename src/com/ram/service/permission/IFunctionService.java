/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.permission;

import java.util.List;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.dao.permission.IFunctionDAO;
import com.ram.model.Function;
import com.ram.model.User;

/**
 * @author hulei
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface IFunctionService extends IService {
	/**
	 * IOC方式注入课程DAO
	 * 
	 * @param FunctionDAO
	 */
	public void setFunctionDAO(IFunctionDAO functionDAO);

	/**
	 * 通过持久化对象找到全部列表
	 * 
	 * @return
	 */
	public PaginationSupport findAllFunctions(int startIndex);

	/**
	 * 得到全部课程的页面单元
	 * 
	 * 
	 * @param order
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public PaginationSupport findALLSubFunctionsPage(int curFunctionId,
			int startIndex);

	public List findAllTopFunctions();

	public List findAllSubFunctions(int parentFunctionId);

	/**
	 * 得到所有顶层功能列表，即parentID=0的功能
	 * 
	 * @param user
	 */
	public PaginationSupport findAllTopFunctionsPage(int startIndex);

	/**
	 * 保存功能信息
	 * 
	 * @param Function
	 * @return
	 */
	public void saveFunction(Function function, User user);

	/**
	 * 通过功能主键进行查找
	 * 
	 * @param id
	 */
	public Function getFunction(Integer id);

	/**
	 * 删除课程纪录
	 * 
	 * @param course
	 */
	public void removeFunction(Function function, User user);

	/**
	 * 找到所有能够尚未分给某个组的，指定父功能下的所有子功能
	 * 
	 * @return
	 */
	public List findAllSubFunctionsNotBelongGroup(int parentFunctionId,
			int groupId);

	
	public List findAllSubFunctionsBelongGroup(int parentFunctionId,int groupId);
	
	public void saveOrderUp(int functionId,User user);
	
	public void saveOrderDown(int functionId,User user);
}
