package com.wjx.crm.workbench.service;

import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;
import com.wjx.crm.workbench.domain.ClueRemark;
import com.wjx.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ClueService {

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean save(Clue c);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue c);

    Clue detail(String id);

    List<ClueRemark> getRemarkListByCid(String clueId);

    boolean deleteRemark(String id);

    boolean saveRemark(ClueRemark cl);

    boolean updateRemark(ClueRemark cl);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
