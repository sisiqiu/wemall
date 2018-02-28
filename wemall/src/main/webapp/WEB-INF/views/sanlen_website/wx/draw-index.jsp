<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>姓名电话随机抽奖</title>
	<link rel="stylesheet" type="text/css" href="${ ctxStatic}/sanlen_website/css/draw.css">
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/vue.js"></script>
	<style>
		select:checked
	</style>
</head>
<body class="bg">
	<div class="box">
		<div class="jz">
			<header>抽奖活动</header>
			<select  id="chooseActivity">
				<option  v-for="one in data" v-bind:value="one.id" >{{one.title}}</option>
			</select>
			<select name="choose" id="choose">
				<option value="五等奖" selected="selected">五等奖</option>
				<option value="四等奖">四等奖</option>
				<option value="三等奖">三等奖</option>
				<option value="二等奖">二等奖</option>
				<option value="一等奖">一等奖</option>
			</select>
			<!-- <span class="name">姓名</span> -->
			<span class="phone">电话</span>
			<div class="start" id="btntxt" onclick="start()">开始</div>
		</div>
		<div class="zjmd">
			<p class="p1">中奖者名单</p>
			<div class="list">
			</div>
		</div>
	</div>
	<script src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/common.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/cj.js"></script>
	<script>
		$(function(){
			var height=window.outerHeight+'px';
			var backgroundSize = '100%' + ' ' + height
			$('body').css({
			    "background-size": backgroundSize 
			});
			/*setTimeout(function(){
				$(".select_box").click(function(){
					// $('.select_option').css('display','none');
					$(this).find('.select_option').css('display','none');
				})
			},1000)*/
		})
	</script>
</body>
</html>