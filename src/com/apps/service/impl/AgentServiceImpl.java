package com.apps.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.apps.dao.IAgentDAO;
import com.apps.model.dto.AgentDTO;
import com.apps.model.dto.BaseDataDTO;
import com.apps.service.IAgentService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;

public class AgentServiceImpl extends BaseService implements
		IAgentService {
	private IAgentDAO agentDAO;

	public IAgentDAO getAgentDAO() {
		return agentDAO;
	}

	public void setAgentDAO(IAgentDAO agentDAO) {
		this.agentDAO = agentDAO;
		super.dao =agentDAO;
	}
	
	@Override
	public List<AgentDTO> findGameBetAndWin(String hqls, List<Object> para) {
		return agentDAO.findGameBetAndWin(hqls,para);
	}
	@Override
	public List<AgentDTO> findGameBetAndWinFromUserTradeDetail(String hqls, List<Object> para) {
		return agentDAO.findGameBetAndWinFromUserTradeDetail(hqls,para);
	}

	@Override
	public List<AgentDTO> findUserTradeStatistics(String hqls,
			List<Object> para) {
		return agentDAO.findUserTradeStatistics(hqls,para);
	}

	@Override
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,
			int startIndex, int pageSize) {
		return agentDAO.findGaBetDetailList(hql,para,startIndex,pageSize);
	}

	@Override
	public List<AgentDTO> findUserTypeCountList(Integer userId) {
		return agentDAO.findUserTypeCountList(userId);
	}


	public Integer CountAgentNum(Integer userId) {
		return agentDAO.CountAgentNum(userId);
	}


	public Integer CountMemberNum(Integer userId) {
		return agentDAO.CountMemberNum(userId);
	}


	public BigDecimal CountUserMoney(Integer userId) {
		return agentDAO.CountUserMoney(userId);
	}
	public BigDecimal CountAgentTwoUserMoney(Integer userId){
		return agentDAO.CountAgentTwoUserMoney(userId);
	}

	@Override
	public List<AgentDTO> findUserTotalRecharge(String hqls, List<Object> para) {
		return agentDAO.findUserTotalRecharge(hqls,para);
	}

	@Override
	public List<AgentDTO> findUserTotalCharge(String hqls, List<Object> para) {
		return agentDAO.findUserTotalCharge(hqls,para);	}

	@Override
	public List<AgentDTO> findUserTotalApplyCash(String hqls, List<Object> para) {
		return agentDAO.findUserTotalApplyCash(hqls,para);
	}
}
