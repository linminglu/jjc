package com.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Title: </p>
 * <p>Description: 这个类中实现了接口Configuration中的所有方法</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: bpc</p>
 * @author lgj
 * @version 1.0
 */

public class AbstractConfiguration
    implements Configuration {
  /** how big the initial arraylist for splitting up name value pairs */
  private static final int INITIAL_LIST_SIZE = 2;

  private static Log log = LogFactory.getLog(AbstractConfiguration.class);
  /**
   * stores the configuration key-value pairs
   */
  protected Configuration defaults = null;

  private HashMap store = new HashMap();

  /**
   * Empty constructor.
   */
  public AbstractConfiguration() {
  }

  public AbstractConfiguration(Configuration defaults) {
    this();
    this.defaults = defaults;
  }

  /**
   * @param key The Key to add the property to.
   * @param token The Value to add.
   */
  public void addProperty(String key, Object token) {
    if (token instanceof String) {
      for (Iterator it = processString( (String) token).iterator();
           it.hasNext(); ) {
        addPropertyDirect(key, it.next());
      }
    }
    else if (token instanceof Collection) {
      for (Iterator it = ( (Collection) token).iterator(); it.hasNext(); ) {
        addProperty(key, it.next());
      }
    }
    else {
      addPropertyDirect(key, token);
    }
  }

  /**
   * Read property. Should return <code>null</code> if the key doesn't
   * map to an existing object.
   * @param key key to use for mapping
   * @return object associated with the given configuration key.
   */
  protected Object getPropertyDirect(String key) {
    return store.get(key);
  }

  /**
   * Adds a key/value pair to the Configuration. Override this method to
   * provide write acces to underlying Configuration store.
   *
   * @param key key to use for mapping
   * @param obj object to store
   */
  protected void addPropertyDirect(String key, Object obj) {
    Object o = getPropertyDirect(key);
    Object objAdd = null;

    if (o == null) {
      objAdd = obj;
    }
    else {
      if (o instanceof Container) {
        ( (Container) o).add(obj);
      }
      else {
        // The token key is not a container.
        Container c = new Container();

        // There is an element. Put it into the container
        // at the first position
        c.add(o);

        // Now gobble up the supplied object
        c.add(obj);

        objAdd = c;
      }
    }

    if (objAdd != null) {
      store.put(key, objAdd);
    }
  }


  /**
   * Returns a Vector of Strings built from the supplied
   * String. Splits up CSV lists. If no commas are in the
   * String, simply returns a Vector with the String as its
   * first element
   *
   * @param token The String to tokenize
   * @return A List of Strings
   */
  protected List processString(String token) {
    List retList = new ArrayList(INITIAL_LIST_SIZE);

    if (token.indexOf(PropertiesTokenizer.DELIMITER) > 0) {
      PropertiesTokenizer tokenizer =
          new PropertiesTokenizer(token);

      while (tokenizer.hasMoreTokens()) {
        String value = tokenizer.nextToken();
        retList.add(value);
      }
    }
    else {
      retList.add(token);
    }

    return retList;
  }

  /**
   * 支持yes,true,on;false,off,no
   * @param value The value to test for boolean state.
   * @return <code>true</code> or <code>false</code> if the supplied
   * text maps to a boolean value, or <code>null</code> otherwise.
   */
  protected final Boolean testBoolean(String value) {
    String s = value.toLowerCase();

    if (s.equals("true") || s.equals("on") || s.equals("yes")) {
      return Boolean.TRUE;
    }
    else if (s.equals("false") || s.equals("off") || s.equals("no")) {
      return Boolean.FALSE;
    }
    else {
      return null;
    }
  }
  /**
   *
   * @param key
   * @return
   */

  public boolean getBoolean(String key) {
    Boolean b = getBoolean(key, (Boolean)null);
    if (b != null) {
      return b.booleanValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return getBoolean(key, new Boolean(defaultValue)).booleanValue();
  }
  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public Boolean getBoolean(String key, Boolean defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Boolean) {
      return (Boolean) value;
    }
    else if (value instanceof String) {
      return testBoolean( (String) value);
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getBoolean(key, defaultValue);
      }
      else {
        log.warn("Use Boolean default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Boolean object");
    }
  }

  /**
   *
   * @param key
   * @return
   */
  public byte getByte(String key) {
    Byte b = getByte(key, null);
    if (b != null) {
      return b.byteValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + " doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return byte
   */
  public byte getByte(String key, byte defaultValue) {
    return getByte(key, new Byte(defaultValue)).byteValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return byte
   */
  public Byte getByte(String key, Byte defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Byte) {
      return (Byte) value;
    }
    else if (value instanceof String) {
      Byte b = new Byte( (String) value);
      return b;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getByte(key, defaultValue);
      }
      else {
        log.warn("Use Byte default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Byte object");
    }
  }

  /**
   *
   * @param key
   * @return double
   */
  public double getDouble(String key) {
    Double d = getDouble(key, null);
    if (d != null) {
      return d.doubleValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }
  /**
   *
   * @param key
   * @param defaultValue
   * @return double
   */
  public double getDouble(String key, double defaultValue) {
    return getDouble(key, new Double(defaultValue)).doubleValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return double
   */

  public Double getDouble(String key, Double defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Double) {
      return (Double) value;
    }
    else if (value instanceof String) {
      Double d = new Double( (String) value);
      return d;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getDouble(key, defaultValue);
      }
      else {
        log.warn("Use Double default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Double object");
    }
  }


  /**
   *
   * @param key
   * @return float
   */

  public float getFloat(String key) {
    Float f = getFloat(key, null);
    if (f != null) {
      return f.floatValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public float getFloat(String key, float defaultValue) {
    return getFloat(key, new Float(defaultValue)).floatValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public Float getFloat(String key, Float defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Float) {
      return (Float) value;
    }
    else if (value instanceof String) {
      Float f = new Float( (String) value);
      return f;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getFloat(key, defaultValue);
      }
      else {
        log.warn("Use Float default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Float object");
    }
  }

  /**
   *
   * @param key
   * @return int
   */
  public int getInt(String key) {
    Integer i = getInteger(key, null);
    if (i != null) {
      return i.intValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return int
   */
  public int getInt(String key, int defaultValue) {
    Integer i = getInteger(key, null);

    if (i == null) {
      return defaultValue;
    }

    return i.intValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return Integer
   */
  public Integer getInteger(String key, Integer defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Integer) {
      return (Integer) value;
    }
    else if (value instanceof String) {
      Integer i = new Integer( (String) value);
      return i;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getInteger(key, defaultValue);
      }
      else {
        log.warn("Use Integer default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Integer object");
    }
  }

  /**
   *
   * @param key
   * @return
   */
  public long getLong(String key) {
    Long l = getLong(key, null);
    if (l != null) {
      return l.longValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public long getLong(String key, long defaultValue) {
    return getLong(key, new Long(defaultValue)).longValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public Long getLong(String key, Long defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Long) {
      return (Long) value;
    }
    else if (value instanceof String) {
      Long l = new Long( (String) value);
      return l;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getLong(key, defaultValue);
      }
      else {
        log.warn("Use Long default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Long object");
    }
  }

  /**
   *
   * @param key
   * @return
   */
  public short getShort(String key) {
    Short s = getShort(key, null);
    if (s != null) {
      return s.shortValue();
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public short getShort(String key, short defaultValue) {
    return getShort(key, new Short(defaultValue)).shortValue();
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public Short getShort(String key, Short defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof Short) {
      return (Short) value;
    }
    else if (value instanceof String) {
      Short s = new Short( (String) value);
      return s;
    }
    else if (value == null) {
      if (defaults != null) {
        return defaults.getShort(key, defaultValue);
      }
      else {
        log.warn("Use Short default value for key '" + key + "' (" +
                 defaultValue + ")");
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a Short object");
    }
  }

  /**
   *
   * @param key
   * @return
   */
  public String getString(String key) {
    String s = getString(key, null);
    if (s != null) {
      return s;
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   * 最常用的方法
   * @param key
   * @param defaultValue
   * @return
   */

  public String getString(String key, String defaultValue) {
    Object value = resolveContainerStore(key);

    if (value instanceof String) {
      //return interpolate( (String) value);
      return (String) value;
    }
    else if (value == null) {
      if (defaults != null) {
        //return interpolate(defaults.getString(key, defaultValue));
        return defaultValue;
      }
      else {
        log.warn("Use String default value for key '" + key + "' (" +
                 defaultValue + ")");
        //return interpolate(defaultValue);
        return defaultValue;
      }
    }
    else {
      throw new ClassCastException('\'' + key + "' doesn't map to a String object");
    }
  }

  /**
   *
   * @param key
   * @return
   */
  public String[] getStringArray(String key) {
    Object value = getPropertyDirect(key);

    String[] tokens;

    if (value instanceof String) {
      tokens = new String[1];

      tokens[0] = (String) value;
    }
    else if (value instanceof Container) {
      tokens = new String[ ( (Container) value).size()];

      for (int i = 0; i < tokens.length; i++) {
        tokens[i] = (String) ( (Container) value).get(i);
      }
    }
    else if (value == null) {
      if (defaults != null) {
        tokens = defaults.getStringArray(key);
      }
      else {
        tokens = new String[0];
      }
    }
    else {
      throw new ClassCastException(
          '\'' + key + "' doesn't map to a String/Vector object");
    }
    return tokens;
  }

  /**
   *  可以把相同的多个取出放到一个 vector里面去
   * @param key
   * @return
   */
  public Vector getVector(String key) {
    Vector v = getVector(key, null);
    if (v != null) {
      return v;
    }
    else {
      throw new NoSuchElementException(
          '\'' + key + "' doesn't map to an existing object");
    }
  }

  /**
   *
   * @param key
   * @param defaultValue
   * @return
   */
  public Vector getVector(String key, Vector defaultValue) {
    Object value = getPropertyDirect(key);
    Vector v = null;

    if (value instanceof String) {
      v = new Vector(1);
      v.addElement(value);
    }
    else if (value instanceof Container) {
      v = ( (Container) value).asVector();
    }
    else if (value == null) {
      if (defaults != null) {
        v = defaults.getVector(key, defaultValue);
      }
      else {
        v = ( (defaultValue == null) ? new Vector() : defaultValue);
      }
    }
    else {
      throw new ClassCastException(
          '\''
          + key
          + "' doesn't map to a Vector object: "
          + value
          + ", a "
          + value.getClass().getName());
    }
    return v;
  }

  /**
   *
   * @param key
   * @return
   */

  private Object resolveContainerStore(String key) {
    Object value = getPropertyDirect(key);
    //Object [] str=new Object[((Container) value).size()];
    if (value != null && value instanceof Container) {
      value = ( (Container) value).get(0);//取第一个
    }
    return value;

  }

  /**
   * This class divides into tokens a property value.  Token
   * separator is "," but commas into the property value are escaped
   * using the backslash in front.
   */
  class PropertiesTokenizer
      extends StringTokenizer {
    /** The property delimiter used while parsing (a comma). */
    static final String DELIMITER = ",";

    /**
     * Constructor.
     *
     * @param string A String.
     */
    public PropertiesTokenizer(String string) {
      super(string, DELIMITER);
    }

    /**
     * Check whether the object has more tokens.
     *
     * @return True if the object has more tokens.
     */
    public boolean hasMoreTokens() {
      return super.hasMoreTokens();
    }

    /**
     * Get next token.
     *
     * @return A String.
     */
    public String nextToken() {
      StringBuffer buffer = new StringBuffer();

      while (hasMoreTokens()) {
        String token = super.nextToken();
        if (token.endsWith("\\")) {
          buffer.append(token.substring(0, token.length() - 1));
          buffer.append(DELIMITER);
        }
        else {
          buffer.append(token);
          break;
        }
      }
      return buffer.toString().trim();
    }
  } // class PropertiesTokenizer


  /**
   * Private Wrapper class for Vector, so we can distinguish between
   * Vector objects and our container
   */
  static class Container {
    /** We're wrapping a List object (A vector) */
    private List l = null;

    /**
     * C'tor
     */
    public Container() {
      l = new Vector(INITIAL_LIST_SIZE);
    }

    /**
     * Add an Object to the Container
     *
     * @param o The Object
     */
    public void add(Object o) {
      l.add(o);
    }

    /**
     * Returns the current size of the Container
     *
     * @return The Number of elements in the container
     */
    public int size() {
      return l.size();
    }

    /**
     * Returns the Element at an index
     *
     * @param index The Index
     * @return The element at that index
     */
    public Object get(int index) {
      return l.get(index);
    }

    /**
     * Returns an Iterator over the container objects
     *
     * @return An Iterator
     */
    public Iterator iterator() {
      return l.iterator();
    }

    /**
     *
     * @return
     */
    public Vector asVector() {
      Vector v = new Vector(l.size());

      for (Iterator it = l.iterator(); it.hasNext(); ) {
        v.add(it.next());
      }
      return v;
    }
  }
}
