package com.apps.service.impl;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.apps.Constants;
import com.apps.dao.ISellerDAO;
import com.apps.model.Promotion;
import com.apps.model.PromotionRule;
import com.apps.model.Seller;
import com.apps.model.SellerTypeRl;
import com.apps.model.SellerUserRl;
import com.apps.model.Type;
import com.apps.service.ISellerService;
import com.apps.util.ProductUtil;
import com.apps.web.form.SellerForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.ram.model.User;
import com.ram.model.dto.UserDTO;

public class SellerServiceImpl extends BaseService implements ISellerService {
	private ISellerDAO sellerDAO;

	public void setSellerDAO(ISellerDAO sellerDAO) {
		this.sellerDAO = sellerDAO;
		super.dao = sellerDAO;
	}

	public PaginationSupport search(String hqls, List<Object> para,
			int pageIndex, int pageSize) {
		return sellerDAO.search(hqls, para, pageIndex, pageSize);
	}

	public PaginationSupport findSellerList(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return sellerDAO.findSellerList(hqls, para, statIndex, pageSize);
	}

	public Seller saveSeller(Seller seller, SellerForm from) {
		Integer tid = seller.getTid();
		Integer columnId = seller.getColumnId();
		Integer tid3 = seller.getTid3();
		Integer sid = seller.getSid();
		Integer id = columnId;
		if (ParamUtils.chkInteger(tid)) {
			id = tid;
		}
		if (ParamUtils.chkInteger(tid3)) {
			id = tid3;
		}

		Type type = (Type) sellerDAO.getObject(Type.class, id);
		String label = type.getTitle();

		if (ParamUtils.chkInteger(sid)) {// 修改
			Seller temp = (Seller) sellerDAO.getObject(Seller.class, sid);
			temp.setAddress(seller.getAddress());
			temp.setTitle(seller.getTitle());
			temp.setTelePhone(seller.getTelePhone());
			temp.setStartTime(seller.getStartTime());
			temp.setEndTime(seller.getEndTime());
			temp.setSendUpPrices(seller.getSendUpPrices());
			temp.setFreightTime(seller.getFreightTime());
			temp.setIsMinus(seller.getIsMinus());
			temp.setIsFreight(seller.getIsFreight());// 是否配送
			temp.setFreightPrices(seller.getFreightPrices());// 运费
			temp.setIsInvoice(seller.getIsInvoice());// 是否开发票
			temp.setRemarks(seller.getRemarks());
			temp.setLogo(seller.getLogo());
			temp.setLongAlt(seller.getLongAlt());
			temp.setLabel(label);
			temp.setAverage(seller.getAverage());
			temp.setColumnId(columnId);
			temp.setTid(tid);
			temp.setTid3(tid3);
			temp.setKeywords(seller.getKeywords());
			temp.setCid(seller.getCid());
			temp.setCcid(seller.getCcid());
			temp.setBusaid(seller.getBusaid());
			temp.setStartMoney(seller.getStartMoney());
			temp.setRatio(seller.getRatio());
			temp.setMaxMoney(seller.getMaxMoney());
			sellerDAO.saveObject(temp);
			seller = temp;
			// 保存类型
			// -1.删除所有的类型
			sellerDAO.delSellerType(sid);
		} else {// 新增
			seller.setStatus(Constants.PUB_STATUS_OPEN);
			seller.setCreateDate(new Date());
			seller.setSalesNum(0);
			seller.setEvalStar(0f);
			seller.setIsFreight(Constants.PUB_STATUS_CLOSE);// 是否配送
			seller.setIsHot(Constants.PUB_STATUS_CLOSE);
			seller.setSort("1");
			seller.setLabel(label);
			seller = (Seller) sellerDAO.saveObjectDB(seller);
			sid = seller.getSid();
		}
		SellerTypeRl r = new SellerTypeRl(sid, columnId);
		sellerDAO.saveObject(r);
		if (ParamUtils.chkInteger(tid)) {
			SellerTypeRl r1 = new SellerTypeRl(sid, tid);
			sellerDAO.saveObject(r1);
		}
		if (ParamUtils.chkInteger(tid3)) {
			SellerTypeRl r2 = new SellerTypeRl(sid, tid3);
			sellerDAO.saveObject(r2);
		}

		// -2.重新保存
		/*
		 * for (int i = 0; i < tids.length; i++) { SellerTypeRl r = new
		 * SellerTypeRl(sid, tids[i]); sellerDAO.saveObject(r); }
		 */
		return seller;
	}

	public List<Type> findSellerType(Integer sid) {
		return sellerDAO.findSellerType(sid);
	}

