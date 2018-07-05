package com.xy.ssc.tjssc.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.ssc.tjssc.model.TjSscGaSession;
import com.xy.ssc.tjssc.model.TjSscGaTrend;
import com.xy.ssc.tjssc.model.dto.TjSscDTO;


public interface ITjSscDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public TjSscGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public TjSscGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<TjSscGaTrend> findTjSscGaTrendList();
	/**
	 * 冷热排行所有数据
	 * @return
	 */
	public List<TjSscGaTrend> findTjSscGaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findTjSscGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期数据
	 * @return
	 */
	public PaginationSupport  findTjSscGaBetList(String hql, List<Object> para,int pageNum,int pageSize);

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
	public List<TjSscDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除TjSscGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteTjSscGaBet(String hql, List<Object> para);
}
