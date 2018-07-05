package com.apps.dao;

import java.math.BigDecimal;
import java.util.List;

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
import com.apps.model.UserTradeDetailRl;
import com.apps.model.Version;
import com.apps.model.dto.BaseDataDTO;
import com.cash.model.UserCheckoutOrderRl;
import com.framework.dao.IDAO;
import com.framework.dao.hibernate.PaginationSupport;
import com.game.model.UserLevel;
import com.game.model.dto.SpDetailDTO;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;

public interface IBaseDataDAO extends IDAO {
	/**
	 * 获得版本
	 * 
	 * @param appsType
	 *            1.IOS 2.Android
	 * @return
	 */
	public Version getDefVer(String type, String s);

	/**
	 * 获得广告
	 * 
	 * @param adType
	 *            发布广告类型 1首页轮播 2.首页的静态管理 3.类型下的广告
	 * @param status
	 *            状态
	 * @param cid 城市id
	 * @return
	 */
	public List<Advertising> findAdvertisingByType(String adType, String status, Integer cid);

	/**
	 * 获得类型下的有效广告
	 * 
	 * @param tid
	 *            类型id
	 * @return
	 */
	public List<Advertising> findAdvertisingByTid(Integer tid);

	/**
	 * 全部分类，除了更多
	 * 
	 * @return
	 */
	public List<Type> findAllTypeNotMore();

	public List<Type> findAllType();

	public PaginationSupport findVerList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 设置此类型的app全部都不是当前版本
	 * 
	 * @param type
	 *            1 ios 2.android
	 */
	public void updateVerNot(String type);

