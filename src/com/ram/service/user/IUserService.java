package com.ram.service.user;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import com.apps.model.LotterySettingRl;
import com.apps.model.UserTradeDetail;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.IService;
import com.game.model.GaBetDetail;
import com.game.model.UserBetCount;
import com.game.model.UserLevel;
import com.game.model.dto.GaDTO;
import com.ram.model.DeskRegister;
import com.ram.model.DeskSoftware;
import com.ram.model.DeskSoftwareVersion;
import com.ram.model.IpRecord;
import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.OnLineUserRecord;
import com.ram.model.RecommendedUserInfo;
import com.ram.model.Tutor;
import com.ram.model.User;
import com.ram.model.UserAndLearnerMainInfo;
import com.ram.model.UserGroup;
import com.ram.model.UserGroupRl;
import com.ram.model.UserLimit;
import com.ram.model.UserLog;
import com.ram.model.dto.LearnerDTO;
import com.ram.model.dto.UserMoneyDTO;
import com.ram.web.user.form.ManagerForm;

public interface IUserService extends IService {

	public PaginationSupport findUserLog(String hsql,List para,int startIndex,int pageSize);
	
	/**
	 * 创建一个用户	 * @param User
	 */
	public void saveUser(User user, User operaUser);
	public void saveUser(User user);
	public void saveReMoneyUser(User user, User operaUser);
	public void saveDdCodeMoney(User user,User user1,String itCode);
	
	/**
	 * 创建或者修改一个用户组
	 * @param User
	 */
	public int saveUserGroup(UserGroup userGroup, User user);
	
	/**
	 * 删除一个用户组
	 * @param User
	 */
	public void deleteUserGroup(int userGroupId, User user);
	
	
	/**
	 * 检索出所有用户组
	 * @param User
	 */
	public List getAllUserGroup();
	
	/**
	 * 根据用户组ID获得用户组的信息
	 * @param User
	 */
	public UserGroup getUserGroupByID(int userGroupID);
	
	/**
	 * 保存tutor对象
	 */ 
	public Tutor saveTutor(Tutor tutor, User user) throws DataAccessException;
	
	/**
	 * 根据userId获得一个学生对象
	 */
	public Learner getLearnerByUserId(int userId) throws DataAccessException;
	
	/**
	 * 修改一个用户状态
	 * @param User
	 */
	public void modifyUserStatus(int id, String status, User user);
	
	/**
	 * 删除一个用户
	 * @param User
	 */
	public void deleteUser(int[] id, User user);
	
	/**
	 * 获得一个用户信息
	 * @param User
	 */
	public Manager getManager(int userId);

	/**
	 * 通过主键获得一个教师的信息
	 * @param id
	 * @return
	 */
	public Tutor getTutor(int id);
	
	/**
	 * 获得Tutor对象列表(分页)
	 */
	public PaginationSupport findALLTutorForPage(int startIndex,int pageSize);
	
	/**
	 * 根据查询条件获得Tutor对象列表(分页)
	 */
	public PaginationSupport findALLTutorForPage(int startIndex,int pageSize, DetachedCriteria detachedCriteria);
	
	public List findAllCenterTutorsByTcId(int tcId, int courseId,String tutorType,int semesterId);
	
	/**
	 * 建立用户和用户组之间的关系
	 */
	public boolean saveUserGroupRl(UserGroupRl userGroupRl, User user);
	
	/**
	 * 根据用户ID获得用户所在的用户组
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getUserGroupById(int userId) throws DataAccessException;
	
	/**
	 * 根据用户ID获得用户可以添加的用户组
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getAvailableUserGroupById(int userId) throws DataAccessException;
	
	/**
	 * 建立用户和用户组之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void addUserAndGroupRl(int userId, int[] userGroupId, User user) throws DataAccessException;
	
	/**
	 * 通过学习中心获得所有教师对象
	 */ 
	
	public List findAllTutorByTcId(Integer tcId);
	
	/**
	 * 获得在tutor里的用户
	 * @author Lu Congyu
	 * @date 06/07/12
	 * @param tcId
	 * @return
	 */
	public List findUserInTutorByTcId(Integer tcId);
	
	/**
	 * 获得是论文评定的教师用户
	 * @return
	 */
	public List findTutorByIsAppraise();
	
