<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.gzlm.util.*"%>
<%@page import="com.gzlm.GzlmConstants"%>

<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	String datePathName = sdf.format(new Date());

	String logoPath = "/file_upload/logo/";
	String datePath = logoPath + datePathName;

	File uploadPath = new File(GzlmConstants.getWebRootPath()
			+ datePath + "/");//�ϴ��ļ�Ŀ¼

	if (!uploadPath.exists()) {
		uploadPath.mkdirs();
	}
	// ��ʱ�ļ�Ŀ¼
	File tempPathFile = new File(GzlmConstants.getWebRootPath()
			+ datePath + "/");
	if (!tempPathFile.exists()) {
		tempPathFile.mkdirs();
	}
	try {
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold(4096); // ���û�������С��������4kb
		factory.setRepository(tempPathFile);//���û�����Ŀ¼

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(4194304); // ��������ļ��ߴ磬������4MB

		List<FileItem> items = upload.parseRequest(request);//�õ����е��ļ�
		Iterator<FileItem> i = items.iterator();
		String logoPathMid = "";//��ͷ��
		String picMini = "";
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			String fileName = fi.getName();
			// 			String fileName = fi.getContentType();
			if (fileName != null) {
				File fullFile = new File(fi.getName());
				long currentTime = System.currentTimeMillis();
				String newFileName = Long.toString(currentTime)
						+ ".jpg";
				
				File savedFile = new File(uploadPath, newFileName);
				fi.write(savedFile);

				logoPathMid = datePath + "/" + newFileName;
				int lastIndexOf = newFileName.lastIndexOf(".");
				String substring = newFileName
						.substring(0, lastIndexOf);
				picMini = datePath + "/" + substring + "_b.jpg";

				ImageUtils.scale2(GzlmConstants.getWebRootPath()
						+ logoPathMid, GzlmConstants.getWebRootPath()
						+ picMini, 50, 50, true);
				File saveMini = new File(GzlmConstants.getWebRootPath()
						+ picMini);
			}
		}
		
		response.sendRedirect("/user/userAction.do?method=uploadLogo2&logoSrc="+logoPathMid+"&logoMiniSrc="+picMini);
		
// 		out.print("true");
	} catch (Exception e) {
// 		out.print("false");
		e.printStackTrace();
	}
%>
