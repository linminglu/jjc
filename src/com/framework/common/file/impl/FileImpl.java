package com.framework.common.file.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.common.file.IFile;
import com.framework.exception.business.FilesFileException;
import com.framework.exception.business.NoExistFileException;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author yangjy
 * @version 1.0
 */
public class FileImpl implements IFile {

	/** log */
	private static Log log = LogFactory.getLog(FileImpl.class);

	/**
	 * 建立目录
	 * 
	 * @param folder
	 *            String 目录名称
	 * @throws FilesException
	 * @throws NoExistException
	 */
	public void createFolder(String folder) throws FilesFileException,
			NoExistFileException {

		if (folder != null && !folder.equals("")) {
			boolean success = true;
			File fileDirectory = new File(folder);
			if (!fileDirectory.exists()) {
				success = fileDirectory.mkdirs();
			}
			fileDirectory = null;
			if (!success) {
				FilesFileException ex = new FilesFileException(
						String.valueOf(String
								.valueOf((new StringBuffer("文件目录：")).append(
										folder).append("没有创建成功"))));
				throw ex;
			} else {
				return;
			}
		} else {
			log.info("输入目录为空！");
			NoExistFileException ne = new NoExistFileException("输入目录为空");
			throw ne;
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param folder
	 *            String
	 * @throws FilesException
	 */
	public void deleteFolder(String folder) throws FilesFileException {
		boolean success = true;
		File fileDirectory = new File(folder);
		if (fileDirectory.exists()) {
			fileDirectory.delete();
		}
		if (!success) {
			FilesFileException ex = new FilesFileException(
					String.valueOf(String.valueOf((new StringBuffer("文件目录："))
							.append(folder).append("没有删除成功"))));
			throw ex;
		} else {
			return;
		}
	}

	/**
	 * 建立文件
	 * 
	 * @param folder
	 *            String 目录名
	 * 
	 * @param fileName
	 *            String 文件名
	 * 
	 * @param htmlText
	 *            String 内容
	 * @throws FilesException
	 * @throws NoExistException
	 * @throws IOException
	 */
	public void createFile(String folder, String fileName, String text)
			throws FilesFileException, NoExistFileException, IOException {
		if (fileName == null || fileName.equals("")) {

			throw new FilesFileException("文件路径为空");
		} else {
			createFolder(folder);
			// 建立一个文件 文件名为fileName
			String newFileName = folder + fileName;
			File file = new File(newFileName);
			log.debug("url===" + file.toURI());

			if (file.exists()) {
				log.debug("原文件已被覆盖");
			}
			// file对象建立一个新文件
			file.createNewFile();
			// new一个文件些对象
			FileWriter fileWriter = new FileWriter(newFileName);
			Writer writer = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(writer);
			StringBuffer sbuffer = new StringBuffer(text);
			printWriter.print(sbuffer);
			writer.close();
			printWriter.close();
			fileWriter.close();
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param srcFileName
	 *            String 要移动的文件
	 * @param desFolder
	 *            String 要移动到的目标目录
	 * 
	 * @throws FilesException
	 * @throws NoExistException
	 */
	public void moveFile(String srcFileName, String desFolder)
			throws FilesFileException, NoExistFileException {
		boolean success = true;
		File srcFile = new File(srcFileName);
		createFolder(desFolder);
		String fileName = srcFile.getName();
		String desFileName = String.valueOf(String.valueOf((new StringBuffer(
				String.valueOf(String.valueOf(desFolder)))).append(
				File.separatorChar).append(fileName)));
		File desFile = new File(desFileName);
		srcFile.renameTo(desFile);
		if (!success) {
			FilesFileException ex = new FilesFileException(
					String.valueOf(String.valueOf((new StringBuffer("文件："))
							.append(srcFile).append("没有拷贝成功"))));
			throw ex;
		} else {
			return;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param srcFileName
	 *            String
	 * @throws FilesException
	 */
	public void deleteFile(String srcFileName) throws FilesFileException {
		boolean success = true;
		File file = new File(srcFileName);
		if (file.exists()) {
			file.delete();
		}
		if (!success) {
			FilesFileException ex = new FilesFileException(
					String.valueOf(String.valueOf((new StringBuffer("文件："))
							.append(srcFileName).append("没有删除成功"))));
			throw ex;
		} else {
			return;
		}
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param fileName
	 *            文件名
	 * 
	 * @return
	 */
	public String getFileExt(String fileName) {
		if (fileName == null) {
			return "";
		}
		int pos = fileName.lastIndexOf(".");
		if (pos == -1) {
			return "";
		}
		return fileName.substring(pos + 1);
	}

	/**
	 * filePath路径下是否存在文件file
	 * 
	 * @param filePath
	 *            文件路径
	 * @param file
	 *            文件名
	 * 
	 * @return boolean
	 */
	public boolean isFileExist(String filePath, String file) {
		if (filePath == null || file == null) {
			return false;
		}
		String absPath = null;
		if (filePath.endsWith("/") || filePath.endsWith("\\")) {
			absPath = filePath + file;
		} else {
			absPath = filePath + File.separator + file;
		}
		return (new File(absPath)).exists();
	}

	public static void main(String agr[]) {
		
//		System.out.println(readDate("d:\\sssss.txt"));
//		FileImpl f = new FileImpl();
		// 得到文件名后缀
		// log.debug(f.getFileExt("fs.tss"));
		// 文件是否存在于某个目录中
		// log.debug(f.isFileExist("c:\\moveFile\\", "out.txt"));
		try {
			// f.moveFile("C:\\zz\\1\\out.txt", "c:\\moveFile\\");
			// f.deleteFile("c://moveFile//out.txt");
			// f.createFile("c://moveFile//", "zz.txt", "tamade");
//			f.createFolder("c://moveFile//case");
		} catch (Exception e) {
		}
	}

	public static String readDate(String filenameTemp) {
		// 定义一个待返回的空字符串
		String strs = "";
		try {
			FileReader read = new FileReader(new File(filenameTemp));
			StringBuffer sb = new StringBuffer();
			char ch[] = new char[1024];
			int d = read.read(ch);
			while (d != -1) {
				String str = new String(ch, 0, d);
				sb.append(str);
				d = read.read(ch);
			}
//			System.out.print(sb.toString());
			String a = sb.toString();
//			String a = sb.toString().replaceAll("@@@@@", ",");
			strs = a.substring(0, a.length() - 1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strs;
	}

}
