package com.wjx.crm.workbench.service;


import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsService {


    boolean save(Contacts c,String customerName);

    PaginationVO<Contacts> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts c,String customerName);

    List<Contacts> getContactsListByName(String cname);
}
