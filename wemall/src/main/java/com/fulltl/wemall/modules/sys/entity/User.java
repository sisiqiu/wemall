/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.supcan.annotation.treelist.cols.SupCol;
import com.fulltl.wemall.common.utils.Collections3;
import com.fulltl.wemall.common.utils.excel.annotation.ExcelField;
import com.fulltl.wemall.common.utils.excel.fieldtype.RoleListType;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * 用户Entity
 * @author ThinkGem
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属公司
	private Office office;	// 归属部门
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像

	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码
	
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	
	private Role role;	// 根据角色查询用户条件
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	
	private String sex; //性别
	private String userCategory; //用户类别（patient--患者，doctor--医生）
	private String platformUserId; //第三方用户id
	private String province; //省份
	private String city; //城市
	private Date birthday; //生日
	private String industry; //行业
	private String profession; //职业
	private String realName; //真实姓名
	private String status; //用户状态；0--邮箱未验证，1--邮箱已验证
	private String emailValidateCode; //邮箱验证码

	private String sid;//存储用户对应的sid
	
	public User() {
		super();
		this.loginFlag = Global.YES;
	}
	
	public User(String id){
		super(id);
	}

	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public User(Role role){
		super();
		this.role = role;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	@SupCol(isUnique="true", isHide="true")
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}

	@JsonIgnore
	@NotNull(message="归属公司不能为空")
	@ExcelField(title="归属公司", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@JsonIgnore
	@NotNull(message="归属部门不能为空")
	@ExcelField(title="归属部门", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=0, max=100, message="工号长度必须介于 0 和 100 之间")
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@ExcelField(title="创建时间", type=0, align=1, sort=90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JsonIgnore
	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPlatformUserId() {
		return platformUserId;
	}

	public void setPlatformUserId(String platformUserId) {
		this.platformUserId = platformUserId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmailValidateCode() {
		return emailValidateCode;
	}

	public void setEmailValidateCode(String emailValidateCode) {
		this.emailValidateCode = emailValidateCode;
	}
	
	public String getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ",");
	}
	
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@JsonIgnore
	public List<String> getUserCategoryList() {
		List<String> userCategoryList = Lists.newArrayList();
		String userCategoryStr = this.getUserCategory();
		if(StringUtils.isNotBlank(userCategoryStr)) {
			userCategoryList = Arrays.asList(userCategoryStr.split(","));
		}
		return userCategoryList;
	}
	
	public void setUserCategoryList(List<String> userCategoryList) {
		String userCategoryStr = Collections3.convertToString(userCategoryList, ",");
		this.setUserCategory(userCategoryStr);
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@Override
	public String toString() {
		return id;
	}
	
	/**
	 * 根据手机号初始快速初始化用户对象
	 * @param mobile 手机号
	 * @param password 未加密的密码
	 * @param remarks 备注信息
	 * @param roleIdStrs 角色id列表
	 */
	public void quickInitBy(String mobile, String password, String remarks, String[] roleIdStrs) {
		List<String> roleIdList = this.getRoleIdList();
		if(roleIdList == null || roleIdList.isEmpty()) {
			roleIdList = new ArrayList<String>();
			roleIdList.add("2");
		}
		
		this.password = SystemService.entryptPassword(password);
		this.setCompany(new Office("1"));
		//预设普通用户
		this.setOffice(new Office("4"));
		this.loginName = mobile;
		this.name = mobile;
		this.setCreateBy(new User("1"));
		this.setUpdateBy(new User("1"));
		this.loginFlag = "1";
		this.setRoleIdList(roleIdList);
		this.mobile = mobile;
		this.remarks = remarks;
		if(roleIdStrs != null) {
			List<String> roleList = Arrays.asList(roleIdStrs);
			this.setRoleIdList(roleList);
			if(roleList.contains("3")) {
				//是医生
				this.setOffice(new Office("3"));
			}
		}
	}
	
	/**
	 * 根据登陆名初始快速初始化用户对象
	 * @param mobile 手机号
	 * @param password 未加密的密码
	 * @param remarks 备注信息
	 * @param roleIdStrs 角色id列表
	 */
	public void quickInitByLoginName(String loginName, String name, String password, String remarks, String[] roleIdStrs) {
		List<String> roleIdList = this.getRoleIdList();
		if(roleIdList == null || roleIdList.isEmpty()) {
			roleIdList = new ArrayList<String>();
			roleIdList.add("2");
		}
		
		this.password = SystemService.entryptPassword(password);
		this.setCompany(new Office("1"));
		//预设普通用户
		this.setOffice(new Office("4"));
		this.loginName = loginName;
		this.name = name;
		this.setCreateBy(new User("1"));
		this.setUpdateBy(new User("1"));
		this.loginFlag = "1";
		this.setRoleIdList(roleIdList);
		this.mobile = "";
		this.remarks = remarks;
		if(roleIdStrs != null) {
			List<String> roleList = Arrays.asList(roleIdStrs);
			this.setRoleIdList(roleList);
			if(roleList.contains("3")) {
				//是医生
				this.setOffice(new Office("3"));
			}
		}
	}
	
	public Map<String, Object> getSmallUser() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", this.getId());
		map.put("name", this.getName());
		map.put("realName", this.getRealName());
		map.put("photo", StringUtils.isBlank(this.getPhoto()) ? Global.getConfig("userDefaultPhoto") : this.getPhoto());
		map.put("sex", this.getSex());
		map.put("mobile", this.getMobile());
		return map;
	}
}