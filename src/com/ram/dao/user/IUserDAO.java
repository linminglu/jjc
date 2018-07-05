package com.ram.dao.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.apps.model.UserTradeDetail;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.UserBetCount;
import com.game.model.dto.GaDTO;
import com.ram.model.DeskRegister;
import com.ram.model.DeskSoftware;
import com.ram.model.DeskSoftwareVersion;
import com.ram.model.DeskTrial;
import com.ram.model.IpRecord;
import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.MobileTrial;
import com.ram.model.OnLineUserRecord;
import com.ram.model.Tutor;
import com.ram.model.User;
import com.ram.model.UserLimit;

public interface IUserDAO extends IDAO{
	public PaginationSupport findUserLog(String hsql,List para,int startIndex,int pageSize);
	public boolean saveUser(User user, User operUser) throws DataAccessException;
	public boolean deleteUser(int[] id, User user) throws DataAccessException;
	public Manager getManager(int userId) throws DataAccessException;
	public Tutor getTeacher(int id) throws DataAccessException;
	public void modifyUserStatus(int id, String status, User user) throws DataAccessException;
	public List getAllTutors(int startIndex,int pageSize);
	public int getTutorsNumber();
	public PaginationSupport findAllTutors(int startIndex,int pageSize, DetachedCriteria detachedCriteria);
	public User getUser(String name, String password);
	public User getUser(Integer originId,Integer originUserId);
	/**
	 * 获取有效的用户（status=1）
	 * @param userId
	 * @return
	 */
	public User getValidUser(Integer userId);
	public User getUser(int userId);
	public Integer getUser(String loginName);
	public User getUserByLoginName(String loginName);
	public User findUserByLoginName(String loginName);
	
	public User getUserByloginComm(String accoutName,String password);//根据账号(登录名或邮箱)和密码查询用户
	public User getUserByloginCommSj(String accoutName,String password);
	
	public User getUserByActCode(String actCode);
	public Integer getUserId(String userName);
	public boolean checkUserExist(String loginName);
	public boolean checkUserExist(Integer uid);
	public User  findUserByloginName(String loginName);
	public List findAllTutorsByCourseId();
	public Query findAllCenterTutorsByTcId(int tcId,int courseId,String tutorType,int semesterId);
	public List findAllTutorsByTcId(int tcId);
	public String getMaxStudyNumber(String str);
	public Integer getTutorIdByUserId(Integer userId);
	public Integer getLearnerIdByStudyNumber(String studyNumber);
	public Learner getLearnerInfoByStudyNumber(String studyNumber);
	
	/**
	 * 检查修改后的用户名和其他用户的用户名是否冲突
	 */
	public boolean checkUserExistAgain(String loginName, Integer userId);
	public Learner getLearnerInfoByLoginName(String loginName);
	public List findTopnUsersByLoginTimes(int offset, int pageSize, String userType);
	
	public User getUserByV3UserId(int v3UserId);
	
	
	public List findAllLearner();
	
	/**
	 * 根据条件查询学员总数
	 * @author Lu Congyu
	 * @date 06/06/05
	 * @param where
	 * @param list
	 * @return
	 */
	public int findLearner(String where, List list);
	
	/**
	 * 根据条件查询学员信息
	 * @author Lu Congyu
	 * @date 06/06/05
	 * @param offset
	 * @param rows
	 * @param where
	 * @param list
	 * @return
	 */
	public List findLearner(int offset, int rows, String where, List list);
	
	/**
	 * 根据tutor得到用户的信息
	 * @param tutorId
	 * @return
	 */
	public User getUserByTutorId(int tutorId);
	
	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public Query findAllTutorsByTcId(int tcId,int courseId);
	
	/**
	 * @author Lu Congyu
	 * @date 06/09/29
	 * @param where
	 * @param list
	 * @return
	 */
	public int findAllLearner(String where, List list);
	
	/**
	 * @author Lu Congyu
	 * @date 06/09/29
	 * @param offset
	 * @param rows
	 * @param where
	 * @param list
	 * @return
	 */
	public List findAllLearner(int offset, int rows, String where, List list);
	
	/**
	 * 入学注册状态，1=进修生 => 2=未入学注册的正式生
	 * @author Lu Congyu
	 * @date 06/09/29
	 * @param learnerId
	 */
	public void updateLearnerEnrollRegisterStatus(Integer learnerId);
	
