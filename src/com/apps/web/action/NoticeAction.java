package com.apps.web.action;

import help.base.APIConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.json.JSONObject;

import com.apps.Constants;
import com.apps.model.Notice;
import com.apps.service.INoticeService;
import com.apps.util.JsonUtil;
import com.apps.web.form.NoticeForm;
import com.framework.dao.hibernate.PaginationSupport;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.framework.web.action.BaseDispatchAction;
import com.ram.RamConstants;
import com.ram.exception.permission.NoFunctionPermissionException;

/**
 * 新闻
 * 
 * @author Mr.zang
 * 
 */
public class NoticeAction extends BaseDispatchAction {

	private final INoticeService noticeService = (INoticeService) getService("noticeService");

	/**
	 * 新闻列表
	 */
	public ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		int pageSize = ParamUtils.getIntParameter(request, "maxPageItems",20);
		NoticeForm noticeForm = (NoticeForm) form;
		List<Object> para = new ArrayList<Object>();
		StringBuffer hqls = new StringBuffer();
		String title = noticeForm.getTitle();
		hqls.append(" order by n.id");
		PaginationSupport ps = noticeService.findNoticeList(hqls.toString(),
				para, startIndex, pageSize);
		List<Notice> list = ps.getItems();
		request.setAttribute("list", list);
		request.setAttribute("count", (ps.getTotalCount() + "").toString());
		return mapping.findForward("init");
	}

	/**
	 * 创建或编辑
	 */
	public ActionForward preAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		NoticeForm noticeForm = (NoticeForm) form;
		if (ParamUtils.chkInteger(id)) {
			Notice notice = (Notice) noticeService.getObject(Notice.class, id);
			noticeForm.setNotice(notice);
		}
		return mapping.findForward("preAdd");
	}

	/**
	 * 保存
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {

		NoticeForm noticeForm = (NoticeForm) form;
		Notice notice = noticeForm.getNotice();
		Integer id = notice.getId();
		if (ParamUtils.chkInteger(id)) {
			Notice old = (Notice) noticeService.getObject(Notice.class, id);
			old.setTitle(notice.getTitle());
			old.setContent(notice.getContent());
			old.setType(notice.getType());
			noticeService.updateNotice(old);
		} else {
			notice.setCreateTime(new Date());
			notice.setStatus(Constants.PUB_STATUS_OPEN);
			noticeService.saveNotice(notice);
		}
		appHtml(notice.getId());
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("save");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	
	/**
	 * 更改状态
	 */
	public void changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		String message = "";
		JSONObject data = new JSONObject();// 返回数据层
		String code = APIConstants.CODE_REQUEST_ERROR;
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		Notice notice = (Notice) noticeService.getObject(
				Notice.class, id);
		String status = notice.getStatus();
		try {
			if (status.equals(Constants.PUB_STATUS_OPEN)) {
				notice.setStatus(Constants.PUB_STATUS_CLOSE);
			} else {
				notice.setStatus(Constants.PUB_STATUS_OPEN);
			}
			noticeService.saveObject(notice, null);
			code=APIConstants.CODE_REQUEST_SUCCESS;
			message="切换状态成功！";
		} catch (Exception e) {
			message="切换状态失败！";
			e.printStackTrace();
		}
		JsonUtil.AjaxWriter(response,code,message,data);
	}
	
	/**
	 * 删除
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception, NoFunctionPermissionException {
		Integer id = ParamUtils.getIntegerParameter(request, "id");
		if (ParamUtils.chkInteger(id)) {
			noticeService.deleteObject(Notice.class, id, null);
		}
		int startIndex = ParamUtils.getIntParameter(request, "pager.offset", 0);
		ActionForward forward = mapping.findForward("del");
		StringBuffer path = new StringBuffer();
		path.append(forward.getPath());
		path.append("&pager.offset=" + startIndex);
		return new ActionForward(forward.getName(), path.toString(), true);
	}

	public void appHtml(Integer id) {
		// 生成静态页面html(商品套餐详情)
		Map<String, Object> map = new HashMap();
		Notice notice = (Notice) noticeService.getObject(Notice.class, id);
		map.put("notice", notice);
		map.put("date", DateTimeUtil.DateToString(notice.getCreateTime()));
		String destDir = Constants.getWebRootPath() + "/app/notice/";// 目标文件路径
		String htmlName = "notice-" + notice.getId() + ".html";
		String templateDir = Constants.getWebRootPath() + "/template";

		boolean newpath = staticHtml(destDir, htmlName, templateDir,
				"appNoticeHtml.vm", map);
		String lnk = "/app/notice/" + htmlName;// 目标文件路径
		notice.setLink(lnk);
		noticeService.updateNotice(notice);
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

}
