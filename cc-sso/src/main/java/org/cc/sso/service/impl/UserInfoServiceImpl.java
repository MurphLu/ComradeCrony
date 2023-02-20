package org.cc.sso.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cc.sso.mapper.UserInfoMapper;
import org.cc.sso.pojo.UserInfo;
import org.cc.sso.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
