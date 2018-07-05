package com.apps.service;

import java.math.BigDecimal;
import java.util.List;

import com.apps.model.Coupons;
import com.apps.model.CouponsLog;
import com.apps.model.dto.CouponsDTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;

/**
 * 消费券
 * 
 * @author Mr.zang
 * 
 */
public interface ICouponsService extends IService {

	/**
	 * 获得消费券
	 * 
	 * @return
	 */
	public PaginationSupport findCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 兑换消费券
	 * 
	 * @param uid
	 * @param id
	 *            消费券id
	 * @return
	 */
	public boolean saveCouponsLog(Integer uid, Integer id);

	/**
	 * 获得我的消费券
	 * 
	 * @return
	 */
	public PaginationSupport findMyCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 获得用户的优惠券信息
	 * 
	 * @param couponsId
	 * @param uid
	 * @return
	 */
	public Coupons getUserCoupons(int couponsId, int uid);
	/**
	 * 获得全部优惠劵信息的列表
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findCouponsListHou(String hqls, List<Object> para,
			int statIndex, int pageSize);
	/**
	 * 保存优惠劵信息
	 * @param coupons
	 * @return
	 */
    public  Coupons  saveCoupons(Coupons coupons);

    
	public PaginationSupport findMyCouponsList(String string, List<Object> para);

	/**
	 * 激活兑换码
	 * @param phone
	 * @param code
	 * @param uid
	 * @param ipAddr
	 * @return
	 */
	public boolean updateCouponsCode(String phone, String code, Integer uid,
			String ipAddr);

	/**
	 * 根据兑换码查询
	 * @param code
	 * @return
	 */
	public Coupons getCouponsByCode(String code);

	/**
	 * 获得可以使用的优惠券数量
	 * @param uid 用户id
	 * @param productPrice 消费金额
	 * @param sid 商家id
	 * @return
	 */
	public int getUsableNum(Integer uid, BigDecimal productPrice, Integer sid);

	/**
	 * 获得可以用的优惠券
	 * @param uid 用户id
	 * @param price 消费金额
	 * @param sid 商家id
	 * @return
	 */
	public List<CouponsDTO> findUsableList(Integer uid, BigDecimal price,
			int sid);

}
