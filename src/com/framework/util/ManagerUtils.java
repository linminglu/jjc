package com.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.apps.Constants;

public class ManagerUtils {
	protected final static Log log = LogFactory.getLog(ManagerUtils.class);
	
	
	/**
	 * 检查用户权限（所有管理员）  管理员、超级管理员、客服、财务、隐藏用户
	 * @param userType 用户类型
	 * @return
	 */
	public static boolean checkRole(String userType){
		if (userType == null)
			return false;
		if (userType.trim().length() <= 0)
			return false;

		if(Constants.USER_TYPE_ADMIN.equals(userType)
				||Constants.USER_TYPE_SUPERADMIN.equals(userType)
				||Constants.USER_TYPE_CUS_SERVICE.equals(userType)
				||Constants.USER_TYPE_FINANCE.equals(userType)
				||Constants.USER_TYPE_HIDDEN.equals(userType))
			return true;
		return false;
	}
	
	/**
	 * 检查用户权限（等级到达财务权限）  隐藏用户、超级管理员、管理员、财务
	 * @param userType 用户类型
	 * @return
	 */
	public static boolean checkFinance(String userType){
		if (userType == null)
			return false;
		if (userType.trim().length() <= 0)
			return false;
		
		if(Constants.USER_TYPE_ADMIN.equals(userType)
				||Constants.USER_TYPE_SUPERADMIN.equals(userType)
				||Constants.USER_TYPE_FINANCE.equals(userType)
				||Constants.USER_TYPE_HIDDEN.equals(userType))
			return true;
		return false;
	}
	/**
	 * 检查用户权限（等级到达普通管理员）  隐藏用户、超级管理员、管理员
	 * @param userType 用户类型
	 * @return
	 */
	public static boolean checkAdmin(String userType){
		if (userType == null)
			return false;
		if (userType.trim().length() <= 0)
			return false;
		
		if(Constants.USER_TYPE_ADMIN.equals(userType)
				||Constants.USER_TYPE_SUPERADMIN.equals(userType)
				||Constants.USER_TYPE_HIDDEN.equals(userType))
			return true;
		return false;
	}
	
	public static boolean checkHidden(String userType){
		if (userType == null)
			return false;
		if (userType.trim().length() <= 0)
			return false;
		if(Constants.USER_TYPE_HIDDEN.equals(userType))
			return true;
		return false;
	}
	public static void main(String[] args) {

	}

}
