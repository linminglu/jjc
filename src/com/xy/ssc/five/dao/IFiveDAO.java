package com.xy.ssc.five.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.ssc.five.model.FiveGaSession;
import com.xy.ssc.five.model.FiveGaTrend;
import com.xy.ssc.five.model.dto.FiveDTO;


public interface IFiveDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public FiveGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public FiveGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<FiveGaTrend> findFiveGaTrendList();
	/**
	 * 冷热排行所有数据
	 * @return
	 */
	public List<FiveGaTrend> findFiveGaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findFiveGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期数据
	 * @return
	 */
	public PaginationSupport  findFiveGaBetList(String hql, List<Object> para,int pageNum,int pageSize);

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
	 * 根据id查询投注详情
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<FiveDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除FiveGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteFiveGaBet(String hql, List<Object> para);
}
