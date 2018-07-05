package com.game.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.ram.model.User;

public class SpDetailDTO {
	private GaBetPart gaBetPart = new GaBetPart();
	private GaBetSponsorDetail gaBetSponsorDetail = new GaBetSponsorDetail();
	private GaBetSponsor gaBetSponsor = new GaBetSponsor();
	private User user = new User();
	private String userName;
	private String gameName;// 游戏名字
	private BigDecimal money;// 投注金额
	private BigDecimal winCash;// 中奖金额
	private Date betTime;// 投注时间
	private String winResult;// 状态1=无效 2=未开始 3=开奖中  4=中奖 5=不中奖
	private String sessionNo;// 期号
	private Integer multiple; // 购买倍数
	private String title;// 玩法中文名字
	public SpDetailDTO(){
		
	}
	public SpDetailDTO(String userName, String gameName, BigDecimal money,
			BigDecimal winCash, Date betTime, String winResult, String sessionNo,
			Integer multiple, String title) {
		super();
		this.userName = userName;
		this.gameName = gameName;
		this.money = money;
		this.winCash = winCash;
		this.betTime = betTime;
		this.winResult = winResult;
		this.sessionNo = sessionNo;
		this.multiple = multiple;
		this.title = title;
	}
	public SpDetailDTO(GaBetSponsorDetail gaBetSponsorDetail,GaBetSponsor gaBetSponsor){
		this.gaBetSponsorDetail = gaBetSponsorDetail;
		this.gaBetSponsor = gaBetSponsor;
	}
	public SpDetailDTO(GaBetPart gaBetPart,GaBetSponsor gaBetSponsor,String userName){
		this.gaBetPart=gaBetPart;
		this.gaBetSponsor =gaBetSponsor;
		this.userName=userName;
	}
	public SpDetailDTO(GaBetSponsor gaBetSponsor,User user){
		this.gaBetSponsor=gaBetSponsor;
		this.user=user;
	}

	public SpDetailDTO(GaBetPart gaBetPart,User user){
		this.gaBetPart=gaBetPart;
		this.user=user;
	}

	public SpDetailDTO(GaBetPart gaBetPart,String userName){
		this.gaBetPart=gaBetPart;
		this.userName=userName;
	}

	public GaBetPart getGaBetPart() {
		return gaBetPart;
	}

	public void setGaBetPart(GaBetPart gaBetPart) {
		this.gaBetPart = gaBetPart;
	}


	public GaBetSponsor getGaBetSponsor() {
		return gaBetSponsor;
	}

	public void setGaBetSponsor(GaBetSponsor gaBetSponsor) {
		this.gaBetSponsor = gaBetSponsor;
	}
	public GaBetSponsorDetail getGaBetSponsorDetail() {
		return gaBetSponsorDetail;
	}
	public void setGaBetSponsorDetail(GaBetSponsorDetail gaBetSponsorDetail) {
		this.gaBetSponsorDetail = gaBetSponsorDetail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getWinCash() {
		return winCash;
	}
	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}
	public Date getBetTime() {
		return betTime;
	}
	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	public String getWinResult() {
		return winResult;
	}
	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}
	public String getTitle() {
		return title;
	}
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
