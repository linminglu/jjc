package com.ram.web.system.form;



import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Semester;

public class SemesterForm extends ValidatorForm {

	private Semester semester = new Semester();
	private java.sql.Date dateStart;
	private java.sql.Date dateEnd;
	
	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {

	}

	public java.sql.Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(java.sql.Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public java.sql.Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(java.sql.Date dateStart) {
		this.dateStart = dateStart;
	}



}
