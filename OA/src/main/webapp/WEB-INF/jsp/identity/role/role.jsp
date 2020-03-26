<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
 <%@ taglib prefix="fk" uri="/longIt/oa"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>OA办公管理系统-角色管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="This is my page" />
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
	<script type="text/javascript"
	src="${ctx}/res/js/operaPageCss.js"></script>
	<link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
	<!-- 引入分页样式 -->
<link rel="stylesheet" href="${ctx}/res/css/pager.css">
	<script type="text/javascript">
	
	    $(function(){

			if("${message}"){
				parent.$.messager.show({
					title:'提示信息',
					msg:"<font color='red'>${message}</font>",
					showType:'show'
				});
			}
			
	    	
	    	   //为添加按钮绑定点击事件
	           $("#addRole").click(function(){
	        	   
	        	   $("#divDialog").dialog({
	        		    title : "添加角色", // 标题
	        			cls : "easyui-dialog", // class
	        			width : 480, // 宽度
	        			height : 255, // 高度
	        			maximizable : true, // 最大化
	        			minimizable : false, // 最小化
	        			collapsible : true, // 可伸缩
	        			modal : true, // 模态窗口
	        			onClose : function(){ // 关闭窗口
	        				window.location = "/identity/role/selectRole?pageIndex=${pageModel.pageIndex}";
	        			} 
	        	   })
	        	   
	        	   //通过iframe将添加角色信息的页面引入
	        	   $("#iframe").prop("src","/identity/role/showAddRole");
	        	       
	           })
	           
	           //为删除按钮绑定点击事件
	           $("#deleteRole").click(function(){
	        	   //获取选中的checkbox
	        	   var boxes = $("input[name='box']:checked");
	        	   if(boxes.length == 0){
	        		   $.messager.alert('提示信息',"请选择需要删除的角色信息！",'warning');
	        	   }else{
	        		   
	        		   $.messager.confirm('确认信息','是否确认删除该记录?',function(r){   
	        			    //如果用户点击确认  那么  r的值为true
	        			    if (r){   
	        			        var arr = new Array();
	        			    	$.each(boxes,function(){
	        			    		arr.push(this.value);
	        			    	})
	        			    	//发送请求删除用户信息      
	        			    	window.location = "/identity/role/deleteRole?roleIds="+arr+"&pageIndex=${pageModel.pageIndex}";
	        			    }   
	        			});  

	        		   
	        		   
	        	   }
	           })
	           
	    	
	    	
	    	
	    })

	    
	    function updateRole(roleId){
	  
    	  $("#divDialog").dialog({
  		    title : "更新角色", // 标题
  			cls : "easyui-dialog", // class
  			width : 480, // 宽度
  			height : 255, // 高度
  			maximizable : true, // 最大化
  			minimizable : false, // 最小化
  			collapsible : true, // 可伸缩
  			modal : true, // 模态窗口
  			onClose : function(){ // 关闭窗口
  				window.location = "/identity/role/selectRole?pageIndex=${pageModel.pageIndex}";
  			} 
  	   })
  	   
  	   //通过iframe将修改角色信息的页面引入
  	   $("#iframe").prop("src","/identity/role/showUpdateRole?roleId="+roleId);
    		  
    }
	    
	</script>
</head>
<body style="overflow: hidden;width: 100%;height: 100%;padding: 5px;">
	<div>
		<div class="panel panel-primary">
			<!-- 工具按钮区 -->
			<div style="padding-top: 4px;padding-bottom: 4px;">
				<a  id="addRole" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
				<a  id="deleteRole" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a>
			</div>
			
			<div class="panel-body">
				<table class="table table-bordered" style="float: right;">
					<thead>
					    <tr>
						<th style="text-align: center;"><input type="checkbox" id="checkAll"/></th>
						<th style="text-align: center;">名称</th>
						<th style="text-align: center;">备注</th>
						<th style="text-align: center;">操作</th>
						<th style="text-align: center;">创建日期</th>
						<th style="text-align: center;">创建人</th>
						<th style="text-align: center;">修改日期</th>
						<th style="text-align: center;">修改人</th>
						<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<c:forEach items="${roles}" var="role" varStatus="stat">
				         <tr align="center" id="dataTr_${stat.index}">
							<td><input type="checkbox" name="box" id="box_${stat.index}" value="${role.id}"/></td>
							<td>${role.name}</td>
							<td>${role.remark}</td>
							<td><span class="label label-success"><a href="${ctx}/identity/role/selectRoleUser?id=${role.id}" style="color: white;">绑定用户</a></span>&nbsp;
								<span class="label label-info"><a href="${ctx}/identity/popedom/mgrPopedom?id=${role.id}" style="color: white;">绑定操作</a></span></td>
							<td><fmt:formatDate value="${role.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${role.creater.name}</td>
							<td><fmt:formatDate value="${role.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${role.modifier.name}</td>
							<td>   <span class="label label-info"><a href="javascript:updateRole('${role.id}')">修改</a></span></td>
						</tr>
		   			 </c:forEach>
				</table>
				
			</div>
			  <!-- 分页标签区 -->
				<fk:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.recordCount}" submitUrl="${ctx}/identity/role/selectRole?pageIndex={0}"></fk:pager>
		</div>
	</div>
    <!-- div作为弹出窗口 -->
    <div id="divDialog">
		<iframe id="iframe"  scrolling="no" frameborder="0" width="100%" height="100%"></iframe>
	</div>
	
</body>
</html>