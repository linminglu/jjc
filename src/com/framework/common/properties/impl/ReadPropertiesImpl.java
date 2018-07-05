package com.framework.common.properties.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.framework.common.properties.IReadProperties;
import com.framework.util.StringUtil;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */

public class ReadPropertiesImpl implements IReadProperties{

  private static Log log = LogFactory.getLog(ReadPropertiesImpl.class);
  private Properties properties;

  /**
   * 构造函数
   */
  public ReadPropertiesImpl (){
    properties = new Properties();
    InputStream is = null;
    String configFilePath="/config.properties";
    try {
    	log.info("配置文件所在路径：" +getClass().getResource(configFilePath).getPath());
    	is = getClass().getResourceAsStream(configFilePath);
    	//properties.load(is);
    	properties.load(new InputStreamReader(getClass().getResourceAsStream(configFilePath), "UTF-8"));
    }
    catch (Exception ex) {
      log.error("读取配置文件/WEB-INF/classes/config.properties文件出错! 请检查文件路径是否有问题!ex=" + ex.toString());
    }
    finally {
      try {
        if (is != null) {
          is.close();
        }
      }
      catch (Exception ex) {
        log.error("读取配置文件/WEB-INF/classes/config.properties文件出错! 请检查文件路径是否有问题!ex=" + ex.toString());
      }
    }
  }

  /**
   * 根据key值读出字符串
   * @param key String
   * @return String
   */
  public String getValue(String key) {
    return properties.getProperty(key);
  }

  /**
   * 根据key值读出中文字符串
   * @param key String
   * @return String
   */
  public String getValueCHN(String key) {
    return StringUtil.convertToChinese(properties.getProperty(key));
  }
}
