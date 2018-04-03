package com.fulltl.wemall.modules.wx.core.utils.accessToken;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fulltl.wemall.common.config.Global;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.modules.wx.core.pojo.AccessToken;

/**
 * 获取accessToken的定时任务类，并使用CacheUtil将accessToken和jsapi_ticket设置在全局cache中
 * @author ldk
 *
 */
@Service
@Lazy(false)
public class ObtainAccessTokenScheduler {
	// 第三方用户唯一凭证
	//自己
	private static final String APP_ID = Global.getConfig("weixin.appId");
	// 第三方用户唯一凭证密钥
	//自己
	private static final String SECRET = Global.getConfig("weixin.secret");
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 获取accessToken和jsapi_ticket的操作，并将在初始化构造时和每个整点执行。
	 */
	@PostConstruct
	@Scheduled(cron="0 0 0/1 * * ?")
	public void obtainAccessToken() {
		// 调用接口获取access_token
		AccessToken accessToken = WXUtil.getAccessToken(APP_ID, SECRET);
		String jsapi_ticket = WXUtil.getJsApiTicket(accessToken.getToken());
		
		CacheUtils.put("accessToken", accessToken);
		CacheUtils.put("jsapi_ticket", jsapi_ticket);
		logger.info("获取accessToken：" + accessToken.getToken() + " 成功！\n"
				+ "获取jsapi_ticket：" + jsapi_ticket + " 成功！");
	}
}
