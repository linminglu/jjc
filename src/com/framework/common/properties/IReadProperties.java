package com.framework.common.properties;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */

public interface IReadProperties {
  /**
   * 根据key读出相应的String
   * @param key String
   * @return String
   */
  public String getValue(String key);
  /**
   * 根据key读出相应的String 汉语的
   * @param key String
   * @return String
   */
  public String getValueCHN(String key);
}
