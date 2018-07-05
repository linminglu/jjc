package com.ram.web.user.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.ram.RamConstants;
import com.ram.model.Learner;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.service.system.ISemesterService;
import com.ram.service.system.ISystemProgramService;
import com.ram.service.system.ISystemTutorCenterService;
import com.ram.service.user.IUserService;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class LearnerListAction extends BaseDispatchAction {
	private final IUserService userService = (IUserService)getService("userService");
	private final ISystemTutorCenterService systemTutorCenterService = (ISystemTutorCenterService)getService("systemTutorCenterService");
	private final ISemesterService semesterService = (ISemesterService)getService("semesterService");
	private final ISystemProgramService systemProgramService = (ISystemProgramService)getService("systemProgramService");

	/** 
	 * 显示Learner列表页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward init(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
		getLearnerInfo(request, startIndex, pageSize);
//		if(request.getSession(false).getAttribute("tcId") != null){
//			request.getSession(false).removeAttribute("tcId");
//		}
//		if(request.getSession(false).getAttribute("userName") != null){
//			request.getSession(false).removeAttribute("userName");
//		}
//		if(request.getSession(false).getAttribute("programId") != null){
//			request.getSession(false).removeAttribute("programId");
//		}
//		if(request.getSession(false).getAttribute("semesterId") != null){
//			request.getSession(false).removeAttribute("semesterId");
//		}
//		if(request.getSession(false).getAttribute("enrollStatus") != null){
//			request.getSession(false).removeAttribute("enrollStatus");
//		}
//		List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
//		Iterator item = tutorCenterList.iterator();
//		Collection result = new ArrayList();
//		
//		// 下拉框中增加一项：所有中心，id=0
//		LabelValueBean topLabel = new LabelValueBean("所有中心","0");
//		result.add(topLabel);
//		while(item.hasNext()){
//			TutorCenter tutorCenter = (TutorCenter)item.next();
//			LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
//			result.add(labelValueBean);
//		}
//		request.setAttribute("tcList", result);
		initList(request,response);
		return mapping.findForward("init");
	}
	
	/**
	 * 根据查询条件获得Learner信息列表
	 * @param request
	 * @param startIndex
	 * @param pageSize
	 * @param query
	 * @return
	 */
	 private boolean getLearnerInfo(HttpServletRequest request, int startIndex, int pageSize){
//    	PaginationSupport paginationSupport;
//    	List result = new ArrayList();
//    	paginationSupport = userService.getAllLearners(startIndex, pageSize);
//		if(paginationSupport == null){
//			return false;
//		}
//		else{
//			List learnerList = paginationSupport.getItems();
//			Iterator learnerItr = learnerList.iterator();
//			while(learnerItr.hasNext()){
//				Learner learner = (Learner)learnerItr.next();
//				try{
//					LearnerForm itemInfo = new LearnerForm();
//					if(learner.getTcId() == null){
//						itemInfo.setLearner(learner);
//						itemInfo.setTcCenterName("");
//						itemInfo.setSemesterName("");
//						itemInfo.setProgramName("");
//						itemInfo.setSpecialityName("");
//					}
//					else{
//						itemInfo.setLearner(learner);
//						itemInfo.setTcCenterName(systemTutorCenterService.getSystemTutorCenter(learner.getTcId()).getTcTitle());
//						itemInfo.setSemesterName(semesterService.getSemester(learner.getSemesterId()).getSemesterTitle());
//						itemInfo.setProgramName(systemProgramService.getSystemProgram(learner.getProgramId()).getProgramName());
//						if(learner.getSpecialityId().compareTo(new Integer(0)) != 0){
//							itemInfo.setSpecialityName(courseService.getSpeciality(learner.getSpecialityId()).getSpecialityTitle());
//						}
//						else{
//							itemInfo.setSpecialityName("尚未选择专业方向");
//						}
//					}
//					result.add(itemInfo);
//				}
//				catch(Exception ex){
//					log.error(ex);
//				}
//			}
//			int learnerTotalCount = paginationSupport.getTotalCount();
//			request.setAttribute("learnerList", result);
//			request.setAttribute("learnerTotalCount", new Integer(learnerTotalCount));
//			return true;
//		}
		 return true;
    }
	 
	 /** 
	 * 显示选定用户的详细信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	 public ActionForward show(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
//		String id = request.getParameter("learnerId");
//		if(id != null){
//			int learnerId = Integer.parseInt(id);
//			Learner learner = userService.getLearner(learnerId);
//			LearnerForm itemInfo = new LearnerForm();
//			if(learner!=null){
//				if(learner.getTcId() == null){
//					itemInfo.setLearner(learner);
//					itemInfo.setTcCenterName("");
//					itemInfo.setSemesterName("");
//					itemInfo.setProgramName("");
//					itemInfo.setSpecialityName("");
//				}
//				else{
//					TutorCenter tc = systemTutorCenterService.getSystemTutorCenter(learner.getTcId());
//					Semester sm = semesterService.getSemester(learner.getSemesterId());
//					itemInfo.setTcCenterName(tc == null ? "" : tc.getTcTitle());
//					itemInfo.setSemesterName(sm.getSemesterTitle());
//				//	itemInfo.setProgramName(this.systemProgramService.getSystemProgram(learner.getProgramId()).getProgramName());
//					if(learner.getSpecialityId()!=null && learner.getSpecialityId().intValue() != 0){
//						itemInfo.setSpecialityName(courseService.getSpeciality(learner.getSpecialityId()).getSpecialityTitle());
//					}
//					else{
//						itemInfo.setSpecialityName("尚未选择专业方向");
//					}
//					itemInfo.setProgramName(systemProgramService.getSystemProgram(learner.getProgramId()).getProgramName());
//					itemInfo.setLearner(learner);
//				}
//				//log.info("userId:"+learner.getUser().getUserId());
//				
//				 Integer semesterId = learner.getSemesterId();
//				 Integer programId = learner.getProgramId();
//				 if(semesterId!=null && programId != null){
//				 Schedule schedule=scheduleService.getScheduleIdBySemProg(semesterId.intValue(), programId.intValue());
//				 
//				 List scheduleModuleList = schedule == null ? null : scheduleService.getScheduleModuleByScheduleId(schedule.getScheduleId().intValue());
//				 request.setAttribute("scheduleModule", scheduleModuleList);
//				 List learnerEnroll = learnerEnrollmentService.findLearnerEnrollByUserId(learner.getUser().getUserId());
//				 request.setAttribute("learnerEnroll", learnerEnroll);
//				 }
//
//				List tclist = systemTutorCenterService.findSystemTutorCenters();
////				List rctclist = enrollManageService.findAllRequestChangeTcByUserId(learner.getUser().getUserId());
////				List hcelist = enrollManageService.getHistoryCourseEnrollmentByUserId(learner.getUser().getUserId());
////				List rdlist = enrollManageService.getRequestDropoutByRequestUserId(learner.getUser().getUserId());
//				
//				request.setAttribute("tclist", tclist);
////				request.setAttribute("rctclist", rctclist);
////				request.setAttribute("hcelist", hcelist);
////				request.setAttribute("rdlist", rdlist);
//			}
//			request.setAttribute("learner", itemInfo);
//		}
		return mapping.findForward("show");
	}
	 
	 /** 
	 * 批量删除用户(user.status=-1)
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) {
		//获取当前用户
  		User user = null;
  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
  		
		String[] indexs = null;
		int[] delIndexs = null;
		if((indexs = request.getParameterValues("selIndexs")) != null){
			delIndexs = new int[indexs.length];
			for(int i = 0; i < indexs.length; i++){
				delIndexs[i] = Integer.parseInt(indexs[i]);
			}
			userService.deleteUser(delIndexs, user);
		}
		
		return mapping.findForward("delete");
	}
//	初始化下拉列表


	public void initList(HttpServletRequest request,HttpServletResponse response){
		    List tcList=systemTutorCenterService.findSystemTutorCenters();
		    List programList=systemProgramService.findSystemPrograms();
		    List semesterList=this.semesterService.findSemesters();
		  
		    
		    Collection  tcListCollection= new ArrayList();  
		    Collection  programListCollection=new ArrayList();
		    Collection  semesterListCollection=new ArrayList();
		
		    
		    /**
		     * 学习中心列表
		     */
		    tcListCollection.add(new LabelValueBean("全部中心", null));
		    if (tcList != null && tcList.size() > 0) {
		      for (int i = 0; i < tcList.size(); i++) {	      
		    	  TutorCenter tutorCenter=(TutorCenter)tcList.get(i); 
		          String tcID=tutorCenter.getTcId()+"";
		          tcListCollection.add(new LabelValueBean(tutorCenter.getTcTitle(),tcID.trim()));
		      }
		    }
		    /**
		     * 专业层次列表
		     */
		    programListCollection.add(new LabelValueBean("全部层次", null));
		    if(programList!=null&&programList.size()>0){
		    	for(int i=0;i<programList.size();i++){
		    		Program program=(Program)programList.get(i);
		    		String programID=program.getProgramId()+"";
		    		programListCollection.add(new LabelValueBean(program.getProgramName(),programID.trim()));
		    	}
		    }
		    /**
		     * 设置学期
		     */
		    semesterListCollection.add(new LabelValueBean("全部学期",null));
		    if(semesterList!=null&&semesterList.size()>0)
		    {
		    	for(int i=0;i<semesterList.size();i++)
		    	{
		    	Semester semester=(Semester)semesterList.get(i);
		    	String semesterId=semester.getSemesterId()+"";
		    	semesterListCollection.add(new LabelValueBean(semester.getSemesterTitle(),semesterId.trim()));
		    	}
		    }
		    
		    Collection studentStatusCollection = new ArrayList();
		    studentStatusCollection.add(new LabelValueBean("所有的状态",null));
		    studentStatusCollection.add(new LabelValueBean("注册未录取", "1"));
		    studentStatusCollection.add(new LabelValueBean("被录取", "2"));
		    studentStatusCollection.add(new LabelValueBean("在学", "3"));
		    studentStatusCollection.add(new LabelValueBean("退学", "4"));
		    studentStatusCollection.add(new LabelValueBean("毕业", "5"));
		    studentStatusCollection.add(new LabelValueBean("流失", "6"));
		    
		    request.setAttribute("tcListCollection", tcListCollection);
		    request.setAttribute("programListCollection", programListCollection);
		    request.setAttribute("semesterListCollection", semesterListCollection);
			request.setAttribute("studentStatusCollection", studentStatusCollection);
	}
	
