package com.wjx.crm.workbench.dao;

import com.wjx.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<ClueRemark> getRemarkListByCid(String clueId);

    int deleteRemark(String id);

    int saveRemark(ClueRemark cr);

    int updateRemark(ClueRemark cr);
}
