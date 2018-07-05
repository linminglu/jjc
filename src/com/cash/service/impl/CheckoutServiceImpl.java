package com.cash.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.apps.Constants;
import com.apps.model.Seller;
import com.apps.util.ProductUtil;
import com.cash.dao.ICheckoutDAO;
import com.cash.model.UserCheckout;
import com.cash.model.UserCheckoutOrderRl;
import com.cash.service.ICheckoutService;
import com.framework.math.Arith;
import com.framework.service.impl.BaseService;
import com.framework.util.HQUtils;
import com.ram.model.User;
import com.ram.service.user.IUserService;

public class CheckoutServiceImpl extends BaseService implements ICheckoutService {
	private ICheckoutDAO checkoutDAO;
	private IUserService userService;

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setCheckoutDAO(ICheckoutDAO checkoutDAO) {
		this.checkoutDAO = checkoutDAO;
		super.dao = checkoutDAO;
	} 
	
	/**
	 * 定时结算用户订单
	 * @param checkoutDate 结算截止时间
	 * @return 1=ok,0=fail
	 */
	@SuppressWarnings("unused")
	public String updateUserOrderCheckoutByTimerAuto(Date checkoutDate){
		String flag = "1";
		
		//结算已完成&&未结算的订单
//		HQUtils hq = null;
//		
//		log.info("____开始结算统计_________________________________");
//		
//		//-----------1.商家结算----------------------------------------
//		hq = new HQUtils("from Seller s where 1=1 ");
//		List<Object> sellers = checkoutDAO.findObjects(hq);//全部有效的商家
//		for(Object sobj:sellers){
//			Seller seller = (Seller)sobj;
//			hq = new HQUtils("select u from User u,SellerUserRl rl where u.userId=rl.uid and rl.sid=? and rl.type=? ");
//			hq.addPars(seller.getSid());
//			hq.addPars(Constants.EMPLOYEE_TYPE_ADMIN);
//			hq.setOrderby(" order by u.userId");
//			List<Object> sUsers = checkoutDAO.findObjects(hq,1);//商家绑定的管理员账号
//			if(sUsers!=null && sUsers.size()>0){
//				User sUser = (User)sUsers.get(0);
//				log.info("___[sid="+seller.getSid()+"]["+seller.getTitle()+"]");
//				
//				//结算资金定义
//				BigDecimal storeFinalMoney = new BigDecimal(0);//电商类结算金额
//				BigDecimal eatFinalMoney = new BigDecimal(0);//订餐类结算金额
//				BigDecimal buyFinalMoney = new BigDecimal(0);//团购类结算金额
//				
//				//1.1 结算电商订单表-----------------------------------------------------------{
//				List<StoreOrder> storeSaveList = new ArrayList<StoreOrder>();//保存订单集合
//				List<UserCheckoutOrderRl> storeRlSaveList = new ArrayList<UserCheckoutOrderRl>();//保存订单结算关联集合
//				
//				hq = new HQUtils("from StoreOrder so where so.sid=? and so.status=? and so.isCheckout=? and so.createDate<=? ");
//				hq.addPars(seller.getSid());
//				hq.addPars(StoreConstants.ORDER_SUCCESS);//已完成
//				hq.addPars(Constants.ORDER_CHECKOUT_NOT);//未结算
//				hq.addPars(checkoutDate);
//				
//				List<Object> soList = checkoutDAO.findObjects(hq);//结算订单
//				log.info("___[store.size="+soList.size()+"]");
//				for(Object soobj:soList){
//					StoreOrder storeOrder = (StoreOrder)soobj;
//					BigDecimal finalMoney = ProductUtil.checkBigDecimal(storeOrder.getFinalMoney());//结算最终金额
//					log.info("___[oid="+storeOrder.getOid()+"][finalMoney="+finalMoney+"]");
//					storeFinalMoney = storeFinalMoney.add(finalMoney);
//					storeSaveList.add(storeOrder);
//					
//					UserCheckoutOrderRl storeRl = new UserCheckoutOrderRl();
//					storeRl.setOid(storeOrder.getOid());
//					storeRlSaveList.add(storeRl);
//				}
//				log.info("___[storeFinalMoney="+storeFinalMoney.doubleValue()+"]");
//				if(storeFinalMoney.doubleValue()>0){//有结算资金则保存到结算表
//					
//					//保存结算记录
//					storeFinalMoney = new BigDecimal(Arith.round(storeFinalMoney.doubleValue(), 2));
//					UserCheckout storeCheckout = new UserCheckout();
//					storeCheckout.setSid(seller.getSid());
//					storeCheckout.setUserId(sUser.getUserId());
//					storeCheckout.setModuleType(Constants.MODULE_STORE);
//					storeCheckout.setStatus(Constants.ORDER_CHECKOUT_PAY_SUCCESS);//直接入商家绑定账号
//					storeCheckout.setTotalMoney(storeFinalMoney);
//					storeCheckout.setCreateTime(checkoutDate);
//					checkoutDAO.saveObject(storeCheckout);
//					
//					//保存结算订单关联表
//					Integer checkoutId = storeCheckout.getCheckoutId(); 
//					for(UserCheckoutOrderRl storeRl:storeRlSaveList){
//						storeRl.setCheckoutId(checkoutId);
//					}
//					checkoutDAO.updateObjectList(storeRlSaveList, null);
//					
//					//更新订单结算状态
//					for(StoreOrder storeOrd:storeSaveList){
//						storeOrd.setIsCheckout(Constants.ORDER_CHECKOUT_SUCCESS);//已结算
//					}
//					//更新订单结算状态
//					checkoutDAO.updateObjectList(storeSaveList, null);
//					
//					//写入资金明细表
//					userService.saveTradeDetail(sUser.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, storeFinalMoney, checkoutId);
//					
//				}else{
//					log.info("___[no store cash to checkout...][sid="+seller.getSid()+"]");
//				}
//				//------------------------------------------------------------------------------------}
//				
//				//1.2 结算订餐订单表-----------------------------------------------------------{
//				List<EatOrder> eatSaveList = new ArrayList<EatOrder>();//保存订单集合
//				List<UserCheckoutOrderRl> eatRlSaveList = new ArrayList<UserCheckoutOrderRl>();//保存订单结算关联集合
//				
//				hq = new HQUtils("from EatOrder so where so.sid=? and so.status=? and so.isCheckout=? and so.createDate<=? ");
//				hq.addPars(seller.getSid());
//				hq.addPars(EatConstants.ORDER_SUCCESS);//已完成
//				hq.addPars(Constants.ORDER_CHECKOUT_NOT);//未结算
//				hq.addPars(checkoutDate);
//				
//				List<Object> eoList = checkoutDAO.findObjects(hq);//结算订单
//				log.info("___[eat.size="+eoList.size()+"]");
//				for(Object soobj:eoList){
//					EatOrder eatOrder = (EatOrder)soobj;
//					BigDecimal finalMoney = ProductUtil.checkBigDecimal(eatOrder.getFinalMoney());//结算最终金额
//					eatFinalMoney = eatFinalMoney.add(finalMoney);
//					eatSaveList.add(eatOrder);
//					
//					UserCheckoutOrderRl eatRl = new UserCheckoutOrderRl();
//					eatRl.setOid(eatOrder.getOid());
//					eatRlSaveList.add(eatRl);
//				}
//				
//				log.info("___[eatFinalMoney="+eatFinalMoney.doubleValue()+"]");
//				if(eatFinalMoney.doubleValue()>0){//有结算资金则保存到结算表
//					//保存结算记录
//					eatFinalMoney = new BigDecimal(Arith.round(eatFinalMoney.doubleValue(), 2));
//					UserCheckout eatCheckout = new UserCheckout();
//					eatCheckout.setSid(seller.getSid());
//					eatCheckout.setUserId(sUser.getUserId());
//					eatCheckout.setModuleType(Constants.MODULE_EAT);
//					eatCheckout.setStatus(Constants.ORDER_CHECKOUT_PAY_SUCCESS);//直接入商家绑定账号
//					eatCheckout.setTotalMoney(eatFinalMoney);
//					eatCheckout.setCreateTime(checkoutDate);
//					checkoutDAO.saveObject(eatCheckout);
//					
//					//保存结算订单关联表
//					Integer checkoutId = eatCheckout.getCheckoutId(); 
//					for(UserCheckoutOrderRl eatRl:eatRlSaveList){
//						eatRl.setCheckoutId(checkoutId);
//					}
//					checkoutDAO.updateObjectList(eatRlSaveList, null);
//					
//					//更新订单结算状态
//					for(EatOrder eatOrd:eatSaveList){
//						eatOrd.setIsCheckout(Constants.ORDER_CHECKOUT_SUCCESS);//已结算
//					}
//					checkoutDAO.updateObjectList(eatSaveList, null);
//					
//					//写入资金明细表
//					userService.saveTradeDetail(sUser.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, eatFinalMoney, checkoutId);
//					
//				}else{
//					log.info("___[no eat cash to checkout...][sid="+seller.getSid()+"]");
//				}
//				//------------------------------------------------------------------------------------}
//				
//				
//				//1.3 结算团购订单表-----------------------------------------------------------{
//				List<BuyOrder> buySaveList = new ArrayList<BuyOrder>();//保存订单集合
//				List<UserCheckoutOrderRl> buyRlSaveList = new ArrayList<UserCheckoutOrderRl>();//保存订单结算关联集合
//				
//				hq = new HQUtils("from BuyOrder so where so.sid=? and so.status=? and so.isCheckout=? and so.createDate<=? ");
//				hq.addPars(seller.getSid());
//				hq.addPars(BuyConstants.ORDER_USE_SUCC);//已消费
//				hq.addPars(Constants.ORDER_CHECKOUT_NOT);//未结算
//				hq.addPars(checkoutDate);
//				
//				List<Object> boList = checkoutDAO.findObjects(hq);//结算订单
//				log.info("___[buy.size="+boList.size()+"]");
//				for(Object boobj:boList){
//					BuyOrder buyOrder = (BuyOrder)boobj;
//					BigDecimal finalMoney = ProductUtil.checkBigDecimal(buyOrder.getFinalMoney());//结算最终金额
//					buyFinalMoney = buyFinalMoney.add(finalMoney);
//					buySaveList.add(buyOrder);
//					
//					UserCheckoutOrderRl buyRl = new UserCheckoutOrderRl();
//					buyRl.setOid(buyOrder.getOid());
//					buyRlSaveList.add(buyRl);
//				}
//				
//				log.info("___[buyFinalMoney="+buyFinalMoney.doubleValue()+"]");
//				if(buyFinalMoney.doubleValue()>0){//有结算资金则保存到结算表
//					//保存结算记录
//					buyFinalMoney = new BigDecimal(Arith.round(buyFinalMoney.doubleValue(), 2));
//					UserCheckout buyCheckout = new UserCheckout();
//					buyCheckout.setSid(seller.getSid());
//					buyCheckout.setUserId(sUser.getUserId());
//					buyCheckout.setModuleType(Constants.MODULE_STORE);
//					buyCheckout.setStatus(Constants.ORDER_CHECKOUT_PAY_SUCCESS);//直接入商家绑定账号
//					buyCheckout.setTotalMoney(buyFinalMoney);
//					buyCheckout.setCreateTime(checkoutDate);
//					checkoutDAO.saveObject(buyCheckout);
//					
//					//保存结算订单关联表
//					Integer checkoutId = buyCheckout.getCheckoutId(); 
//					for(UserCheckoutOrderRl buyRl:buyRlSaveList){
//						buyRl.setCheckoutId(checkoutId);
//					}
//					checkoutDAO.updateObjectList(buyRlSaveList, null);
//					
//					//更新订单结算状态
//					for(BuyOrder buyOrd:buySaveList){
//						buyOrd.setIsCheckout(Constants.ORDER_CHECKOUT_SUCCESS);//已结算
//					}
//					checkoutDAO.updateObjectList(buySaveList, null);
//					
//					//写入资金明细表
//					userService.saveTradeDetail(sUser.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, buyFinalMoney, checkoutId);
//					
//				}else{
//					log.info("___[no buy cash to checkout...][sid="+seller.getSid()+"]");
//				}
//				//------------------------------------------------------------------------------------}
//			}else{
//				log.error("__[seller not bind user => checkout fail...][sid="+seller.getSid()+"]");
//			}
//		}
		
		
		//-----2.个人用户结算-------------------------
		//查询需要结算的个人商品发布用户 ：在线支付&&已完成&&未结算
//		hq = new HQUtils("select distinct mui.sellerId from MytUserIndent mui " +
//				"where mui.moneyType=? and mui.payStatus=? and mui.isCheckout=? ");
//		hq.addPars(Constants.TRADE_TYPE_MONEY_ONLINE);
//		hq.addPars(MytConstants.ORDER_SUCCESS);
//		hq.addPars(Constants.ORDER_CHECKOUT_NOT);
//		
//		List<Object> infoUserIds = checkoutDAO.findObjects(hq);
//		if(infoUserIds!=null && infoUserIds.size()>0){
//			String iUserIds = StringUtil.List2StrOfId(infoUserIds);//有订单的人个卖家用户id
//			hq = new HQUtils("from User u where u.userId in("+iUserIds+")");
//			List<Object> iUsers = checkoutDAO.findObjects(hq);
//			for(Object iobj:iUsers){
//				User iUser = (User)iobj;
//				
//				BigDecimal infoFinalMoney = new BigDecimal(0);//供求类结算金额
//				
//				List<MytUserIndent> infoSaveList = new ArrayList<MytUserIndent>();//保存订单集合
//				List<UserCheckoutOrderRl> infoRlSaveList = new ArrayList<UserCheckoutOrderRl>();//保存订单结算关联集合
//				
//				hq = new HQUtils("from MytUserIndent mui where mui.moneyType=? and mui.payStatus=? and mui.isCheckout=? and mui.createTime<=? ");
//				hq.addPars(Constants.TRADE_TYPE_MONEY_ONLINE);
//				hq.addPars(MytConstants.ORDER_SUCCESS);
//				hq.addPars(Constants.ORDER_CHECKOUT_NOT);
//				hq.addPars(checkoutDate);
//				
//				List<Object> muiList = checkoutDAO.findObjects(hq);//结算订单
//				log.info("___[info.size="+muiList.size()+"]");
//				for(Object muiobj:muiList){
//					MytUserIndent infoOrder = (MytUserIndent)muiobj;
//					BigDecimal finalMoney = ProductUtil.checkBigDecimal(infoOrder.getFinalMoney());//结算最终金额
//					infoFinalMoney = infoFinalMoney.add(finalMoney);
//					infoSaveList.add(infoOrder);
//					
//					UserCheckoutOrderRl infoRl = new UserCheckoutOrderRl();
//					infoRl.setOid(infoOrder.getUserIndentId());
//					infoRlSaveList.add(infoRl);
//				}
//				
//				log.info("___[infoFinalMoney="+infoFinalMoney.doubleValue()+"]");
//				if(infoFinalMoney.doubleValue()>0){//有结算资金则保存到结算表
//					//保存结算记录
//					infoFinalMoney = new BigDecimal(Arith.round(infoFinalMoney.doubleValue(), 2));
//					UserCheckout infoCheckout = new UserCheckout();
//					infoCheckout.setSid(iUser.getUserId());
//					infoCheckout.setUserId(iUser.getUserId());
//					infoCheckout.setModuleType(Constants.MODULE_PUBLISH_INFO);
//					infoCheckout.setStatus(Constants.ORDER_CHECKOUT_PAY_SUCCESS);//直接入商家绑定账号
//					infoCheckout.setTotalMoney(infoFinalMoney);
//					infoCheckout.setCreateTime(checkoutDate);
//					checkoutDAO.saveObject(infoCheckout);
//					
//					//保存结算订单关联表
//					Integer checkoutId = infoCheckout.getCheckoutId(); 
//					for(UserCheckoutOrderRl infoRl:infoRlSaveList){
//						infoRl.setCheckoutId(checkoutId);
//					}
//					checkoutDAO.updateObjectList(infoRlSaveList, null);
//					
//					//更新订单结算状态
//					for(MytUserIndent infoOrd:infoSaveList){
//						infoOrd.setIsCheckout(Constants.ORDER_CHECKOUT_SUCCESS);//已结算
//					}
//					checkoutDAO.updateObjectList(infoSaveList, null);
//					
//					//写入资金明细表
//					userService.saveTradeDetail(iUser.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_CHECKOUT, infoFinalMoney, checkoutId);
//					
//				}else{
//					log.info("___[no info cash to checkout...][sid="+iUser.getUserId()+"]");
//				}
//				//------------------------------------------------------------------------------------}
//			}
//		}else{
//			log.error("__[no info user need checkout => checkout fail...]");
//		}
		
		return flag;
	}
	

}
