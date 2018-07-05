package com.ram.service.user.impl;

import help.base.APIConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.apps.Constants;
import com.apps.dao.IBaseDataDAO;
import com.apps.dao.IParamDAO;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.eff.UserCacheUtil;
import com.apps.eff.dto.ValueConfig;
import com.apps.model.LotterySettingRl;
import com.apps.model.MessageCount;
import com.apps.model.Param;
import com.apps.model.Seller;
import com.apps.model.Type;
import com.apps.model.UserPointDetail;
import com.apps.model.UserTradeDetail;
import com.apps.model.UserTradeDetailRl;
import com.apps.util.DeleteFolder;
import com.apps.util.ProductUtil;
import com.card.model.CardRechargeOrder;
import com.card.model.RechargeWay;
import com.cash.dao.ICashDAO;
import com.cash.model.UserBankBind;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.game.model.GaBetDetail;
import com.game.model.GaBetPart;
import com.game.model.UserBetCount;
import com.game.model.UserLevel;
import com.game.model.dto.GaDTO;
import com.ram.RamConstants;
import com.ram.dao.system.ISemesterDAO;
import com.ram.dao.system.ISystemProgramDAO;
import com.ram.dao.system.ISystemTutorCenterDAO;
import com.ram.dao.user.ILearnerDAO;
import com.ram.dao.user.ITutorDAO;
import com.ram.dao.user.IUserDAO;
import com.ram.dao.user.IUserGroupDAO;
import com.ram.model.DeskRegister;
import com.ram.model.DeskSoftware;
import com.ram.model.DeskSoftwareVersion;
import com.ram.model.DeskTrial;
import com.ram.model.IpRecord;
import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.MobileTrial;
import com.ram.model.OnLineUserRecord;
import com.ram.model.Program;
import com.ram.model.RecommendedUserInfo;
import com.ram.model.Semester;
import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserAndLearnerMainInfo;
import com.ram.model.UserGroup;
import com.ram.model.UserGroupRl;
import com.ram.model.UserLimit;
import com.ram.model.UserLog;
import com.ram.model.dto.LearnerDTO;
import com.ram.model.dto.OnlineUserDTO;
import com.ram.model.dto.TutorialTutorDTO;
import com.ram.model.dto.UserMoneyDTO;
import com.ram.service.user.IUserService;
import com.ram.util.MD5;
import com.ram.web.user.form.ManagerForm;


public class UserServiceImpl extends BaseService implements IUserService {

	private ITutorDAO tutorDAO = null;
	
	private IBaseDataDAO baseDataDAO = null;

	private IUserDAO userDAO = null;

	private IUserGroupDAO userGroupDAO = null;

	private ILearnerDAO learnerDAO = null;

	private ISystemTutorCenterDAO systemTutorCenterDAO = null;

	private ISystemProgramDAO systemProgramDAO = null;

	private ISemesterDAO semesterDAO = null;
	private ICashDAO cashDAO = null;
	private IParamDAO paramDAO = null;

	public void setCashDAO(ICashDAO cashDAO){
		this.cashDAO = cashDAO;
	}

	public void setBaseDataDAO(IBaseDataDAO baseDataDAO) {
		this.baseDataDAO = baseDataDAO;
	}

//	private ISellerDAO sellerDAO= null;
//	private IBetSessionDao betSessionDao =null;
//	public IBetSessionDao getBetSessionDao() {
//		return betSessionDao;
//	}
//
//	public void setBetSessionDao(IBetSessionDao betSessionDao) {
//		this.betSessionDao = betSessionDao;
//	}

public void setParamDAO(IParamDAO paramDAO) {
		this.paramDAO = paramDAO;
	}



	//	public void setSellerDAO(ISellerDAO sellerDAO) {
//		this.sellerDAO = sellerDAO;
//		super.dao = sellerDAO;
//	}
	public ISemesterDAO getSemesterDAO() {
		return semesterDAO;
	}

	public void setSemesterDAO(ISemesterDAO semesterDAO) {
		this.semesterDAO = semesterDAO;
	}

	public void setTutorDAO(ITutorDAO tutorDAO) {
		this.tutorDAO = tutorDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
		super.dao = userDAO;
	}

	public void setUserGroupDAO(IUserGroupDAO userGroupDAO) {
		this.userGroupDAO = userGroupDAO;
	}

	public void setLearnerDAO(ILearnerDAO learnerDAO) {
		this.learnerDAO = learnerDAO;
	}

	public void setSystemTutorCenterDAO(
			ISystemTutorCenterDAO systemTutorCenterDAO) {
		this.systemTutorCenterDAO = systemTutorCenterDAO;
	}

	public PaginationSupport findUserLog(String hsql, List para,
			int startIndex, int pageSize) {
		return userDAO.findUserLog(hsql, para, startIndex, pageSize);
	}

	/**
	 * 创建一个用户
	 */
	public void saveUser(User user, User operaUser) {
		userDAO.saveUser(user, operaUser);
	}

	public void saveUser(User user) {
		userDAO.saveUser(user, null);
	}

	/**
	 * 批量删除用户
	 */
	public void deleteUser(int[] id, User user) {
		userDAO.deleteUser(id, user);
	}

	/**
	 * 保存tutor对象
	 */
	public Tutor saveTutor(Tutor tutor, User user) throws DataAccessException {
		return tutorDAO.saveTutor(tutor, user);
	}

	public Tutor getTutors(Integer tutorId) {
		return (Tutor) userDAO.getObject(Tutor.class, tutorId);
	}

	/**
	 * 根据userId获得一个学生对象
	 * 
	 * 
	 * 
	 */
	public Learner getLearnerByUserId(int userId) throws DataAccessException {
		return learnerDAO.getLearnerByUserId(userId);
	}

	/**
	 * 根据learnerID获得learner
	 * 
	 * @param userId
	 * @return
	 */
	public Learner getLearnerByLearnerId(Integer learnerId) {
		return learnerDAO.getLearnerByLearnerId(learnerId);
	}

	/**
	 * 修改一个用户状态
	 * 
	 * 
	 * 
	 * @param User
	 */
	public void modifyUserStatus(int id, String status, User user) {
		userDAO.modifyUserStatus(id, status, user);

	}

	/**
	 * 获得一个管理员信息
	 * 
	 * @param User
	 */
	public Manager getManager(int userId) {
		return userDAO.getManager(userId);
	}

	/**
	 * 获得一个教师信息
	 * 
	 * 
	 * 
	 * @param User
	 */
	public Tutor getTutor(int id) {
		return userDAO.getTeacher(id);
	}

	/**
	 * 获得Tutor对象列表(分页)
	 */
	public PaginationSupport findALLTutorForPage(int startIndex, int pageSize) {
		List tutorList = userDAO.getAllTutors(startIndex, pageSize);
		int num = userDAO.getTutorsNumber();
		PaginationSupport ps = new PaginationSupport(tutorList, num, pageSize,
				startIndex);
		return ps;
	}

	/**
	 * 根据查询条件获得Manager对象列表(分页)
	 */
	public PaginationSupport findALLTutorForPage(int startIndex, int pageSize,
			DetachedCriteria detachedCriteria) {
		PaginationSupport ps = userDAO.findAllTutors(startIndex, pageSize,
				detachedCriteria);
		return ps;
	}

	/**
	 * 创建或者修改一个用户组
	 * 
	 * @param User
	 */
	public int saveUserGroup(UserGroup userGroup, User user) {
		return userGroupDAO.saveUserGroup(userGroup, user);
	}

	/**
	 * 删除一个用户组
	 * 
	 * @param User
	 */
	public void deleteUserGroup(int userGroupId, User user) {
		userGroupDAO.deleteUserGroup(userGroupId, user);
	}

	/**
	 * 检索出所有用户组
	 * 
	 * @param User
	 */
	public List getAllUserGroup() {
		return userGroupDAO.getAllUserGroup();
	}

	/**
	 * 根据用户组ID获得用户组的信息
	 * 
	 * @param User
	 */
	public UserGroup getUserGroupByID(int userGroupID) {
		return userGroupDAO.getUserGroup(userGroupID);
	}

	/**
	 * 建立用户和用户组之间的关系
	 * 
	 * 
	 * 
	 */
	public boolean saveUserGroupRl(UserGroupRl userGroupRl, User user) {
		userGroupDAO.saveUserGroupRl(userGroupRl, user);
		return true;
	}

	/**
	 * 根据用户ID获得用户所在的用户组
	 * 
	 * 
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getUserGroupById(int userId) throws DataAccessException {
		log.info("在UserServiceImpl.java中调用userGroupDAO.getUserGroupById(userId),userId="
				+ userId);
		return userGroupDAO.getUserGroupById(userId);
	}

	/**
	 * 根据用户ID获得用户可以添加的用户组
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List getAvailableUserGroupById(int userId)
			throws DataAccessException {
		return userGroupDAO.getAvailableUserGroupById(userId);
	}

	/**
	 * 根据课程ID获得教师
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAllTutorsByCourseId() {
		List tutorList = userDAO.findAllTutorsByCourseId();
		return tutorList;
	}

	/**
	 * 根据课程ID和教师ID获得教师
	 * 
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List findAllTutorsByTcId(int tcId) {
		List tutorList = userDAO.findAllTutorsByTcId(tcId);
		return tutorList;
	}

	/*
	 * 获得所有在学学生列表
	 */
	public List findStudyLearners() {
		return learnerDAO.findStudyLearners();
	}

	/**
	 * 通过学习中心获得所有教师对象
	 * 
	 * 
	 */
	public List findAllTutorByTcId(Integer tcId) {
		List tutorList = tutorDAO.findAllTutorByTcId(tcId);
		return tutorList;
	}

	public List findUserInTutorByTcId(Integer tcId) {
		return tutorDAO.findUserInTutorByTcId(tcId);
	}

	public List findTutorByIsAppraise() {
		return tutorDAO.findTutorByIsAppraise();
	}

	/**
	 * 建立用户和用户组之间的关系
	 * 
	 * 
	 * 
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void addUserAndGroupRl(int userId, int[] userGroupId, User user)
			throws DataAccessException {
		userGroupDAO.addUserAndGroupRl(userId, userGroupId, user);
	}

	/**
	 * 删除用户和用户组之间的关系
	 * 
	 * 
	 * 
	 * @param userId
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public void deleteUserAndGroupRl(int userId, int userGroupId, User user)
			throws DataAccessException {
		userGroupDAO.deleteUserAndGroupRl(userId, userGroupId, user);
	}

	/**
	 * 创建或者保存一个学生
	 * 
	 * 
	 * 
	 */
	public void saveLearner(Learner learner, User user)
			throws DataAccessException {
		learnerDAO.saveLearner(learner, user);
	}

	/**
	 * 根据学生ID获得一个学生对象
	 * 
	 * 
	 * 
	 */
	public Learner getLearner(int learnerId) throws DataAccessException {
		return learnerDAO.getLearner(learnerId);
	}

	/**
	 * 获得所有学生信息(分页)
	 */
	public PaginationSupport getAllLearners(int startIndex, int pageSize) {
		List learnerList = learnerDAO.getAllLearners(startIndex, pageSize);
		;
		int num = learnerDAO.getLearnNumber();
		PaginationSupport ps = new PaginationSupport(learnerList, num,
				pageSize, startIndex);
		return ps;
	}

	/**
	 * 根据查询条件获得学生信息(分页)
	 */
	public PaginationSupport findAllLearners(final int startIndex,
			final int pageSize, final DetachedCriteria detachedCriteria) {
		return learnerDAO.findAllLearners(startIndex, pageSize,
				detachedCriteria);
	}

	/**
	 * 修改学生的学籍状态
	 * 
	 * 
	 * 
	 */
	public void modifyLearnerStatus(int learnerId, int status, User user)
			throws DataAccessException {
		learnerDAO.modifyLearnerStatus(learnerId, status, user);
	}

	/**
	 * 根据用户名和密码取得用户信息
	 * 
	 * @return
	 */
	public User getUser(String loginName, String password) {
		return userDAO.getUser(loginName, password);
	}

	public User getUser(Integer originId, Integer originUserId) {
		return userDAO.getUser(originId, originUserId);
	}
	
	public User getUserByloginComm(String accoutName, String password) {
		return userDAO.getUserByloginComm(accoutName, password);
	}

	public User getUserByloginCommSj(String accoutName, String password) {
		return userDAO.getUserByloginCommSj(accoutName, password);
	}

	/**
	 * 根据用户名取得用户信息
	 * 
	 * 
	 * 
	 * @return
	 */
	public Integer getUser(String loginName) {
		return userDAO.getUser(loginName);
	}

	public User getUserByLoginName(String loginName) {
		return userDAO.getUserByLoginName(loginName);
	}

	public User findUserByLoginName(String loginName) {
		return userDAO.findUserByLoginName(loginName);
	}

	public User getUserByActCode(String actCode) {
		return userDAO.getUserByActCode(actCode);
	}

	/**
	 * 检查用户名是否有重复
	 * 
	 * 
	 * 
	 */
	public boolean checkUserExist(String loginName) {
		this.checking();
		return userDAO.checkUserExist(loginName);
	}
	
	public boolean checkUserExist(Integer uid){
		return userDAO.checkUserExist(uid);
	}

	/**
	 * 邀请码是否存在
	 * 
	 */
	public User findUserByItCode(String itCode) {
		this.checking();
		return (User) userDAO.findUserByItCode(itCode);
	}

	public User getUser(int userId) {
		this.checking();
		return (User) userDAO.getObject(User.class, new Integer(userId));
	}

	/**
	 * 根据用户姓名取得用户ID
	 * 
	 * @return
	 */
	public Integer getUserId(String userName) {
		this.checking();
		return userDAO.getUserId(userName);
	}

	/**
	 * 根据用户ID获得TutorID
	 * 
	 * @param userId
	 * @return
	 */
	public Integer getTutorIdByUserId(Integer userId) {
		this.checking();
		return userDAO.getTutorIdByUserId(userId);
	}

	/**
	 * 根据userId,得到该用户属于哪个学习中心id
	 */
	public Integer getTcIdByUserId(Integer userId) {
		this.checking();
		User user = userDAO.getUser(userId.intValue());
		if (user.getUserType().equals(RamConstants.UserTypeIsLearner3)) {
			Learner learner = this.getLearnerByUserId(userId.intValue());
			return learner.getTcId();
		} else if (user.getUserType().equals(RamConstants.UserTypeIsManager2)) {
			Manager manager = this.getManager(userId.intValue());
			return manager.getTcId();
		} else if (user.getUserType().equals(RamConstants.UserTypeIsTutor1)) {
			Tutor tutor = this.getTutor(userId.intValue());
			return tutor.getTutorCenter().getTcId();
		} else {
			return new Integer(0);
		}
	}

	/**
	 * 根据学籍号获得learnerId
	 * 
	 * @param studyNumber
	 * @return
	 */
	public Integer getLearnerIdByStudyNumber(String studyNumber) {
		this.checking();
		return userDAO.getLearnerIdByStudyNumber(studyNumber);
	}

	/**
	 * 根据学籍号获得learner
	 * 
	 * @param studyNumber
	 * @return
	 */
	public Learner getLearnerByStudyNumber(String studyNumber) {
		return userDAO.getLearnerInfoByStudyNumber(studyNumber);
	}

	/**
	 * 生成学籍号 根据年度＋季度＋层次＋中心获得学籍号 所有参数均为字符串类型传入
	 * 
	 * @param year
	 *            年部分，必须为2位，不够系统自动填0补齐
	 * @param season
	 *            季节：必须为1位，不够系统自动填0补齐
	 * @param program
	 *            层次部分，必须为2位，不够系统自动填0补齐
	 * @param tcCenter
	 *            中心部分，必须为3位，不够三位系统自动填0补齐
	 * @return
	 */
	public String getGeneratedStudyNumber(String year, String season,
			String program, int tcId) {
		this.checking();
		String strYear = null;
		String strTcCenter = null;
		String strProgram = null;
		// 年部分，必须为4位

		if (year.length() == 4) {
			strYear = year.substring(2, 4);
		} else {
			strYear = year;
		}
		// 层次部分，必须为2位

		if (program.length() == 1) {
			strProgram = "0" + program;
		} else {
			strProgram = program;
		}
		// 中心部分，必须为3位

		TutorCenter tc = this.systemTutorCenterDAO
				.getSystemTutorCenter(new Integer(tcId));

		if (tc != null) {

			strTcCenter = tc.getTcCode();
			log.info("tc.getTcCode()=" + tc.getTcCode());
			if (strTcCenter == null || strTcCenter.length() != 3) {
				log.info("strTcCenter=" + strTcCenter);
				return null;
			}
		} else {
			log.info("tc is null");
			return null;
		}

		String str = strYear + season + strProgram + strTcCenter;

		String maxStudyNumber = userDAO.getMaxStudyNumber(str);

		// 学籍号不存在的情况下
		if (maxStudyNumber == null) {
			return str + "0001";
		}
		this.checking();
		String tempStr = "1" + maxStudyNumber;

		long tmpStudyNumber = Long.parseLong(tempStr);

		String studyNumberStr = String.valueOf(tmpStudyNumber + 1);

		int length = studyNumberStr.length();
		String newStudyNumber = studyNumberStr.substring(1, length);

		return newStudyNumber;
	}

	/**
	 * 根据查询条件获得学生信息
	 */
	public List findLearnersByCriteria(final DetachedCriteria detachedCriteria) {
		return learnerDAO.findLearnersByCriteria(detachedCriteria);
	}

	/**
	 * 根据登录名获得
	 * 
	 * 
	 * 
	 */
	public Learner getLearnerByLoginName(String loginName) {
		return userDAO.getLearnerInfoByLoginName(loginName);

	}

	/**
	 * 通过用户ID得到LearnerDTO对象 如果该用户是学生，返回LearnerDTO 如果该用户不是学生，返回null
	 * 
	 * @param userId
	 * @return
	 * 
	 */
	public LearnerDTO getLearnerDTOByUserId(int userId) {
		if (userId <= 0)
			return null;
		User user = this.getUser(userId);
		if (user == null)
			return null;
		// 如果是学生

		if (user.getUserType().equals(RamConstants.UserTypeIsLearner3)) {
			Learner learner = getLearnerByLoginName(user.getLoginName());
			Program program = systemProgramDAO.getSystemProgram(learner
					.getProgramId());
			TutorCenter tc = systemTutorCenterDAO.getSystemTutorCenter(learner
					.getTcId());
			Semester semester = semesterDAO
					.getSemester(learner.getSemesterId());
			LearnerDTO learnerDTO = new LearnerDTO();
			learnerDTO.setLearner(learner);
			learnerDTO.setProgram(program);
			learnerDTO.setSemester(semester);
			learnerDTO.setTc(tc);
			learnerDTO.setUser(user);
			return learnerDTO;
		} else {
			// 如果该用户不是学生，返回null
			return null;
		}

	}

	/**
	 * 获得所有教师对象
	 */

	public List findAllTeachers() {
		return tutorDAO.findAllTeachers();
	}

	/**
	 * 根据学习中心获得所有教师对象
	 * 
	 * 
	 */

	public List findAllTutors() {
		return tutorDAO.findAllTutors();
	}

	/**
	 * 获得所有教师对象
	 * 
	 * 
	 * 
	 */

	public List findTeachers() {
		return tutorDAO.findTeachers();
	}

	/**
	 * 检查修改后的用户名和其他用户的用户名是否冲突
	 * 
	 * 
	 * 
	 */
	public boolean checkUserExistAgain(String loginName, Integer userId) {
		this.checking();
		return userDAO.checkUserExistAgain(loginName, userId);
	}

