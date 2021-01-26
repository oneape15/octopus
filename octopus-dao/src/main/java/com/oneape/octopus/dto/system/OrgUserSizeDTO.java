package com.oneape.octopus.dto.system;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 15:31.
 * Modify:
 */
@Data
public class OrgUserSizeDTO implements Serializable {
    private Long    orgId;
    private Integer size;
}
