/*
 * Created on 2005-7-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.service.system;

import java.util.List;

import com.framework.service.IService;
import com.ram.dao.system.ISystemProgramDAO;
import com.ram.model.Program;
import com.ram.model.User;

/**
 * @author yangjy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ISystemProgramService extends IService {
	/**
	 * IOC方式注入层次DAO
	 * @param elCourseDAO
	 */
	public void setSystemProgramDAO(ISystemProgramDAO systemProgramDAO);

	/**
	 * 保存层次信息
	 * @param courseExamSet
	 * @return
	 */
	public void saveSystemProgram(Program Program, User user);

	/**
	 * 通过层次主键进行查找
	 * @param id
	 */
	public Program getSystemProgram(Integer id);

	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemPrograms();

	/**
	 * 删除纪录
	 * @param elcourse
	 */
	public void removeSystemProgram(Integer id, User user);

	/**
	 * 修改记录
	 * @param id
	 */
	public void updateSystemProgram(Program program, User user);
}
