/*
 * Created on 2005-7-26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.RequestProcessor;

import com.ram.model.User;

/**
 * @author zhangkeyi
 * 这里是全局权限控制模块
 */
public class PMRequestProcessor
    extends RequestProcessor {
  protected final Log log = LogFactory.getLog(getClass());

  protected boolean processPreprocess(HttpServletRequest request,
                                      HttpServletResponse response) {
    //如果用户从登陆界面登入则不进行判断
    if (request.getServletPath().equals("/loginInput.do")) {
      return true;
    }
    else {
      return checkSession(request, response);
    }
  }

  private boolean checkSession(HttpServletRequest request,
                               HttpServletResponse response) {
    //如果用户session存在并且pmUser不为空则进行权限判断
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("loginUser") != null) {
      User user = (User) session.getAttribute("loginUser");
      return checkPMService(user);
    }
    else {
      goTOLoginInput(request, response);
    }
    return false;
  }

  private void goTOLoginInput(HttpServletRequest request,
                              HttpServletResponse response) {
    try {
      //If no redirect user to login Page
      request.getRequestDispatcher("/loginInput.do").forward(request, response);
    }
    catch (ServletException e) {
      if (log.isWarnEnabled()) {
        log.warn("登陆页面/loginInput.do跳转失败！");
      }
      e.printStackTrace();
    }
    catch (IOException e) {
      if (log.isWarnEnabled()) {
        log.warn("登陆页面/loginInput.do不存在,请检查路径或配置文件!");
      }
      e.printStackTrace();
    }
  }

  /**
   * @param session
   * @return
   */
  private boolean checkPMService(User user) {
    //在这里实现具体的权限控制方法
    return false;
  }

}
