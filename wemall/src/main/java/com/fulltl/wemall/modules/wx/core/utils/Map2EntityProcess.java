package com.fulltl.wemall.modules.wx.core.utils;

import java.util.Map;

import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;

/**
 * 通过Map将数据转换为实体Xml对象的处理类
 * @author Administrator
 *
 */
public class Map2EntityProcess {
	/**
	 * 将储存有数据的map容器转换为Xml消息实体类
	 * @param map 储存有通过xml解析出来的数据的Map容器
	 * @return 接收Xml实体类（ReceiveXmlEntity类）的对象
	 */
	public ReceiveXmlEntity getMsgEntity(Map<String, String> map) {
		ReceiveXmlEntity xmlEntity = new ReceiveXmlEntity();
		xmlEntity.setContent(map.get("Content"));
		xmlEntity.setCreateTime(map.get("CreateTime"));
		xmlEntity.setDescription(map.get("Description"));
		xmlEntity.setEvent(map.get("Event"));
		xmlEntity.setEventKey(map.get("EventKey"));
		xmlEntity.setFormat(map.get("Format"));
		xmlEntity.setFromUserName(map.get("FromUserName"));
		xmlEntity.setLabel(map.get("Label"));
		xmlEntity.setLatitude(map.get("Latitude"));
		xmlEntity.setLocation_X(map.get("Location_X"));
		xmlEntity.setLocation_Y(map.get("Location_Y"));
		xmlEntity.setLongitude(map.get("Longitude"));
		xmlEntity.setMediaId(map.get("MediaId"));
		xmlEntity.setMsgId(map.get("MsgId"));
		xmlEntity.setMsgType(map.get("MsgType"));
		xmlEntity.setPicUrl(map.get("PicUrl"));
		xmlEntity.setPrecision(map.get("Precision"));
		xmlEntity.setRecognition(map.get("Recognition"));
		xmlEntity.setScale(map.get("Scale"));
		xmlEntity.setTicket(map.get("Ticket"));
		xmlEntity.setTitle(map.get("Title"));
		xmlEntity.setToUserName(map.get("ToUserName"));
		xmlEntity.setUrl(map.get("Url"));
		return xmlEntity;
	}
}
