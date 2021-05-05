

package com.wmsweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
    @RequestMapping(value = {"login"}, method = {RequestMethod.GET})
    public String login(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "login";
    }

    @RequestMapping(value = {"login2"}, method = {RequestMethod.GET})
    public String login2(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "login2";
    }

    @RequestMapping(value = {"index"}, method = {RequestMethod.GET})
    public String index(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "index";
    }

    @RequestMapping(value = {"out"}, method = {RequestMethod.GET})
    public String out(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "out";
    }

    @RequestMapping(value = {"in"}, method = {RequestMethod.GET})
    public String in(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "in";
    }

    @RequestMapping(value = {"transport"}, method = {RequestMethod.GET})
    public String transport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "transport";
    }

    @RequestMapping(value = {"changepassword"}, method = {RequestMethod.GET})
    public String changepassword(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "changepassword";
    }

    @RequestMapping(value = {"changepassword1"}, method = {RequestMethod.GET})
    public String changepassword1(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "changepassword1";
    }

    @RequestMapping(value = {"data1"}, method = {RequestMethod.GET})
    public String data1(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "data1";
    }

    @RequestMapping(value = {"deleteorder"}, method = {RequestMethod.GET})
    public String deleteorder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "deleteorder";
    }

    @RequestMapping(value = {"deletetransport"}, method = {RequestMethod.GET})
    public String deletetransport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "deletetransport";
    }

    @RequestMapping(value = {"editorder"}, method = {RequestMethod.GET})
    public String editorder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "editorder";
    }

    @RequestMapping(value = {"insert"}, method = {RequestMethod.GET})
    public String insert(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "insert";
    }

    @RequestMapping(value = {"logout"}, method = {RequestMethod.GET})
    public String logout(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "logout";
    }

    @RequestMapping(value = {"orderList"}, method = {RequestMethod.GET})
    public String orderList(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "orderlist";
    }

    @RequestMapping(value = {"editOrder"}, method = {RequestMethod.GET})
    public String orderlist(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "editorder";
    }

    @RequestMapping(value = {"productList"}, method = {RequestMethod.GET})
    public String productlist(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "productlist";
    }

    @RequestMapping(value = {"purchase"}, method = {RequestMethod.GET})
    public String purchase(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "purchase";
    }

    @RequestMapping(value = {"purchase1"}, method = {RequestMethod.GET})
    public String purchase1(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "purchase1";
    }

    @RequestMapping(value = {"showorder"}, method = {RequestMethod.GET})
    public String showorder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "showorder";
    }

    @RequestMapping(value = {"sku"}, method = {RequestMethod.GET})
    public String sku(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "sku";
    }

    @RequestMapping(value = {"sorting"}, method = {RequestMethod.GET})
    public String sorting(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "sorting";
    }

    @RequestMapping(value = {"tran"}, method = {RequestMethod.GET})
    public String tran(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "tran";
    }

    @RequestMapping(value = {"transport1"}, method = {RequestMethod.GET})
    public String transport1(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "transport1";
    }

    @RequestMapping(value = {"update"}, method = {RequestMethod.GET})
    public String update(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "update";
    }

    @RequestMapping(value = {"productionPlan"}, method = {RequestMethod.GET})
    public String updatedummy(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "productionPlan";
    }

    @RequestMapping(value = {"excelImport"}, method = {RequestMethod.GET})
    public String excelImport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "excelImport";
    }

    @RequestMapping(value = {"changeSkuCapacity"}, method = {RequestMethod.GET})
    public String changeSkuCapacity(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "changeSkuCapacity";
    }

    @RequestMapping(value = {"changeBayCapacity"}, method = {RequestMethod.GET})
    public String changeBayCapacity(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "changeBayCapacity";
    }

    @RequestMapping(value = {"dispatchPlan"}, method = {RequestMethod.GET})
    public String dispatchPlan(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "dispatchPlan";
    }

    @RequestMapping(value = {"updateProduction"}, method = {RequestMethod.GET})
    public String updateProduction(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "updateProduction";
    }

    @RequestMapping(value = {"orderDetails"}, method = {RequestMethod.GET})
    public String orderDetails(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "orderDetails";
    }

    @RequestMapping(value = {"productOrder"}, method = {RequestMethod.GET})
    public String productOrder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "productOrder";
    }

    @RequestMapping(value = {"orderProduct"}, method = {RequestMethod.GET})
    public String orderProduct(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "orderProduct";
    }

    @RequestMapping(value = {"searchProduct"}, method = {RequestMethod.GET})
    public String searchProduct(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "searchProduct";
    }

    @RequestMapping(value = {"completeProduct"}, method = {RequestMethod.GET})
    public String completeProduct(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "completeProduct";
    }

    @RequestMapping(value = {"verifyProduct"}, method = {RequestMethod.GET})
    public String verifyProduct(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "varifyProduction";
    }

    @RequestMapping(value = {"addDriverDetails"}, method = {RequestMethod.GET})
    public String addDriverDetails(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "addDriverDetails";
    }

    @RequestMapping(value = {"customOrder"}, method = {RequestMethod.GET})
    public String customOrder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "customOrder";
    }

    @RequestMapping(value = {"insertProduction"}, method = {RequestMethod.GET})
    public String insertProduction(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "insertProduction";
    }

    @RequestMapping(value = {"dispatchExcelImport"}, method = {RequestMethod.GET})
    public String dispatchExcelImport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "dispatchExcelImport";
    }

    @RequestMapping(value = {"downloadTransportExcel"}, method = {RequestMethod.GET})
    public String downloadTransportExcel(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "downloadTransportExcel";
    }

    @RequestMapping(value = {"downloadProductionExcel"}, method = {RequestMethod.GET})
    public String downloadProductionExcel(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "downloadProductionExcel";
    }

    @RequestMapping(value = {"completeOrder"}, method = {RequestMethod.GET})
    public String orderComplete(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/completeProduct?order_id=";
    }
    
    @RequestMapping(value = {"editDetail"}, method = {RequestMethod.GET})
    public String editDetail(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/editDetail";
    }
    
    
    @RequestMapping(value = {"complete"}, method = {RequestMethod.GET})
    public String complete(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/complete";
    }
    
    
    @RequestMapping(value = {"cancel"}, method = {RequestMethod.GET})
    public String cancel(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/cancel";
    }
    
    @RequestMapping(value = {"productionPlanImport"}, method = {RequestMethod.GET})
    public String productionPlanImport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/productionPlanImport";
    }
    
    
    @RequestMapping(value = {"manualOrder"}, method = {RequestMethod.GET})
    public String manualOrder(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/manualOrder";
    }

    @RequestMapping(value = {"MannualOrderReport"}, method = {RequestMethod.GET})
    public String manualOrderReport(Model model) {
        model.addAttribute("msg", (Object) "Please Enter Your Login Details");
        return "/MannualOrderReport";
    }
       
}
