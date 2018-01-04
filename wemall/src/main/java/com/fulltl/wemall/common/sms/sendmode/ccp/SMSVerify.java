package com.fulltl.wemall.common.sms.sendmode.ccp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.fulltl.wemall.common.utils.CacheUtils;
import com.fulltl.wemall.common.utils.IdGen;
import com.fulltl.wemall.modules.wx.entity.ConGroupsms;

import net.sf.ehcache.Element;


public class SMSVerify {

    private static String dbName = "default";
    private static String cacheName = "dbobj.mobileVerifyNo";
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * 目前返回的直接是验证码，将来考虑返回一个串号
     * 
     * @param mobileNo
     *            手机号
     * @param verifyCodeLen
     *            验证码的长度
     * @param templateID
     *            发送的模板,短信的模板是在服务器端事先进行定义的
     * @param contentArray
     *            发送的内容，将按顺序替换模板中的内容
     * @return
     */
    public String sendSmsVerifyCode(String mobileNo, int verifyCodeLen, String templateID, String[] contentArray)
            throws Exception {
        // 默认为10分钟
        int expiry = 60 * 10;
        return sendSmsVerifyCode(mobileNo, verifyCodeLen, templateID, contentArray, expiry);
    }

    /**
     * 返回的是一个串号，根据这个串号可以查出缓存中的短信验证码
     * 
     * @param mobileNo
     *            手机号
     * @param verifyCodeLen
     *            验证码的长度
     * @param templateID
     *            发送的模板,短信的模板是在服务器端事先进行定义的
     * @param contentArray
     *            发送的内容，将按顺序替换模板中的内容
     * @param expiry
     *            有效的时间,目前为通过缓存进行处理
     * @return
     */
    public String sendSmsVerifyCode(String mobileNo, int verifyCodeLen, String templateID, String[] contentArray,
            int expiry) throws Exception {
        HashMap<String, Object> result = null;

        // 根据长度、取得验证码
        String verifyCode = getVerifyCode(verifyCodeLen);
        result = CCPSDK.getInstance().sendTemplateSMS(mobileNo, templateID, getSmsContent(contentArray, verifyCode));
        Element myItem = null;
        try {
            if ("000000".equals(result.get("statusCode"))) {

                String[] temp = new String[] { mobileNo, verifyCode, "" };

                // 唯一的串号
                String verifySerialNo = IdGen.uuid();

                // 创建缓存项目，设置过期时间
                myItem = new Element(IdGen.uuid(), temp, false, expiry, expiry);
                // 将短信验证码写入到缓存中
                CacheUtils.put(cacheName, verifySerialNo, myItem);

                return "0|" + verifySerialNo;
            }
        } catch (Exception ex) {
            throw new Exception("Error when sendSmsVerifyCode for mobileNo " + mobileNo, ex);
        }

        return "1|" + result;
    }
    
    /**
     * 根据手机号列表，模板id，内容数组，进行群发短信。
     * @param mobileNos 手机号列表，用“,”隔开
     * @param templateID 短信模板id
     * @param contentArray 短信内容数组
     * @return 每组的发送情况，每组都包含键值对为ret：状态值（200为成功），retMsg：描述信息
     */
    public List<Map<String, Object>> sendSmsByMobiles(List<String> mobileNos, String templateID, String[] contentArray) {
    	List<Map<String, Object>> retMapList = Lists.newArrayList();
    	//String[] allSendMobileNos = mobileNos.split(",");
        int circleTimes = (int)(Math.floor(mobileNos.size() / 100)) + 1;
        for(int i=0; i<circleTimes; i++) {
        	Map<String, Object> retMap = new HashMap<String, Object>();
        	StringBuffer sendMobileNosStr = new StringBuffer();
        	int maxIndex = ((i+1)*100) < mobileNos.size() ? (i+1)*100 : mobileNos.size();
        	//要循环执行发送短信circleTimes次。从第i*100个~(i+1)*100个
        	for(int index=i*100; index<maxIndex; index++) {
        		if(mobileNos.get(index) != null) {
        			sendMobileNosStr.append(mobileNos.get(index)).append(",");
        		}
        	}
        	
        	//执行发送
        	HashMap<String, Object> result = CCPSDK.getInstance().sendTemplateSMS(sendMobileNosStr.toString(), templateID, contentArray);
        	logger.info("发送短信返回信息为：" + result.toString());
        	if ("000000".equals(result.get("statusCode"))) {
        		retMap.put("ret", "200");
        		retMap.put("retMsg", "第" + i*100 + "~" + maxIndex + "条发送成功");
        	} else {
        		retMap.put("ret", "-1");
                retMap.put("retMsg", "第" + i*100 + "~" + maxIndex + "条发送失败，错误原因：" + result.get("statusMsg"));
        	}
        	retMapList.add(retMap);
        }
    	return retMapList;
    }
    
