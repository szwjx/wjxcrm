package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.CustomerRemark;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);
}
