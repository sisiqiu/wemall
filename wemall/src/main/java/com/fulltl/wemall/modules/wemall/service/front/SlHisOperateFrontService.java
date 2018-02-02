package com.fulltl.wemall.modules.wemall.service.front;

import java.util.HashMap
;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.service.BaseService;
import com.fulltl.wemall.modules.cms.entity.Article;
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;
import com.fulltl.wemall.modules.cms.service.ArticleService;
import com.fulltl.wemall.modules.cms.service.CmsFeedbackService;
import com.fulltl.wemall.modules.cms.service.CmsUserRegidService;
import com.fulltl.wemall.modules.sys.entity.SlSysAdvertise;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SlSysAdvertiseService;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;

/**
 * 运营，客服管理前端服务层
 * @author ldk
 *
 */
@Service
@Transactional(readOnly = true)
@Lazy(false)
public class SlHisOperateFrontService extends BaseService {
	@Autowired
	private SystemService systemService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private SlSysAdvertiseService slSysAdvertiseService;
	@Autowired
	private CmsUserRegidService cmsUserRegidService;
	@Autowired 
	private CmsFeedbackService cmsFeedbackService ;
	
	/**
	 * 根据栏目编号进行分页查询资讯
	 */
	public Map<String ,Object> getArticleListByCategory(Article article,HttpServletRequest request, HttpServletResponse response) {
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
			System.out.println("pageNo="+pageNo);
			System.out.println("pageSize="+pageSize);
		} catch (NumberFormatException e) {
			logger.error("缺少页码和每页文章条数！", e);
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页文章条数！");
			return map;
		}
		//根据栏目编号分页查询资讯
		String categoryId = WebUtils.getCleanParam(request, "category.id");
		if(StringUtils.isEmpty(categoryId)) {
			map.put("ret", "-1");
			map.put("retMsg", "缺少文章类别id！");
			return map;
		}
		Page<Article> page=articleService.findPage(new Page<Article>(pageNo, pageSize), article);
		List<Article> articleList = page.getList();
		List<Map<String, Object>> articles = Lists.newArrayList();
		for(Article art : articleList) {
			Map<String, Object> homePageArticleMap = art.getHomePageArticle();
			homePageArticleMap.put("user", art.getUser());
			articles.add(homePageArticleMap);
		}
		map.put("list", articles);
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 查询单个资讯
	 */
	public Map<String ,Object> getArticleById(Article article,HttpServletRequest request, HttpServletResponse response) {
		//根据栏目编号分页查询资讯
		Map<String ,Object> map=new HashMap<String, Object>();
		if(StringUtils.isBlank(article.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "文章id不能为空！");
			return map;
		}
		Article article2=articleService.findArticle(article.getId());
		map.put("data",article2);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 查询广告列表
	 */
	public Map<String ,Object> getAdvertiseList(SlSysAdvertise slSysAdvertise,HttpServletRequest request, HttpServletResponse response) {
		String orderBy = WebUtils.getCleanParam(request, "orderBy");
		Integer pageNo = null;
		Integer pageSize  = null;
		Map<String ,Object> map=new HashMap<String, Object>();
		try {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			logger.error("缺少页码和每页文章条数！", e);
			map.put("ret", "-1");
			map.put("retMsg", "缺少页码和每页文章条数！");
			return map;
		}
		//根据栏目编号分页查询资讯
		Page<SlSysAdvertise> page=slSysAdvertiseService.findPage(new Page<SlSysAdvertise>(pageNo, pageSize, orderBy), slSysAdvertise);
		map.put("list", page.getList());
		map.put("count", page.getCount());
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 查询单个广告
	 */
	public Map<String ,Object> getAdvertiseById(SlSysAdvertise slSysAdvertise,HttpServletRequest request, HttpServletResponse response) {
		//根据栏目编号分页查询资讯
		Map<String ,Object> map=new HashMap<String, Object>();
		if(StringUtils.isBlank(slSysAdvertise.getId())) {
			map.put("ret", "-1");
			map.put("retMsg", "广告栏id不能为空！");
			return map;
		}
		SlSysAdvertise slSysAdvertise2=slSysAdvertiseService.get(slSysAdvertise);
		map.put("data",slSysAdvertise2);
		map.put("ret", "0");
		map.put("retMsg", "获取成功");
		return map;
	}
	
	/**
	 * 新增一条用户反馈意见数据
	 * @param cmsFeedback
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> addFeedback(CmsFeedback cmsFeedback) {
		Map<String, Object> retMap=new HashMap<String, Object>();
		User user = UserUtils.getUser();
		retMap = systemService.checkCurrentUser(user);
		if(!"0".equals(retMap.get("ret"))) return retMap;
		
		cmsFeedback.setUser(user);
		retMap = beanValidator(cmsFeedback);
		/*if(RegExpValidatorUtil.isSpecialChar(cmsFeedback.getOpinionText())) {
			retMap.put("ret", "60046");
    		retMap.put("retMsg", "意见建议内容不可含有特殊字符！");
    		return retMap;
		}*/
		
		if("0".equals(retMap.get("ret"))) {
			cmsFeedbackService.save(cmsFeedback);
			retMap.put("retMsg", "添加成功！");
		}
 		return retMap;
	}
}
