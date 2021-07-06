package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {


    List<Map<String, Object>> selectAllRoles(Integer userId);

    Role selectByRoleName(String RoleName);

    public List<Map<String,Object>> queryAllRoles(Integer userId);
}