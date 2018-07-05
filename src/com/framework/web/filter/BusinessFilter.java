package com.framework.web.filter;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author zhangkeyi
 */

public class BusinessFilter
    implements Filter {
  private FilterConfig config = null;
  private HashMap expiresMap = null;
  private String encoding = null;

  public void init(FilterConfig filterConfig) {
    this.config = filterConfig;
    this.encoding = config.getInitParameter("encoding");
    if (encoding == null || encoding.length() == 0) {
      encoding = "UTF-8";
    }
    expiresMap = new HashMap();
    Enumeration names = config.getInitParameterNames();
    while (names.hasMoreElements()) {
      String paramName = (String) names.nextElement();
      if (!"encoding".equals(paramName)) {
        String paramValue = config.getInitParameter(paramName);
        try {
          Integer expires = Integer.valueOf(config.getInitParameter(paramName));
          expiresMap.put(paramName, expires);
        }
        catch (Exception ex) {
          //LogUtil.logError( "Filter."+paramValue+"="+paramValue);
        }
      }
    }
  }

  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    // 汉字问题
    /**HttpServletRequest httpRequest = (HttpServletRequest) request;
    httpRequest.setCharacterEncoding(encoding);
    response.setContentType("text/html;charset=" + encoding);*/
//  chain.doFilter(request, response);

    // 压缩传输
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String uri = httpRequest.getRequestURI();

    String transferEncoding = getGZIPEncoding( (HttpServletRequest) request);
    if (transferEncoding == null) {
      setResponseHeader(httpResponse, uri, transferEncoding);
      chain.doFilter(request, response);
    }
    else {
      if (uri.endsWith(".do") || uri.endsWith(".jsp")) { // 不处理的 URL
        chain.doFilter(request, response);
      }
      else {
        setResponseHeader(httpResponse, uri, transferEncoding);
        httpResponse.setHeader("Content-Encoding", transferEncoding);
        GZIPEncodableResponse wrappedResponse = new GZIPEncodableResponse( (
            HttpServletResponse) response);
        chain.doFilter(request, wrappedResponse);
        wrappedResponse.flush();
      }
    }
  }

  public void destroy() {
  }

  private void setResponseHeader(HttpServletResponse response, String uri,
                                 String transferEncoding) {
    //LogUtil.logDebug( uri + ".Accept-Encoding: "+ transferEncoding);
    String ext = null;
    int dot = uri.lastIndexOf(".");
    if (dot != -1) {
      ext = uri.substring(dot + 1);
    }
    if (ext != null && ext.length() > 0) {
      Integer expires = (Integer) expiresMap.get(ext);
      if (expires != null) {
        //LogUtil.logDebug( uri + ".Expires: "+ expires.intValue());
        if (expires.intValue() > 0) {
          response.setHeader("Cache-Control", "max-age=" + expires.intValue()); //HTTP 1.1
        }
        else {
          response.setHeader("Cache-Control", "no-cache");
          response.setHeader("Pragma", "no-cache"); //HTTP 1.0
          response.setDateHeader("Expires", expires.intValue());
        }
      }
    }
  }

  private static String getGZIPEncoding(HttpServletRequest request) {
    String acceptEncoding = request.getHeader("Accept-Encoding");
    if (acceptEncoding == null)
      return null;
    acceptEncoding = acceptEncoding.toLowerCase();
    if (acceptEncoding.indexOf("x-gzip") >= 0) {
      return "x-gzip";
    }
    if (acceptEncoding.indexOf("gzip") >= 0) {
      return "gzip";
    }
    return null;
  }


}
