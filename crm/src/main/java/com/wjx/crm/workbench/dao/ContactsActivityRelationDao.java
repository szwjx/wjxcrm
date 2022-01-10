package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.ContactsActivityRelation;

public interface ContactsActivityRelationDao {

    int save(ContactsActivityRelation contactsActivityRelation);

    int bund(ContactsActivityRelation car);

    int unbund(String id);
}
