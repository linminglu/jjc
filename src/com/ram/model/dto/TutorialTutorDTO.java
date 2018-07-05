/**
 * 
 */
package com.ram.model.dto;
import java.io.Serializable;
import java.util.List;

import com.ram.model.Learner;
import com.ram.model.PresentTech;
import com.ram.model.Semester;
import com.ram.model.Tutor;
import com.ram.model.TutorialSchedule;
import com.ram.model.TutorialTutor;
import com.ram.model.User;

/**
 * @author cuilidong
 * createTime 2006-2-27
 * commpany bpc
 * project newplat
 */
public class TutorialTutorDTO implements Serializable{
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private PresentTech presentTech = new PresentTech();
//	private ScheduleTutorCourse schTutorCourse=new ScheduleTutorCourse();
//	
//	private ScheduleCourse scheduleCourse=new ScheduleCourse();
//	private Course course = new Course();
//	private Semester semester = new Semester();
//	
//	private TutorialTutor tutorialTutor=new TutorialTutor();
//	private TutorCourse tutorCourse=new TutorCourse();
//	
//	private TutorialTcSchedule tutorialTcSchedule=new TutorialTcSchedule();
//	private TutorialAttendance tutorialAttendance=new TutorialAttendance();
//	
	private Tutor tutor=new Tutor();
//	private Learner learner = new Learner();
//	
	private User user=new User();
//	private User author=new User();
//	
//	private int count;
//	private int attendedNum;
//	private int notAttenedNum;
//	
//	private TcPresentTech tcPresentTech=new TcPresentTech();
//	
//	private TutorialSchedule tutorialSchedule=new TutorialSchedule();
//	
//	private String isAssigned;
//	private String isCurrent;
//	
//	private List list;
//
//	
//	public List getList() {
//		return list;
//	}
//
//	public void setList(List list) {
//		this.list = list;
//	}
//
//	public TutorialTutorDTO(PresentTech presentTech,Course course,Semester semester){
//		this.presentTech = presentTech;
//		this.course = course;
//		this.semester = semester;
//	}
//	public TutorialTutorDTO(User user,Learner learner){
//		this.user = user;
//		this.learner = learner;
//	}
//	
//	public TutorialTutorDTO(TutorCourse tutorCourse,Tutor tutor,User user){
//		this.tutorCourse = tutorCourse;
//		this.tutor = tutor;
//		this.user = user;
//	}
//	
//	public TutorialTutorDTO(User user,TutorialAttendance tutorialAttendance){
//		this.user = user;
//		this.tutorialAttendance = tutorialAttendance;
//	}
//	
//	public TutorialTutorDTO(TcPresentTech tcPresentTech,User user){
//		this.tcPresentTech = tcPresentTech;
//		this.user = user;
//	}
//	
//	public TutorialTutorDTO(){
//		
//	}
//	
//	public ScheduleTutorCourse getScheduleTutorCourse() {
//		return schTutorCourse;
//	}
//
//	public void setScheduleTutorCourse(ScheduleTutorCourse schTutorCourse) {
//		this.schTutorCourse = schTutorCourse;
//	}
//
//	public TutorialTutor getTutorialTutor() {
//		return tutorialTutor;
//	}
//
//	public void setTutorialTutor(TutorialTutor tutorialTutor) {
//		this.tutorialTutor = tutorialTutor;
//	}
//
	public Tutor getTutor() {
		return tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

//	public ScheduleTutorCourse getSchTutorCourse() {
//		return schTutorCourse;
//	}
//
//	public void setSchTutorCourse(ScheduleTutorCourse schTutorCourse) {
//		this.schTutorCourse = schTutorCourse;
//	}
//
//	public TutorialTcSchedule getTutorialTcSchedule() {
//		return tutorialTcSchedule;
//	}
//
//	public void setTutorialTcSchedule(TutorialTcSchedule tutorialTcSchedule) {
//		this.tutorialTcSchedule = tutorialTcSchedule;
//	}
//
//	public ScheduleCourse getScheduleCourse() {
//		return scheduleCourse;
//	}
//
//	public void setScheduleCourse(ScheduleCourse scheduleCourse) {
//		this.scheduleCourse = scheduleCourse;
//	}
//
//	public TutorialSchedule getTutorialSchedule() {
//		return tutorialSchedule;
//	}
//
//	public void setTutorialSchedule(TutorialSchedule tutorialSchedule) {
//		this.tutorialSchedule = tutorialSchedule;
//	}
//
//	public TcPresentTech getTcPresentTech() {
//		return tcPresentTech;
//	}
//
//	public void setTcPresentTech(TcPresentTech tcPresentTech) {
//		this.tcPresentTech = tcPresentTech;
//	}
//
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
//
//	public int getCount() {
//		return count;
//	}
//
//	public void setCount(int count) {
//		this.count = count;
//	}
//
//	public TutorCourse getTutorCourse() {
//		return tutorCourse;
//	}
//
//	public void setTutorCourse(TutorCourse tutorCourse) {
//		this.tutorCourse = tutorCourse;
//	}
//
//	public Learner getLearner() {
//		return learner;
//	}
//
//	public void setLearner(Learner learner) {
//		this.learner = learner;
//	}
//
//	public String getIsAssigned() {
//		return isAssigned;
//	}
//
//	public void setIsAssigned(String isAssigned) {
//		this.isAssigned = isAssigned;
//	}
//
//	public String getIsCurrent() {
//		return isCurrent;
//	}
//
//	public void setIsCurrent(String isCurrent) {
//		this.isCurrent = isCurrent;
//	}
//
//	public TutorialAttendance getTutorialAttendance() {
//		return tutorialAttendance;
//	}
//
//	public void setTutorialAttendance(TutorialAttendance tutorialAttendance) {
//		this.tutorialAttendance = tutorialAttendance;
//	}
//
//	public Course getCourse() {
//		return course;
//	}
//
//	public void setCourse(Course course) {
//		this.course = course;
//	}
//
//	public PresentTech getPresentTech() {
//		return presentTech;
//	}
//
//	public void setPresentTech(PresentTech presentTech) {
//		this.presentTech = presentTech;
//	}
//
//	public Semester getSemester() {
//		return semester;
//	}
//
//	public void setSemester(Semester semester) {
//		this.semester = semester;
//	}
//
//	public User getAuthor() {
//		return author;
//	}
//
//	public void setAuthor(User author) {
//		this.author = author;
//	}
//
//	public int getAttendedNum() {
//		return attendedNum;
//	}
//
//	public void setAttendedNum(int attendedNum) {
//		this.attendedNum = attendedNum;
//	}
//
//	public int getNotAttenedNum() {
//		return notAttenedNum;
//	}
//
//	public void setNotAttenedNum(int notAttenedNum) {
//		this.notAttenedNum = notAttenedNum;
//	}
//
//	
	

}
