package com.fulltl.wemall.modules.his.util.jpush;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;

public class JPushHelper {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public enum PushMethod {
		pushAll(1), pushWithRegistrationIds(2);
		private int value;
	    private PushMethod(int value) {
	        this.value = value;
	    }
	    public int getValue() {
	        return value;
	    }
	}
	
	/**
	 * 根据jpush对象向指定的RegistrationIds推送
	 * @param push
	 * @param tag
	 * @return
	 */
	public Map<String, Object> pushWithRegistrationIds(JPush jPush) {
		Map<String, Object> retMap = Maps.newHashMap();
		
		if (jPush.getRegistrationIds() == null || jPush.getRegistrationIds().size() == 0) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "请添加极光注册ID（RegistrationIds）列表");
			return retMap;
		}
		
		return commonPush(jPush, PushMethod.pushWithRegistrationIds);
	}
	
	/**
	 * 向所有人推送
	 * @param push
	 * @return
	 */
	public Map<String, Object> pushAll(JPush jPush) {
		return commonPush(jPush, PushMethod.pushAll);
	}
	
	/**
	 * 通用推送
	 * @param jPush
	 * @param pushMethod 
	 * @return
	 */
	private Map<String, Object> commonPush(JPush jPush, PushMethod pushMethod) {
		Map<String, Object> retMap = Maps.newHashMap();
		if(StringUtils.isBlank(jPush.getTitle())) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "请填写标题");
			return retMap;
		} else if (StringUtils.isBlank(jPush.getContent())) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "请填写内容");
			return retMap;
		}
		
		MessagePush push = new MessagePush(jPush.getContent(),jPush.getTitle());
		Set<String> registrationIds = jPush.getRegistrationIds();
		try {
			//判断是否为定时推送
			if(jPush.getSendtime() == null) {
				//非定时
				if(PushMethod.pushAll.equals(pushMethod)) {
					//推送所有人
					retMap = push.sendPushAll();
					logger.info("定时推送所有人成功！");
				} else {
					//推送指定极光注册id列表
					retMap = push.sendPushRegistrationId(registrationIds);
					logger.info("定时推送指定极光注册id列表成功！");
				}
			} else {
				//定时
				String name = jPush.getSchedulePushName();
				if(StringUtils.isBlank(name)) name = "定时推送";
				if(PushMethod.pushAll.equals(pushMethod)) {
					//推送所有人
					retMap = push.scheduleSendPushAll(name, jPush.getSendtime());
					logger.info("即时推送所有人成功！");
				} else {
					//推送指定极光注册id列表
					retMap = push.scheduleSendPushRegistrationId(registrationIds, name, jPush.getSendtime());
					logger.info("即时推送指定极光注册id列表成功！");
				}
			}
		} catch (APIConnectionException e) {
			logger.error("Connection error. Should retry later. ", e);
	        retMap.put("ret", "-1");
	    	retMap.put("retMsg", "推送失败");
		} catch (APIRequestException e) {
			logger.error("HTTP Status: " + e.getStatus());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Error Message: " + e.getErrorMessage());
            logger.error("Msg ID: " + e.getMsgId());
		    retMap.put("ret", "-1");
	    	retMap.put("retMsg", "推送失败," + e.getErrorMessage());
		}
		return retMap;
	}
	
}
