package org.cc.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.cc.dubbo.util.CONSTS;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 评论表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CONSTS.MONGODB_FEED_COMMENT)
public class Comment implements java.io.Serializable{

    private static final long serialVersionUID = -291788258125767614L;

    private ObjectId id;

    private ObjectId publishId; //发布id
    private Integer commentType; //评论类型，1-评论，2-点赞，3-收藏
    private String content; //评论内容
    private Long userId; //评论人
    private Long publishUserId; //发布人的用户id

    private Boolean isParent = false; //是否为父节点，默认是否
    private ObjectId parentId; // 父节点id

    private Long created; //发表时间
}