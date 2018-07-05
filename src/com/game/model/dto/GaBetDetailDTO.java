package com.game.model.dto;
import java.util.Date;
import java.math.BigDecimal;

public class GaBetDetailDTO{
	private Integer betDetailId;// 投注id
	private Integer betOptionId;// 投注项id
	private Integer sessionId;// 比赛局ID
	private Date betTime;// 投注时间
	private Integer betMoney;// 投注金额
	private String winResult;// 0未开奖  1中奖  2未中奖
	private BigDecimal winCash;// 中奖金额
	private String playName;// 比赛名称
	private BigDecimal betRate;// 赔率
	private String optionTitle;// 投注项名称字
	private String room;// 局名称
	
	private Integer matchId;// 比赛id
	
	public GaBetDetailDTO(){}
	
	// 竞猜记录
	public GaBetDetailDTO(Integer betDetailId, Integer betOptionId, Integer sessionId, Date betTime,
			Integer betMoney, String winResult, BigDecimal winCash, String playName, String room,
			String optionTitle, BigDecimal betRate, Integer matchId){
		this.betDetailId=betDetailId;
		this.betOptionId=betOptionId;
		this.sessionId=sessionId;
		this.betTime=betTime;
		this.betMoney=betMoney;
		this.winResult=winResult;
		this.winCash=winCash;
		this.playName=playName;
		this.room=room;
		this.optionTitle=optionTitle;
		this.betRate=betRate;
		this.matchId=matchId;
	}

	public Integer getBetDetailId() {
		return betDetailId;
	}

	public void setBetDetailId(Integer betDetailId) {
		this.betDetailId = betDetailId;
	}

	public Integer getBetOptionId() {
		return betOptionId;
	}

	public void setBetOptionId(Integer betOptionId) {
		this.betOptionId = betOptionId;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}

	public Integer getBetMoney() {
		return betMoney;
	}

	public void setBetMoney(Integer betMoney) {
		this.betMoney = betMoney;
	}

	public String getWinResult() {
		return winResult;
	}

	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}

	public BigDecimal getWinCash() {
		return winCash;
	}

	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}

	public String getPlayName() {
		return playName;
	}

	public void setPlayName(String playName) {
		this.playName = playName;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
}
