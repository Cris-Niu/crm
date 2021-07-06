package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.Role;
import com.xxx.crm.dto.TreeModel;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.service.ModuleService;
import com.xxx.crm.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    @Resource
    private ModuleService moduleService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryAllRoles(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }


    @RequestMapping("index")

    public String index() {
        return "role/role";
    }

    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.addRole(role);
        return success("角色记录添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteByPrimaryKey(id);
        return success("角色记录删除成功");
    }


    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }


    /**
     * 给用户添加授权
     */

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mIds, Integer roleId){
        System.out.println("addgrant开始");
        roleService.addGrant(mIds, roleId);
        return success("授权成功");
    }


    /**
     * 权限回显
     * @return
     */
    @RequestMapping("queryAllModule")
    @ResponseBody
    public List<TreeModel> sayModuleTree(Integer roleId){
        return moduleService.searchAllModules(roleId);
    }
}

