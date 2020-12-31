package com.oneape.octopus.dto.serve;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 15:31.
 * Modify:
 */
@Data
public class ServeGroupSizeDTO implements Serializable {
    private Long    groupId;
    private Integer size;
}
