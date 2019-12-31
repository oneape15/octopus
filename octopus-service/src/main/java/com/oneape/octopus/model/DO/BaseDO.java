package com.oneape.octopus.model.DO;

import com.oneape.octopus.model.enums.Archive;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 归档状态 0 - 正常； 1 - 已删除；
     */
    private Archive archive = Archive.NORMAL;
    /**
     * 创建人
     */
    private Long creator;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 修改人
     */
    private Long modifier;
    /**
     * 修改时间
     */
    private Long modified;
}
