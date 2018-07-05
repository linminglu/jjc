package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;  

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Position;

/**
 * @author lixiaodong 
 */
public class PositionForm extends ValidatorForm {
	private Position position = new Position();
	
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		position = new Position();
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
