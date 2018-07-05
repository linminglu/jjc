package com.xy.ssc.bjssc.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.ssc.bjssc.model.BjSscGaSession;
import com.xy.ssc.bjssc.model.BjSscGaTrend;
import com.xy.ssc.bjssc.model.dto.BjSscDTO;


public interface IBjSscDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public BjSscGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public BjSscGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<BjSscGaTrend> findBjSscGaTrendList();
	/**
	 * 冷热排行所有数据
	 * @return
	 */
	public List<BjSscGaTrend> findBjSscGaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findBjSscGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期数据
	 * @return
	 */
	public PaginationSupport  findBjSscGaBetList(String hql, List<Object> para,int pageNum,int pageSize);

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
	public List<BjSscDTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 删除BjSscGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteBjSscGaBet(String hql, List<Object> para);

}
