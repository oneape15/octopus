package com.oneape.octopus.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseForm implements Serializable {
    /**
     * 一页显示条数
     */
    protected Integer pageSize = 10;
    /**
     * 页号从1开始
     */
    protected Integer currentPage = 1;
}
