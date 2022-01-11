package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<Tran> getTransactionListByCustomerId(String customerId);

    int delete(String[] ids);

    List<Tran> getTransactionListByContactsId(String customerId);

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranByCondition(Map<String, Object> map);

    Tran getTranById(String id);

    boolean update(Tran t);

    Tran getAidAndCidByTid(String id);

    Tran detail(String id);
}
