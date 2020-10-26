package com.oneape.octopus.controller;

import com.oneape.octopus.model.VO.ApiResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HomeController {

    @RequestMapping(value = {"", "index", "index.htm", "index.html"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResult<String> index() {
        return new ApiResult<>("hello world");
    }
}
