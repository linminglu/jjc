package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.dto.SettingDTO;

public class LotterySettingForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private LotterySetting lotterySetting = new LotterySetting();
	private String startDate;
	private String endDate;

	private SettingDTO setting1 = new SettingDTO();
	private SettingDTO setting2 = new SettingDTO();
	private SettingDTO setting3 = new SettingDTO();

	private LotterySettingRl rl1 = new LotterySettingRl();
	private LotterySettingRl rl2 = new LotterySettingRl();
	private LotterySettingRl rl3 = new LotterySettingRl();

	public LotterySettingRl getRl1() {
		return rl1;
	}

	public void setRl1(LotterySettingRl rl1) {
		this.rl1 = rl1;
	}

	public LotterySettingRl getRl2() {
		return rl2;
	}

	public void setRl2(LotterySettingRl rl2) {
		this.rl2 = rl2;
	}

	public LotterySettingRl getRl3() {
		return rl3;
	}

	public void setRl3(LotterySettingRl rl3) {
		this.rl3 = rl3;
	}

	public SettingDTO getSetting1() {
		return setting1;
	}

	public void setSetting1(SettingDTO setting1) {
		this.setting1 = setting1;
	}

	public SettingDTO getSetting2() {
		return setting2;
	}

	public void setSetting2(SettingDTO setting2) {
		this.setting2 = setting2;
	}

	public SettingDTO getSetting3() {
		return setting3;
	}

	public void setSetting3(SettingDTO setting3) {
		this.setting3 = setting3;
	}

	public LotterySetting getLotterySetting() {
		return lotterySetting;
	}

	public void setLotterySetting(LotterySetting lotterySetting) {
		this.lotterySetting = lotterySetting;
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

}
