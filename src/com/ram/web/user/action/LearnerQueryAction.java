package com.ram.web.user.action;

import com.framework.web.action.BaseDispatchAction;

/**
 * @author lixiaodong 
 */
public class LearnerQueryAction extends BaseDispatchAction {
//	private final IUserService userService = (IUserService)getService("userService");
//	private final ISystemTutorCenterService tcService= (ISystemTutorCenterService)getService("systemTutorCenterService");
//	private ISemesterService iSemesterService= (ISemesterService)getService("semesterService");
//	private ISystemProgramService systemProgramService = (ISystemProgramService)getService("systemProgramService");
//	private final ISemesterService semesterService= (ISemesterService)getService("semesterService");
//	/**
//	 * 学员查询的初始化方法
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	public ActionForward init(
//			ActionMapping mapping,
//			ActionForm form,
//			HttpServletRequest request,
//			HttpServletResponse response) {
//		return queryList(mapping, form, request, response);
////		int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
////		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
////		HQuery hQuery = new HQuery();
////		ParaList paraList = new ParaList();
////		StringBuffer studentQuery = new StringBuffer("select learner,user from Learner learner,User user where learner.user.userId=user.userId and user.status='1' ");
////		studentQuery.append(" order by learner.user.userId desc");
////		hQuery.setParalist(paraList);
////		hQuery.setQueryString(studentQuery.toString());
////		getLearnerInfo(request, startIndex, pageSize, hQuery);
////		request.setAttribute("queryFlg", null);
////		initList(request,response);
////		return mapping.findForward("init");
//		
//	}
//	/** 
//	 * 根据查询条件显示Manager列表页面
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return ActionForward
//	 */
//	public ActionForward test(
//		ActionMapping mapping,
//		ActionForm form,
//		HttpServletRequest request,
//		HttpServletResponse response) {
//		LearnerQueryForm learnerQueryForm = (LearnerQueryForm)form;	
//		DetachedCriteria query = DetachedCriteria.forClass(Learner.class, "learner");
//		HttpSession session = request.getSession(false);
//		session.setAttribute("learnerQueryForm",learnerQueryForm);
//		if(learnerQueryForm.getTcId() != null){
//			if(!learnerQueryForm.getTcId().toString().equals("0")){
//				session.setAttribute("tcId", learnerQueryForm.getTcId());
//				query.add(Restrictions.eq("learner.tcId", learnerQueryForm.getTcId()));
//			}
//		}
//    	else if(session.getAttribute("tcId") != null){
//			query.add(Restrictions.eq("learner.tcId", (Integer)session.getAttribute("tcId")));
//		}
//		if(learnerQueryForm.getProgramId()!=null)
//		{
//			session.setAttribute("programId",learnerQueryForm.getProgramId());
//			query.add(Restrictions.eq("learner.programId",learnerQueryForm.getProgramId()));
//		}
//		else if(session.getAttribute("programId") != null)
//		{
//			query.add(Restrictions.eq("learner.programId",(Integer)session.getAttribute("programId")));
//		}
//		if(learnerQueryForm.getSemesterId()!=null)
//		{
//			session.setAttribute("semesterId",learnerQueryForm.getSemesterId());
//			query.add(Restrictions.eq("learner.semesterId",learnerQueryForm.getSemesterId()));
//		}
//		else if(session.getAttribute("semesterId")!=null)
//		{
//			query.add(Restrictions.eq("learner.semesterId",(Integer)session.getAttribute("semesterId")));
//		}
//		log.info("==============================="+learnerQueryForm.getEnrollStatus());
//		if(!learnerQueryForm.getEnrollStatus().equals("0"))
//		{
//			
//			session.setAttribute("enrollStatus",learnerQueryForm.getEnrollStatus());
//			log.info("================================="+session.getAttribute("enrollStatus"));
//			query.add(Restrictions.eq("learner.enrollStatus",learnerQueryForm.getEnrollStatus()));	
//		}
//		else
//		{
//			
//			query.add(Restrictions.eq("learner.enrollStatus",session.getAttribute("enrollStatus")));
//		}
//		if(learnerQueryForm.getUserName() != "" && learnerQueryForm.getUserName() != null){
//			query.createAlias("user", "user").add(Restrictions.ilike("user.loginName", "%" + learnerQueryForm.getUserName() + "%")).add(Restrictions.ne("user.status", "0"));
//			session.setAttribute("userName", learnerQueryForm.getUserName());
//		}
//		else if(session.getAttribute("userName") != null){
//			query.createAlias("user", "user").add(Restrictions.ilike("user.loginName", "%" + (String)session.getAttribute("userName") + "%")).add(Restrictions.ne("user.status", "0"));
//		}
//		else{
//			query.createAlias("user", "user").add(Restrictions.ne("user.status", "0"));
//		}
//		int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
//		int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
//		getLearnerInfo(request, startIndex, pageSize, query);
//		request.setAttribute("queryFlg", "1");
////		List tutorCenterList = systemTutorCenterService.findSystemTutorCenters();
////		Iterator item = tutorCenterList.iterator();
////		Collection result = new ArrayList();
////		
////		// 下拉框中增加一项：所有中心，id=0
////		LabelValueBean topLabel = new LabelValueBean("所有中心","0");
////		result.add(topLabel);
////		
////		while(item.hasNext()){
////			TutorCenter tutorCenter = (TutorCenter)item.next();
////			LabelValueBean labelValueBean = new LabelValueBean(tutorCenter.getTcTitle(), tutorCenter.getTcId().toString());
////			result.add(labelValueBean);
////		}
////		request.setAttribute("tcList", result);
//		initList(request,response);
//		request.setAttribute("learnerQueryForm",session.getAttribute("learnerQueryForm"));
//		return mapping.findForward("query");
//	}
//	
//	public ActionForward query(
//			ActionMapping mapping,
//			ActionForm form,
//			HttpServletRequest request,
//			HttpServletResponse response) {
//		  
//			//LearnerQueryForm learnerQueryForm=(LearnerQueryForm)form;	
//			  HttpSession session=request.getSession(false);
//			int startIndex = ParamUtils.getIntParameter(request,"pager.offset",0);
//			int pageSize = ParamUtils.getIntParameter(request,"maxPageItems",RamConstants.MAXPAGEITEMS);
//			String tcId=ParamUtils.getParameter(request,"tcCenter");
//			
//			String  programId=ParamUtils.getParameter(request,"programId");
//			
//			
//			String  semesterId=ParamUtils.getParameter(request,"semesterId");
//			String userName=ParamUtils.getParameter(request,"userName");
//			String enrollStatus=ParamUtils.getParameter(request,"enrollStatus");
//		
//		
//			HQuery hQuery = new HQuery();
//			ParaList paraList = new ParaList();
//		//	StringBuffer studentQuery = new StringBuffer("select learner,user,tutorCenter.tcTitle,program.programName,semester.semesterTitle,speciality.specialityTitle from Learner learner,User user,TutorCenter tutorCenter,Program program,Semester semester,Speciality speciality where learner.user.userId=user.userId and learner.tcId=tutorCenter.tcId and learner.semesterId=semester.semesterId and learner.specialityId=speciality.specialityId and learner.programId=program.programId");
//			StringBuffer studentQuery = new StringBuffer("select learner,user from Learner learner,User user where learner.user.userId=user.userId and user.status='1' ");
//			if (programId.length()>0) {
//				log.info("===========programId==========="+programId);
//				studentQuery.append(" and ");
//				studentQuery.append("learner.programId = ? ");
//				Paras paras = new Paras();
//				paras.setObj(Integer.valueOf(programId));
//				paras.setTypeNo(Types.INTEGER);
//				paraList.addParas(paras);
//			}
//			if (tcId.length()>0) {
//				studentQuery.append(" and ");
//				studentQuery.append("learner.tcId = ? ");
//				Paras paras = new Paras();
//				paras.setObj(Integer.valueOf(tcId));
//				paras.setTypeNo(Types.INTEGER);
//				paraList.addParas(paras);
//			}
//			if(semesterId.length()>0)
//			{
//				studentQuery.append(" and ");
//				studentQuery.append("learner.semesterId = ? ");
//				Paras paras = new Paras();
//				paras.setObj(Integer.valueOf(semesterId));
//				paras.setTypeNo(Types.INTEGER);
//				paraList.addParas(paras);
//			}
//			if(enrollStatus.length()>0)
//			{
//				studentQuery.append(" and ");
//				studentQuery.append("learner.enrollStatus = ? ");
//				Paras paras = new Paras();
//				paras.setObj(enrollStatus);
//				paras.setTypeNo(Types.VARCHAR);
//				paraList.addParas(paras);
//			}
//		if(userName.trim().length()>0)
//			{
//			studentQuery.append(" and ");
//				studentQuery.append("learner.user.loginName like ? ");
//			    Paras paras = new Paras();
//				paras.setObj(userName + "%");
//				paras.setTypeNo(Types.VARCHAR);
//				paraList.addParas(paras);
//			}	
//		
//		studentQuery.append(" order by learner.user.userId desc");
//			hQuery.setParalist(paraList);
//			hQuery.setQueryString(studentQuery.toString());
//			getLearnerInfo(request, startIndex, pageSize, hQuery);
//			request.getSession(false).setAttribute("tcId",tcId);
//			request.getSession(false).setAttribute("programId",programId);
//			request.getSession(false).setAttribute("semesterId",semesterId);
//			request.getSession(false).setAttribute("userName",userName);
//			request.getSession(false).setAttribute("enrollStatus",enrollStatus);
//			request.setAttribute("queryFlg", "1");
//			initList(request,response);
//			return mapping.findForward("query");
//		}
//		
//	
////	/**
////	 * 根据查询条件获得Learner信息列表
////	 * @param request
////	 * @param startIndex
////	 * @param pageSize
////	 * @param query
////	 * @return
////	 */
////	 private boolean getLearnerInfo(HttpServletRequest request, int startIndex, int pageSize, DetachedCriteria query){
////    	HQuery hquery = new HQuery();
////		PaginationSupport paginationSupport;
////    	List result = new ArrayList();
////    	paginationSupport = userService.findAllLearners(startIndex, pageSize, query);
////		if(paginationSupport == null){
////			return false;
////		}
////		else{
////			List learnerList = paginationSupport.getItems();
////			Iterator learnerItr = learnerList.iterator();
////			while(learnerItr.hasNext()){
////				Object[] infos = (Object[])learnerItr.next();
////				Learner learner = (Learner)infos[1];
////				User user = (User)infos[0];
////				try{
////					LearnerForm itemInfo = new LearnerForm();
////					itemInfo.setUser(user);
////					if(learner.getTcId() == null){
////						itemInfo.setLearner(learner);
////						itemInfo.setTcCenterName("");
////					}
////					else{
////						BeanUtils.copyProperties(itemInfo.getLearner(), learner);
////						itemInfo.setTcCenterName(tcService.getSystemTutorCenter(learner.getTcId()).getTcTitle());	
////						itemInfo.setProgramName(this.systemProgramService.getSystemProgram(learner.getProgramId()).getProgramName());
////						itemInfo.setSemesterName(this.iSemesterService.getSemester(learner.getSemesterId()).getSemesterTitle());
////						itemInfo.setSpecialityName(this.iCourseService.getSpeciality(learner.getSpecialityId()).getSpecialityTitle());
////					}
////					
////					result.add(itemInfo);
////				}
////				catch(Exception ex){
////					log.error(ex);
////				}
////			}
////			int learnerTotalCount = paginationSupport.getTotalCount();
////			request.setAttribute("learnerList", result);
////			request.setAttribute("learnerTotalCount", new Integer(learnerTotalCount));
////			return true;
////		}
////    }
////	初始化下拉列表//
//
//
//		public void initList(HttpServletRequest request,HttpServletResponse response){
//			    List tcList=tcService.findSystemTutorCenters();
//			    List programList=systemProgramService.findSystemPrograms();
//			    List semesterList=this.iSemesterService.findSemesters();
//			  
//			    
//			    Collection  tcListCollection= new ArrayList();  
//			    Collection  programListCollection=new ArrayList();
//			    Collection  semesterListCollection=new ArrayList();
//				//获取当前用户
//		  		User user = null;
//		  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		  		
//			    
//			    /**
//			     * 学习中心列表
//			     */
//			    tcListCollection.add(new LabelValueBean("全部中心", null));
//			    if (tcList != null && tcList.size() > 0) {
//			      for (int i = 0; i < tcList.size(); i++) {	      
//			    	  TutorCenter tutorCenter=(TutorCenter)tcList.get(i); 
//			          String tcID=tutorCenter.getTcId()+"";
//			          tcListCollection.add(new LabelValueBean(tutorCenter.getTcTitle(),tcID.trim()));
//			      }
//			    }
//			    /**
//			     * 专业层次列表
//			     */
//			    programListCollection.add(new LabelValueBean("全部层次", null));
//			    if(programList!=null&&programList.size()>0){
//			    	for(int i=0;i<programList.size();i++){
//			    		Program program=(Program)programList.get(i);
//			    		String programID=program.getProgramId()+"";
//			    		programListCollection.add(new LabelValueBean(program.getProgramName(),programID.trim()));
//			    	}
//			    }
//			    /**
//			     * 设置学期
//			     */
//			    semesterListCollection.add(new LabelValueBean("全部学期",null));
//			    if(semesterList!=null&&semesterList.size()>0)
//			    {
//			    	for(int i=0;i<semesterList.size();i++)
//			    	{
//			    	Semester semester=(Semester)semesterList.get(i);
//			    	String semesterId=semester.getSemesterId()+"";
//			    	semesterListCollection.add(new LabelValueBean(semester.getSemesterTitle(),semesterId.trim()));
//			    	}
//			    }
//			    
//			    Collection studentStatusCollection = new ArrayList();
//			    studentStatusCollection.add(new LabelValueBean("所有的状态",null));
//			    studentStatusCollection.add(new LabelValueBean("注册未录取", "1"));
//			    studentStatusCollection.add(new LabelValueBean("被录取", "2"));
//			    studentStatusCollection.add(new LabelValueBean("在学", "3"));
//			    studentStatusCollection.add(new LabelValueBean("退学", "4"));
//			    studentStatusCollection.add(new LabelValueBean("毕业", "5"));
//			    studentStatusCollection.add(new LabelValueBean("流失", "6"));
//			    
//			    int tcId = userService.getTcId_Of_User(user);
//			    List tcs = tcService.getTutorCenterTree(tcId);
//			    Collection  tc = new ArrayList();
//			    if(tcId==1)tc.add(new LabelValueBean("全部学习中心", null));
//			    tc.addAll(tcs);
//			    
//			    request.setAttribute("tcCollection", tc);
//			    request.setAttribute("tcListCollection",tcListCollection);
//			    request.setAttribute("programListCollection",programListCollection);
//			    request.setAttribute("semesterListCollection",semesterListCollection);
//				request.setAttribute("studentStatusCollection", studentStatusCollection);
//		}
//		/**
//	     * 从数据库中提取退学学生的信息数据
//	     * @param request
//	     * @param startIndex
//	     * @param pageSize
//	     * @param _query
//	     * @param BTcId
//	     * @param ETcId
//	     */
//	    private void getLearnerInfo(HttpServletRequest request, int startIndex, int pageSize,HQuery _query){
//	    	PaginationSupport paginationSupport;
//	        paginationSupport = enrollManageService.findLearnerInfo(_query, pageSize, startIndex);
//	        TutorCenter tc=null;
//	        Semester se=null;
//	        Program pr=null;
//			if(paginationSupport == null){
//				request.setAttribute("requestChangeTcList", new ArrayList());
//				request.setAttribute("courseTotalCount", new Integer(0));			
//			}
//			else{
//				List studentList = paginationSupport.getItems();
//				Iterator item=studentList.iterator();
//				List learnerList=new ArrayList();
//				while(item.hasNext())
//				{
//					Object[] itemInfo=(Object[])item.next();
//					LearnerForm learnerForm = new LearnerForm();
//					Learner learner=(Learner)itemInfo[0];
//					learnerForm.setLearner(learner);
//					learnerForm.setUser((User)itemInfo[1]);
//					if(learner.getTcId() == null){
//						learnerForm.setLearner(learner);
//						learnerForm.setTcCenterName("");
//						learnerForm.setSemesterName("");
//						learnerForm.setProgramName("");
//						learnerForm.setSpecialityName("");
//					}
//					else{
//						learnerForm.setLearner(learner);
//						tc=tcService.getSystemTutorCenter(learner.getTcId());
//						if(tc!=null){
//							learnerForm.setTcCenterName(tc.getTcTitle());
//						}else{
//							learnerForm.setTcCenterName("");
//						}
//						se=semesterService.getSemester(learner.getSemesterId());
//						if(se!=null){
//							learnerForm.setSemesterName(se.getSemesterTitle());
//						}else{
//							learnerForm.setSemesterName("");
//						}
//						
//						pr=systemProgramService.getSystemProgram(learner.getProgramId());
//						if(pr!=null){
//							learnerForm.setProgramName(pr.getProgramName());
//						}else{
//							learnerForm.setProgramName("");
//						}
//						log.info("=======learner.getSpecialityId()2222============"+learner.getSpecialityId());
//						if(learner.getSpecialityId()==null||learner.getSpecialityId().equals(new Integer(0))){
//							log.info("=======learner.getSpecialityId()============"+learner.getSpecialityId());
//							learnerForm.setSpecialityName("尚未选择专业方向");
//							
//						}
//						else {
//						
//							learnerForm.setSpecialityName(courseService.getSpeciality(learner.getSpecialityId()).getSpecialityTitle());
//						}
//					
//					
//					
////					learnerForm.setTcCenterName((String)itemInfo[2]);
////					learnerForm.setProgramName((String)itemInfo[3]);
////					learnerForm.setSemesterName((String)itemInfo[4]);
////					if(((Learner)itemInfo[0]).getSpecialityId()!=null)
////					{
////						learnerForm.setSpecialityName(courseService.getSpeciality(((Learner)itemInfo[0]).getSpecialityId()).getSpecialityTitle());	
////					}
////					else
////					{
////						
////					}
//					}
//			    learnerList.add(learnerForm);
//				}
//				int learnerTotalCount = paginationSupport.getTotalCount();
//				request.setAttribute("learnerList", learnerList);
//				request.setAttribute("learnerTotalCount", new Integer(learnerTotalCount));			
//			}
//	    }
//	    
//	    
//	    public ActionForward queryList(ActionMapping mapping, ActionForm form,
//	    	HttpServletRequest request, HttpServletResponse response){
//			//获取当前用户
//	  		User user = null;
//	  		user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//	  		
//	    	int tcId = ParamUtils.getIntParameter(request, "tcId", 0);			
//			int	pid = ParamUtils.getIntParameter(request, "programId", 0);
//			int	sid = ParamUtils.getIntParameter(request, "semesterId", 0);
//			String userName = ParamUtils.getParameter(request,"userName");
//			String enrollStatus = ParamUtils.getParameter(request,"enrollStatus");
//
//		    int uTcId = userService.getTcId_Of_User(user);
//		    if(uTcId==1){
//		    }else{
//		    Integer tcIdInScope = tcService.tcIdInMyScope(new Integer(tcId), new Integer(uTcId));
//		    tcId = tcIdInScope.intValue();
//		    }
//		    
//			String where = "";
//			List list = new ArrayList();
//			
//			if (pid > 0){
//				where += " and l.programId = ? ";
//				list.add(new Integer(pid));
//			}
//			if (tcId > 0){
//				where += " and l.tcId = ? ";
//				list.add(new Integer(tcId));
//			}
//			if(sid > 0){
//				where += " and l.semesterId = ? ";
//				list.add(new Integer(sid));
//			}
//			if(enrollStatus.length()>0){
//				where += " and l.enrollStatus = ? ";
//				list.add(enrollStatus);
//			}
//			if(userName.trim().length() > 0){
//				where += " and u.loginName like ? ";
//				list.add("%"+userName.trim()+"%");
//			}
//			
//			int rows = 15;
//			int offset = ParamUtils.getIntParameter(request, "offset", 0);
//			int count = userService.findLearner(where, list);
//
//			List l = userService.findLearner(offset, rows, where, list);
//			
//			request.setAttribute("list", l);
//			request.setAttribute("count", new Integer(count));
//			request.setAttribute("rows", new Integer(rows));
//			initList(request, response);
//			
//	    	return mapping.findForward("queryList");
//	    }
		
}
