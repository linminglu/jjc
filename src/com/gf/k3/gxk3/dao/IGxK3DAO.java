package com.gf.k3.gxk3.dao;

import java.math.BigDecimal;
import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.gf.k3.gxk3.model.GfGxK3GaOmit;
import com.gf.k3.gxk3.model.GfGxK3GaSession;
import com.gf.k3.gxk3.model.GfGxK3GaTrend;
import com.gf.k3.gxk3.model.dto.GfGxK3DTO;

public interface IGxK3DAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfGxK3GaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfGxK3GaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfGxK3GaTrend> findGfGxK3GaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGfGxK3GaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGfGxK3GaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<GfGxK3DTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 查询遗漏
	 */
	public GfGxK3GaOmit getGxK3OmitBySessionNo(String sessionNo);
	/**
	 * 根据日期查找日期中的第一期
	 */
	public GfGxK3GaSession getFirstSessionByDate(String date);
	/**
	 * 根据日期查找日期中的最后一期
	 */
	public GfGxK3GaSession getEndSessionByDate(String date);
	/**
	 * 查询遗漏表数据  num是最大查询数量
	 */
	public List<GfGxK3GaOmit> findGfGxK3GaOmitList(String string,
			List<Object> para, int num);
	
	/**
	 * 根据开奖结果的和值，查找该和值的赔率
	 * @param hezhi
	 * @return
	 */
	public BigDecimal getHeZhiBetRate(String hezhi);
}
