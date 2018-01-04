/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 广告位管理Entity
 * @author ldk
 * @version 2017-11-22
 */
public class SlSysAdvertise extends DataEntity<SlSysAdvertise> {
	
	private static final long serialVersionUID = 1L;
	private String adBody;		// body主体内容
	private String adDesc;		// 描述
	private String isDisplay;		// 是否隐藏
	
	private String title;
	private String photo;
	
	public SlSysAdvertise() {
		super();
	}

	public SlSysAdvertise(String id){
		super(id);
	}

	public String getAdBody() {
		return adBody;
	}

	public void setAdBody(String adBody) {
		this.adBody = adBody;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getAdDesc() {
		return adDesc;
	}

	public void setAdDesc(String adDesc) {
		this.adDesc = adDesc;
	}
	
	@Length(min=1, max=1, message="是否隐藏长度必须介于 1 和 1 之间")
	public String getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}