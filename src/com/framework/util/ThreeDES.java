/*
 * Created on 2005-9-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.util;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/*
字符串 DESede(3DES) 加密
*/
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
   
/**
 *
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.   
 *    
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key   
 *    
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String   
 * getDesString(String strMi)对strMin进行解密,返回String明文   
 *    
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]   
 * byteD)byte[]型的解密   
 */
   
public class ThreeDES{
    Key key;
  	//private static Log log = LogFactory.getLog(ThreeDES.class.getName());
    /**   
     * 根据参数生成KEY   
     *    
     * @param strKey   
     */   
    public void getKey(String strKey) {    
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(strKey.getBytes()));
            this.key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }    
   
    /**   
     * 加密String明文输入,String密文输出   
     *    
     * @param strMing   
     * @return   
     */   
    public String encString(String strMing) {
    	if(strMing == null || "".equals(strMing)){
    		return "";
    	}
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try {    
          byteMing = strMing.getBytes("UTF8");
          byteMi = this.getEncCode(byteMing);
          strMi = base64en.encode(byteMi);
      } catch (Exception e) {
      		System.out.println(e.getMessage());
      } finally {
          base64en = null;
          byteMing = null;
          byteMi = null;
      }
      return strMi;
  }    
 
  /**   
   * 解密 以String密文输入,String明文输出   
   *    
   * @param strMi   
   * @return   
   */   
  public String desString(String strMi) {
  	if(strMi == null || "".equals(strMi)){
  		return "";
  	}
      BASE64Decoder base64De = new BASE64Decoder();
      byte[] byteMing = null;
      byte[] byteMi = null;
      String strMing = "";
      try {    
          byteMi = base64De.decodeBuffer(strMi);    
          byteMing = this.getDesCode(byteMi);    
          strMing = new String(byteMing, "UTF8");    
      } catch (Exception e) {
    	  System.out.println(e.getMessage());
      } finally {
          base64De = null;
          byteMing = null;
          byteMi = null;
      }
      return strMing;
  }    
 
  /**   
   * 加密以byte[]明文输入,byte[]密文输出   
   *    
   * @param byteS   
   * @return   
   */   
  private byte[] getEncCode(byte[] byteS) {    
      byte[] byteFina = null;    
      Cipher cipher;    
      try {    
          cipher = Cipher.getInstance("DES");
          cipher.init(Cipher.ENCRYPT_MODE, key);
          byteFina = cipher.doFinal(byteS);
      } catch (Exception e) {
        e.printStackTrace();
    } finally {
        cipher = null;
    }    
    return byteFina;
}    

/**   
 * 解密以byte[]密文输入,以byte[]明文输出   
 *    
 * @param byteD   
 * @return   
 */   
private byte[] getDesCode(byte[] byteD) {
    Cipher cipher;
    byte[] byteFina = null;
    try {    
        cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byteFina = cipher.doFinal(byteD);
    } catch (Exception e) {
    	System.out.println(e.getMessage());
    } finally {
        cipher = null;
    }
    return byteFina;
}    

	public static void main(String[] args) {    
	    ThreeDES des = new ThreeDES();// 实例化一个对像
	    des.getKey("beiwaieclass2011");// 生成密匙
	    
	    String strEnc = des.encString("abcde");// 加密字符串,返回String的密文
	    System.out.println(strEnc);

	    strEnc = "pHqZpgqkeiI=";
	    String strDes = des.desString(strEnc);// 把String 类型的密文解密
	    System.out.println(strDes);
	}
}