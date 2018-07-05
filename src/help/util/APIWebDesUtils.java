/*
 * copyright
 * http://timarcher.com/blog/2007/04/simple-java-class-to-des-encrypt-strings-such-as-passwords-and-credit-card-numbers/
 */

package help.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * web解密工具，保持和js加密一致
 * 
 * @author Tim Archer 11/11/03
 * @version $Revision: 1.2 $
 */
public class APIWebDesUtils {
	protected final static Log log = LogFactory.getLog(APIDesUtils.class);
	private static final String strDefaultKey = "P29lMhJ8";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	/**
	 * Construct a new object which can be utilized to encrypt and decrypt
	 * strings using the specified key with a DES encryption algorithm.
	 * 
	 * @param key
	 *            The secret key used in the crypto operations.
	 * @throws Exception
	 *             If an error occurs.
	 * 
	 */
	public APIWebDesUtils(SecretKey key) throws Exception {
		encryptCipher = Cipher.getInstance("DES");
		decryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	/**
	 * Encrypt a string using DES encryption, and return the encrypted string as
	 * a base64 encoded string.
	 * 
	 * @param unencryptedString
	 *            The string to encrypt.
	 * @return String The DES encrypted and base 64 encoded string.
	 * @throws Exception
	 *             If an error occurs.
	 */
	public String encryptBase64(String unencryptedString) throws Exception {
		// Encode the string into bytes using utf-8
		byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");

		// Encrypt
		byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);

		// Encode bytes to base64 to get a string
		byte[] encodedBytes = Base64.encodeBase64(encryptedBytes);

		return new String(encodedBytes);
	}

	/**
	 * Decrypt a base64 encoded, DES encrypted string and return the unencrypted
	 * string.
	 * 
	 * @param encryptedString
	 *            The base64 encoded string to decrypt.
	 * @return String The decrypted string.
	 * @throws Exception
	 *             If an error occurs.
	 */
	public String decryptBase64(String encryptedString) throws Exception {
		// Encode bytes to base64 to get a string
		byte[] decodedBytes = Base64.decodeBase64(encryptedString.getBytes());
		// Decrypt
		byte[] unencryptedByteArray = decryptCipher.doFinal(decodedBytes);
		// Decode using utf-8
		return new String(unencryptedByteArray, "UTF8");
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
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// Instantiate the encrypter/decrypter
			APIWebDesUtils crypt = new APIWebDesUtils(
					keyFactory.generateSecret(key));
			String decryptedData = crypt.decryptBase64(str);
			return decryptedData;
		} catch (Exception e) {
			log.error("____解密出错__ps::" + str);
			// e.printStackTrace();
			return "";
		}
	}

	public static void main(String args[]) {
		try {
			// Generate the secret key
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			// Instantiate the encrypter/decrypter
			APIWebDesUtils crypt = new APIWebDesUtils(
					keyFactory.generateSecret(key));

			String unencryptedString = "Message";
			String encryptedString = crypt.encryptBase64(unencryptedString);
			// Encrypted String:8dKft9vkZ4I=
			System.out.println("Encrypted String:" + encryptedString);
			// Decrypt the string
			unencryptedString = crypt.decryptBase64(encryptedString);
			// UnEncrypted String:Message
			System.out.println("UnEncrypted String:" + unencryptedString);

		} catch (Exception e) {
			System.err.println("Error:" + e.toString());
		}
	}
}