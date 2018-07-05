package help.pay.lingdian.test.refund;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import help.pay.lingdian.Config.MerConfig;
import help.pay.lingdian.Utils.SignUtil;
/**
 * 210106-支付退款(轧差方式)
 * 调用交易 210106
 * @author Administrator
 *
 */
public class RefundReq {
		private static final String TxCode="210106";//210106
	public static void main(String[] args) {
		  try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("Version", "2.0");	//版本号
			map.put("SignMethod",MerConfig.SIGNMETHOD); //签名类型
			map.put("TxCode",TxCode);  		//交易编码			
			map.put("MerNo",MerConfig.MERNO);  //商户号-测试环境统一商户号
			//map.put("PayChannel","");  //指定渠道
			map.put("TxSN",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//商户退款请求号(唯一)
			map.put("PayTxSN","11111");//商户交易流水号(唯一)
			map.put("Amount","10");//退款金额:单位:分
			map.put("Remark","退款");//退款说明
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
						//请求成功进行处理
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
