package com.fulltl.wemall.modules.pay.util.jpush;

import java.util.Map;
import java.util.Set;

/**
 * jpush消息通知消息实体类
 * @author Administrator
 *
 */
public class JPush {
    private Integer jpushId;	//id
    
    private String title;	//标题
    
    private String content;		//内容
    
    private String tag;		//标签
    
    private String alias;	//别名
    
    private String schedulePushName; //定时推送名称
    
    private String sendtime;	//发送时间,要求格式yyyy-MM-dd HH:mm:ss，如：2016-07-30 12:30:25
    
    private Map<String, String> extraMap;	//附加字段map
    
    private Set<String> registrationIds;	//极光注册号registrationId列表
    
    public Integer getJpushId() {
        return jpushId;
    }

    public void setJpushId(Integer jpushId) {
        this.jpushId = jpushId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }
    
    public String getSchedulePushName() {
		return schedulePushName;
	}

	public void setSchedulePushName(String schedulePushName) {
		this.schedulePushName = schedulePushName;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	public Map<String, String> getExtraMap() {
		return extraMap;
	}

	public void setExtraMap(Map<String, String> extraMap) {
		this.extraMap = extraMap;
	}

	public Set<String> getRegistrationIds() {
		return registrationIds;
	}

	public void setRegistrationIds(Set<String> registrationIds) {
		this.registrationIds = registrationIds;
	}

}