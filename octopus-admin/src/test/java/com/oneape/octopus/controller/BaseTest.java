package com.oneape.octopus.controller;

import com.oneape.octopus.model.VO.ApiResult;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.annotation.Resource;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseTest {

    @Resource
    protected MockMvc mvc;

    protected String baseUrl;

    protected void response(ApiResult result) {
        Assertions.assertThat(result).as("非空判断").isNotNull();
        Assertions.assertThat(result.getCode()).as("响应码判断").isEqualTo(200);
        Assertions.assertThat(result.getData()).as("非空判断").isNotNull();
    }

    protected ResultMatcher result() {
        return new ApiResultMatcher();
    }


}
