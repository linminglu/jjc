package com.apps.web.action;

import help.base.APIConstants;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.BlackListCacheUtil;
import com.apps.eff.CacheUtil;
import com.apps.eff.PayCacheUtil;
import com.apps.model.City;
import com.apps.model.CityBusArea;
import com.apps.model.CityCommunity;
import com.apps.model.LotterySetting;
import com.apps.model.LotterySettingRl;
import com.apps.model.Param;
import com.apps.model.PayConfig;
import com.apps.model.Push;
import com.apps.model.Seller;
import com.apps.model.SysOption;
import com.apps.model.Version;
import com.apps.model.dto.BaseDataDTO;
import com.apps.model.dto.SellerDTO;
import com.apps.service.IBaseDataService;
import com.apps.service.IParamService;
import com.apps.util.JsonUtil;
import com.apps.util.UploadUtil;
import com.apps.web.form.BaseDataForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.service.impl.ServiceLocatorImpl;
import com.framework.util.DateTimeUtil;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetOption;
import com.game.model.GaBetSponsor;
import com.game.model.GaSessionInfo;
import com.game.model.dto.GaBetOptionDto;
import com.game.service.IGaService;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.fivecolor.model.GfFiveGaSession;
import com.gf.fivecolor.service.IFiveService;
import com.gf.pick11.gdpick11.model.GfGdPick11GaSession;
import com.gf.pick11.gdpick11.service.IGdPick11Service;
import com.gf.ssc.cqssc.model.GfCqSscGaSession;
import com.gf.ssc.cqssc.service.ICqSscService;
import com.gf.three.model.GfThreeGaSession;
import com.gf.three.service.IThreeService;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;
import com.ram.model.User;
import com.ram.model.UserLog;
import com.ram.model.dto.MonthlyStatictisDto;

/**
 * 基本功能
 * 
 * @author Mr.zang
 * 
 */
public class BaseDataAction extends BaseDispatchAction {
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final IParamService paramService = (IParamService) getService("paramService");
	private final IThreeService gfThreeService = (IThreeService) getService("gfThreeService");
	private final IFiveService gfFiveService = (IFiveService) getService("gfFiveService");
	//private final IDcbService gfDcbService = (IDcbService) getService("gfDcbService");
	private final ICqSscService gfCqSscService = (ICqSscService) getService("gfCqSscService");
	private final IGdPick11Service gfGdPick11Service = (IGdPick11Service) getService("gfGdPick11Service");
	// private final ISellerService sellerService = (ISellerService)
	// getService("sellerService");
	// private final IStoreOrderService storeOrderService = (IStoreOrderService)
	// getService("storeOrderService");
	// private final IEatOrderService eatOrderService = (IEatOrderService)
	// getService("eatOrderService");
	// private final IBuyOrderService buyOrderService = (IBuyOrderService)
	// getService("buyOrderService");
	// private final IMytService mytService = (IMytService)
	// getService("mytService");
	private final static IGaService gaService = (IGaService) ServiceLocatorImpl
	.getInstance().getService("gaService");
	/**
	 * 版本管理
	 */
	public ActionForward verInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String type = managerForm.getType();

