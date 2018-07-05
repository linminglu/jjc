package com.ram.dao.system.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.ram.dao.system.ISemesterDAO;
import com.ram.model.Semester;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * @author hulei 系统设置学期DAO数据访问控制接口
 */
public class SemesterDAOHibernate extends AbstractBaseDAOHibernate implements
		ISemesterDAO {

	//查找当前学习学期
    private static final String FIND_CURREN_SEMETER = "from Semester s where s.semesterStatus=1";
    
    //查找学期为当前招生的学期，因为招生的学期可能会和学生学习的学期不一样。
    private static final String FIND_CURRENT_Zhaosheng_Semester="from Semester s where s.isEnrollSemester=1";
    
    private static final String FIND_BEFORE_SEMETER = "from Semester s where s.enrollYear=? and s.enrollSeason=?";
	/**
	 * 保存学期信息
	 * 
	 * @param semester
	 * @return
	 */
	public void saveSemester(Semester semester, User user) {
		saveObject(semester, user);
	};

	/**
	 * 通过主键进行查找Semester
	 * 
	 * @param id
	 */
	public Semester getSemester(Integer id) {
		if(id ==null)return null;
		Semester semester = (Semester) getObject(Semester.class, id);
		if (semester == null) {
			log.warn("semester '" + id + "' not found...");
			throw new ObjectRetrievalFailureException(Semester.class, id);
		}
		return semester;
	};

	/**
	 * 通过持久化对象找到全部列表
	 * 
	 * @return
	 */
	public List findSemesters() {
		Query q = getSession().createQuery(
				"from Semester s order by s.semesterId desc");
		return q.list();
	}



	/**
	 * 删除学期
	 * 
	 * @param id
	 */
	public void removeSemester(Integer id, User user) {
		this.removeObject(Semester.class, id, user);
	}

	public void updateSemester(Semester semester, User user) {
		this.updateObject(semester,user);
	}

	public void deleteSemester(Integer id, User user) {
		this.deleteObject(Semester.class,id,user);
	}
	
	
	/**
	 * 查找当前平台学习学期
	 */
	public Semester getCurrentSemester(){
	    	Query query=getSession().createQuery(FIND_CURREN_SEMETER);
	    	if(query.list()==null){
	    	return null;    
	    	}else
	    	return (Semester)query.list().get(0);
	}
	
	/**
	 * 查找当前招生学期
	 * 招生学期和平台当前学期可能不一致
	 * 例如：当前还是01秋，但是可能已经开始招02春的学生了，即招生学期为02春
	 * @return
	 */
	public Semester getCurrentEnrollSemester(){
	    	Query query=getSession().createQuery(FIND_CURRENT_Zhaosheng_Semester);
	    	if(query==null || query.list().size()==0){
	    		return null;    
	    	}else
	    		return (Semester)query.list().get(0);
	}
	
	
//	得到上一学期的
	public Semester getBeforeSemester(){
	    Semester semester=getCurrentSemester();
	    if(semester==null){
	    return null;    
	    }
	    Integer year=semester.getEnrollYear();
	    Integer season=semester.getEnrollSeason();
	    if(season.intValue()==1){
	       year=new Integer(year.intValue()-1); 
	    }
	    if(season.intValue()==2){
	      season=new Integer(1);    
	    }
	    Query query=getSession().createQuery(FIND_BEFORE_SEMETER);
	    query.setInteger(0,year.intValue());
	    query.setInteger(1,season.intValue());
    	if(query.list()==null){
    	return null;    
    	}else
    	return (Semester)query.list().get(0);
	}

}