	/**
	 * 入学注册状态，2=未入学注册的正式生 => 3=已入学注册正式学生
	 * @author Lu Congyu
	 * @date 06/09/29
	 * @param regDate
	 * @param learnerId
	 */
	public void updateLearnerEnrollRegisterStatus(String regDate, List learnerId);
	
	/**
	 * 通过用户名查找所有用户人数
	 */
	public int getALLByLoginNameNum( String loginName );
	
	/**
	 * 根据学籍号查询学生
	 * @param studyNumber
	 * @return
	 */
	public User getUserByStudyNumber(String studyNumber);
	
	/**
	 * 根据报名号查询学生
	 * @param enrollNumber
	 * @return
	 */
	public User getUserByEnrollNumber(String enrollNumber);
	
	/**
	 * 记录登录用户人数
	 * @param serverName
	 * @param userRecordNumber
	 * @param currentTime
	 */
	public void saveOnlineUserRecord( OnLineUserRecord onlineUserRecord,User user );
	
	/**
	 * 统计登录人数
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List findOnlineUserRecord( int offset,int rows );
	public int getOnlineUserCount();
	
	/**
	 * 查询被推荐的人
	 * @param str
	 * @param list
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List findRecommendedUser(String str,List list,int startIndex,int pageSize);
	public int getRecommendedUserNum(String str,List list);
	
	public DeskRegister getDeskRegister(String sn);
	public DeskRegister getDeskRegisterByCodeMac(String code, String mac);
	public DeskRegister getDeskRegisterByCodeLike(String code, String whereSql);
	public boolean checkDeskRegisterSNOk(String sn);
	public List<DeskSoftware> findDeskSoftwareList(String where);
	public DeskSoftware getDeskSoftware(Integer softwareId);
	public DeskSoftware getDeskSoftware(String softwareCode);
	public DeskTrial getDeskTrial(String softwareCode,String mac);
	public MobileTrial getMobileTrial(String softwareCode,String sn);
	public DeskSoftwareVersion getDeskSoftwareVersion(String softwareCode,String v);
	public void updateDeskSoftwareTrialCount(String softwareCode,int step);
	public boolean checkDeskRegisterMacOnly(String mac,String softwareCode);
	public Integer getDeskRegisterSell(String sntype);
	public Integer getDeskRegisterSell2(String sntype);
	public Integer getDeskRegisterSellOfUser(String sntype,Integer userId);
	public Integer getDeskRegisterSellJoin(String sntype);
	public Integer getDeskRegisterSellBySql(String hsql,List<Object> pars);
	public Integer getDeskRegisterSellBySqlJoin(String hsql,List<Object> pars);
	/**
	 * 获得用户列表
	 * 
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findUser(String hqls, List<Object> para,
			int startIndex, int pageSize);
	public boolean checkUserEmailExist(String email);
	public User saveObjectDB(User regUser);
	public void delUserAbsolute(Integer userId, User operator);
	
	
	public List<String> findAllToken();
	
	public User findUserByItCode(String code);
	/**
	 * 通过userId 查订单
	 */
//	public List<BuyOrder> findBuyOrders(Integer userId);
//	public List<EatOrder> findEatOrders(Integer userId);
//	public List<StoreOrder> findStoreOrders(Integer userId);
	
	//
//	public List<Bet> findBetOfUser(Integer userId,Date endDateTime);
//	public List<CjUserBet> findCjUserBetOfUser(Integer userId,String cjtype,Date endDateTime);
	public User getUser(Integer orgType,String name);
	public Integer countSumRechargePoints(String rechargeType,Integer betSessionId);
	/**
	 * 修改用户积分
	 */
	public void modifyBalance(Integer userId,BigDecimal userpoints,String operateType);
	public void updateBalance(BigDecimal userpoints,Integer userId);
	