		if (ParamUtils.chkString(type)) {
			hqls.append(" and v.type= ? ");
			para.add(type);
		}

		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and v.createDate>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and v.createDate<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}

		PaginationSupport ps = baseDataService.findVerList(hqls.toString(),
				para, startIndex, pageSize);
		List<SellerDTO> items = ps.getItems();

		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("verInit");
	}

	/**
	 * 设置当前版本
	 */
	public void setVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		String status = ParamUtils.getParameter(request, "status");
		String flag = "success";
		try {
			Version version = (Version) baseDataService.getObject(
					Version.class, id);
			String type = version.getType();
			baseDataService.updateVerNot(type);
			if (ParamUtils.chkString(status)) {
				if (status.equals(Constants.PUB_STATUS_OPEN)) {
					version.setIsDef(Constants.PUB_STATUS_CLOSE);
				} else {
					version.setIsDef(Constants.PUB_STATUS_OPEN);
				}
			} else {
				version.setIsDef(Constants.PUB_STATUS_OPEN);
			}
			baseDataService.saveObject(version, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 创建
	 */
	public ActionForward createVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("createVer");
	}

	/**
	 * 修改
	 */
	public ActionForward preModifyVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm managerForm = (BaseDataForm) form;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Version ver = (Version) baseDataService.getObject(Version.class, id);
		managerForm.setVersion(ver);
		request.setAttribute("startIndex",
				ParamUtils.getIntParameter(request, "pager.offset", 0));
		request.setAttribute("maxPageItems", ParamUtils.getIntParameter(
				request, "maxPageItems", RamConstants.MAXPAGEITEMS));
		return mapping.findForward("modifVer");
	}

	public ActionForward saveVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm managerForm = (BaseDataForm) form;
		Version version = managerForm.getVersion();
		// version = baseDataService.saveVersion(version);
		String isDef = version.getIsDef();
		String type = version.getType();
		String flag = version.getFlag();
		String link = version.getLink();
		String ver = version.getVer();
		String content = version.getContent();

		// 如果设置了是当前版本，则更新全部的状态
		if (isDef.equals(Constants.PUB_STATUS_OPEN)) {
			Integer id = version.getId();
			if (ParamUtils.chkInteger(id)) {
				version = (Version) baseDataService
						.getObject(Version.class, id);
				// version.setCreateDate(ver.getCreateDate());
				version.setFlag(flag);
				version.setVer(ver);
				version.setLink(link);
				version.setType(type);
				version.setContent(content);
			}
			// baseDataService.updateVerNot(type);
			version.setIsDef(Constants.PUB_STATUS_OPEN);
			baseDataService.saveObject(version, null);
		}

		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("saveVer");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	/**
	 * 反馈信息列表
	 */
	public ActionForward feedbackInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;
		String title = managerForm.getTitle();
		String name = managerForm.getName();
		if (ParamUtils.chkString(name)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(name.trim().toUpperCase());
			para.add(name.trim().toUpperCase());
			para.add(name.trim().toUpperCase());
		}
		if (ParamUtils.chkString(title)) {
			hqls.append(" and upper(f.content) like ? ");
			para.add("%" + title.trim().toUpperCase() + "%");
		}

		PaginationSupport ps = baseDataService.findFeedbackList(
				hqls.toString(), para, startIndex, pageSize);
		List<BaseDataDTO> items = ps.getItems();

		request.setAttribute("list", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("feedbackInit");
	}

	/**
	 * 推送列表
	 */
	public ActionForward pushInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;

		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String type = managerForm.getType();
		String deviceType = managerForm.getDeviceType();
		String title = managerForm.getTitle();
		if (ParamUtils.chkString(title)) {
			hqls.append(" and upper(push.title) like ? ");
			para.add("%" + title.trim().toUpperCase() + "%");
		}

		if (ParamUtils.chkString(deviceType)) {
			hqls.append(" and push.deviceType= ? ");
			para.add(deviceType);
		}
		if (ParamUtils.chkString(type)) {
			hqls.append(" and push.type= ? ");
			para.add(type);
		}

		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and push.createTime>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and push.createTime<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}
		PaginationSupport ps = baseDataService.findPushList(hqls.toString(),
				para, startIndex, pageSize);
		List<Push> items = ps.getItems();
		List<Push> list = new ArrayList<Push>();
		for (Push push : items) {
			String type2 = push.getType();
			if (type2.equals("2")) {// 2代表链接的是商家。
				Integer sid = push.getInfoId();
				Seller seller = (Seller) baseDataService.getObject(
						Seller.class, sid);
				if (seller != null) {
					String sname = seller.getTitle();
					push.setSname(sname);
				}
			}
			list.add(push);
		}
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("pushInit");
	}

	public ActionForward prePush(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		// List<Seller> sellerList = sellerService
		// .findAllList(Constants.PUB_STATUS_OPEN);
		// request.setAttribute("sellerList", sellerList);
		return mapping.findForward("prePush");
	}

	public void savePush(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			BaseDataForm managerForm = (BaseDataForm) form;
			Push push = managerForm.getPush();
			baseDataService.savePush(push);
			StringBuffer path = new StringBuffer(
					"baseDataAction.do?method=pushInit");
			String msg = "推送成功！";
			path.append("&msg=" + msg);
			PrintWriter out = response.getWriter();
			out.print("<script>parent.location.href='" + path.toString()
					+ "'</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 城市管理
	 */
	public ActionForward cityManager(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String type = baseDataForm.getType();
		String keywords = baseDataForm.getKeywords();

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		if (ParamUtils.chkString(type)) {
			hqls.append(" and c.type=? ");
			para.add(type);
		}
		if (ParamUtils.chkString(keywords)) {
			hqls.append(" and c.title like ? ");
			para.add("%" + keywords + "%");
		}

		hqls.append(" order by c.pinyin,c.sort desc");
		PaginationSupport ps = baseDataService.findCity(hqls.toString(), para,
				startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("type", type);
		return mapping.findForward("cityManager");
	}

	/**
	 * 商圈管理
	 */
	public ActionForward cityBusAreaManager(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String keywords = baseDataForm.getKeywords();
		Integer cid = baseDataForm.getCid();

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if (ParamUtils.chkInteger(cid)) {
			hqls.append(" and c.cid =  ? ");
			para.add(cid);
		}

		if (ParamUtils.chkString(keywords)) {
			hqls.append(" and c.title like ? ");
			para.add("%" + keywords + "%");
		}
		hqls.append(" order by c.sort desc,c.busaid desc");
		PaginationSupport ps = baseDataService.findCityBusArea(hqls.toString(),
				para, startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		request.setAttribute("cityList", cityList);
		request.setAttribute("cid", cid + "");
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("cityBusAreaManager");
	}

	/**
	 * 小区管理
	 */
	public ActionForward cityCommUnityManager(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String keywords = baseDataForm.getKeywords();
		Integer cid = baseDataForm.getCid();
		Integer busaid = baseDataForm.getBusaid();

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		if (ParamUtils.chkInteger(cid)) {
			hqls.append(" and c.cid =  ? ");
			para.add(cid);
		}
		if (ParamUtils.chkInteger(busaid)) {
			hqls.append(" and c.busaid =  ? ");
			para.add(busaid);
		}
		if (ParamUtils.chkString(keywords)) {
			hqls.append(" and c.title like ? ");
			para.add("%" + keywords + "%");
		}

		hqls.append(" order by c.sort desc,c.ccid desc");
		PaginationSupport ps = baseDataService.findCityCommunity(
				hqls.toString(), para, startIndex, pageSize);
		List list = ps.getItems();
		request.setAttribute("list", list);
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		request.setAttribute("cityList", cityList);
		List<CityBusArea> cityBusAreaList = new ArrayList<CityBusArea>();
		if (ParamUtils.chkInteger(busaid)) {
			CityBusArea cityBusArea = (CityBusArea) baseDataService.getObject(
					CityBusArea.class, busaid);
			cityBusAreaList.add(cityBusArea);
			baseDataForm.setBusaid(busaid);
		}
		request.setAttribute("cityBusAreaList", cityBusAreaList);
		request.setAttribute("cid", cid + "");
		request.setAttribute("busaid", busaid + "");
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("cityCommUnityManager");
	}

	/**
	 * 增加or修改
	 */
	public ActionForward preAddCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		if (ParamUtils.chkInteger(cid)) {
			City city = (City) baseDataService.getObject(City.class, cid);
			BaseDataForm baseDataForm = (BaseDataForm) form;
			type = city.getType();
			baseDataForm.setCode(city.getCode());
			baseDataForm.setCity(city);
		}

		if (ParamUtils.chkString(type) && type.equals(Constants.CITY_QU)) {
			// 如果是辖区的话，获得城市列表
			List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
			request.setAttribute("cityList", cityList);
		}

		request.setAttribute("type", type);
		return mapping.findForward("preAddCity");
	}

	/**
	 * 添加 / 修改商圈
	 */
	public ActionForward preAddCityBusArea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		if (ParamUtils.chkInteger(cid)) {
			CityBusArea city = (CityBusArea) baseDataService.getObject(
					CityBusArea.class, cid);
			BaseDataForm baseDataForm = (BaseDataForm) form;
			baseDataForm.setCityBusArea(city);
			Integer cid2 = city.getCid();
			baseDataForm.setCid(cid2);
		}
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		request.setAttribute("cityList", cityList);
		request.setAttribute("type", type);
		return mapping.findForward("preAddCityBusArea");
	}

	/**
	 * 添加 / 修改小区
	 */
	public ActionForward preAddCityCommunity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		// List<CityBusArea> cityBusAreaList =
		// baseDataService.findCityBusArea();
		// List<EstateCompany> estateCompanyList =
		// baseDataService.findEstateCompany();
		List<CityBusArea> cityBusAreaList = new ArrayList<CityBusArea>();
		if (ParamUtils.chkInteger(cid)) {
			CityCommunity city = (CityCommunity) baseDataService.getObject(
					CityCommunity.class, cid);
			BaseDataForm baseDataForm = (BaseDataForm) form;
			baseDataForm.setCityCommunity(city);
			Integer busaid = city.getBusaid();
			Integer cid2 = city.getCid();
			Integer companyId = city.getCompanyId();
			baseDataForm.setBusaid(busaid);
			baseDataForm.setCid(cid2);
			baseDataForm.setCompanyId(companyId);
			CityBusArea cityBusArea = (CityBusArea) baseDataService.getObject(
					CityBusArea.class, busaid);
			cityBusAreaList.add(cityBusArea);
		}
		// request.setAttribute("estateCompanyList", estateCompanyList);
		request.setAttribute("cityList", cityList);
		request.setAttribute("cityBusAreaList", cityBusAreaList);
		return mapping.findForward("preAddCityCommunity");
	}

	public ActionForward findCityBusAreaList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		List<CityBusArea> cityList = baseDataService.findCityBusArea(cid);
		JSONArray list = new JSONArray();
		if (cityList != null && cityList.size() > 0) {
			for (CityBusArea cityBusArea : cityList) {
				JSONObject obj = new JSONObject();// 商品
				// 商品
				obj.put("busaid", cityBusArea.getBusaid());
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

	/**
	 * 保存城市or辖区
	 */
	public ActionForward saveCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		City city = baseDataForm.getCity();
		String type = city.getType();
		String title = city.getTitle();
		// String code="";
		// code=GoogleGeocoder.getGLGeocoderCityCode(title);
		// System.out.println(code+">>>>>");
		// String code = city.getCode();
		// if (ParamUtils.chkString(code)) {
		// code = code.replaceAll("\\s*", "");
		// }
		city.setCode(title);
		city = baseDataService.saveCity(city);
		baseDataForm.setKeywords(title);
		baseDataForm.setType(type);
		return cityManager(mapping, baseDataForm, request, response);
	}

	/**
	 * 保存商圈
	 */
	public ActionForward saveCityBusArea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		CityBusArea cityBusArea = baseDataForm.getCityBusArea();
		Integer cid = baseDataForm.getCid();
		cityBusArea.setCid(cid);
		if (ParamUtils.chkInteger(cityBusArea.getBusaid())) {
			baseDataService.updateCityBusArea(cityBusArea);
		} else {
			cityBusArea.setSort("0");
			cityBusArea.setStatus("1");
			baseDataService.saveCityBusArea(cityBusArea);
		}
		// return cityManager(mapping, baseDataForm, request, response);
		return mapping.findForward("saveCityBusArea");
	}

	/**
	 * 保存小区
	 */
	public ActionForward saveCityCommunity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		CityCommunity cityCommunity = baseDataForm.getCityCommunity();
		Integer busaid = baseDataForm.getBusaid();
		Integer companyId = baseDataForm.getCompanyId();
		Integer cid = baseDataForm.getCid();
		cityCommunity.setCid(cid);
		cityCommunity.setBusaid(busaid);
		cityCommunity.setCompanyId(companyId);
		if (ParamUtils.chkInteger(cityCommunity.getCcid())) {
			baseDataService.updateCityCommunity(cityCommunity);
		} else {
			cityCommunity.setSort("0");
			cityCommunity.setStatus("1");
			baseDataService.saveCityCommunity(cityCommunity);
		}
		// return cityManager(mapping, baseDataForm, request, response);
		return mapping.findForward("saveCityCommunity");
	}

	/**
	 * 删除城市or辖区
	 */
	public ActionForward delCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		baseDataService.delCity(cid);
		baseDataForm.setType(type);
		return cityManager(mapping, baseDataForm, request, response);
	}

	/**
	 * 删除商圈
	 */
	public ActionForward delCityBusArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		// String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		baseDataService.deleteObject(CityBusArea.class, cid, null);
		baseDataForm.setCid(null);
		return cityBusAreaManager(mapping, baseDataForm, request, response);
		// return mapping.findForward("delCityBusArea");
	}

	/**
	 * 删除小区
	 */
	public ActionForward delCityCommunity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		// String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		baseDataService.deleteObject(CityCommunity.class, cid, null);
		baseDataForm.setCid(null);
		return cityCommUnityManager(mapping, baseDataForm, request, response);
		// return mapping.findForward("delCityCommunity");
	}

	/**
	 * 更改状态
	 */
	public void changeCityStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "cid");
		City city = (City) baseDataService.getObject(City.class, id);
		String status = city.getStatus();
		String flag = "success";
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				city.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				city.setStatus(Constants.PUB_STATUS_OPEN);
			}
			baseDataService.saveObject(city, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 城市排序
	 */
	public ActionForward orderCity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		String sortType = ParamUtils.getParameter(request, "sortType");// 1.升序
																		// 2.降序
		String type = ParamUtils.getParameter(request, "type");// 1.城市 2.辖区
		City city = (City) baseDataService.getObject(City.class, cid);
		if (city != null) {
			String sort = city.getSort();
			if (!ParamUtils.chkString(sort)) {
				sort = "0";
			}
			int orderno = Integer.parseInt(sort);
			if (sortType.equals("1")) {// 升序
				orderno = orderno + 1;
			} else {// 降序
				if (orderno > 0) {
					orderno = orderno - 1;
				}
			}
			city.setSort(orderno + "");
			baseDataService.saveObject(city, null);
		}
		baseDataForm.setType(type);
		return cityManager(mapping, form, request, response);
	}

	public ActionForward orderCityBusArea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		String sortType = ParamUtils.getParameter(request, "sortType");// 1.升序
		CityBusArea city = (CityBusArea) baseDataService.getObject(
				CityBusArea.class, cid);
		if (city != null) {
			String sort = city.getSort();
			if (!ParamUtils.chkString(sort)) {
				sort = "0";
			}
			int orderno = Integer.parseInt(sort);
			if (sortType.equals("1")) {// 升序
				orderno = orderno + 1;
			} else {// 降序
				if (orderno > 0) {
					orderno = orderno - 1;
				}
			}
			city.setSort(orderno + "");
			baseDataService.saveObject(city, null);
		}
		baseDataForm.setCid(null);
		return cityBusAreaManager(mapping, form, request, response);
	}

	public ActionForward orderCityCommunity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer cid = ParamUtils.getIntegerParameter(request, "cid");
		String sortType = ParamUtils.getParameter(request, "sortType");// 1.升序
																		// 2.降序
		CityCommunity city = (CityCommunity) baseDataService.getObject(
				CityCommunity.class, cid);
		if (city != null) {
			String sort = city.getSort();
			if (!ParamUtils.chkString(sort)) {
				sort = "0";
			}
			int orderno = Integer.parseInt(sort);
			if (sortType.equals("1")) {// 升序
				orderno = orderno + 1;
			} else {// 降序
				if (orderno > 0) {
					orderno = orderno - 1;
				}
			}
			city.setSort(orderno + "");
			baseDataService.saveObject(city, null);
		}
		baseDataForm.setCid(null);
		return cityCommUnityManager(mapping, form, request, response);
	}

	/**
	 * 判断city的编码是否存在
	 */
	public void citycode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String code = ParamUtils.getParameter(request, "code");
		String flag = null;
		if (baseDataService.findCityByCode(code) == false) {
			flag = "error";
		} else {
			flag = "success";
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 积分设置列表
	 */
	public ActionForward initIntegral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		String optionType = ParamUtils.getParameter(request, "optionType");
		User user = getUser(request);

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		BaseDataForm ddhlForm = (BaseDataForm) form;
		String optionTitle = ddhlForm.getOptionTitle();
		if ("3".equals(optionType)) {
			hqls.append(" and s.optionType = '3'");
		}
		if ("26".equals(optionType)) {
			hqls.append(" and s.optionType = '26'");
		}
		if ("27".equals(optionType)) {
			hqls.append(" and s.optionType = '27'");
		}
		if (ParamUtils.chkString(optionTitle)) {
			hqls.append(" and s.optionTitle like ?");
			para.add("%" + optionTitle + "%");
		}
		request.setAttribute("user", user);
		hqls.append(" order by s.optionId desc");
		PaginationSupport ps = baseDataService.findIntegralList(
				hqls.toString(), para, startIndex, pageSize);
		request.setAttribute("list", ps.getItems());
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		request.setAttribute("optionType", optionType);
		return mapping.findForward("initIntegral");
	}

	/**
	 * 积分 添加、修改跳转
	 */
	public ActionForward preAddIntegral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		removeFormBean(mapping, request);
		Integer optionId = ParamUtils.getIntegerParameter(request, "optionId");
		String action = ParamUtils.getParameter(request, "action");
		String optionType = ParamUtils.getParameter(request, "optionType");
		BaseDataForm ddhlForm = (BaseDataForm) form;
		if (ParamUtils.chkInteger(optionId)) {
			SysOption sysOption = (SysOption) baseDataService.getObject(
					SysOption.class, optionId);
			ddhlForm.setSysOption(sysOption);
		}
		request.setAttribute("baseDataForm", ddhlForm);
		request.setAttribute("action", action);
		request.setAttribute("optionType", optionType);
		return mapping.findForward("preAddIntegral");
	}

	/**
	 * 保存积分
	 */
	public ActionForward saveIntegral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		SysOption sysOption = baseDataForm.getSysOption();
		Integer optionId = sysOption.getOptionId();
		String optionType = sysOption.getOptionType();
		BigDecimal points = sysOption.getPoints();
		String content = sysOption.getContent();
		if (ParamUtils.chkInteger(optionId)) {
			// baseDataService.updateIntegral(sysOption);
			// baseDataService.updateIntegralPoints(points,optionId);
			SysOption option = (SysOption) baseDataService.getObject(
					SysOption.class, optionId);
			option.setPoints(points);
			option.setContent(content);
			baseDataService.saveObject(option, null);
		} else {
			baseDataService.saveIntegral(sysOption);
		}
		ActionForward forward = mapping.findForward("saveIntegral");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&optionType=" + optionType);
		return new ActionForward(forward.getName(), path.toString(), true);
		// return mapping.findForward("saveIntegral");
	}

	/**
	 * 积分明细
	 */
	public ActionForward initUserPointDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String userName = managerForm.getUserName();
		String tradeType = managerForm.getTradeType();
		String cashType = managerForm.getCashType();
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(tradeType)) {
			hqls.append(" and ud.tradeType= ? ");
			para.add(tradeType);
		}
		if (ParamUtils.chkString(cashType)) {
			hqls.append(" and ud.cashType = ? ");
			para.add(cashType);
		}

		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and ud.createTime>= ? ");
			para.add(startTime.trim());
			// request.setAttribute("startDate", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and ud.createTime<=? ");
			para.add(endTime.trim());
			// request.setAttribute("endDate", endTime);
		}
		hqls.append(" order by ud.createTime desc ");
		PaginationSupport ps = baseDataService.findUserPointDetail(
				hqls.toString(), para, startIndex, pageSize);
		List<BaseDataDTO> list = new ArrayList<BaseDataDTO>();
		List<BaseDataDTO> items = ps.getItems();
		for (BaseDataDTO baseDataDTO : items) {
			String cashType2 = baseDataDTO.getUserPointDetail().getCashType();
			String moduleType = Constants.getPointTradeTypeNameZh(cashType2);
			baseDataDTO.setModuleType(moduleType);
			list.add(baseDataDTO);
		}
		request.setAttribute("list", list);
		request.setAttribute("userName", userName);
		request.setAttribute("tradeType", tradeType);
		request.setAttribute("cashType", cashType);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("initUserPointDetail");
	}

	/**
	 * 资金明细
	 */
	public ActionForward initUserTradeDetail(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String userName = managerForm.getUserName();
		String tradeType = managerForm.getTradeType();
		String cashType = managerForm.getCashType();
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(tradeType)) {
			hqls.append(" and ud.tradeType= ? ");
			para.add(tradeType);
		}
		if (ParamUtils.chkString(cashType)) {
			hqls.append(" and ud.cashType = ? ");
			para.add(cashType);
		}
		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and ud.createTime>= ? ");
			para.add(startTime.trim());
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and ud.createTime<=? ");
			para.add(endTime.trim());
		}
		hqls.append(" order by ud.createTime desc ");
		PaginationSupport ps = baseDataService.findUserTradeDetail(
				hqls.toString(), para, startIndex, pageSize);
		List<BaseDataDTO> list = new ArrayList<BaseDataDTO>();
		List<BaseDataDTO> items = ps.getItems();
		for (BaseDataDTO baseDataDTO : items) {
			String cashType2 = baseDataDTO.getUserTradeDetail().getCashType();
			String moduleType = Constants.getCashTradeTypeNameZh(cashType2);
			baseDataDTO.setModuleType(moduleType);
			list.add(baseDataDTO);
		}
		request.setAttribute("list", list);
		request.setAttribute("userName", userName);
		request.setAttribute("tradeType", tradeType);
		request.setAttribute("cashType", cashType);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("initUserTradeDetail");
	}

	/**
	 * 结算明细
	 */
	public ActionForward initUserCheckout(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm managerForm = (BaseDataForm) form;
		String startTime = managerForm.getStartDate();
		String endTime = managerForm.getEndDate();
		String userName = managerForm.getUserName();
		String status = managerForm.getStatus();
		String moduleType = managerForm.getModuleType();
		if (ParamUtils.chkString(userName)) {
			hqls.append(" and (upper(u.userName) = ? OR upper(u.userId) = ? OR upper(u.loginName) = ? ) ");
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
			para.add(userName.trim().toUpperCase());
		}
		if (ParamUtils.chkString(status)) {
			hqls.append(" and uc.status= ? ");
			para.add(status);
		}
		if (ParamUtils.chkString(moduleType)) {
			hqls.append(" and uc.moduleType= ? ");
			para.add(moduleType);
		}
		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and uc.createTime>= ? ");
			para.add(startTime.trim());
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and uc.createTime<=? ");
			para.add(endTime.trim());
		}
		hqls.append(" order by uc.createTime desc ");
		PaginationSupport ps = baseDataService.findUserCheckout(
				hqls.toString(), para, startIndex, pageSize);
		List<BaseDataDTO> items = ps.getItems();

		request.setAttribute("list", items);
		request.setAttribute("userName", userName);
		request.setAttribute("status", status);
		request.setAttribute("moduleType", moduleType);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		managerForm.setStartIndex(startIndex + "");
		return mapping.findForward("initUserCheckout");
	}

	/**
	 * 结算订单
	 */
	public ActionForward checkoutDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		// int startIndex = ParamUtils.getIntParameter(request, "pager.offset",
		// 0);
		// int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
		// RamConstants.MAXPAGEITEMS);
		// BaseDataForm baseDataForm = (BaseDataForm) form;
		// Integer id = ParamUtils.getIntegerParameter(request, "id");
		// UserCheckout userCheckout =
		// (UserCheckout)baseDataService.getObject(UserCheckout.class, id);
		// String moduleType = null;
		// if(userCheckout != null){
		// moduleType = userCheckout.getModuleType();
		// }
		// List<UserCheckoutOrderRl> list = baseDataService.findOrderList(id);
		// List<Object> para = new ArrayList<Object>();
		// StringBuffer hqls = new StringBuffer();
		// List orderList = new ArrayList();
		// String moduleType2 = baseDataForm.getModuleType();
		// if(moduleType2 != null){
		// moduleType = moduleType2;
		// }
		// String ids = "";
		// HQUtils hq = new HQUtils();
		// if(list != null && list.size() > 0){
		// for (UserCheckoutOrderRl userCheckoutOrderRl : list) {
		// Integer oid = userCheckoutOrderRl.getOid();
		// ids = ids + oid + ",";
		// }
		// ids = ids.substring(0, ids.length()-1);
		// }
		// if(Constants.MODULE_STORE.equals(moduleType)){//电商
		// hqls.append(" and o.oid in ("+ids+")");
		// PaginationSupport ps =
		// storeOrderService.findOrderList(hqls.toString(),
		// para, startIndex, pageSize);
		// orderList = ps.getItems();
		// baseDataForm.setModuleType(moduleType);
		// request.setAttribute("list", orderList);
		// request.setAttribute("id", id.toString());
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// baseDataForm.setStartIndex(startIndex + "");
		// return mapping.findForward("storeOrderList");
		// }else if(Constants.MODULE_EAT.equals(moduleType)){//订餐
		// hqls.append(" and o.oid in ("+ids+")");
		// PaginationSupport ps = eatOrderService.findOrderList(hqls.toString(),
		// para, startIndex, pageSize);
		// orderList = ps.getItems();
		// baseDataForm.setModuleType(moduleType);
		// request.setAttribute("list", orderList);
		// request.setAttribute("id", id.toString());
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// baseDataForm.setStartIndex(startIndex + "");
		// return mapping.findForward("eatOrderList");
		// }else if(Constants.MODULE_GROUP.equals(moduleType)){//团购
		// hqls.append(" and o.oid in ("+ids+")");
		// PaginationSupport ps = buyOrderService.findOrderList(hqls.toString(),
		// para, startIndex, pageSize);
		// orderList = ps.getItems();
		// baseDataForm.setModuleType(moduleType);
		// request.setAttribute("list", orderList);
		// request.setAttribute("id", id.toString());
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// baseDataForm.setStartIndex(startIndex + "");
		// return mapping.findForward("buyOrderList");
		// }else if(Constants.MODULE_PUBLISH_INFO.equals(moduleType)){//供求
		// hqls.append(" and ho.userIndentId in ("+ids+")");
		// PaginationSupport ps =
		// mytService.findDdhlUserBuyList(hqls.toString(),
		// para, startIndex, pageSize);
		// orderList = ps.getItems();
		// baseDataForm.setModuleType(moduleType);
		// request.setAttribute("userJfList", orderList);
		// request.setAttribute("id", id.toString());
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// baseDataForm.setStartIndex(startIndex + "");
		// return mapping.findForward("mytUserIndentList");
		// }
		return null;

	}

	/**
	 * 统计今天投注、开奖总额
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward gftotalBet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// int startIndex = ParamUtils.getIntParameter(request, "pager.offset",
		// 0);
		// int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
		// RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		User user = getUser(request);
		String userType = user.getUserType();
		// if (userType.equals(Constants.USER_TYPE_ADMIN)) {
		Date date = new Date();
		hqls.append(" and p.buyTime>= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 00:00:00");
		hqls.append(" and p.buyTime <= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 23:59:59");
//		hqls.append(" and ho.winResult != ?  ");
//		para.add(BjPk10Constants.BJ_PK10_OPEN_STATUS_INIT); // 已开奖
//		hqls.append(" and ho.betFlag = ?  ");
//		para.add("1"); //有效投注，未退款的
		List<BaseDataDTO> list = baseDataService.findTotalBet(hqls.toString(),
				para);
		// PaginationSupport ps = baseDataService.findTotalBet(hqls.toString(),
		// para,
		// startIndex, pageSize);
		// List list = ps.getItems();
		// if(){
		//
		// }
		request.setAttribute("list", list);
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// bj3Form.setStartIndex(startIndex + "");
		// }

		return mapping.findForward("gftotalBet");
	}

	/**
	 * 统计今天投注、开奖总额
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward totalBet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// int startIndex = ParamUtils.getIntParameter(request, "pager.offset",
		// 0);
		// int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
		// RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		User user = getUser(request);
		String userType = user.getUserType();
		// if (userType.equals(Constants.USER_TYPE_ADMIN)) {
		Date date = new Date();
		hqls.append(" and ho.createTime>= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 00:00:00");
		hqls.append(" and ho.createTime <= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 23:59:59");
//		hqls.append(" and ho.winResult != ?  ");
//		para.add(Constants.OPEN_STATUS_OPENED); // 已开奖
//		hqls.append(" and ho.betFlag = ?  ");
//		para.add("1"); //有效投注，未退款的
		List<BaseDataDTO> list = baseDataService.findTotalBet(hqls.toString(),
				para);
		// PaginationSupport ps = baseDataService.findTotalBet(hqls.toString(),
		// para,
		// startIndex, pageSize);
		// List list = ps.getItems();
		// if(){
		//
		// }
		request.setAttribute("list", list);
		// request.setAttribute("count", (ps.getTotalCount() + "").toString());
		// bj3Form.setStartIndex(startIndex + "");
		// }

		return mapping.findForward("totalBet");
	}

	public ActionForward dayBetCount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		BaseDataForm baseDataForm = (BaseDataForm) form;
		String startDate = baseDataForm.getStartDate();
		String endDate = baseDataForm.getEndDate();


		if (ParamUtils.chkString(startDate)) {
			hqls.append(" and ho.createTime >= ? ");
			para.add(DateTimeUtil.parse(startDate + " 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			hqls.append(" and ho.createTime <= ? ");
			para.add(DateTimeUtil.parse(endDate + " 23:59:59"));
		}
		User user = getUser(request);
		String userType = user.getUserType();
		// if (userType.equals(Constants.USER_TYPE_ADMIN)) {
		// hqls.append(" and ho.betTime< ? ");
		// para.add(DateTimeUtil.DateToStringAll(new Date()));
		// hqls.append(" group by ho.sessionId order by ho.sessionId desc ");
//		hqls.append(" order by ho.createTime  desc ");
		PaginationSupport ps = baseDataService.findGaDayBetCountList(
				hqls.toString(), para, startIndex, pageSize);
		List list = new ArrayList();
		if (ps != null) {
			list = ps.getItems();
		}
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		baseDataForm.setStartIndex(startIndex + "");
		baseDataForm.setStartIndex(startDate);
		baseDataForm.setStartIndex(endDate);
		// }
		return mapping.findForward("dayBetCount");
	}

	public ActionForward initSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		request.setAttribute("baseDataForm", baseDataForm);
		return mapping.findForward("initSessionNo");
	}

	/**
	 * 支付设置
	 */
	public ActionForward paySet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		BaseDataForm baseDataForm = (BaseDataForm) form;
		hqls.append(" order by pc.type ");
		List<PayConfig> list = null;
		list = baseDataService.findPayConfig();
		PayConfig renxin = null;
		PayConfig shanfu = null;
		PayConfig amx = null;

		if(list.size() >0){
			for(int i =0; i<list.size(); i++){
				if(list.get(i).getType().equals("1")){
					renxin = list.get(i);
				}else if(list.get(i).getType().equals("2")){
					shanfu = list.get(i);
				}else if(list.get(i).getType().equals("3")){
					amx = list.get(i);
				}
			}
		}
		Param param = paramService.getParamByType(Constants.PARAM_DEF_PAY);
		String value = Constants.DEF_PAY_COMPANY; //默认是艾米森支付。
		
		if(param != null){
			value = param.getValue();			
		}
		baseDataForm.setAmx(amx);
		baseDataForm.setRenxin(renxin);
		baseDataForm.setShanfu(shanfu);
		baseDataForm.setCheck(value);
		request.setAttribute("baseDataForm", baseDataForm);
		return mapping.findForward("paySet");
	}

	/**
	 * 保存默认支付设置
	 */
	@SuppressWarnings("unchecked")
	public void saveDefPay(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "false";
		String defPay = ParamUtils.getParameter(request, "defPay"); // 默认支付方式
		if (defPay != null && defPay.length() > 0) {
			Param param = paramService.getParamByType(Constants.PARAM_DEF_PAY); // 1是设置默认收款公司方式。
			if (param != null) {
				param.setValue(defPay);
				paramService.saveObject(param, null);
				flag = "success";
			}
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	
	/**
	 * 保存支付设置
	 */
	public ActionForward savePaySet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String partner = null;
		String secretKey = null; // 密钥
		String srcCode = null; // 终端id, 商户唯一标识
		String payType = null; // 收款公司type	

		if(ParamUtils.chkString(baseDataForm.getRenxin().getPar())){
			partner = baseDataForm.getRenxin().getPar();
			secretKey = baseDataForm.getRenxin().getPayKey(); // 密钥
//			srcCode = baseDataForm.getRenxin().getCode(); // 终端id, 商户唯一标识
			payType = baseDataForm.getRenxin().getType(); // 收款公司type
			if(!ParamUtils.chkString(payType)){
				payType = "1";
			}
		}else if(ParamUtils.chkString(baseDataForm.getShanfu().getPar())){
			partner = baseDataForm.getShanfu().getPar();
			secretKey = baseDataForm.getShanfu().getPayKey(); // 密钥
			srcCode = baseDataForm.getShanfu().getCode(); // 终端id, 商户唯一标识
			payType = baseDataForm.getShanfu().getType(); // 收款公司type	
			if(!ParamUtils.chkString(payType)){
				payType = "2";
			}
		}else{
			partner = baseDataForm.getAmx().getPar();
			secretKey = baseDataForm.getAmx().getPayKey(); // 密钥
			srcCode = baseDataForm.getAmx().getCode(); // 终端id, 商户唯一标识
			payType = baseDataForm.getAmx().getType(); // 收款公司type	
			if(!ParamUtils.chkString(payType)){
				payType = "3";
			}
		}

		if (ParamUtils.chkString(payType)) {
			PayConfig pay = baseDataService.getPayByType(payType);
			if (pay == null) {
				pay = new PayConfig();
				if (ParamUtils.chkString(payType)) {
					pay.setType(payType);
				}
			}
			if (ParamUtils.chkString(partner)) {
				pay.setPar(partner);

			}
			if (ParamUtils.chkString(srcCode)) {
				pay.setCode(srcCode);
			}
			if (ParamUtils.chkString(secretKey)) {
				pay.setPayKey(secretKey);
			}
			baseDataService.saveObject(pay, null);
			PayCacheUtil.updateConfigList();
		}
		return mapping.findForward("savePaySet");
	}
	
	/**
	 * 手动开奖
	 */
	public ActionForward open(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		return mapping.findForward("open");
	}
	
	/**
	 * 输入订单号手动开奖
	 */
	@SuppressWarnings("unchecked")
	public void openOrderNum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "false";
		String orderNum = ParamUtils.getParameter(request, "orderNo");
		HQUtils hq = new HQUtils(" from GaBetSponsor sp where 1=1 ");
		hq.addHsql(" and sp.orderNum = ? ");
		hq.addPars(orderNum);
		
		GaBetSponsor sp = (GaBetSponsor) baseDataService.getObject(hq);
		if(sp == null){
			flag = "opened";
		}else{
			String gameType = sp.getGameType();
			Integer sessionId = sp.getSessionId();
			String openResult = "";
			String openStatus = "";
			GfThreeGaSession three = null;
			GfFiveGaSession five = null;
			GfDcbGaSession dcb = null;
			GfGdPick11GaSession pick11 = null;
			GfCqSscGaSession cqSsc = null;
			if(Constants.GAME_TYPE_GF_THREE.equals(gameType)){
				three = (GfThreeGaSession) baseDataService.getObject(GfThreeGaSession.class, sessionId);
				openStatus = three.getOpenStatus();
				openResult = three.getOpenResult();
			}else if(Constants.GAME_TYPE_GF_FC.equals(gameType)){
				five = (GfFiveGaSession) baseDataService.getObject(GfFiveGaSession.class, sessionId);
				openStatus = five.getOpenStatus();
				openResult = five.getOpenResult();

			}else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)){
				dcb = (GfDcbGaSession) baseDataService.getObject(GfDcbGaSession.class, sessionId);
				openStatus = dcb.getOpenStatus();
				openResult = dcb.getOpenResult();

			}else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)){
				pick11 = (GfGdPick11GaSession) baseDataService.getObject(GfGdPick11GaSession.class, sessionId);
				openStatus = pick11.getOpenStatus();
				openResult = pick11.getOpenResult();

			}else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
				cqSsc = (GfCqSscGaSession) baseDataService.getObject(GfCqSscGaSession.class, sessionId);
				openStatus = cqSsc.getOpenStatus();
				openResult = cqSsc.getOpenResult();
			}
			if(Constants.OPEN_STATUS_INIT.equals(openStatus)){
				flag = "false";
			}else{
				if(Constants.PUB_STATUS_OPEN.equals(sp.getBetFlag())&&Constants.INIT.equals(sp.getWinResult())){
					if(Constants.GAME_TYPE_GF_THREE.equals(gameType)){
                        gfThreeService.updateThreeSessionOpenResultMethod(three, openResult, orderNum);
					}else if(Constants.GAME_TYPE_GF_FC.equals(gameType)){
						gfFiveService.updateFiveSessionOpenResultMethod(five, openResult, orderNum);

//					}else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)){
//						gfDcbService.updateGfDcbGaSessionOpenResultMethod(dcb, openResult, orderNum);
					}else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)){
						gfGdPick11Service.updateGdPick11SessionOpenResultMethod(pick11, openResult, orderNum);
					}else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
						gfCqSscService.updateCqSscSessionOpenResultMethod(cqSsc, openResult, orderNum);
					}
					flag = "success";
					
					// 获取当前用户
					User loginUser = null;
					loginUser = (User) request.getSession(false).getAttribute("loginUser");
					StringBuffer loginText = new StringBuffer();
					loginText.append("手动开奖：操作人");
					loginText.append(loginUser.getLoginName());
					loginText.append("[");
					loginText.append(loginUser.getUserId());
					loginText.append("]，给订单号[");
					loginText.append(orderNum);
					loginText.append("]手动开奖");
					userService.updateUserLog(request,loginUser,loginText.toString());
					
				}else{
					flag = "opened";
				}
			}
			
		}
		JsonUtil.AjaxWriter(response, flag);
	}

	/**
	 * 期号开奖
	 */
	public ActionForward openGaBetSponsor(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception,
			NoFunctionPermissionException {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		request.setAttribute("baseDataForm", baseDataForm);
		return mapping.findForward("openGaBetSponsor");
	}
	
	/**
	 * 输入期号手动开奖
	 */
	@SuppressWarnings("unchecked")
	public void openSessionNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String flag = "false";
		String sessionNo = ParamUtils.getParameter(request, "sessionNo");
		String gameType = ParamUtils.getParameter(request, "gameType");
		
		String openResult = "";
		String openStatus = "";
		GfThreeGaSession three = null;
		GfFiveGaSession five = null;
		GfDcbGaSession dcb = null;
		GfGdPick11GaSession pick11 = null;
		GfCqSscGaSession cqSsc = null;

		if(Constants.GAME_TYPE_GF_THREE.equals(gameType)){
			three = gfThreeService.getPreviousSessionBySessionNo(sessionNo);
			openStatus = three.getOpenStatus();
			openResult = three.getOpenResult();
		}else if(Constants.GAME_TYPE_GF_FC.equals(gameType)){
			five = gfFiveService.getPreviousSessionBySessionNo(sessionNo);
			openStatus = five.getOpenStatus();
			openResult = five.getOpenResult();

//		}else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)){
//			dcb = gfDcbService.getPreviousSessionBySessionNo(sessionNo);
//			openStatus = dcb.getOpenStatus();
//			openResult = dcb.getOpenResult();

		}else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)){
			pick11 = gfGdPick11Service.getPreviousSessionBySessionNo(sessionNo);
			openStatus = pick11.getOpenStatus();
			openResult = pick11.getOpenResult();

		}else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
			cqSsc = gfCqSscService.getPreviousSessionBySessionNo(sessionNo);
			openStatus = cqSsc.getOpenStatus();
			openResult = cqSsc.getOpenResult();
		}
		
		if(Constants.OPEN_STATUS_INIT.equals(openStatus)||Constants.OPEN_STATUS_OPENING.equals(openStatus)){
			flag = "false";
		}else{
			if(Constants.GAME_TYPE_GF_THREE.equals(gameType)){
				gfThreeService.updateCountJointBet(sessionNo);
                gfThreeService.updateThreeSessionOpenResultMethod(three, openResult, null);
			}else if(Constants.GAME_TYPE_GF_FC.equals(gameType)){
				gfFiveService.updateCountJointBet(sessionNo);
				gfFiveService.updateFiveSessionOpenResultMethod(five, openResult, null);

//			}else if(Constants.GAME_TYPE_GF_DCB.equals(gameType)){
//				gfDcbService.updateCountJointBet(sessionNo);
//				gfDcbService.updateGfDcbGaSessionOpenResultMethod(dcb, openResult, null);
			}else if(Constants.GAME_TYPE_GF_GDPICK11.equals(gameType)){
				gfGdPick11Service.updateCountJointBet(sessionNo);
				gfGdPick11Service.updateGdPick11SessionOpenResultMethod(pick11, openResult, null);
			}else if(Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
				gfCqSscService.updateCountJointBet(sessionNo);
				gfCqSscService.updateCqSscSessionOpenResultMethod(cqSsc, openResult, null);
			}
			flag = "success";
			
			// 获取当前用户
			User loginUser = null;
			loginUser = (User) request.getSession(false).getAttribute("loginUser");
			StringBuffer loginText = new StringBuffer();
			loginText.append("手动开奖：操作人");
			loginText.append(loginUser.getLoginName());
			loginText.append("[");
			loginText.append(loginUser.getUserId());
			loginText.append("]，给期号[");
			loginText.append(sessionNo);
			loginText.append("]手动开奖");
			userService.updateUserLog(request,loginUser,loginText.toString());
		}
		JsonUtil.AjaxWriter(response, flag);
	}
	

	/**
	 * 统计今日用户投注中奖及盈利额
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userDayPayoff(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
	    int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String userName = baseDataForm.getUserName();
	    List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		if(ParamUtils.chkString(userName)){
			hqls.append(" and (upper(ho.loginName) =  ?)");
			para.add(userName.trim().toUpperCase());
		}
		Date date = new Date();
		hqls.append(" and ho.createTime>= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 00:00:00");
		hqls.append(" and ho.createTime <= ? ");
		para.add(DateTimeUtil.DateToString(date) + " 23:59:59");
		
//		hqls.append(" and u.userType = ? ");
//		para.add(Constants.USER_TYPE_SUER);
//		hqls.append(" order by (u.dayWinMoney-u.dayBetMoney) desc");
		PaginationSupport ps = baseDataService.findUserTodayBetList(hqls.toString(), para, startIndex, pageSize);
		List<User> list = ps.getItems();

		request.setAttribute("list", list);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("userDayPayoff");
	}
	
	/**
	 *操作日志列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userLogList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",RamConstants.MAXPAGEITEMS);
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String userName = baseDataForm.getUserName();
		String startTime = baseDataForm.getStartDate();
		String endTime = baseDataForm.getEndDate();
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		
		if(ParamUtils.chkString(userName)){
			hqls.append(" and upper(ul.loginName) = ? ");
			para.add(userName.trim().toUpperCase());
		}
		
		if (ParamUtils.chkString(startTime)) {
			startTime = startTime + " 00:00:00";
			hqls.append(" and ul.dateTime>= ? ");
			para.add(startTime.trim());
			request.setAttribute("startTime", startTime);
		}
		if (ParamUtils.chkString(endTime)) {
			endTime = endTime + " 23:59:59";
			hqls.append(" and ul.dateTime<=? ");
			para.add(endTime.trim());
			request.setAttribute("endTime", endTime);
		}
		
		PaginationSupport ps = userService.findUserLog(hqls.toString(), para, startIndex, pageSize);
		List<UserLog> list = ps.getItems();
		
		request.setAttribute("list", list);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		return mapping.findForward("userLogList");
	}

	/***
	 * 初始化期号列表
	 */
	public ActionForward initSessionList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS_MAX);
		String playCate = ParamUtils.getParameter(request, "playCate");// 彩种类型  1=官方   2=信用
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		BaseDataForm baseDataForm = (BaseDataForm) form;
		
		if (ParamUtils.chkString(playCate)) {
			hsql.append(" and g.playCate = ?");
			para.add(playCate);
		}
		hsql.append(" and g.status = ?");
		para.add(Constants.PUB_STATUS_OPEN);
		
		hsql.append(" order by g.infoId desc");
		PaginationSupport ps = baseDataService.findLotteryCatList(hsql.toString(), para,
				startIndex, pageSize);
		
		List<GaSessionInfo> items = ps.getItems();
		request.setAttribute("lotteryCatList", items);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("initSessionList");
	}
	
	/***
	 * 彩种列表
	 */
	public ActionForward lotteryCatList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		BaseDataForm baseDataForm = (BaseDataForm) form;
		
		String title = baseDataForm.getTitle();// 彩票名称
		String playCate = baseDataForm.getPlayCate();// 彩种类型  1=官方   2=信用
		String status = baseDataForm.getStatus(); //状态。默认为1
		String betAvoid = baseDataForm.getBetAvoid(); //是否能投注
		
		if (ParamUtils.chkString(title)) {
			hsql.append(" and g.gameTitle like ?");
			para.add("%"+title.trim()+"%");
		}
		if (ParamUtils.chkString(playCate)) {
			hsql.append(" and g.playCate = ?");
			para.add(playCate);
		}
		if (ParamUtils.chkString(status)) {
			hsql.append(" and g.status = ?");
			para.add(status);
		}
		if (ParamUtils.chkString(betAvoid)) {
			hsql.append(" and g.betAvoid = ?");
			para.add(betAvoid);
		}
		
		hsql.append(" and g.isShow = ?");
		para.add(Constants.PUB_STATUS_OPEN);
		
		hsql.append(" order by g.infoId desc");
		PaginationSupport ps = baseDataService.findLotteryCatList(hsql.toString(), para,
				startIndex, pageSize);
		
		List<User> items = ps.getItems();
		request.setAttribute("lotteryCatList", items);
		request.setAttribute("title", title);
		request.setAttribute("playCate", playCate);
		request.setAttribute("status", status);
		request.setAttribute("betAvoid", betAvoid);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("lotteryCatList");
	}
	
	/***
	 * 彩种添加预处理
	 */
	public ActionForward lotteryCatPreAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer  infoId= ParamUtils.getIntegerParameter(request, "id");
		if(ParamUtils.chkInteger(infoId)){
			GaSessionInfo gasInfo = (GaSessionInfo) baseDataService.getObject(GaSessionInfo.class, infoId);
			baseDataForm.setGasInfo(gasInfo);
			request.setAttribute("infoId", infoId);
		}
		request.setAttribute("baseDataForm", baseDataForm);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		return mapping.findForward("lotteryCatPreAdd");
	}
	
	/***
	 * 彩种添加
	 */
	public ActionForward lotteryCatAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		request.setAttribute("baseDataForm", baseDataForm);
		GaSessionInfo gasInfo = baseDataForm.getGasInfo();
		User user = (User) request.getSession(false).getAttribute("loginUser");
		
		FormFile file = baseDataForm.getFile();
		String imgUrl = "";
		if (file != null && file.getFileName() != "") {
			String savePath = "/column";
			imgUrl = UploadUtil.uploadOneFile(file, savePath);
			gasInfo.setImg(imgUrl);
		}
		
		String msg = baseDataService.saveLotteryCat(gasInfo, user);
		
		return mapping.findForward("lotteryCatInit");
	}
	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		GaSessionInfo gaSessionInfo = (GaSessionInfo) baseDataService.getObject(
				GaSessionInfo.class, infoId);
		String status = gaSessionInfo.getStatus();
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				gaSessionInfo.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				gaSessionInfo.setStatus(Constants.PUB_STATUS_OPEN);
			}
			baseDataService.saveObject(gaSessionInfo, null);
			
			//更新wap首页html - add by cuisy.20180210
			gaService.updateStaticHtml();
			
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
			CacheUtil.updateGameList();
		} catch (Exception e) {
			message="切换状态失败！";
			e.printStackTrace();
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	
	/**
	 * 更改投注状态
	 */
	public void changeBetAvoid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		GaSessionInfo gaSessionInfo = (GaSessionInfo) baseDataService.getObject(
				GaSessionInfo.class, infoId);
		String betAvoid = gaSessionInfo.getBetAvoid();
		try {
			if (betAvoid.equals(Constants.PUB_STATUS_OPEN)) {
				gaSessionInfo.setBetAvoid(Constants.PUB_STATUS_CLOSE);
			} else {
				gaSessionInfo.setBetAvoid(Constants.PUB_STATUS_OPEN);
			}
			baseDataService.saveObject(gaSessionInfo, null);
			
			//更新wap首页html - add by cuisy.20180210
			gaService.updateStaticHtml();
			
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
			CacheUtil.updateGameList();
		} catch (Exception e) {
			e.printStackTrace();
			message="切换状态失败！";
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	/**
	 * 更改是否显示历史开奖记录状态
	 */
	public void changeIsShowHistoryOpen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		GaSessionInfo gaSessionInfo = (GaSessionInfo) baseDataService.getObject(
				GaSessionInfo.class, infoId);
		String isShowHistoryOpen = gaSessionInfo.getIsShowHistoryOpen();
		try {
			if (isShowHistoryOpen.equals(Constants.PUB_STATUS_OPEN)) {
				gaSessionInfo.setIsShowHistoryOpen(Constants.PUB_STATUS_CLOSE);
			} else {
				gaSessionInfo.setIsShowHistoryOpen(Constants.PUB_STATUS_OPEN);
			}
			baseDataService.saveObject(gaSessionInfo, null);
			
			//更新wap首页html - add by cuisy.20180210
			gaService.updateStaticHtml();
			
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
			CacheUtil.updateShowOpenGameList();
		} catch (Exception e) {
			message="切换状态失败！";
			e.printStackTrace();
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	
	
	/***
	 * 彩种删除
	 */
	public void lotteryCatDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Integer infoId = ParamUtils.getIntegerParameter(request, "id");
		JSONObject jo = new JSONObject();
		User user = (User) request.getSession(false).getAttribute("loginUser");
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "";
		if(ParamUtils.chkInteger(infoId)){
			msg = baseDataService.delLotteryCat(infoId,user);
		}
		if(msg.length()==0){
			jo.put("code", code);
		}else{
			jo.put("code", APIConstants.CODE_REQUEST_ERROR);
			jo.put("msg", msg);
		}
		JsonUtil.AjaxWriter(response, jo);
	}
	
	/***
	 * 彩种玩法列表
	 */
	public ActionForward lotteryCatOptionList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		BaseDataForm baseDataForm = (BaseDataForm) form;
		GaSessionInfo gasInfo = baseDataForm.getGasInfo();
		
		Integer gasInfoId = gasInfo.getInfoId();
		if(!ParamUtils.chkInteger(gasInfoId)){
			gasInfoId= ParamUtils.getIntegerParameter(request, "infoId");
		}
		if(!ParamUtils.chkInteger(gasInfoId)){
			gasInfoId = ParamUtils.getIntAttribute(request, "infoId",0);
		}
		String  keyword= ParamUtils.getParameter(request, "userName");
		List<Object> para = new ArrayList<Object>();
		StringBuffer hsql = new StringBuffer();
		Integer agentId=ParamUtils.getIntegerParameter(request, "agentId");
		
		if (ParamUtils.chkInteger(gasInfoId)) {
			GaSessionInfo tmp = (GaSessionInfo) baseDataService.getObject(GaSessionInfo.class, gasInfoId);
			// GaBetOption
			hsql.append(" and g.gameType = ?");
			para.add(tmp.getGameType());
			baseDataForm.setGasInfo(tmp);
			request.setAttribute("gasTitle", tmp.getGameTitle());
		}
		if(ParamUtils.chkString(keyword)){
			hsql.append(" and g.optionTitle like ?");
			para.add("%"+keyword+"%");
		}
		hsql.append(" order by g.betOptionId desc");
		
		PaginationSupport ps = baseDataService.findLotteryCatOptionList(hsql.toString(),para,gasInfoId,startIndex, pageSize);
		
		List<User> items = ps.getItems();
		request.setAttribute("lotteryCatOptionList", items);
		request.setAttribute("gasInfoId", gasInfoId);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("lotteryCatOptionList");
	}
	
	/***
	 * 彩种玩法添加预处理
	 */
	public ActionForward lotteryCatOptionPreAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		Integer gasInfoId= ParamUtils.getIntegerParameter(request, "infoId");
		Integer betOptionId= ParamUtils.getIntegerParameter(request, "betOptionId");
		if(ParamUtils.chkInteger(gasInfoId)){
			GaSessionInfo gasInfo = (GaSessionInfo) baseDataService.getObject(GaSessionInfo.class, gasInfoId);
			baseDataForm.setGasInfo(gasInfo);
			request.setAttribute("gasTitle", gasInfo.getGameTitle());
			request.setAttribute("gasInfoId", gasInfo.getInfoId());
		}
		if(ParamUtils.chkInteger(betOptionId)){
			GaBetOption gaBetOption = (GaBetOption) baseDataService.getObject(GaBetOption.class, betOptionId);
			baseDataForm.setGabOpt(gaBetOption);
		}
		request.setAttribute("baseDataForm", baseDataForm);
		User user = (User) request.getSession(false).getAttribute("loginUser");
		return mapping.findForward("lotteryCatOptionPreAdd");
	}
	
	/**
	 * 彩种玩法添加
	 */
	public ActionForward lotteryCatOptionAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		
		request.setAttribute("baseDataForm", baseDataForm);
		GaSessionInfo gasInfo = baseDataForm.getGasInfo();
		gasInfo = (GaSessionInfo) baseDataService.getObject(GaSessionInfo.class, gasInfo.getInfoId());
		GaBetOption gabOpt = baseDataForm.getGabOpt();
		User user = (User) request.getSession(false).getAttribute("loginUser");
		gabOpt.setGameType(gasInfo.getGameType());
		request.setAttribute("infoId", gasInfo.getInfoId());
		String msg = baseDataService.saveLotteryCatOption(gabOpt, user);
		
		ActionForward actionForward = new ActionForward();
		actionForward.setPath("/baseDataAction.do?method=lotteryCatOptionList&infoId="+gasInfo.getInfoId());
		return actionForward;
	}
	
	/**
	 * 彩种玩法删除
	 */
	public void lotteryCatOptionDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Integer infoId = ParamUtils.getIntegerParameter(request, "id");
		JSONObject jo = new JSONObject();
		User user = (User) request.getSession(false).getAttribute("loginUser");
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String msg = "";
		if(ParamUtils.chkInteger(infoId)){
			msg = baseDataService.delLotteryCatOption(infoId,user);
		}
		if(msg.length()==0){
			jo.put("code", code);
		}else{
			jo.put("code", APIConstants.CODE_REQUEST_ERROR);
			jo.put("msg", msg);
		}
		JsonUtil.AjaxWriter(response, jo);
		
	}
	
	
		public ActionForward turnTableSet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		BaseDataForm baseDataForm = (BaseDataForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and ls.type=? ");
		para.add(Constants.LOTTERY_SETTING_TURNTABLE);
		List<BaseDataDTO> list=baseDataService.findLotterySetList(hqls.toString(),para);
		if(list!=null&&list.size()>0){
			BaseDataDTO dto0=list.get(0);
			LotterySetting ls0=dto0.getLs();
			for(int i=0;i<list.size();i++){
				LotterySettingRl rl=list.get(i).getLsrl();
				String value=rl.getTitle();
				JSONArray array=new JSONArray(value);
				if(i==0){
					
				}else if(i==1){
					
				}else if(i==2){
					
				}
				
			}

		}
		
		request.setAttribute("baseDataForm", baseDataForm);
		return mapping.findForward("turnTableSet");
	}
	
		
	/**
	 * 查询投注项
	 */
	public ActionForward gameOptionList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS_MAX_2000);
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String title = ParamUtils.getParameter(request, "title");
		String playType = ParamUtils.getParameter(request, "playType");
		
		GaSessionInfo gaSessionInfo = (GaSessionInfo) baseDataService.getObject(
				GaSessionInfo.class, infoId);
		
		if(ParamUtils.chkInteger(infoId)){
			
		}
		HQUtils hq = new HQUtils();
		hq.addHsql(" from GaBetOption gbo where 1=1 ");
		hq.addHsql(" and gbo.gameType = ?");
		hq.addPars(gaSessionInfo.getGameType());
		if(ParamUtils.chkString(playType)){
			hq.addHsql(" and gbo.playType = ?");
			hq.addPars(playType);
		}
		if(ParamUtils.chkString(title)){
			hq.addHsql(" and (gbo.title like ? or gbo.optionTitle like ?)");
			hq.addPars("%"+title+"%");
			hq.addPars("%"+title+"%");
		}
		hq.setOrderby(" order by gbo.betOptionId");
		hq.setStartIndex(startIndex);
		hq.setPageSize(pageSize);
		
		List<Object> playTypeStrs = new ArrayList<Object>();
		if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJPK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFPK102)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XYFT)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSFT)){
			//赛车类
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-10球");
			playTypeStrs.add("冠亚军和");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_TJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JXSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJSSC)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJ3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_FC)){
			// 时时彩
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-5球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJKL8)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJPLU28)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJLU28)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_POKER)
				){
			// 幸运28
			playTypeStrs.add("两面盘");
			playTypeStrs.add("特码");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXK10)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_CQK10)){
			// 快10
			playTypeStrs.add("两面盘");
			playTypeStrs.add("第一球");
			playTypeStrs.add("第二球");
			playTypeStrs.add("第三球");
			playTypeStrs.add("第四球");
			playTypeStrs.add("第五球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_MARKSIX)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFLHC)){
			// 六合彩
			playTypeStrs.add("特码A");
			playTypeStrs.add("特码B");
			playTypeStrs.add("正码");
			playTypeStrs.add("正1特");
			playTypeStrs.add("正2特");
			playTypeStrs.add("正3特");
			playTypeStrs.add("正4特");
			playTypeStrs.add("正5特");
			playTypeStrs.add("正6特");
			playTypeStrs.add("正码1-6");
			
			playTypeStrs.add("过关");
			playTypeStrs.add("二全中");
			playTypeStrs.add("二中特");
			playTypeStrs.add("特串");
			playTypeStrs.add("三全中");
			playTypeStrs.add("三中二");
			// 在投注盘上不显示
			playTypeStrs.add("四全中");
			
			playTypeStrs.add("半波");
			playTypeStrs.add("一肖");
			playTypeStrs.add("尾数");
			
			playTypeStrs.add("特码生肖");
			playTypeStrs.add("二肖");
			playTypeStrs.add("三肖");
			playTypeStrs.add("四肖");
			playTypeStrs.add("五肖");
			playTypeStrs.add("六肖");
			playTypeStrs.add("七肖");
			playTypeStrs.add("八肖");
			playTypeStrs.add("九肖");
			playTypeStrs.add("十肖");
			
			playTypeStrs.add("十一肖");
			playTypeStrs.add("二肖连中");
			playTypeStrs.add("三肖连中");
			playTypeStrs.add("四肖连中");
			playTypeStrs.add("五肖连中");
			playTypeStrs.add("二肖连不中");
			playTypeStrs.add("三肖连不中");
			playTypeStrs.add("四肖连不中");
			playTypeStrs.add("二尾连中");
			playTypeStrs.add("三尾连中");
			playTypeStrs.add("四尾连中");
			
			playTypeStrs.add("二尾连不中");
			playTypeStrs.add("三尾连不中");
			playTypeStrs.add("四尾连不中");
			playTypeStrs.add("五不中");
			playTypeStrs.add("六不中");
			playTypeStrs.add("七不中");
			playTypeStrs.add("八不中");
			playTypeStrs.add("九不中");
			playTypeStrs.add("十不中");
			playTypeStrs.add("十一不中");
			playTypeStrs.add("十二不中");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GDPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JXPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SDPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SXPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_TJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HEBPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_NMGPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_LNPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JLPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HLJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SHPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_ZJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_AHPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_FJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HNPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HUBPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GZPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SHXPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GSPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_XJPICK11)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_YNPICK11)){
			// 11选5
			playTypeStrs.add("两面盘");
			playTypeStrs.add("1-5球");
		}else if(gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JSK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_AHK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_BJK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_FJK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GZK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GXK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_GSK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HUBK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HEBK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_HNK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JXK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_JLK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_NMGK3)
				||gaSessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SHK3)){
			playTypeStrs.add("两面盘");
			playTypeStrs.add("两连");
		}
		
		PaginationSupport ps = baseDataService.findObjectPage(hq);
		List<Object> dtoList = new ArrayList<Object>();
		for(Object obj:ps.getItems()){
			GaBetOption gbo = (GaBetOption) obj;
			GaBetOptionDto gboDto = new GaBetOptionDto(gbo);
			if(Integer.valueOf(gbo.getPlayType())<playTypeStrs.size()){
				gboDto.setPlayTypeStr(""+playTypeStrs.get(Integer.valueOf(gbo.getPlayType())));
			}
			dtoList.add(gboDto);
		}
		request.setAttribute("gameTitle", gaSessionInfo.getGameTitle());
		request.setAttribute("infoId", ""+infoId);
		request.setAttribute("title",title);
		
		request.setAttribute("cuPlayType", playType);
		request.setAttribute("playTypeStrs", playTypeStrs);
		request.setAttribute("list", dtoList);
		request.setAttribute("count", String.valueOf(ps.getTotalCount()));
		
		String flag = "success";
		
		return mapping.findForward("gameOptionList");
	}
	
	/**
	 * 更改赔率
	 */
	public void changeBetRate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception, NoFunctionPermissionException {
		Integer infoId = ParamUtils.getIntegerParameter(request, "infoId");
		String rateStr = ParamUtils.getParameter(request, "rate");
		BigDecimal betRate = new BigDecimal(rateStr);
		String flag = "success";
		JSONObject outer = new JSONObject();
		
		
		try {
			GaBetOption gbo = (GaBetOption) baseDataService.getObject(GaBetOption.class, infoId);
			if(gbo!=null){
				gbo.setBetRate(betRate);
			}
			baseDataService.saveObject(gbo, null);
			//更新缓存
			CacheUtil.getLotteryBetting(gbo.getPlayAlias(),true);
			
			rateStr = ""+gbo.getBetRate().setScale(3);
			infoId = gbo.getBetOptionId();
		} catch (Exception e) {
			e.printStackTrace();
			flag = "error";
		}
		if(flag.equals("success")){
			outer.put("code", "200");
			outer.put("flag", flag);
			outer.put("rate", rateStr);
			outer.put("id", infoId);
		}else{
			outer.put("code", "201");
			outer.put("flag", flag);
		}
		JsonUtil.AjaxWriter(response, outer);
	}
		
		
	/**
	 * 统计每月/选中月份的充值与下注总金额
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward monthlyStatisticslist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
//		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
//				RamConstants.MAXPAGEITEMS);
		List<Object> para1 = new ArrayList<Object>();
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hqls1 = new StringBuffer();
		StringBuffer hqls2 = new StringBuffer();

		BaseDataForm baseDataForm = (BaseDataForm) form;
		String startDate = baseDataForm.getStartDate();
		String endDate = baseDataForm.getEndDate();


		if (ParamUtils.chkString(startDate)) {
			hqls1.append(" and ho.createTime >= ? ");
			para1.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
			hqls2.append(" and ho.betTime >= ? ");
			para2.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			// 获取月份的最后一天
			String year = endDate.substring(0, 4);
			String month = endDate.substring(5, 7);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR,Integer.parseInt(year));
			// 月份从0开始
			calendar.set(Calendar.MONTH,Integer.parseInt(month));
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			// 不需要再加1
//			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			
			hqls1.append(" and ho.createTime <= ? ");
			para1.add(calendar.getTime());
			hqls2.append(" and ho.betTime <= ? ");
			para2.add(calendar.getTime());
		}
		
		hqls1.append(" and ho.cashType in ("+Constants.CASH_TYPE_ONLINE+","+Constants.CASH_TYPE_MANAGER_SET+","+Constants.CASH_TYPE_CASH_OTHER_SET+")");
		
		List<Object> tradeList = baseDataService.findMonthlyStaTrade(hqls1.toString(), para1);
		List<Object> betList = baseDataService.findMonthlyStaBet(hqls2.toString(), para2);
		// 查询出来的数据跟在数据库查出来的不一致
		
		Map<String, MonthlyStatictisDto> dataMap = new HashMap<String, MonthlyStatictisDto>();
		List<MonthlyStatictisDto> resultList = new ArrayList<MonthlyStatictisDto>(); 
		for(Object obj:tradeList){
			Object[] tmp = (Object[]) obj;
			if(tmp[0]!=null
					&&tmp[1]!=null){
				MonthlyStatictisDto dto = new MonthlyStatictisDto();
//				dto.setYear(tmp[0].toString());
//				dto.setMonth(tmp[1].toString());
				Date dt = (Date)tmp[0];
				String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
				dto.setYearAndMonth(dtMonth);
				dto.setTradeMoney(""+tmp[1]);
				dataMap.put(dtMonth, dto);
			}
		}

		for(Object obj:betList){
			Object[] tmp = (Object[]) obj;
			if(tmp[0]!=null
					&&tmp[1]!=null){
				Date dt = (Date)tmp[0];
				String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
				MonthlyStatictisDto dto =dataMap.get(dtMonth);
				BigDecimal betM = new BigDecimal(tmp[1].toString()).setScale(3,BigDecimal.ROUND_HALF_UP);
				if(dto!=null){
					dto.setBetMoney(betM.toString());
				}else{
					MonthlyStatictisDto dto2 = new MonthlyStatictisDto();
//					dto2.setYear(tmp[0].toString());
//					dto2.setMonth(tmp[1].toString());
					dto2.setYearAndMonth(dtMonth);
					dto2.setBetMoney(betM.toString());
					dataMap.put(dtMonth, dto2);
				}
			}
		}
		
		for (Map.Entry<String, MonthlyStatictisDto> entry : dataMap.entrySet()) {
			resultList.add(entry.getValue());
		}
		
		request.setAttribute("list", resultList);
//		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		baseDataForm.setStartIndex(startIndex + "");
		baseDataForm.setStartIndex(startDate);
		baseDataForm.setStartIndex(endDate);

		return mapping.findForward("monthlySta");
	}

	/**
	 * 根据传入的用户id统计该用户每个月的投注金额、充值金额
	 */
	public ActionForward monthlyUserStatisticslist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
