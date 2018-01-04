/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wx.core.pojo.AccessToken;
import com.fulltl.wemall.modules.wx.core.pojo.Button;
import com.fulltl.wemall.modules.wx.core.pojo.FirstLevelButton;
import com.fulltl.wemall.modules.wx.core.pojo.Menu;
import com.fulltl.wemall.modules.wx.core.pojo.SecondLevelButton;
import com.fulltl.wemall.modules.wx.core.utils.accessToken.WXUtil;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;
import com.fulltl.wemall.modules.wx.service.WxServiceaccountService;
import com.fulltl.wemall.modules.wx.service.WxWechatMenuService;

/**
 * 微信后台控制器。用于后台微信核心管理部分的控制器，外界不可见。
 * 
 * @author ldks
 * @version 2017-10-11
 */
@Controller
@RequestMapping(value = "${adminPath}/wx/core")
public class WeiXinAdminController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WxWechatMenuService wxWechatMenuService;
	@Autowired
	private WxServiceaccountService wxServiceaccountService;

	/**
	 * 创建菜单
	 * 
	 * @param test
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("wx:core:createMenu")
	@RequestMapping(value = "createMenu")
	//@ResponseBody
	public String createMenu(RedirectAttributes redirectAttributes) {
		//获取所有服务号
		List<WxServiceaccount> wxSas = wxServiceaccountService.findList(new WxServiceaccount());
		//定义存储更新菜单结果消息的Map，key-服务号ID，value-结果代码
		Map<String, Integer> resultMsg = Maps.newHashMap(); 
		int result = 1; //结果值
		//循环所有服务号，对每个服务号进行菜单创建或更新，并将结果记录入
		for(WxServiceaccount sa: wxSas) {
			if(sa.getStatus().equals("0")) {
				//不可用的服务号直接跳过
				continue;
			}
			Menu menu = getWeiXinMenusBySaId(sa.getSaId());
			
			//从缓存中取accessToken
			AccessToken at = (AccessToken)CacheUtils.get("accessToken");
			if(StringUtils.isNotBlank(sa.getToken())) {
				//更新微信菜单列表 
				int resultCode = WXUtil.createMenu(menu, at.getToken()/*sa.getToken()*/);
				//将更新结果放入结果消息的Map中
				resultMsg.put(sa.getServiceId(), resultCode);
			}
		}
		
		//根据resultMsg生成更新菜单的结果提示
		StringBuffer strBuf = new StringBuffer();
		for(String key: resultMsg.keySet()) {
			int resultCode = resultMsg.get(key);
			// 判断菜单创建结果 
			if (0 == resultCode) {
				logger.info("服务号'" + key + "'的菜单创建成功！");
				strBuf.append("服务号'" + key + "'的菜单创建成功！<br/>");
			} else {
				logger.info("服务号'" + key + "'的菜单创建失败，错误码：" + resultCode); 
				strBuf.append("服务号'" + key + "'的菜单创建失败，错误码：" + resultCode + "<br/>");
			}
		}
		
		addMessage(redirectAttributes, strBuf.toString());
		
		return "redirect:" + adminPath + "/wx/wxWechatMenu";
		//return menu;
	}
	
	/**
	 * 构造菜单Menu
	 * 
	 * @return
	 */
	private Menu getWeiXinMenusBySaId(int saId) {
		// 取对应服务号的所有菜单。
		List<WxWechatMenu> wxMenus = wxWechatMenuService.findBySaId(saId);

		// 定义总菜单
		Menu menu = new Menu();
		// 定义一级菜单列表
		List<Button> firstLevelButtons = new ArrayList<Button>();

		for (WxWechatMenu wxMenu : wxMenus) {
			// 若wxMenu是一级菜单，初始化一级菜单对象，填充好其二级菜单，之后将一级菜单对象整个加入一级菜单列表中。
			if (wxMenu.getParent().getId().equals("0")) {
				
				FirstLevelButton firstLevelButton = new FirstLevelButton();
				firstLevelButton.setName(wxMenu.getName());
				
				// 从列表中筛出所有二级菜单，然后加入二级菜单列表secondLevelButtons中。
				List<SecondLevelButton> secondLevelButtons = new ArrayList<SecondLevelButton>();
				for (WxWechatMenu wxMenu_second : wxMenus) {
					//判断若是改一级菜单的子菜单，才构建二级菜单加入二级菜单列表。
					if(wxMenu_second.getParent().getId().equals(wxMenu.getId())) {
						SecondLevelButton secondLevelButton = new SecondLevelButton();
						secondLevelButton.setName(wxMenu_second.getName());
						secondLevelButton.setType(wxMenu_second.getType());
						secondLevelButton.setKey(wxMenu_second.getMenuKey());
						secondLevelButton.setUrl(wxMenu_second.getUrl());
						secondLevelButtons.add(secondLevelButton);
					}
				}
				//将二级菜单数组加入一级菜单中，至此一级菜单对象初始化完成
				firstLevelButton.setSub_button(secondLevelButtons);
				
				if(secondLevelButtons.size() == 0) {
					//若下面没有二级菜单，说明只有一级菜单，那填充其他相关属性。
					SecondLevelButton secondLevelButton = new SecondLevelButton();
					secondLevelButton.setName(wxMenu.getName());
					secondLevelButton.setType(wxMenu.getType());
					secondLevelButton.setKey(wxMenu.getMenuKey());
					secondLevelButton.setUrl(wxMenu.getUrl());
					firstLevelButtons.add(secondLevelButton);
				} else {
					//将一级菜单对象加入到一级菜单列表中
					firstLevelButtons.add(firstLevelButton);
				}
			}
		}

		// 往总菜单中加入所有一级菜单
		menu.setButton(firstLevelButtons);
		return menu;
	}
}