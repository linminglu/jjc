package com.xy.poker.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.poker.model.PokerGaSession;
import com.xy.poker.model.PokerGaTrend;
import com.xy.poker.model.dto.PokerDTO;
import com.xy.ssc.cqssc.model.CqSscGaSession;

public interface IPokerDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public PokerGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public PokerGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<PokerGaTrend> findPokerGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findPokerGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<PokerGaTrend> findPokerGaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findPokerGaBetList(String hql, List<Object> para,
			int pageNum, int pageSize);

	/**
	 * 投注详细信息
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetail(String hql, List<Object> para,
			int pageNum, int pageSize);
	
	/**
	 * 根据投注id查询投注详情。
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<PokerDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除PokerGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deletePokerGaBet(String hql, List<Object> para);
}
