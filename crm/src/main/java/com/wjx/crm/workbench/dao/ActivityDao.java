package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);
}
