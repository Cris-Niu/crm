package com.xxx.crm.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.bean.User;
import com.xxx.crm.mapper.SaleChanceMapper;
import com.xxx.crm.mapper.UserMapper;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.CookieUtil;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    @Resource
    private UserMapper userMapper;
    /**
     * 表单显示
     * @param query
     * @return
     */

    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(query);
      /*  for(SaleChance saleChance : saleChances){
            System.out.println(saleChance.getCreateMan()+ "--创建人-->" + saleChance.getId());
            System.out.println(saleChance.getAssignMan()+ "--指派人-->" + saleChance.getId());
        }*/
        PageInfo<SaleChance> pageInfo = new PageInfo<> (saleChances);
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        //System.out.println(map);
        return map;
        }

    /**
     * 营销数据添加
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(HttpServletRequest req, SaleChance saleChance){
        //校验参数
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //设置创建人
        //int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String userName = CookieUtil.getCookieValue(req, "userName");
        saleChance.setCreateMan(userName);
        saleChance.setState(0);//分配状态
        saleChance.setDevResult(0);//设置开发状态
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //设定指派人
        saleChance.setAssignMan(saleChance.getAssignMan());
        //分配人不为空设置分配信息的默认值
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);//已分配
            saleChance.setDevResult(1);//开发中
            saleChance.setAssignTime(new Date());
        }
        //执行添加操作
        System.out.println(saleChance.getAssignMan() + "前端传过来的assignman");
        Integer result = saleChanceMapper.insertSelective(saleChance);
        AssertUtil.isTrue(result<1,"添加失败");
    }

    /**
     * 校验参数
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入正确的联系方式");
    }

    /**
     * 更新salechance
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        // 通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        // 判断是否为空
        AssertUtil.isTrue(null == temp,"待更新的记录不存在");
        // 校验基础参数
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        // 2. 设置相关参数值
        saleChance.setUpdateDate(new Date());
        //数据库中记录分配人为空,参数的分配人不为空
        if(StringUtils.isBlank(temp.getAssignMan())
                && StringUtils.isNotBlank(saleChance.getAssignMan())){

            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
            //数据库中记录分配人不为空,参数的分配人为空 == 改变分配状态
        }else{

            saleChance.setAssignMan("");
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        }

        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1,"更新失败");
    }

    /**
     * 批量删除
     * @param array
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChance(Integer []array){
        AssertUtil.isTrue(array == null || array.length==0,"请选择要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(array)<1,"批量删除失败了");
    }
}
