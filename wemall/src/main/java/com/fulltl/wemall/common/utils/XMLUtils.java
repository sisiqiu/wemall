package com.fulltl.wemall.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.common.collect.Lists;

/**
 * xml解析工具类
 * @author ldk
 *
 */
public class XMLUtils {
	/**
	 * 将request中的xml文本解析为Map容器返回。
	 * @param request http请求
	 * @return 储存有request请求信息的Map类型容器
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");

        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(isr);
        //遍历将根节点下的子节点加入map当中
        Element Node = document.getRootElement();
        List<Element> elements = Node.elements();
        for(Element e: elements) {
        	map.put(e.getName(), e.getText());
        }
        
        isr.close();
        is.close();
        /*String userName = document.selectSingleNode("//ToUserName").getText();
        map.put("ToUserName", userName);
        String fromUserName = document.selectSingleNode("//FromUserName").getText();
        map.put("FromUserName", fromUserName);
        String createTime = document.selectSingleNode("//CreateTime").getText();
        map.put("CreateTime", createTime);
        String msgType = document.selectSingleNode("//MsgType").getText();
        map.put("MsgType", msgType);
        String content = document.selectSingleNode("//Content").getText();
        map.put("Content", content);
        String msgId = document.selectSingleNode("//MsgId").getText();
        map.put("MsgId", msgId);*/
        return map;
    }
	
	/**
	 * 将xml文本解析为Map容器返回。
	 * @param str http请求
	 * @return Map类型容器
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(String str) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document = null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
		document = reader.read(inputStream);
        //遍历将根节点下的子节点加入map当中
        Element Node = document.getRootElement();
        List<Element> elements = Node.elements();
        for(Element e: elements) {
        	map.put(e.getName(), e.getText());
        }
        inputStream.close();
        return map;
	}
	
	
	 /**
     * xml转成map
     * @param xmlstr xml报文
     * @return
     */
    public static Map<String, Object> xmltoMap(String xmlstr){
         Document doc = null;  
         try {  
             doc = DocumentHelper.parseText(xmlstr);  
         } catch (DocumentException e) {  
            //logger.error("parse text error : " + e);  
         }  
             Element rootElement = doc.getRootElement();
             Map<String,Object> mapXml = new HashMap<String,Object>();  
             elementToMap(mapXml,rootElement);
         return mapXml;  
    }

    /**
     * 遍历子节点
     * @param map 
     * @param rootElement
     */
    public static void elementToMap(Map<String, Object> map, Element rootElement){
        //获得当前节点的子节点  
        List<Element> elements = rootElement.elements();
        Map<String,Object> childMap = new HashMap<String,Object>();
        //如果还存在子节点，就考虑list情况，继续递归 
        for (Element element : elements) {
            List<Element> es = element.elements();
            
            if(es.size()>0){
                //获取当前节点下的子节点
                ArrayList<Map> list = new ArrayList<Map>();
                for(Element e:es){
                    elementChildToList(list,e);
                }
                
            	map.put(element.getName(), list);
            }else{
                map.put(element.getName(),element.getText());
            }
             
        }
    }
    
    /**
     * 验证map中是否有该key，有的话，取出生成list再放进去
     * @param map
     * @param key
     * @param value
     */
    private static void putToMap( Map<String,Object> map, String key, Object value) {
    	Object oldValue = map.get(key);
        if(oldValue != null) {
        	if(oldValue instanceof List) {
        		//如果是List对象
        		((List)oldValue).add(value);
        		map.put(key, oldValue);
        	} else {
        		//如果是其他对象
        		List valueList = Lists.newArrayList();
        		valueList.add(value);
        		map.put(key, valueList);
        	}
        } else {
        	map.put(key, value);
        }
    }

   /**
     * 递归子节点
     * @param arrayList
     * @param rootElement
     */
    public static void elementChildToList(ArrayList<Map> arrayList, Element rootElement){
        //获得当前节点的子节点  
        List<Element> elements = rootElement.elements();
        if(elements.size()>0){
            ArrayList<Map> list = new ArrayList<Map>();
            Map<String,Object> sameTempMap = new HashMap<String,Object>();
            for(Element element:elements){
                elementChildToList(list,element);
                sameTempMap.put(element.getName(), element.getText());
            }
            arrayList.add(sameTempMap);
        }
        
    }
}
