package com.oneape.octopus.datasource.data;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-16 09:32.
 * Modify:
 */
@Data
public class DetailInfo implements Serializable {
    // query total sql
    private String countSql;
    // query detail sql
    private String detailSql;
    // error info message
    private String errMsg;
    // run time
    private Long   runTime;
    // Export file path
    private String exportFile;
}
