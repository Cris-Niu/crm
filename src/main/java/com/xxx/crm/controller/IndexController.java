package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.bean.User;
import com.xxx.crm.service.PermissionService;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    /**
     * 系统登录⻚
     * @return
     */
    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";

    }

    // 系统界⾯欢迎⻚
    @RequestMapping("welcome")
    public String welcome(){
    return "welcome";
    }
    /**
     * 后端管理主⻚⾯
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req){
        // 通过⼯具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);

        //调用service层的方法根据id查询用户
        User user = userService.selectByPrimaryKey(userId);

        //将用户对象存到request作用域中
        req.setAttribute("user",user);

        List<String> permissions = permissionService.queryPermissionByRoleByUserId(userId);
        req.getSession().setAttribute("permissions",permissions);

        return "main";
    }

}
