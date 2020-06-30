package com.oneape.octopus.model.DTO.system;

import com.oneape.octopus.model.DO.system.ResourceDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 14:21.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceDTO extends ResourceDO implements Serializable {
    private Integer mask;
}
