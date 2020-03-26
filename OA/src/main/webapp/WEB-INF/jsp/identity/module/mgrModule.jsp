<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>办公管理系统-菜单管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta name="Keywords" content="keyword1,keyword2,keyword3" />
<meta name="Description" content="网页信息的描述" />
<meta name="Author" content="longIt.org" />
<meta name="Copyright" content="All Rights Reserved." />
<link href="${ctx}/longIt.ico" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="${ctx}/res/resources/easyUI/easyui.css">
<script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx }/res/resources/jquery/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/res/resources/easyUI/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/res/resources/dtree/dtree.css"/>
<script type="text/javascript" src="${ctx}/res/resources/dtree/dtree.js"></script>

 <script type="text/javascript">
 
 
    $(function(){
    	    
    	  
    	createDtree(); 	
    	
    })
    
    
    function createDtree(){
    	  d = new dTree('d');
          
  		d.add(0,-1,'系统模块树');
  		d.add("1",0,'全部',"/identity/module/loadSonModule","全部","rightFrame");
  
  			 //发送异步请求加载模块信息
      	    $.ajax({
    				   type:"post",//请求的方式
    				   url:"/identity/module/ajaxdTree",//指定访问的地址
    				   dataType:"json",//预计服务器返回的数据类型 
    				   success:function(result){ //后台响应成功时的回调函数
    					  $.each(result,function(){
    						//d.add(id,父级节点的id,节点名字,请求地址,标题,跳转位置);
    						d.add(this.code,this.pcode,this.name,'/identity/module/loadSonModule?code='+this.code,this.name,"rightFrame");
    					  })
    					  
    					  //将生成好的树放在id为tree的div中
    					  $("#tree").html(d.toString());
    				   },error:function(){
    					   $.messager.alert('错误提示',"网络异常！",'warning');
    				   }
    				   
     			   })
    }
  

 </script>
</head>
    <body class="easyui-layout" style="width:100%;height:100%;">
			<div id="tree" data-options="region:'west'" title="菜单模块树" style="width:20%;padding:10px">
				 <!-- 展示所有的模块树  -->
				
			</div>
			
			<div data-options="region:'center'" title="模块菜单"  >
			     <!-- 展示当前模块下的子模块  -->
			     <iframe src="/identity/module/loadSonModule" frameborder="0" id="sonModules" name="rightFrame"  width="100%" height="100%" ></iframe>
			</div>
	</body>
</html>
