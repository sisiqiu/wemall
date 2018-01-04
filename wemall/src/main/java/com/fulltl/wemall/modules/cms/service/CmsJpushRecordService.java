/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.CrudService;
import com.fulltl.wemall.common.utils.DateUtils;
import com.fulltl.wemall.modules.cms.dao.CmsJpushRecordDao;
import com.fulltl.wemall.modules.cms.entity.CmsJpushRecord;
import com.fulltl.wemall.modules.cms.entity.CmsPushTag;
import com.fulltl.wemall.modules.cms.entity.CmsUserRegid;
import com.fulltl.wemall.modules.his.util.jpush.JPush;
import com.fulltl.wemall.modules.his.util.jpush.JPushHelper;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * 极光推送历史记录管理Service
 * @author ldk
 * @version 2017-12-09
 */
@Service
@Transactional(readOnly = true)
public class CmsJpushRecordService extends CrudService<CmsJpushRecordDao, CmsJpushRecord> {
	
	@Autowired
	private CmsUserRegidService cmsUserRegidService;
	@Autowired
	private CmsPushTagService cmsPushTagService;
	@Autowired
	private SystemService systemService;
	
	public CmsJpushRecord get(String id) {
		return super.get(id);
	}
	
	public List<CmsJpushRecord> findList(CmsJpushRecord cmsJpushRecord) {
		return super.findList(cmsJpushRecord);
	}
	
	public Page<CmsJpushRecord> findPage(Page<CmsJpushRecord> page, CmsJpushRecord cmsJpushRecord) {
		return super.findPage(page, cmsJpushRecord);
	}
	
	@Transactional(readOnly = false)
	public Map<String, Object> saveAndPush(CmsJpushRecord cmsJpushRecord) {
		Map<String, Object> retMap = Maps.newHashMap();
		if(cmsJpushRecord.getIsNewRecord()) {
			//如果是新增数据
			JPush jPush = new JPush();
			jPush.setTitle(cmsJpushRecord.getTitle());
			jPush.setContent(cmsJpushRecord.getContent());
			if("1".equals(cmsJpushRecord.getType())) {
				//定时推送，设置推送时间
				jPush.setSendtime(DateUtils.formatDateTime(cmsJpushRecord.getScheduleTime()));
				//设置定时推送任务名称
				//jPush.setSchedulePushName(schedulePushName);	
			}
			//设置附加字段
			Map<String, String> extraMap = Maps.newHashMap();
			if(StringUtils.isNotBlank(cmsJpushRecord.getHref())) {
				extraMap.put("href", cmsJpushRecord.getHref());
			}
			if(StringUtils.isNotBlank(cmsJpushRecord.getExtra())) {
				extraMap.put("extra", cmsJpushRecord.getExtra());
			}
			jPush.setExtraMap(extraMap);
			//根据推送目标类型选择推送方式
			if("1".equals(cmsJpushRecord.getTargetType())) {
				//推送所有人
				retMap = new JPushHelper().pushAll(jPush);
			} else {
				//向指定的极光注册id列表RegistrationIds推送
				Set<String> registrationIds = getRegistrationIdsBy(cmsJpushRecord);
				System.err.println("registrationIds列表："+registrationIds);
				jPush.setRegistrationIds(registrationIds);
				retMap = new JPushHelper().pushWithRegistrationIds(jPush);
			}
			
			if("0".equals(retMap.get("ret"))) cmsJpushRecord.setStatus("1");
			else {
				//推送失败
				cmsJpushRecord.setStatus("2");
				cmsJpushRecord.setRemarks(retMap.get("retMsg").toString());
			}
		}
		
		super.save(cmsJpushRecord);
		if("0".equals(retMap.get("ret"))) {
			retMap.put("ret", "0");
			retMap.put("retMsg", "推送成功");
		}
		return retMap;
	}
	
	/**
	 * 根据cmsJpushRecord对象查询用户列表，获取用户对应的极光注册id列表
	 * @param cmsJpushRecord
	 * @return
	 */
	private Set<String> getRegistrationIdsBy(CmsJpushRecord cmsJpushRecord) {
		Set<String> registrationIds = Sets.newHashSet();
		Set<String> userIdSet = Sets.newHashSet();
		if("2".equals(cmsJpushRecord.getTargetType())) {
			//通过角色推送
			String roleIds = cmsJpushRecord.getRoleIds();
			List<String> roleIdList = Lists.newArrayList();
			if(StringUtils.isNotBlank(roleIds)) {
				roleIdList = Arrays.asList(roleIds.split(","));
			}
			//根据roleId列表查询用户列表
			List<String> userIds = systemService.getUserIdsByRoleIds(roleIdList);
			userIdSet.addAll(userIds);
		} else if("3".equals(cmsJpushRecord.getTargetType())) {
			//通过标签推送
			String tagIds = cmsJpushRecord.getTagIds();
			List<String> tagIdList = Lists.newArrayList();
			if(StringUtils.isNotBlank(tagIds)) {
				tagIdList = Arrays.asList(tagIds.split(","));
			}
			//根据tagIds列表查询用户列表
			List<CmsPushTag> tagList = cmsPushTagService.findListByIds(tagIdList);
			for(CmsPushTag pushTag: tagList) {
				if(StringUtils.isNotBlank(pushTag.getUserIds())) {
					userIdSet.addAll(Sets.newHashSet(pushTag.getUserIds().split(",")));
				}
			}
		} else if("4".equals(cmsJpushRecord.getTargetType())) {
			//通过用户列表推送
			String userIds = cmsJpushRecord.getUserIds();
			if(StringUtils.isNotBlank(userIds)) {
				userIdSet = Sets.newHashSet(userIds.split(","));
			}
		}
		
		System.err.println("用户id列表："+userIdSet);
		//根据用户id列表查询极光注册id列表
		List<CmsUserRegid> cmsUserRegidList = cmsUserRegidService.findListByUserIds(userIdSet);
		for(CmsUserRegid userReg: cmsUserRegidList) {
			registrationIds.add(userReg.getRegistrationId());
		}
		
		return registrationIds;
	}

	@Transactional(readOnly = false)
	public void delete(CmsJpushRecord cmsJpushRecord) {
		super.delete(cmsJpushRecord);
	}
	
}