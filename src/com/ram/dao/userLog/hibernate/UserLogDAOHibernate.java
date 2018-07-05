package com.ram.dao.userLog.hibernate;



import java.util.Date;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;

import com.ram.dao.userLog.IUserLogDAO;
import com.ram.model.User;
import com.ram.model.UserLog;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.*;

public class UserLogDAOHibernate  extends AbstractBaseDAOHibernate implements
IUserLogDAO{
	
	private final static String FIND_USERLOG_BY_USER="from UserLog userLog where userLog.loginName=? and " +
		"userLog.dateTime>=? and userLog.dateTime<=? and (userLog.managerId=0 or userLog.managerId is null) order by userLog.userLogId desc";
	private final static String FIND_USERLOG_BY_DATE="from UserLog userLog where userLog.dateTime>=? and " +
		"userLog.dateTime<=? and (userLog.managerId=0 or userLog.managerId is null) order by userLog.userLogId desc";
	private final static String FIND_ALLELUSERLOGSNUM="select count(userLog.userLogId) from UserLog userLog " +
		"where userLog.loginName=? and userLog.dateTime>=? and userLog.dateTime<=? and (userLog.managerId=0 or userLog.managerId is null)" ;
	private final static String FIND_ALLMEUSERLOGSNUM="select count(userLog.userLogId) from UserLog userLog " +
		"where userLog.dateTime>=? and userLog.dateTime<=?  and (userLog.managerId=0 or userLog.managerId is null)";
	/**
	 * 这个方法 是操作人员的日志写入数据库的方法
	 */
	
	public void saveLog(User user,String action_text){
		UserLog userLog=new UserLog();
		if(user==null){
			user=new User();
			user.setUserId(new Integer(0));
			user.setLoginName("未知用户");
		}
		userLog.setUserId(user.getUserId());
		userLog.setActionText(action_text);
		userLog.setLoginName(user.getLoginName());
		userLog.setDateTime(DateTimeUtil.parse(DateTimeUtil.getDateTime()));
		Integer managerId = null;//user.getManagerId();
		userLog.setManagerId(managerId == null ? new Integer(0) : managerId);
		saveObject(userLog,user);
		

		

	}
	/**
	 * 这个方法是用户在线日志的查询方法
	 * @startDateTime
	 * @endDateTime
	 * @loginName
	 * return list
	 */
   public List query(String loginName,Date beginDate,Date endDate){
	   if(loginName.length()>0)
	   {
       Query query=getSession().createQuery(FIND_USERLOG_BY_USER);	
       query.setString(0,loginName);
       query.setDate(0,beginDate);
       query.setDate(1,endDate);
       return query.list();
	   }
	   else
	   {
		   Query query=getSession().createQuery(FIND_USERLOG_BY_DATE);	
	       query.setDate(0,beginDate);
	       query.setDate(1,endDate);
	       return query.list();
		   
	   }
	   
   }
	 
	 //page查询
		public List findALLUserLog(int firstResult, int maxResults,String loginName,Date beginDate,Date endDate) {
			   if(loginName.length()>0)
			   {
		       Query query=getSession().createQuery(FIND_USERLOG_BY_USER);	
		       query.setString(0,loginName);
		       query.setDate(1,beginDate);
		       query.setDate(2,endDate);
		       query.setFirstResult(firstResult);
			   query.setMaxResults(maxResults);
		       return query.list();
			   }
			   else
			   {
				   Query query=getSession().createQuery(FIND_USERLOG_BY_DATE);	
			       query.setDate(0,beginDate);
			       query.setDate(1,endDate);
			       query.setFirstResult(firstResult);
				   query.setMaxResults(maxResults);
			       return query.list();   
			   }
		}
		/**
		 * 查找全部投诉的数据量,为分页提供依据


		 * @return
		 */
		public int findALLUserLogsNum(String loginName,Date beginDate,Date endDate) {
			// TODO Auto-generated method stub
			
			if(loginName.length()>0)
			{
				
				   Query query=getSession().createQuery(FIND_ALLELUSERLOGSNUM);	
			       query.setString(0,loginName);
			       query.setDate(1,beginDate);
			       query.setDate(2,endDate);
			       List l=query.list();  
			      if (l != null && !l.isEmpty()) {
				        return ( (Integer) l.get(0)).intValue();
				      }
				      else {
				        return 0;
				      }
				
			}
			else
			{
		    	   Query query=getSession().createQuery(FIND_ALLMEUSERLOGSNUM);	
			      // query.setString(0,loginName);
			       query.setDate(0,beginDate);
			       query.setDate(1,endDate);
			       List l=query.list();  
			       if (l != null && !l.isEmpty()) {
				        return ( (Integer) l.get(0)).intValue();
				      }
				      else {
				        return 0;
				      }
			}
			
	
		}
		/**
		 * 查询投诉
		 * @param detachedCriteria
		 * 
		 * @param pageSize
		 * @param startIndex
		 */
		public PaginationSupport findElUserLogsByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex){
		    return this.findPageByCriteria(detachedCriteria ,pageSize,startIndex); 
		}

}
