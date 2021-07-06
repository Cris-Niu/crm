package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.Module;
import com.xxx.crm.bean.Permission;
import com.xxx.crm.bean.Role;
import com.xxx.crm.mapper.ModuleMapper;
import com.xxx.crm.mapper.PermissionMapper;
import com.xxx.crm.mapper.RoleMapper;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加role
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"用户名不能为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"该角色已存在");
        //设置默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1,"添加失败");
    }

    /**
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void  updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该角色已存在!");
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败!");
    }


    /**
     * 角色授权
     */

    public void addGrant(Integer[] mIds, Integer roleId){

        Role temp = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp == null || roleId == null,"待授权的角色不存在");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count > 0){
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId) < count,"授权失败");
        }
        if(null != mIds && mIds.length >0){
            List<Permission> permissions = new ArrayList<>();
            for(Integer mId : mIds){
                Permission p = new Permission();
                p.setRoleId(roleId);
                p.setModuleId(mId);
                p.setUpdateDate(new Date());
                p.setCreateDate(new Date());
                Module module = moduleMapper.selectByPrimaryKey(mId);
                p.setAclValue(module.getOptValue());

                permissions.add(p);
            }

            AssertUtil.isTrue(permissionMapper.insertBatch(permissions) < permissions.size(),"授权失败");
        }
    }
}
