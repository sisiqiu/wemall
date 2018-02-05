<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/common.js"></script>
		<title>绑定手机</title>
		<link rel="stylesheet" href="${ ctxStatic}/sanlen_website/css/wx.css">
	<style>
		.container{
			padding-top: 1.26rem;
			padding-bottom: 1.26rem;
		}
		header{
			background: url(${ ctxStatic}/sanlen_website/img/wx/2.png);
			background-size: cover;
			height: 4.79rem;
			width: 4.79rem;
			margin: 0 auto 1.02rem auto;
		}
		.input{
			margin-bottom: -0.08rem;
		}
		form >div{
			text-align: center;
		}
		form >div input:nth-child(1) {
			 width:4.4rem;
			 margin-right: 0.08rem;
			 display: inline;
			 box-sizing: border-box;
			 margin-bottom: 0.41rem;
		}
		form >div button:nth-child(2) {
			 width:2.12rem;
			 height: 0.99rem;
			 display: inline;
			 font-size: 0.24rem;
			 padding-left: 0.48rem;
			 line-height: 0.99rem;
			 word-spacing: 0rem;
			 text-align: left;
			 color: #666;
			 background: white;
			 box-sizing: border-box;
			 margin-bottom: 0.41rem;
		}
		.button{
			background: #06f;
		}
		.textAlert{
			width: 6.6rem;
			font-size: 0.2rem;
			color: #ff0a00;
			margin: -0.2rem auto 0.22rem auto;
			padding-left: 0.38rem;
			/*margin-top: -0.2rem;*/
			/*margin-bottom: 0.22rem;*/
		}
	</style>
</head>
<body>
	<div class="container">
		<header>
				
		</header>
		<main>
			<form action="" onsubmit='return false'>
				<input type="hidden" id="verifyServID" name="verifyServID"
					value="4155e4f33c2c433ab7d3f8769dadd297">
				<input type="hidden" id="serviceId" value="${serviceId}">
				<input type="hidden" id="openId" value="${userInfo.openid}">
				<input id="mobile" class="input" type="text" placeholder="请输入手机号进行绑定">
				<div>
					<input id="sms_code" name="sms_code" class="input" type="text" placeholder="请输入已发送的验证码"><button id="btn_code" class="button " onclick="getSMSCode()">发送验证码</button>
				</div>
				<p class="textAlert" style="display: none;">您输入的手机号或验证码错误，请重试！</p>
				<button onclick="fast_login();" class="button" type="submit">绑定</button>
			</form>
		</main>
	</div>
	<script type="text/javascript">
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
		var openId = "";
		function getOpenId() {
			if(isWeiXin()) {
				//微信场景下，要获取openId。
				//查询localStorage中有没有
				openId = "${openId}";
				var localStorageOpenId = window.localStorage.openId;
				
				if(openId != "") {
					window.localStorage.openId = openId;
				}
				//定义全局openId
				openId = (openId != "") ? openId : localStorageOpenId;
				$("#openId").text("openId:" + openId);
				if(isWeiXin() && openId == "" && localStorageOpenId == null) {
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
		
		$(document).ready(function(){
			if(isWeiXin()) {
				getOpenId();
				//alert(getOpenId());
			}
		});
		
	</script>
	<script>
	
		// 快速登录
		function fast_login() {
			var param = {};
			param.mobile = $("#mobile").val();
			//param.loginFrom = "fast_login";
			param.verifyServID = $("#verifyServID").val();
			param.sms_code = $("#sms_code").val();
			param.serviceId = $("#serviceId").val();
			param.openId = openId; //$("#openId").val()
			console.log(param);
			if (!param.mobile) {
				alert("手机号码不能为空");
				return;
			}
			if (!certMobile(param.mobile)) {
				alert("手机号格式错误");
				return;
			}
			if (!param.sms_code) {
				alert("验证码不能为空");
				return;
			}
			var data = {};
			data.url = "${ctx}/wx/user/bindUser";
			data.type = "GET";
			data.param = param;
			postData(data,function(data) {
				if (data.ret == "200") {
					alert("登陆成功！");
					//跳转到活动参加页面,活动ID：activityId
					window.location.href = "${ ctx}/wx/conActivity/joinPageJump?id=${activityId}";
				} else {
					alert(data.retMsg);
				}
			});
		}

		// 获取手机验证码
		function getSMSCode() {
			var param = {};
			param.mobile = $("#mobile").val();
			param.loginType = "SMSCode";
			if (!param.mobile) {
				alert("手机号码不能为空");
				return;
			}
			if (!certMobile(param.mobile)) {
				alert("手机号格式错误");
				return;
			}
			console.log(param);
			var val = 60;
			$('#btn_code').html('重新发送' + val + '(s)');
			var setTim = setInterval(function() {
				if (val > 1) {
					$('#btn_code').attr('disabled', 'disabled');
					val--;
					$('#btn_code').html('重新发送' + val + '(s)');
				} else {
					val = "获取验证码";
					$('#btn_code').html(val);
					$('#btn_code').removeAttr('disabled');
					clearInterval(setTim);
				}
			}, 1000);
			
			var data = {};
			data.url = "${ctx}/getCode";
			data.type = "GET";
			data.param = param;
			postData(data, function(data) {
				if (data.ret == "200") {
					$("#verifyServID").val(data.verifyID);
					alert("验证码发送成功");
					//                  $("#verifyServID").val() = data.verifyID;
				} else {
					alert(data.retMsg);
				}
			});
		};

		// 手机号或验证码错误时提醒
		function textAlert(){
			$('.textAlert').css('display','block');
		}
</script>
</body>
</html>
