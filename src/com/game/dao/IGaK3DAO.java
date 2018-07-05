package com.game.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;

public interface IGaK3DAO extends IDAO {
	
	/**
	 * 历史开奖结果
	 */
	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 会员投注查询
	 */
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 获取单个用户的查询情况
	 */
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 用户投注情况
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 用户投注详情
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 查询当前期
	 */
	public GaK3Session getCurrentSession();
	/**
	 * 通过期号获取投注期
	 * @return
	 */
	public GaK3Session getGaK3SessionBySessionNo(String sessionNo);
	/**
	 * 获得某一期开奖结果 
	 */
	public GaK3Session getGaK3SessionBySessionNo(String sessionNo,String openStatus);
	/**
	 * 通过订单号查询订单
	 */
	public GaOrder 	getGaOrderByOrderNum(String orderNum);
	/**
	 * 订单列表
	 */
	public PaginationSupport findOrderList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 用户订单详情
	 * @param orderId
	 */
	public List findOrderView(Integer orderId);

	/**
	 * 获得投注情况
	 * @param sessionId
	 */
	public List findBetList(Integer sessionId);

	/**
	 * 查询用户投注记录
	 *
	 */
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);

}
