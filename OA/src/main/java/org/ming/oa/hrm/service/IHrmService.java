package org.ming.oa.hrm.service;

import java.util.List;
import java.util.Map;

import org.ming.oa.hrm.bean.Dept;

public interface IHrmService {
	
	public List<Dept> getAllDepts();

	//获取部门信息
	public List<Map<String, String>> getDepts();
	
	//获取职位信息
	public List<Map<String, String>> getJobs();
}	
