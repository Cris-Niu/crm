package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.query.CusDevPlanQuery;
import com.xxx.crm.service.CusDevPlanService;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.print.DocFlavor;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private CusDevPlanService cusDevPlanService;

    @Resource
    private SaleChanceService saleChanceService;


    /**
     * 首页
     * @return
     */
    @RequestMapping("index")
    public String index2(){
        return "/cusdevplan/cus_dev_plan";
    }

    /**
     * 计划开发数据页面
     */

    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Model model, Integer sid){

        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);

        model.addAttribute("saleChance",saleChance);

        return "/cusdevplan/cus_dev_plan_data";
    }

    /**
     *
     */

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery query){
        return cusDevPlanService.queryCusDevPlanByParams(query);

    }
}