	public List<Seller> findAllList(String status) {
		return sellerDAO.findAllList(status);
	}

	public List<Seller> findAllListByColumnId(Integer columnId) {
		return sellerDAO.findAllListByColumnId(columnId);
	}

	public List<Seller> findAllListByTid(String status, Integer tid) {
		return sellerDAO.findAllListByTid(status, tid);
	}
	public List<Seller> findAllListByTids(String status,String tids){
		return sellerDAO.findAllListByTids(status, tids);
	}
	public SellerUserRl getSellerUser(Integer userId) {
		return sellerDAO.getSellerUser(userId);
	}

	public PaginationSupport findSellerCommEat(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return sellerDAO.findSellerCommEat(hqls, para, statIndex, pageSize);
	}

	public PaginationSupport findSellerCommStore(String hqls,
			List<Object> para, int statIndex, int pageSize) {
		return sellerDAO.findSellerCommStore(hqls, para, statIndex, pageSize);
	}

	public PaginationSupport findPromotionList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return sellerDAO.findPromotionList(hqls, para, startIndex, pageSize);
	}

	public List<PromotionRule> findPromotionRule(Integer id) {
		return sellerDAO.findPromotionRule(id);
	}

	public Promotion savePromotion(Promotion promotion, String[] fullPrices,
			String[] minusPrices) {
		Integer id = promotion.getId();
		StringBuffer title = new StringBuffer();
		if (ParamUtils.chkInteger(id)) {
			// 1.删除以前的
			sellerDAO.delPromotionRule(id);
			// 2.重新写入
			for (int i = 0; i < fullPrices.length; i++) {
				String fullPrice = fullPrices[i];
				if (ParamUtils.chkString(fullPrice)) {
					String minusPrice = minusPrices[i];
					if (ParamUtils.chkString(minusPrice)) {
						title.append("满"
								+ ProductUtil.BigFormat2(new BigDecimal(
										fullPrice))
								+ "减"
								+ ProductUtil.BigFormat2(new BigDecimal(
										minusPrice)) + "元，");
						PromotionRule r = new PromotionRule(id, new BigDecimal(
								fullPrice), new BigDecimal(minusPrice));
						sellerDAO.saveObject(r);
					}
				}
			}

			title.deleteCharAt(title.lastIndexOf("，"));
			Promotion temp = (Promotion) sellerDAO.getObject(Promotion.class,
					id);
			temp.setTitle(title.toString());
//			temp.setEndDate(promotion.getEndDate());
			temp.setStartDate(promotion.getStartDate());
			sellerDAO.saveObject(temp);
		} else {
			// 1.保存
			promotion = (Promotion) sellerDAO.saveObjectDB(promotion);
			id = promotion.getId();

			for (int i = 0; i < fullPrices.length; i++) {
				String fullPrice = fullPrices[i];
				if (ParamUtils.chkString(fullPrice)) {
					String minusPrice = minusPrices[i];
					if (ParamUtils.chkString(minusPrice)) {
						title.append("满"
								+ ProductUtil.BigFormat2(new BigDecimal(
										fullPrice))
								+ "减"
								+ ProductUtil.BigFormat2(new BigDecimal(
										minusPrice)) + "元，");
						PromotionRule r = new PromotionRule(id, new BigDecimal(
								fullPrice), new BigDecimal(minusPrice));
						sellerDAO.saveObject(r);
					}
				}
			}
			title.deleteCharAt(title.lastIndexOf("，"));
			promotion.setTitle(title.toString());
			sellerDAO.saveObject(promotion);
		}

		// 下面是计算 促销信息是否写入到商家信息中
		Date startDate = promotion.getStartDate();
//		Date endDate = promotion.getEndDate();
		String format = "yyyy-MM-dd hh:mm:ss";
		long startSec = DateTimeUtil.dateDiffToSec(
				DateTimeUtil.getDateTime(format),
				DateTimeUtil.format(startDate, format), format);
//		long endSec = DateTimeUtil.dateDiffToSec(
//				DateTimeUtil.getDateTime(format),
//				DateTimeUtil.format(endDate, format), format);

//		Integer sid = promotion.getSid();
//		if (ParamUtils.chkInteger(sid)) {
//			Seller seller = (Seller) sellerDAO.getObject(Seller.class, sid);
//			if (seller != null) {
//				seller.setMinusStr(title.toString());
//				if (startSec < 0 && endSec > 0) {// 在促销时间内
//					seller.setIsMinus(Constants.PUB_STATUS_OPEN);
//				} else {
//					seller.setIsMinus(Constants.PUB_STATUS_CLOSE);
//				}
//				sellerDAO.saveObject(seller);
//			}
//		}
		return promotion;
	}

	public void updateSellerMinusStatus(String nowDate) {
		List<Promotion> pros = sellerDAO.findPromotion(nowDate);
		// 更新商家满减状态
		List<Integer> sids = new ArrayList<Integer>();
		for (Promotion promotion : pros) {
//			Integer sid = promotion.getSid();
//			sids.add(sid);
		}
		sellerDAO.updateSellerMinusStatus(sids, "1");

		// 2.失效的促销
		List<Promotion> pros2 = sellerDAO.findPromotionFail(nowDate);
		List<Integer> sids2 = new ArrayList<Integer>();
		for (Promotion promotion : pros2) {
//			Integer sid = promotion.getSid();
//			sids2.add(sid);
		}
		sellerDAO.updateSellerMinusStatus(sids2, "0");
	}

	public List<PromotionRule> findSellerPromotionRule(Integer sid,
			String nowDate) {
		return sellerDAO.findSellerPromotionRule(sid, nowDate);
	}

	public Seller getSellerByUserId(Integer uid) {
		return sellerDAO.getSellerByUserId(uid);
	}

	@Override
	public List<Type> findTypeBySid(Integer sid) {
		return sellerDAO.findTypeBySid(sid);
	}

	@Override
	public PaginationSupport findSellerListSql(String sqls, List<Object> para,
			int statIndex, int pageSize, String longAlt) {
		return sellerDAO.findSellerListSql(sqls, para, statIndex, pageSize,
				longAlt);
	}

	@Override
	public PaginationSupport findSellerHot(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return sellerDAO.findSellerHot(hqls, para, statIndex, pageSize);
	}

	@Override
	public List<User> findSellerUserBySid(Integer sid) {
		return sellerDAO.findSellerUserBySid(sid);
	}

	public List<Seller> findSellerByCid(Integer cid) {
		return sellerDAO.findSellerByCid(cid);
	}

	@Override
	public int getSellerCountByTid(Integer tid) {
		return sellerDAO.getSellerCountByTid(tid);
	}

	@Override
	public PaginationSupport findSellerListSqlBuy(String hqls,
			List<Object> para, int statIndex, int pageSize, String longAlt) {
		return sellerDAO.findSellerListSqlBuy(hqls, para, statIndex, pageSize,
				longAlt);
	}

	@Override
	public PaginationSupport findSellerCommBuy(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return sellerDAO.findSellerCommBuy(hqls, para, statIndex, pageSize);
	}

	@Override
	public boolean chkSellerUserTask(Integer userId, String type) {
		return sellerDAO.chkSellerUserTask(userId,type);
	}

	@Override
	public List<UserDTO> findSellerUser(Integer sid) {
		return sellerDAO.findSellerUser(sid);
	}

	@Override
	public List<User> findSellerUserBySid(Integer sid, String type) {
		return sellerDAO.findSellerUserBySid(sid, type);
	}

	@Override
	public SellerUserRl getSellerUser(Integer sid, String type) {
		return sellerDAO.getSellerUser(sid, type);
	}
	@Override
	public SellerUserRl getSellerUserType(Integer userId) {
		return sellerDAO.getSellerUserType(userId);
	}
	public String delSeller(Integer sid,String type){
		HQUtils hq = null;
		Integer count = 0;
		//检查电商是否有商品
		if("1".equals(type)){
			hq = new HQUtils("from StoreProduct s where s.sid=?");
			hq.addPars(sid);
			count = sellerDAO.countObjects(hq);
			if(count==0){
				sellerDAO.deleteObject(Seller.class, sid, null);
				sellerDAO.delSellerUserRl(sid);
				return APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		//检查订餐是否有商品
		if("2".equals(type)){
			hq = new HQUtils("from EatProduct e where e.sid=?");
			hq.addPars(sid);
			count = sellerDAO.countObjects(hq);
			
			if(count==0){
				sellerDAO.deleteObject(Seller.class, sid, null);
				sellerDAO.delSellerUserRl(sid);
				return APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		//检查团购是否有商品
		if("3".equals(type)){
			hq = new HQUtils("from BuyProduct b where b.sid=?");
			hq.addPars(sid);
			count = sellerDAO.countObjects(hq);
			
			if(count==0){
				sellerDAO.deleteObject(Seller.class, sid, null);
				sellerDAO.delSellerUserRl(sid);
				return APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		return APIConstants.CODE_REQUEST_ERROR;
	}
}
