package org.ming.oa.identity.dao.impl;


import java.util.List;

import org.ming.oa.common.dao.impl.BaseDaoImpl;
import org.ming.oa.identity.bean.User;
import org.ming.oa.identity.dao.IRoleDao;
import org.ming.oa.util.pager.PageModel;
import org.springframework.stereotype.Repository;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl implements IRoleDao{

	@Override
	public void deleteRoleByIds(Long[] ids) {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer();
		hql.append("update Role set delFlag = 0 where ");
		for(int i=0;i<ids.length;i++) {
			 hql.append(i==0? " id = ? " : " or id = ? ");
		}
		
		this.bulkUpdate(hql.toString(), ids);
	}

	@Override
	public List<User> selectRoleUser(Long id, PageModel pageModel) {
		String hql="select u from User u inner join u.roles r where u.delFlag ='1' and r.id = "+id;
		return this.findByPage(hql, pageModel, null);
	}

	@Override
	public List<User> showAddUnBindUserByRoleId(Long id, PageModel pageModel) {
	
		StringBuffer hql=new StringBuffer();
		hql.append("select u from User u where u.delFlag='1' and u.userId not in ( ");
		hql.append("select u.userId from User u inner join u.roles r where u.delFlag ='1' and r.id = "+id+" )");
		
		return this.findByPage(hql.toString(), pageModel, null);
	}



	

}
