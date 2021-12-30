package com.wjx.crm.settings.service.impl;

import com.wjx.crm.settings.dao.DicTypeDao;
import com.wjx.crm.settings.dao.DicValueDao;
import com.wjx.crm.settings.service.DicService;
import com.wjx.crm.utils.SqlSessionUtil;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
}
