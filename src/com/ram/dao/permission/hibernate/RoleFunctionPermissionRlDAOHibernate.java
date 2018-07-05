package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.ram.dao.permission.IRoleFunctionPermissionRlDAO;
import com.ram.model.Permission;
import com.ram.model.RoleFunctionPermissionRl;
import com.ram.model.RoleFunctionRl;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * @author hulei
 * 
 */
public class RoleFunctionPermissionRlDAOHibernate extends
		AbstractBaseDAOHibernate implements IRoleFunctionPermissionRlDAO {

	public void deleteRoleFunctionPermissionRl(RoleFunctionRl rf,
			int permissionId, User user) {
		RoleFunctionPermissionRl rfp = (RoleFunctionPermissionRl) getRoleFunctionPermissionRl(
				rf.getRole().getRoleId().intValue(), rf.getFunction()
						.getFunctionId().intValue(), permissionId);
		this.deleteObject(rfp, user);
	}

	/**
	 * 列出某角色对某功能拥有的权限列表
	 * 
	 * @param functionId
	 * @return
	 */
	public List findPermissionsOfRoleAndFunction(int roleId, int functionId) {
		String hql = "from Permission p where p.permissionId in "
				+ "(select rfp.permission.permissionId from RoleFunctionPermissionRl rfp "
				+ "where rfp.roleFunctionRl.role.roleId=? and rfp.roleFunctionRl.function.functionId=?)"
				+ "  order by p.permissionId";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0, roleId);
		query.setInteger(1, functionId);
		return query.list();

	}

	/**
	 * 查找可以分配某角色的某功能的权限列表
	 * 
	 * @param functionId
	 * @return
	 */
	public List findPermissionsForRoleAndFunction(int roleId, int functionId) {
		String hql = "from Permission p where p.permissionId not in "
				+ "(select rfp.permission.permissionId from RoleFunctionPermissionRl rfp "
				+ "where rfp.roleFunctionRl.role.roleId=? and rfp.roleFunctionRl.function.functionId=?)"
				+ " order by p.permissionId";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0, roleId);
		query.setInteger(1, functionId);

		return query.list();

	}

	/**
	 * 根据roleid,functionid,permissionid查找RoleFunctionPermission对象
	 */
	public RoleFunctionPermissionRl getRoleFunctionPermissionRl(int roleId,
			int functionId, int permissionId) {
		String hql = " from RoleFunctionPermissionRl rfp "
				+ " where rfp.permission.permissionId=? "
				+ " and rfp.roleFunctionRl.role.roleId=? "
				+ " and rfp.roleFunctionRl.function.functionId=? "
				+ " order by rfp.permission.permissionId";
		Query query = this.getSession().createQuery(hql);
		query.setInteger(0, permissionId);
		query.setInteger(1, roleId);
		query.setInteger(2, functionId);
		List list = query.list();
		RoleFunctionPermissionRl rfp = null;
		if (list.size()>0) {
			rfp = (RoleFunctionPermissionRl) list.get(0);
		}
		return rfp;
	}

	public void saveRoleFunctionPermissionRl(RoleFunctionRl rf, Permission p,
			User user) {
		RoleFunctionPermissionRl rfp = new RoleFunctionPermissionRl();
		rfp.setRoleFunctionRl(rf);
		rfp.setPermission(p);
		this.saveObject(rfp, user);

	}

}
