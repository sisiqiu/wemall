/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 上传文件管理Entity
 * @author ldk
 * @version 2017-11-15
 */
public class CmsFileresource extends DataEntity<CmsFileresource> {
	
	private static final long serialVersionUID = 1L;
	private String module;		// 所属模块
	private String category;		// 所属类别
	private String name;		// 名称
	private Integer size;		// 大小
	private String suffix;		// 格式
	private String filePath;		// 文件地址
	private String downloadNum;		// 下载数
	private String status;		// 状态值
	private Integer beginSize;		// 开始 大小
	private Integer endSize;		// 结束 大小
	private String beginDownloadNum;		// 开始 下载数
	private String endDownloadNum;		// 结束 下载数
	
	public CmsFileresource() {
		super();
	}

	public CmsFileresource(String id){
		super(id);
	}

	@Length(min=1, max=50, message="所属模块长度必须介于 1 和 50 之间")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	@Length(min=1, max=50, message="所属类别长度必须介于 1 和 50 之间")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Length(min=1, max=100, message="名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="大小不能为空")
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	@Length(min=1, max=10, message="格式长度必须介于 1 和 10 之间")
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	@NotNull(message="文件地址不能为空")
	@Length(min=1, max=100, message="文件地址不能为空，文件名长度不能超过60")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Length(min=1, max=11, message="下载数长度必须介于 1 和 11 之间")
	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	
	@Length(min=1, max=1, message="状态值长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getBeginSize() {
		return beginSize;
	}

	public void setBeginSize(Integer beginSize) {
		this.beginSize = beginSize;
	}
	
	public Integer getEndSize() {
		return endSize;
	}

	public void setEndSize(Integer endSize) {
		this.endSize = endSize;
	}
		
	public String getBeginDownloadNum() {
		return beginDownloadNum;
	}

	public void setBeginDownloadNum(String beginDownloadNum) {
		this.beginDownloadNum = beginDownloadNum;
	}
	
	public String getEndDownloadNum() {
		return endDownloadNum;
	}

	public void setEndDownloadNum(String endDownloadNum) {
		this.endDownloadNum = endDownloadNum;
	}
		
}