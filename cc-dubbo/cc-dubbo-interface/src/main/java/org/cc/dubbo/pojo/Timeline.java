package org.cc.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.cc.dubbo.util.CONSTS;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 时间线表，用于存储发布（或推荐）的数据，每一个用户一张表进行存储
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = CONSTS.MONGODB_FEED_TIMELINE)
public class Timeline implements Serializable {
    private static final long serialVersionUID = 9096178416317502524L;
    private ObjectId id;

    private Long userId; // 好友id
    private ObjectId publishId; //发布id

    private Long date; //发布的时间

}