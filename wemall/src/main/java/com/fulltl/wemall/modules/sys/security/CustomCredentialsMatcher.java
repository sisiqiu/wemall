package com.fulltl.wemall.modules.sys.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;


/**
 *	自定义验证器
 *	用于统一 GC与此处用户认证是否相同
 *
 */
public class CustomCredentialsMatcher extends HashedCredentialsMatcher {
    
    /**
     * 
     */
    public void setHashAlgorithmName(String hashAlgorithmName) {
        super.setHashAlgorithmName(hashAlgorithmName);
    };
    
    /* (non-Javadoc)
     * @see org.apache.shiro.authc.credential.HashedCredentialsMatcher#setHashIterations(int)
     */
    @Override
    public void setHashIterations(int hashIterations) {
        // TODO Auto-generated method stub
        super.setHashIterations(hashIterations);
    }
    
    
	public boolean doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info) {
        UsernamePasswordToken usertoken = (UsernamePasswordToken) token; 
        // 密码验证
        String login_type = usertoken.getLogin_type();
        // 如果是短信验证码登陆，则直接跳过密码验证环节
        if ("SMSCode".equals(login_type)){
            return true;
        } else {
//            return true;
//            super.setHashAlgorithmName(hashAlgorithmName);
            return super.doCredentialsMatch(usertoken, info);
        }
        
       
    }
	

    public static void main(String[] args){
	
	}
	
	
}