package com.fulltl.wemall.modules.wx.core.inputHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.modules.wx.core.WXBeanFactory;
import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxUserInfo;
import com.fulltl.wemall.modules.wx.entity.WxWechatMenu;
import com.fulltl.wemall.modules.wx.service.WxServiceaccountService;
import com.fulltl.wemall.modules.wx.service.WxUserInfoService;
import com.fulltl.wemall.modules.wx.service.WxWechatMenuService;

/**
 * 事件输入处理类
 * @author Administrator
 *
 */
@Component
public class EventInputHandler implements WXInputHandler {
	@Autowired
	private WxWechatMenuService wxWechatMenuService;
	@Autowired
	private WxServiceaccountService wxServiceaccountService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private WXBeanFactory wxBeanFactory;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String handleWithReceive(ReceiveXmlEntity receiveXmlEntity) {
		String event = receiveXmlEntity.getEvent();
		String content = StringUtils.EMPTY;
		//订阅
    	if (event.equals("subscribe")) {
    		content = subscribe(receiveXmlEntity);
    	}
    	//取消订阅
    	else if (event.equals("unsubscribe")) {
    		content = unsubscribe(receiveXmlEntity);
    	}
    	//点击菜单
    	else if (event.equals("CLICK")) {
    		content = clickMenu(receiveXmlEntity);
    	}
		
		return content;
	}
	
	/**
	 * 关注事件
	 * @param receiveXmlEntity
	 * @return
	 */
	private String subscribe(ReceiveXmlEntity receiveXmlEntity) {
		String openId = receiveXmlEntity.getFromUserName();
		logger.info("----------------用户：openId=" + openId + " 已关注!-----------------");
		WxUserInfo wxUserInfo = wxUserInfoService.getWxUserInfoBy(receiveXmlEntity);
		wxUserInfoService.updateWXUserInfoBy(UserBehavior.FOCUS_ON, wxUserInfo);
		return null;
	}
	
	/**
	 * 取消关注事件
	 * @param receiveXmlEntity
	 * @return
	 */
	private String unsubscribe(ReceiveXmlEntity receiveXmlEntity) {
		String openId = receiveXmlEntity.getFromUserName();
		logger.info("----------------用户：openId=" + openId + " 取消关注!-----------------");
		WxUserInfo wxUserInfo = wxUserInfoService.getWxUserInfoBy(receiveXmlEntity);
		wxUserInfoService.updateWXUserInfoBy(UserBehavior.FOCUS_OUT, wxUserInfo);
		return null;
	}
	
	/**
	 * 菜单点击事件
	 * @param receiveXmlEntity
	 * @return
	 */
	private String clickMenu(ReceiveXmlEntity receiveXmlEntity) {
		//根据接收的事件key值确定回复消息内容。
		String eventKey = receiveXmlEntity.getEventKey();
		StringBuffer content = new StringBuffer();
		
		int keyValue = Integer.parseInt(eventKey);
		logger.info("----------------eventKey:" + eventKey + "----------------");
		WxWechatMenu menu = wxWechatMenuService.findBySaIdAndKey(1, eventKey);
		/*if(menu.getMsgType().equals("link")) {
			content.append("");
			return wxBeanFactory.get;
		}*/
		switch(keyValue) {
		//山丽产品
		case 11:
			//content.append(getOnepieceflowXml(receiveXmlEntity));
			break;
		//成功案例
		case 12:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//会议现场
		case 21:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//往期回顾
		case 22:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//官方网站
		case 31:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//荣誉资质
		case 32:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//申请试用
		case 33:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		//联系我们
		case 34:
			//content.append(getPeopleReportXml(receiveXmlEntity));
			break;
		default:
			break;
		}
		return content.toString();
	}
}
