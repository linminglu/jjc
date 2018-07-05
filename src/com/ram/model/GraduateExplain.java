package com.ram.model;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
public class GraduateExplain implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer graduateExplainId;
	
	private String graduateExplainDesc;
	
	
	  public GraduateExplain(){}
	  
	  public GraduateExplain(String graduateExplainDesc) {
			this.graduateExplainDesc = graduateExplainDesc;
	 }
	

	public String getGraduateExplainDesc() {
		return graduateExplainDesc;
	}

	public void setGraduateExplainDesc(String graduateExplainDesc) {
		this.graduateExplainDesc = graduateExplainDesc;
	}

	public Integer getGraduateExplainId() {
		return graduateExplainId;
	}

	public void setGraduateExplainId(Integer graduateExplainId) {
		this.graduateExplainId = graduateExplainId;
	}
	  public String toString() {
	        return new ToStringBuilder(this)
	            .append("graduateExplainId", getGraduateExplainId())
	            .toString();
	    }

}
