<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/common.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/vue.js"></script>
	
	<!-- <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> -->
	<title>活动介绍页面</title>
	<link rel="stylesheet" href="${ ctxStatic}/sanlen_website/css/wx.css">
	<style>
		[v-cloak]{
			display:none !important;		
		}
	</style>
</head>
<body>
	<div class="container news-detail" id="activity_info" v-cloak>
		<main>
			<h3 >{{msg.title}}</h3>
			<section>
				<p>主办方：<span>{{msg.organizer}}</span></p>
				<p>开始时间：<span id="time">{{msg.fromdate}}</span></p>
				<p>举办地点 ：<span>{{msg.location}}</span></p>
			</section>

			<section class="bg-logo" style="margin: 0.13rem auto 0.26rem auto;">
				<img  v-bind:src="msg.picurl" alt="">
			</section>

			<section id="content" >
				{{msg.content}}
			</section>
			
			<section>
				<p style="margin-top: 0.5rem;">会议地址：</p>
				<div class="bg-logo" style="margin:0.5rem auto 0.46rem auto; width: 7.02rem; height: 8.73rem; border-radius: 12px; overflow: hidden;">
					<!-- <img src="${ ctxStatic}/sanlen_website/img/wx/9.png" alt=""> -->
					<iframe v-bind:src="['http://apis.map.qq.com/tools/poimarker?type=1&keyword=酒店&center='+msg.lat+','+msg.lng+'&radius=1000&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp']"  frameborder="0" seamless width="100%" height="100%"></iframe>
					<%-- <iframe  src="http://apis.map.qq.com/tools/poimarker?type=1&keyword=酒店&center=${conActivityRe.lat},${conActivityRe.lng}&radius=1000&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp" frameborder="0" seamless width="100%" height="100%"></iframe> --%>
					<!-- <iframe src="http://apis.map.qq.com/tools/routeplan/eword=故宫博物馆&epointx=116.39710&epointy=39.917200?referer=myapp&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77" frameborder="0" seamless width="100%" height="100%"></iframe> -->		
				</div>
			</section>
			<!--  <a href="${ ctx}/publicToPage?pageUrl=sanlen_website/wx/apply"></a>-->
			
			<input id="activity_id" type="hidden" value="${activityID}" />
		</main>
	</div>
	<input id="join_status" class="button" type="button" v-bind:value="buttonText.message"/>
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
					}
				});
			}
			if(isWeiXin()) {
				loginByOpenId(getOpenId());
			}
		});
		
	</script>
	<script>
		
		$(document).ready(function(){
			//vue绑定活动按钮内容
			var buttonText={message: ''};
			var activiveButton=new Vue({
				el:'#join_status',
				data:{ buttonText }
			});
			//获取活动主键Id
			var activityId= $("#activity_id").val();
			//保存活动状态情况
			var activityStatus;
			//保存有无用户以及用户活动的状态
			var userActivityStatus;
			var errorStatus;
			$.ajax({
				type: "POST",
				url: "${ ctx}/wx/conActivity/viewActivityInfo",
				data: "id="+activityId,
				success: function(msg){
					activityStatus=msg.status;
					var activeInfo = new Vue({
						el: '#activity_info',
						data: {
							msg
						}
					});
					theButton();
					//html化会议内容
					$("#content").html($("#content").text());
				}
			});
			
			//保存有无用户情况
			function theButton() {
				$.ajax({
					   type: "POST",
					   url: "${ ctx}/wx/conActivity/userActivityStatus",
					   data: "id="+activityId,
					   success: function(data){
						   var json = JSON.parse(data);
						   userActivityStatus = json.userActivityStatus;
						   errorStatus = json.errorStatus;
						   switch(activityStatus) {
						   case "0":
							   buttonText.message ="活动未开始报名";
							   break;
						   case "1":
							   buttonText.message ="开始报名";
							   break;
						   case "2":
							   buttonText.message ="报名结束";
							   break;
						   case "5":
							   buttonText.message ="等待抽奖";
							   break;
						   case "6":
							   buttonText.message ="活动结束";
							   break;
						   }
						   
						   switch(userActivityStatus) {
						   case "1":
							   if(activityStatus == "1" || activityStatus == "2") {
								   buttonText.message ="已报名";
							   }
							   break;
						   case "3":
							   if(activityStatus == "5") {
									buttonText.message ="已中奖";
							   }
							   break;
						   }
							/* if(activityStatu==0){
								buttonText.message ="活动未开始报名";
							}else if(activityStatu==1){
								if(userActivityStatus=="noUserActivity"){
									buttonText.message ="立即参加" ;
								}else if(userActivityStatus=="1"){
									buttonText.message ="已参加" ;
								}else if(userActivityStatus=="noUser"){
									//alert("进入用户活动状态为空的情况")
									buttonText.message ="立即参加" ;
								}else if(userActivityStatus=="2"){
									buttonText.message = "已签到";
								}else if(userActivityStatus=="3"){
									buttonText.message = "已抽奖";
								}
							}else if(activityStatu=="2"){
								buttonText.message ="报名结束" ;
							}else if(activityStatu=="5"){
								if(userActivityStatus=="1") {
									buttonText.message ="已报名，等待抽奖" ;
								}else if(userActivityStatus=="noUser"){
									buttonText.message ="报名时间结束" ;
								}else if(userActivityStatus=="noUserActivity"){
									buttonText.message ="报名时间结束" ;
								}else if(userActivityStatus=="3"){
									buttonText.message ="已中奖" ;
								}
							}else if(activityStatu=="6"){
								buttonText.message ="活动结束" ;
							} */
					   }
				});
			}
			
			//用户参加活动状态的改变
			$("#join_status").click( function () {
				if(isWeiXin()) {
					//如果是在微信中，判断到：没有用户（即没有绑定系统用户）。
					//执行：跳转到绑定界面（此界面需要获取用户信息）
					
					//如果是在微信中，判断到：已有用户。
					//执行：跳转到参加页面。
					
				} else {
					//如果不在微信中，在网页web中，判断到：没有用户。
					//执行：跳转到官网登陆界面，http://ldkadmin.tunnel.qydev.com/sanlen_website/u/login
					if(errorStatus=="noUser" && activityStatus == "1") {
						window.location.href = "${ctxMember}/login";
						
						return;
					}
					//alert("微信");
					//如果不在微信中，在网页web中，判断到：已有用户。
					//执行：跳转到参加页面
					
				}
				
				switch(activityStatus) {
				case "0":
					//buttonText.message ="活动未开始报名";
					break;
				case "1":
					//buttonText.message ="开始报名";
					if(userActivityStatus == "1" || userActivityStatus == "2") {
						//buttonText.message ="已报名";
					} else {
						window.location.href = "${ ctx}/wx/conActivity/joinPageJump?id="+activityId;
					}
					break;
				case "2":
					//buttonText.message ="报名结束";
					break;
				case "5":
					//buttonText.message ="开始抽奖";
					break;
				case "6":
					//buttonText.message ="活动结束";
					break;
				}
				
				switch(userActivityStatus) {
				case "1":
					break;
				case "3":
					if(activityStatus == "5") {
						//buttonText.message ="已抽奖";
					}
					break;
				}
			   	
				//如果没有用户的情况 就跳到微信绑定账号页面
				/* if(userActivityStatus=="noUser"){
					if(activityStatus == "1"){
						window.location.href = "${ ctx}/wx/conActivity/bindingPageJump?id=" + activityId;
					}
				}else if(userActivityStatus == "noUserActivity"){
					//且当前活动状态为1 开始参加 就跳到参加页面
					if(activityStatus == "1"){
						//有用户存在  且 满足参加时间  跳转至此页面    改成已参加状态 1
						//alert("跳到参加页面的判断成功")
						window.location.href = "${ ctx}/wx/conActivity/joinPageJump?id="+activityId;
					}
				}else if(userActivityStatus == "1"){
					if(activityStatu=="3"){
						//满足签到时间  而且已经报名 改成已签到状态  2
						$.ajax({
						   type: "POST",
						   url: "${ ctx}/wx/conActivity/EditUserActivityStatus",
						   data: "editStatu=1&&id="+activityId,
						   success: function(msg){
							   if(msg == "signed"){
								   buttonText.message ="已签到";
								   //popUp("succeed");
							   }else if(msg == "timeOut"){
								   alert("抱歉，签到时间已过");
							   }
						   }
						});
					}
				} */
			});
		});
		
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
	</script>
</body>
</html>
