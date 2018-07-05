package com.framework.web.filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class GZIPEncodableResponse
    extends HttpServletResponseWrapper {
  private GZIPServletStream wrappedOut;

  public GZIPEncodableResponse(HttpServletResponse response) throws
      IOException {
    super(response);
    wrappedOut = new GZIPServletStream(response.getOutputStream());
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return wrappedOut;
  }

  private PrintWriter wrappedWriter;

  public PrintWriter getWriter() throws IOException {
    if (wrappedWriter == null) {
      wrappedWriter = new PrintWriter(new OutputStreamWriter(
          getOutputStream(), getCharacterEncoding()));
    }
    return wrappedWriter;
  }

  public void flush() throws IOException {
    if (wrappedWriter != null) {
      wrappedWriter.flush();
    }
    wrappedOut.finish();
  }
}
