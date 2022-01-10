package com.wjx.crm.workbench.dao;



public interface TranRemarkDao {


    int deleteByTranId(String[] ids);

    int getCountByTranId(String[] ids);
}
