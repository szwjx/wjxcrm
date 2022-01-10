package com.wjx.crm.workbench.service;


import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.Contacts;
import com.wjx.crm.workbench.domain.ContactsRemark;

import java.util.List;
import java.util.Map;

public interface ContactsService {


    boolean save(Contacts c,String customerName);

    PaginationVO<Contacts> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts c,String customerName);

    List<Contacts> getContactsListByName(String cname);

    List<Contacts> getContactsListByCustomerId(String customerId);

    boolean deleteContacts(String[] ids);

    Contacts detail(String id);

    List<ContactsRemark> getRemarkListByCid(String contactsId);

    boolean saveRemark(ContactsRemark cr);

    boolean updateRemark(ContactsRemark cr);

    boolean deleteRemark(String id);

    boolean bund(String cid, String[] aids);

    boolean unbund(String id);
}
