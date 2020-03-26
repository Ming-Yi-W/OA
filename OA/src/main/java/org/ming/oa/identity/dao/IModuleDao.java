package org.ming.oa.identity.dao;

import java.util.List;

import org.ming.oa.common.dao.BaseDao;
import org.ming.oa.identity.bean.Module;
import org.ming.oa.util.pager.PageModel;

public interface IModuleDao extends BaseDao{

	//获取所有的模块信息
	List<Module> findAllModule();

	//模块信息分页查询
	List<Module> selectModule(String code, int length, PageModel pageModel);

	//根据父级模块的code获取当前父级模块下存在的最大的子模块的code
	String findMaxCode(String parentCode);



}
