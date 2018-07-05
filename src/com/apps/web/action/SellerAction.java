package com.apps.web.action;

import help.base.APIConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.City;
import com.apps.model.CityBusArea;
import com.apps.model.CityCommunity;
import com.apps.model.Seller;
import com.apps.model.Type;
import com.apps.service.IBaseDataService;
import com.apps.service.ISellerService;
import com.apps.service.ITypeService;
import com.apps.util.ImageUtils;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.SellerForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;

/**
 * 商家管理
 * 
 * @author Mr.zang
 * 
 */
public class SellerAction extends BaseDispatchAction {
	private final ISellerService sellerService = (ISellerService) getService("sellerService");
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final ITypeService typeService = (ITypeService) getService("typeService");

	/**
	 * 
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		int sid = ParamUtils.getIntParameter(request, "sid", 0);
		String type = ParamUtils.getParameter(request, "type");
		String title = ParamUtils.getParameter(request, "title");

		SellerForm sellerForm = (SellerForm) form;
		if (!ParamUtils.chkString(type)) {
			type = sellerForm.getType();// 1.电商模块2.订餐模块3.团购模块
		}
		String startDate = sellerForm.getStartDate();
		String endDate = sellerForm.getEndDate();
		Integer tid = sellerForm.getTid();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if (ParamUtils.chkInteger(tid)) {
			hqls.append(" and rl.tid=?");
			para.add(tid);
		}
		if (ParamUtils.chkInteger(sid)) {
			hqls.append(" and s.sid=?");
			para.add(sid);
		}
		if (ParamUtils.chkString(title)) {
			hqls.append(" and s.title like ?");
			para.add("%" + title.trim() + "%");
		}
		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and s.createDate>=?");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and s.createDate<=?");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		User user = getUser(request);
		if("9".equals(user.getUserType())){//小区管理员
			Integer ccid = user.getCcid();
			hqls.append(" and s.ccid = ? ");
			para.add(ccid);
		}

		hqls.append(" order by s.status desc,s.sid desc ");
		PaginationSupport ps = new PaginationSupport(new ArrayList(), 0,
				startIndex, pageSize);
		if (ParamUtils.chkInteger(tid)) {
			ps = sellerService.findSellerList(hqls.toString(), para,
					startIndex, pageSize);
		}

		// PaginationSupport ps = sellerService.findSellerList(hqls.toString(),
		// para, startIndex, pageSize);

		List list = ps.getItems();
		List<Type> typeList = typeService.findTypeList(type);
		request.setAttribute("type", type);
		request.setAttribute("typeList", typeList);
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}

	/**
	 * 创建
	 */
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SellerForm from = (SellerForm) form;
		String type = from.getType();
		List<Type> typeList = typeService.findTypeList(type);
		if (type.equals(Constants.MODULE_GROUP)) {
			Type type2 = typeList.get(0);
			Integer tid = type2.getTid();
			List<Type> tlist = typeService.findTypeList(tid);
			request.setAttribute("tlist", tlist);
			if (tlist != null && tlist.size() > 0) {
				Type type3 = tlist.get(0);
				List<Type> list3 = typeService.findTypeList(type3.getTid());
				request.setAttribute("list3", list3);
			}
		}
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		request.setAttribute("cityList", cityList);
		List<CityBusArea> cityBusAreaList = new ArrayList<CityBusArea>();
		request.setAttribute("cityBusAreaList", cityBusAreaList);
		List<CityCommunity> communityList = new ArrayList<CityCommunity>();
		request.setAttribute("communityList", communityList);
		request.setAttribute("typeList", typeList);
		return mapping.findForward("create");
	}
	
	public ActionForward findCityBusAreaList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<CityBusArea> cityList = baseDataService.findCityBusArea(cid);
		JSONArray list=new JSONArray();
		if(cityList!=null&&cityList.size()>0){
			for(CityBusArea cityBusArea:cityList){
				JSONObject obj = new JSONObject();// 商品
				// 商品
				obj.put("busaid",cityBusArea.getBusaid());
				obj.put("title", cityBusArea.getTitle());
				list.put(obj);
			}
			String jsonData = list.toString();
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(jsonData.toString());
		}
		return null;
	}
	
	public ActionForward findCityCommunityList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<CityCommunity> cityList = baseDataService.findCityCommunityByAId(cid);
		JSONArray list=new JSONArray();
		if(cityList!=null&&cityList.size()>0){
			for(CityCommunity cityCommunity:cityList){
				JSONObject obj = new JSONObject();// 商品
				// 商品
				obj.put("ccid",cityCommunity.getCcid());
				obj.put("title", cityCommunity.getTitle());
				list.put(obj);
			}
			String jsonData = list.toString();
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter();
			pw.write(jsonData.toString());
		}
		return null;
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
//		int[] tids = ParamUtils.getIntParameters(request, "tids", 0);
		SellerForm from = (SellerForm) form;
		String type = from.getType();
		Seller seller = from.getSeller();
		FormFile file = from.getFile();
		Integer ccid = from.getCcid();
		Integer cid = from.getCid();
		Integer busaid = from.getBusaid();
		seller.setBusaid(busaid);
		seller.setCcid(ccid);
		seller.setCid(cid);
		Integer columnId = seller.getColumnId();
		// tids[0]=tid;
		String startDate = from.getStartDate();
		String endDate = from.getEndDate();
		String format = "HH:mm";
		seller.setStartTime(DateTimeUtil.stringToDate(startDate, format));
		seller.setEndTime(DateTimeUtil.stringToDate(endDate, format));
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/seller";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			imgCut(seller, imgUrl, savePath);// 把上传的图片处理成标准尺寸的
		}

		seller = sellerService.saveSeller(seller, from);
		Integer sid = seller.getSid();
		// appSellerHtml(type, sid);// 生成静态页

		// return mapping.findForward("save");
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		path.append("&type=" + type);
		path.append("&sid=" + sid);
		path.append("&tid=" + columnId);
		return new ActionForward(forward.getName(), path.toString(), true);

	}

	/**
	 * 把上传的图片处理成标准尺寸的
	 * 
	 * @param seller
	 *            对象
	 * @param imgUrl
	 *            原图的虚拟路径
	 * @param path
	 *            file_upload 下的文件夹
	 */
	public void imgCut(Seller seller, String imgUrl, String path) {
		String webRoot = Constants.getWebRootPath();// 项目绝对地址

		String yuan = webRoot + imgUrl;// 源文件绝对地址
		// String fileName = UploadUtil.GetUniqueID();// 文件名
		String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1,
				imgUrl.length());
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		// 大图虚拟目录
		String savepath = Constants.getFileUploadPath()
				+ UploadUtil.getFolder(path) + "/" + fileName + "_b"
				+ UploadUtil.getFileExt(imgUrl);
		// 小图虚拟目录
		// String savepathMini = Constants.getFileUploadPath()
		// + UploadUtil.getFolder(path) + "/" + fileName + "_s"
		// + UploadUtil.getFileExt(imgUrl);
		// 大图
		ImageUtils.scale2(yuan, webRoot + savepath,
				Constants.IMG_HEIGHT_SELLER, Constants.IMG_WIDTH_SELLER, true);
		// ImageUtils.scale2(yuan, webRoot + savepathMini,
		// Constants.IMG_MINI_HEIGHT, Constants.IMG_MINI_WIDTH, true);
		seller.setLogo(savepath);
	}

	/**
	 * 修改
	 */
	public ActionForward preModify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		SellerForm from = new SellerForm();
		String type = ParamUtils.getParameter(request, "type");
		Integer sid = ParamUtils.getIntegerParameter(request, "sid");
		Seller seller = (Seller) sellerService.getObject(Seller.class, sid);
		from.setSeller(seller);
		from.setCid(seller.getCid());

		String format = "HH:mm:ss";
		from.setStartDate(DateTimeUtil.dateToString(seller.getStartTime(),
				format));
		from.setEndDate(DateTimeUtil.dateToString(seller.getEndTime(), format));
		List<Type> sellerTypeList = sellerService.findSellerType(sid);
		if (sellerTypeList != null && sellerTypeList.size() > 0) {
			Type type2 = sellerTypeList.get(0);
			Integer tid = type2.getTid();
			from.setTid(tid);
		}
		List<Type> typeList = typeService.findTypeList(type);

		if (type.equals(Constants.MODULE_GROUP)) {
			Type type2 = typeList.get(0);
			Integer tid = type2.getTid();
			List<Type> tlist = typeService.findTypeList(tid);
			request.setAttribute("tlist", tlist);
		}

		from.setType(type);

		if (type.equals(Constants.MODULE_GROUP)) {
			Integer tid2 = seller.getTid();
			if (ParamUtils.chkInteger(tid2)) {
				List<Type> list3 = typeService.findTypeList(tid2);
				request.setAttribute("list3", list3);
			}

		}
		request.setAttribute("typeList", typeList);
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		request.setAttribute("cityList", cityList);
		CityBusArea cityBusArea = null;
		CityCommunity cityCommunity = null;
		if(ParamUtils.chkInteger(sid)){
			Seller seller2 = (Seller)sellerService.getObject(Seller.class, sid);
			Integer ccid = seller2.getCcid();
			from.setCcid(ccid);
			if(ParamUtils.chkInteger(ccid)){
				cityCommunity = (CityCommunity)sellerService.getObject(CityCommunity.class, ccid);
				from.setCid(cityCommunity.getCid());
				from.setBusaid(cityCommunity.getBusaid());
				cityBusArea = (CityBusArea)sellerService.getObject(CityBusArea.class, cityCommunity.getBusaid());
			}
		}
		List<CityBusArea> cityBusAreaList = new ArrayList<CityBusArea>();
		if(ParamUtils.chkInteger(sid) && cityBusArea != null){
			cityBusAreaList.add(cityBusArea);
		}
		request.setAttribute("cityBusAreaList", cityBusAreaList);
		List<CityCommunity> communityList = new ArrayList<CityCommunity>();
		if(ParamUtils.chkInteger(sid) && cityCommunity != null){
			communityList.add(cityCommunity);
		}
		request.setAttribute("communityList", communityList);
		request.setAttribute("sellerTypeList", sellerTypeList);
		request.setAttribute("sellerForm", from);
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("preModify");
	}

	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sid = ParamUtils.getIntegerParameter(request, "sid");
		Seller seller = (Seller) sellerService.getObject(Seller.class, sid);
		String status = seller.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				seller.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				seller.setStatus(Constants.PUB_STATUS_OPEN);
			}
			sellerService.saveObject(seller, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 商家分享页面
	 */
	public ActionForward show(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sid = ParamUtils.getIntegerParameter(request, "sid");
		Seller seller = (Seller) sellerService.getObject(Seller.class, sid);
		if (seller != null) {
			String format = "HH:mm";
			Date startTime = seller.getStartTime();
			Date endTime = seller.getEndTime();
			String date1 = DateTimeUtil.format(startTime, format);
			String date2 = DateTimeUtil.format(endTime, format);
			request.setAttribute("date", date1 + "-" + date2);
		}

		request.setAttribute("seller", seller);
		return mapping.findForward("show");
	}

	/**
	 * 获得城市下的商家
	 */
	public void loadSeller(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<Seller> cityList = sellerService.findSellerByCid(cid);
		JSONArray items = new JSONArray();
		for (Seller seller : cityList) {
			JSONObject obj = new JSONObject();
			obj.put("sid", seller.getSid());// 商家id
			obj.put("sname", seller.getTitle());// 商家名
			items.put(obj);
		}
		JsonUtil.AjaxWriter(response, items);
	}

	/**
	 * 更改是否是热卖
	 */
	public void changeHot(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Seller seller = (Seller) sellerService.getObject(Seller.class, id);
		String isHot = seller.getIsHot();
		String flag = "success";
		try {
			if (isHot.equals(Constants.PUB_STATUS_OPEN)) {
				seller.setIsHot(Constants.PUB_STATUS_CLOSE);
			} else {
				seller.setIsHot(Constants.PUB_STATUS_OPEN);
			}
			sellerService.saveObject(seller, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 生成商家静态页面
	 */
	public void appSellerHtml(String type, Integer sid) {
		// 生成静态页面商家(商品套餐详情)

//		Seller seller = (Seller) sellerService.getObject(Seller.class, sid);
//		Map<String, Object> map = new HashMap();
//		StringBuffer hqls = new StringBuffer();
//		List<Object> para = new ArrayList<Object>();
//		hqls.append(" and s.sid =? ");
//		para.add(sid);
//		// 查询、构造数据
//		PaginationSupport ps = null;
//		if (Constants.MODULE_STORE.equals(type)) {// 电商
//			ps = sellerService.findSellerCommStore(hqls.toString(), para, 0, 5);
//			List<StoreCommDTO> list = ps.getItems();
//			map.put("list", list);
//		} else if (Constants.MODULE_EAT.equals(type)) {// 订餐
//			ps = sellerService.findSellerCommEat(hqls.toString(), para, 0, 5);
//			List<EatCommDTO> list = ps.getItems();
//			map.put("list", list);
//		} else if (Constants.MODULE_GROUP.equals(type)) {// 团购
//			ps = sellerService.findSellerCommBuy(hqls.toString(), para, 0, 5);
//			List<BuyCommDTO> list = ps.getItems();
//			map.put("list", list);
//			String format = "HH:mm";
//			Date startTime = seller.getStartTime();
//			Date endTime = seller.getEndTime();
//			String date1 = DateTimeUtil.format(startTime, format);
//			String date2 = DateTimeUtil.format(endTime, format);
//			map.put("date", date1 + "-" + date2);// 营业时间
//		}
//
//		map.put("totalCount", ps.getTotalCount());
//		map.put("seller", seller);
//		map.put("evalStar",
//				ProductUtil.BigFormatJud(seller.getEvalStar() == null ? 0
//						: seller.getEvalStar()));
//
//		if (Constants.MODULE_STORE.equals(type)) {// 电商
//			String destDir = "/app/seller/";// 目标文件路径
//			String htmlName = "seller-" + seller.getSid() + ".html";
//			String templateDir = Constants.getWebRootPath() + "/template";
//
//			boolean newpath = staticHtml(destDir, htmlName, templateDir,
//					"appStoreSellerHtml.vm", map);
//			String link = "/app/html/seller/" + htmlName;// 目标文件路径
//			seller.setLink(link);
//			System.out.println(link+">>>>>>>");
//			sellerService.saveObject(seller, null);
//		} else if (Constants.MODULE_EAT.equals(type)) {// 订餐
//			String destDir = Constants.getWebRootPath() + "/app/seller/";// 目标文件路径
//			String htmlName = "seller-" + seller.getSid() + ".html";
//			String templateDir = Constants.getWebRootPath() + "/template";
//
//			boolean newpath = staticHtml(destDir, htmlName, templateDir,
//					"appEatSellerHtml.vm", map);
//			String link = "/app/html/seller/" + htmlName;// 目标文件路径
//			seller.setLink(link);
//			sellerService.saveObject(seller, null);
//		} else if (Constants.MODULE_GROUP.equals(type)) {// 团购
//			String destDir = Constants.getWebRootPath() + "/app/seller/";// 目标文件路径
//			String htmlName = "seller-" + seller.getSid() + ".html";
//			String templateDir = Constants.getWebRootPath() + "/template";
//
//			boolean newpath = staticHtml(destDir, htmlName, templateDir,
//					"appBuySellerHtml.vm", map);
//			String link = "/app/html/seller/" + htmlName;// 目标文件路径
//			seller.setLink(link);
//			sellerService.saveObject(seller, null);
//		}

	}

	protected boolean staticHtml(String destDir, String htmlName,
			String templateDir, String templateFile, Map<String, Object> map) {
		boolean ylepath = false;
		Properties prop = new Properties();

		try {
			prop.put("file.resource.loader.path", templateDir);// 配置模版目录
			prop.load(this.getClass().getResourceAsStream(
					"/velocity.properties"));
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}

		StringBuffer str = new StringBuffer();
		FileOutputStream outStream = null;
		OutputStreamWriter writer = null;
		try {
			Velocity.init(prop);// 初始化
			createDirectory(destDir);
			VelocityContext context = new VelocityContext();
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				context.put(key, map.get(key));
			}
			// load applicationResource file
			// Enumeration<String> appSet = AppResoruce.getKeys();
			// while(appSet.hasMoreElements()){
			// String key = appSet.nextElement();
			// context.put(key,AppResoruce.get(key));
			// }
			Template template = Velocity.getTemplate(templateFile);
			// 获得模版; 注意,是在properties里面已经配好的模版目录下
			File destFile = new File(destDir, htmlName);
			outStream = new FileOutputStream(destFile);
			writer = new OutputStreamWriter(outStream, "UTF-8");
			BufferedWriter sw = new BufferedWriter(writer);
			template.merge(context, sw);
			sw.flush();
			sw.close();
			writer.close();
			outStream.close();
			ylepath = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				writer = null;
			}
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				outStream = null;
			}
		}
		return ylepath;
	}

	public static void createDirectory(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			// 文件不存在，创建
			file.mkdirs();
		}
	}
	
	/**
	 * 删除商家
	 */
	public void delSeller(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer sid = ParamUtils.getIntegerParameter(request, "sid");
		String type = ParamUtils.getParameter(request, "type");
		JSONObject jo = new JSONObject();
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(ParamUtils.chkInteger(sid)){
			 code = sellerService.delSeller(sid, type);
		}
		jo.put("code", code);
		JsonUtil.AjaxWriter(response, jo);
	}

}
