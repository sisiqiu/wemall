package com.fulltl.wemall.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

/**
 * 继承javax.servlet.http.HttpServletRequestWrapper，是一个扩展的通用接口
 * @author Administrator
 *
 */
public class ParameterRequestWrapper extends ShiroHttpServletRequest {
    
    private Map<String , String[]> params = new HashMap<String, String[]>();

    @SuppressWarnings("unchecked")
    public ParameterRequestWrapper(ShiroHttpServletRequest request) {
        // 将request交给父类，以便于调用对应方法的时候，将其输出，其实父亲类的实现方式和第一种new的方式类似
        super(request, request.getSession().getServletContext(), request.isHttpSessions());
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }
    
    //重载一个构造方法
    public ParameterRequestWrapper(ShiroHttpServletRequest request , Map<String , Object> extendParams) {
        this(request);
        addAllParameters(extendParams);//这里将扩展参数写入参数表
    }
    
    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }
    
    public String[] getParameterValues(String name) {//同上
         return params.get(name);
    }

   public void addAllParameters(Map<String , Object>otherParams) {//增加多个参数
        for(Map.Entry<String , Object>entry : otherParams.entrySet()) {
            addParameter(entry.getKey() , entry.getValue());
        }
    }


    public void addParameter(String name , Object value) {//增加参数
        if(value != null) {
            if(value instanceof String[]) {
                params.put(name , (String[])value);
            }else if(value instanceof String) {
                params.put(name , new String[] {(String)value});
            }else {
                params.put(name , new String[] {String.valueOf(value)});
            }
        }
    }
    /**
     * 将request.getParameterMap()转化成正常的Map
     * @param req
     * @return
     * @author kang.yang
     * @date   创建日期：2017年11月30日 下午10:26:30
     */
    public static Map<String,Object> reqParamterToMap(HttpServletRequest req){
        Map<String, String[]> m=req.getParameterMap();
        Map<String,Object> rm=new HashMap<String,Object>();
        Iterator<String> itor=m.keySet().iterator();
        while(itor.hasNext()){
            String key=itor.next();
            String[] strs=m.get(key);
            String val=null;
            if(strs.length>0){
                val=strs[0];
            }
            rm.put(key, val);
        }
        return rm;
    }
}