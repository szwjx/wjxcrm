package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.ContactsRemark;

public interface ContactsRemarkDao {

    int save(ContactsRemark contactsRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);
}
