/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wemall.entity;

import org.hibernate.validator.constraints.Length;

import com.fulltl.wemall.common.persistence.DataEntity;

/**
 * 订单-地址管理Entity
 * @author ldk
 * @version 2018-01-05
 */
public class WemallOrderAddress extends DataEntity<WemallOrderAddress> {
	
	private static final long serialVersionUID = 1L;
	private String orderNo;		// 订单号
	private String receiverCountry;		// 国家
	private String receiverProvince;		// 省份
	private String receiverCity;		// 城市
	private String receiverDistrict;		// 区县
	private String receiverAddress;		// 收货地址
	private String receiverZip;		// 邮政编码
	private String receiverName;		// 收货人姓名
	private String receiverMobile;		// 收货人手机
	private String receiverPhone;		// 收货人电话
	
	public WemallOrderAddress() {
		super();
	}

	public WemallOrderAddress(String id){
		super(id);
	}

	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Length(min=1, max=40, message="国家长度必须介于 1 和 40 之间")
	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}
	
	@Length(min=1, max=40, message="省份长度必须介于 1 和 40 之间")
	public String getReceiverProvince() {
		return receiverProvince;
	}

	public void setReceiverProvince(String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}
	
	@Length(min=1, max=40, message="城市长度必须介于 1 和 40 之间")
	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}
	
	@Length(min=1, max=40, message="区县长度必须介于 1 和 40 之间")
	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	
	@Length(min=1, max=100, message="收货地址长度必须介于 1 和 100 之间")
	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	
	@Length(min=1, max=10, message="邮政编码长度必须介于 1 和 10 之间")
	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}
	
	@Length(min=1, max=20, message="收货人姓名长度必须介于 1 和 20 之间")
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	@Length(min=1, max=20, message="收货人手机长度必须介于 1 和 20 之间")
	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	
	@Length(min=1, max=20, message="收货人电话长度必须介于 1 和 20 之间")
	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	
	@Override
	public boolean getIsNewRecord() {
		return isNewRecord;
	}
	
	public void initBy(WemallUserAddress wemallUserAddress, String orderNo) {
		this.orderNo = orderNo;		// 订单号
		this.receiverCountry = wemallUserAddress.getReceiverCountry();		// 国家
		this.receiverProvince = wemallUserAddress.getReceiverProvince();		// 省份
		this.receiverCity = wemallUserAddress.getReceiverCity();		// 城市
		this.receiverDistrict = wemallUserAddress.getReceiverDistrict();		// 区县
		this.receiverAddress = wemallUserAddress.getReceiverAddress();		// 收货地址
		this.receiverZip = wemallUserAddress.getReceiverZip();		// 邮政编码
		this.receiverName = wemallUserAddress.getReceiverName();		// 收货人姓名
		this.receiverMobile = wemallUserAddress.getReceiverMobile();		// 收货人手机
		this.receiverPhone = wemallUserAddress.getReceiverPhone();		// 收货人电话
	}
	
}