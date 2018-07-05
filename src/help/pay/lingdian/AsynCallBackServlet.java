package help.pay.lingdian;

import help.pay.lingdian.Config.MerConfig;
import help.pay.lingdian.Utils.SignUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AsynCallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(MerConfig.CHARSET);
		response.setCharacterEncoding(MerConfig.CHARSET);
		response.setContentType("text/html; charset=" + MerConfig.CHARSET);

		Map<String, String> resMap = SignUtil.parseParam(request, MerConfig.CHARSET);
		System.out.println("异步通知接收数据:" + SignUtil.getURLParam(resMap, MerConfig.URL_PARAM_CONNECT_FLAG, true, null));
		
		
		
		if (MerConfig.verifyAsynNotifySign(resMap)) {
			System.out.println("签名验证结果成功");
			if ("00000".equalsIgnoreCase(resMap.get("RspCod")) || "1".equalsIgnoreCase(resMap.get("Status"))) {
				// 支付成功进行处理
			} else {
				// 支付失败进行处理
			}
		} else {
			System.out.println("签名验证结果失败");
			// 签名异常处理
		}
		PrintWriter out = response.getWriter();
		response.setStatus(200);
		out.print("success");
		out.flush();
		out.close();
	}

}