	/*
	 * add by ysl 根据UserId获得学生的基本信息UserAndLearnerMainInfo
	 */
	public UserAndLearnerMainInfo getUserAndLearnerMainInfo(Integer userId) {
		this.checking();
		User user = userDAO.getUser(userId.intValue());
		Learner learner = userDAO
				.getLearnerInfoByLoginName(user.getLoginName());
		UserAndLearnerMainInfo userAndLearnerMainInfo = null;
		if (user != null && learner != null) {
			this.checking();
			userAndLearnerMainInfo = new UserAndLearnerMainInfo(
					user.getUserId(), user.getLoginName(),
					user.getUserNameZh(), learner.getLearnerId(),
					learner.getTcId(), learner.getStudyNumber());
		}
		return userAndLearnerMainInfo;
	}

	/*
	 * 获得未在可预约用户表中的在学学生列表
	 */
	public List findLearnersCanExamAndNotInTable() {
		return learnerDAO.findLearnersCanExamAndNotInTable();
	}

	/**
	 * 管理员登录排行
	 * 
	 * 
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnManagerByLoginTimes(int offset, int pageSize) {
		return userDAO.findTopnUsersByLoginTimes(offset, pageSize,
				RamConstants.UserTypeIsManager2);
	}

	/**
	 * 学生登录排行
	 * 
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnStudentsByLoginTimes(int offset, int pageSize) {
		return userDAO.findTopnUsersByLoginTimes(offset, pageSize,
				RamConstants.UserTypeIsLearner3);
	}

	/**
	 * 老师登录排行
	 * 
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List findTopnTutorsByLoginTimes(int offset, int pageSize) {
		return userDAO.findTopnUsersByLoginTimes(offset, pageSize,
				RamConstants.UserTypeIsTutor1);
	}

	/**
	 * 判断一个用户是否是总部学习中心的
	 * 
	 * 
	 * add by hulei at 2006-4-6
	 * 
	 * @param tcId
	 * @return
	 */
	public boolean isHeadQuarterManager(int userId) {
		boolean isHqManager = false;
		User user = this.getUser(userId);
		// 如果是管理员
		if (user.getUserType().equals(RamConstants.UserTypeIsManager2)) {
			Manager manager = this.getManager(user.getUserId().intValue());
			if (manager != null) {// 如果管理员不为空
				if (manager.getTcId() != null) {// 如果管理员的TCId设置正确
					TutorCenter hqTc = this.systemTutorCenterDAO
							.getHeadQuarterTutorCenter();
					if (hqTc != null) {// 如果系统的总部学习中心已经设置
						if (manager.getTcId().intValue() == hqTc.getTcId()
								.intValue()) {// 如果当前管理员用户的TCid=总部中心id
							isHqManager = true;
						}
					}
				} else {
					log.error("管理员用户UserId=" + user.getUserId().intValue()
							+ ",UserName=" + user.getUserNameZh()
							+ "，的学习中心未设置！该值是必需的，请及时修改,设置正确！");
				}
			} else {
				log.error("当前用户Userid="
						+ user.getUserId().intValue()
						+ "的用户类型User_Type字段值是管理员，但是对应的Manager表中没有该用户的信息！请补充完善该用户在Manager表中的信息");
			}

		}
		return isHqManager;
	}

	public boolean isAdminManager(int userId) {
		boolean isHqManager = false;
		User user = this.getUser(userId);
		// 如果是管理员
		if (user.getUserType().equals(RamConstants.UserTypeIsManager2)
				|| user.getUserType().equals(RamConstants.UserTypeIsNewUser4)) {
			isHqManager = true;
		}
		return isHqManager;
	}

	/**
	 * 判断一个人是否是当地中心管理员
	 */
	public boolean isLocalCenterManager(int userId) {
		boolean isLocalCenterManager = false;
		if (this.getManager(userId) != null) {
			if (!this.isHeadQuarterManager(userId)) {
				isLocalCenterManager = true;
			}
		}
		return isLocalCenterManager;
	}

	/**
	 * 判断一个管理员用户，是否为学生转学目标中心的管理员
	 * 
	 * @param objectTcId
	 * @param managerId
	 * @return
	 */
	public boolean IsLocalCenterManagerOfTutorCenter(int objectTcId,
			int managerId) {
		boolean isLocal = false;
		Manager manager = this.getManager(managerId);
		if (this.isLocalCenterManager(managerId)) {
			if (manager.getTcId().intValue() == objectTcId) {
				isLocal = true;
			}
		}
		return isLocal;
	}

	/**
	 * 得到User的TcId 如果用户没有设置tcId,返回0
	 * 
	 * @param user
	 * @return
	 */
	public int getTcId_Of_User(User user) {
		int tcId = 0;
		try {
			if (user.getUserType().equals(RamConstants.UserTypeIsLearner3)) {
				Learner learner = this.getLearnerByUserId(user.getUserId()
						.intValue());
				tcId = learner.getTcId().intValue();
			}
			if (user.getUserType().equals(RamConstants.UserTypeIsManager2)) {
				Manager manager = this.getManager(user.getUserId().intValue());
				tcId = manager.getTcId().intValue();
			}
			if (user.getUserType().equals(RamConstants.UserTypeIsTutor1)) {
				Tutor tutor = this.getTutor(user.getUserId().intValue());
				tcId = tutor.getTutorCenter().getTcId().intValue();
			}
		} catch (Exception ex) {
			log.equals(ex.getMessage());
		}
		return tcId;

	}

	public int studyQuery(String where, List list) {
		return this.learnerDAO.studyQuery(where, list);
	}

	public List studyQuery(int offset, int rows, String where, List list) {
		return this.learnerDAO.studyQuery(offset, rows, where, list);
	}

	/**
	 * 根据BWOLV3的UserId得到对应的在BWOLV4中的User_id值
	 * 
	 * 
	 * @param v3UserId
	 * @return
	 */
	public int translateV3UserIdToV4UserId(int v3UserId) {
		User user = this.userDAO.getUserByV3UserId(v3UserId);
		if (user != null) {
			// log.info("user.getid=" + user.getLoginName());
			return user.getUserId().intValue();
		} else {
			log.warn("v3userid=" + v3UserId + " 的用户在新平台中不存在");
			return 0;
		}
	}

	public int findLearner(String where, List list) {
		return this.userDAO.findLearner(where, list);
	}

	public List findAllLearner() {
		return this.userDAO.findAllLearner();
	}

	public List findLearner(int offset, int rows, String where, List list) {
		// // "(l, u, l.learnerId, s.semesterId, p.programName,
		// sp.specialityTitle,
		// // s.semesterTitle, t.tcTitle) " +
		// List listLearnerForm = new ArrayList();
		// LearnerForm lf = null;
		// List learnerList = userDAO.findLearner(offset, rows, where, list);
		// Learner learner = null;
		// Program p = null;
		// Speciality sp = null;
		// TutorCenter tc = null;
		// Semester sem = null;
		//
		// for (int i = 0; i < learnerList.size(); i++) {
		//
		// lf = new LearnerForm();
		// learner = (Learner) learnerList.get(i);
		// lf.setLearner(learner);
		// lf.setUser(learner.getUser());
		// lf.setLearnerId(learner.getLearnerId());
		// // 学期
		// lf.setSemesterId(learner.getSemesterId());
		// if (learner.getSemesterId() != null) {
		// sem = this.semesterDAO.getSemester(learner.getSemesterId());
		// if (sem != null) {
		// lf.setSemesterName(sem.getSemesterTitle());
		// } else {
		// lf.setSemesterName("");
		// }
		// } else {
		// lf.setSemesterName("");
		// }
		// // 层次
		// if (learner.getProgramId() != null) {
		// p = this.systemProgramDAO.getSystemProgram(learner
		// .getProgramId());
		// if (p != null) {
		// lf.setProgramName(p.getProgramName());
		// } else {
		// lf.setProgramName("");
		// }
		// } else {
		// lf.setProgramName("");
		// }
		// // 专业方向
		// if (learner.getSpecialityId() != null) {
		// sp = learner.getSpecialityId() == null
		// || learner.getSpecialityId().intValue() == 0 ? null
		// : this.specialityDAO.getSpeciality(learner
		// .getSpecialityId());
		// if (sp != null) {
		// lf.setSpecialityName(sp.getSpecialityTitle());
		// } else {
		// lf.setSpecialityName("");
		// }
		// } else {
		// lf.setSpecialityName("");
		// }
		// // 中心
		// if (learner.getTcId() != null) {
		// tc = this.systemTutorCenterDAO.getSystemTutorCenter(learner
		// .getTcId());
		// if (tc != null) {
		// lf.setTcCenterName(tc.getTcTitle());
		// } else {
		// lf.setTcCenterName("");
		// }
		// } else {
		// lf.setTcCenterName("");
		// }
		// listLearnerForm.add(lf);
		// }
		// return listLearnerForm;
		return null;
	}

	public ISystemProgramDAO getSystemProgramDAO() {
		return systemProgramDAO;
	}

	public void setSystemProgramDAO(ISystemProgramDAO systemProgramDAO) {
		this.systemProgramDAO = systemProgramDAO;
	}

	/**
	 * 通过课程ID获得所有教师对象
	 * 
	 * 
	 */
	public List findAllTutorByCourseId(Integer courseId, Integer semesterId) {
		return tutorDAO.findAllTutorByCourseId(courseId, semesterId);
	}

	/**
	 * 通过课程ID和学习中心id获得所有教师对象
	 * 
	 * 
	 */
	public List findAllTutorByCourseIdAndTcId(Integer scheduleCourseId,
			Integer tcId, Integer semesterId) {
		return tutorDAO.findAllTutorByCourseIdAndTcId(scheduleCourseId, tcId,
				semesterId);
	}

	public User getUserByTutorId(int tutorId) {
		return userDAO.getUserByTutorId(tutorId);
	}

	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public List findAllTutorsByTcId(int tcId, int courseId) {
		List list = new ArrayList();
		Query query = userDAO.findAllTutorsByTcId(tcId, courseId);
		Iterator it = query.iterate();
		while (it.hasNext()) {
			Object[] itemInfo = (Object[]) it.next();

			User user = (User) itemInfo[0];
			Tutor tutor = (Tutor) itemInfo[1];
			TutorialTutorDTO dto = new TutorialTutorDTO();
			dto.setTutor(tutor);
			dto.setUser(user);
			list.add(dto);
		}
		return list;

	}

	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public List findAllCenterTutorsByTcId(int tcId, int courseId,
			String tutorType, int semesterId) {
		List list = new ArrayList();
		Query query = userDAO.findAllCenterTutorsByTcId(tcId, courseId,
				tutorType, semesterId);
		Iterator it = query.iterate();
		while (it.hasNext()) {
			Object[] itemInfo = (Object[]) it.next();

			User user = (User) itemInfo[0];
			Tutor tutor = (Tutor) itemInfo[1];
			TutorialTutorDTO dto = new TutorialTutorDTO();
			dto.setTutor(tutor);
			dto.setUser(user);
			list.add(dto);
		}
		return list;

	}

	/**
	 * 查询用户组中的用户
	 * 
	 * 
	 * @param userGroupId
	 * @throws DataAccessException
	 */
	public List findUserOfUserGroup(Integer userGroupId) {
		return userGroupDAO.findUserOfUserGroup(userGroupId);
	}

	/**
	 * 根据在线用户列表，得到在线用户的OnlineUserDTO列表
	 * 
	 * @param onlineUsers
	 * @return
	 */
	public List findOnlineUserDTOs(List onlineUsers) {
		User user = null;
		Learner learner = null;
		Program program = null;
		Semester semester = null;
		OnlineUserDTO onlineUserDTO = null;
		List listOnlineUserDTO = new ArrayList();
		for (int i = 0; i < onlineUsers.size(); i++) {
			user = (User) onlineUsers.get(i);
			// 如果是学生，则得到学生对象

			if (user != null
					&& user.getUserType() != null
					&& user.getUserType().equals(
							RamConstants.UserTypeIsLearner3)) {
				learner = this.userDAO.getLearnerInfoByLoginName(user
						.getLoginName());
				semester = this.semesterDAO
						.getSemester(learner.getSemesterId());
				program = systemProgramDAO.getSystemProgram(learner
						.getProgramId());
			} else {
				// 否则为空
				learner = null;
				program = new Program();
				program.setProgramName("");
				if (user.getUserType().equals(RamConstants.UserTypeIsManager2))
					program.setProgramName("管理员");
				if (user.getUserType().equals(RamConstants.UserTypeIsTutor1))
					program.setProgramName("教师");
			}
			onlineUserDTO = new OnlineUserDTO();
			onlineUserDTO.setUser(user);
			onlineUserDTO.setLearner(learner);
			// onlineUserDTO.setProgram(program);
			// onlineUserDTO.setSemester(semester);
			listOnlineUserDTO.add(onlineUserDTO);
		}
		return listOnlineUserDTO;
	}

	public int findAllLearner(String where, List list) {
		return userDAO.findAllLearner(where, list);
	}

	public List findAllLearner(int offset, int rows, String where, List list) {
		return userDAO.findAllLearner(offset, rows, where, list);
	}

	public void updateLearnerEnrollRegisterStatus(Integer userId) {
		userDAO.updateLearnerEnrollRegisterStatus(userId);
	}

	public void updateLearnerEnrollRegisterStatus(String regDate, List userId) {
		userDAO.updateLearnerEnrollRegisterStatus(regDate, userId);
	}

	/**
	 * 通过用户名查找所有用户人数
	 */
	public int getALLByLoginNameNum(String loginName) {
		return userDAO.getALLByLoginNameNum(loginName);
	}

	/**
	 * 判断用户是否属于助学中心的用户组
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean getUserGroupByUserId(Integer userId) throws Exception {
		return userGroupDAO.getUserGroupByUserId(userId);
	}

	public User getUserByStudyNumber(String studyNumber) {
		return userDAO.getUserByStudyNumber(studyNumber);

	}

	public User getUserByEnrollNumber(String enrollNumber) {
		return userDAO.getUserByEnrollNumber(enrollNumber);
	}

	/**
	 * 记录登录用户人数
	 * 
	 * @param serverName
	 * @param userRecordNumber
	 * @param currentTime
	 */
	public void saveOnlineUserRecord(OnLineUserRecord onlineUserRecord,
			User user) {
		userDAO.saveOnlineUserRecord(onlineUserRecord, user);
	}

