package com.apps.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class HtmlStatic {
	public final static Log log = LogFactory.getLog(HtmlStatic.class);

	/**
	 * 生成静态文件
	 * 
	 * @param destDir
	 *            目标文件存储路径
	 * @param htmlName
	 *            文件名
	 * @param templateDir
	 *            模版路径
	 * @param templateFile
	 *            模版名
	 * @param map
	 *            数据集合
	 * @return
	 */
	public static boolean staticHtml(String destDir, String htmlName,
			String templateDir, String templateFile, Map<String, Object> map) {
		boolean ylepath = false;
		Properties prop = new Properties();
		try {
			prop.put("file.resource.loader.path", templateDir);// 配置模版目录
			prop.load(HtmlStatic.class.getResourceAsStream(
					"/velocity.properties"));
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}

		StringBuffer str = new StringBuffer();
		FileOutputStream outStream = null;
		OutputStreamWriter writer = null;
		try {
			Velocity.init(prop);// 初始化
			createDirectory(destDir);
			VelocityContext context = new VelocityContext();
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				context.put(key, map.get(key));
			}
			// load applicationResource file
			// Enumeration<String> appSet = AppResoruce.getKeys();
			// while(appSet.hasMoreElements()){
			// String key = appSet.nextElement();
			// context.put(key,AppResoruce.get(key));
			// }
			Template template = Velocity.getTemplate(templateFile);
			// 获得模版; 注意,是在properties里面已经配好的模版目录下
			File destFile = new File(destDir, htmlName);
			outStream = new FileOutputStream(destFile);
			writer = new OutputStreamWriter(outStream, "UTF-8");
			BufferedWriter sw = new BufferedWriter(writer);
			template.merge(context, sw);
			sw.flush();
			sw.close();
			writer.close();
			outStream.close();
			ylepath = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				writer = null;
			}
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				outStream = null;
			}
		}
		return ylepath;
	}

	public static void createDirectory(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			// 文件不存在，创建
			file.mkdirs();
		}
	}
}
