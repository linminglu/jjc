package help.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class APIDesTest {
	public static void main(String[] args) {
		String s="ss";
		
		String encrypt = APIDesUtils.encryptDES(s);
		System.out.println(encrypt);
		
		String encode = URLEncoder.encode(s);
		
		System.out.println(encode);
		String decode = URLDecoder.decode(encode);
		decode = URLDecoder.decode(decode);
		System.out.println(decode);
		
		
	}
}