	/**
	 * 统计登录人数
	 * 
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List findOnlineUserRecord(int offset, int rows) {
		return userDAO.findOnlineUserRecord(offset, rows);
	}

	public int getOnlineUserCount() {
		return userDAO.getOnlineUserCount();
	}

	public void saveRecommendedUserInfo(RecommendedUserInfo rui, User user) {
		userDAO.saveObject(rui, user);
	}

	public void delRecommendedUserInfo(Integer id, User user) {
		userDAO.deleteObject(RecommendedUserInfo.class, id, user);
	}

	public void saveUserLog(UserLog userLog) {
		userDAO.saveObject(userLog);
	}

	public void saveReMoneyUser(User codeUser, User user1) {


		BigDecimal uYe = codeUser.getUserpoints();

		// 邀请码返现
		double ed = 0.0;
		if (uYe != null) {
			ed = uYe.doubleValue() + RamConstants.USER_ITCODE_FX_MONEY;
		} else {
			ed = RamConstants.USER_ITCODE_FX_MONEY;
		}


		codeUser.setUserpoints(new BigDecimal(ed).setScale(2,
				BigDecimal.ROUND_HALF_UP));

		codeUser.setStatusCode(RamConstants.RANDOM_YAO_QING_CODE_STATUS_NO);

		userDAO.saveObject(codeUser);
	}

	public void saveDdCodeMoney(User user, User user1, String itCode) {

		// DdCodeMoney ddCodeMoney = new DdCodeMoney();
		// ddCodeMoney.setQyUserId(user1.getUserId());
		// ddCodeMoney.setReUserId(user.getUserId());
		//
		// ddCodeMoney.setYqCode(itCode);
		// ddCodeMoney.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
		// ddCodeMoney.setReMoney(new
		// BigDecimal(RamConstants.USER_ITCODE_FX_MONEY).setScale(2,BigDecimal.ROUND_HALF_UP));
		//
		// userDAO.saveObject(ddCodeMoney);
	}

	public List findRecommendedUser(String str, List list, int startIndex,
			int pageSize) {
		return userDAO.findRecommendedUser(str, list, startIndex, pageSize);
	}

	public int getRecommendedUserNum(String str, List list) {
		return userDAO.getRecommendedUserNum(str, list);
	}

	public String createDeskSerialNumber(int len, String softwareCode,
			String sntype) {
		log.fatal("## create Serial Number...");
		DeskSoftware ds = userDAO.getDeskSoftware(softwareCode);
		if (ds == null) {
			return "## not found DeskSoftware.record>" + softwareCode;
		} else {
			for (int i = 0; i < len; i++) {
				DeskRegister dr = new DeskRegister();
				dr.setSerialNumber(ds.getSnHead() + StringUtil.genDeskToefSN());
				// dr.setSoftwareCode(softwareCode);
				dr.setStatus(RamConstants.DESK_SN_STATUS_INIT);
				dr.setUseDays(ds.getUserDays());
				dr.setSntype(sntype);
				userDAO.saveObject(dr);
				log.fatal("## sn:" + dr.getSerialNumber());
			}
			return "ok";
		}
	}

	public String createDeskSN(String softwareCode, String sntype,
			String sntaobao, String sellprice, String status, String remarks,
			int count, Integer userId) {
		log.fatal("## create SN..." + softwareCode + ",sntype:" + sntype
				+ ",sntaobao:" + sntaobao + "," + count + "," + remarks);

		DeskSoftware ds = null;
		String snHead = "TD";
		if (ParamUtils.chkString(softwareCode))
			ds = userDAO.getDeskSoftware(softwareCode);
		if (ds != null) {
			snHead = ds.getSnHead();
		}

		for (int i = 0; i < count; i++) {
			String sn = snHead + StringUtil.genDeskToefSN();
			if (userDAO.checkDeskRegisterSNOk(sn)) {
				DeskRegister dr = new DeskRegister();
				dr.setSerialNumber(sn);
				if (ParamUtils.chkString(softwareCode))
					dr.setSoftwareCode(softwareCode);
				dr.setStatus(status);
				dr.setUseDays(30);
				dr.setSntype(sntype);
				dr.setUserId(userId);
				if (ParamUtils.chkString(sntaobao) && sntype.equals("2"))
					dr.setSntaobao(sntaobao);
				if (ParamUtils.chkString(sellprice))
					dr.setSellprice(Integer.valueOf(sellprice));
				dr.setRemarks(remarks);
				if (sntype.equals("2") || status.equals("2")) {
					dr.setSendTime(DateTimeUtil.getJavaUtilDateNow());
				}
				userDAO.saveObject(dr);
				log.fatal("## sn:" + dr.getSerialNumber());
			}
		}

		return "ok";
	}

	public DeskRegister createDeskSNOne(String softwareCode, String sntype,
			String status) {
		log.fatal((new StringBuilder("## create SN One..."))
				.append(softwareCode).append(",sntype:").append(sntype)
				.toString());
		DeskSoftware ds = userDAO.getDeskSoftware(softwareCode);
		if (ds == null)
			return null;
		String snHead = ds.getSnHead();
		String sn = (new StringBuilder(String.valueOf(snHead))).append(
				StringUtil.genDeskToefSN()).toString();
		if (userDAO.checkDeskRegisterSNOk(sn)) {
			DeskRegister dr = new DeskRegister();
			dr.setSerialNumber(sn);
			if (ParamUtils.chkString(softwareCode))
				dr.setSoftwareCode(softwareCode);
			dr.setStatus(status);
			dr.setUseDays(Integer.valueOf(30));
			dr.setSntype(sntype);
			dr.setSendTime(DateTimeUtil.getJavaUtilDateNow());
			userDAO.saveObject(dr);
			log.fatal((new StringBuilder("## sn:"))
					.append(dr.getSerialNumber()).toString());
			return dr;
		} else {
			return null;
		}
	}

	public boolean checkMacTrue(String storeMac, String fromMac) {
		String[] smacs = storeMac.split(",");
		String[] fmacs = fromMac.split(",");
		for (int i = 0; i < smacs.length; i++) {
			String smac = smacs[i];
			if (!ParamUtils.chkString(smac))
				smac = "smac";
			smac = getFormartMac(smac);
			for (int j = 0; j < fmacs.length; j++) {
				String fmac = fmacs[j];
				if (!ParamUtils.chkString(fmac))
					fmac = "fmac";
				fmac = getFormartMac(fmac);
				if (fmac.equals(smac)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getFormartMac(String fmac) {
		if (!ParamUtils.chkString(fmac))
			return fmac;
		fmac = fmac.trim();
		if (fmac.indexOf(":") > 0) {// Mac macs
			fmac = fmac.toUpperCase();
			fmac = fmac.replace(":", "-");
		}
		return fmac;
	}

	public String updateDeskNewRegister(String code, String sn, String mac,
			String v, String ip) {
		String chkStatus = this.checkDeskParameter("newRegister()", code, sn,
				mac, v, ip);
		if (ParamUtils.chkString(chkStatus)) {
			return this.getReturnData(chkStatus, null);
		}

		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr == null) {// 无此序列号
			return this.getReturnData(RamConstants.DESK_REWRIATE_MAC_FALSE,
					null);
		}
		String rmac = dr.getRegMac();
		if (ParamUtils.chkString(rmac)) {
			String newmac = rmac + "," + mac;
			Integer remacCount = dr.getRemacCount();
			if (!ParamUtils.chkInteger(remacCount))
				remacCount = 0;

			// if(!this.checkMacTrue(rmac, mac)){
			// //String regLog = dr.getRegLog();
			// //if(!ParamUtils.chkString(regLog)) regLog = "";
			// //regLog +=
			// (ParamUtils.chkString(regLog)?"#":"")+"reMac"+"["+DateTimeUtil.getJavaUtilDateNow()+"]["+mac+"]["+v+"]["+ip+"]";
			//
			//
			// //dr.setRegLog(regLog);
			//
			// userDAO.saveObject(dr);
			// //log.fatal("write mac:"+rmac+",rmac:"+newmac);
			// }
			// if(remacCount<RamConstants.getDeskReMacNum()){
			if (remacCount < 0) {
				dr.setRemacCount(remacCount + 1);
				dr.setRegMac(newmac);
				this.updateDeskRegisterLog(dr, RamConstants.DESK_REWRIATE_MAC
						+ "[reMacOk]", mac, v, ip, code);
				return this.getReturnDataMacs(RamConstants.DESK_REWRIATE_MAC,
						null, newmac);
			} else {
				this.updateDeskRegisterLog(dr,
						RamConstants.DESK_REWRIATE_MAC_FALSE + "[reMacFail]",
						mac, v, ip, code);
				return this.getReturnData(RamConstants.DESK_REWRIATE_MAC_FALSE,
						null);
			}
		}
		return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
	}

	public void updateDeskRegisterLog(DeskRegister dr, String returnStatus,
			String mac, String v, String ip, String code) {
		String regLog = dr.getRegLog();
		if (!ParamUtils.chkString(regLog))
			regLog = "";
		regLog += (ParamUtils.chkString(regLog) ? "#" : "")
				+ returnStatus
				+ "["
				+ DateTimeUtil.DateToStringAll(DateTimeUtil
						.getJavaUtilDateNow()) + "][" + mac + "][" + v + "]["
				+ ip + "][" + code + "]";
		dr.setRegLog(regLog);
		userDAO.saveObject(dr);
	}

	public String updateDeskRegister(String code, String sn, String mac,
			String v, String ip) {
		String chkStatus = this.checkDeskParameter("register()", code, sn, mac,
				v, ip);
		if (ParamUtils.chkString(chkStatus)) {
			if (!ParamUtils.chkString(sn)) {
				log.error("## register.DESK_DESTROY_EXEC>delete sn is null:"
						+ sn + ",mac:" + mac);
				return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
			} else {
				return this.getReturnData(chkStatus, null);
			}
		}

		if (this.checkDelSns(sn)) {
			log.error("## register.DESK_DESTROY_EXEC>delete from list:" + sn
					+ ",mac:" + mac);
			return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
		}

		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr == null) {// 无此序列号
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}

		String snStatus = dr.getStatus();
		if (!ParamUtils.chkString(snStatus)) {
			this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID, mac,
					v, ip, code);
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}

		if (snStatus.equals(RamConstants.DESK_SN_STATUS_FREEZE)) {
			this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID, mac,
					v, ip, code);
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}
		if (snStatus.equals(RamConstants.DESK_SN_STATUS_DELETE)) {
			log.error("## register.DESK_DESTROY_EXEC>delete:" + sn + ",mac:"
					+ mac);
			this.updateDeskRegisterLog(dr, RamConstants.DESK_DESTROY_EXEC, mac,
					v, ip, code);
			return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
			// return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}

		String returnData = null;
		String rmac = dr.getRegMac();
		String rcode = dr.getSoftwareCode();
		if (!ParamUtils.chkString(rcode)) {
			this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID, mac,
					v, ip, code);
			return this.getReturnData(RamConstants.DESK_SN_INVALID, returnData);// rcode
																				// =
																				// code;
		}
		if (this.checkTpoCode(rcode)) {
			if (!this.checkTpoCode(code)) {
				this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID,
						mac, v, ip, code);
				return this.getReturnData(RamConstants.DESK_SN_INVALID,
						returnData);
			}
		} else {
			if (!rcode.equals(code)) {
				this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID,
						mac, v, ip, code);
				return this.getReturnData(RamConstants.DESK_SN_INVALID,
						returnData);
			}
		}
		Integer regcount = dr.getRegCount();
		if (!ParamUtils.chkInteger(regcount))
			regcount = 0;

		// if(regcount>=RamConstants.getDeskRegNum()){//max
		// this.updateDeskRegisterLog(dr, RamConstants.DESK_REGISTER_MAX, mac,
		// v, ip, code);
		// return this.getReturnData(RamConstants.DESK_SN_INVALID,null);
		// }

		String snflag = dr.getSnflag();
		if (!ParamUtils.chkString(snflag))
			snflag = "0";
		if (Integer.valueOf(snflag) >= 1) {
			// log.error("###:regCount:"+regcount);
			if (snflag.equals("2")) {// 允许注册一次
				if (regcount >= 1) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("3")) {// 允许注册2次
				if (regcount >= 2) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("4")) {// 允许注册3次
				if (regcount >= 3) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("5")) {// 允许注册4次
				if (regcount >= 4) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("6")) {// 允许注册5次
				if (regcount >= 5) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("7")) {// 允许注册6次
				if (regcount >= 6) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("8")) {// 允许注册7次
				if (regcount >= 7) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			} else if (snflag.equals("9")) {// 允许注册8次
				if (regcount >= 8) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_SN_INVALID, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			}
		} else {
			if (!ParamUtils.chkString(mac)) {// mac地址为空允许注册一次
				if (regcount > 0) {
					this.updateDeskRegisterLog(dr,
							RamConstants.DESK_EMPTY_MAC_OK, mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							null);
				}
			}
		}

		if (ParamUtils.chkString(rmac)) {// mac已注册

			String rv = dr.getRegVer();
			if (!ParamUtils.chkString(rv))
				rv = "";

			String matchStatus = "";
			if (this.checkMacTrue(rmac, mac)) {// 匹配
				matchStatus = RamConstants.DESK_REGISTER_SUCCESS_ALREADY;
				DeskSoftware ds = userDAO.getDeskSoftware(rcode);
				if (ds != null) {
					// //returnData =
					// (RamConstants.getResourceType().equals("all")?ds.getResourceListAll():ds.getResourceList());
					// if(RamConstants.getResourceType().equals("all")){
					// if(v.equals("v3.0")){
					// returnData = ds.getResourceList3();
					// }else{
					// returnData = ds.getResourceListAll();
					// }
					// }else{
					// returnData = ds.getResourceList();
					// }
					returnData = this.getDeskSoftwareVersionResource(rcode,
							code, v);

					if (!ParamUtils.chkString(returnData))
						matchStatus = RamConstants.DESK_REGISTER_PAUSE;
				} else {
					// matchStatus = RamConstants.DESK_PARAM_INCORRENT;
					matchStatus = RamConstants.DESK_SN_INVALID;
				}
			} else {
				matchStatus = RamConstants.DESK_MAC_NOT_MATCH;
			}

			if (matchStatus.equals(RamConstants.DESK_REGISTER_SUCCESS_ALREADY)) {
				dr.setStatus(RamConstants.DESK_SN_STATUS_USED);
				if (!rv.equals(v)) {
					dr.setRegVer(v);
				}
			}
			Integer regCount = dr.getRegCount();
			if (!ParamUtils.chkInteger(regCount))
				regCount = 0;
			dr.setRegCount(regCount + 1);
			String regIps = dr.getRegIp();
			if (ParamUtils.chkString(regIps) && regIps.startsWith("10.8.74.1")) {// 重写新客户ip
				dr.setRegIp(ip);
			}
			userDAO.saveObject(dr);
			this.updateDeskRegisterLog(dr, matchStatus, mac, v, ip, code);
			if (matchStatus.equals(RamConstants.DESK_REGISTER_SUCCESS_ALREADY)
					&& rmac.indexOf(",") > 0) {
				return this.getReturnDataMacs(matchStatus, returnData, rmac);
			} else {
				return this.getReturnData(matchStatus, returnData);
			}
		} else {// 首次注册
			boolean chkCount = true;// userDAO.checkDeskRegisterMacOnly(mac,code);
			log.fatal("##>mac+software.chkCount:" + chkCount);
			if (!chkCount) {// mac地址已经注册过了，非法注册
				this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID,
						mac, v, ip, code);
				return this.getReturnData(RamConstants.DESK_SN_INVALID,
						returnData);
			} else {
				Integer regCount = dr.getRegCount();
				if (!ParamUtils.chkInteger(regCount))
					regCount = 0;
				dr.setRegMac(this.filterInvalidMacs(mac));
				dr.setRegVer(v);
				if (!ParamUtils.chkString(dr.getSoftwareCode()))
					dr.setSoftwareCode(rcode);
				dr.setRegIp(ip);
				if (dr.getRegTime() == null) {
					dr.setRegTime(DateTimeUtil.getJavaUtilDateNow());
				}
				dr.setStatus(RamConstants.DESK_SN_STATUS_USED);
				dr.setRegCount(regCount + 1);
				userDAO.saveObject(dr);
				DeskSoftware ds = userDAO.getDeskSoftware(rcode);
				if (ds != null) {
					// //returnData =
					// (RamConstants.getResourceType().equals("all")?ds.getResourceListAll():ds.getResourceList());
					// if(RamConstants.getResourceType().equals("all")){
					// if(v.equals("v3.0")){
					// returnData = ds.getResourceList3();
					// }else{
					// returnData = ds.getResourceListAll();
					// }
					// }else{
					// returnData = ds.getResourceList();
					// }
					returnData = this.getDeskSoftwareVersionResource(rcode,
							code, v);

					if (ParamUtils.chkString(returnData)) {
						this.updateDeskRegisterLog(dr,
								RamConstants.DESK_REGISTER_SUCCESS, mac, v, ip,
								code);
						return this.getReturnData(
								RamConstants.DESK_REGISTER_SUCCESS, returnData);
					} else {
						this.updateDeskRegisterLog(dr,
								RamConstants.DESK_REGISTER_PAUSE, mac, v, ip,
								code);
						return this.getReturnData(
								RamConstants.DESK_REGISTER_PAUSE, null);
					}
				} else {
					this.updateDeskRegisterLog(dr, RamConstants.DESK_SN_INVALID
							+ "[!code]", mac, v, ip, code);
					return this.getReturnData(RamConstants.DESK_SN_INVALID,
							returnData);
				}
			}
		}

	}

	public String updateAndGetAutoSN(String code, String sn, String sntype,
			String status, String mac, String v, String ip) {
		log.fatal((new StringBuilder("##getAutoSN()>code:")).append(code)
				.append(",sn:").append(sn).append(",mac:").append(mac)
				.append(",v:").append(v).append(",ip:").append(ip).toString());
		if (code.equals("CET4MK")) {
			DeskRegister dr = updateAndGetDeskRegisterAutoSN(code, sntype,
					status, mac);
			if (dr != null) {
				dr.setStatus("2");
				dr.setRegMac(mac);
				userDAO.saveObject(dr);
				return getReturnDataValues("111", dr.getSerialNumber(),
						dr.getSerialNumber());
			} else {
				return getReturnDataValues("102", null, null);
			}
		} else {
			return getReturnDataValues("102", null, null);
		}
	}

	public String updateAndGetFreeSN(String code, String sn, String sntype,
			String status, String mac, String v, String ip) {
		log.fatal((new StringBuilder("##getFreeSN()>code:")).append(code)
				.append(",sn:").append(sn).append(",mac:").append(mac)
				.append(",v:").append(v).append(",ip:").append(ip).toString());
		if (code.equals("CET4MK")) {
			DeskRegister dr = updateAndGetDeskRegisterAutoSN(code, sntype,
					status, mac);
			if (dr != null) {
				dr.setStatus("2");
				dr.setRegMac(mac);
				userDAO.saveObject(dr);
				return dr.getSerialNumber();
			} else {
				return "0";
			}
		} else {
			return "0";
		}
	}

	public User updateDeskUserregister(String sn) {
		User user = userDAO.getUserByLoginName(sn);
		if (user == null) {
			Date now = DateTimeUtil.getJavaUtilDateNow();
			user = new User();
			user.setLoginName(sn);
			user.setUserName(sn);
			user.setPassword("111111");
			user.setRegistDateTime(now);
			user.setStatus(RamConstants.STATUS_INVALID);// 桌面提交用户默认为无效
			user.setUserType(RamConstants.UserTypeIsNewUser6);

			user.setPicture(RamConstants.getUserDefaultPicture(
					user.getGender(), "picture", ""));
			user.setLogo(RamConstants.getUserDefaultPicture(user.getGender(),
					"logo", ""));
			user.setLogoMini(RamConstants.getUserDefaultPicture(
					user.getGender(), "logoMini", ""));
			userDAO.saveObject(user);
		}
		return user;
	}

	public DeskRegister updateAndGetDeskRegisterAutoSN(String code,
			String sntype, String status, String mac) {
		if (!ParamUtils.chkString(mac))
			return null;
		DeskRegister dr = userDAO.getDeskRegisterByCodeMac(code, mac);
		if (dr != null)
			return dr;
		String macs[] = mac.split(",");
		StringBuffer whereSql = new StringBuffer();
		whereSql.append((new StringBuilder(" where dr.softwareCode='"))
				.append(code).append("' and (").toString());
		for (int i = 0; i < macs.length; i++)
			whereSql.append((new StringBuilder(String.valueOf(i <= 0 ? ""
					: " or "))).append("dr.regMac like '")
					.append(macs[i].trim()).append("'").toString());

		whereSql.append(")");
		dr = userDAO.getDeskRegisterByCodeLike(code, whereSql.toString());
		if (dr == null)
			dr = createDeskSNOne(code, sntype, status);
		return dr;
	}

	public String filterInvalidMacs(String frommacs) {
		if (!ParamUtils.chkString(frommacs))
			return frommacs;
		try {
			String imacs = ManageFile.loadTextFile(RamConstants
					.getWebRootPath() + "/deskinc/invalidmacs.d");
			if (ParamUtils.chkString(imacs)) {
				String[] imacarr = imacs.split(",");
				for (int i = 0; i < imacarr.length; i++) {
					String[] ms = imacarr[i].split("#");
					frommacs = frommacs.replace(ms[0], ms[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return frommacs;
	}

	public boolean checkDelSns(String sn) {
		boolean isDel = false;
		try {
			String isns = ManageFile.loadTextFile(RamConstants.getWebRootPath()
					+ "/deskinc/delsns.d");
			if (ParamUtils.chkString(isns)) {
				if (isns.indexOf(sn) >= 0)
					isDel = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDel;
	}

	public String getDeskSoftwareVersionResource(String softwareCode,
			String parSoftwareCode, String v) {
		DeskSoftwareVersion dsv = null;
		if (!softwareCode.equals(parSoftwareCode)
				&& parSoftwareCode.indexOf("SN") > 0) {
			dsv = userDAO.getDeskSoftwareVersion(parSoftwareCode, v);
		} else {
			dsv = userDAO.getDeskSoftwareVersion(softwareCode, v);
		}

		String resource = "";
		if (dsv != null) {
			resource = dsv.getResourceList();
		}
		return resource;
	}

	public boolean checkTpoCode(String code) {
		if (ParamUtils.chkString(code) && code.startsWith("TMK")) {
			return true;
		} else {
			return false;
		}
	}

	public String updateAndCheckDeskRegister(String code, String sn,
			String mac, String v, String ip) {
		log.fatal("##" + "check()" + ">code:" + code + ",sn:" + sn + ",mac:"
				+ mac + ",v:" + v + ",ip:" + ip);
		// if(this.checkDeskIgnoreSN(sn)){
		// return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
		// }

		// String chkStatus = this.checkDeskParameter("check()",code,sn, mac, v,
		// ip);
		// if(ParamUtils.chkString(chkStatus)){
		// log.fatal("##[tip]:Ignore check() method>"+sn);
		// //return this.getReturnData(chkStatus, null);
		// }

		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr == null) {
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}

		// 先保存checkCount
		Integer checkCount = dr.getCheckCount();
		if (!ParamUtils.chkInteger(checkCount))
			checkCount = 0;
		dr.setCheckCount(checkCount + 1);
		userDAO.saveObject(dr);

		// if(1==1){//先全部放开check方法
		return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
		// }

		// String snStatus = dr.getStatus();
		// if(!ParamUtils.chkString(snStatus)) return
		// this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		// if(snStatus.equals(RamConstants.DESK_SN_STATUS_FREEZE)){
		// return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		// }
		// if(snStatus.equals(RamConstants.DESK_SN_STATUS_DELETE)){
		// return this.getReturnData(RamConstants.DESK_DESTROY_EXEC,null);
		// }
		//
		// String rmac = dr.getRegMac();
		// if(ParamUtils.chkString(rmac) && this.checkMacTrue(rmac, mac)){//已注册过
		// 比较mac地址
		// return this.getReturnData(RamConstants.DESK_NO_ACTION,null);
		// }else{
		// return this.getReturnData(RamConstants.DESK_REGISTER_INVALID,null);
		// }
	}

	public String updateAndCheckDeskVerify(String code, String sn, String mac,
			String v, String ip) {

		String chkStatus = this.checkDeskParameter("verify()", code, sn, mac,
				v, ip);
		if (ParamUtils.chkString(chkStatus)) {
			return this.getReturnData(chkStatus, null);
		}

		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr == null) {
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}

		String snStatus = dr.getStatus();
		if (!ParamUtils.chkString(snStatus))
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		if (snStatus.equals(RamConstants.DESK_SN_STATUS_FREEZE)) {
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}
		if (snStatus.equals(RamConstants.DESK_SN_STATUS_DELETE)) {
			return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
		}

		String rmac = dr.getRegMac();
		if (ParamUtils.chkString(rmac) && this.checkMacTrue(rmac, mac)) {// 已注册过
																			// 比较mac地址
			DeskSoftware soft = (DeskSoftware) userDAO.getDeskSoftware(dr
					.getSoftwareCode());
			String latestVer = soft.getLatestVersion();
			String updateExec = soft.getUpdateExec();
			if (!ParamUtils.chkString(latestVer))
				latestVer = "1.0";
			if (!ParamUtils.chkString(updateExec))
				updateExec = "0";
			log.fatal("##v=" + latestVer + ",vfrom=" + v + ",updateExec="
					+ updateExec);
			if (updateExec.equals("1")
					&& RamConstants.DESK_UPDATE_STATUS.equals("1")) {// 可以升级
				if (this.checkDeskVersionUpdate(latestVer, v)) {
					return RamConstants.DESK_UPDATE_EXEC;
				}
			}
			// 到期
			return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
		} else {
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}
	}

	public String updateAndCheckDeskLaunch(String code, String sn, String mac,
			String v, String ip) {

		String chkStatus = this.checkDeskParameter("launch()", code, sn, mac,
				v, ip);
		if (ParamUtils.chkString(chkStatus)) {
			return this.getReturnData(chkStatus, null);
		}

		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr == null) {
			// return this.getReturnData(RamConstants.DESK_SN_INVALID,null);
			log.fatal("##launch.DESK_DESTROY_EXEC.sn not exits>code:" + code
					+ ",sn:" + sn + ",mac:" + mac + ",v:" + v + ",ip:" + ip);
			return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
		}

		// 先保存launch count
		Integer launchCount = dr.getLaunchCount();
		String sver = dr.getRegVer();
		if (!ParamUtils.chkInteger(launchCount))
			launchCount = 0;
		dr.setLaunchCount(launchCount + 1);
		dr.setLaunchTime(DateTimeUtil.getJavaUtilDateNow());
		if (!sver.equals(v)) {
			String chkverfree = dr.getCheckverfree();
			if (ParamUtils.chkString(chkverfree) && chkverfree.equals("1")) {
				log.fatal((new StringBuilder("##Launch.updateVersion>"))
						.append(sver).append(" to ").append(v).toString());
				dr.setRegVer(v);
			}
		}
		userDAO.saveObject(dr);

		String snflag = dr.getSnflag();
		String snStatus = dr.getStatus();
		String badmac = dr.getBadmac();

		if (ParamUtils.chkString(snflag) && snflag.equals("1")) {// 忽略检查
			log.error("##:not get mac but pass_launch:" + sn);
			return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
		}
		if (!ParamUtils.chkString(snStatus))
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		if (snStatus.equals(RamConstants.DESK_SN_STATUS_FREEZE)) {
			return getReturnDataValues(RamConstants.DESK_STOP_USE,
					"Error 120.", "http://www.ugaid.com/dr/alert/error.html");
			// return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}
		if (snStatus.equals(RamConstants.DESK_SN_STATUS_DELETE)) {
			log.error("## launch.DESK_DESTROY_EXEC>delete:" + sn + ",mac:"
					+ mac);
			this.updateDeskRegisterLog(dr, RamConstants.DESK_DESTROY_EXEC
					+ "[launch.status]", mac, v, ip, code);
			return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
		}
		if (ParamUtils.chkString(badmac) && ParamUtils.chkString(mac)) {// 非法mac
			if (this.checkMacTrue(badmac, mac)) {// 包括非法mac执行删除操作
				log.error("## DESK_DESTROY_EXEC_badmac:" + sn + ",mac:" + mac);
				this.updateDeskRegisterLog(dr, RamConstants.DESK_DESTROY_EXEC
						+ "[launch.badmac]", mac, v, ip, code);
				return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
			}
		}

		String rmac = dr.getRegMac();
		if (ParamUtils.chkString(rmac) && this.checkMacTrue(rmac, mac)) {// 已注册过
																			// 比较mac地址
			DeskSoftware soft = (DeskSoftware) userDAO.getDeskSoftware(dr
					.getSoftwareCode());
			String latestVer = soft.getLatestVersion();
			String updateExec = soft.getUpdateExec();
			if (!ParamUtils.chkString(latestVer))
				latestVer = "1.0";
			if (!ParamUtils.chkString(updateExec))
				updateExec = "0";
			log.fatal("##v=" + latestVer + ",vfrom=" + v + ",updateExec="
					+ updateExec);
			if (updateExec.equals("1")
					&& RamConstants.DESK_UPDATE_STATUS.equals("1")) {// 可以升级
				if (this.checkDeskVersionUpdate(latestVer, v)) {
					return RamConstants.DESK_UPDATE_EXEC;
				}
			}
			// 到期
			return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
		} else {
			if (ParamUtils.chkString(this.checkDeskIgnoreSN(sn))) {
				return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
			}
			return this.getReturnData(RamConstants.DESK_SN_INVALID, null);
		}
	}

	public String updateMobileLaunch(String code, String sn, String mac,
			String v, String ip) {
		String configs = ManageFile.loadTextFile((RamConstants.getWebRootPath()
				+ "/deskinc/launchConfig_" + code + ".d"));
		log.fatal(">>mobileLaunch:[code:" + code + "][sn:" + sn + "][v:" + v
				+ "][return:" + configs + "]" + "<" + ip);
		return configs;
	}

	public String updateMobileTrial(String code, String sn, String mac,
			String v, String ip) {
		String configs = ManageFile.loadTextFile((RamConstants.getWebRootPath()
				+ "/deskinc/launchConfig_" + code + ".d"));
		log.fatal(">>mobileTrial:[code:" + code + "][sn:" + sn + "][v:" + v
				+ "][return:" + configs + "]" + "<" + ip);

		// String chkStatus = this.checkDeskParameter("trial()",code,sn, mac,
		// v,ip);
		// if(ParamUtils.chkString(chkStatus)){
		// return this.getReturnData(chkStatus, null);
		// }

		MobileTrial trial = userDAO.getMobileTrial(code, sn);
		Date now = DateTimeUtil.getJavaUtilDateNow();
		Integer count = 0;
		if (trial != null) {
			count = trial.getLaunchCount();
			if (!ParamUtils.chkInteger(count))
				count = 0;
			trial.setUserIp(ip);
			trial.setLastTime(now);
		} else {
			trial = new MobileTrial();
			trial.setSoftwareCode(code);
			trial.setUserMac(sn);
			trial.setUserIp(ip);
			trial.setFirstTime(now);
			trial.setLastTime(now);
			// userDAO.updateDeskSoftwareTrialCount(code, 1);
		}
		trial.setVers(v);
		trial.setLaunchCount(count + 1);
		userDAO.saveObject(trial);

		return configs;
	}

	public String updateAndCheckDeskTrialVersion(String code, String sn,
			String mac, String v, String ip) {

		// if(this.checkDeskDestroySoftware(sn, mac, v)){
		// return this.getReturnData(RamConstants.DESK_DESTROY_EXEC,null);
		// }

		String chkStatus = this.checkDeskParameter("trial()", code, sn, mac, v,
				ip);
		if (ParamUtils.chkString(chkStatus)) {
			return this.getReturnData(chkStatus, null);
		}

		DeskTrial trial = userDAO.getDeskTrial(code, mac);
		Date now = DateTimeUtil.getJavaUtilDateNow();
		Integer count = 0;
		if (trial != null) {
			count = trial.getLaunchCount();
			if (!ParamUtils.chkInteger(count))
				count = 0;
			trial.setUserIp(ip);
			trial.setLastTime(now);
		} else {
			trial = new DeskTrial();
			trial.setSoftwareCode(code);
			trial.setUserMac(mac);
			trial.setUserIp(ip);
			trial.setFirstTime(now);
			trial.setLastTime(now);
			userDAO.updateDeskSoftwareTrialCount(code, 1);
		}
		trial.setVers(v);
		trial.setLaunchCount(count + 1);
		userDAO.saveObject(trial);

		String usestatus = trial.getUsestatus();
		if (ParamUtils.chkString(usestatus)) {
			if (usestatus.equals(RamConstants.DESK_SN_STATUS_FREEZE)) {
				return getReturnDataValues(RamConstants.DESK_STOP_USE,
						"Error 120.",
						"http://www.ugaid.com/dr/alert/error_trial.html");
			}
			if (usestatus.equals(RamConstants.DESK_SN_STATUS_DELETE)) {
				return this.getReturnData(RamConstants.DESK_DESTROY_EXEC, null);
			}
		}

		DeskSoftware soft = (DeskSoftware) userDAO.getDeskSoftware(code);
		if (soft != null) {
			String latestVer = soft.getLatestVersion();
			String updateExec = soft.getUpdateExec();
			if (!ParamUtils.chkString(latestVer))
				latestVer = "1.0";
			if (!ParamUtils.chkString(updateExec))
				updateExec = "0";
			log.fatal("##v=" + latestVer + ",vfrom=" + v + ",updateExec="
					+ updateExec + ",launchCount:" + (count + 1));
			if (updateExec.equals("1")
					&& RamConstants.DESK_UPDATE_STATUS.equals("1")) {// 可以升级
				if (this.checkDeskVersionUpdate(latestVer, v)) {
					return this.getReturnData(RamConstants.DESK_UPDATE_EXEC,
							null);
				}
			}
		}

		// only test code
		// if(code.equals("GREMKSY") && ParamUtils.chkString(mac) &&
		// mac.indexOf("78-2B-CB-86-7B-9A")>=0){
		// return this.getReturnData(RamConstants.DESK_DEADLINE_EXEC,null);
		// }

		return this.getReturnData(RamConstants.DESK_NO_ACTION, null);
	}

	public DeskRegister getDeskRegister(String sn) {
		return userDAO.getDeskRegister(sn);
	}

	public List<DeskSoftware> findDeskSoftwareList(String where) {
		return userDAO.findDeskSoftwareList(where);
	}

	public Integer getDeskRegisterSell(String sntype) {
		return userDAO.getDeskRegisterSell(sntype);
	}

	public Integer getDeskRegisterSell2(String sntype) {
		return userDAO.getDeskRegisterSell2(sntype);
	}

	public Integer getDeskRegisterSellOfUser(String sntype, Integer userId) {
		return userDAO.getDeskRegisterSellOfUser(sntype, userId);
	}

	public Integer getDeskRegisterSellJoin(String sntype) {
		return userDAO.getDeskRegisterSellJoin(sntype);
	}

	public Integer getDeskRegisterSellBySql(String hsql, List<Object> pars) {
		return userDAO.getDeskRegisterSellBySql(hsql, pars);
	}

	public Integer getDeskRegisterSellBySqlJoin(String hsql, List<Object> pars) {
		return userDAO.getDeskRegisterSellBySqlJoin(hsql, pars);
	}

	public DeskSoftware getDeskSoftware(String softwareCode) {
		return userDAO.getDeskSoftware(softwareCode);
	}

	public DeskSoftwareVersion getDeskSoftwareVersion(Integer id) {
		return (DeskSoftwareVersion) userDAO.getObject(
				DeskSoftwareVersion.class, id);
	}

	public void saveDeskSoftwareVersion(DeskSoftwareVersion dsv) {
		userDAO.saveObject(dsv);
	}

	public void saveDeskRegister(DeskRegister dr) {
		userDAO.saveObject(dr);
	}

	// 检查版本高低v:数据库版本
	public boolean checkDeskVersionUpdate(String v, String vfrom) {
		if (!ParamUtils.chkString(v) || !ParamUtils.chkString(vfrom))
			return false;
		v = v.replace("T", "");
		v = v.replace("v", "");
		vfrom = vfrom.replace("T", "");
		vfrom = vfrom.replace("v", "");
		try {
			double sv = new Double(v).doubleValue();
			double svfrom = new Double(vfrom).doubleValue();
			return sv > svfrom ? true : false;
		} catch (Exception e) {
			log.error("## version code error.");
			return false;
		}
	}

	// 检查参数
	public String checkDeskParameter(String method, String code, String sn,
			String mac, String v, String ip) {
		log.fatal("##" + method + ">code:" + code + ",sn:" + sn + ",mac:" + mac
				+ ",v:" + v + ",ip:" + ip);

		String snflag = this.checkDeskIgnoreSN(sn);
		if (ParamUtils.chkString(snflag)) {
			return null;
		}
		// && ParamUtils.chkString(mac)
		if (ParamUtils.chkString(code) && ParamUtils.chkString(sn)
				&& ParamUtils.chkString(v)) {
			if (sn.length() != RamConstants.DESK_SN_LENGTH) {
				return RamConstants.DESK_SN_INVALID;
			}
			v = v.replace("v", "");
			v = v.replace("g", "");
			try {
				new Double(v).doubleValue();
			} catch (Exception e) {
				// return RamConstants.DESK_PARAM_INCORRENT;
				return RamConstants.DESK_SN_INVALID;
			}
			return null;
		} else {
			// return RamConstants.DESK_PARAM_INCORRENT;
			return RamConstants.DESK_SN_INVALID;
		}
	}

	public String checkDeskIgnoreSN(String sn) {
		// String ignoreList = RamConstants.getDeskIgnoreSnList();
		// if(ParamUtils.chkString(ignoreList)){
		// if(ignoreList.indexOf(sn)>=0){
		// return true;
		// }
		// }
		DeskRegister dr = userDAO.getDeskRegister(sn);
		if (dr != null) {
			String snflag = dr.getSnflag();
			if (!ParamUtils.chkString(snflag))
				snflag = "0";
			if (Integer.valueOf(snflag) >= 1) {
				// if(snflag.equals("1")){
				log.error("##:not get mac but pass:" + sn + ",flag num:"
						+ snflag);
				// }
				return snflag;
			}
		}
		return null;
	}

	// 检查sn
	public boolean checkDeskSerialNumber(String sn) {
		if (!ParamUtils.chkString(sn))
			return false;
		sn = sn.trim();
		if (sn.length() == 12) {
			return true;
		} else {
			return false;
		}
	}

	// 摧毁程序
	public boolean checkDeskDestroySoftware(String sn, String mac, String v) {
		boolean destroy = false;
		String destroyList = null;// RamConstants.getDeskDestroySnList();
		if (ParamUtils.chkString(destroyList)) {
			if (destroyList.indexOf(sn) >= 0) {
				destroy = true;
			}
		}
		if (destroy)
			log.info("## desotry sn:" + sn + ",mac=" + mac + ",v=" + v);
		return destroy;
	}

	public String getReturnData(String returnStatus, String returnData) {
		return this.getReturnDataHandle(returnStatus, returnData, null, null,
				null);
	}

	public String getReturnDataMacs(String returnStatus, String returnData,
			String returnMacs) {
		return this.getReturnDataHandle(returnStatus, returnData, returnMacs,
				null, null);
	}

	public String getReturnDataValues(String returnStatus, String values) {
		return this.getReturnDataHandle(returnStatus, null, null, values, null);
	}

	public String getReturnDataValues(String returnStatus, String values,
			String urls) {
		return getReturnDataHandle(returnStatus, null, null, values, urls);
	}

	public String getReturnDataHandle(String returnStatus, String returnData,
			String returnMacs, String values, String urls) {
		log.fatal(">>returnStatus:" + returnStatus);
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append("<status>" + returnStatus + "</status>");

		if (ParamUtils.chkString(returnMacs)) {
			sb.append("<macs>" + returnMacs + "</macs>");
		}
		if (ParamUtils.chkString(values)) {
			sb.append("<value>" + values + "</value>");
		}
		if (ParamUtils.chkString(urls))
			sb.append((new StringBuilder("<url>")).append(urls)
					.append("</url>").toString());

		if (ParamUtils.chkString(returnData)) {
			sb.append("<papers>");
			sb.append(returnData);
			sb.append("</papers>");
		}
		sb.append("</root>");
		return sb.toString();
	}

	// //////////////////////////////////
	public String getGenTestPaperList() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append("<status>" + "100" + "</status>");
		sb.append("<papers>");

		sb.append("<paper><pid>11</pid><title>TEST 1</title><dataxml>gt11.xml</dataxml><testtime>60</testtime><paperdesc>仅用于测试</paperdesc></paper>");
		sb.append("<paper><pid>12</pid><title>TEST 2</title><dataxml>gt12.xml</dataxml><testtime>60</testtime><paperdesc>仅用于测试</paperdesc></paper>");
		sb.append("<paper><pid>13</pid><title>TEST 3</title><dataxml>gt13.xml</dataxml><testtime>60</testtime><paperdesc>仅用于测试</paperdesc></paper>");
		sb.append("<paper><pid>14</pid><title>TEST 4</title><dataxml>gt14.xml</dataxml><testtime>60</testtime><paperdesc>仅用于测试</paperdesc></paper>");
		sb.append("<paper><pid>15</pid><title>TEST 5</title><dataxml>gt15.xml</dataxml><testtime>60</testtime><paperdesc>仅用于测试</paperdesc></paper>");

		sb.append("</papers>");

		sb.append("</root>");
		return sb.toString();
	}

	public String getGenTestPaperXml(String pid) {
		String[] pids = pid.split(",");
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append("<status>" + "100" + "</status>");
		sb.append("<papers>");

		for (int i = 0; i < pids.length; i++) {
			String xmls = ManageFile.loadTextFile(RamConstants.getWebRootPath()
					+ "/gt/xmldata/gt" + pids[i] + ".xml");
			sb.append(xmls);
		}

		sb.append("</papers>");
		sb.append("</root>");
		return sb.toString();
	}

	// ////////////////////////////////////
	public void updateUserPoints(Integer userId, BigDecimal points) {
		if (points != null) {
			User user = (User) userDAO.getObject(User.class, userId);
			if (user != null) {
				BigDecimal up = user.getUserpoints();
				up = ProductUtil.checkBigDecimal(up);
				user.setUserpoints(up.add(points));
				userDAO.saveObject(user);
				log.info("## 用户[" + user.getUserName() + "][" + userId + "]刚才="
						+ up + ",积分+" + points + ",现有=" + user.getUserpoints());
			} else {
				log.error("## 用户增加积分失败，未找到用户信息。userId=" + userId + ",points="
						+ points);
			}
		}
	}

	public User updateUserPointsAndReturn(Integer userId, BigDecimal points) {
		if (points != null) {
			User user = (User) userDAO.getObject(User.class, userId);
			user.setUserpoints(user.getUserpoints().add(points));
			userDAO.saveObject(user);
			log.info("## 用户[" + user.getUserName() + "][" + userId + "]积分+"
					+ points + ",userPoints=" + user.getUserpoints());
			return user;
		}
		return null;
	}

	public String updateDeskTrialData(Integer deskTrialId, String usestatus) {
		DeskTrial trial = (DeskTrial) userDAO.getObject(DeskTrial.class,
				deskTrialId);
		if (trial != null) {
			trial.setUsestatus(usestatus);
			userDAO.saveObject(trial);
			return "1";
		}
		return "0";
	}

	public PaginationSupport findUser(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return userDAO.findUser(hqls, para, startIndex, pageSize);
	}

	public boolean checkUserEmailExist(String email) {
		return userDAO.checkUserEmailExist(email);
	}

	public User saveUserRegister(User userForm) {
		User regUser = new User();
		regUser.setLoginName(userForm.getLoginName());
		regUser.setUserName(userForm.getLoginName());
		regUser.setPassword(userForm.getPassword());
		regUser.setUserEmail(userForm.getUserEmail());
		regUser.setCellPhone(userForm.getCellPhone());

		regUser.setUserType(Constants.USER_TYPE_SUER);
		regUser.setStatus(Constants.PUB_STATUS_OPEN);
		regUser.setRegistDateTime(DateTimeUtil.getNowSQLDate());

		regUser.setLogo(Constants.DEFAULT_LOGO);
		regUser.setLogoMini(Constants.DEFAULT_LOGO_MINI);
		User user = (User) userDAO.saveObjectDB(regUser);
		return user;
	}
	
	public User saveUserReg(User user,BigDecimal points) {
		userDAO.saveObject(user);
		
		//更新余额和明细
		this.savePointDetail(user.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.POINT_TYPE_REGISTER, points, null,"");
		return user;
	}

	public void delUserAbsolute(Integer userId, User operator) {
		userDAO.delUserAbsolute(userId, operator);
	}

	public void modifyLogo(User user, String logoSrc, String logoMiniSrc) {
		String logoPath = user.getLogo();
		String logoMiniPath = user.getLogoMini();
		// 修改成现在的头像
		user.setLogo(logoSrc);
		user.setLogoMini(logoMiniSrc);
		userDAO.saveUser(user, user);
		// 删除原来的头像
		if (!logoPath.equals(Constants.DEFAULT_LOGO)) {
			DeleteFolder.deleteFile(Constants.getWebRootPath() + logoPath);
		}
		if (!logoMiniPath.equals(Constants.DEFAULT_LOGO_MINI)) {
			DeleteFolder.deleteFile(Constants.getWebRootPath() + logoMiniPath);
		}

	}

	public String updateDeviceToken(String deviceToken, String appversion,
			String ip) {
		try {
			// GzlmToken token = userDAO.getGzlmToken(deviceToken, appversion);
			// if(token==null){
			// token = new GzlmToken();
			// token.setDeviceToken(deviceToken);
			// }
			// token.setAppversion(appversion);
			// token.setRegip(ip);
			// token.setUpdateTime(DateTimeUtil.getJavaUtilDateNow());
			// userDAO.saveObject(token);
			//
			// String log = "##[token write][" +
			// DateTimeUtil.getJavaUtilDateNow() +
			// "deviceToken="+deviceToken+",appversion="+appversion+",ip="+ip+"\n";
			// ManageFile.writeTextToFile(log, RamConstants.getWebRootPath()+
			// "/logs/wlog.log", true);

			return "1";
		} catch (Exception e) {
			e.printStackTrace();

			return "0";
		}

	}

	public List<String> findAllToken() {
		return userDAO.findAllToken();
	}

	public void updateUserLog(HttpServletRequest request, User user,
			String loginAction) {
		// 记录访问者
		String ip = request.getRemoteAddr();
		if (user != null) {
			if (user.getUserId() > 1) {
				UserLog log = new UserLog();
				log.setUserId(user.getUserId());
				log.setLoginName(user.getLoginName());
				log.setIpAddress(ip);
				log.setDateTime(DateTimeUtil.getJavaUtilDateNow());
				log.setActionText(loginAction);
				userDAO.saveObject(log);
			}
		} else {
			UserLog log = new UserLog();
			log.setUserId(0);
			log.setLoginName(request.getSession().getId());
			log.setIpAddress(ip);
			log.setDateTime(DateTimeUtil.getJavaUtilDateNow());
			log.setActionText(loginAction);
			userDAO.saveObject(log);
		}
	}

	public User saveUserCommunity(ManagerForm frm){
		User userFrm = frm.getUser();
		Integer userId = userFrm.getUserId();
		Date nowDate = DateTimeUtil.getJavaUtilDateNow();
		User user =null;
		if(ParamUtils.chkInteger(userId)){
			user = this.getUser(userFrm.getUserId());
			if(!userFrm.getPassword().equals(frm.getPassword())){
				user.setPassword(MD5.exc(frm.getPassword()));
			}
//			if(user.getUserType().equals(Constants.USER_TYPE_USER_CCID)){
				Integer ccid=frm.getCcid();
				frm.getCid();
				user.setCcid(ccid);
				user.setCityId(frm.getCid());
				user.setGender(userFrm.getGender());
				user.setCellPhone(userFrm.getCellPhone());
				user.setAddress(userFrm.getAddress());
//			}
		}else{
			user=new User();
			user.setStatus(Constants.PUB_STATUS_OPEN);
			user.setRegistDateTime(nowDate);		
			String userType=frm.getType();
			user.setUserType(userType);
			user.setLoginName(userFrm.getLoginName());
			user.setPassword(MD5.exc(userFrm.getPassword()));
			user.setGender(userFrm.getGender());
			user.setCellPhone(userFrm.getCellPhone());
			user.setAddress(userFrm.getAddress());
//			if(userType.equals(Constants.USER_TYPE_USER_CCID)){
				Integer ccid=frm.getCcid();
				user.setCcid(ccid);
				user.setCityId(frm.getCid());
//			}
		}

		String userName = userFrm.getUserName();
		user.setUserName(userName);		
		userDAO.saveObject(user);
		return user;
	}

	public User saveUser(ManagerForm frm) {
		User userFrm = frm.getUser();
		Integer userId = userFrm.getUserId();
		Date nowDate = DateTimeUtil.getJavaUtilDateNow();
		User user =null;
		if(ParamUtils.chkInteger(userId)){
			user = this.getUser(userFrm.getUserId());
		}else{
			user=new User();
			user.setStatus(Constants.PUB_STATUS_OPEN);
			user.setRegistDateTime(nowDate);
			/**
			 * add  by raopeng  可能会有问题
			 */
			Integer sid=frm.getSid();
			
