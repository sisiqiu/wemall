package com.fulltl.wemall.modules.wx.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.fulltl.wemall.common.config.Global;

public class ApiclientCert {
	
	public static String sendPostByCert(String url, String params) throws Exception {
		// 指定读取证书格式为 PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		// 读取本机存放的 PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(Global.getConfig("weixin.trade.pkcs12_certPath")));
		try {
			// 指定 PKCS12的密码 (商户 ID)
			keyStore.load(instream, Global.getConfig("weixin.trade.mch_id").toCharArray());
		} finally {
			instream.close();
		}
		
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, Global.getConfig("weixin.trade.mch_id").toCharArray()).build();
		// 指定 TLS 版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 设置 httpclient 的 SSLSocketFactory
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		
		HttpPost httpPost = new HttpPost(url);
		// 构建消息实体
        StringEntity entity = new StringEntity(params, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
		httpPost.setEntity(entity);
		//设置请求的报文头部的编码
		httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
		//设置期望服务端返回的编码
		httpPost.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
		
        String result = StringUtils.EMPTY;
        BufferedReader in = null;
        try {  
            //执行post请求  
            HttpResponse httpResponse = httpclient.execute(httpPost);  
            httpResponse.setHeader("Accept", "text/plain;charset=utf-8");
            //获取响应消息实体
            InputStream inStream = httpResponse.getEntity().getContent();  
            
            in = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			return result;
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {                //关闭流并释放资源  
            	httpclient.close();  
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        }
		return null;  
	}
}
