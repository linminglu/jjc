package com.apps.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.apps.model.Feedback;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.Push;
import com.apps.model.Seller;
import com.apps.model.UserPointDetail;
import com.apps.model.UserTradeDetail;
import com.cash.model.UserCheckout;
import com.game.model.GaK3Session;
import com.ram.model.NewsInformation;
import com.ram.model.User;

public class BaseDataDTO {

	private Feedback feedback = new Feedback();
	private Push push = new Push();
	private String userName;
	private UserPointDetail userPointDetail = new UserPointDetail();
	private User user = new User();
	private UserTradeDetail userTradeDetail = new UserTradeDetail();
	private UserCheckout userCheckout = new UserCheckout();
	private Seller seller = new Seller();
	private String moduleType;

//	private AhGaK3Session ahGaK3Session;
//	private GxGaK3Session gxGaK3Session;
//	private JlGaK3Session jlGaK3Session;
//	private JsK3GaSession jsGaK3Session;
	private GaK3Session gaK3Session;
//	private GaK10Session gaK10Session;
	
	
	private  NewsInformation news=new NewsInformation();
	private String title;
	
	private Integer beforeId;
	private String beforeTitle;
	private Integer afterId;
	private String afterTitle;
	private BigDecimal payoff; //盈亏

	private BigDecimal winMoney; //累计中奖

	private Integer betMoney; //累计下注
	private BigDecimal exchangeMoney;//累计积分兑换
	private BigDecimal drawMoney;//累计提现
	private BigDecimal  rechagePresent;//累计充值赠送
	private BigDecimal money;//账户总余额。
	private BigDecimal totalRechage;//累计充值
	private BigDecimal divideMoney;//出入差值
	private BigDecimal betMoney2;//累计下注
	private String gameType;//彩票类型
	private String timestr;//时间
	
	private LotterySetting ls=new LotterySetting();
	private LotterySettingRl lsrl=new LotterySettingRl();
	
	public BaseDataDTO(){
		
	}
	
	public BaseDataDTO(BigDecimal totalRechage,BigDecimal drawMoney,BigDecimal divdeMoney,BigDecimal exchangeMoney
			,BigDecimal  rechagePresent,BigDecimal winMoney,BigDecimal betMoney2,BigDecimal payoff,BigDecimal money){
		this.totalRechage=totalRechage;
		this.drawMoney =drawMoney;
		this.divideMoney = divdeMoney;
		this.exchangeMoney=exchangeMoney;
		this.rechagePresent = rechagePresent;
		this.winMoney=winMoney;
		this.betMoney2=betMoney2;
		this.payoff=payoff;
		this.money=money;
	}
	public BaseDataDTO(Feedback feedback, String userName) {
		this.feedback = feedback;
		this.userName = userName;
	}
	
	public BaseDataDTO(NewsInformation news, String title) {
		this.news = news;
		this.title = title;
	}
	
	public BaseDataDTO(LotterySetting ls, LotterySettingRl lsrl) {
		this.ls = ls;
		this.lsrl = lsrl;
	}
	
	public BaseDataDTO(Integer betMoney, BigDecimal winMoney,BigDecimal payoff) {
		this.betMoney = betMoney;
		this.winMoney = winMoney;
		this.payoff = payoff;
	}
	// basedataAction totalBet 使用
	public BaseDataDTO(BigDecimal betMoney2, BigDecimal winMoney,BigDecimal payoff) {
		this.betMoney2 = betMoney2;
		this.winMoney = winMoney;
		this.payoff = payoff;
	}
	public BaseDataDTO(String gameType,BigDecimal betMoney2, BigDecimal winMoney,BigDecimal payoff) {
		this.gameType = gameType;
		this.betMoney2 = betMoney2;
		this.winMoney = winMoney;
		this.payoff = payoff;
	}
	

	
	public BaseDataDTO(UserPointDetail userPointDetail, User user) {
		this.userPointDetail = userPointDetail;
		this.user = user;
	}
	
	public BaseDataDTO(UserPointDetail userPointDetail, String userName) {
		this.userPointDetail = userPointDetail;
		this.userName = userName;
	}
	
