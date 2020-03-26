package org.ming.oa.identity.controller;

import java.util.List;

import javax.annotation.Resource;

import org.ming.oa.identity.bean.Role;
import org.ming.oa.identity.bean.User;
import org.ming.oa.identity.service.IIdentityService;
import org.ming.oa.util.pager.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/identity/role")
public class RoleController {
	
	@Resource(name="indentityService")
	private IIdentityService identityService;
	
	//角色信息分页查询
	@RequestMapping(value="/selectRole")
	public String selectUser(PageModel pageModel,Model model) {
		
		try {
			pageModel.setPageSize(8);
			List<Role> roles = identityService.selectRole(pageModel);
			model.addAttribute("roles", roles);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//跳转至角色列表页面
		return "identity/role/role";
	}
	
	
	//加载添加角色信息页面
	@RequestMapping(value="/showAddRole")
	public String showAddRole() {
		
		
		return "identity/role/addRole";
	}
	
	
	//添加角色
	@RequestMapping(value="/addRole")
	public String addRole(Role role,Model model) {
		try {
			
			identityService.addRole(role);
			
			model.addAttribute("message", "添加成功！");	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());	
		}
		
		return "identity/role/addRole";
	}
	
	//删除角色
	@RequestMapping(value="/deleteRole")
	public String deleteRole(@RequestParam("roleIds")String roleIds,Model model) {
		try {
			
			identityService.deleteRoleByIds(roleIds);
			
			model.addAttribute("message", "删除成功！");	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());	
		}
		
		//查询用户信息
		return "forward:/identity/role/selectRole";
	}
	
	    //展示修改角色信息页面
		@RequestMapping(value="/showUpdateRole")
		public String showUpdateRole(@RequestParam("roleId")Long roleId,Model model) {
			try {
				
				//根据用户账号获取用户信息
			    Role role = identityService.getRoleByRoleId(roleId);
			    model.addAttribute("role", role);
			    
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());	
			}
			
			
			return "identity/role/updateRole";
		}
		
		//更新角色信息
		@RequestMapping(value="/updateRole")
		public String updateRole(Role role,Model model) {
			try {
				
				identityService.updateRole(role);
				model.addAttribute("message", "更新成功！");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());	
			}
			
			
			return "identity/role/updateRole";
		}
		
		
		
		//查看角色用户信息
		@RequestMapping(value="/selectRoleUser")
		public String selectRoleUser(@RequestParam("id")Long id,PageModel pageModel,Model model) {
			try {
				Role role=identityService.getRoleByRoleId(id);
				List<User> users=identityService.selectRoleUser(id,pageModel); 
				model.addAttribute("users", users);
				model.addAttribute("role", role);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			return "identity/role/bindUser/roleUser";
		}
		
		
		
		//解绑用户信息
		@RequestMapping(value="/unBindUser")
		public String unBindUser(@RequestParam("userIds")String userIds,@RequestParam("id")Long id,PageModel pageModel,Model model) {
			try {
				identityService.unBindUser(id,userIds); 
				model.addAttribute("message", "解绑用户成功！");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				model.addAttribute("message", "解绑用户失败！");
			}
			
			
			return "forward:/identity/role/selectRoleUser";
		}
		
		
		//查看未绑用户信息
		@RequestMapping(value="/showAddUnBindUserByRoleId")
		public String showAddUnBindUserByRoleId(@RequestParam("id")Long id,PageModel pageModel,Model model) {
			try {
				
				List<User> users=identityService.showAddUnBindUserByRoleId(id,pageModel);
				model.addAttribute("users", users);
				model.addAttribute("id", id);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return "identity/role/bindUser/bindUser";
			
		}
		
		//绑定用户
		@RequestMapping(value="/bindUser")
		public String bindUser(@RequestParam("userIds")String userIds,@RequestParam("id")Long id,PageModel pageModel,Model model) {
			try {
				
				identityService.bindUser(id,userIds);
				model.addAttribute("message", "绑定成功!");
			} catch (Exception e) {
				// TODO: handle exception
				model.addAttribute("message", "绑定失败!");
				e.printStackTrace();
			}
			
			return "identity/role/bindUser/bindUser";
			
		}
		
}
