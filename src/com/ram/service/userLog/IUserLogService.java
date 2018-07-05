package com.ram.service.userLog;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.User;

public interface IUserLogService extends IService{
	/**
	 * 这个方法 是操作人员的日志写入数据库的方法
	 */
	
	public  void saveLog(User user,String action_text);
	

	/**
	 * 这个方法是用户在线日志的查询方法
	 * @startDateTime
	 * @endDateTime
	 * @loginName
	 * return list
	 */
   public List query(String loginName,Date beginDate,Date endDate);
   
  // public List query(UserLogForm logInfo);
   
	 //page 查询
	 
	
	/**
	 * 得到全部投诉的页面单元

	 * 
	 * @param order
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public PaginationSupport findALLUserLogPage(int startIndex, int pageSize,String loginName,Date beginDate,Date endDate);
	/**
	 * 查询投诉
	 * 
	 * @param detachedCriteria
	 * @param pageSize
	 * @param startIndex
	 */
	public PaginationSupport findUserLogsByCriteria(
			DetachedCriteria detachedCriteria, int pageSize, int startIndex);
}
