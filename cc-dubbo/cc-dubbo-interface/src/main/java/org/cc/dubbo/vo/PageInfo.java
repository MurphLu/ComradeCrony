package org.cc.dubbo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cc.dubbo.pojo.RecommendUser;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = -2105385689859184204L;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 一页显示的大小
     */
    private Integer pageSize;

    /**
     * 数据列表
     */
    private List<T> records = Collections.emptyList();
}
