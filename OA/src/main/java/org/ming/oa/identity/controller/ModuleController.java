package org.ming.oa.identity.controller;

import java.util.List;

import javax.annotation.Resource;

import org.ming.oa.identity.bean.Module;
import org.ming.oa.util.pager.PageModel;
import org.ming.oa.identity.service.IIdentityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/identity/module")
public class ModuleController {
	
	@Resource(name="indentityService")
	private IIdentityService identityService;
	
	
	//跳转至模块主页面
	@RequestMapping(value="/mgrModule")
	public String mgrModule() {
		
		return "identity/module/mgrModule";
	}
	
	//异步加载模块信息
	 @ResponseBody
    @RequestMapping(value="/ajaxdTree",produces= {"application/json;charset=utf-8"})
    public String ajaxdTree() {
    	String message = "";
    	try {
    		 
    		 message  = identityService.ajaxdTree();
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e.getMessage());
		}
    	
    	
    	return message;
    }

	
	//模块信息分页查询
	@RequestMapping(value="/loadSonModule")
	public String loadSonModule(String code,PageModel pageModel,Model model) {
		
		try {
			List<Module> modules = identityService.selectModule(code,pageModel);
			model.addAttribute("modules", modules);
			//将父级code存放在module中
			model.addAttribute("code", code);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//跳转至模块列表页面
		return "identity/module/module";
	}
	
	
	
	
	  //删除模块信息
    @RequestMapping(value="/deleteModule")
    public String deleteModule(@RequestParam("codes")String codes,Model model) {
    	
    	try {
    		
    		identityService.deleteModule(codes.split(","));
    		
    		model.addAttribute("message", "删除成功！");
			
		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("message", e.getMessage());
		}
    	
    	//删除动过完成之后就进行分页查询
         return "forward:/identity/module/loadSonModule";
    }
    
   
    //展示添加模块信息的页面
    @RequestMapping(value="/showAddModule")
    public String showAddModule(String code,Model model) {
    	
    	//将父级模块的code存放在model
    	model.addAttribute("code", code);

    	return "identity/module/addModule";
    }
    
    //保存模块信息
    @RequestMapping(value="/addModule")
    public String addModule(Module module,@RequestParam("parentCode")String parentCode,Model model) {
    	
    	try {
    		
    		identityService.addModule(module,parentCode);
    		model.addAttribute("code", parentCode);
    		
    		model.addAttribute("message", "添加成功！");
    	} catch (Exception e) {
    		// TODO: handle exception
    		model.addAttribute("message", e.getMessage());
    	}
    	
    	return "identity/module/addModule";
    }
    
    
   
    //展示更新模块的jsp页面
    @RequestMapping(value="/showUpdateModule")
    public String showUpdateModule(@RequestParam("code")String code,Model model) {
    	
    	try {
    		
    		Module module = identityService.getModuleByCode(code);
    		model.addAttribute("module", module);
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	
    	return "identity/module/updateModule";
    }
    
    //更新模块
    @RequestMapping(value="/updateModule")
    public String updateModule(Module module,Model model) {
    	
    	try {
    		
    		identityService.updateModule(module);
    		model.addAttribute("message", "更新成功！");
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    		model.addAttribute("message", e.getMessage());
    	}
    	
    	return "identity/module/updateModule";
    }
    
		
	
}
