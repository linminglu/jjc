package com.card.web.action;

import help.base.APIConstants;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.UploadUtil;
import com.card.CardConstants;
import com.card.model.Card;
import com.card.model.CardGeneLog;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.card.model.dto.CardDTO;
import com.card.service.ICardService;
import com.card.web.form.CardForm;
import com.cash.model.SysBank;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.exception.BusinessException;
import com.framework.util.DateTimeUtil;
import com.framework.util.ManagerUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.util.ExcelUtil;

import help.base.APIConstants;

/**
 * @author liyin
 * 
 */
public class CardAction extends BaseDispatchAction {
	ICardService cardService = (ICardService) super.getService("cardService");
//	private final ILBSService lbsService = (ILBSService)getService("lbsService");
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
	public ActionForward initCardGift(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		// 组装参数
		CardForm cardForm = (CardForm) form;
		Card card = cardForm.getCard();
		Integer itemId=cardForm.getItemId();
		if(StringUtil.noValue(card.getCardType())){
			card.setCardType("1");
		}
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		
		StringBuffer hqls = new StringBuffer();
		if(ParamUtils.chkInteger(itemId)){
//			hqls.append("and card.itemId=");
//			hqls.append(itemId);
			paraMap.put("itemId", itemId);
		}
		PaginationSupport ps = cardService.findCards(startIndex, pageSize,card, paraMap);
//		PaginationSupport ps = cardService.findCards(hqls.toString(),startIndex, pageSize,card, paraMap);
		List<Card> cardList=new ArrayList<Card>();
		Integer count =0;
		if(ps!=null){
			cardList = ps.getItems();
			count = ps.getTotalCount();
		}
		PaginationSupport ps1 = cardService.findCardItemList(new StringBuffer().toString(), new ArrayList<Object>(), 0, 20);
		request.setAttribute("cardItemList", ps1.getItems());
		request.setAttribute("cardList", cardList);
		request.setAttribute("count", count);
		request.setAttribute("card", card);
//		request.setAttribute("cardForm", cardForm);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		return mapping.findForward("initCardGift");
	}
	/**
	 * 转向生成页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardGenerate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		// 组装参数
		CardForm cardForm = (CardForm)form;
//		CardGeneLog cardGeneLogForQuery = cardForm.getCardGeneLogForQuery();
//		Map<String, Object> paraMap = new HashMap<String, Object>();
//		PaginationSupport ps = cardService.findCardGeneLogs(startIndex, pageSize,cardGeneLogForQuery, paraMap);
//		List<CardGeneLog> cardGeneLogList =new ArrayList<CardGeneLog>();
//		Integer count =0;
//		if(ps!=null){
//			cardGeneLogList = ps.getItems();
//			count = ps.getTotalCount();
//		}
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		PaginationSupport ps1 = cardService.findCardItemList(hqls.toString(),para, 0, 20);
//		request.setAttribute("cardGeneLogList", cardGeneLogList);
		request.setAttribute("cardItemList", ps1.getItems());
//		request.setAttribute("count", count);
//		request.setAttribute("cardGeneLogForQuery", cardGeneLogForQuery);
//		request.setAttribute("flag",request.getParameter("flag"));
		request.setAttribute("	cardForm", cardForm);
		return mapping.findForward("cardGenerate");
	}
	/**
	 * 生成学习卡主方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward generateCardMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 组装参数
		User user = super.getUser(request);
		CardForm cardForm = (CardForm) form;
		CardGeneLog cardGeneLog = cardForm.getCardGeneLog();
		try{
			String startDate = cardForm.getStartDate();
			String endDate = cardForm.getEndDate();
			cardGeneLog.setCardType("1");
			cardGeneLog.setItemId(cardForm.getItemId());
			cardGeneLog.setStartDate(DateTimeUtil.StringToDate(startDate));
			cardGeneLog.setEndDate(DateTimeUtil.parse(endDate+" 23:59:59"));
			cardService.addGenerateCard(cardGeneLog, user);
		}catch(BusinessException be){
			request.setAttribute("message", be.getMessage());
			return this.cardGenerate(mapping, form, request, response);
		}catch(Exception e){
			request.setAttribute("message", e.getMessage());
			return this.cardGenerate(mapping, form, request, response);
		}
		return this.initCardGift(mapping, form, request, response);
	}
	/**
	 * 学习卡详情
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cardDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {
		// 组装参数
		String cardCode = request.getParameter("cardCode");
		CardDTO cardDTO = cardService.getCardDTO(cardCode);
		Card card=cardDTO.getCard();
		CardForm cardForm = (CardForm) form;
		CardItem cardItem=(CardItem) cardService.getObject(CardItem.class, card.getItemId());
		cardForm.setPrice(cardItem.getPrice());
		request.setAttribute("cardDTO", cardDTO);
		request.setAttribute("cardForm", cardForm);
		return mapping.findForward("cardDetail");
	}
	/**
	 * 注销
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 组装参数
		User user = super.getUser(request);
		CardForm cardForm = (CardForm) form;
		Integer[] cardIds = cardForm.getCardIds();
		try{
			cardService.removeCard(cardIds, user);
		}catch(BusinessException be){
			request.setAttribute("message", be.getMessage());
			return this.initCardGift(mapping, form, request, response);
		}
		return this.initCardGift(mapping, form, request, response);
	}
	/**
	 * 校验授权码
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkRight(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setContentType( "text/html;charset=UTF-8" );
		response.setHeader("Cache-Control","no-store");    
		response.setHeader("Pragma","no-cache");    
		response.setDateHeader("Expires", 0); 
		JSONObject json=new JSONObject();
		String key = request.getParameter("key");

		return null;
	}
	/**
	 * 导出学习卡到excel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public final ActionForward cardExport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前用户
		CardForm cardForm = (CardForm) form;
		Card card = cardForm.getCard();
		Integer itemId=cardForm.getItemId();
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		if(ParamUtils.chkInteger(itemId)){
//			hqls.append("and card.itemId=");
//			hqls.append(itemId);
			paraMap.put("itemId", itemId);
		}
		String cardType = card.getCardType();
		String modelPath = request.getSession().getServletContext().getRealPath("") + "/card/exportModel/card_export_"+"3"+".xls";
		System.out.println(modelPath);
		paraMap.put("modelPath", modelPath);
		Map<String,Object>map = cardService.cardExport(card, paraMap);
		byte[] byteArray = (byte[])map.get("byteArray");
		ExcelUtil.exportToResponseForXls(response, byteArray, "礼品卡信息_"+StringUtil.getSaveAsName());
		return null;
	}
	
	/**
	 * 验证用户是否存在
	 * @throws IOException 
	 * 
	
	 */
	public ActionForward checkUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String batchCode=request.getParameter("batchCode");
		String hql="from CardGeneLog where batchCode = " +batchCode;
		
