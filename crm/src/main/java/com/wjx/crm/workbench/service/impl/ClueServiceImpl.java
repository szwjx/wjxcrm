package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.settings.dao.UserDao;
import com.wjx.crm.settings.domain.User;
import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.utils.UUIDUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.dao.ClueActivityRelationDao;
import com.wjx.crm.workbench.dao.ClueDao;
import com.wjx.crm.workbench.dao.ClueRemarkDao;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.ClueActivityRelation;
import com.wjx.crm.workbench.domain.ClueRemark;
import com.wjx.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

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

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = clueRemarkDao.getCountByCids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = clueRemarkDao.deleteByCids(ids);

        if (count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = clueDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {
        //取uList
        List<User> uList = userDao.getUserList();
        //取c
        Clue c = clueDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("c",c);

        //返回map集合
        return map;
    }

    @Override
    public boolean update(Clue c) {
        boolean flag = true;

        int count = clueDao.update(c);

        if (count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue c = clueDao.detail(id);

        return c;
    }

    @Override
    public List<ClueRemark> getRemarkListByCid(String clueId) {

        List<ClueRemark> cList = clueRemarkDao.getRemarkListByCid(clueId);

        return cList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = clueRemarkDao.deleteRemark(id);

        if (count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ClueRemark cl) {

        boolean flag = true;

        int count = clueRemarkDao.saveRemark(cl);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public boolean updateRemark(ClueRemark cl) {

        boolean flag = true;

        int count = clueRemarkDao.updateRemark(cl);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public boolean unbund(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count!=1){

            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for (String aid:aids){

            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if (count!=1){
                flag = false;
            }
        }

        return flag;
    }
}
