package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {

    int save(ContactsRemark contactsRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<ContactsRemark> getRemarkListByCid(String contactsId);

    int saveRemark(ContactsRemark cr);

    int updateRemark(ContactsRemark cr);

    int deleteRemark(String id);
}
