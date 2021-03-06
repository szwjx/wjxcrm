package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Activity;
import com.wjx.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    List<Clue> getClueByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int save(Clue c);

    int delete(String[] ids);

    Clue getById(String id);

    int update(Clue c);

    Clue detail(String id);

    int delete1(String clueId);
}
