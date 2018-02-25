/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.service;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.CanFormatToSysUser;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.security.Cryptos;
import com.fulltl.wemall.common.security.Digests;
import com.fulltl.wemall.common.security.shiro.session.SessionDAO;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.service.ServiceException;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.Encodes;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.Servlets;
import com.fulltl.wemall.modules.alipay.core.pojo.AlipayOAuthUserInfo;
import com.fulltl.wemall.modules.qq.core.pojo.QQOAuthUserInfo;
import com.fulltl.wemall.modules.sys.dao.MenuDao;
import com.fulltl.wemall.modules.sys.dao.RoleDao;
import com.fulltl.wemall.modules.sys.dao.UserDao;
import com.fulltl.wemall.modules.sys.entity.Menu;
import com.fulltl.wemall.modules.sys.entity.Office;
import com.fulltl.wemall.modules.sys.entity.Role;
import com.fulltl.wemall.modules.sys.entity.SlAppsession;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.SystemAuthorizingRealm;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.utils.LogUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wx.core.pojo.WXOAuthUserInfo;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * @author ThinkGem
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String USERTYPE_DOCTOR = "doctor";
	public static final String USERTYPE_PATIENT = "patient";
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;
	@Autowired
	private SlAppsessionService slAppsessionService;
	
	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	@Autowired
	private IdentityService identityService;

	//-- User Service --//
	
	/**
	 * 获取用户
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 无分页查询人员列表
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}
	
	/**
	 * 根据userId列表查询用户
	 * @param asList
	 * @return
	 */
	public List<User> findUserByUserIds(List<String> userIdList) {
		List<User> list = userDao.findUserByUserIds(userIdList);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>)CacheUtils.get(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null){
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}
	
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())){
			user.preInsert();
			user.setStatus("0"); //设置为邮箱未验证
			userDao.insert(user);
		}else{
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null){
				CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			if(!Objects.equal(oldUser.getEmail(), user.getEmail())) {
				user.setStatus("0"); //设置为邮箱未验证
			}
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())){
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}else{
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 将当前用户同步到Activiti
			saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
//			// 清除权限缓存
//			systemRealm.clearAllCachedAuthorizationInfo();
		}
	}
	
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 同步到Activiti
		deleteActivitiUser(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		//删除用户对应的appsession
		slAppsessionService.deleteByUserId(user.getId());
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updateMobileById(User user, String oldMobile) {
		userDao.updateMobileAndLoginNameById(user);
		// 清除用户缓存
		user.setLoginName(oldMobile);
		UserUtils.clearCache(user);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}
	
	/**
	 * 根据角色id查询用户id列表
	 * @param roleId
	 * @return
	 */
	public List<String> getUserIdsByRoleIds(List<String> roleIds) {
		return userDao.getUserIdsByRoleIds(roleIds);
	}
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}
	
	/**
	 * 获得活动会话
	 * @return
	 */
	public Collection<Session> getActiveSessions(){
		return sessionDao.getActiveSessions(false);
	}
	
	//-- Role Service --//
	
	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}
	
	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}
	
	public List<Role> findRole(Role role){
		return roleDao.findList(role);
	}
	
	public List<Role> findAllRole(){
		return UserUtils.getRoleList();
	}
	
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())){
			role.preInsert();
			roleDao.insert(role);
			// 同步到Activiti
			saveActivitiGroup(role);
		}else{
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0){
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0){
			roleDao.insertRoleOffice(role);
		}
		// 同步到Activiti
		saveActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 同步到Activiti
		deleteActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles){
			if (e.getId().equals(role.getId())){
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null){
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	//-- Menu Service --//
	
	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu(){
		return UserUtils.getMenuList();
	}
	
	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {
		
		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));
		
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds(); 
		
		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds()+menu.getParent().getId()+",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())){
			menu.preInsert();
			menuDao.insert(menu);
		}else{
			menu.preUpdate();
			menuDao.update(menu);
		}
		
		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%,"+menu.getId()+",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
