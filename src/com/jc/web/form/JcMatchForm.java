package com.jc.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.jc.model.JcField;
import com.jc.model.JcMatch;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;

public class JcMatchForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private JcMatch match = new JcMatch();
	
	private JcField field = new JcField();
	
	private JcPlayType playType = new JcPlayType();
	
	private JcOption option = new JcOption();
	
	private FormFile file;
	
	private String startDate;
	private String endDate;
	
	private String openTime;
	private String matchTime;

	public JcMatch getMatch() {
		return match;
	}

	public void setMatch(JcMatch match) {
		this.match = match;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
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

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public JcField getField() {
		return field;
	}

	public void setField(JcField field) {
		this.field = field;
	}

	public JcPlayType getPlayType() {
		return playType;
	}

	public void setPlayType(JcPlayType playType) {
		this.playType = playType;
	}

	public JcOption getOption() {
		return option;
	}

	public void setOption(JcOption option) {
		this.option = option;
	}
}
