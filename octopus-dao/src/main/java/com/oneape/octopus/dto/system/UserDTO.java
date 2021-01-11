package com.oneape.octopus.dto.system;

import com.oneape.octopus.domain.system.RoleDO;
import com.oneape.octopus.domain.system.UserDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-06-05 10:53.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends UserDO implements Serializable {
    // The department name of user.
    private String                     deptName;
    private Integer                    notifyCount;
    private Integer                    unreadCount;
    // The own Role;
    private List<RoleDO>               roles;
    // Resource operation permissions
    private Map<String, List<Integer>> permissions;
}
