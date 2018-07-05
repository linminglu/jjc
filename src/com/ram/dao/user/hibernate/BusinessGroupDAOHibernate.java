package com.ram.dao.user.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.RamConstants;
import com.ram.dao.user.IBusinessGroupDAO;
import com.ram.model.BusinessGroup;
import com.ram.model.User;

public class BusinessGroupDAOHibernate extends AbstractBaseDAOHibernate
		implements IBusinessGroupDAO {
	private final String GET_ALL_BUSINESSGROUP = "from BusinessGroup as businessGroup order by businessGroup.bizGroupId";
	private final String GET_COUNT_ALL_BUSINESSGROUP = "select count(*) from BusinessGroup  ";
	public BusinessGroupDAOHibernate() {

	}

	public void deleteBusinessGroup(int businessGroupId, User user) {
		this.deleteObject(BusinessGroup.class, new Integer(businessGroupId),
				user);
	}

	public void saveBusinessGroup(BusinessGroup businessGroup, User user) {
		this.saveObject(businessGroup, user);
	}


	
	public List findMyCreatedBusinessGroup(int myUserId) {
		//String hqlFindAllBusinessGroup = "select bg from BusinessGroup bg left join fetch bg.semester left join fetch bg.tutorCenter left join fetch bg.program left join fetch bg.learnerGroup left join fetch bg.userGroup  order by bg.bizGroupId asc";
		String hqlFindAllBusinessGroup = "select bg from BusinessGroup bg " +
				"left join fetch bg.semester " +
				"left join fetch bg.tutorCenter " +
				"left join fetch bg.program " +
				"left join fetch bg.learnerGroup " +
				"left join fetch bg.userGroup  " +
				"where bg.createUserId=?  " +
				"order by bg.bizGroupId asc";		
		Query q = this.getSession().createQuery(hqlFindAllBusinessGroup);
		q.setInteger(0,myUserId);
		
		return q.list();
	}	
	
	public List findAllBusinessGroup() {
		//String hqlFindAllBusinessGroup = "select bg from BusinessGroup bg left join fetch bg.semester left join fetch bg.tutorCenter left join fetch bg.program left join fetch bg.learnerGroup left join fetch bg.userGroup  order by bg.bizGroupId asc";
		String hqlFindAllBusinessGroup = "select bg from BusinessGroup bg " +
				"left join fetch bg.semester " +
				"left join fetch bg.tutorCenter " +
				"left join fetch bg.program " +
				"left join fetch bg.learnerGroup " +
				"left join fetch bg.userGroup  " +
				"order by bg.bizGroupId asc";		
		Query q = this.getSession().createQuery(hqlFindAllBusinessGroup);
		return q.list();
	}
	
	public BusinessGroup getBusinessGroup(int businessGroupId){
		String hqlGetBusinessGroup = "select bg from BusinessGroup bg left join fetch bg.semester left join fetch bg.tutorCenter left join fetch bg.program left join fetch bg.learnerGroup left join fetch bg.userGroup  where bg.bizGroupId=?";
		Query q = this.getSession().createQuery(hqlGetBusinessGroup);
		q.setInteger(0,businessGroupId);
		List list=q.list();
		BusinessGroup bg=null;
		if(list.size()>0){
			bg=(BusinessGroup)list.get(0);
		}
		return bg;
	}
	
	public PaginationSupport getAllBusinessGroupByQuery(DetachedCriteria detachedCriteria,int pageSize, int startIndex) throws DataAccessException{
		return this.findPageByCriteria(detachedCriteria ,pageSize,startIndex); 
	}
	
	public PaginationSupport getAllBusinessGroupByQuery(DetachedCriteria detachedCriteria) throws DataAccessException{
		return this.findPageByCriteria(detachedCriteria); 
	}
	
	public List getAllBusinessGroups(int startIndex, int perPageCount) throws DataAccessException{
		Query query = this.getSession().createQuery(GET_ALL_BUSINESSGROUP);
		query.setFirstResult(startIndex);
		query.setMaxResults(perPageCount);
		List businessGroupList = query.list();
		return businessGroupList;
	}
	
	public int getBusinessGroupsCount() throws DataAccessException{
		Query query = this.getSession().createQuery(GET_COUNT_ALL_BUSINESSGROUP);
		List businessGroupList = query.list();
		int totalCount = ((Integer)businessGroupList.get(0)).intValue();
		return totalCount;
	}
	
	public boolean ExistBusinessGroups(String businessGroupIdstr,User user) throws DataAccessException{
		String str = "";
		boolean isExist = false;
		List businessGroups = null;
		BusinessGroup businessGroup = null;	

		if(user.getUserType().equals(RamConstants.UserTypeIsTutor1)){
			//log.info("is tutor:");
			str = "select businessGroup from BusinessGroup businessGroup,Tutor tutor where tutor.user.userId=? and (businessGroup.tutorCenter.tcId is null or tutor.tutorCenter.tcId=businessGroup.tutorCenter.tcId) and (businessGroup.userGroup.userGroupId is null or businessGroup.userGroup.userGroupId in (select userGroupRl.userGroup.userGroupId from UserGroupRl userGroupRl where userGroupRl.user.userId=?))";
			Query query = this.getSession().createQuery(str);
			query.setInteger(0,user.getUserId().intValue());
			query.setInteger(1,user.getUserId().intValue());
			businessGroups = query.list();
			//		log.info("dddddddddddd busgroups size is:"+businessGroups.size());			
		}else if(user.getUserType().equals(RamConstants.UserTypeIsManager2)){
			str = "select businessGroup from BusinessGroup as businessGroup,Manager manager where manager.user.userId=? and (businessGroup.tutorCenter.tcId is null or manager.tcId=businessGroup.tutorCenter.tcId) and (businessGroup.userGroup.userGroupId is null or businessGroup.userGroup.userGroupId in (select userGroupRl.userGroup.userGroupId from UserGroupRl userGroupRl where userGroupRl.user.userId=?))";
			Query query = this.getSession().createQuery(str);
			query.setInteger(0,user.getUserId().intValue());
			query.setInteger(1,user.getUserId().intValue());
			businessGroups = query.list();
		}else{
//log.info("is learner:");
//			str = "select businessGroup from BusinessGroup businessGroup,Learner learner where (businessGroup.tutorCenter.tcId is null or businessGroup.tutorCenter.tcId=learner.tcId) and (businessGroup.semester.semesterId is null or businessGroup.semester.semesterId=learner.semesterId) and (businessGroup.program.programId is null or businessGroup.program.programId=learner.programId) and ( businessGroup.userGroup.userGroupId is null or businessGroup.userGroup.userGroupId in (select userGroupRl.userGroup.userGroupId from UserGroupRl userGroupRl where userGroupRl.user.userId=?)) ";
			str = "select businessGroup from BusinessGroup businessGroup,Learner learner where learner.user.userId=? and (businessGroup.tutorCenter.tcId is null or businessGroup.tutorCenter.tcId=learner.tcId) and (businessGroup.semester.semesterId is null or businessGroup.semester.semesterId=learner.semesterId) and (businessGroup.program.programId is null or businessGroup.program.programId=learner.programId) and ( businessGroup.userGroup.userGroupId is null or businessGroup.userGroup.userGroupId in (select userGroupRl.userGroup.userGroupId from UserGroupRl userGroupRl where userGroupRl.user.userId=?))";
			Query query = this.getSession().createQuery(str);
			query.setInteger(0,user.getUserId().intValue());
			query.setInteger(1,user.getUserId().intValue());
			businessGroups = query.list();
		}
//log.info("aaa:"+businessGroups);
		String[] businessGroupIds = businessGroupIdstr.split(",");
//log.info("the  eeeeeeeeeeeeeee size is:"+ businessGroupIds);		
		if(businessGroupIds!=null&&businessGroupIds.length>0){
			Iterator iterator = businessGroups.iterator();
			while(iterator.hasNext()){
				businessGroup = (BusinessGroup) iterator.next();
				for(int i=0;i<businessGroupIds.length;i++){
					if(businessGroupIds[i].equals(businessGroup.getBizGroupId().toString())){
						isExist = true;
						break;
					}
						
				}
			}
		}
//log.info("in dao isExist is:"+isExist);
		return isExist;
	}
	
	}
