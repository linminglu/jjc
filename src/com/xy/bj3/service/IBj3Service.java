package com.xy.bj3.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.apps.eff.dto.SessionItem;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.bj3.model.Bj3GaSession;
import com.xy.bj3.model.Bj3GaTrend;
import com.xy.bj3.model.dto.Bj3DTO;
/**
 * 北京三分彩
 * @author admin
 */
public interface IBj3Service  extends IService {

	/**
	 * 初始化彩票场次
	 * 参数 i=0 为今天。i=1 为明天。 i=2 为后天。
	 */
	public String updateInitSession(int num);
	
	/**
	 * 抓取官网开奖数据并更新本地数据，同时开奖
	 */
	public String updateFetchAndOpenResult(Map<String, SessionItem> sessionNoMap);

	/**
	 * 开奖方法，计算所有投注用户的结果并更新相关数据和状态
	 * @param session
	 * @param result 开奖号5组数字 英文逗号连接 [1,2,3,4,5]
	 */
	public boolean openBj3SessionOpenResultMethod(Bj3GaSession session,String result);
    
	/**
	 * 获取当前期号，根据当前时间从数据库查询
	 * @return
	 */
	public Bj3GaSession getCurrentSession();
	
	/**
	 * 获取当前期前start期后end期。例如start=10，end=10，(-10,10] 则查询当前期的10期与后10期，不包过前第10期，报告后第10期
	 * @return
	 */
	public List<Bj3GaSession> findSessionNoByCurrent(int start,int end);

	/**
	 * 跟据期号获取这期相关信息
	 * @param string
	 * @return
	 */
	public Bj3GaSession getPreviousSessionBySessionNo(String sessionNo);

	/**
	 * 冷热排行榜列表
	 * @return
	 */
	public List<Bj3GaTrend> findBj3GaTrendList();

	/**
	 * 获取开奖结果列表
	 * @param string
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBj3GaSessionList(String hql,
			List<Object> para, int pageNum, int pageSize);
	
	/**
	 * 保存用户投注信息
	 */
	public User saveUserBetInfo(String room, Map<String, Integer> betMap,
			List<GaBetOption> list, Bj3GaSession ga3Session, User user,
			BigDecimal betAll);

	/**
	 * 更新游戏冷热排行榜
	 */
//	public void updateGaTrend();
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveOpenResult(Bj3GaSession session,String openResult);
	
	/**
	 * 保存单期开奖结果
	 */
	public boolean saveAndOpenResult(Bj3GaSession session,String openResult);
	
	/**
	 * 获取投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBj3GaBetList(String hql,
			List<Object> para, int pageNum,int pageSize);
	
	/**
	 * 获取投注详细信息
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findGaBetDetail(String hql,
			List<Object> para, int pageNum, int pageSize);

	/**
	 * 根据id查询投注详情
	 * @param string
	 * @param para
	 * @return
	 */
	public List<Bj3DTO> findGaBetDetailById(String string, List<Object> para);
	
	/**
	 * 派彩错误，撤回已经派彩的金额
	 * @param session
	 * @return
	 */
	public boolean saveRevokePrize(Bj3GaSession session);
}
