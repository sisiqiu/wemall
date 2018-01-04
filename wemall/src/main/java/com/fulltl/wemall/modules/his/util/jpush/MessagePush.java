package com.fulltl.wemall.modules.his.util.jpush;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fulltl.wemall.common.config.Global;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;

public class MessagePush {

    public static final String appKey = Global.getConfig("jpush.appKey");
    public static final String masterSecret = Global.getConfig("jpush.masterSecret");
    private Logger logger = LoggerFactory.getLogger(getClass());
    private JPushClient jpushClient;
    private String title;
    private String message;
    private Map<String, String> extraMap;
    
    public MessagePush(String message) {
         this.message = message;
         jpushClient = new JPushClient(masterSecret, appKey);
    }
    
    public MessagePush(String message,String title) {
    	this(message);
    	this.title = title;
	}
    
    public MessagePush(String message,String title,Map<String, String> extraMap) {
    	 this(message);
         this.title = title;
         this.extraMap = extraMap;
	}
    
    /**
     * 构造android对应消息传递Builder对象
     * @return
     */
    public cn.jpush.api.push.model.notification.AndroidNotification.Builder 
    		buildAndroidNotifiBuilder() {
    	cn.jpush.api.push.model.notification.AndroidNotification.Builder androidNotifiBuilder = AndroidNotification.newBuilder()
														.setTitle(title)
														.setAlert(message);
    	if(extraMap != null && extraMap.size() != 0) {
    		androidNotifiBuilder.addExtras(extraMap);
    	}
        return androidNotifiBuilder;
    }
    
    /**
     * 构造ios对应消息传递Builder对象
     * @return
     */
    public cn.jpush.api.push.model.notification.IosNotification.Builder 
    		buildIosNotifiBuilder() {
    	cn.jpush.api.push.model.notification.IosNotification.Builder iosNotifiBuilder = IosNotification.newBuilder()
    													.setAlert(message);
    	if(extraMap != null && extraMap.size() != 0) {
    		iosNotifiBuilder.addExtras(extraMap);
    	}
        return iosNotifiBuilder;
    }
    
    /**
     * 构造消息的builder对象
     * @return
     */
    public cn.jpush.api.push.model.Message.Builder
    		buildMessageBuilder() {
    	cn.jpush.api.push.model.Message.Builder msgBuilder = Message.newBuilder()
    													.setTitle(title)
    													.setMsgContent(message);
    	if(extraMap != null && extraMap.size() != 0) {
    		msgBuilder.addExtras(extraMap);
    	}
    	return msgBuilder;
    }
    
    /**
     * 格式化错误，构造成map消息
     * @param error
     * @return
     */
    public Map<String, Object> formatError(cn.jpush.api.push.PushResult.Error error) {
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	int errorCode = error.getCode();
	    String errorMsg = error.getMessage();
	    if(errorCode == 0) {
	    	retMap.put("ret", "0");
	    	retMap.put("retMsg", "推送成功");
	    } else {
	    	retMap.put("ret", "-1");
	    	retMap.put("retMsg", "推送失败，失败原因：" + errorMsg);
	    }
	    return retMap;
    }
    
    /**
     * 向所有人发送消息
     * @return 消息id
     * @throws APIRequestException 
     * @throws APIConnectionException 
     */
    public Map<String, Object> sendPushAll() throws APIConnectionException, APIRequestException{
    	Map<String, Object> retMap = new HashMap<String, Object>();
		PushPayload payload=buildPushObject_all_all_alertWithTitle();
	    PushResult result=jpushClient.sendPush(payload);
	    retMap = formatError(result.error);
		return retMap;
    }
    
