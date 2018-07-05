package help.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class APIDesUtils {
	protected final static Log log = LogFactory.getLog(APIDesUtils.class);
	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();

	private static final String strDefaultKey = "PuNbYnoG";

	/**
	 * data[]进行编码
	 * 
	 * @param data
	 * @return
	 */
	public static String encode(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 0x0ff) << 8)
					| (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14) {
				n = 0;
				buf.append(" ");
			}
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] decode(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void decode(String s, OutputStream os) throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;

			if (i == len)
				break;

			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);

			i += 4;
		}
	}

	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return
	 * @throws Exception
	 */
	public static String encryptDES(String str){
		byte[] encryptedData;
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(strDefaultKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(str.getBytes());
			return Base64.encode(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public String encryptDES(String str, String encryptKey) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(str.getBytes());
		return Base64.encode(encryptedData);
	}

	/**
	 * 解密
	 * 
	 * @param str
	 *            需要解密的字符串
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String str) throws Exception {
		try {
			byte[] byteMi = Base64.decode(str);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(strDefaultKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData);
		} catch (Exception e) {
			log.error("____解密出错__ps::"+str);
//			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 解密
	 * 
	 * @param str
	 *            需要解密的字符串
	 * @return
	 * @throws Exception
	 */
	public String decryptDES(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData);
	}

	public static void main(String[] args) {
		try {
//			String plaintext = "hahaha";
//			System.out.println("编码前：" + plaintext);
//			plaintext=URLEncoder.encode(plaintext);
			
//			String ciphertext;
//			ciphertext = APIDesUtils.encryptDES(plaintext);
//			System.out.println("编码明文：" + plaintext);
//			System.out.println("密钥：" + strDefaultKey);
//			System.out.println("密文：" + ciphertext);
//			System.out.println("解密后：" + APIDesUtils.decryptDES(ciphertext));
//			System.out.println("解密后解码：" + URLDecoder.decode(APIDesUtils.decryptDES(ciphertext)));
//			System.out.println("解密后解码：" + APIDesUtils.decryptDES("oZsN4hd15N4F7Rjh65YwPqYWHXrs7TWlA3KId5cDYmET2Yan7HZPTtrTXJa1hU6Jr+FGjNfr28BlIOTS/fbP0cmayQ029p4XilWeMNU9Jht84S9rxCLeFAajCKsh5AwjWgkRXxQ+YnSBULfq9W0q785eSzeTJvieMrhwy4U3QrPP2hUuQJ7tZNdbbFx1EeY3USWuFYiBYG48DF+6xBiq77mfVevIgY5SOTXcfqoSXN21Pl35h9zOwLD4D1BHMQ8z"));
//			System.out.println("解密后解码：" + APIDesUtils.decryptDES("wtWUszWGbI+c8sgBzuh8cg1Aw8Hl7Lfp"));
//			
//			System.out.println(URLEncoder.encode("wtWUszWGbI+c8sgBzuh8cg1Aw8Hl7Lfp"));
//			String s="J8xPQkcnoMpGbngSOltdgIsdBn6wSsuFdAASkTE4vOTEAylvDOZY1gF5%2FNJtKJ0LVBTi0Fk0KREXBYORoIayFLm8m%2B%2BB0EGmLNDYZrDZDyKiXD3Fi%2FZdgA%3D%3D";
//			s = URLDecoder.decode(s);
//			System.out.println("___解密后解码：:::::" + APIDesUtils.decryptDES(s));
			System.out.println("___加密：:::::" + APIDesUtils.encryptDES("Message"));
//			System.out.println("___解密后：:::::" + APIDesUtils.decryptDES("qiCAKjeOrbc="));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
