package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Module;
import com.xxx.crm.dto.TreeModel;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {

    //查询所有的资源，roleId
    //id,name,pid;
    public List<TreeModel> selectAllModule();
    //查询所有的资源
    List<Module> selectAllModules();


}