    /**
     * 定时向所有人发送消息
     * @return
     * @throws APIRequestException 
     * @throws APIConnectionException 
     */
    public Map<String, Object> scheduleSendPushAll(String name, String time) throws APIConnectionException, APIRequestException {
    	Map<String, Object> retMap = new HashMap<String, Object>();
     	PushPayload payloadAlias=buildPushObject_all_all_alertWithTitle();
     	ScheduleResult result = jpushClient.createSingleSchedule(name, time, payloadAlias);
 		if(result.isResultOK()) {
 	    	retMap.put("ret", "0");
 	    	retMap.put("retMsg", "定时推送成功");
 	    } else {
 	    	retMap.put("ret", "-1");
 	    	retMap.put("retMsg", "定时推送失败");
 	    }
 		return retMap;
	}
    
	/**
     * 向指定别名的客户端发送消息
     * @param alias 所有别名信息集合
     * @return 消息id
	 * @throws APIRequestException 
	 * @throws APIConnectionException 
     */
    public Map<String, Object> sendPushAlias(Set<String> alias) throws APIConnectionException, APIRequestException{
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_android_alias_alertWithTitle(alias);
        PushResult result=jpushClient.sendPush(payloadAlias);
        retMap = formatError(result.error);
        return retMap;
    }
    
    /**
     * 向指定组发送消息
     * @param tag 组名称
     * @return 消息id     
     * @throws APIRequestException 
     * @throws APIConnectionException 
	*/
    public Map<String, Object> sendPushTag(String tag) throws APIConnectionException, APIRequestException {
        Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_android_tag_alertWithTitle(tag);
        PushResult result=jpushClient.sendPush(payloadAlias);
        retMap = formatError(result.error);
        return retMap;
    }
    
    /**
     * 向指定极光注册id列表发送消息
     * @param tag 组名称
     * @return 消息id     
     * @throws APIRequestException 
     * @throws APIConnectionException 
	*/
    public Map<String, Object> sendPushRegistrationId(Set<String> registrationIds) throws APIConnectionException, APIRequestException {
        Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_androidios_registrationId_alertWithTitle(registrationIds);
        PushResult result=jpushClient.sendPush(payloadAlias);
        retMap = formatError(result.error);
        return retMap;
    }
    
    /**
     * 定时向指定极光注册id列表发送消息
     * @param tag 组名称
     * @return 消息id     
     * @throws APIRequestException 
     * @throws APIConnectionException 
	*/
    public Map<String, Object> scheduleSendPushRegistrationId(Set<String> registrationIds, String name, String time) throws APIConnectionException, APIRequestException {
        Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_androidios_registrationId_alertWithTitle(registrationIds);
    	ScheduleResult result = jpushClient.createSingleSchedule(name, time, payloadAlias);
		if(result.isResultOK()) {
	    	retMap.put("ret", "0");
	    	retMap.put("retMsg", "定时推送成功");
	    } else {
	    	retMap.put("ret", "-1");
	    	retMap.put("retMsg", "定时推送失败");
	    }
        return retMap;
    }
    
    /**
     * 向指定的或标签，或别名发送消息。
     * @param tags 或标签
     * @param aliases 或别名
     * @return
     * @throws APIRequestException 
     * @throws APIConnectionException 
     */
    public Map<String, Object> sendPushTagsAndAliases(Collection<String> tags, Collection<String> aliases) throws APIConnectionException, APIRequestException {
        Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_tags_aliases_alert(tags, aliases);
        PushResult result=jpushClient.sendPush(payloadAlias);
        retMap = formatError(result.error);
        return retMap;
    }
    
    /**
     * 向指定的或标签，或别名发送携带有附加字段的消息。
     * @param tags 或标签
     * @param aliases 或别名
     * @param extras 附加字段
     * @return
     * @throws APIRequestException 
     * @throws APIConnectionException 
     */
    public Map<String, Object> sendPushWithExtras(Collection<String> tags, Collection<String> aliases, Map<String, String> extras) throws APIConnectionException, APIRequestException {
        Map<String, Object> retMap = new HashMap<String, Object>();
    	PushPayload payloadAlias=buildPushObject_tags_aliases_alertWithExtras(tags, aliases, extras);
        PushResult result=jpushClient.sendPush(payloadAlias);
        retMap = formatError(result.error);
        return retMap;
    }

    
    
