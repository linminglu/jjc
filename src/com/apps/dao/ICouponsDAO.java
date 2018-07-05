package com.apps.dao;

import java.math.BigDecimal;
import java.util.List;

import com.apps.model.Coupons;
import com.apps.model.CouponsLog;
import com.apps.model.dto.CouponsDTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface ICouponsDAO extends IDAO {

	/**
	 * 获得代金券列表
	 * 
	 * @return
	 */
	public PaginationSupport findCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 获得我的代金券列表
	 * 
	 * @return
	 */
	public PaginationSupport findMyCouponsList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 判断是否已领取过此优惠券
	 * 
	 * @param uid
	 *            用户id
	 * @param id
	 *            优惠券id
	 * @return true 已领取 false 未领取
	 */
	public boolean chkCoupons(Integer uid, Integer id);

	/**
	 * 获得用户的优惠信息
	 * 
	 * @param couponsId
	 * @param uid
	 * @return
	 */
	public Coupons getUserCoupons(int couponsId, int uid);

	/**
	 * 获得全部优惠劵信息的列表
	 * 
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findCouponsListHou(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 获得用户的优惠券信息
	 * 
	 * @param couponsId
	 * @param userId
	 * @return
	 */
	public CouponsLog getCouponsLog(Integer couponsId, Integer userId);

	public PaginationSupport findMyCouponsList(String hqls, List<Object> para);

	/**
	 * 根据手机号和兑换码查询
	 * 
	 * @param phone
	 * @param code
	 * @return
	 */
	public CouponsLog getCouponsByPhoneAndCode(String phone, String code);

	/**
	 * 根据code获得兑换券
	 * 
	 * @param code
	 * @return
	 */
	public Coupons getCouponsByCode(String code);

	/**
	 * 获得可以使用的优惠券数量
	 * 
	 * @param uid
	 *            用户id
	 * @param productPrice
	 *            消费金额
	 * @param sid
	 *            商家id
	 * @return
	 */
	public int getUsableNum(Integer uid, BigDecimal productPrice, Integer sid);

	/**
	 * 获得可以用的优惠券
	 * 
	 * @param uid
	 *            用户id
	 * @param price
	 *            消费金额
	 * @param sid
	 *            商家id
	 * @return
	 */
	public List<CouponsDTO> findUsableList(Integer uid, BigDecimal price,
			int sid);

}
