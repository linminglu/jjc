package com.jc.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.jc.model.JcField;
import com.jc.model.JcOption;
import com.jc.model.JcPlayType;

public class JcFieldForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private JcField field = new JcField();
	
	private JcPlayType playType = new JcPlayType();
	
	private JcOption option = new JcOption();
	
	private String startDate;
	
	private String endDate;
	
	

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
