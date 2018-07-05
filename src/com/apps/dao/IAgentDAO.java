package com.apps.dao;

import java.math.BigDecimal;
import java.util.List;

import com.apps.model.Address;
import com.apps.model.dto.AgentDTO;
import com.apps.model.dto.BaseDataDTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

/**
 * 代理商dao
 * 
 * 
 */
public interface IAgentDAO extends IDAO {
	/**
	 * 统计每种彩票的投注中奖金额
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findGfGameBetAndWin(String hqls, List<Object> para);
	/**
	 * 统计每种彩票的投注中奖金额
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findGameBetAndWin(String hqls, List<Object> para);

	/**
	 * 统计每种彩票的投注中奖金额 从usertradeDetail表
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findGameBetAndWinFromUserTradeDetail(String hqls, List<Object> para);

	
	
	/**
	 * 统计用户的下级交易记录
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findUserTradeStatistics(String hqls, List<Object> para);

	/**
	 * 查询投注明细列表
	 * @param hql
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 查询代理的 测试用户、正式用户、下级代理的数量
	 * @param userId
	 * @return
	 */
	public List<AgentDTO> findUserTypeCountList(Integer userId);

	/**
	 * 统计直系代理数量
	 * @param userId
	 * @return
	 */
	public Integer CountAgentNum(Integer userId);
	/**
	 * 统计直系会员数量
	 * @param userId
	 * @return
	 */
	public Integer CountMemberNum(Integer userId);
	/**
	 * 统计其下会员和代理下的会员的总余额（管理员就是所有余额）
	 * @param userId
	 * @return
	 */
	public BigDecimal CountUserMoney(Integer userId);
	/**
	 * 统计二级代理下用户余额
	 * @param userId
	 * @return
	 */
	public BigDecimal CountAgentTwoUserMoney(Integer userId);

	/**
	 * 统计用户充值金额
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findUserTotalRecharge(String hqls, List<Object> para);

	/**
	 * 统计用户扣款金额
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findUserTotalCharge(String hqls, List<Object> para);

	/**
	 * 统计用户提现金额
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<AgentDTO> findUserTotalApplyCash(String hqls, List<Object> para);

}
