package com.card.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.apps.Constants;
import com.apps.dao.IParamDAO;
import com.apps.model.Param;
import com.apps.util.ProductUtil;
import com.card.CardConstants;
import com.card.dao.ICardDAO;
import com.card.model.Card;
import com.card.model.CardGeneLog;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.card.model.dto.CardDTO;
import com.card.service.ICardService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQuery;
import com.framework.util.ParaList;
import com.framework.util.ParamUtils;
import com.framework.util.Paras;
import com.framework.util.StringUtil;
import com.framework.util.ThreeDES;
import com.game.service.IGaService;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.ExcelUtil;

public class CardServiceImp extends BaseService implements ICardService {

	private ICardDAO cardDAO;
	private IParamDAO paramDAO;
	private IUserService userService;
	public void setCardDAO(ICardDAO cardDAO) {
		this.cardDAO = cardDAO;
		super.dao = cardDAO;
	}
	public void setParamDAO(IParamDAO paramDAO) {
		this.paramDAO = paramDAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	/**
	 * 获取学习卡
	 * 
	 * @param cardId
	 * @return
	 */
	public Card getCard(Integer cardId) {
		Card card = (Card) cardDAO.getObject(Card.class, cardId);
		return card;
	}
	/**
	 * 获取学习卡
	 * 
	 * @param cardCode
	 * @return
	 */
	public Card getCard(String cardCode) {
		Card card = cardDAO.getCard(cardCode);
		return card;
	}
	
	public Card getCardByPassword(String cardPwd){
		return cardDAO.getCardByPassword(cardPwd);
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
	 * 保存学习卡
	 * 
	 * @param card
	 * @return
	 */
	public Card saveCard(Card card, User user) {
		Card carded = (Card) cardDAO.saveObjectAndReturn(card, user);
		return carded;
	}

	/**
	 * 注销学习卡
	 * 
	 * @param regionId
	 * @param user
	 */
	public void removeCard(Integer cardId, User user) throws Exception {
		Card card = this.getCard(cardId);
		card.setCardStatus("0");
		cardDAO.updateObject(card, user);
	}

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
			Card queryCard, Map<String, Object> otherPara) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select card ");
		hql.append("from Card card,User user ");
		hql.append("where card.createUser=user.userId ");
		// 参数
		ParaList paraList = new ParaList();
		this.combinQueryParamsForCard(queryCard, otherPara, hql, paraList);

		hql.append(" order by card.cardId desc");
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		PaginationSupport ps = cardDAO.findQueryResult(hQuery, pageSize, startIndex);
		return ps;
	}
	
	public PaginationSupport findCards(String hqls,int startIndex, int pageSize,
			Card queryCard, Map<String, Object> otherPara) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select card ");
		hql.append("from Card card,User user ");
		hql.append("where card.createUser=user.userId ");
		hql.append(hqls);
		// 参数
		ParaList paraList = new ParaList();
		this.combinQueryParamsForCard(queryCard, otherPara, hql, paraList);