		CardGeneLog cardGeneLog=cardService.findCardGeneLogByHql(hql);
	  
		if(cardGeneLog!=null){
			response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("yes");  
			response.getWriter().flush();
	     }
			
		return null;
	}
	/**
	 * 充值套餐列表
	 */
	public ActionForward initCardRechargeItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
				
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String title = cardForm.getTitle();
		if (ParamUtils.chkString(title)) {
			hqls.append(" and ci.title like ?");
			para.add("%"+title+"%");
		}
		Card card = cardForm.getCard();
		PaginationSupport ps = cardService.findCardRechargeItemList(hqls.toString(), para, startIndex, pageSize);
		List cardList = ps.getItems();
		
		Integer count = ps.getTotalCount();
		request.setAttribute("cardList", cardList);
		request.setAttribute("count", count.toString());
		request.setAttribute("card", card);
		return mapping.findForward("initCardRechargeItem");
	}
	

	public final ActionForward initCardItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("");
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		
		PaginationSupport ps = findCardItemList(startIndex, pageSize, para, hqls,cardForm,request);
		request.setAttribute("cardItemList",ps.getItems());
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		
		request.setAttribute("cardForm",cardForm);
		return mapping.findForward("initCardItem");
	}
	private PaginationSupport findCardItemList(int startIndex, int pageSize,
			List<Object> para, StringBuffer hqls, CardForm cardForm,HttpServletRequest request) {

		String setMealTitle = cardForm.getTitle();

		if(ParamUtils.chkString(setMealTitle)){
			hqls.append(" and ho.title like ? ");
			para.add("%" + setMealTitle + "%");
		}

		hqls.append(" order by ho.itemId desc  ");
		PaginationSupport ps = cardService.findCardItemList(hqls.toString(),para, startIndex, pageSize);
		return ps;
	}
	
	/**
	 * 添加/修改充值套餐跳转
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward preAddCardItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		saveToken(request);
		String action = ParamUtils.getAttribute(request, "action");
		Integer itemId = ParamUtils.getIntegerParameter(request, "itemId");
		CardForm cardForm = (CardForm) form;
		if (ParamUtils.chkInteger(itemId)) {
			CardItem r = new CardItem();
			r = cardService.getCardItemById(itemId);
			cardForm.setParValue(r.getParValue());
			cardForm.setPrice(r.getPrice());
			cardForm.setTitle(r.getTitle());
			cardForm.setItemId(r.getItemId());
			cardForm.setRemarks(r.getRemarks());
			cardForm.setCardItem(r);
			cardForm.setImgUrl(r.getImgUrl());
		}

		request.setAttribute("action", action);
		request.setAttribute("setMealId", itemId);
		request.setAttribute("cardForm", cardForm);
		return mapping.findForward("preAddCardItem");
	}
	/**
	 * 保存充值套餐
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveCardItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String infoType = ParamUtils.getParameter(request, "infoType");
		Integer itemId = ParamUtils.getIntegerParameter(request, "itemId");
		
		CardItem cardItem = new CardItem();
		CardForm cardForm = (CardForm) form;
		
		FormFile file = cardForm.getFile();
		

		cardItem.setTitle(cardForm.getTitle());
		cardItem.setParValue(cardForm.getParValue());
		cardItem.setPrice(cardForm.getPrice());
		cardItem.setRemarks(cardForm.getRemarks()==null?"":cardForm.getRemarks());
		if (ParamUtils.chkInteger(itemId)) {
			cardItem.setItemId(itemId);
			cardService.saveCardItem(cardItem);
		}
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/card";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			cardItem.setImgUrl(imgUrl);
		}
		cardService.saveCardItem(cardItem);
		ActionForward forward = mapping.findForward("saveCardItem");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		return new ActionForward(forward.getName(),path.toString(),true);
	}
	public ActionForward delCardItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer itemId = ParamUtils.getIntegerParameter(request, "itemId");
		
		if (ParamUtils.chkInteger(itemId)) {
			cardService.delCardItemByItemId(itemId);
		}
		
		ActionForward forward = mapping.findForward("delCardItem");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		return new ActionForward(forward.getName(),path.toString(),true);
	}

	/**
	 * 添加/修改充值套餐跳转
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward preAddCardRechargeItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		saveToken(request);
		String action = ParamUtils.getAttribute(request, "action");
		Integer itemId = ParamUtils.getIntegerParameter(request, "itemId");
		CardForm cardForm = (CardForm) form;
		if (ParamUtils.chkInteger(itemId)) {
			CardRechargeItem cardItem = (CardRechargeItem)cardService.getObject(CardRechargeItem.class,itemId);
			cardForm.setCardRechargeItem(cardItem);
		}
		
		request.setAttribute("action", action);
		request.setAttribute("cardForm", cardForm);
		return mapping.findForward("preAddCardRechargeItem");
	}
	/**
	 * 保存充值
	 */
	public ActionForward saveCardRechargeItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		saveToken(request);
		CardForm cardForm = (CardForm) form;
		CardRechargeItem cardRechargeItem = cardForm.getCardRechargeItem();
		if (ParamUtils.chkInteger(cardRechargeItem.getItemId())) {
			cardService.updateCardRechargeItem(cardRechargeItem);
		}else{
			cardService.saveCardRechargeItem(cardRechargeItem);
		}
		request.setAttribute("cardForm", cardForm);
		return mapping.findForward("saveCardRechargeItem");
	}
	/**
	 * 删除充值套餐
	 */
	public ActionForward delCardRechargeItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer itemId = ParamUtils.getIntegerParameter(request, "itemId");
		
		if (ParamUtils.chkInteger(itemId)) {
			cardService.deleteObject(CardRechargeItem.class, itemId, null);
		}
		
		ActionForward forward = mapping.findForward("delCardRechargeItem");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		return new ActionForward(forward.getName(),path.toString(),true);
	}
	/**
	 * 在线充值订单管理
	 */
	public ActionForward initROrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
				
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String orderNum = cardForm.getOrderNum();
		String userName = cardForm.getUserName();
		String payStatus = cardForm.getPayStatus();
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ro.createTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ro.createTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and ro.orderNum like ?");
			para.add("%"+orderNum.trim()+"%");
		}
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(payStatus)) {
			hqls.append(" and ro.payStatus = ?");
			para.add(payStatus);
		}
		hqls.append(" and( ro.payType = ? or ro.payType =? )");
		para.add("1"); // 1=支付宝
		para.add("5"); // 5=微信
		hqls.append(" order by ro.createTime desc");
		Card card = cardForm.getCard();
		PaginationSupport ps = cardService.findRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
		List cardOrderList = ps.getItems();
		Integer count = ps.getTotalCount();
		request.setAttribute("cardOrderList", cardOrderList);
		request.setAttribute("count", count.toString());
		request.setAttribute("card", card);
		request.setAttribute("userName", userName);
		request.setAttribute("payStatus", payStatus);
		return mapping.findForward("initROrder");
	}
	
	/**
	 * 在线充值订单管理
	 */
	public ActionForward exportROrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
				
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String orderNum = cardForm.getOrderNum();
		String userName = cardForm.getUserName();
		String payStatus = cardForm.getPayStatus();
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ro.createTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ro.createTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and ro.orderNum like ?");
			para.add("%"+orderNum.trim()+"%");
		}
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(payStatus)) {
			hqls.append(" and ro.payStatus = ?");
			para.add(payStatus);
		}
		hqls.append(" and( ro.payType = ? or ro.payType =? )");
		para.add("1"); // 1=支付宝
		para.add("5"); // 5=微信
		hqls.append(" order by ro.createTime desc");
		Card card = cardForm.getCard();
		startIndex = 0;
		pageSize = 999999;
		PaginationSupport ps = cardService.findRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
		List cardOrderList = ps.getItems();
		
		// 导出数据
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		HSSFCellStyle style1 = wb.createCellStyle(); 
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font1 = wb.createFont();    
//				font1.setFontName("仿宋_GB2312");    
		style1.setFont(font1);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
