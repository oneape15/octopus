package com.oneape.octopus.model.DO.report;

import com.oneape.octopus.model.DO.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SqlLogDO extends BaseDO {
    /**
     * 依赖的数据源Id
     */
    @Column(name = "ds_id")
    private Long    dsId;
    /**
     * 所在报表编码
     */
    @Column(name = "report_code")
    private Long    reportCode;
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
    private String  text;
    /**
     * 错误信息
     */
    @Column(name = "err_info")
    private String  errInfo;
    /**
     * 是否缓存中返回的数据
     */
    private Integer cached;
}
