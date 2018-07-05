package com.ram.service.user.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.ram.dao.user.IManagerPositionDAO;
import com.ram.model.Manager;
import com.ram.model.Position;
import com.ram.model.User;
import com.ram.service.user.IManagerPositionService;

/**
 * @author lixiaodong 
 */
public class ManagerPositionServiceImpl extends BaseService implements
	IManagerPositionService {

	private IManagerPositionDAO managerPositionDAO = null;

	public void setManagerPositionDAO(IManagerPositionDAO managerPositionDAO) {
		this.managerPositionDAO = managerPositionDAO;
	}
	
	/**
	 * 根据职位ID删除相应的职位
	 */ 
	public boolean deletePosition(Integer id, User user){
		try{
			Position postion = (Position)managerPositionDAO.getPosition(id);
			managerPositionDAO.deletePosition(postion, user);
			return true;
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
	}
	
	/**
	 * 获得Manager可选的职位列表
	 */ 
	public List getManagerPosition(int managerId){
		return managerPositionDAO.getManagerPosition(managerId);
	}
	
	/**
	 * 获得Manager和职位之间一对多/一对一的关系
	 */ 
	public List getManagerPositionRL(int managerId){
		List managerPositionList = managerPositionDAO.getManagerPositionRL(managerId);
		Iterator item = managerPositionList.iterator();
		List result = new ArrayList();
		while(item.hasNext()){
			Position info = (Position)item.next();
			result.add(info);
		}
		return result;
	}
	
	/**
	 * 添加Manager和职位的关系
	 */ 
	public boolean addManagerPositionRL(int managerId, int[] positionId, User user){
		try{
			managerPositionDAO.addManagerPositionRL(managerId, positionId, user);	
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 删除Manager和职位的关系
	 */ 
	public boolean deleteManagerPositionRL(int managerId, int positionId, User user){
		try{
			managerPositionDAO.deleteManagerPositionRL(managerId, positionId, user);	
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 保存Manager对象
	 */ 
	public Manager saveAndReturnManager(Manager manager, User user){
		return managerPositionDAO.saveAndReturnManager(manager, user);	
	}
	
	public void saveManager(Manager manager, User user){
		managerPositionDAO.saveManager(manager, user);	
	}

	/**
	 * 保存Position对象
	 */ 
	public boolean savePosition(Position position, User user){
		try{
			managerPositionDAO.savePosition(position, user);	
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
		return true;
	}

	/**
	 * 获得Manager对象列表(分页)
	 */
	public PaginationSupport findALLManagerForPage(int startIndex,int pageSize){
		List managerList = managerPositionDAO.getAllManagers(startIndex,pageSize);
		int num = managerPositionDAO.getManagersCount();
		PaginationSupport ps = new PaginationSupport(managerList, num, pageSize, startIndex);
		return ps;
	}
	
	/**
	 * 根据查询条件获得Manager对象列表(分页)
	 */
	public PaginationSupport findALLManagerForPage(int startIndex,int pageSize, DetachedCriteria detachedCriteria){
		PaginationSupport ps = managerPositionDAO.findAllManagers(startIndex,pageSize, detachedCriteria);
		return ps;
	}
	
	/**
	 * 获得Position对象
	 */ 
	public Object getPosition(Integer id){
		try{
			return managerPositionDAO.getPosition(id);	
		}
		catch(DataAccessException ex){
			log.error(ex);
			return null;
		}
	}
	
	public List getAllPosition() throws DataAccessException{
		return managerPositionDAO.getAllPositions();
	}
	
	/**
	 * 删除Manager对象
	 */ 
	public boolean deleteManager(Manager manager, User user){
		try{
			managerPositionDAO.deleteManager(manager, user);	
			return true;
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
	}
	
	/**
	 * 删除Position对象
	 */ 
	public boolean deletePosition(Position position,User user){
		try{
			managerPositionDAO.deletePosition(position,user);	
			return true;
		}
		catch(DataAccessException ex){
			log.error(ex);
			return false;
		}
	}
}