//				font2.setFontHeightInPoints((short) 12);  
		HSSFCellStyle style2 = wb.createCellStyle(); 
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		int width = 256;
		wb.setSheetName(0, "在线订单表");

		int rowNum = 0;
		int columeNum = 0;
		
		HSSFRow row = sheet.createRow(rowNum++);
		HSSFCell cell = null;
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"序号");
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"订单号");
		setCellWidthByZhCotent(sheet, columeNum-1, "4171123142702163905");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"用户");
		setCellWidthByZhCotent(sheet, columeNum-1, "17681881255");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"标题");
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"支付方式");
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"付款");
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"付款金额");
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"下单时间");
		setCellWidthByZhCotent(sheet, columeNum-1, "2017-11-23 14:27:09");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"付款时间");
		setCellWidthByZhCotent(sheet, columeNum-1, "2017-11-23 14:27:09");
		
		
		int i = 1;
		for(Object obj:cardOrderList){
			CardDTO dto = (CardDTO) obj;
			row = sheet.createRow(rowNum++);
			columeNum = 0;
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,i);
			i++;
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeOrder().getOrderNum());
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCreateUser().getLoginName()!=null?dto.getCreateUser().getLoginName():dto.getCreateUser().getUserName());
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeItem().getTitle());
			
			
			cell = row.createCell(columeNum++);
			String payTypeStr = "";
			if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_ALIPAY)){
				payTypeStr = "支付宝";
			}else if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_UNIONPAY)){
				payTypeStr = "银联";
			}else if(dto.getCardRechargeOrder().getPayType().equals("3")){
				payTypeStr = "线下支付";
			}else if(dto.getCardRechargeOrder().getPayType().equals("4")){
				payTypeStr = "积分";
			}else if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_WECHAT)){
				payTypeStr = "微信";
			}
			setCellValue(style2,cell,payTypeStr);
			
			String payStatusStr = "";
			if(dto.getCardRechargeOrder().getPayStatus().equals("1")){
				payStatusStr="未付款";
			}else if(!dto.getCardRechargeOrder().getPayStatus().equals("1")){
				payStatusStr="已付款";
			}
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,payStatusStr);
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeOrder().getTotalMoney());
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,DateTimeUtil.DateToStringAll(dto.getCardRechargeOrder().getCreateTime()));
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,DateTimeUtil.DateToStringAll(dto.getCardRechargeOrder().getPayTime()));
			
		}
		try {
			String exportName = "线上订单表";
			OutputStream out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition",
					"attachment; filename=" + StringUtil.toUtf8String(exportName) + "_"
							+ DateTimeUtil.getDateTime("yyyy-MM-dd") + ".xls");
			wb.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Integer count = ps.getTotalCount();
