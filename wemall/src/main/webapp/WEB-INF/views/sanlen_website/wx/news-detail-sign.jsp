<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="${ ctxStatic}/sanlen_website/js/wxjs.js"></script>
	<title>签到页</title>
	<link rel="stylesheet" href="${ ctxStatic}/sanlen_website/css/wx.css">
</head>
<body>
	<div class="container news-detail">
		<h3>企业信息安全与超融合交流会，邀请您的参与！</h3>
		<main>
			<section>
				<p>主办方：<span>山丽信息&nbsp;群立集团</span></p>
				<p>开始时间：<span>2017年8月31日&nbsp;13:30</span></p>
				<p>举办地点：<span>西子宾馆.苏州</span></p>
			</section>

			<section class="bg-logo" style="margin: 0.13rem auto 0.26rem auto;">
				<img src="${ ctxStatic}/sanlen_website/img/wx/8.png" alt="">
			</section>

			<section>
				<p>会议内容：<br>
					<span>
						1.企业信息安全风险评估和安全解决方案-助力企业信息安全的防护。<br>
						2.企业超融合解决方案-助力企业IT架构的转型。		
					</span>
				</p>
				<p style="margin-top: 0.5rem;">会议流程：<br>
					<span>
						13:15-13:45	&nbsp; 签到<br>
						14:00-14:25 &nbsp; 山丽信息安全大数据平台智慧城市介绍<br>
						14:35-15:05 &nbsp; 企业信息安全风险评估与解决方案介绍<br>
						15:05-15:20 &nbsp; 现场答疑互动/茶歇<br>
					</span>
				</p>
			</section>

			<section>
				<p style="margin-top: 0.5rem;">会议地址：</p>
				<div class="bg-logo" style="margin:0.5rem auto 0.46rem auto;">
					<img src="${ ctxStatic}/sanlen_website/img/wx/9.png" alt="">		
				</div>
			</section>
			<a href="#"><input class="button" type="button" value="立即签到"></a>

			<!-- 弹出消息框 -->
			<div class="pop-up" style="display: none;">
				<div class="bg"></div>
				<div class="message">
					<p>提示</p>
					<hr>
					<p class="alert-text" style="display: none;">恭喜您,您已签到成功！<br>
					稍后请凭手机号领奖。&nbsp;
					</p>
					<button>关闭</button>
					<a href="${ ctx}/publicToPage?pageUrl=sanlen_website/wx/news-detail-sign"><button>确认</button></a>
				</div>
			</div>
		</main>
	</div>
	<script>
		$(function(){
			$('input:last').click(function(e){

			// 签到成功后可执行此函数，弹出消息框
			popUp('succeed');
			})
		});
	</script>
</body>
</html>
