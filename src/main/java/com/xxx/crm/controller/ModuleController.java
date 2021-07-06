package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;

import com.xxx.crm.dto.TreeModel;
import com.xxx.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {


    @Resource
    private ModuleService moduleService;

    @RequestMapping("index")
    public String index(){
        return "module/module";
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(){
        return moduleService.queryAllModules();
    }

    /**
     * ztree显示
     * @return
     */
    @RequestMapping("queryModule")
    @ResponseBody
    public List<TreeModel> sayAllModule(){
        return moduleService.queryAllModule();
    }

  /*  *//**
     * 权限回显
     * @return
     *//*
    @RequestMapping("queryAllModule")
    @ResponseBody
    public List<TreeModel> sayModuleTree(Integer roleId){
        return moduleService.searchAllModules(roleId);
    }

*/


}
