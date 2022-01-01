package com.wjx.crm.workbench.service;

import com.wjx.crm.vo.PaginationVO;
import com.wjx.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean save(Clue c);

   // boolean delete(String[] ids);
}
