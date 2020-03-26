package org.ming.oa.identity.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.ming.oa.identity.bean.Module;
import org.ming.oa.identity.service.IIdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@Resource(name="indentityService")
	private IIdentityService identityService;

	@RequestMapping(value="/main")
	public String main(Model model,HttpSession session) {
		
		try {
			
			//1、根据用户账号查询权限信息，控制左侧菜单栏的相关菜单的显示与隐藏
			//map集合的key存放的是 一级模块   value存放的是对应的二级模块
			Map<Module,List<Module>>  maps = identityService.findLeftMenuOperas();
			model.addAttribute("maps", maps);
			
			//2、根据用户账号查询权限信息，用于控制页面中按钮的显示与隐藏   user:addUser   user:deleteUser
			List<String> userOperas = identityService.findUserOperasByUserId();
			session.setAttribute("userOperas", userOperas);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//  /WEB-INF/jsp/main.jsp
		return "main";
	}

	@RequestMapping(value = "/login")
	public String login() {
		//WEB-INF/jsp/main.jsp
		return "login";
	}

	@RequestMapping(value = "/home")
	public String home() {
		//WEB-INF/jsp/home.jsp
		return "home";
	}

}
