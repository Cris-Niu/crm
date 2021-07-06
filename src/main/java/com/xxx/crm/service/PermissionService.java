package com.xxx.crm.service;


import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.Permission;
import com.xxx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;


    public List<String> queryPermissionByRoleByUserId(int userId) {
        return  permissionMapper.queryPermissionByUserId(userId);
    }
}
