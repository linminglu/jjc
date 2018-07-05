package help.base;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.eff.BlackListCacheUtil;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.apps.eff.TradeCacheUtil;
import com.apps.eff.UserCacheUtil;
import com.apps.model.Advertising;
import com.apps.model.City;
import com.apps.model.Feedback;
import com.apps.model.LotterySetting;
import com.apps.model.Message;
import com.apps.model.Notice;
import com.apps.model.Param;
import com.apps.model.SysOption;
import com.apps.model.UserPointDetail;
import com.apps.model.UserTradeDetail;
import com.apps.model.Version;
import com.apps.model.dto.BaseDataDTO;
import com.apps.service.IBaseDataService;
import com.apps.service.IMessageService;
import com.apps.service.INoticeService;
import com.apps.service.IParamService;
import com.apps.util.JsonUtil;
import com.apps.util.ProductUtil;
import com.apps.util.QRCodeUtil;
import com.apps.util.UploadUtil;
import com.cash.model.SysBank;
import com.cash.model.UserBankBind;
import com.cash.service.ICashService;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.DesUtils;
import com.framework.util.FSO;
import com.framework.util.HQUtils;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;
import com.game.model.GaBetDetail;
import com.game.model.GaBetOption;
import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.game.model.GaSessionInfo;
import com.game.model.UserLevel;
import com.game.model.dto.SpDetailDTO;
import com.game.service.IGaService;
import com.gf.dcb.DcbConstants;
import com.gf.dcb.model.dto.GfDcbDTO;
import com.gf.ssc.cqssc.util.CqSscUtil;
import com.ram.RamConstants;
import com.ram.model.NewsInformation;
import com.ram.model.NewsType;
import com.ram.model.User;
import com.ram.service.user.IUserService;
import com.ram.util.IPUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.xy.ssc.cqssc.CqSscConstants;

/**
 * 公用接口管理类</br>
 * 
 * @author Mr.zang
 * 
 */
public class BaseDataManager extends BaseDispatchAction {
	private final IBaseDataService baseDataService = (IBaseDataService) getService("baseDataService");
	private final ICashService cashService = (ICashService) getService("cashService");
	private final IGaService gaService = (IGaService) getService("gaService");
	private final IParamService paramService = (IParamService) getService("paramService");
	private final IMessageService messageService = (IMessageService) getService("messageService");
	private final INoticeService noticeService = (INoticeService) getService("noticeService");
	private final IUserService userService = (IUserService) getService("userService");
	
