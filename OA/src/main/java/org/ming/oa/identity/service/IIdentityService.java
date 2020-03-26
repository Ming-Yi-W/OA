package org.ming.oa.identity.service;

import java.util.List;
import java.util.Map;

import org.ming.oa.identity.bean.Module;
import org.ming.oa.identity.bean.Role;
import org.ming.oa.identity.bean.User;
import org.ming.oa.util.pager.PageModel;

public interface IIdentityService {

	String ajaxLogin(User user, String vCode, String rem);

	User findUserByUserId(String string);

	List<User> SelectUser(User user, PageModel pageModel) ;

	void updateUser(User user);

	String validUserId(String userId);

	void addUser(User user);

	void deleteUser(String userIds);

	void showUpdateUser(User userId);

	void activeUser(String  userId,Short status);
		

	/**************************************************角色管理模块**************************************************/
	//角色分页查询
	List<Role> selectRole(PageModel pageModel);

	//更新角色信息
	void updateRole(Role role);

	//根据角色id获取角色信息
	Role getRoleByRoleId(Long roleId);

	//删除用户
	void deleteRoleByIds(String roleIds);

	//添加角色
	void addRole(Role role) throws Exception;

	List<User> selectRoleUser(Long id,PageModel pageModel);

	void unBindUser(Long id, String userIds);

	List<User> showAddUnBindUserByRoleId(Long id,PageModel pageModel);

	void bindUser(Long id, String userIds);
	/**************************************************模块管理模块**************************************************/

	//异步加载模块信息
	String ajaxdTree();

	//模块信息分页查询
	List<Module> selectModule(String code, PageModel pageModel);

	//更新模块
	void updateModule(Module module);

	 //展示更新模块的jsp页面
	Module getModuleByCode(String code);

	//保存模块信息
	void addModule(Module module, String parentCode);

	 //删除模块信息
	void deleteModule(String[] split);
	
	//根据模块的code获取第三级子模块信息
	List<Module> loadThirdModule(String code);

	//根据角色id一级二级模块code获取已绑定的第三级模块信息
	List<String> findOperasByRoleIdAndCode(String code, Long id);

	
	 //绑定权限
	void bindOperas(String code, Long id, String codes);
	
	
	//1、根据用户账号查询权限信息，控制左侧菜单栏的相关菜单的显示与隐藏
	Map<Module, List<Module>> findLeftMenuOperas();

	//2、根据用户账号查询权限信息，用于控制页面中按钮的显示与隐藏   user:addUser   user:deleteUser
	List<String> findUserOperasByUserId();
	
}
