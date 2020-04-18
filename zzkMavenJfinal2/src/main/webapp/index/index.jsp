<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="<%=path%>/images/favicon.ico">
<title>投票</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/common/layui/css/layui.css">
<script src="<%=path%>/common/layui/layui.js"></script>
<script src="<%=path%>/js/jquery.js"></script>
</head>
<body>

<div class="demoTable">
  搜索ID：
  <div class="layui-inline">
    <input class="layui-input" name="id" id="demoReload" autocomplete="off">
  </div>
  <button class="layui-btn zzk-search" data-type="reload">搜索</button>
  <button class="layui-btn zzk-add" data-type="add">添加</button>
</div>
	<table id="demo" lay-filter="test"></table>
	
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<script>
$(function(){
	  $('.demoTable .zzk-add').on('click', function(){
		//alert("11");
		add();
		});
});
layui.use('table', function(){
  var table = layui.table;
  
  //第一个实例
  table.render({
    elem: '#demo'
    ,height: 312
    ,url: '<%=path%>/index1' //数据接口
    ,page: true //开启分页
    ,initSort: {
        field: 'Id' //排序字段，对应 cols 设定的各字段名
        ,type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
    }
    ,sort:true  //重点1：这里的sort表示 table表在取得接口数据后，对页面渲染后的table数据进行排序。同时，这里的true 会影响页面sort 上下小箭头的 显示效果
    ,cols: [[ //表头
      {field: 'Id', title: 'ID', width:80, sort: true, fixed: 'left'}
      ,{field: 'Name', title: '用户名', width:80}
      ,{field: 'Sex', title: '性别', width:80, sort: true}
      ,{field: 'Nationality', title: '国家', width:80} 
      ,{field: 'Phone', title: '电话', width: 80}
      ,{field: 'Salary', title: '工资', width: 135, sort: true}
      ,{fixed: 'right', title:'操作',width:178, align:'center', toolbar: '#barDemo'}
    ]]
  });

  table.on('sort(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  console.log(obj.field); //当前排序的字段名
	  console.log(obj.type); //当前排序类型：desc（降序）、asc（升序）、null（空对象，默认排序）
	  console.log(this); //当前排序的 th 对象
	  
	  //尽管我们的 table 自带排序功能，但并没有请求服务端。
	  //有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，从而实现服务端排序，如：
	  table.reload('demo', {
	    initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。 layui 2.1.1 新增参数
	    ,where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
	      field: obj.field //排序字段   在接口作为参数字段  field order
	      ,order: obj.type //排序方式   在接口作为参数字段  field order
	    }
	  });
	});
  

  //监听工具条
  table.on('tool(test)', function(obj){
    var data = obj.data;
    if(obj.event === 'detail'){
      layer.msg('ID：'+ data.Id + ' 的查看操作');
    } else if(obj.event === 'del'){
      layer.confirm('真的删除行么', function(index){
    	  if(deletee(data.Id)){
    	        obj.del();
    	        layer.close(index);    		  
    	  };
      });
    } else if(obj.event === 'edit'){
      //layer.alert('编辑行：<br>'+ JSON.stringify(data.Id))
    	$(location).attr('href', '<%=path%>/edit?editId='+data.Id);
    }
  });
  
  var $ = layui.$, active = {
    reload: function(){
      var demoReload = $('#demoReload');
      
      //执行重载
      table.reload('demo', {
        page: {
          curr: 1 //重新从第 1 页开始
        }
        ,where: {
          key: {
            parameter: {
            	Nationality:demoReload.val(),
            	uName:demoReload.val()
            }
          }
        }
      }, 'data');
    }
  };
  
  $('.demoTable .zzk-search').on('click', function(){
    var type = $(this).data('type');
    active[type] ? active[type].call(this) : '';
  });
});
function add(){
	$(location).attr('href', '<%=path%>/add');
}
function deletee(Uid){
	var rt=false;
	$.ajax( {    
	    url:'<%=path%>/delete',// 跳转到 action    
	    data:JSON.stringify({    __method:"DELETE",
	             Uid : Uid
	    }),    
	    type:'delete',    
	    cache:false,    
	    dataType:'json', 
	    async : false,
	    success:function(data) {    
	        if(data.code =="1" ){  
	            //alert("修改成功！");
	            rt = true;
	        }else{    
	        	alert(data.msg);    
	        }    
	     },    
	     error : function() { 
	          alert("异常！");    
	     }    
	}); 
	return rt;
}
</script>
</body>
</html>
