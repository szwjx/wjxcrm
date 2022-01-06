package com.wjx.crm.workbench.service;


import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Customer;

import java.util.Map;

public interface CustomerService {

    boolean save(Customer c);

    PaginationVO<Customer> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Customer c);

    Customer detail(String id);
}
