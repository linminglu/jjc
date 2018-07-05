/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.system.ISemesterDAO;
import com.ram.model.Semester;
import com.ram.model.User;
import com.ram.service.system.ISemesterService;

/**
 * @author hulei
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SemesterServiceImpl extends BaseService implements
		ISemesterService {

	private ISemesterDAO semesterDAO;
	
//	public Semester getCurrentSemester(){
//		List list=semesterDAO.findSemesters();
//		Semester s=null;
//		for(int i=0;i<list.size();i++){
//			s=(Semester)list.get(i);
//			if(s.getSemesterStatus().equals("1")){
//				break;
//			}else{
//				log.info("循环semester,获取当前semester，当前循环无效,s.id=" + s.getSemesterId().intValue());
//			}
//		}
//		return s;
//	}
//	add by lgj
	public Semester getCurrentSemester(){
	    return semesterDAO.getCurrentSemester();
	}
//	add by lgj 得到上一个学期
	public Semester getBeforeSemester(){
	    return semesterDAO.getBeforeSemester();   
	}
	/**
	 * IOC方式注入DAO
	 * 
	 * @param DAO
	 */
	public void setSemesterDAO(ISemesterDAO systemSemesterDAO) {
		this.semesterDAO = systemSemesterDAO;
	};

	/**
	 * 保存学期信息
	 * 
	 * @param courseExamSet
	 * @return
	 */
	public void saveSemester(Semester semester, User user) {
		semesterDAO.saveSemester(semester, user);
	};

	/**
	 * 通过学期信息主键进行查找
	 * 
	 * @param id
	 */
	public Semester getSemester(Integer id) {
		Semester semester = semesterDAO.getSemester(id);
		return semester;
	};

	/**
	 * 通过持久化对象找到全部列表
	 * 
	 * @return
	 */
	public List findSemesters() {
		List list = semesterDAO.findSemesters();
		return list;
	};

	public void removeSemester(Integer semesterId, User user) {
		semesterDAO.removeSemester(semesterId, user);
	}

	/**
	 * 保存所有的semester为非当前状态
	 */
	public void saveAllSemesterNotCurrentSemester(User user) {
		List list = semesterDAO.findSemesters();
		for (int i = 0; i < list.size(); i++) {
			Semester semester = (Semester) list.get(i);
			semester.setSemesterStatus("0");
			semesterDAO.saveObject(semester, user);
		}

	}

	public void updateSemester(Semester semester, User user) {

		semesterDAO.updateObject(semester, user);

	}

	public void deleteSemester(Integer semesterId, User user) {

		semesterDAO.deleteObject(Semester.class, semesterId,user);

	}

	/**
	 * 得到当前招生学期
	 */
	 public Semester getCurrentEnrollSemester(){
		 return semesterDAO.getCurrentEnrollSemester();
	 }
 }

