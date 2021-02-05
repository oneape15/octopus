package com.oneape.octopus.admin.model.vo;

import com.oneape.octopus.dto.serve.ServeSqlDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ReportSqlVO implements Serializable {
    private Long id;
    // 依赖数据源ID
    private Long dsId;
    // 是否需要缓存 0 - 不需要； 1 - 需要
    private Integer cached;
    // 缓存时间（秒）
    private Integer cachedTime;
    // 查询超时时间(秒)
    private Integer timeout;
    // 是否分页
    private Integer paging;
    // SQL内容
    private String text;
    // 是否需要运行日志
    private Integer needDetailLog;
    // 描述信息
    private String comment;

    public ServeSqlDTO toDO() {
        ServeSqlDTO rdo = new ServeSqlDTO();
        BeanUtils.copyProperties(this, rdo);
        return rdo;
    }

    public static ReportSqlVO ofDO(ServeSqlDTO rdo) {
        if (rdo == null) return null;

        ReportSqlVO vo = new ReportSqlVO();
        BeanUtils.copyProperties(rdo, vo);
        return vo;
    }
}
