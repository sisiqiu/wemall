package com.fulltl.wemall.modules.wx.core.utils.accessToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.modules.wx.core.pojo.AccessToken;
import com.fulltl.wemall.modules.wx.core.pojo.Menu;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * 微信接口调用相关工具方法。获取accessToken和jsApi_ticket的核心工具类。
 * @author Administrator
 *
 */
public class WXUtil {
	private static Logger logger = LoggerFactory.getLogger(WXUtil.class);
    
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    
    public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    
    public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    
    /**
     * 调用微信接口获取accessToken的工具方法。
     * @param appId
     * @param appSecret
     * @return
     */
    public static AccessToken getAccessToken(String appId,String appSecret) {
        String reqUrl = access_token_url.replace("APPID", appId).replace("APPSECRET", appSecret);
        JSONObject jsonObject = httpRequest(reqUrl, "GET", null);
        AccessToken access_Token = (AccessToken)JSONObject.toBean(jsonObject, AccessToken.class);
        if (null != jsonObject) {
            try {
                access_Token = new AccessToken();
                access_Token.setToken(jsonObject.get("access_token").toString());
                access_Token.setExpiresIn((Integer)jsonObject.get("expires_in"));
                CacheUtils.put("accessToken", access_Token);
            } catch (Exception e) {
                access_Token = null;
                // 获取token失败
                logger.error("获取token失败 errcode:" + (Integer)jsonObject.get("errcode") + " errmsg:" + jsonObject.get("errmsg").toString());
            }
        }
        
        return access_Token;
    }
    
    /**
     * 根据accessToken调用获取微信JS接口的临时票据jsapi_ticket
     * 
     * @param access_token 接口访问凭证
     * @return
     */
    public static String getJsApiTicket(String access_token) {
        String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", access_token);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        String ticket = null;
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                logger.error("获取token失败 errcode:{} errmsg:{}" + jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
            }
        }
        return ticket;
    }
    
    public static JSONObject httpRequest(String reqUrl, String reqMethod, String outStr) {
        JSONObject jsonObject = null;
        jsonObject = JSONObject.fromObject(getStrByHttpRequest(reqUrl, reqMethod, outStr));
        return jsonObject;
    }
    
    /**
     * 微信凭证类型下，发http请求，获取结果。
     * @param reqUrl
     * @param reqMethod
     * @param outStr
     * @return
     */
    public static String getStrByHttpRequest(String reqUrl, String reqMethod, String outStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = { new MyX509TrustManager()};
            SSLContext  sslContext = SSLContext.getInstance("SSL","SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            
            URL url = new URL(reqUrl);
            HttpsURLConnection httpUrlCon = (HttpsURLConnection)url.openConnection();
            httpUrlCon.setSSLSocketFactory(ssf);
            
            httpUrlCon.setDoInput(true);
            httpUrlCon.setDoOutput(true);
            httpUrlCon.setUseCaches(false);
            
            httpUrlCon.setRequestMethod(reqMethod);
            
            if ("GET".equalsIgnoreCase(reqMethod)) {
                httpUrlCon.connect();
            }
            
            if (null != outStr) {
                OutputStream outputStream = httpUrlCon.getOutputStream();
                outputStream.write(outStr.getBytes("utf-8"));
                outputStream.close();
            }
            
            InputStream inputStream = httpUrlCon.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            
            bufferedReader.close();
            reader.close();
            inputStream.close();
            httpUrlCon.disconnect();
        } catch (ConnectException ex) {
            logger.error("Weixin server connection timed out.");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        
        return buffer.toString();
    }
    
    /**
     * 调用微信的接口创建菜单。
     * @param menu
     * @param accessToken
     * @return
     */
    public static int createMenu(Menu menu,String accessToken) {
        int result = 0;
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonMenu = JSONObject.fromObject(menu);
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu.toString());
        
        if (jsonObject != null) {
            if (0 != (Integer)jsonObject.get("errcode")) {
                result = (Integer)jsonObject.get("errcode");
                logger.error("创建菜单失败errcode:" + result + "errmsg:" + jsonObject.get("errmsg").toString());
            }
        }
        
        return result;
    }
    
}