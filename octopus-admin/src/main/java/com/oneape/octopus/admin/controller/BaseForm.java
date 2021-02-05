package com.oneape.octopus.admin.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseForm implements Serializable {
    /**
     * Displays the number of entries per page.
     */
    protected Integer pageSize = 10;
    /**
     * The page number starts at 1.
     */
    protected Integer current  = 1;

    private String sorter;
    private String filter;
}
