package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-08-31 13:35:26
 **/
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @RequestMapping("/main.html")
    public String main(HttpSession session){
        if(session.getAttribute(Constants.USER_SESSION)==null){
            return "redirect:/user/login.html";
        }
        return "frame";
    }
//          异常处理器
//    @ExceptionHandler(value = {RuntimeException.class})
//    public String handlerException(RuntimeException e, HttpServletRequest request){
//        request.setAttribute("e",e);
//        return "login";
//    }


    @RequestMapping(value = "/userlist.html",method = {RequestMethod.GET,RequestMethod.POST})
    public String userList(@RequestParam(value = "queryname",defaultValue = "") String queryUserName,
                           @RequestParam(value = "queryUserRole",defaultValue = "0") Integer queryUserRole,
                           @RequestParam(value = "pageIndex",defaultValue = "1") Integer currentPageNo,
                           Model model){
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        List<Role> roleList  = roleService.getRoleList();
        model.addAttribute("userList", userList);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

@RequestMapping("/adduser.html")
    public String addUser(@ModelAttribute("user") User user){
    return "useradd";
}
    @RequestMapping("/addusersave.html")
    public  String addUserSave(@ModelAttribute("user") @Valid User user,
                               BindingResult result, HttpSession session,
                               @RequestParam("a_idPicPath")MultipartFile multipartFile,
                               @RequestParam("a_workPicPath")MultipartFile workFile,Model model){
        String savePath=null;
        if(!multipartFile.isEmpty()){
            //上传准备工作
            //获取文件原名和大小
            String oldName=multipartFile.getOriginalFilename();
            String ext= FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            //文件大小不超过500k
            if(size >500*1024){
                model.addAttribute("uploadFileError","上传图片不能超过500k");
                return "useradd";
            }else{
                String[] types = {"jpg","jpeg","gif","png","pneg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadFileError","上传图片格式不正确");
                    return "useradd";
                }else{
                    //正式上传
                    String targetPath = session.getServletContext().getRealPath("statics"+ File.separator+"upload");
                    //修改上传文件名
                    String fileName=System.currentTimeMillis()+ RandomUtils.nextInt(100000)+"_Personal."+ext;
                    File saveFile = new File(targetPath,fileName);
                    if(!saveFile.exists()){
                        saveFile.mkdirs();
                    }try {
                        multipartFile.transferTo(saveFile);
                    }catch (IOException e){
                        e.printStackTrace();
                        model.addAttribute("uploadFileError","上传文件失败，联系管理员");
                        return "useradd";
                    }
                    savePath=targetPath+File.separator+fileName;
                }
            }
        }
        String saveWorkPath=null;
        if(!workFile.isEmpty()){
            //上传准备工作
            //获取文件原名和大小
            String oldName=workFile.getOriginalFilename();
            String ext= FilenameUtils.getExtension(oldName);
            long size = workFile.getSize();
            //文件大小不超过500k
            if(size >500*1024){
                model.addAttribute("uploadWorkFileError","上传图片不能超过500k");
                return "useradd";
            }else{
                String[] types = {"jpg","jpeg","gif","png","pneg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadWorkFileError","上传图片格式不正确");
                    return "useradd";
                }else{
                    //正式上传
                    String targetPath = session.getServletContext().getRealPath("statics"+ File.separator+"upload");
                    //修改上传文件名
                    String fileName=System.currentTimeMillis()+ RandomUtils.nextInt(100000)+"_Work."+ext;
                    File saveFile = new File(targetPath,fileName);
                    if(!saveFile.exists()){
                        saveFile.mkdirs();
                    }try {
                        workFile.transferTo(saveFile);
                    }catch (IOException e){
                        e.printStackTrace();
                        model.addAttribute("uploadWorkFileError","上传文件失败，联系管理员");
                        return "useradd";
                    }
                    saveWorkPath=targetPath+File.separator+fileName;
                }
            }
        }
        if(result.hasErrors()){
            return "useradd";
        }
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setIdPicPath(savePath);
        user.setWorkPicPath(saveWorkPath);
        if(userService.add(user)){
           return "redirect:/user/userlist.html";
        }else{
            return "useradd";
        }
    }
    @RequestMapping("/tomodify.html")
    public String getUserById(@RequestParam(value = "uid",defaultValue = "") String id,
                              Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            String idPicPath = user.getIdPicPath();
            if(idPicPath!=null){
                String str = idPicPath.substring(47);
                model.addAttribute("str",str);
            }
            model.addAttribute("user", user);
            return "usermodify";
        }else {
            throw new RuntimeException("id不存在");
        }
    }
    @RequestMapping("/modifysave.html")
    public String modifySave(User user,HttpServletRequest request,
                             BindingResult result, HttpSession session,
                             @RequestParam("a_idPicPath")MultipartFile multipartFile,
                            Model model){
        String savePath=null;
        if(!multipartFile.isEmpty()){
            //上传准备工作
            //获取文件原名和大小
            String oldName=multipartFile.getOriginalFilename();
            String ext= FilenameUtils.getExtension(oldName);
            long size = multipartFile.getSize();
            //文件大小不超过500k
            if(size >500*1024){
                model.addAttribute("uploadFileError","上传图片不能超过500k");
                return "usermodify";
            }else{
                String[] types = {"jpg","jpeg","gif","png","pneg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadFileError","上传图片格式不正确");
                    return "usermodify";
                }else{
                    //正式上传
                    String targetPath = session.getServletContext().getRealPath("statics"+ File.separator+"upload");
                    //修改上传文件名
                    String fileName=System.currentTimeMillis()+ RandomUtils.nextInt(100000)+"_Personal."+ext;
                    File saveFile = new File(targetPath,fileName);
                    if(!saveFile.exists()){
                        saveFile.mkdirs();
                    }try {
                        multipartFile.transferTo(saveFile);
                    }catch (IOException e){
                        e.printStackTrace();
                        model.addAttribute("uploadFileError","上传文件失败，联系管理员");
                        return "usermodify";
                    }
                    savePath=targetPath+File.separator+fileName;
                }
            }
        }
        if(result.hasErrors()){
            return "usermodify";
        }
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        user.setIdPicPath(savePath);
        if(userService.modify(user)){
            return "redirect:/user/userlist.html";
        }else{
          return "usermodify";
        }
    }
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable(value = "id") String id,
                              Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "userview";
        }else {
            throw new RuntimeException("id不存在");
        }
    }
    @RequestMapping(value = "/view"/*,produces = "application/json;charset=utf-8"*/)
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "")String id){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
           return JSON.toJSONString(user);
        }else {
            return "null";
        }
    }
    @RequestMapping("/topwdmodify")
    public String toPwdModify(){
        return "pwdmodify";
    }
    @RequestMapping("/pwdmodify.html")
    @ResponseBody
    public String pwdModify(@RequestParam(value = "oldpassword") String oldpassword,
                            HttpServletRequest request){
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();
        if(null == o ){//session过期
            resultMap.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
            resultMap.put("result", "error");
        }else{
            String sessionPwd = ((User)o).getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                resultMap.put("result", "true");
            }else{//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping("/newpwdmodify.html")
    public String pwdmodify(@RequestParam(value = "newpassword",defaultValue = "") String newpassword,
                            HttpServletRequest request){
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);

        if(o != null && !StringUtils.isNullOrEmpty(newpassword)){


            if( userService.updatePwd(((User)o).getId(),newpassword)){
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                request.getSession().removeAttribute(Constants.USER_SESSION);//session注销
            }else{
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        }else{
            request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "pwdmodify";

    }
    @RequestMapping("/ucexist.html")
    @ResponseBody
    public String ucexist(@RequestParam(value = "userCode",defaultValue = "")String userCode){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        //把resultMap转为json字符串以json的形式输出
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping("/getrolelist")
    @ResponseBody
    public String getRoleList(){
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        return JSONArray.toJSONString(roleList);
    }
    @RequestMapping("/deluser")
    @ResponseBody
    public String delUser(@RequestParam(value = "uid") String id
                          ){
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            // TODO: handle exception
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();

        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            resultMap.put("mess","删除成功");}
            if(userService.deleteUserById(delId)){
                    resultMap.put("delResult", "true");
                }else{
                    resultMap.put("delResult", "false");
                }
        return JSON.toJSONString(resultMap);
    }
}

