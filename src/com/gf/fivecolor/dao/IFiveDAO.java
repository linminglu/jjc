package com.gf.fivecolor.dao;

import java.util.List;

import com.gf.fivecolor.model.GfFiveGaOmit;
import com.gf.fivecolor.model.GfFiveGaSession;
import com.gf.fivecolor.model.GfFiveGaTrend;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.dto.WinCoDTO;

public interface IFiveDAO  extends IDAO {

	/**
	 * 根据系统当前时间获取当前场次
	 * @return
	 */
	public GfFiveGaSession getCurrentSession();

	/**
	 * 根据期号获取场次相关信息
	 * @param sessionNo
	 * @return
	 */
	public GfFiveGaSession getPreviousSessionBySessionNo(String sessionNo);

	/**
	 * 冷热排行榜列表
	 * @return
	 */
	public List<GfFiveGaTrend> findGaTrendList();

	/**
	 * 开奖列表
	 * @return
	 */
	public PaginationSupport findFcGaSessionList(String hsql,List<Object> para,int pageNum, int pageSize);

//	public List<FcGaSession> getCurrentSession(int start, int end);
	
//	/**
//	 * 查询北京3分彩投注列表
//	 * @param hql
//	 * @param para
//	 * @param pageNum
//	 * @param pageSize
//	 * @return
//	 */
//	public PaginationSupport findFcGaBetList(String hql, List<Object> para,
//			int pageNum, int pageSize);

//	/**
//	 * 投注详细信息
//	 * @param hql
//	 * @param para
//	 * @param pageNum
//	 * @param pageSize
//	 * @return
//	 */
//	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
//			int pageNum, int pageSize);

//	/**
//	 * 根据id查询投注详情
//	 * @param hql
//	 * @param para
//	 * @return
//	 */
//	public List<FcDTO> findGaBetDetailById(String hql, List<Object> para);

	/**
	 * 查询中奖排行榜
	 * @return
	 */
	public List<WinCoDTO> findGaWinCountList();
	
	public GfFiveGaOmit getFcGaOmitBySessionNo(String sessionNo);

	public List<GfFiveGaOmit> findGfFiveGaOmitList(String hqls, List<Object> para,
			int i);

	public PaginationSupport findGfFiveGaSessionList(String hqls,
			List<Object> para, int startIndex, int pageSize);
	
	/**
	 * 统计每一种彩票的每一期的投注中奖金额。
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGameBetAndWinList(String hqls,
			List<Object> para, int startIndex, int pageSize);

}
