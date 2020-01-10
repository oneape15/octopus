package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.report.DatasourceDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class DatasourceVO implements Serializable {
    private Long id;
    // 数据库名称
    private String name;
    // 数据源类型 MySQL, Oracle
    private String type;
    // 状态, 0 - 可用; 1 - 不可用
    private Integer status;
    // 数据源地址
    private String jdbcUrl;
    // jdbc驱动
    private String jdbcDriver;
    // 数据源用户名
    private String username;
    // 数据源登录密码
    private String password;
    // 连接池超时时间(ms)
    private Integer timeout;
    // 检测SQL
    private String testSql;
    // 描述
    private String comment;

    public static DatasourceVO ofDO(DatasourceDO udo) {
        if (udo == null) {
            return null;
        }
        DatasourceVO vo = new DatasourceVO();
        BeanUtils.copyProperties(udo, vo);
        return vo;
    }

    public DatasourceDO toDO() {
        DatasourceDO udo = new DatasourceDO();
        BeanUtils.copyProperties(this, udo);
        return udo;
    }
}
