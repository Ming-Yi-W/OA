package org.ming.oa.identity.dao;

import org.ming.oa.common.dao.BaseDao;

public interface IUserDao extends BaseDao{

	void deleteUser(String[] ids);

}
