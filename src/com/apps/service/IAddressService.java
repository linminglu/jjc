package com.apps.service;

import java.util.List;

import com.apps.model.Address;
import com.framework.service.IService;

/**
 * 地址service
 * 
 * @author Mr.zang
 * 
 */
public interface IAddressService extends IService {

	/***
	 * 获得用户的地址
	 * 
	 * @param uid
	 *            用户id
	 * @return
	 */
	public List<Address> findListByUserId(int uid);

	/**
	 * 获得地址信息
	 * 
	 * @param aid
	 * @return
	 */
	public Address getAddress(int aid);

	/**
	 * 保存地址
	 * @param address
	 * @return
	 */
	public Address saveAddress(Address address);

	/**
	 * 获得默认地址
	 * @param uid
	 * @return
	 */
	public Address getDefAddress(int uid);



}
