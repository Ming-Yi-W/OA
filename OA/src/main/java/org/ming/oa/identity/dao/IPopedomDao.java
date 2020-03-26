package org.ming.oa.identity.dao;

import java.util.List;

import org.ming.oa.common.dao.BaseDao;

public interface IPopedomDao extends BaseDao{

	//根据角色的id以及二级模块的code将之前绑定所绑定的权限信息都删除
	void deletePopedom(String code, Long id);

	//查询权限表获取用户拥有的二级模块的code的信息 
	List<String> findLeftMenuOperas(String userId);

	//2、根据用户账号查询权限信息，用于控制页面中按钮的显示与隐藏 
	List<String> findUserOperasByUserId(String userId);

	

}
