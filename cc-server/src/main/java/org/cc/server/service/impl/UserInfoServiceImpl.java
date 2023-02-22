package org.cc.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cc.server.mapper.UserInfoMapper;
import org.cc.server.pojo.UserInfo;
import org.cc.server.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Override
    public UserInfo queryUserInfoById(Long id) {
        return getById(id);
    }
}
