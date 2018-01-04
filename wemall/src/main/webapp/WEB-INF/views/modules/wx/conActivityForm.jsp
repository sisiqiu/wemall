<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>活动表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		
			$("#picurlPreview").css("list-style","none");
			var editorElement = CKEDITOR.document.getById('content');
			editorElement.setHtml(CKEDITOR.instances.content.getData());
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if (CKEDITOR.instances.content.getData()==""){
                        top.$.jBox.tip('请填写活动内容','warning');
                    }else{
                    	//验证时间是否满足要求
                    		//registrationStarttime  registrationEndtime  
                    	//attendanceStarttime  attendanceEndtime  lotteryStarttime  lotteryEndtime
                    	var startJoin=$("[name=registrationStarttime]").val();
                    	var endJoin=$("[name=registrationEndtime]").val();
                    	var startQian=$("[name=attendanceStarttime]").val();
                    	var endQian=$("[name=attendanceEndtime]").val();
                    	var startChou=$("[name=lotteryStarttime]").val();
                    	var endChou=$("[name=lotteryEndtime]").val();
                    	//startActive endActive
                    	var startActive =$("[name=fromdate]").val();
                    	var endActive =$("[name=enddate]").val();
                    	console.log("开始报名时间="+startJoin)
                    	console.log("结束报名时间="+endJoin)
                    	function comptime(beginTime,endTime) {
                    	    /*  beginTime = "2009-09-21 00:00:00";
                    	    endTime = "2009-09-21 00:00:01"; */
                    	    
                    	    var beginTimes = beginTime.substring(0, 10).split('-');
                    	    var endTimes = endTime.substring(0, 10).split('-');

                    	    beginTime = beginTimes[1] + '/' + beginTimes[2] + '/' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
                    	    endTime = endTimes[1] + '/' + endTimes[2] + '/' + endTimes[0] + ' ' + endTime.substring(10, 19);

                    	    
                    	    var a = (Date.parse(endTime) - Date.parse(beginTime)) / 3600 / 1000;
                    	    return a;
                    	}
                    	//判断开始时间与结束时间比较
                    	var a=comptime(startJoin,endJoin);
	                    	if (a < 0) {
	                	        alert("报名结束时间不能比报名开始时间早!");
	                	        return;
	                	    }  else if (a == 0) {
	                	        alert("报名结束时间不能等于报名开始时间!");
	                	        return;
	                	    } 
	                    var b=comptime(startQian,endQian);
	                    	if (b < 0) {
	                	        alert("签到结束时间不能比签到开始时间早!");
	                	        return;
	                	    }  else if (b == 0) {
	                	        alert("签到结束时间不能等于签到开始时间!");
	                	        return;
	                	    } 
	                    var c=comptime(startChou,endChou);
	                    	if (c < 0) {
	                	        alert("抽奖结束时间不能比抽奖开始时间早!");
	                	        return;
	                	    }  else if (c == 0) {
	                	        alert("抽奖结束时间不能等于抽奖开始时间!");
	                	        return;
	                	    } 	 
	                    //判断活动状态时间比较
	                    var d=comptime(endJoin,startQian);
	                    	if (d < 0) {
	                	        alert("签到开始时间不能比报名结束时间早!");
	                	        return;
	                	    }  /* else if (d == 0) {
	                	        alert("签到开始时间不能等于参加结束时间!");
	                	        return;
	                	    }  */
	                    var e=comptime(endQian,startChou);
	                    	if (e < 0) {
	                	        alert("抽奖开始时间不能比签到结束时间早!");
	                	        return;
	                	    }  /* else if (e == 0) {
	                	        alert("抽奖开始时间不能等于签到结束时间");
	                	        return;
	                	    }  */
	                    //活动时间比较
	                    //startActive endActive
	                    var f=comptime(startActive,startJoin);
	                    	if (f < 0) {
	                	        alert("报名开始时间不能比活动开始时间早");
	                	        return;
	                	    }  
	                    var g=comptime(endChou,endActive);
	                    	if (g < 0) {
	                	        alert("活动结束时间不能比抽奖结束时间早!");
	                	        return;
	                	    }  
                    	//---------------------
                    	
                    	//判断资源路径是否合法，若有资源未在上传文件管理中，进行提示确认。取消则进行资源路径修改或上传文件管理，确认则提交。
                    	var resourcepath = $("#resourcepath").val();
                    	if(resourcepath != "") {
                    		//如果选择了资源文件，则验证资源文件是否在上传文件管理的数据范围内，若不在，给出提示。
                    		var url = "${ctx}/cms/cmsFileresource/confirmExistsData";
                        	$.post(url, { resourcepath: resourcepath },
                       			  function(data){
                        				var json = JSON.parse(data);
                 						$("#resourcepathPreview li font").remove();
                        				if(json.ret != "200") {
                        					var arr = json.retMsg.split(",");
                        					for(var i=0; i<arr.length; i++) {
    	                    					$("#resourcepathPreview li:eq(" + arr[i] + ")").append("<font color='red'>此项未在内容管理--文件上传管理模块中进行管理，将无法记录下载数。</font>");
                        					}
                        					confirmx("活动资源有些未在内容管理--文件上传管理模块中进行管理，将无法记录文件下载数！</br>确认要保存活动信息吗？",
                        							function() {
                        						loading('正在提交，请稍等...');
        				                        form.submit();
                        					})
                        				} else {
    	                   					loading('正在提交，请稍等...');
    				                        form.submit();
                        				}
                       			  });
                    	} else {
                    		loading('正在提交，请稍等...');
	                        form.submit();
                    	}
                    }
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			/*
			*	复写ckfinder的文件管理打开操作。使用公共文件库。
			*/
			resourcepathFinderOpen = function () {
				var url = getresourcepathFinderUrl();
				url = url + "&viewCommonFile=1";
				windowOpen(url,"文件管理",1000,700);
			};
		});
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/wx/conActivity/">活动表列表</a></li>
		<li class="active"><a href="${ctx}/wx/conActivity/form?id=${conActivity.id}">活动表<shiro:hasPermission name="wx:conActivity:edit">${not empty conActivity.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wx:conActivity:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="conActivity" action="${ctx}/wx/conActivity/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">活动标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">活动图片：</label>
			<div class="controls">
				<form:input path="picurl" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">活动图片:</label>
			<div class="controls">
                <input type="hidden" id="picurl" name="picurl" value="${conActivity.picurl}" />
				<sys:ckfinder input="picurl" type="thumb" uploadPath="/wx/activity" selectMultiple="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">举办方：</label>
			<div class="controls">
				<form:input path="organizer" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">举办地点：</label>
			<div class="controls">
				<form:input path="location" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">经度：</label>
			<div class="controls">
				<form:input path="lng" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">纬度：</label>
			<div class="controls">
				<form:input path="lat" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">活动内容：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" class="input-xxlarge "/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">活动内容:</label>
			<div class="controls">
				<form:textarea id="content" htmlEscape="true" path="content" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="content" uploadPath="/wx/activity" />
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">报名开始时间：</label>
			<div class="controls">
				<input name="registrationStarttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.registrationStarttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报名结束时间：</label>
			<div class="controls">
				<input name="registrationEndtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.registrationEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">签到开始时间：</label>
			<div class="controls">
				<input name="attendanceStarttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.attendanceStarttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">签到结束时间：</label>
			<div class="controls">
				<input name="attendanceEndtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.attendanceEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">抽奖开始时间：</label>
			<div class="controls">
				<input name="lotteryStarttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.lotteryStarttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">抽奖结束时间：</label>
			<div class="controls">
				<input name="lotteryEndtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${conActivity.lotteryEndtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动开始时间：</label>
			<div class="controls">
				<input name="fromdate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${conActivity.fromdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动结束时间：</label>
			<div class="controls">
				<input name="enddate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${conActivity.enddate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">活动资源路径：</label>
			<div class="controls">
				<input type="hidden" id="resourcepath" name="resourcepath" value="${conActivity.resourcepath}" />
				<sys:ckfinder input="resourcepath" type="commonFiles" uploadPath="" selectMultiple="true"/>
				<%-- <form:input path="resourcepath" htmlEscape="false" maxlength="200" class="input-xlarge "/> --%>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">最大人数：</label>
			<div class="controls">
				<form:input path="maxpeoplenum" htmlEscape="false" maxlength="6" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font> 最大人数必须为数字，且不能为空！</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前已报名人数：</label>
			<div class="controls">
				<form:input path="currentpeoplenum" htmlEscape="false" maxlength="6" class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font> 当前已报名人数必须为数字，且不能为空！</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:select path="status" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('con_activity_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">奖项json：</label>
			<div class="controls">
				<form:input path="prize" htmlEscape="false" maxlength="1000" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wx:conActivity:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>