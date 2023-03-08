package org.cc.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.cc.server.pojo.User;
import org.cc.server.pojo.UserInfo;
import org.cc.server.service.UserInfoService;
import org.cc.server.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @GetMapping()
    public UserInfo getUserInfo() {
        User user = UserThreadLocal.get();
        return userInfoService.getById(user.getId());
    }
}
