package com.fulltl.wemall.modules.wx.core.outputHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.modules.wx.core.pojo.SendXmlEntity;

@Component
public class LinkOutputHandler implements WXOutputHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 根据发送Xml实体对象构建发送xml文本。
	 * @param xmlEntity 发送Xml实体对象（SendXmlEntity类型）
	 * @return 要发送的xml文本(String类型)
	 */
	@Override
	public String getXmlResult(SendXmlEntity sendXmlEntity) {
		logger.info("-------begin--------getXmlResult");
		StringBuffer result = new StringBuffer();
		result.append("<xml><ToUserName><![CDATA[");
		result.append(sendXmlEntity.getToUserName());
		result.append("]]></ToUserName><FromUserName><![CDATA[");
		result.append(sendXmlEntity.getFromUserName());
		result.append("]]></FromUserName><CreateTime>");
		result.append(sendXmlEntity.getCreateTime());
		result.append("</CreateTime><MsgType><![CDATA[");
		result.append(sendXmlEntity.getMsgType());
		result.append("]]></MsgType><Title><![CDATA[");
		result.append(sendXmlEntity.getTitle());
		result.append("]]></Title><Description><![CDATA[");
		result.append(sendXmlEntity.getDescription());
		result.append("]]></Description><Url><![CDATA[");
		result.append(sendXmlEntity.getUrl());
		result.append("]]></Url><MsgId>");
		result.append(sendXmlEntity.getMsgId());
		result.append("</MsgId></xml>");
		return result.toString();
	}
}
