package com.apps.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.City;
import com.apps.model.CityBusArea;
import com.apps.model.CityCommunity;
import com.apps.model.LotterySetting;
import com.apps.model.PayConfig;
import com.apps.model.Push;
import com.apps.model.SysOption;
import com.apps.model.Version;
import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;

public class BaseDataForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Version version = new Version();
	private Push push = new Push();
	private String startIndex;
	private String startDate;
	private String endDate;
	private String title;
	private String name;
	private String type;
	private String deviceType;
	private String keywords = "";
	private City city = new City();
	private String code = "";
	
	private CityBusArea cityBusArea = new CityBusArea();
	private CityCommunity cityCommunity = new CityCommunity();
	private Integer companyId;
	private Integer busaid;
	private Integer cid;
	private String optionTitle;
	private SysOption sysOption=new SysOption();
	private String tradeType;
	private String orderNum;// 订单号
	private Integer totalCount;
	private String sid;
	private String userName;
	private String tid;
	private String uid;
	private Integer oid;
	private String expressName;//物流公司
	private String expressNo;//物流单号
	private String isExpress;//是否走第三方物流
	private String status;// 状态。默认为1
	
	private String moduleType;
	private String cashType;
	private String unitType;
	private PayConfig renxin = new PayConfig();
	private PayConfig shanfu = new PayConfig();
	private PayConfig amx = new PayConfig();
	private String check;
	private String userType;
	
	private FormFile file;
	// 彩种
	private GaSessionInfo gasInfo = new GaSessionInfo();
	// 彩种玩法
	private GaBetOption gabOpt = new GaBetOption();
	private LotterySetting  lotterySetting=new LotterySetting();
	private String type1;
	private String type2;
	private String type3;
	private String rechargeMinMoney1;
	private String rechargeMinMoney2;
	private String rechargeMinMoney3;
	private String rechargeMaxMoney1;
	private String rechargeMaxMoney2;
	private String rechargeMaxMoney3;
	private String fixedMoney1;
	private String fixedMoney2;
	private String fixedMoney3;
	private String minMoney1;
	private String minMoney2;
	private String minMoney3;
	private String maxMoney1;
	private String maxMoney2;
	private String maxMoney3;
	private String title1;
	private String title2;
	private String title3;
	
	private String userId;
	
	private String playCate;//彩种类型  1=官方   2=信用
	private String betAvoid; //是否能投注
	
	private String gasInfoId;
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Push getPush() {
		return push;
	}

	public void setPush(Push push) {
		this.push = push;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGasInfoId() {
		return gasInfoId;
	}

	public void setGasInfoId(String gasInfoId) {
		this.gasInfoId = gasInfoId;
	}

	public CityBusArea getCityBusArea() {
		return cityBusArea;
	}

	public void setCityBusArea(CityBusArea cityBusArea) {
		this.cityBusArea = cityBusArea;
	}

	public CityCommunity getCityCommunity() {
		return cityCommunity;
	}

	public void setCityCommunity(CityCommunity cityCommunity) {
		this.cityCommunity = cityCommunity;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getPlayCate() {
		return playCate;
	}

	public void setPlayCate(String playCate) {
		this.playCate = playCate;
	}

	public String getBetAvoid() {
		return betAvoid;
	}

	public void setBetAvoid(String betAvoid) {
		this.betAvoid = betAvoid;
	}

	public SysOption getSysOption() {
		return sysOption;
	}

	public void setSysOption(SysOption sysOption) {
		this.sysOption = sysOption;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getIsExpress() {
		return isExpress;
	}

	public void setIsExpress(String isExpress) {
		this.isExpress = isExpress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	/**
	 * @return the renxin
	 */
	public PayConfig getRenxin() {
		return renxin;
	}

	/**
	 * @param renxin the renxin to set
	 */
	public void setRenxin(PayConfig renxin) {
		this.renxin = renxin;
	}

	/**
	 * @return the shanfu
	 */
	public PayConfig getShanfu() {
		return shanfu;
	}

	/**
	 * @param shanfu the shanfu to set
	 */
	public void setShanfu(PayConfig shanfu) {
		this.shanfu = shanfu;
	}

	/**
	 * @return the amx
	 */
	public PayConfig getAmx() {
		return amx;
	}

	/**
	 * @param amx the amx to set
	 */
	public void setAmx(PayConfig amx) {
		this.amx = amx;
	}

	/**
	 * @return the check
	 */
	public String getCheck() {
		return check;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		this.check = check;
	}

	public LotterySetting getLotterySetting() {
		return lotterySetting;
	}

	public void setLotterySetting(LotterySetting lotterySetting) {
		this.lotterySetting = lotterySetting;
	}
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public GaSessionInfo getGasInfo() {
		return gasInfo;
	}

	public void setGasInfo(GaSessionInfo gasInfo) {
		this.gasInfo = gasInfo;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public GaBetOption getGabOpt() {
		return gabOpt;
	}

	public void setGabOpt(GaBetOption gabOpt) {
		this.gabOpt = gabOpt;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public String getRechargeMinMoney1() {
		return rechargeMinMoney1;
	}

	public void setRechargeMinMoney1(String rechargeMinMoney1) {
		this.rechargeMinMoney1 = rechargeMinMoney1;
	}

	public String getRechargeMinMoney2() {
		return rechargeMinMoney2;
	}

	public void setRechargeMinMoney2(String rechargeMinMoney2) {
		this.rechargeMinMoney2 = rechargeMinMoney2;
	}

	public String getRechargeMinMoney3() {
		return rechargeMinMoney3;
	}

	public void setRechargeMinMoney3(String rechargeMinMoney3) {
		this.rechargeMinMoney3 = rechargeMinMoney3;
	}

	public String getRechargeMaxMoney1() {
		return rechargeMaxMoney1;
	}

	public void setRechargeMaxMoney1(String rechargeMaxMoney1) {
		this.rechargeMaxMoney1 = rechargeMaxMoney1;
	}

	public String getRechargeMaxMoney2() {
		return rechargeMaxMoney2;
	}

	public void setRechargeMaxMoney2(String rechargeMaxMoney2) {
		this.rechargeMaxMoney2 = rechargeMaxMoney2;
	}

	public String getRechargeMaxMoney3() {
		return rechargeMaxMoney3;
	}

	public void setRechargeMaxMoney3(String rechargeMaxMoney3) {
		this.rechargeMaxMoney3 = rechargeMaxMoney3;
	}

	public String getFixedMoney1() {
		return fixedMoney1;
	}

	public void setFixedMoney1(String fixedMoney1) {
		this.fixedMoney1 = fixedMoney1;
	}

	public String getFixedMoney2() {
		return fixedMoney2;
	}

	public void setFixedMoney2(String fixedMoney2) {
		this.fixedMoney2 = fixedMoney2;
	}

	public String getFixedMoney3() {
		return fixedMoney3;
	}

	public void setFixedMoney3(String fixedMoney3) {
		this.fixedMoney3 = fixedMoney3;
	}

	public String getMinMoney1() {
		return minMoney1;
	}

	public void setMinMoney1(String minMoney1) {
		this.minMoney1 = minMoney1;
	}

	public String getMinMoney2() {
		return minMoney2;
	}

	public void setMinMoney2(String minMoney2) {
		this.minMoney2 = minMoney2;
	}

	public String getMinMoney3() {
		return minMoney3;
	}

	public void setMinMoney3(String minMoney3) {
		this.minMoney3 = minMoney3;
	}

	public String getMaxMoney1() {
		return maxMoney1;
	}

	public void setMaxMoney1(String maxMoney1) {
		this.maxMoney1 = maxMoney1;
	}

	public String getMaxMoney2() {
		return maxMoney2;
	}

	public void setMaxMoney2(String maxMoney2) {
		this.maxMoney2 = maxMoney2;
	}

	public String getMaxMoney3() {
		return maxMoney3;
	}

	public void setMaxMoney3(String maxMoney3) {
		this.maxMoney3 = maxMoney3;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
