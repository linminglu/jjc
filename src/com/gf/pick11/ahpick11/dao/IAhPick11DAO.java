package com.gf.pick11.ahpick11.dao;

import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.gf.pick11.ahpick11.model.GfAhPick11GaOmit;
import com.gf.pick11.ahpick11.model.GfAhPick11GaSession;
import com.gf.pick11.ahpick11.model.GfAhPick11GaTrend;
import com.gf.pick11.ahpick11.model.dto.GfAhPick11DTO;

public interface IAhPick11DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfAhPick11GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfAhPick11GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfAhPick11GaTrend> findGfAhPick11GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGfAhPick11GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findAhPick11GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<GfAhPick11DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 查询遗漏
	 */
	public GfAhPick11GaOmit getAhPick11OmitBySessionNo(String sessionNo);
	/**
	 * 根据日期查找日期中的第一期
	 */
	public GfAhPick11GaSession getFirstSessionByDate(String date);
	/**
	 * 根据日期查找日期中的最后一期
	 */
	public GfAhPick11GaSession getEndSessionByDate(String date);
	/**
	 * 查询遗漏表数据  num是最大查询数量
	 */
	public List<GfAhPick11GaOmit> findGfAhPick11GaOmitList(String string,
			List<Object> para, int num);
}
