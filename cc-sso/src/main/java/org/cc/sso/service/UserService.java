package org.cc.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cc.sso.pojo.User;

public interface UserService extends IService<User> {

    String login(String mobile, String code);

    User queryUserByToken(String token);
}