	/**
	 * 删除用户和用户组之间的关系
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndGroupRl(int userId, int userGroupId, User user) throws DataAccessException;
	
	/**
	 * 创建或者保存一个学生
	 * @param learner
	 * @throws DataAccessException
	 */
	public void saveLearner(Learner learner, User user) throws DataAccessException;
	
	/**
	 * 根据学生ID获得一个学生对象
	 * @param learnerId
	 * @return
	 * @throws DataAccessException
	 */
	public Learner getLearner(int learnerId) throws DataAccessException;
	
	/**
	 * 修改学生的学籍状态
	 * @param learnerId
	 * @param status
	 * @throws DataAccessException
	 */
	public void modifyLearnerStatus(int learnerId, int status, User user) throws DataAccessException;
	
	/**
	 * 获得所有学生信息(分页)
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport getAllLearners(int startIndex,int pageSize);
	/**
	 * 根据课程ID获得教师
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAllTutorsByCourseId();
	/**
	 * 根据课程ID和教师ID获得教师
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAllTutorsByTcId(int tcId);
	/**
	 * 根据查询条件获得学生信息(分页)
	 * @param startIndex
	 * @param pageSize
	 * @param detachedCriteria
	 * @return
	 */
	public PaginationSupport findAllLearners(int startIndex,int pageSize, DetachedCriteria detachedCriteria);
	
	/**
	 * 根据用户名和密码取得用户信息
	 * @return
	 */
	public User getUser(String name, String password);/**
	public User getUser(Integer originId,Integer originUserId);
	
	/**
	 * 根据账号(登录名或邮箱)和密码查询用户
	 * @param accoutName
	 * @param password
	 * @return
	 */
	public User getUserByloginComm(String accoutName,String password);//根据账号(登录名或邮箱)和密码查询用户
	public User getUserByloginCommSj(String accoutName,String password);//根据账号(登录名或邮箱)和密码查询用户
	
	/**
	 * 根据用户名取得用户信息
	 * @return
	 */
	public Integer getUser(String loginName);
	public User getUserByLoginName(String loginName);
	public User findUserByLoginName(String loginName);
	public User getUserByActCode(String actCode);
	
	/**
	 * 检查用户名是否有重复
	 */
	public boolean checkUserExist(String loginName);
	public boolean checkUserExist(Integer uid);
	
	public User findUserByItCode(String code);
	
	public User getUser(int userId);
	
	/**
	 * 根据用户姓名取得用户ID
	 * @return
	 */
	public Integer getUserId(String userName);
	
	/**
	 * 根据学籍号获得learnerId
	 * @param studyNumber
	 * @return
	 */
	public Integer getLearnerIdByStudyNumber(String studyNumber);
	
	/**
	 * 根据学籍号获得learner
	 * @param studyNumber
	 * @return
	 */
	public Learner getLearnerByStudyNumber(String studyNumber);
	
	
	public Learner getLearnerByLoginName(String loginName);
	
	/**
	 * 通过用户ID得到LearnerDTO对象
	 * @param userId
	 * @return LearnerDTO
	 */
	public LearnerDTO getLearnerDTOByUserId(int userId);
		
	/**
	 * 根据用户ID获得TutorID
	 * @param userId
	 * @return
	 */
	public Integer getTutorIdByUserId(Integer userId);
	
	public Tutor getTutors(Integer tutorId);
	/**
	 * 根据用户ID得到中心ID
	 * 
	 * @param userId
	 * @return
	 */
	public Integer getTcIdByUserId(Integer userId);
	
	/**
	 * 根据learnerID获得learner
	 * @param userId
	 * @return
	 */
	public Learner getLearnerByLearnerId(Integer learnerId);
	
	/**
	 * 根据年度＋季度＋层次＋中心获得学籍号 
	 * @param year
	 * @param season
	 * @param program
	 * @param tcCenter
	 * @return
	 */
	public String getGeneratedStudyNumber(String year, String season, String program, int tcId);
	
	/**
	 * 根据查询条件获得学生信息
	 */
	public List findLearnersByCriteria(final DetachedCriteria detachedCriteria);
	
	/**
	 * 根据学习中心获得所有教师对象
	 */ 
	public List findAllTeachers();
	
	/**
	 * 获得所有教师对象
     */
	public List findAllTutors();
	/**
	 * 获得所有教师对象
     */ 
	public List findTeachers();
	
	/**
	 * 检查修改后的用户名和其他用户的用户名是否冲突
	 */
	public boolean checkUserExistAgain(String loginName, Integer userId);
	
