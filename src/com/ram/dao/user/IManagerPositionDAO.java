package com.ram.dao.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.model.Manager;
import com.ram.model.Position;
import com.ram.model.User;

public interface IManagerPositionDAO extends IDAO{
	
	/**
	 * 获得所有Manager对象
	 */ 
	public List getAllManagers(int startIndex, int perPageCount) throws DataAccessException;
	
	/**
	 * 根据查询条件获得所有Manager对象
	 */ 
	public PaginationSupport findAllManagers(int startIndex,int pageSize, final DetachedCriteria detachedCriteria);
	
	
	/**
	 * 获得Manager对象的数量
	 */ 
	public int getManagersCount() throws DataAccessException;
	
	/**
	 * 获得所有Position对象
	 */ 
	public List getAllPositions() throws DataAccessException;
	
	/**
	 * 获得Position对象的数量
	 */ 
	public int getPositionsCount() throws DataAccessException;
	
	/**
	 * 保存Manager对象
	 */ 
	public Manager saveAndReturnManager(Manager manager, User user);
	
	public void saveManager(Manager manager, User user);
	
	/**
	 * 保存Position对象
	 */ 
	public void savePosition(Position position, User user) throws DataAccessException;
	
	/**
	 * 获得Position对象
	 */ 
	public Object getPosition(Integer id) throws DataAccessException;
	
	/**
	 * 删除Manager对象
	 */ 
	public void deleteManager(Manager manager, User user) throws DataAccessException;
	
	/**
	 * 删除Position对象
	 */ 
	public void deletePosition(Position position, User user) throws DataAccessException;
	
	/**
	 * 建立Manager和职位之间一对多/一对一的关系	 */ 
	public void addManagerPositionRL(int managerId, int[] positionId, User user) throws DataAccessException;
	
	/**
	 * 删除Manager和职位之间关系	 */ 
	public void deleteManagerPositionRL(int managerId, int positionId, User user) throws DataAccessException;
	
	/**
	 * 获得Manager和职位之间一对多/一对一的关系
	 */ 
	public List getManagerPositionRL(int managerId) throws DataAccessException;
	
	/**
	 * 获得Manager可选择的职位
	 */ 
	public List getManagerPosition(int managerId) throws DataAccessException;
}
