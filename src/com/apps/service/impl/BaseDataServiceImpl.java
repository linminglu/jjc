package com.apps.service.impl;

import help.push.PushBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.apps.model.Push;
import com.apps.model.SysOption;
import com.apps.model.Type;
import com.apps.model.UserTradeDetailRl;
import com.apps.model.Version;
import com.apps.model.dto.BaseDataDTO;
import com.apps.service.IBaseDataService;
import com.cash.model.UserCheckoutOrderRl;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.BaseService;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;
import com.game.model.UserLevel;
import com.game.model.dto.SpDetailDTO;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;
import com.ram.model.User;

public class BaseDataServiceImpl extends BaseService implements
		IBaseDataService {
	private IBaseDataDAO baseDataDAO;

	public void setBaseDataDAO(IBaseDataDAO baseDataDAO) {
		this.baseDataDAO = baseDataDAO;
		super.dao = baseDataDAO;
	}

	/**
	 * 获得版本
	 * @param s 1.用户版2.商家版
	 * 
	 * @param appsType
	 *            1.IOS 2.Android
	 * @return
	 */
	public Version getDefVer(String type, String s) {
		return baseDataDAO.getDefVer(type,s);
	}

	public List<Advertising> findAdvertisingByType(String adType, String status, Integer cid) {
		return baseDataDAO.findAdvertisingByType(adType, status,cid);
	}
	/**
	 * 获得类型下的有效广告
	 * 
	 * @param tid
	 *            类型id
	 * @return
	 */
	public List<Advertising> findAdvertisingByTid(Integer tid) {
		return baseDataDAO.findAdvertisingByTid(tid);
	}

	public List<Type> findAllTypeNotMore() {
		return baseDataDAO.findAllTypeNotMore();
	}

	public List<Type> findAllType() {
		return baseDataDAO.findAllType();
	}

	public PaginationSupport findVerList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return baseDataDAO.findVerList(hqls,para,startIndex,pageSize);
	}

	public Version saveVersion(Version version) {
		Integer id = version.getId();
		String isDef = version.getIsDef();
		if(ParamUtils.chkInteger(id)){
			Version ver=(Version) baseDataDAO.getObject(Version.class, id);
			ver.setIsDef(isDef);
			ver.setLink(version.getLink());
			ver.setType(version.getType());
			ver.setVer(version.getVer());
			baseDataDAO.saveObject(ver);
		}else{
			Version ver=new Version();
			ver.setIsDef(isDef);
			ver.setLink(version.getLink());
			ver.setType(version.getType());
			ver.setVer(version.getVer());
			ver.setCreateDate(new Date());
			version=(Version) baseDataDAO.saveObjectDB(ver);
		}
		return version;
	}

	public void updateVerNot(String type) {
		baseDataDAO.updateVerNot(type);
	}

	public PaginationSupport findFeedbackList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return baseDataDAO.findFeedbackList(hqls,para,startIndex,pageSize);
	}

	public PaginationSupport findPushList(String hqls, List<Object> para,
			int startIndex, int pageSize) {
		return baseDataDAO.findPushList(hqls,para,startIndex,pageSize);
	}

	public void savePush(Push push) {
		Push temp=new Push();
		String content = push.getContent();
		Integer infoId = push.getInfoId();
		String deviceType = push.getDeviceType();
		String title = push.getTitle();
		String url = push.getUrl();
		String type = push.getType();//1.普通消息
		
		temp.setContent(content);
		temp.setCreateDate(new Date());
		temp.setDeviceType(deviceType);
		temp.setInfoId(infoId);
		temp.setTitle(title);
		temp.setType(push.getType());
		
		PushBean pushBean=new PushBean(type,title,content,url);
		try {
//			BaiduPushUtil.all(pushBean);
		} catch (Exception e) {
		}
		baseDataDAO.saveObject(temp);
	}

	public List<Type> findTypeByPosition(String position) {
		return baseDataDAO.findTypeByPosition(position);
	}
	public List<City> findCity(String type) {
		return baseDataDAO.findCity(type);
	}
	public List<CityBusArea> findCityBusArea(){
		return baseDataDAO.findCityBusArea();
	}
	
	public List<CityBusArea> findCityBusArea(Integer cid){
		return baseDataDAO.findCityBusArea(cid);
	}
	
