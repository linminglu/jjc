

package com.ram.dao.system.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;

import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;


/**
 * @author yangjingyue
 * 系统设置层次DAO数据访问控制接口
 */
public class SystemTutorCenterDAOHibernate extends AbstractBaseDAOHibernate implements ISystemTutorCenterDAO{
	
	private static final String FIND_SUB_TUTOR_CENTER_BY_TC_ID = "select tutorCenter from TutorCenter tutorCenter where tutorCenter.parentTcId = ?";
	private static final String FIND_SUB_TUTOR_CENTER_AND_SELF = "select tutorCenter from TutorCenter tutorCenter where tutorCenter.parentTcId = ? or tutorCenter.tcId=?";
	private static final String FIND_HEAD_QUARTOR_TUTOR_CENTER = "select tutorCenter from TutorCenter tutorCenter where tutorCenter.parentTcId = 0";
	private static final String GET_TUTOR_CENTER = "select new com.ram.model.RecursionBean(tc.tcId,tc.parentTcId,tc.tcTitle) from TutorCenter tc";
	private static final String PARENTTUTORCENTERS = "from TutorCenter tutorCenter where tutorCenter.tcLevel = ?";
	private static final String TUTORCENTERBYPARENTID = "from TutorCenter tutorCenter where tutorCenter.parentTcId = ? ";
	private static final String Find_Sub_TutorCenter_By_ParentId="from TutorCenter tutorCenter where tutorCenter.parentTcId = ? or tutorCenter.tcId=? order by tutorCenter.parentTcId asc, tutorCenter.tcTitle asc";
	private static final String LEVELBYPARENTTCID = "from TutorCenter tutorCenter where tutorCenter.tcId = ?";	
	private static final String FIND_ALLCENTERS = "from TutorCenter tutorCenter order by tutorCenter.tcId asc";
	private static final String FIND_ALLCENTERNUM = "select count(tutorCenter.tcId) from TutorCenter tutorCenter";
	private static final String Find_Tc_By_TcCode="from TutorCenter tc where tc.tcCode=?";
	private static final String Find_TC_By_USERID="select tc from TutorCenter tc,User u,Learner l where u.userId=l.user.userId and l.tcId=tc.tcId and u.userId=? ";
	private static final String GET_LEARNER_TUTOT_CENTER = "select tc from TutorCenter tc,User u,Learner l where u.userId=l.user.userId and l.tcId=tc.tcId and u.userId=? ";
	private static final String GET_TEACHER_TUTOT_CENTER = "select tc from TutorCenter tc,User u,Tutor   t where u.userId=t.user.userId and t.tutorCenter.tcId=tc.tcId and u.userId=?";
	private static final String GET_MANAGER_TUTOT_CENTER = "select tc from TutorCenter tc,User u,Manager m where u.userId=m.user.userId and m.tcId=tc.tcId and u.userId=?";
	
	private static final String FIND_TUTORCENTER_FOR_EDU_BULLETIN = "from TutorCenter tc order by tc.tcId";
	private static final String FIND_NOT_IN_TUTORCENTER_FOR_EDU_BULLETIN ="from TutorCenter tc where tc.tcId not in (select bt.tcId from EduBulletinTc bt where bt.eduBulletin.eduBulletinId=?) order by tc.tcTitle";
	private static final String FIND_IN_TUTORCENTER_FOR_EDU_BULLETIN ="from TutorCenter tc where tc.tcId in (select bt.tcId from EduBulletinTc bt where bt.eduBulletin.eduBulletinId=?) order by tc.tcTitle";
	private static final String FIND_TUTORCENTER_FOR_EDU_BULLETIN_BY_PAR_TCID = "from TutorCenter tc where tc.tcId=? or tc.parentTcId=? order by tc.parentTcId";
	private static final String FIND_NOT_IN_TUTORCENTER_FOR_EDU_BULLETIN_BY_PAR_TCID = "from TutorCenter tc where (tc.tcId=? or tc.parentTcId=?) and tc.tcId not in(select bt.tcId from EduBulletinTc bt where bt.eduBulletin.eduBulletinId=?) order by tc.parentTcId";
	public TutorCenter getTcByTcCode(String tcCode){
		Query q=getSession().createQuery(Find_Tc_By_TcCode);
		q.setString(0,tcCode);
		List list=q.list();
		TutorCenter tc=null;
		if(list!=null && list.size()>0){
			tc=(TutorCenter)list.get(0);
		}
		return tc;
	}	
	
	/**
	 * 保存学习中心信息
	 * @param tutor
	 * @return
	 */
	public void saveSystemTutorCenter(TutorCenter tutorCenter, User user){
		saveObject(tutorCenter,user);
	};
	
