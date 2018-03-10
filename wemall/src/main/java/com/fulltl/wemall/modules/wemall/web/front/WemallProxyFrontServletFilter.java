package com.fulltl.wemall.modules.wemall.web.front;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fulltl.wemall.common.config.Global;


/**
 * 医院用户管理前端接口
 * @author ldk
 *
 */
@SuppressWarnings("serial")
public class WemallProxyFrontServletFilter implements Filter {
	
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    	/*slAppsessionService = SpringContextHolder.getBean(SlAppsessionService.class);
    	systemService = SpringContextHolder.getBean(SystemService.class);*/
    }
    
    /**
     * 此处做参数签名验证
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	//统一跨域处理
    	allowOrigin((HttpServletRequest)request, (HttpServletResponse)response);
    	chain.doFilter(request, response);
    }
    
    /**
	 * 统一处理跨域请求
	 * @param response
	 */
	protected void allowOrigin(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("text/plain;charset=UTF-8");
		
        String []  allowDomain= Global.getConfig("AccessControlAllowOrigin").split(",");  
        Set allowedOrigins= new HashSet(Arrays.asList(allowDomain));  
        String originHeader = request.getHeader("Origin");  
        if (allowedOrigins.contains(originHeader)){  
        	response.setHeader("Access-Control-Allow-Origin", originHeader);  
        }  
		
		//response.setHeader("Access-Control-Allow-Origin", ); //允许哪些url可以跨域请求到本域
	}
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
}