	public BaseDataDTO(UserTradeDetail userTradeDetail, User user) {
		this.userTradeDetail = userTradeDetail;
		this.user = user;
	}
	
	public BaseDataDTO(UserTradeDetail userTradeDetail, String userName) {
		this.userTradeDetail = userTradeDetail;
		this.userName = userName;
	}
	
	public BaseDataDTO(UserCheckout userCheckout,User user) {
		this.userCheckout = userCheckout;
		this.user = user;
	}
	
	public BaseDataDTO(UserCheckout userCheckout,String userName) {
		this.userCheckout = userCheckout;
		this.userName = userName;
	}
	

	public Push getPush() {
		return push;
	}

	public void setPush(Push push) {
		this.push = push;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserPointDetail getUserPointDetail() {
		return userPointDetail;
	}

	public void setUserPointDetail(UserPointDetail userPointDetail) {
		this.userPointDetail = userPointDetail;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserTradeDetail getUserTradeDetail() {
		return userTradeDetail;
	}

	public void setUserTradeDetail(UserTradeDetail userTradeDetail) {
		this.userTradeDetail = userTradeDetail;
	}

	public UserCheckout getUserCheckout() {
		return userCheckout;
	}

	public void setUserCheckout(UserCheckout userCheckout) {
		this.userCheckout = userCheckout;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}



	public GaK3Session getGaK3Session() {
		return gaK3Session;
	}

	public void setGaK3Session(GaK3Session gaK3Session) {
		this.gaK3Session = gaK3Session;
	}
//
//	public GaK10Session getGaK10Session() {
//		return gaK10Session;
//	}
//
//	public void setGaK10Session(GaK10Session gaK10Session) {
//		this.gaK10Session = gaK10Session;
//	}


	public Integer getBetMoney() {
		return betMoney;
	}


	public void setBetMoney(Integer betMoney) {
		this.betMoney = betMoney;
	}


	public BigDecimal getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}

	public BigDecimal getPayoff() {
		return payoff;
	}

	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}


	public NewsInformation getNews() {
		return news;
	}


	public void setNews(NewsInformation news) {
		this.news = news;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBeforeId() {
		return beforeId;
	}

	public void setBeforeId(Integer beforeId) {
		this.beforeId = beforeId;
	}

	public String getBeforeTitle() {
		return beforeTitle;
	}

	public void setBeforeTitle(String beforeTitle) {
		this.beforeTitle = beforeTitle;
	}

	public Integer getAfterId() {
		return afterId;
	}

	public void setAfterId(Integer afterId) {
		this.afterId = afterId;
	}

	public String getAfterTitle() {
		return afterTitle;
	}

	public void setAfterTitle(String afterTitle) {
		this.afterTitle = afterTitle;
	}
	public BigDecimal getExchangeMoney() {
		return exchangeMoney;
	}
	public void setExchangeMoney(BigDecimal exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}
	public BigDecimal getDrawMoney() {
		return drawMoney;
	}
	public void setDrawMoney(BigDecimal drawMoney) {
		this.drawMoney = drawMoney;
	}
	public BigDecimal getRechagePresent() {
		return rechagePresent;
	}
	public void setRechagePresent(BigDecimal rechagePresent) {
		this.rechagePresent = rechagePresent;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getTotalRechage() {
		return totalRechage;
	}
	public void setTotalRechage(BigDecimal totalRechage) {
		this.totalRechage = totalRechage;
	}
	public BigDecimal getDivideMoney() {
		return divideMoney;
	}
	public void setDivideMoney(BigDecimal divideMoney) {
		this.divideMoney = divideMoney;
	}
	public BigDecimal getBetMoney2() {
		return betMoney2;
	}
	public void setBetMoney2(BigDecimal betMoney2) {
		this.betMoney2 = betMoney2;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
		public LotterySetting getLs() {
		return ls;
	}


	public void setLs(LotterySetting ls) {
		this.ls = ls;
	}


	public LotterySettingRl getLsrl() {
		return lsrl;
	}


	public void setLsrl(LotterySettingRl lsrl) {
		this.lsrl = lsrl;
	}

	public String getTimestr() {
		return timestr;
	}

	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

	
}
