package cn.smbms.controller;

import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-09-07 09:28:12
 **/
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @RequestMapping("/login.html")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/dologin.html")
    public String doLogin(@RequestParam("userCode") String userCode,
                          @RequestParam("userPassword") String userPassword,
                          HttpSession session,
                          Model model){
        System.out.println("login ============ " );
        //调用service方法，进行用户匹配
        User user = userService.login(userCode);
        if(null != user){//登录成功
            if(!user.getUserPassword().equals(userPassword)){
                throw new RuntimeException("密码不正确");
            }else{
                //放入session
                session.setAttribute(Constants.USER_SESSION, user);
                //页面跳转（frame.jsp）
                return "redirect:/user/main.html";
            }
        }else{
            //页面跳转（login.jsp）带出提示信息--转发
//           model.addAttribute("error", "用户名或密码不正确");
//            return "login";
            throw new RuntimeException("用户名不存在");
        }
    }
    @RequestMapping("/logout.html")
    public String logout(HttpSession session){
        session.removeAttribute(Constants.USER_SESSION);
        return "redirect:/user/login.html";
    }
}
