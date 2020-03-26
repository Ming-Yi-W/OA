package org.ming.oa.hrm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.ming.oa.hrm.bean.Dept;
import org.ming.oa.hrm.dao.DeptDao;
import org.ming.oa.hrm.dao.IJobDao;
import org.ming.oa.hrm.service.IHrmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "hrmService")
//事务
@Transactional
public class HrmServiceImpl implements IHrmService {

	@Resource(name = "deptDao")
	private DeptDao deptdao;
	
	@Resource(name = "jobDao")
	private IJobDao jobDao;
	
	@Override
	public List<Dept> getAllDepts() {
		
		List<Dept> depts=deptdao.find(Dept.class);
		return depts;
	}
	//获取部门信息
	@Override
	public List<Map<String, String>> getDepts() {
		//hql语句
		String hql="select new map(d.id as id,d.name as name) from Dept d";
		
		return deptdao.find(hql);
	}
	//获取职位信息
	@Override
	public List<Map<String, String>> getJobs() {
		String hql="select new map(j.code as code,j.name as name) from Job j";
		
		return jobDao.find(hql);
	}
	
}