//		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
//				RamConstants.MAXPAGEITEMS);
		List<Object> para1 = new ArrayList<Object>();
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hqls1 = new StringBuffer();
		StringBuffer hqls2 = new StringBuffer();
		String msg = "必要参数为空";
		BaseDataForm baseDataForm = (BaseDataForm) form;
		String startDate = baseDataForm.getStartDate();
		String endDate = baseDataForm.getEndDate();
		String userIdStr = baseDataForm.getUserId();
		Integer userId  = null;
		if(userIdStr!=null&&userIdStr.length()>0){
			userId=Integer.valueOf(baseDataForm.getUserId());
		}

		if(!ParamUtils.chkInteger(userId)){
			request.setAttribute("msg", msg);
		}else{
			hqls1.append(" and ho.userId = ?");
			para1.add(userId);
			
			hqls2.append(" and ho.userId = ?");
			para2.add(userId);
			
			if (ParamUtils.chkString(startDate)) {
				hqls1.append(" and ho.createTime >= ? ");
				para1.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
				hqls2.append(" and ho.betTime >= ? ");
				para2.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
			}
			if (ParamUtils.chkString(endDate)) {
				// 获取月份的最后一天
				String year = endDate.substring(0, 4);
				String month = endDate.substring(5, 7);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR,Integer.parseInt(year));
				// 月份从0开始
				calendar.set(Calendar.MONTH,Integer.parseInt(month));
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				// 不需要再加1
//			calendar.add(Calendar.MONTH, 1);
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				
				hqls1.append(" and ho.createTime <= ? ");
				para1.add(calendar.getTime());
				hqls2.append(" and ho.betTime <= ? ");
				para2.add(calendar.getTime());
			}
			
			hqls1.append(" and ho.cashType in ("+Constants.CASH_TYPE_ONLINE+","+Constants.CASH_TYPE_MANAGER_SET+","+Constants.CASH_TYPE_CASH_OTHER_SET+")");
			
			List<Object> tradeList = baseDataService.findMonthlyStaTrade(hqls1.toString(), para1);
			List<Object> betList = baseDataService.findMonthlyStaBet(hqls2.toString(), para2);
			// 查询出来的数据跟在数据库查出来的不一致
			
			Map<String, MonthlyStatictisDto> dataMap = new HashMap<String, MonthlyStatictisDto>();
			List<MonthlyStatictisDto> resultList = new ArrayList<MonthlyStatictisDto>(); 
			for(Object obj:tradeList){
				Object[] tmp = (Object[]) obj;
				if(tmp[0]!=null
						&&tmp[1]!=null){
					MonthlyStatictisDto dto = new MonthlyStatictisDto();
//				dto.setYear(tmp[0].toString());
//				dto.setMonth(tmp[1].toString());
					Date dt = (Date)tmp[0];
					String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
					dto.setYearAndMonth(dtMonth);
					dto.setTradeMoney(""+tmp[1]);
					dataMap.put(dtMonth, dto);
				}
			}
			
			for(Object obj:betList){
				Object[] tmp = (Object[]) obj;
				if(tmp[0]!=null
						&&tmp[1]!=null){
					Date dt = (Date)tmp[0];
					String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
					MonthlyStatictisDto dto =dataMap.get(dtMonth);
					BigDecimal betM = new BigDecimal(tmp[1].toString()).setScale(3,BigDecimal.ROUND_HALF_UP);
					if(dto!=null){
						dto.setBetMoney(betM.toString());
					}else{
						MonthlyStatictisDto dto2 = new MonthlyStatictisDto();
//					dto2.setYear(tmp[0].toString());
//					dto2.setMonth(tmp[1].toString());
						dto2.setYearAndMonth(dtMonth);
						dto2.setBetMoney(betM.toString());
						dataMap.put(dtMonth, dto2);
					}
				}
			}
			
			for (Map.Entry<String, MonthlyStatictisDto> entry : dataMap.entrySet()) {
				resultList.add(entry.getValue());
			}
			
			request.setAttribute("list", resultList);
