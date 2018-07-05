package com.gf.dcb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gf.dcb.model.GfDcbGaOmit;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.model.GfDcbGaTrend;
import com.gf.dcb.model.dto.GfDcbDTO;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.gf.pick11.gdpick11.model.GfGdPick11GaOmit;
import com.ram.model.User;

public interface IDcbService   extends IService {
	/**
	 * 初始化场次
	 */
	public String updateInitSession();
	
	/**
	 * 开奖
	 * @return
	 */
	public List<GfDcbGaSession> updateAndOpenResult();
	/**
	 * 更新每期遗漏
	 * @return
	 */
	public void updateFetchAndOpenOmit(GfDcbGaSession tempsession);
	
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfDcbGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfDcbGaSession getPreviousSessionBySessionNo(String sessionNo);
//	/**
//	 * 保存用户投注信息
//	 */
//	public User saveUserBetInfo(String room,Map<Integer,Integer> betMap,List<GaBetOption> list,GfDcbGaSession session,User user,BigDecimal betAll);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfDcbGaTrend> findDcbTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGfDcbGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);

	
	/**
	 * 更新冷热排行
	 * @param count
	 */
	public void updateFetchAndOpenTrendResult();
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(GfDcbGaSession session,String openResult);
	
//	/**
//	 * 保存单期开奖结果
//	 */
//	public boolean saveAndOpenResult(GfDcbGaSession session,String openResult);
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGfDcbGaBetList(String hql,
			List<Object> para, int pageNum,int pageSize);

	/**
	 * 获取投注详细信息
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetail(String hql,
			List<Object> para, int pageNum, int pageSize);
	
	/**
	 * 根据id查询投注详情
	 * @param string
	 * @param para
	 * @return
	 */
	public List<GfDcbDTO> findGaBetDetailById(String string, List<Object> para);

	/**
	 * 修改结束时间。自动根据当前期修改的时间修改剩下期号的时间。
	 * @param session
	 * @param endTime
	 * @return
	 */
	public boolean modifyDate(GfDcbGaSession session, String endTime);

	/**
	 * 计算合买。
	 */
	public void updateCountJointBet(String sessionNo);
	
	/**
	 * 保存合买投注
	 * @param session 购买的彩票期
	 * @param totalMultiple 倍数
	 * @param u 用户
	 * @param list 投注号码列表
	 * @param num  总共分的份数
	 * @param buyNum  购买份数
	 * @param isGuaranteed  是否保底
	 * @param gaNum  保底份数
	 * @param betNum  总注数
	 * @param betMoney 保底+购买所需花费的钱
	 * @return User 用户
	 */
	public User saveSponsorBet(GfDcbGaSession session,int totalMultiple,User u,List<String> list,int num,int buyNum,String isGuaranteed,int gaNum,int betNum,BigDecimal betMoney);

	/**
	 * 保存代购投注
	 * @param user
	 * @param list
	 * @param seMap 期号对应倍数 key期号 value倍数
	 * @param isAddNo 是否追号
	 * @param isWinStop 是否中奖后停止
	 * @return
	 */
	public User saveProcurementServiceBet(User user, ArrayList<String> list,
			Map<String, Integer> seMap, String isAddNo, String isWinStop,int betNum);

	/**
	 * 更新期号时间
	 * @param startDate开始时间
	 * @param endDate结束时间
	 * @param i 从第几期开始更新
	 */
	public void updateSessionNo(Date startDate, Date endDate, int i);

//	/**
//	 * 开奖前更新合买表。
//	 */
//	public void updateGaBetSponsor();
	/**
	 * 查询走势
	 */
	public List<GfDcbGaOmit> findGfDcbGaOmitList(String string,
			List<Object> para, int num);

	/**
	 * 计算开奖
	 * @param session
	 * @param openResult
	 * @return
	 */
	public String updateGfDcbGaSessionOpenResultMethod(GfDcbGaSession session,
			String openResult,String orderNum);
	
	/**
	 * 统计每日盈亏、每期盈亏。
	 */
	public void updateDayBetCount(GfDcbGaSession session);
	
}