	/**
	 * 通过主键进行查找tutorCenter
	 * @param id
	 */
	public TutorCenter getSystemTutorCenter(Integer id){
		if(id==null) return null;
		Object obj = getHibernateTemplate().get(TutorCenter.class, id);
		if(obj == null){
			return null;
		}        
        return (TutorCenter)obj;		
	};
	
	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemTutorCenters(){
		Query q = getSession().createQuery("from TutorCenter tc order by tc.parentTcId asc, tc.tcTitle");
		return q.list();
	}

	/**
	 * 物理删除
	 * @param tutorCenter
	 */
	public void deleteSystemTutorCenter(Integer id, User user){
		this.deleteObject(TutorCenter.class, id, user);		
	};
	
	/**
	 * 逻辑删除
	 * @param tutorCenter
	 */
	public void removeSystemTutorCenter(Integer id, User user){		
		this.removeObject(TutorCenter.class, id, user);
	}

	public void updateSystemTutorCenter(TutorCenter tutorCenter, User user) {
		this.updateObject(tutorCenter, user);		
	}
	
	/**
	 * 获得指定学习中心ID的所有下级学习中心
	 * @param id
	 */
	public List findSubTutorCenterByTcId (Integer tcId){
		Query query = getSession().createQuery(FIND_SUB_TUTOR_CENTER_BY_TC_ID);
		query.setInteger(0, tcId.intValue());
		return query.list();
	}
	
	/**
	 * 得当前学习中心的所有上级学习中心
	 * @param id
	 */
	public List findParentTutorCentersByTcLevel (int tcLevel){
		if(tcLevel - 1 >= 0){
			Query query = getSession().createQuery(PARENTTUTORCENTERS);
			query.setInteger(0, (tcLevel - 1));
			log.info("query.list().size()===" + query.list().size());
			return query.list();
		} else {
			return null;			
		}
	}
	
	/**
	 * 查找本中心以及本中心的下级中心
	 * 
	 * @param id
	 */
	public TutorCenter getTutorCenterByParentId(int parentId){
		Query query = getSession().createQuery(TUTORCENTERBYPARENTID);
		query.setInteger(0, parentId);
		
		return (TutorCenter)(query.list().get(0));
	};

	/**
	 * 递归得到所有的学习中心的集合 
	 */
	public List getTutorCenterList(int parentTcId, List list) {
		//Query query = getSession().createQuery(TUTORCENTERBYPARENTID);
		Query query = getSession().createQuery(this.Find_Sub_TutorCenter_By_ParentId);
		query.setInteger(0, parentTcId);
		query.setInteger(1, parentTcId);
		List subTcList=query.list();
		TutorCenter tutorCenter = null;
		int curTcId=0;
		if(subTcList.size() > 0){
			for(int i = 0; i < subTcList.size(); i++){
				tutorCenter = (TutorCenter)(subTcList.get(i));
				curTcId = tutorCenter.getTcId().intValue();
				if(curTcId==parentTcId){
					list.add(tutorCenter);
				}else{
					getTutorCenterList(curTcId, list);
				}
			}
			return list;
		} else {
			return list;
		}
	}
	
	public List getTcTree(int parentTcId){
//		String sql = "select new org.apache.struts.util.LabelValueBean(concat(lpad('-',level,'-'), tc.tcTitle), concat('',tc.tcId)) " +
//			" from tutor_center tc start with tc.tc_id=? connect by prior tc.tc_id=tc.parent_tc_id";
//		Query q = getSession().createQuery(sql);
		String sql = "select concat('', {a}.tc_id) id, concat(lpad('-',level,'-'), {a}.tc_title) title " +
			" from tutor_center {a} start with {a}.tc_id=? " +
			" connect by prior {a}.tc_id={a}.parent_tc_id";
		SQLQuery q = getSession().createSQLQuery(sql);
		q.addEntity("a", TutorCenter.class);
		q.addScalar("id", Hibernate.STRING);
		q.addScalar("title", Hibernate.STRING);
		q.setInteger(0, parentTcId);
		return q.list();
	}
	
	/**
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @return
	 */
	public List getTutorCenter(){
		Query query = getSession().createQuery(GET_TUTOR_CENTER);
		return query.list();
	}

	public int getTcLevelByParentTcId(int parentTcId) {
		if(parentTcId > 0){
			Query query = getSession().createQuery(LEVELBYPARENTTCID);
			query.setInteger(0, parentTcId);
			return ((TutorCenter)(query.list().get(0))).getTcLevel().intValue();
		} else {
			return 0;
		}
	}
	
	/**
	 * 获得总部学习中心
	 * parent_id=0
	 */
	public TutorCenter getHeadQuarterTutorCenter(){
		Query q=getSession().createQuery(FIND_HEAD_QUARTOR_TUTOR_CENTER);
		TutorCenter tc=(TutorCenter)q.uniqueResult();
		if(tc==null){
			log.error("最顶层的总部学习中心未设置，请设置一个再TutorCenter表中Parent_Id值为0的总部学习中心！");
		}
		return tc;
	}
	
