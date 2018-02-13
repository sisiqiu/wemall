package com.fulltl.wemall.modules.wemall.service.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.entity.WemallItemSort;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.fulltl.wemall.modules.wemall.service.WemallItemSortService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 商品前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class SlHisItemFrontService extends BaseService {

	@Autowired
	private WemallItemSortService wemallItemSortService;
	@Autowired
	private WemallItemService wemallItemService;
	
	/**
	 * 获取商品类别列表
	 * @param wemallItemSort
	 * @param request 
	 * @return
	 */
	public Map<String, Object> getItemSortList(WemallItemSort wemallItemSort, HttpServletRequest request) {
		Map<String, Object> map = Maps.newHashMap();
		List<WemallItemSort> findList = wemallItemSortService.findList(wemallItemSort);
		
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallItemSort entity : findList) {
			dataList.add(entity.getSmallEntityMap());
		}
		map.put("list", dataList);
		map.put("count", dataList.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取商品列表
	 * @param wemallItem
	 * @param request 
	 * @return
	 */
	public Map<String, Object> getItemList(WemallItem wemallItem, HttpServletRequest request) {
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页条数！");
			return map;
		}
		Page<WemallItem> page = wemallItemService.findPage(new Page<WemallItem>(pageNo, pageSize), wemallItem);
		List<Map<String, Object>> dataList = Lists.newArrayList();
		for(WemallItem entity : page.getList()) {
			dataList.add(entity.getSmallEntityMap());
		}
		map.put("list", dataList);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取商品详情
	 * @param wemallItem
	 * @param request
	 * @return
	 */
	public Map<String, Object> getItemDetail(WemallItem wemallItem, HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		if(StringUtils.isBlank(wemallItem.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "商品id不能为空！");
			return map;
		}
		WemallItem entity = wemallItemService.get(wemallItem);
		map.put("data",entity);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

}
