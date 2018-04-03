package com.fulltl.wemall.modules.wx.core.outputHandler;

import com.fulltl.wemall.modules.wx.core.pojo.SendXmlEntity;

/**
 * 微信输出xml文本处理类的上层接口。
 * @author Administrator
 *
 */
public interface WXOutputHandler {

	/**
	 * 根据输出xml对象获取输出的xml文本。
	 * @param sendXmlEntity
	 * @return
	 */
	public String getXmlResult(SendXmlEntity sendXmlEntity);
	
}
