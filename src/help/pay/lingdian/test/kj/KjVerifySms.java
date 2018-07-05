package help.pay.lingdian.test.kj;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import help.pay.lingdian.Config.MerConfig;
import help.pay.lingdian.Utils.HttpClientUtil;
import help.pay.lingdian.Utils.SignUtil;
/**
 * 210103-机构快捷支付（验证并支付） 
 * 调用交易 210103-机构快捷支付
 * @author Administrator
 *
 */
public class KjVerifySms {
		private static final String TxCode="210103";//210103-机构快捷支付（验证并支付） 
	public static void main(String[] args) {
		  try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("Version", "2.0");	//版本号
			map.put("SignMethod",MerConfig.SIGNMETHOD); //签名类型
			map.put("TxCode",TxCode);  		//交易编码			
			map.put("MerNo",MerConfig.MERNO);  //商户号-测试环境统一商户号
			//map.put("PayChannel","");  //指定渠道
			map.put("TxSN","20170711161013");//商户交易流水号(唯一)
			map.put("SMSVerifyCode","151293");//短信验证码
			map.put("Terminal_type","mobile");//终端类型 默认值:mobile web、wap、 mobile
			map.put("Terminal_info","1212121");//终端信息:格式为IMEI_MAC/序列号_SIM
			map.put("Member_ip","127.0.0.1");//用户的真实IP地址
			
	       // 设置签名
			MerConfig.setSignature(map);
	        //报文明文
	        String plain = SignUtil.getURLParam(map,MerConfig.URL_PARAM_CONNECT_FLAG, true, null);
	        System.out.println("请求原始报文:"+plain);
			//测试地址
	        String msg = MerConfig.sendMsg(MerConfig.REQURL, map);
	        if (null == msg) {
				System.out.println("报文发送失败或应答消息为空");
			} else {
				Map<String,String> resMap = SignUtil.parseResponse(msg,MerConfig.base64Keys,MerConfig.URL_PARAM_CONNECT_FLAG,MerConfig.CHARSET);
				System.out.println("URLDecoder处理后返回数据:"+SignUtil.getURLParam(resMap,MerConfig.URL_PARAM_CONNECT_FLAG, true, null));
				if (MerConfig.verifySign(resMap)){
					System.out.println("签名验证结果成功");
					if ("00000".equalsIgnoreCase(resMap.get("RspCod"))
						|| "1".equalsIgnoreCase(resMap.get("Status"))){
						//验证短信成功，支付已提交到银行等待支付结果的异步通知,在接收的异步通知里面判断支付是否成功
						//业务处理
					}
					else {
						//请求失败进行处理
					}
				}
				else {
					System.out.println("签名验证结果失败");
				}
			}
		  }
		  catch (Exception e){
			  e.printStackTrace();
		  }
	}
}
