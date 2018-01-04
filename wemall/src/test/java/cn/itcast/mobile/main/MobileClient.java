package cn.itcast.mobile.main;

import cn.itcast.mobile.ArrayOfString;
import cn.itcast.mobile.EnglishChinese;
import cn.itcast.mobile.EnglishChineseSoap;
import cn.itcast.mobile.MobileCodeWS;
import cn.itcast.mobile.MobileCodeWSSoap;

/**
 * 
 * <p>Title: MobileClient.java</p>
 * <p>Description:公网手机号查询客户端</p>*
 */
public class MobileClient {

	public static void main(String[] args) {
		//创建服务视图
		MobileCodeWS mobileCodeWS = new MobileCodeWS();
		//获取服务实现类
		MobileCodeWSSoap mobileCodeWSSoap = mobileCodeWS.getPort(MobileCodeWSSoap.class);
		//调用查询方法
		String reuslt = mobileCodeWSSoap.getMobileCodeInfo("1580399", null);
		System.out.println(reuslt);
		
		//创建服务视图
		/*EnglishChinese englishChinese = new EnglishChinese();
		//获取服务实现类
		EnglishChineseSoap englishChineseSoap = englishChinese.getPort(EnglishChineseSoap.class);
		//调用查询方法
		ArrayOfString translatorReferString = englishChineseSoap.translatorReferString("西瓜");
		System.out.println(translatorReferString.getString());*/
		
	}
}
