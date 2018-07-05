package com.framework.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author zhangkeyi
 * 统一进行Cookie的提取和保存
 * 
 */


public class CookieUtil
{

    public CookieUtil()
    {
    }

    public static String getString(HttpServletRequest request, String key)
    {
        String value = null;
        Cookie cookie = null;
        Cookie cookies[] = request.getCookies();
        if(cookies != null)
        {
            for(int i = 0; i < cookies.length; i++)
            {
                if(!cookies[i].getName().equals(key))
                    continue;
                cookie = cookies[i];
                break;
            }

        }
        if(cookie != null)
            value = cookie.getValue();
        if(value == null)
            value = "";
        return value;
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int seconds)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(seconds);
        response.addCookie(cookie);
    }
}
