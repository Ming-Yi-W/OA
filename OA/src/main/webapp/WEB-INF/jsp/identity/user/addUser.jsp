<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>办公管理系统-添加用户</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta name="Keywords" content="keyword1,keyword2,keyword3" />
<meta name="Description" content="网页信息的描述" />
<meta name="Author" content="longIt.org" />
<meta name="Copyright" content="All Rights Reserved." />
<link href="${ctx}/longIt.ico" rel="shortcut icon" type="image/x-icon" />
<link rel="stylesheet"
	href="${ctx }/res/resources/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript"
	src="${ctx }/res/resources/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript"
	src="${ctx }/res/resources/jquery/jquery-migrate-1.2.1.min.js"></script>
<!-- 导入bootStrap的库 -->
<script type="text/javascript"
	src="${ctx}/res/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${ctx}/res/resources/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/res/resources/easyUI/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
<script type="text/javascript">
	$(function() {
		if("${message}"){
			parent.$.messager.show({
				title:'提示信息',
				msg:"<font color='red'>${message}</font>",
				showType:'show'
			});
		}
		
		
		 //通过异步请求加载部门以及职位信息
        $.post("${ctx}/identity/user/getDeptandJobJson",function(result,status){
     	   
     	   if(status == "success"){
     		   //获取部门信息
     		   var depts = result.depts;
     		   $.each(depts,function(){
     			   //填充部门信息
     			   $("#deptSelect").append("<option value='"+this.id+"'>"+this.name+"</option>");
     			  // $("<option>").val(this.id).text(this.name).appendTo("#deptSelect");
     		   })
     		   
     		    //获取职位信息
     		    var jobs = result.jobs;
     		   $.each(jobs,function(){
     			   //填充职位信息
     			  $("#jobSelect").append("<option value='"+this.code+"'>"+this.name+"</option>");
     			 // $("<option>").val(this.code).text(this.name).appendTo("#jobSelect");
     		   })
     		   
     	   }else{
     		   $.messager.alert('提示信息',"网络异常！",'warning');
     	   }
     	   
        },"json")
        

		/**  添加用户，提交表单函数 */
		$("#btn_submit").click(function() {
			// 对表单中所有字段做校验
			var userId = $("#userId");
			var name = $("#name");
			var passWord = $("#password");
			var repwd = $("#repwd");
			var email = $("#email");
			var tel = $("#tel");
			var phone = $("#phone");
			var qqNum = $("#qqNum");
			var answer = $("#answer");
			var msg = "";
			if ($.trim(userId.val()) == "") {
				msg += "用户登录名不能为空!";
				userId.focus();
			} else if (!/^\w{5,20}$/.test(userId.val())) {
				msg += "用户登录名长度必须在5-20之间!";
				userId.focus();
			}else if ($.trim(name.val()) == "") {
				msg += "姓名不能为空!";
				name.focus();
			} else if ($.trim(passWord.val()) == "") {
				msg += "密码不能为空!";
				passWord.focus();
			} else if (!/^\w{6,20}$/.test(passWord.val())) {
				msg += "密码长度必须为6-20之间!";
				passWord.focus();
			} else if (repwd.val() != passWord.val()) {
				msg += "两次输入的密码不一致!";
				repwd.focus();
			} else if ($.trim(email.val()) == "") {
				msg += "邮箱不能为空!";
				email.focus();
			} else if (!/^\w+@\w{2,}\.\w{2,}$/.test(email.val())) {
				msg += "邮箱格式不正确!";
				email.focus();
			} else if ($.trim(tel.val()) == "") {
				msg += "电话号码不能为空!";
				tel.focus();
			// 020-38216920 02034432323  0755
			} else if (!/^0\d{2,3}-?\d{7,8}$/.test(tel.val())) {
				msg += "电话号码格式不正确!";
				tel.focus();
			} else if ($.trim(phone.val()) == "") {
				msg += "手机号码不能为空!";
				phone.focus();
			} else if (!/^1[3|4|5|8]\d{9}$/.test(phone.val())) {
				msg += "手机号码格式不正确!";
				phone.focus();
			} else if ($.trim(qqNum.val()) == "") {
				msg += "QQ号码不能为空!";
				qqNum.focus();
			} else if (!/^\d{5,12}$/.test(qqNum.val())) {
				msg += "QQ号码长度必须在5-12之间!";
				qqNum.focus();
			} else if ($.trim(answer.val()) == "") {
				msg += "密保问题不能为空!";
				answer.focus();
			}
			if(msg){
				$.messager.alert("提示信息",msg,"warning");
			}else{
				$("#addUserForm").submit();
			}

		});
	});
	
	
	function blurFn(obj){
		if($.trim(obj.value)){
			//如果用户有输入账号信息，则对该信息进行校验，如果账号存在则给用户信息
			 $.post("${ctx}/identity/user/validUserId","userId="+obj.value,function(result,status){
        	   
        	   if(status == "success"){
        		   if(result){
        			   $.messager.alert('提示信息',result,'warning');
        			   //清空用户输入的信息
        			   obj.value = "";
        		   }
        		   
        	   }else{
        		   $.messager.alert('提示信息',"网络异常！",'warning');
        	   }
        	   
           },"text")
		}
	}
