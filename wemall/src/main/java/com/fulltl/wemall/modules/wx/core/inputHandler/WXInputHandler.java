package com.fulltl.wemall.modules.wx.core.inputHandler;

import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;

/**
 * 微信输入xml文本处理类的上层接口。
 * @author Administrator
 *
 */
public interface WXInputHandler {
	
	/**
	 * 根据接收到的实体对象，确定回复Xml文本消息内容。
	 * @param xmlEntity 接收到的实体对象（ReceiveXmlEntity类型）
	 * @return 回复Xml消息内容（String类型）
	 */
	public String handleWithReceive(ReceiveXmlEntity receiveXmlEntity);
	
}
