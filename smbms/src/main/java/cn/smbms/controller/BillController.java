package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-09-01 10:01:14
 **/
@Controller
@RequestMapping("bill")
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private ProviderService providerService;
    @RequestMapping(value = "/billlist.html",method = {RequestMethod.GET,RequestMethod.POST})
    public String BillList(@RequestParam(value = "queryProductName",defaultValue = "") String queryProductName,
                           @RequestParam(value = "queryProviderId",defaultValue = "0") String queryProviderId,
                           @RequestParam(value = "queryIsPayment",defaultValue = "0") String queryIsPayment,
                           Model model){

        List<Provider> providerList = providerService.getProviderList("","");
        model.addAttribute("providerList", providerList);
        List<Bill> billList = new ArrayList<Bill>();
        Bill bill = new Bill();
        bill.setIsPayment(Integer.parseInt(queryIsPayment));
        bill.setProviderId(Integer.parseInt(queryProviderId));
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    @RequestMapping("/addbill.html")
    public String addProvider(@ModelAttribute("bill") Bill bill){
        return "billadd";
    }
    @RequestMapping("/addbillsave.html")
    public String addProviderSave(Bill bill, HttpSession session){
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setCreationDate(new Date());
        if( billService.add(bill)){
            return "redirect:/bill/billlist.html";
        }else{
            return "billadd";
        }
    }
    @RequestMapping("/tomodify.html")
    public String billModify(@RequestParam(value = "billid",defaultValue = "") String id,
                             Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billmodify";
        }else{
            throw new RuntimeException("id不存在");
        }
    }
    @RequestMapping("modifysave.html")
    public String modifySave(Bill bill, HttpServletRequest request){
        bill.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        if(billService.modify(bill)){
            return "redirect:/bill/billlist.html";
        }else{
            return "billmodify";
        }
    }
    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public String view(@PathVariable(value = "id") String id,Model model){
        if(!StringUtils.isNullOrEmpty(id)){
            Bill bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billview";
        }else{
            throw new RuntimeException("id不存在");
        }
    }
    @RequestMapping(value = "/view"/*,produces = "application/json;charset=utf-8"*/)
    @ResponseBody
    public String view(@RequestParam(value = "id",defaultValue = "")String id){
        if(!StringUtils.isNullOrEmpty(id)){
            //调用后台方法得到user对象
           Bill bill = billService.getBillById(id);
            return JSON.toJSONString(bill);
        }else {
            return "null";
        }
    }
    @RequestMapping("/getprolist")
    @ResponseBody
    public String getproList(){
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("","");
        //把providerList转换成json对象输出
       return JSON.toJSONString(providerList);
    }
    @RequestMapping("/delbill")
    @ResponseBody
    public String delBill(@RequestParam("billid") String id){

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            boolean flag = billService.deleteBillById(id);
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        return JSON.toJSONString(resultMap);
    }
}
