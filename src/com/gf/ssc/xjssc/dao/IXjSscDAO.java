package com.gf.ssc.xjssc.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.dto.WinCoDTO;
import com.gf.ssc.xjssc.model.GfXjSscGaOmit;
import com.gf.ssc.xjssc.model.GfXjSscGaSession;
import com.gf.ssc.xjssc.model.GfXjSscGaTrend;

public interface IXjSscDAO  extends IDAO {

	/**
	 * 根据系统当前时间获取当前场次
	 * @return
	 */
	public GfXjSscGaSession getCurrentSession();

	/**
	 * 根据期号获取场次相关信息
	 * @param sessionNo
	 * @return
	 */
	public GfXjSscGaSession getPreviousSessionBySessionNo(String sessionNo);

	/**
	 * 冷热排行榜列表
	 * @return
	 */
	public List<GfXjSscGaTrend> findGaTrendList();

	/**
	 * 开奖列表
	 * @return
	 */
	public PaginationSupport findGaSessionList(String hsql,List<Object> para,int pageNum, int pageSize);

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
	
	public GfXjSscGaOmit getFcGaOmitBySessionNo(String sessionNo);

	public List<GfXjSscGaOmit> findGfXjSscGaOmitList(String hql, List<Object> para,
			int num);


	public PaginationSupport findGfXjSscGaSessionList(String hql,
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