    /**
	 * 向所有平台的所有人发送。
	 * @return PushPayload
	 */
	private PushPayload buildPushObject_all_all_alertWithTitle() {
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setMessage(buildMessageBuilder().build())
				.setNotification(Notification.newBuilder()
                        .addPlatformNotification(buildAndroidNotifiBuilder().build())
                        .addPlatformNotification(buildIosNotifiBuilder().build())
                        .build())
				.build();
	}
	
	/**
	 * 向Android和IOS平台，指定别名的用户推送。
	 * @param alias 别名
	 * @return PushPayload
	 */
	public PushPayload buildPushObject_android_alias_alertWithTitle(Set<String> alias) {
		return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setMessage(buildMessageBuilder().build())
                .setNotification(Notification.newBuilder()
                		.addPlatformNotification(buildAndroidNotifiBuilder().build())
                        .addPlatformNotification(buildIosNotifiBuilder().build())
                		.build())
                .build();
	}
	
	/**
	 * 向Android和IOS平台，指定标签的组推送。
	 * @param tag 标签
	 * @return PushPayload
	 */
	public PushPayload buildPushObject_android_tag_alertWithTitle(String tag){
		return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setMessage(buildMessageBuilder().build())
                .setNotification(Notification.newBuilder()
                		.addPlatformNotification(buildAndroidNotifiBuilder().build())
                        .addPlatformNotification(buildIosNotifiBuilder().build())
                		.build())
                .build();
	}
	
	/**
	 * 向Android和IOS平台，指定registrationId的用户推送。
	 * @param alias 别名
	 * @return PushPayload
	 */
	public PushPayload buildPushObject_androidios_registrationId_alertWithTitle(Set<String> registrationIds) {
		return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setMessage(buildMessageBuilder().build())
                .setNotification(Notification.newBuilder()
                		.addPlatformNotification(buildAndroidNotifiBuilder().build())
                        .addPlatformNotification(buildIosNotifiBuilder().build())
                		.build())
                .build();
	}
	
	/**
	 * 向所有平台推送；标签或集合tags, 别名或集合alias
	 * @param tags 标签或集合
	 * @param aliases 别名或集合
	 * @return PushPayload
	 */
	public  PushPayload buildPushObject_tags_aliases_alert(Collection<String> tags, Collection<String> aliases) {
		//return PushPayload.alertAll(title);
		 return PushPayload.newBuilder()
					.setPlatform(Platform.all())
				 	.setAudience(Audience.newBuilder()
	                        .addAudienceTarget(AudienceTarget.tag(tags))
	                        .addAudienceTarget(AudienceTarget.alias(aliases))
	                        .build())
	                .setMessage(buildMessageBuilder().build())
	                .setNotification(Notification.newBuilder()
	                		.addPlatformNotification(buildAndroidNotifiBuilder().build())
	                        .addPlatformNotification(buildIosNotifiBuilder().build())
	                		.build())
	                .build();
	}
	
	/**
	 * 向所有平台推送；标签或集合tags, 别名或集合alias
	 * @param tags 标签或集合
	 * @param aliases 别名或集合
	 * @param extras Map<String,String>类型附加字段
	 * @return PushPayload
	 */
	public  PushPayload buildPushObject_tags_aliases_alertWithExtras(Collection<String> tags,  Collection<String> aliases, Map<String, String> extras) {
		 return PushPayload.newBuilder()
					.setPlatform(Platform.all())
				 	.setAudience(Audience.newBuilder()
	                        .addAudienceTarget(AudienceTarget.tag(tags))
	                        .addAudienceTarget(AudienceTarget.alias(aliases))
	                        .build())
	                .setMessage(buildMessageBuilder().build())
	                .setNotification(Notification.newBuilder()
	                		.addPlatformNotification(buildAndroidNotifiBuilder().build())
	                        .addPlatformNotification(buildIosNotifiBuilder().build())
	                		.build())
	                .build();
	}

}