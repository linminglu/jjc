package com.ram.dao.user.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.ram.dao.user.ILearnerGroupDAO;
import com.ram.model.LearnerGroup;
import com.ram.model.User;

public class LearnerGroupDAOHibernate extends AbstractBaseDAOHibernate
		implements ILearnerGroupDAO {

	public LearnerGroupDAOHibernate() {

	}

	public LearnerGroup getLearnerGroup(int learnerGroupId){
		return (LearnerGroup)getObject(LearnerGroup.class,new Integer(learnerGroupId));
	}
	
	public void deleteLearnerGroup(int LearnerGroupId, User user) {
		this.deleteObject(LearnerGroup.class, new Integer(LearnerGroupId),
				user);
	}

	public void saveLearnerGroup(LearnerGroup LearnerGroup, User user) {
		this.saveObject(LearnerGroup.class, user);
	}

	public List findAllLearnerGroup(Integer tcId,Integer scheduleCourseId,Integer userId) {
		String hql = "";
		if(scheduleCourseId.intValue() > 0){
			hql = "from Classes cls where cls.tcId=? and cls.courseId=? and cls.classId in(select cm.classes.classId from ClassMember cm where cm.userId=?)";
		}else{
			hql = "from Classes cls where cls.tcId=? and cls.classId in(select cm.classes.classId from ClassMember cm where cm.userId=?)";
		}
		Query q = this.getSession().createQuery(hql);
		q.setInteger(0,tcId.intValue());
		if(scheduleCourseId.intValue() > 0){
			q.setInteger(1,scheduleCourseId.intValue());
			q.setInteger(2,userId.intValue());
		}else{
			q.setInteger(1,userId.intValue());
		}
		
		return q.list();
	}
	
	public List findUserLearnerGroupByUserId(Integer userId){
		String hql = "from ClassMember cm where cm.userId = ?";
		Query q = getSession().createQuery(hql);
		q.setInteger(0,userId.intValue());
		return q.list();
	}
}
