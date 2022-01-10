package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Tran;

import java.util.List;

public interface TranDao {

    int save(Tran t);

    List<Tran> getTransactionListByCustomerId(String customerId);

    int delete(String[] ids);
}
