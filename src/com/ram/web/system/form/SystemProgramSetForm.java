package com.ram.web.system.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Program;

public class SystemProgramSetForm extends ValidatorForm {


	
	private Program program = new Program();
	

     
     public Program getProgram() {
		return program;
	}



	public void setProgram(Program program) {
		this.program = program;
	}



	public void reset(ActionMapping mapping, HttpServletRequest request) {
    	 
     }
}
