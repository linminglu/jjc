package com.apps.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.apps.Constants;
import com.apps.dao.IBaseDataDAO;
import com.apps.model.Activity;
import com.apps.model.Advertising;
import com.apps.model.City;
import com.apps.model.CityBusArea;
import com.apps.model.CityCommunity;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.PayConfig;
import com.apps.model.SysOption;
import com.apps.model.Type;
import com.apps.model.UserTradeDetail;
import com.apps.model.UserTradeDetailRl;
import com.apps.model.Version;
import com.apps.model.dto.BaseDataDTO;
import com.cash.model.UserCheckoutOrderRl;
import com.framework.dao.hibernate.AbstractBaseDAOHibernate;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.game.model.GaK3Session;
import com.game.model.UserLevel;
import com.game.model.dto.SpDetailDTO;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public class BaseDataDAOHibernate extends AbstractBaseDAOHibernate implements
		IBaseDataDAO {
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

	public Version getDefVer(String type, String s) {
		Criteria criteria = getSession().createCriteria(Version.class);
		criteria.add(Restrictions.eq("isDef", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.eq("appType", s));
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			return (Version) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Advertising> findAdvertisingByType(String adType,
			String status, Integer cid) {
		List<Advertising> newList = new ArrayList<Advertising>();
		Criteria criteria = getSession().createCriteria(Advertising.class);
		criteria.add(Restrictions.eq("status", status));//状态
		criteria.add(Restrictions.eq("adType", adType));//类型
		
		//检查数据范围位置id
		if(ParamUtils.chkInteger(cid)) {
			if(Constants.PLAT_RUN_MODE_ID.equals(Constants.PLAT_RUN_MODE_CITY)){
				criteria.add(Restrictions.eq("cid", cid));//城市
			}else if(Constants.PLAT_RUN_MODE_ID.equals(Constants.PLAT_RUN_MODE_COMMUNITY)){
				criteria.add(Restrictions.eq("ccid", cid));//小区
			}else if(Constants.PLAT_RUN_MODE_ID.equals(Constants.PLAT_RUN_MODE_UNIVERSITY)){
				criteria.add(Restrictions.eq("unid", cid));//学校
			}
		}
		
		criteria.addOrder(Order.desc("sort"));// 降序
		criteria.addOrder(Order.desc("id"));// 降序
		criteria.setFirstResult(0);
		criteria.setMaxResults(Constants.ADVERTISING_LUNBO_HOME_MAX);
		
		List<Advertising> list = criteria.list();
		
		//小区模式时~{
//		System.out.println(Constants.PLAT_RUN_MODE_ID+"_______________");
//		System.out.println(Constants.PLAT_RUN_MODE_COMMUNITY+"_______________");
		if(Constants.PLAT_RUN_MODE_ID.equals(Constants.PLAT_RUN_MODE_COMMUNITY)){
		
			if(list==null || list.size()<Constants.ADVERTISING_LUNBO_HOME_MAX){//当小于5条时从商圈上再查询广告
				Criteria criteria1 = getSession().createCriteria(Advertising.class);
				criteria1.add(Restrictions.eq("status", status));
				criteria1.add(Restrictions.eq("adType", adType));
				if (ParamUtils.chkInteger(cid)) {
					Integer baid=this.getCityBusAreaByCcid(cid);
					criteria1.add(Restrictions.eq("busaid", baid));
				}
				criteria1.addOrder(Order.desc("sort"));// 降序
				criteria1.addOrder(Order.desc("id"));// 降序
				criteria1.setFirstResult(0);
				if(list==null){
					criteria1.setMaxResults(5);
					List<Advertising> list1=criteria1.list();
					if(list1==null){
						Criteria criteria2 = getSession().createCriteria(Advertising.class);
						criteria2.add(Restrictions.eq("status", status));
						criteria2.add(Restrictions.eq("adType", adType));
	//					if (ParamUtils.chkInteger(cid)) {
	//						Integer bid=this.getCityAreaByCcid(cid);
	//						criteria2.add(Restrictions.eq("cid", bid));
	//					}
						criteria2.addOrder(Order.desc("sort"));// 降序
						criteria2.addOrder(Order.desc("id"));// 降序
						criteria2.setFirstResult(0);
						criteria2.setMaxResults(5);
						List<Advertising> list2=criteria2.list();
						return list2;
					}else{
						Criteria criteria2 = getSession().createCriteria(Advertising.class);
						criteria2.add(Restrictions.eq("status", status));
						criteria2.add(Restrictions.eq("adType", adType));
	//					if (ParamUtils.chkInteger(cid)) {
	//						Integer bid=this.getCityAreaByCcid(cid);
	//						criteria2.add(Restrictions.eq("cid", bid));
	//					}
						criteria2.addOrder(Order.desc("sort"));// 降序
						criteria2.addOrder(Order.desc("id"));// 降序
						criteria2.setFirstResult(0);
						criteria2.setMaxResults(5-list1.size());
						List<Advertising> list2=criteria2.list();
						newList.addAll(list1);
						newList.addAll(list2);
						return newList;
					}
				}else{
					criteria1.setMaxResults(5-list.size());
					List<Advertising> list1=criteria1.list();
					if(list1==null){
						Criteria criteria2 = getSession().createCriteria(Advertising.class);
						criteria2.add(Restrictions.eq("status", status));
						criteria2.add(Restrictions.eq("adType", adType));
	//					if (ParamUtils.chkInteger(cid)) {
	//						Integer bid=this.getCityAreaByCcid(cid);
	//						criteria2.add(Restrictions.eq("cid", bid));
	//					}
						criteria2.addOrder(Order.desc("sort"));// 降序
						criteria2.addOrder(Order.desc("id"));// 降序
						criteria2.setFirstResult(0);
						criteria2.setMaxResults(5-list.size());
						List<Advertising> list2=criteria2.list();
						newList.addAll(list);
						newList.addAll(list2);
						return newList;
					}else  if((list.size()+list1.size())<5){
						Criteria criteria2 = getSession().createCriteria(Advertising.class);
						criteria2.add(Restrictions.eq("status", status));
						criteria2.add(Restrictions.eq("adType", adType));
	//					if (ParamUtils.chkInteger(cid)) {
	//						Integer bid=this.getCityAreaByCcid(cid);
	//						criteria2.add(Restrictions.eq("cid", bid));
	//					}
						criteria2.addOrder(Order.desc("sort"));// 降序
						criteria2.addOrder(Order.desc("id"));// 降序
						criteria2.setFirstResult(0);
						criteria2.setMaxResults(5-list.size()-list1.size());
						List<Advertising> list2=criteria2.list();
						newList.addAll(list);
						newList.addAll(list1);
						newList.addAll(list2);
						return newList;
					}else{
						newList.addAll(list1);
						newList.addAll(list);
						return newList;
					}
				}
			}
		}
		//}~
		
		return list;
	}

	public List<Advertising> findAdvertisingByTid(Integer tid) {
		Criteria criteria = getSession().createCriteria(Advertising.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.eq("adType", Constants.ADVERTISING_TYPE));
		criteria.add(Restrictions.eq("tid", tid));
		return criteria.list();
	}

	public List<Type> findAllTypeNotMore() {
		Criteria criteria = getSession().createCriteria(Type.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.ne("isMore", Constants.PUB_STATUS_OPEN));
		criteria.addOrder(Order.desc("sort"));// 降序
		return criteria.list();
	}

	public List<Type> findAllType() {
		Criteria criteria = getSession().createCriteria(Type.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.addOrder(Order.desc("sort"));// 降序
		return criteria.list();
	}

	public PaginationSupport findVerList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Version v where 1=1 ";
		String findCount = "select count(v.id) from Version v where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	public void updateVerNot(String type) {
		String hql = "update Version set isDef='0' where type=?";
		Query query = getSession().createQuery(hql);
		query.setString(0, type);
		query.executeUpdate();
	}

	public PaginationSupport findFeedbackList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "select new com.apps.model.dto.BaseDataDTO(f,u.userName) from Feedback f,User u where f.userId=u.userId ";
		String findCount = "select count(f.id) from Feedback f,User u where f.userId=u.userId ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	public PaginationSupport findPushList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findList = "from Push push where 1=1 ";
		String findCount = "select count(push.pushId) from Push push where 1=1 ";

		Query record = makeQuerySQL(findList + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List list = record.list();
		// 总记录数
		Query count = makeQuerySQL(findCount + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(list, totalCount.intValue());
	}

	@Override
	public List<Type> findTypeByPosition(String position) {
		Criteria criteria = getSession().createCriteria(Type.class);
		criteria.add(Restrictions.eq("status", Constants.PUB_STATUS_OPEN));
		criteria.add(Restrictions.eq("position", position));
		criteria.addOrder(Order.desc("sort"));// 降序
		return criteria.list();
	}

	public List<City> findCity(String type) {
		// String sql =
		// "select new com.apps.model.City(c.cid,c.title,c.pinyin) from City c where c.type=? ";
		String sql = "from City c where c.type=? order by c.pinyin,c.sort ";
		Query query = getSession().createQuery(sql);
		query.setString(0, type);
		return query.list();
	}
	
	public List<CityBusArea> findCityBusArea(){
		String sql = "from CityBusArea c where 1=1 order by c.sort desc";
		Query query = getSession().createQuery(sql);
		return query.list();
	}
	
	public List<CityBusArea> findCityBusArea(Integer cid){
		String sql = "from CityBusArea c where c.cid = ? ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, cid);
		return query.list();
	}
	
//	public List<EstateCompany> findEstateCompany(){
//		String sql = "from EstateCompany e where 1=1 ";
//		Query query = getSession().createQuery(sql);
//		return query.list();
//	}

	public List<City> findAreaByCityId(int cid) {
		String sql = "from City c where c.type=2 and c.parentId=? order by c.pinyin,c.sort ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, cid);
		return query.list();
	}

	public List<String> findCityKeys(String type) {
		String sql = "select c.pinyin from City c where c.type=? group by c.pinyin order by c.pinyin,c.sort ";
		Query query = getSession().createQuery(sql);
		query.setString(0, type);
		return query.list();
	}

	public PaginationSupport findCity(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		String findHql = "from City c where 1=1 ";
		String countHql = "select count(c.cid) from City c where 1=1 ";

		Query record = makeQuerySQL(findHql + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findCityBusArea(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findHql = "from CityBusArea c where 1=1 ";
		String countHql = "select count(c.busaid) from CityBusArea c where 1=1 ";

		Query record = makeQuerySQL(findHql + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findCityCommunity(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String findHql = "from CityCommunity c where 1=1 ";
		String countHql = "select count(c.ccid) from CityCommunity c where 1=1 ";

		Query record = makeQuerySQL(findHql + hqls, para);
		record.setFirstResult(startIndex);
		record.setMaxResults(pageSize);
		List queList = record.list();
		// 总记录数
		Query count = makeQuerySQL(countHql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	public void deleteAreaByCity(Integer cid) {
		String hql = "delete from City where parentId=? ";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, cid);
		query.executeUpdate();
	}

	public City getCityByCode(String cityName) {
		String sql = "from City c where c.type='1' and c.code like ? ";
		Query query = getSession().createQuery(sql);
		query.setString(0, "%"+cityName+"%");
		List list = query.list();
		if (list != null & list.size() > 0) {
			return (City) list.get(0);
		}
		return null;
	}

	public List<City> findCity(String type, String key) {
		String sql = "from City c where c.type=? and c.pinyin=? order by c.sort desc";
		Query query = getSession().createQuery(sql);
		query.setString(0, type);
		query.setString(1, key);
		return query.list();
	}
	public List<CityBusArea> findCityAreaByCityId(Integer cid){
		String sql = "from CityBusArea c where c.cid=? order by c.sort desc";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, cid);
		return query.list();
	}
	public List<CityCommunity> findCityCommunityByAId(Integer aid){
		String sql = "from CityCommunity c where c.busaid=?   order by c.sort desc";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, aid);
		return query.list();
	}
	public boolean findCityByCode(String code) {
		boolean flag = true;
		String sql = "from City c where c.type='1' and c.code=? ";
		Query query = getSession().createQuery(sql);
		query.setString(0, code);
		List<City> list = query.list();
		if (list != null & list.size() > 0) {
			for (City city : list) {
				if (code.equals(city.getCode())) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public List<Type> findTypeIndex() {
		String sql = "from Type t where t.status='1' and t.parentId=0 and (t.type=? or t.type=? or t.type=?) order by t.sort ";
		Query query = getSession().createQuery(sql);
		query.setString(0, Constants.MODULE_STORE);
		query.setString(1, Constants.MODULE_EAT);
		query.setString(2, Constants.MODULE_GROUP);
		return query.list();
	}
	/**
	 * 返回所有有效城市
	 * @return List<City>
	 */
	@SuppressWarnings("unchecked")
	public List<City> findCityOfValid(){
		return getSession().createQuery("from City c where c.status=1 order by c.code").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<City> findCityOfValidByCityId(Integer cityId){
		StringBuffer hq = new StringBuffer("from City c where c.status=1  ");
		if(ParamUtils.chkIntegerPlus(cityId)){
			hq.append("and c.cid=");
			hq.append(cityId);
		}
		hq.append("  order by c.cid");
		return getSession().createQuery(hq.toString()).list();
	}
	public void saveCityBusArea(CityBusArea cityBusArea){
		getSession().save(cityBusArea);
	}
	
	public void updateCityBusArea(CityBusArea cityBusArea){
		getSession().update(cityBusArea);
	}
	public void saveCityCommunity(CityCommunity cityCommunity){
		getSession().save(cityCommunity);
	}
	public void updateCityCommunity(CityCommunity cityCommunity){
		getSession().update(cityCommunity);
	}
	public  BigDecimal getPointsByType(String type){
		String sql = "select c.points  from SysOption c where c.optionType= ? ";
		Query query = getSession().createQuery(sql);
		query.setString(0, type);
		List list = query.list();
		if (list != null & list.size() > 0) {
			return (BigDecimal) list.get(0);
		}
		return new BigDecimal(0);
	}
	public PaginationSupport findIntegralList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String hql = "from SysOption s where 1=1 ";
		String sql = "select count(s.optionId) from SysOption s where 1=1";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public void updateIntegral(SysOption sysOption){
		getSession().update(sysOption);
	}
	
	public void updateIntegralPoints(BigDecimal points,Integer optionId){
		Query query = getSession().createQuery("update SysOption  set points = ? where optionId = ?");
		query.setBigDecimal(0, points);
		query.setInteger(1, optionId);
		query.executeUpdate();
	}
	
	public void saveIntegral(SysOption sysOption){
		getSession().save(sysOption);
	}
	public Integer getCityBusAreaByCcid(Integer ccid){
		String sql = "select c.busaid from CityCommunity c where c.ccid=? ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, ccid);
		List list = query.list();
		if (list != null & list.size() > 0) {
			return (Integer) list.get(0);
		}
		return null;
	}
	public Integer getCityAreaByCcid(Integer ccid){
		String sql = "select c.cid from CityCommunity c where c.ccid=? ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, ccid);
		List list = query.list();
		if (list != null & list.size() > 0) {
			return (Integer) list.get(0);
		}
		return null;
	}
	public PaginationSupport findUserPointDetail(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String hql = "select new com.apps.model.dto.BaseDataDTO(ud,u.userName) from UserPointDetail ud,User u where ud.userId = u.userId ";
		String sql = "select count(ud.tradeDetailId) from UserPointDetail ud,User u where ud.userId = u.userId";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findUserTradeDetail(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String hql = "select new com.apps.model.dto.BaseDataDTO(ud,u.userName) from UserTradeDetail ud,User u where ud.userId = u.userId  ";
		String sql = "select count(ud.tradeDetailId) from UserTradeDetail ud,User u where ud.userId = u.userId  and ho.type = 1 ";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public PaginationSupport findGaDayBetCountList(String string, List<Object> para,
			int startIndex, int pageSize){
		
		//GaDayBetCount 表 ，只有官方玩法开奖时才会更新
		// 去userTradeDetail表查询
		
		String hql = " select sum(ho.cashMoney),concat('',date_format(ho.createTime,'%Y-%m-%d'))";
		String sql = " select count(*)  ";
		
		String hqHead = " from UserTradeDetail ho where 1=1 and ho.status='1' and ho.modelType is not null  and ho.type=1 ";
		// 收入 奖金
		String param1 = " and ho.tradeType = 1";
		// 支出 投注
		String param2 = " and ho.tradeType = 2";
		
		String groupBy = " group by concat('',date_format(ho.createTime,'%Y-%m-%d'))";
		
		String orderBy = " order by ho.createTime desc";
		
		// 收入 奖金
		Query query1 = makeQuerySQL(hql+hqHead + param1+string+groupBy+orderBy, para);
		query1.setFirstResult(startIndex);
		query1.setMaxResults(pageSize);
		List queList1 = query1.list();
		
		// 支出  投注
		Query query2 = makeQuerySQL(hql+hqHead + param2+string+groupBy+orderBy, para);
		query2.setFirstResult(startIndex);
		query2.setMaxResults(pageSize);
		List queList2 = query2.list();
		
		Map<String, BaseDataDTO> dtoMap = new HashMap<String, BaseDataDTO>();
		
		for(Object obj:queList1){
			Object[] tmp = (Object[]) obj;
			if(tmp[1]!=null){
				if(dtoMap.get(tmp[1])==null){
					BaseDataDTO dto = new BaseDataDTO();
					dto.setTimestr(""+tmp[1]);
					dto.setWinMoney(new BigDecimal(""+tmp[0]));
					dto.setBetMoney2(new BigDecimal(0));
					dtoMap.put(""+tmp[1], dto);
				}
			}
		}
		for(Object obj:queList2){
			Object[] tmp = (Object[]) obj;
			if(tmp[1]!=null){
				if(dtoMap.get(tmp[1])==null){
					BaseDataDTO dto = new BaseDataDTO();
					dto.setBetMoney2(new BigDecimal(""+tmp[0]).abs());
					if(dto.getWinMoney()==null){
						dto.setWinMoney(new BigDecimal(0));
					}
					dto.setTimestr(""+tmp[1]);
					dtoMap.put(""+tmp[1], dto);
				}else{
					BaseDataDTO dto = dtoMap.get(tmp[1]);
					dto.setBetMoney2(new BigDecimal(""+tmp[0]).abs());
					if(dto.getWinMoney()==null){
						dto.setWinMoney(new BigDecimal(0));
					}
					dtoMap.put(""+tmp[1], dto);
				}
			}
		}
		List<BaseDataDTO> result = new ArrayList<BaseDataDTO>();
		for(BaseDataDTO dto :dtoMap.values()){
			dto.setPayoff(dto.getBetMoney2().subtract(dto.getWinMoney()));
			result.add(dto);
		}

		Collections.sort(result,new Comparator<BaseDataDTO>() {
            public int compare(BaseDataDTO o1, BaseDataDTO o2) {
            	String format = "yyyy-MM-dd";
            	long t1 = DateTimeUtil.StringToDate(o1.getTimestr(), format).getTime();
            	long t2 = DateTimeUtil.StringToDate(o2.getTimestr(), format).getTime();
            	if(t1>=t2){
            		return -1;
            	}else{
            		return 1;
            	}
            }
        });
		Query count = makeQuerySQL(sql + hqHead+string+groupBy, para);
		List queList3 = count.list();
		
		return new PaginationSupport(result, queList3.size());
	}
	public PaginationSupport findUserCheckout(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String hql = "select new com.apps.model.dto.BaseDataDTO(uc,u.userName) from UserCheckout uc,User u where uc.userId = u.userId  ";
		String sql = "select count(uc.checkoutId) from UserCheckout uc,User u where uc.userId = u.userId ";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	public List<UserCheckoutOrderRl> findOrderList(Integer checkoutId){
		Query query = getSession().createQuery("from UserCheckoutOrderRl uo where uo.checkoutId = ?");
		query.setInteger(0, checkoutId);
		List list = query.list();
		return list;
	}
	public BaseDataDTO findLatestOpenSession(){
		BaseDataDTO dto=null;
		

		
		GaK3Session hbs=null;
		String hql4="  from GaK3Session hb where  hb.openStatus='2' order by hb.sessionId desc ";
		List<GaK3Session> list4=getSession().createQuery(hql4).setMaxResults(1).list();
		if(list4!=null && list4.size()>0){
			hbs=(GaK3Session)list4.get(0);
		}
		
//		GaK10Session gx=null;
//		String hql5="  from GaK10Session gxk where  gxk.openStatus='2' order by gxk.sessionId desc ";
//		List<GaK10Session> list5=getSession().createQuery(hql5).setMaxResults(1).list();
//		if(list5!=null && list5.size()>0){
//			gx=(GaK10Session)list5.get(0);
//		}
//		dto=new BaseDataDTO(ahs,gxs,jls,jss,hbs);
		return dto;
	}


	public List<BaseDataDTO> findGfTotalBet(String hql, List<Object> para) {
//		Query query = makeQuerySQL( "select sum(ho.winCash),sum(ho.betMoney) from GaBetDetail ho where 1=1 "+hql, para);
//		query.setFirstResult(startIndex);
//		query.setMaxResults(pageSize);
//		List queList = query.list();
//		Query count = makeQuerySQL("select count(ho.betId) from Bj3GaBet ho, Bj3GaSession se where 1=1" +
//				" and  se.sessionId = ho.sessionId "+hql, para);
//		Integer totalCount = (Integer) count.uniqueResult();
//		return new PaginationSupport(queList, totalCount.intValue());
		Query query = makeQuerySQL("select new com.apps.model.dto.BaseDataDTO(sum(p.betMoney),sum(p.winCash),sum(p.betMoney)-sum(p.winCash)) from  GaBetSponsor ho,GaBetPart p where ho.betFlag='1' and p.jointId=ho.jointId  "
				+ hql, para);                  
		List<BaseDataDTO> list = query.list();
		return list;

	}

	public List<BaseDataDTO> findTotalBet(String hql, List<Object> para) {
//		Query query = makeQuerySQL( "select sum(ho.winCash),sum(ho.betMoney) from GaBetDetail ho where 1=1 "+hql, para);
//		query.setFirstResult(startIndex);
//		query.setMaxResults(pageSize);
//		List queList = query.list();
//		Query count = makeQuerySQL("select count(ho.betId) from Bj3GaBet ho, Bj3GaSession se where 1=1" +
//				" and  se.sessionId = ho.sessionId "+hql, para);
//		Integer totalCount = (Integer) count.uniqueResult();
//		return new PaginationSupport(queList, totalCount.intValue());
		String hqHead = "select sum(ho.cashMoney) from UserTradeDetail ho where 1=1  and ho.status='1' and ho.modelType is not null  and ho.type=1 ";
		// 收入
		String param1 = " and ho.tradeType = 1";
		// 支出
		String param2 = " and ho.tradeType = 2";
		
//		Query query = makeQuerySQL("select new com.apps.model.dto.BaseDataDTO(sum(ho.betMoney),sum(ho.winCash),sum(ho.betMoney)-sum(ho.winCash)) from  GaBetDetail ho where 1=1 "
//				+ hql, para);
		Query query1 = makeQuerySQL(hqHead+param1+hql, para);
		List<BigDecimal> list1 = query1.list();
		BigDecimal income = list1.get(0)!=null?list1.get(0):new BigDecimal(0);
		
		Query query2 = makeQuerySQL(hqHead+param2+hql, para);
		List<BigDecimal> list2 = query2.list();
		BigDecimal outcome = list2.get(0)!=null?list2.get(0):new BigDecimal(0);
		
		BaseDataDTO dto = new BaseDataDTO();
		dto.setBetMoney2(outcome);
		dto.setWinMoney(income);
		dto.setPayoff(income.add(outcome));
		
		List<BaseDataDTO> list = new ArrayList<BaseDataDTO>();
		list.add(dto);
		 
		return list;

	}
	@Override
	public PayConfig getPayByType(String payType) {
		String findList = "from PayConfig p where  p.type=? ";
		Query query = getSession().createQuery(findList);
		query.setString(0, payType);
		List<PayConfig> list = query.list();
		PayConfig pay = null; 
		if(list != null && list.size() >0){
			pay = list.get(0);
		}
		return pay;
	}

	@Override
	public List<PayConfig> findPayConfig() {
		String findList = "from PayConfig p where 1=1 ";
		Query query = getSession().createQuery(findList);
		return query.list();
	}

	@Override
	public List<Activity> findActivity() {
		String findList = " from Activity ac where 1=1 ";
		Query query = getSession().createQuery(findList);
		return query.list();
	}
	public List<NewsType>findNewsTypeList(){
		String findList = " from NewsType nt where nt.status='1' order by nt.sort ";
		Query query = getSession().createQuery(findList);
		return query.list();
	}
	public PaginationSupport findNewsList(String hqls, List<Object> para,
			int startIndex, int pageSize){
		String hql = " from NewsInformation n where 1=1  ";
		String sql = "select count(n.nid)  from NewsInformation n where 1=1  ";
		Query query = makeQuerySQL(hql + hqls, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + hqls, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}

	@Override
	public List<SpDetailDTO> findSpDetailDTOByOrderNum(String jointId) {
		List<SpDetailDTO> spde= null;
		String sql = "select new com.game.model.dto.SpDetailDTO(de,sp) from GaBetSponsorDetail de, GaBetSponsor sp where sp.jointId = ? and sp.jointId = de.jointId ";
		Query query = getSession().createQuery(sql);
		query.setString(0, jointId);
		spde = (List<SpDetailDTO>) query.list();
		return spde;
	}

	@Override
	public List<SpDetailDTO> findSpPartDTOByOrderNum(String jointId) {
		List<SpDetailDTO> spde= null;
		String sql = "select new com.game.model.dto.SpDetailDTO(pa,u.userName) from GaBetPart pa,User u where pa.jointId = ? and pa.userId = u.userId ";
		Query query = getSession().createQuery(sql);
		query.setString(0, jointId);
		spde = (List<SpDetailDTO>) query.list();
		return spde;

	}
	public List<BaseDataDTO> findNewsList(String hql, List<Object> para,int num){
		Query query = makeQuerySQL("select new com.apps.model.dto.BaseDataDTO(n,c.title) from  NewsInformation n,NewsCategory c,NewsCategoryRl rl where n.nid=rl.nid and rl.cid=c.cid and n.status='1' "
				+ hql, para);
		if(ParamUtils.chkInteger(num)){
			query.setMaxResults(num);
		}
		List<BaseDataDTO> list = query.list();
		return list;
	}
	public BaseDataDTO getBeforeAndAfterNews(Integer newsId){
		String sql = "select new com.apps.model.dto.BaseDataDTO(a.nid,a.title,c.nid,c.title) from NewsInformation a, NewsInformation b, NewsInformation c where a.tid=b.prevId and c.prevId=b.nid and b.nid=? and n.status='1'  ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, newsId);
		List<BaseDataDTO> list = query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}	
	}
	
	public NewsInformation getNewsInfomationByPrevId(Integer newsId){
		String sql = " from NewsInformation n where n.prevId=? and n.status='1'  ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, newsId);
		List<NewsInformation> list = query.list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public PaginationSupport findLotteryCatList(String string, List<Object> para,
			int startIndex, int pageSize){
		
		String hql = " from GaSessionInfo g where 1=1 ";
		String sql = "select count(g.infoId) from GaSessionInfo g where 1=1 ";
		Query query = makeQuerySQL(hql + string, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + string, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
	
	public PaginationSupport findLotteryCatOptionList(String string, List<Object> para,
			int startIndex, int pageSize){
		
		String hql = " from GaBetOption g where 1=1 ";
		String sql = "select count(g.betOptionId) from GaBetOption g where 1=1 ";
		Query query = makeQuerySQL(hql + string, para);
		query.setFirstResult(startIndex);
		query.setMaxResults(pageSize);
		List queList = query.list();
		// 总记录数
		Query count = makeQuerySQL(sql + string, para);
		Integer totalCount = (Integer) count.uniqueResult();
		return new PaginationSupport(queList, totalCount.intValue());
	}
		public List<LotterySetting> findLotterySettingList(){
		String findList = "from LotterySetting ho where 1=1 ";
		Query query = getSession().createQuery(findList);
		return query.list();
	}
	public List<BaseDataDTO> findLotterySetList(String hql, List<Object> para){
		Query query = makeQuerySQL("select new com.apps.model.dto.BaseDataDTO(ls,rl) from  LotterySetting ls,LotterySettingRl rl where rl.lsId=ls.lsId  "
				+ hql, para);
		List<BaseDataDTO> list = query.list();
		return list;
	}

	@Override
	public LotterySetting getLotterySetting(String type) {
		String sql = "from LotterySetting ls where ls.type=? ";
		Query query = getSession().createQuery(sql);
		query.setString(0, type);
		List list = query.list();
		if(list!=null&&list.size()>0){
			return (LotterySetting) list.get(0);
		}
		return null;
	}

	@Override
	public List<LotterySettingRl> findLotterySetList(Integer id) {
		String sql = "from LotterySettingRl rl where rl.lsId=? order by rl.rechargeMinMoney asc";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, id);
		List list = query.list();
		return list;
	}
	
	public List<Object> findMonthlyStaTrade(String string, List<Object> para){
//		String sql = "select  YEAR(ho.createTime),MONTH(ho.createTime),sum(ho.cashMoney) from UserTradeDetail ho where 1=1 ";
//		String groupBy = "group by YEAR(ho.createTime),MONTH(ho.createTime)";
		String sql = "select ho.createTime,sum(ho.cashMoney) from UserTradeDetail ho where 1=1 and ho.status='1' and ho.type=1 ";
		String groupBy = "group by date_format(ho.createTime,'%Y%m')";
		Query query = makeQuerySQL(sql + string+groupBy, para);
		System.out.println(sql + string+groupBy);
		List<Object> queList = query.list();
		return queList;
	}

	public List<Object> findMonthlyStaBet(String string, List<Object> para){
		String sql = "select ho.betTime,sum(ho.betMoney) from GaBetDetail ho where 1=1 ";
		String groupBy = "group by  date_format(ho.betTime,'%Y%m')";
		Query query = makeQuerySQL(sql + string+groupBy, para);
		System.out.println(sql + string+groupBy);
		List queList = query.list();
		return queList;
	}
	
	public List<UserLevel> findUserLevel(){
		String sql = "from UserLevel ul where 1=1 order by ul.level asc";
		Query query = getSession().createQuery(sql);
		List queList = query.list();
		return queList;
	}
	
	public PaginationSupport findUserTodayBetList(String string, List<Object> para,
			int startIndex, int pageSize){
		
		//GaDayBetCount 表 ，只有官方玩法开奖时才会更新
		// 去userTradeDetail表查询
		
		String hql = " select sum(ho.cashMoney),ho.userId,ho.loginName";
		String sql = " select count(*)  ";
		
		String hqHead = " from UserTradeDetail ho where 1=1 and ho.status='1' and ho.modelType is not null and ho.status = 1 and ho.type=1 ";
		// 收入 奖金
		String param1 = " and ho.tradeType = 1";
		// 支出 投注
		String param2 = " and ho.tradeType = 2";
		
		String groupBy = " group by ho.userId";
		
		String orderBy = " order by sum(ho.cashMoney) desc";
		
		// 收入 奖金
		Query query1 = makeQuerySQL(hql+hqHead + param1+string+groupBy+orderBy, para);
		query1.setFirstResult(startIndex);
		query1.setMaxResults(pageSize);
		List queList1 = query1.list();
		
		// 支出  投注
		Query query2 = makeQuerySQL(hql+hqHead + param2+string+groupBy+orderBy, para);
		query2.setFirstResult(startIndex);
		query2.setMaxResults(pageSize);
		List queList2 = query2.list();
		
		Map<String, BaseDataDTO> dtoMap = new HashMap<String, BaseDataDTO>();
		
		for(Object obj:queList1){
			Object[] tmp = (Object[]) obj;
			if(tmp[1]!=null){
				if(dtoMap.get(tmp[1])==null){
					BaseDataDTO dto = new BaseDataDTO();
					dto.setTimestr(""+tmp[1]);
					dto.setWinMoney(new BigDecimal(""+tmp[0]));
					dto.setBetMoney2(new BigDecimal(0));
					dto.setUserName(""+tmp[2]);
					dtoMap.put(""+tmp[1], dto);
					
				}
			}
		}
		for(Object obj:queList2){
			Object[] tmp = (Object[]) obj;
			if(tmp[1]!=null){
				if(dtoMap.get(""+tmp[1])==null){
					BaseDataDTO dto = new BaseDataDTO();
					dto.setBetMoney2(new BigDecimal(""+tmp[0]).abs());
					if(dto.getWinMoney()==null){
						dto.setWinMoney(new BigDecimal(0));
					}
					dto.setTimestr(""+tmp[1]);
					dto.setUserName(""+tmp[2]);
					dtoMap.put(""+tmp[1], dto);
				}else{
					BaseDataDTO dto = dtoMap.get(""+tmp[1]);
					dto.setBetMoney2(new BigDecimal(""+tmp[0]).abs());
					if(dto.getWinMoney()==null){
						dto.setWinMoney(new BigDecimal(0));
					}
					dtoMap.put(""+tmp[1], dto);
				}
			}
		}
		List<BaseDataDTO> result = new ArrayList<BaseDataDTO>();
		for(BaseDataDTO dto :dtoMap.values()){
			dto.setPayoff(dto.getWinMoney().subtract(dto.getBetMoney2()));
			result.add(dto);
		}

		Collections.sort(result,new Comparator<BaseDataDTO>() {
            public int compare(BaseDataDTO o1, BaseDataDTO o2) {
            	BigDecimal num1 = o1.getWinMoney().subtract(o1.getBetMoney2());
            	BigDecimal num2 = o2.getWinMoney().subtract(o2.getBetMoney2());
            	return num1.compareTo(num2);
            }
        });
		Query count = makeQuerySQL(sql + hqHead+string+groupBy, para);
		List queList3 = count.list();
		
		return new PaginationSupport(result, queList3.size());
	}

	@Override
	public UserTradeDetailRl findUserTradeDetailRlByTradeId(Integer tradeId) {
		String sql = "from UserTradeDetailRl u where u.tradeDetailId = ? ";
		Query query = getSession().createQuery(sql);
		query.setInteger(0, tradeId);
		return (UserTradeDetailRl) query.uniqueResult();
	}
	
	
}
