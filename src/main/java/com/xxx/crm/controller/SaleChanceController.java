package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.bean.User;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.CookieUtil;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private UserService userService;

    /**
     * 显示表单数据
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query, Integer flag,
                                                        HttpServletRequest request) {
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人参数
        if (null != flag && flag == 1) {
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 营销机会模块首页
     * @return
     */
    @RequestMapping("/index")
    public String index(){

        return "/salechance/sale_chance";
    }

    /**
     * 添加或者修改营销数据页面
     * @return
     */
    @RequestMapping("/addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        //修改和添加主要的区别是表单中是否有ID,有id修改操作，否则添加
        if (id != null) {
            //查询销售机会对象
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance", saleChance);
        }
        return "salechance/add_update";
    }

    /**
     * 添加营销数据
     * @param saleChance
     * @param req
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo sayAdd(SaleChance saleChance, HttpServletRequest req) {
        //从获取userId
        /*Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法查询
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        //指定创建人
        saleChance.setAssignMan(trueName);*/
        //添加
        saleChanceService.addSaleChance(req,saleChance);
        return success("营销机会添加成功");
    }

    /**
     * 更新营销数据
     * @param saleChance
     * @param req
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo sayUpdate(SaleChance saleChance, HttpServletRequest req) {
        //修改
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会修改成功");
    }

    /**
     * 删除营销数据
     * @param ids
     * @return
     */
    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo sayDels(Integer[] ids) {
        System.out.println(Arrays.toString(ids)+"<<<controller");
        saleChanceService.removeSaleChance(ids);
        return success("批量删除营销机会成功");
    }
}
