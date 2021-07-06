package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.User;
import com.xxx.crm.bean.UserRole;
import com.xxx.crm.mapper.UserMapper;
import com.xxx.crm.mapper.UserRoleMapper;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.util.LimitedInputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin (String userName, String userPwd){

        //判断用户名/密码是否为空
        checkParam(userName, userPwd);
        //根据用户名查询用户
        User temp = userMapper.selectByName(userName);
        AssertUtil.isTrue(null == temp,"用户不存在或者已注销");
        //校验密码
        checkUserPwd(userPwd, temp.getUserPwd());
        return buildUserInfo(temp);

    }

    /**
     *  构建返回的⽤户信息
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user){
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;

    }


    /**
     * 校验密码是否正确
     * @param userPwd  用户输入的密码
     * @param pwd 数据库密码
     */
    private void checkUserPwd(String userPwd,String pwd) {
        userPwd = Md5Util.encode(userPwd);

        AssertUtil.isTrue(!userPwd.equals(pwd),"密码不正确");

    }

    /**
     * 判断用户名和密码是否为空
     * @param userName 用户名
     * @param userPwd 密码
     */
    private void checkParam(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId, String oldPassword,
                               String newPassword, String confirmPassword){
        //通过id获取对象
        User user = userMapper.selectByPrimaryKey(userId);
        //校验参数
        checkPassword(user,oldPassword,  newPassword, confirmPassword);

        //新密码加密
        user.setUserPwd(Md5Util.encode(newPassword));
        //修改密码
        AssertUtil.isTrue(userMapper.updateByPrimaryKey(user)<1,"修改密码失败");

    }

    /**
     * 校验新密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPassword(User user,String oldPassword, String newPassword, String confirmPassword){
        //验证用户是否为空
        AssertUtil.isTrue(null == user,"用户未登录或者不存在");
        //原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"原始密码不能为空");
        //原始密码比对数据库密码
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原始密码不正确");
        //新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        //新密码和原始密码不能相同;
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码和原始密码不能相同");
        //确认密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        //确认密码和新密码相同;
        AssertUtil.isTrue(!newPassword.equals(confirmPassword),"确认密码和新密码要一致");


    }

    /**
     * 保存用户信息
     * @param req
     * @return
     */
    public void updateInfo(Integer userId){
        userMapper.updateParams(userId);

    }

    public List<Map<String,Object>> queryAllSales(){

        return userMapper.selectAllSales();
    }


    /**
     * 用户管理模块
     *
     */


    /**
     * 表格数据查询
     * @param query
     * @return
     */
    public Map<String,Object> queryUserByParams(UserQuery query){
        Map<String,Object> map = new HashMap<>();
        //初始化分页
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> users = userMapper.selectByParams(query);
       /* for(User u :users){
            System.out.println(u.toString());
        }*/
        PageInfo<User> plist = new PageInfo<>(users);
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());

        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //参数校验
        checkParam(user.getUserName(), user.getEmail(), user.getPhone());

        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("12345"));
        //添加
        AssertUtil.isTrue(userMapper.insertUserByReturnKey(user)<1,"添加失败");
        System.out.println(user.getId()+"----");

        /**
         * 用户角色关联
         */
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 用户角色关联方法 *********
     * @param id
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //统计角色
        int count = userRoleMapper.countUserRoleByUserId(userId);

        if(count>0){
            AssertUtil.isTrue(userRoleMapper.delUserRoleByUserId(userId)!=count,"角色分配失败");
        }
        if(StringUtils.isNotBlank(roleIds)){
            //准备一个List
            List<UserRole> urlist=new ArrayList<>();
            //遍历
            for (String rid: roleIds.split(",")) {
                UserRole ur=new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(Integer.parseInt(rid));
                ur.setCreateDate(new Date());
                ur.setUpdateDate(new Date());
                //添加容器
                urlist.add(ur);
            }
            //insert
            AssertUtil.isTrue(userRoleMapper.insertBatch(urlist)!=urlist.size(),"角色分配失败了");
        }
    }

    /**
     * 用户删除用户
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer userId) {
        User user = selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == userId || null == user, "待删除记录不存在!");
        int count = userRoleMapper.countUserRoleByUserId(userId);
        System.out.println(count + "count--->");
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.delUserRoleByUserId(userId) != count, "用户角色删除失败!");
        }
        user.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(user) < 1, "用户记录删除失败!");

    }



    /**
     * 用户模块更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //通过id查询用户
        User temp = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(null == temp,"待更新的用户不存在");

        //参数校验
        checkParamUpdate(user.getUserName(), user.getEmail(), user.getPhone());

        //设置默认值
        user.setUpdateDate(new Date());

        //更新
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"更新失败");

        /**
         * 用户角色关联
         */
        Integer userId = userMapper.selectByName(user.getUserName()).getId();
        relationUserRole(userId, user.getRoleIds());
    }

    /**
     * 添加或更新用户的校验
     * @param userName
     * @param email
     * @param phone
     */


    private void checkParamUpdate(String userName, String email, String phone){

        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号不合法");
    }

    private void checkParam(String userName, String email, String phone){

        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        User user = userMapper.selectByName(userName);
        AssertUtil.isTrue(null != user,"用户已存在");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号不合法");
    }

    /**
     * 用户模块的批量删除业务
     * @param arr
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delBatchUsers(Integer[] arr){
        AssertUtil.isTrue(null == arr || arr.length == 0 ,"请选择要删除的数据");
        AssertUtil.isTrue(userMapper.delBatch(arr) != arr.length,"删除失败");
    }
}
