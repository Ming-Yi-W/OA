package org.ming.oa.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {

	//cookieName cookie名，age cookie存活时间
	public static void addCookie(String cookieName,int age,String cookieValue,HttpServletRequest request,HttpServletResponse response) {
			
		Cookie cookie=getCookieByName(cookieName,request);
		if(cookie==null) {
			cookie=new Cookie(cookieName, cookieValue);
		}
		cookie.setMaxAge(age);
		//改为SpringBoot后，request.getContextPath()为空。所以要用/表示8080后得目录
		cookie.setPath("/"); //   /OA
		//一个浏览器只能有一个cookie，防止不同cookieName的value不被更改
		cookie.setValue(cookieValue);
		
		//将服务端设置好的Cookie相应至客户端
		response.addCookie(cookie);
	}
	
	public static void removeCookie(String cookieName,HttpServletRequest request,HttpServletResponse response) {
		Cookie cookie=getCookieByName(cookieName, request);
		if (cookie!=null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	public static Cookie getCookieByName(String cookieName, HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		if (cookies==null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			//客户端浏览器中存在此cookie
			if (cookie.getName().equals(cookieName)) {
				//返回当前cookie
				return cookie;
			}
		}
		
		return null;
	}
}