	/*
	 * 根据UserId获得学生的基本信息UserAndLearnerMainInfo
	 */
	public UserAndLearnerMainInfo getUserAndLearnerMainInfo(Integer userId);
	/*
	 * 获得未在可预约用户表中的在学学生列表
	 */
	public List findLearnersCanExamAndNotInTable();
	
	
	/**
	 * 管理员登录排行
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnManagerByLoginTimes(int offset, int pageSize);

	
	/**
	 * 学生登录排行
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnStudentsByLoginTimes(int offset, int pageSize);

	/**
	 * 老师登录排行
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnTutorsByLoginTimes(int offset, int pageSize);
	
	/**
	 * 判断一个人是否是总部管理员
	 * @param userId
	 * @return
	 */
	public boolean isHeadQuarterManager(int userId);
	public boolean isAdminManager(int userId);
	
	/**
	 * 判断一个人是否是当地中心管理员
	 * @param userId
	 * @return
	 */
	public boolean isLocalCenterManager(int userId);
	
	public boolean IsLocalCenterManagerOfTutorCenter(int objectTcId,int managerId);
	
	public int getTcId_Of_User(User user);
	
	/**
	 * 学生查询结果总数
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @param where
	 * @param list
	 * @return
	 */
	public int studyQuery(String where, List list);
	
	/**
	 * 学生查询结果
	 * @author Lu Congyu
	 * @date 06/05/26
	 * @param offset
	 * @param rows
	 * @param where
	 * @param list
	 * @return
	 */
	public List studyQuery(int offset, int rows, String where, List list);
	
	
	public int translateV3UserIdToV4UserId(int v3UserId);
	
	/**
	 * 根据条件查询学员总数
	 * @author Lu Congyu
	 * @date 06/06/05
	 * @param where
	 * @param list
	 * @return
	 */
	public int findLearner(String where, List list);
	
	
	public List findAllLearner();
	
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
	 * 通过课程ID获得所有教师对象
	 */ 
	public List findAllTutorByCourseId(Integer courseId,Integer semesterId);
	
	/**
	 * 通过课程ID和学习中心id获得所有教师对象
	 */ 
	public List findAllTutorByCourseIdAndTcId(Integer scheduleCourseId,Integer tcId,Integer semesterId);
	
	/*
	 * 获得所有在学学生列表
	 */
	public List findStudyLearners();	
	
	/**
	 * 根据tutor得到用户的信息
	 * @param tutorId
	 * @return
	 */
	public User getUserByTutorId(int tutorId);
	
	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public List findAllTutorsByTcId(int tcId,int courseId);
	
	/**
	 * 查询用户组中的用户
     * @param userGroupId
	 * @throws DataAccessException
	 */
	public List findUserOfUserGroup(Integer userGroupId);
	
	/**
	 * 根据在线用户列表，得到在线用户的OnlineUserDTO列表
	 * @param onlineUsers
	 * @return
	 */
	public List findOnlineUserDTOs(List onlineUsers);
	
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
	 * 判断用户是否属于助学中心的用户组
	 * @param userId
	 * @return boolean
	 */
	public boolean getUserGroupByUserId(Integer userId) throws Exception;
	
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
	 * 保存推荐人
	 * @param rui
	 * @param user
	 */
	public void saveRecommendedUserInfo(RecommendedUserInfo rui,User user);
	
	/**
	 * 删除RecommendedUserInfo
	 * @param id
	 */
	public void delRecommendedUserInfo(Integer id,User user);
	
	public void saveUserLog(UserLog userLog);
	
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
	
	public String createDeskSerialNumber(int len,String softwareCode,String sntype);
	public String createDeskSN(String softwareCode,String sntype,String sntaobao,String sellprice,String status,String remarks,int count,Integer userId);
	public String updateDeskRegister(String code,String sn,String mac,String v,String ip);
	public String updateDeskNewRegister(String code,String sn,String mac,String v,String ip);
	public String updateAndCheckDeskRegister(String code,String sn,String mac,String v,String ip);
	public String updateAndCheckDeskVerify(String code,String sn,String mac,String v,String ip);
	public String updateAndCheckDeskLaunch(String code,String sn,String mac,String v,String ip);
	public String updateMobileLaunch(String code,String sn,String mac,String v,String ip);
	public String updateAndCheckDeskTrialVersion(String code,String sn,String mac,String v,String ip);
	public String updateMobileTrial(String code,String sn,String mac,String v,String ip);
	public DeskRegister getDeskRegister(String sn);
	public String getReturnData(String returnStatus,String returnData);
	public List<DeskSoftware> findDeskSoftwareList(String where);
	public Integer getDeskRegisterSell(String sntype);
	public Integer getDeskRegisterSell2(String sntype);
	public Integer getDeskRegisterSellOfUser(String sntype,Integer userId);
	public Integer getDeskRegisterSellJoin(String sntype);
	public Integer getDeskRegisterSellBySql(String hsql,List<Object> pars);
	public Integer getDeskRegisterSellBySqlJoin(String hsql,List<Object> pars);
	
