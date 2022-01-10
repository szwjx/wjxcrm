package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.utils.DateTimeUtil;
import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.utils.UUIDUtil;
import com.wjx.crm.workbench.dao.CustomerDao;
import com.wjx.crm.workbench.dao.TranDao;
import com.wjx.crm.workbench.dao.TranHistoryDao;
import com.wjx.crm.workbench.dao.TranRemarkDao;
import com.wjx.crm.workbench.domain.Customer;
import com.wjx.crm.workbench.domain.Tran;
import com.wjx.crm.workbench.domain.TranHistory;
import com.wjx.crm.workbench.service.TranService;

import java.util.List;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        /*
          交易添加业务：

                在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId

                先处理客户相关的需求

                （1）判断customerName，根据客户名称在客户表进行精确查询
                       如果有这个客户，则取出这个客户的id，封装到t对象中
                       如果没有这个客户，则再客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

                （2）经过以上操作后，t对象中的信息就全了，需要执行添加交易的操作

                （3）添加交易完毕后，需要创建一条交易历史
         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if (cus==null){

            //创建一个客户
            cus = new Customer();

            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            //其他信息根据用户需求可加可不加
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setDescription(t.getDescription());
            cus.setOwner(t.getOwner());

            //添加客户
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag=false;
            }
        }

        //通过以上处理一定会有一个客户，客户的Id就有了
        //将客户id封装到t中

        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if (count2!=1){
            flag=false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count3 = tranHistoryDao.save(th);
        if (count3!=1){
            flag=false;
        }


        return flag;
    }

    @Override
    public List<Tran> getTransactionListByCustomerId(String customerId) {

        List<Tran> tList = tranDao.getTransactionListByCustomerId(customerId);

        return tList;
    }

    @Override
    public boolean deleteTransaction(String[] ids) {

        /*
               删除交易前先查询交易相关的备注，将其删除，再查询交易相关的交易历史删除

          */
        boolean flag = true;

        //查询需要删除的交易备注数量
        int count1 = tranRemarkDao.getCountByTranId(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = tranRemarkDao.deleteByTranId(ids);

        if (count1!=count2){
            flag = false;
        }


        //删除交易
        int count3 = tranDao.delete(ids);

        if (count3!=1){
            flag = false;
        }

        return flag;
    }


}
