package com.wjx.crm.settings.service;

import com.wjx.crm.exception.LoginException;
import com.wjx.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