	public DeskSoftwareVersion getDeskSoftwareVersion(Integer id);
	public DeskSoftware getDeskSoftware(String softwareCode);
	public void saveDeskSoftwareVersion(DeskSoftwareVersion dsv);
	public void saveDeskRegister(DeskRegister dr);
	
	public void updateDeskRegisterLog(DeskRegister dr,String returnStatus,String mac,String v,String ip,String code);
	
	//////////////////////////
	public String getGenTestPaperList();
	public String getGenTestPaperXml(String pid);
	
	///////////////////////////
	public void updateUserPoints(Integer userId,BigDecimal points);
	public User updateUserPointsAndReturn(Integer userId,BigDecimal points);
	public String updateDeskTrialData(Integer deskTrialId,String usestatus);
	
	public String updateAndGetAutoSN(String code, String sn, String sntype,
			String status, String mac, String v, String ip);
	public String updateAndGetFreeSN(String code, String sn, String sntype,
			String status, String mac, String v, String ip);
	
	public User updateDeskUserregister(String sn);
	
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
	/**
	 * 查询用户列表
	 */
	public List<User> findUserList(String hqls, List<Object> para);
	public PaginationSupport findUserList(String userName, String userType, String startDate,
			String endDate, int startIndex, int pageSize);
	public boolean checkUserEmailExist(String email);

	public User saveUserRegister(User userForm);
	public User saveUserReg(User user,BigDecimal points);

	public void delUserAbsolute(Integer userId, User user);

	/**
	 * 修改头像
	 * @param user
	 * @param logoSrc
	 * @param logoMiniSrc
	 */
	public void modifyLogo(User user, String logoSrc, String logoMiniSrc);
	
	/**
	 * 保存设备token
	 * @param deviceToken
	 * @param appversion
	 * @param ip
	 * @return
	 */
	public String updateDeviceToken(String deviceToken,String appversion,String ip);
	public List<String> findAllToken();
	
	public void updateUserLog(HttpServletRequest request,User u, String loginText);
	/**
	 * 保存用户信息，如果是商家用户，也会保存商家信息
	 * @param frm
	 */
	public User saveUser(ManagerForm frm);
	/**
	 * 保存用户信息，如果是商家用户，也会保存商家信息
	 * @param frm
	 */
	public User saveUser(ManagerForm frm,User loginUser);
	/**
	 * 保存用户信息 小区管理员信息
	 * @param frm
	 * @return
	 */
	public User saveUserCommunity(ManagerForm frm);
	/**
	 * 通过userId 查订单
	 */
//	public List<BuyOrder> findBuyOrders(Integer userId);
//	public List<EatOrder> findEatOrders(Integer userId);
//	public List<StoreOrder> findStoreOrders(Integer userId);
	
	//
	public boolean checkUserAccoutValid(Integer userId);
	/**
	 * 检查用户权限级别
	 * @param userId
	 * @return
	 */
	public String checkUserPermissionLevel(Integer userId);
	/**
	 * 积分明细保存表，所有牵涉到用户积分明细情况都调用此方法 
	 * @param userId 操作用户
	 * @param type 积分类型
	 * @param points 积分值
	 * @param infoId [关联信息id]
	 * @param comments [备注]
	 * @param beUserId [被操作用户用于给别人充值的情况]
	 */
	public void updateMytUserDetailOfPublic(Integer userId,String type,BigDecimal points,Integer infoId,String comments,Integer beUserId);
	
	public Integer countSumRechargePoints(String rechargeType,Integer betSessionId);
	//
	//
	//检查用户的积分是否免发布新产品的积分
	public boolean checkUserPointsEnough(Integer userId,BigDecimal comparePoints);
	public User getUserValidById(int userId);
	public BigDecimal getUserPoints(Integer userId);
	public void savetbMessageCount(String cellPhone);
	
