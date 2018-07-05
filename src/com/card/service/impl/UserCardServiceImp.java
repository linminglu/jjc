package com.card.service.impl;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.card.dao.ICardDAO;
import com.card.model.Card;
import com.card.model.CardConsume;
import com.card.model.dto.UserCardDTO;
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

public class UserCardServiceImp extends BaseService implements IUserCardService {

	private ICardDAO cardDAO;
	public void setCardDAO(ICardDAO cardDAO) {
		this.cardDAO = cardDAO;
	}

	/**
	 * 获得学习卡
	 * @param cardCode
	 * @param cardPwd
	 * @return
	 */
	public Card getCard(String cardCode,String cardPwd){
		Card card = cardDAO.getCard(cardCode,cardPwd);
		return card;
	}
	/**
	 * 查找学习卡列表
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @param queryCard
	 * @param user
	 * @return
	 */
	public PaginationSupport findUserCards(int startIndex, int pageSize,Card queryCard, User user) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select card ");
		hql.append("from Card card ");
		hql.append("where 1=1 ");
		// 参数
		ParaList paraList = new ParaList();
		this.combinQueryParamsForCard(queryCard, user, hql, paraList);

		hql.append(" order by card.endDate desc ");
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		PaginationSupport ps = cardDAO.findQueryResult(hQuery, pageSize, startIndex);
		return ps;
	}
	private void combinQueryParamsForCard(Card queryCard,User user,StringBuffer hql, ParaList paraList) {
		// 协议号
		String protocolCode = queryCard.getProtocolCode();
		if (!StringUtil.noValue(protocolCode)) {
			hql.append(" and card.protocolCode = ? ");
			Paras pa = new Paras(Types.VARCHAR, protocolCode);
			paraList.addParas(pa);
		}
		// 批次号
		String batchCode = queryCard.getBatchCode();
		if (!StringUtil.noValue(batchCode)) {
			hql.append(" and card.batchCode like ? ");
			Paras pa = new Paras(Types.VARCHAR, batchCode + "%");
			paraList.addParas(pa);
		}
		// 学习卡类型
		String cardType = queryCard.getCardType();
		if (!StringUtil.noValue(cardType)) {
			hql.append(" and card.cardType = ? ");
			Paras pa = new Paras(Types.VARCHAR, cardType);
			paraList.addParas(pa);
		}
		// 学习卡状态
		String cardStatus = queryCard.getCardStatus();
		if (!StringUtil.noValue(cardStatus)) {
			hql.append(" and card.cardStatus = ? ");
			Paras pa = new Paras(Types.VARCHAR, cardStatus);
			paraList.addParas(pa);
		}
		// 学习卡余额
		Integer remainAmount = queryCard.getRemainAmount();
		if (remainAmount != null) {
			hql.append(" and card.remainAmount > ? ");
			Paras pa = new Paras(Types.INTEGER, remainAmount);
			paraList.addParas(pa);
		}
		// 用户
		Integer userId = user.getUserId();
		hql.append(" and card.userId = ? ");
		Paras pa = new Paras(Types.INTEGER, userId);
		paraList.addParas(pa);
	}
	/**
	 * 激活学习卡
	 * 
	 * @param card
	 * @param user
	 * @throws Exception
	 */
	public void addUserCard(Card card, User user) throws Exception{
		// 判断能否生成
		this.checkCanActivate(card, user);
		// 激活学习卡
		this.activateCard(card, user);
	}

	/**
	 * 激活学习卡
	 * @param card
	 * @param user
	 */
	private void activateCard(Card card, User user) {
		String cardCode = card.getCardCode();
		Card cardNeedActivate = cardDAO.getCard(cardCode);
		
		Date chargeDate = DateTimeUtil.getCurrentDate();
		if("2".equals(cardNeedActivate.getCardType())){// 礼品卡
			Date endDate  = DateTimeUtil.getDateAfter(chargeDate,cardNeedActivate.getValidDay(),"yyyy-MM-dd");
			cardNeedActivate.setEndDate(endDate);// 重置礼品卡的有效期
		}
		cardNeedActivate.setCardStatus("2");// 2为使用状态
		cardNeedActivate.setChargeDate(chargeDate);
		cardNeedActivate.setUserId(user.getUserId());
		cardNeedActivate.setRemainAmount(cardNeedActivate.getCardAmount());
		cardDAO.updateObject(cardNeedActivate, user);
		
		// 课程学习卡 还需要生成订单
		if("1".equals(cardNeedActivate.getCardType())){
			String productStr = cardNeedActivate.getProductStr();
			
//			OrderInfo orderSaved = this.generateOrder(user, cardCode);
//			String[] productArray = productStr.split(",");
//			Integer orderId = orderSaved.getOrderId();
//			Date nowDate = DateTimeUtil.getJavaUtilDateNow();
//			for (int i = 0; i < productArray.length; i++) {
//					Integer productId = Integer.valueOf(productArray[i]);
//					Product product = (Product)cardDAO.getObject(Product.class, productId);
//					String productName = product.getProductName();
//					// 保存订单商品（虚拟的）
//					OrderProduct orderProduct = new OrderProduct();
//					orderProduct.setOrderId(orderId);
//					orderProduct.setProductId(productId);
//					orderProduct.setProductName(productName);
//					// 暂不考虑礼包
//					orderProduct.setPayStatus("1");
//					orderProduct.setUserId(user.getUserId());
//					orderProduct.setPayTime(nowDate);
//					orderProduct.setLb("0");
//					orderProduct.setProductNumber(1);
//					orderProduct.setCreateTime(nowDate);
//					cardDAO.saveObject(orderProduct,user);
//			}
		}
		card.setCardType(cardNeedActivate.getCardType());
	}

	/**
	 * 生成订单
	 * @param user
	 * @param cardCode
	 * @return
	 */
