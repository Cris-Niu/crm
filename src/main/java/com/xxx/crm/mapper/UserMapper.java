package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    User updateParams(Integer id);

    int updateByPrimaryKey(User record);

    User selectByName(String userName);

    List<Map<String,Object>> selectAllSales();

    int delBatch(Integer[] arr);

    //插入用户信息，有当前对象的userId
    public int insertUserByReturnKey(User user);

}