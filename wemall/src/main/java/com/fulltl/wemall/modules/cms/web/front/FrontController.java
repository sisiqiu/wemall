/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.fulltl.wemall.modules.cms.web.front;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.persistence.Page;
import com.fulltl.wemall.common.servlet.ValidateCodeServlet;
import com.fulltl.wemall.common.sms.sendmode.ccp.SMSVerify;
import com.fulltl.wemall.common.utils.FileUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.common.utils.SendMailUtil;
import com.fulltl.wemall.common.utils.StringUtils;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.cms.entity.Article;
import com.fulltl.wemall.modules.cms.entity.Category;
import com.fulltl.wemall.modules.cms.entity.CmsFeedback;
import com.fulltl.wemall.modules.cms.entity.Comment;
import com.fulltl.wemall.modules.cms.entity.Link;
import com.fulltl.wemall.modules.cms.entity.Site;
import com.fulltl.wemall.modules.cms.service.ArticleDataService;
import com.fulltl.wemall.modules.cms.service.ArticleService;
import com.fulltl.wemall.modules.cms.service.CategoryService;
import com.fulltl.wemall.modules.cms.service.CmsFeedbackService;
import com.fulltl.wemall.modules.cms.service.CmsFileresourceService;
import com.fulltl.wemall.modules.cms.service.CommentService;
import com.fulltl.wemall.modules.cms.service.LinkService;
import com.fulltl.wemall.modules.cms.service.SiteService;
import com.fulltl.wemall.modules.cms.utils.CmsUtils;
import com.fulltl.wemall.modules.sys.entity.Office;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.security.UsernamePasswordToken;
import com.fulltl.wemall.modules.sys.service.SystemService;
import com.fulltl.wemall.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 网站Controller
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleDataService articleDataService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CmsFeedbackService cmsFeedbackService;
    @Autowired
    private CmsFileresourceService cmsFileresourceService;
    
    /**
     * 网站首页
     */
    @RequestMapping
    public String index(Model model) {
       /* Site site = CmsUtils.getSite(Site.defaultSiteId());
        model.addAttribute("site", site);
        model.addAttribute("isIndex", true);*/
        return "redirect:" + adminPath;
        // return "modules/cms/front/themes/"+site.getTheme()+"/frontIndex";
    }

    /**
     * 注册页面和短信快速登录页面
     */
    @RequestMapping(value = "getArticles")
    @ResponseBody
    public String getArticles(HttpServletRequest request, HttpServletResponse response, Model model) {
        String ret = "-1";
        String retMsg = "操作失败";
        Map<String, Object> retMap = new HashMap<String, Object>();
        String categoryID = WebUtils.getCleanParam(request, "categoryID");

        List<Article> articleList = null;
        List<Map<String, String>> articleList2 = new ArrayList<Map<String, String>>();
        Map<String, String> articleMap = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (categoryID == null) {
            retMap.put("ret", ret);
            retMap.put("retMsg", retMsg);
            return getMapToJSON(retMap);
        } else {
            articleList = CmsUtils.getArticleList(Site.defaultSiteId(), categoryID);
            for (Article ar : articleList) {
                articleMap = new HashMap<String, String>();
                articleMap.put("title", ar.getTitle());
                articleMap.put("id", ar.getId());
                articleMap.put("createDate", sdf.format(ar.getCreateDate()));
                articleList2.add(articleMap);
            }
            retMap.put("areaInfo", articleList2);
            retMap.put("showNum", articleList2.size());
            ret = "0";
            retMsg = "操作成功";
        }

        return getMapToJSON(retMap);
    }

    /**
     * 注册页面和短信快速登录页面
     */
    @RequestMapping(value = "gotoArticle")
    public String gotoArticle(HttpServletRequest request, HttpServletResponse response, @RequestParam String articleID,
            Model model) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Article article = null;

        if (articleID == null) {
            return "";
        } else {
            article = CmsUtils.getArticleData(articleID);
            model.addAttribute("article", article);
            return "sanlen_website/footer/new-media-news-detail";
        }

    }

    /**
     * @param retMap
     * @return
     * @author kang.yang
     * @date 创建日期：2017年9月22日 下午2:57:28
     */
    private String getMapToJSON(Map<String, Object> retMap) {
        Gson gs = new Gson();

        return gs.toJson(retMap);
    }

    /**
     * 注册页面和短信快速登录页面
     */
    @RequestMapping(value = "register")
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
        String ret = "400";
        String retMsg = "操作失败";
        Map<String, Object> retMap = new HashMap<String, Object>();
        String mobile = WebUtils.getCleanParam(request, "mobile");
        String username = WebUtils.getCleanParam(request, "username");
        String verifyServID = WebUtils.getCleanParam(request, "verifyServID");
        String sms_code = WebUtils.getCleanParam(request, "sms_code");
        String loginFrom = WebUtils.getCleanParam(request, "loginFrom");
        String password = WebUtils.getCleanParam(request, "password");
        String loginname = WebUtils.getCleanParam(request, "loginname");
        String rememberMe = WebUtils.getCleanParam(request, "rememberMe");
        //System.out.println("记住我="+rememberMe);
        if (password == null) {
            password = "123456";
        }

        if (loginname == null) {
            loginname = mobile;
        }
        // 如果是登陆，则之间进行登陆验证
        User user = null;
        UsernamePasswordToken userToken = new UsernamePasswordToken();
        //自动登录
        if("selected".equals(rememberMe)){
        	userToken.setRememberMe(true);
        }else if("noSelect".equals(rememberMe)){
        	userToken.setRememberMe(false);
        }
        if ("user_login".equals(loginFrom)) {
            // 如果该用户已经存在，则不再新建
            if (systemService.getUserByLoginName(username) != null) {
                // 直接登录
                userToken.setUsername(username);
                userToken.setPassword(password.toCharArray());
                try {
                    UserUtils.getSubject().login(userToken);
                    ret = "200";
                    retMsg = "操作成功";
                    retMap.put("loginname", username);
                    retMap.put("password", password);
                } catch (UnknownAccountException uae) {
                    logger.error("用户名不存在",uae);
                    ret = "201";
                    retMsg = "用户不存在，请先注册";
                } catch (IncorrectCredentialsException ice) {
                    logger.error("用户名密码不正确",ice);
                    ret = "204";
                    retMsg = "用户名或密码不正确，请重试";
                } catch (LockedAccountException lae) {
                    logger.error("账户状态被锁",lae);
                    ret = "205";
                    retMsg = "账户异常，请联系管理员";
                } catch (ExcessiveAttemptsException eae) {
                    logger.error("密码输入次数超出限制",eae);
                    ret = "206";
                    retMsg = "账户异常，请联系管理员";
                }

            } else {
                ret = "201";
                retMsg = "用户不存在，请先注册";
            }
            retMap.put("ret", ret);
            retMap.put("retMsg", retMsg);
            return getMapToJSON(retMap);
        }

        SMSVerify sms = new SMSVerify();
        try {
            if (sms.checkVerifyCode(mobile, sms_code, verifyServID, false).equals("0")) {
                // User user = null;
                // UsernamePasswordToken userToken = new
                // UsernamePasswordToken();
                // 如果该用户已经存在，则不再新建
                if (systemService.getUserByLoginName(mobile) != null) {
                    if ("fast_login".equals(loginFrom)) {
                        user = systemService.getUserByLoginName(mobile);
                        // 自动登录
                        userToken.setUsername(user.getMobile());
                        //userToken.setPassword("123456".toCharArray());
                        // 短信验证码登陆，跳过密码对比校验
                        userToken.setLogin_type("SMSCode");
                        UserUtils.getSubject().login(userToken);

                        ret = "200";
                        retMsg = "操作成功";
                        retMap.put("loginname", user.getLoginName());
                        retMap.put("password", user.getPassword());
                    } else {
                        ret = "203";
                        retMsg = "手机号已经注册，请重新输入";
                    }
                } else {
                	if("fast_login".equals(loginFrom)) {
                		user = systemService.quickGetUserByMobileForWeb(mobile);
						userToken.setUsername(user.getMobile());
						userToken.setPassword(password.toCharArray());
						userToken.setLogin_type("SMSCode");
						UserUtils.getSubject().login(userToken);
                		ret = "200";
                        retMsg = "操作成功";
                        retMap.put("loginname", user.getLoginName());
                        retMap.put("password", password);
                	}
                	else if ("register".equals(loginFrom)) {
                        List<String> listRoleID = new ArrayList<String>();
                        listRoleID.add("2");
                        user = new User();
                        // user.setId(IdGen.uuid());
                        user.setMobile(mobile);
                        user.setUserType("6");
                        user.setPassword(SystemService.entryptPassword(password));
                        user.setCompany(new Office("1"));
                        user.setOffice(new Office("1"));
                        user.setLoginName(loginname);
                        user.setName(mobile);
                        user.setNo("0000001");
                        //user.setEmail("123456@qq.com");
                        //user.setPhone("85003121");
                        user.setCreateBy(new User("1"));
                        user.setUpdateBy(new User("1"));
                        user.setLoginFlag("1");
                        user.setDelFlag("0");
                        user.setRoleIdList(listRoleID);
                        user.setRemarks("自注册用户");
                        systemService.saveUser(user);

                        userToken.setUsername(user.getMobile());
                        userToken.setPassword(password.toCharArray());
                        userToken.setLogin_type("SMSCode");
                        UserUtils.getSubject().login(userToken);
                        ret = "200";
                        retMsg = "操作成功";
                        retMap.put("loginname", user.getLoginName());
                        retMap.put("password", password);
                    } else {
                        ret = "201";
                        retMsg = "当前还未注册，请先注册";
                    }

                }
            } else {
                ret = "204";
                retMsg = "短信验证码错误！请重试！";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        retMap.put("ret", ret);
        retMap.put("retMsg", retMsg);
        // return "redirect:/sanlen_website/u/login";
        return new Gson().toJson(retMap);
    }

    /**
     * 发送验证码
     */
    @RequestMapping(value = "getCode")
    @ResponseBody
    public String getSMSCode(String mobile) {
        Map<String, String> retMap = new HashMap<String, String>();
        String ret = "400";
        String retMsg = "操作成功";
        Gson gs = new Gson();

        // 短信发送
        SMSVerify sms = new SMSVerify();
        String[] contentArray = { "[VerifyCode]", "5分钟" }; // 定义5分钟内有效

        // 发送短信: TODO 应将该校验串缓存 10 分钟，以便与客户端的请求参数进行比较??
        // 如果是新注册用户，直接发送
        try {
            String mobileMessageServ = sms.sendSmsVerifyCode(mobile, 6, "204863", contentArray);
            String[] messageServ = mobileMessageServ.split("\\|");
            if (messageServ != null && messageServ[0].equals("0")) {
                ret = "200";
                retMsg = "操作成功";
                retMap.put("mobile", mobile);
                retMap.put("verifyID", messageServ[1]);// 服务器缓存的验证码id
            }
        } catch (Exception e) {
            logger.error("短信发送失败", e);
            e.printStackTrace();
        }
        retMap.put("ret", ret);
        retMap.put("retMsg", retMsg);

        return gs.toJson(retMap);
    }

    /**
     * 网站首页
     */
    @RequestMapping(value = "index-{siteId}${urlSuffix}")
    public String index(@PathVariable String siteId, Model model) {
        if (siteId.equals("1")) {
            return "redirect:" + Global.getFrontPath();
        }
        Site site = CmsUtils.getSite(siteId);
        // 子站有独立页面，则显示独立页面
        if (StringUtils.isNotBlank(site.getCustomIndexView())) {
            model.addAttribute("site", site);
            model.addAttribute("isIndex", true);
            return "modules/cms/front/themes/" + site.getTheme() + "/frontIndex" + site.getCustomIndexView();
        }
        // 否则显示子站第一个栏目
        List<Category> mainNavList = CmsUtils.getMainNavList(siteId);
        if (mainNavList.size() > 0) {
            String firstCategoryId = CmsUtils.getMainNavList(siteId).get(0).getId();
            return "redirect:" + Global.getFrontPath() + "/list-" + firstCategoryId + Global.getUrlSuffix();
        } else {
            model.addAttribute("site", site);
            return "modules/cms/front/themes/" + site.getTheme() + "/frontListCategory";
        }
    }

    /**
     * 内容列表
     */
    @RequestMapping(value = "list-{categoryId}${urlSuffix}")
    public String list(@PathVariable String categoryId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model) {
        Category category = categoryService.get(categoryId);
        if (category == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return "error/404";
        }
        Site site = siteService.get(category.getSite().getId());
        model.addAttribute("site", site);
        // 2：简介类栏目，栏目第一条内容
        if ("2".equals(category.getShowModes()) && "article".equals(category.getModule())) {
            // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
            List<Category> categoryList = Lists.newArrayList();
            if (category.getParent().getId().equals("1")) {
                categoryList.add(category);
            } else {
                categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
            }
            model.addAttribute("category", category);
            model.addAttribute("categoryList", categoryList);
            // 获取文章内容
            Page<Article> page = new Page<Article>(1, 1, -1);
            Article article = new Article(category);
            page = articleService.findPage(page, article, false);
            if (page.getList().size() > 0) {
                article = page.getList().get(0);
                article.setArticleData(articleDataService.get(article.getId()));
                articleService.updateHitsAddOne(article.getId());
            }
            model.addAttribute("article", article);
            CmsUtils.addViewConfigAttribute(model, category);
            CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
            return "modules/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
        } else {
            List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
            // 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
            if ("1".equals(category.getShowModes()) || categoryList.size() == 0) {
                // 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
                if (categoryList.size() > 0) {
                    category = categoryList.get(0);
                } else {
                    // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
                    if (category.getParent().getId().equals("1")) {
                        categoryList.add(category);
                    } else {
                        categoryList = categoryService.findByParentId(category.getParent().getId(),
                                category.getSite().getId());
                    }
                }
                model.addAttribute("category", category);
                model.addAttribute("categoryList", categoryList);
                // 获取内容列表
                if ("article".equals(category.getModule())) {
                    Page<Article> page = new Page<Article>(pageNo, pageSize);
                    // System.out.println(page.getPageNo());
                    page = articleService.findPage(page, new Article(category), false);
                    model.addAttribute("page", page);
                    // 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章
                    if ("2".equals(category.getShowModes())) {
                        Article article = new Article(category);
                        if (page.getList().size() > 0) {
                            article = page.getList().get(0);
                            article.setArticleData(articleDataService.get(article.getId()));
                            articleService.updateHitsAddOne(article.getId());
                        }
                        model.addAttribute("article", article);
                        CmsUtils.addViewConfigAttribute(model, category);
                        CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
                        return "modules/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
                    }
                } else if ("link".equals(category.getModule())) {
                    Page<Link> page = new Page<Link>(1, -1);
                    page = linkService.findPage(page, new Link(category), false);
                    model.addAttribute("page", page);
                }
                String view = "/frontList";
                if (StringUtils.isNotBlank(category.getCustomListView())) {
                    view = "/" + category.getCustomListView();
                }
                CmsUtils.addViewConfigAttribute(model, category);
                site = siteService.get(category.getSite().getId());
                // System.out.println("else 栏目第一条内容 _2
                // :"+category.getSite().getTheme()+","+site.getTheme());
                return "modules/cms/front/themes/" + siteService.get(category.getSite().getId()).getTheme() + view;
                // return
                // "modules/cms/front/themes/"+category.getSite().getTheme()+view;
            }
            // 有子栏目：显示子栏目列表
            else {
                model.addAttribute("category", category);
                model.addAttribute("categoryList", categoryList);
                String view = "/frontListCategory";
                if (StringUtils.isNotBlank(category.getCustomListView())) {
                    view = "/" + category.getCustomListView();
                }
                CmsUtils.addViewConfigAttribute(model, category);
                return "modules/cms/front/themes/" + site.getTheme() + view;
            }
        }
    }

    /**
     * 内容列表（通过url自定义视图）
     */
    @RequestMapping(value = "listc-{categoryId}-{customView}${urlSuffix}")
    public String listCustom(@PathVariable String categoryId, @PathVariable String customView,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "15") Integer pageSize, Model model) {
        Category category = categoryService.get(categoryId);
        if (category == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return "error/404";
        }
        Site site = siteService.get(category.getSite().getId());
        model.addAttribute("site", site);
        List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
        model.addAttribute("category", category);
        model.addAttribute("categoryList", categoryList);
        CmsUtils.addViewConfigAttribute(model, category);
        return "modules/cms/front/themes/" + site.getTheme() + "/frontListCategory" + customView;
    }

    /**
     * 共用跳转页面方法
     * 
     * @param pageUrl
     *            页面路径
     * @return JsonObjRet
     */
    @RequestMapping(value = "publicToPage")
    public String publicToPage(String pageUrl, HttpServletRequest request, Model model) {
        Site site = CmsUtils.getSite(Site.getCurrentSiteId());
        model.addAttribute("site", site);

        User user = UserUtils.getUser();
        model.addAttribute("user", user);
        return pageUrl;
    }

    /**
     * 显示内容
     */
    @RequestMapping(value = "view-{categoryId}-{contentId}${urlSuffix}")
    public String view(@PathVariable String categoryId, @PathVariable String contentId, Model model) {
        Category category = categoryService.get(categoryId);
        if (category == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return "error/404";
        }
        model.addAttribute("site", category.getSite());
        if ("article".equals(category.getModule())) {
            // 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
            List<Category> categoryList = Lists.newArrayList();
            if (category.getParent().getId().equals("1")) {
                categoryList.add(category);
            } else {
                categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
            }
            // 获取文章内容
            Article article = articleService.get(contentId);
            if (article == null || !Article.DEL_FLAG_NORMAL.equals(article.getDelFlag())) {
                return "error/404";
            }
            // 文章阅读次数+1
            articleService.updateHitsAddOne(contentId);
            // 获取推荐文章列表
            List<Object[]> relationList = articleService
                    .findByIds(articleDataService.get(article.getId()).getRelation());
            // 将数据传递到视图
            model.addAttribute("category", categoryService.get(article.getCategory().getId()));
            model.addAttribute("categoryList", categoryList);
            article.setArticleData(articleDataService.get(article.getId()));
            model.addAttribute("article", article);
            model.addAttribute("relationList", relationList);
            CmsUtils.addViewConfigAttribute(model, article.getCategory());
            CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
            Site site = siteService.get(category.getSite().getId());
            model.addAttribute("site", site);
            // return
            // "modules/cms/front/themes/"+category.getSite().getTheme()+"/"+getTpl(article);
            return "modules/cms/front/themes/" + site.getTheme() + "/" + getTpl(article);
        }
        return "error/404";
    }

    /**
     * 内容评论
     */
    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public String comment(String theme, Comment comment, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        Page<Comment> page = new Page<Comment>(request, response);
        Comment c = new Comment();
        c.setCategory(comment.getCategory());
        c.setContentId(comment.getContentId());
        
        try {
        	comment.setTitle(URLDecoder.decode(comment.getTitle(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        c.setDelFlag(Comment.DEL_FLAG_NORMAL);
        page = commentService.findPage(page, c);
        model.addAttribute("page", page);
        model.addAttribute("comment", comment);
        return "modules/cms/front/themes/" + theme + "/frontComment";
    }

    /**
     * 内容评论保存
     */
    @ResponseBody
    @RequestMapping(value = "comment", method = RequestMethod.POST)
    public String commentSave(Comment comment, String validateCode, @RequestParam(required = false) String replyId,
            HttpServletRequest request) {
        if (StringUtils.isNotBlank(validateCode)) {
            if (ValidateCodeServlet.validate(request, validateCode)) {
                if (StringUtils.isNotBlank(replyId)) {
                    Comment replyComment = commentService.get(replyId);
                    if (replyComment != null) {
                        comment.setContent("<div class=\"reply\">" + replyComment.getName() + ":<br/>"
                                + replyComment.getContent() + "</div>" + comment.getContent());
                    }
                }
                comment.setIp(request.getRemoteAddr());
                comment.setCreateDate(new Date());
                comment.setDelFlag(Comment.DEL_FLAG_AUDIT);
                commentService.save(comment);
                return "{result:1, message:'提交成功。'}";
            } else {
                return "{result:2, message:'验证码不正确。'}";
            }
        } else {
            return "{result:2, message:'验证码不能为空。'}";
        }
    }

    /**
     * 站点地图
     */
    @RequestMapping(value = "map-{siteId}${urlSuffix}")
    public String map(@PathVariable String siteId, Model model) {
        Site site = CmsUtils.getSite(siteId != null ? siteId : Site.defaultSiteId());
        model.addAttribute("site", site);
        return "modules/cms/front/themes/" + site.getTheme() + "/frontMap";
    }

    private String getTpl(Article article) {
        if (StringUtils.isBlank(article.getCustomContentView())) {
            String view = null;
            Category c = article.getCategory();
            boolean goon = true;
            do {
                if (StringUtils.isNotBlank(c.getCustomContentView())) {
                    view = c.getCustomContentView();
                    goon = false;
                } else if (c.getParent() == null || c.getParent().isRoot()) {
                    goon = false;
                } else {
                    c = c.getParent();
                }
            } while (goon);
            return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
        } else {
            return article.getCustomContentView();
        }
    }

    /**
     * 从上传文件资源中下载文件接口
     */
    @RequestMapping(value = "download")
    @ResponseBody
    public String downloadLocal(HttpServletRequest request, HttpServletResponse response) {
    	String filePath = WebUtils.getCleanParam(request, "filePath");
    	try {
			filePath = new String(filePath.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	Map<String, Object> retMap = FileUtils.downloadFile(filePath, response);
        if("0".equals(retMap.get("ret"))) {
        	cmsFileresourceService.updateDownloadNum(filePath);
        }
        return null;
    }
    
    /**
     * 邮箱验证接口
     */
    @RequestMapping(value = "validateEmail")
    @ResponseBody
    public String validateEmail(HttpServletRequest request, Model model){
    	String email = WebUtils.getCleanParam(request, "email");
    	String validateCode = WebUtils.getCleanParam(request, "validateCode");
    	Map<String, Object> retMap = systemService.validateEmail(email, validateCode);
    	return new Gson().toJson(retMap);
    }
    
    /**
     * 根据当前用户发送密码重置的邮件的接口
     */
    @RequestMapping(value = "sendEmailForFindPW")
    @ResponseBody
    public String sendEmailForFindPW(HttpServletRequest request, Model model){
    	Map<String, Object> retMap = Maps.newHashMap();
    	User u = UserUtils.getUser();
    	if(u == null || StringUtils.isBlank(u.getId())) {
    		retMap.put("ret", "415");
			retMap.put("retMsg", "请先登录！");
			return new Gson().toJson(retMap);
    	}
    	if(StringUtils.isBlank(u.getEmail())) {
    		retMap.put("ret", "413");
			retMap.put("retMsg", "用户尚未填写邮箱信息！");
			return new Gson().toJson(retMap);
    	}
    	if(!"1".equals(u.getStatus())) {
    		retMap.put("ret", "416");
			retMap.put("retMsg", "用户邮箱尚未验证，请先验证邮箱！");
			return new Gson().toJson(retMap);
    	}
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", u.getEmail());
		map.put("userName", u.getName());
		String templatePath = "mailTemplate/findPasswordByMail.ftl";
		
		//获取随机字符串
		String newPassword = IdGen.randomBase62(10);
		map.put("newPassword", newPassword);
		u.setPassword(SystemService.entryptPassword(newPassword));
		systemService.saveUser(u);
		
		boolean result = SendMailUtil.sendFtlMail(u.getEmail(), "山丽信息（密码找回）", templatePath, map);
		if(result) {
			retMap.put("ret", "200");
			retMap.put("retMsg", "邮件发送成功！");
		} else {
			retMap.put("ret", "414");
			retMap.put("retMsg", "邮件发送失败！");
		}
    	
    	return new Gson().toJson(retMap);
    }
    
    /**
     * 根据用户id发送验证邮箱的邮件的接口
     */
    @RequestMapping(value = "sendEmailForValidate")
    @ResponseBody
    public String sendEmailForValidate(HttpServletRequest request, Model model){
    	Map<String, Object> retMap = Maps.newHashMap();
    	String userId = WebUtils.getCleanParam(request, "userId");
    	User u = UserUtils.get(userId);
    	if(u == null) {
    		retMap.put("ret", "410");
			retMap.put("retMsg", "该用户不存在！");
			return new Gson().toJson(retMap);
    	}
    	if(StringUtils.isBlank(u.getEmail())) {
    		retMap.put("ret", "413");
			retMap.put("retMsg", "用户尚未填写邮箱信息！");
			return new Gson().toJson(retMap);
    	}
    	if("1".equals(u.getStatus())) {
    		retMap.put("ret", "412");
			retMap.put("retMsg", "用户已完成邮箱验证，请勿重复验证！");
			return new Gson().toJson(retMap);
    	}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", u.getEmail());
		map.put("userName", u.getName());
		String basePath = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		String validateCode = IdGen.uuid();
		String validateUrl = basePath + frontPath + "/validateEmail" + "?email=" + u.getEmail() + "&validateCode=" + validateCode;
		map.put("validateUrl", validateUrl);
		String templatePath = "mailTemplate/validateEmail.ftl";
		
		//将validateCode验证码存入用户数据中，再执行发送邮件
		u.setEmailValidateCode(validateCode);
		systemService.saveUser(u);
		boolean result = SendMailUtil.sendFtlMail(u.getEmail(), "山丽信息（邮箱验证）", templatePath, map);
		if(result) {
			retMap.put("ret", "200");
			retMap.put("retMsg", "邮件发送成功！");
		} else {
			retMap.put("ret", "414");
			retMap.put("retMsg", "邮件发送失败！");
		}
		return new Gson().toJson(retMap);
    }
    
    /**
     * 新增用户反馈
     */
    @RequestMapping(value = "addFeedback")
    @ResponseBody
    public String addFeedback(CmsFeedback cmsFeedback,Model model){
    	//System.out.println("进入添加反馈的方法");
    	String isSuccess="";
        
         UserUtils.getUser();
         
         cmsFeedback.setUser(UserUtils.getUser());
         
         cmsFeedbackService.save(cmsFeedback);
         isSuccess="success";
    	return isSuccess;
    }
    
}
