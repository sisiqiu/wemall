/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.sys.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fulltl.wemall.common.mapper.JsonMapper;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.SpringContextHolder;
import com.fulltl.wemall.modules.sys.dao.OrderDictDao;
import com.fulltl.wemall.modules.sys.entity.OrderDict;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2013-5-29
 */
public class OrderDictUtils {
	
	private static OrderDictDao orderDictDao = SpringContextHolder.getBean(OrderDictDao.class);

	public static final String CACHE_ORDER_DICT_MAP = "orderDictMap";
	
	public static String getOrderDictLabel(String value, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			for (OrderDict dict : getOrderDictList(type)){
				if (type.equals(dict.getType()) && value.equals(dict.getValue())){
					return dict.getLabel();
				}
			}
		}
		return defaultValue;
	}
	
	public static String getOrderDictLabels(String values, String type, String defaultValue){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)){
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")){
				valueList.add(getOrderDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getOrderDictValue(String label, String type, String defaultLabel){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			for (OrderDict dict : getOrderDictList(type)){
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())){
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}
	
	public static List<OrderDict> getOrderDictList(String type){
		@SuppressWarnings("unchecked")
		Map<String, List<OrderDict>> dictMap = (Map<String, List<OrderDict>>)CacheUtils.get(CACHE_ORDER_DICT_MAP);
		if (dictMap==null){
			dictMap = Maps.newHashMap();
			for (OrderDict dict : orderDictDao.findAllList(new OrderDict())){
				List<OrderDict> dictList = dictMap.get(dict.getType());
				if (dictList != null){
					dictList.add(dict);
				}else{
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_ORDER_DICT_MAP, dictMap);
		}
		List<OrderDict> dictList = dictMap.get(type);
		if (dictList == null){
			dictList = Lists.newArrayList();
		}
		return dictList;
	}
	
	/**
	 * 返回字典列表（JSON）
	 * @param type
	 * @return
	 */
	public static String getOrderDictListJson(String type){
		return JsonMapper.toJsonString(getOrderDictList(type));
	}
	
}
