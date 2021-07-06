package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.User;
import com.xxx.crm.exception.ParamsException;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd){

        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
     /*   try{


        }catch (ParamsException p){
            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }*/

        return resultInfo;

    }


    /**
     * 修改用户密码
     * @param req
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @RequestMapping("/updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest req, String oldPassword,
                                         String newPassword, String confirmPassword){

        ResultInfo resultInfo = new ResultInfo();
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用service的方法修改密码
        userService.updatePassword(userId, oldPassword, newPassword, confirmPassword);
       /* try{

        }catch (ParamsException p){
            p.printStackTrace();
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }*/

        return resultInfo;

    }

    /**
     *
     * @return
     */
   /* @RequestMapping("/toSettingPage")
    @ResponseBody
    public ResultInfo updateInfo(HttpServletRequest req){
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        userService.updateInfo(userId);
        return success("保存成功");


    }*/


    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("/toSettingPage")
    public String toSettingPage(){
        return "user/setting";
    }

    /**
     * 增加修改的下拉框
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){

        return userService.queryAllSales();
    }


    /**
     * 以下为用户管理模块
     */


    /**
     * 用户模块表格数据
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery query){
        return userService.queryUserByParams(query);
    }

    /**
     * 用户管理模块首页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    /**
     * 用户模块添加操作
     */

    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){

        userService.addUser(user);
        return success("添加用户成功");

    }


    /**
     * 用户模块更新操作
     */

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户修改成功");
    }

    /**
     * 进入用户更新或者添加页面
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        User temp = userService.selectByPrimaryKey(id);

        if(id != null){
            model.addAttribute("user",temp);
        }
        return "user/add_update";
    }

    /**
     * 用户模块批量删除用户
     * @param ids
     */
    @RequestMapping("delmore")
    @ResponseBody
    public ResultInfo delBatchUsers(Integer[] ids){
        userService.delBatchUsers(ids);
        return success("删除成功");
    }

    @RequestMapping("del")
    @ResponseBody
    public ResultInfo delUser(Integer id){
        userService.deleteUser(id);
        return success("删除成功");
    }
}
