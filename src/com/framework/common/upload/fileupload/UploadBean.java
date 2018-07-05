package com.framework.common.upload.fileupload;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author yangjy
 * @version 1.0
 */
public class UploadBean {
	public UploadBean() {
	}

	private String saveAsName;
	
	private String uploadtime;

	private String fileExt;

	private String url;

	private String filename;

	private String absAddress;

	private String address;

	private long size;

	private String message;

	private String attachName;

	private String title;
	
	private String usetId;

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getAbsAddress() {
		return absAddress;
	}

	public void setAbsAddress(String absAddress) {
		this.absAddress = absAddress;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSaveAsName() {
		return saveAsName;
	}

	public void setSaveAsName(String saveAsName) {
		this.saveAsName = saveAsName;
	}

	public String getUsetId() {
		return usetId;
	}

	public void setUsetId(String usetId) {
		this.usetId = usetId;
	}

}
