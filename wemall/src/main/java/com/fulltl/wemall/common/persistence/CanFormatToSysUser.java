package com.fulltl.wemall.common.persistence;

import com.fulltl.wemall.modules.sys.entity.User;

/**
 * 可以转换为系统用户。所有可以转换为系统用户的类，都应实现此接口。如微信用户类，支付宝用户类，qq用户类等。
 * @author Administrator
 *
 */
public interface CanFormatToSysUser {
	
	/**
	 * 根据自己用户类中的已有的字段，格式化生成一个系统用户类的对象。
	 * @return 生成的系统用户类对象
	 * @throws Exception 
	 */
	public User toSysUser() throws Exception;
}
