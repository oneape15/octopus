package com.oneape.octopus.model.VO;

import com.oneape.octopus.model.DO.DatasourceDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class DatasourceVO {
    private Long id;
    /**
     * 数据库别名
     */
    private String nickname;
    /**
     * 数据源地址
     */
    private String url;
    /**
     * 驱动class名称
     */
    private String driverClass;
    /**
     * 数据源用户名
     */
    private String username;
    /**
     * 数据源登录密码
     */
    private String password;

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
