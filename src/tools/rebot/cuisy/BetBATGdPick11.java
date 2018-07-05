package tools.rebot.cuisy;

import help.util.APIWebDesUtils;

import java.net.URLEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.json.JSONObject;

import com.framework.util.StringUtil;
import com.ram.util.URLUtil;

public class BetBATGdPick11 {
	
	//public static String server = "http://127.0.0.1";
	public static String server = "http://192.168.1.139";
	
	public static int counter = 0;
	
	private static final String strDefaultKey = "P29lMhJ8";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//下注批处理
		gdPick11Auto();
		
//		//开始启动定时器bet
//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//	        //private int reqCount = 0;//请求计数
//	        @Override
//	        public void run() {
//	        	counter++;
//	        	P("开始 Timer["+counter+"]..................................");
//	        	gdPick11Auto();
//	        }
//	    };
//	    timer.schedule(task, 0, StringUtil.getRandomInt(3000, 10000));
	}
	
	
	public static void gdPick11Auto(){
		
		Thread t = new Thread(){
            public void run(){
            	counter = 0;
            	//3000>
            	int buyNum = 100;//购买次数
            	int joinNum = 3;//购买人数
            	long startId = 16600000004L;//购买账号//19900000001 ~ 19900001000
            	
            	for(int aa=0;aa<buyNum;aa++){
	            	for(int i=0;i<joinNum;i++){
		    			String loginName = (startId+i)+"";
		    			try {
		    				gdPick11_bet(loginName);
		    				int sleepms = StringUtil.getRandomInt(10, 200);
		    				P("sleep["+sleepms+"ms]......");
			    			sleep(sleepms);
			    			
	//		    			if(i>=end-1){
	//		    				//P("sleep[3000ms] gdPick11Auto()......");
	//		    				sleep(500);
	//		    				gdPick11Auto();
	//		    			}
						} catch (Exception e) {
							e.printStackTrace();
						}
		    			
		    		}
            	}
            }
		};
		t.start();
		
	}
	
	//玩法和投注金额
	public static String[] gdPick11OptionArray = {
		"[{\"id\":\"1032\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//和大
		"[{\"id\":\"1033\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//和小
		"[{\"id\":\"1034\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//和单
		"[{\"id\":\"1035\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//和双
		"[{\"id\":\"1036\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//尾大
		"[{\"id\":\"1037\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//尾小
		"[{\"id\":\"1038\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//龙
		"[{\"id\":\"1039\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]" //虎
	};
//	public static String[] gdPick11OptionArray = {
//		"[{\"id\":\"1032\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和大
//		"[{\"id\":\"1033\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和小
//		"[{\"id\":\"1034\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和单
//		"[{\"id\":\"1035\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和双
//		"[{\"id\":\"1036\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//尾大
//		"[{\"id\":\"1037\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//尾小
//		"[{\"id\":\"1038\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//龙
//		"[{\"id\":\"1039\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]" //虎
//	};
	public static void gdPick11_bet(String loginName){
		String path = "/api/gdPick11_bet?";
		
		//登录获取ukey
		String ukey = BetBATGdPick11.login(loginName);
		String sessionNo = BetBATGdPick11.gdPick11_currentTime(ukey);
		String optionArray = gdPick11OptionArray[StringUtil.getRandomInt(0,7)];
		
		if(sessionNo!=null && sessionNo.length()>0){
			//构造下注参数
			StringBuffer sb = new StringBuffer();
			sb.append("u=").append(ukey);
			sb.append("&sessionNo=").append(sessionNo);
			sb.append("&optionArray=").append(optionArray);
			try {
				DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	
				APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
				String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
				
				//P("source>"+sb.toString());
				//P("target>"+querys);
				
				String returns = URLUtil.HttpRequestUTF8(server+path+querys);
				counter++;
				P("下注["+counter+"]>"+loginName+">"+optionArray+">"+returns);
				
	//			JSONObject joR = new JSONObject(returns);
	//			JSONObject joData = joData.getJSONObject("data");
	//			JSONObject joObj = joR.getJSONObject("obj");
				
			} catch (Exception e) {
				
			}
		}else{
			counter++;
			P("封盘["+counter+"].............");
		}
	}
	
	//获取当前期信息
	public static String gdPick11_currentTime(String ukey){
		String path = "/api/gdPick11_currentTime";
		
		String sessionNo = "";
		
		//构造下注参数
		StringBuffer sb = new StringBuffer();
		sb.append("u=").append(ukey);
		
		try {
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
			String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
			
			//P("source>"+sb.toString());
			//P("target>"+querys);
			
			String returns = URLUtil.HttpRequestUTF8(server+path, querys);
			//P("returns>"+returns);
			
			JSONObject joR = new JSONObject(returns);
			JSONObject joData = joR.getJSONObject("data");
			JSONObject joObj = joData.getJSONObject("obj");
			String betTime = joObj.getString("betTime");
			if(Integer.valueOf(betTime)>0){//可以投注返回期号
				sessionNo = joObj.getString("sessionNo");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//P("期号>"+sessionNo);
		return sessionNo;
	}
	
	//登录
	public static String login(String loginName){
		String path = "/api/user_login";
		
		String ukey = "";
		
		StringBuffer sb = new StringBuffer();
		sb.append("loginName=").append(loginName);
		sb.append("&password=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&repassword=").append("96E79218965EB72C92A549DD5A330112");
		sb.append("&machineType=").append("3");
		
		try {
			DESKeySpec key = new DESKeySpec(strDefaultKey.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			APIWebDesUtils crypt = new APIWebDesUtils(keyFactory.generateSecret(key));
			String querys = "mw="+URLEncoder.encode(crypt.encryptBase64(sb.toString()),"utf-8");
			
			//P("source>"+sb.toString());
			//P("target>"+querys);
			
			String returns = URLUtil.HttpRequestUTF8(server+path, querys);
			//P("returns>"+returns);
			
			JSONObject joR = new JSONObject(returns);
			JSONObject joData = joR.getJSONObject("data");
			JSONObject joObj = joData.getJSONObject("obj");
			ukey = joObj.getString("u");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//P("登录>"+loginName+">ukey="+ukey);
		return ukey;
	}
	
	//打印
	public static void P(String info){
		System.out.println(info);
	}
}
