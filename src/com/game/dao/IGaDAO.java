package com.game.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.apps.model.Param;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.UserBetCount;
import com.game.model.dto.GaDTO;
import com.game.model.dto.SpDetailDTO;
import com.game.model.dto.WinCoDTO;

public interface IGaDAO extends IDAO {

	/**
	 * 根据游戏类型和玩法类型获取投注类型
	 */
	public List<GaDTO> getOptionTypeList(String  gameType,String playType);
	/**
	 * 根据投注类型查询投注项
	 */
	public List<GaBetOption> getBetPanelOptionList(String  gameType,String playType,String  optionType,Integer userId);
	/**
	 * 根据投注id查询投注项
	 */
	public List<GaBetOption> getGaBetOptionByIds(String ids);
	/**
	 * 查询具体投注项
	 */
	public List<GaBetDetail> findGaBetDetailList(String hql,List<Object> para);
	
	/**
	 * 统计每日盈亏
	 */
	public GaDTO getGaDTO(String gameType,Date date);
	/**
	 * 查询用户投注记录
	 * @return
	 */
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,int statIndex,int pageNum);
	
	/**
	 * 查询用户竞猜记录
	 * @return
	 */
	public PaginationSupport findJcRecordlList(String hql, List<Object> para,int statIndex,int pageNum);
	
	public GaSessionInfo findGaSessionInfo(String gameType);
	public List<GaSessionInfo> findGaSessionInfoList();
	public List<GaSessionInfo> findGaSessionInfoList(String playCate);
	
	/**
	 * 查询投注发起表
	 * @return
	 */
	public PaginationSupport findGaBetSponsor(String hql, List<Object> para,int statIndex,int pageNum);
	
	/**
	 * 根据彩票类型查询赔率表
	 * @param gameType
	 * @return
	 */
	public List<GaBetOption> findGaBetOptionByGameType(String gameType);
	
	/**
	 * 根据id查询发起投注表信息
	 * @param id
	 * @return
	 */
	public GaBetSponsor findGaBetSponsorById(String id);
	/**
	 * 查询用户所有购买彩票记录
	 * @param hql
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetSponsorDetail(String hql,
			List<Object> para, int statIndex, int pageSize);
	/**
	 * 查询中奖排行榜前10位
	 * @return
	 */
	public List<WinCoDTO> findGaWinCountList(String gameType);
	/**
	 * 开奖前更新合买记录表，清算用户保底钱数，结算最终投注的钱数
	 */
	public List<SpDetailDTO>  findGaBetSponsorListByGameTypeAndBetType(String gameType,String betType);
	/**
	 * 查询用户参与合买记录
	 */
	public List<SpDetailDTO> findGaBetPartListByJointId(Integer jointId);
	/**
	 * @param jointId
	 * @return
	 */
	public List<GaBetSponsorDetail> findGaBetSponsorDetailListByJointId(Integer jointId);
	/**
	 * 查询合买表
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<GaBetSponsor> findGaBetSponsorList(String hql,List<Object> para);
	/**
	 * 查询合买表及关联的user表
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<SpDetailDTO> findGaBetSponsorAndUserList(String hql,
			List<Object> para);
	
	public List<GaWinCount> findGaWinCountList(String hql, List<Object> para);
	/**
	 * 查询每日投注
	 * @param hqls
	 * @param para
	 * @return
	 */
	public GaDayBetCount findDayBetCount(StringBuffer hqls, List<Object> para);
	/**
	 * 更新每日投注及每日中奖金额
	 */
	public void updateUserDayWin();
	/**
	 * 更新每周中奖金额
	 */
	public void updateUserWeekWin();
	
	/**
	 * 查询游戏信息表
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaSessionInfoList(String string,
			List<Object> para, int startIndex, int pageSize);

	public void updateBj3GaSession(Date date);
	public void updateMarkSixGaSession(Date date);
	public void updateGdK10GaSession(Date date);
	public void updateBjLu28GaSession(Date date);
	public void updateBjPk10GaSession(Date date);
	public void updateGxK10GaSession(Date date);
	public void updateCqSscGaSession(Date date);
	public void updateJsK3GaSession(Date date);
	public void updatePokerGaSession(Date date);
	public void updateTjSscGaSession(Date date);
	public void updateXjSscGaSession(Date date);
	public void updateGdPick11GaSession(Date date);
	public void updateXjpLu28GaSession(Date date);
	public void updateGaBetDetail(Date date);
	
	/**
	 * 查询用户每日统计数据
	 * @param hql
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findDailyStatisticsList(String hql,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 查询用户每日统计数据,不按userId gruop by
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findDailyStatisticsList2(String hql,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 统计用户交易记录
	 * @return
	 */
	public List<GaDTO> findUserTradeDetailList(String hql,List<Object> para);
	
	/**
	 * 统计代理数据
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<GaDTO> findAgentStatisticsList(String hql,
			List<Object> para);
	/**
	 * 统计代理的下级会员
	 * @param hsql
	 * @param para
	 * @return
	 */
	public List<GaDTO> findUserSubmemberList(String hsql, List<Object> para);
	public void updateUserInfo();
	public List<UserBetCount> findUserBetCountListForAgent(Date date);
	public List<GaDTO> findUserBetCountList(Date now);
	public List<GaDTO> findUserBetCountList(String string, List<Object> para);
	
	/**
	 * 统计预设结果中奖合值
	 * @param sessionNo
	 * @param gameType
	 * @return
	 */
	public GaDTO getDetailEstimateMoney(String sessionNo, String gameType);
	
	/**
	 * 根据关联表查询投注记录
	 * @param string
	 * @param para
	 * @return
	 */
	public List<GaBetDetail> findGaBetDetailListByRL(String string,
			List<Object> para);
	
	
	/**
	 * 更新用户投注明细状态为未开奖(信用)
	 * @param sessionId
	 * @param gameType
	 */
	public void updateGaBetDetailWinResult(Integer sessionId, String gameType);
	/**
	 * 更新用户投注明细状态为未开奖(官方)
	 * @param sessionId
	 * @param gameType
	 */
	public void updateBetDetailWinResult(Integer sessionId, String gameType);
	/**
	 * 获得用户当天的打码量
	 * @param uid
	 * @return
	 */
	public BigDecimal getDayBetMoney(Integer uid);
	
	public Param getParam(String type);
}
