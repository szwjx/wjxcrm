package com.wjx.crm.workbench.service;

import com.wjx.crm.workbench.domain.Tran;

public interface TranService {
    boolean save(Tran t, String customerName);
}
