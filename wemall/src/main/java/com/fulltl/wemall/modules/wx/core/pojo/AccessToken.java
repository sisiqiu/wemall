package com.fulltl.wemall.modules.wx.core.pojo;

import java.io.Serializable;

public class AccessToken implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;
    
    private int expiresIn;

   
    public String getToken() {
        return token;
    }

   
    public void setToken(String token) {
        this.token = token;
    }

   
    public int getExpiresIn() {
        return expiresIn;
    }

   
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
    
}

