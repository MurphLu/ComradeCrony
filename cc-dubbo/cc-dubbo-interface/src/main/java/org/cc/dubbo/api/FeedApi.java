package org.cc.dubbo.api;

import org.cc.dubbo.pojo.Publish;
import org.cc.dubbo.vo.PageInfo;

public interface FeedApi {
    boolean savePublish(Publish publish);

    PageInfo<Publish> queryPublishList(Long userId, Integer page, Integer pageSize);
}
