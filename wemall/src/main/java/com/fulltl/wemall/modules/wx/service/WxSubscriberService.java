/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.wx.core.pojo.ReceiveXmlEntity;
import com.fulltl.wemall.modules.wx.dao.WxSubscriberDao;
import com.fulltl.wemall.modules.wx.entity.UserBehavior;
import com.fulltl.wemall.modules.wx.entity.WxServiceaccount;
import com.fulltl.wemall.modules.wx.entity.WxSubscriber;
import com.fulltl.wemall.modules.wx.entity.WxUserinfo;

/**
 * 微信关注用户管理Service
 * @author ldk
 * @version 2017-10-13
 */
@Service
@Transactional(readOnly = true)
public class WxSubscriberService extends CrudService<WxSubscriberDao, WxSubscriber> {
	
	@Autowired
	private WxServiceaccountService wxServiceaccountService;
	@Autowired
	private WxUserinfoService wxUserinfoService;
	
	public WxSubscriber get(String id) {
		return super.get(id);
	}
	
	public List<WxSubscriber> findList(WxSubscriber wxSubscriber) {
		return super.findList(wxSubscriber);
	}
	
	public Page<WxSubscriber> findPage(Page<WxSubscriber> page, WxSubscriber wxSubscriber) {
		return super.findPage(page, wxSubscriber);
	}
	
	@Transactional(readOnly = false)
	public void save(WxSubscriber wxSubscriber) {
		System.err.println(wxSubscriber.getHeadImgUrl());
		super.save(wxSubscriber);
	}
	
	@Transactional(readOnly = false)
	public void delete(WxSubscriber wxSubscriber) {
		super.delete(wxSubscriber);
	}

	public WxSubscriber findByOpenId(String openId) {
		return dao.findByOpenId(openId);
	}
	
	/**
	 * 根据接收微信Xml对象获取关注用户对象。
	 * @param receiveXmlEntity
	 * @return
	 */
	public WxSubscriber getWxSubscriberBy(ReceiveXmlEntity receiveXmlEntity) {
		String openId = receiveXmlEntity.getFromUserName();
		String serviceId = receiveXmlEntity.getToUserName();
		return this.getWxSubscriberBy(openId, serviceId);
	}
	
	/**
	 * 根据openId，serviceId获取关注用户对象。
	 * @param openId
	 * @param serviceId
	 * @return
	 */
	public WxSubscriber getWxSubscriberBy(String openId, String serviceId) {
		System.err.println("微信号：" + serviceId);
		
		//查询关注用户表中是否有该用户
		WxSubscriber curSubscriber = this.findByOpenId(openId);
		if(curSubscriber == null) {
			//不存在该对象时，则new一个该对象，填充好ServiceId，openId字段。
			curSubscriber = new WxSubscriber();
			curSubscriber.setServiceId(serviceId);
			curSubscriber.setOpenId(openId);
		}
		return curSubscriber;
	}
	
