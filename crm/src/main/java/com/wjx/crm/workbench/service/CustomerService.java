package com.wjx.crm.workbench.service;


import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Customer;
import com.wjx.crm.workbench.domain.CustomerRemark;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    boolean save(Customer c);

    PaginationVO<Customer> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndCustomer(String id);

    boolean update(Customer c);

    Customer detail(String id);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    boolean deleteRemark(String id);

    boolean saveRemark(CustomerRemark cusr);

    boolean updateRemark(CustomerRemark cusr);

    List<String> getCustomerName(String name);
}