//		request.setAttribute("count", (ps.getTotalCount() + "").toString());
			baseDataForm.setStartIndex(startIndex + "");
			baseDataForm.setStartIndex(startDate);
			baseDataForm.setStartIndex(endDate);
		}

		return mapping.findForward("monthlyUserSta");
	}
	
	public ActionForward exportMonthlyStatisticslist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para1 = new ArrayList<Object>();
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hqls1 = new StringBuffer();
		StringBuffer hqls2 = new StringBuffer();

		BaseDataForm baseDataForm = (BaseDataForm) form;
		String startDate = baseDataForm.getStartDate();
		String endDate = baseDataForm.getEndDate();


		if (ParamUtils.chkString(startDate)) {
			hqls1.append(" and ho.createTime >= ? ");
			para1.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
			hqls2.append(" and ho.createTime >= ? ");
			para2.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
		}
		if (ParamUtils.chkString(endDate)) {
			// 获取月份的最后一天
			String year = endDate.substring(0, 4);
			String month = endDate.substring(5, 7);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR,Integer.parseInt(year));
			// 月份从0开始
			calendar.set(Calendar.MONTH,Integer.parseInt(month));
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			// 不需要再加1
//			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			
			hqls1.append(" and ho.createTime <= ? ");
			para1.add(calendar.getTime());
			hqls2.append(" and ho.createTime <= ? ");
			para2.add(calendar.getTime());
		}
		
		hqls1.append(" and ho.cashType in (1,27,28)");
		
		List<Object> tradeList = baseDataService.findMonthlyStaTrade(hqls1.toString(), para1);
		List<Object> betList = baseDataService.findMonthlyStaBet(hqls2.toString(), para2);
		
		Map<String, MonthlyStatictisDto> dataMap = new HashMap<String, MonthlyStatictisDto>();
		List<MonthlyStatictisDto> resultList = new ArrayList<MonthlyStatictisDto>(); 
		for(Object obj:tradeList){
			Object[] tmp = (Object[]) obj;
			if(tmp[0]!=null
					&&tmp[1]!=null){
				MonthlyStatictisDto dto = new MonthlyStatictisDto();
//				dto.setYear(tmp[0].toString());
//				dto.setMonth(tmp[1].toString());
				Date dt = (Date)tmp[0];
				String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
				dto.setYearAndMonth(dtMonth);
				dto.setTradeMoney(""+tmp[1]);
				dataMap.put(dtMonth, dto);
			}
		}

		for(Object obj:betList){
			Object[] tmp = (Object[]) obj;
			if(tmp[0]!=null
					&&tmp[1]!=null){
				Date dt = (Date)tmp[0];
				String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
				
				MonthlyStatictisDto dto =dataMap.get(dtMonth);
				if(dto!=null){
					dto.setBetMoney(""+tmp[1]);
				}else{
					MonthlyStatictisDto dto2 = new MonthlyStatictisDto();
//					dto2.setYear(tmp[0].toString());
//					dto2.setMonth(tmp[1].toString());
					dto2.setYearAndMonth(dtMonth);
					dto2.setBetMoney(""+tmp[1]);
					dataMap.put(dtMonth, dto2);
				}
			}
		}
		
		for (Map.Entry<String, MonthlyStatictisDto> entry : dataMap.entrySet()) {
			resultList.add(entry.getValue());
		}
		
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
		wb.setSheetName(0, "月统计表");
		sheet.setColumnWidth(0,20*width);
		sheet.setColumnWidth(1,20*width);
		sheet.setColumnWidth(2,20*width);

		int rowNum = 0;
		int columeNum = 0;
		
		HSSFRow row = sheet.createRow(rowNum++);
		HSSFCell cell = null;
		// 添加表头  第一行
		// 空白
		// columeNum++ 先使用columeNum，然后再加1，所以后续操作需要-1
		cell = row.createCell(columeNum++);
		cell.setCellValue("年月");
		cell = row.createCell(columeNum++);
		cell.setCellValue("充值金额");
		cell = row.createCell(columeNum++);
		cell.setCellValue("投注金额");
		
		for(MonthlyStatictisDto dto:resultList){
			row = sheet.createRow(rowNum++);
			columeNum = 0;
			cell = row.createCell(columeNum++);
			cell.setCellValue(""+dto.getYearAndMonth());
			cell = row.createCell(columeNum++);
			cell.setCellValue(""+dto.getTradeMoney()!=null?dto.getTradeMoney():"");
			cell = row.createCell(columeNum++);
			cell.setCellValue(""+dto.getBetMoney()!=null?dto.getBetMoney():"");
		}
		try {
			String exportName ="月统计表";
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
		return null;
	}
	
	public ActionForward exportMonthlyUserStatisticslist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",
				RamConstants.MAXPAGEITEMS);
		List<Object> para1 = new ArrayList<Object>();
		List<Object> para2 = new ArrayList<Object>();
		StringBuffer hqls1 = new StringBuffer();
		StringBuffer hqls2 = new StringBuffer();

		BaseDataForm baseDataForm = (BaseDataForm) form;
		String startDate = baseDataForm.getStartDate();
		String endDate = baseDataForm.getEndDate();
		String userIdStr = baseDataForm.getUserId();
		Integer userId  = null;
		if(userIdStr!=null&&userIdStr.length()>0){
			userId=Integer.valueOf(baseDataForm.getUserId());
		}
		String msg = "请输入用户id";
		if(!ParamUtils.chkInteger(userId)){
			request.setAttribute("msg", msg);
		}else{
			hqls1.append(" and ho.userId = ?");
			para1.add(userId);
			
			hqls2.append(" and ho.userId = ?");
			para2.add(userId);
			
			if (ParamUtils.chkString(startDate)) {
				hqls1.append(" and ho.createTime >= ? ");
				para1.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
				hqls2.append(" and ho.createTime >= ? ");
				para2.add(DateTimeUtil.parse(startDate + "-01 00:00:00"));
			}
			if (ParamUtils.chkString(endDate)) {
				// 获取月份的最后一天
				String year = endDate.substring(0, 4);
				String month = endDate.substring(5, 7);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR,Integer.parseInt(year));
				// 月份从0开始
				calendar.set(Calendar.MONTH,Integer.parseInt(month));
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				// 不需要再加1
//				calendar.add(Calendar.MONTH, 1);
				calendar.add(Calendar.DAY_OF_YEAR, -1);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				
				hqls1.append(" and ho.createTime <= ? ");
				para1.add(calendar.getTime());
				hqls2.append(" and ho.createTime <= ? ");
				para2.add(calendar.getTime());
			}
			
			hqls1.append(" and ho.cashType in (1,27,28)");
			
			List<Object> tradeList = baseDataService.findMonthlyStaTrade(hqls1.toString(), para1);
			List<Object> betList = baseDataService.findMonthlyStaBet(hqls2.toString(), para2);
			// 查询出来的数据跟在数据库查出来的不一致
			
			Map<String, MonthlyStatictisDto> dataMap = new HashMap<String, MonthlyStatictisDto>();
			List<MonthlyStatictisDto> resultList = new ArrayList<MonthlyStatictisDto>(); 
			for(Object obj:tradeList){
				Object[] tmp = (Object[]) obj;
				if(tmp[0]!=null
						&&tmp[1]!=null){
					MonthlyStatictisDto dto = new MonthlyStatictisDto();
//					dto.setYear(tmp[0].toString());
//					dto.setMonth(tmp[1].toString());
					Date dt = (Date)tmp[0];
					String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
					dto.setYearAndMonth(dtMonth);
					dto.setTradeMoney(""+tmp[1]);
					dataMap.put(dtMonth, dto);
				}
			}

			for(Object obj:betList){
				Object[] tmp = (Object[]) obj;
				if(tmp[0]!=null
						&&tmp[1]!=null){
					Date dt = (Date)tmp[0];
					String dtMonth = DateTimeUtil.dateToString(dt, "yyyy年MM月");
					
					MonthlyStatictisDto dto =dataMap.get(dtMonth);
					if(dto!=null){
						dto.setBetMoney(""+tmp[1]);
					}else{
						MonthlyStatictisDto dto2 = new MonthlyStatictisDto();
//						dto2.setYear(tmp[0].toString());
//						dto2.setMonth(tmp[1].toString());
						dto2.setYearAndMonth(dtMonth);
						dto2.setBetMoney(""+tmp[1]);
						dataMap.put(dtMonth, dto2);
					}
				}
			}
			
			for (Map.Entry<String, MonthlyStatictisDto> entry : dataMap.entrySet()) {
				resultList.add(entry.getValue());
			}
			
			// 导出数据
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			
			HSSFCellStyle style1 = wb.createCellStyle(); 
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			HSSFFont font1 = wb.createFont();    
//					font1.setFontName("仿宋_GB2312");    
			style1.setFont(font1);
			font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
