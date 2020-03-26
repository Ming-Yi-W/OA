package org.ming.oa.identity.dao;


import java.util.List;

import org.ming.oa.common.dao.BaseDao;
import org.ming.oa.identity.bean.User;
import org.ming.oa.util.pager.PageModel;

public interface IRoleDao extends BaseDao{

	//删除角色
	void deleteRoleByIds(Long[] ids);

	List<User> selectRoleUser(Long id, PageModel pageModel);

	List<User> showAddUnBindUserByRoleId(Long id, PageModel pageModel);



	

}
