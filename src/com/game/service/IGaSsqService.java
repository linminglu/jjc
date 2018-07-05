package com.game.service;


import com.framework.dao.hibernate.PaginationSupport;

import java.util.List;

import org.json.JSONObject;

import com.framework.service.IService;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;
import com.game.model.GaSsqSession;

public interface IGaSsqService extends IService {
	
	/**
	 * 初始化场次
	 * 00:00 生成当天的所有场次
	 */
	public String updateInitSession();
	
	/**
	 * 抓取开奖数据
	 */
	public void updateFetchAndOpenResult();
	
	/**
	 * 获取当前期信息
	 * @return
	 */
	public GaSsqSession getCurrentSession();
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
	 *获取所有已经开奖的信息
	 */
	public PaginationSupport findAllOpenSessionList(int pageNum,int pageSize);
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
	 * 查询用户投注详细记录
	 */
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 通过期号获取投注期
	 * @return
	 */
	public GaSsqSession getGaSsqSessionBySessionNo(String sessionNo);
	
	/**
	 * 通过订单号查询订单
	 */
	public GaOrder getGaOrderByOrderNum(String orderNum);
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
	 * 保存用户投注记录
	 */
	public void saveUserBet(JSONObject map,GaSsqSession session,Integer uid,String kitems,Integer countSession,Integer times
			,Integer countBet,Integer money,String payType);

}
