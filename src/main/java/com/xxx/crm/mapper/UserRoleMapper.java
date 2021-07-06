package com.xxx.crm.mapper;

import com.xxx.crm.bean.UserRole;

import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    int countUserRoleByUserId(Integer userId);

    int delUserRoleByUserId(Integer userId);

    int insertBatch(List<UserRole> list);
}