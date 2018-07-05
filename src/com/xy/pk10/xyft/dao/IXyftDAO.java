package com.xy.pk10.xyft.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pk10.xyft.model.XyftGaSession;
import com.xy.pk10.xyft.model.XyftGaTrend;
import com.xy.pk10.xyft.model.dto.XyftDTO;


public interface IXyftDAO extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public XyftGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public XyftGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<XyftGaTrend> findXyftGaTrendList();
	/**
	 * 冷热所有
	 * @return
	 */
	public List<XyftGaTrend> findXyftGaTrendAllList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findXyftGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期的开奖数据
	 */
	public PaginationSupport  findXyftGaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<XyftDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 获取上一期
	 * @return
	 */
	public XyftGaSession getPreSession();
	/**
	 * 删除XyftGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteXyftGaBet(String hql, List<Object> para);
}