	/**
	   * 查询学习中心列表，供分页用。

	   */
		public List findALLTutorCenters(int firstResult, int maxResults) {
			Query q = getSession().createQuery(FIND_ALLCENTERS);		
			q.setFirstResult(firstResult);
			q.setMaxResults(maxResults);		
			List result = q.list();
			return result;
		}

		/* (non-Javadoc)
		 * @see com.ram.el.dao.course.IElCourseDao#findALLElCoursesNum()
		 */
		public int findALLTutorCenterNum() {
			// TODO Auto-generated method stub	   
			List l = getHibernateTemplate().find(FIND_ALLCENTERNUM);		
		      if (l != null && !l.isEmpty()) {
		        return ( (Integer) l.get(0)).intValue();
		      }
		      else {
		        return 0;
		      }
		}
		
		public TutorCenter getTutorCenterByUserId(Integer userId){
			Query q=getSession().createQuery(Find_TC_By_USERID);
			q.setInteger(0,userId.intValue());
			TutorCenter tc=(TutorCenter)q.uniqueResult();
			if(tc==null){
				log.error("用户userid="+userId+"的学习中心未设置！");
			}
			return tc;
		}
		
		public TutorCenter getLearnerTutotCenter(Integer userId){
			Query q=getSession().createQuery(GET_LEARNER_TUTOT_CENTER);
			q.setInteger(0,userId.intValue());
			TutorCenter tc=(TutorCenter)q.uniqueResult();
			if(tc==null){
				log.error("学生用户userid="+userId+"的学习中心未设置！");
			}
			return tc;
		}
		public TutorCenter getTeacherTutotCenter(Integer userId){
			Query q=getSession().createQuery(GET_TEACHER_TUTOT_CENTER);
			q.setInteger(0,userId.intValue());
			TutorCenter tc=(TutorCenter)q.uniqueResult();
			if(tc==null){
				log.error("教师用户userid="+userId+"的学习中心未设置！");
			}
			return tc;
		}
		public TutorCenter getManagerTutotCenter(Integer userId){
			Query q=getSession().createQuery(GET_MANAGER_TUTOT_CENTER);
			q.setInteger(0,userId.intValue());
			TutorCenter tc=(TutorCenter)q.uniqueResult();
			if(tc==null){
				log.error("管理员用户userid="+userId+"的学习中心未设置！");
			}
			return tc;
		}
		
		public PaginationSupport findElTutorCenterByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex){
		    return this.findPageByCriteria(detachedCriteria ,pageSize,startIndex);    
		}
	
		public List findTutorCenterForEduBulletin(){
			Query q = getSession().createQuery(FIND_TUTORCENTER_FOR_EDU_BULLETIN);		
			return q.list();
		}
		
		
		public List findNotInTutorCenterForEduBulletin(int eduBulletinId){
			Query q = getSession().createQuery(FIND_NOT_IN_TUTORCENTER_FOR_EDU_BULLETIN);
			q.setInteger(0,eduBulletinId);
			return q.list();
		}
		public List findInTutorCenterForEduBulletin(int eduBulletinId){
			Query q = getSession().createQuery(FIND_IN_TUTORCENTER_FOR_EDU_BULLETIN);
			q.setInteger(0,eduBulletinId);
			return q.list();
		}
		
		public List findTutorCenterForEduBulletinByParTcId(int tcId){
			Query q = getSession().createQuery(FIND_TUTORCENTER_FOR_EDU_BULLETIN_BY_PAR_TCID);
			q.setInteger(0,tcId);
			q.setInteger(1,tcId);
			return q.list();
		}
		
	public List findNotInTutorCenterForEduBulletinByParTcId(int tcId,int eduBulletinId){
		Query q = getSession().createQuery(FIND_NOT_IN_TUTORCENTER_FOR_EDU_BULLETIN_BY_PAR_TCID);
		q.setInteger(0,tcId);
		q.setInteger(1,tcId);
		q.setInteger(2,eduBulletinId);
		return q.list();
	}
		
	public List findSubTutorCenter(Integer tcId){
		Query q = getSession().createQuery(FIND_SUB_TUTOR_CENTER_AND_SELF);
		q.setInteger(0,tcId.intValue());
		q.setInteger(1,tcId.intValue());
		return q.list();
	}
		
	public Integer getParentTcId(Integer tcId){
		String sql = "select tc.parentTcId from TutorCenter tc where tc.tcId=?";
		Query q = getSession().createQuery(sql);
		q.setInteger(0, tcId.intValue());
		List l = q.list();
		return l.size()==0?null:(Integer)l.get(0);
	}
	
	public List getAllTc(){
		Query q=getSession().createQuery("from TutorCenter tc order by tc.tcTitle");
		return q.list();
	}
}
