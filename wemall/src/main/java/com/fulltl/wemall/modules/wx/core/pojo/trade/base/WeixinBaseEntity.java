package com.fulltl.wemall.modules.wx.core.pojo.trade.base;

/**
 * 微信基础实体类
 * @author Administrator
 *
 */
public class WeixinBaseEntity {
	private String return_code; //返回状态码。SUCCESS/FAIL，此字段是通信标识，非交易标识
	private String return_msg; //返回信息。返回信息，如非空，为错误原因：签名失败，参数格式校验错误
	
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
}
