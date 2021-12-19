package com.wjx.crm.settings.service.impl;

import com.wjx.crm.settings.dao.UserDao;
import com.wjx.crm.settings.service.UserService;
import com.wjx.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
}

