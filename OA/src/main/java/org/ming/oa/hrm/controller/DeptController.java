package org.ming.oa.hrm.controller;

import java.util.List;

import javax.annotation.Resource;

import org.ming.oa.hrm.bean.Dept;
import org.ming.oa.hrm.service.IHrmService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hrm/dept")
public class DeptController {
	
	@Resource(name = "hrmService")
	private IHrmService hrmService;
	
	//获取所有的部门信息
	@ResponseBody
	@RequestMapping("/getAllDept")
	public String getAllDept() {
		
		List<Dept> depts=hrmService.getAllDepts();
		System.out.println("dept:"+depts.size());
		return "";
	}
	
}
