package com.examples.esbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/6
 */
@Controller
public class IndexController {

    @GetMapping
    public String index() {
        return "list";
    }
}
