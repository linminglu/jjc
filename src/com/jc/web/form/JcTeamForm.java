package com.jc.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.jc.model.JcTeam;

public class JcTeamForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	
	private JcTeam jcTeam = new JcTeam();

	private FormFile file;
	
	public JcTeam getJcTeam() {
		return jcTeam;
	}

	public void setJcTeam(JcTeam jcTeam) {
		this.jcTeam = jcTeam;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

}
