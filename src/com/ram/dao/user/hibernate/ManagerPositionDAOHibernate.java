package com.ram.dao.user.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.ram.RamConstants;
import com.ram.dao.user.IManagerPositionDAO;
import com.ram.model.Manager;
import com.ram.model.ManagerPositionRl;
import com.ram.model.Position;
import com.ram.model.User;

/**
 * @author lixiaodong 
 */
public class ManagerPositionDAOHibernate extends AbstractBaseDAOHibernate
		implements IManagerPositionDAO {
	private final String GET_ALL_MANAGER = "from Manager as manager where manager.user.status != ? order by manager.user.status";
	private final String GET_ALL_POSITION = "from Position as position";
	private final String GET_POSITION_BY_ID = "from Position as position where position.positionId = ?";
	private final String GET_ALL_MANAGER_COUNT = "select count(*) from Manager as manager where manager.user.status != ?";
	private final String GET_ALL_POSITION_COUNT = "select count(*) from Position";
	private final String GET_MANAGER_POSITION = "from Position as position where position.positionId in (select managerPositionRl.position.positionId from ManagerPositionRl as managerPositionRl where managerPositionRl.manager.managerId = ?)";
	private final String GET_MANAGER_ADD_POSITION = "from Position as position where position.positionId not in (select managerPositionRl.position.positionId from ManagerPositionRl as managerPositionRl where managerPositionRl.manager.managerId = ?)";
	private final String GET_MANAGER_BY_ID = "from Manager manager where manager.managerId = ?";
	private final String DEL_MANAGER_POSITION_RL = "delete from ManagerPositionRl where managerPositionId = (select manposRl.managerPositionId from ManagerPositionRl as manposRl where manposRl.manager.managerId = ? and manposRl.position.positionId = ?)";
	
	/**
	 * 获得Manager对象
	 */ 
	public List getAllManagers(int startIndex, int perPageCount) throws DataAccessException{
		Query query = getSession().createQuery(GET_ALL_MANAGER);
		query.setString(0, RamConstants.DELETE_STATUS);
		query.setFirstResult(startIndex);
		query.setMaxResults(perPageCount);
		List managerList = query.list();
		return managerList;
	}
	
	/**
	 * 根据查询条件获得所有Manager对象
	 */ 
	public PaginationSupport findAllManagers(int startIndex,int pageSize, final DetachedCriteria detachedCriteria){
		return findPageByCriteria(detachedCriteria, pageSize, startIndex); 

	}
	
	/**
	 * 获得Manager对象的数量
	 */ 
	public int getManagersCount() throws DataAccessException{
		Query query = getSession().createQuery(GET_ALL_MANAGER_COUNT);
		query.setString(0, RamConstants.DELETE_STATUS);
		List list = query.list();
		int totalCount = ((Integer)list.get(0)).intValue();
		return totalCount;
	}
	
	/**
	 * 获得Position对象
	 */ 
	public List getAllPositions() throws DataAccessException{
		Query query = getSession().createQuery(GET_ALL_POSITION);
		List PositionList = query.list();
		return PositionList;
	}
	
	/**
	 * 获得Position对象的数量
	 */ 
	public int getPositionsCount() throws DataAccessException{
		Query query = getSession().createQuery(GET_ALL_POSITION_COUNT);
		List list = query.list();
		int totalCount = ((Integer)list.get(0)).intValue();
		return totalCount;
	}
	
	/**
	 * 获得Manager对象
	 */ 
	private Manager getManager(int mangerId) throws DataAccessException{
		Query query = getSession().createQuery(GET_MANAGER_BY_ID);
		query.setInteger(0, mangerId);
		List list = query.list();
		Iterator it = list.iterator();
		return (Manager)it.next();

	}
	
	/**
	 * 保存Manager对象
	 */ 
	public Manager saveAndReturnManager(Manager manager, User user){
		manager=(Manager)this.saveObjectAndReturn(manager,user);
		return manager;
	}
	
	public void saveManager(Manager manager, User user){
		this.saveObject(manager,user);
	}
	
	/**
	 * 保存Position职位对象
	 */ 
	public void savePosition(Position position, User user) throws DataAccessException{
		saveObject(position, user);
	}
	

	/**
	 * 根据ID获得Position职位对象
	 */
	public Object getPosition(Integer id) throws DataAccessException{
		Query query = getSession().createQuery(GET_POSITION_BY_ID);
		query.setInteger(0, id.intValue());
		List list = query.list();
		Iterator it = list.iterator();
		return it.next();
	}
	
	/**
	 * 删除Manager对象
	 */ 
	public void deleteManager(Manager manager, User user) throws DataAccessException{
		removeObject(manager, user);
	}
	
	/**
	 * 删除Position对象
	 */ 
	public void deletePosition(Position position, User user) throws DataAccessException{
		deleteObject(position, user);
	}
	
	/**
	 * 建立Manager和职位之间一对多/一对一的关系
	 * @param positionCollection Collection 职位列表
	 */
	public void addManagerPositionRL(int managerId, int[] positionId, User user) throws DataAccessException{
		Manager manager = getManager(managerId);
		for(int i = 0;i < positionId.length;i++){
			ManagerPositionRl ManagerPositionRl = new ManagerPositionRl();
			ManagerPositionRl.setManager(manager);
			Position Position = (Position)getPosition(new Integer(positionId[i]));
			ManagerPositionRl.setPosition(Position);
			getHibernateTemplate().save(ManagerPositionRl);
		}
	}
	
	/**
	 * 删除Manager和职位之间一对多/一对一的关系
	 */ 
	public void deleteManagerPositionRL(int managerId, int positionId, User user) throws DataAccessException{
		Query query = getSession().createQuery(DEL_MANAGER_POSITION_RL);
		query.setInteger(0, managerId);
		query.setInteger(1, positionId);
		query.executeUpdate();
	}
	
	/**
	 * 获得Manager和职位之间一对多/一对一的关系
	 */ 
	public List getManagerPositionRL(int managerId) throws DataAccessException{
		Query query = getSession().createQuery(GET_MANAGER_POSITION);
		query.setInteger(0, managerId);
		List list = query.list();
		return list;
	}
	
	/**
	 * 获得Manager可选择的职位
	 */ 
	public List getManagerPosition(int managerId) throws DataAccessException{
		Query query = getSession().createQuery(GET_MANAGER_ADD_POSITION);
		query.setInteger(0, managerId);
		List list = query.list();
		return list;
	}
}
