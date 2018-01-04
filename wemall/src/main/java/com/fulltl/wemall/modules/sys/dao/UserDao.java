/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fulltl.wemall.common.persistence.CrudDao;
import com.fulltl.wemall.common.persistence.annotation.MyBatisDao;
import com.fulltl.wemall.modules.sys.entity.User;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	public User getNotDel(String id);
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	   /**
     * 根据用户类别登录名称查询用户
     * @param loginName
     * @return
     */
    public User getByUserTypeLoginName(User user);
    /**
     * 根据openID查询用户
     * @param openID
     * @return
     */
    public User getByOpenIDLoginName(User user);

    /**
     * 根据手机号,用户类型
     * @param user
     * @return
     */
    public User getByMobile(User user);
	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新用户手机号和登陆名
	 * @param user
	 * @return
	 */
	public int updateMobileAndLoginNameById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

	/**
	 * 根据第三方用户id，和第三方平台标识查询用户
	 * @param platformUserId 第三方用户id
	 * @param string 第三方平台标识，此处根据remarks进行like查询
	 * @return
	 */
	public User findByPlatformUserId(@Param(value="platformUserId") String platformUserId, 
										@Param(value="remarks") String remarks);

	/**
	 * 根据email获取用户
	 * @param email
	 * @return
	 */
	public User findByEmail(String email);

	/**
	 * 根据用户id列表查询用户
	 * @param userIds
	 * @return
	 */
	public List<User> findUserByUserIds(List<String> userIdList);
	
	/**
	 * 根据角色id查询用户id列表
	 * @param roleId
	 * @return
	 */
	public List<String> getUserIdsByRoleIds(List<String> roleIds);
	
}
