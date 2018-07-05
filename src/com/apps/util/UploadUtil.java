package com.apps.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.struts.upload.FormFile;
import org.jfree.util.Log;

import com.apps.Constants;

/**
 * 文件操作类
 * 
 * @author Simon
 * 
 */
public class UploadUtil {

	/**
	 * 上传头像
	 * 
	 * @param formFile
	 * @param savePath
	 * @param fileName
	 * @return 文件保存的虚拟路径
	 */
	public static String uploadOneFile(FileItem formFile, String savePath,
			String fileName) {
		if (formFile == null) {
			Log.error("没有要上传的文件！");
			return null;
		}
		String actualPath = Constants.getWebRootPath()
				+ Constants.getFileUploadPath();// 本地存储的实际目录
		String fileFolder = actualPath + getFolder(savePath);// 文件存储的实际目录
		String xuniPath = Constants.getFileUploadPath() + getFolder2(savePath)
				+ "/" + fileName;// 虚拟目录
		try {
			InputStream in = formFile.getInputStream();
			File saveFile = new File(fileFolder);
			if (!saveFile.isDirectory()) {
				saveFile.mkdirs();
			}
			OutputStream out = new FileOutputStream(Constants.getWebRootPath()
					+ xuniPath);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.error("上传文件失败！");
		}
		return xuniPath;
	}

	/**
	 * 上传单个文件
	 * 
	 * @param formFile
	 * @param savePath
	 * @return 文件保存的虚拟路径
	 */
	public static String uploadOneFile(FormFile formFile, String savePath) {
		if (formFile == null) {
			Log.error("没有要上传的文件！");
			return null;
		}
		String fileName = formFile.getFileName();
		String actualPath = Constants.getWebRootPath()
				+ Constants.getFileUploadPath();// 本地存储的实际目录
		String fileFolder = actualPath + getFolder(savePath);// 文件存储的实际目录
		String xuniPath = Constants.getFileUploadPath() + getFolder(savePath)
				+ "/" + GetUniqueID() + getFileExt(fileName);// 虚拟目录
		try {
			InputStream in = formFile.getInputStream();
			File saveFile = new File(fileFolder);
			if (!saveFile.isDirectory()) {
				saveFile.mkdirs();
			}
			OutputStream out = new FileOutputStream(Constants.getWebRootPath()
					+ xuniPath);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			in.close();
			System.out.println(">>>>>>>>>>>>>>>");
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.error("上传文件失败！");
		}
		return xuniPath;
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	public static String getFolder2(String path) {
		SimpleDateFormat formater1 = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		path += "/" + formater1.format(date);
		File dir = new File(Constants.getWebRootPath()
				+ Constants.getFileUploadPath() + path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return "";
			}
		}
		return path;
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	public static String getFolder(String path) {
		SimpleDateFormat formater1 = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		path += "/" + formater1.format(date) + "/" + formater.format(date);
		// path += "/"+formater1.format(date) ;
		File dir = new File(Constants.getWebRootPath()
				+ Constants.getFileUploadPath() + path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return "";
			}
		}
		return path;
	}

