package org.cc.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cc.sso.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
