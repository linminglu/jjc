

package com.ram.dao.system;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

/**
 * @author yangjingyue
 * 系统设置层次DAO数据访问控制接口
 */
public interface ISystemTutorCenterDAO extends IDAO{
	
	public TutorCenter getTcByTcCode(String tcCode);
	/**
	 * 保存层次信息
	 * @param tutorCenter
	 * @return
	 */
	public void saveSystemTutorCenter(TutorCenter tutorCenter, User user);
	
	/**
	 * 通过主键进行查找TutorCenter
	 * @param id
	 */
	public TutorCenter getSystemTutorCenter(Integer id);
	
	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemTutorCenters();

	/**
	 * 物理删除
	 * @param
	 */
	public void deleteSystemTutorCenter(Integer id, User user);
	
	/**
	 * 逻辑删除
	 * @param
	 */
	public void removeSystemTutorCenter(Integer id, User user);
	
	/**
	 * 更改
	 * @param
	 */
	public void updateSystemTutorCenter(TutorCenter tutorCenter, User user);
	
	/**
	 * 获得指定学习中心ID的所有下级学习中心
	 * @param id
	 */
	public List findSubTutorCenterByTcId (Integer tcId);
	
	/**
	 * 得当前学习中心的所有上级学习中心	 * @param id
	 */
	public List findParentTutorCentersByTcLevel (int tcLevel);
	
	/**
	 * 递归得到所有的学习中心的集合 
	 */
	public List getTutorCenterList(int parentTcId, List list);

	public List getTcTree(int parentTcId);
	
	/**
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @return
	 */
	public List getTutorCenter();
	
	/**
	 *  通过parentTcId得到tcLevel 
	 */
	public int getTcLevelByParentTcId(int parentTcId);

	/**
	 * 获得总部学习中心--parent_id==0
	 * @return
	 */
	public TutorCenter getHeadQuarterTutorCenter();
	
	public List findALLTutorCenters(int firstResult, int maxResults);
	
	public int findALLTutorCenterNum();
	
	public TutorCenter getTutorCenterByUserId(Integer userId);
	
	/**
	 * 查询学生，教师，管理员的中心
	 */
	public TutorCenter getLearnerTutotCenter(Integer userId);
	public TutorCenter getTeacherTutotCenter(Integer userId);
	public TutorCenter getManagerTutotCenter(Integer userId);
	
	public PaginationSupport findElTutorCenterByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex);
	
	
	/**
	 * 查询教务公告中学习中心列表
	 */
	public List findTutorCenterForEduBulletin();
	
	/**
	 * 查询教务公告直属学习中心列表
	 */
	public List findTutorCenterForEduBulletinByParTcId(int tcId);
	
	/**
	 * 查询教务公告直属学习中心除tcIds之外的中心列表
	 * @param tcId
	 * @param tcIds
	 * @return
	 */
	public List findNotInTutorCenterForEduBulletinByParTcId(int tcId,int eduBulletinId);
	
	/**
	 * 查询除tcIds之外的学习中心
	 * @return
	 */
	public List findNotInTutorCenterForEduBulletin(int eduBulletinId);
	public List findInTutorCenterForEduBulletin(int eduBulletinId);
	
	/**
	 * 查找直属中心的下级中心，包括自身
	 * @param tcId
	 * @return
	 */
	public List findSubTutorCenter(Integer tcId);
	
	/**
	 * tcId 上级学习中心Id
	 * @param tcId
	 */
	public Integer getParentTcId(Integer tcId);	
	
	public List getAllTc();
}
