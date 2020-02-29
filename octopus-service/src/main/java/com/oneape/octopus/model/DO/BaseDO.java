package com.oneape.octopus.model.DO;

import com.oneape.octopus.annotation.AutoUniqueId;
import com.oneape.octopus.annotation.Creator;
import com.oneape.octopus.annotation.Modifier;
import com.oneape.octopus.model.enums.Archive;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class BaseDO implements Serializable {
    /**
     * 主键
     */
    @AutoUniqueId
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * 归档状态 0 - 正常； 1 - 已删除；
     */
    @Column(name = "archive", nullable = false)
    private Integer archive;
    /**
     * 创建人
     */
    @Creator
    private Long creator;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 修改人
     */
    @Modifier
    private Long modifier;
    /**
     * 修改时间
     */
    private Long modified;
}