//	public List<EstateCompany> findEstateCompany(){
//		return baseDataDAO.findEstateCompany();
//	}

	public List<City> findAreaByCityId(int cid) {
		return baseDataDAO.findAreaByCityId(cid);
	}

	public void saveError(String content) {
//		boolean isExist = baseDataDAO.getMessageByContent(content);
//		if (!isExist) {
//			MytMessage msg = new MytMessage();
//			msg.setCreateTime(new Date());
//			msg.setContent(content);
//			msg.setUserId(-1);
//			baseDataDAO.saveObject(msg);
//		}
	}

	public List<String> findCityKeys(String type) {
		return baseDataDAO.findCityKeys(type);
	}

	public void saveObject(Object obj, User user) {
		baseDataDAO.saveObject(obj);
	}

	public PaginationSupport findCity(String hql, List<Object> para,
			int startIndex, int pageSize) {
		return baseDataDAO.findCity(hql, para, startIndex, pageSize);
	}
	
	public PaginationSupport findCityBusArea(String hql, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = baseDataDAO.findCityBusArea(hql, para, startIndex, pageSize);
		List<CityBusArea> list = ps.getItems();
		List list2 = new ArrayList();
		if(list != null && list.size() > 0 ){
			for (CityBusArea cityBusArea : list) {
				Integer cid = cityBusArea.getCid();
				City city = (City)baseDataDAO.getObject(City.class, cid);
				cityBusArea.setCtitle(city.getTitle());
				list2.add(cityBusArea);
			}
			list = list2;
		}
		return ps;
	}
	
	public PaginationSupport findCityCommunity(String hql, List<Object> para,
			int startIndex, int pageSize){
		PaginationSupport ps = baseDataDAO.findCityCommunity(hql, para, startIndex, pageSize);
		List<CityCommunity> list = ps.getItems();
		List list2 = new ArrayList();
		if(list != null && list.size() > 0 ){
			for (CityCommunity cityCommunity : list) {
				Integer cid = cityCommunity.getCid();
				Integer busaid = cityCommunity.getBusaid();
				City city = (City)baseDataDAO.getObject(City.class, cid);
				CityBusArea cityBusArea = (CityBusArea)baseDataDAO.getObject(CityBusArea.class, busaid);
				cityCommunity.setCtitle(city.getTitle());
				cityCommunity.setBtitle(cityBusArea.getTitle());
				list2.add(city.getTitle());
			}
			list = list2;
		}
		return ps;
	}

	public City saveCity(City city) {
//		String title = city.getTitle();
//		Integer cid = city.getCid();
//		String type = city.getType();
//		String pinyin = PinyinUtil.getFirstCharToString(title);
//		city.setPinyin(pinyin);
//		if (type.equals(Constants.CITY_SHI)) {
//			city.setParentId(0);
//			// city.setCode(GoogleGeocoder.getGLGeocoderCityCode(title));
//		}
//		city.setSort("0");
//		if (!ParamUtils.chkInteger(cid)) {
//			city = (City) baseDataDAO.saveObjectDB(city);
//		} else {
//			baseDataDAO.saveObject(city);
//		}
		return city;
	}
	
	public void saveCityBusArea(CityBusArea cityBusArea){
		baseDataDAO.saveCityBusArea(cityBusArea);
	}
	
	public void updateCityBusArea(CityBusArea cityBusArea){
		baseDataDAO.updateCityBusArea(cityBusArea);
	}
	public void saveCityCommunity(CityCommunity cityCommunity){
		baseDataDAO.saveCityCommunity(cityCommunity);
	}
	public void updateCityCommunity(CityCommunity cityCommunity){
		baseDataDAO.updateCityCommunity(cityCommunity);
	}

	public void delCity(Integer cid) {
		City city = (City) baseDataDAO.getObject(City.class, cid);
		if (city != null) {
			String type = city.getType();
			if (type.equals(Constants.CITY_SHI)) {// 辖区
				baseDataDAO.deleteAreaByCity(cid);
			}
			baseDataDAO.deleteObject(City.class, cid, null);
		}
	}

	public City getCityByCode(String cityName) {
		return baseDataDAO.getCityByCode(cityName);
	}

	public List<City> findCity(String type, String key) {
		return baseDataDAO.findCity(type,key);
	}
	public List<CityBusArea> findCityAreaByCityId(Integer cid){
		return baseDataDAO.findCityAreaByCityId(cid);
	}
	public List<CityCommunity> findCityCommunityByAId(Integer aid){
		return baseDataDAO.findCityCommunityByAId(aid);
	}
	public boolean findCityByCode(String code){
		return baseDataDAO.findCityByCode(code);
	}
	public List<City> findCityOfValid(){
		return baseDataDAO.findCityOfValid();
	}
	@Override
	public List<Type> findTypeIndex() {
		return baseDataDAO.findTypeIndex();
	}
	public List<City> findCityOfValidByCityId(Integer cityId){
		return baseDataDAO.findCityOfValidByCityId(cityId);
	}
	public  BigDecimal getPointsByType(String type){
		return baseDataDAO.getPointsByType(type);
	}
	public PaginationSupport findIntegralList(String string, List<Object> para,
			int startIndex, int pageSize){
		return  baseDataDAO.findIntegralList(string, para, startIndex, pageSize);
	}
	public void updateIntegral(SysOption sysOption){
		baseDataDAO.updateIntegral(sysOption);
	}
	public void updateIntegralPoints(BigDecimal points,Integer optionId){
		baseDataDAO.updateIntegralPoints(points,optionId);
	}
	public void saveIntegral(SysOption sysOption){
		baseDataDAO.saveIntegral(sysOption);
	}
	public Integer getCityBusAreaByCcid(Integer ccid){
		return baseDataDAO.getCityBusAreaByCcid(ccid);
	}
	public PaginationSupport findUserPointDetail(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findUserPointDetail(string, para, startIndex, pageSize);
	}
	public PaginationSupport findUserTradeDetail(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findUserTradeDetail(string, para, startIndex, pageSize);
	}
	public PaginationSupport findGaDayBetCountList(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findGaDayBetCountList(string, para, startIndex, pageSize);
	}
	public PaginationSupport findUserCheckout(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findUserCheckout(string, para, startIndex, pageSize);
	}
	public List<UserCheckoutOrderRl> findOrderList(Integer checkoutId){
		return baseDataDAO.findOrderList(checkoutId);
	}
	public SysOption getSysOptionByType(String type){
		HQUtils hq=new HQUtils(" from SysOption s where 1=1 ");
		hq.addHsql(" and  s.optionType=? ");
		hq.addPars(type);
		Object object=baseDataDAO.getObject(hq);
		if(object!=null){
			return (SysOption) object;
		}else{
			return null;
		}
		
	}
	public BaseDataDTO findLatestOpenSession(){
		return baseDataDAO.findLatestOpenSession();
	}

	@Override
	public List<BaseDataDTO> findTotalBet(String hql, List<Object> para) {
		return baseDataDAO.findTotalBet(hql,para);
	}

	@Override
	public PayConfig getPayByType(String payType) {
		return baseDataDAO.getPayByType(payType);
	}

	@Override
	public List<PayConfig> findPayConfig() {
		return baseDataDAO.findPayConfig();
	}

	@Override
	public List<Activity> findActivity() {
		return baseDataDAO.findActivity();
	}
	public List<NewsType>findNewsTypeList(){
		return baseDataDAO.findNewsTypeList();
	}
	public PaginationSupport findNewsList(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findNewsList(string, para, startIndex, pageSize);
	}

	@Override
	public List<SpDetailDTO> findSpDetailDTOByOrderNum(String jointId) {
		return baseDataDAO.findSpDetailDTOByOrderNum(jointId);
	}

	@Override
	public List<SpDetailDTO> findSpPartDTOByOrderNum(String jointId) {
		return baseDataDAO.findSpPartDTOByOrderNum(jointId);
	}
	public List<BaseDataDTO> findNewsList(String string, List<Object> para,int num){
		return baseDataDAO.findNewsList(string,para,num);
	}
	public BaseDataDTO getBeforeAndAfterNews(Integer newsId){
		return baseDataDAO.getBeforeAndAfterNews(newsId);
	}
	public NewsInformation getNewsInfomationByPrevId(Integer newsId){
		return baseDataDAO.getNewsInfomationByPrevId(newsId);
	}

	public PaginationSupport findLotteryCatList(String string, List<Object> para,
			int startIndex, int pageSize){
		return  baseDataDAO.findLotteryCatList(string, para, startIndex, pageSize);
	}

	public String saveLotteryCat(GaSessionInfo gasInfo, User user) {
		String msg = "";
		if(ParamUtils.chkInteger(gasInfo.getInfoId())&&gasInfo.getInfoId().intValue()>0){
			GaSessionInfo savedBean = (GaSessionInfo) baseDataDAO.getObject(GaSessionInfo.class, gasInfo.getInfoId());
			savedBean.setGameTitle(gasInfo.getGameTitle());
			savedBean.setGameType(gasInfo.getGameType());
			savedBean.setGameCode(gasInfo.getGameCode());
			savedBean.setGameSet(gasInfo.getGameSet());
			savedBean.setImg(gasInfo.getImg());
			savedBean.setStatus(gasInfo.getStatus());
			savedBean.setDes(gasInfo.getDes());
			savedBean.setExp(gasInfo.getExp());
			baseDataDAO.saveObject(savedBean);
		}else{
			gasInfo.setInfoId(null);
			baseDataDAO.saveObject(gasInfo);
		}
		return  msg;
	}

	public String delLotteryCat(Integer infoId, User user) {
		String msg = "";
		GaSessionInfo gasInfo = (GaSessionInfo) baseDataDAO.getObject(GaSessionInfo.class, infoId);
		if(gasInfo!=null){
			baseDataDAO.deleteObject(GaSessionInfo.class, gasInfo.getInfoId(), user);
		}else{
			msg = "参数错误";
		}
		return msg;
	}

	@Override
	public PaginationSupport findLotteryCatOptionList(String string, List<Object> para,Integer infoId, int startIndex, int pageSize) {
		
		return  baseDataDAO.findLotteryCatOptionList(string, para, startIndex, pageSize);
	}
	
	public String saveLotteryCatOption(GaBetOption gabOpt, User user) {
		String msg = "";
		if(ParamUtils.chkInteger(gabOpt.getBetOptionId())&&gabOpt.getBetOptionId().intValue()>0){
			GaBetOption savedBean = (GaBetOption) baseDataDAO.getObject(GaBetOption.class, gabOpt.getBetOptionId());
			savedBean.setOptionTitle(gabOpt.getOptionTitle());
			savedBean.setOptionType(gabOpt.getOptionType());
			savedBean.setBetRate(gabOpt.getBetRate());
			savedBean.setBetRate2(""+gabOpt.getBetRate());
			savedBean.setPlayType(gabOpt.getPlayType());
			savedBean.setPointMultiple(gabOpt.getPointMultiple());
			baseDataDAO.saveObject(savedBean);
		}else{
			gabOpt.setBetOptionId(null);
			gabOpt.setBetRate2(""+gabOpt.getBetRate());
			baseDataDAO.saveObject(gabOpt);
		}
		return  msg;
	}
	
	public String delLotteryCatOption(Integer infoId, User user) {
		String msg = "";
		GaBetOption gaBetOption = (GaBetOption) baseDataDAO.getObject(GaBetOption.class, infoId);
		if(gaBetOption!=null){
			baseDataDAO.deleteObject(GaBetOption.class, gaBetOption.getBetOptionId(), user);
		}else{
			msg = "参数错误";
		}
		return msg;
	}
		public List<LotterySetting> findLotterySettingList(){
		return baseDataDAO.findLotterySettingList();
	}
	public List<BaseDataDTO> findLotterySetList(String hql, List<Object> para){
		return baseDataDAO.findLotterySetList(hql, para);
	}

	@Override
	public LotterySetting getLotterySetting(String type) {
		return baseDataDAO.getLotterySetting(type);
	}

	@Override
	public List<LotterySettingRl> findLotterySetList(Integer id) {
		return baseDataDAO.findLotterySetList(id);
	}
	public List<Object> findMonthlyStaTrade(String string, List<Object> para){
		return baseDataDAO.findMonthlyStaTrade(string, para);
	}
	public List<Object> findMonthlyStaBet(String string, List<Object> para){
		return baseDataDAO.findMonthlyStaBet(string, para);
	}

	@Override
	public List<UserLevel> findUserLevel() {
		return baseDataDAO.findUserLevel();
	}
	
	public PaginationSupport findUserTodayBetList(String string, List<Object> para,
			int startIndex, int pageSize){
		return baseDataDAO.findUserTodayBetList(string, para, startIndex, pageSize);
	}

	@Override
	public UserTradeDetailRl findUserTradeDetailRlByTradeId(Integer tradeId) {
		return baseDataDAO.findUserTradeDetailRlByTradeId(tradeId);
	}
	
	
	
}
