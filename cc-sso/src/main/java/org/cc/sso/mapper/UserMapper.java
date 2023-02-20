package org.cc.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cc.sso.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
