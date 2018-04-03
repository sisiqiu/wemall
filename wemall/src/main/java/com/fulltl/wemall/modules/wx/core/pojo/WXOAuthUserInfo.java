package com.fulltl.wemall.modules.wx.core.pojo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fulltl.wemall.common.persistence.CanFormatToSysUser;
import com.fulltl.wemall.modules.sys.entity.Office;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * 微信用户信息类
 * @author Administrator
 *
 */
public class WXOAuthUserInfo implements CanFormatToSysUser {
	private String openid; //用户的唯一标识
	private String nickname; //用户昵称
	private Integer sex; //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 
	private String language; //
	private String country; //国家，如中国为CN
	private String province; //用户个人资料填写的省份
	private String city; //普通用户个人资料填写的城市
	private String headimgurl; //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private List<String> privilege; //用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
    
    private String fromPlatForm; //获取信息的平台途径；1--微信公众号；2--网页第三方登陆。
    
    public WXOAuthUserInfo() {}
	
	public WXOAuthUserInfo(String openid) {
		this.openid = openid;
	}
    
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public List<String> getPrivilege() {
		return privilege;
	}
	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}
	
	public String getFromPlatForm() {
		return fromPlatForm;
	}
	
	/**
	 * 设置获取信息的平台途径。1--微信公众号；2--网页第三方登陆。
	 * @param fromPlatForm
	 */
	public void setFromPlatForm(String fromPlatForm) {
		this.fromPlatForm = fromPlatForm;
	}
	
	/**
	 * 根据微信用户对象转系统用户对象。
	 * @return
	 */
	@Override
	public User toSysUser() throws Exception {
		User u = new User();
		u.setLoginName(openid);
		u.setName(StringUtils.isBlank(nickname) ? openid : nickname);
		u.setPhoto(headimgurl);
		if(StringUtils.isBlank(openid)) {
			throw new Exception("微信用户的openid不能为空！");
		}
		u.setPlatformUserId(openid);
		/*u.setProvince(province);
		u.setCity(city);*/
		if(sex.equals("0")) {
			u.setSex(null);
		} else {
			u.setSex(sex.toString());
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
        //获取信息的平台途径；1--微信公众号；2--网页第三方登陆。
        if("1".equals(fromPlatForm)) {
        	u.setRemarks("微信公众号用户");
        } else if("2".equals(fromPlatForm)) {
        	u.setRemarks("微信网页用户");
        } else {
        	u.setRemarks("微信用户");
        }
		return u;
	}
	
	/**
	 * 根据map构造微信用户对象WXOAuthUserInfo
	 * @param map
	 * @return
	 */
	/*public static WXOAuthUserInfo valueOf(Map<String, String> map) {
		WXOAuthUserInfo userInfo = new WXOAuthUserInfo();
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
	}*/
    
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("微信用户：").append("openid=").append(openid)
				.append("; nickname=").append(nickname)
				.append("; headimgurl=").append(headimgurl);
		return strBuilder.toString();
	}
}
