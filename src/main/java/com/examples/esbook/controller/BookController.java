package com.examples.esbook.controller;

import com.alibaba.fastjson.JSON;
import com.examples.esbook.domain.common.Page;
import com.examples.esbook.domain.common.Result;
import com.examples.esbook.domain.model.BookModel;
import com.examples.esbook.domain.vo.BookRequestVo;
import com.examples.esbook.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Slf4j
@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 列表分页查询
     */
    @GetMapping("/list")
    public Result list(BookRequestVo bookRequestVO) {
        Page<BookModel> page = bookService.list(bookRequestVO);
        if (null == page) {
            return Result.error();
        }
        return Result.ok(page);
    }

    /**
     * 查看文档
     */
    @GetMapping("/detail")
    public Result detail(Integer id) {
        if (null == id) {
            return Result.error("ID不能为空");
        }
        BookModel book = bookService.detail(id);
        return Result.ok(book);
    }

    /**
     * 添加文档
     */
    @PostMapping("/add")
    public Result add(@RequestBody BookModel bookModel) {
        bookService.save(bookModel);
        log.info("插入文档成功！请求参数: {}", JSON.toJSONString(bookModel));
        return Result.ok();
    }

    /**
     * 修改文档
     */
    @PostMapping("/update")
    public Result update(@RequestBody BookModel bookModel) {
        Integer id = bookModel.getId();
        if (null == id) {
            return Result.error("ID不能为空");
        }
        BookModel book = bookService.detail(id);
        if (null == book) {
            return Result.error("记录不存在");
        }
        bookService.update(bookModel);
        log.info("更新文档成功！请求参数: {}", JSON.toJSONString(bookModel));
        return Result.ok();
    }

    /**
     * 删除文档
     */
    @GetMapping("/delete")
    public Result delete(Integer id) {
        if (null == id) {
            return Result.error("ID不能为空");
        }
        bookService.delete(id);
        return Result.ok();
    }
}
