/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 商品管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallItem extends DataEntity<WemallItem> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 商品名称
	private Integer sortId;		// 商品类别id
	private Integer originalPrice;		// 商品原价
	private Integer currentPrice;		// 商品现价
	private Integer storage;		// 商品库存
	private String photo;		// 缩略图
	private Integer sort;		// 排序序号
	private Integer isTop;		// 是否置顶
	private Integer isNew;		// 是否新品
	private Integer isOnShelf;		// 是否上架
	private Integer salesNum;		// 销量
	private String productPlace;		// 产地
	private Integer canUseBounty;		// 是否可用奖励金
	private Integer canUseCoupon;		// 是否可用优惠券
	private Integer canUseScoreDeduct;		// 是否可用积分抵扣
	private Integer scoreDeductPrice;		// 积分最大抵扣金额
	private Integer canUseScoreExchange;		// 是否可用积分兑换
	private Integer subStock;		// 是否支持下单减库存
	private Integer freightFree;		// 是否免邮
	private Integer freightPrice;		// 运费
	private Integer activitySort;		// 参与的活动（0--未参与活动，1--限时打折，2--订单返现，3--满减送，4--限时拼团）
	private Integer activityId;		// 活动id
	private String desc;		// 商品描述
	private String photoUrls;		// 图片列表
	private Integer beginOriginalPrice;		// 开始 商品原价
	private Integer endOriginalPrice;		// 结束 商品原价
	private Integer beginCurrentPrice;		// 开始 商品现价
	private Integer endCurrentPrice;		// 结束 商品现价
	private Integer beginStorage;		// 开始 商品库存
	private Integer endStorage;		// 结束 商品库存
	private Integer beginSalesNum;		// 开始 销量
	private Integer endSalesNum;		// 结束 销量
	private Integer beginFreightPrice;		// 开始 运费
	private Integer endFreightPrice;		// 结束 运费
	
	private String specInfoStr; //规格值列表json字符串
	private String sortName; //商品类别名称
	
	public WemallItem() {
		super();
	}

	public WemallItem(String id){
		super(id);
	}

	@Length(min=1, max=100, message="商品名称长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="商品类别id不能为空")
	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	
	@NotNull(message="商品原价不能为空")
	public Integer getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	@NotNull(message="商品现价不能为空")
	public Integer getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Integer currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	@NotNull(message="商品库存不能为空")
	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}
	
	@Length(min=1, max=200, message="缩略图长度必须介于 1 和 200 之间")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@NotNull(message="排序序号不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@NotNull(message="是否置顶不能为空")
	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}
	
	@NotNull(message="是否新品不能为空")
	public Integer getIsNew() {
		return isNew;
	}

	public void setIsNew(Integer isNew) {
		this.isNew = isNew;
	}
	
	@NotNull(message="是否上架不能为空")
	public Integer getIsOnShelf() {
		return isOnShelf;
	}

	public void setIsOnShelf(Integer isOnShelf) {
		this.isOnShelf = isOnShelf;
	}
	
	@NotNull(message="销量不能为空")
	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}
	
	@Length(min=0, max=100, message="产地长度必须介于 0 和 100 之间")
	public String getProductPlace() {
		return productPlace;
	}

	public void setProductPlace(String productPlace) {
		this.productPlace = productPlace;
	}
	
	@NotNull(message="是否可用奖励金不能为空")
	public Integer getCanUseBounty() {
		return canUseBounty;
	}

	public void setCanUseBounty(Integer canUseBounty) {
		this.canUseBounty = canUseBounty;
	}
	
	@NotNull(message="是否可用优惠券不能为空")
	public Integer getCanUseCoupon() {
		return canUseCoupon;
	}

	public void setCanUseCoupon(Integer canUseCoupon) {
		this.canUseCoupon = canUseCoupon;
	}
	
	@NotNull(message="是否可用积分抵扣不能为空")
	public Integer getCanUseScoreDeduct() {
		return canUseScoreDeduct;
	}

	public void setCanUseScoreDeduct(Integer canUseScoreDeduct) {
		this.canUseScoreDeduct = canUseScoreDeduct;
	}
	
	@NotNull(message="积分最大抵扣金额不能为空")
	public Integer getScoreDeductPrice() {
		return scoreDeductPrice;
	}

	public void setScoreDeductPrice(Integer scoreDeductPrice) {
		this.scoreDeductPrice = scoreDeductPrice;
	}
	
	@NotNull(message="是否可用积分兑换不能为空")
	public Integer getCanUseScoreExchange() {
		return canUseScoreExchange;
	}

	public void setCanUseScoreExchange(Integer canUseScoreExchange) {
		this.canUseScoreExchange = canUseScoreExchange;
	}
	
	@NotNull(message="是否支持下单减库存不能为空")
	public Integer getSubStock() {
		return subStock;
	}

	public void setSubStock(Integer subStock) {
		this.subStock = subStock;
	}
	
	@NotNull(message="是否免邮不能为空")
	public Integer getFreightFree() {
		return freightFree;
	}

	public void setFreightFree(Integer freightFree) {
		this.freightFree = freightFree;
	}
	
	@NotNull(message="运费不能为空")
	public Integer getFreightPrice() {
		return freightPrice;
	}

	public void setFreightPrice(Integer freightPrice) {
		this.freightPrice = freightPrice;
	}
	
	@NotNull(message="参与的活动（0--未参与活动，1--限时打折，2--订单返现，3--满减送，4--限时拼团）不能为空")
	public Integer getActivitySort() {
		return activitySort;
	}

	public void setActivitySort(Integer activitySort) {
		this.activitySort = activitySort;
	}
	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Length(min=1, max=2000, message="图片列表长度必须介于 1 和 2000 之间")
	public String getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(String photoUrls) {
		this.photoUrls = photoUrls;
	}
	
	public Integer getBeginOriginalPrice() {
		return beginOriginalPrice;
	}

	public void setBeginOriginalPrice(Integer beginOriginalPrice) {
		this.beginOriginalPrice = beginOriginalPrice;
	}
	
	public Integer getEndOriginalPrice() {
		return endOriginalPrice;
	}

	public void setEndOriginalPrice(Integer endOriginalPrice) {
		this.endOriginalPrice = endOriginalPrice;
	}
		
	public Integer getBeginCurrentPrice() {
		return beginCurrentPrice;
	}

	public void setBeginCurrentPrice(Integer beginCurrentPrice) {
		this.beginCurrentPrice = beginCurrentPrice;
	}
	
	public Integer getEndCurrentPrice() {
		return endCurrentPrice;
	}

	public void setEndCurrentPrice(Integer endCurrentPrice) {
		this.endCurrentPrice = endCurrentPrice;
	}
		
	public Integer getBeginStorage() {
		return beginStorage;
	}

	public void setBeginStorage(Integer beginStorage) {
		this.beginStorage = beginStorage;
	}
	
	public Integer getEndStorage() {
		return endStorage;
	}

	public void setEndStorage(Integer endStorage) {
		this.endStorage = endStorage;
	}
		
	public Integer getBeginSalesNum() {
		return beginSalesNum;
	}

	public void setBeginSalesNum(Integer beginSalesNum) {
		this.beginSalesNum = beginSalesNum;
	}
	
	public Integer getEndSalesNum() {
		return endSalesNum;
	}

	public void setEndSalesNum(Integer endSalesNum) {
		this.endSalesNum = endSalesNum;
	}
		
	public Integer getBeginFreightPrice() {
		return beginFreightPrice;
	}

	public void setBeginFreightPrice(Integer beginFreightPrice) {
		this.beginFreightPrice = beginFreightPrice;
	}
	
	public Integer getEndFreightPrice() {
		return endFreightPrice;
	}

	public void setEndFreightPrice(Integer endFreightPrice) {
		this.endFreightPrice = endFreightPrice;
	}

	public String getSpecInfoStr() {
		return specInfoStr;
	}

	public void setSpecInfoStr(String specInfoStr) {
		this.specInfoStr = specInfoStr;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
		
	
}