//	private OrderInfo generateOrder(User user, String cardCode) {
//		OrderInfo orderInfo = new OrderInfo();
//		orderInfo.setPayStatus("5");
//		orderInfo.setPayType("8");
//		orderInfo.setOrderStatus("1"); //订单状态 0 先产生订单 1 已处理订单
//		
//		String orderSn = "C"+cardCode;
//		orderInfo.setOrderSn(orderSn);
//		orderInfo.setOrderType("2");
//		Integer userId = user.getUserId();
//		orderInfo.setUserId(userId);
//		orderInfo.setConsignee(user.getName());
//		orderInfo.setPayTime(DateTimeUtil.getJavaUtilDateNow());
//		OrderInfo orderSaved = (OrderInfo)cardDAO.saveObjectAndReturn(orderInfo,user);
//		return orderSaved;
//	}

	/**
	 * 校验学习卡是否能激活
	 * @param card
	 * @param user
	 * @throws BusinessException
	 */
	private void checkCanActivate(Card card, User user) throws BusinessException {
		// 学习卡的卡号、密码是否正确 后续考虑加密
		String cardCode = card.getCardCode();
		String cardPwd = card.getCardPwd();
		Card cardNeedActivate = cardDAO.getCard(cardCode);
		String message = "";
		if(cardNeedActivate == null){
			message = "卡号不存在";
			throw new BusinessException("激活失败："+message);
		}
		ThreeDES des = new ThreeDES();// 实例化一个对像
		des.getKey(RamConstants.getConfigInfo("card.pwd.key"));// 生成密匙
		String dbCardPwd = des.desString(cardNeedActivate.getCardPwd());
		if(!dbCardPwd.equals(cardPwd)){
			message = "密码错误";
			throw new BusinessException("激活失败："+message);
		}
		// 校验该学生只能充一张同批次的学习卡
		String protocolCode = cardNeedActivate.getProtocolCode();
		String batchCode = cardNeedActivate.getBatchCode();
		Integer userId = user.getUserId();
		Card cardInBatch = this.getCard(protocolCode, batchCode, userId, cardCode);
		if(cardInBatch != null){
			message = "同批次的学习卡只能充一张";
			throw new BusinessException("激活失败："+message);
		}
		// 校验卡状态是否为未用状态
		if(!"1".equals(cardNeedActivate.getCardStatus())){
			message = "该学习卡已经注销或者激活";
			throw new BusinessException("激活失败："+message);
		}
		// 该卡是否在有效期内
		Date startDate = DateTimeUtil.getDateFormart_YYYY_MM_DD(cardNeedActivate.getStartDate());
		Date endDate = DateTimeUtil.getDateFormart_YYYY_MM_DD(cardNeedActivate.getEndDate());
		Date nowDate = DateTimeUtil.getNowDate();
		if(nowDate.getTime() > endDate.getTime() || nowDate.getTime() < startDate.getTime()){
			message = "该学习卡已经过期";
			throw new BusinessException("激活失败："+message);
		}
		// 校验学习卡的商品是否存在 只校验课程学习卡
		String cardType = cardNeedActivate.getCardType();
		String productStr = cardNeedActivate.getProductStr();
		String message_product = this.checkProductExists(productStr,cardType);
		if(!StringUtil.noValue(message_product)){
			message = message_product;
		}
	}
	/**
	 * 校验学习卡的商品是否存在
	 * @param productStr
	 * @param cardType
	 * @return
	 */
	private String checkProductExists(String productStr,String cardType) {
		String message = "";
		if(!"1".equals(cardType)){
			return message;
		}
		if(StringUtil.noValue(productStr)){
			message = "该学习卡的商品列表为空";
		}else{
			String[] productArray = productStr.split(",");
			if(productArray == null || productArray.length == 0){
				message = "该学习卡的商品列表为空";
			}else{
				for (int i = 0; i < productArray.length; i++) {
					Integer productId = Integer.valueOf(productArray[i]);
//					Product product = (Product)cardDAO.getObject(Product.class, productId);
//					if(product == null || !"1".equals(product.getAlive())){
//						message = "该学习卡中商品"+productId+"不存在或者已经无效";
//						break;
//					}
				}
			}
		}
		return message;
	}

	/**
	 * 查找学习卡明细
	 * 
	 * @param protocolCode
	 * @param batchCode
	 * @param userId
	 * @param cardCode
	 * @return
	 */
	public Card getCard(String protocolCode,String batchCode,Integer userId,String cardCode) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select card ");
		hql.append("from Card card ");
		hql.append("where card.userId=? ");
		// 参数
		ParaList paraList = new ParaList();
		Paras pa = new Paras(Types.INTEGER, userId);
		paraList.addParas(pa);
		// 协议号
		hql.append(" and card.protocolCode = ? ");
		pa = new Paras(Types.VARCHAR, protocolCode);
		paraList.addParas(pa);
		// 批次号
		hql.append(" and card.batchCode = ? ");
		pa = new Paras(Types.VARCHAR, batchCode);
		paraList.addParas(pa);
		// 卡号
		hql.append(" and card.cardCode <> ? ");
		pa = new Paras(Types.VARCHAR, cardCode);
		paraList.addParas(pa);
		
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		List list = cardDAO.getQueryResult(hQuery);
		Card card = null;
		if(list != null && list.size() > 0){
			card = (Card)list.get(0);
		}
		return card;
	}
	/**
	 * 获取礼品学习卡的相关信息
	 * @param user
	 * @param itemList
	 * @return
	 */
