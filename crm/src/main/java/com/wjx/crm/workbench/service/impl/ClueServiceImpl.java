package com.wjx.crm.workbench.service.impl;

import com.wjx.crm.utils.SqlSessionUtil;
import com.wjx.crm.workbench.dao.ClueDao;
import com.wjx.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
}
