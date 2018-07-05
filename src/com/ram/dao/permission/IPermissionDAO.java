/*
 * Created on 2005-7-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ram.dao.permission;


import java.util.List;

import com.framework.dao.IDAO;
import com.ram.model.Permission;
import com.ram.model.User;

/**
 * @author zhangkeyi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IPermissionDAO
    extends IDAO {

  /**
   * 保存分制类型
   * @param scoreType
   * @return
   */
  public void savePermission(Permission permission,User user);

  /**
   * 通过主键进行查找
   * @param id
   */
  public Permission getPermission(Integer permissionId);
  
  public Permission getPermission(String permissionValue);
  /**
   * 通过持久化对象找到全部列表

   * @return
   */
  public List findPermissions();

  /**
   * 用recover开头，表示设置status字段为0。并不真正删除
   * @param id
   */
  public void recoverPermission(Integer permissionId,User user);

  /**
   * 用remove开头，表示设置status字段为0。并不真正删除
   * @param id
   */
  public void removePermission(Integer permissionId,User user);
  /**
   * 真正的删除纪录
   * @param Permission
   */
  public void deletePermission(Integer permissionId,User user);
  
  public void updatePermission(Integer permissionId,User user);
  
  /**
   * 查找不属于某个角色的所有权限
   * @param roleId
   * @return
   */
  public List findPermissionsNotBelongToRole(int roleId);
}
