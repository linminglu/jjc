

package com.ram.dao.system;

import java.util.List;

import com.ram.model.Semester;
import com.ram.model.User;
import com.framework.dao.IDAO;

/**
 * @author yangjingyue
 * 系统设置层次DAO数据访问控制接口
 */
public interface ISemesterDAO extends IDAO{
	
	public Semester getSemester(Integer id);
	public void saveSemester(Semester Semester,User user);
	public void updateSemester(Semester semester,User user);
	public void removeSemester(Integer id,User user);
	public void deleteSemester(Integer id,User user);
	public List findSemesters();	
	
	/**
	 * add by lgj
	 * 查找当前平台学习学期
	 */
	public Semester getCurrentSemester();
	
	/**
	 * 查找当前招生学期
	 * 招生学期和平台当前学期可能不一致
	 * 例如：当前还是01秋，但是可能已经开始招02春的学生了，即招生学期为02春
	 * @return
	 */
	public Semester getCurrentEnrollSemester();

	//得到上一学期的	public Semester getBeforeSemester();
	
	
	
}
