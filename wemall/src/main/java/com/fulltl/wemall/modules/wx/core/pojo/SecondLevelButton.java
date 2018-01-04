package com.fulltl.wemall.modules.wx.core.pojo;

/**
 * 二级Button的pojo类
 * @author Administrator
 *
 */
public class SecondLevelButton extends Button {
    private String key;
    private String type;
    private String url;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
}
