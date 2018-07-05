package com.apps.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Type;

public class TypeForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Type type = new Type();
	private FormFile file;

	private String tid;
	private String column;
	private Integer typeCateId;
	private String types;//栏目类型
	
	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Integer getTypeCateId() {
		return typeCateId;
	}

	public void setTypeCateId(Integer typeCateId) {
		this.typeCateId = typeCateId;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}
	
	
}
