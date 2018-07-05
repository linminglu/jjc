package com.ram.dao.user.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.apps.model.UserTradeDetail;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.game.model.UserBetCount;
import com.game.model.dto.GaDTO;
import com.ram.RamConstants;
import com.ram.dao.user.IUserDAO;
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
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserLimit;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class UserDAOHibernate extends AbstractBaseDAOHibernate implements
		IUserDAO {

	private final String DELETE_SELECTED_USERS = "update User set status = '0' where userId = ?";

	private final String GET_MANAGER_BY_ID = "from Manager manager where manager.user.userId = ?";

	private final String GET_TEACHER_BY_ID = "from Tutor tutor where tutor.user.userId = ?";

	private final String MODIFY_USER_STATUS_BY_ID = "update User set status = ? where userId = ?";

	private final String GET_ALL_TUTOR = "from Tutor as tutor where tutor.user.status != ?";

	private final String GET_TUTOR_NUMBER = "select count(*) from Tutor as tutor where tutor.user.status != ?";

	private final String GET_USER_INFO = "from User as user where user.loginName = ? and user.password = ? and user.status = '1'";

	private final String CHECK_USER_EXIST = "from User as user where user.loginName = ?";

	private final String CHECK_USER_NAME_EXIST_AGAIN = "from User as user where user.loginName = ? and user.userId != ?";
	
	private final String CHECK_USER_EXIST_CODE = "from User as user where user.invitationCode = ?";

	

	// private final String GET_TUTOR_BY_COURSE ="select distinct tutor. from
	// Tutor as tutor where tutor.tutorCenter.tcId='1' and tutor.tutorId not
	// in(select tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where
	// tutorCourse.course.courseId=?)";

	// private final String GET_TUTORCENTER_BY_COURSE="select distinct
	// (tutorCenter) from TutorCenter as tutorCenter,Tutor as tutor where
	// tutorCenter.tcId=tutor.tutorCenter.tcId and tutor.tutorId not in (select
	// tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where
	// tutorCourse.course.courseId=?) ";
	// private final String GET_TUTORCENTER_BY_COURSE="select distinct
	// (tutorCenter) from TutorCenter as tutorCenter fetch join tutors";

	// private final String GET_TUTORCENTER_BY_COURSE="from TutorCenter as
	// tutorCenter where tutorCenter.tutors.tutorId not in (select
	// tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where
	// tutorCourse.course.courseId=?) ";

	// private final String GET_TUTOR_BY_COURSE ="select distinct
	// tutorCenter.tutorCenterId,tutor from Tutor as tutor,TutorCenter as
	// tutorCenter where tutorCenter.tutor.tutorId not in(select
	// tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where
	// tutorCourse.course.courseId=?)";
	// private final String GET_TUTOR_BY_TCID="from Tutor tutor where
	// tutor.tutorCenter.tcId = ? and tutor.tutorId not in(select
	// tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where
	// tutorCourse.course.courseId=?)";
	// private final String GET_TUTOR_BY_TCID=" select distinct (tutorCenter)
	// from TutorCenter as tutorCenter,Tutor as tutor where
	// tutorCenter.tcId=tutor.tutorCenter.tcId and tutorCenter.tcId = ? and
	// tutor.tutorId not in (select tutorCourse.tutor.tutorId from TutorCourse
	// as tutorCourse where tutorCourse.course.courseId=?)";

	private final String GET_USER_BY_LOGIN_NAME = "from User as user where user.loginName = ?";
	private final String GET_USER_BY_ACT_CODE = "from User as user where user.actCode = ?";

	private final String GET_USER_BY_USER_NAME = "from User as user where user.userNameZh = ?";

	private final String GET_USER_BY_STUDY_NUMBER = "select learner.learnerId from Learner as learner where learner.studyNumber = ?";

	private final String GET_STUDY_NUMBER = "select max(learner.studyNumber) from Learner learner where learner.studyNumber like ?";

	private final String GET_TUTOR_ID_BY_USER_ID = "select tutor.tutorId from Tutor tutor where tutor.user.userId = ?";

	private final String GET_LEARNER_BY_STUDYNUMBER = "select learner from Learner learner inner join fetch learner.user where learner.studyNumber = ?";

	private final String GET_LEARNER_BY_LoginName = "select learner from Learner learner inner join fetch learner.user where learner.user.loginName = ?";

	private final String FIND_TOPN_USERS_BY_LOGINTIMES = "from User as user where user.userType = ? order by user.loginTimes desc";

	private final String getUserByV3UserId = "from User user where user.v3UserId=? ";

	private static final String FIND_TUTOR_BY_TCID = " select user,tutor from "
			+ "Tutor as tutor,User as user where tutor.user.userId=user.userId and tutor.tutorCenter.tcId=? and "
			+ "tutor.tutorId not in(select tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where tutorCourse.course.courseId=?) and user.status=?";

	private static final String FIND_TECENT_TUTOR_BY_TCID = " select user,tutor from "
			+ "Tutor as tutor,User as user where tutor.user.userId=user.userId and tutor.tutorCenter.tcId=? and "
			+ "tutor.tutorId not in(select tutorCourse.tutor.tutorId from TutorCourse as tutorCourse where tutorCourse.course.courseId=? and tutorCourse.tutorType=?  and tutorCourse.semesterId=?) and user.status=?";

	private final String FIND_LEARNER_COUNT = "select count(*) from Learner l,User u"
			+ " where l.user.userId=u.userId and u.status='1' ";

	private final String FIND_LEARNER = " select l from Learner l,User u "
			+ " where l.user.userId=u.userId ";

	private final String GET_USER_BY_TUTORID = "from User u where u.userId=(select t.user.userId from Tutor t where t.tutorId=?)";

	private final String GET_ALL_BY_LOGINNAME_NUM = "select count(*) from User u where u.loginName like ? ";

	private static final String GET_USER_BY_STUDYNUMBER = "select u from User u,Learner l where l.studyNumber=? and u.userId=l.user.userId";

	private static final String GET_USER_BY_ENROLLNUMBER = "select u from User u,Learner l where l.enrollNumber=? and u.userId=l.user.userId order by u.userId desc";

	private static final String FIND_ONLINE_USER_RECORD = "from OnLineUserRecord onLineUserRecord order by onLineUserRecord.recordTime desc";
	private static final String FIND_ONLINE_USER_COUNT = "select count(*) from OnLineUserRecord onLineUserRecord";

	private static final String FIND_RECOMMENDED_USER_INFO = "from RecommendedUserInfo rui where 1=1";
	private static final String GET_RECOMMENDED_USER_INFO_NUM = "select count(rui.recommendedUserInfoId)from RecommendedUserInfo rui where 1=1";

	private static final String FIND_USER_LOG = "from UserLog ul where 1=1";
	private static final String FIND_USER_LOG_COUNT = "select count(*) from UserLog ul where 1=1";

	private static final String FIND_USER_LIST = "from User u where 1=1";
	private static final String FIND_USER_COUNT = "select count(*) from User u where 1=1";
	
	private final String GET_USER_INFO_ORG_UN = "from User as user where user.loginName = ? and user.status = '1'";

	public PaginationSupport findUserLog(String hsql, List para,
			int startIndex, int pageSize) {
		Query record = makeQuerySQL(FIND_USER_LOG + hsql
				+ " order by ul.dateTime desc", para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(FIND_USER_LOG_COUNT + hsql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	/**
	 * 创建/修改一个用户
	 */
	public boolean saveUser(User user, User operUser)
			throws DataAccessException {
		try {
			saveObject(user, operUser);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除一个用户
	 */
	public boolean deleteUser(int[] id, User user) throws DataAccessException {
		try {
			Query query = getSession().createQuery(DELETE_SELECTED_USERS);
			for (int i = 0; i < id.length; i++) {
				query.setInteger(0, id[i]);
				query.executeUpdate();
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 创建/修改一个用户
	 */
	public Manager getManager(int userId) throws DataAccessException {
		try {
			Query query = getSession().createQuery(GET_MANAGER_BY_ID);
			query.setCacheable(true);
			query.setInteger(0, userId);
			List userList = query.list();
			Iterator itr = userList.iterator();
			Manager manager = null;
			if (itr.hasNext()) {
				manager = (Manager) itr.next();
			}
			return manager;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建/修改一个用户
	 */
	public Tutor getTeacher(int id) throws DataAccessException {
		try {
			Query query = getSession().createQuery(GET_TEACHER_BY_ID);
			query.setCacheable(true);
			query.setInteger(0, id);
			List userList = query.list();
			Iterator itr = userList.iterator();
			if (itr.hasNext()) {
				return (Tutor) itr.next();
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 修改用户的状态使之有效或者无效
	 */
	public void modifyUserStatus(int id, String status, User user)
			throws DataAccessException {
		try {
			Query query = getSession().createQuery(MODIFY_USER_STATUS_BY_ID);
			query.setString(0, status);
			query.setInteger(1, id);
			query.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * 获得所有教师信息(分页)
	 */
	public List getAllTutors(int startIndex, int pageSize) {
		try {
			Query query = getSession().createQuery(GET_ALL_TUTOR);
			query.setString(0, RamConstants.DELETE_STATUS);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List tutorList = query.list();
			return tutorList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得所有教师信息(根据courseId)
	 */
	public List findAllTutorsByCourseId() {
		try {
			Criteria criteria = getSession().createCriteria(TutorCenter.class);
			criteria.setFetchMode("tutors", FetchMode.JOIN).createAlias(
					"tutors.user", "user");
			criteria.setFetchMode("user", FetchMode.JOIN).add(
					Expression.eq("user.status", "1"));
			// 注释部运行时出现错误
			// criteria.setFetchMode("tutors",FetchMode.JOIN).setFetchMode("tutors.user",FetchMode.JOIN).
			// add(Expression.eq("user.status","1"));
			// 动态抓取防止学习中心出现重复值

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.addOrder(Order.asc("tcId"));
			List tutorList = criteria.list();

			return tutorList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// public List findAllTutorsByCourseId(){
	// Query query = getSession().createQuery(GET_TUTORCENTER_BY_COURSE);
	// List tutorList = query.list();
	// return tutorList;
	// }
	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public List findAllTutorsByTcId(int tcId) {
		try {
			Criteria criteria = getSession().createCriteria(TutorCenter.class);
			criteria.setFetchMode("tutors", FetchMode.JOIN).createAlias(
					"tutors.user", "user");
			criteria.setFetchMode("user", FetchMode.JOIN).add(
					Expression.eq("user.status", "1"));

			// criteria.setFetchMode("tutors",FetchMode.JOIN).
			// setFetchMode("tutors.tutorCenter",FetchMode.JOIN).
			// add(Restrictions.idEq(new Integer(tcId)));
			// 动态抓取防止学习中心出现重复值

			criteria.add(Restrictions.idEq(new Integer(tcId)));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.addOrder(Order.asc("tcId"));
			List tutorList = criteria.list();
			return tutorList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得所有教师信息(根据tcId)
	 */
	public Query findAllTutorsByTcId(int tcId, int courseId) {
		try {
			Query query = getSession().createQuery(FIND_TUTOR_BY_TCID);
			query.setInteger(0, tcId);
			query.setInteger(1, courseId);
			query.setString(2, "1");
			return query;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Query findAllCenterTutorsByTcId(int tcId, int courseId,
			String tutorType, int semesterId) {
		try {
			Query query = getSession().createQuery(FIND_TECENT_TUTOR_BY_TCID);
			query.setInteger(0, tcId);
			query.setInteger(1, courseId);
			query.setString(2, tutorType);
			query.setInteger(3, semesterId);
			query.setString(4, "1");
			return query;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得查询后教师的信息(分页)
	 */
	public PaginationSupport findAllTutors(int startIndex, int pageSize,
			final DetachedCriteria detachedCriteria) {
		return findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}

	/**
	 * 根据一个用户名和密码获得一个User
	 */
	public User getUser(String name, String password) {
		Query query = getSession().createQuery(" from User as user where (user.loginName = ? or user.userId=?) and user.password = ? and user.status = '1' ");
		// query.setCacheable(true);
		query.setString(0, name);
		query.setString(1, name);
		query.setString(2, password);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}

	public User getUser(Integer originId, Integer originUserId) {
		try {
			return (User) getSession()
					.createQuery(
							"from User u where u.organizationId=? and u.originUserId=?")
					.setInteger(0, originId).setInteger(1, originUserId)
					.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获得教师用户的数目
	 */
	public int getTutorsNumber() {
		try {
			Query query = getSession().createQuery(GET_TUTOR_NUMBER);
			query.setString(0, "0");
			Iterator item = query.iterate();
			int count = 0;
			if (item.hasNext()) {
				count = ((Integer) item.next()).intValue();
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 检查用户名是否有重复
	 */
	public boolean checkUserExist(String loginName) {
		try {
			Query query = getSession().createQuery(CHECK_USER_EXIST);
			query.setString(0, loginName);
			List userInfo = query.list();
			if (userInfo.size() == 0) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public boolean checkUserExist(Integer uid){
		Integer count = (Integer)getSession().createQuery("select count(*) from User u where u.userId=?")
		.setInteger(0, uid)
		.uniqueResult();
		return count>0?true:false;
		
	}
	
	public User findUserByItCode(String itCode) {
		try {
			Query query = getSession().createQuery(CHECK_USER_EXIST_CODE);
			query.setString(0, itCode);
			List userInfo = query.list();
			if (userInfo.size() > 0) {
				return (User)userInfo.get(0);
			}else{
				return new User();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new User();
		}
	}
	
	public User findUserByloginName(String loginName) {
		User user = null;
		try {
			Query query = getSession().createQuery(CHECK_USER_EXIST);
			query.setString(0, loginName);
			List userInfo = query.list();
			if (userInfo.size() > 0) {
				user = (User)userInfo.get(0);
				return user;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return user;
	}
	

	/**
	 * 检查修改后的用户名和其他用户的用户名是否冲突
	 * 
	 * 
	 */
	public boolean checkUserExistAgain(String loginName, Integer userId) {
		try {
			Query query = getSession().createQuery(CHECK_USER_NAME_EXIST_AGAIN);
			query.setString(0, loginName);
			query.setInteger(1, userId.intValue());
			List userInfo = query.list();
			if (userInfo.size() == 0) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public User getUser(int userId) {
		try {
			return (User) getObject(User.class, new Integer(userId));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据用户名取得用户信息
	 * 
	 * 
	 * @return
	 */
	public Integer getUser(String loginName) {
		try {
			Query query = getSession().createQuery(GET_USER_BY_LOGIN_NAME);
			query.setCacheable(true);
			query.setString(0, loginName);
			Iterator item = query.iterate();
			if (item.hasNext()) {
				return ((User) item.next()).getUserId();
			} else {
				return null;
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}

	public User getUserByLoginName(String loginName) {
		try {
			Query query = getSession().createQuery(GET_USER_BY_LOGIN_NAME);
			query.setCacheable(true);
			query.setString(0, loginName);
			return (User) query.uniqueResult();
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}
	}
	
	public User getUserByloginComm(String accoutName,String password){
		List list = getSession().createQuery("from User u where u.status='1' and u.loginName=? and u.password=?")
		.setString(0, accoutName)
		.setString(1, password)
		.list();
		if(list==null || list.size()==0){
			list = getSession().createQuery("from User u where u.status='1' and u.userEmail=? and u.password=?")
			.setString(0, accoutName)
			.setString(1, password)
			.list();
		}
		if(list!=null && list.size()>0){
			return (User)list.get(0);
		}
		return null;
	}
	public User getUserByloginCommSj(String accoutName,String password){
		List list = getSession().createQuery("from User u where u.status='1' and (u.userType='3' or u.userType='2') and u.loginName=? and u.password=?")
		.setString(0, accoutName)
		.setString(1, password)
		.list();
		if(list==null || list.size()==0){
			list = getSession().createQuery("from User u where u.status='1' and (u.userType='3' or u.userType='2') and u.userEmail=? and u.password=?")
			.setString(0, accoutName)
			.setString(1, password)
			.list();
		}
		if(list!=null && list.size()>0){
			return (User)list.get(0);
		}
		return null;
	}

	public User getUserByActCode(String actCode) {
		@SuppressWarnings("unchecked")
		List<User> list = getSession().createQuery(GET_USER_BY_ACT_CODE)
				.setString(0, actCode).list();
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}
	public User findUserByLoginName(String userName) {
		@SuppressWarnings("unchecked")
		List<User> list = getSession().createQuery(" from User as user where user.loginName = ?  order by user.userId desc ")
				.setString(0, userName).list();
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据用户姓名取得用户ID
	 * 
	 * @return
	 */
	public Integer getUserId(String userName) {
		try {
			Query query = getSession().createQuery(GET_USER_BY_USER_NAME);
			query.setString(0, userName);
			Iterator item = query.iterate();
			if (item.hasNext()) {
				return ((User) item.next()).getUserId();
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据数据库中的数据获得当前学生所在年级层次中心的学籍号的最大序号
	 * 
	 * 
	 */
	public String getMaxStudyNumber(String str) {
		try {
			Query query = getSession().createQuery(GET_STUDY_NUMBER);
			query.setString(0, str + "%");
			return (String) query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据UserId获得TutorId
	 * 
	 * @param userId
	 * @return
	 */
	public Integer getTutorIdByUserId(Integer userId) {
		try {
			Query query = getSession().createQuery(GET_TUTOR_ID_BY_USER_ID);
			query.setInteger(0, userId.intValue());
			return (Integer) query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据学籍号获得learnerId
	 * 
	 * @param studyNumber
	 * @return
	 */
	public Integer getLearnerIdByStudyNumber(String studyNumber) {
		try {
			Query query = getSession().createQuery(GET_USER_BY_STUDY_NUMBER);
			query.setString(0, studyNumber);
			return (Integer) query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Learner getLearnerInfoByStudyNumber(String studyNumber) {
		try {
			Query query = getSession().createQuery(GET_LEARNER_BY_STUDYNUMBER);
			query.setString(0, studyNumber);
			return (Learner) query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Learner getLearnerInfoByLoginName(String loginName) {
		try {
			Query query = getSession().createQuery(GET_LEARNER_BY_LoginName);
			query.setString(0, loginName);
			List list = query.list();
			Learner learner = null;
			if (list != null && list.size() > 0) {
				learner = (Learner) list.get(0);
			}
			return learner;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 用户登录排行
	 * 
	 * @param offset
	 *            页数
	 * @param pageSize
	 * @param userType
	 * @return
	 */
	public List findTopnUsersByLoginTimes(int offset, int pageSize,
			String userType) {
		try {
			offset = offset < 0 ? 0 : offset;
			Query query = getSession().createQuery(
					FIND_TOPN_USERS_BY_LOGINTIMES);
			query.setString(0, userType);
			query.setFirstResult(offset * pageSize);
			query.setMaxResults(pageSize);
			List userList = query.list();
			return userList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据BWOLV3的userid得到对应的BWOLV4的userId
	 * 
	 * 这两个id都在users表中记录
	 */
	public User getUserByV3UserId(int v3UserId) {
		try {
			Query query = getSession().createQuery(getUserByV3UserId);
			// log.info("getUserByV3UserId=" + getUserByV3UserId + ",v3UserId="
			// + v3UserId);

			query.setInteger(0, v3UserId);
			List list = query.list();
			User user = null;
			if (list != null) {
				if (list.size() == 1) {
					user = (User) list.get(0);
					return user;
				} else if (list.size() > 1) {
					user = (User) list.get(0);
					log.info("旧平台上一个用id在新平台上有多个用户记录，获取的用户为------userid="
							+ user.getUserId() + ",loginname="
							+ user.getLoginName());
					log.info("返回空值，请检查");
					return null;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List findAllLearner() {
		try {
			String hql = "from Learner l order by l.userId";
			Query q = getSession().createQuery(hql);
			return q.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public int findLearner(String where, List list) {
		try {
			Query query = getSession().createQuery(FIND_LEARNER_COUNT + where);
			Iterator it = list.iterator();
			int i = 0;
			Object obj;
			while (it.hasNext()) {
				obj = it.next();
				if (obj instanceof String) {
					query.setString(i, (String) obj);
				} else if (obj instanceof Integer) {
					query.setInteger(i, ((Integer) obj).intValue());
				} else if (obj instanceof Date) {
					query.setDate(i, (Date) obj);
				}
				i++;
			}
			Iterator item = query.iterate();
			int count = 0;
			if (item.hasNext()) {
				count = ((Integer) item.next()).intValue();
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public List findLearner(int offset, int rows, String where, List list) {
		try {
			Query query = getSession().createQuery(
					FIND_LEARNER + where + " order by l.learnerId desc");
			Iterator it = list.iterator();
			int i = 0;
			Object obj;
			while (it.hasNext()) {
				obj = it.next();
				if (obj instanceof String) {
					query.setString(i, (String) obj);
				} else if (obj instanceof Integer) {
					query.setInteger(i, ((Integer) obj).intValue());
				} else if (obj instanceof Date) {
					query.setDate(i, (Date) obj);
				}
				i++;
			}
			query.setFirstResult(offset * rows);
			query.setMaxResults(rows);

			return query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public User getUserByTutorId(int tutorId) {
		try {
			Query query = getSession().createQuery(GET_USER_BY_TUTORID);
			query.setInteger(0, tutorId);
			return (User) query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public int findAllLearner(String where, List list) {
		try {
			Query q = getSession()
					.createQuery(
							"select count(*) from Learner l,User u where l.user.userId=u.userId and u.status='1'"
									+ where);
			Iterator it = list.iterator();
			int i = 0;
			Object obj;
			while (it.hasNext()) {
				obj = it.next();
				if (obj instanceof String) {
					q.setString(i, (String) obj);
				} else if (obj instanceof Integer) {
					q.setInteger(i, ((Integer) obj).intValue());
				} else if (obj instanceof Date) {
					q.setDate(i, (Date) obj);
				}
				i++;
			}
			List item = q.list();
			int count = 0;
			if (item.size() > 0) {
				count = ((Integer) item.get(0)).intValue();
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public List findAllLearner(int offset, int rows, String where, List list) {
		try {
			Query q = getSession().createQuery(
					"from Learner l inner join fetch l.user u where u.status='1'"
							+ where + " order by l.learnerId desc");
			Iterator it = list.iterator();
			int i = 0;
			Object obj;
			while (it.hasNext()) {
				obj = it.next();
				if (obj instanceof String) {
					q.setString(i, (String) obj);
				} else if (obj instanceof Integer) {
					q.setInteger(i, ((Integer) obj).intValue());
				} else if (obj instanceof Date) {
					q.setDate(i, (Date) obj);
				}
				i++;
			}
			q.setFirstResult(offset * rows);
			q.setMaxResults(rows);
			return q.list();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void updateLearnerEnrollRegisterStatus(Integer learnerId) {
		// try {
		// String sql =
		// "update Learner set enrollRegisterStatus=? where learnerId=? and enrollRegisterStatus=?";
		// Query q = getSession().createQuery(sql);
		// q.setString(
		// 0,
		// RamConstants.EnrollRegisterStatus_ZhengShiSheng_WeiRuXueZhuCe);// 2
		// q.setInteger(1, learnerId.intValue());
		// // q.setString(2, RamConstants.EnrollRegisterStatus_JinXiuSheng);// 1
		// q.executeUpdate();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		//
		// }
	}

	public void updateLearnerEnrollRegisterStatus(String regDate, List learnerId) {
		// try {
		// String sql =
		// "update Learner set enrollRegisterStatus=?, enrollRegisterDate=? "
		// + "where enrollRegisterStatus=? and learnerId in(:learnerId)";
		// Query q = getSession().createQuery(sql);
		// q
		// .setString(
		// 0,
		// RamConstants.EnrollRegisterStatus_ZhengShiSheng_YiRuXueZhuCe);// 3
		// q.setString(1, regDate);
		// q
		// .setString(
		// 2,
		// RamConstants.EnrollRegisterStatus_ZhengShiSheng_WeiRuXueZhuCe);// 2
		// q.setParameterList("learnerId", learnerId);
		// q.executeUpdate();
		// } catch (Exception ex) {
		// ex.printStackTrace();
		//
		// }
	}

	/**
	 * 通过用户名查找所有用户人数
	 * 
	 * 
	 */
	public int getALLByLoginNameNum(String loginName) {
		try {
			Query q = this.getSession().createQuery(GET_ALL_BY_LOGINNAME_NUM);
			q.setString(0, loginName + "%");
			List list = q.list();
			if (list != null && !list.isEmpty()) {
				return ((Integer) list.get(0)).intValue();
			} else {
				return 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public User getUserByStudyNumber(String studyNumber) {
		try {
			Query q = getSession().createQuery(GET_USER_BY_STUDYNUMBER)
					.setString(0, studyNumber);
			if (q.list().size() > 1)
				return (User) q.list().get(0);
			else
				return (User) q.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public User getUserByEnrollNumber(String enrollNumber) {
		try {
			Query q = getSession().createQuery(GET_USER_BY_ENROLLNUMBER)
					.setString(0, enrollNumber);
			if (q.list().size() > 1)
				return (User) q.list().get(0);
			else
				return (User) q.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
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
		this.saveObject(onlineUserRecord, user);
	}

	/**
	 * 统计登录人数
	 * 
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List findOnlineUserRecord(int offset, int rows) {
		Query query = this.getSession().createQuery(FIND_ONLINE_USER_RECORD);
		query.setFirstResult(offset * rows);
		query.setMaxResults(rows);
		List list = query.list();
		return list;
	}

	public int getOnlineUserCount() {
		Query query = this.getSession().createQuery(FIND_ONLINE_USER_COUNT);
		List list = query.list();
		Integer count = null;
		if (list != null && !list.isEmpty()) {
			count = (Integer) list.get(0);
		}
		return count.intValue();
	}

	public List findRecommendedUser(String str, List list, int startIndex,
			int pageSize) {
		Query q = getSession().createQuery(FIND_RECOMMENDED_USER_INFO + str);
		Iterator it = list.iterator();
		int i = 0;
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setDate(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			}
			i++;
		}
		q.setFirstResult(startIndex);
		q.setMaxResults(pageSize);
		return q.list();
	}

	public int getRecommendedUserNum(String str, List list) {
		Query q = getSession().createQuery(GET_RECOMMENDED_USER_INFO_NUM + str);
		Iterator it = list.iterator();
		int i = 0;
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setDate(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			}
			i++;
		}
		return ((Integer) q.uniqueResult()).intValue();
	}

	// **************************************
	private static final String GET_DESK_REGISTER = "from DeskRegister dr where dr.serialNumber=?";
	private static final String GET_DESK_SOFTWARE = "from DeskSoftware ds where ds.softwareId=?";
	private static final String GET_DESK_SOFTWARE_BY_CODE = "from DeskSoftware ds where ds.softwareCode=?";
	private static final String GET_DESK_TRIAL = "from DeskTrial dt where dt.softwareCode=? and dt.userMac=?";
	private static final String GET_MOBILE_TRIAL = "from MobileTrial dt where dt.softwareCode=? and dt.userMac=?";
	private static final String CHECK_DESKREGISTER_MAC_ONLY = "select count(*) from DeskRegister dr where dr.regMac=? and dr.softwareCode=?";
	private static final String UPDATE_DESK_SOFTWARE_TRIAL_COUNT = "update DeskSoftware set trialCount=trialCount+{step} where softwareCode=?";
	private static final String FIND_DESK_SOFTWARE_LIST = "select new com.ram.model.DeskSoftware(ds.deskSoftwareId,ds.softwareCode,ds.title,ds.sellprice) from DeskSoftware ds where 1=1 ";
	private static final String CHECK_DESK_REGISTER = "select count(*) from DeskRegister dr where dr.serialNumber=?";
	private static final String GET_DESK_REGISTER_SELL = "select sum(dr.sellprice) from DeskRegister dr where dr.sntype=? and dr.status>0";
	private static final String GET_DESK_REGISTER_SELL2 = "select sum(dr.sellprice) from DeskRegister dr where dr.sntype=? and dr.status in(0,1,2)";
	private static final String GET_DESK_REGISTER_SELL_OF_USER = "select sum(dr.sellprice) from DeskRegister dr where dr.sntype=? and dr.userId=? and dr.status>0";
	private static final String GET_DESK_REGISTER_SELL_JOIN = "select sum(ds.sellprice) from DeskRegister dr,DeskSoftware ds where dr.softwareCode=ds.softwareCode and dr.status in(0,1,2) and dr.sntype=?";
	private static final String GET_DESK_REGISTER_SELL_BY_SQL = "select sum(dr.sellprice) ";
	private static final String GET_DESK_REGISTER_SELL_BY_SQL_JOIN = "select sum(ds.sellprice) ";
	private static final String getDeskRegisterByCodeMac = "from DeskRegister dr where dr.softwareCode=? and dr.regMac=?";
	private static final String getDeskRegisterByCodeLike = "from DeskRegister dr";
	private static final String getDeskRegisterByCodeAutoSelect = "select min(dr.deskRegisterId) from DeskRegister dr where dr.status='0' and dr.softwareCode=?";

	private static final String CHECK_USER_EMAIL_EXIST = "from User as user where user.userEmail = ?";

	public DeskRegister getDeskRegister(String sn) {
		@SuppressWarnings("rawtypes")
		List list = getSession().createQuery(GET_DESK_REGISTER)
				.setString(0, sn).list();
		if (list.size() > 0) {
			return (DeskRegister) list.get(0);
		} else {
			return null;
		}
	}

	public DeskRegister getDeskRegisterByCodeMac(String code, String mac) {
		List list = getSession().createQuery(getDeskRegisterByCodeMac)
				.setString(0, code).setString(1, mac).list();
		if (list.size() > 0)
			return (DeskRegister) list.get(0);
		else
			return null;
	}

	public DeskRegister getDeskRegisterByCodeLike(String code, String whereSql) {
		List list = getSession().createQuery(
				getDeskRegisterByCodeLike + whereSql).list();
		if (list.size() > 0)
			return (DeskRegister) list.get(0);
		else
			return null;
	}

	public DeskRegister getDeskRegisterByCodeAutoSelect(String code) {
		Integer minId = (Integer) getSession()
				.createQuery(getDeskRegisterByCodeAutoSelect)
				.setString(0, code).uniqueResult();
		return (DeskRegister) getObject(DeskRegister.class, minId);
	}

	public boolean checkDeskRegisterSNOk(String sn) {
		Integer count = (Integer) getSession().createQuery(CHECK_DESK_REGISTER)
				.setString(0, sn).uniqueResult();
		if (ParamUtils.chkIntegerPlus(count)) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DeskSoftware> findDeskSoftwareList(String where) {
		return getSession().createQuery(
				FIND_DESK_SOFTWARE_LIST + where + " order by ds.title").list();
	}

	public DeskSoftware getDeskSoftware(Integer softwareId) {
		List list = getSession().createQuery(GET_DESK_SOFTWARE)
				.setInteger(0, softwareId).list();
		if (list != null && list.size() > 0) {
			return (DeskSoftware) list.get(0);
		} else {
			return null;
		}
	}

	public DeskSoftware getDeskSoftware(String softwareCode) {
		List list = getSession().createQuery(GET_DESK_SOFTWARE_BY_CODE)
				.setString(0, softwareCode).list();
		if (list != null && list.size() > 0) {
			return (DeskSoftware) list.get(0);
		} else {
			return null;
		}
	}

	public DeskTrial getDeskTrial(String softwareCode, String mac) {
		List list = getSession().createQuery(GET_DESK_TRIAL)
				.setString(0, softwareCode).setString(1, mac).list();
		if (list != null && list.size() > 0) {
			return (DeskTrial) list.get(0);
		} else {
			return null;
		}
	}

	public MobileTrial getMobileTrial(String softwareCode, String sn) {
		List list = getSession().createQuery(GET_MOBILE_TRIAL)
				.setString(0, softwareCode).setString(1, sn).list();
		if (list != null && list.size() > 0) {
			return (MobileTrial) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public DeskSoftwareVersion getDeskSoftwareVersion(String softwareCode,
			String v) {
		List<DeskSoftwareVersion> list = getSession()
				.createQuery(
						"from DeskSoftwareVersion dsv where dsv.softwareCode=? and dsv.softwareVersion=?")
				.setString(0, softwareCode).setString(1, v).list();
		if (list != null && list.size() > 0) {
			return (DeskSoftwareVersion) list.get(0);
		} else {
			return null;
		}
	}

	public void updateDeskSoftwareTrialCount(String softwareCode, int step) {
		getSession()
				.createQuery(
						UPDATE_DESK_SOFTWARE_TRIAL_COUNT.replace("{step}", step
								+ "")).setString(0, softwareCode)
				.executeUpdate();
	}

	public boolean checkDeskRegisterMacOnly(String mac, String softwareCode) {
		Integer count = (Integer) getSession()
				.createQuery(CHECK_DESKREGISTER_MAC_ONLY).setString(0, mac)
				.setString(1, softwareCode).uniqueResult();
		if (count == null)
			count = 0;
		return (count == 0) ? true : false;
	}

	public Integer getDeskRegisterSell(String sntype) {
		return (Integer) getSession().createQuery(GET_DESK_REGISTER_SELL)
				.setString(0, sntype).uniqueResult();
	}

	public Integer getDeskRegisterSell2(String sntype) {
		return (Integer) getSession().createQuery(GET_DESK_REGISTER_SELL2)
				.setString(0, sntype).uniqueResult();
	}

	public Integer getDeskRegisterSellOfUser(String sntype, Integer userId) {
		return (Integer) getSession()
				.createQuery(GET_DESK_REGISTER_SELL_OF_USER)
				.setString(0, sntype).setInteger(1, userId).uniqueResult();
	}

	public Integer getDeskRegisterSellJoin(String sntype) {
		return (Integer) getSession().createQuery(GET_DESK_REGISTER_SELL_JOIN)
				.setString(0, sntype).uniqueResult();
	}

	public Integer getDeskRegisterSellBySql(String hsql, List<Object> pars) {
		return (Integer) this.getParsQuerySQL(
				GET_DESK_REGISTER_SELL_BY_SQL + hsql, pars).uniqueResult();
	}

	public Integer getDeskRegisterSellBySqlJoin(String hsql, List<Object> pars) {
		return (Integer) this.getParsQuerySQL(
				GET_DESK_REGISTER_SELL_BY_SQL_JOIN + hsql, pars).uniqueResult();
	}

	// 构造Query********************************************
	public Query makeQuerySQL(String hsql, List param) {
		Query q = getSession().createQuery(hsql);
		Iterator<Object> it = param.iterator();
		int i = 0;
		Object obj;
		while (it.hasNext()) {
			obj = it.next();
			if (obj instanceof String) {
				q.setString(i, (String) obj);
			} else if (obj instanceof Date) {
				q.setTimestamp(i, (Date) obj);
			} else if (obj instanceof Integer) {
				q.setInteger(i, ((Integer) obj).intValue());
			} else if (obj instanceof Integer[]) {
				q.setParameterList("ids", (Integer[]) obj);
			}
			i++;
		}
		return q;
	}

	public PaginationSupport findUser(String hqls, List<Object> para,
			int startIndex, int pageSize) {
//		Query record = makeQuerySQL(FIND_USER_LIST + hqls, para);
//		record.setFirstResult(startIndex);
//		record.setMaxResults(pageSize);
//		List queList = record.list();
//		// 总记录数
//		Query count = makeQuerySQL(FIND_USER_COUNT + hqls, para);
//		Integer totalCount = (Integer) count.uniqueResult();
//		return new PaginationSupport(queList, totalCount.intValue());
		String FIND_USER_LIST = "select u from User u where 1=1";
	    String FIND_USER_COUNT = "select count(*) from User u where 1=1";
		if(hqls.indexOf("and su.sid=?")>0||hqls.indexOf("and su.type=?")>0){
			FIND_USER_LIST = "select u from User u,SellerUserRl su where u.userId=su.uid ";
			FIND_USER_COUNT = "select count(*) from User u,SellerUserRl su where u.userId=su.uid ";
		}
		System.out.println(FIND_USER_LIST + hqls);
		Query record = makeQuerySQL(FIND_USER_LIST + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(FIND_USER_COUNT + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public boolean checkUserEmailExist(String email) {
		List list = getSession().createQuery(CHECK_USER_EMAIL_EXIST)
				.setString(0, email).list();
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public User saveObjectDB(User regUser) {
		Serializable serializable = getHibernateTemplate().save(regUser);
		return (User) getHibernateTemplate().get(User.class, serializable);
	}

	public void delUserAbsolute(Integer userId, User operator) {
		//删除用户组关系
//		List ugList = getSession().createQuery("from UserGroupRl rl where rl.user.userId=? ").setInteger(0, userId.intValue()).list();
//		getHibernateTemplate().deleteAll(ugList);
		/*getSession().createQuery("delete from GzlmActivity where releUser=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmActivitySingup where user=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmNews where releUser=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmNewsComm where user=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmPicItems where userId=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmSuccour where releUser=?").setInteger(0, userId.intValue()).executeUpdate();
		getSession().createQuery("delete from GzlmSuccourComm  where user=?").setInteger(0, userId.intValue()).executeUpdate();*/
		getSession().createQuery("delete from User where userId=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_activity where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_activity_singup where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_news where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_news_comm where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_succour where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
//		getSession().createSQLQuery("delete from gzlm_succour_comm where user_id=?").setInteger(0, userId.intValue()).executeUpdate();
		log.error("##彻底删除用户:"+userId+",operator:"+operator.getUserId());
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllToken(){
		return getSession().createQuery("select gt.deviceToken from GzlmToken gt").list();
	}



	@Override
	public User getUser(Integer orgType, String name) {
		Query query = getSession().createQuery(GET_USER_INFO_ORG_UN);
		// query.setCacheable(true);
		//query.setInteger(0, orgType);
		query.setString(0, name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}


	@Override
	public Integer countSumRechargePoints(String rechargeType,
			Integer betSessionId) {
		StringBuffer hql = new StringBuffer("select sum(rc.rechargeBalance) from Recharge rc where rc.rechargeType='"+rechargeType+"'");
		if(ParamUtils.chkInteger(betSessionId)){
			hql.append(" and rc.recordId=");
			hql.append(betSessionId);
		}
		return (Integer)getSession().createQuery(hql.toString()).uniqueResult();
	}
	
	public void modifyBalance(Integer userId,BigDecimal userpoints,String operateType){
		String hql="";
		if("1".equals(operateType)){
			hql="update User set userpoints =userpoints + ? where userId = ? ";
		}else{
			hql="update User set userBalance =userpoints - ? where userId = ? ";
		}
		Query query = getSession().createQuery(hql);
		query.setBigDecimal(0, userpoints);
		query.setInteger(1, userId);
		query.executeUpdate();
	}
	public void updateBalance(BigDecimal userpoints,Integer userId){
		String hql = "update User set userpoints =? where userId = ?";
		Query query = getSession().createQuery(hql);
		query.setBigDecimal(0, userpoints);
		query.setInteger(1, userId);
		query.executeUpdate();
	}

	@Override
	public PaginationSupport findUserTradeDetail(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL("select new com.apps.model.UserTradeDetail(" +
		        "de.tradeDetailId,"+
				"de.userId," +
				"de.loginName," +
				"de.tradeType," +
				"de.cashType," +
				"de.cashMoney," +
				"de.userMoney," +
				"de.createTime," +
				"de.modelType," +
				"de.remark," +
				"de.sessionNo," +
				"de.refId" +
				") from UserTradeDetail de,User u where de.userId=u.userId  "+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		if(hql.contains("order by")) hql = hql.split("order by")[0];//count去掉排序
		Query count = makeQuerySQL("select count(*) from UserTradeDetail de,User u where de.userId=u.userId  "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public PaginationSupport findBetList(String hql, List<Object> para,
			int pageNum, int pageSize) {
		Query query = makeQuerySQL( " select new com.ram.model.dto.UserDTO(u,be) from User u,GaBetDetail be where 1=1 "
				+ " and u.userId = be.userId "+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(be.betDetailId) from  User u,GaBetDetail be where 1=1" +
				" and  u.userId = be.userId "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public User getAgentById(Integer agentId) {
		String  hql=" from User where  userId= ? ";
		Query query =  getSession().createQuery(hql);
		query.setInteger(0, agentId);
		List<User> list=query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;		
	}
	public List<User> findUserList(String hqls, List<Object> para) {
		Query query = makeQuerySQL("select u from User u where 1=1 "
				+ hqls, para);
		List<User> list = query.list();
		return 	list;
	}
	public User getAgentByName(String agentName){
		String  hql=" from User where  loginName= ? ";
		Query query =  getSession().createQuery(hql);
		query.setString(0, agentName);
		List<User> list=query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;		
	}


	public BigDecimal getSumCashMoneyByUserId(Integer userId){
		String hql="select sum(ho.cashMoney)  from UserTradeDetail ho where ho.userId=? and ho.status='1' ";
		Query query =  getSession().createQuery(hql);
		query.setInteger(0, userId);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return (BigDecimal) list.get(0);
		}else{
			return new BigDecimal(0);
		}
		
	}
	
	public BigDecimal getSumCashMoneyByDate(Integer userId, String startDate, String endDate, String cashType){
		String hql="select sum(ho.cashMoney) from UserTradeDetail ho where ho.userId=? and ho.createTime>=? and ho.createTime<=? and ho.status='1' and ho.cashType=?";
		Query query =  getSession().createQuery(hql);
		query.setInteger(0, userId);
		query.setString(1, startDate);
		query.setString(2, endDate);
		query.setString(3, cashType);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return (BigDecimal) list.get(0);
		}else{
			return new BigDecimal(0);
		}
	}

	public void updateUserMoney(List<Integer> userIds) {
		if(userIds!=null && userIds.size()>0){
//			List<User> saveList = new ArrayList<User>();
			for(Integer id:userIds){
//				User user = (User)getObject(User.class, id);
				BigDecimal money = getCountUserMoney(id);
				BigDecimal banlance = getCountUserBanlance(id);//每日盈亏
				BigDecimal dayWinMoney=this.getDayWinMoney(id);//日中奖
				BigDecimal weekWinMoney=this.getWeekWinMoney(id);//周中奖
				BigDecimal winMoney=this.getWinMoney(id);//累计中奖

//				BigDecimal dayWinMoney=new BigDecimal(0);//日中奖
//				BigDecimal weekWinMoney=new BigDecimal(0);//周中奖
//				BigDecimal winMoney=new BigDecimal(0);//累计中奖
				
				getSession().createQuery("update User set money=?, userBalance=?,dayWinMoney=?,weekWinMoney=?,winMoney=? where userId=?")
				.setBigDecimal(0, money.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(1, banlance.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(2, dayWinMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(3, weekWinMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(4, winMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setInteger(5, id)
				.executeUpdate();
				//log.info("[dao.ids.setUserMoney]>>> userId="+id+",user.money="+money);
			}

		}

		
	}


	public void updateUserMoney(List<Integer> userIds, String type) {
		if(userIds!=null && userIds.size()>0){
			for(Integer id:userIds){
				BigDecimal money = getCountUserMoney(id);
				BigDecimal banlance = getCountUserBanlance(id);//每日盈亏
				BigDecimal dayWinMoney=this.getDayWinMoney(id);//日中奖
				BigDecimal weekWinMoney=this.getWeekWinMoney(id);//周中奖
				BigDecimal winMoney=this.getWinMoney(id);//累计中奖	
				BigDecimal dayBetMoney=this.getDayBetMoney(id);
				BigDecimal betMoney=this.getBetMoney(id);

//				BigDecimal dayWinMoney=new BigDecimal(0);//日中奖
//				BigDecimal weekWinMoney=new BigDecimal(0);//周中奖
//				BigDecimal winMoney=new BigDecimal(0);//累计中奖	s
//				BigDecimal dayBetMoney=new BigDecimal(0);
//				BigDecimal betMoney=new BigDecimal(0);
				
				getSession().createQuery("update User set money=?, userBalance=?,dayWinMoney=?,weekWinMoney=?,winMoney=?,dayBetMoney=?,aggregateBetMoney=? where userId=?")
				.setBigDecimal(0, money.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(1, banlance.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(2, dayWinMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(3, weekWinMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(4, winMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(5, dayBetMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(6, betMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setInteger(7, id)
				.executeUpdate();
				log.info("[dao.ids.setUserMoney]>>> userId="+id+",user.money="+money);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public void updateUserTradeDetailStatus(String sessionNo, String gameType,String stauts) {
//		StringBuffer hql = new StringBuffer();
//		List<Object> para = new ArrayList<Object>();
//		hql.append("update UserTradeDetail set status = ? where ");
//		para.add(stauts);
//		hql.append(" sessionNo = ? ");
//		para.add(sessionNo);
//		hql.append(" and cashType = ? ");
//		para.add(Constants.CASH_TYPE_CASH_BUY_LOTO);
//		hql.append(" and modelType = ? ");
//		para.add(gameType);
//		Query query = makeQuerySQL(hql.toString() , para);
//		query.executeUpdate();
		
//		String hql = "update UserTradeDetail set status='1' where sessionNo='"+sessionNo+"' and cashType='"+Constants.CASH_TYPE_CASH_BUY_LOTO+"' and modelType='"+gameType+"'";
//		GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"BatD="+hql);
//		getSession().createQuery(hql).executeUpdate();
		
		List<UserTradeDetail> list = getSession().createQuery("from UserTradeDetail a where a.sessionNo='"+sessionNo+"' and a.cashType='"+Constants.CASH_TYPE_CASH_BUY_LOTO+"' and a.modelType='"+gameType+"'").list();
		for(UserTradeDetail utd:list){
			utd.setStatus(Constants.PUB_STATUS_OPEN);
		}
		updateObjectList(list, null);
		
		GameHelpUtil.log(Constants.getGameCodeOfGameType(gameType),"update trade detail status..."+sessionNo);
	}

	public void updateUserMoney(Integer userId) {
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? ")
				.setInteger(0, userId)
				.uniqueResult();
				if(money==null) money = new BigDecimal(0);//无为0
				//更新账户余额
				getSession().createQuery("update User set money=? where userId=?")
				.setBigDecimal(0, money)
				.setInteger(1, userId)
				.executeUpdate();
				
				log.info("[dao.setUserMoney]>>> userId="+userId+",user.money="+money);
		
	}
	
	public void updateUserAddUpRechargeMoney(User user) {
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd"
				+ " where utd.userId=? and utd.status='1' and utd.cashType=? ")
				.setInteger(0, user.getUserId())
				.setString(1, Constants.CASH_TYPE_MANAGER_SET)
				.uniqueResult();
				if(money==null) money = new BigDecimal(0);//无为0
				user.setAddUpRechargeMoney(money);
				getSession().update(user);
	}


	public BigDecimal updateUserBanlance(Integer userId) {
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from"
				+ " UserTradeDetail utd where utd.userId=? and utd.createTime>=? and utd.createTime<=?"
				+ " and (utd.cashType=? or utd.cashType=?) and utd.status=?")
				.setInteger(0, userId)
				.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
				.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
				.setString(3, Constants.CASH_TYPE_CASH_BUY_LOTO)
				.setString(4, Constants.CASH_TYPE_CASH_PRIZE)
				.setString(5, Constants.PUB_STATUS_OPEN)
				.uniqueResult();
		//从投注表里查询每日的输赢
//		BigDecimal gfMoney = (BigDecimal)getSession().createQuery("select sum(gbs.winCash)-sum(gbs.money) from GaBetSponsor gbs where gbs.userId=? and gbs.betTime>=? and gbs.betTime<=? and (gbs.winResult=? or gbs.winResult=?)")
//				.setInteger(0, userId)
//				.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
//				.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
//				.setString(3, Constants.WIN)
//				.setString(4, Constants.WIN_NO)
//				.uniqueResult();
//		if(gfMoney==null) gfMoney = new BigDecimal(0);//无为0
//		
//		BigDecimal xyMoney = (BigDecimal)getSession().createQuery("select sum(gbd.winCash)-sum(gbd.betMoney) from GaBetDetail gbd where gbd.userId=? and gbd.betTime>=? and gbd.betTime<=? and (gbd.winResult='1' or gbd.winResult='2')")
//				.setInteger(0, userId)
//				.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
//				.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
//				.uniqueResult();
//		if(xyMoney==null) xyMoney = new BigDecimal(0);//无为0
//				
//		BigDecimal money = gfMoney.add(xyMoney);
		
		//更新账户余额
		getSession().createQuery("update User set userBalance=? where userId=?")
		.setBigDecimal(0, money)
		.setInteger(1, userId)
		.executeUpdate();
		
		log.info("[dao.setUserBalance]>>> userId="+userId+",user.userBalance="+money);
	
		return money;
	}


	public void updateUserPoints(List<Integer> userIds) {
		if(userIds!=null && userIds.size()>0){
			for(Integer id:userIds){
				BigDecimal userpoints = getCountUserPoints(id);
				getSession().createQuery("update User set userpoints=? where userId=?")
				.setBigDecimal(0, userpoints)
				.setInteger(1, id)
				.executeUpdate();
				log.info("[dao.ids.setUserPoints]>>> userId="+id+",user.points="+userpoints);
			}
		}	
	}
	
	public BigDecimal getCountUserPoints(Integer userId){//统计用户实时积分
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashPoint) from UserPointDetail utd where utd.userId=?")
		.setInteger(0, userId)
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}

	public void updateUserPoints(Integer userId) {
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashPoint) from UserPointDetail utd where utd.userId=?")
				.setInteger(0, userId)
				.uniqueResult();
				if(money==null) money = new BigDecimal(0);//无为0
				//更新账户余额
				getSession().createQuery("update User set userpoints=? where userId=?")
				.setBigDecimal(0, money)
				.setInteger(1, userId)
				.executeUpdate();
				
				log.info("[dao.setUserPoints]>>> userId="+userId+",user.points="+money);
	}


	public void updateUserMoneyAndBetMoney(Integer userId) {
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' ")
				.setInteger(0, userId)
				.uniqueResult();
				if(money==null) money = new BigDecimal(0);//无为0
//				BigDecimal dayBetMoney=this.getDayBetMoney(userId);
				BigDecimal dayBetMoney=new BigDecimal(0);
				if(dayBetMoney==null) dayBetMoney = new BigDecimal(0);//无为0
//				BigDecimal betMoney=this.getBetMoney(userId);
				BigDecimal betMoney=new BigDecimal(0);
				if(betMoney==null) betMoney = new BigDecimal(0);//无为0
				//更新账户余额
				getSession().createQuery("update User set money=?,dayBetMoney=?,aggregateBetMoney=?  where userId=?")
				.setBigDecimal(0, money)
				.setBigDecimal(1, dayBetMoney)
				.setBigDecimal(2, betMoney)
				.setInteger(3, userId)
				.executeUpdate();
				
				log.info("[dao.setUserMoney]>>> userId="+userId+",user.money="+money);
	}


	public void updateUserMoneyAndBetMoney(List<Integer> userIds) {
		if(userIds!=null && userIds.size()>0){
			for(Integer id:userIds){

				BigDecimal money = getCountUserMoney(id);
				BigDecimal banlance = getCountUserBanlance(id);//每日盈亏
//				BigDecimal dayBetMoney=getDayBetMoney(id);
//				BigDecimal betMoney=getBetMoney(id);
				BigDecimal dayBetMoney=new BigDecimal(0);
				BigDecimal betMoney=new BigDecimal(0);
				getSession().createQuery("update User set money=?, userBalance=?,dayBetMoney=?,aggregateBetMoney=? where userId=?")
				.setBigDecimal(0, money.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(1, banlance.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(2, dayBetMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setBigDecimal(3, betMoney.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setInteger(4, id)
				.executeUpdate();
				log.info("[dao.ids.setUserMoney]>>> userId="+id+",user.money="+money);
			}

		}
		
	}

	public BigDecimal getCountUserMoney(Integer userId){//统计用户实时余额
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1'")
		.setInteger(0, userId)
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	public BigDecimal getCountUserBanlance(Integer userId){//统计用户今日输赢
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.createTime>=? and utd.createTime<=? and ( utd.cashType='10' or utd.cashType='18' or utd.cashType='39'  or utd.cashType='17' or utd.cashType='13'  or  utd.cashType='21' )")
		.setInteger(0, userId)
		.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
		.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	public BigDecimal getDayWinMoney(Integer userId){//统计用户日中奖
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.createTime>=? and utd.createTime<=? and utd.cashType='18' ")
		.setInteger(0, userId)
		.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
		.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	
	public BigDecimal getWeekWinMoney(Integer userId){//统计用户周中奖
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.createTime>=? and utd.createTime<=? and utd.cashType='18' ")
		.setInteger(0, userId)
		.setString(1, DateTimeUtil.DateToString(DateTimeUtil.getThisWeekMonday(new Date()))+" 00:00:00")
		.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	public BigDecimal getWinMoney(Integer userId){//统计用户累计中奖
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.cashType='18' ")
		.setInteger(0, userId)
//		.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
//		.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	public BigDecimal getDayBetMoney(Integer userId){//统计用户日投注
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.createTime>=? and utd.createTime<=? and (utd.cashType='10' or utd.cashType='39' or utd.cashType='17' or utd.cashType='13'  or utd.cashType='21' )")
		.setInteger(0, userId)
		.setString(1, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayStart()))
		.setString(2, DateTimeUtil.DateToStringAll(DateTimeUtil.getCurrentDayend()))
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	public BigDecimal getBetMoney(Integer userId){//统计用户投注总和
		BigDecimal money = (BigDecimal)getSession().createQuery("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and (utd.cashType='10' or utd.cashType='39' or utd.cashType='17' or utd.cashType='13'  or utd.cashType='21'  )")
		.setInteger(0, userId)
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
		public void updateUserBetCountPersonStatus(UserBetCount userBetCount,String status) {
		String hql="";
		hql="update UserBetCount set personStatus ='"+status+"'  where countId = ? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userBetCount.getCountId());
		query.executeUpdate();
	}


	public void updateUserBetCountAgentStatus(UserBetCount userBetCount) {
		String hql="";
		hql="update UserBetCount set agentStatus ='1'  where countId = ? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, userBetCount.getCountId());
		query.executeUpdate();
		
	}

	@Override
	public IpRecord getIpRecordByIp(String ip) {
		String hql="from IpRecord where ip=? ";
		Query query =  getSession().createQuery(hql);
		query.setString(0, ip);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return (IpRecord) list.get(0);
		}else{
			return null;
		}
	}
	public void updateUserProperty(String property, Object obj,Integer userId){
		String hql="";
		hql="update User set "+property+" =?  where userId = ? ";
		Query query = getSession().createQuery(hql);
		if (obj instanceof String) {
			query.setString(0, (String) obj);
		} else if (obj instanceof Date) {
			query.setTimestamp(0, (Date) obj);
		} else if (obj instanceof Integer) {
			query.setInteger(0, ((Integer) obj).intValue());
		} else if (obj instanceof BigDecimal) {
			query.setBigDecimal(0, (BigDecimal) obj);
		}
		query.setInteger(1, userId);
		query.executeUpdate();
	}

	@Override
	public PaginationSupport findHeroicList(String hql, List<Object> para,
			int pageNum, int pageSize) {
//		Query query = makeQuerySQL( " select new com.ram.model.dto.UserDTO(u,be) from User u,GaBetDetail be where 1=1 "
//				+ " and u.userId = be.userId "+hql, para);
		Query query = makeQuerySQL( " select new com.ram.model.User(u.userId,u.logo,u.userpoints) from User u where 1=1 "+hql, para);
		query.setFirstResult(pageNum);
		query.setMaxResults(pageSize);
		List queList = query.list();
		Query count = makeQuerySQL("select count(u.userId) from  User u where 1=1 "+hql, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	/**
	 * 获取有效的用户（status=1）
	 * @param userId
	 * @return
	 */
	public User getValidUser(Integer userId){
		Query query = getSession().createQuery("from User as user where user.status = '1' and user.userId=? ");
		query.setInteger(0, userId);
		List list = query.list();
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 统计用户今日输赢
	 * @param userId
	 * @return
	 */
	public BigDecimal getCountUserBalanceMoney(Integer userId){//统计用户实时今日输赢金额
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.status='1' and utd.cashType in (");
		sb.append(Constants.CASH_TYPE_CASH_BUY_LOTO);
		sb.append(",");
		sb.append(Constants.CASH_TYPE_CASH_PRIZE);
		sb.append(",");
		sb.append(Constants.CASH_TYPE_CASH_REVOKE_PRIZE);
		sb.append(",");
		sb.append(Constants.CASH_TYPE_CASH_DRAWBACK);
		sb.append(")");
		sb.append(" and utd.status =? ");
		sb.append(" and utd.createTime >= ? ");
		sb.append(" and utd.createTime <= ? ");
		BigDecimal money = (BigDecimal)getSession().createQuery(sb.toString())
		.setInteger(0, userId)
		.setString(1, Constants.PUB_STATUS_OPEN)
		.setString(2, DateTimeUtil.getCurrentDayStartStr())
		.setString(3, DateTimeUtil.getCurrentDayendStr())
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	
	@Override
	public void updateUserBalanceMoney(List<Integer> userIds) {
		if(userIds!=null && userIds.size()>0){
			for(Integer id:userIds){
				BigDecimal money = getCountUserBalanceMoney(id);
				getSession().createQuery("update User set userBalance=? where userId=?")
				.setBigDecimal(0, money.setScale(2,BigDecimal.ROUND_HALF_UP))//统一都保留两位小数
				.setInteger(1, id)
				.executeUpdate();
			}
		}
	}

	@Override
	public List<GaDTO> findUserTradeDetailList(String hql,List<Object> para) {
		String hqls="select new com.game.model.dto.GaDTO(ho.userId,sum(ho.cashMoney),ho.cashType) from UserTradeDetail ho where 1=1 ";
		Query query =  makeQuerySQL(hqls + hql , para);
		List<GaDTO> list=query.list();
		return list;
	}

	@Override
	public List<GaDTO> findUserPayoffList(String hsql, List<Object> para) {
		Query query = makeQuerySQL("select new com.game.model.dto.GaDTO(sum(ho.cashMoney),ho.userId) from UserTradeDetail ho where 1=1 "
				+ hsql, para);
		@SuppressWarnings("unchecked")
		List<GaDTO> list = query.list();
		return list;
	}

	@Override
	public List<GaDTO> findUserSubmemberList(String hqls, List<Object> para) {
		String hqls2="select new com.game.model.dto.GaDTO(u.agentId,u.agentName,count(u.userId)) from User u where 1=1 ";
		Query query =  makeQuerySQL(hqls2 + hqls , para);
		List<GaDTO> list=query.list();
		return list;
	}

	@Override
	public List<GaDTO> findSumUserTradeDetail(String hql, List<Object> para) {
		String hqls="select new com.game.model.dto.GaDTO(0,sum(ho.cashMoney),ho.cashType) from UserTradeDetail ho,User u where ho.userId = u.userId ";
		Query query =  makeQuerySQL(hqls + hql , para);
		@SuppressWarnings("unchecked")
		List<GaDTO> list=query.list();
		return list;
	}

	@Override
	public BigDecimal findSumUserPayoff(String hsql, List<Object> para) {
		Query query = makeQuerySQL("select sum(ho.cashMoney) from UserTradeDetail ho where 1=1 "
				+ hsql, para);
		@SuppressWarnings("unchecked")
		List<BigDecimal> list = query.list();
		if(list!=null&&list.size()>0){
			return (BigDecimal) list.get(0);
		}else{
			return new BigDecimal(0);
		}
	}
	
	@Override
	public BigDecimal getDayRecharge(Integer userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(utd.cashMoney) from UserTradeDetail utd where utd.userId=? and utd.cashType in (");
		sb.append(Constants.CASH_TYPE_ONLINE);
		sb.append(",");
		sb.append(Constants.CASH_TYPE_MANAGER_SET);
		sb.append(")");
		sb.append(" and utd.createTime >= ? ");
		sb.append(" and utd.createTime <= ? ");
		BigDecimal money = (BigDecimal)getSession().createQuery(sb.toString())
		.setInteger(0, userId)
		.setString(1, DateTimeUtil.getCurrentDayStartStr())
		.setString(2, DateTimeUtil.getCurrentDayendStr())
		.uniqueResult();
		if(money==null) money = new BigDecimal(0);//无为0
		return money;
	}
	public PaginationSupport findUserLimit(String hqls, List<Object> para,
			int startIndex, int pageSize) {
//		Query record = makeQuerySQL(FIND_USER_LIST + hqls, para);
//		record.setFirstResult(startIndex);
//		record.setMaxResults(pageSize);
//		List queList = record.list();
//		// 总记录数
//		Query count = makeQuerySQL(FIND_USER_COUNT + hqls, para);
//		Integer totalCount = (Integer) count.uniqueResult();
//		return new PaginationSupport(queList, totalCount.intValue());
		String FIND_USER_LIST = "select u from UserLimit u where 1=1";
	    String FIND_USER_COUNT = "select count(*) from UserLimit u where 1=1";
	    
		Query record = makeQuerySQL(FIND_USER_LIST + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(FIND_USER_COUNT + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public UserLimit findUserLimitByUid(Integer uid) {
		String FIND_USER_LIST = "from UserLimit u where 1=1 and u.uid = "+uid +" and u.betMoneyStatus = 1";
	    
		UserLimit record = (UserLimit)getSession().createQuery(FIND_USER_LIST).uniqueResult();
		return record;
	}

	@Override
	public BigDecimal getUserBetMoneyFromLastCash(Integer userId) {
		
		BigDecimal betMoney = new BigDecimal(0);
		// 查询最后一次提现记录
	    List<Object> para = new ArrayList<Object>();
	    String selectHql = "select MAX(u.createTime) ";
	    String hql = "from UserTradeDetail u where u.cashType = ? and u.userId = ? and u.status = ? order by u.createTime desc";
	    para.add(Constants.CASH_TYPE_CASH_OUT);
	    para.add(userId);
	    para.add(Constants.PUB_STATUS_OPEN);
	    
		Query record = makeQuerySQL(selectHql + hql, para);
		Date createTime = null;
		try {
			createTime = (Date) record.uniqueResult();
			if(createTime!=null){
				
			}
		} catch (Exception e) {
		}
		
		// 查询从上一次提现到现在的打码量
		List<Object> para2 = new ArrayList<Object>();
		String hql2 = "select sum(u.cashMoney) from UserTradeDetail u where u.cashType = ?  and u.userId = ? and u.status = ?";
		para2.add(Constants.CASH_TYPE_CASH_BUY_LOTO);
		para2.add(userId);
	    para2.add(Constants.PUB_STATUS_OPEN);
	    if(createTime !=null){
			hql2 += " and u.createTime > ?";
			para2.add(DateTimeUtil.getDateFormart(createTime, "yyyy-MM-dd HH-mm-SS"));
		}
	    GameHelpUtil.log("_cash",userId+",上次提现时间="+createTime);
	    try {
	    	Query record2 = makeQuerySQL(hql2, para2);
			Object obj = record2.uniqueResult();
			
			if(obj!=null){
				betMoney = new BigDecimal(""+record2.uniqueResult());
			}
		} catch (Exception e) {
		}
		return betMoney;
	}

	@Override
	public UserTradeDetail getUserLastRecharge(Integer userId) {
		// 查询最后一次充值记录
	    List<Object> para = new ArrayList<Object>();
	    String hql = "from UserTradeDetail u where u.cashType = ? and u.userId = ? order by u.tradeDetailId desc";
	    para.add(Constants.CASH_TYPE_MANAGER_SET);
	    para.add(userId);
	    
		Query record = makeQuerySQL(hql, para);
		List result1 = record.list();
		
		UserTradeDetail detail = (UserTradeDetail) result1.get(0);
	    
	    return detail;
	}

	@Override
	public BigDecimal getUserRechargeFromLastCash(Integer userId) {
		// 查询最后一次提现记录
		BigDecimal rechargeMoney = new BigDecimal(0);
	    List<Object> para = new ArrayList<Object>();
	    String selectHql = "select MAX(u.createTime) ";
	    String hql = "from UserTradeDetail u where u.cashType = ? and u.userId = ? and u.status = ? order by u.createTime desc";
	    para.add(Constants.CASH_TYPE_CASH_OUT);
	    para.add(userId);
	    para.add(Constants.PUB_STATUS_OPEN);
	    
		Query record = makeQuerySQL(selectHql+ hql, para);
		Date createTime = null;
		try {
			createTime = (Date) record.uniqueResult();
			if(createTime!=null){
				
			}
		} catch (Exception e) {
		}
		
		// 查询从上一次提款到现在的充值量
		// 充值包括：管理员充值27  其他加款28
		List<Object> para2 = new ArrayList<Object>();
		String hql2 = "select sum(u.cashMoney) from UserTradeDetail u where (u.cashType = ? or u.cashType = ?) and u.userId = ? and u.status = ?";
		para2.add(Constants.CASH_TYPE_CASH_OTHER_SET);
		para2.add(Constants.CASH_TYPE_MANAGER_SET);
		para2.add(userId);
	    para2.add(Constants.PUB_STATUS_OPEN);
		if(createTime !=null){
			hql2 += " and u.createTime > ?";
			para2.add(DateTimeUtil.getDateFormart(createTime, "yyyy-MM-dd HH-mm-SS"));
		}
		try {
	    	Query record2 = makeQuerySQL(hql2, para2);
			Object obj = record2.uniqueResult();
			
			if(obj!=null){
				rechargeMoney = new BigDecimal(""+record2.uniqueResult());
			}
		} catch (Exception e) {
		}
		return rechargeMoney;
	}
	
	
}
