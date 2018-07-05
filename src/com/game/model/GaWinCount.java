package com.game.model;
import java.math.BigDecimal;

   /**
    * GaWinCount 实体类
    * author:	ch
    * createDate:	2017-07-22 17:44:37
    */ 


public class GaWinCount{
	private Integer id;
	private Integer userId;
	private String gameType;//游戏类型
	private BigDecimal totalMoney;//累计中奖金额
	public void setId(Integer id){
	this.id=id;
	}
	public Integer getId(){
		return id;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setGameType(String gameType){
	this.gameType=gameType;
	}
	public String getGameType(){
		return gameType;
	}
	public void setTotalMoney(BigDecimal totalMoney){
	this.totalMoney=totalMoney;
	}
	public BigDecimal getTotalMoney(){
		return totalMoney;
	}
}
