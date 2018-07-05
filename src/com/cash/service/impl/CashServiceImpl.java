package com.cash.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.apps.model.UserPointDetail;
import com.apps.model.UserTradeDetail;
import com.apps.util.ProductUtil;
import com.cash.dao.ICashDAO;
import com.cash.model.SysBank;
import com.cash.model.UserApplyCash;
import com.cash.model.UserBankBind;
import com.cash.model.dto.CashDTO;
import com.cash.service.ICashService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.StringUtil;
import com.framework.util.ThreeDES;
import com.ram.RamConstants;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.ExcelUtil;

public class CashServiceImpl extends BaseService implements ICashService {
	private ICashDAO cashDAO;
	private IUserService userService;
	
	public void setCashDAO(ICashDAO cashDAO) {
		this.cashDAO = cashDAO;
		super.dao = cashDAO;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	} 
	@Override
	public UserBankBind getUserBankBindByUid(Integer userId) {	
		return cashDAO.getUserBankBindByUid(userId);
	}

	@Override
	public List<UserBankBind> findBankBindListByUid(Integer userId) {
		return cashDAO.findBankBindListByUid(userId);
	}

	@Override
	public PaginationSupport findUserApplyCashList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		return cashDAO.findUserApplyCashList(hqls, para, statIndex, pageSize);
	}
	
	public PaginationSupport findUserCashBankList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		return cashDAO.findUserCashBankList(hqls, para, statIndex, pageSize);
	}
	
	public List<CashDTO> findUserCashBankList(String hqls, List<Object> para){
		return cashDAO.findUserCashBankList(hqls, para);
	}
	
	public void updateUserApplyCash(UserApplyCash userApplyCash){
		cashDAO.updateUserApplyCash(userApplyCash);
		
		StringBuffer remark = new StringBuffer();
		remark.append("提现审核不通过，拒绝提现，返回金额：").append(userApplyCash.getCashMoney());
		userService.saveTradeDetail(null,userApplyCash.getUserId(), Constants.TRADE_TYPE_INCOME,
				Constants.CASH_TYPE_CASH_RETURN, userApplyCash.getCashMoney(), userApplyCash.getApplyCashId(),null,remark.toString());
		userService.updateUserMoney(userApplyCash.getUserId());
		
	}
	public void updateUserApplyCashPass(UserApplyCash userApplyCash){
		cashDAO.updateUserApplyCash(userApplyCash);
		User user=(User) cashDAO.getObject(User.class, userApplyCash.getUserId());
		BigDecimal drawMoney = user.getDrawMoney();
		if(drawMoney==null){
			drawMoney=new BigDecimal(0);
		}
		user.setDrawMoney(GameHelpUtil.round(drawMoney.add(userApplyCash.getCashMoney())));
		userService.saveUser(user);
		
	}
	@Override
	public UserBankBind getUserBankBindByBankAccount(String bankAccount) {
		return cashDAO.getUserBankBindByBankAccount(bankAccount);
	}

	@Override
	public PaginationSupport findUserTradeDetailList(String hqls,
			List<Object> para, int statIndex, int pageSize) {		
		return cashDAO.findUserTradeDetailList(hqls, para, statIndex, pageSize);
	}
	@Override
	public PaginationSupport findUserPointDetailList(String hqls,
			List<Object> para, int statIndex, int pageSize) {		
		return cashDAO.findUserPointDetailList(hqls, para, statIndex, pageSize);
	}
	public PaginationSupport findSysBankList(String hqls, List<Object> para,
			int statIndex, int pageSize){
		return cashDAO.findSysBankList(hqls, para, statIndex, pageSize);
	}
	public void updateBank(SysBank sysBank){
		cashDAO.updateBank(sysBank);
	}
	public List<SysBank> findSysBankList(){
		return cashDAO.findSysBankList();
	}
	public User updateCash(User user,UserApplyCash cash){
		Integer userId = cash.getUserId();
		cash=(UserApplyCash) cashDAO.saveObjectDB(cash);
//		User user=userService.saveTradeDetail(cash.getUserId(), Constants.TRADE_TYPE_PAY,
//		Constants.CASH_TYPE_CASH_OUT, cash.getCashMoney(), cash.getApplyCashId());
		StringBuffer remark = new StringBuffer();
		remark.append(userId).append("申请提现,金额：").append(cash.getCashMoney());
		
		
		//开奖投注用户ids --by.cuisy.20171209
		List<Integer> userIds = new ArrayList<Integer>();
		
		//开奖时把需要sum更新余额的用户id统计放入开奖及明细写入后批量更新 --by.cuisy.20171209
		if(!StringUtil.chkListIntContains(userIds, cash.getUserId())){
			userIds.add(cash.getUserId());
		}//~
		
		userService.saveTradeDetail(user,cash.getUserId(), Constants.TRADE_TYPE_PAY,
				Constants.CASH_TYPE_CASH_OUT, cash.getCashMoney(), cash.getApplyCashId(),null,remark.toString());
		
		//批量更新开奖用户实时余额 --by.cuisy.20171209
		userService.updateUserMoney(userId);
//		log.info("afterMoney>>>>>>>>>>>"+user.getMoney());
		return user;
	}

	@Override
	public Map<String, Object> applyCashExport(List<CashDTO> list, String path) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		WritableWorkbook backbook = null;
		byte[] b = null;
		try {
			String modelPath = path;
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
			
	        writeExcel(backSheet, list);

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
	private static void writeExcel(WritableSheet ws, List<CashDTO> list) throws Exception {
		try {
			String value[] = null;
			Integer rownum = 1;
			Integer[] columnWidth = new Integer[8];
			for (int i = 0; i < columnWidth.length; i++) {
				columnWidth[i] = 20;
			}
			ThreeDES des = new ThreeDES();// 实例化一个对像
			des.getKey(RamConstants.getConfigInfo("card.pwd.key"));// 生成密匙
			for (CashDTO dto : list) {
				String userName = dto.getUser().getUserName();
				String bankName = dto.getUserApplyCash().getBankName();
				String userNameCash = dto.getUserApplyCash().getUserName();
				String accountNo = dto.getUserApplyCash().getAccountNo();
				String cashMoney = dto.getUserApplyCash().getCashMoney().toString();
				String createTime = DateTimeUtil.DateToString(dto.getUserApplyCash().getCreateTime());
				String auditTime = DateTimeUtil.DateToString(dto.getUserApplyCash().getAuditTime());
				String auditStatus = dto.getUserApplyCash().getAuditStatus();
				if(auditStatus.equals("0")){
					auditStatus = "待审核";
				}else if(auditStatus.equals("1")){
					auditStatus = "已通过";
				}else if(auditStatus.equals("2")){
					auditStatus = "未通过";
				}else{
					auditStatus = "未知";
				}
				value = new String[]{userName,bankName,userNameCash,accountNo,cashMoney,createTime,auditTime,auditStatus};
				ExcelUtil.addRow(ws, value, rownum ++,columnWidth);
			}
		} catch (Exception err) {
			System.out.print(err);
			throw err;
		}
	}

	@Override
	public boolean updatePointExchangeMoney(User user, int point) {
		BigDecimal exchangeMoney = new BigDecimal(point/100);//兑换金钱
		Integer userId = user.getUserId();
		
		BigDecimal moeny=ProductUtil.checkBigDecimal(user.getMoney());//用户余额
		BigDecimal userBal=ProductUtil.checkBigDecimal(user.getUserBalance());//每日收益
		BigDecimal userpoints=ProductUtil.checkBigDecimal(user.getUserpoints());//用户积分
//		BigDecimal totalExchangeMoney=ProductUtil.checkBigDecimal(user.getExchangeMoney());//用户累计兑换金额
		
		BigDecimal userMoney = moeny.add(exchangeMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal userPoint = userpoints.subtract(new BigDecimal(point)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		user.setMoney(userMoney);
		user.setUserpoints(userPoint);
		user.setUserBalance(userBal.add(exchangeMoney).setScale(2, BigDecimal.ROUND_HALF_EVEN));
//		user.setExchangeMoney(totalExchangeMoney.add(exchangeMoney));
		cashDAO.saveObject(user);

		//用户的交易明细
		StringBuilder remark = new StringBuilder();
		remark.append("积分兑换：兑换积分").append(point).append("得到金额：").append(exchangeMoney.toString()).append("元");
		UserTradeDetail utd = new UserTradeDetail();
		utd.setUserId(userId);
		utd.setTradeType(Constants.TRADE_TYPE_INCOME);//收入
		utd.setCashType(Constants.CASH_TYPE_CASH_EXCHANGE);
		utd.setCashMoney(exchangeMoney);//金额
		utd.setCreateTime(DateTimeUtil.getNowSQLDate());
		utd.setRemark(remark.toString());
//		utd.setRefId(sp.getJointId());
		utd.setUserMoney(userMoney);
//		utd.setModelType(Constants.GAME_TYPE_THREE);//彩票类型
		cashDAO.saveObject(utd);
		
		StringBuilder pointRemark = new StringBuilder();
		pointRemark.append("积分兑换：兑换积分").append(point).append("得到金额：").append(exchangeMoney.toString()).append("元");

		UserPointDetail pointDetail = new UserPointDetail();
		pointDetail.setUserId(userId);
		pointDetail.setTradeType(Constants.TRADE_TYPE_PAY);//收入
		pointDetail.setCashType(Constants.CASH_TYPE_CASH_EXCHANGE);//系统结算
		pointDetail.setCashPoint(new BigDecimal(point));
		pointDetail.setCreateTime(DateTimeUtil.getNowSQLDate());
		pointDetail.setRemark(pointRemark.toString());
		pointDetail.setUserPoint(userPoint);
		cashDAO.saveObject(pointDetail);
		return true;
	}


	public void saveBank(UserBankBind bank, Integer uid) {
		cashDAO.saveObjectDB(bank);
		User user=(User) cashDAO.getObject(User.class, uid);
		user.setAccountNo(bank.getBindAccount());
		user.setBankName(bank.getBankName());
		user.setBindName(bank.getBindName());
		cashDAO.saveObject(user);
		
	}
	
	@Override
	public int getDayDrawingNum(Integer uid) {
		return cashDAO.getDayDrawingNum(uid);
	}

}
