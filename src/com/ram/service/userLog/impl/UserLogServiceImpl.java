package com.ram.service.userLog.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.ram.dao.userLog.IUserLogDAO;
import com.ram.model.User;
import com.ram.service.userLog.IUserLogService;

public class UserLogServiceImpl extends BaseService implements
IUserLogService{
	
	private IUserLogDAO userLogDAO;

	/**
	 * IOC方式注入DAO
	 * 
	 * @param DAO
	 */
	public void setUserLogDAO(IUserLogDAO userLogDAO) {
		this.userLogDAO = userLogDAO;
	};
	
	
	/**
	 * 这个方法 是操作人员的日志写入数据库的方法
	 */
	
	public void saveLog(User user,String action_text){
		userLogDAO.saveLog(user,action_text);
	};
	

	/**
	 * 这个方法是用户在线日志的查询方法
	 * @startDateTime
	 * @endDateTime
	 * @loginName
	 * return list
	 */
   public List query(String loginName,Date beginDate,Date endDate){
	   return userLogDAO.query(loginName,beginDate,endDate);
   };
   
	 //page 查询
	 
		/**
		 * 得到全部投诉的页面单元

		 * 
		 * @param order
		 * @param firstResult
		 * @param maxResults
		 * @return
		 */
		public PaginationSupport findALLUserLogPage(int startIndex, int pageSize,String loginName,Date beginDate,Date endDate) {
			List l = userLogDAO.findALLUserLog(startIndex,pageSize,loginName,beginDate,endDate);
			int num = userLogDAO.findALLUserLogsNum(loginName,beginDate,endDate);
			this.checking();
			PaginationSupport ps = new PaginationSupport(l, num, pageSize,
					startIndex);
			return ps;
		}

		/**
		 * 查询投诉根据页面
		 * @param detachedCriteria
		 * @param pageSize
		 * @param startIndex
		 */
		public PaginationSupport findUserLogsByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex){
			return userLogDAO.findElUserLogsByCriteria(detachedCriteria, pageSize,
					startIndex);
		}

	

   
   
   
}
