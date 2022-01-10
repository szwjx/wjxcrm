package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int save(Contacts con);

    int getTotalByCondition(Map<String, Object> map);

    List<Contacts> getContactsByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Contacts getById(String id);

    int update(Contacts c);

    List<Contacts> getContactsListByName(String cname);

    List<Contacts> getContactsListByCustomerId(String customerId);

    Contacts detail(String id);

//    Contacts getByid(String customerId);
}
