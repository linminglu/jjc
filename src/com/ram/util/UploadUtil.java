package com.ram.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ram.service.userLog.IUserLogService;
import com.framework.common.file.IFile;
import com.framework.common.file.impl.FileImpl;
import com.framework.common.properties.IReadProperties;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;

/**
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * @author yangjy
 */
public class UploadUtil extends BaseDispatchAction {
	protected final static Log log = LogFactory.getLog("");




	private IUserLogService userLogService = (IUserLogService) getService("userLogService");

	private IReadProperties readService = (IReadProperties) getService("readProperties");

	IFile ifile = new FileImpl();

	// 上传文件的基础目录
	private String uploadBasePath = readService
			.getValue("SYSTEM.FILESERVER.PATH")
			+ readService.getValue("FILEUPLOAD.FOLDER");

	// 临时文件目录
	private String tempPath = readService.getValue("SYSTEM.FILESERVER.PATH")
			+ readService.getValue("FILEUPLOAD.FOLDER.TEMP");

	// 传文件的完整目录
	private String mappingPath = readService.getValue("FILEUPLOAD.PATH");

	// 上传文件的完整目录

	private String uploadPath = null;

	private UploadBean uploadBean = new UploadBean();

	private int maxSize = Integer.parseInt(readService
			.getValue("FILEUPLOAD.MAXSIZE")) * 1024 * 1024;

	// private User user = null;

	public UploadUtil(String urlPath, String fileName) {
		if (urlPath != null && !urlPath.equals("")) {
			this.mappingPath = this.mappingPath + "/" + urlPath + "/";
			this.uploadPath = this.uploadBasePath + urlPath + "/";
		} else {
			log
					.error("UploadUtil(String arg0, String arg1) param arg0 is null");
		}
	}

