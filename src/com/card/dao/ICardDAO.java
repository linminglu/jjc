package com.card.dao;

import java.util.Collection;
import java.util.List;

import com.card.model.Card;
import com.card.model.CardGeneLog;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeOrder;
import com.card.model.CardItem;
import com.card.model.CardRechargeItem;
import com.card.model.dto.CardDTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.HQuery;
import com.ram.model.User;

public interface ICardDAO extends IDAO{
	/**
	 * 获得学习卡
	 * @param cardCode
	 * @return
	 */
	public Card getCard(String cardCode);
	/**
	 * 获得学习卡
	 * @param cardCode
	 * @param cardPwd
	 * @return
	 */
	public Card getCard(String cardCode,String cardPwd);
	public Card getCardByPassword(String cardPwd);
	/**
	 * 查找学习卡生成记录
	 * @param protocolCode
	 * @param batchCode
	 * @return
	 */
	public CardGeneLog getCardGeneLog(String protocolCode,String batchCode);
	public CardGeneLog findCardGeneLogByHql(String hql);
	public PaginationSupport findQueryResult(HQuery _query, int pageSize, int startIndex);
	/**
	 * 保存或者更新
	 * @param entity
	 * @param user
	 * @return
	 */
	public void saveObjectBatch(Collection entity,User user);
	/**
	 * 充值套餐列表
	 */
	public PaginationSupport findCardRechargeItemList(String string,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 修改充值套餐
	 */
	public void updateCardRechargeItem(CardRechargeItem cardRechargeItem);
	/**
	 * 保存充值套餐
	 */
	public void saveCardRechargeItem(CardRechargeItem cardRechargeItem);
	/**
	 * 充值订单列表
	 */
	public PaginationSupport findRechargeOrderList(String string,
			List<Object> para, int startIndex, int pageSize);
	public PaginationSupport findOfflineRechargeOrderList(String string,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 充值卡订单列表
	 */
	public PaginationSupport findCardItemOrderList(String string,
			List<Object> para, int startIndex, int pageSize);

	
	
	/**
	 * 根据订单号查询订单
	 * @param orderNum
	 * @return
	 */
	public CardRechargeOrder getRechargeOrderByOrderNum(String orderNum);
	public CardItemOrder getGiftCardOrderByOrderNum(String orderNum);
	
	public PaginationSupport findCardItemList(String string, List<Object> para,
			int startIndex, int pageSize);
	public CardItem getCardItemById(Integer itemId);
	public CardDTO getCardItemOrder(Integer orderId);
}