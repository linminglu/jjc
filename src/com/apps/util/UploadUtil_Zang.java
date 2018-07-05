package com.apps.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.jspsmart.upload.SmartUpload;
import com.ram.model.User;

public class UploadUtil_Zang {

	SmartUpload smartUpload = new SmartUpload();

	private String uploadPath;
	private Map uploadFiles;
	private String Msg;

	// 用户获取用户的id，命名用户logo，如果用时间命名的话，可删去
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 初始化参数
	 * 
	 * @param pageContext
	 * @return
	 */
	public boolean initParam(PageContext pageContext, HttpServletRequest request) {

		String fileSizeStr = "";
		String allowedFileTypeStr = "";
		//
		try {
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "upload\\";
			smartUpload.initialize(pageContext);
			this.uploadPath = path;
			fileSizeStr = "1024";
			allowedFileTypeStr = "jpg,gif,png,bmp,tif";
			int fileSizeLimit = Integer.parseInt(fileSizeStr);
			smartUpload.setMaxFileSize(fileSizeLimit * 1024);
			smartUpload.setAllowedFilesList(allowedFileTypeStr);
			smartUpload.upload();
			// 此两句是为了获得用户id//有的浏览器是获取不到的，也就是session失效，那就用时间命名临时文件
			User user = (User) request.getSession(true).getAttribute(
					"loginUser");
			if (user != null) {
				userId = user.getUserId().toString();
			}

			//
		} catch (SecurityException e) {
			if (e.getMessage().contains("1010")) {
				this.setMsg("上传文件失败：允许上传文件的类型：" + allowedFileTypeStr + "，错误信息："
						+ e.getMessage());

			} else if (e.getMessage().contains("1105")) {
				this.setMsg("上传文件失败：文件大小不能超过" + fileSizeStr + "M" + "，错误信息："
						+ e.getMessage());
			} else {
				this.setMsg(e.getMessage());
			}

			return false;
		} catch (Exception e) {
			this.setMsg("初始化上传组件参数失败：" + e.getMessage());
			e.printStackTrace();
			return false;

		}
		return true;
	}

	/**
	 * 上传文件，上传成功后将相对路径和文件名放在map中
	 * 
	 * @param type
	 *            上传文件的的类型：用户头像,企业logo,图片
	 * @return true： 成功，false：失败
	 */
	public boolean uploadFile(String type) {

		Map uploadFiles = new HashMap();
		try {

			for (int i = 0; i < smartUpload.getFiles().getCount(); i++) {
				com.jspsmart.upload.File file = smartUpload.getFiles().getFile(
						i);
				String orderPath = "";
				if (file.getFilePathName() != null) {
					if (file.getFilePathName().trim().equals("")) {
						continue;
					}
				}
				String dbPath = "";
				if (!"".equals(type))// 传递类型，以类型名称作为目录进行创建
				{
					dbPath = File.separator + type + File.separator
							+ this.getCurrentYear() + File.separator
							+ this.getCurrentMonth() + File.separator;

				} else// 没有传递类型，读取临时文件路径
				{
					dbPath = File.separator + "tmp" + File.separator;
				}

				orderPath = uploadPath + dbPath;
				File orderDir = new File(orderPath);
				if (!orderDir.exists()) {
					if (!orderDir.mkdirs()) {
						this.setMsg("创建文件夹[" + orderPath + "]失败...");
						return false;
					}
				}
				String newFileName = null;
				if (userId != null) {// 如果可以获取到用户id就用用户id命名临时文件，方便删除，如果没有的话就用时间命名
					newFileName = userId + "." + file.getFileExt();
				} else {
					// 用当前时间命名用户logo
					newFileName = randomFileName() + "." + file.getFileExt();
				}
				file.saveAs(orderPath + newFileName);
				uploadFiles.put(dbPath + newFileName, file.getFileName());
			}
			this.setUploadFiles(uploadFiles);

		} catch (Exception e) {
			this.setMsg("上传失败:" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 产生一个当时时间的文件名
	 * 
	 * @return
	 */
	public String randomFileName() {
		Date currTime = new Date();
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddhhmmssS",
				Locale.US);
		String fileName = "";
		try {
			fileName = new String(
					(formatter2.format(currTime)).getBytes("iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fileName;

	}

	public String getParameter(String str) {
		return smartUpload.getRequest().getParameter(str);

	}

	public Map getUploadFiles() {
		return uploadFiles;
	}

	private void setUploadFiles(Map uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}

	public String getCurrentMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(new Date());

	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

}