	/**
	 * 首页广告
	 */
	public void advert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String adType = ParamUtils.getParameter(deParameter, "adType", "1");// l.轮播 2.静态 3.类型下的广告
		Integer tid = ParamUtils.getIntegerParameter(deParameter, "tid");// 当adType=3时，有此值

		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(adType)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONArray items = new JSONArray();// 数据集合
		JSONObject obj = null;
		List<Advertising> list = null;
		if (message.equals("")) {
			list = baseDataService.findAdvertisingByType(adType,
					Constants.PUB_STATUS_OPEN, null);
			if (list != null && list.size() > 0) {
				for (Advertising ad : list) {
					obj = new JSONObject();
					obj.put("type", "2");
					obj.put("link", StringUtil.checkAPIHttpUrl(ad.getLink()));
					obj.put("img", StringUtil.checkAPIHttpUrl(ad.getImg()));//
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_NOT_FOUND;
			}
			data.put("items", items);
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	 /**
	 * 首页广告 
	 */
	 public void notice(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONArray items = new JSONArray();// 数据集合
		JSONObject obj = null;
		List<Advertising> list = null;
		if (message.equals("")) {
			list = baseDataService
					.findAdvertisingByType(Constants.ADVERTISING_STAT,
							Constants.PUB_STATUS_OPEN, null);
			if (list != null && list.size() > 0) {
				for (Advertising ad : list) {
					obj = new JSONObject();
					obj.put("title", ParamUtils.chkStringNotNull(ad.getTitle()));
					obj.put("type", "2");
					obj.put("link", ParamUtils.chkStringNotNull(StringUtil
							.checkAPIHttpUrl(ad.getLink())));
					obj.put("img", Constants.getDomainNameFile() + ad.getImg());//
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_NOT_FOUND;
			}
			data.put("items", items);
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	 }
	 
	 /**
	 * 首页帮助链接 
	 */
	 public void helpList(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONArray items = new JSONArray();// 数据集合
		JSONObject obj = null;
		List<Object> list = null;
		
		HQUtils hq = new HQUtils();
		hq.addHsql(" from Notice n where 1=1 ");
		hq.addHsql(" and n.type in (5,6,7,8)");
		hq.addHsql(" and n.status = ?");
		hq.addPars("1");
		list = baseDataService.findObjects(hq);
		
		for(Object tmp:list){
			Notice no = (Notice) tmp;
			obj = new JSONObject();
			obj.put("title", ParamUtils.chkStringNotNull(no.getTitle()));
			obj.put("type", no.getType());
			obj.put("link", ParamUtils.chkStringNotNull(StringUtil
					.checkAPIHttpUrl(no.getLink())));
			items.put(obj);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		// qq客服地址
		HQUtils hq1 = new HQUtils();
		hq1.addHsql(" from Param n where 1=1 ");
		hq1.addHsql(" and n.type =?");
		hq1.addPars(Constants.PARAM_QQ_CUSTOMER_URL);
		Param pa1 = (Param) baseDataService.getObject(hq1);
		if(pa1!=null){
			data.put("qqServerUrl", ParamUtils.chkStringNotNull(pa1.getValue()));
		}else{
			data.put("qqServerUrl","");
		}
		// qq客服账号
		HQUtils hq2 = new HQUtils();
		hq2.addHsql(" from Param n where 1=1 ");
		hq2.addHsql(" and n.type =?");
		hq2.addPars(Constants.PARAM_QQ_ACCOUNT);
		Param pa2 = (Param) baseDataService.getObject(hq2);
		if(pa2!=null){
			data.put("qqServerAccount", ParamUtils.chkStringNotNull(pa2.getValue()));
		}else{
			data.put("qqServerAccount", "");
		}
		// 在线客服地址
		HQUtils hq3 = new HQUtils();
		hq3.addHsql(" from Param n where 1=1 ");
		hq3.addHsql(" and n.type =?");
		hq3.addPars(Constants.PARAM_ONLINE_CUSTOMER_URL);
		Param pa3 = (Param) baseDataService.getObject(hq3);
		if(pa3!=null){
			data.put("onlineServerUrl", ParamUtils.chkStringNotNull(pa3.getValue()));
		}else{
			data.put("onlineServerUrl", "");
		}
		
		//app下载地址
		hq3 = new HQUtils();
		hq3.addHsql(" from Param n where n.type =?");
		hq3.addPars(Constants.PARAM_LOTTERY_APP_DOWN_URL);
		pa3 = (Param) baseDataService.getObject(hq3);
		if(pa3!=null){
			data.put("appDownUrl", ParamUtils.chkStringNotNull(pa3.getValue()));
		}else{
			data.put("appDownUrl", "");
		}
				
		data.put("items", items);
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	 }

	/**
	 * 注册时的服务和条款
	 */
	public void terms(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", Constants.getDomainName() + "/help/protocol.html");
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 关于我们
	 */
	public void aboutus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", Constants.getDomainName() + "/help/aboutus.html?t="
				+ System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 常见问题
	 */
	public void question(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", Constants.getDomainName() + "/help/questions.html?t="
				+ System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 活动
	 */
	public void activitie(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		Notice notice = noticeService.getLatestNotice("1");
		if (notice != null) {
			data.put("link", Constants.getDomainName() + notice.getLink()
					+ "?t=" + System.currentTimeMillis());
		} else {
			data.put("link", "");
		}
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 重要通知(弹出)
	 */
	public void importantNotice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type");// 3首页的弹出通知 4存取款的弹出通知
		String u = ParamUtils.getParameter(deParameter, "u", "");
		String code=APIConstants.CODE_REQUEST_ERROR;
		Notice notice=noticeService.getLatestNotice(type);
		String title="";
		String content="";
		String time="";
		if(notice!=null){
			if(Constants.PUB_STATUS_OPEN.equals(notice.getStatus())){
				title=ParamUtils.chkStringNotNull(notice.getTitle());
				content=ParamUtils.chkStringNotNull(notice.getContent());
				time=DateTimeUtil.dateToString(notice.getCreateTime(), "yyyyMMddHH:mm:ss");
				code=APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		
		if (ParamUtils.chkString(u)) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer.valueOf(decryptMap
					.get(Constants.DES_KEY_UID));
			User user = userService.getUser(uid);
			if (user != null) {
				BigDecimal money = user.getMoney();
				if (money != null) {
					data.put("money", money);
					code=APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					data.put("money", 0);
				}
			} else {
				data.put("money", 0);
			}
		} else {
			data.put("money", 0);
		}
		
		data.put("title", title);
		data.put("content", content);
		data.put("time", time);
		map.put("code", code);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	

	/**
	 * 隐私政策
	 */
	public void privacy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		data.put("link", Constants.getDomainName() + "/help/privacy.html?t="
				+ System.currentTimeMillis());
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 分享
	 */
	public void share(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String u = ParamUtils.getParameter(deParameter, "u");// 用户key
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				String QRCode = user.getQRCode();
				// 二维码
				// 虚拟目录
				String path = Constants.getFileUploadPath()
						+ UploadUtil.getFolderYYYYMM("/userQRCode");

				// 二维码存储目录
				String QRCodePath = Constants.getWebRootPath() + path;
				String QRName = ProductUtil
						.createOrderNum(Constants.MODULE_STORE) + ".png";

				if (!ParamUtils.chkString(QRCode)) {
					QRCode = path + "/" + QRName;
				}
				// 二维码内容为oid加密，如需要二维码的话需要解密
				String QRCodeUrl = Constants.getDomainName() + "/download"
						+ "?key=" + uid;
				File file = new File(Constants.getWebRootPath() + QRCode);

				if (!file.exists()) {// 如果没有，生成一个二维码
					new FSO().createFolder(QRCodePath);
					int width = 300;
					int height = 300;
					String format = "png";
					QRCodeUtil.genQRCodeImg(QRCodeUrl, width, height, format,
							QRCodePath + "/" + QRName);
					user.setQRCode(QRCode);
				}
				data.put(
						"link",
						StringUtil.httpTohttps(
								request,
								StringUtil.checkAPIHttpUrl("/m/QRCode?u=" + u
										+ "&t=" + System.currentTimeMillis())));
				data.put("uid", user.getUserId());
				data.put("title", "苹果彩票");
				String words = "来自App的分享：这里有北京三分彩，北京赛车，幸运28，重庆时时彩，PC蛋蛋，广东快乐十分等多种游戏和各种玩法，欢迎下载";
				data.put("words", words);
				String img = "/images/aboutus.png";
				data.put(
						"img",
						StringUtil.httpTohttps(request,
								StringUtil.checkAPIHttpUrl(img)));
				data.put("qrCodeUrl",
						StringUtil.checkAPIHttpUrl(user.getQRCode()));
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = APIConstants.TIPS_NOT_USER;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * app下载
	 */
	public void download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		// String u = ParamUtils.getParameter(deParameter, "u");// 用户key
		String message = "";
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		// if (!ParamUtils.chkString(u)) {
		// message = APIConstants.PARAMS_EMPTY_MSG;
		// }
		if (message.equals("")) {
			// Map<String, String> decryptMap = DesUtils.decryptMap(u);
			// Integer uid = Integer
			// .valueOf(decryptMap.get(Constants.DES_KEY_UID));
			// User user = userService.getUser(uid);
			// if (user != null) {
			data.put("link",
					StringUtil.httpTohttps(request, Constants.getDomainName())
							+ "/download");
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 签到信息
	 */
	public void sign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String u = ParamUtils.getParameter(deParameter, "u");// 用户key
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				if (DateTimeUtil.DateToString(user.getLastSignDate()).equals(
						DateTimeUtil.DateToString(new Date()))) {
					code = APIConstants.CODE_REQUEST_ERROR;
					message = "您今日已经签过到了";
				} else {
					user.setLastSignDate(new Date());
					userService.saveObject(user, null);
//					userService.updateUserProperty("lastSignDate", new Date(), user.getUserId());
					List<Param> list = CacheUtil.getParam();
					Param para = null;
					BigDecimal money = null;//赠送金额
					for(Param param:list){
						if(Constants.PARAM_SIGN_SEND.equals(param.getType())){
							para = param;
							break;
						}
					}
					if(para != null){
						String value = para.getValue();
						if(ParamUtils.chkString(value)){
							String array[] = value.split(",|，");
							
							if(array.length==2){
								// 只有两个数字
								BigDecimal minMoney = null;
								BigDecimal maxMoney = null;
								try{
									minMoney = new BigDecimal(array[0]);
									maxMoney = new BigDecimal(array[1]);
									int scale = minMoney.scale();
									if(!ParamUtils.chkInteger(scale)){
										scale = 2;
									}
									money = new BigDecimal(Math.random())
											.multiply(maxMoney.subtract(minMoney))
											.add(minMoney)
											.setScale(scale, BigDecimal.ROUND_HALF_UP);// 保留2位小数并四舍五入
								}catch(Exception e){
									log.info("赠送金额错误");
								}
							}else if(array.length>2){
								Double subscripts = (Math.random()*(array.length-1));
								
								money = new BigDecimal(array[subscripts.intValue()])
										.setScale(2);// 保留2位小数并四舍五入;
							}
							
						}
					}
					if(money != null && money.doubleValue()>0){
						userService.saveUserSignMoney(user,money);
						message = "签到赠送"+ProductUtil.BigFormatJud(money);
					}else{
						message = "签到成功";
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}
			} else {
				message = APIConstants.TIPS_NOT_USER;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 检测版本
	 */
	public void checkVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String type = ParamUtils.getParameter(deParameter, "type");// 设备类型 1.IOS
																	// 2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		String s = ParamUtils.getParameter(deParameter, "s", "1");// 1.用户版 2.商家版
		String message = "";
		if (!ParamUtils.chkString(type) || !ParamUtils.chkString(ver)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Version version = baseDataService.getDefVer(type, s);
			if (version != null) {
				String defVer = version.getVer();
				String link = version.getLink();
				int compareVersion = StringUtil.compareVersion(defVer, ver);
				if (compareVersion > 0) {
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = APIConstants.VER_NEW;
					data.put("link", link);
					data.put("flag", version.getFlag());
					data.put("content", version.getContent() == null ? "修复了一些问题"
							: version.getContent());
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = APIConstants.VER_NEWEST;
				}
			} else {
				code = APIConstants.CODE_NOT_FOUND;
				message = APIConstants.VER_NEWEST;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 反馈
	 */
	public void feedback(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer uid = ParamUtils.getIntegerParameter(deParameter, "uid");
		String content = ParamUtils.getParameter(deParameter, "content");// 内容
		String message = "";
		if (!ParamUtils.chkInteger(uid) && !ParamUtils.chkString(content)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Feedback feedback = new Feedback(uid, new Date(), content);
			baseDataService.saveObject(feedback, null);
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = APIConstants.TIPS_FEEDBACK;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 获取城市列表
	 */
	public void city(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String message = "";
		// List<City> cityList = baseDataService.findCity(Constants.CITY_SHI);
		List<String> keys = baseDataService.findCityKeys(Constants.CITY_SHI);

		JSONArray items = new JSONArray();// 数据集合
		JSONObject obj = null;

		// 热门城市
		JSONObject joAbc = new JSONObject();
		JSONArray jaCity = new JSONArray();

		joAbc.put("key", "热");
		HQUtils hq = new HQUtils(
				"from City c where c.ishot=1 order by c.sort desc");
		List<Object> cList = baseDataService.findObjects(hq);
		for (Object objc : cList) {
			City city = (City) objc;
			JSONObject joCity = new JSONObject();
			joCity.put("cid", city.getCid());
			joCity.put("title", city.getTitle());
			joCity.put("pinyin", city.getPinyin());
			joCity.put("code", city.getCode() == null ? "" : city.getCode());//
			jaCity.put(joCity);
		}
		joAbc.put("list", jaCity);
		items.put(joAbc);

		for (String key : keys) {
			JSONObject keyObj = new JSONObject();
			JSONArray list = new JSONArray();// 数据集合
			List<City> cityList = baseDataService.findCity(Constants.CITY_SHI,
					key);
			keyObj.put("key", key);
			for (City city : cityList) {
				obj = new JSONObject();
				obj.put("cid", city.getCid());// 城市id
				obj.put("title", city.getTitle().trim());// 显示的
				obj.put("pinyin",
						city.getPinyin() == null ? "" : city.getPinyin());//
				obj.put("code", city.getCode());//
				list.put(obj);
			}
			keyObj.put("list", list);
			items.put(keyObj);
		}

		data.put("keys", JsonUtil.ArrayToJsonString(keys));
		data.put("items", items);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 用户最新余额
	 */
	public void money(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		User user =null;
		Integer uid =0;
		Map<String, String> decryptMap = DesUtils.decryptMap(u);
		if(decryptMap!=null){
			uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			user = (User) userService.getObject(User.class, uid);
		}
		if (user != null) {
			BigDecimal money = TradeCacheUtil.getUserMoney(user.getUserId());
			BigDecimal drawMoney = user.getDrawMoney();
//			BigDecimal userBalance = user.getUserBalance();
			BigDecimal userBalance = userService.updateUserBanlance(uid);
			BigDecimal addUpRechargeMoney = user.getAddUpRechargeMoney();
			if(addUpRechargeMoney==null) addUpRechargeMoney = new BigDecimal(0.00);
			data.put("addUpRechargeMoney", ProductUtil.BigFormatJud(addUpRechargeMoney));
			if (money != null) {
				data.put("money", ProductUtil.BigFormat(money));
			} else {
				data.put("money", "0.00");
			}
			if (drawMoney != null) {
				data.put("drawMoney", ProductUtil.BigFormat(drawMoney));
			} else {
				data.put("drawMoney", "0.00");
			}
			if (userBalance != null) {
				data.put("userBalance",
						ProductUtil.BigFormatJud(userBalance));
			} else {
				data.put("userBalance", "0.00");
			}
			if (ParamUtils.chkString(user.getLogo())) {
				data.put(
						"logo",
						StringUtil.httpTohttps(request,
								StringUtil.checkAPIHttpUrl(user.getLogo())));
			} else {
				data.put("logo", "");
			}
			if (user.getUserType().equals(Constants.USER_TYPE_AGENT_ONE)
					|| user.getUserType().equals(
							Constants.USER_TYPE_AGENT_TWO)
					|| user.getUserType().equals(
							Constants.USER_TYPE_AGENT_THREE)) {
				data.put("isAgent", "1");
			} else {
				data.put("isAgent", "0");
			}
			List<Param> list = CacheUtil.getParam();
			Param chatroom = null;//聊天室
			Param task = null;//任务赚钱
			for(Param param:list){
				String type = param.getType();
				if(Constants.PARAM_CHATROOM.equals(type)){
					chatroom = param;
				}else if(Constants.PARAM_TASK_SEND.equals(type)){
					task = param;
				}
			}
			Notice notice = noticeService.getLatestNotice("2");
			if(chatroom != null){
				data.put("chatRoom",
						ParamUtils.getStringNotNull(chatroom.getValue()));
			}else{
				data.put("chatRoom", "");
			}
			if(task != null){
				data.put("task",
						ParamUtils.getStringNotNull(task.getValue())+ "?t="
								+ System.currentTimeMillis());
			}else{
				data.put("task", "");
			}
			if (notice != null) {
				data.put("agentSystem",
						ParamUtils.getStringNotNull(Constants.getDomainName()
								+ notice.getLink() + "?t="
								+ System.currentTimeMillis()));
			} else {
				data.put("agentSystem", "");
			}
			
			List<UserLevel> levelList = baseDataService.findUserLevel();
			Integer level = 0;// 用户等级
			BigDecimal nowLevelPoint = new BigDecimal(0);// 当前等级的积分
			BigDecimal nextLevelPoint = new BigDecimal(0);// 下一等级的积分
			BigDecimal scale = new BigDecimal(0);// 进度条的比例
			BigDecimal divisor = new BigDecimal(0);// 除数
			BigDecimal divisor2 = new BigDecimal(0);// 被除数
			if (levelList != null) {// 根据用户积分判断用户是哪个等级的
				JSONObject ulObject = null;
				boolean isNext = false;
				for (int i=0;i < levelList.size();i++) {
					UserLevel userLevel = levelList.get(i);
					BigDecimal point = userLevel.getPoint();
					String lev = userLevel.getLevel();
					if (addUpRechargeMoney.compareTo(point) >= 0) {
						isNext = true;
						if (levelList.size()-1 == i) {
							level = Integer.valueOf(lev);
							nowLevelPoint = point;
						}
					} else {
						if (isNext) {
							level = Integer.valueOf(lev)-1;
							nextLevelPoint = point;
							nowLevelPoint = levelList.get(i-1).getPoint();
							divisor = addUpRechargeMoney.subtract(nowLevelPoint);
							divisor2 = nextLevelPoint.subtract(nowLevelPoint);
						}
						isNext = false;
					}
				}
			}
			data.put("level", level);// 用户的级别
			// 顶级
			if (divisor.compareTo(new BigDecimal(0))==0 && divisor2.compareTo(new BigDecimal(0))==0) {
				data.put("scale", "1");// 积分比例
				data.put("nowLevelPoint", levelList.get(levelList.size()-2).getPoint());// 倒数第二级所需的积分
				data.put("nextLevelPoint", levelList.get(levelList.size()-1).getPoint());// 顶级所需的积分
				data.put("isTopLevel", Constants.PUB_STATUS_OPEN);
			} else {
				data.put("scale", divisor.divide(divisor2,2, BigDecimal.ROUND_DOWN).doubleValue());// 积分比例
				data.put("nowLevelPoint", nowLevelPoint);// 当前等级所需的积分
				data.put("nextLevelPoint", nextLevelPoint);// 下一等级所需的积分
				data.put("isTopLevel", Constants.PUB_STATUS_CLOSE);
			}
			
			
			Param param = paramService.getParamByType(Constants.PARAM_QQ_CUSTOMER_URL);
			String qqUrl = "";
			if (param != null) {
				qqUrl = param.getValue();
			}
			data.put("qqUrl", qqUrl);// qq客服的点击地址
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 用户最新积分
	 */
	public void point(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(message)) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				BigDecimal point = user.getUserpoints();
				if (point != null) {
					data.put("point",
							ParamUtils.chkStringNotNull(point.toString()));
					data.put("thirdCharge",
							Constants.POINTS_CHARGE_THIRD_PAY_SWITCH);
					data.put("status",
							Constants.POINTS_USER_THIRD_INTRODUCTION_SWITCH);
					SysOption sysOption = baseDataService
							.getSysOptionByType(Constants.GAME_POINT_INSTRUCTION);
					if (sysOption != null) {
						data.put("content", sysOption.getContent());
					} else {
						data.put("content", "");
					}
				} else {
					data.put("point", "0");
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 上传图片
	 */
	public void upfile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer uid = ParamUtils.getIntegerParameter(request, "uid");
		Integer itemId = ParamUtils.getIntegerParameter(request, "id");
		// 头像的key是 head
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		// 保存文件
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);
		ServletFileUpload upload = new ServletFileUpload(factory);
		FileItem file = null;
		try {
			List<FileItem> fitems = upload.parseRequest(request);
			for (FileItem fi : fitems) {
				String field = fi.getFieldName();
				String val = fi.getString();
				if (!fi.isFormField()) {// 上传文件
					file = fi;
				} else {
					if (ParamUtils.chkString(val)) {// 从FileItem取参数
						if (field.equals("uid")) {
							uid = Integer.valueOf(val);
						} else if (field.equals("id")) {
							itemId = Integer.valueOf(val);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = APIConstants.CODE_REQUEST_ERROR;
			message = APIConstants.TIPS_SERVER_ERROR;
		}

		// if (!ParamUtils.chkInteger(uid)) {
		// message = APIConstants.PARAMS_EMPTY_MSG;
		// }

		if (message.equals("")) {
			try {
				int maxPostSize = 100 * 1024 * 1024;
				String uploadPath = Constants.getWebRootPath();
				String uploadFolder = Constants.getFileUploadPath();

				upload.setSizeMax(maxPostSize);

				if (file != null && file.getSize() > 0) {
					String fname = file.getName();
					String fileExt = UploadUtil.getFileExt(fname);
					String getUniqueID = null;
					getUniqueID = UploadUtil.GetUniqueID();
					fname = getUniqueID + fileExt;// 文件名
					String folder = UploadUtil.getFolder2("/head");
					uploadFolder = uploadFolder + folder;
					String upPath = uploadPath + uploadFolder;
					new FSO().createFolder(upPath);
					try {
						System.out.println(upPath + "/" + fname);
						File saveFile = new File(upPath + "/" + fname);
						file.write(saveFile);
						data.put("logo", Constants.getDomainName()
								+ uploadFolder + "/" + fname);
						data.put("uid", uid);
						data.put("id", itemId);
						code = APIConstants.CODE_REQUEST_SUCCESS;
					} catch (Exception e) {
						code = APIConstants.CODE_REQUEST_ERROR;
						message = APIConstants.TIPS_SERVER_ERROR;
						e.printStackTrace();
					}
				} else {
					code = APIConstants.CODE_REQUEST_ERROR;
					message = APIConstants.FILE_TIP_NOT_FILE;
				}

			} catch (Exception e) {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = APIConstants.TIPS_SERVER_ERROR;
				e.printStackTrace();
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 生成图形码
	 */
	public void checkCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("image/jpeg");
		// ?? ? ? 即?? 校验 ??
		String checkCode = RandomStringUtils.randomNumeric(4);
		// ? ?
		BufferedImage image = new BufferedImage(50, 18,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		Font font = new Font(g2d.getFont().getFontName(), Font.PLAIN, 18);
		g2d.setFont(font);
		g2d.setColor(Color.white);
		g2d.fill(new Rectangle(image.getWidth(), image.getHeight()));
		g2d.setColor(Color.darkGray);

		for (int i = 0; i < ((int) (Math.random() * 60) + 20); ++i) {

			g2d.fill(new Rectangle((int) (Math.random() * image.getWidth()),
					(int) (Math.random() * image.getHeight()), 1, 1));
		}

		g2d.setPaint(Color.blue);
		g2d.drawString(checkCode, 2, 15); // (int) (Math.random() * 15) + 5
		g2d.dispose();

		// 输出
		try {

			// ImageIO.write(image, "gif", response.getOutputStream());
			OutputStream out = response.getOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.flush();
		} catch (Exception ex) {
			if (log.isInfoEnabled()) {
				log.info(ex);
			}
		}

		request.getSession().setAttribute("chkCode", checkCode);
	}

	/**
	 * 获取用户的默认绑定账户
	 */
	public void bindBank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			UserBankBind bank = cashService.getUserBankBindByUid(uid);
			JSONObject obj = new JSONObject();// 返回数据层
			if (bank != null) {
				data.put("isDef", "1");
				obj.put("bindId", bank.getBankBindId());
				obj.put("bankName", bank.getBankName());
				obj.put("bindName", bank.getBindName());
				obj.put("bindAccount",
						bank.getBindAccount()
								.substring(0,
										bank.getBindAccount().length() - 4)
								.replaceAll("[0-9]", "*")
								+ bank.getBindAccount().substring(
										bank.getBindAccount().length() - 4,
										bank.getBindAccount().length()));
				obj.put("bindType", bank.getBindType());
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				data.put("isDef", "0");
				obj.put("bindId", "");
				obj.put("bankName", "");
				obj.put("bindAccount", "");
				obj.put("bindName", "");
				obj.put("bindType", "");
				code = APIConstants.CODE_REQUEST_SUCCESS;
				map.put("msg", "您还没有绑定的银行卡");
			}
			data.put("obj", obj);
		}
		map.put("msg", message);
		map.put("code", code);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 徒弟列表
	 */
	public void apprenticelist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String u = ParamUtils.getParameter(deParameter, "u");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// RamConstants.MAXPAGEITEMS
																				// 查出所有的所以写1000条
		String message = "";
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = userService.getUser(uid);
			}
			if (user != null) {
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				hqls.append(" and u.agentId = ? ");
				para.add(uid);
				hqls.append(" order by u.userId desc ");
				PaginationSupport ps = userService.findUser(
						hqls.toString(), para, statIndex, pageSize);
				List<User> list = ps.getItems();
				JSONArray items = new JSONArray();// 数据集合
				if (list != null && list.size() > 0) {
					for (User user1 : list) {
						JSONObject obj = new JSONObject();
//						obj.put("logo", ParamUtils
//								.getStringNotNull(StringUtil
//										.checkAPIHttpUrl(user1.getLogo())));
						obj.put("logo", "/images/head/"+StringUtil.getRandomInt(0, 9)+".png");
						obj.put("userId", user1.getUserId());
						obj.put("createTime",
								DateTimeUtil.DateToStringHHMM(user1
										.getRegistDateTime()));
						items.put(obj);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					code = APIConstants.CODE_NOT_FOUND;
				}
				data.put("items", items);
				data.put("pageIndex", pageIndex);
				data.put("pageNum", list.size());// 当前页数量
				data.put("pageSize", pageSize);// 每页条数
				data.put("total", ps.getTotalCount());// 总数量
			} else {
				message = APIConstants.TIPS_NOT_USER;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 首页游戏栏目
	 */
	public void gameColumn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型	// 1.IOS
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		String u = ParamUtils.getParameter(deParameter, "u");// 当前版本
//		String playCate= ParamUtils.getParameter(deParameter, "playCate");//玩法类型web 可以不传  app传1=官方  2=信用
		JSONArray gfGameList = new JSONArray();// 数据集合
		JSONArray xyGameList = new JSONArray();// 数据集合
		
//		List<GaSessionInfo> list = CacheUtil.getGameList();
//		for (GaSessionInfo gaSessionInfo : list) {
//			JSONObject obj = new JSONObject();
//			obj.put("xcode", gaSessionInfo.getInfoId());
//			String gameType = gaSessionInfo.getGameType();
//			String liveUrl = "";
//			if (gameType.equals("1")) {// 北京赛车
//				liveUrl = "";
//			}
//			String playCate = gaSessionInfo.getPlayCate();
//			obj.put("liveUrl", ParamUtils.chkStringNotNull(StringUtil
//					.checkAPIHttpUrl(liveUrl)));
//			obj.put("type", gaSessionInfo.getGameType());
//			obj.put("status", gaSessionInfo.getStatus());
//			obj.put("img",
//					StringUtil.httpTohttps(request,
//							StringUtil.checkAPIHttpUrl(gaSessionInfo.getImg())));
//			obj.put("title", gaSessionInfo.getGameTitle());
//			obj.put("exp", gaSessionInfo.getExp());
//			if("1".equals(playCate)){//官方
//				obj.put("subtitle", gaSessionInfo.getGameTitle());//
//				gfGameList.put(obj);
//			}else if("2".equals(playCate)){//信用
//				obj.put("subtitle", gaSessionInfo.getGameTitle());//
//				xyGameList.put(obj);
//			}
//		}
		
		String turnTableSwitch = "0";
		String redPacketsSwitch = "0";

		LotterySetting redls=CacheUtil.getLotterySetting("redpackets");
		LotterySetting turnls=CacheUtil.getLotterySetting("turntable");
		
		if (Constants.LOTTERY_SETTING_TURNTABLE.equals(turnls.getType())) {
			if (ParamUtils.chkString(turnls.getStatus())) {
				turnTableSwitch = turnls.getStatus();
			}
		} 
		if (Constants.LOTTERY_SETTING_REDPACKETS.equals(redls.getType())) {
			if (ParamUtils.chkString(redls.getStatus())) {
				redPacketsSwitch = redls.getStatus();
			}
		}

		data.put("xyGamelist", xyGameList);
		data.put("gfGamelist", gfGameList);
		data.put("turnTableSwitch", turnTableSwitch);
		data.put("redPacketsSwitch", redPacketsSwitch);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 中奖记录
	 */
	public void winList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
		// 1.IOS      2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本

		data.put("winlist", CacheUtil.getWinlist(device, ver));
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 所有奖项最新一期 ||购彩大厅   官方
	 */
	public void gflatestList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray k10List = new JSONArray();// 数据集合
		List<GaSessionInfo> list = gaService.findGaSessionInfoList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GaSessionInfo sessionInfo = list.get(i);
				if(sessionInfo.getPlayCate().equals("1")){
					JSONObject obj = new JSONObject();// 返回数据层
					obj.put("type", sessionInfo.getGameType());
					obj.put("gameName", sessionInfo.getGameTitle());
					obj.put("openSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getOpenSessionNo()));
					obj.put("img",
							StringUtil.httpTohttps(
									request,
									StringUtil.checkAPIHttpUrl(ParamUtils
											.chkStringNotNull(sessionInfo.getImg())))
									+ "?t=" + System.currentTimeMillis()); // 图标
					JSONArray jsonArray = new JSONArray();
					if (ParamUtils.chkString(sessionInfo.getOpenResult())) {
						String[] results = sessionInfo.getOpenResult().split(
								",|，|\\+|\\|");
						for (int j = 0; j < results.length; j++) {
							jsonArray.put(results[j]);
						}
					}
					obj.put("openResult", jsonArray);
					obj.put("openTime",
							DateTimeUtil.DateToStringMMddHHmm(sessionInfo.getEndTime()));
					obj.put("explain",
							ParamUtils.chkStringNotNull(sessionInfo.getExp())); // 描述
					obj.put("gameCode", sessionInfo.getGameCode());
					// obj.put("latestSessionNo", ParamUtils
					// .getStringNotNull(sessionInfo.getLatestSessionNo()));
					k10List.put(obj);
				}else{
					continue;
				}
			}
		}
		data.put("items", k10List);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	/**
	 * 所有奖项最新一期    信用
	 */
	public void latestList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray k10List = new JSONArray();// 数据集合
		String code = APIConstants.CODE_REQUEST_ERROR;
		List<GaSessionInfo> list = CacheUtil.getShowOpenGameList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GaSessionInfo sessionInfo = list.get(i);
					JSONObject obj = new JSONObject();// 返回数据层

					obj.put("xcode", sessionInfo.getInfoId());
					String gameType = sessionInfo.getGameType();
					String liveUrl = "";
					if (gameType.equals("1")) {// 北京赛车
						liveUrl = "";
					}
					obj.put("liveUrl", ParamUtils.chkStringNotNull(StringUtil
							.checkAPIHttpUrl(liveUrl)));

					obj.put("type", sessionInfo.getGameType());
					obj.put("gameName", sessionInfo.getGameTitle());
					obj.put("openSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getOpenSessionNo()));
					obj.put("time",
							DateTimeUtil.DateToStringHHMM(sessionInfo.getEndTime()));
					obj.put("img", StringUtil.httpTohttps(request,
							StringUtil.checkAPIHttpUrl(sessionInfo.getImg())));
					obj.put("code",sessionInfo.getGameCode());
					obj.put("cate", sessionInfo.getPlayCate());
					JSONArray jsonArray = new JSONArray();
					if (ParamUtils.chkString(sessionInfo.getOpenResult())) {
						if (sessionInfo.getGameType().equals(
								Constants.GAME_TYPE_XY_XJPLU28)) {// 幸运28

							String result = sessionInfo.getOpenResult();
							String number1 = result.split(",")[0];
							String number2 = result.split(",")[1];
							String number3 = result.split(",")[2];
							String number = result.split(",")[3];
							String colour = result.split(",")[4];

							jsonArray.put(number1);
							jsonArray.put(number2);
							jsonArray.put(number3);
							jsonArray.put(number);
							jsonArray.put(colour);
						} else if (sessionInfo.getGameType().equals(
								Constants.GAME_TYPE_XY_JSK3)) {//
							String result = sessionInfo.getOpenResult();
							String number1 = result.split(",")[0];
							String number2 = result.split(",")[1];
							String number3 = result.split(",")[2];
//							String number = result.split(",")[3];
//							String colour = result.split(",")[4];

							jsonArray.put(number1);
							jsonArray.put(number2);
							jsonArray.put(number3);
//							jsonArray.put(number);
//							jsonArray.put(colour);
						} else if (sessionInfo.getGameType().equals(
								Constants.GAME_TYPE_XY_BJLU28)) {// PC蛋蛋
							String result = sessionInfo.getOpenResult();
							String number1 = result.split(",")[0];
							String number2 = result.split(",")[1];
							String number3 = result.split(",")[2];
							String number = result.split(",")[3];
							String colour = result.split(",")[4];
							// String string =
							// number1+","+number2+","+number3+","+number+","+colour;
							jsonArray.put(number1);
							jsonArray.put(number2);
							jsonArray.put(number3);
							jsonArray.put(number);
							jsonArray.put(colour);
						} else if (sessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_MARKSIX)
								||sessionInfo.getGameType().equals(Constants.GAME_TYPE_XY_SFLHC)) {//
							String[] results = sessionInfo.getOpenResult().split(
									",|，|\\+");
							for (int j = 0; j < results.length; j++) {
								jsonArray.put(results[j]);
							}
						} else {
							String[] results = sessionInfo.getOpenResult().split(
									",");
							for (int j = 0; j < results.length; j++) {
								jsonArray.put(results[j]);
							}
						}
					}
					obj.put("openResult", jsonArray);
					obj.put("latestSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getLatestSessionNo()));
					k10List.put(obj);
					code = APIConstants.CODE_REQUEST_SUCCESS;
				}	
			//}
		} else {
			code = APIConstants.CODE_NOT_FOUND;
		}
		data.put("items", k10List);
		map.put("code", code);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 彩票开奖
	 */
	public void lotteryDraw(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray k10List = new JSONArray();// 数据集合
		JSONArray xyItems = new JSONArray();// 信用数据集合
		List<GaSessionInfo> list = gaService.findGaSessionInfoList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GaSessionInfo sessionInfo = list.get(i);
				if(sessionInfo.getPlayCate().equals("1")){
					String gameType = ParamUtils.chkStringNotNull(sessionInfo.getGameType());
					JSONObject obj = new JSONObject();// 返回数据层
					// obj.put("type", sessionInfo.getGameType());
					obj.put("gameName",
							ParamUtils.chkStringNotNull(sessionInfo.getGameTitle()));
					obj.put("openSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getOpenSessionNo()));
					obj.put("img", StringUtil.httpTohttps(request, StringUtil
							.checkAPIHttpUrl(ParamUtils
									.chkStringNotNull(sessionInfo.getImg())))); // 图标
					JSONArray jsonArray = new JSONArray();
					if (ParamUtils.chkString(sessionInfo.getOpenResult())) {
						String[] results = sessionInfo.getOpenResult().split(
								",|，|\\+|\\|");
						for (int j = 0; j < results.length; j++) {
							jsonArray.put(results[j]);
						}
						if(Constants.GAME_TYPE_GF_THREE.equals(gameType)||Constants.GAME_TYPE_GF_FC.equals(gameType)
								||Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
							obj.put("afThree",CqSscUtil.getAfterThree(sessionInfo.getOpenResult())); //后三
						}else{
							obj.put("afThree","");
						}
					}else{
						obj.put("afThree","");
					}
					obj.put("openResult", jsonArray);
					obj.put("openTime",
							DateTimeUtil.DateToStringAll(sessionInfo.getEndTime())); // 开奖时间
					obj.put("explain",
							ParamUtils.chkStringNotNull(sessionInfo.getExp())); // 描述
					obj.put("gameType", gameType);// 彩票类型
					obj.put("gameCode", sessionInfo.getGameCode());// 彩票别名
					// obj.put("latestSessionNo", ParamUtils
					// .getStringNotNull(sessionInfo.getLatestSessionNo()));
					k10List.put(obj);
				} else if(sessionInfo.getPlayCate().equals("2")){
					String gameType = ParamUtils.chkStringNotNull(sessionInfo.getGameType());
					JSONObject obj = new JSONObject();// 返回数据层
					// obj.put("type", sessionInfo.getGameType());
					obj.put("gameName",
							ParamUtils.chkStringNotNull(sessionInfo.getGameTitle()));
					obj.put("openSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getOpenSessionNo()));
					obj.put("img", StringUtil.httpTohttps(request, StringUtil
							.checkAPIHttpUrl(ParamUtils
									.chkStringNotNull(sessionInfo.getImg())))); // 图标
					JSONArray jsonArray = new JSONArray();
					if (ParamUtils.chkString(sessionInfo.getOpenResult())) {
						String[] results = sessionInfo.getOpenResult().split(
								",|，|\\+|\\|");
						for (int j = 0; j < results.length; j++) {
							jsonArray.put(results[j]);
						}
						if(Constants.GAME_TYPE_GF_THREE.equals(gameType)||Constants.GAME_TYPE_GF_FC.equals(gameType)
								||Constants.GAME_TYPE_GF_CQSSC.equals(gameType)){
							obj.put("afThree",CqSscUtil.getAfterThree(sessionInfo.getOpenResult())); //后三
						}else{
							obj.put("afThree","");
						}
					}else{
						obj.put("afThree","");
					}
					obj.put("openResult", jsonArray);
					obj.put("openTime",
							DateTimeUtil.DateToStringAll(sessionInfo.getEndTime())); // 开奖时间
					obj.put("explain",
							ParamUtils.chkStringNotNull(sessionInfo.getExp())); // 描述
					obj.put("gameType", gameType);// 彩票类型
					obj.put("gameCode", sessionInfo.getGameCode());// 彩票别名
					// obj.put("latestSessionNo", ParamUtils
					// .getStringNotNull(sessionInfo.getLatestSessionNo()));
					xyItems.put(obj);
				}
				
			}
		}
		data.put("items", k10List);
		data.put("xyItems", xyItems);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 所有有效彩票的名称和类型
	 */
	public void lotteryNameAndType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray gfItems = new JSONArray();// 官方数据集合
		JSONArray xyItems = new JSONArray();// 信用数据集合
		List<GaSessionInfo> list = gaService.findGaSessionInfoList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				GaSessionInfo sessionInfo = list.get(i);
				if(sessionInfo.getPlayCate().equals("1")){
					String gameType = ParamUtils.chkStringNotNull(sessionInfo.getGameType());
					JSONObject obj = new JSONObject();// 返回数据层
					obj.put("gameName",
							ParamUtils.chkStringNotNull(sessionInfo.getGameTitle()));
					obj.put("gameType", gameType);// 彩票类型
					obj.put("gameCode", sessionInfo.getGameCode());// 彩票别名
					gfItems.put(obj);
				} else if(sessionInfo.getPlayCate().equals("2")){
					String gameType = ParamUtils.chkStringNotNull(sessionInfo.getGameType());
					JSONObject obj = new JSONObject();// 返回数据层
					obj.put("gameName",
							ParamUtils.chkStringNotNull(sessionInfo.getGameTitle()));
					obj.put("gameType", gameType);// 彩票类型
					obj.put("gameCode", sessionInfo.getGameCode());// 彩票别名
					xyItems.put(obj);
				}
			}
		}
		data.put("gfItems", gfItems);
		data.put("xyItems", xyItems);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 开奖公告
	 */
	public void lotteryNotice(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		JSONArray k10List = new JSONArray();// 数据集合
		List<GaSessionInfo> list = gaService.findGaSessionInfoList();
		if (list != null && list.size() > 0) {
			String gameType = null;
			for (int i = 0; i < list.size(); i++) {
				GaSessionInfo sessionInfo = list.get(i);
				gameType = sessionInfo.getGameType();
				JSONObject obj = new JSONObject();// 返回数据层
				if (Constants.GAME_TYPE_XY_CQSSC.equals(gameType)
						|| Constants.GAME_TYPE_XY_BJPK10.equals(gameType)
						|| Constants.GAME_TYPE_XY_XJPLU28.equals(gameType)
					){
					obj.put("gameName", ParamUtils.chkStringNotNull(sessionInfo
							.getGameTitle()));
					obj.put("gameType", ParamUtils.chkStringNotNull(sessionInfo
							.getGameType()));
					obj.put("openSessionNo", ParamUtils
							.getStringNotNull(sessionInfo.getOpenSessionNo()));
					JSONArray jsonArray = new JSONArray();
					if (ParamUtils.chkString(sessionInfo.getOpenResult())) {
						String[] results = sessionInfo.getOpenResult().split(
								",|，|\\+|\\|");
						for (int j = 0; j < results.length; j++) {
							jsonArray.put(results[j]);
						}
					}
					obj.put("openResult", jsonArray);
					obj.put("openTime", DateTimeUtil
							.DateToStringAll(sessionInfo.getEndTime())); // 开奖时间
					k10List.put(obj);
				}
			}
		}
		data.put("items", k10List);
		map.put("code", APIConstants.CODE_REQUEST_SUCCESS);
		map.put("msg", "");
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 投注记录 ||购彩记录
	 */
	/*public void gfbetlist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		String gameType = ParamUtils.getParameter(deParameter, "gameType"); // 彩票类型
		String startTime = ParamUtils.getParameter(deParameter, "startTime");// 开始时间--web
		String endTime = ParamUtils.getParameter(deParameter, "endTime");// 结束时间--web
		String status = ParamUtils.getParameter(deParameter, "status"); // 彩票类型

		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			// if(true){
			// Integer uid =22757;
			// startTime = "2017-08-01";
			// endTime = "2017-08-10";
			// status = "5";

			User user = (User) userService.getObject(User.class, uid);
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(gameType)) {
					hqls.append(" and sp.gameType=? ");
					para.add(gameType);
				}
				if (ParamUtils.chkString(startTime)
						&& ParamUtils.chkString(endTime)) {
					Date startDate = DateTimeUtil.stringToDate(startTime,
							"yyyy-MM-dd");
					Date endDate = DateTimeUtil.stringToDate(endTime,
							"yyyy-MM-dd");
					if (startDate == null || endDate == null
							|| startDate.compareTo(endDate) == 1) {
						message = "参数错误！";
						map.put("data", data);
						map.put("code", code);
						map.put("msg", message);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					hqls.append(" and sp.betTime >= ? ");
					para.add(startDate);
					hqls.append(" and sp.betTime <= ? ");
					para.add(endDate);
				}

				if (ParamUtils.chkString(status)) {
					if (!"0".equals(status)) {// 全部
						String winResult = "";

						if ("1".equals(status)) {// 进行中
							winResult = Constants.INIT;
						} else if ("2".equals(status)) { // 未中奖
							winResult = Constants.WIN_NO;
						} else if ("3".equals(status)) {// 已中奖
							winResult = Constants.WIN;
						} else if ("4".equals(status)) {// 中奖撤单
							winResult = Constants.INVALID_REFUND;
							hqls.append(" and sp.betType = ? ");
							para.add(Constants.PROCUREMENT_SERVICE); // 代购
						} else if ("5".equals(status)) {// 流产撤单
							winResult = Constants.INVALID_REFUND;
							hqls.append(" and sp.betType = ? ");
							para.add(Constants.SPONSOR); // 合买
						} else {
							message = "参数错误！";
							map.put("data", data);
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}

						hqls.append(" and sp.winResult = ? ");
						para.add(winResult);
					}
				}
				hqls.append(" and u.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by  sp.jointId desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findGaBetSponsorDetail(
						hqls.toString(), para, statIndex, pageSize);
				List<SpDetailDTO> list = ps.getItems();

				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					// JSONObject obj = new JSONObject();
					for (int i = 0; i < list.size(); i++) {
						GaBetSponsor sp = list.get(i).getGaBetSponsor();
						GaBetPart pa = list.get(i).getGaBetPart();
						JSONObject it = new JSONObject();
						it.put("gameName", sp.getGameName());
						it.put("sessionNo", sp.getSessionNo());
						it.put("jointId", sp.getJointId());
						it.put("orderNum", sp.getOrderNum());// 方案号
						String betType = sp.getBetType();// 方案类型
						if (Constants.PROCUREMENT_SERVICE.equals(betType)) {
							it.put("betType", "单");
						} else if (Constants.SPONSOR.equals(betType)) {
							it.put("betType", "合");
						}
						String spName = list.get(i).getUserName();// 发起人
						if (spName.length() < 2) {
							spName = spName + "***";
						} else {
							spName = spName.substring(0, 2) + "***";
						}
						it.put("spName", spName); // 发起人
						it.put("money",
								sp.getMoney()
										.setScale(2, BigDecimal.ROUND_DOWN)
										.toString()); // 方案金额
						it.put("betMoney",
								pa.getBetMoney()
										.setScale(2, BigDecimal.ROUND_DOWN)
										.toString()); // 认购金额
						if (pa.getWinCash() != null) {
							it.put("winCash",
									pa.getWinCash()
											.setScale(2, BigDecimal.ROUND_DOWN)
											.toString()); // 奖金
						} else {
							it.put("winCash", "0"); // 奖金
						}
						if (pa.getWinPoint() != null) {
							it.put("betPoint",
									pa.getWinPoint()
											.setScale(2, BigDecimal.ROUND_DOWN)
											.toString());// 积分
						} else {
							it.put("betPoint", "0");// 积分
						}

						it.put("betTime",
								DateTimeUtil.DateToStringHHMM(pa.getBuyTime()));// 时间
						it.put("winResult", sp.getWinResult());// 状态
						it.put("winResult2",
								Constants.getWinResultNameZh(sp.getWinResult()));// 状态
						it.put("gameType", sp.getGameType());// 玩法类型
						it.put("num", sp.getNum());// 总份数
						it.put("preMoney",
								ProductUtil.checkBigDecimal(sp.getPreMoney()));// 每份金额
						items.put(it);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}*/

	/**
	 * 账户明细
	 */
	public void accountDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String cashType = ParamUtils.getParameter(deParameter, "cashType"); // 资金来源类型--web
		String u = ParamUtils.getParameter(deParameter, "u");
		String startTime = ParamUtils.getParameter(deParameter, "startTime");// 开始时间--web
		String endTime = ParamUtils.getParameter(deParameter, "endTime");// 结束时间--web

		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			// if(true){
			// Integer uid = 22757;
			// startTime = "2017-08-01";
			// endTime = "2017-08-10";

			User user = (User) userService.getObject(User.class, uid);
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(cashType)) {
					hqls.append(" and ho.cashType=? ");
					para.add(cashType);
				}
				if (ParamUtils.chkString(startTime)
						&& ParamUtils.chkString(endTime)) {
					Date startDate = DateTimeUtil.stringToDate(startTime,
							"yyyy-MM-dd");
					Date endDate = DateTimeUtil.stringToDate(endTime,
							"yyyy-MM-dd");
					if (startDate == null || endDate == null
							|| startDate.compareTo(endDate) == 1) {
						message = "参数错误！";
						map.put("data", data);
						map.put("code", code);
						map.put("msg", message);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					hqls.append(" and ho.createTime >= ? ");
					para.add(startDate);
					hqls.append(" and ho.createTime <= ? ");
					para.add(endDate);
				}

				hqls.append(" and ho.userId=? ");
				para.add(user.getUserId());
				hqls.append(" and ho.status = ? ");
				para.add(Constants.PUB_STATUS_OPEN);
				hqls.append(" order by  ho.createTime desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = cashService.findUserTradeDetailList(
						hqls.toString(), para, statIndex, pageSize);
				List<UserTradeDetail> list = ps.getItems();

				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (int i = 0; i < list.size(); i++) {
						UserTradeDetail de = list.get(i);
						JSONObject it = new JSONObject();
						it.put("tradeTime", DateTimeUtil.DateToStringAll(de
								.getCreateTime())); // 交易时间
						if (ProductUtil.checkBigDecimal(de.getCashMoney()).compareTo(new BigDecimal(0)) == 0) {
							it.put("income", "0.00");
							it.put("pay", "0.00");// 支出
						} else {
							if (Constants.TRADE_TYPE_INCOME.equals(de
									.getTradeType())) {// 收入
								it.put("income", "+" + ProductUtil.BigFormatJud(de.getCashMoney()));
								it.put("pay", "0.00");// 支出
							} else {// 支出
								it.put("income", "0.00");
								it.put("pay", ProductUtil.BigFormatJud(de.getCashMoney()));// 支出
							}
						}
						it.put("userMoney", ProductUtil.BigFormatJud(de
								.getUserMoney()));// 交易后余额
						it.put("remark",
								ParamUtils.chkStringNotNull(de.getRemark())); // 备注
						String type = de.getCashType();

						it.put("cashType",
								Constants.getCashTradeTypeNameZh(type));
						// if (Constants.CASH_TYPE_CASH_BUY_LOTO.equals(type)) {
						// it.put("cashType", "购买彩票"); // 资金类型
						// } else if (Constants.CASH_TYPE_CASH_SPONSOR
						// .equals(type)) {
						// it.put("cashType", "参与合买");
						// } else if (Constants.CASH_TYPE_CASH_GUARANTEE
						// .equals(type)) {
						// it.put("cashType", "方案保底");
						// } else if
						// (Constants.CASH_TYPE_CASH_FROZEN.equals(type)) {
						// it.put("cashType", "保底冻结");
						// } else if
						// (Constants.CASH_TYPE_CASH_DRAW.equals(type)) {
						// it.put("cashType", "方案撤单");
						// } else if (Constants.CASH_TYPE_ONLINE.equals(type)) {
						// it.put("cashType", "在线充值 ");
						// } else if (Constants.CASH_TYPE_CASH_OUT.equals(type))
						// {
						// it.put("cashType", "用户提款 ");
						// } else if
						// (Constants.CASH_TYPE_CASH_SYSTEM.equals(type)) {
						// it.put("cashType", "系统充值 ");
						// } else if (Constants.CASH_TYPE_CASH_SYS_CHARGE
						// .equals(type)) {
						// it.put("cashType", "系统扣款 ");
						// } else if (Constants.CASH_TYPE_CASH_PRESENT
						// .equals(type)) {
						// it.put("cashType", "注册赠送 ");
						// } else if (Constants.CASH_TYPE_CASH_GUA_BACK
						// .equals(type)) {
						// it.put("cashType", "方案退保 ");
						// } else if
						// (Constants.CASH_TYPE_CASH_PRIZE.equals(type)) {
						// it.put("cashType", "中奖彩派 ");
						// } else if (Constants.CASH_TYPE_CASH_EXCHANGE
						// .equals(type)) {
						// it.put("cashType", "中积分兑换 ");
						// }
						// 资金类型 10=购买彩票，21=参与合买，11=方案保底，12=保底冻结，13=方案撤单，1=在线充值
						// ，5=用户提款，
						// 14=系统充值，15=系统扣款，16=注册赠送，17=方案退保，18=中奖彩派，20=积分兑换
						items.put(it);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 积分明细
	 */
	public void pointDetails(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String cashType = ParamUtils.getParameter(deParameter, "cashType"); // 积分来源类型
		String u = ParamUtils.getParameter(deParameter, "u");
		String startTime = ParamUtils.getParameter(deParameter, "startTime");// 开始时间--web
		String endTime = ParamUtils.getParameter(deParameter, "endTime");// 结束时间--web
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码--web
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量--web
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			// if(true){
			// Integer uid = 22757;
			// startTime = "2017-08-01";
			// endTime = "2017-08-10";
			User user = (User) userService.getObject(User.class, uid);
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(cashType)) {
					hqls.append(" and ho.cashType=? ");
					para.add(cashType);
				}
				if (ParamUtils.chkString(startTime)
						&& ParamUtils.chkString(endTime)) {
					Date startDate = DateTimeUtil.stringToDate(startTime,
							"yyyy-MM-dd");
					Date endDate = DateTimeUtil.stringToDate(endTime,
							"yyyy-MM-dd");
					if (startDate == null || endDate == null
							|| startDate.compareTo(endDate) == 1) {
						message = "参数错误！";
						map.put("data", data);
						map.put("code", code);
						map.put("msg", message);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					hqls.append(" and ho.createTime >= ? ");
					para.add(startDate);
					hqls.append(" and ho.createTime <= ? ");
					para.add(endDate);
				}
				hqls.append(" and ho.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by  ho.createTime desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = cashService.findUserPointDetailList(
						hqls.toString(), para, statIndex, pageSize);
				List<UserPointDetail> list = ps.getItems();

				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (int i = 0; i < list.size(); i++) {
						UserPointDetail de = list.get(i);
						JSONObject it = new JSONObject();
						it.put("pointId", de.getTradeDetailId()); // id
						it.put("time", DateTimeUtil.DateToStringAll(de
								.getCreateTime())); // 更新时间
						if (de.getCashPoint().compareTo(new BigDecimal(0)) == 0) {
							it.put("income", "0");
							it.put("pay", "0");// 支出
						} else {
							if (Constants.TRADE_TYPE_INCOME.equals(de
									.getTradeType())) {// 收入
								it.put("income", "+"
										+ de.getCashPoint().intValue());
								it.put("pay", "0");// 支出
							} else {// 支出
								it.put("income", "0");
								it.put("pay", "-"
										+ de.getCashPoint().intValue());// 支出
							}
						}
						if (de.getUserPoint() != null) {
							it.put("userPoint", de.getUserPoint().intValue());// 交易后积分余额
						} else {
							it.put("userPoint", "0");// 交易后积分余额
						}
						it.put("tradeTime",
								DateTimeUtil.format(de.getCreateTime())); // 备注
						it.put("remark", de.getRemark()); // 备注
						items.put(it);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				}

				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
				data.put("userName",
						ParamUtils.chkStringNotNull(user.getUserName()));// 收款账户
				data.put("userPoints",
						ProductUtil.checkBigDecimal(user.getUserpoints())
								.intValue());// 剩余积分
				data.put("ratio", "100积分可兑换1元"); // 兑换比例
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 投注记录(官方)
	 */
	public void betlist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		String gameType = ParamUtils.getParameter(deParameter, "gameType"); // 彩票类型
		String startTime = ParamUtils.getParameter(deParameter, "startTime");// 开始时间--web
		String endTime = ParamUtils.getParameter(deParameter, "endTime");// 结束时间--web
		String status = ParamUtils.getParameter(deParameter, "status"); // 彩票类型

		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			User user = (User) userService.getObject(User.class, uid);
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(gameType)) {
					hqls.append(" and sp.gameType=? ");
					para.add(gameType);
				}
				if (ParamUtils.chkString(startTime)
						&& ParamUtils.chkString(endTime)) {
					Date startDate = DateTimeUtil.stringToDate(startTime,
							"yyyy-MM-dd");
					Date endDate = DateTimeUtil.stringToDate(endTime,
							"yyyy-MM-dd");
					if (startDate == null || endDate == null
							|| startDate.compareTo(endDate) == 1) {
						message = "参数错误！";
						map.put("data", data);
						map.put("code", code);
						map.put("msg", message);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					hqls.append(" and sp.betTime >= ? ");
					para.add(startDate);
					hqls.append(" and sp.betTime <= ? ");
					para.add(endDate);
				}

				if (ParamUtils.chkString(status)) {
					if (!"0".equals(status)) {// 全部
						String winResult = "";
						if ("1".equals(status)) {// 进行中
							winResult = Constants.INIT;
						} else if ("2".equals(status)) { // 未中奖
							winResult = Constants.WIN_NO;
						} else if ("3".equals(status)) {// 已中奖
							winResult = Constants.WIN;
						} else if ("4".equals(status)) {// 中奖撤单
							winResult = Constants.INVALID_REFUND;
							hqls.append(" and sp.betType = ? ");
							para.add(Constants.PROCUREMENT_SERVICE); // 代购
						} else if ("5".equals(status)) {// 流产撤单
							winResult = Constants.INVALID_REFUND;
							hqls.append(" and sp.betType = ? ");
							para.add(Constants.SPONSOR); // 合买
						} else {
							message = "参数错误！";
							map.put("data", data);
							map.put("code", code);
							map.put("msg", message);
							JsonUtil.AjaxWriter(response, map);
							return;
						}

						hqls.append(" and sp.winResult = ? ");
						para.add(winResult);
					}
				}
				hqls.append(" and u.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by sp.jointId desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findGaBetSponsorDetail(
						hqls.toString(), para, statIndex, pageSize);
				List<SpDetailDTO> list = ps.getItems();

				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (int i = 0; i < list.size(); i++) {
						SpDetailDTO dto = list.get(i);
						BigDecimal winCash = dto.getWinCash();
						JSONObject it = new JSONObject();
						it.put("sessionNo", dto.getSessionNo());
						it.put("gameName", dto.getGameName());
						it.put("multiple", dto.getMultiple());// 购买倍数
						it.put("betMoney", dto.getMoney().setScale(
								2, BigDecimal.ROUND_DOWN).toString()); // 投注金额
						if (winCash != null 
								&& winCash.compareTo(new BigDecimal(0)) != -1) {
							it.put("winCash", winCash.setScale(
									2, BigDecimal.ROUND_DOWN).toString()); // 中奖金额
						} else {
							it.put("winCash", "0.00"); // 中奖金额
						}
						it.put("betTime",
								DateTimeUtil.DateToStringHHMM(dto.getBetTime()));// 时间
						it.put("winResult", dto.getWinResult());// 状态
						it.put("winResult2",
								Constants.getWinResultNameZh(dto.getWinResult()));// 中文状态
						it.put("playName", dto.getTitle());// 玩法名称
//						it.put("orderNum", sp.getOrderNum());// 方案号
//						it.put("gameType", sp.getGameType());// 玩法类型
//						
//						it.put("betMoney",pa.getBetMoney().setScale(
//								2, BigDecimal.ROUND_DOWN).toString()); // 认购金额
//						it.put("jointId", sp.getJointId());
//						String betType = sp.getBetType();// 方案类型
//						if (Constants.PROCUREMENT_SERVICE.equals(betType)) {
//							it.put("betType", "单");
//						} else if (Constants.SPONSOR.equals(betType)) {
//							it.put("betType", "合");
//						}
//						String spName = list.get(i).getUserName();// 发起人
//						if (spName.length() < 2) {
//							spName = spName + "***";
//						} else {
//							spName = spName.substring(0, 2) + "***";
//						}
//						it.put("spName", spName); // 发起人
//						if (pa.getWinPoint() != null) {
//							it.put("betPoint", pa.getWinPoint().setScale(
//									2, BigDecimal.ROUND_DOWN).toString());// 积分
//						} else {
//							it.put("betPoint", "0");// 积分
//						}
//						it.put("buyNum", pa.getBuyNum());// 购买份额
//						it.put("preMoney",
//								ProductUtil.checkBigDecimal(sp.getPreMoney()));// 每份金额
						items.put(it);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 信用投注记录
	 */
	public void xyBetlist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String status = ParamUtils.getParameter(deParameter, "status");
		String u = ParamUtils.getParameter(deParameter, "u");
		String gameType = ParamUtils.getParameter(deParameter, "gameType");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(status) && status.equals("1")) {
					hqls.append(" and ho.winResult=? ");// 已开奖
					para.add(status);
				}
				hqls.append(" and u.userId=? ");
				para.add(user.getUserId());
				if (ParamUtils.chkString(gameType)) {
					hqls.append(" and ho.gameType=? ");
					para.add(gameType);
				}
				hqls.append(" order by  ho.betDetailId desc ");// 已开奖
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findGaBetDetailList(
						hqls.toString(), para, statIndex, pageSize);
				List<GaBetDetail> list = ps.getItems();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					for (int i = 0; i < list.size(); i++) {
						GaBetDetail detail = list.get(i);
						JSONObject it = new JSONObject();
						it.put("gameName", detail.getGameName());
						it.put("sessionNo", detail.getSessionNo());
						it.put("betTime", DateTimeUtil.DateToStringHHMM(detail
								.getBetTime()));
						it.put("room", detail.getRoom());
						it.put("playName", detail.getPlayName());
						it.put("betRate", nf.format(detail.getBetRate()));
						it.put("betName",
								detail.getBetName() + "-"
										+ detail.getOptionTitle());
						it.put("betPoint", detail.getBetMoney().toString());
						if ("0".equals(detail.getBetFlag())) { // 无效投注
							it.put("winResult", CqSscConstants.CQ_SSC_WIN_BACK); // 撤单
						} else {
							it.put("winResult", detail.getWinResult());//0=未开奖1=中奖2=未中奖3=打和4=撤单
						}

						if (detail.getWinCash() != null
								&& detail.getWinCash().compareTo(
										new BigDecimal(0)) != -1) {
							it.put("winCash",
									nf.format(detail.getWinCash().setScale(2,
											BigDecimal.ROUND_HALF_UP)));
						} else {
							it.put("winCash", "0.00");
						}
						it.put("betMoney", nf.format(detail.getBetMoney()));
//						if (detail.getPayoff() != null
//								&& detail.getPayoff().compareTo(
//										new BigDecimal(0)) != -1) {
//							it.put("payoff",
//									nf.format(detail.getPayoff().setScale(2,
//											BigDecimal.ROUND_HALF_UP)));
//						} else {
//							if (detail.getWinResult().equals("1")) {
//								it.put("payoff", nf.format(detail
//										.getWinCash()
//										.subtract(
//												new BigDecimal(detail
//														.getBetMoney()))
//										.setScale(2, BigDecimal.ROUND_HALF_UP)));
//							} else {
//								it.put("payoff", "omg2");
//							}
//						}
						items.put(it);
					}
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	public void list3(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (message.equals("")) {
			JSONArray items = CacheUtil.getCity3();
			data.put("items", items);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 微信版用到
	 */
	public void wechatList3(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (message.equals("")) {
			JSONArray items = CacheUtil.getWechatCity3();
			data.put("items", items);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 银行充值账户
	 */
	public void bankMsg(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (message.equals("")) {
			// String str = ManageFile.loadTextFileConfig(RamConstants
			// .getWebRootPath() + "/_help/" + "bankMsg.js");
			List<SysBank> list = cashService.findSysBankList();
			JSONArray array = new JSONArray();
			if (list != null && list.size() > 0) {
				for (SysBank sysBank : list) {
					JSONObject obj = new JSONObject();
					obj.put("bankId", sysBank.getBankId());
					obj.put("bankName", ParamUtils.chkStringNotNull(sysBank.getBankName()));
					obj.put("bankAccount", ParamUtils.chkStringNotNull(sysBank.getBankAccount()));
					obj.put("userName", ParamUtils.chkStringNotNull(sysBank.getUserName()));
					obj.put("country", ParamUtils.chkStringNotNull(sysBank.getCountry()));
					array.put(obj);
				}
			}
			data.put("items", array);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}


	/**
	 * 充值开关
	 */
	public void rechargeSwitch(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (message.equals("")) {
			StringBuffer hqls = new StringBuffer();
			List<Object> para = new ArrayList<Object>();
			hqls.append(" and (t.type=? or t.type=? or t.type=? or t.type=?)");
			para.add(Constants.PARAM_RECHARGE_ONLINE);
			para.add(Constants.PARAM_RECHARGE_OFFLINE);
			para.add(Constants.PARAM_RECHARGE_FAST);
			para.add(Constants.PARAM_RECHARGE_CODE);
			List<Param> list = paramService
					.findParamList(hqls.toString(), para);
			String onlineRecharge = "0";
			String offlineRecharge = "0";
			String fastRecharge = "0";
			String codeRecharge = "0";
			String fastRechargeTitle = "";
			String codeRechargeTitle = "";
			String fastRechargelink = "";
			String codeRechargeUrl = "";
			if (list != null && list.size() > 0) {
				for (Param param : list) {
					if (param.getType().equals(Constants.PARAM_RECHARGE_ONLINE)) {
						onlineRecharge = param.getStatus();
					} else if (param.getType().equals(
							Constants.PARAM_RECHARGE_OFFLINE)) {
						offlineRecharge = param.getStatus();
					} else if (param.getType().equals(
							Constants.PARAM_RECHARGE_FAST)) {
						fastRecharge = param.getStatus();
						fastRechargeTitle = param.getTitle();
						fastRechargelink = ParamUtils.chkStringNotNull(param
								.getValue());
					} else if (param.getType().equals(
							Constants.PARAM_RECHARGE_CODE)) {
						codeRecharge = param.getStatus();
						codeRechargeTitle = param.getTitle();
						codeRechargeUrl = StringUtil.httpTohttps(request,
								StringUtil.checkAPIHttpUrl(ParamUtils
										.chkStringNotNull(param.getValue())));
					}
				}
			}
			data.put("onlineRecharge", onlineRecharge);
			data.put("offlineRecharge", offlineRecharge);
			data.put("fastRecharge", fastRecharge);
			data.put("codeRecharge", codeRecharge);
			data.put("fastRechargeTitle", fastRechargeTitle);
			data.put("fastRechargelink", fastRechargelink);
			data.put("fastRechargelinkIsWeb", "1");
			
			data.put("codeRechargeTitle", codeRechargeTitle);
			data.put("codeRechargeUrl", codeRechargeUrl);
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 加载配置
	 */
	public void ac(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		// 2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(device) || !ParamUtils.chkString(ver)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
				if (Constants.getIOS_REVIEW_VISION().equals(ver)) {
//					if (device.equals("2")) {
						data.put("a", "0");// 隐藏功能，隐藏彩票
//					} else {
//						data.put("a", "1");
//					}
				} else {
					data.put("a", "1");// 显示功能
				}
			} else {
				data.put("a", "1");// 显示功能
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 加载配置
	 */
	public void loadConfig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		// 2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		String u = ParamUtils.getParameter(deParameter, "u", "");
		String token = ParamUtils.getParameter(deParameter, "token", "");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(device) || !ParamUtils.chkString(ver)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			if (ParamUtils.chkString(u)) {
				User user = null;
				Integer uid =0;
				Map<String, String> decryptMap = DesUtils.decryptMap(u);
				if(decryptMap!=null){
					uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
					user = userService.getUser(uid);
				}
				if(user!=null){
					Integer browseCount = user.getBrowseCount();
					if(!ParamUtils.chkInteger(browseCount)){
						browseCount=0;
					}
					user.setBrowseCount(browseCount + 1);
					user.setLastLoginIp(IPUtil.getIpAddr(request));
					user.setLastLoginDate(new Date());
					userService.saveObject(user, user);
				}
			}

			// 1.版本信息
			JSONObject verObj = new JSONObject();// 返回数据层
			JSONObject dataVer = new JSONObject();// 返回数据层
			String codeVer = APIConstants.CODE_NOT_FOUND;
			String messageVer = "";
			if (ParamUtils.chkString(device)) {
				Version version = baseDataService.getDefVer(device, "1");
				String defVer = version.getVer();
				String link = version.getLink();
				int compareVersion = StringUtil.compareVer(defVer, ver);
				if (compareVersion > 0) {
					codeVer = APIConstants.CODE_REQUEST_SUCCESS;
					message = APIConstants.VER_NEW;
					dataVer.put("link", link);
					String content = version.getContent();
					if (!ParamUtils.chkString(content)) {
						content = "新版本";
					}
					dataVer.put("content", content);
					dataVer.put("flag", version.getFlag());
				} else {
					codeVer = APIConstants.CODE_NOT_FOUND;
					messageVer = APIConstants.VER_NEWEST;
				}
				verObj.put("code", codeVer);
				verObj.put("msg", messageVer);
				verObj.put("data", dataVer);
				data.put("verObj", verObj);
			}

			// 2.用户定位信息
			JSONObject userObj = new JSONObject();// 返回数据层
			JSONObject dataUser = new JSONObject();// 返回数据层
			String codeUser = APIConstants.CODE_REQUEST_SUCCESS;
			String messageUser = "";
			String loginCode = APIConstants.CODE_REQUEST_SUCCESS;
			String userType = Constants.USER_TYPE_SUER;
			String loginMsg = "";
			if (ParamUtils.chkString(u)) {
				Map<String, String> decryptMap = DesUtils.decryptMap(u);
				Integer uid = Integer.valueOf(decryptMap
						.get(Constants.DES_KEY_UID));
				String password = decryptMap.get(Constants.DES_KEY_PASSWORD);
				User user = userService.getUser(uid);
				if (user != null) {
					String password2 = user.getPassword();
					// 登录信息过期
					if (!password2.equals(password)) {// 密码不同
						loginCode = APIConstants.CODE_REQUEST_ERROR;
						loginMsg = "登录信息已过期，请重新登录";
					} else {
						String oldToken = user.getToken();// 原设备码
						if (ParamUtils.chkString(token)
								&& ParamUtils.chkString(oldToken)) {
							if (!oldToken.equals(token)) {// 设备码不同
								// 如果是201，此种情况，app应该注销掉app的用户信息
								loginCode = APIConstants.CODE_REQUEST_ERROR;
								loginMsg = "您的帐号已于其他设备登录！";
							} else {
								if (ParamUtils.chkString(ver)) {
									user.setLastLoginDate(new Date());
									user.setLastLoginIp(IPUtil
											.getIpAddr(request));
								}
								userService.saveObject(user, null);
							}
						} else {
							if (ParamUtils.chkString(token)) {
								user.setToken(token);
							}
							if (ParamUtils.chkString(ver)) {
								user.setLastLoginDate(new Date());
								user.setLastLoginIp(IPUtil.getIpAddr(request));
							}
							userService.saveObject(user, null);
						}
					}
					userType = user.getUserType();
				} else {
					codeUser = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				codeUser = APIConstants.CODE_NOT_FOUND;
			}
			dataUser.put("loginCode", loginCode);
			dataUser.put("loginMsg", loginMsg);
			dataUser.put("userType", userType);
			userObj.put("code", codeUser);
			userObj.put("msg", messageUser);
			userObj.put("data", dataUser);
			data.put("userObj", userObj);

			if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
				if (Constants.getIOS_REVIEW_VISION().indexOf("," + ver) > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver + ",") > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver) > -1) {
					if (device.equals("2")) {
						data.put("show", "1");// 不显示
						data.put("dp", "1");// 不显示
						data.put("a", "1");// 不显示
					} else {
						data.put("show", "0");// 不显示
						data.put("dp", "0");// 不显示
						data.put("a", "0");// 不显示
					}
				} else {
					data.put("show", "0");
					data.put("dp", "0");//
					data.put("a", "0");// 显示
				}
			} else {
				data.put("show", "0");// 显示
				data.put("dp", "0");// 显示
				data.put("a", "0");// 显示
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	public void unit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		// 2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(device) || !ParamUtils.chkString(ver)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
				if (Constants.getIOS_REVIEW_VISION().indexOf("," + ver) > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver + ",") > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver) > -1) {
					data.put("unit", "积分");// 不显示
					data.put("tips", "请输入积分");// 不显示
				} else {
					data.put("unit", "元");
					data.put("tips", "请输入余额");
				}
			} else {
				data.put("unit", "元");// 显示
				data.put("tips", "请输入余额");// 显示
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	public void tips(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		String device = ParamUtils.getParameter(deParameter, "device");// 设备类型
																		// 1.IOS
		// 2.Android
		String ver = ParamUtils.getParameter(deParameter, "ver");// 当前版本
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(device) || !ParamUtils.chkString(ver)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
				if (Constants.getIOS_REVIEW_VISION().indexOf("," + ver) > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver + ",") > -1
						|| Constants.getIOS_REVIEW_VISION().indexOf(ver) > -1) {
					data.put("tips", "请输入积分");// 不显示
				} else {
					data.put("tips", "请输入余额");
				}
			} else {
				data.put("tips", "请输入余额");// 显示
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}


	/**
	 * 中奖记录
	 */
	public void winninglist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String status = ParamUtils.getParameter(deParameter, "status", "1");
		String u = ParamUtils.getParameter(deParameter, "u");
		String device = ParamUtils.getParameter(deParameter, "device");
		String ver = ParamUtils.getParameter(deParameter, "ver");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 10);// 每页记录数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		// String str =
		// ManageFile.loadTextFileGBK(RamConstants.getWebRootPath()+"/api/_api_js_k10/"+"openList.js");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		// JSONObject map = new JSONObject(str);
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(status) && status.equals("1")) {
					hqls.append(" and ho.winResult=? ");// 已开奖
					para.add(status);
				}
				hqls.append(" and u.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by  ho.betDetailId desc ");// 已开奖
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findGaBetDetailList(
						hqls.toString(), para, statIndex, pageSize);
				List<GaBetDetail> list = ps.getItems();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				JSONArray items = new JSONArray();
				if (list != null & list.size() > 0) {
					pageNum = list.size();
					// JSONObject obj = new JSONObject();
					String unit = "";
					if (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION())) {
						if (Constants.getIOS_REVIEW_VISION().indexOf("," + ver) > -1
								|| Constants.getIOS_REVIEW_VISION().indexOf(
										ver + ",") > -1
								|| Constants.getIOS_REVIEW_VISION()
										.indexOf(ver) > -1) {
							unit = "积分";
						} else {
							unit = "元";
						}
					} else {
						unit = "元";
					}
					for (int i = 0; i < list.size(); i++) {
						GaBetDetail detail = list.get(i);
						JSONObject it = new JSONObject();
						it.put("sessionTitle", "<" + detail.getGameName()
								+ "> " + detail.getSessionNo() + "期");
						it.put("playContens",
								DateTimeUtil.DateToStringHHMM(detail
										.getBetTime())
										+ " "
										+ detail.getRoom()
										+ " "
										+ detail.getPlayName()
										+ " @"
										+ nf.format(detail.getBetRate()));
						// it.put("room", detail.getRoom());
						// it.put("playName", detail.getPlayName());
						// it.put("betRate", nf.format(detail.getBetRate()));
						it.put("betName",
								detail.getBetName() + "-"
										+ detail.getOptionTitle());
						it.put("betPoint", detail.getBetMoney().toString()
								+ unit);
						if ("0".equals(detail.getBetFlag())) { // 无效投注
							it.put("winStatus", CqSscConstants.CQ_SSC_WIN_BACK); // 撤单
						} else {
							it.put("winStatus", detail.getWinResult());
						}
						;

						if (detail.getWinCash() != null
								&& detail.getWinCash().compareTo(
										new BigDecimal(0)) != -1) {
							it.put("winCash",
									nf.format(detail.getWinCash().setScale(2,
											BigDecimal.ROUND_HALF_UP))
											+ unit);
						} else {
							it.put("winCash", "");
						}
						if (detail.getPayoff() != null
								&& detail.getPayoff().compareTo(
										new BigDecimal(0)) != -1) {
							it.put("payoff",
									"+"
											+ nf.format(detail
													.getPayoff()
													.setScale(
															2,
															BigDecimal.ROUND_HALF_UP))
											+ unit);
						} else {
							if (detail.getWinResult().equals("1")) {
								it.put("payoff",
										"+"
												+ nf.format(detail
														.getWinCash()
														.subtract(
																new BigDecimal(
																		detail.getBetMoney()))
														.setScale(
																2,
																BigDecimal.ROUND_HALF_UP))
												+ unit);
							} else {
								it.put("payoff", "");
							}
						}
						items.put(it);
					}
					// data.put("items", items);
					code = APIConstants.CODE_REQUEST_SUCCESS;
					message = "操作成功";
				} else {
					// map.put("msg", "没有开奖结果");
					code = APIConstants.CODE_NOT_FOUND;
					message = "没有更多了";
				}
				data.put("items", items);
				data.put("total", ps.getTotalCount());
				data.put("pageIndex", pageIndex);
				data.put("pageSize", pageSize);// 每页条数
				data.put("pageNum", pageNum);// 当前页数量
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 个人中奖记录
	 */
	public void gfwinninglist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String status = ParamUtils.getParameter(deParameter, "status",
				Constants.WIN);
		String u = ParamUtils.getParameter(deParameter, "u");
		String device = ParamUtils.getParameter(deParameter, "device");
		String ver = ParamUtils.getParameter(deParameter, "ver");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		// String str =
		// ManageFile.loadTextFileGBK(RamConstants.getWebRootPath()+"/api/_api_js_k10/"+"openList.js");
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = "必要参数为空";
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			// if(true){
			// Integer uid =22757;

			User user = (User) userService.getObject(User.class, uid);
			if (user != null) {
				List<Object> para = new ArrayList<Object>();
				StringBuffer hqls = new StringBuffer();
				if (ParamUtils.chkString(status)
						&& status.equals(Constants.WIN)) {
					hqls.append(" and sp.winResult=? ");// 已中奖
					para.add(status);
				}
				hqls.append(" and u.userId=? ");
				para.add(user.getUserId());
				hqls.append(" order by  sp.jointId desc ");
				int statIndex = pageIndex * pageSize;// 计算开始的条数
				int pageNum = 0;
				PaginationSupport ps = gaService.findGaBetSponsorDetail(
						hqls.toString(), para, statIndex, pageSize);
				List<SpDetailDTO> list = ps.getItems();
				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				JSONArray items = new JSONArray();
				if (list != null && list.size() > 0) {
					for (SpDetailDTO dto : list) {
						pageNum = list.size();
						// String unit = "";
						// if
						// (ParamUtils.chkString(Constants.getIOS_REVIEW_VISION()))
						// {
						// if (Constants.getIOS_REVIEW_VISION().indexOf("," +
						// ver) > -1
						// || Constants.getIOS_REVIEW_VISION().indexOf(
						// ver + ",") > -1
						// || Constants.getIOS_REVIEW_VISION()
						// .indexOf(ver) > -1) {
						// unit = "积分";
						// } else {
						// unit = "元";
						// }
						// } else {
						// unit = "元";
						// }
						GaBetSponsor gaBetSponsor = dto.getGaBetSponsor();
						GaBetPart gaBetPart = dto.getGaBetPart();
						JSONObject it = new JSONObject();
						it.put("gameName", gaBetSponsor.getGameName());// 彩票名字
						it.put("sessionNo", gaBetSponsor.getSessionNo());// 期号
						it.put("jointId", gaBetSponsor.getJointId());// 合买Id
						it.put("betTime", DateTimeUtil
								.DateToStringAll(gaBetPart.getBuyTime()));// 投注时间
						it.put("winCash", gaBetPart.getWinCash());// 获奖金额
						items.put(it);
						code = APIConstants.CODE_REQUEST_SUCCESS;
						message = "操作成功";
					}
					data.put("items", items);
					data.put("total", ps.getTotalCount());
					data.put("pageIndex", pageIndex);
					data.put("pageSize", pageSize);// 每页条数
					data.put("pageNum", pageNum);// 当前页数量
				} else {
					message = APIConstants.TIPS_DATA_NOT;
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = "用户不存在";
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 
	 * 合买大厅
	 */
	public void joinHall(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		// 最近购买时间 1=今天全部 2=最近3天 3=最近7天 4=最近一个月 5=最近3个月
		String betTime = ParamUtils.getParameter(deParameter, "betTime", "5");
		String winResult = ParamUtils.getParameter(deParameter, "winResult");// 状态
		// 方案金额 1=10元以下 2=10到100 3=100到1千 4=1千到1万 5=1万到3万
		String money = ParamUtils.getParameter(deParameter, "money"); // 方案金额
		// 方案进度 1=10%以下，
		// 2=10%-20%，3=20%-30%，4=30%-40%，5=40%-50%，6=50%-60%，7=60%-70%，8=70%-80%，9=80%-90%，10=90%-100%，
		String schedule = ParamUtils.getParameter(deParameter, "schedule");// 进度
		String isGuarantee = ParamUtils
				.getParameter(deParameter, "isGuarantee");// 是否保底
		String gameType = ParamUtils.getParameter(deParameter, "gameType");// 彩票类型

		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		int pageNum = 0;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		// if (!ParamUtils.chkString(u)) {
		// message = "必要参数为空";
		// }
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append("and sp.betType = ? ");
		para.add(DcbConstants.SPONSOR);// 合买
		if (ParamUtils.chkString(gameType)) {
			hqls.append(" and sp.gameType =? ");
			para.add(gameType);
		}
		if (ParamUtils.chkString(betTime)) {
			Date betTime2 = DateTimeUtil.getCurrentDayStart(); // 今天0点
			if ("1".equals(betTime)) {
				betTime2 = DateTimeUtil.getDateTimeOfDays(betTime2, -2); // 最近3天
			} else if ("2".equals(betTime)) {
				// betTime2 = DateTimeUtil.getCurrentDayStart(); //今天0点
			} else if ("3".equals(betTime)) {
				betTime2 = DateTimeUtil.getDateTimeOfDays(betTime2, -6); // 最近7天
			} else if ("4".equals(betTime)) {
				betTime2 = DateTimeUtil.getDateTimeOfDays(betTime2, -29); // 最近30天
			} else if ("5".equals(betTime)) {
				betTime2 = DateTimeUtil.getDateTimeOfDays(betTime2, -89); // 最近90天
			}
			hqls.append(" and sp.betTime >=? ");
			para.add(betTime2);
		}
		if (ParamUtils.chkString(winResult)) {
			hqls.append(" and sp.winResult = ? ");
			para.add(winResult);
		}
		if (ParamUtils.chkString(money)) {
			int minMoney = 0;
			int maxMoney = 0;
			if ("1".equals(money)) {
				minMoney = 0;
				maxMoney = 10;
			} else if ("2".equals(money)) {
				minMoney = 10;
				maxMoney = 100;
			} else if ("3".equals(money)) {
				minMoney = 100;
				maxMoney = 1000;
			} else if ("4".equals(money)) {
				minMoney = 1000;
				maxMoney = 10000;
			} else if ("5".equals(money)) {
				minMoney = 10000;
				maxMoney = 30000;
			}
			hqls.append(" and sp.money >= ? ");
			para.add(minMoney);
			hqls.append(" and sp.money <= ? ");
			para.add(maxMoney);

		}
		if (ParamUtils.chkString(schedule)) {
			String minSchedule = null;
			String maxSchedule = null;
			if ("1".equals(schedule)) {
				minSchedule = "0";
				maxSchedule = "0.1";
			} else if ("2".equals(schedule)) {
				minSchedule = "0.1";
				maxSchedule = "0.2";
			} else if ("3".equals(schedule)) {
				minSchedule = "0.2";
				maxSchedule = "0.3";
			} else if ("4".equals(schedule)) {
				minSchedule = "0.3";
				maxSchedule = "0.4";
			} else if ("5".equals(schedule)) {
				minSchedule = "0.4";
				maxSchedule = "0.5";
			} else if ("6".equals(schedule)) {
				minSchedule = "0.5";
				maxSchedule = "0.6";
			} else if ("7".equals(schedule)) {
				minSchedule = "0.6";
				maxSchedule = "0.7";
			} else if ("8".equals(schedule)) {
				minSchedule = "0.7";
				maxSchedule = "0.8";
			} else if ("9".equals(schedule)) {
				minSchedule = "0.8";
				maxSchedule = "0.9";
			} else if ("10".equals(schedule)) {
				minSchedule = "0.9";
				maxSchedule = "1";
			}
			hqls.append(" and sp.schedule >= ? ");
			para.add(minSchedule);
			hqls.append(" and sp.schedule <= ? ");
			para.add(maxSchedule);
		}
		if (ParamUtils.chkString(isGuarantee)) {
			hqls.append(" and sp.isGuarantee = ? ");
			para.add(isGuarantee);
		}
		hqls.append(" and sp.winResult != ? ");
		para.add(Constants.INVALID_REFUND);
		hqls.append(" order by  sp.jointId desc ");
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		PaginationSupport ps = gaService.findGaBetSponsor(hqls.toString(),
				para, statIndex, pageSize);
		List<GfDcbDTO> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			for (int i = 0; i < list.size(); i++) {
				GaBetSponsor sp = list.get(i).getGaBetSponsor();
				User user = list.get(i).getUser();
				JSONObject it = new JSONObject();
				it.put("gameName", sp.getGameName());
				String name = user.getUserName();
				if (name.length() < 2) {
					name = name + "***";
				} else {
					name = name.substring(0, 2) + "***";
				}
				it.put("userName", name); // 用户名
//				it.put("aggBetMoney", user.getAggregateBetMoney()); // 累计投注金额
				it.put("aggBetMoney", 0); // 累计投注金额
				it.put("money", sp.getMoney());// 方案金额
				it.put("money2", "-"); // 战绩
				it.put("restNum", sp.getRestNum());// 剩余份数
				String betType = sp.getBetType();
				// String isGuarantee2="0";
				// if(betType=="1"){//合买
				String isGuarantee2 = sp.getIsGuarantee();
				// }
				int baodiNum = 0;
				if (isGuarantee2.equals(Constants.GUARANTEE)) {// 保底
					baodiNum = sp.getGuaranteedNum();
				}

				StringBuilder sb = new StringBuilder();
				String sch = new BigDecimal(sp.getNum() - sp.getRestNum())
						.multiply(new BigDecimal(100))
						.divide(new BigDecimal(sp.getNum()), 1,
								BigDecimal.ROUND_HALF_EVEN).toString();
				sb.append(sch).append("%+");
				String baodi = new BigDecimal(baodiNum)
						.multiply(new BigDecimal(100))
						.divide(new BigDecimal(sp.getNum()), 1,
								BigDecimal.ROUND_HALF_EVEN).toString();
				sb.append(baodi).append("%");
				it.put("schedule", sb);// 进度
				it.put("jindu", sch + "%");
				it.put("baodi", baodi + "%");
				it.put("winResult", sp.getWinResult());// 状态
				it.put("jointId", sp.getJointId());// Id
				it.put("gameType", sp.getGameType());// 彩票类型
				it.put("isGuarantee", sp.getIsGuarantee());// 是否保底
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			message = "没有更多了";
		}
		data.put("items", items);
		data.put("total", ps.getTotalCount());
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 合买详情
	 */
	public void jointBetDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String jointId = ParamUtils.getParameter(deParameter, "jointId");// 订单id
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(jointId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}

		if (message.equals("")) {
			Integer joId = Integer.parseInt(jointId);//订单id
			GaBetSponsor sp= (GaBetSponsor) baseDataService.getObject(GaBetSponsor.class, joId);

			if(sp == null){
			    code = APIConstants.CODE_REQUEST_ERROR;
			    message = "参数错误";
			    map.put("code", code);
			    map.put("msg", message);
			    JsonUtil.AjaxWriter(response, map);
			    return;
			}
			List<GaBetSponsorDetail> spDetaillist = gaService.findGaBetSponsorDetailListByJointId(joId);
			if(spDetaillist == null || spDetaillist.size()<=0){
			    code = APIConstants.CODE_REQUEST_ERROR;
			    message = "参数错误";
			    map.put("code", code);
			    map.put("msg", message);
			    JsonUtil.AjaxWriter(response, map);
			    return;
			}
			List<SpDetailDTO> paList = baseDataService
					.findSpPartDTOByOrderNum(jointId);
			if(paList == null || paList.size()<=0){
			    code = APIConstants.CODE_REQUEST_ERROR;
			    message = "参数错误";
			    map.put("code", code);
			    map.put("msg", message);
			    JsonUtil.AjaxWriter(response, map);
			    return;
			}

			JSONArray betNumIt = new JSONArray();
			JSONArray userIt = new JSONArray();

			for(GaBetSponsorDetail de:spDetaillist){
				JSONObject it = new JSONObject();
				it.put("title", ParamUtils.chkStringNotNull(de.getTitle()));// 玩法名字
				it.put("betBall", de.getOptionTitle());// 投注号码
				if (ParamUtils.chkInteger(de.getPlayType())) {
					it.put("playType", de.getPlayType());
				} else {
					it.put("playType", "");
				}
				betNumIt.put(it);
			}
			for (int i = 0; i < paList.size(); i++) {
				GaBetPart pa = paList.get(i).getGaBetPart();
				JSONObject it = new JSONObject();
				String name = paList.get(i).getUserName();
				if (name.length() < 2) {
					name = name + "***";
				} else {
					name = name.substring(0, 2) + "***";
				}
				it.put("userName", name);// 用户姓名
				it.put("betMoney", pa.getBetMoney());// 认购金额

				it.put("buyTime", DateTimeUtil.format(pa.getBuyTime()));// 认购时间
				if (pa.getWinCash() != null) {
					it.put("winCash", pa.getWinCash());// 中奖金额
				} else {
					it.put("winCash", "");// 中奖金额
				}
				userIt.put(it);
			}

			User u = (User) baseDataService.getObject(User.class,
					sp.getUserId());
			String name = u.getUserName();
			if (name.length() < 2) {
				name = name + "***";
			} else {
				name = name.substring(0, 2) + "***";
			}
			data.put("sponsor", u.getUserName()); // 发起人
			data.put("logo", ParamUtils.chkStringNotNull(StringUtil
					.checkAPIHttpUrl(u.getLogo())));// 头像
			data.put("motto", ParamUtils.chkStringNotNull(u.getMotto()));// 个性签名|中奖宣言
			data.put("winResult", sp.getWinResult());// 方案状态
			data.put("winResult2",
					Constants.getWinResultNameZh(sp.getWinResult()));// 方案状态
			if (sp.getWinCash() != null) {
				data.put("totalWinCash", sp.getWinCash().toString());// 总中奖金额
			} else {
				data.put("totalWinCash", "");// 总中奖金额
			}
			data.put("money", sp.getMoney());// 方案金额
			data.put("restNum", sp.getRestNum());// 剩余份额
			data.put("num", sp.getNum());// 总份数
			data.put("preMoney", sp.getPreMoney().toString());// 每份金额
			data.put("betNumIt", betNumIt);// 投注号码列表
			data.put("sessionNo", sp.getSessionNo());
			data.put("multiple", sp.getMultiple());// 倍数
			JSONArray jsonArray = new JSONArray();
			if (ParamUtils.chkString(sp.getOpenResult())) {
				data.put("openResult", sp.getOpenResult());// 开奖号码
				String array[] = sp.getOpenResult().split(",|，|\\+|\\|");

				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
				data.put("openTime", DateTimeUtil.format(sp.getOpenTime()));// 开奖时间
			} else {
				data.put("openResult", "");// 开奖号码
				data.put("openTime", "");// 开奖时间
			}
			data.put("resultItems", jsonArray);
			data.put("userIt", userIt); // 参与用户
			data.put("gameName", sp.getGameName());// 游戏名称
			data.put("gameType", sp.getGameType());// 游戏类型
			data.put("jid", sp.getJointId());// 合买id
			String sch = new BigDecimal(sp.getNum() - sp.getRestNum())
					.multiply(new BigDecimal(100))
					.divide(new BigDecimal(sp.getNum()), 1,
							BigDecimal.ROUND_HALF_EVEN).toString();
			StringBuilder sb = new StringBuilder();
			sb.append(sch).append("%+");

			String gua = "";
			if (Constants.GUARANTEE.equals(sp.getIsGuarantee())) {// 如果保底
				data.put("guaMoney", ParamUtils.chkStringNotNull(sp
						.getGuaranteedMoney().toString()));// 保底金额
				gua = new BigDecimal(sp.getGuaranteedNum())
						.multiply(new BigDecimal(100))
						.divide(new BigDecimal(sp.getNum()), 1,
								BigDecimal.ROUND_HALF_EVEN).toString();
			} else {
				data.put("guaMoney", "0");// 保底金额
			}
			sb.append(gua).append("%");

			data.put("jindu", sch + "%");
			data.put("baodi", gua + "%");
			data.put("betTime", DateTimeUtil.format(sp.getBetTime()));// 发起合买时间
			data.put("orderNum", sp.getOrderNum());// 订单编号
			data.put("schedule", sb.toString());// 方案进度+保底
			GaSessionInfo ga = gaService.findGaSessionInfo(sp.getGameType());
			data.put("img", StringUtil.checkAPIHttpUrl(ga.getImg()));
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);

	}

	/**
	 * 订单详情
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void gaBetDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String jointId = ParamUtils.getParameter(deParameter, "jointId");// 订单id
		String msg = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (ParamUtils.chkString(jointId)) {
			List<SpDetailDTO> deList = baseDataService
					.findSpDetailDTOByOrderNum(jointId);
			List<SpDetailDTO> paList = baseDataService
					.findSpPartDTOByOrderNum(jointId);
			JSONArray betNumIt = new JSONArray();

			for (int i = 0; i < deList.size(); i++) {
				GaBetSponsorDetail de = deList.get(i).getGaBetSponsorDetail();
				JSONObject it = new JSONObject();
				it.put("title", ParamUtils.chkStringNotNull(de.getTitle()));// 玩法名字
				it.put("betBall", de.getOptionTitle());// 投注号码
				if (ParamUtils.chkInteger(de.getPlayType())) {
					it.put("playType", de.getPlayType());
				} else {
					it.put("playType", "");
				}
				betNumIt.put(it);
			}

			GaBetSponsor sp = deList.get(0).getGaBetSponsor();
			for (int i = 0; i < paList.size(); i++) {
				GaBetPart pa = paList.get(i).getGaBetPart();
				int parUser = pa.getUserId();
				if (parUser == sp.getUserId()) {
					String name = paList.get(i).getUserName();
					if (name.length() < 2) {
						name = name + "***";
					} else {
						name = name.substring(0, 2) + "***";
					}
					data.put("name", name);// 发起人
					data.put("betMoney", pa.getBetMoney());// 认购金额
					data.put("buyNum", pa.getBuyNum());// 投注注数
					data.put("buyTime", DateTimeUtil.format(pa.getBuyTime()));// 认购时间
					if (pa.getWinCash() != null) {
						data.put("winCash", pa.getWinCash().toString());// 中奖金额
					} else {
						data.put("winCash", "");// 中奖金额
					}
				}
			}
			data.put("winResult", sp.getWinResult());// 方案状态
			data.put("winResult2",
					Constants.getWinResultNameZh(sp.getWinResult()));// 方案状态
			data.put("betNumIt", betNumIt);// 投注号码列表
			data.put("sessionNo", sp.getSessionNo());
			data.put("multiple", sp.getMultiple());// 倍数
			JSONArray jsonArray = new JSONArray();
			if (ParamUtils.chkString(sp.getOpenResult())) {
				data.put("openResult", sp.getOpenResult());// 开奖号码
				String array[] = sp.getOpenResult().split(",|，|\\+|\\|");

				for (int j = 0; j < array.length; j++) {
					jsonArray.put(array[j]);
				}
			} else {
				data.put("openResult", "");// 开奖号码
			}
			data.put("resultItems", jsonArray);
			data.put("gameName", sp.getGameName());// 游戏名称
			data.put("jid", sp.getJointId());// 合买id
			data.put("orderNum", sp.getOrderNum());// 订单编号
			data.put("gameType", sp.getGameType());
			GaSessionInfo ga = gaService.findGaSessionInfo(sp.getGameType());
			data.put("img", StringUtil.checkAPIHttpUrl(ga.getImg()));
			msg = "查询成功";
			code = APIConstants.CODE_REQUEST_SUCCESS;
		} else {
			msg = "参数错误";
			code = APIConstants.CODE_REQUEST_ERROR;
		}
		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);

	}

	/**
	 * 用户中奖排行榜
	 */
	public void winRank(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String rankType = ParamUtils.getParameter(deParameter, "rankType", "2");// 排行榜
																				// 2=总排行榜
																				// 0=日排行榜
																				// 1=周排行榜
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and u.userType=?");
		para.add(Constants.USER_TYPE_SUER);
		if (ParamUtils.chkString(rankType)) {
			if ("0".equals(rankType)) {
				hqls.append(" order by u.dayWinMoney desc ");
			} else if ("2".equals(rankType)) {
				hqls.append(" order by u.winMoney desc ");
			} else if ("1".equals(rankType)) {
				hqls.append(" order by u.weekWinMoney desc ");
			}

		}

		PaginationSupport ps = userService.findUser(hqls.toString(), para, 0,
				20);
		List<User> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				JSONObject it = new JSONObject();
				String name = user.getUserName();
				if (name.length() < 2) {
					name = name + "***";
				} else {
					name = name.substring(0, 2) + "*****"+name.substring(name.length()-2,name.length());
				}
				it.put("userName", name); // 用户名
				if ("0".equals(rankType)) {
					it.put("winMoney", user.getDayWinMoney());// 获奖金额
				} else if ("1".equals(rankType)) {
					it.put("winMoney", user.getWeekWinMoney());// 获奖金额
				} else if ("2".equals(rankType)) {
					it.put("winMoney", user.getWinMoney());// 获奖金额
				}
//				if ("0".equals(rankType)) {
//					it.put("winMoney", 0);// 获奖金额
//				} else if ("1".equals(rankType)) {
//					it.put("winMoney", 0);// 获奖金额
//				} else if ("2".equals(rankType)) {
//					it.put("winMoney", 0);// 获奖金额
//				}
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			message = "没有更多了";
		}
		data.put("items", items);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 新闻资讯类型列表
	 */
	public void newsTypelist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		List<NewsType> list = baseDataService.findNewsTypeList();
		JSONArray items = new JSONArray();

		JSONObject it1 = new JSONObject();
		it1.put("tid", 0);
		it1.put("title", "新闻资讯");
		items.put(it1);
		if (list != null & list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject it = new JSONObject();
				NewsType type = list.get(i);
				it.put("tid", type.getTid());
				it.put("title", type.getTitle());
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		}
		data.put("items", items);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 新闻资讯列表
	 */
	public void newslist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		int tid = ParamUtils.getIntParameter(deParameter, "tid", 0);// 类型id
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		int pageNum = 0;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();

		if (ParamUtils.chkInteger(tid)) {
			hqls.append(" and n.tid=? ");
			para.add(tid);
		}
		hqls.append("  order by n.submitTime desc");
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		PaginationSupport ps = baseDataService.findNewsList(hqls.toString(),
				para, statIndex, pageSize);
		List<NewsInformation> list = ps.getItems();
		JSONArray items = new JSONArray();
		if (list != null & list.size() > 0) {
			pageNum = list.size();
			for (int i = 0; i < list.size(); i++) {
				NewsInformation news = list.get(i);
				JSONObject it = new JSONObject();
				it.put("newsId", news.getNid());
				it.put("type",
						"[" + ParamUtils.getStringNotNull(news.getType()) + "]");
				it.put("title", news.getTitle());
				it.put("time", DateTimeUtil.DateToString(news.getSubmitTime()));
				it.put("tid", news.getTid());
//				it.put("img", ParamUtils.chkStringNotNull(StringUtil
//						.checkAPIHttpUrl(news.getImg())));
				// it.put("link", StringUtil.checkAPIHttpUrl(news.getLink()));
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			message = "没有更多了";
		}
		data.put("items", items);
		data.put("total", ps.getTotalCount());
		data.put("pageIndex", pageIndex);
		data.put("pageSize", pageSize);// 每页条数
		data.put("pageNum", pageNum);// 当前页数量
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 新闻资讯详情
	 * 
	 * @param mapping
	 * @param form
	 */
	public void newsView(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		Integer newsId = ParamUtils.getIntegerParameter(deParameter, "id");// 类型id

		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (!ParamUtils.chkInteger(newsId)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			NewsInformation news = (NewsInformation) baseDataService.getObject(
					NewsInformation.class, newsId);
			if (news != null) {
				JSONObject obj = new JSONObject();
				obj.put("newId", news.getNid());
				obj.put("title", news.getTitle());
				obj.put("company", news.getCompany());
				obj.put("author", news.getAuthor());
				obj.put("dateTime",
						DateTimeUtil.DateToStringAll(news.getSubmitTime()));
				obj.put("content", news.getContent());
//				obj.put("img", ParamUtils.chkStringNotNull(StringUtil
//						.checkAPIHttpUrl(news.getImg())));

				data.put("obj", obj);

				// BaseDataDTO
				// dto=baseDataService.getBeforeAndAfterNews(newsId);
				NewsInformation beforeNews = baseDataService
						.getNewsInfomationByPrevId(newsId);
//				if (beforeNews != null) {
//					data.put("afterNid", beforeNews.getNid() + "");
//					data.put("afterTitle", beforeNews.getTitle());
//				} else {
//					data.put("afterNid", "");
//					data.put("afterTitle", "");
//				}
//				NewsInformation afterNews = (NewsInformation) baseDataService
//						.getObject(NewsInformation.class, news.getPrevId());
//				if (afterNews != null) {
//					data.put("beforeNid", afterNews.getNid() + "");
//					data.put("beforeTitle", afterNews.getTitle());
//				} else {
//					data.put("beforeNid", "");
//					data.put("beforeTitle", "");
//				}

				NewsType type = (NewsType) baseDataService.getObject(
						NewsType.class, news.getTid());
				data.put("tid", type.getTid());
				data.put("typeTitle", type.getTitle());
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				code = APIConstants.CODE_REQUEST_ERROR;
				message = APIConstants.TIPS_SERVER_ERROR;
			}

		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 参与合买投注
	 */
	public void jointBet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String jointId = ParamUtils.getParameter(deParameter, "jointId");// 订单id
		String ucode = ParamUtils.getParameter(deParameter, "u");
		String buyNum = ParamUtils.getParameter(deParameter, "buyNum");// 投注数量
		String msg = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		if (!ParamUtils.chkString(ucode) || !ParamUtils.chkString(jointId)
				|| !ParamUtils.chkString(buyNum)) {
			msg = "必要参数为空";
		} else {
			User user = null;
			Map<String, String> decryptMap = DesUtils.decryptMap(ucode);
			Integer uid = Integer
					.valueOf(decryptMap.get(Constants.DES_KEY_UID));
			user = (User) userService.getObject(User.class, uid);
			if (user == null) {
				code = APIConstants.CODE_REQUEST_ERROR;
				msg = "用户不存在";
			} else {
				int betNum2 = Integer.parseInt(buyNum);// 投注数量
				if (betNum2 <= 0) {
					msg = "您没有投注！";
					code = APIConstants.CODE_REQUEST_ERROR;
					map.put("code", code);
					map.put("msg", msg);
					JsonUtil.AjaxWriter(response, map);
					return;
				} else {
					BigDecimal userMoney = user.getMoney(); // 用户余额
					BigDecimal betMoney = null; // 本次投注金额
					GaBetSponsor sp = gaService.findGaBetSponsorById(jointId);
					int restNum = sp.getRestNum();// 剩余份数
					int totalNmu = sp.getNum();
					String sessionNo = sp.getSessionNo();

					if (restNum == 0) {// 已满员
						msg = "此合买投注数量已满，不能投注了";
						code = APIConstants.CODE_REQUEST_ERROR;
						map.put("code", code);
						map.put("msg", msg);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					if (Constants.PUB_STATUS_CLOSE.equals(sp.getBetFlag())) {
						msg = "此订单已关闭，不能投注了";
						code = APIConstants.CODE_REQUEST_ERROR;
						map.put("code", code);
						map.put("msg", msg);
						JsonUtil.AjaxWriter(response, map);
						return;
					}
					if (restNum < betNum2) {
						msg = "投注数量不能超过剩余份额";
						code = APIConstants.CODE_REQUEST_ERROR;
						map.put("code", code);
						map.put("msg", msg);
						JsonUtil.AjaxWriter(response, map);
						return;
					} else {
						System.out.println(sp+"____1");
						System.out.println(sp.getPreMoney()+"----");
						betMoney = sp.getPreMoney().multiply(
								new BigDecimal(betNum2));
						// -1 小于 0等于 1大于
						if (betMoney.compareTo(userMoney) == 1) {
							msg = "您的余额不足！";
							code = APIConstants.CODE_REQUEST_ERROR;
							map.put("code", code);
							map.put("msg", msg);
							JsonUtil.AjaxWriter(response, map);
							return;
						}
					}
					restNum = restNum - betNum2;
					BigDecimal schedule = new BigDecimal(0);
					schedule = new BigDecimal(totalNmu - restNum).divide(
							new BigDecimal(totalNmu), 2,
							BigDecimal.ROUND_HALF_EVEN);

					StringBuilder sb = new StringBuilder();
					String sch = schedule.multiply(new BigDecimal(100))
							.toString();
					sb.append(sch).append("%+");
					Integer guaranteedNum = sp.getGuaranteedNum();
					if(!ParamUtils.chkInteger(guaranteedNum)){
						guaranteedNum=0;
					}
					
					String gua = new BigDecimal(guaranteedNum)
							.multiply(new BigDecimal(100))
							.divide(new BigDecimal(sp.getNum()), 1,
									BigDecimal.ROUND_HALF_EVEN).toString();
					sb.append(gua).append("%");
					data.put("schedule", sb);// 进度
					data.put("jindu", sch + "%");

					user = gaService.updateJointBet(user, sp, betMoney,
							betNum2, restNum, schedule);
					msg = "投注成功！";
					code = APIConstants.CODE_REQUEST_SUCCESS;
					data.put("money", user.getMoney().toString());
					data.put("restNum", restNum);
				}
			}
		}

		map.put("code", code);
		map.put("msg", msg);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 首页公告或帮助
	 */
	public void noticeOrHelp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		String type = ParamUtils.getParameter(deParameter, "type", "0");// 类型id
		int num = 5;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		hqls.append(" and c.type=? ");
		para.add(type);
		
		hqls.append("  order by n.submitTime desc");

		List<BaseDataDTO> list = baseDataService.findNewsList(hqls.toString(),
				para, num);
		JSONArray items = new JSONArray();

		if (list != null & list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				NewsInformation news = list.get(i).getNews();
				JSONObject it = new JSONObject();
				if (i == 0) {
					data.put("ctitle", list.get(i).getTitle());
				}
				it.put("newsId", news.getNid());
				it.put("title", news.getTitle());
				String content = news.getContent();
				content = content.replaceAll("</?[a-zA-Z]+[^><]*>", "");
				it.put("content", content);
				it.put("dateTime",
						"[系统公告] "
								+ DateTimeUtil.DateToStringAll(news
										.getSubmitTime()));
				items.put(it);
			}
			code = APIConstants.CODE_REQUEST_SUCCESS;
			message = "操作成功";
		} else {
			code = APIConstants.CODE_NOT_FOUND;
			message = "暂无数据";
		}
		data.put("items", items);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 首页下面彩票资讯
	 */
	public void newsInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		// String type= ParamUtils.getParameter(deParameter, "type", "0");//类型id
		int num = 5;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		JSONArray items = new JSONArray();
		for (int i = 2; i < 6; i++) {// 0= 网站公告 1=帮助中心 2=焦点新闻 3=单场竞技 4=数字彩 5=高频彩
										// 6=充值指南 7=提款帮助 8=常见问题 9=网站帮助
			JSONObject obj = new JSONObject();
			StringBuffer hqls = new StringBuffer();
			List<Object> para = new ArrayList<Object>();
			hqls.append(" and c.type=?  ");
			para.add(i + "");
			hqls.append("  order by n.submitTime desc");

			List<BaseDataDTO> list = baseDataService.findNewsList(
					hqls.toString(), para, num);
			JSONArray subItems = new JSONArray();
			if (list != null & list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					if (j == 0) {
						obj.put("ctitle", list.get(j).getTitle());
					}
					NewsInformation news = list.get(j).getNews();
					JSONObject it = new JSONObject();
					it.put("newsId", news.getNid());
					it.put("title", news.getTitle());
					subItems.put(it);
				}
				obj.put("subItems", subItems);
				items.put(obj);
			}
		}
		code = APIConstants.CODE_REQUEST_SUCCESS;
		message = "操作成功";
		data.put("items", items);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 首页底部栏
	 */
	public void newsInfoBottom(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		// String type= ParamUtils.getParameter(deParameter, "type", "0");//类型id
		int num = 4;// 当前页数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		JSONArray items = new JSONArray();
		JSONObject obj1 = new JSONObject();
		obj1.put("ctitle", "安全购彩");
		JSONArray subItems1 = new JSONArray();
		JSONObject it1 = new JSONObject();
		it1.put("newsId", "");
		it1.put("title", "交易安全");
		subItems1.put(it1);
		JSONObject it2 = new JSONObject();
		it2.put("newsId", "");
		it2.put("title", "购买便捷");
		subItems1.put(it2);
		JSONObject it3 = new JSONObject();
		it3.put("newsId", "");
		it3.put("title", "派奖快速");
		subItems1.put(it3);
		obj1.put("subItems", subItems1);
		items.put(obj1);

		for (int i = 6; i < 9; i++) {// 0= 网站公告 1=帮助中心 2=焦点新闻 3=单场竞技 4=数字彩 5=高频彩
										// 6=充值指南 7=提款帮助 8=常见问题 9=网站帮助
			JSONObject obj = new JSONObject();

			hqls.append(" and c.type=?  ");
			para.add(i + "");
			hqls.append("  order by n.submitTime desc");

			List<BaseDataDTO> list = baseDataService.findNewsList(
					hqls.toString(), para, num);
			JSONArray subItems = new JSONArray();
			if (list != null & list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					if (j == 0) {
						obj.put("ctitle", list.get(j).getTitle());
					}
					NewsInformation news = list.get(j).getNews();
					JSONObject it = new JSONObject();
					it.put("newsId", news.getNid());
					it.put("title", news.getSubTitle());
					subItems.put(it);
				}
				obj.put("subItems", subItems);
			}
			items.put(obj);
		}

		JSONObject obj2 = new JSONObject();
		obj2.put("ctitle", "联系我");
		JSONArray subItems2 = new JSONArray();
		JSONObject it4 = new JSONObject();
		it4.put("newsId", "");
		it4.put("title", "电话");
		subItems2.put(it4);
		JSONObject it5 = new JSONObject();
		it5.put("newsId", "");
		it5.put("title", "QQ：1991399995");
		subItems2.put(it5);
		JSONObject it6 = new JSONObject();
		it6.put("newsId", "");
		it6.put("title", "版权所有:");
		subItems2.put(it6);
		JSONObject it7 = new JSONObject();
		it7.put("newsId", "");
		it7.put("title", "鼎鑫国际");
		subItems2.put(it7);
		obj2.put("subItems", subItems2);
		items.put(obj2);

		code = APIConstants.CODE_REQUEST_SUCCESS;
		message = "操作成功";
		data.put("items", items);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 网站帮助
	 */
	public void websiteHelp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		// String u = ParamUtils.getParameter(deParameter, "u");
		// String type= ParamUtils.getParameter(deParameter, "type", "0");//类型id
		int num = 10;// 查询数量
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		// JSONArray items = new JSONArray();
		//
		// JSONObject obj = new JSONObject();
		//
		hqls.append(" and c.type=?  ");
		para.add(RamConstants.NewsCategoryWebsiteHelp);
		hqls.append("  order by n.submitTime desc");

		List<BaseDataDTO> list = baseDataService.findNewsList(hqls.toString(),
				para, num);
		JSONArray subItems = new JSONArray();
		if (list != null & list.size() > 0) {
			for (int j = 0; j < list.size(); j++) {
				NewsInformation news = list.get(j).getNews();
				JSONObject it = new JSONObject();
				it.put("newsId", news.getNid());
				it.put("title", news.getTitle());
				subItems.put(it);
			}
		}
		code = APIConstants.CODE_REQUEST_SUCCESS;
		message = "操作成功";
		data.put("items", subItems);
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 消息列表
	 */
	public void messageList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		int pageIndex = ParamUtils.getIntParameter(deParameter, "pageIndex", 0);// 页码
		int pageSize = ParamUtils.getIntParameter(deParameter, "pageSize", 20);// 每页记录数量
		int statIndex = pageIndex * pageSize;// 计算开始的条数
		int pageNum = 0;

		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}

		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if (decryptMap != null) {
				if (decryptMap.get(Constants.DES_KEY_UID) != null) {
					Integer uid = Integer.valueOf(decryptMap
							.get(Constants.DES_KEY_UID));
					List<Object> para = new ArrayList<Object>();
					StringBuffer hqls = new StringBuffer();
					hqls.append(" and (upper(ho.userId) = ? OR upper(ho.userId) = ? ) ");
					para.add(0);
					para.add(uid);
//					hqls.append(" and ho.status = ? ");
//					para.add(Constants.PUB_STATUS_OPEN);
					hqls.append(" order by ho.id desc");
					PaginationSupport ps = messageService.findList(
							hqls.toString(), para, statIndex, pageSize);
					List<Message> list = ps.getItems();
					JSONArray items = new JSONArray();
					if (list != null && list.size() > 0) {
						pageNum = list.size();
						for (Message msg : list) {
							JSONObject obj = new JSONObject();
							obj.put("id", msg.getId());
							obj.put("status", msg.getStatus());
							obj.put("title",
									ParamUtils.chkStringNotNull(msg.getTitle()));
							obj.put("content", ParamUtils.chkStringNotNull(msg
									.getContent()));
							obj.put("createTime", DateTimeUtil
									.DateToStringAll(msg.getCreateTime()));
							items.put(obj);
						}
						data.put("items", items);
						data.put("pageIndex", pageIndex);
						data.put("pageNum", pageNum);// 当前页数量
						data.put("pageSize", pageSize);// 每页条数
						data.put("total", ps.getTotalCount());// 总数量
						code = APIConstants.CODE_REQUEST_SUCCESS;
						message = APIConstants.TIPS_OPERATION_SUCC;
					} else {
						code = APIConstants.CODE_NOT_FOUND;
						message = APIConstants.TIPS_DATA_NULL;
					}
				} else {
					message = APIConstants.TIPS_SERVER_ERROR;
				}
			} else {
				message = APIConstants.TIPS_SERVER_ERROR;
			}
		}

		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 消息详情
	 */
	public void messageDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		Integer id = ParamUtils.getIntegerParameter(deParameter, "id"); //
		String u = ParamUtils.getParameter(deParameter, "u");

		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if(!ParamUtils.chkInteger(id) || !ParamUtils.chkString(u)){
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if (decryptMap != null) {
				if (decryptMap.get(Constants.DES_KEY_UID) != null) {
					Integer uid = Integer.valueOf(decryptMap
							.get(Constants.DES_KEY_UID));
					Message msg = (Message) messageService.getObject(Message.class, id);
					if(msg != null){
						Integer userId = msg.getUserId();
						if((0 == userId.intValue())||(uid.intValue() == userId.intValue())){
							data.put("id", msg.getId());
							data.put("title", ParamUtils.chkStringNotNull(msg.getTitle()));
							data.put("content", ParamUtils.chkStringNotNull(msg.getContent()));
							data.put("createTime", DateTimeUtil.DateToStringAll(msg.getCreateTime()));
							code = APIConstants.CODE_REQUEST_SUCCESS;
							message = APIConstants.TIPS_OPERATION_SUCC;
							msg.setStatus("2");
							baseDataService.saveObject(msg, null);
						}else{
							code = APIConstants.CODE_REQUEST_ERROR;
							message = "参数错误！";
						}
					}else{
						code = APIConstants.CODE_REQUEST_ERROR;
						message = "参数错误！";
					}
				}
			}
		}
		
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	/**
	 * 微信、支付宝 支付二维码
	 */
	public void payQRCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		String message = "";
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		
		List<Param> findParamList = paramService.findParamList();
		for (Param param : findParamList) {
			String type = param.getType();
			String title = param.getTitle();
			String value = param.getValue();
			
			if(Constants.PARAM_DEF_ZHIFUBAO.equals(type)){
				JSONObject obj=new JSONObject();
				obj.put("val", ParamUtils.chkStringNotNull(StringUtil.checkAPIHttpUrl(value)));
				data.put("alipayObj", obj);
			}else if(Constants.PARAM_DEF_WEIXIN.equals(type)){
				JSONObject obj=new JSONObject();
				obj.put("val", ParamUtils.chkStringNotNull(StringUtil.checkAPIHttpUrl(value)));
				data.put("wechatObj", obj);
			}else if(Constants.PARAM_DEF_HINT.equals(type)){
				JSONObject obj=new JSONObject();
				obj.put("val", ParamUtils.chkStringNotNull(value));
				data.put("tipsObj", obj);
			}
		}
		code = APIConstants.CODE_REQUEST_SUCCESS;
		message = APIConstants.TIPS_OPERATION_SUCC;
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);//suijishu
		JsonUtil.AjaxWriter(response, map);
	}

	public void agentShare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String u = ParamUtils.getParameter(deParameter, "u");// 用户key
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			User user =null;
			Integer uid =0;
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				user = (User) userService.getObject(User.class, uid);
			}
			if (user != null) {
				String QRCode = user.getQRCode();
				// 二维码
				// 虚拟目录
				String path = Constants.getFileUploadPath()
						+ UploadUtil.getFolderYYYYMM("/userQRCode");

				// 二维码存储目录
				String QRCodePath = Constants.getWebRootPath() + path;
				String QRName = ProductUtil
						.createOrderNum(Constants.MODULE_STORE) + ".png";

				if (!ParamUtils.chkString(QRCode)) {
					QRCode = path + "/" + QRName;
				}
				// 二维码内容为oid加密，如需要二维码的话需要解密
				String QRCodeUrl = Constants.getDomainName()
						+ "/game/register.html" + "?key=" + uid;
				File file = new File(Constants.getWebRootPath() + QRCode);

				if (!file.exists()) {// 如果没有，生成一个二维码
					new FSO().createFolder(QRCodePath);
					int width = 300;
					int height = 300;
					String format = "png";
					QRCodeUtil.genQRCodeImg(QRCodeUrl, width, height, format,
							QRCodePath + "/" + QRName);
					user.setQRCode(QRCode);
				}
				// data.put(
				// "link",
				// StringUtil.httpTohttps(
				// request,
				// StringUtil.checkAPIHttpUrl("/m/QRCode?key=" + key
				// + "&t=" + System.currentTimeMillis())));

				data.put("link", StringUtil.checkAPIHttpUrl(user.getQRCode()));
				data.put("uid", user.getUserId());
				data.put("title", "大地彩票");
				String words = "来自App的分享：这里北京赛车，幸运28，重庆时时彩，PC蛋蛋，广东快乐十分等多种游戏和各种玩法，欢迎下载";
				data.put("words", words);
				String img = "/images/aboutus.png";
				data.put("img",StringUtil.httpTohttps(request,StringUtil.checkAPIHttpUrl(img)));
				data.put("qrCodeUrl",StringUtil.checkAPIHttpUrl("/file_upload/ss.jpg"));
				code = APIConstants.CODE_REQUEST_SUCCESS;
			} else {
				message = APIConstants.TIPS_NOT_USER;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 公众号
	 */
	public void officialAccounts(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Map<String, String> deParameter = ParamUtils.deParameter(request);
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层

		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Param param = paramService
					.getParamByType(Constants.PARAM_OFFICIAL_ACCOUNTS);
			data.put("link", StringUtil.checkAPIHttpUrl(param.getValue()));
			data.put("title", "大地彩票");
			String words = "加入微信公众号，获取更多消息";
			data.put("words", words);
			String img = "/images/aboutus.png";
			data.put(
					"img",
					StringUtil.httpTohttps(request,
							StringUtil.checkAPIHttpUrl(img)));
			data.put("codeUrl", StringUtil.checkAPIHttpUrl(param.getValue()));
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	
	/**
	 * 官方采种玩法
	 */
	public void gameOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String gameType = ParamUtils.getParameter(deParameter, "gameType");// 采种类型
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		
		String message = "";
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			List<GaBetOption> optionList = gaService.findGaBetOptionByGameType(gameType);
			JSONArray items = new JSONArray();
			if (optionList != null) {
				for (GaBetOption option : optionList) {
					JSONObject obj = new JSONObject();
					obj.put("betRate", ProductUtil.BigFormat(option.getBetRate()));
					items.put(obj);
				}
				data.put("items", items);
				code = APIConstants.CODE_REQUEST_SUCCESS;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}
	
	/**
	 * 登出黑名单
	 */
	public void blacklist(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Map<String, String> deParameter = ParamUtils.deParameter(request);
		String u = ParamUtils.getParameter(deParameter, "u");
		String message = "";
		String code = APIConstants.CODE_REQUEST_SUCCESS;
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		if (!ParamUtils.chkString(u)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		if (message.equals("")) {
			
			Integer uid =0;
			String loginName="";
			String password="";
			String status="";
			String session="";
			
			Map<String, String> decryptMap = DesUtils.decryptMap(u);
			if(decryptMap!=null){
				uid=Integer.valueOf(decryptMap.get(Constants.DES_KEY_UID));
				loginName=decryptMap.get(Constants.DES_KEY_LOGINNAME);
				password=decryptMap.get(Constants.DES_KEY_PASSWORD);
				status=decryptMap.get(Constants.DES_KEY_STATUS);
				session=decryptMap.get(Constants.DES_KEY_SESSION);
			}
			if(ParamUtils.chkInteger(uid)){
				User user = UserCacheUtil.getUser(uid);
				if(user!=null){
					String userType = user.getUserType();
					
					if(ParamUtils.chkString(userType)){
						if(userType.equals(Constants.USER_TYPE_SUER)||userType.equals(Constants.USER_TYPE_AGENT)){
							BlackListCacheUtil.saveOnlineListU(u);
						}
					}
					
					
//					String loginNameDB = user.getLoginName();
					String passwordDB = user.getPassword();
					String statusDB = user.getStatus();
					String sessionDB = user.getSession();
					
					//GameHelpUtil.log("_temp",password+","+status+","+session);
					
					if(ParamUtils.chkString(passwordDB)&&ParamUtils.chkString(password)&&!password.equals(passwordDB)){
						code=APIConstants.CODE_USER_ERROR;
						message="密码失效，请重新登录";
					}
					if(ParamUtils.chkString(statusDB)&&ParamUtils.chkString(status)&&!status.equals(statusDB)){
						code=APIConstants.CODE_USER_ERROR;
						message="账户失效，请重新登录";
					}
					if(ParamUtils.chkString(sessionDB)&&ParamUtils.chkString(session)&&!session.equals(sessionDB)){
						code=APIConstants.CODE_USER_ERROR;
						message="您帐号已在另一设备登录";
					}
					
					if(code==APIConstants.CODE_REQUEST_SUCCESS){
						BigDecimal userMoney = TradeCacheUtil.getUserMoney(uid);
						if(userMoney==null){
							userMoney=new BigDecimal(0);
						}
						data.put("money", userMoney);
					}
					
				}
			}
		}
		map.put("data", data);
		map.put("code", code);
		map.put("msg", message);
		JsonUtil.AjaxWriter(response, map);
	}
}