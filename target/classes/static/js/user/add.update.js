layui.use(['form', 'jquery', 'jquery_cookie', 'table','formSelects'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
        formSelects = layui.formSelects;


    /**
     * user --> add_update.ftl 确定按钮绑定的事件
     */
    form.on('submit(addOrUpdateUser)', function (data){

            //弹出层
            var index = layer.msg("数据提交中,请稍后...", {
                icon: 16, // 图标
                time: false, // 不关闭
                shade: 0.8 // 设置遮罩的透明度
            });

            var url = ctx + "/user/add";
        if($("input[name='id']").val()){
                url = ctx + "/user/update";
            }
        $.ajax({
            type:"post",
            url:url,
            data: data.field,
            dataType:"json",
            success:function(data){
                if(data.code == 200){
                    layer.close(index);
                    layer.closeAll("iframe");
                    parent.location.reload();

                }else{
                    //修改失败了
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });

    /**
     * 关闭弹出层
     */

    $("#closeBtn").click(function (){
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //关闭弹窗
        parent.layer.close(index);
    });


    /**
     * 加载下拉框数据
     */
    var userId = $("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx + "/role/queryAllRoles?userId="+ userId,
        //自定义返回数据中name的key, 默认 name
        keyName: 'roleName',
        //自定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);
});