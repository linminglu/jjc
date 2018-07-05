package com.card.service;

import java.util.List;

import com.card.model.Card;
import com.card.model.CardConsume;
import com.card.model.dto.UserCardDTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.User;

public interface IUserCardService extends IService {
	/**
	 * 查找学习卡列表
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @param queryCard
	 * @param user
	 * @return
	 */
	public PaginationSupport findUserCards(int startIndex, int pageSize,
			Card queryCard, User user);

	/**
	 * 激活学习卡
	 * 
	 * @param card
	 * @param user
	 * @throws Exception
	 */
	public void addUserCard(Card card, User user) throws Exception;

	/**
	 * 获取礼品学习卡的相关信息
	 * 
	 * @param user
	 * @param itemList
	 * @return
	 */
//	public UserCardDTO getUserCardDTO(User user, List<CartItem> itemList);
	/**
	 * 根据订单更新学习卡消费记录
	 * 
	 * @param orderId
	 * @param user
	 * @param status
	 */
	public void updateCardConsume(Integer orderId, User user, String status);
	/**
	 * 保存学习卡消费记录
	 * @param list
	 * @param user
	 * @param orderId
	 */
//	public void saveCardConsume(List<Product> list,User user, Integer orderId);
	/**
	 * 查找学习卡消费记录
	 * @param orderId
	 * @param status
	 * @param user
	 * @return
	 */
	public List<CardConsume> findCardConsume(Integer orderId,String status,User user);
	/**
	 * 卡反余额
	 * @param orderId
	 * @param user
	 */
	public void backCardAmount(Integer orderId,User user);
	
	void deleteCardConsume(Integer cardConsumeId,User user);
	/**
	 * 查找学习卡消费记录
	 * @param user
	 * @param orderId
	 */
	public UserCardDTO findCardConsume4Order(Integer orderId,User user);
	
}