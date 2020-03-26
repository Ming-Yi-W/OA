package org.ming.oa.util.Interceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ming.oa.identity.bean.User;
import org.ming.oa.identity.service.IIdentityService;
import org.ming.oa.util.ConstantUtils;
import org.ming.oa.util.CookiesUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	
	@Resource
	private IIdentityService indentityService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//拦截器工作
		
		//先判断是否存在Session
		User user=(User)request.getSession().getAttribute(ConstantUtils.SESSION_USER);
		if (user!=null) {
			return true;
		}else {
			//判断Cookie，即是否有记住一周
			Cookie cookie=CookiesUtil.getCookieByName(ConstantUtils.LOGIN_COOKIE, request);
			if (cookie!=null) {
				String cookieValue=cookie.getValue();
				String infos[]=cookieValue.split("_");
				//判断cookie中的用户名是否存在
				//查询数据库的原因是  有可能用户修改了密码等操作，所以必须进行验证
				User u=indentityService.findUserByUserId(infos[0]);
				if (u==null||u.getDelFlag().equals("0")) {
					request.setAttribute("message", "用户名有误，请联系管理员");
					request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
					return false;
					//判断cookie中的密码是否正确
				}else if(u.getStatus()==0) {
					request.setAttribute("message", "用户尚未激活，请联系管理员");
					request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
					return false;
				} else if (!u.getPassWord().equals(infos[1])) {
					request.setAttribute("message", "密码有误，请联系管理员");
					request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
					return false;
				}else {
					request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
					request.getSession().setAttribute(ConstantUtils.SESSION_USER, u);
					return true;
				}
			
			//Cookie不存在
			}else {
				request.setAttribute("message", "请先进行登录");
				request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
				return false;
			}
			
		}
		
	}
	
	
	
	
	

}
