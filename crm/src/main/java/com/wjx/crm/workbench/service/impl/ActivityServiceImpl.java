package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.dao.ActivityDao;
import com.wjx.crm.workbench.domain.Activity;
import com.wjx.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;


public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public boolean save(Activity a) {

        boolean flag = true;

        int count = activityDao.save(a);

        if (count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total = activityDao.getTotalByCondition(map);
        //取得dataList
        List<Activity> dataList = activityDao.getActivityByCondition(map);
        //创建一个vo对象将total和dataList封装到VO
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);


        //返回vo
        return vo;
    }
}
