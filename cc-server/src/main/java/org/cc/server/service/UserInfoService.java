package org.cc.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cc.server.pojo.UserInfo;
import org.springframework.stereotype.Service;


public interface UserInfoService extends IService<UserInfo> {
    UserInfo queryUserInfoById(Long id);
}
