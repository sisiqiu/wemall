/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 订单-商品管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallOrderItem extends DataEntity<WemallOrderItem> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private Integer itemId;		// 商品id
	private Integer itemNum;		// 商品数量
	private String title;		// 商品标题
	private String photo;		// 商品缩略图
	private Integer totalFee;		// 商品总金额
	private String itemsData;		// 商品规格说明，[{&quot;k&quot;:属性类别,&quot;v&quot;:属性名}]
	private String freightName;		// 物流名称
	private String freightNo;		// 物流单号
	private String buyerMessage;		// 买家留言
	private String buyerNick;		// 买家昵称
	private Integer buyerComment;		// 买家是否已评价
	private Integer beginTotalFee;		// 开始 商品总金额
	private Integer endTotalFee;		// 结束 商品总金额
	
	public WemallOrderItem() {
		super();
	}

	public WemallOrderItem(String id){
		super(id);
	}

	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@NotNull(message="商品id不能为空")
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@NotNull(message="商品数量不能为空")
	public Integer getItemNum() {
		return itemNum;
	}

	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	
	@Length(min=1, max=50, message="商品标题长度必须介于 1 和 50 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=200, message="商品缩略图长度必须介于 1 和 200 之间")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@NotNull(message="商品总金额不能为空")
	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	
	@Length(min=1, max=200, message="商品规格说明，[{&quot;k&quot;:属性类别,&quot;v&quot;:属性名}]长度必须介于 1 和 200 之间")
	public String getItemsData() {
		return itemsData;
	}

	public void setItemsData(String itemsData) {
		this.itemsData = itemsData;
	}
	
	@Length(min=1, max=50, message="物流名称长度必须介于 1 和 50 之间")
	public String getFreightName() {
		return freightName;
	}

	public void setFreightName(String freightName) {
		this.freightName = freightName;
	}
	
	@Length(min=1, max=64, message="物流单号长度必须介于 1 和 64 之间")
	public String getFreightNo() {
		return freightNo;
	}

	public void setFreightNo(String freightNo) {
		this.freightNo = freightNo;
	}
	
	@Length(min=1, max=200, message="买家留言长度必须介于 1 和 200 之间")
	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	
	@Length(min=1, max=50, message="买家昵称长度必须介于 1 和 50 之间")
	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	
	@NotNull(message="买家是否已评价不能为空")
	public Integer getBuyerComment() {
		return buyerComment;
	}

	public void setBuyerComment(Integer buyerComment) {
		this.buyerComment = buyerComment;
	}
	
	public Integer getBeginTotalFee() {
		return beginTotalFee;
	}

	public void setBeginTotalFee(Integer beginTotalFee) {
		this.beginTotalFee = beginTotalFee;
	}
	
	public Integer getEndTotalFee() {
		return endTotalFee;
	}

	public void setEndTotalFee(Integer endTotalFee) {
		this.endTotalFee = endTotalFee;
	}
		
	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
}