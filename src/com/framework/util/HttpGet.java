package com.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * @author MagicLiao
 * @version 1.0
 */
public class HttpGet {

	protected final static Log log = LogFactory.getLog("");

//	private static int BUFFER_SIZE = 8096;// 缓冲区大小

	private List vDownLoad = new ArrayList();// URL列表

	private Vector vFileList = new Vector();// 下载后的保存文件名列表

	/**
	 * 构造方法
	 * 
	 * 
	 */
	public HttpGet() {
	}

	/**
	 * 清除下载列表
	 */
	public void resetList() {
		vDownLoad.clear();
		vFileList.clear();
	}

	/**
	 * 增加下载列表项
	 * 
	 * 
	 * 
	 * @param url
	 *            String
	 * @param filename
	 *            String
	 */

	public void addItem(String url, String filename) {
		vDownLoad.add(url);
		vFileList.add(filename);
	}

	/**
	 * * 将数组中的文件压缩到一个文件中进行下载
	 * 
	 * @param fileURLList
	 *            需要被下载的文件的文件URL集合
	 * @param fileServerRealPath
	 *            输出压缩文件的文件服务器物理路径
	 * @param outputZipFileName
	 *            输出的压缩文件的名称
	 * @return 压缩以后的文件路径
	 */
	public String downloadZipFile(String fileServerRealPath,
			String outputZipFileName) {
		String outputZipFilePath = "";
		String outputZipFileURL="";
		boolean haveFile=false;//判断所压缩的包中，是否存在文件
		boolean haveError=false;//表示压缩过程没有错误
		StringBuffer sbErr=new StringBuffer();
		try {
			//输出的zip文件路径
			outputZipFileURL= "/file_download/zuoye/" + outputZipFileName + ".zip";
			outputZipFilePath = fileServerRealPath + outputZipFileURL;
			
			log.info("输出的zip文件路径outputZipFilePath=" + outputZipFilePath);
//			File zipfile = new File(outputZipFilePath);


			FileOutputStream fileOut = new FileOutputStream(outputZipFilePath);
			
			ZipOutputStream outputStream = new ZipOutputStream(fileOut);
			File fileIntoZip = null;
			String fileRealPath = "";
			String fileName = "";
			log.info("vDownload.size=" + vDownLoad.size());
			
			for (int loop = 0; loop < vDownLoad.size(); loop++) {
				try {
					fileRealPath = fileServerRealPath + vDownLoad.get(loop).toString();
					log.info("file real address into zip,fileRealPath =" + fileRealPath);
					fileIntoZip = new File(fileRealPath);
					if(fileIntoZip.exists()){
						haveFile=true;
						log.info("被压缩的文件存在，开始压缩");
						fileName = this.vFileList.get(loop).toString();
						log.info("fileName in zip=" + fileName);
						FileInputStream fileIn = new FileInputStream(fileIntoZip);
						outputStream.putNextEntry(new ZipEntry(fileName));
						byte[] buffer = new byte[1024];
						while (fileIn.read(buffer) != -1) {
							outputStream.write(buffer);
						}
						outputStream.closeEntry();
						fileIn.close();
					}else{
						log.info("被压缩的文件不存在!");
						haveError=true;
						sbErr.append("教师下载学生的作业文件不存在,")
							.append("\n")
							.append("请检查数据库中记录的路径和物理路径是否匹配!")
							.append("\n")
							.append("作业文件路径：")
							.append(fileRealPath)
							.append("\n");						
					}
				} catch (Exception ex) {
					log.error("压缩然后下载作业文件的时候出错："+ex.getMessage());
				}
			}
			//如果还没有人提交作业
			if(!haveFile){
				fileIntoZip=new File(fileServerRealPath + "/AssignementNotExist.txt");
				fileIntoZip.createNewFile();
				fileName="There is no file exist now!Maybe students have not submit their assignments!";
				FileInputStream fileIn = new FileInputStream(fileIntoZip);
				outputStream.putNextEntry(new ZipEntry(StringUtil.convertToChinese(fileName)));
				byte[] buffer = new byte[1024];
				while (fileIn.read(buffer) != -1) {
					outputStream.write(buffer);
				}
				outputStream.closeEntry();
				fileIn.close();
			}
			outputStream.close();
		} catch (IOException ioe) {
			log.error("对文件打包进行下载的时候出现错误.err=" + ioe.getMessage());
		}
		
//		if(haveError){
//			SendMailUtil.MailToTechDepartment(sbErr.toString(),request);
//		}
		
		return outputZipFileURL;
		
	}

}