package help.pay.lingdian.Utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Redirect{
	  public static final String URL = "url";

	  public static void sendRedirect(String url, String target, String charset, Map<String, String> paramMap, HttpServletResponse response)
	    throws IOException
	  {
	    if (url == null) throw new IOException("can't find redirect url.");
	    String html = toHTML(url, target, paramMap, charset);
	    //LogFactory.getLog().debug("Redirect:\r\n" + html);
	    response.setContentType("text/html; charset=" + charset);
	    response.getWriter().write(html);
	  }

	  public static void forward(String path, Map<String, Object> attrMap, HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException
	  {
	    if (path == null) throw new IOException("can't find forward path.");
	    StringBuffer buf = new StringBuffer("Forward:URL=[");
	    buf.append(path).append("];");
	    for (String key : attrMap.keySet()) {
	      Object value = attrMap.get(key);
	      request.setAttribute(key, value);
	      buf.append(key).append("=[").append(value).append("];");
	    }
	   // LogFactory.getLog().debug(Redirect.class, buf.toString());
	    RequestDispatcher rd = request.getRequestDispatcher(path);
	    if (rd != null)
	      rd.forward(request, response);
	    else
	      throw new ServletException("Can not find [" + path + "];RequestDispatcher is null!");
	  }

	  public static void sendRedirect(String url, String target, String charset, KeyValuePair<String, String>[] nameValuePairs, HttpServletResponse response)
	    throws IOException
	  {
	    if (url == null) throw new IOException("can't find redirect url.");
	    String html = toHTML(url, target, nameValuePairs, charset);
	    //LogFactory.getLog().debug("Redirect:\r\n" + html);
	    response.setContentType("text/html; charset=" + charset);
	    response.getWriter().write(html);
	  }

	  private static String toHiddenTag(String name, String value) {
	    return "<input type=\"hidden\" name=\"" + 
	      name + "\" value=\"" + value + "\" />";
	  }

	  public static String toHTML(String url, String target, Map<String, String> paramMap, String charset)
	  {
	    StringBuffer html = new StringBuffer();
	    html.append("<html>");
	    html.append("<head>");
	    html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(charset).append("\"/>");
	    html.append("<title> </title>");
	    html.append("</head>");
	    html.append("<body>");
	    html.append("<form name=\"AutoForm\"");
	    if (target != null) {
	      html.append(" target=\"").append(target).append("\"");
	    }
	    html.append(" action=\"").append(url).append("\" method=\"post\">");
	    for (String key : paramMap.keySet()) {
	      html.append(toHiddenTag(key, (String)paramMap.get(key)));
	    }
	    html.append("</form>");
	    html.append("<script type=\"text/javascript\">");
	    html.append("document.AutoForm.submit();");
	    html.append("</script>");
	    html.append("</body></html>");
	    return html.toString();
	  }

	  public static String toHTML(String url, String target, KeyValuePair<String, String>[] nameValuePairs, String charset)
	  {
	    StringBuffer html = new StringBuffer();
	    html.append("<html>");
	    html.append("<head>");
	    html.append("<meta http-equiv=\"Content-Type\" content=\"").append(charset).append("\"/>");
	    html.append("<title> </title>");
	    html.append("</head>");
	    html.append("<body>");
	    html.append("<form name=\"AutoForm\"");
	    if (target != null) {
	      html.append(" target=\"").append(target).append("\"");
	    }
	    html.append(" action=\"").append(url).append("\" method=\"post\">");
	    for (KeyValuePair nv : nameValuePairs) {
	      String name = (String)nv.getKey();
	      for (Object value : nv.getValueList()) {
	        html.append(toHiddenTag(name, (String)value));
	      }
	    }
	    html.append("</form>");
	    html.append("<script type=\"text/javascript\">");
	    html.append("document.AutoForm.submit();");
	    html.append("</script>");
	    html.append("</body></html>");
	    return html.toString();
	  }	
}