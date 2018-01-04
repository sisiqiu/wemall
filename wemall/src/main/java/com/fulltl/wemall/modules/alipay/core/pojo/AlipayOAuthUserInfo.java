package com.fulltl.wemall.modules.alipay.core.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fulltl.wemall.common.persistence.CanFormatToSysUser;
import com.fulltl.wemall.modules.sys.entity.Office;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * 支付宝用户对象
 * @author Administrator
 *
 */
public class AlipayOAuthUserInfo implements CanFormatToSysUser {
	private String user_id; //支付宝用户id
	private String avatar; //用户头像
	private String nick_name; //用户昵称
	private String province; //省份
	private String city; //城市
	private String gender; //用户性别，M为男性，F为女性
	private String user_type; //用户类型，1代表公司账户2代表个人账户 
	private String user_status; //用户状态，Q代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户 
	private String is_certified; //是否通过实名认证，	T是通过 F是没有实名认证 
	private String is_student_certified; //是否是学生，T是学生 F不是学生 
	
	public AlipayOAuthUserInfo() {}
	
	public AlipayOAuthUserInfo(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getIs_certified() {
		return is_certified;
	}
	public void setIs_certified(String is_certified) {
		this.is_certified = is_certified;
	}
	public String getIs_student_certified() {
		return is_student_certified;
	}
	public void setIs_student_certified(String is_student_certified) {
		this.is_student_certified = is_student_certified;
	}

	/**
	 * 根据支付宝用户对象转系统用户对象。
	 * @return
	 */
	@Override
	public User toSysUser() throws Exception {
		User u = new User();
		u.setLoginName(user_id);
		u.setName(StringUtils.isBlank(nick_name) ? user_id : nick_name);
		u.setPhoto(avatar);
		if(StringUtils.isBlank(user_id)) {
			throw new Exception("支付宝用户的user_id不能为空！");
		}
		u.setPlatformUserId(user_id);
		u.setProvince(province);
		u.setCity(city);
		//gender---M为男性，F为女性
		if("M".equals(gender)) {
			u.setSex("1");
		} else if("F".equals(gender)) {
			u.setSex("2");
		} else {
			u.setSex(null);
		}
		
		List<String> listRoleID = new ArrayList<String>();
        listRoleID.add("2");
        u.setRoleIdList(listRoleID);
        //u.setMobile(mobile);
        u.setUserType("6");
        u.setPassword(SystemService.entryptPassword("123456"));
        u.setCompany(new Office("1"));
        u.setOffice(new Office("1"));
        u.setNo("0000001");
        u.setEmail("123456@qq.com");
        u.setPhone("85003121");
        
        u.setCreateBy(new User("1"));
        u.setUpdateBy(new User("1"));
        u.setLoginFlag("1");
        u.setDelFlag("0");
        u.setRemarks("支付宝用户");
		return u;
	}
	
	/**
	 * 根据map构造支付宝用户对象AlipayOAuthUserInfo
	 * @param map
	 * @return
	 */
	public static AlipayOAuthUserInfo valueOf(Map<String, String> map) {
		AlipayOAuthUserInfo userInfo = new AlipayOAuthUserInfo();
		userInfo.setUser_id(map.get("user_id"));
		userInfo.setAvatar(map.get("avatar"));
		userInfo.setNick_name(map.get("nick_name"));
		userInfo.setProvince(map.get("province"));
		userInfo.setCity(map.get("city"));
		userInfo.setGender(map.get("gender"));
		userInfo.setUser_type(map.get("user_type"));
		userInfo.setUser_status(map.get("user_status"));
		userInfo.setIs_certified(map.get("is_certified"));
		userInfo.setIs_student_certified(map.get("is_student_certified"));
		return userInfo;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("支付宝用户：").append("user_id=").append(user_id)
				.append("; nick_name=").append(nick_name)
				.append("; avatar=").append(avatar);
		return strBuilder.toString();
	}
}
