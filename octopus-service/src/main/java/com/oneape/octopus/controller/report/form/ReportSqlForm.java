package com.oneape.octopus.controller.report.form;

import com.oneape.octopus.controller.BaseForm;
import com.oneape.octopus.model.DO.report.ReportSqlDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReportSqlForm extends BaseForm implements Serializable {
    // 报表主键Id
    @NotNull(message = "报表主键为空", groups = {SaveCheck.class})
    private Long reportId;
    // sql主键Id
    @NotNull(message = "sql主键为空", groups = {IdCheck.class})
    private Long sqlId;
    // 依赖的数据源Id
    @NotNull(message = "数据源Id为空", groups = {SaveCheck.class})
    private Long dsId;
    // 是否需要缓存; 0 - 不需要; 1 - 需要
    @NotNull(message = "是否缓存标志为空", groups = {SaveCheck.class})
    private Integer cached;
    // 缓存时间(秒)
    private Integer cachedTime;
    // 超时时间(秒)
    private Integer timeout;
    // sql内容
    @NotBlank(message = "SQL内容为空", groups = {SaveCheck.class})
    private String text;
    // 是否采用分页模式; 0 - 否; 1 - 是
    private Integer paging;
    // 是否需要详细运行日志, 0 - 不需要(只记录运行耗时、运行错误信息), 1 - 记录运行sql及所有默认信息
    @NotNull(message = "记录详细日志标志为空", groups = {SaveCheck.class})
    private Integer needDetailLog;
    // 描述信息
    private String comment;

    public interface IdCheck {
    }

    public interface SaveCheck {
    }

    public ReportSqlDO toDO() {
        ReportSqlDO rs = new ReportSqlDO();
        BeanUtils.copyProperties(this, rs);
        rs.setId(sqlId);
        return rs;
    }
}
