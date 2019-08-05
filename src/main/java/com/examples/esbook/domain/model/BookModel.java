package com.examples.esbook.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Data
public class BookModel implements Serializable {
    private static final long serialVersionUID = 8485760387999168962L;

    /**
     * 图书ID
     */
    private Integer id;

    /**
     * 图书名称
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 图书分类
     */
    private Integer category;

    /**
     * 图书价格
     */
    private Double price;

    /**
     * 上架理由
     */
    private String sellReason;

    /**
     * 上架时间
     */
    private String sellTime;

    /**
     * 状态（1：可售，0：不可售）
     */
    private Integer status;

}
