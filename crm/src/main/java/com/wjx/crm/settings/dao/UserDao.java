package com.wjx.crm.settings.dao;

import com.wjx.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
}
