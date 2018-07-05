package com.apps.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.apps.Constants;
import com.apps.dao.ICouponsDAO;
import com.apps.model.Coupons;
import com.apps.model.CouponsLog;
import com.apps.model.dto.CouponsDTO;
import com.apps.service.ICouponsService;
import com.apps.util.ProductUtil;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.ParamUtils;
import com.ram.model.User;

public class CouponsServiceImpl extends BaseService implements ICouponsService {

	private ICouponsDAO couponsDAO;

	public void setCouponsDAO(ICouponsDAO couponsDAO) {
		this.couponsDAO = couponsDAO;
		super.dao = couponsDAO;
	}

	public PaginationSupport findCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return couponsDAO.findCouponsList(hqls, para, statIndex, pageSize);
	}

	public boolean saveCouponsLog(Integer uid, Integer id) {

		boolean isHave = couponsDAO.chkCoupons(uid, id);
		if (isHave) {// 有此优惠券
			return false;
		} else {
			Coupons coupons = (Coupons) couponsDAO.getObject(Coupons.class, id);
			Integer num = coupons.getNum();// 剩余数量
			if (num.intValue() > 0) {
				String tp = coupons.getTp();
				// 需要积分
				User user = (User) couponsDAO.getObject(User.class, uid);
				Integer points = coupons.getPoints();// 积分
//				Integer userPoints = user.getPoints();// 用户积分
//				if (userPoints != null && points != null) {
//					if (userPoints.intValue() > points.intValue()) {
//						// 优惠券数量-1
//						num = num - 1;
//						coupons.setNum(num);
//						couponsDAO.saveObject(coupons);
//						CouponsLog log = new CouponsLog(id, uid, points,
//								new Date(), "1");
//						if (tp.equals("2")) { // 1.优惠券 2.兑换券
//							log.setCode(ProductUtil.createCode());
//						}
//
//						couponsDAO.saveObject(log);
//						int a = userPoints - points;
//						user.setPoints(a);
//						couponsDAO.saveObject(user);
//						return true;
//					} else {// 积分不足
//						return false;
//					}
//				} else {// 积分不足
					return false;
//				}
			} else {
				return false;
			}
		}
	}

	public PaginationSupport findMyCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return couponsDAO.findMyCouponsList(hqls, para, statIndex, pageSize);
	}

	public Coupons getUserCoupons(int couponsId, int uid) {
		return couponsDAO.getUserCoupons(couponsId, uid);
	}

	public PaginationSupport findCouponsListHou(String hqls, List<Object> para,
			int statIndex, int pageSize) {
		return couponsDAO.findCouponsListHou(hqls, para, statIndex, pageSize);
	}

	public Coupons saveCoupons(Coupons coupons) {
		Integer id = coupons.getId();
		
		Integer sid = coupons.getSid();
		if(!ParamUtils.chkInteger(sid)){
			coupons.setSid(null);
		}
		
		if (ParamUtils.chkInteger(id)) {
			// 修改
			Coupons temp = (Coupons) couponsDAO.getObject(Coupons.class, id);
			temp.setLastSum(coupons.getLastSum());
			temp.setCreateDate(coupons.getCreateDate());
			temp.setNum(coupons.getNum());
			temp.setPoints(coupons.getPoints());
			temp.setRemarks(coupons.getRemarks());
			temp.setSum(coupons.getSum());
			temp.setTotal(coupons.getTotal());
			temp.setType(coupons.getType());
			temp.setValidDate(coupons.getValidDate());
			temp.setTemplate(coupons.getTemplate());
			temp.setJoinAward(coupons.getJoinAward());
			// 修改后保存
			couponsDAO.saveObject(temp);

		} else {
			// 添加
			Integer total = coupons.getTotal();
			coupons.setNum(total);
			coupons.setStatus(Constants.PUB_STATUS_OPEN);
			coupons = (Coupons) couponsDAO.saveObjectDB(coupons);
		}

		return coupons;
	}

	public PaginationSupport findMyCouponsList(String hqls, List<Object> para) {
		return couponsDAO.findMyCouponsList(hqls, para);
	}

	public boolean updateCouponsCode(String phone, String code, Integer uid,
			String ipAddr) {
		CouponsLog cl = couponsDAO.getCouponsByPhoneAndCode(phone, code);
		if (cl != null) {
			String status = cl.getStatus();
			if (status.equals("1")) {// 未使用
				cl.setStatus("2");
				cl.setIp(ipAddr);
				couponsDAO.saveObject(cl);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public Coupons getCouponsByCode(String code) {
		return couponsDAO.getCouponsByCode(code);
	}

	@Override
	public int getUsableNum(Integer uid, BigDecimal productPrice, Integer sid) {
		return couponsDAO.getUsableNum(uid,productPrice,sid);
	}

	@Override
	public List<CouponsDTO> findUsableList(Integer uid, BigDecimal price,
			int sid) {
		return couponsDAO.findUsableList(uid,price,sid);
	}

}
