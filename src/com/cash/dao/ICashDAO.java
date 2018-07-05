package com.cash.dao;

import java.util.List;

import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.model.dto.CashDTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;

public interface ICashDAO extends IDAO{
	/**
	 * 根据用户id查询用户绑定的银行卡号
	 */
	public UserBankBind getUserBankBindByUid(Integer userId);
	/**
	 * 根据用户id查询用户绑定的银行卡列表
	 */
	public List<UserBankBind> findBankBindListByUid(Integer userId);
	/**
	 * 提现记录列表
	 * @param hqls
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findUserApplyCashList(String hqls, List<Object> para,
			int statIndex, int pageSize);

	/**
	 * 提现记录详情
	 */
	public PaginationSupport findUserCashBankList(String hqls, List<Object> para,
			int statIndex, int pageSize);
	
	/**
	 * 提现记录详情，用于导出数据。
	 */
	public List<CashDTO> findUserCashBankList(String hqls, List<Object> para);
	/**
	 * 修改提现
	 */
	public void updateUserApplyCash(UserApplyCash userApplyCash);

	/**
	 * 通过绑定卡号查询绑定账号
	 * @param bankAccount
	 * @return
	 */
	public UserBankBind getUserBankBindByBankAccount(String bankAccount);
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

	/**
	 * 获得当天提现次数
	 * @param uid
	 * @return
	 */
	public int getDayDrawingNum(Integer uid);
}
