package org.ming.oa.identity.controller;

import java.util.List;

import javax.annotation.Resource;
import org.ming.oa.identity.bean.Module;
import org.ming.oa.identity.bean.Role;
import org.ming.oa.identity.service.IIdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/identity/popedom")
public class PopedomController {
	
	@Resource(name="indentityService")
	private IIdentityService identityService;

	
		
	//加载角色绑定操作主页面
	@RequestMapping(value="/mgrPopedom")
	public String mgrPopedom(@RequestParam("id")Long id,Model model) {
		//将角色id存放在modle中
		model.addAttribute("id", id);
		
		return "identity/role/bindPopedom/mgrPopedom";
	}
	

	//加载角色绑定操作主页面
	@RequestMapping(value="/loadThirdModule")
	public String loadThirdModule(@RequestParam("code")String code,@RequestParam("id")Long id,Model model) {
		
		try {
			
			//根据角色id获取角色信息
			Role role = identityService.getRoleByRoleId(id);
			model.addAttribute("role", role);
			
			Module module = identityService.getModuleByCode(code);
			model.addAttribute("module", module);
			
			//根据模块的code获取第三级子模块信息
			List<Module>  modules = identityService.loadThirdModule(code);
			model.addAttribute("modules", modules);
			
			//根据角色id以及二级模块code获取已绑定的第三级模块信息
			List<String> codes = identityService.findOperasByRoleIdAndCode(code,id);
			model.addAttribute("codes", codes);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "identity/role/bindPopedom/operas";
	}
	
	
	 //绑定权限
	@RequestMapping(value="/bindOperas")
	public String bindOperas(@RequestParam("code")String code,@RequestParam("id")Long id,@RequestParam("codes")String codes,Model model) {
		
		try {
			identityService.bindOperas(code,id,codes);
			model.addAttribute("message", "绑定成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
		}

		return "forward:/identity/popedom/loadThirdModule";
	}
		
		
		
}
