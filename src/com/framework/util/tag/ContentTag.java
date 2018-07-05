package com.framework.util.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 显示固定长度内容标签
 * @author liuhuayu
 *
 */
@SuppressWarnings("serial")
public class ContentTag extends BodyTagSupport {

	private Log log = LogFactory.getLog(ContentTag.class);
	
	/**
	 * style样式
	 */
	private String style;
	
	/**
	 * class样式名称
	 */
	private String classname;
	
	/**
	 * 标签ID
	 */
	private String id;
	
	/**
	 * 标签name
	 */
	private String name;
	
	/**
	 * 允许显示内容的最大长度
	 */
	private Integer maxLength;

	/**
	 * 默认允许显示内容的最大长度是50
	 */
	private final static Integer MAX_LENGTH = 50;
	
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		
		try {
			BodyContent body = super.getBodyContent();
			String content = null;
			String show = null;
			
			if (body != null) {
				content = body.getString().trim();
			}
			
			JspWriter out = pageContext.getOut();
			StringBuffer buffer = new StringBuffer();
			
			if (content != null && content.length() > 0) {
				buffer.append("<span title=\"")
				  	  .append(content)
				  	  .append("\" ")

				  	  .append(" style=\"")
				  	  .append(style)
				  	  .append("\" ")
				  	  
				  	  .append(" class=\"")
				  	  .append(classname)
				  	  .append("\" ")
				  	  
				  	  .append(" id=\"")
				  	  .append(id)
				  	  .append("\" ")
				  	  
				  	  .append(" name=\"")
				  	  .append(name)
				  	  .append("\" >");
				
				if (maxLength == null || maxLength <= 0) {
					this.maxLength = MAX_LENGTH;
				}
				if (maxLength != null && maxLength > 0) {
					if (maxLength > content.length()) {
						maxLength = content.length();
					}
				}
				
				show = content.substring(0, maxLength);
				if (!content.equals(show)) {
					show += "...";
				}
				buffer.append(show).append("</span>");
			} else {
				buffer.append("&nbsp;");
			}
			
			out.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e);
		}	  
		
		return EVAL_PAGE;
	}
	
}
