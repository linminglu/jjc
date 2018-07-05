package com.xy.hk.marksix.dao;

import java.util.Date;
import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.hk.marksix.model.MarkSixGaSession;
import com.xy.hk.marksix.model.MarkSixGaTrend;
import com.xy.hk.marksix.model.dto.MarkSixDTO;

public interface IMarkSixDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public MarkSixGaSession getCurrentSession(Date now);
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public MarkSixGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<MarkSixGaTrend> findMarkSixGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findMarkSixGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<MarkSixGaTrend> findMarkSixGaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findMarkSixGaBetList(String hql, List<Object> para,
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
	public List<MarkSixDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 根据当前时间获取上一期
	 * @param now
	 * @return
	 */
	public MarkSixGaSession getPreSession(Date now);
	/**
	 * 删除MarkSixGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteMarkSixGaBet(String hql, List<Object> para);
}
