package com.gf.dcb.dao;

import java.util.List;

import com.gf.dcb.model.GfDcbGaOmit;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.model.GfDcbGaTrend;
import com.gf.dcb.model.dto.GfDcbDTO;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.gf.pick11.gdpick11.model.GfGdPick11GaOmit;

public interface IDcbDAO  extends IDAO {

	/**
	 * 根据系统当前时间获取当前场次
	 * @return
	 */
	public GfDcbGaSession getCurrentSession();

	/**
	 * 根据期号获取场次相关信息
	 * @param sessionNo
	 * @return
	 */
	public GfDcbGaSession getPreviousSessionBySessionNo(String sessionNo);

	/**
	 * 冷热排行榜列表
	 * @return
	 */
	public List<GfDcbGaTrend> findGaTrendList();

	/**
	 * 开奖列表
	 * @return
	 */
	public PaginationSupport findGfDcbGaSessionList(String hsql,List<Object> para,int pageNum, int pageSize);

	public List<GfDcbGaSession> getCurrentSession(int start, int end);
	
	/**
	 * 查询北京3分彩投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGfDcbGaBetList(String hql, List<Object> para,
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
	 * 根据id查询投注详情
	 * @param hql
	 * @param para
	 * @return
	 */
	public List<GfDcbDTO> findGaBetDetailById(String hql, List<Object> para);

	public GfDcbGaOmit getGfDcbGaOmitBySessionNo(String sessionNo);

	/**
	 * 查询走势  num是最大查询条数
	 * @param hqls
	 * @param para
	 * @param num
	 * @return
	 */
	public List<GfDcbGaOmit> findGfDcbGaOmitList(String hqls,
			List<Object> para, int num);
	
}
