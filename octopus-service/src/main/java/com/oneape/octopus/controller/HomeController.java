package com.oneape.octopus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HomeController {

    @RequestMapping(value = {"", "index", "index.htm", "index.html"})
    public String index() {
        return "ok";
    }
}
