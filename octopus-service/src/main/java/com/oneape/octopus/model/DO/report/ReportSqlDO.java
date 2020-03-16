package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportSqlDO extends BaseDO {
    /**
     * 依赖数据源ID
     */
    @Column(name = "ds_id")
    private Long dsId;
    /**
     * 是否需要缓存 0 - 不需要； 1 - 需要
     */
    private Integer cached;
    /**
     * 缓存时间（秒）
     */
    @Column(name = "cached_time")
    private Integer cachedTime;
    /**
     * 查询超时时间(秒)
     */
    private Integer timeout;
    /**
     * 是否分页
     */
    private Integer paging;
    /**
     * SQL内容
     */
    @Column(name = "text")
    private String text;
    /**
     * 是否需要运行日志
     */
    @Column(name = "need_detail_log")
    private Integer needDetailLog;
    /**
     * 描述信息
     */
    private String comment;
}
