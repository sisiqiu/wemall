package com.fulltl.wemall.modules.wemall.web.front;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fulltl.wemall.common.web.BaseController;
import com.fulltl.wemall.modules.wemall.service.front.WemallSysFrontService;
import com.google.gson.Gson;

/**
 * 系统管理前端接口
 * @author ldk
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/interface/wemall/sys")
public class WemallSysFrontController extends BaseController {
	@Autowired
	private WemallSysFrontService slHisSysFrontService;
	
	/**
	 * 根据字典类型获取字典列表的接口。
	 * 
	 * 测试用例：
	 * 	url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/getDictListByType
	 *	参数：type=字典类型
	 *
	 * 	例：type=sex
	 * 
	 * 结果示例：{"ret":"0","data":{"list":[{},{}],"count":3},"retMsg":"获取成功！"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"字典类型不能为空！"}
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getDictListByType"})
	@ResponseBody
	public String getDictListByType(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> map = slHisSysFrontService.getDictListByType(request);
		return new Gson().toJson(formatReturnMsg(map));
	}
	
	/**
	 * 首页信息获取接口
	 * http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/getHomePageData
	 * 参数：
	 *	例：{
	 *		}
	 *
	 * 结果示例：{
	 *		    "ret": "0",
	 *		    "data": {
	 *		        "banna": [],
	 *		        "navBars": [],
	 *		        "middleNavBars": [],
	 *		        "goodsList": [],
	 *				"servicePhone": ""
	 *		    },
	 *		    "retMsg": "获取成功"
	 *		}
	 */
	@RequestMapping(value = {"getHomePageData"})
	@ResponseBody
	public String getHomePageData(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.getHomePageData(request, response);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 获取公司宣传文章类别列表获取接口
	 * http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/getIntroduceData
	 * 参数：
	 *	例：{
	 *		}
	 *
	 * 结果示例：{
	 *		    "ret": "0",
	 *		    "data": {
	 *		    },
	 *		    "retMsg": "获取成功"
	 *		}
	 */
	@RequestMapping(value = {"getIntroduceData"})
	@ResponseBody
	public String getIntroduceData(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.getIntroduceData(request, response);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 文件上传接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/uploadFile
	 * 参数：file=文件
	 *
	 *	例：
	 *
	 * 结果示例：{"ret": "0", "data": {}, "retMsg": "上传成功"}
	 * 		或
	 * 		{"ret":"60025","data":{},"retMsg":"上传失败，文件过大！"}
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"uploadFile"})
	@ResponseBody
	public String uploadFile(@RequestParam(value="file", required=false) MultipartFile file, HttpServletRequest request, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.uploadFile(file, request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 头像上传接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/uploadUserPhoto
	 * 参数：file=文件
	 * 		type=doctor或者patient，
	 *	例：
	 *
	 * 结果示例：{"ret": "0", "data": {}, "retMsg": "上传成功"}
	 * 		或
	 * 		{"ret":"60025","data":{},"retMsg":"上传失败，文件过大！"}
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"uploadUserPhoto"})
	@ResponseBody
	public String uploadUserPhoto(@RequestParam(value="file", required=false) MultipartFile file, HttpServletRequest request, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.uploadUserPhoto(file, request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 获取系统配置的独立url链接接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/getConfigUrls
	 * 参数：
	 * 
	 *	例：
	 *
	 * 结果示例：{"ret": "0", "data": {}, "retMsg": "获取成功"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"获取失败！"}
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getConfigUrls"})
	@ResponseBody
	public String getConfigUrls(HttpServletRequest request, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.getConfigUrls(request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
	
	/**
	 * 根据类别获取系统配置的独立url链接接口
	 * url：http://ldkadmin.viphk.ngrok.org/f/interface/wemall/sys/getConfigUrlsByType
	 * 参数：type=类别
	 * 
	 *	例：
	 *
	 * 结果示例：{"ret": "0", "data": {}, "retMsg": "获取成功"}
	 * 		或
	 * 		{"ret":"-1","data":{},"retMsg":"获取失败！"}
	 * 
	 * @param file
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"getConfigUrlsByType"})
	@ResponseBody
	public String getConfigUrlsByType(HttpServletRequest request, Model model) {
		Map<String ,Object> retMap=slHisSysFrontService.getConfigUrlsByType(request);
		return new Gson().toJson(formatReturnMsg(retMap));
	}
}
