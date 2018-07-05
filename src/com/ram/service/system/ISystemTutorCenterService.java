/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.model.TutorCenter;
import com.ram.model.User;

/**
 * @author yangjy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISystemTutorCenterService extends IService{
	
	public TutorCenter getTutorCenterByTcCode(String tcCode);
	
	/**
	 * IOC方式注入层次DAO
	 * @param systemTutorCenterDAO
	 */
	public void setSystemTutorCenterDAO(ISystemTutorCenterDAO systemTutorCenterDAO);	

	/**
	 * 保存层次信息
	 * @param courseExamSet
	 * @return
	 */
	public void saveSystemTutorCenter(TutorCenter tutorCenter, User user);

	/**
	 * 通过层次主键进行查找
	 * @param id
	 */
	public TutorCenter getSystemTutorCenter(Integer id);

	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemTutorCenters();
	
	/**
	 * 删除纪录
	 * @param elcourse
	 */
	public void removeSystemTutorCenter(Integer id, User user);
	
	/**
	 * 修改记录
	 * @param id
	 */
	public void updateSystemTutorCenter (TutorCenter tutorCenter,User user);
	
	/**
	 * 获得指定学习中心ID的所有下级学习中心
	 * @param id
	 */
	public List findSubTutorCenterByTcId (Integer tcId);
	
	public void deleteSystemTutorCenter(Integer id, User user);
	
	/**
	 * 得到指定学习中心的所有上级学习中心 
	 * 
	 */
	public List findParentTutorCentersByTcLevel(int tcLevel);
	
	/**
	 * 递归得到所有的学习中心
	 * 
	 */
	public List findAllSubTutorCenters(int tcId, List list);
	
	/**
	 *  通过parentTcId得到tcLevel 
	 */
	public int getTcLevelByParentTcId(int parentTcId);	
	
	/**
	 * 得到学习中心的树状结构集合
	 * @param start
	 * @return
	 */
	public List getTutorCenterTree(int parentTcId);
	
	/**
	 * 得到学习中心子孙id集合
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @param cid
	 * @return
	 */
	public Integer[] getSubTcId(int cid);
	
	public PaginationSupport findALLTutorCenterPage(int startIndex, int pageSize);
	public TutorCenter getTutorCenterByUserId(Integer userId);
	
	/**
	 * 找出用户的中心Id
	 */
	public TutorCenter getUserTutorCenter(Integer userId);
	public PaginationSupport findTutorCenterByCriteria(
			DetachedCriteria detachedCriteria, int pageSize, int startIndex);
	
	public TutorCenter getTutorCenter(Integer tcId);
	
	/**
	 * 查询教务公告中学习中心列表
	 * 总部显示所有学习中心
	 * 直属中心显示其以下的中心
	 * 地方中心显示地方中心
	 */
	public List findTutorCenterForEduBulletin(int tcId);
	
	/**
	 * 查询除tcIds之外的学习中心
	 * @return
	 */
	public List findNotInTutorCenterForEduBulletin(int tcId,int eduBulletinId);
	public List findInTutorCenterForEduBulletin(int eduBulletinId);
	
	/**
	 * 查找直属中心的下级中心，包括自身
	 * @param tcId
	 * @return
	 */
	public List findSubTutorCenter(Integer tcId);
	
	/**
	 * tcId 是否为selfTcId或子学习中心Id return true:tcId false:selfTcId
	 * @param tcId
	 * @param selfTcId
	 */
	public Integer tcIdInMyScope(Integer tcId, Integer selfTcId);
	
	public List getAllTc();
}
