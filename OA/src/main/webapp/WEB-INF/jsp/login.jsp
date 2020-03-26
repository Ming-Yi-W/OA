<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<!DOCTYPE html> 
<html lang="en"> 
<head> 
    <meta charset="utf-8"> 
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
    <title>智能办公</title> 
    <link href="${ctx}/res/css/base.css" rel="stylesheet">
    <link href="${ctx}/res/css/login.css" rel="stylesheet">
    <link href="${ctx}/res/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="${ctx }/res/resources/bootstrap/js/bootstrap.min.js"></script>
     <script type="text/javascript" src="${ctx}/res/resources/easyUI/jquery.easyui.min.js"></script>
     <script type="text/javascript" src="${ctx}/res/resources/easyUI/easyui-lang-zh_CN.js"></script>
     <link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
	 <script type="text/javascript">
	 $(document).ready(function(){
		 
		 if("${message}"){
			 alert("${message}");
		 }
		 
		 if(window.location!=top.window.location){
			 top.window.location=window.location;
		 }
		 
		 
		 //为验证码绑定点击事件
		 $("#vimg").click(function(){
			 //$("vimg").prop();  prop和attr用法相同
			 $("#vimg").attr("src","${ctx}/createCode?data="+Math.random());
			 
		 });
		 //为登录按钮绑定点击事件
		 $("#login_id").bind("click",function(){
			 //获取账号
			 var userId=$("#userId").val();
			 //获取密码
			var passWord=$("#passWord").val();			 
			 //获取验证码
			 var vcode=$("#vcode").val();
			 //通过正则表达式进行信息的校验，如果校验
			 //通过则发送ajax异步请求进行登录，否则提示错误
			 if(!/^[0-9a-zA-Z_]{5,16}$/.test(userId)){
				 $.messager.show({
						title:'提示信息',
						msg:'您输入的用户名不合法!',
						timeout:3000,
						showType:'slide'
					});
				 $("#userId").focus();
			 }else if(!/^[0-9a-zA-Z_]{5,16}$/.test(passWord)){
				 $.messager.show({
						title:'提示信息',
						msg:'您输入的密码不合法!',
						timeout:3000,
						showType:'slide'
					});
				 $("#passWord").focus();
			 }else if(!/^[0-9a-zA-Z]{4}$/.test(vcode)){
				 $.messager.show({
						title:'提示信息',
						msg:'您输入的验证码错误!',
						timeout:3000,
						showType:'slide'
					});
				 $("#vcode").focus();
			 }else{
				 //通过序列化拿到输入数据
				 
				 var data=$("#loginForm").serialize();
				//前台验证通过，发送ajax请求
				
				$.post(
					"${ctx}/identity/user/ajaxLogin",
					data,
					function(msg){					
						if(msg){//""是false，在后台正确传的字符串是""
							 $.messager.show({
									title:'提示信息',
									msg:msg,
									timeout:3000,
									showType:'slide'
								});
						//验证码刷新
						$("#vimg").trigger("click");
						}else{
							window.location="${ctx}/main";
						}
					},
					"text"
				);
				
				
		
		    	
			 }
			 
			 
		 });
		 
		 $(window).keydown(function(event){
			 if(event.keyCode==13){
				 $("#login_id").trigger("click");    //trigger触发器，触发#login_id的click事件
			 }
			 
		 });
		 
		 
		 
		 
	 });
	 
	 </script>
</head> 
<body>
	<div class="login-hd">
		<div class="left-bg"></div>
		<div class="right-bg"></div>
		<div class="hd-inner">
			<span class="logo"></span>
			<span class="split"></span>
			<span class="sys-name">智能办公平台</span>
		</div>
	</div>
	<div class="login-bd">
		<div class="bd-inner">
			<div class="inner-wrap">
				<div class="lg-zone">
					<div class="lg-box">
						<div class="panel-heading" style="background-color: #11a9e2;">
							<h3 class="panel-title" style="color: #FFFFFF;font-style: italic;">用户登陆</h3>
						</div>
						<form id="loginForm">
						   <div class="form-horizontal" style="padding-top: 20px;padding-bottom: 30px; padding-left: 20px;">
								
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<input class="form-control" id="userId" name="userId" type="text" placeholder="账号/邮箱">
									</div>
								</div>
				
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<input  class="form-control"  id="passWord" name="passWord" type="password" placeholder="请输入密码">
									</div>
								</div>
				
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<div class="input-group">
										<input class="form-control " id="vcode" name="vcode" maxlength="4" type="text" placeholder="验证码">
										<span class="input-group-addon" id="basic-addon2"><img class="check-code" style="cursor:pointer" id="vimg"  alt="" src="${ctx}/createCode"></span>
										</div>
									</div>
								</div>
				
						</div>
							<div class="tips clearfix">
											<label><input type="checkbox" name="rem" value="1">记住一周</label>
											<a href="javascript:;" class="register">忘记密码？</a>
										</div>
							<div class="enter">
								<a href="javascript:;" id="login_id" class="purchaser" >登录</a>
								<a href="javascript:;" class="supplier" onClick="javascript:window.location='main.html'">重 置</a>
							</div>
						</form>
					</div>
				</div>
				<div class="lg-poster"></div>
			</div>
	</div>
	</div>
	<div class="login-ft">
		<div class="ft-inner">
			<div class="about-us">
				<a href="javascript:;">关于我们</a>
				<a href="javascript:;">法律声明</a>
				<a href="javascript:;">服务条款</a>
				<a href="javascript:;">联系方式</a>
			</div>
			<div class="address">
			地址：广州市天河区
			&nbsp;邮编：510000&nbsp;&nbsp;
			分享知识，传递希望&nbsp;版权所有</div>
			<div class="other-info">
			建议使用火狐、谷歌浏览器，不建议使用IE浏览器！</div>
		</div>
	</div>
</body> 
</html>