		hql.append(" order by card.cardId desc");
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		PaginationSupport ps = cardDAO.findQueryResult(hQuery, pageSize, startIndex);
		return ps;
	}
	private void combinQueryParamsForCard(Card queryCard, Map<String, Object> otherPara,
			StringBuffer hql, ParaList paraList) {
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
		// 学习卡号
		String cardCode = queryCard.getCardCode();
		if (!StringUtil.noValue(cardCode)) {
			hql.append(" and card.cardCode like ? ");
			Paras pa = new Paras(Types.VARCHAR, cardCode + "%");
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
		// 起始时间
		String startDate = (String)otherPara.get("startDate");
		if(!StringUtil.noValue(startDate)){
			hql.append(" and card.endDate >= ? ");
			Paras pa = new Paras(Types.VARCHAR,startDate + " 00:00:00");
			paraList.addParas(pa);
		}
		// 结束时间
		String endDate = (String)otherPara.get("endDate");
		if(!StringUtil.noValue(endDate)){
			hql.append(" and card.endDate <= ? ");
			Paras pa = new Paras(Types.VARCHAR,endDate + " 23:59:59");
			paraList.addParas(pa);
		}	
		if(otherPara.get("itemId")!=null&&ParamUtils.chkInteger((Integer)otherPara.get("itemId"))){
			Integer itemId=(Integer) otherPara.get("itemId");
			hql.append(" and card.itemId = ? ");
			Paras pa = new Paras(Types.INTEGER,itemId);
			paraList.addParas(pa);
		}
	}
	/**
	 * 查找学习卡生成记录
	 * 
	 * @param startIndex
	 * @param pageSize
	 * @param queryCard
	 * @param otherPara
	 * @return
	 */
	public PaginationSupport findCardGeneLogs(int startIndex, int pageSize,CardGeneLog cardGeneLog, Map<String, Object> otherPara){
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select cardGene ");
		hql.append("from CardGeneLog cardGene,User user ");
		hql.append("where cardGene.createUser=user.userId ");
		// 参数
		ParaList paraList = new ParaList();
		this.combinQueryParamsForCardGeneLog(cardGeneLog, otherPara, hql, paraList);

		hql.append(" order by cardGene.protocolCode,cardGene.batchCode ");
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		PaginationSupport ps = cardDAO
				.findQueryResult(hQuery, pageSize, startIndex);
		return ps;
	}

	private void combinQueryParamsForCardGeneLog(CardGeneLog cardGeneLog, Map<String, Object> otherPara,
			StringBuffer hql, ParaList paraList) {
		// 协议号
		String protocolCode = cardGeneLog.getProtocolCode();
		if (!StringUtil.noValue(protocolCode)) {
			hql.append(" and cardGene.protocolCode = ? ");
			Paras pa = new Paras(Types.VARCHAR, protocolCode);
			paraList.addParas(pa);
		}
		// 批次号
		String batchCode = cardGeneLog.getBatchCode();
		if (!StringUtil.noValue(batchCode)) {
			hql.append(" and cardGene.batchCode = ? ");
			Paras pa = new Paras(Types.VARCHAR, batchCode);
			paraList.addParas(pa);
		}
		// 卡类型
		String cardType = cardGeneLog.getCardType();
		if (!StringUtil.noValue(cardType)) {
			hql.append(" and cardGene.cardType = ? ");
			Paras pa = new Paras(Types.VARCHAR, cardType);
			paraList.addParas(pa);
		}
	}

	/**
	 * 批量生成学习卡
	 * 
	 * @param cardGeneLog
	 * @param user
	 * @throws Exception
	 */
	public void addGenerateCard(CardGeneLog cardGeneLog, User user) throws Exception {
		// 判断能否生成
		this.checkCanGege(cardGeneLog, user);
		// 根据给定条件生成学习卡
		this.doGenerateCard(cardGeneLog, user);
		// 保存批次课程关系
		this.saveBatchProduct(cardGeneLog, user);
		// 保存生成记录
		this.saveGenerateLog(cardGeneLog, user);
	}

	/**
	 * 校验是否能生成
	 * @param cardGeneLog
	 * @param user
	 * @throws Exception
	 */
	private void checkCanGege(CardGeneLog cardGeneLog, User user) throws Exception {
		Boolean cannotGene = false;
		String protocolCode = cardGeneLog.getProtocolCode();
		String batchCode = cardGeneLog.getBatchCode();
		String cardType = cardGeneLog.getCardType();
		Integer cardCnt = cardGeneLog.getCardCnt();
		String message = "";
		if(cardCnt <= 0){
			cannotGene = true;
			message = "学习卡数据量输入有误";
		}
		// 如果该批次已经生成，则不能再生成
		CardGeneLog cardGeneLogDb = cardDAO.getCardGeneLog(protocolCode, batchCode);
		if(cardGeneLogDb != null){
			cannotGene = true;
			message = "该批次已经生成学习卡，不能再生成；您可以选择其他的批次生成";
		}
		// 校验课程 暂不校验课程
/*		if("1".equals(cardType)){
			String productStr = cardGeneLog.getProductStr();
			if(StringUtil.noValue(productStr)){
				cannotGene = true;
				message = "产品列表为空";
			}else{
				String[] productArray = productStr.split(",");
				if(productArray == null || productArray.length == 0){
					cannotGene = true;
					message = "产品列表为空";
				}else{
					for (int i = 0; i < productArray.length; i++) {
						Integer productId = Integer.valueOf(productArray[i]);
						Product product = (Product)cardDAO.getObject(Product.class, productId);
						if(product == null || !"1".equals(product.getAlive())){
							cannotGene = true;
							message = "产品"+productId+"不存在或者已经无效";
							break;
						}
					}
				}
			}
		}*/
		if(cannotGene){
			throw new BusinessException("生成失败："+message);
		}
	}

	/**
	 * 生成学习卡号
	 * @param cardGeneLog
	 * @param user
	 */
	private void doGenerateCard(CardGeneLog cardGeneLog, User user) {
		String protocolCode = cardGeneLog.getProtocolCode();
		String batchCode = cardGeneLog.getBatchCode();
		String cardStatus = "1";// 默认有效
		Integer cardCnt = cardGeneLog.getCardCnt();
		String cardType = cardGeneLog.getCardType();
//		Integer cardAmount = cardGeneLog.getCardAmount();
		Integer itemId = cardGeneLog.getItemId();
		CardItem cardItem=cardDAO.getCardItemById(itemId);
		Integer cardAmount =null;
		if(cardItem!=null){
			cardAmount=cardItem.getParValue().intValue();
		}
		String year = DateTimeUtil.getDateTime("yyyy");
		Date startDate = cardGeneLog.getStartDate();
		Date endDate = cardGeneLog.getEndDate();
		String productStr = cardGeneLog.getProductStr();
		String productName = cardGeneLog.getProductName();
		Integer validDay = null;// 有效期
		Date createDate = DateTimeUtil.getCurrentDate();
		if("1".equals(cardType)){// 课程学习卡
		}else if("2".equals(cardType)){// 礼品学习卡
			validDay = Integer.valueOf(RamConstants.getConfigInfo("card.cardtype.validday"));
		}
		
		Integer createUser = user.getUserId();
		cardGeneLog.setCreateDate(createDate);
		cardGeneLog.setCreateUser(createUser);
//		ThreeDES des = new ThreeDES();// 实例化一个对像
//		des.getKey(RamConstants.getConfigInfo("card.pwd.key"));// 生成密匙
		List<Card> list = new ArrayList<Card>();
		Integer count = 1;
		for (int i = 0; i < cardCnt; i++) {
			Card card = new Card();
			card.setProtocolCode(protocolCode);
			card.setBatchCode(batchCode);
			
			String cnt = String.format("%04d", i+1);
			//String cardCode = "NDB" + year.substring(year.length()-2) + protocolCode + batchCode + cnt;// 学习卡号
			String cardCode = protocolCode + batchCode + cnt;// 学习卡号
			String cardPwd = StringUtil.getRandomNumber(12); // 动态生成八位密码
			
			// 密码加密
			//cardPwd = des.encString(cardPwd);
			
			card = new Card(null, protocolCode, batchCode,
					cardCode, cardPwd, cardType, cardStatus,
					cardAmount, startDate, endDate, validDay,productStr,productName,
					createUser, createDate,itemId);
			if("1".equals(cardType)){// 课程学习卡
			}else if("2".equals(cardType)){// 礼品学习卡
				card.setRemainAmount(cardAmount);
			}
			list.add(card);
			if(count++ == 100){
				cardDAO.saveObjectBatch(list, user);
				count = 1;
				list = new ArrayList<Card>();
				// 只是将Hibernate缓存中的数据提交到数据库，保持与数据库数据的同步，同时清除内部缓存的全部数据
				cardDAO.flush();  
                cardDAO.clear();
			}
		}
		if(list != null && list.size() > 0){
			cardDAO.saveObjectBatch(list, user);
		}
	}
	/**
	 * 保存批次商品串
	 * @param cardGeneLog
	 * @param user
	 */
	private void saveBatchProduct(CardGeneLog cardGeneLog, User user) {
		if(!"1".equals(cardGeneLog.getCardType())){
			cardGeneLog.setProductStr(null);
		}
	}
	/**
	 * 保存生成记录
	 * @param cardGeneLog
	 * @param user
	 */
	private void saveGenerateLog(CardGeneLog cardGeneLog, User user) {
		cardDAO.saveObject(cardGeneLog, user);
	}
	/**
	 * 查找学习卡明细
	 * 
	 * @param cardCode
	 * @return
	 */
	public CardDTO getCardDTO(String cardCode) {
		HQuery hQuery = new HQuery();
		StringBuffer hql = new StringBuffer();
		hql.append("select new com.card.model.dto.CardDTO(card,user) ");
		hql.append("from Card card,User user ");
		hql.append("where card.createUser=user.userId ");
		// 参数
		ParaList paraList = new ParaList();
		// 协议号
		hql.append(" and card.cardCode = ? ");
		Paras pa = new Paras(Types.VARCHAR, cardCode);
		paraList.addParas(pa);
		
		hQuery.setQueryString(hql.toString());

		hQuery.setParalist(paraList);
		List list = cardDAO.getQueryResult(hQuery);
		CardDTO cardDTO = new CardDTO();
		if(list != null && list.size() > 0){
			cardDTO = (CardDTO)list.get(0);
		}
		return cardDTO;
	}
	 /**
	 * 注销学习卡
	 * @param cardIds
	 * @param user
	 */
	public void removeCard(Integer[] cardIds,User user) throws Exception{
		if(cardIds == null || cardIds.length == 0){
			return;
		}
		for (int i = 0; i < cardIds.length; i++) {
			this.removeCard(cardIds[i], user);
		}
	}
	/**
	 * 导出学习卡--导出xls
	 * @param queryCustomer
	 * @param otherPara
	 * @return
	 */
	public Map<String,Object> cardExport(Card queryCard,Map<String, Object> otherPara){
		PaginationSupport ps = this.findCards(0, 5000, queryCard, otherPara);
		List<Card> cardList = ps.getItems();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		WritableWorkbook backbook = null;
		byte[] b = null;
		try {
			String modelPath = (String)otherPara.get("modelPath");
			modelPath = modelPath.replace("/", "\\");
			File file = new File(modelPath);
			if(!file.exists()){
				throw new BusinessException("模板文件不存在。");
			}
			InputStream inputStream = new FileInputStream(file);
			Workbook inbook = Workbook.getWorkbook(inputStream);
			inputStream.close();
			
	    backbook = Workbook.createWorkbook(bos,inbook);
	    WritableSheet backSheet = backbook.getSheet(0);
			
	    String cardType = "1";
	    if("1".equals(cardType)){
	    	writeExcel1(backSheet, cardList);
	    }else if("2".equals(cardType)){
	    	writeExcel2(backSheet, cardList);
	    }else{
	    	writeExcel(backSheet, cardList);
	    }
			
			backbook.write();
			backbook.close();
			b = bos.toByteArray();
		} catch (Exception e) {
			try {
				if(bos != null){
					bos.close();
				}
			} catch (IOException e1) {
				bos = null;
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("byteArray", b);
		return map;
	}
	/**
	 * 导出到Excel
	 * 
	 * @param ws
	 * @param list
	 * @throws Exception
	 */
	private static void writeExcel(WritableSheet ws, List<Card> list) throws Exception {
		try {
			String value[] = null;
			Integer rownum = 1;
			Integer[] columnWidth = new Integer[6];
			for (int i = 0; i < columnWidth.length; i++) {
				columnWidth[i] = 20;
			}
			ThreeDES des = new ThreeDES();// 实例化一个对像
			des.getKey(RamConstants.getConfigInfo("card.pwd.key"));// 生成密匙
			for (Card card : list) {
				String cardCode = card.getCardCode();
				String cardPwd = des.desString(card.getCardPwd());
				String startDate = DateTimeUtil.DateToString(card.getStartDate());
				String endDate = DateTimeUtil.DateToString(card.getEndDate());
				value = new String[]{cardCode,cardPwd,startDate,endDate};
				ExcelUtil.addRow(ws, value, rownum ++,columnWidth);
			}
		} catch (Exception err) {
			System.out.print(err);
			throw err;
		}
	}
	/**
	 * 导出到Excel（课程卡）
	 * 
	 * @param ws
	 * @param list
	 * @throws Exception
	 */
	private static void writeExcel1(WritableSheet ws, List<Card> list) throws Exception {
		try {
			String value[] = null;
			Integer rownum = 1;
			Integer[] columnWidth = new Integer[6];
			for (int i = 0; i < columnWidth.length; i++) {
				columnWidth[i] = 20;
			}
			
			for (Card card : list) {
				String cardCode = card.getCardCode();
				String cardPwd = card.getCardPwd();
				String cardAmount=card.getCardAmount().toString();
				String startDate = DateTimeUtil.DateToString(card.getStartDate());
				String endDate = DateTimeUtil.DateToString(card.getEndDate());
				String status = card.getCardStatus();
				if(status.equals("0")){
					status="注销";
			    }else if(status.equals("1")){
			    	status="未激活";
			    }else if(status.equals("2")){
			    	status="已激活";
			    }
				value = new String[]{cardCode,cardPwd,cardAmount,startDate,endDate,status};
				ExcelUtil.addRow(ws, value, rownum ++,columnWidth);
			}
		} catch (Exception err) {
			System.out.print(err);
			throw err;
		}
	}
	/**
	 * 导出到Excel（礼品卡）
	 * 
	 * @param ws
	 * @param list
	 * @throws Exception
	 */
	private static void writeExcel2(WritableSheet ws, List<Card> list) throws Exception {
		try {
			String value[] = null;
			Integer rownum = 1;
			Integer[] columnWidth = new Integer[5];
			for (int i = 0; i < columnWidth.length; i++) {
				columnWidth[i] = 20;
			}
			ThreeDES des = new ThreeDES();// 实例化一个对像
			des.getKey(RamConstants.getConfigInfo("card.pwd.key"));// 生成密匙
			for (Card card : list) {
				String cardCode = card.getCardCode();
				String cardPwd = des.desString(card.getCardPwd());
				String startDate = DateTimeUtil.DateToString(card.getStartDate());
				String endDate = DateTimeUtil.DateToString(card.getEndDate());
				value = new String[]{cardCode,cardPwd,startDate,endDate};
				ExcelUtil.addRow(ws, value, rownum ++,columnWidth);
			}
		} catch (Exception err) {
			System.out.print(err);
			throw err;
		}
	}

	public CardGeneLog findCardGeneLogByHql(String hql) {
		// TODO Auto-generated method stub
		return cardDAO.findCardGeneLogByHql(hql);
	}
	
	/**
	 * 根据订单号查询订单
	 * @param orderNum
	 * @return
	 */
	public CardRechargeOrder getRechargeOrderByOrderNum(String orderNum){
		return cardDAO.getRechargeOrderByOrderNum(orderNum);
	}
	public CardItemOrder getGiftCardOrderByOrderNum(String orderNum){
		return cardDAO.getGiftCardOrderByOrderNum(orderNum);
	}
	
	/**
	 * 保存用户充值
	 * @param order
	 * @param uid
	 */
	public void updateUserRecharge(CardRechargeOrder order){
//		cardDAO.saveObject(order,null);
//		CardRechargeItem card = (CardRechargeItem)cardDAO.getObject(CardRechargeItem.class,order.getItemId());//套餐
//		//更新账户资金
////		userService.savePointDetail(order.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_ONLINE, card.getParValue().intValue(), card.getItemId());
////		userService.saveTradeDetail(order.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_ONLINE, card.getParValue(), order.getOrderId());
//		String remark="在线充值，订单号:"+order.getOrderNum()+",充值金额："+card.getParValue().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//		userService.saveTradeDetail(null, order.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_ONLINE, card.getParValue(), order.getOrderId(), null, remark);
//		userService.updateUserMoney(order.getUserId());
////		User user=cardDAO.getObject(User.class, id)
//		userService.saveUserDayRecharge(order.getUserId(), card.getParValue(), order.getOrderId());
	}
	
	/**
	 * 激活礼品卡更新效状态
	 * @param cardPwd 卡密
	 * @return  0=有效 1=已激活 2=已过期 3=未启用 -1=无效卡 
	 */
	public String updateGiftCardStatusOfActive(String cardPwd,Integer uid){
		Card card = cardDAO.getCardByPassword(cardPwd);
		if(card!=null){
			Date now = DateTimeUtil.getJavaUtilDateNow();
			String status = card.getCardStatus();//状态
			Date startDate = card.getStartDate();//开始时间
			Date endDate = card.getEndDate();//结束时间
			if(status.equals(CardConstants.CARD_STATUS_NORMAL)){
				if(startDate!=null && startDate.after(now)){
					return "3";//未到开始使用时间
				}
				if(endDate!=null && endDate.before(now)){
					return "2";//已过期
				}
				if(startDate.before(now) && endDate.after(now)){
					card.setCardStatus(CardConstants.CARD_STATUS_ACTIVED);
					card.setChargeDate(now);
					card.setUserId(uid);
					cardDAO.saveObject(card);//更新卡状态
					String remark="";
					//更新账户资金
					userService.savePointDetail(uid, Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_CARD, new BigDecimal(card.getCardAmount()), card.getCardId(),remark);
					
					return "0";//有效
				}
			}else if(status.equals(CardConstants.CARD_STATUS_CANCEL)){
				return "-1";//无效卡号
			}else if(status.equals(CardConstants.CARD_STATUS_ACTIVED)){
				return "1";//已激活
			}
		}
		return "-1";//无效卡号
	}
	


	@Override
	public PaginationSupport findCardItemList(String string, List<Object> para,
			int startIndex, int pageSize) {
		return cardDAO.findCardItemList(string,para,startIndex,pageSize);
	}
	public CardItem getCardItemById(Integer itemId){
		return cardDAO.getCardItemById(itemId);
	}

	@Override
	public void saveCardItem(CardItem cardItem) {
		cardDAO.saveObject(cardItem);
	}
	public void delCardItemByItemId(Integer itemId){
		CardItem cardItem = cardDAO.getCardItemById(itemId);
		cardDAO.deleteObject(cardItem.getClass(), itemId, null);
	}

	
	public PaginationSupport findCardRechargeItemList(String string,
			List<Object> para, int startIndex, int pageSize){
		PaginationSupport ps = cardDAO.findCardRechargeItemList(string, para, startIndex, pageSize);
		return ps;
	}
	
	public void updateCardRechargeItem(CardRechargeItem cardRechargeItem){
		cardDAO.updateCardRechargeItem(cardRechargeItem);
	}
	
	public void saveCardRechargeItem(CardRechargeItem cardRechargeItem){
		cardDAO.saveCardRechargeItem(cardRechargeItem);
	}
	
	public PaginationSupport findRechargeOrderList(String string,
			List<Object> para, int startIndex, int pageSize){
		PaginationSupport ps = cardDAO.findRechargeOrderList(string, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findOfflineRechargeOrderList(String string,
			List<Object> para, int startIndex, int pageSize){
		PaginationSupport ps = cardDAO.findOfflineRechargeOrderList(string, para, startIndex, pageSize);
		return ps;
	}
	public PaginationSupport findCardItemOrderList(String string,
			List<Object> para, int startIndex, int pageSize){
		PaginationSupport ps = cardDAO.findCardItemOrderList(string, para, startIndex, pageSize);
		return ps;
	}
	
	public CardDTO getCardItemOrder(Integer orderId){
		CardDTO dto = cardDAO.getCardItemOrder(orderId);
		return dto;
	}
	@Override
	public void updateUserRechargeOffLine(CardRechargeOrder order) {
		cardDAO.saveObject(order,null);	
	}
	@Override
	public void updateUserOfflineRecharge(CardRechargeOrder order, String status) {
		order.setPayTime(new Date());
		order.setPayStatus(status);
		cardDAO.updateObject(order, null);
		BigDecimal totalMoney = order.getTotalMoney();
		Integer orderId = order.getUserId();
		User user=(User) cardDAO.getObject(User.class, order.getUserId());
		if(status.equals("2")){//订单通过
			String remark="线下充值订单通过充值，订单号："+order.getOrderNum()+"订单金额："+totalMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			userService.saveTradeDetail(null, orderId,
					Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_MANAGER_SET, order.getTotalMoney(),
					order.getOrderId(), null, remark.toString(),null,user.getUserType(),user.getLoginName());
			userService.updateUserMoney(order.getUserId());
			//跳级的奖励金额
			userService.updateUserAddUpRechargeMoney(user, totalMoney, orderId);
			//返水
			userService.saveUserDayRecharge(order.getUserId(), order.getTotalMoney(), order.getOrderId());
		}
	}
}