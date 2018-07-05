package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.ram.dao.permission.IRolePermissionRlDAO;
import com.ram.model.Permission;
import com.ram.model.RolePermissionRl;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * @author hulei
 * 
 */
public class RolePermissionRlDAOHibernate extends AbstractBaseDAOHibernate
		implements IRolePermissionRlDAO {

	public List findPermissionBelongToRole(int roleId) {
		String hql = "from RolePermissionRl rp where rp.role.roleId=?";
		Query q = getSession().createQuery(hql);
		q.setInteger(0, roleId);
		return q.list();
	}
	public List findPermissionNotBelongToRole(int roleId) {
		
		String hql = "from Permission p where p.permissionId not in(select rp.permission.permissionId from RolePermissionRl rp where rp.role.roleId=?";
		//String hql="select p from permission p where p.permissionId not in (select permission_id from role_permission_rl  where role_id=?)";
		SQLQuery q = getSession().createSQLQuery(hql).addEntity("permission",Permission.class);
		q.setInteger(0, roleId);
		List l=q.list();
		return q.list();
	}
	public int getPermissionNumberOfRole(int roleId) {
		String hql = "select count(rp.permission.permissionId) from RolePermissionRl rp where rp.role.roleId=?";
		Query q = this.getSession().createQuery(hql);
		q.setInteger(0, roleId);
		Integer itg = (Integer) q.list().get(0);
		return itg.intValue();
	}

	public RolePermissionRl getRolePermissionRl(int roleId, int permissionId) {
		String hql = "from RolePermissionRl rp where rp.role.roleId=? and rp.permission.permissionId=?";
		Query q = this.getSession().createQuery(hql);
		q.setInteger(0, roleId);
		q.setInteger(1, permissionId);
		return (RolePermissionRl) q.list().get(0);
	}

}
