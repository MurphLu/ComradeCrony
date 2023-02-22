package org.cc.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.cc.server.pojo.UserInfo;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
