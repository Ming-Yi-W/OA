package org.ming.oa.identity.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.ming.oa.identity.bean.Module;
import org.ming.oa.identity.bean.Popedom;
import org.ming.oa.identity.bean.Role;
import org.ming.oa.identity.bean.User;
import org.ming.oa.identity.dao.IRoleDao;
import org.ming.oa.identity.dao.IUserDao;
import org.ming.oa.identity.dao.IModuleDao;
import org.ming.oa.identity.dao.IPopedomDao;
import org.ming.oa.identity.service.IIdentityService;
import org.ming.oa.util.ConstantUtils;
import org.ming.oa.util.CookiesUtil;
import org.ming.oa.util.MD5;
import org.ming.oa.util.pager.PageModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service(value = "indentityService")
@Transactional
public class IndentityServiceImpl implements IIdentityService {

	@Resource(name = "userDaoImpl")
	private IUserDao userdao;

	@Resource(name = "roleDao")
	private IRoleDao roleDao;
	
	@Resource(name = "moduleDao")
	private IModuleDao moduleDao;
	
	@Resource(name = "popedomDao")
	private IPopedomDao popedomDao;
	
	@Override
	public String ajaxLogin(User user, String vCode, String rem) {
		// 1.判断验证码输入是否正确
		try {
			String checkVcode = (String) ConstantUtils.getRequest().getSession()
					.getAttribute(ConstantUtils.VERIFY_CODE);
			if (!vCode.equals(checkVcode)) {
				return "验证码输入有误，请核实！";

			} else { // 2.判断用户名密码是否正确
				User v = userdao.get(User.class, user.getUserId());
				if (v == null||v.getDelFlag().equals("0")) {
					return "用户名输入有误！";
				}else if(v.getStatus()==0) {
					return "用户尚未激活，请联系管理员！";
				}else if (!v.getPassWord().equals(user.getPassWord())) {
					return "密码输入有误！";
				}
				// 登录成功,设置session
				ConstantUtils.getRequest().getSession().setAttribute(ConstantUtils.SESSION_USER, v);

				// 3.判断是否选中 记住一周
				if ("1".equals(rem)) {
					// 设置cookie
					CookiesUtil.addCookie(ConstantUtils.LOGIN_COOKIE, 60 * 60 * 24 * 7,
							v.getUserId() + "_" + v.getPassWord(), ConstantUtils.getRequest(),
							ConstantUtils.getResponse());
				}
			}

			// 执行到这个return说明信息正确，隐式Boolean类型，所有非空字符串都为true
			return "";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public User findUserByUserId(String userId) {
		return userdao.get(User.class, userId);
	}

	// 分页查询用户信息
	@Override
	public List<User> SelectUser(User user, PageModel pageModel) {
	
		
		try {
			StringBuffer hql = new StringBuffer();
			List<Object> params=new ArrayList<Object>();
			hql.append(" from User u where u.delFlag=1 ");
			
			if (StringUtils.isNotEmpty(user.getName())) {
				hql.append(" and u.name like ?  ");
				params.add("%"+user.getName()+"%");
			}
			if (StringUtils.isNotEmpty(user.getPhone())) {
				hql.append(" and u.phone like ? ");
				params.add("%"+user.getPhone()+"%");
			}
			if (user.getDept() != null && user.getDept().getId() != null
					&& user.getDept().getId() != 0) {// getId==0（Long向下转型） 表示没有选，前台默认value=0
				hql.append(" and u.dept.id like ? ");
				params.add(user.getDept().getId());
			}
			if (user.getJob() != null && StringUtils.isNotEmpty(user.getJob().getCode())
					&& !user.getJob().getCode().equals("0")) {
				hql.append(" and u.job.code like ? ");
				params.add(user.getJob().getCode());
			}
			return userdao.findByPage(hql.toString(), pageModel, params);
		} catch (Exception e) {
			throw new RuntimeException("分页查询Service异常"+e.getMessage());
		}
		
	
	}

	@Override
	public void updateUser(User user) {
		try {
			User u=userdao.get(User.class, user.getUserId());
			//用到了hibernate持久化
			u.setName(user.getName());
			u.setEmail(user.getEmail());
			u.setTel(user.getTel());
			u.setQqNum(user.getQqNum());
			u.setPhone(user.getPhone());
			u.setAnswer(user.getAnswer());
			u.setModifier(user);
			u.setModifyDate(new Date());
			//session 为修改后的user
			ConstantUtils.getRequest().getSession().setAttribute(ConstantUtils.SESSION_USER, u);
		} catch (Exception e) {
			throw new RuntimeException("更新主页用户异常"+e.getMessage());
		}
		
		
	}

	@Override
	public String validUserId(String userId) {
		try {
			User user=userdao.get(User.class, userId);
			if(user!=null) {
				return "用户名已存在，请重新输入！";
			}else {
				return "";
			}
			
		} catch (Exception e) {
			throw new RuntimeException("异步验证用户名异常"+e.getMessage());
		}
		
	
	}

	@Override
	public void addUser(User user) {
		
		try {
			//user.setPassWord(MD5.getMD5(user.getPassWord()));
			user.setPassWord(user.getPassWord());
			user.setChecker((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
			user.setCreateDate(new Date());
			userdao.save(user);
		} catch (Exception e) {
			
			throw new RuntimeException("添加用户名异常"+e.getMessage());
		}
		
	}

	@Override
	public void deleteUser(String userIds) {
		try {
			String ids[]=userIds.split(",");
			userdao.deleteUser(ids);
			
		} catch (Exception e) {
			throw new RuntimeException("删除用户异常"+e.getMessage());
		
		}
		
	}

	@Override
	public void showUpdateUser(User user) {
		try {
			User u=userdao.get(User.class, user.getUserId());
			//设置修改时间
			u.setModifyDate(new Date());
			//获取当前用户
			User sessionUser = (User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER);

			//设置修改人
			u.setModifier(sessionUser);
			u.setName(user.getName());
			u.setEmail(user.getEmail());
			u.setSex(user.getSex());
			u.setTel(user.getTel());
			u.setPhone(user.getPhone());
			u.setQuestion(user.getQuestion());
			u.setAnswer(user.getAnswer());
			u.setQqNum(user.getQqNum());
			u.setDept(user.getDept());
			u.setJob(user.getJob());
			
			
		} catch (Exception e) {
			throw new RuntimeException("更新用户异常"+e.getMessage());
		}
		
		
	}

	@Override
	public void activeUser(String userId,Short status) {
		try {
			User u=userdao.get(User.class, userId);
			u.setStatus(status);
		} catch (Exception e) {
			throw new RuntimeException(status==1?"激活异常":"冻结异常"+e.getMessage());
		}
		
		
	}

	/**************************************************角色管理模块**************************************************/
	@Override
	public List<Role> selectRole(PageModel pageModel) {
		// TODO Auto-generated method stub
		try {
		    
			String hql = "from Role where delFlag = 1";
			
			return roleDao.findByPage(hql.toString(), pageModel, null);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("查询异常："+e.getMessage());
		}
	}

	//更新角色信息
	@Override
	public void updateRole(Role role) {
		// TODO Auto-generated method stub
		 try {
			    
				Role r = roleDao.get(Role.class, role.getId());
				r.setName(role.getName());
				r.setRemark(role.getRemark());
				r.setModifyDate(new Date());
				r.setModifier((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
			
		 } catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("角色更新异常："+e.getMessage());
			}
		
	}

	//根据角色id获取角色信息
	@Override
	public Role getRoleByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		 try {
			    
				return roleDao.get(Role.class, roleId);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("角色信息加载异常："+e.getMessage());
			}
	}

	//删除角色
	@Override
	public void deleteRoleByIds(String roleIds) {
		// TODO Auto-generated method stub
		try {

			String[] ids = roleIds.split(",");
			
            Long[] rIds = new Long[ids.length];
            for(int i=0;i<rIds.length;i++) {
            	rIds[i] = Long.valueOf(ids[i]);
            }
			roleDao.deleteRoleByIds(rIds);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException("角色信息删除失败："+e.getMessage());
		}
	}

	//添加角色
	@Override
	public void addRole(Role role) throws Exception {
		// TODO Auto-generated method stub
      try {
		    
			role.setCreateDate(new Date());
			role.setCreater((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
			roleDao.save(role);
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("保存异常："+e.getMessage());
		}
	}

	@Override
	public List<User> selectRoleUser(Long id,PageModel pageModel) {
		  try {
			  //多对多查询
			  List<User> users=roleDao.selectRoleUser(id,pageModel);
			  return users;
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("查询角色对应用户异常："+e.getMessage());
			}
	}

	@Override
	public void unBindUser(Long id, String userIds) {
		  try {
			  
			  Role role = roleDao.get(Role.class, id);
			  Set<User> users=role.getUsers();
			  
			  String[] uids=userIds.split(",");
			  for (String uid : uids) {
				users.remove(userdao.get(User.class, uid));
			}
			  
			  //多对多查询
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("解绑用户异常："+e.getMessage());
			}
		
	}

	@Override
	public List<User> showAddUnBindUserByRoleId(Long id,PageModel pageModel) {
		 try {
			  //多对多查询
			  List<User> users=roleDao.showAddUnBindUserByRoleId(id,pageModel);
			  return users;
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("查询角色对应用户异常："+e.getMessage());
			}
	}

	@Override
	public void bindUser(Long id, String userIds) {
		 try {
			  Role role = roleDao.get(Role.class, id);
			  Set<User> users=role.getUsers();
			  
			  String[] uids=userIds.split(",");
			  for (String uid : uids) {
				users.add(userdao.get(User.class, uid));
			}
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("绑定角色对应用户异常："+e.getMessage());
			}
	}

	/**************************************************角色管理模块**************************************************/
	//获取所有的模块信息
	@Override
	public String ajaxdTree() {
		// TODO Auto-generated method stub
		try {
			
			List<Module> modules = moduleDao.findAllModule();
			
			JSONArray arr = new JSONArray();
			for(Module module : modules) {
				
				JSONObject obj = new JSONObject();
				
				//获取模块的code
				String code = module.getCode();
				//计算模块的父级code
				String pcode = code.length() == 4? "1" : code.substring(0,code.length()-4);
			    
				String name = module.getName();
				obj.put("code", code);
				obj.put("pcode", pcode);
				obj.put("name", name);
				
				arr.add(obj);
			}
			System.out.println("arr.toString():"+arr.toString());
			return arr.toString();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("模块信息加载异常："+e.getMessage());
		}
	}

	//模块信息分页查询
	@Override
	public List<Module> selectModule(String code, PageModel pageModel) {
		// TODO Auto-generated method stub
		try {
			List<Module> modules = moduleDao.selectModule(code == null ? "%"  : code+"%",StringUtils.isEmpty(code) ? 4 : code.length()+4,pageModel);
			
			return modules;
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("模块信息加载异常："+e.getMessage());
		}
	}

	//更新模块信息
	@Override
	public void updateModule(Module module) {
		// TODO Auto-generated method stub
      try {
			
            Module m = moduleDao.get(Module.class, module.getCode());
            //设置修改时间
            m.setModifyDate(new Date());
            m.setModifier((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
			
            m.setName(module.getName());
            m.setRemark(module.getRemark());
            m.setUrl(module.getUrl());
            
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("模块更新异常："+e.getMessage());
		}
	}

	//根据模块的code获取模块信息
	@Override
	public Module getModuleByCode(String code) {
		// TODO Auto-generated method stub
      try {
            return  moduleDao.get(Module.class, code);
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("模块加载异常："+e.getMessage());
		}
	}

	//添加模块
	@Override
	public void addModule(Module module, String parentCode) {
		// TODO Auto-generated method stub
		try {
			 
			   //parentCode：00010001   ---- > maxCode：000100010005   ---->当前添加的模块code:000100010006
			   //根据父级模块的code获取当前父级模块下存在的最大的子模块的code    
			   String maxCode = moduleDao.findMaxCode(parentCode);//000100010005
			   
			   String finalCode = StringUtils.isEmpty(parentCode) ? "" : parentCode;
			   
			   if(StringUtils.isEmpty(maxCode)) {
				    //没有找到子模块信息  当前模块code：00010001
				    //00010001    --- > 000100010001
				   finalCode = finalCode + "0001";
			   }else {
				   //找到子模块信息 000100010009   当前模块code：00010001   -- >000100010010
				  String newCode =  maxCode.substring(maxCode.length() - 4, maxCode.length());
				  int intNewCode = Integer.valueOf(newCode);
				  
				  if(intNewCode == 9999) {
					  throw new RuntimeException("子模块code过大，请联系管理员！");
				  }else {
					  //让最大值加1
					  int fCode = intNewCode + 1;
					  //进行补零 操作
					  for(int i=0;i<4 - String.valueOf(fCode).length();i++) {
						  finalCode = finalCode + "0";
					  }
					  
					  finalCode = finalCode + fCode;
				  }
				   
			   }

			   module.setCode(finalCode);
	           module.setCreater((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
	           module.setCreateDate(new Date());
	           
	            moduleDao.save(module);
	           
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException("模块保存异常："+e.getMessage());
			}
	}

	//删除模块
	@Override
	public void deleteModule(String[] codes) {
		// TODO Auto-generated method stub
		 try {
				
             StringBuffer hql = new StringBuffer();
			 
			 hql.append("delete from  Module  where ");
		    		    
			 for(int i=0;i<codes.length;i++) {   	 
		    	 hql.append( i == 0 ? " code like '"+codes[i]+"%'" : " or code like '"+codes[i]+"%'");
		     }
			 
			 roleDao.bulkUpdate(hql.toString(),null);
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("删除模块异常："+e.getMessage());
		}
	}
	
	//根据模块的code获取第三级子模块信息
		@Override
		public List<Module> loadThirdModule(String code) {
			 try {
					
	            String hql = "from Module where code like ? and length(code) = ? and delFlag = '1'";
				 
	            return  (List<Module>) popedomDao.find(hql, new Object[] {code+"%",code.length()+4});
				
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException("模块信息加载异常："+e.getMessage());
			}
		}

		//根据角色id以及二级模块code获取已绑定的第三级模块信息
		@Override
		public List<String> findOperasByRoleIdAndCode(String code, Long id) {
			// TODO Auto-generated method stub
			try {
				
	            String hql = "select  opera.code from  Popedom  where module.code = ? and role.id = ? ";
				return (List<String>) popedomDao.find(hql,new Object[] {code,id});
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException("模块信息加载异常："+e.getMessage());
			}
		}

		
		//绑定权限
		@Override
		public void bindOperas(String code, Long id, String codes) {
			// TODO Auto-generated method stub
			try {
				
				
				//根据角色的id以及二级模块的code将之前绑定所绑定的权限信息都删除
				popedomDao.deletePopedom(code,id);
				if(StringUtils.isNotEmpty(codes)) {
					
					Module m = new Module();
					m.setCode(code);
					
					Role r = new Role();
					r.setId(id);
					
					
					//根据前台传入的角色的id以及二级模块的code以及需要绑定的三级模块的code（操作），将数据添加至权限表
					String[] operaCodes = codes.split(",");
					for(String operaCode : operaCodes) {
						Popedom popedom = new Popedom();
						//设置创建时间
						popedom.setCreateDate(new Date());
						//设置创建人
						popedom.setCreater((User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER));
					    //指定角色信息
						popedom.setRole(r);
						//指定二级模块信息
						popedom.setModule(m);
						
						
						Module module = new Module();
						module.setCode(operaCode);
						//指定操作信息
						popedom.setOpera(module);
						
						popedomDao.save(popedom);
					}
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException("权限信息绑定异常："+e.getMessage());
			}
			
		}

		//1、根据用户账号查询权限信息，控制左侧菜单栏的相关菜单的显示与隐藏
		@Override
		public Map<Module, List<Module>> findLeftMenuOperas() {
			// TODO Auto-generated method stub
			try {
				
				//获取session中用户信息
				User user = (User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER);
				
				//查询权限表获取用户拥有的二级模块的code的信息     00010001    00010002    00020001  00020002
				List<String> secondCodes = popedomDao.findLeftMenuOperas(user.getUserId());
				
				if(secondCodes!=null && secondCodes.size() > 0) {
					 //创建map集合用于存放一级和二级模块
					 Map<Module,List<Module>>  maps = new LinkedHashMap<>();
					 
					 List<Module> list = null;
					 
					 for(String code : secondCodes) {
						 
						 //根据二级模块code获取对应的一级模块code
						 String firstCode = code.substring(0, code.length() - 4);
						 
						 //根据一级模块的code获取一级模块信息
						 Module module = moduleDao.get(Module.class, firstCode);
						 

						 //如果一级模块在map集合中不存在  则将一级模块存放在map集合
						 if(!maps.containsKey(module)) {
							 list = new ArrayList<>();

							 //将一级模块存放在map集合中
							 maps.put(module, list);
						 }
						 
						 //查询二级模块信息并将二级模块信息存放在  map集合中
						 Module secondModule = moduleDao.get(Module.class, code);
						 list.add(secondModule);
						 
						 
					 }
					 
					 return maps;

				}else {
					return null;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("用户权限信息加载异常："+e.getMessage());
			}
		}

		//2、根据用户账号查询权限信息，用于控制页面中按钮的显示与隐藏   user:addUser   user:deleteUser
		@Override
		public List<String> findUserOperasByUserId() {
			// TODO Auto-generated method stub
			try {
				//获取session中用户信息
				User user = (User)ConstantUtils.getRequest().getSession().getAttribute(ConstantUtils.SESSION_USER);
				
				List<String>  operas = new ArrayList<>();
				// 000100010001    000100010002   == >  user:addUser    user:deleteUser
				List<String> codes = popedomDao.findUserOperasByUserId(user.getUserId());
				if(codes != null  && codes.size()>0) {
					for(String code : codes) {
						Module module = moduleDao.get(Module.class, code);
						operas.add(module.getUrl());
					}
				}
				return operas;
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException("用户权限信息加载异常："+e.getMessage());
			}
		}


	
}
