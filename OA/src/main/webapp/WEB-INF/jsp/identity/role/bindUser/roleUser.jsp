<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
 <%@ taglib prefix="fk" uri="/longIt/oa"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>OA办公管理系统-用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
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
	<!-- 引入分页样式 -->
<link rel="stylesheet" href="${ctx}/res/css/pager.css">
<script type="text/javascript"
	src="${ctx}/res/js/operaPageCss.js"></script>
<script type="text/javascript">

 
      $(function(){
    	  
    	  if("${message}"){
				$.messager.show({
					title:'提示信息',
					msg:"<font color='red'>${message}</font>",
					showType:'show'
				});
			}
    	  
    	  //为解绑按钮绑定事件
    	  $("#unBindUser").click(function(){
    		//获取选中的checkbox
       	   var boxes = $("input[name='box']:checked");
       	   if(boxes.length == 0){
       		   $.messager.alert('提示信息',"请选择需要解绑的用户信息！",'warning');
       	   }else{
      			        var arr = new Array();
      			    	$.each(boxes,function(){
      			    		arr.push(this.value);
      			    	})
      			    	//发送请求删除用户信息      
      			    	window.location = "/identity/role/unBindUser?userIds="+arr+"&id=${role.id}&pageIndex=${pageModel.pageIndex}";
       	         }
    		  
    	  });
    	  
    	  //为绑定用户按钮绑定点击事件
    	  $("#bindUser").click(function(){
    		  $("#divDialog").dialog({
      		    title : "绑定用户", // 标题
      			cls : "easyui-dialog", // class
      			width : 780, // 宽度
      			height : 455, // 高度
      			maximizable : true, // 最大化
      			minimizable : false, // 最小化
      			collapsible : true, // 可伸缩
      			modal : true, // 模态窗口
      			onClose : function(){ // 关闭窗口
      				window.location = "/identity/role/selectRoleUser?id=${role.id}";
      			} 
      	   })
      	   
      	   //通过iframe将添加角色信息的页面引入
      	   $("#iframe").prop("src","/identity/role/showAddUnBindUserByRoleId?id=${role.id}");
    		  
    	  })
      })
</script>
</head>
<body style="overflow: hidden; width: 98%; height: 100%;" >
		<!-- 工具按钮区 -->
		
 		<div class="panel panel-primary" style="padding-left: 5px;">
 			<div style="padding-top: 4px;padding-bottom: 4px;">
				<a  id="back" class="btn btn-primary" onclick="history.back()"><span class="glyphicon glyphicon-hand-left"></span>&nbsp;返回</a>
				<a  id="bindUser" class="btn btn-success"><span class="glyphicon glyphicon-copy"></span>&nbsp;绑定用户</a>
				<a  id="unBindUser" class="btn btn-danger"><span class="glyphicon glyphicon-paste"></span>&nbsp;解绑用户</a>
			    
			 
			</div>
 			<div class="panel-heading" style="background-color: #11a9e2;">
				<h3 class="panel-title"><label style="color: white;font-size: 15px;"><span class="glyphicon glyphicon-user"></span>&nbsp;${role.name }</label></h3>
			</div>
			<div class="panel-body" >
				<table class="table table-bordered">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input id="checkAll"
								type="checkbox" /></th>
							<th style="text-align: center;">账户</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">性别</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">邮箱</th>
							<th style="text-align: center;">审核人</th>
						</tr>
					</thead>
					<c:forEach items="${requestScope.users}" var="user"
						varStatus="stat">
						<tr id="dataTr_${stat.index}" align="center">
							<td><input type="checkbox" name="box" id="box_${stat.index}"
								value="${user.userId}" /></td>
							<td>${user.userId}</td>
							<td>${user.name}</td>
							<td>${user.sex == 1 ? '男' : '女' }</td>
							<td>${ user.dept.name}</td>
							<td>${ user.job.name}</td>
							<td>${user.phone}</td>
							<td>${user.email}</td>
							<td>${user.checker.name}</td>
						</tr>
					</c:forEach>
				</table>
			
			</div>
             <fk:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.recordCount}" submitUrl="${ctx}/identity/role/selectRoleUser?pageIndex={0}"></fk:pager>
		</div>
		
		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
		</div>
	
</body>
</html>