package com.framework.web.filter;

import java.io.*;
import java.util.zip.*;
import javax.servlet.*;

public class GZIPServletStream
    extends ServletOutputStream {
  private GZIPOutputStream outputStream;

  public GZIPServletStream(OutputStream source) throws IOException {
    outputStream = new GZIPOutputStream(source);
  }

  public void finish() throws IOException {
    outputStream.finish();
  }

  public void write(byte[] buf) throws IOException {
    outputStream.write(buf);
  }

  public void write(byte[] buf, int off, int len) throws IOException {
    outputStream.write(buf, off, len);
  }

  public void write(int c) throws IOException {
    outputStream.write(c);
  }

  public void flush() throws IOException {
    outputStream.flush();
  }

  public void close() throws IOException {
    outputStream.close();
  }
}