//		request.setAttribute("cardOrderList", cardOrderList);
//		request.setAttribute("count", count.toString());
//		request.setAttribute("card", card);
//		request.setAttribute("userName", userName);
//		request.setAttribute("payStatus", payStatus);
		return null;
	}
	
	/**
	 * 线下充值订单管理
	 */
	public ActionForward initofflineOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		String userId = ParamUtils.getParameter(request, "userId");		
		
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String orderNum = cardForm.getOrderNum();
		String userName = cardForm.getUserName();
		String payStatus = cardForm.getPayStatus();
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ro.createTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ro.createTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and ro.orderNum like ?");
			para.add("%"+orderNum.trim()+"%");
		}
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}else if(ParamUtils.chkString(userId)){
			hqls.append(" and upper(u.userId) = ? ");
			para.add(userId.trim().toUpperCase());
			userName = userId;
			cardForm.setUserName(userName);
		}
		if (ParamUtils.chkString(payStatus)) {
			hqls.append(" and ro.payStatus = ?");
			para.add(payStatus);
		}
		hqls.append(" and (ro.payType =? or  ro.payType =?) ");
		para.add("3"); //线下充值
		para.add("7"); //线下充值
		hqls.append(" order by ro.createTime desc");
		Card card = cardForm.getCard();
