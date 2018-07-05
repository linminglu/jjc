package com.xy.bj3.model.dto;

import java.math.BigDecimal;

import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.ram.model.User;
import com.xy.bj3.model.Bj3GaBet;
import com.xy.bj3.model.Bj3GaSession;

/**
 * 北京三分彩类
 * @author hpz
 */
public class Bj3DTO {

	private Bj3GaSession bj3GaSession = new Bj3GaSession();
	private String result0; // 第一球
	private String result1; // 第二球
	private String result2; // 第三球
	private String result3; // 第四球
	private String result4; // 第五球
	private Integer resultSum; // 总和
	private String resultDX; // 大小
	private String resultDS; // 单双
	private String resultLH; // 龙虎
	private Bj3GaBet bj3GaBet = new Bj3GaBet();
	private GaBetDetail gaBetDetail = new GaBetDetail();
	private User user = new User();
	private GaBetOption gaBetOption = new GaBetOption();
	private Integer totalNum;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private BigDecimal payoff;
	
	
	public Bj3DTO(){
		
	}
	
	public Bj3DTO(GaBetDetail gaBetDetail, User user){
		this.gaBetDetail = gaBetDetail;
		this.user = user;
	}
	
	public Bj3DTO(Bj3GaSession bj3GaSession,String result0,String result1,String result2,String result3,
			String result4,Integer resultSum,String resultDX,String resultDS,String resultLH){
		this.bj3GaSession = bj3GaSession;
		this.result0 = result0;
		this.result1 = result1;
		this.result2 = result2;
		this.result3 = result3;
		this.result4 = result4;
		this.resultSum = resultSum;
		this.resultDX = resultDX;
		this.resultDS = resultDS;
		this.resultLH = resultLH;
	}
	
	public Bj3DTO(Bj3GaBet bj3GaBet,Bj3GaSession bj3GaSession){
		this.bj3GaBet = bj3GaBet;
		this.bj3GaSession = bj3GaSession;
	}
	public Bj3DTO(BigDecimal totalPoint,
			BigDecimal winCash,BigDecimal payoff){
		this.totalPoint = totalPoint;
		this.winCash = winCash;
		this.payoff = payoff;
	}
	
	public Bj3DTO(GaBetDetail bj3GaBetDetail,Bj3GaSession bj3GaSession,GaBetOption bj3GaBetOption,
			User user){
		this.gaBetDetail = bj3GaBetDetail;
		this.bj3GaSession = bj3GaSession;
		this.user = user;
		this.gaBetOption = bj3GaBetOption;
	}
	
	public Bj3DTO(GaBetDetail bj3GaBetDetail,GaBetOption bj3GaBetOption){
		this.gaBetDetail = bj3GaBetDetail;
		this.gaBetOption = bj3GaBetOption;
	}
	
	public Bj3DTO(GaBetDetail bj3GaBetDetail,GaBetOption bj3GaBetOption,User user){
		this.gaBetDetail = bj3GaBetDetail;
		this.gaBetOption = bj3GaBetOption;
		this.user = user;
	}
	
	public Bj3DTO(Bj3GaBet bj3GaBet,GaBetOption bj3GaBetOption,User user){
		this.bj3GaBet = bj3GaBet;
		this.gaBetOption = bj3GaBetOption;
		this.user = user;
	}
    
	public Bj3DTO(Bj3GaBet bj3GaBet,Bj3GaSession bj3GaSession,User user,GaBetDetail bj3GaBetDetail){
		this.bj3GaBet = bj3GaBet;
		this.bj3GaSession = bj3GaSession;
		this.user = user;
		this.gaBetDetail = bj3GaBetDetail;
	}
	
	public Bj3DTO(GaBetDetail bj3GaBetDetail,GaBetOption bj3GaBetOption,Bj3GaSession bj3GaSession,
			User user){
		this.gaBetDetail = bj3GaBetDetail;
		this.gaBetOption = bj3GaBetOption;
		this.bj3GaSession = bj3GaSession;
		this.user = user;
	}
	
	public Bj3DTO(Bj3GaBet bj3GaBet,Integer totalNum,BigDecimal totalPoint,BigDecimal winCash ){
		this.bj3GaBet = bj3GaBet;
		this.totalNum = totalNum;
		this.totalPoint = totalPoint;
		this.winCash = winCash;
	}
	
	public String getResult0() {
		return result0;
	}

	public void setResult0(String result0) {
		this.result0 = result0;
	}

	public String getResult1() {
		return result1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getResult3() {
		return result3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public String getResult4() {
		return result4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public Integer getResultSum() {
		return resultSum;
	}

	public void setResultSum(Integer resultSum) {
		this.resultSum = resultSum;
	}

	public String getResultDX() {
		return resultDX;
	}

	public void setResultDX(String resultDX) {
		this.resultDX = resultDX;
	}

	public String getResultDS() {
		return resultDS;
	}

	public void setResultDS(String resultDS) {
		this.resultDS = resultDS;
	}

	public String getResultLH() {
		return resultLH;
	}

	public void setResultLH(String resultLH) {
		this.resultLH = resultLH;
	}

	public Bj3GaBet getBj3GaBet() {
		return bj3GaBet;
	}

	public void setBj3GaBet(Bj3GaBet bj3GaBet) {
		this.bj3GaBet = bj3GaBet;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(BigDecimal totalPoint) {
		this.totalPoint = totalPoint;
	}

	public BigDecimal getWinCash() {
		return winCash;
	}

	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}

	public Bj3GaSession getBj3GaSession() {
		return bj3GaSession;
	}

	public void setBj3GaSession(Bj3GaSession bj3GaSession) {
		this.bj3GaSession = bj3GaSession;
	}

	public GaBetDetail getGaBetDetail() {
		return gaBetDetail;
	}

	public void setGaBetDetail(GaBetDetail gaBetDetail) {
		this.gaBetDetail = gaBetDetail;
	}

	public GaBetOption getGaBetOption() {
		return gaBetOption;
	}

	public void setGaBetOption(GaBetOption gaBetOption) {
		this.gaBetOption = gaBetOption;
	}

	public BigDecimal getPayoff() {
		return payoff;
	}

	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}

	
}
