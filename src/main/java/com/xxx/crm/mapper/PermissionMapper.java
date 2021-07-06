package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    Integer insertBatch(List<Permission> permissions);

    Integer countPermissionByRoleId(Integer roleId);

    Integer deletePermissionByRoleId(Integer roleId);

    List<Integer> queryRolesHasAllModulesByRoleId(Integer roleId);

    List<String> queryPermissionByUserId(int userId);
}