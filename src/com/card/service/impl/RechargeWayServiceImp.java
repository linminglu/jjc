package com.card.service.impl;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.card.dao.ICardDAO;
import com.card.dao.IRechargeWayDAO;
import com.card.model.Card;
import com.card.model.CardConsume;
import com.card.model.dto.UserCardDTO;
import com.card.service.IRechargeWayService;
import com.card.service.IUserCardService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQuery;
import com.framework.util.ParaList;
import com.framework.util.Paras;
import com.framework.util.StringUtil;
import com.framework.util.ThreeDES;
import com.ram.RamConstants;
import com.ram.model.User;

public class RechargeWayServiceImp extends BaseService implements IRechargeWayService {

	private IRechargeWayDAO rechargeWayDAO;

	public void setRechargeWayDAO(IRechargeWayDAO rechargeWayDAO) {
		this.rechargeWayDAO = rechargeWayDAO;
		super.dao = rechargeWayDAO;
	}

	
}