//		PaginationSupport ps = cardService.findRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
		PaginationSupport ps = cardService.findOfflineRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
		List cardOrderList = ps.getItems();
		Integer count = ps.getTotalCount();
		request.setAttribute("cardOrderList", cardOrderList);
		request.setAttribute("count", count.toString());
		request.setAttribute("card", card);
		request.setAttribute("userName", userName);
		request.setAttribute("payStatus", payStatus);
		return mapping.findForward("initofflineOrder");
	}
	
	/**
	 * 导出线下充值订单管理
	 */
	public ActionForward exportOfflineOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String orderNum = cardForm.getOrderNum();
		String userName = cardForm.getUserName();
		String payStatus = cardForm.getPayStatus();
		String startDate = cardForm.getStartDate();
		String endDate = cardForm.getEndDate();
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ro.createTime>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ro.createTime<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and ro.orderNum like ?");
			para.add("%"+orderNum.trim()+"%");
		}
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(payStatus)) {
			hqls.append(" and ro.payStatus = ?");
			para.add(payStatus);
		}
		hqls.append(" and (ro.payType =? or  ro.payType =?) ");
		para.add("3"); //线下充值
		para.add("7"); //线下充值
		hqls.append(" order by ro.createTime desc");
		Card card = cardForm.getCard();
		startIndex =0;
		pageSize = 9999999;
		log.info("____________查询开始时间"+DateTimeUtil.DateToStringAll(new Date()));
		PaginationSupport ps = cardService.findOfflineRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
		log.info("____________查询结束时间"+DateTimeUtil.DateToStringAll(new Date()));
		List cardOrderList = ps.getItems();
		
		// 导出数据
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		HSSFCellStyle style1 = wb.createCellStyle(); 
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font1 = wb.createFont();    
//				font1.setFontName("仿宋_GB2312");    
		style1.setFont(font1);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
