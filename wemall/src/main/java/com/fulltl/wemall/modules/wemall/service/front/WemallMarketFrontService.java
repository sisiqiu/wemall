package com.fulltl.wemall.modules.wemall.service.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.DataEntity;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity.ActivityTypeEnum;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;
import com.google.common.collect.Lists;

/**
 * 营销管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class WemallMarketFrontService extends BaseService {

	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	
	/**
	 * 根据活动类别，获取当前未过期的活动列表的接口。
	 * @param request
	 * @return
	 */
	public Map<String, Object> findListByActivityType(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String activityType = WebUtils.getCleanParam(request, "activityType");
		if(StringUtils.isBlank(activityType)) {
			map.put("ret", "-1");
			map.put("retMsg", "活动类别不能为空！");
			return map;
		}
		ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getEnumByValue(Integer.parseInt(activityType));
		if(activityTypeEnum == null) {
			map.put("ret", "-1");
			map.put("retMsg", "活动类别错误！");
			return map;
		}
		List<DataEntity> list = wemallItemActivityService.findListByActivityType(activityTypeEnum);
		map.put("list", list);
		map.put("count", list.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 根据活动id和活动类别，获取参与该活动的商品列表。
	 * @param request
	 * @return
	 */
	public Map<String, Object> findItemsByActivity(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		String activityId = WebUtils.getCleanParam(request, "activityId");
		String activityType = WebUtils.getCleanParam(request, "activityType");
		
		if(StringUtils.isBlank(activityId) || StringUtils.isBlank(activityType)) {
			map.put("ret", "-1");
			map.put("retMsg", "活动id和活动类别不能为空！");
			return map;
		}
		ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getEnumByValue(Integer.parseInt(activityType));
		if(activityTypeEnum == null) {
			map.put("ret", "-1");
			map.put("retMsg", "活动类别错误！");
			return map;
		}
		
		List<WemallItem> list = wemallItemActivityService.findItemsByActId(activityId, activityTypeEnum);
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallItem entity : list) {
			dataList.add(entity.getSmallEntityMap());
		}
		map.put("list", dataList);
		map.put("count", dataList.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
}
