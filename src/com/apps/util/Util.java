package com.apps.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class Util {

	public static String loadFile(String fileName) {
		InputStream fis;
		try {
			fis = new FileInputStream(fileName);
			byte[] bs = new byte[fis.available()];
			fis.read(bs);
			String res = new String(bs);
			fis.close();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String md5EncryptAndBase64(String str) {
		return encodeBase64(md5Encrypt(str));
	}

	private static byte[] md5Encrypt(String encryptStr) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptStr.getBytes("utf8"));
			return md5.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String encodeBase64(byte[] b) {
		sun.misc.BASE64Encoder base64Encode = new BASE64Encoder();
		String str = base64Encode.encode(b);
		return str;
	}

	
	public static String encryption(String plainText) throws UnsupportedEncodingException {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("GB2312"));
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

	
	 public static String getMD5Str(String plainText) throws UnsupportedEncodingException {  
	        String re_md5 = new String();
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(plainText.getBytes("utf8"));
	            byte b[] = md.digest();
	 
	            int i;
	 
	            StringBuffer buf = new StringBuffer("");
	            for (int offset = 0; offset < b.length; offset++) {
	                i = b[offset];
	                if (i < 0)
	                    i += 256;
	                if (i < 16)
	                    buf.append("0");
	                buf.append(Integer.toHexString(i));
	            }
	 
	            re_md5 = buf.toString();
	 
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return re_md5;
	 }  
	
	public static void main(String[] args) {
		String xml = loadFile(args[0]);
		String checkword = loadFile(args[1]);
		System.out.println(md5EncryptAndBase64(xml + checkword));
		System.out.println(md5EncryptAndBase64("abc"));
		System.out.println(md5EncryptAndBase64("中"));
	}
}