	/**
	 * 更新用户余额并且保存交易明细
	 * </br><b>调用此方法时不需要再对用户余额进行操作</b>
	 * @param userId 用户id
	 * @param tradeType 收支类型 1.收入 2.支出
	 * @param cashType 1.在线充值 2.卡充 3.余额支付 4.分销佣金 
	 * @param cashMoney 金额
	 * @param regId 引用数据id <em>可选</em>
	 * @return User 返回最新的用户信息
	 */
	public User saveTradeDetail(User user,Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId,String gameType,String remark);
	public User saveTradeDetail(Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId);
	
	/**
	 * 投注使用 保存明细
	 *  @param userId
	 * @param tradeType
	 * @param cashType
	 * @param cashMoney
	 * @param refId
	 * @param gameType
	 * @param remark
	 * @param sessionNo
	 * @param type usertype
	 * @param loginName
	 */
	public Integer saveTradeDetail(User user,Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId,String gameType,String remark, String sessionNo,String type,String loginName);
	
	/**
	 * 用户投注时，更新用户限制提现的信息（具体看user.java里limitBetBack的注释）
	 * @param user
	 * @param cashType
	 * @param cashMoney
	 */
	public User updateLimitBetBack(User user, String cashType, BigDecimal cashMoney);
	

	
//	/**
//	 * 更新用户余额并且保存交易明细
//	 * </br><b>调用此方法时不需要再对用户余额进行操作</b>
//	 * @param userId 用户id
//	 * @param tradeType 收支类型 1.收入 2.支出
//	 * @param cashType 1.在线充值 2.卡充 3.余额支付 4.分销佣金 
//	 * @param cashMoney 金额
//	 * @param regId 引用数据id <em>可选</em>
//	 * @return User 返回最新的用户信息
//	 */
//	public User saveTradeDetail(Integer userId,String tradeType,String cashType,BigDecimal cashMoney,Integer regId);
//	
//	/**
//	 * 更新用户余额并且保存交易明细
//	 * @param user
//	 * @param userId
//	 * @param tradeType
//	 * @param cashType
//	 * @param cashMoney
//	 * @param regId
//	 * @return
//	 */
//	public User saveTradeDetail(User user,Integer userId,String tradeType,String cashType,BigDecimal cashMoney,Integer regId,String gameType,String remark);
	
	/**
	 * 更新用户积分并且保存交易明细
	 * </br><b>调用此方法时不需要再对用户积分进行操作</b>
	 * @param userId 用户id
	 * @param tradeType 收支类型 1.收入 2.支出
	 * @param cashType Constants.POINT_TYPE.*
	 * @param cashPoint 积分
	 * @param regId 引用数据id <em>可选</em>
	 * @return User 返回最新的用户信息
	 */
	public User savePointDetail(Integer userId,String tradeType,String cashType,BigDecimal cashPoint,Integer regId,String remark);
	/**
	 * 删除用户
	 * @param uid
	 * @return 200=ok 201=fail
	 */
	public String delUser(Integer uid,String userType);
	/**
	 * 修改用户余额
	 */
	public void modifyBalance(Integer userId,BigDecimal userpoints,String type);
	
	public void saveUserBalance_(Integer userId,BigDecimal userpoints,String operateType,Integer itemId);

	/**
	 * 用户余额明细
	 */
	public PaginationSupport findUserTradeDetail(String hql, List<Object> para, int pageNum, int pageSize);
	
	/**
	 * 用户投注列表
	 */
	public PaginationSupport findBetList(String hql, List<Object> para, int pageNum, int pageSize);
	
	/**
	 * 后台扣款
	 * @param userId
	 * @param userpoints
	 */
	public void modifyMoney(Integer userId, BigDecimal userpoints,String type);

	/**
	 * 保存用户信息
	 * @param userFrm
	 */
	public void saveUserInfo(User userFrm);

	/**
	 * 根据id查询代理
	 * @param agentId
	 * @return
	 */
	public User getAgentById(Integer agentId);

	public User updateUserTurnTable(User user, LotterySettingRl rl,
			BigDecimal drawMoney);

	/**
	 * 更新抽中红包相关数据
	 * @param user
	 * @param rl
	 * @param drawMoney
	 * @return
	 */
	public User updateUserRedPackets(User user, LotterySettingRl rl,
			BigDecimal drawMoney);

