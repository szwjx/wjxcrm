package com.wjx.crm.workbench.service;

import com.wjx.crm.workbench.domain.Tran;

import java.util.List;

public interface TranService {
    boolean save(Tran t, String customerName);

    List<Tran> getTransactionListByCustomerId(String customerId);


    boolean deleteTransaction(String[] ids);
}
