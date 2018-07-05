/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.system.ISemesterDAO;
import com.ram.model.Semester;
import com.ram.model.User;

/**
 * @author hulei
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface ISemesterService extends IService {
	/**
	 * IOC方式注入层次DAO
	 * 
	 * @param elCourseDAO
	 */
	public void setSemesterDAO(ISemesterDAO systemSemesterDAO);


	public Semester getSemester(Integer id);
	public Semester getCurrentSemester();
	public List findSemesters();	
	public void saveSemester(Semester semester, User user);
	public void updateSemester(Semester semester,User user);
	public void removeSemester(Integer semesterId,User user);
	public void deleteSemester(Integer semesterId,User user);
	
	/**
	 * 得到当前招生学期
	 * @return
	 */
	public Semester getCurrentEnrollSemester();
	//add by lgj 得到上一个学期
	public Semester getBeforeSemester();

	/**
	 * 设置所有的学期为非当前学期
	 *
	 */
	public void saveAllSemesterNotCurrentSemester(User user);
}