	/**
	 * 获得留言列表
	 */
	public PaginationSupport findFeedbackList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 获得推送列表
	 * @param hqls
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findPushList(String hqls, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 根据显示的位置获取数据
	 * @param position
	 * @return
	 */
	public List<Type> findTypeByPosition(String position);
	/**
	 * 获取市区数据
	 * 
	 * @param type
	 *            1.市 2.区
	 * @return
	 */
	public List<City> findCity(String type);
	public List<CityBusArea> findCityBusArea();
	public List<CityBusArea> findCityBusArea(Integer cid);
//	public List<EstateCompany> findEstateCompany();

	/**
	 * 获取城市下的辖区
	 * 
	 * @param cid
	 * 城市id
	 * @return
	 */
	public List<City> findAreaByCityId(int cid);


	/**
	 * 获得城市的首字母分组
	 * 
	 * @param type
	 * @return
	 */
	public List<String> findCityKeys(String type);

	public PaginationSupport findCity(String hql, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 获得商圈列表
	 */
	public PaginationSupport findCityBusArea(String hql, List<Object> para,
			int startIndex, int pageSize);
	public PaginationSupport findCityCommunity(String hql, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 删除城市下的辖区
	 * 
	 * @param cid
	 */
	public void deleteAreaByCity(Integer cid);

	public City getCityByCode(String cityName);

	/**
	 * 根据key获得城市列表
	 * @param type
	 * @param key
	 * @return
	 */
	public List<City> findCity(String type, String key);
	/**
	 *  根据上一级城市获取下一级商圈
	 * @param type
	 * @param key
	 * @return
	 */
	public List<CityBusArea> findCityAreaByCityId(Integer cid);
	/**
	 *  根据商圈获取小区
	 * @param type
	 * @param key
	 * @return
	 */
	public List<CityCommunity> findCityCommunityByAId(Integer aid);
	/**
	 * 对比城市编码是否存在
	 * @param code
	 * @return
	 */
	public boolean findCityByCode(String code) ;

	/**
	 * 获得一级类型
	 * @return
	 */
	public List<Type> findTypeIndex();
	/**
	 * 返回所有有效城市
	 * @return List<City>
	 */
	public List<City> findCityOfValid();
	public List<City> findCityOfValidByCityId(Integer cityId);
	public void saveCityBusArea(CityBusArea cityBusArea);
	public void updateCityBusArea(CityBusArea cityBusArea);
	public void saveCityCommunity(CityCommunity cityCommunity);
	public void updateCityCommunity(CityCommunity cityCommunity);
	/**
	 * 通过类型判断该类型设置的积分是多少
	 * @param type 
	 * @return
	 */
	public  BigDecimal getPointsByType(String type);
	/**
	 * 积分列表
	 */
	public PaginationSupport findIntegralList(String string, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 编辑积分
	 */
	public void updateIntegral(SysOption sysOption);
	public void updateIntegralPoints(BigDecimal points,Integer optionId);
	/**
	 * 保存积分
	 */
	public void saveIntegral(SysOption sysOption);
	/**
	 * 根据小区id查找商圈id
	 * @param ccid
	 * @return
	 */
	public Integer getCityBusAreaByCcid(Integer ccid);
	/**
	 * 积分明细
	 */
	public PaginationSupport findUserPointDetail(String string, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 资金明细
	 */
	public PaginationSupport findUserTradeDetail(String string, List<Object> para,
			int startIndex, int pageSize);
	public PaginationSupport findGaDayBetCountList(String string, List<Object> para,
			int startIndex, int pageSize);
	public PaginationSupport findUserTodayBetList(String string, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 结算明细
	 */
	public PaginationSupport findUserCheckout(String string, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 订单列表
	 */
	public List<UserCheckoutOrderRl> findOrderList(Integer checkoutId);
	public BaseDataDTO findLatestOpenSession();

	public List<BaseDataDTO> findGfTotalBet(String hql, List<Object> para);
	public List<BaseDataDTO> findTotalBet(String hql, List<Object> para);

	public PayConfig getPayByType(String payType);

	public List<PayConfig> findPayConfig();

	/**
	 * 查找活动表
	 * @return
	 */
	public List<Activity> findActivity();
	/**
	 * 查询新闻咨询所属分类的类型列表
	 */
	public List<NewsType>findNewsTypeList();
	/**
	 * 查询新闻列表
	 */
	public PaginationSupport findNewsList(String string, List<Object> para,
			int startIndex, int pageSize);

	/**
	 * 查找购买详解
	 * @param orderNum
	 * @return
	 */
	public List<SpDetailDTO> findSpDetailDTOByOrderNum(String orderNum);

	/**
	 * 根据合买表id查询投注用户
	 * @param jointId
	 * @return
	 */
	public List<SpDetailDTO> findSpPartDTOByOrderNum(String jointId);
	/**
	 * 查询新闻资讯
	 * @param string
	 * @param para
	 * @param num  限制查询的条数  如果num<=0则不限制查询，即查询所有数据
	 */
	public List<BaseDataDTO> findNewsList(String string, List<Object> para,int num);
	/**
	 * 查询前后资讯信息
	 * @param newsId
	 * @return
	 */
	public BaseDataDTO getBeforeAndAfterNews(Integer newsId);
	/**
	 * 查询前一条新闻资讯
	 * @param newsId
	 * @return
	 */
	public NewsInformation getNewsInfomationByPrevId(Integer newsId);

	/**
	 * 查询彩种
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findLotteryCatList(String string, List<Object> para,
			int startIndex, int pageSize);
	/**
	 * 查询彩种玩法s
	 * @param string
	 * @param para
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public PaginationSupport findLotteryCatOptionList(String string, List<Object> para,
			int startIndex, int pageSize);
	
	/**
	 * 查询红包和抽奖设置
	 * @return
	 */
	public List<LotterySetting> findLotterySettingList();

	/**
	 * 查询转盘设置信息
	 * @return
	 */
	public List<BaseDataDTO> findLotterySetList(String hql, List<Object> para);

	public LotterySetting getLotterySetting(String type);

	public List<LotterySettingRl> findLotterySetList(Integer id);
	
	public List<Object> findMonthlyStaTrade(String string, List<Object> para);
	public List<Object> findMonthlyStaBet(String string, List<Object> para);
	
	/**
	 * 查找用户的等级
	 * @return
	 */
	public List<UserLevel> findUserLevel();
	
	public UserTradeDetailRl findUserTradeDetailRlByTradeId(Integer tradeId);
}
