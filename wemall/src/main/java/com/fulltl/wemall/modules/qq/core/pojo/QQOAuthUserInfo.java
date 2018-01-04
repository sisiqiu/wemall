package com.fulltl.wemall.modules.qq.core.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fulltl.wemall.common.persistence.CanFormatToSysUser;
import com.fulltl.wemall.modules.sys.entity.Office;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * qq用户对象
 * @author Administrator
 *
 */
public class QQOAuthUserInfo implements CanFormatToSysUser {
	private String openid; //qq用户openid
	private String nickname; //用户昵称
	private String province; //省会
	private String city; //城市
	private String figureurl; //大小为30×30像素的QQ空间头像URL。
	private String figureurl_1; //大小为50×50像素的QQ空间头像URL。
	private String figureurl_2; //大小为100×100像素的QQ空间头像URL。
	private String figureurl_qq_1; //大小为40×40像素的QQ头像URL。
	private String figureurl_qq_2; //大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100×100的头像，但40×40像素则是一定会有。 
	private String gender; //性别。 如果获取不到则默认返回”男” 
	private String is_yellow_vip; //标识用户是否为黄钻用户（0：不是；1：是）。 
	private String vip; //标识用户是否为黄钻用户（0：不是；1：是） 
	private String yellow_vip_level; //黄钻等级 
	private String level; //黄钻等级 
	private String is_yellow_year_vip; //标识是否为年费黄钻用户（0：不是； 1：是）
	
	public QQOAuthUserInfo() {}
	
	public QQOAuthUserInfo(String openid) {
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

	public String getFigureurl() {
		return figureurl;
	}

	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	public String getFigureurl_1() {
		return figureurl_1;
	}

	public void setFigureurl_1(String figureurl_1) {
		this.figureurl_1 = figureurl_1;
	}

	public String getFigureurl_2() {
		return figureurl_2;
	}

	public void setFigureurl_2(String figureurl_2) {
		this.figureurl_2 = figureurl_2;
	}

	public String getFigureurl_qq_1() {
		return figureurl_qq_1;
	}

	public void setFigureurl_qq_1(String figureurl_qq_1) {
		this.figureurl_qq_1 = figureurl_qq_1;
	}

	public String getFigureurl_qq_2() {
		return figureurl_qq_2;
	}

	public void setFigureurl_qq_2(String figureurl_qq_2) {
		this.figureurl_qq_2 = figureurl_qq_2;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIs_yellow_vip() {
		return is_yellow_vip;
	}

	public void setIs_yellow_vip(String is_yellow_vip) {
		this.is_yellow_vip = is_yellow_vip;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getYellow_vip_level() {
		return yellow_vip_level;
	}

	public void setYellow_vip_level(String yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}

	public void setIs_yellow_year_vip(String is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
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

	/**
	 * 根据QQ用户对象转系统用户对象。
	 * @return
	 * @throws Exception 
	 */
	@Override
	public User toSysUser() throws Exception {
		User u = new User();
		u.setLoginName(openid);
		u.setName(StringUtils.isBlank(nickname) ? openid : nickname);
		u.setPhoto(figureurl_qq_1);
		if(StringUtils.isBlank(openid)) {
			throw new Exception("QQ用户的openid不能为空！");
		}
		u.setPlatformUserId(openid);
		u.setProvince(province);
		u.setCity(city);
		if("男".equals(gender)) {
			u.setSex("1");
		} else if("女".equals(gender)) {
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
        u.setRemarks("QQ用户");
		return u;
	}
	
	/**
	 * 根据map构造QQ用户对象AlipayOAuthUserInfo
	 * @param map
	 * @return
	 */
	public static QQOAuthUserInfo valueOf(Map<String, String> map) {
		QQOAuthUserInfo userInfo = new QQOAuthUserInfo();
		userInfo.setOpenid(map.get("openid"));
		userInfo.setNickname(map.get("nickname"));
		userInfo.setProvince(map.get("province"));
		userInfo.setCity(map.get("city"));
		userInfo.setFigureurl(map.get("figureurl"));
		userInfo.setFigureurl_1(map.get("figureurl_1"));
		userInfo.setFigureurl_2(map.get("figureurl_2"));
		userInfo.setFigureurl_qq_1(map.get("figureurl_qq_1"));
		userInfo.setFigureurl_qq_2(map.get("figureurl_qq_2"));
		userInfo.setGender(map.get("gender"));
		userInfo.setIs_yellow_vip(map.get("is_yellow_vip"));
		userInfo.setVip(map.get("vip"));
		userInfo.setYellow_vip_level(map.get("yellow_vip_level"));
		userInfo.setLevel(map.get("level"));
		userInfo.setIs_yellow_year_vip(map.get("is_yellow_year_vip"));
		
		return userInfo;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("QQ用户：").append("openid=").append(openid)
				.append("; nickname=").append(nickname)
				.append("; figureurl_qq_1=").append(figureurl_qq_1);
		return strBuilder.toString();
	}
}
