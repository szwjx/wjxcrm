package com.wjx.crm.settings.dao;

import com.wjx.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
