package com.apps.dao;

import java.util.List;

import com.apps.model.Address;
import com.framework.dao.IDAO;

/**
 * 地址dao
 * 
 * @author Mr.zang
 * 
 */
public interface IAddressDAO extends IDAO {

	/**
	 * 获得用户的收货地址
	 * 
	 * @param uid
	 * @return
	 */
	public List<Address> findListByUserId(int uid);

	/**
	 * 更新用户的地址为非默认地址
	 * @param userId
	 */
	public void updateAddressDef(Integer userId);

	/**
	 * 获得默认地址
	 * @param uid
	 * @return
	 */
	public Address getDefAddress(int uid);

}