	/**
	 * 得到一个随机的字符串是(从时间取得)
	 * 
	 * @return String
	 */
	public static String GetUniqueID() {
		SimpleDateFormat dateform = new SimpleDateFormat("yyyyMMddHHmmhhSSS");// notice
																				// here
		GregorianCalendar calendar = new GregorianCalendar();
		String s = dateform.format(calendar.getTime());
		return s;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	// 上传单个文件
	public static String uploadOneFile(FormFile formFile, String savePath,
			String saveName) {
		String uploadMess = null;
		if (formFile == null) {
			uploadMess = "没有取得要上传的文件！";
			return uploadMess;
		}
		String fileName = saveName;
		if (fileName == null || fileName.equals("")) {
			fileName = formFile.getFileName();
		}
		try {
			InputStream in = formFile.getInputStream();
			File saveFile = new File(savePath + "/" + fileName);
			if (saveFile.exists()) {
				saveFile.delete();
			}
			OutputStream out = new FileOutputStream(saveFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			uploadMess = fileName + "文件上传失败！请重试！";
			return uploadMess;
		}
		return null;
	}

	// 下载文件，以zip压缩格式 (下载的文件后缀.csv)
	public static String downLoadZipCsvFile(String filePath, String fileName,
			String saveName, String reportTitle, HttpServletResponse m_response) {
		String downLoadMess = null;
		File file = new File(filePath + "/" + fileName);
		if (!file.exists()) {
			downLoadMess = "文件不存在！";
			return downLoadMess;
		}
		try {
			m_response.setContentType("application/x-msdownload");
			saveName += ".zip";
			String saveDownLoadName = URLEncoder.encode(saveName, "UTF-8");
			m_response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + saveDownLoadName);
			LineNumberReader fReader = new LineNumberReader(
					new FileReader(file));
			PrintWriter fWriter;
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					m_response.getOutputStream()));
			out.setMethod(ZipOutputStream.DEFLATED);
			fWriter = new PrintWriter(out);
			String savesplitfilename = "";
			long lineCount = 0;
			long splitcount = 1;
			while (true) {
				savesplitfilename = "";
				if (String.valueOf(splitcount).length() == 1)
					savesplitfilename += "00" + splitcount;
				else if (String.valueOf(splitcount).length() == 2)
					savesplitfilename += "0" + splitcount;
				else
					savesplitfilename += splitcount;
				savesplitfilename += fileName;
				ZipEntry entry = new ZipEntry(savesplitfilename);
				out.putNextEntry(entry);
				if (reportTitle != null && !"".equals(reportTitle)) {
					fWriter.println(reportTitle);
				}
				String bufferLine = null;
				while ((bufferLine = fReader.readLine()) != null) {
					fWriter.println(bufferLine);
					lineCount++;
					if (lineCount > 65500) {
						lineCount = 0;
						break;
					}
				}
				fWriter.flush();
				splitcount++;
				if (bufferLine == null) {
					break;
				}
			}
			fReader.close();
			fReader = null;
			fWriter.close();
			fWriter = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			downLoadMess = ex.getMessage();
		}
		return downLoadMess;
	}

	public static String downLoadZipCsvFileAddCol(String filePath,
			String fileName, String saveName, String reportTitle,
			String colName, HttpServletResponse m_response) {
		String downLoadMess = null;
		File file = new File(filePath + "/" + fileName);
		if (!file.exists()) {
			downLoadMess = "文件不存在！";
			return downLoadMess;
		}
		try {
			m_response.setContentType("application/x-msdownload");
			saveName += ".zip";
			String saveDownLoadName = URLEncoder.encode(saveName, "UTF-8");
			m_response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + saveDownLoadName);
			LineNumberReader fReader = new LineNumberReader(
					new FileReader(file));
			PrintWriter fWriter;
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					m_response.getOutputStream()));
			out.setMethod(ZipOutputStream.DEFLATED);
			fWriter = new PrintWriter(out);
			String savesplitfilename = "";
			long lineCount = 0;
			long splitcount = 1;
			while (true) {
				savesplitfilename = "";
				if (String.valueOf(splitcount).length() == 1)
					savesplitfilename += "00" + splitcount;
				else if (String.valueOf(splitcount).length() == 2)
					savesplitfilename += "0" + splitcount;
				else
					savesplitfilename += splitcount;
				savesplitfilename += fileName;
				ZipEntry entry = new ZipEntry(savesplitfilename);
				out.putNextEntry(entry);
				if (reportTitle != null && !"".equals(reportTitle)) {
					fWriter.println(reportTitle);
				}
				if (colName != null && !"".equals(colName)) {
					fWriter.println(colName);
				}
				String bufferLine = null;
				while ((bufferLine = fReader.readLine()) != null) {
					fWriter.println(bufferLine);
					lineCount++;
					if (lineCount > 65500) {
						lineCount = 0;
						break;
					}
				}
				fWriter.flush();
				splitcount++;
				if (bufferLine == null) {
					break;
				}
			}
			fReader.close();
			fReader = null;
			fWriter.close();
			fWriter = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			downLoadMess = ex.getMessage();
		}
		return downLoadMess;
	}

	// 下载文件，以普通格式
	public static void downLoadNormalFile(String filePath, String fileName,
			String saveFileName, HttpServletResponse m_response) {

		File file = new File(filePath + "/" + fileName);
		if (!file.exists()) {
			return;
		}
		if (saveFileName == null || "".equals(saveFileName.trim())) {
			saveFileName = fileName;
		}
		try {
			saveFileName = URLEncoder.encode(saveFileName, "UTF-8");
			m_response.setContentType("application/x-msdownload");
			m_response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + saveFileName);
			ServletOutputStream sos = null;
			FileInputStream fis = null;
			byte[] b = new byte[1024];
			fis = new FileInputStream(file);
			sos = m_response.getOutputStream();
			int len = 0;
			while ((len = fis.read(b)) > 0) {
				sos.write(b, 0, len);
			}
			sos.close();
			fis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 上传单个文件
	 * 
	 * @param formFile
	 * @param savePath
	 * @return 文件保存的虚拟路径
	 */
	public static String uploadOneFile_(FormFile formFile, String savePath) {
		if (formFile == null) {
			Log.error("没有要上传的文件！");
			return null;
		}
		String fileName = formFile.getFileName();
		String name = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
		name = "ddhl" + name;
		String xuniPath = savePath + "/" + name;
		try {
			InputStream in = formFile.getInputStream();
			File saveFile = new File(savePath);
			if (!saveFile.isDirectory()) {
				saveFile.mkdirs();
			}
			OutputStream out = new FileOutputStream(Constants.getWebRootPath()
					+ xuniPath);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.error("上传文件失败！");
		}
		return xuniPath;
	}
	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	public static String getFolderYYYYMM(String path) {
		SimpleDateFormat formater1 = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		path += "/" + formater1.format(date);
		File dir = new File(Constants.getWebRootPath()
				+ Constants.getFileUploadPath() + path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return "";
			}
		}
		return path;
	}
}
