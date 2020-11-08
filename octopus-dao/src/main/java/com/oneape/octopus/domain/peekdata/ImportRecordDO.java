package com.oneape.octopus.domain.peekdata;

import com.oneape.octopus.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ImportRecordDO extends BaseDO {
    /**
     * 数据源Id
     */
    private Long datasourceId;
    /**
     * 具体表名
     */
    private String tableName;
    /**
     * 导入文件名
     */
    private String fileName;
    /**
     * 是否覆盖 0 - 不覆盖,追加; 1 - 覆盖
     */
    private Integer cover;
    /**
     * 分区名称
     */
    private String partitionName;
    /**
     * 导入状态,0:导入中,1导入成功,2导入失败
     */
    private Integer status;
}