	/**
	 * 更新微信用户对应的 关注表和绑定表。
	 * @param userBehavior 用户行为
	 * @param curSubscriber 用户关注对象（主要用户信息相关字段都已经设置完毕，这些字段将更新到用户绑定表）；
	 * 						不存在该对象时，则new一个该对象，填充好ServiceId，openId字段。
	 * @param user 可为null，绑定行为所需传参，表示绑定的系统用户。
	 */
	@Transactional(readOnly = false)
	public void updateWXUserBy(UserBehavior userBehavior, WxSubscriber curSubscriber, User user) {
		//设定系统管理员，默认用户是系统管理员添加的
		User administrator = new User();
		administrator.setId("1");
		System.err.println(curSubscriber.getSubscriberId());
		if(curSubscriber.getSubscriberId() != null && !curSubscriber.getSubscriberId().equals(0)) {
			//查询数据库中的关注用户对象，为后续确定是否需要update做准备。
			WxSubscriber oldWxSubscriber = this.get(curSubscriber.getSubscriberId().toString());
			
			/**
			 * 说明当前已存在该关注用户了。查询绑定用户表判断该用户是否已绑定。
			 */
			WxUserinfo wxUserInfo = wxUserinfoService.findBySubscriberId(curSubscriber.getSubscriberId());
			
			if(wxUserInfo != null) {
				/**
				 * 说明是绑定用户
				 */
				switch(userBehavior) {
				case FOCUS_OUT:
					//说明是绑定用户执行取消关注行为，更新关注用户表和绑定用户中间表 状态值为1--未关注已绑定
					curSubscriber.setStatus("1");
					if(wxUserInfo.isNeedUpdate(curSubscriber, null)) {
						wxUserInfo.initBySubscriber(curSubscriber);
						wxUserinfoService.save(wxUserInfo); //更新绑定用户信息
					}
					if(!curSubscriber.equals(oldWxSubscriber)) {
						//改变了才更新。
						this.save(curSubscriber); //更新关注用户信息
					}
					break;
				case FOCUS_ON:
					//说明是绑定用户执行关注行为，更新关注用户表和绑定用户中间表 状态值为3--已关注已绑定
					curSubscriber.setStatus("3");
					if(wxUserInfo.isNeedUpdate(curSubscriber, null)) {
						wxUserInfo.initBySubscriber(curSubscriber);
						wxUserinfoService.save(wxUserInfo); //更新绑定用户信息
					}
					if(!curSubscriber.equals(oldWxSubscriber)) {
						//改变了才更新。
						this.save(curSubscriber); //更新关注用户信息
					}
					break;
				case BIND:
					//前端提示：该用户已进行过绑定！
					System.err.println("抱歉！您已经绑定过了！");
					break;
				}
			} else {
				/**
				 * 说明是尚未绑定用户
				 */
				switch(userBehavior) {
				case FOCUS_OUT:
					//说明尚未绑定用户执行取消关注行为，更新关注用户表的状态值为0--未关注未绑定
					curSubscriber.setStatus("0");
					if(!curSubscriber.equals(oldWxSubscriber)) {
						//改变了才更新。
						this.save(curSubscriber); //更新关注用户信息
					}
					break;
				case FOCUS_ON:
					//说明尚未绑定用户执行取消关注行为，更新关注用户表的状态值为2--已关注未绑定
					curSubscriber.setStatus("2");
					if(!curSubscriber.equals(oldWxSubscriber)) {
						//改变了才更新。
						this.save(curSubscriber);
					}
					break;
				case BIND:
					//说明尚未绑定用户执行绑定行为，需要判断当前用户到底关注了没有。暂时先默认为已关注。更新关注用户表的状态值为3--已关注已绑定
					//判断是否已设定绑定系统用户
					curSubscriber.initByUser(user);
					System.err.println("已设定绑定系统用户：" + curSubscriber.getSlUserId());
					//需要判断当前用户到底关注了没有。。暂时先默认为已关注。
					curSubscriber.setStatus("3"); //3--已关注已绑定
					if(!curSubscriber.equals(oldWxSubscriber)) {
						//改变了才更新。
						this.save(curSubscriber); //更新关注用户
					}
					
					wxUserInfo = new WxUserinfo();
					wxUserInfo.initBySubscriberAndUser(curSubscriber, user);
					wxUserinfoService.save(wxUserInfo); //保存绑定用户
					break;
				}
			}
		} else {
			/**
			 * 说明当前不存在该关注用户。添加该用户到关注用户表
			 */
			//取微信服务号
			WxServiceaccount wxServiceaccount = wxServiceaccountService.findByServiceId(curSubscriber.getServiceId());
			if(wxServiceaccount == null) {
				//说明系统中没有该服务号。
				System.err.println("系统中暂无该服务号！");
				return;
			}
			
			System.out.println(curSubscriber.getServiceId() + "---" + wxServiceaccount);
			//同时判断是否已设定serviceId和openId
			System.err.println("已设定serviceId：" + curSubscriber.getServiceId() + ";已设定openId：" + curSubscriber.getOpenId());
			curSubscriber.setSaId(wxServiceaccount.getSaId());
			curSubscriber.setServiceId(wxServiceaccount.getServiceId());
			
			curSubscriber.setSubscribe("0");
			curSubscriber.setCreateBy(administrator);
			curSubscriber.setCreateDate(new Date());
			curSubscriber.setUpdateBy(administrator);
			curSubscriber.setUpdateDate(new Date());
			
			switch(userBehavior) {
			case FOCUS_OUT:
				curSubscriber.setStatus("0"); //未关注未绑定
				this.save(curSubscriber);
				break;
			case FOCUS_ON:
				curSubscriber.setStatus("2"); //已关注未绑定
				this.save(curSubscriber);
				break;
			case BIND:
				//判断是否已设定绑定系统用户
				curSubscriber.initByUser(user);
				System.err.println("已设定绑定系统用户：" + curSubscriber.getSlUserId());
				//需要判断当前用户到底关注了没有。。暂时先默认为已关注。
				curSubscriber.setStatus("3"); //3--已关注已绑定
				this.save(curSubscriber); //保存关注用户
				
				WxUserinfo wxUserInfo = new WxUserinfo();
				wxUserInfo.initBySubscriberAndUser(curSubscriber, user);
				wxUserinfoService.save(wxUserInfo); //保存绑定用户
				break;
			}
			
			System.err.println("获取自增的id值：" + curSubscriber.getSubscriberId());
			
			logger.info("----------------用户：openId=" + curSubscriber.getOpenId() + " 已加入关注用户表!-----------------");
		}
	}
	
}