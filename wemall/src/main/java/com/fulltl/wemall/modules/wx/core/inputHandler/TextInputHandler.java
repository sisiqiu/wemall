package com.fulltl.wemall.modules.wx.core.inputHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.modules.wx.core.outputHandler.OutXmlProcess;
import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;

/**
 * 文本输入处理类
 * @author Administrator
 *
 */
@Component
public class TextInputHandler implements WXInputHandler {
	@Autowired
	private OutXmlProcess outXmlProcess;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static String register = "Hi，欢迎关注上海山丽，参加会议点【" +
			"<a href=\"" + Global.getConfig("weixin.appUrl") + Global.getConfig("frontPath") + "/wx/conActivity/viewActivities\">这里</a>" + "】！\n";
	
	/**
	 * 根据接收到的实体对象，确定回复Xml文本消息内容。
	 * @param xmlEntity 接收到的实体对象（ReceiveXmlEntity类型）
	 * @return 回复Xml消息内容（String类型）
	 */
	public String handleWithReceive(ReceiveXmlEntity receiveXmlEntity) {
		String receiveContent = receiveXmlEntity.getContent();
		StringBuffer content = new StringBuffer();
		
		if(receiveContent != null) {
			String openId = receiveXmlEntity.getFromUserName();
			logger.info("----------------收到用户：openId=" + openId + " 的文本消息,文本内容为：" + receiveContent + "----------------");
			content.append(register.replace("OPEN_ID", receiveXmlEntity.getFromUserName()));
			return outXmlProcess.getTextResult(receiveXmlEntity, content.toString());
		}
		
		return outXmlProcess.getTextResult(receiveXmlEntity, content.toString());
	}
}
