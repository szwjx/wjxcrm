package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.settings.dao.UserDao;
import com.wjx.crm.settings.domain.User;
import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.dao.*;
import com.wjx.crm.workbench.domain.*;
import com.wjx.crm.workbench.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerServiceImpl implements CustomerService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);


    @Override
    public boolean save(Customer c) {

        boolean flag = true;

        int count = customerDao.save(c);

        if (count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Customer> pageList(Map<String, Object> map) {
        //取得total
        int total = customerDao.getTotalByCondition(map);
        //取得dataList
        List<Customer> dataList = customerDao.getClueByCondition(map);
        //创建一个vo对象将total和dataList封装到VO
        PaginationVO<Customer> vo = new PaginationVO<Customer>();
        vo.setTotal(total);
        vo.setDataList(dataList);


        //返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = customerRemarkDao.getCountByCids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = customerRemarkDao.deleteByCids(ids);

        if (count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = customerDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndCustomer(String id) {
        //取uList
        List<User> uList = userDao.getUserList();
        //取c
        Customer c = customerDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("c",c);

        //返回map集合
        return map;
    }

    @Override
    public boolean update(Customer c) {

        boolean flag = true;

        int count = customerDao.update(c);

        if (count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Customer detail(String id) {

        Customer c = customerDao.detail(id);

        return c;
    }

    @Override
    public List<CustomerRemark> getRemarkListByCid(String customerId) {

        List<CustomerRemark> cList = customerRemarkDao.getRemarkListByCid(customerId);

        return cList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = customerRemarkDao.deleteRemark(id);

        if (count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(CustomerRemark cusr) {

        boolean flag = true;

        int count = customerRemarkDao.saveRemark(cusr);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public boolean updateRemark(CustomerRemark cusr) {

        boolean flag = true;

        int count = customerRemarkDao.updateRemark(cusr);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public List<String> getCustomerName(String name) {

        List<String > sList = customerDao.getCustomerName(name);

        return sList;
    }


}
