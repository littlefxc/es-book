package com.examples.esbook.domain.vo;

import lombok.Data;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Data
public class BookRequestVo {

    private int pageNo;
    private int pageSize;

    private String name;

    private String author;

    private String status;

    private String sellTime;

    private String categories;

}
