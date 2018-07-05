package com.xy.pick11.sdpick11.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.xy.pick11.sdpick11.model.SdPick11GaSession;
import com.xy.pick11.sdpick11.model.SdPick11GaTrend;
import com.xy.pick11.sdpick11.model.dto.SdPick11DTO;

public interface ISdPick11DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public SdPick11GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public SdPick11GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<SdPick11GaTrend> findSdPick11GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findSdPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findSdPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<SdPick11DTO> findGaBetDetailById(String hql, List<Object> para);
	/**
	 * 删除SdPick11GaBet表数据
	 * @param hql
	 * @param para
	 */
	public void deleteSdPick11GaBet(String hql, List<Object> para);
}