//					font2.setFontHeightInPoints((short) 12);  
			HSSFCellStyle style2 = wb.createCellStyle(); 
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			int width = 256;
			wb.setSheetName(0, "月统计表");
			sheet.setColumnWidth(0,20*width);
			sheet.setColumnWidth(1,20*width);
			sheet.setColumnWidth(2,20*width);

			int rowNum = 0;
			int columeNum = 0;
			
			HSSFRow row = sheet.createRow(rowNum++);
			HSSFCell cell = null;
			// 添加表头  第一行
			// 空白
			// columeNum++ 先使用columeNum，然后再加1，所以后续操作需要-1
			cell = row.createCell(columeNum++);
			cell.setCellValue("年月");
			cell = row.createCell(columeNum++);
			cell.setCellValue("充值金额");
			cell = row.createCell(columeNum++);
			cell.setCellValue("投注金额");
			
			for(MonthlyStatictisDto dto:resultList){
				row = sheet.createRow(rowNum++);
				columeNum = 0;
				cell = row.createCell(columeNum++);
				cell.setCellValue(""+dto.getYearAndMonth());
				cell = row.createCell(columeNum++);
				cell.setCellValue(""+dto.getTradeMoney()!=null?dto.getTradeMoney():"");
				cell = row.createCell(columeNum++);
				cell.setCellValue(""+dto.getBetMoney()!=null?dto.getBetMoney():"");
			}
			try {
				String exportName =userId+"用户月统计表";
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
		}
		return null;
	}
	
	public void chkLoginValid(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		String loginCode= (String) request.getSession().getAttribute("loginCode");
		if(ParamUtils.chkString(loginCode)&&loginCode.equals("402")){//用户登录失效
			code=APIConstants.CODE_USER_ERROR;
			message="您的帐号已经在另一处登录，您被迫下线！";
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/***
	 * 在线人数
	 */
	public void onlineNumber(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		String message="";
		data.put("number", BlackListCacheUtil.onlineNumber);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
}
