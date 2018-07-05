package com.xy.hk.sflhc.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.hk.sflhc.model.SflhcGaSession;
import com.xy.hk.sflhc.model.SflhcGaTrend;
import com.xy.hk.sflhc.model.dto.SflhcDTO;

public interface ISflhcDAO  extends IDAO {
	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SflhcGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SflhcGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SflhcGaTrend> findSflhcGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSflhcGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 冷热所有
	 * @return
	 */
	public List<SflhcGaTrend> findSflhcGaTrendAllList();
	/**
	 * 查询投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findSflhcGaBetList(String hql, List<Object> para,
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
	public List<SflhcDTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除SflhcGaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteSflhcGaBet(String hql, List<Object> para);
}
