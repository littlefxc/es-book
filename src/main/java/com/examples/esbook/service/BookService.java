package com.examples.esbook.service;

import com.examples.esbook.domain.common.Page;
import com.examples.esbook.domain.model.BookModel;
import com.examples.esbook.domain.vo.BookRequestVo;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
public interface BookService {

    Page<BookModel> list(BookRequestVo bookRequestVo);

    BookModel detail(Integer id);

    void save(BookModel bookModel);

    void update(BookModel bookModel);

    void delete(Integer id);
}
