package org.ming.oa.identity.dao.impl;

import java.util.Arrays;
import java.util.List;
import org.ming.oa.common.dao.impl.BaseDaoImpl;
import org.ming.oa.identity.dao.IPopedomDao;
import org.springframework.stereotype.Repository;

@Repository("popedomDao")
public class PopedomDaoImpl extends BaseDaoImpl implements IPopedomDao{

	//根据角色的id以及二级模块的code将之前绑定所绑定的权限信息都删除
	@Override
	public void deletePopedom(String code, Long id) {
		// TODO Auto-generated method stub
		String hql = "delete from Popedom p where p.role.id = "+id+" and p.module.code = '"+code+"' ";
		
		this.bulkUpdate(hql, null);
	}

	//查询权限表获取用户拥有的二级模块的code的信息 
	@Override
	public List<String> findLeftMenuOperas(String userId) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct p.module.code from Popedom p where p.role.id in ( ");
		//通过子查询获取指定用户拥有的角色信息
		hql.append(" select r.id from Role r inner join r.users u where u.userId = ? ) order by p.module.code asc");
		return (List<String>) this.find(hql.toString(), new Object[] {userId});
	}

	
	//2、根据用户账号查询权限信息，用于控制页面中按钮的显示与隐藏 
	@Override
	public List<String> findUserOperasByUserId(String userId) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct p.opera.code from Popedom p where p.role.id in ( ");
		//通过子查询获取指定用户拥有的角色信息
		hql.append(" select r.id from Role r inner join r.users u where u.userId = ? ) order by p.opera.code asc");
		return (List<String>) this.find(hql.toString(), new Object[] {userId});
	}

	

}
