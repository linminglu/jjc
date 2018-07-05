package com.ram.model.dto;

import java.io.Serializable;

import com.ram.model.Learner;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.model.User;

public class LearnerDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Learner learner;
	private Program program;
	private TutorCenter tc;
	private Semester semester;
//	private AllowedApplyGraduate aag;
	
	public LearnerDTO(){}
	
	public LearnerDTO(User user, Learner learner, Program program, TutorCenter tc, Semester semester){
		this.user = user;
		this.learner = learner;
		this.program = program;
		this.tc = tc;
		this.semester = semester;
	}
	
//	public LearnerDTO(User user, Learner learner, Program program, TutorCenter tc, Semester semester, AllowedApplyGraduate aag){
//		this.user = user;
//		this.learner = learner;
//		this.program = program;
//		this.tc = tc;
//		this.semester = semester;
//		this.aag = aag;
//	}
	
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public Program getProgram() {
		return program;
	}
	public void setProgram(Program program) {
		this.program = program;
	}
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	public TutorCenter getTc() {
		return tc;
	}
	public void setTc(TutorCenter tc) {
		this.tc = tc;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

//	public AllowedApplyGraduate getAag() {
//		return aag;
//	}
//
//	public void setAag(AllowedApplyGraduate aag) {
//		this.aag = aag;
//	}	
}
