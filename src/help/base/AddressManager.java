package help.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONObject;

import com.apps.model.Address;
import com.apps.service.IAddressService;
import com.apps.util.JsonUtil;
import com.framework.util.ParamUtils;
import com.framework.util.StringUtil;
import com.framework.web.action.BaseDispatchAction;

/**
 * 地址接口管理
 * 
 * @author Mr.zang
 * 
 */
public class AddressManager extends BaseDispatchAction {
	private final IAddressService addressService = (IAddressService) getService("addressService");

	/**
	 * 地址列表
	 */
	public void list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int uid = ParamUtils.getIntParameter(request, "uid", 0);// 用户id
		String isDef = ParamUtils.getParameter(request, "isDef");

		String message = "";
		if (!ParamUtils.chkInteger(uid)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			List<Address> list =new ArrayList<Address>();
			if(ParamUtils.chkString(isDef)){
				Address address=addressService.getDefAddress(uid);
				if(address!=null){
					list.add(address);
				}
			}else{
				list = addressService.findListByUserId(uid);
			}
			
			JSONArray items = new JSONArray();// 数据集合
			if (list != null && list.size() > 0) {
				JSONObject obj = null;
				for (Address address : list) {
					obj = new JSONObject();
					obj.put("aid", address.getId());//
					obj.put("uid", address.getUserId());//
					obj.put("userName", address.getUserName());//
					obj.put("address", address.getAddress());//
					obj.put("cellPhone", address.getCellPhone());//
					obj.put("isDef", address.getIsDef());// 是否是默认
					items.put(obj);
				}
				code = APIConstants.CODE_REQUEST_SUCCESS;
				data.put("items", items);
			} else {
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 保存地址
	 */
	public void save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int uid = ParamUtils.getIntParameter(request, "uid", 0);// 用户id
		int aid = ParamUtils.getIntParameter(request, "aid", 0);// 地址id
		String userName = ParamUtils.getParameter(request, "userName");//
		String cellPhone = ParamUtils.getParameter(request, "cellPhone");//
		String addr = ParamUtils.getParameter(request, "address");//
//		String gender = ParamUtils.getParameter(request, "gender");//
		String isDef = ParamUtils.getParameter(request, "isDef");//

		String message = "";
		if (!ParamUtils.chkInteger(uid) || !ParamUtils.chkString(userName)
				|| !ParamUtils.chkString(cellPhone)
				|| !ParamUtils.chkString(addr) ) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}else if (!StringUtil.isMobileNO(cellPhone)) {
			message = APIConstants.MOBILE_NOT;
		}

		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Address address = null;
			if (ParamUtils.chkInteger(aid)) {
				// 修改
				address = addressService.getAddress(aid);
				if (address != null) {
					address.setAddress(addr);
					address.setCellPhone(cellPhone);
//					address.setGender(gender);
					address.setIsDef(isDef);
					address.setUserId(uid);
					address.setUserName(userName);
				}
			} else {
				// 新增
				address = new Address();
				address.setAddress(addr);
				address.setCellPhone(cellPhone);
				address.setCreateTime(new Date());
//				address.setGender(gender);
				address.setIsDef(isDef);
				address.setUserId(uid);
				address.setUserName(userName);
			}
			address=addressService.saveAddress(address);
			
			JSONObject obj = new JSONObject();
			obj.put("aid", address.getId());//
			obj.put("uid", address.getUserId());//
			obj.put("userName", address.getUserName());
			obj.put("address", address.getAddress());//
			obj.put("cellPhone", address.getCellPhone());//
			obj.put("isDef", address.getIsDef());// 是否是默认
			obj.put("gender", address.getGender());// 1男 2女
			data.put("obj", obj);
			message = APIConstants.TIPS_SAVE;
			code = APIConstants.CODE_REQUEST_SUCCESS;
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 编辑地址
	 */
	public void edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int uid = ParamUtils.getIntParameter(request, "uid", 0);// 用户id
		int aid = ParamUtils.getIntParameter(request, "aid", 0);// 地址id

		String message = "";
		if (!ParamUtils.chkInteger(uid) || !ParamUtils.chkInteger(aid)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Address address = addressService.getAddress(aid);
			if (address != null) {
				Integer userId = address.getUserId();
				if (userId.intValue() == uid) {
					JSONObject obj = new JSONObject();
					obj.put("aid", address.getId());//
					obj.put("uid", address.getUserId());//
					obj.put("userName", address.getUserName());//
					obj.put("address", address.getAddress());//
					obj.put("cellPhone", address.getCellPhone());//
					obj.put("isDef", address.getIsDef());// 是否是默认
					obj.put("gender", address.getGender());// 1男 2女
					data.put("obj", obj);
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					message = APIConstants.TIPS_NOT_DATA;
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = APIConstants.TIPS_DATA_NOT;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

	/**
	 * 删除地址
	 */
	public void del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int uid = ParamUtils.getIntParameter(request, "uid", 0);// 用户id
		int aid = ParamUtils.getIntParameter(request, "aid", 0);// 地址id

		String message = "";
		if (!ParamUtils.chkInteger(uid) || !ParamUtils.chkInteger(aid)) {
			message = APIConstants.PARAMS_EMPTY_MSG;
		}
		JSONObject map = new JSONObject();// 最外层
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		if (message.equals("")) {
			Address address = addressService.getAddress(aid);
			if (address != null) {
				Integer userId = address.getUserId();
				if (userId.intValue() == uid) {
					// 删除
					addressService.deleteObject(Address.class, aid, null);
					message = APIConstants.TIPS_DEL_SUCCESS;
					code = APIConstants.CODE_REQUEST_SUCCESS;
				} else {
					message = APIConstants.TIPS_NOT_DATA;
					code = APIConstants.CODE_NOT_FOUND;
				}
			} else {
				message = APIConstants.TIPS_DATA_NOT;
				code = APIConstants.CODE_NOT_FOUND;
			}
		}
		map.put("code", code);
		map.put("msg", message);
		map.put("data", data);
		JsonUtil.AjaxWriter(response, map);
	}

}