package com.oneape.octopus.model.DO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportSqlLogDO extends BaseDO {
    /**
     * 依赖的数据源Id
     */
    @Column(name = "ds_id")
    private Long dsId;
    /**
     * 所在报表Id
     */
    @Column(name = "report_id")
    private Long reportId;
    /**
     * 原sqlId
     */
    @Column(name = "report_sql_id")
    private Long reportSqlId;
    /**
     * 运行耗时
     */
    @Column(name = "elapsed_time")
    private Integer elapsedTime;
    /**
     * 是否运行成功
     */
    private Integer complete;
    /**
     * sql内容
     */
    @Column(name = "raw_sql")
    private String rawSql;
    /**
     * 错误信息
     */
    @Column(name = "err_info")
    private String errInfo;
    /**
     * 是否缓存中返回的数据
     */
    private Integer cached;
}
