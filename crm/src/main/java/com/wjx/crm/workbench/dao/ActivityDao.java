package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNoClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);

    List<Activity> getActivityListByNameAndNoContactsId(Map<String, String> map);

    List<Activity> getActivityListByContactsId(String contactsId);
}
