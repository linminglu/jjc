package com.ram.dao.userLog;

import java.util.Date;




import com.ram.model.User;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
/**
 * 
 * @author wangyuanjun
 *
 */
public interface IUserLogDAO  extends IDAO{
	/**
	 * 这个方法 是操作人员的日志写入数据库的方法
	 */
	
	public void saveLog(User user,String action_text);
	
	/**
	 * 这个方法是用户在线日志的查询方法
	 * @startDateTime
	 * @endDateTime
	 * @loginName
	 * return list
	 */
   public List query(String loginName,Date beginDate,Date endDate);
   
	 
	 //page 查询
	 
	 public List findALLUserLog(int firstResult, int maxResults,String loginName,Date beginDate,Date endDate) ;
		/**
		 * 查找全部投诉的数据量,为分页提供依据


		 * @return
		 */
		public int findALLUserLogsNum(String loginName,Date beginDate,Date endDate);
		

		/**
		 * 查询投诉根据页面
		 * @param detachedCriteria
		 * @param pageSize
		 * @param startIndex
		 */
		public PaginationSupport findElUserLogsByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex);

	

}
