/*
 * Created on 2005-7-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.dao.permission.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.ram.dao.permission.IPermissionDAO;
import com.ram.model.Permission;
import com.ram.model.User;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;

/**
 * @author cuilidong
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PermissionDAOHibernate extends AbstractBaseDAOHibernate implements
IPermissionDAO {
	
	private final String hqlGetPermissionByValue = "from Permission p where p.permissionValue=? order by p.permissionId";
	public Permission getPermission(String permissionValue) {
		
		Query q = getSession().createQuery(hqlGetPermissionByValue);
		q.setString(0,permissionValue);
		List list = q.list();
		if (list.size() > 0) {
			return (Permission)list.get(0);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ram.el.dao.course.IElCourseDao#saveElCourse(com.ram.el.model.ElCourse)
	 */
	public void savePermission(Permission permission, User user) {
		this.saveObject(permission, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ram.el.dao.course.IElCourseDao#removeForum(com.ram.el.model.ElCourse)
	 */
	public void recoverPermission(Integer id, User user) {
		Permission perm = (Permission) this.getObject(Permission.class, id);
		this.updateObject(perm, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ram.el.dao.course.IElCourseDao#removeForum(java.lang.Long)
	 */
	public void removePermission(Integer id, User user) {
		this.removeObject(Permission.class, id, user);
	}

	public void deletePermission(Integer id, User user) {
		this.deleteObject(Permission.class, id, user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ram.el.dao.course.IElCourseDao#getElCourse(java.lang.Long)
	 */
	public Permission getPermission(Integer id) {
		Permission permission = (Permission) getObject(Permission.class, id);
		return permission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ram.el.dao.course.IElCourseDao#getElCourses()
	 */
	public List findPermissions() {
		return this.findObjects(Permission.class);
	}

	public void updatePermission(Integer permissionId, User user) {
		// TODO Auto-generated method stub
		this.updateObject(this.getObject(Permission.class, permissionId), user);

	}
	
	private final String hql_findPermissionsNotBelongToRole = "select p from Permission p where p.permissionId not in (select rp.permission.permissionId from RolePermissionRl rp where rp.role.roleId=?) order by p.permissionId";
	public List findPermissionsNotBelongToRole(int roleId) {
		
		Query q = getSession().createQuery(hql_findPermissionsNotBelongToRole);
		q.setInteger(0, roleId);
		return q.list();
	}

}
