package com.fulltl.wemall.modules.wx.core.outputHandler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.modules.wx.core.pojo.ArticleItem;
import com.fulltl.wemall.modules.wx.core.pojo.SendXmlEntity;

/**
 * 图文输出处理类
 * @author Administrator
 *
 */
@Component
public class ArticlesOutputHandler implements WXOutputHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 根据发送Xml实体对象构建发送xml文本。
	 * @param xmlEntity 发送Xml实体对象（SendXmlEntity类型）
	 * @return 要发送的xml文本(String类型)
	 */
	@Override
	public String getXmlResult(SendXmlEntity sendXmlEntity) {
		StringBuffer result = new StringBuffer();
		result.append("<xml><ToUserName><![CDATA[");
		result.append(sendXmlEntity.getToUserName());
		result.append("]]></ToUserName><FromUserName><![CDATA[");
		result.append(sendXmlEntity.getFromUserName());
		result.append("]]></FromUserName><CreateTime>");
		result.append(sendXmlEntity.getCreateTime());
		result.append("</CreateTime><MsgType><![CDATA[");
		result.append(sendXmlEntity.getMsgType());
		result.append("]]></MsgType><ArticleCount>");
		result.append(sendXmlEntity.getArticleCount());
		result.append("</ArticleCount><Articles>");
		
		List<ArticleItem> articleItems = sendXmlEntity.getArticles();
		for(ArticleItem ai : articleItems) {
			result.append("<item><Title><![CDATA[");
			result.append(ai.getTitle());
			result.append("]]></Title><Description><![CDATA[");
			result.append(ai.getDescription());
			result.append("]]></Description><PicUrl><![CDATA[");
			result.append(ai.getPicUrl());
			result.append("]]></PicUrl><Url><![CDATA[");
			result.append(ai.getUrl());
			result.append("]]></Url></item>");
		}
		result.append("</Articles></xml>");
		return result.toString();
	}
	
}