//	/**
//	 * 以学生用户身份登录//
//	 * @author Lu Congyu
//	 * @date 06/07/10
//	 * @return
//	 */
//	public ActionForward login(ActionMapping mapping, ActionForm form,
//		HttpServletRequest request, HttpServletResponse response){
//		
//		int userId = ParamUtils.getIntParameter(request,"id", 0);
//		
//		if(userId == 0) return mapping.findForward("notAllowed");
//		//获取当前用户
//  		User user = null;
//  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//  		
//		//如果,此用户为学生,则不可以登录其它学生帐号
//		Learner l = userService.getLearnerByUserId(user.getUserId().intValue());
//
//		
//		if(l != null) return mapping.findForward("notAllowed");
//		
//		Integer managerId = user.getUserId();
//		String managerName = user.getUserNameZh();
//
//		User luser = userService.getUser(userId);
//		
//		//设置登录前用户ID号//
//		luser.setManagerId(managerId);
//		userLogService.saveLog(user,managerName+"在"+request.getRemoteAddr()+"以用户[username="+luser.getLoginName()+"]身份登录" );
//		request.getSession(false).setAttribute("loginUser", luser);
//		request.setAttribute("loginUser", luser);
//		
//		//用户登录组//
//		List userGroupList = userService.getUserGroupById(userId);
//		request.setAttribute("loginUserGroupList", userGroupList);
//		return mapping.findForward("login");
//	}
//	
//	public ActionForward loginM(ActionMapping mapping, ActionForm form,
//		HttpServletRequest request, HttpServletResponse response){
//		//获取当前用户
//  		User user = null;
//  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//  		
//		int userId = ParamUtils.getIntParameter(request, "id", 0);
//		int managerId = user.getManagerId() == null ? 0 : user.getManagerId().intValue();
//
//		if(managerId > 0 && userId == managerId){
//		}else{
//			managerId = userId;
//		}
//		
//		User luser = userService.getUser(managerId);
//		request.getSession(false).setAttribute("loginUser", luser);
//		request.setAttribute("loginUser", luser);
//		
//		//用户登录组//
//		List userGroupList = userService.getUserGroupById(managerId);
//		request.setAttribute("loginUserGroupList", userGroupList);
//		return mapping.findForward("login");
//	}
	
}
