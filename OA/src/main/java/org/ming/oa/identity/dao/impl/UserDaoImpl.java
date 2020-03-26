package org.ming.oa.identity.dao.impl;

import org.ming.oa.common.dao.impl.BaseDaoImpl;
import org.ming.oa.identity.dao.IUserDao;
import org.springframework.stereotype.Repository;

@Repository(value = "userDaoImpl")
public class UserDaoImpl extends BaseDaoImpl implements IUserDao {

	@Override
	public void deleteUser(String[] ids) {
		StringBuffer sb=new StringBuffer();
		sb.append(" update User set delFlag =0 where ");
		for (int i = 0; i < ids.length; i++) {
			sb.append(i==0?" userId= ? ":" or userId = ? ");
		}
		
		this.bulkUpdate(sb.toString(), ids);
	}

}
