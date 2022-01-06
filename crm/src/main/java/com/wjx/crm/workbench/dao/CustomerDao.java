package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
