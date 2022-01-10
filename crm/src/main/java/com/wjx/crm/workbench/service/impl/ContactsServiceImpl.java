package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.settings.dao.UserDao;
import com.wjx.crm.settings.domain.User;
import com.wjx.crm.utils.DateTimeUtil;
import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.utils.UUIDUtil;
import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.dao.ContactsDao;
import com.wjx.crm.workbench.dao.ContactsRemarkDao;
import com.wjx.crm.workbench.dao.CustomerDao;
import com.wjx.crm.workbench.dao.CustomerRemarkDao;
import com.wjx.crm.workbench.domain.Contacts;
import com.wjx.crm.workbench.domain.ContactsRemark;
import com.wjx.crm.workbench.domain.Customer;
import com.wjx.crm.workbench.service.ContactsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsServiceImpl implements ContactsService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);


    @Override
    public boolean save(Contacts c,String customerName) {

        /*
            联系人添加业务：

                在做添加之前，参数c里面就少了一项信息，就是客户的主键，customerId

                先处理客户相关的需求

                （1）判断customerName，根据客户名称在客户表进行精确查询
                       如果有这个客户，则取出这个客户的id，封装到c对象中
                       如果没有这个客户，则再客户表新建一条客户信息，然后将新建的客户的id取出，封装到c对象中
         */
        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null，需要创建客户
        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(c.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(c.getContactSummary());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setOwner(c.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了，客户的id就有了
        //将客户id封装到t对象中
        c.setCustomerId(cus.getId());

        //添加联系人
        int count2 = contactsDao.save(c);
        if (count2!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Contacts> pageList(Map<String, Object> map) {

        //取得total
        int total = contactsDao.getTotalByCondition(map);
        //取得dataList
        List<Contacts> dataList = contactsDao.getContactsByCondition(map);

        //创建一个vo对象将total和dataList封装到VO
        PaginationVO<Contacts> vo = new PaginationVO<Contacts>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = contactsRemarkDao.getCountByCids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = contactsRemarkDao.deleteByCids(ids);

        if (count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = contactsDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndContacts(String id) {

        //取uList
        List<User> uList = userDao.getUserList();
        //取c
        Contacts c = contactsDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList",uList);
        map.put("c",c);

        //返回map集合
        return map;
    }

    @Override
    public boolean update(Contacts c,String customerName) {

        boolean flag = true;

        Customer cus=customerDao.getCustomerByName(customerName);//这个方法再convert中使用过。不用重新再写

        if(cus==null){
            cus=new Customer();
            //下面的set完全是可以不写太多，只写一些简单的即可。这些都视用户的需求而定。但是我们这里是尽可能的多填充一些信息

            cus.setId(UUIDUtil.getUUID());
            cus.setContactSummary(c.getContactSummary());
            cus.setCreateBy(c.getCreateBy());
            cus.setCreateTime(c.getCreateTime());//这里的时间就算是使用t的，前后跳转也不过是几毫秒。不会影响太大
            cus.setDescription(c.getDescription());
            cus.setOwner(c.getOwner());
            cus.setName(customerName);
            cus.setNextContactTime(c.getNextContactTime());


            int count1=customerDao.save(cus);//这的方法仍旧是使用convert中的

            if(count1!=1){
                flag=false;
            }
        }
        //到这一步说明customer一定是存在的。而且根据实际情况。世界上的公司名字也只能有一个，不会有重复的情况，所以这里不考虑customer即公司的
        //名字出现重复一致。
        c.setCustomerId(cus.getId());

        int count2 = contactsDao.update(c);

        if (count2!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public List<Contacts> getContactsListByName(String cname) {

        List<Contacts> cList = contactsDao.getContactsListByName(cname);

        return cList;
    }

    @Override
    public List<Contacts> getContactsListByCustomerId(String customerId) {

        List<Contacts> cList = contactsDao.getContactsListByCustomerId(customerId);

        return cList;
    }

    @Override
    public boolean deleteContacts(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = contactsRemarkDao.getCountByCids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = contactsRemarkDao.deleteByCids(ids);

        if (count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = contactsDao.delete(ids);
        if (count3!=ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Contacts detail(String id) {

        Contacts c = contactsDao.detail(id);

        return c;
    }

    @Override
    public List<ContactsRemark> getRemarkListByCid(String contactsId) {

        List<ContactsRemark> cList = contactsRemarkDao.getRemarkListByCid(contactsId);

        return cList;
    }

    @Override
    public boolean saveRemark(ContactsRemark cr) {

        boolean flag = true;

        int count = contactsRemarkDao.saveRemark(cr);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public boolean updateRemark(ContactsRemark cr) {


        boolean flag = true;

        int count = contactsRemarkDao.updateRemark(cr);

        if (count!=1){

            flag=false;

        }

        return flag;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = contactsRemarkDao.deleteRemark(id);

        if (count!=1){
            flag = false;
        }

        return flag;
    }


}
