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
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.common.utils.FileUtils;
import com.fulltl.wemall.modules.cms.entity.Category;
import com.fulltl.wemall.modules.cms.entity.Link;
import com.fulltl.wemall.modules.cms.service.ArticleService;
import com.fulltl.wemall.modules.cms.service.LinkService;
import com.fulltl.wemall.modules.his.util.DataStorageUtil;
import com.fulltl.wemall.modules.sys.entity.Dict;
import com.fulltl.wemall.modules.sys.service.SlSysAdvertiseService;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.DictUtils;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 系统管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
public class SlHisSysFrontService extends BaseService {
	
	@Autowired
	private SystemService systemService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SlHisUserFrontService slHisUserFrontService;
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
	 * 获取用户端主页数据
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> getUserHomePageData(HttpServletRequest request, HttpServletResponse response) {
		Map<String ,Object> map = new HashMap<String, Object>();
		String hospitalId = WebUtils.getCleanParam(request, "hospitalId");
		if(StringUtils.isEmpty(hospitalId)) {
			map.put("ret", "60022");
			map.put("retMsg", "缺少医院id！");
			return map;
		}
		Link link = new Link();
		link.setDelFlag("0");//查询发布的
		link.setCategory(new Category(DataStorageUtil.USER_HOME_BANNA_CATICLE_ID));//对应类别为banna图
		//首页banna图部分
		List<Link> bannaArticlesList = linkService.findList(link);
		List<Map<String, Object>> bannaArticles = Lists.newArrayList();
		for(Link banna : bannaArticlesList) {
			bannaArticles.add(banna.getHomePageLink());
		}
		//导航栏部分
		link.setCategory(new Category(DataStorageUtil.USER_NAV_CATICLE_ID));//对应类别为用户端导航栏
		List<Link> navArticlesList = linkService.findList(link);
		List<Map<String, Object>> navArticles = Lists.newArrayList();
		for(Link nav : navArticlesList) {
			navArticles.add(nav.getHomePageLink());
		}
		//健康资讯部分
		List<Map<String, Object>> articleList = articleService.getHomePageArticlesByCatId(1, 6, DataStorageUtil.HEALTHYART_CATICLE_ID);
		Map<String, Object> articlesMap = Maps.newHashMap();
		articlesMap.put("first", articleList.get(0));
		articleList.remove(0);
		for(Map<String, Object> articleMap : articleList) {
			articleMap.remove("image");
			articleMap.remove("description");
		}
		articlesMap.put("others", articleList);
		//用户端公告部分
		List<Map<String, Object>> noticeList = articleService.getHomePageArticlesByCatId(1, 6, DataStorageUtil.USERNOTICE_CATICLE_ID);
		
		//首页banna图部分
		map.put("banna", bannaArticles);
		//导航栏部分
		map.put("navBar", navArticles);
		//健康资讯部分
		map.put("articlesMap", articlesMap);
		//用户端公告部分
		map.put("noticeList", noticeList);
		map.put("moreExpertsUrl", Global.getConfig("moreExpertsUrl"));
		map.put("moreHealthyArticlesUrl", Global.getConfig("moreHealthyArticlesUrl"));
		
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 获取医生端主页数据
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> getDoctorHomePageData(HttpServletRequest request, HttpServletResponse response) {
		Map<String ,Object> map = new HashMap<String, Object>();
		String userId = WebUtils.getCleanParam(request, "userId");
		if(StringUtils.isEmpty(userId)) {
			map.put("ret", "60018");
			map.put("retMsg", "用户id不能为空！");
			return map;
		}
		//医生端导航栏部分
		Link link = new Link(new Category(DataStorageUtil.DOCTOR_NAV_CATICLE_ID));//对应类别为医生端导航栏
		List<Link> navArticlesList = linkService.findList(link);
		List<Map<String, Object>> navArticles = Lists.newArrayList();
		for(Link nav : navArticlesList) {
			navArticles.add(nav.getHomePageLink());
		}
		//医生端端公告部分
		List<Map<String, Object>> noticeList = articleService.getHomePageArticlesByCatId(1, 6, DataStorageUtil.DOCNOTICE_CATICLE_ID);
		
		//医生端导航栏部分
		map.put("navBar", navArticles);
		//医生端公告部分
		map.put("noticeList", noticeList);
		
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
	 * 获取系统配置的单个单个的独立url链接地址。
	 * @param request
	 * @return
	 */
	public Map<String, Object> getConfigUrls(HttpServletRequest request) {
		Map<String ,Object> retMap = Maps.newHashMap();
		Map<String, String> hisUserGlobalConfig = Global.getHisUserGlobalConfig();
        if(hisUserGlobalConfig.isEmpty()) {
        	//从数据库中去取,初始化app页面配置到Global缓存中
        	slSysAdvertiseService.initAppUrlConfig();
        	hisUserGlobalConfig = Global.getHisUserGlobalConfig();
        }
        retMap.put("ret", "0");
        retMap.put("retMsg", "获取成功！");
        retMap.put("data", hisUserGlobalConfig);
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
		Map<String, String> hisGlobalConfig = Global.getHisGlobalConfigByType(type);
		
        if(hisGlobalConfig.isEmpty()) {
        	//从数据库中去取,初始化app页面配置到Global缓存中
        	slSysAdvertiseService.initAppUrlConfig();
        	hisGlobalConfig = Global.getHisGlobalConfigByType(type);
        }
        retMap.put("ret", "0");
        retMap.put("retMsg", "获取成功！");
        retMap.put("data", hisGlobalConfig);
		return retMap;
	}

}
