

package com.ram.dao.system;

import java.util.List;

import com.ram.model.Program;
import com.ram.model.User;
import com.framework.dao.IDAO;

/**
 * @author yangjingyue
 * 系统设置层次DAO数据访问控制接口
 */
public interface ISystemProgramDAO extends IDAO{
	
	/**
	 * 保存层次信息
	 * @param program
	 * @return
	 */
	public void saveSystemProgram(Program program,User user);
	
	/**
	 * 通过主键进行查找Program
	 * @param id
	 */
	public Program getSystemProgram(Integer id);
	
	/**
	 * 通过持久化对象找到全部列表
	 * @return
	 */
	public List findSystemPrograms();

	/**
	 * 物理删除
	 * @param program
	 */
	public void deleteSystemProgram(Integer id, User user);
	
	/**
	 * 逻辑删除
	 * @param id
	 */
	public void removeSystemProgram(Integer id, User user);
	
	/**
	 * 更改
	 * @param id
	 */
	public void updateSystemProgram(Program program,User user);

}
