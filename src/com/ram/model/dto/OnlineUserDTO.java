package com.ram.model.dto;

import java.io.Serializable;

import com.ram.model.Learner;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.User;

public class OnlineUserDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Learner learner;
//	private Program program;
//	private Semester semester;
	
//	public Program getProgram() {
//		return program;
//	}
//	public void setProgram(Program program) {
//		this.program = program;
//	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
//	public Semester getSemester() {
//		return semester;
//	}
//	public void setSemester(Semester semester) {
//		this.semester = semester;
//	}
	
	
}