    /**
     * 根据ConGroupsms对象，发送群发短信。
     * @param conGroupsms ConGroupsms对象
     * @return Map<String, Object>对象，包含键值对为ret：状态值（200为成功），retMsg：描述信息
     */
    public Map<String, Object> sendSmsByGroupsms(ConGroupsms conGroupsms) {
    	String ret = "200";
    	String retMsg = "";
    	Map<String, Object> retMap = new HashMap<String, Object>();
    	List<String> mobileNos = new Gson().fromJson(conGroupsms.getMobileArr(), List.class);
    	String[] contentArray = ((List<String>)new Gson().fromJson(conGroupsms.getContentArr(), List.class)).toArray(new String[0]);
    	//执行短信发送
    	List<Map<String, Object>> retMapList = this.sendSmsByMobiles(mobileNos, conGroupsms.getTempleteId(), contentArray);
    	for(Map<String, Object> resultRetMap : retMapList) {
			if(!resultRetMap.get("ret").equals("200")) {
				ret = resultRetMap.get("ret").toString();
				
				retMsg = retMsg + resultRetMap.get("retMsg").toString() + ";";
			}
		}
		retMap.put("ret", ret);
		retMap.put("retMsg", retMsg);
    	return retMap;
    }

    /**
     * 取得短信模板中需要处理的内容，目前主要处理验证码
     * 
     * @param contentArray
     * @return
     */

    public String[] getSmsContent(String[] contentArray, String verifyCode) {
        for (int i = 0; i < contentArray.length; i++) {
            if (contentArray[i] != null && contentArray[i].equals("[VerifyCode]")) {
                contentArray[i] = verifyCode;
            }
        }
        return contentArray;
    }

    public String checkVerifyCode(String mobileNo, String verifyCode, String verifySerialNo) throws Exception {
        return checkVerifyCode(mobileNo, verifyCode, verifySerialNo, true);
    }

    /**
     * 校验验证码是否有效
     * 
     * @throws Exception
     */
    public String checkVerifyCode(String mobileNo, String verifyCode, String verifySerialNo,
            boolean isCleanVerifySerialNo) throws Exception {

        if (mobileNo == null || mobileNo.length() == 0 || verifySerialNo == null || verifySerialNo.length() == 0) {
            // 参数传递错误
            return "1";
        }

        Element myItem = null;
        try {
            Object cacheObj = CacheUtils.get(cacheName, verifySerialNo);
            if (cacheObj != null && cacheObj instanceof Element) {
                
                myItem = (Element) cacheObj;
            }

            // 缓存中的value存放数组,0-手机号,1-验证码,2-有效时间(目前只进行存放)
            if (myItem != null) {
                String[] tmpString = (String[]) myItem.getValue();
                if (tmpString != null && tmpString.length == 3) {
                    if (tmpString[0] != null && tmpString[0].equals(mobileNo) && tmpString[1] != null
                            && tmpString[1].equals(verifyCode)) {
                        if (isCleanVerifySerialNo) {
                            // 验证成功后清楚缓存
//                            CacheManager.getInstance().removeItem(dbName, cacheName, verifySerialNo);
                          CacheUtils.remove(cacheName, verifySerialNo);
                        }
                        return "0";
                    }
                }
            }
            return "1";
        } catch (Exception ex) {
            throw new Exception("Error when checkVerifyCode for verifySerialNo field " + verifySerialNo
                    + " of mobileNo " + mobileNo, ex);
        }
    }

    /**
     * 目前支持 4-8位长度的数字验证码
     * 
     * @return
     */
    public static String getVerifyCode(int verifyCodeLen) {
        // int charLength = 8;
        if (verifyCodeLen < 4 || verifyCodeLen > 8) {
            verifyCodeLen = 4;
        }

        int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < verifyCodeLen; i++)
            result = result * 10 + array[i];

        String f = "%0" + verifyCodeLen + "d";
        return String.format(f, result);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        SMSVerify mm = new SMSVerify();
        String[] contentArray = { "[VerifyCode]", "5分钟" }; // 定义5分钟内有效
         try {
            System.out.println(mm.sendSmsVerifyCode("18317165040", 6, "204863", contentArray, 500));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
