package com.cash.service;

import java.util.List;
import java.util.Map;

import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.model.dto.CashDTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.ram.model.User;

public interface ICashService extends IService {

	/**
	 * 根据用户id查询用户绑定的银行卡号
	 */
	public UserBankBind getUserBankBindByUid(Integer userId);
	/**
	 * 根据用户id查询用户绑定的银行卡列表
	 */
	public List<UserBankBind> findBankBindListByUid(Integer userId);
	/**
	 * 提现记录
	 */
	public PaginationSupport findUserApplyCashList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 提现记录详情
	 */
	public PaginationSupport findUserCashBankList(String hqls, List<Object> para,
			int statIndex, int pageSize);
	
	/**
	 * 提现记录详情，不用分页，用于导出数据。
	 */
	public List<CashDTO> findUserCashBankList(String hqls, List<Object> para);
	
	/**
	 * 修改提现
	 */
	public void updateUserApplyCash(UserApplyCash userApplyCash);
	/**
	 * 修改提现 通过
	 */
	public void updateUserApplyCashPass(UserApplyCash userApplyCash);

	/**
	 * 根据卡号查询绑定账号
	 */
	public UserBankBind getUserBankBindByBankAccount(String  bankAccount);
	
	/**
	 * 资金明细
	 */
	public PaginationSupport findUserTradeDetailList(String hqls, List<Object> para,
			int statIndex, int pageSize);
	/**
	 * 积分明细
	 */
	public PaginationSupport findUserPointDetailList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 银行列表
	 */
	public PaginationSupport findSysBankList(String hqls, List<Object> para,
			int statIndex, int pageSize);
	/**
	 * 修改银行
	 */
	public void updateBank(SysBank sysBank);
	/**
	 * 所有银行列表
	 */
	public List<SysBank> findSysBankList();
	
	public User updateCash(User user,UserApplyCash cash);
	
	/**
	 * 导出用户提现表。
	 * @param list
	 * @param path
	 * @return
	 */
	public Map<String,Object> applyCashExport(List<CashDTO> list,String path);
	
	/**
	 * 积分兑换
	 * @param user
	 * @param point
	 * @return
	 */
	public boolean updatePointExchangeMoney(User user, int point);
	public void saveBank(UserBankBind bank, Integer  uid);
	
	/**
	 * 获取用户当天提现次数
	 * @param uid
	 * @return
	 */
	public int getDayDrawingNum(Integer uid);
}
