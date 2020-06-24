package com.oneape.octopus.model.DTO.system;

import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.DO.system.UserDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 10:53.
 * Modify:
 */
@Data
public class UserDTO extends UserDO implements Serializable {

    // Token when the user logs in successfully
    private String                     token;
    // The own Role;
    private List<RoleDO>               roles;
    // Resource operation permissions
    private Map<String, List<Integer>> permissions;
}