<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>限时返现活动管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var itemArr = [];
	var currentArr = [];
	var checkedTr = {};
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
		});
		function page(n,s){
			var url = window.location.href;
			var id = jsUrlHelper.getUrlParam(url, "id");
			$.ajax({
	            type: "POST",
	            url: "${ctx}/wemall/wemallCashbackDiscount/pageData",
	            data: {id: id, pageNo:n?n:$(".serverPageNo").val(), pageSize:s?s:$(".serverPageSize").val(),name:$("#itemName").val()},
	            dataType: "json",
	            success: function(data){
					$(".pagination").html(data.page);
					$("#firstTable tbody").empty();
					$.each(data.list, function(index, wemallItem) {
						var tbodyTrHtml = "<tr>" +
							"<td class=\"checkboxTd\"><input name=\"itemId\" class=\"allChild\" onchange=\"allChildChange(this)\" type=\"checkbox\" value=\"" + wemallItem.id + "\"></td>" +
							"<td><img src=\"" + wemallItem.photo + "\" width=\"40px\" height=\"40px\">" + wemallItem.name + "</td>" +
							"<td>" + wemallItem.sortId + "</td>" +
							"<td>" + wemallItem.originalPrice + "</td>" +
							"<td>" + wemallItem.currentPrice + "</td>" +
							"<td>" + wemallItem.storage + "</td>" +
						"</tr>";
						$("#firstTable tbody").append(tbodyTrHtml);
					});
					fillCheckBox();
	            }
			});
        }
		
		function returnList(){
			document.getElementById("firstDis").click();
		}
		
		function allChange() {
			if ($(".all").attr("checked")=="checked") {
				$(".allChild").attr("checked","checked");
				$.each($(".allChild"), function(index, item) {
					itemArr.push($(item).val());
					checkedTr[$(item).val()] = $(item).parent().parent();
				});
				//$("td").remove(".checkboxTd");
			} else {
				$(".allChild").removeAttr("checked");
				$.each($(".allChild"), function(index, item) {
					itemArr.removeByValue($(item).val());
					delete checkedTr[$(item).val()];
				});
			}
			console.log(checkedTr);
		}
		
		function allChildChange(obj) {
			if ($(obj).attr("checked")=="checked") {
				itemArr.push($(obj).val());
				checkedTr[$(obj).val()] = $(obj).parent().parent();
			} else {
				itemArr.removeByValue($(obj).val());
				delete checkedTr[$(obj).val()];
			}
			console.log(checkedTr);
		}
		
		function showDiv(num){
			$(".discountTab li").removeClass("active");
			$(".discountTab li").eq(num-1).addClass("active");
			$(".tabsDiv").hide();
			$(".tabsDiv").eq(num-1).show();
			if(num==2){
				$("#secondTable tbody").empty();
				for(var k in checkedTr){
					var useObj = checkedTr[k].clone();
					var txt3=document.createElement("td");  // 以 DOM 创建新元素
					txt3.innerHTML="<input type=\"button\" class=\"btn btn-primary\" value=\"取 消\" onclick=\"cancle('"+k+"',this)\">";
					useObj.append(txt3);
					$("#secondTable tbody").append(useObj);
				}
				$("#secondTable td").remove(".checkboxTd");
			} else {
				fillCheckBox();
			}
		}
		function cancle(k,obj){
			itemArr.removeByValue(k);
			delete checkedTr[k];
			$(obj).parent().parent().remove();
		}
		
		function fillCheckBox() {
			$("#firstTable .allChild").removeAttr("checked");
			$.each($("#firstTable .allChild"), function(index, item) {
				if(itemArr.indexOf($(item).val()) != -1) {
					$(item).attr("checked","checked");
				}
			});
			console.log(itemArr);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a id="firstDis" href="${ctx}/wemall/wemallCashbackDiscount/">限时返现活动列表</a></li>
		<li class="active"><a href="${ctx}/wemall/wemallCashbackDiscount/form?id=${wemallCashbackDiscount.id}">限时返现活动<shiro:hasPermission name="wemall:wemallCashbackDiscount:edit">${not empty wemallCashbackDiscount.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="wemall:wemallCashbackDiscount:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="wemallCashbackDiscount" action="${ctx}/wemall/wemallCashbackDiscount/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">活动名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序：</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="6" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="startDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wemallCashbackDiscount.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wemallCashbackDiscount.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标签名：</label>
			<div class="controls">
				<form:input path="label" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型：</label>
			<div class="controls">
				<form:radiobuttons path="type" items="${fns:getDictList('cashback_type')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">返现比例：</label>
			<div class="controls">
				<form:input path="cashback" htmlEscape="false" maxlength="20" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">返现订单数：</label>
			<div class="controls">
				<form:input path="limit" htmlEscape="false" maxlength="9" class="input-xlarge  digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wemall:wemallCashbackDiscount:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="returnList()"/>
		</div>
	</form:form>
	<ul class="nav nav-tabs discountTab">
		<li class="active"><a onclick="showDiv(1)">第一步：选择商品</a></li>
		<li><a onclick="showDiv(2)">第二步：确认商品</a></li>
	</ul>
	<div id="firstDiv" class="tabsDiv">
	<div>	
		<form:form id="firstQueryForm" modelAttribute="wemallItem" class="breadcrumb form-search">			
			<li><label>商品名称：</label>
				<input id="itemName" name="name" class="input-medium" type="text" value="" maxlength="100">
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" onclick="page()" type="button" value="查询"/>
				<input id="btn_reset" class="btn btn-primary" onclick="document.getElementById('firstQueryForm').reset();page()" type="button" value="重置"/>
			</li>
			<li class="clearfix"></li>
		</form:form>
	</div>
	<table id="firstTable" class="table table-striped table-bordered table-condensed">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<thead>
			<tr>
				<th><input type="checkbox" class="all" onchange="allChange()"></th>
				<th>商品名称</th>
				<th>商品类别id</th>
				<th>商品原价</th>
				<th>商品现价</th>
				<th>商品库存</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallItem">
			<tr>
				<td class="checkboxTd">
					<input name="itemId" class="allChild" onchange="allChildChange(this)" type="checkbox" value="${wemallItem.id}">
				</td>
				<td>
					<img  src="${wemallItem.photo}" width="40px" height="40px">${wemallItem.name}
				</td>
				<td>
					${wemallItem.sortId}
				</td>
				<td>
					${wemallItem.originalPrice}
				</td>
				<td>
					${wemallItem.currentPrice}
				</td>
				<td>
					${wemallItem.storage}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</div>
	<div id = "secondDiv" style="display: none" class="tabsDiv">
	<table id="secondTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>商品类别id</th>
				<th>商品原价</th>
				<th>商品现价</th>
				<th>商品库存</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
	</table>
	</div>
</body>
</html>