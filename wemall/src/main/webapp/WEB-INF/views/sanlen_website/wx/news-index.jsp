<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/common.js"></script>
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/wxjs.js"></script>
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/vue.js"></script>
		<title>新闻活动主页</title>
		<link rel="stylesheet" href="${ ctxStatic}/sanlen_website/css/wx.css">
	<style>
		li{
			list-style:none;		
		}
		[v-cloak]{
			display:none !important;		
		} 
	</style>
</head>
<body>
	
	<div class="container news-index">
		<div class="swiper">
			<!-- <ul>
			</ul> -->
			<ul id="new_list" v-cloak>
  				<li v-for="one in data">
    				<img v-bind:src="one.picurl" />
					<div>
						<h3>{{ one.title }}</h3>
						<a v-bind:href="['${ ctx}/wx/conActivity/viewActivity?id='+one.id]"><button>点击进入</button></a>
					</div>
 				</li>
			</ul>
				<%-- <li>
					<img src="${ ctxStatic}/sanlen_website/img/wx/1-1.png" alt="">
					<div>
						<h3>企业信息安全与超融合交流会</h3>
						<a href="#"><button>点击进入</button></a>
					</div>
				</li>
				<li>
					<img src="${ ctxStatic}/sanlen_website/img/wx/2-1.png" alt="">
					<div>
						<h3>企业信息安全与超融合交流会</h3>
						<a href="#"><button>点击进入</button></a>
					</div>
				</li>
				<li>
					<img src="${ ctxStatic}/sanlen_website/img/wx/3-1.png" alt="">
					<div>
						<h3>企业信息安全与超融合交流会</h3>
						<a href="#"><button>点击进入</button></a>
					</div>
				</li>
				<li>
					<img src="${ ctxStatic}/sanlen_website/img/wx/2-1.png" alt="">
					<div>
						<h3>企业信息安全与超融合交流会</h3>
						<a href="#"><button>点击进入</button></a>
					</div>
				</li>
				<li>
					<img src="${ ctxStatic}/sanlen_website/img/wx/3-1.png" alt="">
					<div>
						<h3>企业信息安全与超融合交流会</h3>
						<a href="#"><button>点击进入</button></a>
					</div>
				</li> --%>
		</div>
		
	</div>
	<script type="text/javascript">
		//微信部分openId部分代码
		$(document).ready(function(){
			/**
			*	判断是否处于微信浏览器中。
			*/
			function isWeiXin(){
				var ua = window.navigator.userAgent.toLowerCase();
				if(ua.match(/MicroMessenger/i) == 'micromessenger'){
					return true;
				}else{ 
					return false;
				} 
			} 
		
			function getOpenId() {
				if(isWeiXin()) {
					//微信场景下，要获取openId。
					//查询localStorage中有没有
					var openId = "${openId}";
					var localStorageOpenId = window.localStorage.openId;
					
					if(openId != "") {
						window.localStorage.openId = openId;
					}
					//定义全局openId
					openId = (openId != "") ? openId : localStorageOpenId;
					$("#openId").text("openId:" + openId);
					if(isWeiXin() && !openId) {
						//设定服务号
						var serviceId = "${serviceId}";
						//获取主机地址之后的目录，如： SSAPP/manage/backManage.html
						var pathName = window.document.location.pathname;
						//当前地址，获取openId之后要跳转的地址，如"/f/wx/conActivity/viewActivities"
						var redirect = pathName;
						window.location.href = "${ctx}/wx/core/getOpenId?serviceId=" + serviceId + "&redirect=" + redirect;
					}
					return openId;
				}
			}
			
			function loginByOpenId(openId) {
				var param = {};
				param.openId = openId;
				var data = {};
				data.url = "${ ctx}/wx/user/loginByWxOpenId";
				data.type = "GET";
				data.param = param;
				postData(data, function(data) {
					if(data.ret == "200") {
						//console.log(data.retMsg);
						//alert("登陆成功！");
					}
				});
			}
			if(isWeiXin()) {
				loginByOpenId(getOpenId());
				//alert(getOpenId());
			}
		});
	</script>
	<script>
		$(document).ready(function(){
			var HTML="";

			// 后台获取活动列表相关信息，渲染到页面
			/* $.ajax({
                url:"/f/wx/conActivity/listForAjax",
                type:"GET",
                dataType:"json",
                data:{"categoryID":"1f02c851daec4f8f97e21afea95cdd0e"},
                contentType:"application/x-www-form-urlencoded",
                success:function(data){
                	alert("回调函数");
                    console.log(data);
                    for(var i in data){
                    	HTML += '<li id="'+i+'"><img src="'+图片地址+'"><div><h3>'+data.标题+'</h3><a href="'+链接地址+'">点击进入</a></div> '

                    	$('.swiper>ul').html(HTML)//内容渲染完成
                    }
                }
            }); */
			var data = {};
			data.url = "${ ctx}/wx/conActivity/listForAjax";
			data.type = "GET";
			//data.param = param;
			postData(data, function(data) {
				console.log("data===",JSON.stringify(data));
				//alert(data.length)
				/* for(var i=0;i<data.length; i++){
                	HTML += "<li id=\"" +i + "\">" + 
                				"<img src=\"" + data[i].picurl + "\">" + 
               					"<div>" + 
               						"<h3>" + data[i].title + "</h3>" + 
               						"<a href=\"${ ctx}/wx/conActivity/viewActivity?id=" + data[i].id + "\"><button>点击进入</button></a>" +
               					"</div>" + 
                			"</li>";

                	$('.swiper>ul').html(HTML)//内容渲染完成
                } */
                var activeInfo = new Vue({
                	  el: '#new_list',
                	  data: {
                		 data
                	  }
                	});
               
//                 activeInfo.push;
			});
		});
		///static/sanlen_website/img/wx/1-1.png
	</script>
</body>
</html>
