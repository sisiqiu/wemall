package com.fulltl.wemall.modules.qq.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.qq.core.pojo.QQOAuthUserInfo;
import com.fulltl.wemall.modules.sys.entity.User;
import com.fulltl.wemall.modules.sys.service.SystemService;

/**
 * qq前台控制器。包含qq第三方授权登陆接口。
 * 
 * @author ldk
 * @version 2017-10-27
 */
@Controller
@RequestMapping(value = "${frontPath}/qq/core/web")
public class QQWebFrontController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(value = "returnUrl")
	public String returnUrl(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
		// Authorization Code
		String code = WebUtils.getCleanParam(request, "code");
		// 原始的state值
		String state = WebUtils.getCleanParam(request, "state");
		// 授权成功后要跳转的应用url
		String myRedirectUrl = WebUtils.getCleanParam(request, "myRedirectUrl");
		//String redirectFromPrefix = "http://ldkadmin.viphk.ngrok.org/f/wx/core/common/returnToOtherUrl?otherUrl=";
		String redirectFromPrefix = "";
		String qqRedirectUrl = redirectFromPrefix + request.getRequestURL() + "?myRedirectUrl=" + myRedirectUrl;
		//验证是否获取权限code
		if(StringUtils.isBlank(code)) {
			logger.error("qq第三方授权获取到的code为空！");
			return myRedirectUrl;
		}

		Map<String, Object> retMap = Maps.newHashMap();
		
		//获取access_token
		String access_token = getAccessTokenProcess(code, qqRedirectUrl);
		
		//获取openid
		String openid = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(access_token)) {
			openid = getOpenIdProcess(access_token);
		}

		//获取用户信息，添加系统用户，并登陆
		if(StringUtils.isNotBlank(openid)) {
			retMap = getUserInfoProcess(access_token, openid);
		}
		if(!"200".equals(retMap.get("ret"))) {
			String errorMsg = retMap.get("retMsg") == null ? "openid和access_token为空" : retMap.get("retMsg").toString();
			logger.error(errorMsg);
		}
		
		logger.info("重定向的网页为：" + myRedirectUrl);
		return "redirect:"+myRedirectUrl;
	}

	//--------------获取access_token部分begin-------------------------------------//
	/**
	 * 获取access_token的过程，返回获取的access_token
	 * @param code
	 * @param qqRedirectUrl
	 * @return
	 */
	private String getAccessTokenProcess(String code, String qqRedirectUrl) {
		//测试可否用HttpClient获取access_token，如果不能，则跳转。
		HttpClient clientForToken = new HttpClient(); 
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod methodForToken = new GetMethod(QQConfig.getTokenUrl + "?" + QQConfig.getTokenParamStr.replace("APP_ID", QQConfig.appId)
																.replace("APP_KEY", QQConfig.appKey)
																.replace("CODE", code)
																.replace("REDIRECT_URI", qqRedirectUrl)); //REDIRECT_URI与上一步一致。
		String access_token = ""; //定义获取到的access_token
		String refresh_token = "";
		try {
			clientForToken.executeMethod(methodForToken);
			// 打印服务器返回的状态
			logger.info("获取access_token过程：" + methodForToken.getStatusLine().toString());
			// 打印返回的信息，获取到access_token和expires_in；接口调用有错误时，会返回code和msg字段
			String params = methodForToken.getResponseBodyAsString();
			logger.info("获取access_token过程：" + params);
			//access_token = "";
			Map<String, String> urlSplit = urlSplit(params);
			access_token = urlSplit.get("access_token");
			refresh_token = urlSplit.get("refresh_token");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			methodForToken.releaseConnection();
		}
		return access_token;
	}
	
	//--------------获取access_token部分end---------------------------------------//
	//--------------获取openid部分begin------------------------------------------------//

	/**
	 * 获取openid的过程，返回openid
	 * @param access_token
	 */
	private String getOpenIdProcess(String access_token) {
		//测试可否用HttpClient获取openId
		HttpClient clientForOpenId = new HttpClient();
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod methodForOpenId = new GetMethod(QQConfig.getOpenIdUrl + "?" + QQConfig.getOpenIdParamStr.replace("ACCESS_TOKEN", access_token));
		String openid = ""; //定义获取到的openId
		String cliend_id = "";
		try {
			clientForOpenId.executeMethod(methodForOpenId);
			// 打印服务器返回的状态
			logger.info("获取openid过程：" + methodForOpenId.getStatusLine().toString());
			// 打印返回的信息，获取到client_id和openid；接口调用有错误时，会返回code和msg字段
			String resultMsg = methodForOpenId.getResponseBodyAsString();
			logger.info("获取openid过程：" + resultMsg);
			resultMsg = resultMsg.substring(10, resultMsg.length()-3);
			logger.info("获取openid过程：" + resultMsg);
			Map<String, String> params = new Gson().fromJson(resultMsg, Map.class);
			openid = params.get("openid");
			cliend_id = params.get("cliend_id");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 释放连接
			methodForOpenId.releaseConnection();
		}
		return openid;
	}
	
	//--------------获取openid部分end---------------------------------------------//
	//--------------获取userInfo部分begin-----------------------------------------//

	/**
	 * 根据access_token和openid获取用户信息的过程，并执行添加对应的系统用户和登陆操作的过程。
	 * @param access_token
	 * @param openid
	 */
	private Map<String, Object> getUserInfoProcess(String access_token, String openid) {
		Map<String, Object> retMap = Maps.newHashMap();
		//校验数据
		if(StringUtils.isBlank(access_token)) {
			retMap.put("ret", "-1");
			retMap.put("retMap", "access_token不能为空！");
			return retMap;
		}
		if(StringUtils.isBlank(openid)) {
			retMap.put("ret", "-1");
			retMap.put("retMap", "openid不能为空！");
			return retMap;
		}
		
		boolean getUserInfoSuccess = true; //标识获取用户信息成功
		
		//测试可否用HttpClient获取openId
		HttpClient clientForUserInfo = new HttpClient();
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		HttpMethod methodForUserInfo = new GetMethod(QQConfig.getUserInfoUrl + "?" + QQConfig.getUserInfoParamStr
																.replace("ACCESS_TOKEN", access_token)
																.replace("APP_ID", QQConfig.appId)
																.replace("OPENID", openid));
		try {
			clientForUserInfo.executeMethod(methodForUserInfo);
			// 打印服务器返回的状态
			logger.info("获取userInfo过程：" + methodForUserInfo.getStatusLine().toString());
			// 打印返回的信息，获取到client_id和openid；接口调用有错误时，会返回code和msg字段
			String resultMsg = methodForUserInfo.getResponseBodyAsString();
			logger.info("获取userInfo过程：" + resultMsg);
			//QQOAuthUserInfo
			Map params = new Gson().fromJson(resultMsg, Map.class);
			//判断调用接口是否有错误，如果有错，将getUserInfoSuccess设置为false；如果没错，构造QQOAuthUserInfo，填充相关信息，添加用户。
			if(new Double(0).equals(params.get("ret"))) {
				logger.info("getUserInfo调用成功");
				getUserInfoSuccess = true;
				QQOAuthUserInfo qqOAuthUserInfo = QQOAuthUserInfo.valueOf(params);
				qqOAuthUserInfo.setOpenid(openid);
				User u = systemService.quickGetUserBy(qqOAuthUserInfo);
				try {
					retMap = systemService.loginByUser(u, false);
				} catch (Exception e) {
					logger.error("登陆出错", e);
				}
			} else {
				logger.info("getUserInfo调用失败");
				getUserInfoSuccess = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户userInfo过程出错！错误信息：" + e.getMessage());
			getUserInfoSuccess = false;
		} finally {
			// 释放连接
			methodForUserInfo.releaseConnection();
		}
		
		if(!getUserInfoSuccess) {
			//获取用户详情未成功，使用基础openid添加系统用户。
			retMap = getUserByOpenId(openid);
		}
		
		return retMap;
	}
	//--------------获取userInfo部分end-------------------------------------------//
	
	/**
	 * 使用基础的openid获取或添加系统用户，并登陆
	 * @param openid
	 * @return
	 */
	private Map<String, Object> getUserByOpenId(String openid) {
		QQOAuthUserInfo qqOAuthUserInfo = new QQOAuthUserInfo(openid);
		User u = systemService.quickGetUserBy(qqOAuthUserInfo);
		Map<String, Object> retMap = Maps.newHashMap();
		try {
			retMap = systemService.loginByUser(u, false);
		} catch (Exception e1) {
			logger.error("登陆出错", e1);
		}
    	return retMap;
	}

	/**
     * 解析出url参数中的键值对
     * 如 "Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     * @author lzf
     */
    public static Map<String, String> urlSplit(String strUrlParam){
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit=null;
        if(strUrlParam==null){
            return mapRequest;
        }
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit){
              String[] arrSplitEqual=null;         
              arrSplitEqual= strSplit.split("[=]");
              //解析出键值
              if(arrSplitEqual.length>1){
                  //正确解析
                  mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
              }else{
                  if(arrSplitEqual[0]!=""){
                  //只有参数没有值，不加入
                  mapRequest.put(arrSplitEqual[0], "");       
                  }
              }
        }   
        return mapRequest;   
    }
}