package com.examples.esbook.domain.common;

import lombok.Data;

import java.util.List;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Data
public class Page<T> {

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalCount;

    private List<T> pageList;

    public Page(int pageNumber, int pageSize, long totalCount, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.pageList = list;
    }
}
