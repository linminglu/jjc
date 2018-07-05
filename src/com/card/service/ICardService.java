package com.card.service;

import java.util.List;
import java.util.Map;

import com.card.model.Card;
import com.card.model.CardGeneLog;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.card.model.dto.CardDTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.User;

public interface ICardService extends IService {
	/**
	 * 查找学习卡列表
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @param queryCard
	 * @param otherPara
	 * @return
	 */
	public PaginationSupport findCards(int startIndex, int pageSize,
			Card queryCard, Map<String, Object> otherPara);
	
	public PaginationSupport findCards(String hqls,int startIndex, int pageSize,
			Card queryCard, Map<String, Object> otherPara);
	/**
	 * 查找学习卡生成记录
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @param queryCard
	 * @param otherPara
	 * @return
	 */
	public PaginationSupport findCardGeneLogs(int startIndex, int pageSize,CardGeneLog cardGeneLog, Map<String, Object> otherPara);
	/**
	 * 获取学习卡
	 * @param cardId
	 * @return
	 */
	 public Card getCard(Integer cardId);
	 /**
	 * 保存学习卡
	 * @param card
	 * @return
	 */
	 public Card saveCard(Card card,User user);
	 /**
	  * 注销学习卡
	  * @param cardId
	  * @param user
	  */
	 public void removeCard(Integer cardId,User user) throws Exception;
	 /**
		 * 注销学习卡
		 * @param cardIds
		 * @param user
		 */
		public void removeCard(Integer[] cardIds,User user) throws Exception;
		/**
		 * 获得学习卡
		 * @param cardCode
		 * @return
		 */
		public Card getCard(String cardCode);
		public Card getCardByPassword(String cardPwd);
		/**
		 * 获得学习卡
		 * @param cardCode
		 * @param cardPwd
		 * @return
		 */
		public Card getCard(String cardCode,String cardPwd);
		/**
		 * 批量生成学习卡
		 * 
		 * @param cardGeneLog
		 * @param user
		 * @throws Exception
		 */
		public void addGenerateCard(CardGeneLog cardGeneLog, User user) throws Exception;
		/**
		 * 查找学习卡明细
		 * 
		 * @param cardCode
		 * @return
		 */
		public CardDTO getCardDTO(String cardCode);
		/**
		 * 导出学习卡--导出xls
		 * @param otherPara
		 * @return
		 */
		public Map<String,Object> cardExport(Card queryCard,Map<String, Object> otherPara);
		public CardGeneLog findCardGeneLogByHql(String hql);
		
		/**
		 * 根据订单号查询订单
		 * @param orderNum
		 * @return
		 */
		public CardRechargeOrder getRechargeOrderByOrderNum(String orderNum);
		public CardItemOrder getGiftCardOrderByOrderNum(String orderNum);

		/**
		 * 保存用户充值
		 * @param order
		 * @param uid
		 */
		public void updateUserRecharge(CardRechargeOrder order);
		
		/**
		 * 激活礼品卡更新效状态
		 * @param cardPwd 卡密
		 * @return  0=有效 1=已激活 2=过期 -1=无效卡 
		 */
		public String updateGiftCardStatusOfActive(String cardPwd,Integer uid);

		public PaginationSupport findCardItemList(String string,
				List<Object> para, int startIndex, int pageSize);
		public CardItem getCardItemById(Integer itemId);
		public void saveCardItem(CardItem cardItem);
		public void delCardItemByItemId(Integer itemId);

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
		 * 礼品卡订单列表
		 */
		public PaginationSupport findCardItemOrderList(String string,
				List<Object> para, int startIndex, int pageSize);
		
		public CardDTO getCardItemOrder(Integer orderId);

		/**
		 * 保存线下充值
		 * @param order
		 */
		public void updateUserRechargeOffLine(CardRechargeOrder order);
		
		/**
		 * 审核线下订单
		 */
		public void updateUserOfflineRecharge(CardRechargeOrder order,
				String status);

}