
var roleName = null;
var saveBtn = null;
var backBtn = null;
$(function(){
   roleName = $("#roleName");
   saveBtn = $("#save");
    backBtn = $("#back");

    /*
     * 验证
     * 失焦\获焦
     * jquery的方法传递
     */
    roleName.on("focus",function(){
        validateTip(roleName.next(),{"color":"#666666"},"* 请输入角色名称",false);
    }).on("blur",function(){
        if(roleName.val() != null && roleName.val() != ""){
            validateTip(roleName.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(roleName.next(),{"color":"red"},imgNo+" 角色名称不能为空，请重新输入",false);
        }
    });
    saveBtn.on("click",function(){
        roleName.blur();
        if(roleName.attr("validateStatus") == "true"){
            if(confirm("是否确认提交数据")){
                $("#roleForm").submit();
            }
        }
    });
    backBtn.on("click",function(){
        //alert("modify: "+referer);
        if(referer != undefined
            && null != referer
            && "" != referer
            && "null" != referer
            && referer.length > 4){
            window.location.href = referer;
        }else{
            history.back(-1);
        }
    });
});