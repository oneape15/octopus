package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.report.SqlLogDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class SqlLogVO implements Serializable {
    private Long    id;
    // 数据源Id
    private Long    dsId;
    // 所在报表Id
    private String  reportCode;
    // 运行耗时
    private Integer elapsedTime;
    // 是否运行成功
    private Integer complete;
    // sql内容
    private String  rawSql;
    // 错误信息
    private String  errInfo;
    // 是否缓存中返回的数据
    private Integer cached;

    public SqlLogDO toDO() {
        SqlLogDO ldo = new SqlLogDO();
        BeanUtils.copyProperties(this, ldo);
        return ldo;
    }

    public static SqlLogVO ofDO(SqlLogDO ldo) {
        if (ldo == null) return null;

        SqlLogVO vo = new SqlLogVO();
        BeanUtils.copyProperties(ldo, vo);
        return vo;
    }
}
