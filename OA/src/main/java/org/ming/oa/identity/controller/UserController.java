package org.ming.oa.identity.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.ming.oa.hrm.bean.Dept;
import org.ming.oa.hrm.bean.Job;
import org.ming.oa.hrm.service.IHrmService;
import org.ming.oa.identity.bean.User;
import org.ming.oa.identity.service.IIdentityService;
import org.ming.oa.util.ConstantUtils;
import org.ming.oa.util.CookiesUtil;
import org.ming.oa.util.pager.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/identity/user")
public class UserController {
	
	@Resource(name = "indentityService")
	private IIdentityService indentityService;
	
	@Resource(name = "hrmService")
	private IHrmService hrmService;
	
	@Autowired
	private HttpServletRequest request;
	
	@ResponseBody
	@RequestMapping(value = "/ajaxLogin",produces = {"application/text;charset=utf-8"})
	public String ajaxLogin(User user,@RequestParam("vcode")String vcode,String rem) {
		
		String message="";
		try {
			message=indentityService.ajaxLogin(user,vcode,rem);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return message;
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		//1.将session中的用户信息清空
		request.getSession().removeAttribute(ConstantUtils.SESSION_USER);
		//2.清除Cookies中的用户信息
		CookiesUtil.removeCookie(ConstantUtils.LOGIN_COOKIE,request,response);
		return "login";
	}
	
	//分页查询用户信息
	@RequestMapping(value = "/selectUser")
	public String selectUser(User user,PageModel pageModel,Model model) {
		//Spring-mvc将前台的信息自动封装到user中，并且可以自动保存再request中
		//可以通过Spring的Model对象来进行addAttribute操作
		try {
			
			//System.out.println("-------------->"+pageModel.getPageIndex());
			
			List<User> users = indentityService.SelectUser(user,pageModel);
			model.addAttribute("users", users);
			model.addAttribute("page", pageModel); //不写 Spring-mvc也自动加上
			
			if(user.getDept()==null) {
				Dept dept=new Dept();
				dept.setId(Long.parseLong("0"));
				user.setDept(dept);
			}
			if(user.getJob()==null) {
				Job job=new Job();
				job.setCode("0");
				user.setJob(job);
			}
			
			model.addAttribute("user", user);   //不写 Spring-mvc也自动加上
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		System.out.println("----->"+user);
		//  /WEB-INF/jsp/identity/user/user.jsp
		return "identity/user/user";
	}
	
	//此方法是通过异步请求的方式拿信息，同样可以采用 request.setAttribute()，前台EL拿的方式。
	@ResponseBody   //写异步请求有json必须加它
	@RequestMapping(value = "/getDeptandJobJson",produces = {"application/json;charset=utf-8"})
	public String getDeptandJobJson() {
		try {
			List<Map<String, String>> depts=hrmService.getDepts();
			List<Map<String, String>> jobs=hrmService.getJobs();
			
			JSONObject json=new JSONObject();
			json.put("depts", depts);
			json.put("jobs", jobs);
			
			//{"dept":[{"name":"技术部","id":1},{"name":"运营部","id":2},{"name":"财务部","id":3},{"name":"人事部","id":4},{"name":"总公办","id":5}],
			//"jobs":[{"name":"职员","code":"0001"},{"name":"Java开发工程师","code":"0002"},{"name":"Java中级开发工程师","code":"0003"},{"name":"Java高级开发工程师","code":"0004"},{"name":"系统管理员","code":"0005"},{"name":"架构师","code":"0006"},{"name":"主管","code":"0007"},{"name":"经理","code":"0008"},{"name":"总经理","code":"0009"}]}

			System.out.println(json);
			
			return json.toString();
		} catch (Exception e) {
			throw new RuntimeException("获取部分以及职位异常"+e.getMessage());
		}
		
				
	}
	
	
	//更新个人主页信息
	@RequestMapping(value = "/updateSelf")
	public String updateSelf(User user,Model model) {
		//Spring-mvc将前台的信息自动封装到user中，并且可以自动保存再request中
		//可以通过Spring的Model对象来进行addAttribute操作
		try {
			indentityService.updateUser(user);
			model.addAttribute("message", "更新成功！");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("message", e.getMessage());
		}
		
		//  /WEB-INF/jsp/identity/user/user.jsp
		return "home";
	}
	
	
	//更新个人主页信息
	@RequestMapping(value = "/showAddUser")
	public String showAddUser() {
		
		return "identity/user/addUser";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/validUserId",produces = {"application/text;charset=utf-8"})
	public String validUserId(@RequestParam("userId")String userId) {
		
		String result="";
		try {
			result = indentityService.validUserId(userId);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("用户信息查询失败"+e.getMessage());
		}
		
		
	}

	

	//添加个人主页信息
		@RequestMapping(value = "/addUser")
		public String addUser(User user,Model model) {
			
			try {
			indentityService.addUser(user);
			model.addAttribute("message", "用户信息添加成功！");
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				
			}
			
			return "identity/user/addUser";
		}


		//删除用户信息
		@RequestMapping(value = "/deleteUser")
		public String deleteUser(@RequestParam("userIds")String userIds,PageModel pageModel,Model model) {
			
			try {
			indentityService.deleteUser(userIds);
			model.addAttribute("message", "删除成功！");
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				
			}
			
			return "forward:/identity/user/selectUser";
		}


		
		//查看修改用户信息
		@RequestMapping(value = "/showUpdateUser")
		public String showUpdateUser(@RequestParam("userId")String userId,Model model) {
			
			try {
				User user=indentityService.findUserByUserId(userId);
				model.addAttribute("user", user);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				
			}
			
			return "identity/user/updateUser";
		}
		
		

		//修改用户信息
		@RequestMapping(value = "/updateUser")
		public String updateUser(User user,Model model) {
			
			try {
			indentityService.showUpdateUser(user);
			model.addAttribute("message", "修改用户信息成功！");
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				
			}
			
			return "identity/user/updateUser";
		}
		
		
		//预览用户信息
		@RequestMapping(value = "/preUser")
		public String preUser(@RequestParam("userId")String userId,Model model) {
			
			try {
				User user=indentityService.findUserByUserId(userId);
			model.addAttribute("user",user);
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
			}
			
			return "identity/user/preUser";
		}


		
		//激活用户
		@ResponseBody
		@RequestMapping(value = "/activeUser",produces = {"application/text;charset=utf-8"})
		public String activeUser(@RequestParam("status")Short status,@RequestParam("userId")String userId,PageModel pageModel,Model model) {
			
			try {
				indentityService.activeUser(userId,status);
				return status==1?"用户激活成功！":"用户冻结成功！";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return status==1?"用户激活失败！":"用户冻结失败！";
				
			}
			
		}
		
		

}
