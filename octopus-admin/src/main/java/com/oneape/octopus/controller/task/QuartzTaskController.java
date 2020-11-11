package com.oneape.octopus.controller.task;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-11 19:51.
 * Modify:
 */
@Slf4j
@RequestMapping("/task")
@RestController
@Api(value = "/task", tags = "Task opt api")
public class QuartzTaskController {


}
