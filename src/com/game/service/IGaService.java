package com.game.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.apps.model.Param;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaDayBetCount;
import com.game.model.GaSessionInfo;
import com.game.model.GaWinCount;
import com.game.model.dto.GaDTO;
import com.game.model.dto.SpDetailDTO;
import com.game.model.dto.WinCoDTO;
import com.ram.model.User;

public interface IGaService extends IService {
	/**
	 * 每天凌晨更新用户的今日收益  置为0
	 */
	
	public  void updateUserGains();
	/**
	 * 统计每日盈亏
	 * @param statDay 格式yyyy-MM-dd null实际时间
	 */
	public void updateDayBetCount(String statDay);
	/**
	 * 根据游戏类型和玩法类型获取投注类型
	 */
	public List<GaDTO> getOptionTypeList(String gameType, String playType);

	/**
	 * 根据投注类型查询投注项
	 * @param gameType 游戏类型   0=三分彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
	 * @param playType 玩法类型
	 * @param optionType 
	 * @param userId
	 * @return
	 */
	public List<GaBetOption> getBetPanelOptionList(String gameType,
			String playType, String optionType, Integer userId);

	/**
	 * 根据投注id查询投注项
	 */
	public List<GaBetOption> getGaBetOptionByIds(String ids);

	/**
	 * 查询具体投注项
	 */
	public List<GaBetDetail> findGaBetDetailList(String hql, List<Object> para);

	/**
	 * 统计每日盈亏
	 */
	public GaDTO getGaDTO(String gameType, Date date);

	/**
	 * 查询用户投注记录
	 * 
	 * @return
	 */
	public PaginationSupport findGaBetDetailList(String hql, List<Object> para,
			int statIndex, int pageNum);
	
	/**
	 * 查询用户竞猜记录
	 * @return
	 */
	public PaginationSupport findJcRecordlList(String hql, List<Object> para,int statIndex,int pageNum);
	
	/**
	 * 查询彩票信息
	 * @param gameType
	 */
	public GaSessionInfo findGaSessionInfo(String gameType);
	public List<GaSessionInfo> findGaSessionInfoList();
	public List<GaSessionInfo> findGaSessionInfoList(String playCate);
	/**
	 * 退还用户本期投注金额
	 * @param sessionId
	 * @return
	 */
	public boolean saveDrawback(Integer sessionId,String gameType);
	
	/**
	 * 查询投注发起表
	 * @param string
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetSponsor(String string, List<Object> para,
			int statIndex, int pageSize);
	
	/**
	 * 根据游戏类型查询游戏赔率列表
	 * @param playType
	 * @return
	 */
	public List<GaBetOption> findGaBetOptionByGameType(String gameType);
	
	/**
	 * 根据id查询发起投注详情
	 * @param string
	 * @param para
	 * @return
	 */
	public GaBetSponsor findGaBetSponsorById(String string);
	
	/**
	 * 追加（参与）合买投注
	 * @param user
	 * @param sp 发起购买表
	 * @param betMoney 购买金额
	 * @param buyNum2 购买份数
	 * @param restNum 剩余份数
	 * @param schedul 进度
	 * @return user
	 */
	public User updateJointBet(User user, GaBetSponsor sp, BigDecimal betMoney,
			int buyNum2,int restNum,BigDecimal schedul);
	/**
	 * 查询用户所有购买彩票记录
	 * @param string
	 * @param para
	 * @param statIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetSponsorDetail(String string,
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
	 * 查询投注具体项
	 */
	public List<GaBetSponsorDetail> findGaBetSponsorDetailListByJointId(Integer jointId);
	/**
	 * 
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<GaBetSponsor> findGaBetSponsorList(String hql,List<Object> para);
	/**
	 * 
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<SpDetailDTO> findGaBetSponsorAndUserList(String hql,List<Object> para);
	
	/**
	 * 查询中奖排行榜
	 * @return
	 */
	public List<GaWinCount> findGaWinCountList(String hql,List<Object> para);
	/**
	 * 更新每日投注金额，每日中奖金额
	 */
	public void updateUserDayWinAndBet();
	/**
	 * 更新每周中奖金额
	 */
	public void updateUserWeekWin();
	/**
	 * 查询每日投注统计
	 * @param hqls
	 * @param para
	 * @return
	 */
	public GaDayBetCount findDayBetCount(StringBuffer hqls, List<Object> para);
	
