package com.apps.service.impl;

import java.util.List;

import com.apps.dao.IAddressDAO;
import com.apps.model.Address;
import com.apps.service.IAddressService;
import com.framework.service.impl.BaseService;
import com.framework.util.ParamUtils;

public class AddressServiceImpl extends BaseService implements IAddressService {
	private IAddressDAO addressDAO;

	public void setAddressDAO(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
		super.dao = addressDAO;
	}

	public List<Address> findListByUserId(int uid) {
		return addressDAO.findListByUserId(uid);
	}

	public Address getAddress(int aid) {
		return (Address) addressDAO.getObject(Address.class, aid);
	}

	public Address saveAddress(Address address) {
		Integer aid = address.getId();
		String isDef = address.getIsDef();
		if(isDef.equals("1")){
			Integer userId = address.getUserId();
			addressDAO.updateAddressDef(userId);
		}
		if (ParamUtils.chkInteger(aid)) {
			addressDAO.saveObject(address);
		}else{
			address=(Address) addressDAO.saveObjectDB(address);
		}
		return address;
	}

	@Override
	public Address getDefAddress(int uid) {
		return addressDAO.getDefAddress(uid);
	}

}
