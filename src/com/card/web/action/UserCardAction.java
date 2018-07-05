package com.card.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.card.model.Card;
import com.card.service.IUserCardService;
import com.card.web.form.CardForm;
import com.card.web.form.UserCardForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.model.User;

/**
 * @author liyin
 * 
 */
public class UserCardAction extends BaseDispatchAction{
	IUserCardService userCardService = (IUserCardService) super.getService("userCardService");
	/**
	 * 入口页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		User user = super.getUser(request);
		// 组装参数
		UserCardForm userCardForm = (UserCardForm) form;
		Card card = userCardForm.getCard();
		if(StringUtil.noValue(card.getCardType())){
			card.setCardType("1");// 默认课程学习卡
		}
		PaginationSupport ps = userCardService.findUserCards(startIndex, pageSize,card, user);
		List<Card> userCardList = ps.getItems();
		Integer count = ps.getTotalCount();
		request.setAttribute("userCardList", userCardList);
		request.setAttribute("count", count);
		request.setAttribute("card", card);
		return mapping.findForward("userCard");
	}
	/**
	 * 转向激活页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward toActivateCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return mapping.findForward("userCardActivate");
	}
	/**
	 * 激活
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward activateCard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 组装参数
		String validateSn = ParamUtils.getParameter(request, "validateSn");
		boolean validateOk = this.varifyData(request, validateSn);// 验证码验证是否通过
		User user = super.getUser(request);
		String flag = "";
		String message = "";
		if(!validateOk){
			message = "验证码有误，请重新输入";
		}
		Card card = new Card();
		try{
			String cardCode = ParamUtils.getParameter(request, "cardCode");
			String cardPwd = ParamUtils.getParameter(request, "cardPwd");
			card.setCardCode(cardCode);
			card.setCardPwd(cardPwd);
			userCardService.addUserCard(card, user);// 激活学习卡
			flag = "isSuccess";
		}catch(BusinessException be){
			message = be.getMessage();
//			return this.toActivateCard(mapping, form, request, response);
		}catch(Exception e){
			message = "系统忙，请刷新后重试";
		}
		// 成功后返回
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("flag", flag);
		jsonResult.put("cardType",card.getCardType());
		jsonResult.put("message", message);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonResult.toString());
		return null;
	}
  /**
   * 获取校验码
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward getValidateSn(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession();
    // 设置页面不缓存
    response.reset();
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    // 在内存中创建图象(设置显示在页面的图像的宽度，大概一个数字设为15就可以，高度基本不变)
    int width = 60, height = 20;
    BufferedImage image = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
    // 获取图形上下文
    Graphics g = image.getGraphics();
    double shearX = 0.0;
    // 生成随机类
    Random random = new Random();
    // 设定背景色
    g.setColor(Color.yellow);
    g.fillRect(0, 0, width, height);
    // 设定字体
    Font font = new Font("Times New Roman", Font.PLAIN, 18);

    AffineTransform fontAT1 = new AffineTransform();
    Font theDerivedFont1;
    // 画边框
    g.drawRect(0, 0, width - 1, height - 1);
    // 取随机产生的认证码(4位数字)
    String sRand = "";
    for (int i = 0; i < 4; i++) {
      String rand = String.valueOf(random.nextInt(10));
      sRand += rand;
      // 将认证码显示到图象中
      g.setColor(new Color(50 + random.nextInt(110), 50 + random.nextInt(110),
          50 + random.nextInt(110)));
      fontAT1.shear(shearX, 0.0);
      theDerivedFont1 = font.deriveFont(fontAT1);
      g.setFont(theDerivedFont1);
      // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
      g.drawString(rand, 13 * i + 6, 16);
    }
    // 将认证码存入SESSION
    session.setAttribute("rand", sRand);
    // 图象生效
    g.dispose();
    // 输出图象到页面
    ImageIO.write(image, "JPEG", response.getOutputStream());
    return null;
  }
	/**
	 * 判断校验码是否正确
	 * 
	 * @param request
	 * @param inputdata
	 * @return
	 */
	private boolean varifyData(HttpServletRequest request, String inputdata) {
		String rightdata = (String) request.getSession().getAttribute("rand");
		if (StringUtil.noValue(inputdata)|| StringUtil.noValue(rightdata) || !inputdata.equals(rightdata)) {
			return false;
		}
		return true;
	}
	
}