package com.oneape.octopus.controller;

import com.oneape.octopus.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datasource")
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;


}
