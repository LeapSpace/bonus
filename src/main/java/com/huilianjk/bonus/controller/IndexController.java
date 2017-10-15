package com.huilianjk.bonus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by space on 2017/10/15.
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
        return "hello world";
    }
}
