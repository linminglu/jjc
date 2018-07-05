package com.game.service;


import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaHhmfBet;
import com.game.model.GaHhmfBetDetail;
import com.game.model.GaHhmfBetOption;
import com.game.model.GaHhmfSession;
import com.game.model.dto.GaHhmfDTO;

public interface IGaHhmfService extends IService {
	
	
	/**
	 * 初始化场次
	 */
	public String updateInitSession();
	
	/**
	 * 检查场次
	 */
	public String updateCheckSession();
	
	/**
	 * 历史开奖结果
	 */
	public PaginationSupport findLotteryResultList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 会员投注查询
	 */
	public PaginationSupport findBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 获取单个用户的查询情况
	 */
	public PaginationSupport findOneUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 用户投注情况
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */

	public PaginationSupport findBetUserList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 用户投注详情
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBetDetailList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 每种花色开出的数量统计
	 * @return
	 */
	public List<GaHhmfDTO> findColorCountList();
	/**
	 * 查找最近30期
	 * @return
	 */
	public List<GaHhmfDTO> findRecent30GaHhmfSessionList();
	/**
	 * 查询当前期
	 * @return
	 */
	public GaHhmfSession getCurrentSession(Date now);
	/**
	 * 统计一种花色当前期下注的积分数
	 */
	public Integer findCurrentSessionCountPointsByBetType(String betType,Date now);
	/**
	 * 查询用户的投注记录
	 */
	public PaginationSupport findUserBetList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 查询本期所有详情记录
	 */
	public List<GaHhmfBetDetail> findUserBetDetailList(Integer sessionId,Integer uid);
	
	/**
	 * 查找某一期用户的投注项
	 */
	public GaHhmfBet findGaHhmfBetBySessionIdAndUid(Integer sessionId,Integer uid);
//	public void saveBetInfo();

	/**
	 * 赔率列表
	 */
	public PaginationSupport findBetOptionList(String hqls, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 排序
	 */
	public void updateSort(Integer id, String flag);

	/**
	 * 查询黑红梅方的赔率
	 * @return
	 */
	public List<GaHhmfBetOption> findOddsList(String hql, List<Object> para);
	/**
	 * 根据类型查询投注赔率
	 */
	public GaHhmfBetOption getGaHhmfOddsByType(String betType);
	
	/**
	 * 保存用户投注记录
	 */
	public String saveUserBet(String code,String message,JSONObject map,GaHhmfSession currentSession,Integer uid,String betType,String betPoints,Date now);

	/**
	 * 保存用户投注记录
	 */
	public void saveUserBet(JSONObject map,GaHhmfSession currentSession,Integer uid,String betType,String betPoints,Date now);
	/**
	 * 保存用户取消投注
	 */
	public void saveUserCancel(JSONObject map,GaHhmfSession currentSession,Integer uid);
	/**
	 * 保存用户续押投注记录
	 */
	public void saveUserContinueBet(JSONObject map,GaHhmfSession currentSession,Integer uid,String pitems,Date now);
}
