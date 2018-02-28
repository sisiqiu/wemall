package com.fulltl.wemall.modules.wx.core.inputHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;

/**
 * 图片输入处理类
 * @author Administrator
 *
 */
@Component
public class ImageInputHandler implements WXInputHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String handleWithReceive(ReceiveXmlEntity receiveXmlEntity) {
		String openId = receiveXmlEntity.getFromUserName();
		logger.info("----------------收到用户：openId=" + openId + " 的图片消息----------------");
		return null;
	}
	
}