	/**
	 * 保存修改的用户信息
	 * @param userFrm
	 */
	public void saveModifyUserInfo(User userFrm);
	public void saveUserDayRecharge(Integer userId,BigDecimal rechargeMoney,Integer itemId);

	/**
	 * 创建用户赠余额
	 * @param regUser
	 * @param money
	 */
	public void saveUserRegSendMoney(User regUser, BigDecimal money);
/**
 * 签到赠余额
 * @param user
 * @param money
 */
	public void saveUserSignMoney(User user, BigDecimal money);

    /**
     * 统计用户余额和明细对比  更新数据  修复明细
     */
	public void saveCountUserMoney();

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
	
	public void updateUserMoney(List<Integer> userIds,String type);
	
	/**
	 * 更新交易明细表用户投注记录状态
	 * @param sessionNo
	 * @param gameType
	 * @param status 
	 */
	public void updateUserTradeDetailStatus(String sessionNo, String gameType, String status);
	
	/**
	 * 统计更新用户实时余额sum()方式
	 * 一个是批量，一个是单个
	 * @param userId
	 * @return
	 */
	public void updateUserMoney(List<Integer> userIds);
	public void updateUserMoney(Integer userId);
	public void updateUserPoints(List<Integer> userIds);
	public void updateUserPoints(Integer userId);
	public void updateUserMoneyAndBetMoney(Integer userId);
	public void updateUserMoneyAndBetMoney(List<Integer> userIds);
	public BigDecimal updateUserBanlance(Integer userId);
	
	/**
	 * 更新用户累计充值金额并给用户跳级奖励
	 * @param userId
	 */
	public void updateUserAddUpRechargeMoney(User user, BigDecimal cashMoney, Integer reId);
	/**
	 * 根据ip获取该ip已经注册次数
	 * @param ip
	 * @return
	 */
	public IpRecord getIpRecordByIp(String ip);

	public void saveTradeDetail(User user, Integer userId, String tradeType,
			String gameType, BigDecimal money, Integer redId);
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
	
	public String addLevel(UserLevel userLevel,Integer id);
	
	/**
	 * 获取有效的用户（status=1）
	 * @param userId
	 * @return
	 */
	public User getValidUser(Integer userId);
	
	/**
	 * 更新今日输赢
	 * @param userIds
	 */
	public void updateUserBalanceMoney(List<Integer> userIds);
	
	/**
	 * 统计用户各项交易记录
	 * @param ids
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<GaDTO> findUserTradeDetailList(String ids,
			String startDate, String endDate);
	
	/**
	 * 统计用户投注 已开奖部分
	 * @param ids
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<GaDTO> findUserBetCountList(String ids, String startDate,
			String endDate);

	/**
	 * 统计用户下级数量
	 * @param string
	 * @return
	 */
	public List<GaDTO> findSubNumList(String string);

	/**
	 * 把统计结果给dto赋值
	 * @param list
	 * @param tradeList
	 * @param betCountList
	 * @param memberList
	 * @return
	 */
	public List<UserMoneyDTO> updateUserMoneyDTO(List<UserMoneyDTO> list,
			List<GaDTO> tradeList, List<GaDTO> betCountList,
			List<GaDTO> memberList);
	
	/**
	 * 统计用户总盈亏各项和值
	 * @param string
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public UserMoneyDTO countSumMoneyDTO(String string, String startDate,
			String endDate);
	
	/**
	 * 获得用户当日充值金额
	 * @param uid
	 * @return
	 */
	public BigDecimal getDayRecharge(Integer uid);
	/**
	 * 查询用户限制列表
	 */
	public PaginationSupport findUserLimitList(String hqls, List<Object> para,int startIndex,int endIndex);
	/**
	 * 查询用户
	 */
	public UserLimit findUserLimitByUid(Integer uid);
	
	public BigDecimal getDayBetMoney(Integer userId);

	/**
	 * 查询自从上一次提款开始用户的打码量
	 * @return
	 */
	public BigDecimal getUserBetMoneyFromLastCash(Integer userId);
	/**
	 * 查询用户上一次充值
	 * @return
	 */
	public UserTradeDetail getUserLastRecharge(Integer userId);
	
	/**
	 * 查询自从上一次提款开始用户的充值量
	 * @return
	 */
	public BigDecimal getUserRechargeFromLastCash(Integer userId);
	
	/**
	 * 保存gaBetDetail,userTradeDetail
	 */
	public Integer updateOpenData(GaBetDetail detail, User user, String remark, String userType);
}
