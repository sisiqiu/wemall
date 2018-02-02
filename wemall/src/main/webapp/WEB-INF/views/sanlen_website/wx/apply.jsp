<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/common.js"></script>
	<link rel="stylesheet" href="${ ctxStatic}/sanlen_website/css/wx.css">
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/wxjs.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/vue.js"></script>
	<title>会议报名</title>
	<style>
		.container{
			padding-top: 1.5rem;
		}
	</style>
</head>
<body>
	<div class="container apply">
		<header>
			<p>
				立即参加<hr>
			</p>
			<p>
				ATTEND IMMEDIATELY
			</p>	
		</header>
		<main>
			<form action=""  method="post" id="user_act_form" onsubmit='return false'>
				<input type="hidden" id="verifyServID" name="verifyServID"
					value="4155e4f33c2c433ab7d3f8769dadd297">
				<input type="hidden" id="serviceId" value="${serviceId}">
				<input type="hidden" id="openId" value="${userInfo.openid}">
			
				<input id="activityId" name="activityid" type="hidden"  value="${activityId}"/>
				<input type="text" name="userName"  placeholder="姓名：">
				<input id="mobile" type="text" name="mobile"  placeholder="联系方式：">
				<div style="overflow:hidden;width:6.6rem;margin:auto;">
					<input style="float:left;width:4rem;" id="sms_code" name="sms_code" class="input" type="text" placeholder="请输入已发送的验证码">
					<button style="float:right;width:2rem;font-size: 0.24rem;" id="btn_code" class="button" onclick="getSMSCode()">发送验证码</button>
				</div>
				<input type="text" name="information"  placeholder="参会信息：">
				<input type="text" name="note"  placeholder="备注留言：">
				<input type="button" value="立即参加">
			</form>
		</main>
		<footer>
			<div>期待光临<br>我们会场不见不散</div>
		</footer>
		<div class="pop-up" style="display: none;">
			<div class="bg"></div>
			<div class="message">
				<p>提示</p>
				<hr>
				<p class="alert-text" style="display: none;">恭喜您，报名成功！<br>届时请凭登记的联系电话参加会议。</p>
				<button>关闭</button>
				<a href="${ctx}/wx/conActivity/viewActivity?id=${activityId}"><button>确认</button></a>
			</div>
		</div>
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
		$(function(){
			$('form input[type="button"]').click(function(){
				//第二种方法
				var param = {};
				param.activityid = $("#activityId").val();
				param.userName = ($("input[name='userName']").val());
				param.information = ($("input[name='information']").val());
				param.note = ($("input[name='note']").val()); 
				param.mobile = $("#mobile").val();
				param.verifyServID = $("#verifyServID").val();
				param.sms_code = $("#sms_code").val();
				param.serviceId = $("#serviceId").val();
				param.openId = openId;
				
				if (!param.sms_code) {
					alert("验证码不能为空");
					return;
				}
				
				if (param.userName == "") {
					alert("姓名不能为空!");
					return;
				}
				if (!param.mobile) {
					alert("联系方式不能为空");
					return;
				}
				if (!certMobile(param.mobile)) {
					alert("联系方式格式错误!");
					return;
				}
				//原始ajax
				 /* $.ajax({
					   type: "POST",
					   url: "",
					   data: "userName="+userName+"&&activityid="+activityId+"&&mobile="+mobile+
					   		 "&&information="+information+"&&note="+note,
					   success: function(msg){
						   //alert("msg="+msg);
						   if(msg=="man"){
							   alert("抱歉，报名人数已满");
						   }else if(msg=="succeed"){
							   popUp(msg);
						   }else if(msg=="timeOut"){
							   alert("当前时间已过报名时间，不能参加");
						   }
					   }
				});  */
				
				$.ajax({
				   type: "POST",
				   url: "${ctx}/wx/conActivity/addUserActivity",
				   data: param,
				   success: function(data){
					   var result = JSON.parse(data);
						if (result.ret == "200") {
							popUp("succeed");
						} else {
							alert(result.retMsg);
						}
				   }
				});
			});
			
		});
		
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
		
	</script>
</body>
</html>
