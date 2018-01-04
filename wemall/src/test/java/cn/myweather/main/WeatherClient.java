package cn.myweather.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import cn.myweather.WeatherInterfaceImpl;

import cn.itcast.mobile.MobileCodeWSSoap;
import cn.myweather.WeatherInterfaceImplService;

/**
 * 
 * <p>Title: ServiceClient.java</p>
 * <p>Description:Service编程实现服务端调用</p>
 * 
 */
public class WeatherClient {

	public static void main(String[] args) throws IOException {
		//创建WSDL的URL，注意不是服务地址
		URL url = new URL("http://127.0.0.1:12345/weather?wsdl");
		
		//创建服务名称
		//1.namespaceURI - 命名空间地址
		//2.localPart - 服务视图名
		QName qname = new QName("http://ldk.sanlen.com/", "WeatherInterfaceImplService");
		
		//创建服务视图
		//参数解释：
		//1.wsdlDocumentLocation - wsdl地址
		//2.serviceName - 服务名称
		Service service = Service.create(url, qname);
		//获取服务实现类
		WeatherInterfaceImpl mobileCodeWSSoap = service.getPort(WeatherInterfaceImpl.class);
		//调用查询方法
		String result = mobileCodeWSSoap.queryWeather("bb");
		System.out.println(result);
	}
}
