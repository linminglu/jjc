package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.game.model.GaBetOption;
import com.game.model.GaSessionInfo;

public class GameForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private String moduleType;
	private String startIndex;
	private GaSessionInfo gaSessionInfo = new GaSessionInfo();
	private GaBetOption option=new GaBetOption();
	private String gameType;
	private String title;
	
	private String startDate;
	private String playCate;//彩种类型  1=官方   2=信用
	private String status; //状态。默认为1
	private String betAvoid; //是否能投注
	
	
	public GaSessionInfo getGaSessionInfo() {
		return gaSessionInfo;
	}
	public void setGaSessionInfo(GaSessionInfo gaSessionInfo) {
		this.gaSessionInfo = gaSessionInfo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public GaBetOption getOption() {
		return option;
	}
	public void setOption(GaBetOption option) {
		this.option = option;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
		public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getPlayCate() {
		return playCate;
	}
	public void setPlayCate(String playCate) {
		this.playCate = playCate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBetAvoid() {
		return betAvoid;
	}
	public void setBetAvoid(String betAvoid) {
		this.betAvoid = betAvoid;
	}	
}
