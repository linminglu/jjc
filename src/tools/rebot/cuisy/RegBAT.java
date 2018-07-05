package tools.rebot.cuisy;

import help.util.APIWebDesUtils;

import java.net.URLEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.ram.util.URLUtil;

public class RegBAT {
	
	public static String server = "http://127.0.0.1";
	
	public static int counter = 0;
	
	private static final String strDefaultKey = "P29lMhJ8";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//RegBAT r = new RegBAT();
		
//		//定义特殊手机号，批量注册账号 - gdpick
//		long startId = 19900000001L;
//		for(int i=0;i<1;i++){
//			String loginName = (startId+i)+"";
//			reg(i,loginName);
//		}
		
		//定义特殊手机号，批量注册账号 - cqssc
		long startId = 12300000001L;
		for(int i=0;i<10;i++){
			String loginName = (startId+i)+"";
			reg(i,loginName);
		}
		
	}	
	
	
	
	//注册
	public static void reg(int i,String loginName){
		String path = "/api/user_reg1";
		
		StringBuffer sb = new StringBuffer();
		sb.append("loginName=").append(loginName);
		sb.append("&password=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&repassword=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&inpcode=").append("000000");
		sb.append("&inviteCode=").append("2021");
		
		try {
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
			String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
			
			P("source>"+sb.toString());
			//P("target>"+querys);
			
			String returns = URLUtil.HttpRequestUTF8(server+path, querys);
			P("注册账号["+i+"]>"+returns);
			
			//JSONObject joR = new JSONObject(returns);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//打印
	public static void P(String info){
		System.out.println(info);
	}
}
