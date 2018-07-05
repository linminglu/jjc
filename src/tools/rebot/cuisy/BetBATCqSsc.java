package tools.rebot.cuisy;

import help.util.APIWebDesUtils;

import java.net.URLEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.json.JSONObject;

import com.framework.util.StringUtil;
import com.ram.util.URLUtil;

public class BetBATCqSsc {
	
	//public static String server = "http://127.0.0.1";
	public static String server = "http://192.168.1.139";
	
	public static int counter = 0;
	
	private static final String strDefaultKey = "P29lMhJ8";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//下注批处理
		cqSscAuto();
		
//		//开始启动定时器bet
//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//	        //private int reqCount = 0;//请求计数
//	        @Override
//	        public void run() {
//	        	counter++;
//	        	P("开始 Timer["+counter+"]..................................");
//	        	cqSscAuto();
//	        }
//	    };
//	    timer.schedule(task, 0, StringUtil.getRandomInt(3000, 10000));
	}
	
	
	public static void cqSscAuto(){
		
		Thread t = new Thread(){
            public void run(){
            	counter = 0;
            	
            	int buyNum = 100;//购买次数
            	int joinNum = 3;//购买人数
            	long startId = 16600000001L;//购买账号//18800000001 ~ 18800001000
            	
            	for(int aa=0;aa<buyNum;aa++){
	            	for(int i=0;i<joinNum;i++){
		    			String loginName = (startId+i)+"";
		    			try {
		    				cqSsc_bet(loginName);
		    				//int sleepms = StringUtil.getRandomInt(50, 500);
		    				//P("sleep["+sleepms+"ms]......");
			    			//sleep(sleepms);
			    			
	//		    			if(i>=end-1){
	//		    				//P("sleep[3000ms] cqSscAuto()......");
	//		    				sleep(500);
	//		    				cqSscAuto();
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
	public static String[] cqSscOptionArray = {
		"[{\"id\":\"498\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//总和大
		"[{\"id\":\"499\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//总和小
		"[{\"id\":\"500\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//总和单
		"[{\"id\":\"501\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//总和双
		"[{\"id\":\"502\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//龙
		"[{\"id\":\"503\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//虎
		"[{\"id\":\"504\",\"p\":\""+StringUtil.getRandomInt(2, 100)+"\"}]",//和
	};
//	public static String[] cqSscOptionArray = {
//		"[{\"id\":\"498\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和大
//		"[{\"id\":\"499\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和小
//		"[{\"id\":\"500\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和单
//		"[{\"id\":\"501\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//总和双
//		"[{\"id\":\"502\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//龙
//		"[{\"id\":\"503\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//虎
//		"[{\"id\":\"504\",\"p\":\""+StringUtil.getRandomInt(1, 1)+"\"}]",//和
//	};
	public static void cqSsc_bet(String loginName){
		String path = "/api/cqSsc_bet?";
		
		//登录获取ukey
		String ukey = BetBATCqSsc.login(loginName);
		String sessionNo = BetBATCqSsc.cqSsc_currentTime(ukey);
		String optionArray = cqSscOptionArray[StringUtil.getRandomInt(0,6)];
		
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
	public static String cqSsc_currentTime(String ukey){
		String path = "/api/cqSsc_currentTime";
		
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
