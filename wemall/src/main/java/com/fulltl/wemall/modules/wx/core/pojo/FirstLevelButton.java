package com.fulltl.wemall.modules.wx.core.pojo;

import java.util.List;

/**
 * 一级Button的pojo类。
 * @author Administrator
 *
 */
public class FirstLevelButton extends Button {
	private List<SecondLevelButton> sub_button ;

	public List<SecondLevelButton> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<SecondLevelButton> sub_button) {
		this.sub_button = sub_button;
	}

}
