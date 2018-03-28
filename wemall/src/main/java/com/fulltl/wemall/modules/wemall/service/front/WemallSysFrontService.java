package com.fulltl.wemall.modules.wemall.service.front;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.FileUtils;
import com.fulltl.wemall.modules.cms.entity.Category;
import com.fulltl.wemall.modules.cms.entity.Link;
import com.fulltl.wemall.modules.cms.service.ArticleService;
import com.fulltl.wemall.modules.cms.service.LinkService;
import com.fulltl.wemall.modules.pay.util.DataStorageUtil;
import com.fulltl.wemall.modules.sys.entity.Dict;
import com.fulltl.wemall.modules.sys.service.SlSysAdvertiseService;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.DictUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.fulltl.wemall.modules.wemall.entity.WemallItem;
import com.fulltl.wemall.modules.wemall.service.WemallItemService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 系统管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class WemallSysFrontService extends BaseService {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private WemallItemService wemallItemService;
	@Autowired
	private WemallUserFrontService slHisUserFrontService;
	@Autowired
	private SlSysAdvertiseService slSysAdvertiseService;
	
	/**
	 * 根据字典类型获取字典列表的接口。
	 * @param request
	 * @return
	 */
	public Map<String, Object> getDictListByType(HttpServletRequest request) {
		Map<String ,Object> map=new HashMap<String, Object>();
		//判断类型是否为空
		String type = WebUtils.getCleanParam(request, "type");
		if(StringUtils.isEmpty(type)) {
			map.put("ret", "-1");
			map.put("retMsg", "字典类型不能为空！");
			return map;
		}

		List<Dict> dictList = DictUtils.getDictList(type);
		
		map.put("list", dictList);
		map.put("count", dictList.size());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}

	/**
	 * 获取主页数据
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> getHomePageData(HttpServletRequest request, HttpServletResponse response) {
		Map<String ,Object> map = new HashMap<String, Object>();
		Link link = new Link();
		link.setCategory(new Category(DataStorageUtil.WEMALL_HOME_BANNA_CATICLE_ID));//对应类别为商城首页大型导航栏
		//首页banna图部分
		List<Link> bannaArticlesList = linkService.findList(link);
		List<Map<String, Object>> bannaArticles = Lists.newArrayList();
		for(Link banna : bannaArticlesList) {
			bannaArticles.add(banna.getHomePageLink());
		}
		//小型导航栏部分
		link.setCategory(new Category(DataStorageUtil.WEMALL_NAV_CATICLE_ID));//对应类别为商城首页小型导航栏
		List<Link> navBarsList = linkService.findList(link);
		List<Map<String, Object>> navBars = Lists.newArrayList();
		for(Link nav : navBarsList) {
			navBars.add(nav.getHomePageLink());
		}
		//中型导航栏部分
		link.setCategory(new Category(DataStorageUtil.WEMALL_MIDDLE_NAV_CATICLE_ID));//对应类别为商城首页中型导航栏
		List<Link> middleNavBarsList = linkService.findList(link);
		List<Map<String, Object>> middleNavBars = Lists.newArrayList();
		for(Link nav : middleNavBarsList) {
			middleNavBars.add(nav.getHomePageLink());
		}
		
		//新品商品部分
		WemallItem queryNewItem = new WemallItem();
		queryNewItem.setIsNew(1);//新品
		queryNewItem.setIsOnShelf(1);//上架
		
		Page<WemallItem> newPage = wemallItemService.findPage(new Page<WemallItem>(0, 10), queryNewItem);
		List<Map<String, Object>> newGoodsList = Lists.newArrayList();
		for(WemallItem entity : newPage.getList()) {
			newGoodsList.add(entity.getSmallEntityMap());
		}
		
		//推荐商品部分
		WemallItem queryRecommendItem = new WemallItem();
		queryRecommendItem.setIsRecommend(1);//推荐
		queryRecommendItem.setIsOnShelf(1);//上架
		
		Page<WemallItem> recommendPage = wemallItemService.findPage(new Page<WemallItem>(0, 10), queryRecommendItem);
		List<Map<String, Object>> recommendGoodsList = Lists.newArrayList();
		for(WemallItem entity : recommendPage.getList()) {
			recommendGoodsList.add(entity.getSmallEntityMap());
		}
		
		//用户端公告部分
		//List<Map<String, Object>> noticeList = articleService.getHomePageArticlesByCatId(1, 6, DataStorageUtil.USERNOTICE_CATICLE_ID);
		
		//商品列表部分
		List<Map<String, Object>> goodsList = Lists.newArrayList();
		Map<String, Object> newGoodsMap = Maps.newHashMap();
		newGoodsMap.put("topurl", "/static/images/wemall/newGoods.jpg");
		newGoodsMap.put("goodsList", newGoodsList);
		newGoodsMap.put("href", "");
		
		Map<String, Object> recommendGoodsMap = Maps.newHashMap();
		recommendGoodsMap.put("topurl", "/static/images/wemall/recommendGoods.jpg");
		recommendGoodsMap.put("goodsList", recommendGoodsList);
		recommendGoodsMap.put("href", "");
		
		goodsList.add(newGoodsMap);
		goodsList.add(recommendGoodsMap);
		
		//首页banna图部分
		map.put("banna", bannaArticles);
		//首页小型导航栏部分
		map.put("navBars", navBars);
		//首页中型导航栏部分
		map.put("middleNavBars", middleNavBars);
		//新品商品部分
		//map.put("newGoodsList", newGoodsList);
		//推荐商品部分
		//map.put("recommendGoodsList", recommendGoodsList);
		
		map.put("goodsList", goodsList);
		//客服电话部分
		map.put("servicePhone", getConfig("servicePhone"));
		//map.put("moreExpertsUrl", Global.getConfig("moreExpertsUrl"));
		//map.put("moreHealthyArticlesUrl", Global.getConfig("moreHealthyArticlesUrl"));
		
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 上传文件处理。
	 * @param file
	 * @param request
	 * @return
	 */
	public Map<String, Object> uploadFile(MultipartFile file, HttpServletRequest request) {
		Map<String ,Object> retMap = Maps.newHashMap();
		String path = request.getSession().getServletContext().getRealPath(""); 
        String basePath = Global.getConfig("uploadBasePath");
        path = path + basePath;
        path = FileUtils.path(path);
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        if(file.getSize() > Long.parseLong(Global.getConfig("web.maxUploadSize"))) {
        	retMap.put("ret", "60025");
            retMap.put("retMsg", "上传失败，文件过大！");
    		return retMap;
        }
        //String fileName = file.getOriginalFilename();
        String fileName = new Date().getTime()+"." + suffix;
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
  
        //保存  
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
        	logger.error("上传文件出错！", e);
        	retMap.put("ret", "-1");
            retMap.put("retMsg", "上传文件出错！");
    		return retMap;
        }
        String filePath = path + "/" + fileName;
        String loginName = UserUtils.getUser().getLoginName();
        logger.info(loginName + "用户，执行文件上传，上传路径：" + filePath);
        retMap.put("fileUrl", basePath + "/" + fileName);
        retMap.put("ret", "0");
        retMap.put("retMsg", "上传成功");
        return retMap;
	}
	
	/**
	 * 上传文件处理。并更新当前用户头像
	 * @param file
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> uploadUserPhoto(MultipartFile file, HttpServletRequest request) {
		Map<String ,Object> retMap = Maps.newHashMap();
		String type = WebUtils.getCleanParam(request, "type");
		retMap = uploadFile(file, request);
		if("0".equals(retMap.get("ret"))) {
			retMap = slHisUserFrontService.updateUserPhoto(retMap.get("fileUrl").toString(), type);
		}
        return retMap;
	}
	
	/**
	 * 获取微商城配置集中的配置项
	 * @param key
	 * @return
	 */
	public Object getConfig(String key) {
		Map<String, String> wemallGlobalConfig = Global.getWemallGlobalConfig();
        if(wemallGlobalConfig.isEmpty()) {
        	//从数据库中去取,初始化app页面配置到Global缓存中
        	slSysAdvertiseService.initAppUrlConfig();
        	wemallGlobalConfig = Global.getWemallGlobalConfig();
        }
        return wemallGlobalConfig.get(key);
	}
	
	/**
	 * 获取系统配置的单个单个的独立url链接地址。
	 * @param request
	 * @return
	 */
	public Map<String, Object> getConfigUrls(HttpServletRequest request) {
		Map<String ,Object> retMap = Maps.newHashMap();
		Map<String, String> wemallGlobalConfig = Global.getWemallGlobalConfig();
        if(wemallGlobalConfig.isEmpty()) {
        	//从数据库中去取,初始化app页面配置到Global缓存中
        	slSysAdvertiseService.initAppUrlConfig();
        	wemallGlobalConfig = Global.getWemallGlobalConfig();
        }
        retMap.put("ret", "0");
        retMap.put("retMsg", "获取成功！");
        retMap.put("data", wemallGlobalConfig);
		/*SlSysAdvertise slSysAdvertise = slSysAdvertiseService.get("1");
		if(slSysAdvertise == null) {
			retMap.put("ret", "-1");
	        retMap.put("retMsg", "对应配置为空！");
			return retMap;
		}
		retMap.put("ret", "0");
        retMap.put("retMsg", "获取成功！");
        retMap.put("data", new Gson().fromJson(slSysAdvertise.getAdBody(), Map.class));*/
		return retMap;
	}

	/**
	 * 获取系统配置的单个单个的独立url链接地址。
	 * @param request
	 * @return
	 */
	public Map<String, Object> getConfigUrlsByType(HttpServletRequest request) {
		Map<String ,Object> retMap = Maps.newHashMap();
		String type = WebUtils.getCleanParam(request, "type");
		if(StringUtils.isEmpty(type)) {
			retMap.put("ret", "-1");
			retMap.put("retMsg", "类型不能为空！");
			return retMap;
		}
		Map<String, String> hisGlobalConfig = Global.getGlobalConfigByType(type);
		
        if(hisGlobalConfig.isEmpty()) {
        	//从数据库中去取,初始化app页面配置到Global缓存中
        	slSysAdvertiseService.initAppUrlConfig();
        	hisGlobalConfig = Global.getGlobalConfigByType(type);
        }
        retMap.put("ret", "0");
        retMap.put("retMsg", "获取成功！");
        retMap.put("data", hisGlobalConfig);
		return retMap;
	}

}