	/**
	 * 余额明细
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findUserTradeDetail(String hql, List<Object> para,
			int pageNum, int pageSize);
	
	/**
	 * 用户投注列表
	 * @param hql
	 * @param para
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findBetList(String hql, List<Object> para,
			int pageNum, int pageSize);
	
	/**
	 * 根据id查询代理
	 * @param agentId
	 * @return
	 */
	public User getAgentById(Integer agentId);
	/**
	 * 查找用户列表
	 * @param hqls
	 * @param para
	 * @return
	 */
	public List<User> findUserList(String hqls, List<Object> para);
	/**
	 * 统计更新用户实时余额sum()方式
	 * 一个是批量，一个是单个
	 * @param userId
	 * @return
	 */
	public void updateUserMoney(List<Integer> userIds);
	public void updateUserMoney(List<Integer> userIds,String type);
	/**
	 * 更新交易明细表用户投注记录状态
	 * @param sessionNo
	 * @param gameType
	 * @param status 
	 */
	public void updateUserTradeDetailStatus(String sessionNo, String gameType, String status);
	public void updateUserMoney(Integer userId);
	public BigDecimal updateUserBanlance(Integer userId);
	public void updateUserPoints(List<Integer> userIds);
	public void updateUserPoints(Integer userId);
	public void updateUserMoneyAndBetMoney(Integer userId);
	public void updateUserMoneyAndBetMoney(List<Integer> userIds);
	
	
	/**
	 * 更新用户累计充值金额
	 * @param userId
	 */
	public void updateUserAddUpRechargeMoney(User user);
	
	
		/**
	 * 查询UserTradeDetail表  统计余额
	 * @return
	 */
	public BigDecimal getSumCashMoneyByUserId(Integer userId);
	
	/**
	 * 获取用户此时间段内对应类型的交易总额
	 * UserTradeDetail表
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @param cashType 交易类型
	 * @return
	 */
	public BigDecimal getSumCashMoneyByDate(Integer userId, String startDate, String endDate, String cashType);
	
	
	
	/**
	 *  打码返水之后 更新 返水的状态 该条记录已给自己已返水
	 * @param userBetCount
	 */
	public void updateUserBetCountPersonStatus(UserBetCount userBetCount,String status);
	/**
	 * 打码返水之更新 返水的状态  该条记录代理已返水
	 * @param userBetCount
	 */
	public void updateUserBetCountAgentStatus(UserBetCount userBetCount);
	
	/**
	 * 根据ip获取该ip已经注册次数
	 * @param ip
	 * @return
	 */
	public IpRecord getIpRecordByIp(String ip);
	/**
	 * 更新用户单个字段
	 * @param property 字段名称 
	 * @param obj  值
	 * @param userId
	 */
	public void updateUserProperty(String property, Object obj,Integer userId);

	/**
	 * 英雄榜
	 */
	public PaginationSupport findHeroicList(String hql, List<Object> para,
			int pageNum, int pageSize);
	
	/**
	 * 更新今日输赢
	 * @param userIds
	 * @return
	 */
	public void updateUserBalanceMoney(List<Integer> userIds);
	/**
	 * 统计用户交易记录
	 * @return
	 */
	public List<GaDTO> findUserTradeDetailList(String hql,List<Object> para);
	/**
	 * 统计用户盈亏
	 * @param hsql
	 * @param para
	 * @return
	 */
	public List<GaDTO> findUserPayoffList(String hsql, List<Object> para);
	/**
	 * 统计代理的下级会员
	 * @param hsql
	 * @param para
	 * @return
	 */
	public List<GaDTO> findUserSubmemberList(String hsql, List<Object> para);
	
	/**
	 * 统计用户交易记录
	 * @param hql4
	 * @param para4
	 * @return
	 */
	public List<GaDTO> findSumUserTradeDetail(String hql4, List<Object> para4);
	
	/**
	 * 统计所有用户盈亏总计
	 * @param hsql
	 * @param para
	 * @return
	 */
	public BigDecimal findSumUserPayoff(String hsql, List<Object> para);
	/**
	 * 获取用户当日充值金额
	 * @param uid
	 * @return
	 */
	public BigDecimal getDayRecharge(Integer uid);
	/**
	 * 获得用户列表
	 * 
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findUserLimit(String hqls, List<Object> para,
			int startIndex, int pageSize);
	public UserLimit findUserLimitByUid(Integer uid);
	public BigDecimal getDayBetMoney(Integer userId);
	
	public BigDecimal getUserBetMoneyFromLastCash(Integer userId);
	public UserTradeDetail getUserLastRecharge(Integer userId);

	public BigDecimal getUserRechargeFromLastCash(Integer userId);
	

}
