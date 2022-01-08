package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    int getTotalByCondition(Map<String, Object> map);

    List<Customer> getClueByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Customer getById(String id);

    int update(Customer c);

    Customer detail(String id);

    List<String> getCustomerName(String name);
}
