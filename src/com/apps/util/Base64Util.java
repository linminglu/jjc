
package com.apps.util;
import java.io.UnsupportedEncodingException;
 
import sun.misc.BASE64Decoder;
 
public class Base64Util {
    // 把普通字符编码成64bit
    public static String StrToBase64(String s)
            throws UnsupportedEncodingException {
        if (s == null)
            return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes("UTF-8"));
    }
 
    // 把64bit编码转化成普通字符串
    public static String Base64ToStr(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
 
            return new String(b, "UTF-8");
 
        } catch (Exception e) {
            return null;
        }
    }
     
    public static void main(String[] args) {
        String s = "我是超人";
        try {
            System.out.println(StrToBase64(s));
            System.out.println(Base64ToStr(StrToBase64(s)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}