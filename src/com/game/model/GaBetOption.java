package com.game.model;
import java.io.Serializable;
import java.math.BigDecimal;

public class GaBetOption implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer betOptionId;
	private String optionTitle;//下注具体的项  龙  虎  和 大  小
	private Integer optionType;//下注类型，例如 三分彩的两面盘 0=龙虎 1=第一球，三分彩的1-5球  0=第一球，1=第2球
	private BigDecimal betRate;//赔率
	private String gameType; //游戏类型 0=三份彩  1=北京赛车  2=幸运28  3=重庆时时彩  4=PC蛋蛋  5=广东快乐10分
	private String playType;// 根据具体的游戏具体 比如北京赛车 0=两面盘 1=1-10名  2=冠亚军和   三分彩  0=两面盘 1=1-5球
	private String playAlias;//玩法别名
	private String title;
	private String betRate2; //string类型的赔率。
	private BigDecimal pointMultiple;//积分倍数
	private String hint;//描述
	private String playTypeZh; //playType对应的汉字
	public void setBetOptionId(Integer betOptionId){
	this.betOptionId=betOptionId;
	}
	public Integer getBetOptionId(){
		return betOptionId;
	}
	public void setOptionTitle(String optionTitle){
	this.optionTitle=optionTitle;
	}
	public String getOptionTitle(){
		return optionTitle;
	}
	
	public Integer getOptionType() {
		return optionType;
	}
	public void setOptionType(Integer optionType) {
		this.optionType = optionType;
	}
	public void setBetRate(BigDecimal betRate){
	this.betRate=betRate;
	}
	public BigDecimal getBetRate(){
		return betRate;
	}
	public void setGameType(String gameType){
	this.gameType=gameType;
	}
	public String getGameType(){
		return gameType;
	}
	public void setPlayType(String playType){
	this.playType=playType;
	}
	public String getPlayType(){
		return playType;
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
	public String getPlayTypeZh() {
		return playTypeZh;
	}
	public void setPlayTypeZh(String playTypeZh) {
		this.playTypeZh = playTypeZh;
	}
	public String getPlayAlias() {
		return playAlias;
	}
	public void setPlayAlias(String playAlias) {
		this.playAlias = playAlias;
	}
	
	
}
