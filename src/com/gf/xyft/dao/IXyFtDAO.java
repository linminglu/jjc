package com.gf.xyft.dao;

import java.math.BigDecimal;
import java.util.List;

import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.gf.xyft.model.GfXyFtGaOmit;
import com.gf.xyft.model.GfXyFtGaSession;
import com.gf.xyft.model.GfXyFtGaTrend;
import com.gf.xyft.model.dto.GfXyFtDTO;

public interface IXyFtDAO extends IDAO {

	/**
	 * 获取当前场次，根据系统时间从数据库查询
	 * @return
	 */
	public GfXyFtGaSession getCurrentSession();
	/**
	 * 根据期号获取当前期
	 * @param sessionNo
	 * @return
	 */
	public GfXyFtGaSession getPreviousSessionBySessionNo(String sessionNo);
	/**
	 * 冷热排行列表
	 * @return
	 */
	public List<GfXyFtGaTrend> findGfXyFtGaTrendList();
	/**
	 * 开奖列表
	 */
	public PaginationSupport  findGfXyFtGaSessionList(String hql, List<Object> para,int pageNum,int pageSize);
	/**
	 * 统计每期开奖盈亏
	 */
	public PaginationSupport  findGfXyFtGaBetList(String hql, List<Object> para,int pageNum,int pageSize);
	
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
	public List<GfXyFtDTO> findGaBetDetailById(String hql, List<Object> para);
	
	/**
	 * 查询遗漏
	 */
	public GfXyFtGaOmit getXyFtOmitBySessionNo(String sessionNo);
	/**
	 * 根据日期查找日期中的第一期
	 */
	public GfXyFtGaSession getFirstSessionByDate(String date);
	/**
	 * 根据日期查找日期中的最后一期
	 */
	public GfXyFtGaSession getEndSessionByDate(String date);
	/**
	 * 查询遗漏表数据  num是最大查询数量
	 */
	public List<GfXyFtGaOmit> findGfXyFtGaOmitList(String string,
			List<Object> para, int num);
	
	/**
	 * 根据开奖结果的和值，查找该和值的赔率
	 * @param hezhi
	 * @return
	 */
	public BigDecimal getHeZhiBetRate(String hezhi);
}