	public UploadBean doupload(HttpServletRequest request, String title) {
		try {
			log.info("调用该方法的页面是：" + request.getRequestURI());
            log.info("UploadUtil.doupload----uploadPath=" + uploadPath);
			  log.info("ifile=" + ifile);
			   if(ifile==null){
			     ifile = new FileImpl();
			   }
			log.info("create Folder: uploadPath=" + uploadPath);
            ifile.createFolder(uploadPath);
			log.info("create Folder: tempPath=" + tempPath);
            ifile.createFolder(tempPath);
		} catch (Exception e) {
			log.error("***********create Folder failed*************");
			// e.printStackTrace();
		}
		try {

			DiskFileUpload fu = new DiskFileUpload();
			// 设置最大文件尺寸，从properties文件里读
			fu.setSizeMax(maxSize);
			// 设置缓冲区大小，这里是4kb
			fu.setSizeThreshold(4096);
			// 设置临时目录：

			fu.setRepositoryPath(this.tempPath);

			// 得到所有的文件：

			List fileItems = fu.parseRequest(request);

			Iterator iter = fileItems.iterator();
			// 依次处理每一个文件：
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// 忽略其他不是文件域的所有表单信息

				String attachName = "无标题";
				if (item.getFieldName().equals("attachName")) {
					if (item.getString() != null)
						attachName = item.getString();
					uploadBean.setAttachName(attachName);
				}
				if (!item.isFormField()) {
					String name = item.getName();
					name = name.substring(name.lastIndexOf("\\") + 1);
					uploadBean.setFilename(name);
					uploadBean.setSaveAsName(StringUtil.getSaveAsName());
					String type = name.substring(name.lastIndexOf("."), name
							.length());
					uploadBean.setUrl(this.mappingPath
							+ name);
					uploadBean.setFileExt(type);
					long size = item.getSize();
					uploadBean.setSize(size);
					uploadBean.setTitle(title);

					if ((name == null || name.equals("")) && size == 0) {
						continue;
					}
					item.write(new File(this.uploadPath
							+ name));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			uploadBean.setMessage("toBig");
		}
		return uploadBean;
	}

	/**
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public String deleteFile(String path, String fileName) {
		String result = "succeed";
		try {
			ifile.deleteFile(this.uploadBasePath + "/" + path + fileName);
		} catch (Exception e) {
			result = "failed";
			log.info("**" + this.uploadBasePath + path + fileName + "删除失败***");
		}
		return result;
	}

//	public void topicAttachmentSaveToDB(UploadBean uploadBean, Integer topicId,HttpServletRequest request) {
//		User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		
//		
//		TopicAttachment topicAttachment = new TopicAttachment();
//		topicAttachment.setFileType(uploadBean.getFileExt());
//		topicAttachment.setTopicId(topicId);
//		topicAttachment.setAttachementUrl(uploadBean.getUrl());
//		topicAttachment.setAttachementTitle(uploadBean.getFilename());
//		assignmentService.saveTopicAttachment(topicAttachment, user);
//	}

//	public void assignmentAttachmentSaveToDB(UploadBean uploadBean,
//			Integer assignmentId,HttpServletRequest request) {
//		User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		
//		AssignmentAttachment assignmentAttachment = new AssignmentAttachment();
//		assignmentAttachment.setAssignmentId(assignmentId);
//		assignmentAttachment.setAttachLink(uploadBean.getUrl());
//
//		String title = StringUtil.convertToUTF8(uploadBean.getTitle().trim());
//
//		// new String((uploadBean.getTitle()).getBytes("ISO-8859-1"), "UTF-8");
//		assignmentAttachment.setAttachTitle(title);
//		assignmentService.saveAssignmentAttachment(assignmentAttachment, user);
//	}

//	public void feedbackAttachmentSaveToDB(UploadBean uploadBean,
//			Integer assignmentFeedbackId,HttpServletRequest request) {
//		User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		
//		AssignmentFeedback assignmentFeedback = null;
//		if (assignmentFeedbackId != null && !("").equals(assignmentFeedbackId)) {
//			assignmentFeedback = scheduleAssignmentService
//					.getAssignmentFeedbackByPk(assignmentFeedbackId);
//		} else {
//			assignmentFeedback = new AssignmentFeedback();
//		}
//		assignmentFeedback.setFeedbackLinkAddress(uploadBean.getUrl());
//		scheduleAssignmentService.saveOrUpdateAssignmentFeedback(
//				assignmentFeedback, user);
//	}

	/**
	 * 上传作业到数据库
	 * 
	 * @param uploadBean
	 * @param learnerAssignmentForm
	 */
//	public void learnerAssignmentSaveToDB(UploadBean uploadBean,
//			LearnerAssignmentForm learnerAssignmentForm,
//			HttpServletRequest request) {
//        
//		LearnerAssignment la = new LearnerAssignment();
//
//		String scheduleAssignmentId = learnerAssignmentForm
//				.getScheduleAssignmentId();
//
//		la.setScheduleAssignmentId(Integer.valueOf(scheduleAssignmentId));
//        la.setCourseId(Integer.valueOf(learnerAssignmentForm.getCourseId()));
//		
////		if (la.getCourseId() != null && !("").equals(la.getCourseId())) {
////			la
////					.setCourseId(Integer.valueOf(learnerAssignmentForm
////							.getCourseId()));
////		} else {
////			la.setCourseId(Integer.valueOf(learnerAssignmentForm
////					.getCourseId()));
////		}
//
//		
//
//		la.setAssignmentStatus("1");
//		la.setSubmitUserId(new Integer(uploadBean.getUsetId()));
//		la.setAssignmentSerial(new Integer(learnerAssignmentForm
//				.getAssignmentSerial()));
//		la.setUpdateDateTime(new Date());
//		la.setSubmitAssignmentLink(uploadBean.getUrl());
//
//		LearnerAssignment obj = scheduleAssignmentService.getLearnerAssignmentBySerial(la.getSubmitUserId().intValue(),Integer.parseInt(scheduleAssignmentId), la.getCourseId().intValue(), la.getAssignmentSerial().intValue());
//
//		if (obj != null && obj.getLearnerAssignmentId() != null) {
//			la.setLearnerAssignmentId(obj.getLearnerAssignmentId());
//		}
//		la.setUpdateDateTime(DateTimeUtil.getNowSQLDate());
//
//		ScheduleAssignment sa = (ScheduleAssignment) scheduleAssignmentService
//				.getScheduleAssignment(la.getScheduleAssignmentId());
//		User user = (User) request.getSession().getAttribute("loginUser");
//		try {
//			scheduleAssignmentService.saveLearnerAssignment(la, user);
//			StringBuffer sb = new StringBuffer();
//			sb.append("上传作业成功！作业ID=");
//			sb.append(sa.getAssignmentId());
//			sb.append("作业名称：<");
//			sb.append(sa.getAssignmentTitleCh());
//			sb.append(">作业地址：");
//			sb.append(la.getSubmitAssignmentLink());
//			userLogService.saveLog(user, sb.toString());
//		} catch (Exception ex) {
//			userLogService.saveLog(user, "作业上传失败！作业名称<"+sa.getAssignmentTitleCh()+">");
//		}
//	}
	
	
	/**
	 * 上传作业到数据库
	 * 
	 * @param uploadBean
	 * @param learnerAssignmentForm
	 */
//	public void learnerAssignmentUpdateToDB(UploadBean uploadBean,
//			LearnerAssignmentForm learnerAssignmentForm,
//			HttpServletRequest request) {
//        
//		LearnerAssignment la = new LearnerAssignment();
//
//		String scheduleAssignmentId = learnerAssignmentForm
//				.getScheduleAssignmentId();
//		
//		String learnerAssignmentId = learnerAssignmentForm.getLearnerAssignmentId();
//
//
//		la.setLearnerAssignmentId(Integer.valueOf(learnerAssignmentId));
//		la.setScheduleAssignmentId(Integer.valueOf(scheduleAssignmentId));
//        la.setCourseId(Integer.valueOf(learnerAssignmentForm.getCourseId()));
//		
////		if (la.getCourseId() != null && !("").equals(la.getCourseId())) {
////			la
////					.setCourseId(Integer.valueOf(learnerAssignmentForm
////							.getCourseId()));
////		} else {
////			la.setCourseId(Integer.valueOf(learnerAssignmentForm
////					.getCourseId()));
////		}
//
//		
//
//		la.setAssignmentStatus("1");
//		la.setSubmitUserId(new Integer(uploadBean.getUsetId()));
//		la.setAssignmentSerial(new Integer(learnerAssignmentForm
//				.getAssignmentSerial()));
//		la.setUpdateDateTime(new Date());
//		la.setSubmitAssignmentLink(uploadBean.getUrl());
//
//		LearnerAssignment obj = scheduleAssignmentService.getLearnerAssignmentBySerial(la.getSubmitUserId().intValue(),Integer.parseInt(scheduleAssignmentId), la.getCourseId().intValue(), la.getAssignmentSerial().intValue());
//
//		if (obj != null && obj.getLearnerAssignmentId() != null) {
//			la.setLearnerAssignmentId(obj.getLearnerAssignmentId());
//		}
//		la.setUpdateDateTime(DateTimeUtil.getNowSQLDate());
//
//		ScheduleAssignment sa = (ScheduleAssignment) scheduleAssignmentService
//				.getScheduleAssignment(la.getScheduleAssignmentId());
//		User user = (User) request.getSession().getAttribute("loginUser");
//		try {
//			scheduleAssignmentService.saveLearnerAssignment(la, user);
//			StringBuffer sb = new StringBuffer();
//			sb.append("上传作业成功！作业ID=");
//			sb.append(sa.getAssignmentId());
//			sb.append("作业名称：<");
//			sb.append(sa.getAssignmentTitleCh());
//			sb.append(">作业地址：");
//			sb.append(la.getSubmitAssignmentLink());
//			userLogService.saveLog(user, sb.toString());
//		} catch (Exception ex) {
//			userLogService.saveLog(user, "作业上传失败！作业名称<"+sa.getAssignmentTitleCh()+">");
//		}
//	}

	//暂时注释
//	public void saveToBulletinAttachDB(UploadBean uploadBean, Integer bulletinId
//			,HttpServletRequest request) {
//		User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		// log.info("the AttachName is:"+uploadBean.getAttachName());
//		// log.info("the FileName is:"+uploadBean.getFilename());
//		// log.info("the BulletinId is:"+bulletinId);
//		BulletinAttach attach = new BulletinAttach();
//		attach.setFileType(uploadBean.getFileExt());
//		attach.setAttachId(null);
//		attach.setAttachName(uploadBean.getAttachName());
//		attach.setFileName(uploadBean.getFilename());
//		attach.setAttachUrl(uploadBean.getUrl());
//		attach.setBulletinId(bulletinId);
//		bulletinAttachService.saveBulletinAttach(attach, user);
//	}

//	public void saveEstudyAttachToDB(
//			UploadBean uploadBean, 
//			Integer estudyId,
//			HttpServletRequest request) {
//		
//		User user = ( User )request.getSession( false ).getAttribute( "loginUser" );
//		EstudyAttach estudyAttach = new EstudyAttach();
//		estudyAttach.setEstudyId(estudyId);
//		estudyAttach.setFileName(uploadBean.getFilename());
//		estudyAttach.setFileSize(new Long(uploadBean.getSize()));
//		estudyAttach.setFileType(uploadBean.getFileExt());
//		estudyAttach.setFileUrl(uploadBean.getUrl());
//		estudyAttach.setUpdateDateTime(DateTimeUtil.getCurrentDate());
////		estudyAttachService.createEstudyAttach(estudyAttach, user);
//	}

	public static void main(String[] args) {
		UploadUtil up = new UploadUtil("temp", null);
		up.deleteFile("moveFile/", "test.txt");
	}
}
