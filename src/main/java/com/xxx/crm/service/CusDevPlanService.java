package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.CusDevPlan;
import com.xxx.crm.mapper.CusDevPlanMapper;
import com.xxx.crm.query.CusDevPlanQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;


    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<CusDevPlan> cusDevPlans = cusDevPlanMapper.selectByParams(query);
        PageInfo<CusDevPlan> plist = new PageInfo<>(cusDevPlans);
        map.put("code",0);
        map.put("mag","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());

        return map;
    }


}
