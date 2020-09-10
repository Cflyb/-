package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-09-01 09:47:11
 **/
@Controller
@RequestMapping("provider")
public class ProviderController {
    @Autowired
    private ProviderService providerService;
    @RequestMapping(value = "/providerlist.html",method = {RequestMethod.GET,RequestMethod.POST})
    public String providerList(@RequestParam(value = "queryProName",defaultValue = "") String queryProName,
                               @RequestParam(value = "queryProCode",defaultValue = "") String queryProCode,
                               Model model){
        List<Provider> providerList = providerService.getProviderList(queryProName,queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }
    @RequestMapping("/addprovider.html")
    public String addProvider(@ModelAttribute("provider") Provider provider){
        return "provideradd";
    }
    @RequestMapping("/addprovidersave.html")
    public String addProviderSave(@ModelAttribute("provider") @Valid Provider provider, BindingResult result, HttpSession session,
                                  @RequestParam("a_companyLicPicPath")MultipartFile multipartFile,
                                  @RequestParam("a_orgCodePicPath")MultipartFile OrgFile,
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
               return "provideradd";
           }else{
               String[] types = {"jpg","jpeg","gif","png","pneg"};
               if(!Arrays.asList(types).contains(ext)){
                   model.addAttribute("uploadFileError","上传图片格式不正确");
                   return "provideradd";
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
                       return "provideradd";
                   }
                   savePath=targetPath+File.separator+fileName;
               }
           }
       }
        String saveOrgPath=null;
        if(!OrgFile.isEmpty()){
            //上传准备工作
            //获取文件原名和大小
            String oldName=OrgFile.getOriginalFilename();
            String ext= FilenameUtils.getExtension(oldName);
            long size = OrgFile.getSize();
            //文件大小不超过500k
            if(size >500*1024){
                model.addAttribute("uploadOrgFileError","上传图片不能超过500k");
                return "provideradd";
            }else{
                String[] types = {"jpg","jpeg","gif","png","pneg"};
                if(!Arrays.asList(types).contains(ext)){
                    model.addAttribute("uploadOrgFileError","上传图片格式不正确");
                    return "provideradd";
                }else{
                    //正式上传
                    String targetPath = session.getServletContext().getRealPath("statics"+ File.separator+"upload");
                    //修改上传文件名
                    String fileName=System.currentTimeMillis()+ RandomUtils.nextInt(100000)+"_Org."+ext;
                    File saveFile = new File(targetPath,fileName);
                    if(!saveFile.exists()){
                        saveFile.mkdirs();
                    }try {
                        OrgFile.transferTo(saveFile);
                    }catch (IOException e){
                        e.printStackTrace();
                        model.addAttribute("uploadOrgFileError","上传文件失败，联系管理员");
                        return "provideradd";
                    }
                    saveOrgPath=targetPath+File.separator+fileName;
                }
            }
        }
        if(result.hasErrors()){
            return "provideradd";
        }
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCompanyLicPicPath(savePath);
        provider.setOrgCodePicPath(saveOrgPath);
        provider.setCreationDate(new Date());
        if( providerService.add(provider)){
            return "redirect:/provider/providerlist.html";
        }else{
           return "provideradd";
        }
    }
    @RequestMapping(value = "/tomodify/{proid}")
    public String view1(@PathVariable(value = "proid") String id,
                                  Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Provider provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providermodify";
        }else{
            throw new RuntimeException("ID不存在");
        }
    }
    @RequestMapping("/modifysave.html")
    public String modifySave(Provider provider,HttpServletRequest request){
        provider.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        if(providerService.modify(provider)){
            return "redirect:/provider/providerlist.html";
        }else{
            return "providermodify";
        }
    }
    @RequestMapping(value = "view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable(value = "id") String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Provider provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providerview";
        }else{
            throw new RuntimeException("ID不存在");
        }
    }

    @RequestMapping(value = "/view"/*,produces = "application/json;charset=utf-8"*/)
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "")String id){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
            Provider provider = providerService.getProviderById(id);
            return JSON.toJSONString(provider);
        }else {
            return "null";
        }
    }
    @RequestMapping("proexist.html")
    @ResponseBody
    public String proexist(@RequestParam(value = "proCode",defaultValue = "") String proCode){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(proCode)){
            //userCode == null || userCode.equals("")
            resultMap.put("proCode", "exist");
        }else{
            Provider provider = providerService.selectProviderExist(proCode);
            if(null != provider){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }
        //把resultMap转为json字符串以json的形式输出
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping("/Delprovider")
    @ResponseBody
    public String delProvider(@RequestParam(value = "proid",defaultValue = "") String id){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
       return JSON.toJSONString(resultMap);
    }
}
