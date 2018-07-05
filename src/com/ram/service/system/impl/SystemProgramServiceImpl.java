/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system.impl;

import java.util.List;

import com.framework.service.impl.BaseService;
import com.ram.dao.system.ISystemProgramDAO;
import com.ram.model.Program;
import com.ram.model.User;
import com.ram.service.system.ISystemProgramService;

/**
 * @author yangjy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemProgramServiceImpl extends BaseService implements
	ISystemProgramService {
	
	private ISystemProgramDAO systemProgramDAO;
	/**
	 * IOC方式注入DAO
	 * @param DAO
	 */
	public void setSystemProgramDAO(ISystemProgramDAO systemProgramDAO){
		this.systemProgramDAO = systemProgramDAO;		
	};	

	/**
	 * 保存课程考核信息
	 * @param courseExamSet
	 * @return
	 */
	public void saveSystemProgram(Program program,User user){
		systemProgramDAO.saveSystemProgram(program,user);
	};

	/**
	 * 通过课程考核信息主键进行查找
	 * @param id
	 */
	public Program getSystemProgram(Integer id){
		Program program = systemProgramDAO.getSystemProgram(id);
		return program;
	};

	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemPrograms(){
		List list = systemProgramDAO.findSystemPrograms();
		return list;
	};

	/**
	 * 删除课程考核纪录
	 * @param elcourse
	 */
	public void removeSystemProgram(Integer id, User user){
		systemProgramDAO.removeSystemProgram(id, user);
	}
	
	/**
	 * 修改记录
	 * @param id
	 */
	public void updateSystemProgram(Program program, User user){
		systemProgramDAO.updateSystemProgram(program, user);		
	}
}
