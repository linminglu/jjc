package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.ram.dao.permission.IRoleFunctionRlDAO;
import com.ram.model.Function;
import com.ram.model.Role;
import com.ram.model.RoleFunctionRl;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * @author hulei
 * 
 */
public class RoleFunctionRlDAOHibernate extends AbstractBaseDAOHibernate
		implements IRoleFunctionRlDAO {
	
	public RoleFunctionRl getRoleFunctionRl(int roleId, int functionId) {
		log.info("roleId=" + roleId);
		log.info("functionId=" + functionId);
		String hql = "from RoleFunctionRl rf where rf.role.roleId=? and rf.function.functionId=?";
		Query q = this.getSession().createQuery(hql);
		q.setInteger(0, roleId);
		q.setInteger(1, functionId);
		List list=q.list();
		log.info("list.size=" + list.size());
		RoleFunctionRl rf=(RoleFunctionRl)q.list().get(0);
		return rf;
	}
	/**
	 * 列出某功能下的，拥有该功能的角色列表
	 * 
	 * @param functionId
	 * @return
	 */
	public List findRolesByFunction(int functionId) {
		String hql = "from Role role where role.roleId in (select rf.role.roleId from RoleFunctionRl rf where rf.function.functionId=?)";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, functionId);
		return query.list();
	}

	/**
	 * 列出某个功能下，还不拥有该功能的所有角色的列表
	 */
	public List findRolesForFunction(int functionId) {
		String hql = "from Role role where role.roleId not in (select rf.role.roleId from RoleFunctionRl rf where rf.function.functionId=?)";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, functionId);
		return query.list();
	}

	public void deleteRoleFunctionRl(int roleId, int functionId, User user) {
		RoleFunctionRl rf = this.getRoleFunctionRl(roleId, functionId);
		this.deleteObject(rf, user);
	}

	public void saveRoleFunctionRl(int roleId, int functionId, User user) {
		RoleFunctionRl rf = new RoleFunctionRl();
		rf.setRole((Role) getObject(Role.class, new Integer(roleId)));
		rf.setFunction((Function) getObject(Function.class, new Integer(
				functionId)));
		this.saveObject(rf, user);
	}



}
