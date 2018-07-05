package com.ram.service.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.Manager;
import com.ram.model.Position;
import com.ram.model.User;

/**
 * @author lixiaodong 
 */
public interface IManagerPositionService extends IService {
	
	/**
	 * 根据职位ID删除相应的职位
	 */ 
	public boolean deletePosition(Integer id, User user);

	/**
	 * 获得Manager和职位之间一对多/一对一的关系
	 */ 
	public List getManagerPositionRL(int managerId);
	
	/**
	 * 获得Manager可选的职位列表
	 */ 
	public List getManagerPosition(int managerId);
	
	/**
	 * 添加Manager和职位的关系
	 */ 
	public boolean addManagerPositionRL(int managerId, int[] positionId, User user);
	
	/**
	 * 删除Manager和职位之间一对多/一对一的关系
	 */ 
	public boolean deleteManagerPositionRL(int managerId, int positionId, User user);
	
	/**
	 * 保存Manager对象
	 */ 
	public Manager saveAndReturnManager(Manager manager, User user);
	
	public void saveManager(Manager manager, User user);
	
	/**
	 * 保存Position对象
	 */ 
	public boolean savePosition(Position position, User user) throws DataAccessException;
	
	/**
	 * 获得Manager对象列表(分页)
	 */
	public PaginationSupport findALLManagerForPage(int startIndex,int pageSize);
	
	/**
	 * 根据查询条件获得Manager对象列表(分页)
	 */
	public PaginationSupport findALLManagerForPage(int startIndex,int pageSize, DetachedCriteria detachedCriteria);
	
	/**
	 * 获得Position对象
	 */ 
	public Object getPosition(Integer id) throws DataAccessException;
	
	/**
	 * 获得Position对象列表
	 */ 
	public List getAllPosition() throws DataAccessException;

	/**
	 * 删除Position对象
	 */ 
	public boolean deletePosition(Position position, User user) throws DataAccessException;
}