//				font2.setFontHeightInPoints((short) 12);  
		HSSFCellStyle style2 = wb.createCellStyle(); 
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		int width = 256;
		wb.setSheetName(0, "线下订单表");
		int rowNum = 0;
		int columeNum = 0;
		
		HSSFRow row = sheet.createRow(rowNum++);
		HSSFCell cell = null;
		
		// 表头 第一行
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"序号");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"订单号");
		setCellWidthByZhCotent(sheet, columeNum-1, "4171219110111507495");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"id");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"用户");
		setCellWidthByZhCotent(sheet, columeNum-1, "12300000010");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"余额");
		setCellWidthByZhCotent(sheet, columeNum-1, "101518.000");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"支付方式");
		setCellWidthByZhCotent(sheet, columeNum-1, "线下支付");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"处理状态");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"付款金额");
		setCellWidthByZhCotent(sheet, columeNum-1, "100000.00");
		
		cell = row.createCell(columeNum++);
		setCellValue(style1,cell,"下单时间");
		setCellWidthByZhCotent(sheet, columeNum-1, "2017-12-19 11:01:29");
		
		log.info("____________导出开始时间"+DateTimeUtil.DateToStringAll(new Date()));
		int i = 1;
		for(Object obj:cardOrderList){
			CardDTO dto = (CardDTO) obj;
			row = sheet.createRow(rowNum++);
			columeNum = 0;
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,i);
			i++;
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeOrder().getOrderNum());
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeOrder().getUserId());
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCreateUser().getLoginName()!=null?dto.getCreateUser().getLoginName():dto.getCreateUser().getUserName());
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCreateUser().getMoney());
			
			cell = row.createCell(columeNum++);
			String payTypeStr = "";
			if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_ALIPAY)){
				payTypeStr = "支付宝";
			}else if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_UNIONPAY)){
				payTypeStr = "银联";
			}else if(dto.getCardRechargeOrder().getPayType().equals("3")){
				payTypeStr = "线下支付";
			}else if(dto.getCardRechargeOrder().getPayType().equals("4")){
				payTypeStr = "积分";
			}else if(dto.getCardRechargeOrder().getPayType().equals(APIConstants.PAY_TYPE_WECHAT)){
				payTypeStr = "微信";
			}else if(dto.getCardRechargeOrder().getPayType().equals("7")){
				payTypeStr = "支付宝/微信/QQ扫码支付";
			}
			setCellValue(style2,cell,payTypeStr);
			
			String payStatusStr = "";
			if(dto.getCardRechargeOrder().getPayStatus().equals("0")){
				payStatusStr = "未处理";
			}else if(dto.getCardRechargeOrder().getPayStatus().equals("1")){
				payStatusStr = "已拒绝";
			}else if(dto.getCardRechargeOrder().getPayStatus().equals("2")){
				payStatusStr = "已充值";
			}
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,payStatusStr);
			
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,dto.getCardRechargeOrder().getTotalMoney());
			cell = row.createCell(columeNum++);
			setCellValue(style2,cell,DateTimeUtil.DateToStringAll(dto.getCardRechargeOrder().getCreateTime()));
			
		}
		
		try {
			String exportName = "线下订单表";
			OutputStream out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition",
					"attachment; filename=" + StringUtil.toUtf8String(exportName) + "_"
							+ DateTimeUtil.getDateTime("yyyy-MM-dd") + ".xls");
			wb.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("____________导出结束时间"+DateTimeUtil.DateToStringAll(new Date()));
		return null;
	}
	
	/**
	 * 礼品卡订单
	 */
	public ActionForward initCardItemOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
				
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		CardForm cardForm = (CardForm) form;
		String cardCode = cardForm.getCardCode();
		String userName = cardForm.getUserName();
		String orderNum = cardForm.getOrderNum();
		if (ParamUtils.chkString(cardCode)) {
			hqls.append(" and c.cardCode = ?");
			para.add(cardCode);
		}
		if (ParamUtils.chkString(orderNum)) {
			hqls.append(" and co.orderNum = ?");
			para.add(orderNum);
		}
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		hqls.append(" order by co.createTime desc");
		Card card = cardForm.getCard();
		PaginationSupport ps = cardService.findCardItemOrderList(hqls.toString(), para, startIndex, pageSize);
		List cardOrderList = ps.getItems();
		System.out.println(cardOrderList);
		request.setAttribute("cardOrderList", cardOrderList);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("card", card);
		request.setAttribute("userName", userName);
		request.setAttribute("cardCode", cardCode);
		return mapping.findForward("initCardItemOrder");
	}
	/**
	 *  订单确认发货
	 */
	public ActionForward deliver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		CardItemOrder order = (CardItemOrder)cardService.getObject(CardItemOrder.class, id);
		if(order != null){
			order.setPayStatus(CardConstants.ORDER_USDE_SUCCESS);//  3表示待收货   后台显示就是已发货
			cardService.saveObject(order, null);
		}
		
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("deliver");
	}
	/**
	 * 礼品卡订单详情
	 */
	public ActionForward viewCardItemOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		CardDTO cardDTO = cardService.getCardItemOrder(id);
		request.setAttribute("cardDTO", cardDTO);
		return mapping.findForward("viewCardItemOrder");
	}

	
	public ActionForward viewofflineOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "orderId");
