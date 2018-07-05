package com.game.model.dto;

import java.math.BigDecimal;

import com.game.model.GaBetOption;

public class GaBetOptionDto {
	private Integer betOptionId;
	private String optionTitle;//下注具体的项  龙  虎  和 大  小
	private Integer optionType;//下注类型，例如 三分彩的两面盘 0=龙虎 1=第一球，三分彩的1-5球  0=第一球，1=第2球
	private BigDecimal betRate;//赔率
	private String gameType; //游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
	private String playType;// 根据具体的游戏具体 比如北京赛车 0=两面盘 1=1-10名  2=冠亚军和   三分彩  0=两面盘 1=1-5球
	private String title;
	private String betRate2; //string类型的赔率。
	private BigDecimal pointMultiple;//积分倍数
	private String hint;//描述
	private String playTypeStr;//将代码变成文字
	
	public GaBetOptionDto() {
		// TODO Auto-generated constructor stub
	}
	
	public GaBetOptionDto(GaBetOption gbo) {
		this.betOptionId = gbo.getBetOptionId();
		this.optionTitle = gbo.getOptionTitle();
		this.optionType = gbo.getBetOptionId();
		this.betRate = gbo.getBetRate();
		this.gameType = gbo.getGameType();
		this.playType = gbo.getPlayType();
		this.title = gbo.getTitle();
		this.betRate2 = gbo.getBetRate2();
		this.pointMultiple = gbo.getPointMultiple();
		this.hint = gbo.getHint();
	}
	
	public Integer getBetOptionId() {
		return betOptionId;
	}
	public void setBetOptionId(Integer betOptionId) {
		this.betOptionId = betOptionId;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public Integer getOptionType() {
		return optionType;
	}
	public void setOptionType(Integer optionType) {
		this.optionType = optionType;
	}
	public BigDecimal getBetRate() {
		return betRate;
	}
	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getPlayType() {
		return playType;
	}
	public void setPlayType(String playType) {
		this.playType = playType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBetRate2() {
		return betRate2;
	}
	public void setBetRate2(String betRate2) {
		this.betRate2 = betRate2;
	}
	public BigDecimal getPointMultiple() {
		return pointMultiple;
	}
	public void setPointMultiple(BigDecimal pointMultiple) {
		this.pointMultiple = pointMultiple;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getPlayTypeStr() {
		return playTypeStr;
	}
	public void setPlayTypeStr(String playTypeStr) {
		this.playTypeStr = playTypeStr;
	}
	
	
}
