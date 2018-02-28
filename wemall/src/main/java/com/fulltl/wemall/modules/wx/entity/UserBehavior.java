package com.fulltl.wemall.modules.wx.entity;

/**
 * 用户行为枚举类，用于表示用户的操作行为。
 * @author Administrator
 *
 */
public enum UserBehavior {
	/**
	 * 关注行为
	 */
	FOCUS_ON(1), 
	/**
	 * 取消关注行为
	 */
	FOCUS_OUT(2), 
	/**
	 * 绑定行为
	 */
	BIND(3);
	
	private int value;
	
	private UserBehavior(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
	
}
