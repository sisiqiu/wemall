/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 会员卡管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallVipCard extends DataEntity<WemallVipCard> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 会员卡名称
	private String photo;		// 图片
	private Integer type;		// 类别（1--银卡；2--金卡；3--白金）
	private Integer sort;		// 排序
	private String discount;		// 享受折扣
	private Integer freightFree;		// 是否免邮
	private String usageKnown;		// 使用须知
	private String obtainSetting;		// 领取设置
	private String sellerTip;		// 商家提醒
	private Integer orderNum;		// 交易笔数
	private Integer consumeNum;		// 消费金额
	private Integer scoreNum;		// 累计积分
	
	public WemallVipCard() {
		super();
	}

	public WemallVipCard(String id){
		super(id);
	}

	@Length(min=1, max=50, message="会员卡名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=200, message="图片长度必须介于 1 和 200 之间")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@NotNull(message="类别（1--银卡；2--金卡；3--白金）不能为空")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Length(min=0, max=10, message="享受折扣长度必须介于 0 和 10 之间")
	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	@NotNull(message="是否免邮不能为空")
	public Integer getFreightFree() {
		return freightFree;
	}

	public void setFreightFree(Integer freightFree) {
		this.freightFree = freightFree;
	}
	
	@Length(min=1, max=255, message="使用须知长度必须介于 1 和 255 之间")
	public String getUsageKnown() {
		return usageKnown;
	}

	public void setUsageKnown(String usageKnown) {
		this.usageKnown = usageKnown;
	}
	
	@Length(min=1, max=255, message="领取设置长度必须介于 1 和 255 之间")
	public String getObtainSetting() {
		return obtainSetting;
	}

	public void setObtainSetting(String obtainSetting) {
		this.obtainSetting = obtainSetting;
	}
	
	@Length(min=1, max=255, message="商家提醒长度必须介于 1 和 255 之间")
	public String getSellerTip() {
		return sellerTip;
	}

	public void setSellerTip(String sellerTip) {
		this.sellerTip = sellerTip;
	}
	
	@NotNull(message="交易笔数不能为空")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	@NotNull(message="消费金额不能为空")
	public Integer getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(Integer consumeNum) {
		this.consumeNum = consumeNum;
	}
	
	@NotNull(message="累计积分不能为空")
	public Integer getScoreNum() {
		return scoreNum;
	}

	public void setScoreNum(Integer scoreNum) {
		this.scoreNum = scoreNum;
	}
	
}