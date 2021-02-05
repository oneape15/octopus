package com.oneape.octopus.admin.controller;

import com.oneape.octopus.admin.model.vo.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HomeController {

    @GetMapping(value = {"", "index", "index.htm", "index.html"})
    public ApiResult<String> index() {
        return new ApiResult<>("hello world");
    }
}

