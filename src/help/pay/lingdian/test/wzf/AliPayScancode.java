package help.pay.lingdian.test.wzf;
import help.pay.lingdian.Config.MerConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import help.pay.lingdian.Utils.SignUtil;

/**
 * 支付宝扫码支付(正扫) 商家在平台下订单生成二维码 -》消费者扫描支付
 * 调用交易 210110-机构统一下单
 * @author Administrator
 *
 */
public class AliPayScancode {
		private static final String TxCode="210110";//210110-机构统一下单
		private static final String ProductId="0602";//产品类型:0602-支付宝扫码
	public static void main(String[] args) {
		  try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("Version", "2.0");	//版本号
			map.put("SignMethod",MerConfig.SIGNMETHOD); //签名类型
			map.put("TxCode",TxCode);  		//交易编码			
			map.put("MerNo",MerConfig.MERNO);  //商户号-测试环境统一商户号
			//map.put("PayChannel","");  //指定渠道
			map.put("ProductId",ProductId);//产品类型:0602-支付宝扫码
			map.put("TxSN",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//商户交易流水号(唯一)
			map.put("Amount","100");//金额:单位:分
			map.put("PdtName","面包");//商品名称
			map.put("Remark","测试");//备注
			map.put("StoreId","01");//门店号
			map.put("TerminalId","001");//终端号
			map.put("NotifyUrl",MerConfig.NOTIFYURL);//异步通知URL
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
						String ImgUrl=resMap.get("ImgUrl");
						String CodeUrl=resMap.get("CodeUrl");
						System.out.println("二维码图片地址:"+ImgUrl+"_____CodeUrl::"+CodeUrl);
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
