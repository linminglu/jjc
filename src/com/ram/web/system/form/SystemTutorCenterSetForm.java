package com.ram.web.system.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.TutorCenter;

public class SystemTutorCenterSetForm extends ValidatorForm {

    private String searchName;
	private Integer tcId;
	
	private Integer parentTcId;
	
	private TutorCenter tutorCenter = new TutorCenter();
	

     
     public TutorCenter getTutorCenter() {
		return tutorCenter;
	}



	public void setTutorCenter(TutorCenter tutorCenter) {
		this.tutorCenter = tutorCenter;
	}



	public void reset(ActionMapping mapping, HttpServletRequest request) {
    	 
     }



	public Integer getTcId() {
		return tcId;
	}



	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}



	public Integer getParentTcId() {
		return parentTcId;
	}



	public void setParentTcId(Integer parentTcId) {
		this.parentTcId = parentTcId;
	}



	public String getSearchName() {
		return searchName;
	}



	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
}