	/**
	 * 更新没日投注及中奖金额
	 */
	public void updateUserDayWin();
	
	
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
	public void updateCqSscGaSession(Date date);
	public void updateGdPick11GaSession(Date date);
	public void updateGxK10GaSession(Date date);
	public void updateJsK3GaSession(Date date);
	public void updatePokerGaSession(Date date);
	public void updateTjSscGaSession(Date date);
	public void updateXjSscGaSession(Date date);
	public void updateXjpLu28GaSession(Date date);
	public void updateGaBetDetail(Date date);
	
	/**
	 * 查询用户每日统计数据
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findDailyStatisticsList(String string,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 查询用户每日统计数据,不按userId gruop by
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findDailyStatisticsList2(String string,
			List<Object> para, int startIndex, int pageSize);
	/**
	 * 统计每个用户每日充值提现打码中奖数据
	 */
	public void updateDailyStatistics(String statDay);
	
	/**
	 * 统计代理数据
	 * @param string
	 * @param para
	 * @return
	 */
	public List<GaDTO> findAgentStatisticsList(String string,
			List<Object> para);
	/**
	 * 统计代理的下级会员
	 * @param hsql2
	 * @param para
	 * @return
	 */
	public List<GaDTO> findUserSubmemberList(String hsql2,
			List<Object> para);
	/**
	 * 统计用户交易
	 * @param string
	 * @param para6
	 * @return
	 */
	public List<GaDTO> findUserTradeDetailList(String string, List<Object> para6);
	/**
	 * 每日会员返水给代理
	 */
	public String updateAgentBack(Date date);
	/**
	 * 每日会员返水给自己
	 */
	public String updateMemberBack(Date date);
	public void saveSessionInfo(GaSessionInfo sessionInfo);
	/**
	 * 会员每日投注统计
	 * @param statDay 格式yyyy-MM-dd null实际时间
	 */
	public void updateUserBetCount(String statDay);
	
	/**
	 * 静态化wap版网页
	 * @return
	 */
	public String updateStaticHtml();
	
	/**
	 * 统计各预设结果中奖值
	 * @param sessionNo
	 * @param gameType
	 * @return
	 */
	public GaDTO getDetailEstimateMoney(String sessionNo,
			String gameType);
	
	/**
	 * 获取开奖最小结果
	 * @param sessionNo 期号
	 * @param estimateResult 预设结果
	 * @param gameType 彩种
	 * @param i 开奖次数
	 * @return
	 */
	public String getMinResult(String sessionNo, String estimateResult, String gameType, Integer i);
	
	/**
	 * 根据id查询交易明细
	 * @param tradeDetailId
	 * @return
	 */
	public List<GaBetDetail> findGaBetDetailListByTradeId(Integer tradeDetailId);
	
	/**
	 * 派彩错误，撤回已经派彩的金额(信用)
	 * @param sessionId
	 * @param gameType
	 * @param sessionNo 
	 * @return
	 */
	public boolean saveXyRevokePrize(Integer sessionId,String gameType, String sessionNo);
	/**
	 * 派彩错误，撤回已经派彩的金额(官方)
	 * @param sessionId
	 * @param gameType
	 * @param sessionNo 
	 * @return
	 */
	public boolean saveGfRevokePrize(Integer sessionId,String gameType, String sessionNo);
	
	
	/**
	 * 获得用户当天的打码量
	 * @param uid
	 * @return
	 */
	public BigDecimal getDayBetMoney(Integer uid);
	/**
	 * 获得param
	 * @param uid
	 * @return
	 */
	public Param getParam(String type);
}
