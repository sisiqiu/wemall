/*!
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * 
 * 通用公共方法
 * @author ThinkGem
 * @version 2014-4-29
 */
$(document).ready(function() {
	try{
		// 链接去掉虚框
		$("a").bind("focus",function() {
			if(this.blur) {this.blur()};
		});
		//所有下拉框使用select2
		$("select").select2();
	}catch(e){
		// blank
	}
});

// 引入js和css文件
function include(id, path, file){
	if (document.getElementById(id)==null){
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++){
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
            var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
            document.write("<" + tag + (i==0?" id="+id:"") + attr + link + "></" + tag + ">");
        }
	}
}

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
	    url = window.location.search;
    }else{	
    	url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}

//获取字典标签
function getDictLabel(data, value, defaultValue){
	for (var i=0; i<data.length; i++){
		var row = data[i];
		if (row.value == value){
			return row.label;
		}
	}
	return defaultValue;
}

// 打开一个窗体
function windowOpen(url, name, width, height){
	var top=parseInt((window.screen.height-height)/2,10),left=parseInt((window.screen.width-width)/2,10),
		options="location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes,"+
		"resizable=yes,scrollbars=yes,"+"width="+width+",height="+height+",top="+top+",left="+left;
	window.open(url ,name , options);
}

// 恢复提示框显示
function resetTip(){
	top.$.jBox.tip.mess = null;
}

// 关闭提示框
function closeTip(){
	top.$.jBox.closeTip();
}

//显示提示框
function showTip(mess, type, timeout, lazytime){
	resetTip();
	setTimeout(function(){
		top.$.jBox.tip(mess, (type == undefined || type == '' ? 'info' : type), {opacity:0, 
			timeout:  timeout == undefined ? 2000 : timeout});
	}, lazytime == undefined ? 500 : lazytime);
}

// 显示加载框
function loading(mess){
	if (mess == undefined || mess == ""){
		mess = "正在提交，请稍等...";
	}
	resetTip();
	top.$.jBox.tip(mess,'loading',{opacity:0});
}

// 关闭提示框
function closeLoading(){
	// 恢复提示框显示
	resetTip();
	// 关闭提示框
	closeTip();
}

// 警告对话框
function alertx(mess, closed){
	top.$.jBox.info(mess, '提示', {closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}

// 确认对话框
function confirmx(mess, href, closed){
	top.$.jBox.confirm(mess,'系统提示',function(v,h,f){
		if(v=='ok'){
			if (typeof href == 'function') {
				href();
			}else{
				resetTip(); //loading();
				location = href;
			}
		}
	},{buttonsFocus:1, closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

// 提示输入对话框
function promptx(title, lable, href, closed){
	top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + lable + "：<input type='text' id='txt' name='txt'/></div>", {
			title: title, submit: function (v, h, f){
	    if (f.txt == '') {
	        top.$.jBox.tip("请输入" + lable + "。", 'error');
	        return false;
	    }
		if (typeof href == 'function') {
			href();
		}else{
			resetTip(); //loading();
			location = href + encodeURIComponent(f.txt);
		}
	},closed:function(){
		if (typeof closed == 'function') {
			closed();
		}
	}});
	return false;
}

// 添加TAB页面
function addTabPage(title, url, closeable, $this, refresh){
	top.$.fn.jerichoTab.addTab({
        tabFirer: $this,
        title: title,
        closeable: closeable == undefined,
        data: {
            dataType: 'iframe',
            dataLink: url
        }
    }).loadData(refresh != undefined);
}

// cookie操作
function cookie(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
}

// 数值前补零
function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

// 转换为日期
function strToDate(date){
	return new Date(date.replace(/-/g,"/"));
}

// 日期加减
function addDate(date, dadd){  
	date = date.valueOf();
	date = date + dadd * 24 * 60 * 60 * 1000;
	return new Date(date);  
}

//截取字符串，区别汉字和英文
function abbr(name, maxLength){  
 if(!maxLength){  
     maxLength = 20;  
 }  
 if(name==null||name.length<1){  
     return "";  
 }  
 var w = 0;//字符串长度，一个汉字长度为2   
 var s = 0;//汉字个数   
 var p = false;//判断字符串当前循环的前一个字符是否为汉字   
 var b = false;//判断字符串当前循环的字符是否为汉字   
 var nameSub;  
 for (var i=0; i<name.length; i++) {  
    if(i>1 && b==false){  
         p = false;  
    }  
    if(i>1 && b==true){  
         p = true;  
    }  
    var c = name.charCodeAt(i);  
    //单字节加1   
    if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {  
         w++;  
         b = false;  
    }else {  
         w+=2;  
         s++;  
         b = true;  
    }  
    if(w>maxLength && i<=name.length-1){  
         if(b==true && p==true){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         if(b==false && p==false){  
             nameSub = name.substring(0,i-3)+"...";  
         }  
         if(b==true && p==false){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         if(p==true){  
             nameSub = name.substring(0,i-2)+"...";  
         }  
         break;  
    }  
 }  
 if(w<=maxLength){  
     return name;  
 }  
 return nameSub;  
}

Array.prototype.removeByValue = function(val) {
  for(var i=0; i<this.length; i++) {
    if(this[i] == val) {
      this.splice(i, 1);
      break;
    }
  }
}

var jsUrlHelper = {
    getUrlParam : function(url, ref) {
        var str = "";

        // 如果不包括此参数
        if (url.indexOf(ref) == -1)
            return "";

        str = url.substr(url.indexOf('?') + 1);

        arr = str.split('&');
        for (i in arr) {
            var paired = arr[i].split('=');

            if (paired[0] == ref) {
                return paired[1];
            }
        }

        return "";
    },
    putUrlParam : function(url, ref, value) {

        // 如果没有参数
        if (url.indexOf('?') == -1)
            return url + "?" + ref + "=" + value;

        // 如果不包括此参数
        if (url.indexOf(ref) == -1)
            return url + "&" + ref + "=" + value;

        var arr_url = url.split('?');

        var base = arr_url[0];

        var arr_param = arr_url[1].split('&');

        for (i = 0; i < arr_param.length; i++) {

            var paired = arr_param[i].split('=');

            if (paired[0] == ref) {
                paired[1] = value;
                arr_param[i] = paired.join('=');
                break;
            }
        }

        return base + "?" + arr_param.join('&');
    },
    delUrlParam : function(url, ref) {

        // 如果不包括此参数
        if (url.indexOf(ref) == -1)
            return url;

        var arr_url = url.split('?');

        var base = arr_url[0];

        var arr_param = arr_url[1].split('&');

        var index = -1;

        for (i = 0; i < arr_param.length; i++) {

            var paired = arr_param[i].split('=');

            if (paired[0] == ref) {

                index = i;
                break;
            }
        }

        if (index == -1) {
            return url;
        } else {
            arr_param.splice(index, 1);
            return base + "?" + arr_param.join('&');
        }
    }
};

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
}

function initCheckBox(actIds) {
	var actIdArr = actIds.split(",");
	if(actIdArr && actIdArr.length > 0) {
		for(var i=0; i<actIdArr.length; i++) {
			var obj = $(".allChild[value='" + actIdArr[i] + "']");
			itemArr.push($(obj).val());
			checkedTr[$(obj).val()] = $(obj).parent().parent();
		}
	}
}