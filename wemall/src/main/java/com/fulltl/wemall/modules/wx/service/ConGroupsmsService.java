/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.wx.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.modules.wx.dao.ConGroupsmsDao;
import com.fulltl.wemall.modules.wx.entity.ConGroupsms;

/**
 * 群发短信管理Service
 * @author ldk
 * @version 2017-10-24
 */
@Service
@Transactional(readOnly = true)
public class ConGroupsmsService extends CrudService<ConGroupsmsDao, ConGroupsms> {

	public ConGroupsms get(String id) {
		return super.get(id);
	}
	
	public List<ConGroupsms> findList(ConGroupsms conGroupsms) {
		return super.findList(conGroupsms);
	}
	
	public Page<ConGroupsms> findPage(Page<ConGroupsms> page, ConGroupsms conGroupsms) {
		return super.findPage(page, conGroupsms);
	}
	
	@Transactional(readOnly = false)
	public void save(ConGroupsms conGroupsms) {
		super.save(conGroupsms);
	}
	
	/**
	 * 执行群发短信保存的服务。
	 * @param conGroupsms
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> saveWithSendSMS(ConGroupsms conGroupsms) {
		Map<String, Object> retMap = Maps.newHashMap();
		if (conGroupsms.getIsNewRecord()){
			conGroupsms.preInsert();
			conGroupsms.setStatus("0"); //初始为0--未发送
			dao.insert(conGroupsms);
			
			sendSMS(conGroupsms, retMap);
		} else {
			sendSMS(conGroupsms, retMap);
		}
		return retMap;
	}
	
	/**
	 * 执行发送短信，并填充结果map。
	 * @param conGroupsms
	 * @param retMap
	 */
	private void sendSMS(ConGroupsms conGroupsms, Map<String, Object> retMap) {
		//状态值status；0--未发送；1--已发送；2--发送失败。
		/**
		 * 判断实体对象的状态值，
		 * 若为1--已发送，或2--发送失败：则不再发送，返回前端“发送失败，暂不支持对已发群发短信执行再次发送，请重新添加群发短信！”。
		 * 若为0--未发送，或状态为空：则执行发送，将发送结果保存在状态值中（结果可能为1--已发送，2--发送失败）
		 */
		if(conGroupsms.getStatus().equals("1") || conGroupsms.getStatus().equals("2")) {
			retMap.put("ret", -1);
			retMap.put("retMsg", "发送失败，暂不支持对已发送过的群发短信执行再次发送，请重新添加群发短信！");
		} else {
			//执行群发短信
			SMSVerify smsVerify = new SMSVerify();
			Map<String, Object> result = smsVerify.sendSmsByGroupsms(conGroupsms);
			//将发送结果保存在状态值中
			if(result.get("ret").equals("200")) {
				retMap.put("ret", "200");
				retMap.put("retMsg", "群发短信成功");
				//更新对象的状态值为1--已发送。
				conGroupsms.setStatus("1");
				dao.update(conGroupsms);
			} else {
				retMap.put("ret", result.get("ret"));
				retMap.put("retMsg", result.get("retMsg"));
				//更新对象的状态值为2--发送失败。
				conGroupsms.setStatus("2");
				dao.update(conGroupsms);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ConGroupsms conGroupsms) {
		super.delete(conGroupsms);
	}
	
}