package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    int deleteRemark(String id);

    int saveRemark(CustomerRemark cusr);

    int updateRemark(CustomerRemark cusr);
}
