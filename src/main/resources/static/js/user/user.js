layui.use(['form','jquery','jquery_cookie','table'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        table = layui.table;
        $ = layui.jquery_cookie($);

    // 用户表格渲染

    var tabIns = table.render({
        elem:"#userList",
        url:ctx + "/user/list",
        cellMinWidth:95,
        page:true,
        height:"full-125",
        limits:[10,15,20,25],
        limit:10,
        toolbar:"#toolbarDemo",
        id:"userListTable",
        cols:[[
        {type: "checkbox", fixed:"left", width:50},
        {field: "id", title:'编号',fixed:"true", width:80},
        {field: 'userName', title: '⽤户名', minWidth:50, align:"center"},
        {field: 'email', title: '⽤户邮箱', minWidth:100, align:'center'},
        {field: 'phone', title: '联系电话', minWidth:100, align:'center'},
        {field: 'trueName', title: '真实姓名', align:'center'},
        {field: 'createDate', title: '创建时间',
            align:'center',minWidth:150},
        {field: 'updateDate', title: '更新时间',
            align:'center',minWidth:150},
        {title: '操作', minWidth:150,
            templet:'#userListBar',fixed:"right",align:"center"}
        ]]

    });


    /**
     * 表格顶部的搜索框绑定事件
     *
     */
    $('.search_btn').on('click',function (){
        tabIns.reload({
            //第一页开始
            page:{curr:1},
            where:{
                userName:$('input[name=userName]').val(),
                email:$('input[name=email]').val(),
                phone:$("input[name=phone]").val()
            }
        });
    });


    /**
     * 用户模块*******
     * 头部工具栏监听事件
     */
    //users = 表格的lay.filter
    table.on('toolbar(users)', function (obj){
        var checkStatus = table.checkStatus(obj.config.id);

        switch (obj.event){
            case 'add':
                openAddOrUpdateFun();
                break;
            case 'del':
                delUsers(checkStatus.data);
        }
    });

    function delUsers(data){
        //验证
        if(data.length==0){
            layer.msg("请选择要删除的数据?");
            return ;
        }
        //声明数组存储数据
        var ids=[];
        //遍历
        for(var x in data){
            ids.push(data[x].id);
        }
        layer.confirm("你确定要删除数据吗?",{
            btn:["确定","取消"],
        },function(index){
            layer.close(index);
            //发送ajax
            $.ajax({
                type:"post",
                data:{"ids":ids.toString()},
                url:ctx+"/user/del",
                dataType:"json",
                success:function (obj){
                    if(obj.code == 200){
                        //重新加载表格
                        tabIns.reload();
                    }else{
                        //删除失败
                        layer.msg(obj.msg,{icon: 5 });
                    }
                }
            });

        });


    }

        function openAddOrUpdateFun(userId) {
            var url = ctx + "/user/addOrUpdateUserPage";
            var title = "用户管理--添加操作";
            if (userId) {
                url = url + "?id=" + userId;
                title = "用户管理--更新操作";
            }

            layui.layer.open({
                title: title,
                type: 2,
                area: ['650px', '400px'],
                maxmin: true,
                content: url
            });
        };

        /**
         * 行监听事件-- 编辑或者删除
         */
        //users = 表格的lay.filter
        table.on('tool(users)', function (obj) {
            var layEvent = obj.event;

            if (layEvent == 'edit') {
                openAddOrUpdateFun(obj.data.id);
            }
            if(layEvent == 'del'){

                layer.confirm("确定删除?",{icon:3, title:"用户管理"}, function (index){
                    var url = ctx + "/user/delmore";
                    $.post(url,{ids:obj.data.id},function (data){
                        if(data.code == 200){
                            layer.msg("操作成功");
                            tabIns.reload();
                        }else{
                            layer.msg(data.msg,{icon:5});
                        }
                    });
                });
            };
        });
});