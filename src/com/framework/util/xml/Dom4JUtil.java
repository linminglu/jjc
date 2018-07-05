/*
 * Created on 2005-8-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.util.xml;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.framework.util.LogUtil;
import com.ram.RamConstants;

/**
 * @author liuguojun
 * createTime 
 * 2005-8-15 
 * commpany bpc 
 * project newPlat
 */
public final class Dom4JUtil {
	private static Document document = null;

	private static Element rootElment = null;

	private static Dom4JUtil dom4j = null;
	
	/**
	 * xml文件的路径
	 */
	public  String filePath = null;
	
	/**
	 * 要parse的节点的命名字例如learner/select
	 */
	public  String parsePath = "/WEB-INF/classes/select.xml";

	/**
	 * gain a instance
	 * 
	 * @return
	 */
	public static Dom4JUtil getInstance() {
		if (dom4j == null) {
			dom4j = new Dom4JUtil();
		}

		return dom4j;
	}

	/**
	 * gain document
	 * 
	 * @return Document
	 */
	public static Document getDocument() {
		SAXReader saxReader = new SAXReader();
		try {
			if (document == null) {
				document = saxReader.read(dom4j.filePath);
			}
		} catch (DocumentException e) {
			LogUtil.getLogger().error("获取document文档时发生错误", e);
		}
		return document;
	}

	/**
	 * gain rootElment
	 * 
	 * @return Element
	 */
	public static Element getRootElement() {
		try {
			rootElment = getDocument().getRootElement();
		} catch (Exception e) {
			LogUtil.getLogger().error("获取rootElment文档时发生错误", e);
		}
		return rootElment;
	}

	//	public static void main(String[] args) {
	//	}

	/**
	 * @return Returns the filePath.
	 */
	public  String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 * The filePath to set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return Returns the parsePath.
	 */
	public  String getParsePath() {
		return parsePath;
	}
	/**
	 * @param parsePath The parsePath to set.
	 * 存放的格式是这样的:learner/select
	 */
	public  void setParsePath(String parsePath) {
		this.parsePath = RamConstants.getWebappContext()+"/"+parsePath;
	}
}