//	public UserCardDTO getUserCardDTO(User user, List<CartItem> itemList){
//		Card queryCard = new Card();
//		queryCard.setCardType("2");
//		queryCard.setRemainAmount(0);
//		List<Card> list = this.findUserCards(queryCard, user);
//		Double cardRemainAmount = 0.0;// 学习卡有效余额
//		Double payCardAmount = 0.0;// 商品使用支付卡金额
//
//		Boolean needPayByCard = false;
//		for (int i = 0; i < itemList.size(); i++) {
//			CartItem cardItem = itemList.get(i);
//			Product product = cardItem.getProduct();
//			if("1".equals(product.getLearningCard())){
//				needPayByCard = true;
//				payCardAmount += product.getLearningCardAmount();
//			}
//		}
//		Boolean canPayByCard = false;// 能否使用学习卡
//		if(needPayByCard){
//			for (int i = 0; i < list.size(); i++) {
//				Card card = list.get(i);
//				Date chargeDate = DateTimeUtil.getDateFormart_YYYY_MM_DD(card.getChargeDate());
//				Integer validDay = card.getValidDay();
//				Date nowDate = DateTimeUtil.getNowDate();
//				long validDayNow = nowDate.getTime() - chargeDate.getTime();
//				if(validDayNow <= validDay * 24 * 60 * 60 * 1000){// 是否在有效期内
//					Integer remainAmount = card.getRemainAmount();
//					cardRemainAmount += remainAmount;
//					canPayByCard = true;
//				}
//			}
//		}
//		// payCardAmount
//		UserCardDTO userCardDTO = new UserCardDTO();
//		userCardDTO.setCanPayByCard(canPayByCard);
//		userCardDTO.setCardRemainAmount(cardRemainAmount);// 卡余额
//		userCardDTO.setPayCardAmount(payCardAmount);
//		
//		return userCardDTO;
//	}
	/**
	 * 查找学习卡列表
	 * 
	 * @param queryCard
	 * @param user
	 * @return
	 */
	public List<Card> findUserCards(Card queryCard, User user) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select card ");
		hql.append("from Card card ");
		hql.append("where card.cardStatus <> '0' ");
		// 参数
		ParaList paraList = new ParaList();
		// 学习卡类型
		String cardType = queryCard.getCardType();
		if (!StringUtil.noValue(cardType)) {
			hql.append(" and card.cardType = ? ");
			Paras pa = new Paras(Types.VARCHAR, cardType);
			paraList.addParas(pa);
		}
		// 学习卡余额
		Integer remainAmount = queryCard.getRemainAmount();
		if (remainAmount != null) {
			hql.append(" and card.remainAmount > ? ");
			Paras pa = new Paras(Types.INTEGER, remainAmount);
			paraList.addParas(pa);
		}
		// 用户
		Integer userId = user.getUserId();
		hql.append(" and card.userId = ? ");
		Paras pa = new Paras(Types.INTEGER, userId);
		paraList.addParas(pa);
		/*// 日期
		Date currentDate = DateTimeUtil.getDateFormart_YYYY_MM_DD(DateTimeUtil.getCurrentDate());
		hql.append(" and card.endDate <= ? ");
		pa = new Paras(Types.TIME, currentDate);
		paraList.addParas(pa);*/
		
		hql.append(" order by card.endDate desc ");
		hQuery.setQueryString(hql.toString());
		
		hQuery.setParalist(paraList);
		List<Card> list = cardDAO.getQueryResult(hQuery);
		return list;
	}
  /**
   * 根据订单更新学习卡消费记录
   * @param orderId
   * @param user
   * @param status
   */
  public void updateCardConsume(Integer orderId, User user,String status) {
  	List<CardConsume> cardConsumeList = this.findCardConsume(orderId,"0",user);
		for (CardConsume cardConsume : cardConsumeList) {
			cardConsume.setConsumeStatus(status);
			cardDAO.updateObject(cardConsume, user);
		}
	}
	/**
	 * @param list
	 * @param user
	 * @param orderId
	 */
