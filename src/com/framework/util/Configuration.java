package com.framework.util;

import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: 这个是一个接口，在这个接口中定义了各种get方法</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company:bpc </p>
 * @author lgj
 * @version 1.0
 */

public interface Configuration{

  /**
   *
   * @param key
   * @param value
   */
    void addProperty(String key, Object value);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Boolean getBoolean(String key, Boolean defaultValue);

    /**
     *
     * @param key
     * @return
     */
    byte getByte(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    byte getByte(String key, byte defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Byte getByte(String key, Byte defaultValue);

    /**
     *
     * @param key
     * @return
     */
    double getDouble(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    double getDouble(String key, double defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Double getDouble(String key, Double defaultValue);

    /**
     *
     * @param key
     * @return
     */
    float getFloat(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    float getFloat(String key, float defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Float getFloat(String key, Float defaultValue);

    /**
     *
     * @param key
     * @return
     */
    int getInt(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    int getInt(String key, int defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Integer getInteger(String key, Integer defaultValue);

    /**
     *
     * @param key
     * @return
     */
    long getLong(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    long getLong(String key, long defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Long getLong(String key, Long defaultValue);

    /**
     *
     * @param key
     * @return
     */
    short getShort(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    short getShort(String key, short defaultValue);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Short getShort(String key, Short defaultValue);

    /**
     *
     * @param key
     * @return
     */
    String getString(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    String getString(String key, String defaultValue);

    /**
     *
     * @param key
     * @return
     */
    String[] getStringArray(String key);

    /**
     *
     * @param key
     * @return
     */
    Vector getVector(String key);

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Vector getVector(String key, Vector defaultValue);
}

