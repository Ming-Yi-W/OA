package org.ming.oa.identity.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ming.oa.common.dao.impl.BaseDaoImpl;
import org.ming.oa.identity.bean.Module;
import org.ming.oa.identity.dao.IModuleDao;
import org.ming.oa.util.pager.PageModel;
import org.springframework.stereotype.Repository;

@Repository("moduleDao")
public class ModuleDaoImpl extends BaseDaoImpl implements IModuleDao{

	//获取所有的模块信息
	@Override
	public List<Module> findAllModule() {
		// TODO Auto-generated method stub
		String hql = "from Module where delFlag = '1' ";
		return this.find(hql);
	}

	//模块信息分页查询
	@Override
	public List<Module> selectModule(String code, int length, PageModel pageModel) {
		// TODO Auto-generated method stub
		String hql = " FROM Module WHERE CODE LIKE ? AND LENGTH(CODE) = ? ";
		return this.findByPage(hql, pageModel, Arrays.asList(code,length));
	}

	 //根据父级模块的code获取当前父级模块下存在的最大的子模块的code
	@Override
	public String findMaxCode(String parentCode) {
		// TODO Auto-generated method stub
		Object[] params = new Object[2];
		String hql = "select max(code) from Module  where code like ? and length(code) = ? and delFlag = '1' ";
		params[0] = StringUtils.isEmpty(parentCode) ? "%" : parentCode+"%";
		params[1] = StringUtils.isEmpty(parentCode) ? 4 : parentCode.length() + 4;
		return this.findUniqueEntity(hql,params);
	}

	
}
