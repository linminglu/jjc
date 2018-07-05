package com.gf.three.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.dto.WinCoDTO;
import com.gf.pick11.gdpick11.model.GfGdPick11GaSession;
import com.gf.ssc.bjssc.model.GfBjSscGaSession;
import com.gf.three.model.GfThreeGaOmit;
import com.gf.three.model.GfThreeGaSession;
import com.gf.three.model.GfThreeGaTrend;
import com.ram.model.User;

public interface IThreeService   extends IService {
	/**
	 * 初始化彩票场次
	 * 参数 i=0 为今天。i=1 为明天。 i=2 为后天。
	 */
	public String updateInitSession(int num);
	
	
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfThreeGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfThreeGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfThreeGaTrend> findFcTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findFcGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);

	/**
	 * 更新冷热排行
	 * @param count
	 */
	public void updateFetchAndOpenTrendResult(GfThreeGaSession session);

	/**
	 * 计算合买。
	 */
	public void updateCountJointBet(String sessionNo);

	/**
	 * 保存合买投注
	 * @param GfCqSscGaSession 期
	 * @param mutiple 倍数
	 * @param u 用户
	 * @param list 投注号码列表
	 * @param seMap 期号对应倍数
	 * @param num 总共分的份数
	 * @param buyNum 购买份数
	 * @param isGuaranteed 是否保底
	 * @param guNum 保底份数
	 * @param betTotalNum 投总注数
	 * @param money 需要投注总金额
	 * @return
	 */
	public User saveSponsorBet(GfThreeGaSession se,int mutiple,User u,List<String> list,int num,int buyNum,String isGuaranteed,int gaNum,int betTotalNum,BigDecimal money);

	/**
	 * 保存代购投注
	 * @param user
	 * @param list
	 * @param seMap 期号对应倍数 key期号 value倍数
	 * @param isAddNo 是否追号
	 * @param isWinStop 是否中奖后停止
	 * @param betNum 投注总注数
	 * @return
	 */
	public User saveProcurementServiceBet(User user, List<String> list,
			Map<String, Integer> seMap, String isAddNo, String isWinStop,int betNum);

	/**
	 * 查询中奖排行榜前10位
	 * @return
	 */
	public List<WinCoDTO> findGaWinCountList();
	
	/**
	 * 更新每期遗漏数据
	 */

	public void updateFetchAndOpenOmit(GfThreeGaSession session);

	/**
	 * 开奖，更新sessionInfo表，更新trend表
	 * @return
	 */
	//public GfThreeGaSession updateAndOpenResult();
	public List<GfThreeGaSession> updateAndOpenResult(Map<String, SessionItem> sessionNoMap);
	


	/**
	 * 计算开奖结果
	 * @param threeSessaion
	 * @param openResult
	 * @return
	 */
	public String updateThreeSessionOpenResultMethod(
			GfThreeGaSession threeSessaion, String openResult, String orderNum);


	/**
	 * 查询遗漏
	 * @param string
	 * @param para
	 * @param i
	 * @return
	 */
	public List<GfThreeGaOmit> findGfThreeGaOmitList(String string,
			List<Object> para, int i);
	
	/**
	 * 统计每一种彩票的每一期的投注中奖金额。
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGameBetAndWinList(String string,
			List<Object> para, int startIndex, int pageSize);


	public PaginationSupport findGfThreeGaSessionList(String string,
			List<Object> para, int startIndex, int pageSize);


	public String saveAndOpenResult(GfThreeGaSession session, String openResult);

	/**
	 * 统计每日盈亏、每期盈亏。
	 */
	public void updateDayBetCount(GfThreeGaSession session);
	
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(GfThreeGaSession session);

}
