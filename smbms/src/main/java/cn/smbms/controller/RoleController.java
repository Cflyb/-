package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-09-07 19:12:11
 **/
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/rolelist.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String RoleList(Model model) {
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        return "rolelist";
    }

    @RequestMapping("/addrole.html")
    public String addProvider(@ModelAttribute("role") Role role) {
        return "roleadd";
    }

    @RequestMapping("/addrolesave.html")
    public String addProviderSave(Role role) {
        role.setCreationDate(new Date());
        if (roleService.add(role)) {
            return "redirect:/role/rolelist.html";
        } else {
            return "roleadd";
        }
    }

    @RequestMapping("delrole")
    @ResponseBody
    public String delRole(@RequestParam("roleid") String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            boolean flag = roleService.deleteRoleById(id);
            if (flag) {//删除成功
                resultMap.put("roleResult", "true");
            } else {//删除失败
                resultMap.put("roleResult", "false");
            }
        } else {
            resultMap.put("roleResult", "notexit");
        }
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping("/tomodify.html")
    public String modifyRole(@RequestParam(value = "roleid", defaultValue = "") String id,
                             Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Role role = roleService.getRoleById(id);
            model.addAttribute("role", role);
            return "rolemodify";
        } else {
            throw new RuntimeException("ID不存在");
        }
    }

    @RequestMapping("/modifysave.html")
    public String modifySave(Role role, HttpServletRequest request) {
        role.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        role.setModifyDate(new Date());
        if (roleService.modify(role)) {
            return "redirect:/role/rolelist.html";
        } else {
            return "rolemodify";
        }
    }
    @RequestMapping(value = "view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable(value = "id") String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
           Role role = roleService.getRoleById(id);
            model.addAttribute("role", role);
            return "roleview";
        }else{
            throw new RuntimeException("ID不存在");
        }
    }
    @RequestMapping(value = "/view"/*,produces = "application/json;charset=utf-8"*/)
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "")String id){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            Role role = roleService.getRoleById(id);
            return JSON.toJSONString(role);
        }else {
            return "null";
        }
    }
}
