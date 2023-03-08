package org.cc.dubbo.enums;

import lombok.Getter;

@Getter
public enum CommentType {
    COMMENT(1, "评论"),
    LIKE(2, "点赞"),
    FAVORITE(3, "收藏");

    private int value;
    private String desc;

    CommentType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static CommentType withValue(Integer value) {
        switch (value) {
            case 1:
                return CommentType.COMMENT;
            case 2:
                return CommentType.LIKE;
            case 3:
                return CommentType.FAVORITE;
            default:
                return null;
        }
    }
}
