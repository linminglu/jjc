package com.ram.web.permission.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Function;

public class FunctionForm extends ValidatorForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Function function=new Function();
	private Function parentFunction;
	
	private int parentFunctionID=0;
	private int currentFunctionID=0;
	
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return super.validate(arg0, arg1);
	}

	public int getParentFunctionID() {
		return parentFunctionID;
	}

	public void setParentFunctionID(int parentFunctionID) {
		this.parentFunctionID = parentFunctionID;
	}

	public int getCurrentFunctionID() {
		return currentFunctionID;
	}

	public void setCurrentFunctionID(int currentFunctionID) {
		this.currentFunctionID = currentFunctionID;
	}

	public Function getParentFunction() {
		return parentFunction;
	}

	public void setParentFunction(Function parentFunction) {
		this.parentFunction = parentFunction;
	}

}
