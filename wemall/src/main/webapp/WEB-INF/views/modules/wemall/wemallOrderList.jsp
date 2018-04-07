<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//重置选中
			$("#btn_reset").click(function(){
				 location.replace(location.href);
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function refund(href) {
			href = href + "&refundFee=";
			promptx("填写退款金额", "退款金额", href, null);
		}
		
		function delivery(href) {
			promptxDouble("填写发货信息", "物流公司","物流单号", href, null);
		}
		
		function downloadBill() {
			var href = "${ctx}/wemall/wemallOrder/downloadBill";
			mypromptx("下载对账单", href, null);
		}
		
		function mypromptx(title, href, closed){
			top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + 
						"第三方类型：<select id='payMethod1' name='payMethod1'>" +
									"<option value=''>请选择</option>" +
									"<option value='1'>支付宝</option>" +
									"<option value='0'>微信</option>" +
								"</select></div>" +
						"<div class='form-search' style='padding:20px;text-align:center;'>" + 
						"账单类型：<select id='bill_type' name='bill_type'>" +
									"<option value=''>请选择</option>" +
								"</select></div>" +
						"<div class='form-search' style='padding:20px;text-align:center;'>" + 
						"日期：<input type='text' id='bill_date' name='bill_date' /></div>"
						, {
					title: title, submit: function (v, h, f){
			    if (f.payMethod1 == '') {
			        top.$.jBox.tip("请选择第三方类型。", 'error');
			        return false;
			    }
			    if (f.bill_type == '') {
			        top.$.jBox.tip("请选择账单类型。", 'error');
			        return false;
			    }
			    if (f.bill_date == '') {
			        top.$.jBox.tip("请输入日期。", 'error');
			        return false;
			    }
				if (typeof href == 'function') {
					href();
				}else{
					resetTip(); //loading();
					
					var url = href + 
							"?paymentType=" + encodeURIComponent(f.payMethod1) +
							"&bill_type=" + encodeURIComponent(f.bill_type) +
							"&bill_date=" + encodeURIComponent(f.bill_date);
					$.ajax({
			             type: "GET",
			             url: url,
			             data: {},
			             dataType: "json",
			             success: function(data){
			            	 if(data.ret != '0') {
			            		 top.$.jBox.tip(data.retMsg, 'error');
			            	 } else {
			            		 top.$.jBox.close(true);
			            		 location = data.retMsg;
			            	 }
			             },
			             error: function(data){
			            	 top.$.jBox.tip("下载错误", 'error');
			             }
			        });
					return false;
				}
			},closed:function(){
				if (typeof closed == 'function') {
					closed();
				}
			},loaded: function (h) {
				h.find("#payMethod1").change(function() {
					h.find("#bill_date").attr("placeholder", "");
					h.find("#bill_type").empty();
					if($(this).val() == "1") {
						h.find("#bill_date").attr("placeholder", "格式为yyyy-MM-dd或者yyyy-MM");
						h.find("#bill_type").append("<option value=''>请选择</option>" +
								"<option value='trade'>trade</option>" +
								"<option value='signcustomer'>signcustomer</option>");
					} else if($(this).val() == "0") {
						h.find("#bill_date").attr("placeholder", "格式为yyyyMMdd");
						h.find("#bill_type").append("<option value=''>请选择</option>" +
								"<option value='ALL'>ALL</option>" +
								"<option value='SUCCESS'>SUCCESS</option>" +
								"<option value='REFUND'>REFUND</option>" +
								"<option value='RECHARGE_REFUND'>RECHARGE_REFUND</option>");
					}
				});
			}});
			return false;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wemall/wemallOrder/">订单列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wemallOrder" action="${ctx}/wemall/wemallOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订单号：</label>
				<form:input path="orderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>平台订单号：</label>
				<form:input path="platformOrderNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>订单金额：</label>
				<form:input path="orderPrice" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>实付金额：</label>
				<form:input path="payment" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>支付类型：</label>
				<form:select path="paymentType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>总退款金额：</label>
				<form:input path="totalRefundFee" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>订单名称：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>付款时间：</label>
				<input name="beginPaymentDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallOrder.beginPaymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endPaymentDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${wemallOrder.endPaymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li><label>类别：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>会员卡id：</label>
				<form:input path="vipCardId" htmlEscape="false" maxlength="6" class="input-medium"/>
			</li>
			<li><label>是否申请退货：</label>
				<form:radiobuttons path="applyForReject" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</li>
			<li class="btns">
				<input id="btn_reset" class="btn btn-primary" type="button" value="重置"/>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
<!-- 				<input id="btn_downloadBill" class="btn btn-primary" type="button" value="下载对账单" onclick="downloadBill()"/>
 -->			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>订单号</th>
				<th>原始订单金额</th>
				<th>订单金额</th>
				<th>实付金额</th>
				<th>支付类型</th>
				<th>总退款金额</th>
				<th>订单名称</th>
				<th>状态</th>
				<th>付款时间</th>
				<th>类别</th>
				<th>申请退货</th>
				<shiro:hasPermission name="wemall:wemallOrder:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wemallOrder">
			<tr>
				<td><a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">
					${wemallOrder.orderNo}
				</a></td>
				<td>
					${wemallOrder.originalOrderPrice}
				</td>
				<td>
					${wemallOrder.orderPrice}
				</td>
				<td>
					${wemallOrder.payment}
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.paymentType, 'payment_type', '')}
				</td>
				<td>
					${wemallOrder.totalRefundFee}
				</td>
				<td>
					${wemallOrder.title}
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.status, 'order_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${wemallOrder.paymentDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.type, 'order_type', '')}
				</td>
				<td>
					${fns:getDictLabel(wemallOrder.applyForReject, 'yes_no', '')}
				</td>
				<%-- <shiro:hasPermission name="wemall:wemallOrder:edit"><td>
    				<a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">修改</a>
					<a href="${ctx}/wemall/wemallOrder/delete?orderNo=${wemallOrder.orderNo}" onclick="return confirmx('确认要删除该订单吗？', this.href)">删除</a>
				</td></shiro:hasPermission> --%>
				<shiro:hasPermission name="wemall:wemallOrder:edit"><td>
    				<a href="${ctx}/wemall/wemallOrder/form?orderNo=${wemallOrder.orderNo}">查看详情</a>
					<a href="${ctx}/wemall/wemallOrder/delete?orderNo=${wemallOrder.orderNo}" onclick="return confirmx('确认要删除该订单吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				<shiro:hasPermission name="wemall:wemallOrder:refund"><td>
					<a onclick="return refund('${ctx}/wemall/wemallOrder/refund?orderNo=${wemallOrder.orderNo}')" >部分退款</a>
				</td></shiro:hasPermission>
				<shiro:hasPermission name="wemall:wemallOrder:refund"><td>
					<a href="${ctx}/wemall/wemallOrder/refund?orderNo=${wemallOrder.orderNo}&refundFee=${wemallOrder.payment}" onclick="return refundTotal('')" >全部退款</a>
				</td></shiro:hasPermission>
				<c:if test="${wemallOrder.status == 2}">
					<td>
						<a onclick="return delivery('${ctx}/wemall/wemallOrder/alreadyShipped?orderNo=${wemallOrder.orderNo}')" >发货</a>
					</td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>