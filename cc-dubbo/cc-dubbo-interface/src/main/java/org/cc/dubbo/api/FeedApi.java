package org.cc.dubbo.api;

import org.cc.dubbo.enums.CommentType;
import org.cc.dubbo.pojo.Publish;
import org.cc.dubbo.vo.PageInfo;

public interface FeedApi {
    /**
     * 保存动态
     * @param publish 动态信息
     * */
    boolean savePublish(Publish publish);

    /**
     * 查询动态列表
     * @param userId 用户ID
     * @param pageSize 每页条数
     * @param page 第几页
     * */
    PageInfo<Publish> queryPublishList(Long userId, Integer page, Integer pageSize);

    /**
     * 保存文字评论
     * @param userId 用户id
     * @param publishId 动态id
     * @param commentContent 评论内容
     * */
    boolean saveComment(Long userId, String publishId, String commentContent);

    /**
     * 删除文字评论
     * @param userId 用户id
     * @param commentId 评论id
     * */
    boolean removeComment(Long userId, Long commentId);

    /**
     * 保存点赞评论
     * @param userId 用户id
     * @param publishId 动态id
     * */
    boolean saveLikeComment(Long userId, String publishId);

    /**
     * 保存收藏
     * @param userId 用户id
     * @param publishId 动态id
     * */
    boolean saveLoveComment(Long userId, String publishId);

    /**
     * 删除点赞收藏
     * @param userId 用户id
     * @param publishId 动态id
     * @param commentType 评论类型
     * */
    boolean removeNonTextComment(Long userId, String publishId, CommentType commentType);

    /**
     * 获取点赞收藏数
     * @param publishId 动态id
     * @param type 评论类型
     * */
    Long queryCommentCount(String publishId, CommentType type);
}