			Seller seller=(Seller) userDAO.getObject(Seller.class, sid);
			Integer columnId = seller.getColumnId();
			if(ParamUtils.chkInteger(columnId)){
				Type type=(Type) userDAO.getObject(Type.class, columnId);
				String module = type.getType();
				if(module.equals(Constants.MODULE_EAT)){
					user.setUserType(Constants.USER_TYPE_SELLER_EAT);
				}else if(module.equals(Constants.MODULE_GROUP)){
					user.setUserType(Constants.USER_TYPE_SELLER_BUY);
				}else if(module.equals(Constants.MODULE_STORE)){
					user.setUserType(Constants.USER_TYPE_SELLER_STORE);
				}
			}
			userDAO.saveObject(user);
			user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
			user.setLoginTimes(new Integer(0));
		}
		String userType=user.getUserType();
//		String userType = userFrm.getUserType();//用户类型
		String cellPhone = userFrm.getCellPhone();//用户电话
		String userName = userFrm.getUserName();
		Integer ccid=frm.getCcid();
		
		user.setUserName(userName);
		user.setUserEmail(userFrm.getUserEmail());
		user.setUserType(userType);
		user.setCellPhone(userFrm.getCellPhone());
		String gender = userFrm.getGender();
		if (ParamUtils.chkString(gender)) {
			user.setGender(gender);
		}
		user.setAddress(userFrm.getAddress());
		if (userType.equals(Constants.USER_TYPE_SUER)) {
			Date birthday = userFrm.getBirthday();
			user.setBirthday(birthday);
			user.setCellPhone(cellPhone);//app用户的登录名就是手机
			if(ParamUtils.chkInteger(userId)){
				
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				user.setPassword(MD5.exc(userFrm.getPassword()));
				user = userDAO.saveObjectDB(user);
			}
		}else if(userType.equals(Constants.USER_TYPE_ADMIN_SHANGJIA)
				||userType.equals(Constants.USER_TYPE_SELLER_BUY)
				|| userType.equals(Constants.USER_TYPE_SELLER_EAT)
				|| userType.equals(Constants.USER_TYPE_SELLER_STORE)){
			if(ParamUtils.chkInteger(userId)){
				String password = user.getPassword();
				String password2 = frm.getPassword();
				if(!password.equals(password2)){
					user.setPassword(MD5.exc(password2));
				}
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				user.setPassword(MD5.exc(userFrm.getPassword()));
				user = userDAO.saveObjectDB(user);
			}
//			userId=user.getUserId();//用户id，新建的则返回新建的用户id
//			Integer sid = frm.getSid();
			//保存用户商家关系
//			SellerUserRl sellerUser = sellerDAO.getSellerUser(userId);
//			if(sellerUser!=null){
//				sellerUser.setSid(sid);
//			}else{			
//				sellerUser = new SellerUserRl(sid, userId, "1", userName);
//			}
//			sellerUser.setType(frm.getUserLx());
//			sellerDAO.saveObject(sellerUser);
//			Seller seller=(Seller) sellerDAO.getObject(Seller.class, sid);
//			Integer columnId = seller.getColumnId();
//			if(ParamUtils.chkInteger(columnId)){
//				Type type=(Type) sellerDAO.getObject(Type.class, columnId);
//				String module = type.getType();
//				if(module.equals(Constants.MODULE_EAT)){
//					user.setUserType(Constants.USER_TYPE_SELLER_EAT);
//				}else if(module.equals(Constants.MODULE_GROUP)){
//					user.setUserType(Constants.USER_TYPE_SELLER_BUY);
//				}else if(module.equals(Constants.MODULE_STORE)){
//					user.setUserType(Constants.USER_TYPE_SELLER_STORE);
//				}
//			}
//			userDAO.saveObject(user);
		}else{
			if(ParamUtils.chkInteger(userId)){
				userDAO.saveUser(user, null);
			}else{
				user = userDAO.saveObjectDB(user);
			}
		}
		return user;

	}
	
	public User saveUser(ManagerForm frm,User loginUser) {
		User userFrm = frm.getUser();
		Integer userId = userFrm.getUserId();
		Date nowDate = DateTimeUtil.getJavaUtilDateNow();
		User user =null;
		if(ParamUtils.chkInteger(userId)){
			user = this.getUser(userFrm.getUserId());
		}else{
			user=new User();
			user.setStatus(Constants.PUB_STATUS_OPEN);
			user.setRegistDateTime(nowDate);
			userDAO.saveObject(user);
			user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
			user.setLoginTimes(new Integer(0));
		}
		String userType=user.getUserType();
//		String userType = userFrm.getUserType();//用户类型
		String cellPhone = userFrm.getCellPhone();//用户电话
		String userName = userFrm.getUserName();
		Integer ccid=frm.getCcid();
		
		user.setUserName(userName);
		user.setUserEmail(userFrm.getUserEmail());
		user.setUserType(userType);
		user.setCellPhone(userFrm.getCellPhone());
		String gender = userFrm.getGender();
		if (ParamUtils.chkString(gender)) {
			user.setGender(gender);
		}
		user.setAddress(userFrm.getAddress());
		if (userType.equals(Constants.USER_TYPE_SUER)) {
			Date birthday = userFrm.getBirthday();
			user.setBirthday(birthday);
			user.setCellPhone(cellPhone);//app用户的登录名就是手机
			if(ParamUtils.chkInteger(userId)){
				
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				user.setPassword(MD5.exc(userFrm.getPassword()));
				user = userDAO.saveObjectDB(user);
			}
		}else if(userType.equals(Constants.USER_TYPE_ADMIN_SHANGJIA)
				||userType.equals(Constants.USER_TYPE_SELLER_BUY)
				|| userType.equals(Constants.USER_TYPE_SELLER_EAT)
				|| userType.equals(Constants.USER_TYPE_SELLER_STORE)){
			if(ParamUtils.chkInteger(userId)){
				String password = user.getPassword();
				String password2 = frm.getPassword();
				if(!password.equals(password2)){
					user.setPassword(MD5.exc(password2));
				}
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				user.setPassword(MD5.exc(userFrm.getPassword()));
				user = userDAO.saveObjectDB(user);
			}
		}else{
			if(ParamUtils.chkInteger(userId)){
				userDAO.saveUser(user, null);
			}else{
				user = userDAO.saveObjectDB(user);
			}
		}
		return user;

	}

	
	// add  by  raopeng
	@Override
	public boolean checkUserAccoutValid(Integer userId) {
		if(!ParamUtils.chkInteger(userId)) return false;
		return this.getUserValid(userId)!=null?true:false;
	}

	/**
	 * 获取有效的用户
	 * @param userId
	 * @return
	 */
	public User getUserValid(int userId) {
		User vUser = (User) userDAO.getObject(User.class, userId);
		if(vUser!=null){
			if(vUser.getStatus().equals(RamConstants.USER_ACCOUNT_OPEN)){
				return vUser;
			}
		}
		return null;
	}
	/**
	 * 是否还在当天抽奖时间中
	 * @param nowtime
	 * @return
	 */
	public boolean isBetTimeDuring(Date nowtime){
		Integer nowhms = Integer.valueOf(DateTimeUtil.format(nowtime, "HHmmss"));
		if(nowhms>=RamConstants.PCE_START_TIME_NUMBER && nowhms<=RamConstants.PCE_END_TIME_NUMBER_MAX){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否抽奖时间
	 * 9:00 - 23:50
	 * @param nowtime
	 * @return
	 */
	public boolean isBetTimeOk(Date nowtime){
		Integer nowhms = Integer.valueOf(DateTimeUtil.format(nowtime, "HHmmss"));
		if(nowhms>=RamConstants.PCE_START_TIME_NUMBER && nowhms<RamConstants.PCE_END_TIME_NUMBER){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 判断是否中奖
	 * @param cjnum
	 * @param betResult
	 * @return
	 */
	public boolean isUserBetSuccess(String cjnum,String betResult){
		if(cjnum.equals(RamConstants.PCE_UNIT_DAN)){
			if(Integer.valueOf(betResult) % 2>0){//中奖
				return true;
			}
		}else if(cjnum.equals(RamConstants.PCE_UNIT_SHUANG)){
			if(Integer.valueOf(betResult) % 2==0){//中奖
				return true;
			}
		}else{//数字
			if(betResult.trim().equals(cjnum.trim())){//中奖
				return true;
			}
		}
		return false;
	}
	public BigDecimal getUserPoints(Integer userId){
		User user = (User)userDAO.getObject(User.class, userId);
		BigDecimal points = user.getUserpoints();//user.getUserpoints();
		points = ProductUtil.checkBigDecimal(points);
		return points;
	}
	public Double getCjGameRate(String cjtype,Integer bPoints,String cjnum){
//		if(cjtype.equals(Constants.POINT_TYPE_GAME_12)){
//			String[] arr = cjnum.split(",");
//			int len = arr.length;
//			if(len==1){
//				return RamConstants.PCE_BET_EARN_OF_12_s1;
//			}else if(len==2){
//				return RamConstants.PCE_BET_EARN_OF_12_s2;
//			}else if(len==3){
//				return RamConstants.PCE_BET_EARN_OF_12_s3;
//			}else if(len==4){
//				return RamConstants.PCE_BET_EARN_OF_12_s4;
//			}else if(len==5){
//				return RamConstants.PCE_BET_EARN_OF_12_s5;
//			}else if(len==6){
//				return RamConstants.PCE_BET_EARN_OF_12_s6;
//			}else if(len==7){
//				return RamConstants.PCE_BET_EARN_OF_12_s7;
//			}else if(len==8){
//				return RamConstants.PCE_BET_EARN_OF_12_s8;
//			}else if(len==9){
//				return RamConstants.PCE_BET_EARN_OF_12_s9;
//			}else if(len==10){
//				return RamConstants.PCE_BET_EARN_OF_12_s10;
//			}else if(len==11){
//				return RamConstants.PCE_BET_EARN_OF_12_s11;
//			}else if(len==12){
//				return RamConstants.PCE_BET_EARN_OF_12_s12;
//			}else{
//				return RamConstants.PCE_BET_EARN_OF_12_s1;
//			}
//		}else if(cjtype.equals(Constants.POINT_TYPE_GAME_ZYM)){
//			return bPoints>100?RamConstants.PCE_BET_EARN_OF_ZHUAYUMI_V2:RamConstants.PCE_BET_EARN_OF_ZHUAYUMI;
//		}else{
			return 1.0;
//		}
	}
	/**
	 * 更新用户总账启积分余额
	 * @param userId
	 * @param points
	 */
	public void updateUserPointsOfPublic(Integer userId, BigDecimal points) {
		User user = (User) userDAO.getObject(User.class, userId);
		if (user != null) {
			BigDecimal up = null;
			if(user.getUserpoints() != null){
				up = user.getUserpoints();
			}
			user.setUserpoints(up.add(points));
			userDAO.saveObject(user);
			log.fatal("## 用户[" + user.getUserName() + "][" + userId + "]现在=" + up + ",积分=+" + points + ",更新后=" + user.getUserpoints());
		} else {
			log.error("## 用户增加积分失败，未找到用户信息。userId=" + userId + ",points=" + points);
		}
	}

	@Override
	public String checkUserPermissionLevel(Integer userId) {
		User user = this.getUser(userId);
		if(user.getUserType().equals(RamConstants.UserTypeIsManager2) || user.getUserType().equals(RamConstants.UserTypeIsSuperAdmin4)) {
			return RamConstants.USER_PERMISSION_LEVEL_ADMIN;//管理员
		}/*else if(user.getUserType().equals(RamConstants.UserTypeIsCityAdmin6)){
			return RamConstants.USER_PERMISSION_LEVEL_CITY_ADMIN;//城市代理
		}*/else if(user.getUserType().equals(RamConstants.UserTypeIsSeller3)){
			return RamConstants.USER_PERMISSION_LEVEL_SHANGJIA;//商家
		}else if(user.getUserType().equals(Constants.USER_TYPE_SELLER_BUY)){
			return RamConstants.USER_PERMISSION_LEVEL_SHANGJIA;
		}else if(user.getUserType().equals(Constants.USER_TYPE_SELLER_EAT)){
			return RamConstants.USER_PERMISSION_LEVEL_SHANGJIA;
		}else if(user.getUserType().equals(Constants.USER_TYPE_SELLER_STORE)){
			return RamConstants.USER_PERMISSION_LEVEL_SHANGJIA;
		}
		return "unknown";
	}

	@Override
	public Integer countSumRechargePoints(String rechargeType,
			Integer betSessionId) {
		return userDAO.countSumRechargePoints(rechargeType, betSessionId);
	}
	//检查用户的积分是否免发布新产品的积分
	public boolean checkUserPointsEnough(Integer userId,BigDecimal comparePoints){
		User user = (User)userDAO.getObject(User.class, userId);
		BigDecimal uPoints = user.getUserpoints();
		//if(uPoints==null) uPoints = new BigDecimal(0);
		return uPoints.doubleValue()>=comparePoints.doubleValue()?true:false;
	}
	/**
	 * 获取有效的用户
	 * @param userId
	 * @return
	 */
	public User getUserValidById(int userId) {
		User vUser = (User) userDAO.getObject(User.class, new Integer(userId));
		if(vUser!=null){
			if(vUser.getStatus().equals(RamConstants.USER_ACCOUNT_OPEN)){
				return vUser;
			}
		}
		return null;
	}
	public void savetbMessageCount(String cellPhone){
		MessageCount tbMessageCount = new MessageCount();
		//tbMessageCount.setSendCount(1);
		tbMessageCount.setSendTime(DateTimeUtil.getJavaUtilDateNow());
		tbMessageCount.setReceivePhone(cellPhone);
		userDAO.saveObject(tbMessageCount);
	}
	
	
	/**
	 * 保存用户余额收支明细
	 */
	public User saveTradeDetail(User user,Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId,String gameType,String remark){
		//----this.updateLimitBetBack(user, cashType, cashMoney);
		
		//有user先保存
		if(user!=null){
			userDAO.saveObject(user);
		}else{
			user = (User)userDAO.getObject(User.class,userId);
		}
		//收入转换
		if(tradeType.equals(Constants.TRADE_TYPE_PAY)) {//支出
			cashMoney = cashMoney.multiply(new BigDecimal(-1));
		}
		
		//保存明细
		UserTradeDetail utd = new UserTradeDetail(userId, tradeType, cashType,GameHelpUtil.round(cashMoney), DateTimeUtil.getJavaUtilDateNow());
		if(!ParamUtils.chkInteger(refId)) refId = 0;
		utd.setRefId(refId);
		if(!ParamUtils.chkString(gameType)) gameType = "0";
		utd.setModelType(gameType);
		if(!ParamUtils.chkString(remark)) remark = "";
		utd.setRemark(remark);
		utd.setType(user.getUserType());
		utd.setGfxy(Constants.getGFXYFlag(gameType));//信用官方区别默认为0
		utd.setLoginName(user.getLoginName());
		utd.setUserMoney(GameHelpUtil.round(TradeCacheUtil.updateUserMoney(userId,cashMoney)));//从实时缓存中更新和读取最新余额				
		// 如果是购买彩票
		if (Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType))
			utd.setStatus(Constants.PUB_STATUS_CLOSE);// 设为不统计
		else
			utd.setStatus(Constants.PUB_STATUS_OPEN);// 设为统计
		userDAO.saveObject(utd);
		
		// 增加与ga_bet_sponsor表的关联0000000000000000000000000000000
//		UserTradeDetailRl rl = new UserTradeDetailRl();
//		rl.setTradeDetailId(utd.getTradeDetailId());
//		rl.setGfxy(Constants.GAME_PLAY_CATE_GF);
//		GaBetPart part = (GaBetPart) userDAO.getObject(GaBetPart.class,refId);
//		rl.setBetDetailId(part.getJointId());
//		userDAO.saveObject(rl);
		
		return user;
	}
	
	/**
	 * 修复明细使用
	 */
	public User saveTradeDetail(User user,Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId,String gameType,String remark,BigDecimal money){
		//有user先保存
		if(user!=null){
			userDAO.saveObject(user);
		}else{
			user = (User)userDAO.getObject(User.class,userId);
		}
		
		//收入转换
		if(tradeType.equals(Constants.TRADE_TYPE_PAY)) {//支出
			cashMoney = cashMoney.multiply(new BigDecimal(-1));
		}
		
		//保存明细
		UserTradeDetail utd = new UserTradeDetail(userId, tradeType, cashType,cashMoney, DateTimeUtil.getJavaUtilDateNow());
		if(!ParamUtils.chkInteger(refId)) refId = 0;
		utd.setRefId(refId);
		if(!ParamUtils.chkString(gameType)) gameType = "0";
		utd.setModelType(gameType);
		if(!ParamUtils.chkString(remark)) remark = "";
		utd.setRemark(remark);
		utd.setUserMoney(money);//以当前余额为主
		utd.setType(user.getUserType());
		utd.setLoginName(user.getLoginName());
		utd.setGfxy(Constants.getGFXYFlag(gameType));//信用官方区别默认为0
		// 如果是购买彩票
		if (Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType))
			utd.setStatus(Constants.PUB_STATUS_CLOSE);// 设为不统计
		else
			utd.setStatus(Constants.PUB_STATUS_OPEN);// 设为统计
		userDAO.saveObject(utd);
		
		//return (User)userDAO.getObject(User.class, userId);//目前直接返user，如无必要减少数据库操作
		return user;
	}
	
	public Integer saveTradeDetail(User user, Integer userId, String tradeType,
			String cashType, BigDecimal cashMoney, Integer refId,
			String gameType, String remark, String sessionNo,String type,String loginName) {
		//有user先保存
		if(user!=null){
			userDAO.saveObject(user);
		}
		
		//收入转换
		if(tradeType.equals(Constants.TRADE_TYPE_PAY)) {//支出
			cashMoney = cashMoney.multiply(new BigDecimal(-1));
		}
		
		//保存明细
		UserTradeDetail utd = new UserTradeDetail(userId, tradeType, cashType,cashMoney, DateTimeUtil.getJavaUtilDateNow());
		if(!ParamUtils.chkInteger(refId)) refId = 0;
		utd.setRefId(refId);
		if(!ParamUtils.chkString(gameType)) gameType = "0";
		utd.setModelType(gameType);
		if(!ParamUtils.chkString(remark)) remark = "";
		utd.setRemark(remark);
		if(!ParamUtils.chkString(loginName)) loginName = "";
		utd.setLoginName(loginName);
		utd.setUserMoney(TradeCacheUtil.updateUserMoney(userId,cashMoney));//从实时缓存中更新和读取最新余额
		if(ParamUtils.chkString(sessionNo)) utd.setSessionNo(sessionNo);
		if(!ParamUtils.chkString(type)) type = "0";
		utd.setType(type);
//		utd.setGfxy(Constants.getGFXYFlag(gameType));//信用官方区别默认为0 TODO 此处需要修改，会报错
		utd.setGfxy("0");//TODO 此处为避免报错，暂存0，需根据具体情况判断
		// 如果是购买彩票
		if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType))
			utd.setStatus(Constants.PUB_STATUS_CLOSE);// 设为不统计
		else
			utd.setStatus(Constants.PUB_STATUS_OPEN);// 设为统计
		
		userDAO.saveObject(utd);
		return utd.getTradeDetailId();
	}
	
	public User saveTradeDetail(Integer userId,String tradeType,String cashType,BigDecimal cashMoney, Integer refId){
		return saveTradeDetail(null, userId, tradeType, cashType, cashMoney, refId, null, null);
	}
	
	public User updateLimitBetBack(User user, String cashType, BigDecimal cashMoney){
		
		if (Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)) {// 必须是购买彩票才可以
			if (user != null) {
				BigDecimal limitBetBack = user.getLimitBetBack();// 限制打码金额
				if (limitBetBack != null && limitBetBack.compareTo(new BigDecimal(0)) > 0) {
					Date limitDate = user.getLimitDate();
					// oldTotal是个负数
					BigDecimal oldTotal = userDAO.getSumCashMoneyByDate(user.getUserId(), DateTimeUtil.DateToStringAll(limitDate), DateTimeUtil.DateToStringAll(new Date()), cashType);
					BigDecimal total = null;
					if (oldTotal == null) total = cashMoney;
					else total = cashMoney.subtract(oldTotal);
					if (total.compareTo(limitBetBack) >= 0) {// 解除用户限制
						user.setLimitBetBack(new BigDecimal(0));
						user.setLimitRecharge(new BigDecimal(0));
						user.setLimitDate(null);
					}
				}
			}
		}
		
		return user;
	}
	
	/**
	 * 保存用户积分收支明细
	 */
	@Override
	public User savePointDetail(Integer userId, String tradeType,
			String cashType, BigDecimal cashPoint, Integer refId,String remark) {
		// 1.计算余额
		User user = userDAO.getUser(userId);
	// 1.计算余额
		
		if (tradeType.equals(Constants.TRADE_TYPE_PAY)) {// 支出
			cashPoint = cashPoint.multiply(new BigDecimal(-1));
		}

		//保存明细
		UserPointDetail utd = new UserPointDetail(userId, tradeType, cashType,cashPoint.setScale(2, BigDecimal.ROUND_HALF_UP), DateTimeUtil.getJavaUtilDateNow());
		if(ParamUtils.chkInteger(refId)) utd.setRefId(refId);
		utd.setRemark(remark);
		utd.setUserPoint(TradeCacheUtil.updateUserPoints(userId,cashPoint).setScale(2, BigDecimal.ROUND_HALF_UP));//从实时缓存中更新和读取最新余额
		userDAO.saveObject(utd);
		return user;
	}
	
	/**
	 * 积分明细保存表，所有牵涉到用户积分明细情况都调用此方法 
	 * @param userId 操作用户
	 * @param type 积分类型
	 * @param points 积分值
	 * @param infoId [关联信息id]
	 * @param comments [备注]
	 * @param beUserId [被操作用户用于给别人充值的情况]
	 */
	public void updateMytUserDetailOfPublic(Integer userId,String type,BigDecimal points,Integer infoId,String comments,Integer beUserId){
//		//Integer addPoints = StringUtil.stringToInteger(points);
//		if(points!=null && points>0 || points<0){
//			MytUserDetail mut = new MytUserDetail();//积分明细只添加新记录
//			mut.setUserId(userId);
//			mut.setType(type);
//			mut.setScore(points.toString());
//			mut.setSubmitTime(DateTimeUtil.getJavaUtilDateNow());
//			if(ParamUtils.chkInteger(infoId)) mut.setInfoId(infoId);
//			if(ParamUtils.chkInteger(beUserId)) mut.setBeUserId(beUserId);
//			if(ParamUtils.chkString(comments)) mut.setTitle(comments);
//			userDAO.saveObject(mut);
//			//更新用户部账号
//			this.updateUserPointsOfPublic(userId, points);
//		}
		int point =points.intValue();
		savePointDetail(userId, point>0?Constants.TRADE_TYPE_INCOME:Constants.TRADE_TYPE_PAY, type, points, beUserId,"");
	}
	public String delUser(Integer uid,String userType) {
		HQUtils hq = null;
		Integer count = 0;
		//检查用户下是否有订单  普通用户
		if("1".equals(userType)){
			hq = new HQUtils("from StoreOrder s where s.userId=?");
			hq.addPars(uid);
			count = userDAO.countObjects(hq);
			if(count==0){
				hq = new HQUtils("from EatOrder e where e.userId=?");
				hq.addPars(uid);
				count = userDAO.countObjects(hq);
			}
			if(count==0){
				hq = new HQUtils("from BuyOrder b where b.userId=?");
				hq.addPars(uid);
				count = userDAO.countObjects(hq);
			}
			if(count==0){
				hq = new HQUtils("from GaOrder g where g.userId=?");
				hq.addPars(uid);
				count = userDAO.countObjects(hq);
			}
			if(count==0){
				userDAO.deleteObject(User.class, uid, null);
				return APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		//检查用户是否与商家关联   商家用户
		if("3".equals(userType)){
			hq = new HQUtils("from SellerUserRl rl where rl.uid=?");
			hq.addPars(uid);
			count = userDAO.countObjects(hq);
			
			if(count==0){
				userDAO.deleteObject(Seller.class, uid, null);
				return APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		return APIConstants.CODE_REQUEST_ERROR;
	}
	
	public BigDecimal levelAward(User user, BigDecimal userpoints) {
		// 用户之前累计充值的金额
		BigDecimal addUpRechargeMoney = user.getAddUpRechargeMoney();
		if (addUpRechargeMoney == null) {
			addUpRechargeMoney = new BigDecimal(0);
		}
		// 用户充值完本次累计充值的金额
		BigDecimal addUpRechargeMoney2 = addUpRechargeMoney.add(userpoints);
		Integer level = 0;// 用户等级
		Integer level2 = 0;// 用户充值完之后的等级
		BigDecimal levelAward = new BigDecimal(0);// 跳级奖励的金额
		List<UserLevel> levelList = baseDataDAO.findUserLevel();
		
		if (levelList != null) {// 根据用户积分判断用户是哪个等级的
			boolean isNext = false;
			boolean isNext2 = false;
			for (int i=0;i < levelList.size();i++) {
				UserLevel userLevel = levelList.get(i);
				BigDecimal point = userLevel.getPoint();
				String lev = userLevel.getLevel();
				BigDecimal award = userLevel.getAward();
				if (addUpRechargeMoney.compareTo(point) >= 0) {
					isNext = true;
					if (levelList.size()-1 == i) {
						level = Integer.valueOf(lev);
					}
				} else {
					if (isNext) {
						level = Integer.valueOf(lev)-1;
					}
					isNext = false;
				}
				if (addUpRechargeMoney2.compareTo(point) >= 0) {
					isNext2 = true;
					if (levelList.size()-1 == i) {
						level2 = Integer.valueOf(lev);
						levelAward = award;
					}
				} else {
					if (isNext2) {
						level2 = Integer.valueOf(lev)-1;
						levelAward = levelList.get(i-1).getAward();
					}
					isNext2 = false;
				}
			}
		}
		if (level2 - level >= 2) {// 跳级
			return levelAward;
		} else {
			return new BigDecimal(0);
		}
	}
	
//	public void 
	
	public void modifyBalance(Integer userId,BigDecimal userpoints, String type) {
		User user = (User)userDAO.getObject(User.class, userId);
		if(user != null){
			try {
				String cashType = Constants.CASH_TYPE_MANAGER_SET;
				StringBuffer remark=new StringBuffer();
				
				remark.append(userpoints).append("元");
				CardRechargeOrder order = new CardRechargeOrder();
				order.setUserId(userId);
				order.setPayType("3");// 在线支付方式
				order.setTotalMoney(userpoints);
				String orderNum = ProductUtil
						.createOrderNum(Constants.MODULE_RECHARGE);
				order.setOrderNum(orderNum);
				order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
				order.setPayStatus("2");
				order = (CardRechargeOrder) userDAO.saveObjectDB(order);
				Integer orderId = order.getOrderId();
				
				BigDecimal lmitBetBack = user.getLimitBetBack();
				BigDecimal lmitRecharge = user.getLimitRecharge();
				if (lmitBetBack != null && lmitBetBack.compareTo(new BigDecimal(0)) > 0) {
					if (lmitRecharge != null && lmitRecharge.compareTo(userpoints) >= 0) {
						// 解除限制（具体看user.java里lmitBetBack的注释）
						user.setLimitBetBack(new BigDecimal(0));
						user.setLimitRecharge(new BigDecimal(0));
						user.setLimitDate(null);
					}
				}
				
				//其他加款不参与返利
				if("1".equals(type)){//正常加款
					cashType = Constants.CASH_TYPE_MANAGER_SET;
					remark.append("管理员充值 ");
				}else if("0".equals(type)){//其他加款
					cashType = Constants.CASH_TYPE_CASH_OTHER_SET;
					remark.append("其他加款 ");
				}
				
				this.saveTradeDetail(user, userId,
						Constants.TRADE_TYPE_INCOME,
						cashType, userpoints,
						order.getOrderId(), null, remark.toString());
				
				if("1".equals(type)){//管理员充值
					//跳级的奖励金额
					this.updateUserAddUpRechargeMoney(user, userpoints, orderId);
					//返利
					this.saveUserDayRecharge(userId, userpoints, orderId);
				}
				
				this.updateUserMoney(order.getUserId());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void saveUserBalance_(Integer userId,BigDecimal userpoints,String operateType,Integer itemId){
//		User user = (User)userDAO.getObject(User.class, userId);
//		if(user != null){
//			try {
//				CardRechargeOrder order=(CardRechargeOrder) userDAO.getObject(CardRechargeOrder.class, itemId);
//				if(order!=null){
//					String remark="线下充值："+order.getOrderNum()+"金额："+GameHelpUtil.round(userpoints);
//					this.saveTradeDetail(null, userId, Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_MANAGER_SET, userpoints, itemId, null, remark);
//					userDAO.updateUserMoney(userId);
//					
//					//返利
//					this.saveUserDayRecharge(userId, userpoints, itemId);
//					
//					order.setPayStatus("2");
//					userDAO.updateObject(order, null);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * 充值返水
	 * 保存用户每日充值金额，判断抽奖次数，充值返水等
	 * @param user
	 * @param rechargeMoney
	 */
	public void saveUserDayRecharge(Integer userId,BigDecimal rechargeMoney,Integer itemId){
		User user = (User) userDAO.getObject(User.class, userId);
		BigDecimal dayRecharge=user.getDayRecharge();//每日充值
		if(dayRecharge==null) dayRecharge=new BigDecimal(0);
		
		BigDecimal dayRechargeMore = GameHelpUtil.round(dayRecharge.add(rechargeMoney));
		
		//充值返水处理---------------------------------------------
		//获取充值返水比例配置
		ValueConfig vcMember = CacheUtil.rechargeBackConfig("member");//自返
		ValueConfig vcAgent = CacheUtil.rechargeBackConfig("agent");//返代理
		if(vcMember.isOpen()){//自返水
			//计算返水的钱
			BigDecimal backMoney = GameHelpUtil.getBackMoney(rechargeMoney, vcMember);
			if(backMoney.compareTo(new BigDecimal(0))==1){//大于0则保存记录
				//更新返水明细
				StringBuffer remark = new StringBuffer();
				remark.append("ID："+user.getUserId()+"充值："+GameHelpUtil.round(rechargeMoney)+"自返："+backMoney);
				this.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,
						Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF, backMoney, itemId,null,remark.toString());
			}
		}
		if(vcAgent.isOpen()){//返代理
			if(ParamUtils.chkIntegerPlus(user.getAgentId())){//有代理
				User agent = (User)userDAO.getObject(User.class, user.getAgentId());
				if(agent!=null){
					//计算返水的钱
					BigDecimal backMoney = GameHelpUtil.getBackMoney(rechargeMoney, vcAgent);
					if(backMoney.compareTo(new BigDecimal(0))==1){//大于0则保存记录
						//更新代理收益
						BigDecimal recRevenue = agent.getRecRevenue();
						if(recRevenue==null) recRevenue = new BigDecimal(0);
						agent.setRecRevenue(GameHelpUtil.round(recRevenue.add(backMoney)));
						//更新返水明细
						StringBuffer remark = new StringBuffer();
						remark.append("ID："+user.getUserId()+"充值："+GameHelpUtil.round(rechargeMoney)+"返代理："+agent.getUserId()+backMoney);
						this.saveTradeDetail(agent,agent.getUserId(), Constants.TRADE_TYPE_INCOME,
								Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN, backMoney, itemId,null,remark.toString());
					}
				}
			}
		}
		
		//红包-----------------------------------------------------
		if(!ParamUtils.chkInteger(user.getRedPacketsNum())){//无红包时
			List<LotterySettingRl> rllist = CacheUtil.getLotterySettingRl("redpackets");
			if(rllist!=null&&rllist.size()>0){
				for(int i=0;i<rllist.size();i++){
					if(dayRecharge.compareTo(rllist.get(i).getRechargeMinMoney())<0){
						if(dayRechargeMore.compareTo(rllist.get(i).getRechargeMinMoney())>-1){
							user.setRedPacketsNum(1);
							GameHelpUtil.log("["+userId+"]RedPacket+1 ..... "+rllist.get(i).getRechargeMinMoney());
							break;
						}
					}else{
						continue;
					}
				}
			}
		}
		//转盘-------------------------------------------------------
		if(!ParamUtils.chkInteger(user.getTurnTableNum())){//无转盘时
			List<LotterySettingRl> rllist = CacheUtil.getLotterySettingRl("turntable");
			if(rllist!=null&&rllist.size()>0){
				for(int i=0;i<rllist.size();i++){
					if(dayRecharge.compareTo(rllist.get(i).getRechargeMinMoney())<0){
						if(dayRechargeMore.compareTo(rllist.get(i).getRechargeMinMoney())>-1){
							user.setTurnTableNum(1);
							GameHelpUtil.log("["+userId+"]TurnTable+1 ..... "+rllist.get(i).getRechargeMinMoney());
							break;
						}
					}else{
						continue;
					}
				}
			}
		}
		user.setDayRecharge(dayRechargeMore);
		userDAO.saveObject(user);
	}
	
//	public void saveUserDayRecharge(Integer userId,BigDecimal rechargeMoney,Integer itemId){
//		User user=(User) userDAO.getObject(User.class, userId);
//		BigDecimal dayRecharge=user.getDayRecharge();//每日充值
//		if(dayRecharge==null){
//			dayRecharge=new BigDecimal(0);
//		}
////		if(user.getDayRecharge()!=null){
////			user.setDayRecharge(user.getDayRecharge().add(rechargeMoney));
////		}else{
////			user.setDayRecharge(rechargeMoney);
////		}
//		
//		List<Param> list = CacheUtil.getParam();
//		String agentBack="";
//		String memberBack="";
//		for (Param param : list) {
//			String type = param.getType();
//			if(Constants.PARAM_RECHARGE_SEND.equals(type)){
//				memberBack=param.getValue();
//			}else if(Constants.PARAM_RECHARGE_SEND_AGENT.equals(type)){
//				agentBack=param.getValue();
//			}
//		}
//		
//		if(agentBack!=null&&new BigDecimal(agentBack).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN).compareTo(new BigDecimal(0))>0){//充值返水给代理
//			if(user.getAgentId() != null){
//				User agent=(User) userDAO.getObject(User.class, user.getAgentId());
//				if(agent!=null){
//					BigDecimal rate=new BigDecimal(agentBack).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);
//						
//					BigDecimal recRevenue=agent.getRecRevenue();
//					if(recRevenue==null){
//						recRevenue=new BigDecimal(0);
//					}
//					agent.setRecRevenue(recRevenue.add(rate.multiply(rechargeMoney)));
//					
//					String remark=user.getLoginName()+"充值了"+rechargeMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+","+agent.getLoginName()+"获得了"+rate.multiply(rechargeMoney).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"元返水";
//					
//					saveTradeDetail(agent,agent.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN, rate.multiply(rechargeMoney), itemId,null,remark);
//					
//					updateUserMoney(agent.getUserId());
////					userDAO.updateObject(agent, null);
//				}
//			}
//		}
//		
//		
//		if(!ParamUtils.chkInteger(user.getRedPacketsNum())){
//			List<LotterySettingRl> rllist = CacheUtil.getLotterySettingRl("redpackets");
//			if(rllist!=null&&rllist.size()>0){
//				for(int i=0;i<rllist.size();i++){
//					if(dayRecharge.compareTo(rllist.get(i).getRechargeMinMoney())<0){
//						log.info("红包———————————加："+rllist.get(i).getRechargeMinMoney());
//						if(dayRecharge.add(rechargeMoney).compareTo(rllist.get(i).getRechargeMinMoney())>-1){
//							user.setRedPacketsNum(1);
//							log.info("红包———————————加1："+rllist.get(i).getRechargeMinMoney());
//							break;
//						}
//					}else{
//						continue;
//					}
//				}
//			}
//		}
//		if(!ParamUtils.chkInteger(user.getTurnTableNum())){
//			List<LotterySettingRl> rllist = CacheUtil.getLotterySettingRl("turntable");
//			if(rllist!=null&&rllist.size()>0){
//				for(int i=0;i<rllist.size();i++){
//					if(dayRecharge.compareTo(rllist.get(i).getRechargeMinMoney())<0){
//						log.info("转盘———————————加："+rllist.get(i).getRechargeMinMoney());
//						if(dayRecharge.add(rechargeMoney).compareTo(rllist.get(i).getRechargeMinMoney())>-1){
//							user.setTurnTableNum(1);
//							log.info("转盘———————————1加："+rllist.get(i).getRechargeMinMoney());
//							break;
//						}
//					}else{
//						continue;
//					}
//				}
//			}
//		}
//		user.setDayRecharge(dayRecharge.add(rechargeMoney));
//		
//		if(memberBack!=null&&new BigDecimal(memberBack).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN).compareTo(new BigDecimal(0))>0){//充值返水给自己
//			BigDecimal rate=new BigDecimal(memberBack).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);
//			
////			user=this.saveTradeDetail(user.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF, rate.multiply(rechargeMoney), itemId);
//		
//			
//			String remark=user.getLoginName()+"充值了"+rechargeMoney.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+","+user.getLoginName()+"获得了"+rate.multiply(rechargeMoney).setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"元返水";
//			
//			this.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME, Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF, rate.multiply(rechargeMoney), itemId,null,remark);
//			
//			this.updateUserMoney(user.getUserId());
//			log.info("保存用户："+user.getRedPacketsNum());
//			log.info("保存用户："+user.getTurnTableNum());
//		}else{
//			log.info("保存用户sss："+user.getRedPacketsNum());
//			log.info("保存用户sss："+user.getTurnTableNum());
//			userDAO.updateObject(user, null);
//		}
//	}
	
	
	
	@Override
	public PaginationSupport findUserTradeDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return userDAO.findUserTradeDetail(hql, para, pageNum, pageSize);
	}

	@Override
	public PaginationSupport findBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return userDAO.findBetList(hql,para,pageNum,pageSize);
	}
	
	@Override
	public void modifyMoney(Integer userId, BigDecimal userpoints,String type) {

		User user = (User) userDAO.getObject(User.class,userId);
		if(user != null){
			try {
				BigDecimal money = ProductUtil.checkBigDecimal(user.getMoney());//用户余额
				if(money.compareTo(userpoints)== -1){
					return;
				};
				String payType = Constants.CASH_TYPE_CASH_SYS_CHARGE;//扣款类型
				StringBuffer remark = new StringBuffer();
				if(ParamUtils.chkString(type)){
					if("0".equals(type)){
						payType = Constants.CASH_TYPE_CASH_OTHER_CHARGE;
						remark.append("其它扣款，订单号：");
					}else if("1".equals(type)){
						payType = Constants.CASH_TYPE_CASH_SYS_CHARGE;
						remark.append("系统扣款，订单号：");
					}
				}
				CardRechargeOrder order = new CardRechargeOrder();
				order.setUserId(user.getUserId());
				order.setPayType(payType);// 扣款
				order.setTotalMoney(userpoints);
				String orderNum = ProductUtil
						.createOrderNum(Constants.MODULE_RECHARGE);
				order.setOrderNum(orderNum);
				order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
				order.setPayStatus(Constants.PAY_STATUS_RESOLVED);//已完成
				order = (CardRechargeOrder) userDAO.saveObjectDB(order);

				
				remark.append(order.getOrderNum())
						.append("，扣款金额：").append(userpoints.toString())
						.append("元。");
				this.saveTradeDetail(user, userId,
						Constants.TRADE_TYPE_PAY,
						payType, userpoints,
						order.getOrderId(), null, remark.toString());

				this.updateUserMoney(userId);

				
//				order.setUserMoney(user.getMoney());
				userDAO.saveObject(order);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
//	/**
//	 * 保存用户余额收支明细
//	 */
//	public User saveTradeDetail(User user,Integer userId, String tradeType,
//			String cashType, BigDecimal cashMoney, Integer refId,String gameType,String remark) {
//		// 1.计算余额
//		if(user==null){
//			user = userDAO.getUser(userId);
//		}
////		User user = userDAO.getUser(userId);
//		BigDecimal money = user.getMoney();
//		if(money==null) money = new BigDecimal(0);
//		
//		if (tradeType.equals(Constants.TRADE_TYPE_PAY)) {// 支出
//			cashMoney = cashMoney.multiply(new BigDecimal(-1));
//		}
//		money = money.add(cashMoney);// 计算后的余额
//
//		user.setMoney(money);
//		userDAO.updateObject(user, null);
//		
//		// 2.保存明细
//		UserTradeDetail utd = new UserTradeDetail(userId, tradeType, cashType,cashMoney, new Date());
//		if(ParamUtils.chkInteger(refId)){
//			utd.setRefId(refId);
//		}
//		utd.setModelType(gameType);
//		utd.setUserMoney(user.getMoney());
//		utd.setRemark(remark);
//		userDAO.saveObject(utd);
//		return user;
//	}

	@Override
	public void saveUserInfo(User userFrm) {
		Integer userId = userFrm.getUserId();
		Date nowDate = DateTimeUtil.getJavaUtilDateNow();
		User user =null;
		if(ParamUtils.chkInteger(userId)){
			user = this.getUser(userId);
			user.setAgentId(userFrm.getAgentId());
			if(ParamUtils.chkString(userFrm.getAgentName())){
				user.setAgentName(userFrm.getAgentName());
			}
			if(!ParamUtils.chkString(user.getLoginName())){
				user.setLoginName(userFrm.getLoginName());
			}
			if(ParamUtils.chkString(userFrm.getUserType()) && Constants.USER_TYPE_SUER.equals(userFrm.getUserType())){
				if(ParamUtils.chkInteger(userFrm.getAgentId1())){
					user.setAgentId1(userFrm.getAgentId1());
				}
			}else{
				if(ParamUtils.chkInteger(userFrm.getAgentId())){
					User tempUser=(User) userDAO.getObject(User.class, userFrm.getAgentId());
					if(tempUser.getAgentId()>0){
						user.setAgentId1(tempUser.getAgentId());
					}else{
						user.setAgentId1(userFrm.getAgentId());
					}
					user.setAgentId1(userFrm.getAgentId1());
				}else{
					user.setAgentId1(0);
				}
			}

			
			String cashPassword = userFrm.getCashPassword();
			if(ParamUtils.chkString(cashPassword)){
				user.setCashPassword(cashPassword);
			}
			if(ParamUtils.chkString(userFrm.getUserType())){
				user.setUserType(userFrm.getUserType());
			}
			
		}else{
			user=new User();
			user.setLimitBetBack(new BigDecimal(0));
			user.setLimitRecharge(new BigDecimal(0));
			user.setMoney(new BigDecimal(0));
			user.setUserpoints(new BigDecimal(0));
			user.setAddUpRechargeMoney(new BigDecimal(0));
			user.setIsBetBack(Constants.PUB_STATUS_OPEN);
			user.setStatus(Constants.PUB_STATUS_OPEN);
			user.setUserType(userFrm.getUserType());
			user.setRegistDateTime(nowDate);
			// 设置用户的充值通道，默认第一个
			HQUtils hq = new HQUtils();
			hq.addHsql("from RechargeWay rw where rw.type=? ");
			hq.addPars(Constants.RECHARGE_WAY_1);
			List<Object> rwList = userDAO.findObjects(hq);
			if (rwList != null) {
				RechargeWay r = (RechargeWay)rwList.get(0);
				user.setRechargeWay(r.getId());
			}
			
			user = userDAO.saveObjectDB(user);
			
			List<Param> list = CacheUtil.getParam();
			Param para = null;
			BigDecimal money = null;//赠送金额
			for(Param param:list){
				if(Constants.PARAM_REGISTER_SEND.equals(param.getType())){
					para = param;
					break;
				}
			}
			if(para != null){
				String value = para.getValue();
				if(ParamUtils.chkString(value)){
					try{
						money = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
					}catch(Exception e){
						log.info("赠送金额错误");
					}
				}
			}
			if(money != null){
				StringBuffer remark=new StringBuffer();
				remark.append("注册赠送 金额 ")
				    .append(money.toString()).append("元");
				this.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRESENT, money, null, null,remark.toString());
				//--by.cuisy.20171209
				this.updateUserMoney(user.getUserId());
			}

			user.setLastLoginDate(DateTimeUtil.getJavaUtilDateNow());
			user.setLoginTimes(new Integer(0));
			user.setAgentId(userFrm.getAgentId());
			if(ParamUtils.chkString(userFrm.getAgentName())){
				user.setAgentName(userFrm.getAgentName());
			}
			if(ParamUtils.chkString(userFrm.getUserType()) && Constants.USER_TYPE_SUER.equals(userFrm.getUserType())){
				if(ParamUtils.chkInteger(userFrm.getAgentId1())){
					user.setAgentId1(userFrm.getAgentId1());
				}
			}else{
				if(ParamUtils.chkInteger(userFrm.getAgentId())){
					User tempUser=(User) userDAO.getObject(User.class, userFrm.getAgentId());
					if(tempUser.getAgentId()>0){
						user.setAgentId1(tempUser.getAgentId());
					}else{
						user.setAgentId1(userFrm.getAgentId());
					}
				}else{
					user.setAgentId1(0);
				}
			}
			user.setLoginName(userFrm.getLoginName());
		}
		String userType=userFrm.getUserType();
		String cellPhone = userFrm.getCellPhone();//用户电话
		String userName = userFrm.getUserName();
		String bankName = userFrm.getBankName();
		String accountNo = userFrm.getAccountNo();
		String bindName = userFrm.getBindName();
		
		String cashPassword = userFrm.getCashPassword();
		if(ParamUtils.chkString(cashPassword)){
			user.setCashPassword(cashPassword);
		}
		
		
		user.setUserName(userName);
		user.setUserEmail(userFrm.getUserEmail());
		user.setUserType(userType);
		if(ParamUtils.chkString(cellPhone)){
			user.setCellPhone(userFrm.getCellPhone());
		}		
		String gender = userFrm.getGender();
		if (ParamUtils.chkString(gender)) {
			user.setGender(gender);
		}
		if(ParamUtils.chkString(userFrm.getAddress())){
			user.setAddress(userFrm.getAddress());
		}

		List<UserBankBind> list= cashDAO.findBankBindListByUid(user.getUserId());
		UserBankBind bank = null;
		if(list != null && list.size()>0){
			bank = list.get(0);
		}else{
			bank=new UserBankBind();
			bank.setUserId(userId);
			bank.setBindName(bindName);
		}

		if(ParamUtils.chkString(bankName)&& !bankName.equals(user.getBankName())){
			user.setBankName(bankName);
			bank.setBankName(bankName);
		}
		if(ParamUtils.chkString(accountNo)&& !accountNo.equals(user.getAccountNo())){
			user.setAccountNo(accountNo);
			bank.setBindAccount(accountNo);
		}
		if(ParamUtils.chkString(bindName)&& !bindName.equals(user.getBindName())){
			user.setBindName(bindName);
			bank.setBindName(bindName);
		}
		
		if(ParamUtils.chkString(bindName)){
			userDAO.saveObject(bank);
		}
		
		if (userType.equals(Constants.USER_TYPE_SUER)||userType.equals(Constants.USER_TYPE_ADMIN)) {
			Date birthday = userFrm.getBirthday();
			user.setBirthday(birthday);
			if(ParamUtils.chkInteger(userId)){
				String password2 = userFrm.getPassword();
				String password = user.getPassword();
				if(ParamUtils.chkString(password2)){
					if(!password.equals(password2)){
						user.setPassword(MD5.exc(password2));
					}
				}
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				if(ParamUtils.chkString(userFrm.getPassword())){
					user.setPassword(MD5.exc(userFrm.getPassword()));
				}else{
					user.setPassword(MD5.exc(Constants.USER_PASSWORD_DEFAULT));
				}
				user = userDAO.saveObjectDB(user);
			}
		}else if(userType.equals(Constants.USER_TYPE_TEST)
				||userType.equals(Constants.USER_TYPE_AGENT)
				||userType.equals(Constants.USER_TYPE_AGENT_TWO)
				||userType.equals(Constants.USER_TYPE_AGENT_THREE)
				||userType.equals(Constants.USER_TYPE_CUS_SERVICE)
				||userType.equals(Constants.USER_TYPE_FINANCE)){
			if(ParamUtils.chkInteger(userId)){
				String password = user.getPassword();
				String password2 = userFrm.getPassword();
				if(ParamUtils.chkString(password2)){
					if(!password.equals(password2)){
						user.setPassword(MD5.exc(password2));
					}
				}
				userDAO.saveUser(user, null);
			}else{
				String loginName = userFrm.getLoginName();
				user.setLoginName(loginName);
				String password2 = userFrm.getPassword();
				if(ParamUtils.chkString(password2)){
					user.setPassword(MD5.exc(password2));
				}else{
					user.setPassword(MD5.exc(Constants.USER_PASSWORD_DEFAULT));
				}
				user = userDAO.saveObjectDB(user);
			}
		}else{
			if(ParamUtils.chkInteger(userId)){
				userDAO.saveUser(user, null);
			}else{
				user = userDAO.saveObjectDB(user);
			}
		}
	}


	@Override
	public User getAgentById(Integer agentId) {
		return userDAO.getAgentById(agentId);
	}
	public User updateUserTurnTable(User user, LotterySettingRl rl,
			BigDecimal drawMoney){
		String  remark="转盘抽奖获得"+drawMoney.toString()+"元奖励";
		user.setTurnTableNum(user.getTurnTableNum()-1);
		
		if(drawMoney!=null && drawMoney.doubleValue()>0){
			this.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_TURN_TABLE, drawMoney, rl.getRid(), null, remark);
			this.updateUserMoney(user.getUserId());
		}
	
		user=(User) userDAO.getObject(User.class, user.getUserId());
//		userDAO.updateObject(rl, null);
		return user;
	}

	@Override
	public User updateUserRedPackets(User user, LotterySettingRl rl,
			BigDecimal drawMoney) {
		String  remark="抽中红包获得"+drawMoney.toString()+"元奖励";
		user.setRedPacketsNum(user.getRedPacketsNum()-1);
		this.saveTradeDetail(user, user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_RED_PACKETS, drawMoney, rl.getRid(), null, remark);
		this.updateUserMoney(user.getUserId());
		rl.setNum(rl.getNum()-1);
//		userDAO.updateObject(user, null);
		userDAO.updateObject(rl, null);
		return (User) userDAO.getObject(User.class, user.getUserId());
	}


	@Override
	public void saveModifyUserInfo(User userFrm) {
		Integer userId = userFrm.getUserId();
		User user =null;
		if(ParamUtils.chkInteger(userId)){
			user = this.getUser(userId);
			String userType=userFrm.getUserType();
			String address = userFrm.getAddress();
		    String cellPhone = userFrm.getCellPhone();
			String userName = userFrm.getUserName();
			String password = userFrm.getPassword();//action中处理
			Integer agentId = userFrm.getAgentId();
			String agentName = userFrm.getAgentName();
			String gender = userFrm.getGender();
			String isBetBack = userFrm.getIsBetBack();
			Integer rechargeWay = userFrm.getRechargeWay();
			BigDecimal limitBetBack = userFrm.getLimitBetBack();
			BigDecimal limitRecharge = userFrm.getLimitRecharge();

			if (limitBetBack != null) {
				user.setLimitBetBack(limitBetBack);
				user.setLimitDate(new Date());
			}
			if (limitRecharge != null) {
				user.setLimitRecharge(limitRecharge);
			}
			if(ParamUtils.chkString(userType)){
//				if(Constants.USER_TYPE_AGENT_ONE.equals(userType)||Constants.USER_TYPE_AGENT_TWO.equals(userType)
//						||Constants.USER_TYPE_AGENT_THREE.equals(userType)){
//					agentId = 0;
//					agentName = "admin";
//				}
				user.setUserType(userType);
			}
			if(ParamUtils.chkString(address)){
				user.setAddress(address);
			}
			if(ParamUtils.chkString(cellPhone)){
				user.setCellPhone(cellPhone);
			}
			if(ParamUtils.chkString(userName)){
				user.setUserName(userName);
			}
			if(ParamUtils.chkString(password)){
				user.setPassword(password);
			}else{
				user.setPassword(MD5.exc(Constants.USER_PASSWORD_DEFAULT));
			}
			if(ParamUtils.chkInteger(agentId)){
				user.setAgentId(agentId);
			}
//			if(Constants.USER_TYPE_SUER.equals(user.getUserType())&&ParamUtils.chkInteger(agentId)){
//				user.setAgentId(agentId);
//			}
			if(ParamUtils.chkString(agentName)){
				user.setAgentName(agentName);
			}
			if(ParamUtils.chkString(gender)){
				user.setGender(gender);
			}
			if(ParamUtils.chkString(isBetBack)){
				user.setIsBetBack(isBetBack);
			}
			if(ParamUtils.chkInteger(rechargeWay)){
				user.setRechargeWay(rechargeWay);
			}

			userDAO.saveUser(user, null);
		}
	}


	@Override
	public List<User> findUserList(String hqls, List<Object> para) {
		return userDAO.findUserList(hqls,para);
	}
	public void saveUserRegSendMoney(User regUser, BigDecimal money){	
		StringBuffer remark=new StringBuffer();
		remark.append("注册赠送 金额 ")
		    .append(money.toString()).append("元");
		this.saveTradeDetail(regUser,regUser.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRESENT, money, null, null,remark.toString());
		//--by.cuisy.20171209
		this.updateUserMoney(regUser.getUserId());
	}


	public void saveUserSignMoney(User user, BigDecimal money) {
		StringBuffer remark=new StringBuffer();
		remark.append("签到赠送 金额 ")
		    .append(money.toString()).append("元");
		this.saveTradeDetail(user,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_SIGN, money, null, null,remark.toString());
		//--by.cuisy.20171209
		this.updateUserMoney(user.getUserId());
		
	}
	
	public void saveCountUserMoney(){
		StringBuffer hqls =new StringBuffer();
		List<Object> para=new ArrayList<Object>();
		List<User>  userList=userDAO.findUserList(hqls.toString(), para);
		List<Integer> userIds=new  ArrayList<Integer>();
		for(User user:userList){
			BigDecimal money=user.getMoney();
			if(money==null){
				money=new BigDecimal(0);
			}
			if(money.compareTo(new BigDecimal("0"))>=0){
				BigDecimal cashMoney=userDAO.getSumCashMoneyByUserId(user.getUserId());
				if(cashMoney==null){
					cashMoney=new BigDecimal(0);
				}
				if(money.compareTo(cashMoney)>0){
					if(!StringUtil.chkListIntContains(userIds, user.getUserId())){
						userIds.add(user.getUserId());
					}
					StringBuffer remark=new StringBuffer();
					remark.append("其它加款 ")
					    .append(money.subtract(cashMoney).setScale(2, BigDecimal.ROUND_HALF_UP)).append("元");
					
					CardRechargeOrder order = new CardRechargeOrder();
					order.setUserId(user.getUserId());
					order.setPayType("3");// 在线支付方式
					order.setTotalMoney(money.subtract(user.getMoney()));
					String orderNum = ProductUtil
							.createOrderNum(Constants.MODULE_RECHARGE);
					order.setOrderNum(orderNum);
					order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
					order.setPayStatus("2");
					order = (CardRechargeOrder) userDAO.saveObjectDB(order);
					
//					this.saveTradeDetail(null,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD, money.subtract(cashMoney), order.getOrderId(), null,remark.toString());

					this.saveTradeDetail(null,user.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_REPAIR_DETAIL_ADD, money.subtract(cashMoney), order.getOrderId(), null,remark.toString(),money);

				}else if(money.compareTo(cashMoney)<0){
					StringBuffer remark=new StringBuffer();
					remark.append("其它扣款，订单号：");
					String payType = Constants.CASH_TYPE_CASH_OTHER_CHARGE;
					CardRechargeOrder order = new CardRechargeOrder();
					order.setUserId(user.getUserId());
					order.setPayType(payType);// 扣款
					order.setTotalMoney(cashMoney.subtract(money));
					String orderNum = ProductUtil
							.createOrderNum(Constants.MODULE_RECHARGE);
					order.setOrderNum(orderNum);
					order.setCreateTime(DateTimeUtil.getJavaUtilDateNow());
					order.setPayStatus(Constants.PAY_STATUS_RESOLVED);//已完成
					order = (CardRechargeOrder) userDAO.saveObjectDB(order);

					remark.append(order.getOrderNum())
							.append("，扣款金额：").append(cashMoney.subtract(money).toString())
							.append("元。");
//					this.saveTradeDetail(null,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB, cashMoney.subtract(money), order.getOrderId(), null,remark.toString());
			
					this.saveTradeDetail(null,user.getUserId(), Constants.TRADE_TYPE_PAY,Constants.CASH_TYPE_CASH_REPAIR_DETAIL_SUB, cashMoney.subtract(money), order.getOrderId(), null,remark.toString(),money);

				}
			}
		}
		//批量更新开奖用户实时余额 --by.cuisy.20171209
//		this.updateUserMoney(userIds);
	}
	public void updateUserMoney(List<Integer> userIds,String type){
		userDAO.updateUserMoney(userIds,type);
	}
	public void updateUserMoney(Integer userId){
		userDAO.updateUserMoney(userId);	
	}
	public void updateUserMoneyAndBetMoney(Integer userId){
		userDAO.updateUserMoneyAndBetMoney(userId);	
	}
	public void updateUserMoneyAndBetMoney(List<Integer> userIds){
		userDAO.updateUserMoneyAndBetMoney(userIds);
	}
	public void updateUserPoints(Integer userId) {
		userDAO.updateUserPoints(userId);	
	}
	
	public void updateUserAddUpRechargeMoney(User user, BigDecimal cashMoney, Integer reId){
		
		// 跳级的奖励金额
		BigDecimal levelAward = this.levelAward(user, cashMoney);
		if (levelAward.compareTo(new BigDecimal(0)) == 1) {
			this.saveTradeDetail(user, user.getUserId(),
					Constants.TRADE_TYPE_INCOME,
					Constants.CASH_TYPE_RECHARGE_AWARD, levelAward,
					reId, null, "充值跳级奖励:"+levelAward);
		}
		
		userDAO.updateUserAddUpRechargeMoney(user);
	}
	
    public BigDecimal updateUserBanlance(Integer userId){
    	return userDAO.updateUserBanlance(userId);
    }
    
    public void updateUserTradeDetailStatus(String sessionNo, String gameType,
			String status) {
    	userDAO.updateUserTradeDetailStatus(sessionNo, gameType, status);
	}

	public void updateUserPoints(List<Integer> userIds) {
		userDAO.updateUserPoints(userIds);
	}
		public void updateUserBetCountPersonStatus(UserBetCount userBetCount,String status) {
		userDAO.updateUserBetCountPersonStatus(userBetCount,status);
	}

	public void updateUserBetCountAgentStatus(UserBetCount userBetCount) {
		userDAO.updateUserBetCountAgentStatus(userBetCount);
	}


	public IpRecord getIpRecordByIp(String ip) {
		return userDAO.getIpRecordByIp(ip);
	}
	public void saveTradeDetail(User user, Integer userId, String tradeType,
			String gameType, BigDecimal cashMoney, Integer refId){
		this.saveTradeDetail(userId, tradeType, gameType, cashMoney, refId);
	}


	public void updateUserMoney(List<Integer> userIds) {
		userDAO.updateUserMoney(userIds);
	}
	public void updateUserProperty(String property, Object obj,Integer userId){
		userDAO.updateUserProperty(property, obj, userId);
	}

	@Override
	public PaginationSupport findHeroicList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		return userDAO.findHeroicList(hql, para, pageNum, pageSize);
	}

	public String addLevel(UserLevel userLevel,Integer id) {
		
		String msg = "";
		// 等级名称与等级不能重复
		
		// 查询重复的 等级名称和等级
		HQUtils hq = new HQUtils();
		hq.addHsql(" from UserLevel ul where 1=1 ");
		hq.addHsql(" and (ul.levelName = ? or ul.level = ?)");
		hq.addPars(userLevel.getLevelName());
		hq.addPars(userLevel.getLevel());
		List<Object> llist = userDAO.findObjects(hq);
		
		if(llist.size()>0){
			for(Object obj:llist){
				UserLevel ul = (UserLevel) obj;
				if(ul.getId().equals(id)){
					
				}else{
					if(ul.getLevelName().equals(userLevel.getLevelName())){
						msg = "等级名称重复";
					}else if(ul.getLevel().equals(userLevel.getLevel())){
						msg = "等级重复";
					}
					break;
				}
			}
		}
		
		if(ParamUtils.chkInteger(id)){
			// 修改
			if(msg.length()==0){
				// 
				UserLevel savedUl = (UserLevel) userDAO.getObject(UserLevel.class, id);
				savedUl.setAward(userLevel.getAward());
				savedUl.setPoint(userLevel.getPoint());
				savedUl.setLevel(userLevel.getLevel());
				savedUl.setLevelName(userLevel.getLevelName());
				userDAO.saveObject(savedUl);
			}
		}else{
			//新增
			if(msg.length()==0){
				// 
				userDAO.saveObject(userLevel);
			}
		}
		return msg;
	}


	@Override
	public User getValidUser(Integer userId) {
		return userDAO.getValidUser(userId);
	}
	
	@Override
	public void updateUserBalanceMoney(List<Integer> userIds) {
		userDAO.updateUserBalanceMoney(userIds);
	}

	
	
	@Override
	public PaginationSupport findUserList(String userName, String userType, String startDate,
			String endDate, int startIndex, int pageSize) {
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();

		if(ParamUtils.chkString(userName)){
			hsql.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if(ParamUtils.chkString(userType)){
			hsql.append(" and u.userType=? ");
			para.add(userType);
		} else {
			hsql.append(" and u.userType<? ");
			para.add(Constants.USER_TYPE_TEST);
		}
		hsql.append(" order by u.userId desc ");
		//查询出用户
		PaginationSupport ps = this.findUser(hsql.toString(), para, startIndex, pageSize);
		return ps;
	}

	@Override
	public List<GaDTO> findUserTradeDetailList(String ids,
			String startDate, String endDate) {
		List<GaDTO> list = new ArrayList<GaDTO>();
		
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		if(ParamUtils.chkString(startDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate + " 00:00:00");
		}
		if(ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate + " 23:59:59");
		}
		
		hsql.append(" and ho.userId in (");
		hsql.append(ids).append(")");
		hsql.append(" group by ho.cashType,ho.userId ");
		list = userDAO.findUserTradeDetailList(hsql.toString(),para);
		return list;
	}

	@Override
	public List<GaDTO> findUserBetCountList(String ids, String startDate,
			String endDate) {
		List<GaDTO> list = new ArrayList<GaDTO>();
		//查询用户盈亏---购买彩票可统计部分。
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		hsql.append(" and ho.cashType = ? ");
		para.add(Constants.CASH_TYPE_CASH_BUY_LOTO);//
		hsql.append(" and ho.status = ? ");
		para.add(Constants.PUB_STATUS_OPEN);
		if(ParamUtils.chkString(startDate)){
			hsql.append(" and ho.createTime >= ? ");
			para.add(startDate + " 00:00:00");
		}
		if(ParamUtils.chkString(endDate)){
			hsql.append(" and ho.createTime <= ? ");
			para.add(endDate + " 23:59:59");
		}
		hsql.append(" and ho.userId in (");
		hsql.append(ids).append(")");
		hsql.append(" group by ho.userId ");
		list = userDAO.findUserPayoffList(hsql.toString(),para);
		return list;
	}

	@Override
	public List<GaDTO> findSubNumList(String ids) {
		List<GaDTO> list = new ArrayList<GaDTO>();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		hsql.append(" and u.agentId in (");
		hsql.append(ids).append(")");
		hsql.append(" group by u.agentId ");
		list = userDAO.findUserSubmemberList(hsql.toString(),para);
		return list;
	}

	@Override
	public List<UserMoneyDTO> updateUserMoneyDTO(List<UserMoneyDTO> list,
			List<GaDTO> tradeList, List<GaDTO> betCountList,
			List<GaDTO> memberList) {
		List<UserMoneyDTO> newList = new ArrayList<UserMoneyDTO>();
		
		for(UserMoneyDTO dto:list){
			Integer userId = dto.getUserId();
			BigDecimal totalRechage = new BigDecimal(0);//充值
			BigDecimal totalDrawMoney = new BigDecimal(0);//累计提现
			BigDecimal bet =  new BigDecimal(0); //总投注
			BigDecimal win = new BigDecimal(0);//中奖彩派
			BigDecimal fanshui = new BigDecimal(0);//返水
			BigDecimal huodong = new BigDecimal(0);//活动
			BigDecimal commission = new BigDecimal(0);//佣金
			BigDecimal totalPayoff = new BigDecimal(0); //盈利
			Integer memberNum = 0;//下线会员

			for(GaDTO tradeDto: tradeList){
				String cashType = tradeDto.getCashType();
				BigDecimal paperMoney = tradeDto.getPaperMoney();
				Integer userId2 = tradeDto.getUserId();
				
				if(userId.equals(userId2) && cashType != null && paperMoney != null){

					if(Constants.CASH_TYPE_ONLINE.equals(cashType)
							||Constants.CASH_TYPE_MANAGER_SET.equals(cashType)
							||Constants.CASH_TYPE_OFFLINE.equals(cashType)){
						//充值= 在线充值+管理员充值+线下充值
						totalRechage = totalRechage.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_OUT.equals(cashType)){//提现
						totalDrawMoney = totalDrawMoney.add(paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN.equals(cashType)){//下级投注返水
						commission = commission.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN.equals(cashType)){//下级充值返水
						commission = commission.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF.equals(cashType)){//自己投注返水
						fanshui = fanshui.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF.equals(cashType)){//充值赠送
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)||Constants.CASH_TYPE_CASH_REVOKE_PRIZE.equals(cashType)){
						//中奖彩派 = 中奖彩派+撤回彩派
						win = win.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_TURN_TABLE.equals(cashType)){//转盘
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_RED_PACKETS.equals(cashType)){//红包
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_PRESENT.equals(cashType)){//注册赠送
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)||Constants.CASH_TYPE_CASH_DRAWBACK.equals(cashType)){
						//投注 = 投注+投注退回
						bet = bet.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_SYS_CHARGE.equals(cashType)){//后台扣款
						totalRechage=totalRechage.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_RETURN.equals(cashType)){// 提现审核返回
						totalDrawMoney = totalDrawMoney.subtract(paperMoney); //提现减去审核返回金额
					}else if (Constants.CASH_TYPE_CASH_OTHER_SET.equals(cashType)){//其它加款--2018-04-18改为活动赠送
						huodong = huodong.add(paperMoney);
					}else if (Constants.CASH_TYPE_CASH_OTHER_CHARGE.equals(cashType)){//其它扣款
						totalRechage=totalRechage.add(paperMoney);
					}
				}
			}
			bet = bet.multiply(new BigDecimal("-1")).setScale(2, BigDecimal.ROUND_HALF_EVEN);//负数改为正数
			
			//用户盈亏--未计算投注
			totalPayoff = win.add(fanshui).add(huodong).add(commission);
					
			for(GaDTO subDto:memberList){
				Integer userId2 = subDto.getUserId();
				if(userId.equals(userId2)){
					memberNum = subDto.getNumber();
				}
			}
			
			for(GaDTO subDto:betCountList){
				Integer userId2 = subDto.getUserId();
				if(userId.equals(userId2)){
					totalPayoff =totalPayoff.add(subDto.getCountMoney());
				}
			}
			
			dto.setMemberNum(memberNum);
			dto.setTotalRechage(totalRechage);
			dto.setTotalDrawMoney(totalDrawMoney);
			dto.setBet(bet);
			dto.setWin(win);
			dto.setFanshui(fanshui);
			dto.setHuodong(huodong);
			dto.setCommission(commission);
			dto.setTotalPayoff(totalPayoff);
			newList.add(dto);
		}
		return newList;
	}

	@Override
	public UserMoneyDTO countSumMoneyDTO(String ids, String startDate,
			String endDate) {
		List<GaDTO> sumList = new ArrayList<GaDTO>();//各项统计
				
		//查询合计各项交易记录
		List<Object> para4 = new ArrayList<Object>();
		StringBuffer hsql4 = new StringBuffer();
		if(ParamUtils.chkString(startDate)){
			hsql4.append(" and ho.createTime >= ? ");
			para4.add(startDate + " 00:00:00");
		}
		if(ParamUtils.chkString(endDate)){
			hsql4.append(" and ho.createTime <= ? ");
			para4.add(endDate + " 23:59:59");
		}
		hsql4.append(" and (u.userType = ? or u.userType = ?) ");
		para4.add(Constants.USER_TYPE_AGENT);
		para4.add(Constants.USER_TYPE_SUER);
		
		hsql4.append(" and ho.userId in (");
		hsql4.append(ids).append(")");

		hsql4.append(" group by ho.cashType");
		sumList = userDAO.findSumUserTradeDetail(hsql4.toString(),para4);
		
		//查询用户盈亏总计--投注已开奖部分
		List<Object> para5 = new ArrayList<Object>();
		StringBuffer hsql5 = new StringBuffer();
		hsql5.append(" and ho.cashType = ? ");
		para5.add(Constants.CASH_TYPE_CASH_BUY_LOTO);//购买彩票
		hsql5.append(" and ho.status = ? ");
		para5.add(Constants.PUB_STATUS_OPEN);
		if(ParamUtils.chkString(startDate)){
			hsql5.append(" and ho.createTime >= ? ");
			para5.add(startDate + " 00:00:00");
		}
		if(ParamUtils.chkString(endDate)){
			hsql5.append(" and ho.createTime <= ? ");
			para5.add(endDate + " 23:59:59");
		}
		hsql5.append(" and ho.userId in (");
		hsql5.append(ids).append(")");

		hsql5.append(" and (ho.type = ? or ho.type = ?) ");
		para5.add(Constants.USER_TYPE_AGENT);
		para5.add(Constants.USER_TYPE_SUER);
		

		UserMoneyDTO sumDto = new UserMoneyDTO();//用户盈亏合计dto
		BigDecimal totalRechage = new BigDecimal(0);//充值
		BigDecimal totalDrawMoney = new BigDecimal(0);//累计提现
		BigDecimal bet =  new BigDecimal(0); //总投注
		BigDecimal win = new BigDecimal(0);//中奖彩派
		BigDecimal fanshui = new BigDecimal(0);//返水
		BigDecimal huodong = new BigDecimal(0);//活动
		BigDecimal commission = new BigDecimal(0);//佣金
		BigDecimal totalPayoff = new BigDecimal(0); //盈利

		if(sumList != null && sumList.size()>0){

			for(GaDTO tradeDto: sumList){
				String cashType = tradeDto.getCashType();
				BigDecimal paperMoney = tradeDto.getPaperMoney();
				if(cashType != null && paperMoney != null){
					if(Constants.CASH_TYPE_ONLINE.equals(cashType)||Constants.CASH_TYPE_MANAGER_SET.equals(cashType)
							||Constants.CASH_TYPE_OFFLINE.equals(cashType)){
						//充值= 在线充值+管理员充值+线下充值
						totalRechage = totalRechage.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_OUT.equals(cashType)){//提现
						totalDrawMoney = totalDrawMoney.add(paperMoney.abs().setScale(2, BigDecimal.ROUND_HALF_EVEN));
					}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN.equals(cashType)){//下级投注返水
						commission = commission.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_RECHARGE_RETURN.equals(cashType)){//下级充值返水
						commission = commission.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_BET_RETURN_SELF.equals(cashType)){//自己投注返水
						fanshui = fanshui.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_MEMBER_RETURN_SELF.equals(cashType)){//充值赠送
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_PRIZE.equals(cashType)||Constants.CASH_TYPE_CASH_REVOKE_PRIZE.equals(cashType)){
						//中奖彩派 = 中奖彩派+撤回彩派
						win = win.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_TURN_TABLE.equals(cashType)){//转盘
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_RED_PACKETS.equals(cashType)){//红包
						huodong = huodong.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_BUY_LOTO.equals(cashType)||Constants.CASH_TYPE_CASH_DRAWBACK.equals(cashType)){
						//投注 = 投注+投注退回
						bet = bet.add(paperMoney);
					}else if(Constants.CASH_TYPE_CASH_SYS_CHARGE.equals(cashType)){//后台扣款
					}else if(Constants.CASH_TYPE_CASH_RETURN.equals(cashType)){// 提现审核返回
						totalDrawMoney = totalDrawMoney.subtract(paperMoney); //提现减去审核返回金额
					}else if (Constants.CASH_TYPE_CASH_OTHER_SET.equals(cashType)){//其它加款--2018-04-18改为活动赠送
						huodong = huodong.add(paperMoney);
					}else if (Constants.CASH_TYPE_CASH_OTHER_CHARGE.equals(cashType)){//其它扣款
					}else if(Constants.CASH_TYPE_CASH_PRESENT.equals(cashType)){//注册赠送
						huodong = huodong.add(paperMoney);
					}

				}
			}
			bet = bet.multiply(new BigDecimal("-1")).setScale(2, BigDecimal.ROUND_HALF_EVEN);//负数改为正数
		
			//用户盈亏--未计算投注
			totalPayoff = win.add(fanshui).add(huodong).add(commission);
		}
		sumDto.setUserName("合计");
		totalPayoff = totalPayoff.add(ProductUtil.checkBigDecimal(userDAO.findSumUserPayoff(hsql5.toString(),para5)));
		
		sumDto.setTotalRechage(totalRechage);
		sumDto.setTotalDrawMoney(totalDrawMoney);
		sumDto.setBet(bet);
		sumDto.setWin(win);
		sumDto.setFanshui(fanshui);
		sumDto.setHuodong(huodong);
		sumDto.setCommission(commission);
		sumDto.setTotalPayoff(totalPayoff);
		return sumDto;
	}
	@Override
	public BigDecimal getDayRecharge(Integer uid) {
		return userDAO.getDayRecharge(uid);
	}
	
	@Override
	public PaginationSupport findUserLimitList(String hqls, List<Object> para,int startIndex,int endIndex){
		return userDAO.findUserLimit(hqls,para,startIndex,endIndex);
	}

	@Override
	public UserLimit findUserLimitByUid(Integer uid) {
		return userDAO.findUserLimitByUid(uid);
	}
	@Override
	public BigDecimal getDayBetMoney(Integer userId) {
		return userDAO.getDayBetMoney(userId);
	}

	@Override
	public BigDecimal getUserBetMoneyFromLastCash(Integer userId) {
		return userDAO.getUserBetMoneyFromLastCash(userId);
	}

	@Override
	public UserTradeDetail getUserLastRecharge(Integer userId) {
		return userDAO.getUserLastRecharge(userId);
	}

	@Override
	public BigDecimal getUserRechargeFromLastCash(Integer userId) {
		return userDAO.getUserRechargeFromLastCash(userId);
	}
	
	public Integer updateOpenData(GaBetDetail detail,User user,String remark,String type){
		
		userDAO.saveObject(detail);
		if(user==null){
			user=UserCacheUtil.getUser(detail.getUserId());
		}
		return this.saveTradeDetail(null,detail.getUserId(), Constants.TRADE_TYPE_INCOME,Constants.CASH_TYPE_CASH_PRIZE, detail.getWinCash(), detail.getBetDetailId(), 
				detail.getGameType(),remark,detail.getSessionNo(),type,user.getLoginName());
	}
}
