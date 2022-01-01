package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.dao.ClueDao;
import com.wjx.crm.workbench.dao.ClueRemarkDao;
import com.wjx.crm.workbench.domain.Activity;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        //取得total
        int total = clueDao.getTotalByCondition(map);
        //取得dataList
        List<Clue> dataList = clueDao.getClueByCondition(map);
        //创建一个vo对象将total和dataList封装到VO
        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);


        //返回vo
        return vo;
    }

    @Override
    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if (count!=1){
            flag=false;
        }

        return flag;
    }

    /*@Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = clueRemarkDao.getCountByAids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = clueRemarkDao.deleteByAids(ids);

        if (count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = clueDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }*/
}
