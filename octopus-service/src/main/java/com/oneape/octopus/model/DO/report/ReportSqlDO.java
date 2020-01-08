package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportSqlDO extends BaseDO {
    /**
     * 依赖数据源ID
     */
    private Long dsId;
    /**
     * 是否需要缓存 0 - 不需要； 1 - 需要
     */
    private Integer cached = GlobalConstant.NO;
    /**
     * 缓存时间（秒）
     */
    private Integer cachedTime;
    /**
     * 查询超时时间(秒)
     */
    private Integer timeout;
    /**
     * 是否分页
     */
    private Integer paging = GlobalConstant.NO;
    /**
     * SQL内容
     */
    private String rawSql;
    /**
     * 是否需要运行日志
     */
    private Integer needDetailLog = GlobalConstant.YES;
    /**
     * 描述信息
     */
    private String comment;
}
