package com.framework.util;

import java.math.BigDecimal;

public class DataTypeUtil {
  public DataTypeUtil() {
  }

  /**
   * 将对象转换为整数型
   * @param  o  源对象
   * @return    对应的整数值,如果出错,则返回Integer.MIN_VALUE
   */
  public static int toInt(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("该对象为空");
    }
    String s = o.toString();
    try {
      return Integer.parseInt(s);
    }
    catch (Exception ex) {
    
      return Integer.MIN_VALUE;
    }
  }
  public static BigDecimal str2BigDecimal(String str){
      if(str!=null&&str.length()>0){
          BigDecimal big=new BigDecimal(str);    
          return big;
      }else{
        return (new BigDecimal("0"));    
      }
  
  }
}
