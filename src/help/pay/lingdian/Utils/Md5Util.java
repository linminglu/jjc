package help.pay.lingdian.Utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f' };
/*
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F' };
*/
	public static String md5(String s,String charset) 
	{  
		try {  
			// Create MD5 Hash  
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");  
			digest.update((s).getBytes(charset));  
			byte messageDigest[] = digest.digest();  
			                     
			return toHexString(messageDigest);  
		} 
		catch (NoSuchAlgorithmException e) 
		{  
			e.printStackTrace();  
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		             
		return "";  
	}
    
    public static String toHexString(byte[] b) 
    {    
        StringBuilder sb = new StringBuilder(b.length * 2);    
        for (int i = 0; i < b.length; i++) {    
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);    
            sb.append(HEX_DIGITS[b[i] & 0x0f]);    
        }    
        return sb.toString();    
    }
}