//		CardDTO cardDTO = cardService.getCardItemOrder(id);
		CardRechargeOrder order=(CardRechargeOrder) cardService.getObject(CardRechargeOrder.class, id);
		User user=(User) cardService.getObject(User.class, order.getUserId());
		request.setAttribute("order", order);
		request.setAttribute("user", user);
		CardForm cardForm = (CardForm) form;
		cardForm.setUser(user);
		cardForm.setItemId(id);
		cardForm.setType(order.getPayType());
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		cardForm.setUserpoints(nf.format(order.getTotalMoney()));
		saveToken(request);
		return mapping.findForward("viewofflineOrder");
	}
	/**
	 * 线上订单详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	public ActionForward viewCardRechargeOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws ServletException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "orderId");
//		CardDTO cardDTO = cardService.getCardItemOrder(id);
		CardRechargeOrder order=(CardRechargeOrder) cardService.getObject(CardRechargeOrder.class, id);
		User user=(User) cardService.getObject(User.class, order.getUserId());
		request.setAttribute("order", order);
		request.setAttribute("user", user);
		
		CardForm cardForm = (CardForm) form;
		cardForm.setUser(user);
		cardForm.setItemId(id);
		cardForm.setCardRechargeOrder(order);
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		cardForm.setUserpoints(nf.format(order.getTotalMoney()));
		return mapping.findForward("viewCardRechargeOrder");
	}
	
	/**
	 * 修改并保存用户余额
	 */
