package tools.test;

import help.util.APIWebDesUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.apps.Constants;
import com.apps.util.SessionUtil;
import com.framework.util.DesUtils;
import com.ram.model.User;

public class Test {
	public static void main(String[] args) {
		encrypt("id=1");
		
		String u_3004 = "e9f20adcf158e622dac562af88a0207c0192a954173945ae00c1e6385d5ca0846d183d9bfc99efdfa1f9ebbb33cabe7181ff0bce58a434c6d27bd0a2cb7efe3f701dca8c5fcc171a9f6c22983522a1be8568104388c5cb0f568cada10dc243a6da1dc9c58da31d6e";
		String u_3007 = "1f87e510929cae48dac562af88a0207c53d3755528ced88673fc169fc3b22d316d183d9bfc99efdfa8b860b4162d7f1bebd7eaba98b96bee86f18642a955df1abf709dd2546767519f6c22983522a1be8568104388c5cb0fc7c13da9e0bf6917fb504163dd8cad53";
		
		String u_23306 = "7fa1da4fea133b5b36dcb734fe580cd8e0aaa8649ce47ac4a8396e6fe3a5b92822f874add604477acc23c12903bc12e7d994323a62b4fe891a69005868e34f2d472be7005d551a0e8888887fd45813c3";
		String u_24793 = "e75f077fd4727c21ed9eab5b4770c395fe511f13cec220a4d5bf8b534804505622f874add604477af96f1043900612ac921048f96d18d06c6f620025c5674ad0d0ab84623184d8171bbfc689172ac784";
		
		User user = new User();
		user.setUserId(24793);
		user.setLoginName("13636024089");
		user.setPassword("A9030FBF59C0BAD0DFB964550C1AF80B");
		user.setStatus("1");
//		getU(user);
		
//		bigdecimal();
		
		int a = 001;
		System.out.println(Integer.valueOf(a));
	}
	
	public static void encrypt(String str){
		try {
			String strDefaultKey = "P29lMhJ8";
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	
			APIWebDesUtils crypt = new APIWebDesUtils(
					keyFactory.generateSecret(key));
	
			String unencryptedString = str;
			String encryptedString = URLEncoder.encode(crypt.encryptBase64(unencryptedString));
			
			System.out.println("mw=" + encryptedString);
		} catch (Exception e) {
			System.err.println("Error:" + e.toString());
		}
	}
	
	
	public static void getU(User user){
		String session = SessionUtil.random();
		String u = Constants.DES_KEY_UID + "=" + user.getUserId() + "&"
				+ Constants.DES_KEY_LOGINNAME + "=" + user.getLoginName() + "&"
				+ Constants.DES_KEY_PASSWORD + "=" + user.getPassword();
		
		System.out.println(DesUtils.encrypt(u));
	}
	
	public static void bigdecimal(){
		BigDecimal coinNum = new BigDecimal(10);
		BigDecimal tax = new BigDecimal(6).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP);
		BigDecimal tax1 = coinNum.multiply(tax).setScale(2,BigDecimal.ROUND_HALF_UP);
		coinNum = coinNum.multiply(new BigDecimal("1").subtract(tax)).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		System.out.println(coinNum.toString());
		System.out.println(tax1);
	}
}
