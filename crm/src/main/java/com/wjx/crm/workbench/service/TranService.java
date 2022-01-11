package com.wjx.crm.workbench.service;

import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Activity;
import com.wjx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    List<Tran> getTransactionListByCustomerId(String customerId);


    boolean deleteTransaction(String[] ids);

    List<Tran> getTransactionListByContactsId(String contactsId);

    PaginationVO<Tran> pageList(Map<String, Object> map);

    Tran getTranById(String id);

    boolean update(Tran t);

    Tran getAidAndCidByTid(String id);

    Tran detail(String id);
}