//	public void saveCardConsume(List<Product> list,User user, Integer orderId) {
//		// 扣除礼品卡的金额
//		 CardConsume cardConsume = this.getCardConsume(orderId, user.getUserId());
//		 if(cardConsume == null){
//			 // 需要保存
//			 Card queryCard = new Card();
//			 queryCard.setCardType("2");
//			 queryCard.setRemainAmount(0);
//			 List<Card> cardList = this.findUserCards(queryCard, user);
//			 this.doSaveCardConsume(orderId,list,cardList,user);
//		 }
//	}
  /**
	 * @param orderId
	 * @param list
	 * @param cardList
	 * @param user
	 * @return
	 */
//	private boolean doSaveCardConsume(Integer orderId, List<Product> list,List<Card> cardList,User user) {
//		if(cardList == null && cardList.size() == 0){// 用户的学习卡列表
//			return false;
//		}
//		// 需要保存
//		List<CardConsume> cardConsumeList = new ArrayList<CardConsume>();
//		Integer userId = user.getUserId();
//		Date consumeDate = DateTimeUtil.getCurrentDate();
//		
//		int learningCardRemain = 0;// 商品的学习卡支付余额
//		for (int i = 0; i < cardList.size(); i++) {
//			Card card = cardList.get(i);// 尽量先消费有效期靠前的学习卡
//			Date chargeDate = DateTimeUtil.getDateFormart_YYYY_MM_DD(card.getChargeDate());
//			Integer validDay = card.getValidDay();
//			Date nowDate = DateTimeUtil.getNowDate();
//			long validDayNow = nowDate.getTime() - chargeDate.getTime();
//			if(validDayNow <= validDay * 24 * 60 * 60 * 1000){// 是否在有效期内
//				Integer cardRemain = card.getRemainAmount(); // 卡余额
//				
//				for (int j = 0; j < list.size();) {
//					Product productOld = (Product)list.get(j);
//					Product product = (Product)cardDAO.getObject(Product.class,productOld.getProductId());
//					if(!"1".equals(product.getLearningCard())){// 不需要学习卡支付
//						list.remove(j);
//						continue;
//					}
//					int learningCardAmount = product.getLearningCardAmount();
//					if(learningCardRemain == 0){// 首次初始化
//						learningCardRemain = learningCardAmount;
//					}
//					
//					Integer cardId = card.getCardId();
//					String cardType = card.getCardType();
//					Integer consumeAmount = 0;
//					if(cardRemain >= learningCardRemain){
//						consumeAmount = learningCardRemain;
//						cardRemain = cardRemain - consumeAmount;
//						learningCardRemain = 0;
//					}else{
//						consumeAmount = cardRemain;
//						learningCardRemain = learningCardRemain - cardRemain;
//						cardRemain = 0;
//					}
//					String consumeStatus = "0"; //支付，等待确认
//					Integer productId = product.getProductId();
//					String remark = "";
//					CardConsume cardConsume = new CardConsume(cardId, cardType, orderId,
//							userId, consumeAmount, consumeDate,
//							consumeStatus, productId, remark);
//					cardConsumeList.add(cardConsume);
//					
//					if(cardRemain > 0){// 卡里有余额
//						list.remove(j);
//					}else{
//						break;
//					}
//				}
//				card.setRemainAmount(cardRemain);// 更新学习卡余额
//				cardDAO.updateObject(card, user);
//			}
//		}
//		for (int i = 0; i < cardConsumeList.size(); i++) {
//			CardConsume cardConsume = (CardConsume)cardConsumeList.get(i);
//			cardDAO.saveObject(cardConsume, user);
//		}
//		return true;
//	}
	/**
	 * 查找该订单是否已经使用礼品卡
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	private CardConsume getCardConsume(Integer orderId,Integer userId) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select cs ");
		hql.append("from CardConsume cs ");
		hql.append("where cs.orderId=? and cs.userId=? ");
		// 参数
		ParaList paraList = new ParaList();
		Paras pa = new Paras(Types.INTEGER, orderId);
		paraList.addParas(pa);
		
		pa = new Paras(Types.INTEGER, userId);
		paraList.addParas(pa);
		
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		List list = cardDAO.getQueryResult(hQuery);
		CardConsume card = null;
		if(list != null && list.size() > 0){
			card = (CardConsume)list.get(0);
		}
		return card;
	}
	/**
	 * 查找学习卡消费记录
	 * @param orderId
	 * @param status
	 * @param user
	 * @return
	 */
	public List<CardConsume> findCardConsume(Integer orderId,String status,User user){
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select cardConsume ");
		hql.append("from CardConsume cardConsume ");
		hql.append("where 1=1 ");
		// 参数
		ParaList paraList = new ParaList();
		
		hql.append(" and cardConsume.orderId = ? ");
		Paras pa = new Paras(Types.INTEGER, orderId);
		paraList.addParas(pa);

		if(!StringUtil.noValue(status)){
			hql.append(" and cardConsume.consumeStatus = ? ");
			pa = new Paras(Types.VARCHAR, status);
			paraList.addParas(pa);
		}

		// 用户
		if(user != null && user.getUserId() != null){
			Integer userId = user.getUserId();
			hql.append(" and cardConsume.userId = ? ");
			pa = new Paras(Types.INTEGER, userId);
			paraList.addParas(pa);
		}
		
		hQuery.setQueryString(hql.toString());
		hQuery.setParalist(paraList);
		
		List<CardConsume> cardConsumeList = cardDAO.getQueryResult(hQuery);
		return cardConsumeList;
	}
	/**
	 * 卡反余额
	 * @param orderId
	 * @param user
	 */
	public void backCardAmount(Integer orderId,User user) {
		List<CardConsume> cardConsumeList = this.findCardConsume(orderId,"0",null);
		if(cardConsumeList == null || cardConsumeList.size() == 0){
			return;
		}
		for (CardConsume cardConsume : cardConsumeList) {
			// 反余额
			Integer cardId = cardConsume.getCardId();
			Integer consumeAmount = cardConsume.getConsumeAmount();
			Card card = (Card)cardDAO.getObject(Card.class,cardId);
			Integer remainAmount = card.getRemainAmount() + consumeAmount;
			card.setRemainAmount(remainAmount);
			cardDAO.updateObject(card,null);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.studycard.service.IUserCardService#deleteCardConsume(java.lang.Integer, com.ram.model.User)
	 */
	public void deleteCardConsume(Integer cardConsumeId,User user){
		cardDAO.deleteObject(CardConsume.class, cardConsumeId, user);
	}
	/**
	 * 查找学习卡消费记录
	 * @param user
	 * @param orderId
	 */
	public UserCardDTO findCardConsume4Order(Integer orderId,User user){
		List<CardConsume> cardConsumeList = this.findCardConsume(orderId,null,user);
		boolean userCard = false;
		Double payCardAmount = 0.0;
		for (CardConsume cardConsume : cardConsumeList) {
			userCard = true;
			payCardAmount += cardConsume.getConsumeAmount();
		}
		UserCardDTO userCardDTO = new UserCardDTO();
		userCardDTO.setCanPayByCard(userCard);
		userCardDTO.setPayCardAmount(payCardAmount);
		return userCardDTO;
	}
	
}