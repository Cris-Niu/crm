package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;

import com.xxx.crm.bean.Module;
import com.xxx.crm.bean.Permission;
import com.xxx.crm.dto.TreeModel;
import com.xxx.crm.mapper.ModuleMapper;
import com.xxx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeModel> queryAllModule(){
        return  moduleMapper.selectAllModule();
    }


    /**
     * ztree数据的方法
     * @return
     */
    public Map<String,Object> queryAllModules(){
        Map<String,Object> map=new HashMap<>();
        //查询所有的数据
        List<Module> mlist = moduleMapper.selectAllModules();

        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",mlist.size());
        map.put("data",mlist);
        //返回目标map
        return map;
    }

    /**
     * 回显权限
     * @param roleId
     * @return
     */
    public List<TreeModel> searchAllModules(Integer roleId){
        List<TreeModel> treeModels = moduleMapper.selectAllModule();

        //查询角色拥有的模块id
        List<Integer> integers = permissionMapper.queryRolesHasAllModulesByRoleId(roleId);

        if(integers != null && integers.size()>0){

            for(TreeModel treeModel : treeModels){
                if(integers.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            }
        }

        return treeModels;

    }





}