//		// 清除权限缓存
//		systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}
	
	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage(){
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 "+Global.getConfig("productName")+"  - Powered By http://jeesite.com\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}
	
	///////////////// Synchronized to the Activiti //////////////////
	
	// 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;
	public void afterPropertiesSet() throws Exception {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if (isSynActivitiIndetity){
			isSynActivitiIndetity = false;
	        // 同步角色数据
			List<Group> groupList = identityService.createGroupQuery().list();
			if (groupList.size() == 0){
			 	Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
			 	while(roles.hasNext()) {
			 		Role role = roles.next();
			 		saveActivitiGroup(role);
			 	}
			}
		 	// 同步用户数据
			List<org.activiti.engine.identity.User> userList = identityService.createUserQuery().list();
			if (userList.size() == 0){
			 	Iterator<User> users = userDao.findAllList(new User()).iterator();
			 	while(users.hasNext()) {
			 		saveActivitiUser(users.next());
			 	}
			}
		}
	}
	
	private void saveActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String groupId = role.getEnname();
		
		// 如果修改了英文名，则删除原Activiti角色
		if (StringUtils.isNotBlank(role.getOldEnname()) && !role.getOldEnname().equals(role.getEnname())){
			identityService.deleteGroup(role.getOldEnname());
		}
		
		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		if (group == null) {
			group = identityService.newGroup(groupId);
		}
		group.setName(role.getName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);
		
		// 删除用户与用户组关系
		List<org.activiti.engine.identity.User> activitiUserList = identityService.createUserQuery().memberOfGroup(groupId).list();
		for (org.activiti.engine.identity.User activitiUser : activitiUserList){
			identityService.deleteMembership(activitiUser.getId(), groupId);
		}

		// 创建用户与用户组关系
		List<User> userList = findUser(new User(new Role(role.getId())));
		for (User e : userList){
			String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
			// 如果该用户不存在，则创建一个
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
			if (activitiUser == null){
				activitiUser = identityService.newUser(userId);
				activitiUser.setFirstName(e.getName());
				activitiUser.setLastName(StringUtils.EMPTY);
				activitiUser.setEmail(e.getEmail());
				activitiUser.setPassword(StringUtils.EMPTY);
				identityService.saveUser(activitiUser);
			}
			identityService.createMembership(userId, groupId);
		}
	}

	public void deleteActivitiGroup(Role role) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(role!=null) {
			String groupId = role.getEnname();
			identityService.deleteGroup(groupId);
		}
	}

	private void saveActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			activitiUser = identityService.newUser(userId);
		}
		activitiUser.setFirstName(user.getName());
		activitiUser.setLastName(StringUtils.EMPTY);
		activitiUser.setEmail(user.getEmail());
		activitiUser.setPassword(StringUtils.EMPTY);
		identityService.saveUser(activitiUser);
		
		// 删除用户与用户组关系
		List<Group> activitiGroups = identityService.createGroupQuery().groupMember(userId).list();
		for (Group group : activitiGroups) {
			identityService.deleteMembership(userId, group.getId());
		}
		// 创建用户与用户组关系
		for (Role role : user.getRoleList()) {
	 		String groupId = role.getEnname();
	 		// 如果该用户组不存在，则创建一个
		 	Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            if(group == null) {
	            group = identityService.newGroup(groupId);
	            group.setName(role.getName());
	            group.setType(role.getRoleType());
	            identityService.saveGroup(group);
            }
			identityService.createMembership(userId, role.getEnname());
		}
	}

	private void deleteActivitiUser(User user) {
		if (!Global.isSynActivitiIndetity()){
			return;
		}
		if(user!=null) {
			String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
			identityService.deleteUser(userId);
		}
	}
	
	///////////////// Synchronized to the Activiti end //////////////////
	
	
	///////////////// write by ldk begin ////////////////////////////////
	
	/**
	 * 根据手机号和设定的密码字符串，快速新建用户。
	 * @param user 需要设置mobile，未加密的password，remarks，roleIdList
	 * @return 新建的用户对象。
	 */
	/*@Transactional(readOnly = false)
	public User quickSaveUserByMobile(User user) {
		List<String> roleIdList = user.getRoleIdList();
		if(roleIdList == null || roleIdList.isEmpty()) {
			roleIdList = new ArrayList<String>();
			roleIdList.add("2");
		}
        // user.setId(IdGen.uuid());
        user.setUserType("6");
        user.setPassword(SystemService.entryptPassword(user.getPassword()));
        user.setCompany(new Office("1"));
        user.setOffice(new Office("1"));
        user.setLoginName(user.getMobile());
        user.setName(user.getMobile());
        user.setNo("0000001");
        //user.setEmail("123456@qq.com");
        //user.setPhone("85003121");
        user.setCreateBy(new User("1"));
        user.setUpdateBy(new User("1"));
        user.setLoginFlag("1");
        user.setDelFlag("0");
        user.setRoleIdList(roleIdList);
        this.saveUser(user);
        System.err.println("快速添加的用户id：" + user.getId());
        
        //短信提醒用户其密码的值
        
        return user;
	}*/
	
	/**
	 * 快速根据手机号获取其用户对象。若不存在则新建用户返回。
	 * @param mobile
	 * @return
	 */
	@Transactional(readOnly = false)
	public User quickGetUserByMobile(String mobile, String remarks) {
		User user = this.getUserByLoginName(mobile);
		if(user != null) {
			return user;
		} else {
			User u = new User();
			u.quickInitBy(mobile, "123456", remarks, null);
			this.saveUser(u);
			return u;
			//return this.quickSaveUserByMobile(u);
		}
	}
	
	/**
	 * 快速根据手机号获取其用户对象，若不存在则新建用户，并标注用户为“微信公众号绑定用户”
	 * @param mobile
	 * @return
	 */
	@Transactional(readOnly = false)
	public User quickGetUserByMobileForWX(String mobile) {
		return quickGetUserByMobile(mobile, "微信公众号绑定用户");
	}
	
	/**
	 * 快速根据手机号获取其用户对象，若不存在则新建用户，并标注用户为“自注册用户”
	 * @param mobile
	 * @return
	 */
	@Transactional(readOnly = false)
	public User quickGetUserByMobileForWeb(String mobile) {
		return quickGetUserByMobile(mobile, "自注册用户");
	}
	
	/**
	 * 根据可以转换为系统用户的其他用户类，添加系统用户，并返回新添加的系统用户对象
	 * @param user 可以转换为系统用户的其他用户类，如微信用户类，支付宝用户类，qq用户类等。
	 * @return 新添加的系统用户对象
	 */
	@Transactional(readOnly = false)
	public User quickGetUserBy(CanFormatToSysUser user) {
		User sysUserFromFormat = null;
		try {
			sysUserFromFormat = user.toSysUser();
		} catch (Exception e) {
			logger.error("第三方用户信息转为系统用户错误：" + e.getMessage());
		}
		User sysUserInDB = null; //定义从数据库中查到的系统用户
		//初始先查询用户是否已经第三方登陆过，在系统中已有用户，若有，则直接取出返回。
		if(sysUserFromFormat != null) {
			if(user instanceof WXOAuthUserInfo) {
				//微信用户
				sysUserInDB = UserUtils.getByPlatformUserId(sysUserFromFormat.getPlatformUserId(), sysUserFromFormat.getRemarks());
			} else if(user instanceof AlipayOAuthUserInfo) {
				//支付宝用户
				sysUserInDB = UserUtils.getByPlatformUserId(sysUserFromFormat.getPlatformUserId(), sysUserFromFormat.getRemarks());
			} else if(user instanceof QQOAuthUserInfo) {
				//QQ用户
				sysUserInDB = UserUtils.getByPlatformUserId(sysUserFromFormat.getPlatformUserId(), sysUserFromFormat.getRemarks());
			}
		}
		
		if(sysUserInDB == null) {
			//若没有查到，说明是初次第三方登陆，进行系统用户添加。
			this.saveUser(sysUserFromFormat);
			return sysUserFromFormat;
		} else {
			//若查到了，则检查是否需要更新，如果需要，执行更新。更新与否，主要检查省市，性别字段。
			boolean needUpdate = false;
			if(StringUtils.isBlank(sysUserInDB.getSex()) && !StringUtils.isBlank(sysUserFromFormat.getSex())) {
				//如果DB中没有性别，且传入用户对象中有，需要更新
				sysUserInDB.setSex(sysUserFromFormat.getSex());
				needUpdate = true;
			}
			/*if(StringUtils.isBlank(sysUserInDB.getCity()) && !StringUtils.isBlank(sysUserFromFormat.getCity())) {
				//如果DB中没有城市，且传入用户对象中有，需要更新
				sysUserInDB.setCity(sysUserFromFormat.getCity());
				needUpdate = true;
			}
			if(StringUtils.isBlank(sysUserInDB.getProvince()) && !StringUtils.isBlank(sysUserFromFormat.getProvince())) {
				//如果DB中没有省，且传入用户对象中有，需要更新
				sysUserInDB.setProvince(sysUserFromFormat.getProvince());
				needUpdate = true;
			}*/
			if(sysUserInDB.getName().equals(sysUserInDB.getPlatformUserId()) &&
					!sysUserFromFormat.getName().equals(sysUserInDB.getPlatformUserId())) {
				//如果DB中名称和第三方平台id一样，且传入用户对象中有的名称和第三方平台id不同，需要更新
				sysUserInDB.setName(sysUserFromFormat.getName());
				needUpdate = true;
			}
			if(needUpdate) {
				this.saveUser(sysUserInDB);
			}
			return sysUserInDB;
		}
	}
	
	/**
	 * 执行用户登陆操作。
	 * @param u
	 * @param rememberMe
	 * @return
	 * @throws AuthenticationException
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> loginByUser(User u, boolean rememberMe) throws AuthenticationException {
		Map<String, Object> result = Maps.newHashMap();
		if(StringUtils.isNotBlank(u.getId()) && 
				StringUtils.isNotBlank(u.getLoginName()) &&
				StringUtils.isNotBlank(u.getPassword())) {
			UsernamePasswordToken userToken = new UsernamePasswordToken();
			// 自动登录
			userToken.setUsername(u.getLoginName());
			userToken.setPassword(u.getPassword().toCharArray());
			userToken.setRememberMe(rememberMe);
			// 短信验证码登陆，跳过密码对比校验
			userToken.setLogin_type("SMSCode");
			UserUtils.getSubject().login(userToken);
			result.put("ret", "0");
			result.put("retMsg", "登陆成功！");
		} else {
			result.put("ret", "-1");
			result.put("retMsg", "登陆失败！失败原因：用户id，登陆名或密码不存在！");
		}
		return result;
	}
	
	/**
	 * 执行用户登陆操作。并传入是否更新session
	 * @param u
	 * @param rememberMe
	 * @param updateSession
	 * @return
	 * @throws AuthenticationException
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> loginByUser(User u, boolean rememberMe, boolean updateSession) throws AuthenticationException {
		Map<String, Object> result = Maps.newHashMap();
		result = loginByUser(u, rememberMe);
		if(!"0".equals(result.get("ret"))) return result;
		if(updateSession) addOrUpdateAppSession(result, u);
		return result;
	}
	
	/**
	 * 添加或更新appsession相关数据
	 * @param retMap 结果map
	 * @param user 需要id字段和loginName字段
	 */
	@Transactional(readOnly = false)
	public void addOrUpdateAppSession(Map<String, Object> retMap, User user) {
		//获取sessionId，构造access_token
		String sid = UserUtils.getSession().getId().toString();
        String access_token = Cryptos.generateAesKeyString();
        retMap.put("sid",sid);
        retMap.put("access_token", access_token);
        
        //查询配置的session管理方式是哪种（OUOS(一个用户一个session)，还是OUMS(一个用户多个session)）
        String sessionMgrMethod = Global.getConfig("sessionMgrMethod");
        SlAppsession slAppsession = new SlAppsession(user); 
        if("OUMS".equals(sessionMgrMethod)) {
        	//如果是OUMS(一个用户多个session)：
        	//查询appsession表中是否有该sid的数据，如果有，执行更新，如果没有执行插入
        	SlAppsession oldAppsession = slAppsessionService.get(sid);
        	if(oldAppsession == null) {
        		//标记为插入
        		slAppsession.setIsNewRecord(true);
        	} else {
        		//更新改appsession
        		oldAppsession.setAccessToken(access_token);
        		oldAppsession.setLoginName(user.getLoginName());
        		oldAppsession.setAccessTokenExpiry(Long.parseLong(Global.getConfig("session.sessionTimeout")));//默认30分钟
        		slAppsessionService.save(oldAppsession);
        		return;
        	}
        } else {
        	//如果是OUOS(一个用户一个session)：
        	//查询appsession表中是否有该用户的SlAppsession，
            //若不控制更新，只插入的话，需要查询当前sid有没有记录，没有执行插入，有就更新。
            List<SlAppsession> sessionList = slAppsessionService.findList(slAppsession);
            if(sessionList.size() == 1) {
            	slAppsession = sessionList.get(0);
            } else if(sessionList.size() > 1) {
            	//如果对应该用户的appsession个数大于1个，删除所有该用户的appsession。防止之后更新时主键冲突
            	slAppsessionService.deleteByUserId(user.getId());
            	//标记为插入
            	slAppsession.setIsNewRecord(true);
            } else {
            	//没有，标记为插入
            	slAppsession.setIsNewRecord(true);
            }
        }
    	
        slAppsession.setAccessToken(access_token);
        slAppsession.setLoginName(user.getLoginName());
        slAppsession.setSid(sid);
        slAppsession.setAccessTokenExpiry(Long.parseLong(Global.getConfig("session.sessionTimeout")));//默认30分钟
        //若有，更新数据库中的session信息。若没有，存入数据库。
    	if(slAppsession.getIsNewRecord()) {
    		slAppsessionService.save(slAppsession);
    	} else {
    		slAppsessionService.updateByUserId(slAppsession);
    	}
    	
    	//设置缓存中的用户的sid为此sid
    	UserUtils.getUser().setSid(sid);
	}
	
	/**
	 * 验证邮箱email
	 * @param email
	 * @param validateCode
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> validateEmail(String email, String validateCode) {
		Map<String, Object> result = Maps.newHashMap();
		User user = findByEmail(email);
		if(user != null) {
			if("1".equals(user.getStatus())) {
				result.put("ret", "412");
				result.put("retMsg", "用户已完成邮箱验证，请勿重复验证！");
				return result;
			}
			
			if(StringUtils.isNotBlank(validateCode) && validateCode.equals(user.getEmailValidateCode())) {
				user.setStatus("1");
				userDao.update(user);
				UserUtils.clearCache(user);
				result.put("ret", "200");
				result.put("retMsg", "邮箱验证成功！");
			} else {
				result.put("ret", "411");
				result.put("retMsg", "邮箱验证失败！");
			}
		} else {
			result.put("ret", "410");
			result.put("retMsg", "该用户不存在！");
		}
		return result;
	}
	
	/**
	 * 根据email查询用户
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		if(StringUtils.isBlank(email)) {
			return null;
		}
		return userDao.findByEmail(email);
	}
	
	/**
	 * 验证用户是否已经登陆
	 * @param user
	 * @return
	 */
	public Map<String, Object> checkCurrentUser(User user) {
		Map<String, Object> retMap = Maps.newHashMap();
    	if(StringUtils.isBlank(user.getId())) {
    		retMap.put("ret", "60015");
    		retMap.put("retMsg", "用户尚未登陆！请先登录！");
    		return retMap;
    	} else {
			retMap.put("ret", "0");
			retMap.put("retMsg", "用户已经登陆！");
			return retMap;
		}
	}
	
	/**
	 * 验证手机发送的短信验证码
	 * @param mobileNo
	 * @param verifyCode
	 * @param verifySerialNo
	 * @return
	 */
	public Map<String, Object> checkVerifyCode(String mobileNo, String verifyCode, String verifySerialNo) {
		Map<String, Object> retMap = Maps.newHashMap();
		SMSVerify sms = new SMSVerify();
        try {
			if ("F".equals(Global.getConfig("isSendSms")) || sms.checkVerifyCode(mobileNo, verifyCode, verifySerialNo, false).equals("0")) {
				retMap.put("ret", "0");
				retMap.put("retMsg", "短信验证码校验成功！");
				return retMap;
			} else {
				//验证不成功
				retMap.put("ret", "60002");
				retMap.put("retMsg", "短信验证码错误！请重试！");
				return retMap;
			}
		} catch (Exception e) {
			logger.error("短信验证码校验失败", e);
            retMap.put("ret", "-1");
            retMap.put("retMsg", "校验短信验证码出错！");
            return retMap;
		}
	}

	///////////////// write by ldk end //////////////////////////////////
	
	/**
	 * 获取用户（没有执行添加，有用户没有角色执行更新）。
	 * @param loginName
	 * @param name
	 * @param password
	 * @return
	 */
	@Transactional(readOnly = false)
	public User saveUserByTypeAndLoginName(String loginName, String name, String password) {
		User user = this.getUserByLoginName(loginName);
		if(user == null) {
			User u = new User();
			u.quickInitByLoginName(loginName, name, password, "", null);
			//u.setUserCategory("doctor");
			this.saveUser(u);
			return u;
		} else {
			return user;
		}
	}
}
