package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.SqlLogDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SqlLogForm extends BaseForm implements Serializable {

    private Long sqlLogId;
    // 依赖的数据源Id
    private Long dsId;
    // 所在报表Id
    private Long reportId;
    // 原sqlId
    private Long reportSqlId;
    // 运行耗时
    private Integer elapsedTime;
    // 是否运行成功
    private Integer complete;
    // sql内容
    private String rawSql;
    // 错误信息
    private String errInfo;
    // 是否缓存中返回的数据
    private Integer cached;

    public SqlLogDO toDO() {
        SqlLogDO ldo = new SqlLogDO();
        BeanUtils.copyProperties(this, ldo);
        ldo.setId(sqlLogId);
        return ldo;
    }
}