</script>
</head>
<body style="background: #F5FAFA;">
	<center>
		<form id="addUserForm" action="/identity/user/addUser"
			method="post">
			<table class="table-condensed">
				<tbody>
					<tr>
						<td><label>登陆名称:<label></td>
						<td><input type="text" id="userId" name="userId"
							value="${user.name}" class="form-control" placeholder="请输入您的登录名" onblur="blurFn(this)"></td>
						<td><label>用户姓名:<label></td>
						<td><input type="text" id="name" name="name"
							value="${user.name}" class="form-control" placeholder="请输入您的电子邮件"></td>
					</tr>
					<tr>
						<td><label>密码:<label></td>
						<td><input type="text" id="password" name="passWord"
							value="${user.name}" class="form-control" placeholder="请输入您的密码"></td>
						<td><label>确认密码:<label></td>
						<td><input type="text" id="repwd" name="repwd"
							value="${user.name}" class="form-control" placeholder="请输入您的确认密码"></td>
					</tr>
					<tr>
						<td><label>性别:<label></td>
						<td><select name="sex" id="sex" class="btn btn-default">
								<option value="1">男</option>
								<option value="2">女</option>
						</select></td>
						<td><label>部门:<label></td>
						<td><select id="deptSelect" name="dept.id"
							class="btn btn-default" id="deptSelect" name="dept.id"></select>
						</td>
					</tr>
					<tr>
						<td><label>职位:<label></td>
						<td><select id="jobSelect" name="job.code"
							class="btn btn-default" id="jobSelect" name="job.code"></select>
						</td>
						<td><label>邮箱:<label></td>
						<td><input id="email" name="email" type="text"
							value="${user.email}" class="form-control"
							placeholder="请输入您的电子邮件"></td>
					</tr>

					<tr>
						<td><label>电话:<label></td>
						<td><input id="tel" name="tel" type="text"
							value="${user.tel}" class="form-control" placeholder="请输入您的电话">
						</td>
						<td><label>手机:<label></td>
						<td><input id="phone" name="phone" type="text"
							value="${user.phone}" class="form-control" placeholder="请输入您的手机">

						</td>
					</tr>

					<tr>
						<td><label>问题:<label></td>
						<td><select name="question" class="btn btn-default"
							id="question">
								<option value="1">您的生日</option>
								<option value="2">您的出生地</option>
								<option value="3">您母亲的名字</option>
						</select></td>
						<td><label>答案:<label></td>
						<td><input id="answer" name="answer" value="${user.answer}"
							type="text" class="form-control" placeholder="请输入您的答案"></td>
					</tr>
					<tr>
						<td><label>qq号码:<label></td>
						<td><input id="qqNum" name="qqNum" value="${user.qqNum}"
							type="text" class="form-control" placeholder="请输入您的qq号码">
						</td>
					</tr>

				</tbody>
			</table>
			<div align="center" style="margin-top: 20px;">
				<a id="btn_submit" class="btn btn-info"><span
					class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
				<button type="reset" class="btn btn-danger">
					<span class="glyphicon glyphicon-remove"></span>&nbsp;重置
				</button>
			</div>
		</form>

	</center>
</body>
</html>