//	public ActionForward modifyBalance(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		CardForm cardForm = (CardForm) form;
//		User user = cardForm.getUser();
//		if (isTokenValid(request, true)) {
//			Integer itemId=cardForm.getItemId();
//			BigDecimal userpoints=new BigDecimal(cardForm.getUserpoints());
//			Integer userId = user.getUserId();
//			userService.saveUserBalance_(userId, userpoints, "1",itemId);
//		}else{
//			request.setAttribute("error", "NOT_SUBMIT_FORM");// 表单重复提交
//		}
//		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
//		ActionForward forward = mapping.findForward("modifyBalance");
//		StringBuffer path = new StringBuffer();
//		path.append(forward.getPath());
//		path.append("&pager.offset=" + startIndex);
//		
//		return new ActionForward(forward.getName(), path.toString(), true);
//	}
	
	public ActionForward refuse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		String reason = ParamUtils.getParameter(request, "reason");
		
		CardRechargeOrder order = (CardRechargeOrder)cardService.getObject(CardRechargeOrder.class, id);
		if(order != null){
			order.setPayStatus("1");//拒绝
			order.setReason(reason);
			cardService.saveObject(order, null);
		}
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("refuse");
	}
	
	public void setCellValue(HSSFCellStyle style,HSSFCell cell,Object value){
		if(value!=null){
			cell.setCellValue(""+value);
			cell.setCellStyle(style);
		}else{
			cell.setCellValue("");
			cell.setCellStyle(style);
		}
	}
	// 设置中文内容的单元格宽度
	public void setCellWidthByZhCotent(HSSFSheet sheet,Integer columnNum,String content){
		int width = 256;
		sheet.setColumnWidth(columnNum,(content.length()+3)*width*2);
	}
	
	/**
	 * 审核用户线下充值
	 */
	public void updateRechargeOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		Integer orderId = ParamUtils.getIntegerParameter(request, "orderId");
		String status=ParamUtils.getParameter(request, "status");
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		String loginUserType = loginUser.getUserType();//登录用户类型
		//管理员和超级管理员才能查询
		if(Constants.USER_TYPE_ADMIN.equals(loginUserType)
				||Constants.USER_TYPE_SUPERADMIN.equals(loginUserType)||Constants.USER_TYPE_CUS_SERVICE.equals(loginUserType)){
			if(ParamUtils.chkInteger(orderId)&&ParamUtils.chkString(status)){
				try{
					String keyWord="";
					CardRechargeOrder order = (CardRechargeOrder)cardService.getObject(CardRechargeOrder.class, orderId);
					order.setOperator(loginUser.getLoginName());
					if("2".equals(status)){	
						cardService.updateUserOfflineRecharge(order,"1");
						keyWord="拒绝";
					}else if("1".equals(status)){
						cardService.updateUserOfflineRecharge(order,"2");
						keyWord="通过";
					}
					
					StringBuffer loginText = new StringBuffer();
					loginText.append("[审核用户线下充值]：操作人登录名[");
					loginText.append(loginUser.getLoginName());
					loginText.append("]，操作人ID[");
					loginText.append(loginUser.getUserId());
					loginText.append("]，申请人的ID[");
					loginText.append(order.getUserId());
					loginText.append("]，金额[");
					loginText.append(order.getTotalMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					loginText.append("]，审核操作[");
					loginText.append(keyWord);
					loginText.append("]，该条申请记录id[");
					loginText.append(order.getOrderId());
					loginText.append("]");
					userService.updateUserLog(request,loginUser,loginText.toString());			
					
					code=APIConstants.CODE_REQUEST_SUCCESS;
					message="该充值申请已"+keyWord;
				}catch(Exception e){
					message="审核出错！";
				}	
			}else{
				message="参数有误";
			}
		}else{
			message="无权限！请联系管理员！";
		}
		data.put("code", code);
		data.put("msg", message);
		JsonUtil.AjaxWriter(response, data);
	}
	/**
	 * 查询新线下订单条数。
	 */
	public void loadNewOfflineOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		int num = 0;//未处理订单数量
		int unLockNum = 0;//未处理订单中未锁定部分的数量
		User loginUser = (User) request.getSession(false).getAttribute("loginUser");//登录的用户
		if(loginUser!=null){
			String loginUserType = loginUser.getUserType();//登录用户类型
			//管理员和超级管理员才能查询
			if(ManagerUtils.checkRole(loginUserType)){
				int startIndex = 0;
				int pageSize = 999;
	
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				
				hqls.append(" and ro.payStatus = ?");
				para.add(Constants.PAY_STATUS_UNHANDLE);//未处理
				hqls.append(" and (ro.payType =? or  ro.payType =? or  ro.payType =? or  ro.payType =? or  ro.payType =?) ");
				para.add("3"); //线下充值
				para.add("7"); //线下充值
				para.add("8"); //线下微信充值
				para.add("9"); //线下支付宝充值
				para.add("10"); //支付宝转银行卡充值
	
				PaginationSupport ps = cardService.findOfflineRechargeOrderList(hqls.toString(), para, startIndex, pageSize);
				@SuppressWarnings("unchecked")
				List<CardDTO> list = ps.getItems();
				num = list.size();
				
				for(CardDTO dto:list){
					CardRechargeOrder order = dto.getCardRechargeOrder();
					String isLock = order.getIsLock();
					if(Constants.PUB_STATUS_CLOSE.equals(isLock)){
						unLockNum++;
					}
				}
				
				code=APIConstants.CODE_REQUEST_SUCCESS;
				message="操作成功！";
			}else{
				message="无权限！请联系管理员！";
			}
		}
		data.put("code", code);
		data.put("msg", message);
		data.put("num", num);
		data.put("unLockNum", unLockNum);
		JsonUtil.AjaxWriter(response, data);
	}
}